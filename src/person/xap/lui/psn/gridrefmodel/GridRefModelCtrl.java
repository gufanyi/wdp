package xap.lui.psn.gridrefmodel;

import org.apache.commons.lang.StringUtils;

import xap.lui.core.command.CmdInvoker;
import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.common.LuiWebSession;
import xap.lui.core.comps.ButtonComp;
import xap.lui.core.comps.FormComp;
import xap.lui.core.comps.FormElement;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.Row;
import xap.lui.core.event.DatasetEvent;
import xap.lui.core.event.DialogEvent;
import xap.lui.core.event.MouseEvent;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.model.AppSession;
import xap.lui.core.model.LuiAppUtil;
import xap.lui.core.model.PagePartMeta;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.core.refmodel.BaseRefModel;
import xap.lui.core.refmodel.GridRefModel;
import xap.lui.core.refmodel.TreeRefModel;
import xap.lui.core.services.IPaEditorService;
import xap.lui.core.services.PaEditorServiceImpl;
import xap.lui.psn.cmd.LuiAddRowCmd;
import xap.lui.psn.pamgr.PaEntityDsListener;

public class GridRefModelCtrl {
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
	
	public void onBeforeShow(DialogEvent dialogEvent) {
		FormComp form = (FormComp) LuiAppUtil.getCntView().getViewComponents().getComponent("showmodel_form");
		FormElement childele = form.getElementById("childField");
		FormElement fatherele = form.getElementById("fatherField");
		LuiWebSession session = LuiRuntimeContext.getWebContext().getPageWebSession();
		String reftype = session.getOriginalParamMap().get("reftype");
		if(StringUtils.equals(reftype, "grid")){
			childele.setVisible(false);
			fatherele.setVisible(false);
		}
	}
	
	public void ds_form_onDataLoad(DatasetEvent event) {
		initParams();
		String dsId = "ds_form";
		CmdInvoker.invoke(new LuiAddRowCmd(dsId));
		if(StringUtils.equals(neworedit, "edit")){
			BaseRefModel basemodel = sourceWidget.getViewModels().getRefModel(modelId);
			if(basemodel == null) return;
			Dataset ds1 = event.getSource();
			Row selRow = ds1.getSelectedRow();
			selRow.setValue(ds1.nameToIndex("id"), basemodel.getId());
			selRow.setValue(ds1.nameToIndex("tableName"), basemodel.getTableName());
			selRow.setValue(ds1.nameToIndex("showFields"), basemodel.getShowFields());
			selRow.setValue(ds1.nameToIndex("showFieldNames"), basemodel.getShowFieldNames());
			selRow.setValue(ds1.nameToIndex("hiddenFields"), basemodel.getHiddenFields());
			selRow.setValue(ds1.nameToIndex("blurFields"), basemodel.getBlurFields());
			selRow.setValue(ds1.nameToIndex("wherePart"), basemodel.getWherePart());
			selRow.setValue(ds1.nameToIndex("groupPart"), basemodel.getGroupPart());
			selRow.setValue(ds1.nameToIndex("orderPart"), basemodel.getOrderPart());
			selRow.setValue(ds1.nameToIndex("pageSize"), basemodel.getPageSize());
			selRow.setValue(ds1.nameToIndex("enviranmentPart"), basemodel.getEnviranmentPart());
			if(basemodel instanceof TreeRefModel){
				TreeRefModel treemodel = (TreeRefModel)basemodel;
				selRow.setValue(ds1.nameToIndex("childField"), treemodel.getChildField());
				selRow.setValue(ds1.nameToIndex("fatherField"), treemodel.getFatherField());
			}
		}
	}

	public void ds_form2_onDataLoad(DatasetEvent event) {
		initParams();
		String dsId = "ds_form2";
		CmdInvoker.invoke(new LuiAddRowCmd(dsId));
		if(StringUtils.equals(neworedit, "edit")){
			BaseRefModel basemodel = sourceWidget.getViewModels().getRefModel(modelId);
			if(basemodel == null) return;
			Dataset ds = event.getSource();
			Row row = ds.getSelectedRow();
			row.setValue(ds.nameToIndex("refTitle"), basemodel.getRefTitle());
			row.setValue(ds.nameToIndex("pkField"), basemodel.getPkField());
			row.setValue(ds.nameToIndex("nameField"), basemodel.getNameField());
			row.setValue(ds.nameToIndex("codeField"), basemodel.getCodeField());
		}
	}

