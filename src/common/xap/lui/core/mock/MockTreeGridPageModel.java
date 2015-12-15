package xap.lui.core.mock;

import xap.lui.core.builder.Window;
import xap.lui.core.common.LuiWebContext;
import xap.lui.core.comps.ButtonComp;
import xap.lui.core.comps.GridColumn;
import xap.lui.core.comps.GridComp;
import xap.lui.core.comps.RecursiveTreeLevel;
import xap.lui.core.comps.TextComp;
import xap.lui.core.comps.TreeViewComp;
import xap.lui.core.constant.StringDataTypeConst;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.Field;
import xap.lui.core.event.AppRequestProcessor;
import xap.lui.core.event.DatasetEvent;
import xap.lui.core.event.MouseEvent;
import xap.lui.core.event.TextEvent;
import xap.lui.core.layout.UIButton;
import xap.lui.core.layout.UIFlowhLayout;
import xap.lui.core.layout.UIFlowhPanel;
import xap.lui.core.layout.UIFlowvLayout;
import xap.lui.core.layout.UIFlowvPanel;
import xap.lui.core.layout.UIGridComp;
import xap.lui.core.layout.UIPartMeta;
import xap.lui.core.layout.UITextField;
import xap.lui.core.layout.UITreeComp;
import xap.lui.core.layout.UIViewPart;
import xap.lui.core.listener.DatasetRule;
import xap.lui.core.listener.EventSubmitRule;
import xap.lui.core.listener.LuiEventConf;
import xap.lui.core.listener.WidgetRule;
import xap.lui.core.model.IUIPartMeta;
import xap.lui.core.model.PagePartMeta;
import xap.lui.core.model.ViewPartMeta;

/**
 * 树表型页面生成类
 * @author zhangxya
 *
 */
public class MockTreeGridPageModel extends Window {
	public static final String TREE = "tree";
	public static final String TREEDS = "treeDs";
	
	
	public static final String GRID = "grid";
	public static final String GRIDDS = "masterDs";
	
	private static final String CANCELBT = "cancelbt";
	private static final String OKBT = "okbt";
	public static final String WIDGET_ID = "main";
	private static final String ID_FIELD = "id";
	private static final String PID_FIELD = "pid";
	private static final String LABEL_FIELD = "label";
	private static final String LOAD_FIELD = "load";
	@Override
	protected IUIPartMeta createUIMeta(PagePartMeta pm) {
		UIPartMeta uimeta = new UIPartMeta();
		
		UIViewPart widget = new UIViewPart();
		widget.setId(WIDGET_ID);
		UIPartMeta wuimeta = new UIPartMeta();
		wuimeta.setId(WIDGET_ID + "_meta");
		widget.setUimeta(wuimeta);
		
		constructViewUI(wuimeta);
		
		uimeta.setIsReference(1);
		uimeta.setElement(widget);
		return uimeta;
	}

	private void constructViewUI(UIPartMeta wuimeta) {
			
		UIFlowvLayout flowvLayout = new UIFlowvLayout();
		flowvLayout.setId("flowvLayout");
		wuimeta.setElement(flowvLayout);
	
		//center
		UIFlowvPanel centerFlowvPanel = new UIFlowvPanel();
		flowvLayout.addPanel(centerFlowvPanel);
				
		
		UIFlowhLayout flowhLayout = new UIFlowhLayout();
		flowhLayout.setId("flowhlayout");
		
		centerFlowvPanel.setElement(flowhLayout);
		
		UIFlowhPanel uiSplitterOne = new UIFlowhPanel();
		uiSplitterOne.setId("panel1");
		uiSplitterOne.setWidth("200");
		uiSplitterOne.setRightBorder("#");
		flowhLayout.addPanel(uiSplitterOne);
		
		UIFlowhPanel uiSplitterTwo = new UIFlowhPanel();
		uiSplitterTwo.setId("panel2");
		flowhLayout.addPanel(uiSplitterTwo);
		
		UITreeComp treeComp = new UITreeComp();
		treeComp.setId(TREE);
		treeComp.setViewId("main");
		
		UITextField search = new UITextField();
		search.setId("search");
		search.setViewId("main");
		
			UIFlowvLayout vlayout = new UIFlowvLayout();
			vlayout.setId("vlayout1");
			
			UIFlowvPanel vpanel1 = new UIFlowvPanel();
			vpanel1.setId("vpanel1");
			vpanel1.setHeight("30");
			vpanel1.setLeftPadding("10");
			vpanel1.setTopPadding("5");
			vpanel1.setElement(search);
			vlayout.addPanel(vpanel1);
		
			UIFlowvPanel vpanel2 = new UIFlowvPanel();
			vpanel2.setId("vpanel2");
			vpanel2.setElement(treeComp);
			vlayout.addPanel(vpanel2);
			
			uiSplitterOne.setElement(vlayout);	
			
		UIGridComp uigridComp = new UIGridComp();
		uigridComp.setId(GRID);
		uigridComp.setViewId("main");
		uiSplitterTwo.setElement(uigridComp);
		
		//bottom
		UIFlowvPanel borderPanelBottom = new UIFlowvPanel();
		borderPanelBottom.setId("bottomvp1");
		borderPanelBottom.setHeight("45");
		flowvLayout.addPanel(borderPanelBottom);

		UIFlowhLayout uiflowhLayout = new UIFlowhLayout();
		uiflowhLayout.setId("flowhLayout");
		borderPanelBottom.setElement(uiflowhLayout);
		
		UIFlowhPanel flowhPanel1 = new UIFlowhPanel();
		flowhPanel1.setAttribute("id", "flowhPanel1");
		uiflowhLayout.addPanel(flowhPanel1);
	
		UIFlowhPanel flowhPanel2 = new UIFlowhPanel();
		flowhPanel2.setAttribute("id", "flowhPanel2");
		uiflowhLayout.addPanel(flowhPanel2);
		flowhPanel2.setWidth("84");
		flowhPanel2.setTopPadding("12");
		
		UIButton buttonOk = new UIButton();
		buttonOk.setId("okbt");
		buttonOk.setViewId("main");
		buttonOk.setWidth("74");
		buttonOk.setClassName("blue_button_div");
		flowhPanel2.setElement(buttonOk); 
		
		UIFlowhPanel flowhPanel3 = new UIFlowhPanel();
		uiflowhLayout.addPanel(flowhPanel3);
		flowhPanel3.setAttribute("id", "flowhPanel3");
		flowhPanel3.setTopPadding("12");
		flowhPanel3.setWidth("94");
		
		UIButton buttonCanCel = new UIButton();
		buttonCanCel.setId("cancelbt");
		buttonCanCel.setViewId("main");
		buttonCanCel.setWidth("74");
		flowhPanel3.setElement(buttonCanCel);
	}

