package xap.lui.core.mock;

import xap.lui.core.builder.Window;
import xap.lui.core.common.LuiWebContext;
import xap.lui.core.comps.ButtonComp;
import xap.lui.core.comps.RecursiveTreeLevel;
import xap.lui.core.comps.TextComp;
import xap.lui.core.comps.TreeViewComp;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.Field;
import xap.lui.core.event.AppRequestProcessor;
import xap.lui.core.event.DatasetEvent;
import xap.lui.core.event.KeyEvent;
import xap.lui.core.event.MouseEvent;
import xap.lui.core.layout.UIButton;
import xap.lui.core.layout.UIFlowhLayout;
import xap.lui.core.layout.UIFlowhPanel;
import xap.lui.core.layout.UIFlowvLayout;
import xap.lui.core.layout.UIFlowvPanel;
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
 * 快速构建树及带有确定取消按钮的基类
 * @author dengjt
 *
 */
public class MockTreePageModel extends Window {
	public static final String TREE = "tree";
	public static final String TREEDS = "masterDs";
	public static final String CANCELBT = "cancelbt";
	public static final String OKBT = "okbt";
	public static final String WIDGET_ID = "main";
	public static final String ID_FIELD = "id";
	public static final String PID_FIELD = "pid";
	public static final String LABEL_FIELD = "label";
	public static final String LOAD_FIELD = "load";
	public static final String LOCATE_TEXT="locatetext";
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
		UIFlowvLayout flvLayout = new UIFlowvLayout();
		flvLayout.setId("flowv1");
		flvLayout.setViewId(WIDGET_ID);
		wuimeta.setElement(flvLayout);
		
		UIFlowvPanel flvPanel0 = new UIFlowvPanel();
		flvPanel0.setId("flowvp0");
		flvPanel0.setHeight("50");
		flvLayout.addPanel(flvPanel0);
		UIFlowvLayout flvLayout0 = new UIFlowvLayout();
		flvLayout0.setId("flowv0");
		flvLayout0.setViewId(WIDGET_ID);
		generateLocatePanle(flvLayout0);
		flvPanel0.setElement(flvLayout0);
		
		UIFlowvPanel flvPanel1 = new UIFlowvPanel();
		flvPanel1.setId("flowvp1");
//		flvPanel1.setHeight("350");
		flvLayout.addPanel(flvPanel1);
		
		UITreeComp uiTree = new UITreeComp();
		uiTree.setViewId(WIDGET_ID);
		uiTree.setId(TREE);
		flvPanel1.setElement(uiTree);
		
		UIFlowvPanel flvPanel2 = new UIFlowvPanel();
		flvPanel2.setId("flowvp2");
		flvPanel2.setHeight("50");
		flvLayout.addPanel(flvPanel2);
		wuimeta.setElement(flvLayout);
		
		UIFlowhLayout flhLayout = new UIFlowhLayout();
		flhLayout.setId("flowh1");
		flhLayout.setViewId(WIDGET_ID);
		UIFlowhPanel flhP1 = new UIFlowhPanel();
		flhP1.setId("flowhp1");
		flhLayout.addPanel(flhP1);
		
		UIFlowhPanel flhP2 = new UIFlowhPanel();
		flhP2.setId("flowhp2");
		flhP2.setWidth("70");
		flhLayout.addPanel(flhP2);
		
		UIFlowhPanel flhP3 = new UIFlowhPanel();
		flhP3.setId("flowhp3");
		flhP3.setWidth("100");
		flhLayout.addPanel(flhP3);
		
		flvPanel2.setElement(flhLayout);
		UIButton okbt = new UIButton();
		okbt.setViewId(WIDGET_ID);
		okbt.setId(OKBT);
		okbt.setWidth("70");
		okbt.setClassName("blue_button_div");
		okbt.setTop(15);
		flhP2.setElement(okbt);
		
		UIButton cancelbt = new UIButton();
		cancelbt.setViewId(WIDGET_ID);
		cancelbt.setId(CANCELBT);
		cancelbt.setWidth("70");
		cancelbt.setLeft(10);
		cancelbt.setTop(15);
		flhP3.setElement(cancelbt);
		
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
		constructDataset(ds);
		widget.getViewModels().addDataset(ds);
		
		TreeViewComp tree = new TreeViewComp();
		tree.setId(TREE);
		constructTree(tree);
		RecursiveTreeLevel level = new RecursiveTreeLevel();
		level.setId("level1");
		constructTreeLevel(level);
		tree.setTopLevel(level);
		widget.getViewComponents().addComponent(tree);
		
		ButtonComp okbt = new ButtonComp();
		constructOkButton(okbt);
		widget.getViewComponents().addComponent(okbt);
		
		ButtonComp cancelbt = new ButtonComp();
		constructCancelButton(cancelbt);
		widget.getViewComponents().addComponent(cancelbt);
		
		//创建定位
		TextComp locateComp = new TextComp();
		constructLocateText(locateComp);
		widget.getViewComponents().addComponent(locateComp);
		
		return pm;
	}
	
	protected void constructLocateText(TextComp locateComp){
		locateComp.setId(LOCATE_TEXT);
		locateComp.setText("定位");
		
		// 回车后过滤
		LuiEventConf enterEvent = KeyEvent.getOnEnterEvent();
		enterEvent.setId("locatetextKeyListener");
//		enterEvent.setOnserver(false);
//		enterEvent.setScript("doFilter(pageUI.getViewPart('main').getComponent('locatetext').getValue())");
		enterEvent.setMethod("onLocate");
		enterEvent.setEventType(KeyEvent.class.getSimpleName());
		EventSubmitRule submitRule = new EventSubmitRule();
		WidgetRule pwr = new WidgetRule();
		pwr.setId("main");
		DatasetRule dsRule = new DatasetRule();
		dsRule.setId(TREEDS);
		submitRule.addWidgetRule(pwr);
		pwr.addDsRule(dsRule);
		dsRule.setType(DatasetRule.TYPE_ALL_LINE);
		enterEvent.setSubmitRule(submitRule);
		
		locateComp.addEventConf(enterEvent);
		
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

	protected void constructDataset(Dataset ds) {
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
	}
	//生成定位
	private void generateLocatePanle(UIFlowvLayout flowvLayout) {
		UIFlowvPanel locateTextpanel = new UIFlowvPanel();
		locateTextpanel.setId("locateTextpanel");
		locateTextpanel.setHeight("30");
		locateTextpanel.setLeftPadding("20");
		locateTextpanel.setTopPadding("6");
		locateTextpanel.setBottomBorder("#");
		flowvLayout.addPanel(locateTextpanel);
		
		UITextField locateField = new UITextField();
		locateField.setId(LOCATE_TEXT);
		locateField.setWidth("220");
		locateField.setViewId("main");
		locateTextpanel.setElement(locateField);
	}
}
