package xap.lui.psn.treegridmodel;


import org.apache.commons.lang.StringUtils;

import xap.lui.core.command.CmdInvoker;
import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.common.LuiWebSession;
import xap.lui.core.comps.ButtonComp;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.Row;
import xap.lui.core.event.DatasetEvent;
import xap.lui.core.event.MouseEvent;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.model.AppSession;
import xap.lui.core.model.LuiAppUtil;
import xap.lui.core.model.PagePartMeta;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.core.refmodel.TreeGridRefModel;
import xap.lui.core.services.IPaEditorService;
import xap.lui.core.services.PaEditorServiceImpl;
import xap.lui.psn.cmd.LuiAddRowCmd;
import xap.lui.psn.pamgr.PaEntityDsListener;

public class TreeGridModelCtrl {
	public static final String PARAMS_SRCVIEW_ID = "sourceView";
	private ViewPartMeta sourceWidget = null;
	private String neworedit;
	private String modelId;
	private void initParams() {
		LuiWebSession session = LuiRuntimeContext.getWebContext().getPageWebSession();
		this.neworedit = session.getOriginalParameter("neworedit");
		this.modelId = session.getOriginalParameter("modelId");
		String pageId = session.getOriginalParamMap().get(LuiRuntimeContext.DESIGNWINID);
		String viewId = session.getOriginalParamMap().get(PARAMS_SRCVIEW_ID);
		if(pageId == null || viewId == null)
			return;
		String sessionId = LuiRuntimeContext.getWebContext().getRequest().getSession().getId();
		IPaEditorService pes = new PaEditorServiceImpl();
		PagePartMeta pagemeta = pes.getOriPageMeta(pageId, sessionId);
		this.sourceWidget = pagemeta.getWidget(viewId);// PaCache.getEditorViewPartMeta();
	}
	
	public void ds_form_onDataLoad(DatasetEvent event) {
		initParams();
		String dsId = "ds_form";
		CmdInvoker.invoke(new LuiAddRowCmd(dsId));
		if(StringUtils.equals(neworedit, "edit")){
			TreeGridRefModel tgmodel = (TreeGridRefModel) sourceWidget.getViewModels().getRefModel(modelId);
			if(tgmodel == null) return;
			Dataset ds = event.getSource();
			Row row = ds.getSelectedRow();
			row.setValue(ds.nameToIndex("id"), tgmodel.getId());
			row.setValue(ds.nameToIndex("classTableName"), tgmodel.getClassTableName());
			row.setValue(ds.nameToIndex("classPKField"), tgmodel.getClassPKField());
			row.setValue(ds.nameToIndex("classNameField"), tgmodel.getClassNameField());
			row.setValue(ds.nameToIndex("classCodeField"), tgmodel.getClassCodeField());
			row.setValue(ds.nameToIndex("classJoinField"), tgmodel.getClassJoinField());
			row.setValue(ds.nameToIndex("childField"), tgmodel.getChildField());
			row.setValue(ds.nameToIndex("fatherField"), tgmodel.getFatherField());
			row.setValue(ds.nameToIndex("classWherePart"), tgmodel.getClassWherePart());
			row.setValue(ds.nameToIndex("classOrderPart"), tgmodel.getClassOrderPart());
		}
	}
	
	public void ds_form2_onDataLoad(DatasetEvent event) {
		String dsId = "ds_form2";
		CmdInvoker.invoke(new LuiAddRowCmd(dsId));
		initParams();
		if(StringUtils.equals(neworedit, "edit")){
			Dataset ds = getDs("ds_form2");
			Row row = ds.getSelectedRow();
			TreeGridRefModel tgmodel = (TreeGridRefModel) sourceWidget.getViewModels().getRefModel(modelId);
			if(tgmodel == null) return;
			row.setValue(ds.nameToIndex("tableName"), tgmodel.getTableName());
			row.setValue(ds.nameToIndex("showFields"), tgmodel.getShowFields());
			row.setValue(ds.nameToIndex("showFieldNames"), tgmodel.getShowFieldNames());
			row.setValue(ds.nameToIndex("hiddenFields"), tgmodel.getHiddenFields());
			row.setValue(ds.nameToIndex("docJoinField"), tgmodel.getDocJoinField());
			row.setValue(ds.nameToIndex("blurFields"), tgmodel.getBlurFields());
			row.setValue(ds.nameToIndex("wherePart"), tgmodel.getWherePart());
			row.setValue(ds.nameToIndex("orderPart"), tgmodel.getOrderPart());
			row.setValue(ds.nameToIndex("groupPart"), tgmodel.getGroupPart());
			row.setValue(ds.nameToIndex("pkField"), tgmodel.getPkField());
			row.setValue(ds.nameToIndex("nameField"), tgmodel.getNameField());
			row.setValue(ds.nameToIndex("codeField"), tgmodel.getCodeField());
		}
	}

