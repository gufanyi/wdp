package xap.lui.core.refrence;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import xap.lui.core.command.LuiCommand;
import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.common.LuiWebContext;
import xap.lui.core.common.LuiWebSession;
import xap.lui.core.comps.ButtonComp;
import xap.lui.core.comps.FormComp;
import xap.lui.core.comps.FormElement;
import xap.lui.core.comps.GridColumn;
import xap.lui.core.comps.GridComp;
import xap.lui.core.comps.ReferenceComp;
import xap.lui.core.comps.TextComp;
import xap.lui.core.constant.DatasetConstant;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.DatasetRelation;
import xap.lui.core.dataset.DatasetRelations;
import xap.lui.core.dataset.PaginationInfo;
import xap.lui.core.dataset.Parameter;
import xap.lui.core.dataset.Row;
import xap.lui.core.event.DatasetEvent;
import xap.lui.core.event.GridRowEvent;
import xap.lui.core.event.MouseEvent;
import xap.lui.core.event.PageEvent;
import xap.lui.core.event.ScriptEvent;
import xap.lui.core.event.TextEvent;
import xap.lui.core.event.TreeNodeEvent;
import xap.lui.core.model.AppSession;
import xap.lui.core.model.PagePartMeta;
import xap.lui.core.model.ViewPartContext;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.core.serializer.List2DatasetSerializer;
import xap.lui.core.util.ClassUtil;
import xap.lui.core.util.JsURLDecoder;
import xap.sys.bdrefinfo.d.BdRefInfoDO;
/**
 *  参照的ctr类 
 */
public class AppRefDftCtrl {
	
	private static final String RELATION_WHERE_SQL = "relationWhereSql";
	
	public static final String REFORG = "refcomp_org";
	
