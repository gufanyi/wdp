package xap.lui.psn.refence;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.common.LuiWebSession;
import xap.lui.core.comps.FormComp;
import xap.lui.core.comps.FormElement;
import xap.lui.core.comps.GridColumn;
import xap.lui.core.comps.GridComp;
import xap.lui.core.comps.TextComp;
import xap.lui.core.comps.WebComp;
import xap.lui.core.constant.StringDataTypeConst;
import xap.lui.core.dao.CRUDHelper;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.Field;
import xap.lui.core.dataset.FieldRelation;
import xap.lui.core.dataset.MatchField;
import xap.lui.core.dataset.PageData;
import xap.lui.core.dataset.RefMdDataset;
import xap.lui.core.dataset.Row;
import xap.lui.core.dataset.WhereField;
import xap.lui.core.event.DatasetEvent;
import xap.lui.core.event.MouseEvent;
import xap.lui.core.event.TextEvent;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.logger.LuiLogger;
import xap.lui.core.mock.MockTreeGridController;
import xap.lui.core.model.AppSession;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.model.LuiPlugoutCmd;
import xap.lui.core.model.PagePartMeta;
import xap.lui.core.model.RequestLifeCycleContext;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.core.refrence.BaseRefNode;
import xap.lui.core.refrence.GenericRefNode;
import xap.lui.core.refrence.IRefModel;
import xap.lui.core.refrence.IRefNode;
import xap.lui.core.refrence.RefSelfUtil;
import xap.lui.core.refrence.SelfDefRefNode;
import xap.lui.core.services.IPaEditorService;
import xap.lui.core.services.PaEditorServiceImpl;
import xap.lui.psn.command.PaHelper;
import xap.lui.psn.pamgr.PaModelOperateController;
import xap.sys.bdrefinfo.d.BdRefInfoDO;
import xap.sys.jdbc.handler.MapListHandler;
/**
 * 引用参照控制类
 * 
 * @author licza
 * 
 */
public class MockRefDataRefController extends MockTreeGridController {
	private static final String PID = "pid";
	private static final String ID = "id";
	private static final String MASTER_DS = "masterDs";
	private static final String SOURCE_VIEW = "sourceView";
	private static final String OWNER = "owner";
	private static final String SOURCE_WIN = "sourceWin";
	private static final String DS_MIDDLE = "ds_middle";
	private static final String DATASET = "Dataset";
	private static final String TYPE = "type";
	private static final String MAIN = "main";
	private static final String REF_OK_PLUGOUT = "refOkPlugout";
	private static final String WRITE_FIELDS = "writeFields";
	private static final String CURRENT_WIDGET = "CURRENT_WIDGET";
	private static final String YES = "Y";
	private static final String METADATA = "METADATA";
	private static final String QRY_SQL_HEAD = "SELECT   refmodelname, co.namespace AS nbs, cc.name as cmdname FROM md_class cc ,md_component co WHERE ";
	@Override
	public void dataLoad(DatasetEvent e) {
		Dataset ds = e.getSource();
		ds.clear();
		createRow(ds, CURRENT_WIDGET, "", "已定义的参照");
		createRow(ds, METADATA, "", "NC参照");
		createNcModuleRow(ds, METADATA);
		createRow(ds, "BASEDOC", "", "基本档案");
		// createRow(ds, "SELFDEFDOC", "", "自定义档案");
	}

	private void createNcModuleRow(Dataset ds, String metadata2) {
		try {
			List<Map> modules = (List<Map>) CRUDHelper.getCRUDService().query("SELECT * FROM md_module  WHERE isactive = 'Y' ", new MapListHandler());
			if (modules != null && !modules.isEmpty()) {
				for (int i = 0; i < modules.size(); i++) {
					Map module = modules.get(i);
					String displayName = (String) module.get("displayname");
					String productCode = (String) module.get("resmodule");
					String key = (String) module.get("resid");
					Row row = ds.getEmptyRow();
					row.setValue(0, module.get("id"));
					row.setValue(1, metadata2);
					row.setValue(2, displayName);
					String pid = (String) module.get("parentmoduleid");
					if (pid != null && !pid.isEmpty())
						row.setValue(ds.nameToIndex(PID), pid);
					ds.addRow(row);
				}
			}
		} catch (Exception e) {
			LuiLogger.error(e.getMessage(), e);
		}
		// List<MdModuleVO> modules = getAllModules();
		// if(modules != null && modules.size() > 0){
		//			
		// }
	}