	public void onOkClick(MouseEvent<ButtonComp> e) {
		initParams();
		Dataset ds1 = getDs("ds_form");
		Row selRow1 = ds1.getSelectedRow();
		String id = selRow1.getString(ds1.nameToIndex("id"));
		if(StringUtils.isBlank(id))
			throw new LuiRuntimeException("id不能为空！");
		String classTableName = selRow1.getString(ds1.nameToIndex("classTableName"));
		if(StringUtils.isBlank(classTableName))
			throw new LuiRuntimeException("主表名不能为空！");
		String classPKField = selRow1.getString(ds1.nameToIndex("classPKField"));
		if(StringUtils.isBlank(classPKField))
			throw new LuiRuntimeException("主表主键字段不能为空");
		String classNameField = selRow1.getString(ds1.nameToIndex("classNameField"));
		if(StringUtils.isBlank(classNameField))
			throw new LuiRuntimeException("主表名称字段不能为空");
		String classJoinField = selRow1.getString(ds1.nameToIndex("classJoinField"));
		if(StringUtils.isBlank(classJoinField))
			throw new LuiRuntimeException("关联子表字段不能为空");
		String childField = selRow1.getString(ds1.nameToIndex("childField"));
		if(StringUtils.isBlank(childField))
			throw new LuiRuntimeException("递归字段不能为空");
		String fatherField = selRow1.getString(ds1.nameToIndex("fatherField"));
		if(StringUtils.isBlank(fatherField))
			throw new LuiRuntimeException("递归父字段不能为空");
		String classCodeField = selRow1.getString(ds1.nameToIndex("classCodeField"));
		String classWherePart = selRow1.getString(ds1.nameToIndex("classWherePart"));
		String classOrderPart = selRow1.getString(ds1.nameToIndex("classOrderPart"));
		
		Dataset ds2 = getDs("ds_form2");
		Row selRow2 = ds2.getSelectedRow();
		String tableName = selRow2.getString(ds2.nameToIndex("tableName"));
		if(StringUtils.isBlank(tableName))
			throw new LuiRuntimeException("子表名不能为空");
		String showFields = selRow2.getString(ds2.nameToIndex("showFields"));
		if(StringUtils.isBlank(showFields))
			throw new LuiRuntimeException("显示字段不能为空");
		String showFieldNames = selRow2.getString(ds2.nameToIndex("showFieldNames"));
		if(StringUtils.isBlank(showFieldNames))
			throw new LuiRuntimeException("显示中文名不能为空");
		String hiddenFields = selRow2.getString(ds2.nameToIndex("hiddenFields"));
		String docJoinField = selRow2.getString(ds2.nameToIndex("docJoinField"));
		if(StringUtils.isBlank(docJoinField))
			throw new LuiRuntimeException("关联主表字段不能为空");
		String blurFields = selRow2.getString(ds2.nameToIndex("blurFields"));
		String wherePart = selRow2.getString(ds2.nameToIndex("wherePart"));
		String orderPart = selRow2.getString(ds2.nameToIndex("orderPart"));
		String groupPart = selRow2.getString(ds2.nameToIndex("groupPart"));
		
		String pkField = selRow2.getString(ds2.nameToIndex("pkField"));
		if(StringUtils.isBlank(pkField))
			throw new LuiRuntimeException("参照主键不能为空");
		String codeField = selRow2.getString(ds2.nameToIndex("codeField"));
		String nameField = selRow2.getString(ds2.nameToIndex("nameField"));
		if(StringUtils.isBlank(nameField))
			throw new LuiRuntimeException("参照名称字段不能为空");
		
		TreeGridRefModel treegridmodel = null;
		if(StringUtils.equals(neworedit, "edit")){
			treegridmodel = (TreeGridRefModel) sourceWidget.getViewModels().getRefModel(modelId);
		}else{
			treegridmodel = new TreeGridRefModel();
		}
		treegridmodel.setId(id);
		treegridmodel.setClassTableName(classTableName);
		treegridmodel.setClassPKField(classPKField);
		treegridmodel.setClassNameField(classNameField);
		treegridmodel.setClassCodeField(classCodeField);
		treegridmodel.setClassJoinField(classJoinField);
		treegridmodel.setChildField(childField);
		treegridmodel.setFatherField(fatherField);
		treegridmodel.setClassWherePart(classWherePart);
		treegridmodel.setClassOrderPart(classOrderPart);
		
		treegridmodel.setTableName(tableName);
		treegridmodel.setShowFields(showFields);
		treegridmodel.setShowFieldNames(showFieldNames);
		treegridmodel.setHiddenFields(hiddenFields);
		treegridmodel.setDocJoinField(docJoinField);
		treegridmodel.setBlurFields(blurFields);
		treegridmodel.setWherePart(wherePart);
		treegridmodel.setOrderPart(orderPart);
		treegridmodel.setGroupPart(groupPart);
		treegridmodel.setPkField(pkField);
		treegridmodel.setNameField(nameField);
		treegridmodel.setCodeField(codeField);
		if(!StringUtils.equals(neworedit, "edit")){
			sourceWidget.getViewModels().addRefModel(treegridmodel);
		}
		
		Dataset entityDateset = LuiAppUtil.getCntAppCtx().getWindowContext("pa").getViewContext("data").getView().getViewModels().getDataset("entityds");
		PaEntityDsListener.setModelData(entityDateset, sourceWidget);
		closepage();
	}
	
	private Dataset getDs(String id) {
		return LuiAppUtil.getCntView().getViewModels().getDataset(id);
	}

	public void onCancelClick(MouseEvent<ButtonComp> e) {
		closepage();
	}

	private void closepage() {
		AppSession.current().getAppContext().closeWinDialog();
	}
}