	public void onOkClick(MouseEvent<ButtonComp> e) {
		initParams();
		Dataset ds1 = getDs("ds_form");
		Row selRow1 = ds1.getSelectedRow();
		String id = selRow1.getString(ds1.nameToIndex("id"));
		if(StringUtils.isBlank(id))
			throw new LuiRuntimeException("id不能为空！");
		String tableName = selRow1.getString(ds1.nameToIndex("tableName"));
		if(StringUtils.isBlank(tableName))
			throw new LuiRuntimeException("表名不能为空！");
		String showFields = selRow1.getString(ds1.nameToIndex("showFields"));
		if(StringUtils.isBlank(showFields))
			throw new LuiRuntimeException("显示字段不能为空");
		String showFieldNames = selRow1.getString(ds1.nameToIndex("showFieldNames"));
		if(StringUtils.isBlank(showFieldNames))
			throw new LuiRuntimeException("显示名称不能为空");
		LuiWebSession session = LuiRuntimeContext.getWebContext().getPageWebSession();
		String reftype = session.getOriginalParamMap().get("reftype");
		String childField = selRow1.getString(ds1.nameToIndex("childField"));
		String fatherField = selRow1.getString(ds1.nameToIndex("fatherField"));
		if(StringUtils.equals(reftype, "tree")){
			if(StringUtils.isBlank(childField))
				throw new LuiRuntimeException("递归字段不能为空");
			if(StringUtils.isBlank(fatherField))
				throw new LuiRuntimeException("父递归字段不能为空");
		}
		
		String hiddenFields = selRow1.getString(ds1.nameToIndex("hiddenFields"));
		String blurFields = selRow1.getString(ds1.nameToIndex("blurFields"));
		String wherePart = selRow1.getString(ds1.nameToIndex("wherePart"));
		String groupPart = selRow1.getString(ds1.nameToIndex("groupPart"));
		String orderPart = selRow1.getString(ds1.nameToIndex("orderPart"));
		int pageSize = -1;
		if(selRow1.getValue(ds1.nameToIndex("pageSize")) != null)
			pageSize = (int)selRow1.getValue(ds1.nameToIndex("pageSize"));
		String enviranmentPart = selRow1.getString(ds1.nameToIndex("enviranmentPart"));
		
		Dataset ds2 = getDs("ds_form2");
		Row selRow2 = ds2.getSelectedRow();
		String refTitle = selRow2.getString(ds2.nameToIndex("refTitle"));
		if(StringUtils.isBlank(refTitle))
			throw new LuiRuntimeException("参照标题不能为空");
		String pkField = selRow2.getString(ds2.nameToIndex("pkField"));
		if(StringUtils.isBlank(pkField))
			throw new LuiRuntimeException("参照主键不能为空");
		String codeField = selRow2.getString(ds2.nameToIndex("codeField"));
		String nameField = selRow2.getString(ds2.nameToIndex("nameField"));
		if(StringUtils.isBlank(nameField))
			throw new LuiRuntimeException("参照名称字段不能为空");
		
		BaseRefModel refmodel = null;
		if(StringUtils.equals(neworedit, "edit")){
			if(StringUtils.equals(reftype, "grid")){
				refmodel = (GridRefModel)sourceWidget.getViewModels().getRefModel(modelId);
			}else{
				refmodel = (TreeRefModel)sourceWidget.getViewModels().getRefModel(modelId);
			}
		}else{
			if(StringUtils.equals(reftype, "grid")){
				refmodel = new GridRefModel();
			}else{
				refmodel = new TreeRefModel();
			}
		}
		if(StringUtils.equals(reftype, "tree")){
			((TreeRefModel)refmodel).setChildField(childField);
			((TreeRefModel)refmodel).setFatherField(fatherField);
		}
		refmodel.setId(id);
		refmodel.setTableName(tableName);
		refmodel.setShowFields(showFields);
		refmodel.setShowFieldNames(showFieldNames);
		refmodel.setHiddenFields(hiddenFields);
		refmodel.setBlurFields(blurFields);
		refmodel.setWherePart(wherePart);
		refmodel.setGroupPart(groupPart);
		refmodel.setOrderPart(orderPart);
		refmodel.setPageSize(pageSize);
		refmodel.setEnviranmentPart(enviranmentPart);
		
		refmodel.setRefTitle(refTitle);
		refmodel.setPkField(pkField);
		refmodel.setCodeField(codeField);
		refmodel.setNameField(nameField);
		if(!StringUtils.equals(neworedit, "edit")){
			sourceWidget.getViewModels().addRefModel(refmodel);
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