	@Override
	protected PagePartMeta createPageMeta() {
		PagePartMeta pm = new PagePartMeta();
		pm.setId(getWebSesssion().getPageId());
		pm.setProcessorClazz(AppRequestProcessor.class.getName());
		ViewPartMeta widget = new ViewPartMeta();
		constructWidget(widget);
		widget.setId(WIDGET_ID);
		pm.addWidget(widget);
		
		Dataset ds = new Dataset(TREEDS);
		ds.setLazyLoad(false);
		ds.setEdit(true);
		constructTreeDataset(ds);
		widget.getViewModels().addDataset(ds);
		
		TreeViewComp tree = new TreeViewComp();
		tree.setId(TREE);
		constructTree(tree);
		RecursiveTreeLevel level = new RecursiveTreeLevel();
		level.setId("level1");
		constructTreeLevel(level);
		tree.setTopLevel(level);
		widget.getViewComponents().addComponent(tree);
		
		TextComp search = new TextComp();
		search.setId("search");
		search.setText("定位");
		search.setFocus(true);
		constructText(search);
		widget.getViewComponents().addComponent(search);
		
		Dataset gridDs = new Dataset(GRIDDS);
		gridDs.setLazyLoad(true);
		constructGridDataset(gridDs);
		widget.getViewModels().addDataset(gridDs);
		
		GridComp grid = new GridComp();
		grid.setId(GRID);
		grid.setDataset(GRIDDS);
		constructGrid(grid);
		
		widget.getViewComponents().addComponent(grid);
		
		
		ButtonComp okbt = new ButtonComp();
		constructOkButton(okbt);
		widget.getViewComponents().addComponent(okbt);
		
		ButtonComp cancelbt = new ButtonComp();
		constructCancelButton(cancelbt);
		widget.getViewComponents().addComponent(cancelbt);
		return pm;
	}

	protected void constructGrid(GridComp grid) {
		GridColumn columnId = generateGridColumn(ID_FIELD, "ID");
		grid.addColumn(columnId);
		
		GridColumn columnCode = generateGridColumn(LABEL_FIELD, "名称");
		grid.addColumn(columnCode);
	}
	
	
	private GridColumn generateGridColumn(String id, String text) {
		GridColumn columnId = new GridColumn();
		columnId.setId(id);
		columnId.setText(text);
		columnId.setWidth(120);
		columnId.setField(id);
		columnId.setDataType(StringDataTypeConst.STRING);
		columnId.setVisible(true);
		columnId.setEdit(false);
		return columnId;
	}
	
	protected void constructWidget(ViewPartMeta widget) {
	
	}

	protected void constructTree(TreeViewComp tree) {
		
	}

