package xap.lui.psn.formula;

import org.apache.commons.lang.StringUtils;

import xap.lui.core.comps.GridColumn;
import xap.lui.core.comps.GridComp;
import xap.lui.core.comps.GridTreeLevel;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.Field;
import xap.lui.core.layout.UIFlowhLayout;
import xap.lui.core.layout.UIFlowhPanel;
import xap.lui.core.layout.UIGridComp;
import xap.lui.core.layout.UITabItem;
import xap.lui.core.listener.LuiEventConf;
import xap.lui.core.listener.WidgetRule;
import xap.lui.core.listener.EventSubmitRule;
import xap.lui.core.listener.DatasetRule;
import xap.lui.core.model.DataModels;
import xap.lui.core.model.ViewPartComps;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.psn.refence.FormulaPageModel;

public class DesignerFormulaPageModel extends FormulaPageModel{

	private static final String CTRL_ClASS = "xap.lui.psn.formula.DesignerFormulaController";
	
	@Override
	protected void addTabItem() {
		ViewPartMeta formulaView = getFormulaView();
		DataModels dataModels = formulaView.getViewModels();
		ViewPartComps viewpartcomps = formulaView.getViewComponents();
		Dataset datasetds = createDs("datasetds");
		addField(datasetds, "id");
		addField(datasetds, "description");
		addDsEvent(datasetds, "onDataLoad", "onDataLoad_ds");
		addDsEvent(datasetds, "onAfterRowSelect", "onDatasetDsAfterSelect");
		dataModels.addDataset(datasetds);//数据集ds
		
		Dataset propertyds = createDs("propertyds");
		addField(propertyds, "id");
		addField(propertyds, "description");
		addField(propertyds, "text");
		dataModels.addDataset(propertyds);//属性ds
		
		GridComp datasetgrid = createGrid("datasetgrid", datasetds.getId());//数据集grid
		addColumn(datasetgrid, "id", null, true);
		addColumn(datasetgrid, "description", null, false);
		viewpartcomps.addComponent(datasetgrid);
		
		GridComp propertygrid = createGrid("propertygrid", propertyds.getId());//属性grid
		addColumn(propertygrid, "id", "字段", true);
		addColumn(propertygrid, "description", "字段描述", true);
		addColumn(propertygrid, "text", "名称", true);
		String script = "setDescription(rowSelectedEvent);";
		addGridEvent(propertygrid, "onRowSelected", "onPropertyGridSelect", false, script);
		addGridEvent(propertygrid, "onRowDbClick", "onPropertyGirdDbClick", true, "");
		viewpartcomps.addComponent(propertygrid);
		
		UITabItem dsitem = createTabItem("数据集");
		UIFlowhLayout hlayout = new UIFlowhLayout();
		hlayout.setId("FlowHLafffut0585");
		hlayout.setViewId(VIEWID);
		UIFlowhPanel hPanel = hlayout.addElementToPanel(createUIGrid("datasetgrid"));
		hPanel.setWidth("140");
		hPanel.setRightBorder("1px gray dashed");
		hlayout.addElementToPanel(createUIGrid("propertygrid"));
		dsitem.setElement(hlayout);
		
		UITabItem ctrlitem = createTabItem("已定义控件");
		Dataset ctrlds = createDs("ctrlds");
		addField(ctrlds, "id");
		addField(ctrlds, "name");
		addField(ctrlds, "pid");
		addField(ctrlds, "description");
		addField(ctrlds, "demo");
		addDsEvent(ctrlds, "onDataLoad", "onDataLoad_ctrl");
		dataModels.addDataset(ctrlds);//控件ds
		GridComp ctrlgrid = createGrid("ctrlgrid", ctrlds.getId());//属性grid
		addColumn(ctrlgrid, "id", "id", true);
		addColumn(ctrlgrid, "name", "名称", true);
		GridTreeLevel gridTree = new GridTreeLevel();
		gridTree.setId("level"+ctrlgrid.getId());
		gridTree.setRecursiveField("id");
		gridTree.setRecursiveParentField("pid");
		gridTree.setDataset(ctrlgrid.getDataset());
		gridTree.setLabelFields("name");
		ctrlgrid.setTopLevel(gridTree);
		addGridEvent(ctrlgrid, "onRowSelected", "setDescription2", true, "");
		LuiEventConf eventConf = addGridEvent(ctrlgrid, "onRowDbClick", "onCtrlGirdDbClick", true, "");
		EventSubmitRule sr = new EventSubmitRule();
		sr.setCardSubmit(false);
		WidgetRule wr = new WidgetRule();
		wr.setId("editor");
		DatasetRule dr = new DatasetRule();
		dr.setId("ctrlds");
		dr.setType("ds_all_line");
		wr.addDsRule(dr);
		sr.addWidgetRule(wr);
		eventConf.setSubmitRule(sr);
		viewpartcomps.addComponent(ctrlgrid);
		
		ctrlitem.setElement(createUIGrid("ctrlgrid"));
		
//	    UIViewPart  uielement=	(UIViewPart)getUIPartMeta().findChildById("editor");
//		java.util.logging.Logger.getAnonymousLogger().info(uielement.getUimeta().toXml());
	}

	private UIGridComp createUIGrid(String id){
		UIGridComp uigrid = new UIGridComp();
		uigrid.setId(id);
		uigrid.setViewId(VIEWID);
		return uigrid;
	}
	
	private void addColumn(GridComp grid, String id, String text, boolean isVisible) {
		GridColumn column = new GridColumn();
		column.setId(id);
		column.setField(id);
		column.setVisible(isVisible);
		if(StringUtils.isNotBlank(text))
			column.setText(text);
		else
			column.setText(id);
		grid.addColumn(column);
	}

	private GridComp createGrid(String gridId, String dsId) {
		GridComp grid = new GridComp();
		grid.setId(gridId);
		grid.setDataset(dsId);
		return grid;
	}

	private LuiEventConf addGridEvent(GridComp grid, String eventName, String method, boolean onserver, String script){
		LuiEventConf eventConf = new LuiEventConf();
		eventConf.setEventName(eventName);
		eventConf.setMethod(method);
		eventConf.setEventType("GridRowEvent");
		eventConf.setOnserver(onserver);
		eventConf.setControllerClazz(CTRL_ClASS);
		eventConf.setScript(script);
		grid.addEventConf(eventConf);
		return eventConf;
	}
	
	private void addDsEvent(Dataset ds, String eventName, String method){
		LuiEventConf eventConf = new LuiEventConf();
		eventConf.setEventName(eventName);
		eventConf.setMethod(method);
		eventConf.setOnserver(true);
		eventConf.setEventType("DatasetEvent");
		eventConf.setControllerClazz(CTRL_ClASS);
		ds.addEventConf(eventConf);
	}
	
	private void addField(Dataset ds, String fieldId) {
		Field field = new Field();
		field.setId(fieldId);
		field.setText(fieldId);
		ds.addField(field);
	}

	private Dataset createDs(String dsId) {
		Dataset datasetds = new Dataset();
		datasetds.setId(dsId);
		datasetds.setCaption(dsId);
		datasetds.setEdit(false);
		return datasetds;
	}

}
