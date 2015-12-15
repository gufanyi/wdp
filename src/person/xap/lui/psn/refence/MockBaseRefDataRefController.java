package xap.lui.psn.refence;

import java.util.HashMap;
import java.util.Map;

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
import xap.lui.core.logger.LuiLogger;
import xap.lui.core.mock.MockTreeGridController;
import xap.lui.core.model.AppSession;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.model.LuiPlugoutCmd;
import xap.lui.core.model.PagePartMeta;
import xap.lui.core.model.RequestLifeCycleContext;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.core.refrence.GenericRefNode;
import xap.lui.core.refrence.IRefModel;
import xap.lui.core.refrence.IRefNode;
import xap.lui.core.refrence.RefSelfUtil;
import xap.lui.core.refrence.SelfDefRefNode;
import xap.lui.core.services.IPaEditorService;
import xap.lui.core.services.PaEditorServiceImpl;
import xap.sys.bdrefinfo.d.BdRefInfoDO;

public class MockBaseRefDataRefController extends MockTreeGridController {
	private static final String PID = "pid";
	private static final String ID = "id";
	private static final String MASTER_DS = "masterDs";
	private static final String SOURCE_VIEW = "sourceView";
	private static final String OWNER = "owner";
	private static final String SOURCE_WIN = "sourceWin";
	private static final String DS_MIDDLE = "refcfgds";
	private static final String DATASET = "Dataset";
	private static final String TYPE = "type";
	private static final String MAIN = "main";
	private static final String REF_OK_PLUGOUT = "refOkPlugout";
	private static final String WRITE_FIELDS = "writeFields";
	// private static final String CURRENT_WIDGET = "CURRENT_WIDGET";
	private static final String YES = "Y";
	private static final String METADATA = "METADATA";

	@Override
	public void dataLoad(DatasetEvent e) {
		Dataset ds = e.getSource();
		ds.clear();
		// 创建NC参照和自定义档案的数据
		createRow(ds, METADATA, "", "NC参照");
		createRow(ds, "BASEDOC", "", "基本档案");
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
		String sourceWin =null;// PaHelper.getCurrentEditWindowId();
		if (sourceWin == null) {
			sourceWin = LuiRuntimeContext.getWebContext().getAppWebSession().getOriginalParameter("sourceWinId");
		}
		// 判断从设计器还是从eclipse传过来的url
		String from = LuiRuntimeContext.getWebContext().getAppWebSession().getOriginalParameter("from");
		// 获取要编辑的参照的id
		String refId = LuiRuntimeContext.getWebContext().getAppWebSession().getOriginalParameter("refId");
		String sourceView = LuiRuntimeContext.getWebContext().getAppWebSession().getOriginalParameter(SOURCE_VIEW);
		IPaEditorService es =  new PaEditorServiceImpl();
		String sessionId = LuiRuntimeContext.getWebContext().getRequest().getSession().getId();
		PagePartMeta pm = es.getOriPageMeta(sourceWin, sessionId);
		ViewPartMeta wd = pm.getWidget(sourceView);
		// 通过所编辑对象获取当前编辑的数据集
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
		Dataset currentDs = widget.getViewModels().getDataset(MASTER_DS);
		;
		Row currRow = null;
		currRow = currentDs.getSelectedRow();
		// String value = currRow.getString(currentDs.nameToIndex(ID)) ;
		String name = currRow.getString(currentDs.nameToIndex("label"));
		String pid = currRow.getString(currentDs.nameToIndex(PID));
		String code = currRow.getString(3);
		if (currRow != null) {
			Dataset writeDataset = null;
			if (pid != null) {
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
					} else {
						writeFieldsStr = dsFeild + "," + dsFeild.replace("_refname", "");
					}
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
						GenericRefNode refnode = (GenericRefNode) wd.getViewModels().getRefNode(refId);
						refnode.setRefcode(name);
						refnode.setId(code);
						if (writeDs != null) {
							FieldRelation fr = new FieldRelation();
							RefMdDataset rmd = new RefMdDataset();
							rmd.setId("$refds_" + refnode.getId().trim() + "refDataset");
							rmd.setCaption(subeleid);
							String objMeta = currRow.getString(4);
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
							refnode.setReadFields(rm.getRefNameField() + "," + rm.getPkFieldCode());
							refnode.setWriteFields(writeFieldsStr);
							refnode.setWriteDataset(writeDs);
							MatchField mf = new MatchField();
							mf.setReadField(rm.getRefNameField());
							mf.setWriteField(subeleid + "_refname");
							fr.setMatchFields(new MatchField[] { mf });
							WhereField wf = new WhereField();
							wf.setKey(rm.getPkFieldCode());
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
						refnode.setPath("app/mockapp/cdref?model=" + MockTreeRefPageModel.class.getName() + "&ctrl=" + MockSelfBasicDocRefController.class.getName() + "&basedoccode=" + code);
						if (writeDs != null) {
							FieldRelation fr = new FieldRelation();
							RefMdDataset rmd = new RefMdDataset();
							rmd.setId("$refds_defdoc_refDataset");
							rmd.setCaption(subeleid);
							fr.setRefDataset(rmd.getId());
							RequestLifeCycleContext.get().setPhase(LifeCyclePhase.nullstatus);
//							rmd.setObjMeta("uap.defdoc");
							wd.getViewModels().addDataset(rmd);
							RequestLifeCycleContext.get().setPhase(LifeCyclePhase.ajax);
							fr.setId("$FieldRel_defdoc");
							/**
							 * 写入读取字段
							 */
							// refnode.setReadFields("id,label");
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
			}
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
		String loaded = row.getString(3);
		Dataset gridDs = AppSession.current().getViewContext().getView().getViewModels().getDataset("masterDs");
		if ("BASEDOC".equals(id) && !YES.equals(loaded)) {
//			IDefdoclistQryService docQry = NCLocator.getInstance().lookup(IDefdoclistQryService.class);
//			try {
//				DefdoclistVO[] doclist = docQry.queryDefdoclistVOs();
//				if (doclist != null && doclist.length > 0) {
//					for (DefdoclistVO doc : doclist) {
//						createRow(gridDs, doc.getCode(), id, doc.getName(), doc.getPk_defdoclist());
//					}
//				}
//			} catch (BusinessException e1) {
//				LuiLogger.error(e1.getMessage(), e1);
//			}
//			row.setString(3, YES);
		}
	}

	private BdRefInfoDO[] getRefNodeByModule(String moduleid) {
		String sql = "select * from bd_refinfo where modulename = '" + moduleid + "'";
		try {
			BdRefInfoDO[] refinfos = (BdRefInfoDO[])CRUDHelper.getCRUDService().queryVOs(sql, BdRefInfoDO.class, null, null);
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
		Row[] rows = ds.getCurrentPageData().getRows();
		PageData rd = ds.getCurrentPageData();
		rd.clear();
		if (rows == null || rows.length == 0)
			return;
		int idIndex = ds.nameToIndex("id");
		int labelIndex = ds.nameToIndex("label");
		for (int i = 0; i < rows.length; i++) {
			Row row = rows[i];
			String id = (String) row.getValue(idIndex);
			String label = (String) row.getValue(labelIndex);
			if ((id != null && id.contains(filter)) || (label != null && label.contains(filter)))
				rd.addRow(row);
		}
	}
}