	protected void constructOkButton(ButtonComp okbt) {
		okbt.setId(OKBT);
		okbt.setText("确定");
		
		LuiEventConf event = new LuiEventConf();
		event.setEventName(MouseEvent.ON_CLICK);
		event.setMethod("okButtonClick");
		
		EventSubmitRule submitRule = new EventSubmitRule();
		WidgetRule pwr = new WidgetRule();
		pwr.setId("main");
		DatasetRule dsRule = new DatasetRule();
		dsRule.setId(TREEDS);
		submitRule.addWidgetRule(pwr);
		pwr.addDsRule(dsRule);
		event.setSubmitRule(submitRule);
		LuiWebContext ctx = getWebSesssion();
		String widgetId = ctx.getParameter("widgetId");
		if(widgetId == null || widgetId.equals(""))
			widgetId = "main";
		EventSubmitRule parentSum = new EventSubmitRule();
		WidgetRule pwidgetRule = new WidgetRule();
		parentSum.addWidgetRule(pwidgetRule);
		pwidgetRule.setId(widgetId);
		submitRule.setParentSubmitRule(parentSum);
		
		event.setEventType(MouseEvent.class.getSimpleName());
		okbt.addEventConf(event);
	}

	
	private void constructText(TextComp search) {
		LuiEventConf event = new LuiEventConf();
		event.setEventName(TextEvent.VALUE_CHANGED);
		event.setMethod("textValueChanged");
		
		EventSubmitRule sbRule = new EventSubmitRule();
		WidgetRule pwr = new WidgetRule();
		pwr.setId("main");
		DatasetRule dsRule = new DatasetRule();
		dsRule.setType(DatasetRule.TYPE_ALL_LINE);
		dsRule.setId(GRIDDS);
		pwr.addDsRule(dsRule);
		sbRule.addWidgetRule(pwr);
		
		LuiWebContext ctx = getWebSesssion();
		String widgetId = ctx.getParameter("widgetId");
		if(widgetId == null || widgetId.equals(""))
			widgetId = "main";
		
		EventSubmitRule parentSum = new EventSubmitRule();
		WidgetRule pwidgetRule = new WidgetRule();
		parentSum.addWidgetRule(pwidgetRule);
		pwidgetRule.setId(widgetId);
		sbRule.setParentSubmitRule(parentSum);
		event.setSubmitRule(sbRule);
		event.setEventType(TextEvent.class.getSimpleName());
		search.addEventConf(event);
		
		
	}
	
	protected void constructCancelButton(ButtonComp cancelbt) {
		cancelbt.setId(CANCELBT);
		cancelbt.setText("取消");
		
		LuiEventConf event = new LuiEventConf();
		event.setEventName(MouseEvent.ON_CLICK);
		event.setMethod("cancelButtonClick");
		event.setEventType(MouseEvent.class.getSimpleName());
		cancelbt.addEventConf(event);
	}

	protected void constructTreeLevel(RecursiveTreeLevel level) {
		level.setMasterKeyField(ID_FIELD);
		level.setLabelFields(LABEL_FIELD);
		level.setRecursiveField(ID_FIELD);
		level.setRecursiveParentField(PID_FIELD);
		level.setLoadField(LOAD_FIELD);
		level.setDataset(TREEDS);
	}
	
	
	
	protected void constructGridDataset(Dataset ds) {
		Field idField = new Field();
		idField.setId(ID_FIELD);
		idField.setText("ID");
		ds.addField(idField);
		
		Field pidField = new Field();
		pidField.setId(PID_FIELD);
		pidField.setText("PID");
		ds.addField(pidField);
		
		Field labelField = new Field();
		labelField.setId(LABEL_FIELD);
		labelField.setText("名称");
		ds.addField(labelField);
		
		Field loadField = new Field();
		loadField.setId(LOAD_FIELD);
		loadField.setText("LOAD");
		ds.addField(loadField);
		
		Field ext1Field = new Field();
		ext1Field.setId("ext1");
		ext1Field.setText("ext1");
		ds.addField(ext1Field);
		
	}

	protected void constructTreeDataset(Dataset ds) {
		Field idField = new Field();
		idField.setId(ID_FIELD);
		idField.setText("ID");
		ds.addField(idField);
		
		Field pidField = new Field();
		pidField.setId(PID_FIELD);
		pidField.setText("PID");
		ds.addField(pidField);
		
		Field labelField = new Field();
		labelField.setId(LABEL_FIELD);
		labelField.setText("LABEL");
		ds.addField(labelField);
		
		Field loadField = new Field();
		loadField.setId(LOAD_FIELD);
		loadField.setText("LOAD");
		ds.addField(loadField);
		
		LuiEventConf event = new LuiEventConf();
		event.setEventName(DatasetEvent.ON_DATA_LOAD);
		event.setMethod("dataLoad");
		event.setEventType(DatasetEvent.class.getSimpleName());
		ds.addEventConf(event);
		
		
		LuiEventConf onafterRowSelevent = new LuiEventConf();
		onafterRowSelevent.setEventName(DatasetEvent.ON_AFTER_ROW_SELECT);
		onafterRowSelevent.setMethod("afterRowSel");
		onafterRowSelevent.setEventType(DatasetEvent.class.getSimpleName());
		ds.addEventConf(onafterRowSelevent);
	}
}