	public void onDataLoad(DatasetEvent e) {
		String widgetId = (String) LuiRuntimeContext.getWebContext().getPageWebSession().getAttribute("widgetId");
		String refNodeId = (String) LuiRuntimeContext.getWebContext().getPageWebSession().getAttribute("refNodeId");
		PagePartMeta parentPm = LuiRuntimeContext.getWebContext().getParentPageMeta();
		GenericRefNode rfnode = (GenericRefNode) parentPm.getWidget(widgetId).getViewModels().getRefNode(refNodeId);
		IRefModel refModel = RefSelfUtil.getRefModel(rfnode);
		Dataset ds = (Dataset) e.getSource();
		String filterValue = ds.getReqParameters().getParameterValue("filterValue");
		
		//自定义档案参照特殊处理  TODO
		BdRefInfoDO bdRefInfoDo = RefPubUtil.getRefinfoVO(rfnode.getRefcode());
		if(bdRefInfoDo!=null&&("bd_udidoc").equals(bdRefInfoDo.getPara2())){
			refModel.addWherePart("bd_udidoc.id_udidoclist = '"+bdRefInfoDo.getPara1()+"'");
		}
		if (filterValue != null) {
			filterValue = JsURLDecoder.decode(filterValue, "UTF-8");
		}
		if (ds.getId().equals(IRefConst.MASTER_DS_ID)) {
			ViewPartContext widgetCtx = AppSession.current().getViewContext();
			ViewPartMeta widget = widgetCtx.getView();
			List<List<Object>> v = null;
			PaginationInfo pInfo = ds.getPaginationInfo();
			String pk_org = null;
			processSelfWherePart(ds, rfnode, filterValue, refModel);
			// 根据参照关联关系产生的查询条件语句
			int refType = RefSelfUtil.getRefType(refModel);
			// 树表的参照加载表时不需要此参数
			if (refType != IRefConst.GRIDTREE) {
				Parameter parameter = ds.getReqParameter(RELATION_WHERE_SQL);
				if (null != parameter) {
					String relationWhereSql = parameter.getValue();
					if (relationWhereSql != null && !"".equals(relationWhereSql))
						refModel.addWherePart(relationWhereSql);
				}
			} else {
				processTreeSelWherePart(ds, rfnode, refModel);
			}
			//TODO  dataset模型不一致导致  Parameter  获取不到值
			if(rfnode.getFilterSql()!=null)
				refModel.addWherePart(rfnode.getFilterSql());
			if (filterValue == null || filterValue.equals("")) {
				DatasetRelations dsRels = widget.getViewModels().getDsrelations();
				if (dsRels != null) {
					String parentDsId = dsRels.getMasterDsByDetailDs(ds.getId());
					if (parentDsId != null) { // 为子DS
						Dataset parentDs = widget.getViewModels().getDataset(parentDsId);
						Row row = parentDs.getSelectedRow();
						if (row != null) { // 主DS有选中行
							DatasetRelation dr = dsRels.getDsRelation(parentDsId, ds.getId());
							String masterKey = dr.getMasterKeyField();
							String keyValue = (String) row.getValue(parentDs.nameToIndex(masterKey));
							if (keyValue == null)
								keyValue = row.getRowId();
							ds.clear();
							PaginationInfo pinfo = ds.getPaginationInfo();
							if (pinfo.getPageIndex() == -1)
								pinfo.setPageIndex(0);
						}
					}
				}
				RefResultSet rd = null;
				if (pInfo.getPageSize() != -1) {
					rd = refModel.getRefData(pInfo);
				} else {
					rd = refModel.getRefData();
				}
				if (rd != null) {
//					pInfo.setRecordsCount(rd.getTotalCount());
					v = rd.getData();
				}
				//new List2DatasetSerializer().serialize(ds.getCurrentKey(), pInfo, v, ds);
			} else {
				DatasetRelations dsRels = widget.getViewModels().getDsrelations();
				if (dsRels != null) {
					String parentDsId = dsRels.getMasterDsByDetailDs(ds.getId());
					if (parentDsId != null) { // 为子DS
						Dataset parentDs = widget.getViewModels().getDataset(parentDsId);
						Row row = parentDs.getSelectedRow();
						if (row != null) { // 主DS有选中行
							DatasetRelation dr = dsRels.getDsRelation(parentDsId, ds.getId());
							String masterKey = dr.getMasterKeyField();
							String keyValue = (String) row.getValue(parentDs.nameToIndex(masterKey));
							if (refModel instanceof IRefTreeGridModel) {
								IRefTreeGridModel treeRefModel = (IRefTreeGridModel) refModel;
								treeRefModel.setClassJoinValue(keyValue);
								refModel.setPk_org(pk_org);
							}
						}
					}
				}
				RefResultSet rd = refModel.filterRefValue(filterValue, pInfo);// (filterValue,
																								// pInfo.getPageIndex());
				if (rd != null) {
//					pInfo.setRecordsCount(rd.getTotalCount());
					v = rd.getData();
				}
			}
			new List2DatasetSerializer().serialize(pInfo, v, ds);
		} else if (ds.getId().equals("rightGridDs")) {
			Dataset wds = parentPm.getWidget(widgetId).getViewModels().getDataset(rfnode.getWriteDataset());
			Row row = wds.getFocusRow();
			if (row == null)
				row = wds.getSelectedRow();
			String writeFieldStr = rfnode.getWriteFields();
			String[] writeFields = writeFieldStr.split(",");
			String valuePK = (String) row.getValue(wds.nameToIndex(writeFields[0]));
			if (valuePK == null)
				return;
			String[] valuePKs = valuePK.split(",");
			String valueName = (String) row.getValue(wds.nameToIndex(writeFields[1]));
			String[] valueNames = valueName.split(",");
			for (int i = 0; i < valuePKs.length; i++) {
				String valPk = valuePKs[i];
				Row newRow = ds.getEmptyRow();
				ds.addRow(newRow);
				newRow.setValue(0, valPk);
				String valName = valueNames[i];
				newRow.setValue(1, valName);
			}
		} else {
			IRefTreeGridModel tgModel = (IRefTreeGridModel) refModel;
			// 根据参数设置VO条件
			String keys = ds.getReqParameters().getParameterValue(DatasetConstant.QUERY_PARAM_KEYS);
			if (keys != null && !keys.equals("")) {
				String values = ds.getReqParameters().getParameterValue(DatasetConstant.QUERY_PARAM_VALUES);
				String wherePart = (keys + " = '" + values + "'");
				tgModel.setClassWherePart(wherePart);
			}
			processSelfWherePart(ds, rfnode, filterValue, refModel);
			List<List<Object>> v = tgModel.getClassData().data;
			new List2DatasetSerializer().serialize(v, ds);
			// 设置选中行
			if (v != null && v.size() > 0)
				ds.setRowSelectIndex(0);
		}
	}
	/**
	 * 搜索全部
	 * 
	 * @param e
	 */
	public void searchAllData(MouseEvent e) {
		String widgetId = (String) LuiRuntimeContext.getWebContext().getPageWebSession().getAttribute("widgetId");
		String refNodeId = (String) LuiRuntimeContext.getWebContext().getPageWebSession().getAttribute("refNodeId");
		PagePartMeta parentPm = LuiRuntimeContext.getWebContext().getParentPageMeta();
		GenericRefNode rfnode = (GenericRefNode) parentPm.getWidget(widgetId).getViewModels().getRefNode(refNodeId);
		IRefModel refModel = RefSelfUtil.getRefModel(rfnode);
		ViewPartContext widgetCtx = AppSession.current().getViewContext();
		ViewPartMeta widget = widgetCtx.getView();
		TextComp locateText = (TextComp) widget.getViewComponents().getComponent("locatetext");
		Dataset ds = widget.getViewModels().getDataset(IRefConst.MASTER_DS_ID);
		ds.clear();
		String filterValue = locateText.getValue();
		// if(filterValue!=null){
		// filterValue = JsURLDecoder.decode(filterValue, "UTF-8")
		// ;
		// }
		//
		// if(ds.getId().equals(ILuiRefConst.MASTER_DS_ID)){
		// ViewContext widgetCtx =
		// AppLifeCycleContext.current().getViewContext();
		// LuiWidget widget = widgetCtx.getView();
		List<List<Object>> v = null;
		PaginationInfo pInfo = ds.getPaginationInfo();
		String pk_org = null;
		// if(rfnode instanceof NCRefNode){
		// boolean orgs = ((NCRefNode)rfnode).isOrgs();
		// if(orgs){
		// ReferenceComp reftext = (ReferenceComp)
		// widget.getViewComponents().getComponent("refcomp_org");
		// if(reftext != null){
		// pk_org = reftext.getValue();
		// //根据参数设置VO条件
		//
		// }
		// }
		// }
		// 设置组织参数
		if (null != pk_org && !"".equals(pk_org)) {
			refModel.setPk_org(pk_org);
		}
		if (refModel instanceof IRefTreeGridModel) {
			((IRefTreeGridModel) refModel).setClassJoinValue(IRefConst.QUERY);
		}
		processSelfWherePart(ds, rfnode, filterValue, refModel);
		// 根据参照关联关系产生的查询条件语句
		// 树表的参照加载表时不需要此参数
		processTreeSelWherePart(ds, rfnode, refModel);
		String pk_group = LuiRuntimeContext.getSessionBean().getPk_unit();
		refModel.setPk_group(pk_group);
		if (filterValue == null || filterValue.equals("")) {
			DatasetRelations dsRels = widget.getViewModels().getDsrelations();
			if (dsRels != null) {
				String parentDsId = dsRels.getMasterDsByDetailDs(ds.getId());
				if (parentDsId != null) { // 为子DS
					Dataset parentDs = widget.getViewModels().getDataset(parentDsId);
					Row row = parentDs.getSelectedRow();
					if (row != null) { // 主DS有选中行
						DatasetRelation dr = dsRels.getDsRelation(parentDsId, ds.getId());
						String masterKey = dr.getMasterKeyField();
						ds.clear();
						PaginationInfo pinfo = ds.getPaginationInfo();
						if (pinfo.getPageIndex() == -1)
							pinfo.setPageIndex(0);
					}
				}
			}
			RefResultSet rd = null;
			if (pInfo.getPageSize() != -1) {
				int pageIndex = pInfo.getPageIndex();
				rd = refModel.getRefData(pageIndex);
			} else {
				rd = refModel.getRefData(0);
			}
			if (rd != null) {
				pInfo.setRecordsCount(rd.getTotalCount());
				v = rd.getData();
			}
		} else {
			DatasetRelations dsRels = widget.getViewModels().getDsrelations();
			if (dsRels != null) {
				String parentDsId = dsRels.getMasterDsByDetailDs(ds.getId());
				if (parentDsId != null) { // 为子DS
					Dataset parentDs = widget.getViewModels().getDataset(parentDsId);
					Row row = parentDs.getSelectedRow();
					if (row != null) { // 主DS有选中行
						DatasetRelation dr = dsRels.getDsRelation(parentDsId, ds.getId());
						String masterKey = dr.getMasterKeyField();
						// String keyValue = (String)
						// row.getValue(parentDs.getFieldSet().nameToIndex(masterKey));
						// ds.setCurrentKey(keyValue);
						if (refModel instanceof IRefTreeGridModel) {
							IRefTreeGridModel treeRefModel = (IRefTreeGridModel) refModel;
							// treeRefModel.setClassJoinValue(keyValue);
							// 设置组织参数
							// if (null != pk_org && !"".equals(pk_org)) {
							// refModel.setPk_org(pk_org);
							// }
						}
					}
				}
			}
			RefResultSet rd = refModel.filterRefValue(filterValue, pInfo);
			if (rd != null) {
				//pInfo.setRecordsCount(rd.getTotalCount());
				v = rd.getData();
			}
		}
		new List2DatasetSerializer().serialize(pInfo, v, ds);
	}
	/**
	 * 数据加载时,处理参照运行时条件,默认增加组织条件
	 */
	protected void processSelfWherePart(Dataset ds, GenericRefNode rfnode, String filterValue, IRefModel refModel) {
		ViewPartMeta widget = AppSession.current().getViewContext().getView();
		ReferenceComp reference = (ReferenceComp) widget.getViewComponents().getComponent(REFORG);
		if (reference != null) {
			String pk_org = reference.getValue();
			if (pk_org != null && !"".equals(pk_org))
				refModel.setPk_org(pk_org);
		}
	}
	private void loadTreeGridDatas(Row curRow) {
		ViewPartContext widgetCtx = AppSession.current().getWindowContext().getCurrentViewContext();
		ViewPartMeta widget = widgetCtx.getView();
		Dataset masterDs = widget.getViewModels().getDataset("masterDs_tree");
		Row masterSelecteRow = curRow;
		DatasetRelations dsRels = widget.getViewModels().getDsrelations();
		if (dsRels != null) {
			DatasetRelation[] masterRels = dsRels.getDsRelations(masterDs.getId());
			for (int i = 0; i < masterRels.length; i++) {
				DatasetRelation dr = masterRels[i];
				Dataset detailDs = widget.getViewModels().getDataset(dr.getDetailDataset());
				String masterKey = dr.getMasterKeyField();
				// String detailFk = dr.getDetailForeignKey();
				if (masterSelecteRow != null) { // 有选中行，查询detailDs内容
					String keyValue = (String) masterSelecteRow.getValue(masterDs.nameToIndex(masterKey));
					if (keyValue == null)
						keyValue = masterSelecteRow.getRowId();
					PaginationInfo pinfo = detailDs.getPaginationInfo();
					if (pinfo == null || pinfo.getPageCount() == -1)
						pinfo.setPageIndex(0);
					String widgetId = (String) LuiRuntimeContext.getWebContext().getPageWebSession().getAttribute("widgetId");
					String refNodeId = (String) LuiRuntimeContext.getWebContext().getPageWebSession().getAttribute("refNodeId");
					PagePartMeta parentPm = LuiRuntimeContext.getWebContext().getParentPageMeta();
					GenericRefNode rfnode = (GenericRefNode) parentPm.getWidget(widgetId).getViewModels().getRefNode(refNodeId);
					IRefModel baseRefModel = RefSelfUtil.getRefModel(rfnode);
					String pk_org = null;
					// if(rfnode instanceof NCRefNode){
					// boolean orgs = ((NCRefNode)rfnode).isOrgs();
					// if(orgs){
					// ReferenceComp reftext = (ReferenceComp)
					// widget.getViewComponents().getComponent("refcomp_org");
					// if(reftext != null){
					// pk_org = reftext.getValue();
					// //根据参数设置VO条件
					//
					// }
					// }
					// }
					if (baseRefModel instanceof IRefTreeGridModel) {
						IRefTreeGridModel refModel = (IRefTreeGridModel) baseRefModel;
						// if(pk_org != null && !pk_org.equals("")){
						// refModel.setPk_org(pk_org);
						// }
						processTreeSelWherePart(masterDs, rfnode, refModel);
						// 如果是通过第一级树与masterDs建立的关系
						if (dr.getId().equals("master_slave_rel1")) {
							refModel.setClassJoinValue(keyValue);
							// 设置关联字段，为了有分页信息时处理，点”下一页“等处理条件
							RefResultSet rr = refModel.getRefData();
							List<List<Object>> v = rr.getData();
							pinfo.setRecordsCount(rr.getTotalCount());
							new List2DatasetSerializer().serialize(pinfo, v, detailDs);
						} else {
							refModel.setClassJoinValue(keyValue);
							RefResultSet rr = refModel.getRefData();
							List<List<Object>> v = rr.getData();
							pinfo.setRecordsCount(rr.getTotalCount());
							pinfo.setPageIndex(0);
							detailDs.clear();
							new List2DatasetSerializer().serialize(pinfo, v, detailDs);
						}
					}
				} else { // 没有选中行，detailDs置空
					new List2DatasetSerializer().serialize(null, new ArrayList<List<Object>>(), detailDs);
				}
			}
		}
	}
	public void onAfterRowSelect(DatasetEvent se) {
		Dataset masterDs = (Dataset) se.getSource();
		Row masterSelecteRow = masterDs.getSelectedRow();
		ViewPartContext widgetCtx = AppSession.current().getWindowContext().getCurrentViewContext();
		ViewPartMeta widget = widgetCtx.getView();
		DatasetRelations dsRels = widget.getViewModels().getDsrelations();
		if (dsRels != null) {
			DatasetRelation[] masterRels = dsRels.getDsRelations(masterDs.getId());
			for (int i = 0; i < masterRels.length; i++) {
				DatasetRelation dr = masterRels[i];
				Dataset detailDs = widget.getViewModels().getDataset(dr.getDetailDataset());
				String masterKey = dr.getMasterKeyField();
				if (masterSelecteRow != null) { // 有选中行，查询detailDs内容
					String keyValue = (String) masterSelecteRow.getValue(masterDs.nameToIndex(masterKey));
					if (keyValue == null)
						keyValue = masterSelecteRow.getRowId();
					PaginationInfo pinfo = detailDs.getPaginationInfo();
					if (pinfo == null || pinfo.getPageCount() == -1)
						pinfo.setPageIndex(0);
					String widgetId = (String) LuiRuntimeContext.getWebContext().getPageWebSession().getAttribute("widgetId");
					String refNodeId = (String) LuiRuntimeContext.getWebContext().getPageWebSession().getAttribute("refNodeId");
					PagePartMeta parentPm = LuiRuntimeContext.getWebContext().getParentPageMeta();
					GenericRefNode rfnode = (GenericRefNode) parentPm.getWidget(widgetId).getViewModels().getRefNode(refNodeId);
					IRefModel baseRefModel = RefSelfUtil.getRefModel(rfnode);
					String pk_org = null;
					ReferenceComp reftext = (ReferenceComp) widget.getViewComponents().getComponent("refcomp_org");
					if (reftext != null) {
						pk_org = reftext.getValue();
						// 根据参数设置VO条件
					}
					if (baseRefModel instanceof IRefTreeGridModel) {
						IRefTreeGridModel refModel = (IRefTreeGridModel) baseRefModel;
						if (pk_org != null && !pk_org.equals("")) {
							refModel.setPk_org(pk_org);
						}
						processTreeSelWherePart(masterDs, rfnode, refModel);
						// 如果是通过第一级树与masterDs建立的关系
						if (dr.getId().equals("master_slave_rel1")) {
							refModel.setClassJoinValue(keyValue);
							// 设置关联字段，为了有分页信息时处理，点”下一页“等处理条件
							// detailDs.setLastCondition(refModel.getDocJoinField()
							// + " = '" + keyValue + "'");
							RefResultSet rr = refModel.getRefData();
							List<List<Object>> v = rr.getData();
							pinfo.setRecordsCount(rr.getTotalCount());
							new List2DatasetSerializer().serialize(pinfo, v, detailDs);
						} else {
							refModel.setClassJoinValue(keyValue);
							// 设置关联字段，为了有分页信息时处理，点”下一页“等处理条件
							// detailDs.setLastCondition(refModel.getDocJoinField()
							// + " = '" + keyValue + "'");
							// int pageIndex = pinfo.getPageIndex();
							RefResultSet rr = refModel.getRefData();
							List<List<Object>> v = rr.getData();
							pinfo.setRecordsCount(rr.getTotalCount());
							pinfo.setPageIndex(0);
							detailDs.clear();
							new List2DatasetSerializer().serialize(pinfo, v, detailDs);
						}
					}
				} else { // 没有选中行，detailDs置空
					new List2DatasetSerializer().serialize(null, new ArrayList<List<Object>>(), detailDs);
				}
			}
		}
	}
	public void onRowDbClick(GridRowEvent e) {
		ViewPartMeta widget = AppSession.current().getViewContext().getView();
		Dataset ds = widget.getViewModels().getDataset(IRefConst.MASTER_DS_ID);
		String refNodeId = (String) LuiRuntimeContext.getWebContext().getPageWebSession().getAttribute("refNodeId");
		String widgetId = (String) LuiRuntimeContext.getWebContext().getPageWebSession().getAttribute("widgetId");
		PagePartMeta parentPm = LuiRuntimeContext.getWebContext().getParentPageMeta();
		GenericRefNode refnode = (GenericRefNode) parentPm.getWidget(widgetId).getViewModels().getRefNode(refNodeId);
		LuiCommand refOkCmd = null;
		if (refnode != null && StringUtils.isNotBlank(refnode.getDelegator())) {
			refOkCmd = (LuiCommand) ClassUtil.newInstance(refnode.getDelegator(), new Class[] {
				Dataset.class
			}, new Object[] {
				ds
			});
		} else
			refOkCmd = new AppRefDftOkCmd(ds);
		refOkCmd.execute();
	}
	/**
	 * 点确定的时候执行方法
	 * 
	 * @param e
	 */
	public void refOkDelegator(MouseEvent<ButtonComp> e) {
		ViewPartMeta widget = AppSession.current().getViewContext().getView();
		Dataset ds = widget.getViewModels().getDataset(IRefConst.MASTER_DS_ID);
		String refNodeId = (String) LuiRuntimeContext.getWebContext().getPageWebSession().getAttribute("refNodeId");
		String widgetId = (String) LuiRuntimeContext.getWebContext().getPageWebSession().getAttribute("widgetId");
		String parentPageId = (String) LuiRuntimeContext.getWebContext().getPageWebSession().getOriginalParameter("otherPageId");
		PagePartMeta parentPm = AppSession.current().getAppContext().getWindowContext(parentPageId).getPagePartMeta();
		GenericRefNode refnode = (GenericRefNode) parentPm.getWidget(widgetId).getViewModels().getRefNode(refNodeId);
		LuiCommand refOkCmd = null;
		if (refnode != null && StringUtils.isNotBlank(refnode.getDelegator())) {
			refOkCmd = (LuiCommand) ClassUtil.newInstance(refnode.getDelegator(), new Class[] {
				Dataset.class
			}, new Object[] {
				ds
			});
		} else
			refOkCmd = new AppRefDftOkCmd(ds);
		refOkCmd.execute();
	}
	public void onTreeNodedbclick(TreeNodeEvent e) {
		Dataset ds = AppSession.current().getViewContext().getView().getViewModels().getDataset(IRefConst.MASTER_DS_ID);
		String refNodeId = (String) LuiRuntimeContext.getWebContext().getPageWebSession().getAttribute("refNodeId");
		String widgetId = (String) LuiRuntimeContext.getWebContext().getPageWebSession().getAttribute("widgetId");
		PagePartMeta parentPm = LuiRuntimeContext.getWebContext().getParentPageMeta();
		GenericRefNode refnode = (GenericRefNode) parentPm.getWidget(widgetId).getViewModels().getRefNode(refNodeId);
		LuiCommand refOkCmd = null;
		if (refnode != null && StringUtils.isNotBlank(refnode.getDelegator())) {
			refOkCmd = (LuiCommand) ClassUtil.newInstance(refnode.getDelegator(), new Class[] {
				Dataset.class
			}, new Object[] {
				ds
			});
		} else
			refOkCmd = new AppRefDftOkCmd(ds);
		refOkCmd.execute();
	}
	public void orgValueChanged(TextEvent e) {
		ReferenceComp text = (ReferenceComp) e.getSource();
		String pk_org = text.getValue();
		String widgetId = (String) LuiRuntimeContext.getWebContext().getPageWebSession().getAttribute("widgetId");
		String refNodeId = (String) LuiRuntimeContext.getWebContext().getPageWebSession().getAttribute("refNodeId");
		PagePartMeta parentPm = LuiRuntimeContext.getWebContext().getParentPageMeta();
		GenericRefNode rfnode = (GenericRefNode) parentPm.getWidget(widgetId).getViewModels().getRefNode(refNodeId);
		IRefModel refModel = RefSelfUtil.getRefModel(rfnode);
		Dataset ds = null;
		if (refModel instanceof IRefTreeGridModel) {
			ds = AppSession.current().getViewContext().getView().getViewModels().getDataset("masterDs_tree");
			IRefTreeGridModel tgModel = (IRefTreeGridModel) refModel;
			// 根据参数设置VO条件
			if (pk_org != null && !pk_org.equals("")) {
				tgModel.setPk_org(pk_org);
				tgModel.setClassWherePart(" pk_org='" + pk_org + "'");
			}
			List<List<Object>> v = tgModel.getClassData().data;
			new List2DatasetSerializer().serialize(null, v, ds);
			// 默认选中第一个作为右侧查询条件 2012-09-07修改
			Row curRow = ds.getSelectedRow();
			if (curRow == null) {
				Row[] rows = ds.getCurrentPageData() == null ? null : ds.getCurrentPageData().getRows();
				if (rows != null && rows.length > 0) {
					curRow = rows[0];
				}
			}
			if (curRow != null)
				loadTreeGridDatas(curRow);
			else {
				curRow = ds.getEmptyRow();
				loadTreeGridDatas(curRow);
			}
		} else if (refModel instanceof IRefTreeModel) {
			ds = AppSession.current().getViewContext().getView().getViewModels().getDataset("masterDs");
			IRefTreeModel tgModel = (IRefTreeModel) refModel;
			if (pk_org != null && !pk_org.equals("")) {
				tgModel.setPk_org(pk_org);
			}
			RefResultSet rr = tgModel.getRefData();
			List<List<Object>> v = rr.getData();
			new List2DatasetSerializer().serialize(null, v, ds);
		} else {
			ds = AppSession.current().getViewContext().getView().getViewModels().getDataset("masterDs");
			IRefGridModel ncModel = (IRefGridModel) refModel;
			if (pk_org != null && !pk_org.equals("")) {
				ncModel.setPk_org(pk_org);
			}
			// RefResult rr = ncModel.getRefData(0);
			PaginationInfo pinfo = ds.getPaginationInfo();
			RefResultSet rd = null;
			List<List<Object>> v = null;
			if (pinfo.getPageSize() != -1) {
				int pageIndex = pinfo.getPageIndex();
				rd = ncModel.getRefData(pageIndex);
			} else {
				rd = ncModel.getRefData(0);
			}
			if (rd != null) {
				pinfo.setRecordsCount(rd.getTotalCount());
				v = rd.getData();
			}
			// pinfo.setRecordsCount(rr.getTotalCount());
			new List2DatasetSerializer().serialize(null, v, ds);
		}
	}
	public void filterValueChanged(TextEvent e) {
		ReferenceComp text = (ReferenceComp) e.getSource();
		String filterValue = text.getValue();
		String widgetId = (String) LuiRuntimeContext.getWebContext().getPageWebSession().getAttribute("widgetId");
		String refNodeId = (String) LuiRuntimeContext.getWebContext().getPageWebSession().getAttribute("refNodeId");
		PagePartMeta parentPm = LuiRuntimeContext.getWebContext().getParentPageMeta();
		GenericRefNode rfnode = (GenericRefNode) parentPm.getWidget(widgetId).getViewModels().getRefNode(refNodeId);
		IRefModel refModel = RefSelfUtil.getRefModel(rfnode);
		Dataset ds = null;
		if (refModel instanceof IRefTreeGridModel) {
			ds = AppSession.current().getViewContext().getView().getViewModels().getDataset("masterDs_tree");
			IRefTreeGridModel tgModel = (IRefTreeGridModel) refModel;
			// 根据参数设置VO条件
			// tgModel.setFilterValue(filterValue);
			// tgModel.filterValueChanged(filterValue);
			RefResultSet rr = tgModel.getRefData();
			List<List<Object>> v1 = rr.getData();
			List<List<Object>> v = tgModel.getClassData().data;
			new List2DatasetSerializer().serialize(null, v, ds);
		} else if (refModel instanceof IRefTreeModel) {
			ds = AppSession.current().getViewContext().getView().getViewModels().getDataset("masterDs");
			IRefTreeModel tgModel = (IRefTreeModel) refModel;
			// tgModel.filterValueChanged(filterValue);
			RefResultSet rr = tgModel.getRefData();
			List<List<Object>> v = rr.getData();
			new List2DatasetSerializer().serialize(null, v, ds);
		} else {
			ds = AppSession.current().getViewContext().getView().getViewModels().getDataset("masterDs");
			IRefGridModel ncModel = (IRefGridModel) refModel;
			// ncModel.filterValueChanged(filterValue);
			RefResultSet rr = ncModel.getRefData(0);
			PaginationInfo pinfo = ds.getPaginationInfo();
			RefResultSet rd = null;
			List<List<Object>> v = null;
			if (pinfo.getPageSize() != -1) {
				int pageIndex = pinfo.getPageIndex();
				rd = refModel.getRefData(pageIndex);
			} else {
				rd = refModel.getRefData(0);
			}
			if (rd != null) {
				pinfo.setRecordsCount(rd.getTotalCount());
				v = rd.getData();
			}
			pinfo.setRecordsCount(rr.getTotalCount());
			new List2DatasetSerializer().serialize(pinfo, v, ds);
		}
	}
	protected void processTreeSelWherePart(Dataset ds, GenericRefNode rfnode, IRefModel refModel) {}
	/**
	 * 参照初始化后加载参照的值
	 * 
	 * @param pageEvent
	 */
	public void afterPageInit(PageEvent pageEvent) {
		ViewPartMeta widget = AppSession.current().getViewContext().getView();
		ReferenceComp reference = (ReferenceComp) widget.getViewComponents().getComponent(REFORG);
		if (reference != null) {
			reference.setValue(LuiRuntimeContext.getSessionBean().getPk_unit());
			reference.setShowValue(LuiRuntimeContext.getSessionBean().getUnitName());
		}
	}
	public String[] matchRefPk(ScriptEvent scriptEvent) {
		LuiWebContext ctx = LuiRuntimeContext.getWebContext();
		// ctx.setPageUniqueId(pageUniqueId);
		String widgetId = (String) AppSession.current().getParameter("widgetId");
		if (widgetId == null || "".equals(widgetId))
			return null;
		String refNodeId = (String) AppSession.current().getParameter("refNodeId");
		String value = (String) AppSession.current().getParameter("matchValue");
		LuiWebSession ws = ctx.getPageWebSession();
		PagePartMeta pageMeta = (PagePartMeta) ws.getAttribute(LuiWebContext.PAGEMETA_KEY);
		IRefNode refNode = pageMeta.getWidget(widgetId).getViewModels().getRefNode(refNodeId);
		if (refNode instanceof GenericRefNode) {
			GenericRefNode rf = (GenericRefNode) refNode;
			// //如果输入的是""的话，将ds的值清空
			// if("".equals(value) || value == null)
			// clearRowValue(ds, rf);
			IRefModel rm = RefSelfUtil.getRefModel(rf);
			onBeforeMatchRefPk(rm);
			String rfStr = rf.getReadFields();
			String[] rfs = rfStr.split(",");
			String returnField = null;
			String pkField = "";
			String codeField = "";
			String nameField = "";
			if (rm != null) {
				pkField = rm.getPkFieldCode();
				codeField = rm.getRefCodeField();
				nameField = rm.getRefNameField();
			}
			int returnIndex = -1;
			int pkIndex = -1;
			if (null != nameField && nameField.indexOf(".") != -1) {
				String[] fieldCodes = rm.getShowFieldCode();
				for (int i = 0; i < rfs.length; i++) {
					for (int j = 0; j < fieldCodes.length; j++) {
						String fieldCode = fieldCodes[j];
						if (!fieldCode.equals(rfs[i]) && fieldCode.endsWith(rfs[i])) {
							rfs[i] = fieldCode.split("\\.")[0] + "." + rfs[i];
							break;
						}
					}
				}
			}
			for (int i = 0; i < rfs.length; i++) {
				if (!rfs[i].equals(pkField)) {
					if (pkField.endsWith(rfs[i]))
						rfs[i] = pkField;
					if (rfs[i].equals(codeField) || rfs[i].equals(nameField)) {
						returnField = rfs[i];
						returnIndex = i;
					}
				} else {
					pkIndex = i;
				}
			}
			String datasetId = (String) AppSession.current().getParameter("datasetId");
			Dataset ds = null;
			ReferenceComp ref = null;
			if (datasetId != null && !"".equals(datasetId))
				ds = pageMeta.getWidget(widgetId).getViewModels().getDataset(datasetId);
			else {
				String refId = (String) AppSession.current().getParameter("referenceTextId");
				ref = (ReferenceComp) pageMeta.getWidget(widgetId).getViewComponents().getComponent(refId);
			}
			if (returnIndex == -1) {
				if (((GenericRefNode) refNode).isQuickInput()) {// 允许手动输入,设置PK和Name为value值.
					if (ds != null) {
						// 设置可以手工输入的情况下，需要设置同步的数据
						dealAllowInputMatch(value, refNode, ds);
					} else {
						ref.setValue(value);
						ref.setShowValue(value);
					}
				} else {
					setRowValue(ds, ref, (GenericRefNode) refNode, value, null, 0);
				}
				return null;
			}
			/*
			 * if(pkIndex == -1 || returnIndex == -1){ throw new
			 * LuiRuntimeException("参照必须配置读入主键及编码或者名称字段"); }
			 */
			String wpk = null;
			String wfield = null;
			String wfStr = rf.getWriteFields();
			if (wfStr != null && !wfStr.equals("")) {
				String[] wfs = wfStr.split(",");
				if (pkIndex != -1)
					wpk = wfs[pkIndex];
				if (returnIndex != -1)
					wfield = wfs[returnIndex];
			}
			String[][] rts = null;
			if (value != null && !value.equals("")) {
				// 处理自定义条件
				processSelfWherePart(ds, (GenericRefNode) refNode, "", rm);
				processTreeSelWherePart(ds, (GenericRefNode) refNode, rm);
				// rts = rm.matchRefPk(value, returnField, rfs,
				// ((GenericRefNode) refNode).isMultiSel());
			}
			if (rts == null || rts.length == 0) {
				if (((GenericRefNode) refNode).isQuickInput()) {// 允许手动输入,设置PK和Name为value值.
					if (ds != null) {
						// 设置可以手工输入的情况下，需要设置同步的数据
						dealAllowInputMatch(value, refNode, ds);
					} else {
						ref.setValue(value);
						ref.setShowValue(value);
					}
				} else {
					setRowValue(ds, ref, (GenericRefNode) refNode, value, null, 0);
				}
				return null;
			}
			// 获取读入字段的值
			String[] values = value.split(",");
			int rsize = rfs.length;
			for (int j = 0; j < rts.length; j++) {
				value = values[j];
				String[] result = new String[4 + rsize];
				String[] rt = rts[j];
				result[0] = rt[0];
				result[1] = rt[1];
				result[2] = wpk;
				result[3] = wfield;
				for (int i = 0, n = rsize; i < n; i++) {
					String rv = rt[2 + i];
					result[i + 4] = rv;
				}
				if (((GenericRefNode) refNode).isQuickInput()) { // 如果允许手工输入额外值
					// if (datasetId != null) { // 绑定了ds的情况
					setRowValue(ds, ref, (GenericRefNode) refNode, value, result, j);
					// }
					// else{
					// }
				} else { // 如果不允许手工输入额外值
					// if (datasetId != null && !"".equals(datasetId)) { //
					// 绑定了ds的情况
					setRowValue(ds, ref, (GenericRefNode) refNode, value, result, j);
					// }
					// else {
					// }
				}
			}
		}
		return null;
	}
	/**
	 * 处理可以手工输入的参照匹配
	 * 
	 * @param value
	 * @param refNode
	 * @param ds
	 */
	protected void dealAllowInputMatch(String value, IRefNode refNode, Dataset ds) {
		String[] writeFields = ((GenericRefNode) refNode).getWriteFields().split(",");
		Row row = ds.getSelectedRow();
		if (row != null) {
			int n = writeFields.length;
			if (n - 2 >= 0) {
				for (int i = n - 2; i < n; i++) {
					int index = ds.nameToIndex(writeFields[i]);
					row.setValue(index, value);
				}
			}
		}
	}
	/**
	 * 根据输入值精确匹配PK之前调用，可设置参照限定条件
	 * 
	 * @param rm
	 */
	protected void onBeforeMatchRefPk(IRefModel rm) {}
	/**
	 * 打开参照前获取参数信息
	 * TODO    zhaoweiwu        
	 * @param scriptEvent
	 */
//	public void getExtendsParam(ScriptEvent scriptEvent) {
////		 String widgetId = (String)
////		 AppLifeCycleContext.current().getParameter("widgetId");
////		 WebContext ctx = LuiRuntimeEnvironment.getWebContext();
////		 WebSession ws = ctx.getWebSession();
////		 PageMeta pageMeta = (PageMeta)
////		 ws.getAttribute(WebContext.PAGEMETA_KEY);
////		 ReferenceComp referenceText = null;
////		 String formId = (String)
////		 AppLifeCycleContext.current().getParameter("refFormId");
////		 String formElementId = (String)
////		 AppLifeCycleContext.current().getParameter("refFormeleID");
////		 String gridId = (String)
////		 AppLifeCycleContext.current().getParameter("refGridId");
////		 String gridHeaderId = (String)
////		 AppLifeCycleContext.current().getParameter("refGridHeaderId");
////		 FormElement formEle = null;
////		 GridColumn column = null;
////		 if(formId != null && !"".equals(formId) && formElementId != null &&
////		 !"".equals(formElementId)){
////		 FormComp formComp = (FormComp)
////		 pageMeta.getWidget(widgetId).getViewComponents().getComponent(formId);
////		 formEle = formComp.getElementById(formElementId);
////		 }
////		 else if(gridId != null && !"".equals(gridId) && gridHeaderId != null
////		 && !"".equals(gridHeaderId)){
////		 GridComp gridComp = (GridComp)
////		 pageMeta.getWidget(widgetId).getViewComponents().getComponent(gridId);
////		 column = (GridColumn)gridComp.getColumnById(gridHeaderId);
////		 }
////		 else{
////		 String referenceTextId = (String)
////		 AppLifeCycleContext.current().getParameter("referenceTextId");
////		 if(referenceTextId != null && !"".equals(referenceTextId))
////		 referenceText = (ReferenceComp)
////		 pageMeta.getWidget(widgetId).getViewComponents().getComponent(referenceTextId);
////		 }
//	}
	/**
	 * 匹配信息
	 * 
	 * @param scriptEvent
	 */
	public void matchSearch(ScriptEvent scriptEvent) {
		String widgetId = (String) AppSession.current().getParameter("widgetId");
		String refNodeId = (String) AppSession.current().getParameter("refNodeId");
		String value = (String) AppSession.current().getParameter("matchValue");
		LuiWebContext ctx = LuiRuntimeContext.getWebContext();
		LuiWebSession ws = ctx.getPageWebSession();
		PagePartMeta pageMeta = (PagePartMeta) ws.getAttribute(LuiWebContext.PAGEMETA_KEY);
		ReferenceComp referenceText = null;
		String formId = (String) AppSession.current().getParameter("refFormId");
		String formElementId = (String) AppSession.current().getParameter("refFormeleID");
		String gridId = (String) AppSession.current().getParameter("refGridId");
		String gridHeaderId = (String) AppSession.current().getParameter("refGridHeaderId");
		FormElement formEle = null;
		GridColumn column = null;
		if (formId != null && !"".equals(formId) && formElementId != null && !"".equals(formElementId)) {
			FormComp formComp = (FormComp) pageMeta.getWidget(widgetId).getViewComponents().getComponent(formId);
			formEle = formComp.getElementById(formElementId);
		} else if (gridId != null && !"".equals(gridId) && gridHeaderId != null && !"".equals(gridHeaderId)) {
			GridComp gridComp = (GridComp) pageMeta.getWidget(widgetId).getViewComponents().getComponent(gridId);
			column = (GridColumn) gridComp.getColumnById(gridHeaderId);
		} else {
			String referenceTextId = (String) AppSession.current().getParameter("referenceTextId");
			if (referenceTextId != null && !"".equals(referenceTextId))
				referenceText = (ReferenceComp) pageMeta.getWidget(widgetId).getViewComponents().getComponent(referenceTextId);
		}
		IRefNode refNode = pageMeta.getWidget(widgetId).getViewModels().getRefNode(refNodeId);
		if (refNode instanceof GenericRefNode) {
			GenericRefNode rf = (GenericRefNode) refNode;
			IRefModel ncmodel = RefSelfUtil.getRefModel(rf);
			onBeforeMatchSearch(ncmodel);
			String datasetId = (String) AppSession.current().getParameter("datasetId");
			Dataset ds = null;
			if (datasetId != null && !"".equals(datasetId))
				ds = pageMeta.getWidget(widgetId).getViewModels().getDataset(datasetId);
			// 处理自定义条件
			processSelfWherePart(ds, (GenericRefNode) refNode, "", ncmodel);
			processTreeSelWherePart(ds, (GenericRefNode) refNode, ncmodel);
			AbstractRefListItem[] objs = new RefDftListFilter().filter(value, ncmodel);
			if (objs == null || objs.length == 0) {
				if (formEle != null)
					formEle.setMatchValues("");
				else if (column != null)
					column.setMatchValues("");
			}
			String[] strs = new String[objs.length];
			for (int i = 0; i < objs.length; i++) {
				AbstractRefListItem item = objs[i];
				String refCodeField = ncmodel.getRefCodeField();
				if (refCodeField != null && !"".equals(refCodeField))
					strs[i] = item.getCode();
				if (item.getName() != null && !"".equals(item.getName())) {
					if (strs[i] == null) {
						strs[i] = item.getName();
					} else
						strs[i] += "," + item.getName();
				}
			}
			StringBuffer matchsValues = new StringBuffer();
			for (int i = 0; i < strs.length; i++) {
				if (i != strs.length - 1) {
					matchsValues.append(strs[i]);
					matchsValues.append(";");
				} else
					matchsValues.append(strs[i]);
			}
			if (formEle != null)
				formEle.setMatchValues(matchsValues.toString());
			else if (column != null)
				column.setMatchValues(matchsValues.toString());
			else if (referenceText != null)
				referenceText.setMatchValues(matchsValues.toString());
		}
	}
	/**
	 * 根据输入值模糊匹配之前调用，可设置参照限定条件
	 * 
	 * @param rm
	 */
	protected void onBeforeMatchSearch(IRefModel rm) {}
	// /**
	// *
	// * @param ds
	// * @param refNode
	// */
	// private void clearRowValue(Dataset ds, RefNode refNode){
	// if(refNode.getWriteFields() == null)
	// return;
	// String[] writeFields = refNode.getWriteFields().split(",");
	// if(ds == null)
	// return;
	// Row row = ds.getSelectedRow();
	// if(row == null)
	// return;
	//
	// // 输入值为空的情况，清空关联字段的值
	// for (int i = 0, n = writeFields.length; i < n; i++) {
	// int index = ds.nameToIndex(writeFields[i]);
	// row.setValue(index, null);
	// }
	// }
	private void setRowValue(Dataset ds, ReferenceComp ref, GenericRefNode refNode, String value, String[] result, int currentIndex) {
		if (value == null || value == "" || result == null) { // 输入值为空的情况，清空关联字段的值
			if (ds != null) {
				String[] writeFields = refNode.getWriteFields().split(",");
				Row row = ds.getSelectedRow();
				if (row == null)
					return;
				for (int i = 0, n = writeFields.length; i < n; i++) {
					int index = ds.nameToIndex(writeFields[i]);
					row.setValue(index, null);
				}
			} else {
				// ref.setValue(null);
				// ref.setShowValue(null);
			}
		} else {
			// 设置关联字段的值
			if (ds != null) {
				String[] writeFields = refNode.getWriteFields().split(",");
				Row row = ds.getSelectedRow();
				if (row == null)
					return;
				for (int i = 0, n = writeFields.length; i < n; i++) {
					int index = ds.nameToIndex(writeFields[i]);
					if (result == null || result.length <= 4) {
						row.setValue(index, null);
					} else {
						String fieldValue = (result[i + 4] == null || result[i + 4].equals("")) ? null : result[i + 4];
						if (currentIndex == 0) {
							row.setValue(index, fieldValue);
						} else {
							String oldValue = (String) row.getValue(index);
							if (oldValue != null && !oldValue.equals("")) {
								if (fieldValue != null) {
									row.setValue(index, oldValue + "," + fieldValue);
								}
							} else
								row.setValue(index, fieldValue);
						}
					}
				}
			} else {
				if (currentIndex == 0) {
					ref.setValue("".equals(result[0]) ? null : result[0]);
					ref.setShowValue(result[1]);
				} else {
					String oldValue = ref.getValue();
					String oldShowValue = ref.getShowValue();
					if (oldValue != null && !oldValue.equals("")) {
						ref.setValue(oldValue + "," + result[0]);
						ref.setShowValue(oldShowValue + "," + result[1]);
					} else {
						ref.setValue("".equals(result[0]) ? null : result[0]);
						ref.setShowValue(result[1]);
					}
				}
				// if ((result[0] == "" || result[0] == null) && value != null
				// && value != "")
				// ref.setValue(value);
				// else
				// ref.setValue(result[0]);
				// if ((result[1] == "" || result[1] == null) && value != null
				// && value != "")
				// ref.setShowValue(value);
				// else
				// ref.setShowValue(result[1]);
			}
		}
	}
}