	@Override
	public void cancelButtonClick(MouseEvent e) {
		super.cancelButtonClick(e);
	}
	public Row createRow(Dataset ds, String id, String pid, String title) {
		return createRow(ds, id, pid, title, null);
	}
	public Row createRow(Dataset ds, String id, String pid, String title, String ext) {
		Row row = ds.getEmptyRow();
		row.setValue(0, id);
		row.setValue(1, pid);
		row.setValue(2, title);
		row.setValue(3, ext);
		ds.addRow(row);
		return row;
	}
	@Override
	public void okButtonClick(MouseEvent e) {
		LuiWebSession webSession = LuiRuntimeContext.getWebContext().getPageWebSession();
		String owner = (String) webSession.getOriginalParameter(OWNER);
		String writeDs = (String) webSession.getOriginalParameter("writeDs");
		String subeleid = (String) webSession.getOriginalParameter("subeleid");
		String eleid = webSession.getOriginalParameter("eleid");
		String dsFeild = subeleid;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put(TYPE, DATASET);
		paramMap.put(ID, DS_MIDDLE);
		String sourceWin = PaHelper.getCurrentEditWindowId();
		String sourceView = webSession.getOriginalParameter(SOURCE_VIEW);
		IPaEditorService es =  new PaEditorServiceImpl();
		String sessionId = LuiRuntimeContext.getWebContext().getRequest().getSession().getId();
		PagePartMeta pm = es.getOriPageMeta(sourceWin, sessionId);
		ViewPartMeta wd = pm.getWidget(sourceView);
		FormElement formElement = null;
		GridColumn gridColumn = null;
		WebComp wc = wd.getViewComponents().getComponent(eleid);
		if (wc instanceof FormComp) {
			formElement = ((FormComp) wc).getElementById(subeleid);
			dsFeild = formElement.getField();
		} else if (wc instanceof GridComp) {
			gridColumn = ((GridComp) wc).getElementById(subeleid);
			dsFeild = gridColumn.getField();
		}
		IRefNode[] cds = wd.getViewModels().getRefNodes();
		ViewPartMeta widget = AppSession.current().getViewContext().getView();
		Dataset currentDs = widget.getViewModels().getDataset(MASTER_DS);;
		Row currRow = null;
		currRow = currentDs.getSelectedRow();
		if (currRow == null) {
			throw new LuiRuntimeException("请选择数据集！");
		}
		String value = currRow.getString(currentDs.nameToIndex(ID));
		String name = currRow.getString(currentDs.nameToIndex("label"));
		String pid = currRow.getString(currentDs.nameToIndex(PID));
		String code = currRow.getString(3);
		if (currRow != null) {
			// 'defdoc', 'uap'
			Dataset writeDataset = null;
			if (!CURRENT_WIDGET.equals(pid)) {
				String writeFieldsStr = null;
				if (writeDs != null) {
					// 写入FeildRel和创建显示的Feild
					writeDataset = wd.getViewModels().getDataset(writeDs);
					if (!dsFeild.endsWith("_refname")) {
						Field nameField = writeDataset.getField(dsFeild + "_refname");
						if (nameField == null) {
							nameField = new Field(dsFeild + "_refname");
							nameField.setText(dsFeild + "_refname");
							nameField.setDataType(StringDataTypeConst.STRING);
							LifeCyclePhase oriPhase = RequestLifeCycleContext.get().getPhase();
							RequestLifeCycleContext.get().setPhase(LifeCyclePhase.nullstatus);
							writeDataset.addField(nameField);
							RequestLifeCycleContext.get().setPhase(oriPhase);
						}
						if (wc instanceof FormComp) {
							formElement.setField(nameField.getId());
						}
						if (wc instanceof GridComp) {
							gridColumn.setField(nameField.getId());
						}
						writeFieldsStr = dsFeild + "_refname," + dsFeild;
						// refnode.setWriteFields(subeleid + "_refname," +
						// subeleid);
					} else {
						writeFieldsStr = dsFeild + "," + dsFeild.replace("_refname", "");
						// refnode.setWriteFields(subeleid + "," +
						// subeleid.replace("_refname", ""));
					}
					// refnode.setWriteDs(writeDs);
				}
				boolean isEx = false;
				if (cds != null && cds.length > 0) {
					for (IRefNode cd : cds) {
						if (code.equals(cd.getId())) {
							isEx = true;
						}
					}
				}
				if (!isEx) {
					if (METADATA.equals(pid)) {
						GenericRefNode refnode = new GenericRefNode();
						refnode.setRefcode(name);
						refnode.setTitle(name);
						refnode.setFilterNames(false);
						refnode.setId(code);
						if (writeDs != null) {
							FieldRelation fr = new FieldRelation();
							RefMdDataset rmd = new RefMdDataset();
							rmd.setId("$refds_" + refnode.getId().trim() + "refDataset");
							rmd.setCaption(subeleid);
							String objMeta = currRow.getString(4);
							if (objMeta == null || objMeta.contains("null") || (refnode.getId() != null && refnode.getId().contains("-"))) {
								throw new LuiRuntimeException("所选参照定义有错误，请重新选择！");
							}
							fr.setRefDataset(rmd.getId());
							RequestLifeCycleContext.get().setPhase(LifeCyclePhase.nullstatus);
//							rmd.setObjMeta(objMeta);
							wd.getViewModels().addDataset(rmd);
							RequestLifeCycleContext.get().setPhase(LifeCyclePhase.ajax);
							fr.setId("$FieldRel_" + refnode.getId());
							IRefModel rm = RefSelfUtil.getRefModel(refnode);
							/**
							 * 写入读取字段
							 */
							if (rm == null) {
								throw new LuiRuntimeException("所选参照定义有错误，请重新选择！");
							}
							refnode.setReadFields(rm.getRefNameField() + "," + rm.getPkFieldCode());
							refnode.setWriteFields(writeFieldsStr);
							refnode.setWriteDataset(writeDs);
							MatchField mf = new MatchField();
							String refNameField = rm.getRefNameField();
							/**
							 * 解决参照中返回的字段带表名的问题
							 */
							String[] refNameFieldArray = refNameField.split("\\.");
							mf.setReadField(refNameFieldArray[refNameFieldArray.length - 1]);
							mf.setWriteField(subeleid + "_refname");
							fr.setMatchFields(new MatchField[] { mf });
							WhereField wf = new WhereField();
							String PkFieldCode = rm.getPkFieldCode();
							String[] PkFieldCodeArray = PkFieldCode.split("\\.");
							wf.setKey(PkFieldCodeArray[PkFieldCodeArray.length - 1]);
							wf.setValue(subeleid);
							fr.setWhereField(wf);
							writeDataset.getFieldRelations().addFieldRelation(fr);
						}
						LifeCyclePhase oriPhase = RequestLifeCycleContext.get().getPhase();
						RequestLifeCycleContext.get().setPhase(LifeCyclePhase.nullstatus);
						wd.getViewModels().addRefNode(refnode);
						RequestLifeCycleContext.get().setPhase(oriPhase);
					} else if ("BASEDOC".equals(pid)) {
						SelfDefRefNode refnode = new SelfDefRefNode();
						refnode.setId(code);
						refnode.setTitle(name);
						String[] doccodeArr = code.split("_");
						String doccode = doccodeArr[doccodeArr.length - 1];
						refnode.setPath("app/mockapp/cdref?model=" + MockTreeRefPageModel.class.getName() + "&ctrl=" + MockSelfBasicDocRefController.class.getName() + "&basedoccode=" + doccode);
						if (writeDs != null) {
							FieldRelation fr = new FieldRelation();
							RefMdDataset rmd = new RefMdDataset();
							rmd.setId("$refds_defdoc_refDataset" + code);
							rmd.setCaption(subeleid);
							fr.setRefDataset(rmd.getId());
							RequestLifeCycleContext.get().setPhase(LifeCyclePhase.nullstatus);
//							rmd.setObjMeta("uap.defdoc");
							wd.getViewModels().addDataset(rmd);
							RequestLifeCycleContext.get().setPhase(LifeCyclePhase.ajax);
							fr.setId("$FieldRel_defdoc" + code);
							/**
							 * 写入读取字段
							 */
							refnode.setWriteFields(writeFieldsStr);
							refnode.setWriteDs(writeDs);
							MatchField mf = new MatchField();
							mf.setReadField("name");
							mf.setWriteField(subeleid + "_refname");
							fr.setMatchFields(new MatchField[] { mf });
							WhereField wf = new WhereField();
							wf.setKey("pk_defdoc");
							wf.setValue(subeleid);
							fr.setWhereField(wf);
							writeDataset.getFieldRelations().addFieldRelation(fr);
						}
						LifeCyclePhase oriPhase = RequestLifeCycleContext.get().getPhase();
						RequestLifeCycleContext.get().setPhase(LifeCyclePhase.nullstatus);
						wd.getViewModels().addRefNode(refnode);
						RequestLifeCycleContext.get().setPhase(oriPhase);
					}
				}
				Map<String, String> writeFields = new HashMap<String, String>();
				writeFields.put(owner, code);
				paramMap.put(WRITE_FIELDS, writeFields);
			} else if (CURRENT_WIDGET.equals(pid)) {
				Map<String, String> writeFields = new HashMap<String, String>();
				writeFields.put(owner, value);
				paramMap.put(WRITE_FIELDS, writeFields);
			}
		}
		if (code != null) {
			AppSession.current().getAppContext().addExecScript("parent.document.getElementById('iframe_tmp').contentWindow.reNewModel('" + code + "','" + PaModelOperateController.TYPE_REFNODE + "','" + wd.getId() + "');");
		}
		LuiPlugoutCmd uifPluOutCmd = new LuiPlugoutCmd(MAIN, REF_OK_PLUGOUT, paramMap);
		uifPluOutCmd.execute();
	}
	/**
	 * 行选中事件.用以懒加载元数据枚举
	 * 
	 * @param e
	 */
	public void afterRowSel(xap.lui.core.event.DatasetEvent e) {
		Dataset ds = e.getSource();
		Row row = ds.getSelectedRow();
		String id = row.getString(0);
		String pid = row.getString(1);
		String loaded = "N";
		Dataset gridDs = AppSession.current().getViewContext().getView().getViewModels().getDataset("masterDs");
		gridDs.clear();
		if (CURRENT_WIDGET.equals(id) && !YES.equals(loaded)) {
			LuiWebSession webSession = LuiRuntimeContext.getWebContext().getPageWebSession();
			String sourceWin = webSession.getOriginalParameter(LuiRuntimeContext.DESIGNWINID);
			String sourceView = webSession.getOriginalParameter(SOURCE_VIEW);
			IPaEditorService es =  new PaEditorServiceImpl();
			String sessionId = LuiRuntimeContext.getWebContext().getRequest().getSession().getId();
			PagePartMeta pm = es.getOriPageMeta(sourceWin, sessionId);
			AppSession.current().getAppContext().getWindowContext("pa");
			ViewPartMeta wd = pm.getWidget(sourceView);
			IRefNode[] cds = wd.getViewModels().getRefNodes();
			if (cds != null) {
				for (IRefNode cd : cds) {
					BaseRefNode rn = (BaseRefNode) cd;
					createRow(gridDs, rn.getId(), id, rn.getTitle());
				}
			}
			row.setString(3, YES);
			return;
		}
		/**
		 * 根据module查询nc参照，避免太多
		 */
		BdRefInfoDO[] refs = getRefNodeByModule(id);
		gridDs.clear();
		if (refs != null && refs.length > 0) {
			for (int j = 0; j < refs.length; j++) {
				BdRefInfoDO ref = refs[j];
				Row rowx = createRow(gridDs, ref.getCode(), pid, ref.getName(), ref.getCode());
				//rowx.setString(gridDs.nameToIndex("ext1"), ref.getModuleName() + "." + ref.getMetadataTypeName());
			}
			row.setString(3, YES);
			return;
		}
	}
	/**
	 * 根据模块联查参照信息
	 * 
	 * @param moduleid
	 * @return
	 */
	private BdRefInfoDO[] getRefNodeByModule(String moduleid) {
		String sql = " refmodelname IS NOT NULL AND cc.componentid = co.id AND cc.componentid in (SELECT id FROM md_component   WHERE ownmodule = '" + moduleid + "')";
		List<BdRefInfoDO> refList = qryRefInfosByWherePart(sql);
		return refList.toArray(new BdRefInfoDO[] {});
	}
	/**
	 * 根据条件从元数据表中查询参照数组
	 * 
	 * @param sql
	 * @return
	 */
	protected List<BdRefInfoDO> qryRefInfosByWherePart(String sql) {
		LuiWebSession webSession = LuiRuntimeContext.getWebContext().getPageWebSession();
		String owner = (String) webSession.getOriginalParameter(OWNER);
		String writeDs = (String) webSession.getOriginalParameter("writeDs");
		String subeleid = (String) webSession.getOriginalParameter("subeleid");
		String eleid = webSession.getOriginalParameter("eleid");
		if (writeDs != null)
			eleid = writeDs + "_" + subeleid;
		List<Map> mds = null;
		try {
			mds = (List<Map>) CRUDHelper.getCRUDService().query(QRY_SQL_HEAD + sql, new MapListHandler());
		} catch (Exception e) {
			LuiLogger.error(e.getMessage(), e);
		}
		List<BdRefInfoDO> refList = buildRefInfo(mds, eleid);
		return refList;
	}
	/**
	 * 构造参照信息
	 * 
	 * @param mds
	 * @return
	 */
	protected List<BdRefInfoDO> buildRefInfo(List<Map> mds, String fieldName) {
		List<BdRefInfoDO> refList = new ArrayList<BdRefInfoDO>();
		if (mds != null && !mds.isEmpty()) {
			for (Map md : mds) {
				String refName = (String) md.get("refmodelname");
				String nameSpace = (String) md.get("nbs");
				String mdName = (String) md.get("cmdname");
				String condition = " name in('" + StringUtils.join(refName.split(";"), "','") + "') ";
				BdRefInfoDO[] refInfs = getRefNodeByCondition(condition);
				if (refInfs != null && refInfs.length > 0) {
					for (BdRefInfoDO refInf : refInfs) {
						//refInf.setCode(nameSpace + "_" + fieldName + "_" + refInf.getPk_refinfo().replace("@", ""));
						//refInf.setModuleName(nameSpace);
						//refInf.setMetadataTypeName(mdName);
						refList.add(refInf);
					}
				}
			}
		}
		return refList;
	}
	/**
	 * 根据条件查询功能节点
	 * 
	 * @param condition
	 * @return
	 */
	private BdRefInfoDO[] getRefNodeByCondition(String condition) {
		String sql = "select * from bd_refinfo where " + condition;
		try {
			BdRefInfoDO[] refinfos =(BdRefInfoDO[]) CRUDHelper.getCRUDService().queryVOs(sql, BdRefInfoDO.class, null, null);
			if (refinfos != null && refinfos.length > 0)
				return refinfos;
		} catch (Throwable e) {
			LuiLogger.error(e.getMessage(), e);
		}
		return null;
	}
	@Override
	public void textValueChanged(TextEvent e) {
		TextComp text = (TextComp)e.getSource();
		String filter = text.getValue();
		if (filter == null)
			return;
		Dataset ds = AppSession.current().getViewContext().getView().getViewModels().getDataset(MASTER_DS);
		PageData rd = ds.getCurrentPageData();
		rd.clear();
		String sql = " refmodelname IS NOT NULL AND cc.componentid = co.id AND refmodelname LIKE '%" + filter + "%'";
		/**
		 * 根据过滤条件查询nc参照
		 */
		BdRefInfoDO[] refs = null;
		List<BdRefInfoDO> refList = qryRefInfosByWherePart(sql);
		refs = refList.toArray(new BdRefInfoDO[] {});
		if (refs == null || refs.length == 0)
			return;
		for (int i = 0; i < refs.length; i++) {
			BdRefInfoDO ref = refs[i];
			Row rowx = createRow(ds, ref.getCode(), METADATA, ref.getName(), ref.getCode());
			//rowx.setString(ds.nameToIndex("ext1"), ref.getModuleName() + "." + ref.getMetadataTypeName());
		}
		//		 
		// if(rows == null || rows.length == 0)
		// return;
		//
		// int idIndex = ds.nameToIndex("id");
		// int labelIndex = ds.nameToIndex("label");
		// for(int i = 0; i < rows.length; i++){
		//			 
		// Row row = rows[i];
		// String id = (String) row.getValue(idIndex);
		// String label = (String) row.getValue(labelIndex);
		// if((id != null && id.contains(filter)) || (label != null &&
		// label.contains(filter)))
		// rd.addRow(row);
		// }
	}
}
