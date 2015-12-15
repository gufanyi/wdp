package xap.lui.core.mock;

import xap.lui.core.builder.Window;
import xap.lui.core.comps.ButtonComp;
import xap.lui.core.comps.GridColumn;
import xap.lui.core.comps.GridComp;
import xap.lui.core.constant.StringDataTypeConst;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.Field;
import xap.lui.core.event.AppRequestProcessor;
import xap.lui.core.event.DatasetEvent;
import xap.lui.core.event.MouseEvent;
import xap.lui.core.layout.UIButton;
import xap.lui.core.layout.UIFlowhLayout;
import xap.lui.core.layout.UIFlowhPanel;
import xap.lui.core.layout.UIFlowvLayout;
import xap.lui.core.layout.UIFlowvPanel;
import xap.lui.core.layout.UIGridComp;
import xap.lui.core.layout.UIPartMeta;
import xap.lui.core.layout.UIViewPart;
import xap.lui.core.listener.DatasetRule;
import xap.lui.core.listener.EventSubmitRule;
import xap.lui.core.listener.LuiEventConf;
import xap.lui.core.listener.WidgetRule;
import xap.lui.core.model.IUIPartMeta;
import xap.lui.core.model.PagePartMeta;
import xap.lui.core.model.ViewPartMeta;

/**
 * 自定义参照的表型参照
 * @author zhangxya
 *
 */
public class MockGridPageModel extends Window {
	
	private static final String GRID = "grid";
	private static final String GRIDDS = "masterDs";
	private static final String CANCELBT = "cancelbt";
	private static final String OKBT = "okbt";
	private static final String WIDGET_ID = "main";
	private static final String ID_FIELD = "id";
	private static final String LOAD_CODE = "code";
	private static final String LOAD_NAME = "name";
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
		
		UIFlowvPanel flvPanel1 = new UIFlowvPanel();
		flvPanel1.setId("flowvp1");
		flvLayout.addPanel(flvPanel1);
		
		UIGridComp uiGrid = new UIGridComp();
		uiGrid.setViewId(WIDGET_ID);
		uiGrid.setId(GRID);
		flvPanel1.setElement(uiGrid);
		
		UIFlowvPanel flvPanel2 = new UIFlowvPanel();
		flvPanel2.setId("flowvp2");
		flvPanel2.setHeight("30");
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
		flhP2.setWidth("120");
		flhLayout.addPanel(flhP2);
		
		UIFlowhPanel flhP3 = new UIFlowhPanel();
		flhP3.setId("flowhp3");
		flhP3.setWidth("120");
		flhLayout.addPanel(flhP3);
		
		flvPanel2.setElement(flhLayout);
		UIButton okbt = new UIButton();
		okbt.setViewId(WIDGET_ID);
		okbt.setId(OKBT);
		flhP2.setElement(okbt);
		
		UIButton cancelbt = new UIButton();
		cancelbt.setViewId(WIDGET_ID);
		cancelbt.setId(CANCELBT);
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
		
		Dataset ds = new Dataset(GRIDDS);
		ds.setLazyLoad(false);
		constructDataset(ds);
		widget.getViewModels().addDataset(ds);
		
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

	protected void constructWidget(ViewPartMeta widget) {
	}

	/**
	 * 构建grid
	 * @param tree
	 */
	protected void constructGrid(GridComp grid) {
		GridColumn columnId = generateGridColumn(ID_FIELD);
		grid.addColumn(columnId);
		
		GridColumn columnCode = generateGridColumn(LOAD_CODE);
		grid.addColumn(columnCode);
		
		GridColumn columnName = generateGridColumn(LOAD_NAME);
		grid.addColumn(columnName);
		
	}

	private GridColumn generateGridColumn(String id) {
		GridColumn columnId = new GridColumn();
		columnId.setId(id);
		columnId.setText(id);
		columnId.setWidth(120);
		columnId.setDataType(StringDataTypeConst.STRING);
		columnId.setVisible(true);
		columnId.setEdit(false);
		return columnId;
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
		dsRule.setId(GRIDDS);
		submitRule.addWidgetRule(pwr);
		pwr.addDsRule(dsRule);
		event.setSubmitRule(submitRule);
		
		
		EventSubmitRule parentSum = new EventSubmitRule();
		WidgetRule pwidgetRule = new WidgetRule();
		parentSum.addWidgetRule(pwidgetRule);
		pwidgetRule.setId("main");
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



	protected void constructDataset(Dataset ds) {
		Field idField = new Field();
		idField.setId(ID_FIELD);
		idField.setText("ID");
		ds.addField(idField);
		
		Field labelField = new Field();
		labelField.setId(LOAD_CODE);
		labelField.setText("Code");
		ds.addField(labelField);
		
		Field loadField = new Field();
		loadField.setId(LOAD_NAME);
		loadField.setText("name");
		ds.addField(loadField);
		constructDsLoadEvent(ds);
	}

	/**
	 * 创建ds事件加载
	 * @param ds
	 */
	private void constructDsLoadEvent(Dataset ds) {
		LuiEventConf event = new LuiEventConf();
		event.setEventName(DatasetEvent.ON_DATA_LOAD);
		event.setMethod("dataLoad");
		event.setEventType(DatasetEvent.class.getSimpleName());
		ds.addEventConf(event);
	}
}
