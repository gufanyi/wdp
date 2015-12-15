package xap.lui.psn.refence;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import xap.lui.core.builder.Window;
import xap.lui.core.comps.ButtonComp;
import xap.lui.core.comps.FormComp;
import xap.lui.core.comps.FormElement;
import xap.lui.core.constant.EditorTypeConst;
import xap.lui.core.constant.StringDataTypeConst;
import xap.lui.core.dataset.ComboData;
import xap.lui.core.dataset.DataItem;
import xap.lui.core.dataset.DataList;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.Field;
import xap.lui.core.dataset.Parameter;
import xap.lui.core.event.AppRequestProcessor;
import xap.lui.core.event.DatasetEvent;
import xap.lui.core.event.MouseEvent;
import xap.lui.core.layout.UIButton;
import xap.lui.core.layout.UIFlowhLayout;
import xap.lui.core.layout.UIFlowhPanel;
import xap.lui.core.layout.UIFlowvLayout;
import xap.lui.core.layout.UIFlowvPanel;
import xap.lui.core.layout.UIFormComp;
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
 * 边框模板样式的弹出view，用来设置边框属性
 * 
 * @author wupeng1
 */
public class BorderStylePageModel extends Window {

	public static final String WIDGET_ID = "main";
	public static final String FORM_ID = "borderform";
	public static final String OK_BTN = "okbtn";
	public static final String CANCEL_BTN = "cancelbtn";
	public static final String DS_ID = "masterDs";
	
	/**
	 * 用代码创建uimeta
	 * @param pm
	 * @return
	 */
	@Override
	protected IUIPartMeta createUIMeta(PagePartMeta pm) {
		UIPartMeta uimeta = new UIPartMeta();
		
		UIViewPart uiWidget = new UIViewPart();
		uiWidget.setId(WIDGET_ID);
		UIPartMeta wuimeta = new UIPartMeta();
		wuimeta.setId(WIDGET_ID + "_um");
		wuimeta.setFlowmode(false);
		uiWidget.setUimeta(wuimeta);
		constructViewUI(wuimeta);
		
		uimeta.setIsReference(1);
		uimeta.setElement(uiWidget);
		return uimeta;
	}
	/**
	 * 代码创建view
	 * @param wuimeta
	 */
	private void constructViewUI(UIPartMeta wuimeta) {
		
		//纵向布局，三个纵向panel
		UIFlowvLayout fvlayout = new UIFlowvLayout();
		fvlayout.setId("fvlayout1");
		fvlayout.setViewId(WIDGET_ID);
		wuimeta.setElement(fvlayout);
		
		//第一个panel的高为30
		UIFlowvPanel fvp1 = new UIFlowvPanel();
		fvp1.setId("fvp1");
		fvp1.setHeight("30");
		fvp1.setViewId(WIDGET_ID);
		fvlayout.addPanel(fvp1);
		
		//第二个panel要添加三个横向布局，其中一、三为布局，第二个添加form
		UIFlowvPanel fvp2 = new UIFlowvPanel();
		fvp2.setId("fvp2");
		fvp2.setViewId(WIDGET_ID);
		
			UIFlowhLayout fhlayout1 = new UIFlowhLayout();
			fhlayout1.setId("fvhlayout1");
			fhlayout1.setViewId(WIDGET_ID);
				
				UIFlowhPanel fhp1 = new UIFlowhPanel();
				fhp1.setId("fhp1");
				fhp1.setViewId(WIDGET_ID);
				fhp1.setWidth("50");
				
				UIFlowhPanel fhp2 = new UIFlowhPanel();
				fhp2.setId("fhp2");
				fhp2.setViewId(WIDGET_ID);
				UIFormComp uifrm = new UIFormComp();
				uifrm.setId(FORM_ID);
				uifrm.setViewId(WIDGET_ID);
				fhp2.setElement(uifrm);
				
				UIFlowhPanel fhp3 = new UIFlowhPanel();
				fhp3.setId("fhp3");
				fhp3.setViewId(WIDGET_ID);
				fhp3.setWidth("50");
				
			fhlayout1.addPanel(fhp1);
			fhlayout1.addPanel(fhp2);
			fhlayout1.addPanel(fhp3);
			
		fvp2.setElement(fhlayout1);
		fvlayout.addPanel(fvp2);
		
		//第三个panel要添加五个横向布局，其中四、五放置按钮，其他为布局
		UIFlowvPanel fvp3 = new UIFlowvPanel();
		fvp3.setId("fvp3");
		fvp3.setViewId(WIDGET_ID);
		fvp3.setHeight("45");
			UIFlowhLayout fhlayout2 = new UIFlowhLayout();
			fhlayout2.setId("fhlayout2");
			fhlayout2.setViewId(WIDGET_ID);
				UIFlowhPanel fhp01 = new UIFlowhPanel();
				fhp01.setId("fhp01");
				fhp01.setViewId(WIDGET_ID);
				
				UIFlowhPanel fhp02 = new UIFlowhPanel();
				fhp02.setId("fhp02");
				fhp02.setViewId(WIDGET_ID);
				
				UIFlowhPanel fhp03 = new UIFlowhPanel();
				fhp03.setId("fhp03");
				fhp03.setViewId(WIDGET_ID);
				
				UIFlowhPanel fhp04 = new UIFlowhPanel();
				fhp04.setId("fhp04");
				fhp04.setWidth("84");
				fhp04.setTopPadding("12");
				fhp04.setViewId(WIDGET_ID);
					UIButton okbt = new UIButton();
					okbt.setId(OK_BTN);
					okbt.setViewId(WIDGET_ID);
					okbt.setWidth("74");
					okbt.setClassName("blue_button_div");
				fhp04.setElement(okbt);
				
				UIFlowhPanel fhp05 = new UIFlowhPanel();
				fhp05.setId("fhp05");
				fhp05.setWidth("94");
				fhp05.setTopPadding("12");				
				fhp05.setViewId(WIDGET_ID);
					UIButton cancelBtn = new UIButton();
					cancelBtn.setId(CANCEL_BTN);
					cancelBtn.setViewId(WIDGET_ID);
					cancelBtn.setWidth("74");
				fhp05.setElement(cancelBtn);				
				
			fhlayout2.addPanel(fhp01);
			fhlayout2.addPanel(fhp02);
			fhlayout2.addPanel(fhp03);
			fhlayout2.addPanel(fhp04);
			fhlayout2.addPanel(fhp05);
		fvp3.setElement(fhlayout2);
		fvlayout.addPanel(fvp3);
	}

	/**
	 * 代码创建PageMeta
	 * @return
	 */
	@Override
	protected PagePartMeta createPageMeta() {
		PagePartMeta pm = new PagePartMeta();
		pm.setProcessorClazz(AppRequestProcessor.class.getName());
		
		pm.setId("cdref");
		ViewPartMeta wd = new ViewPartMeta();
		wd.setId("main");
		
		wd.setController(BorderStyleViewController.class.getName());
		
		FormComp form = new FormComp();
		form.setId(FORM_ID);
		form.setColumn((1));
		form.setDataset(DS_ID);
		form.setRenderType(2);
		form.setEnabled(true);
		form.setEleWidth(150);
		
		Dataset ds = new Dataset(DS_ID);
		ds.setLazyLoad(false);
		ds.setEdit(true);
		wd.getViewModels().addDataset(ds);
		
		//添加下拉数据集
		ComboData comboData_borderColor = new DataList();
		comboData_borderColor.setId("comboDataset_borderColor");
		String[] colors = "black,red,green,blue,gray".split(",");
		String colorStr = "黑色,红色,绿色,蓝色,灰色";
		String[] colorTexts = colorStr.split(",");
		for (int i = 0; i < colors.length; i++) {
			DataItem item = new DataItem();
			item.setText(colorTexts[i]);
			item.setValue(colors[i]);
			comboData_borderColor.addDataItem(item);
		}
		wd.getViewModels().addComboData(comboData_borderColor);
		
		ComboData comboData_borderStyle = new DataList();
		comboData_borderStyle.setId("comboDataset_borderStyle");
		//String[] styles = "none,dotted,dashed,solid,double,groove,ridge,inset,window-inset,outset".split(",");
		String[] styles = "none,dotted,dashed,solid,double".split(",");
		String styleStr = "无,点,虚线,实线,双线";
		String[] styleTexts = styleStr.split(",");
		for (int i = 0; i < styles.length; i++) {
			DataItem item = new DataItem();
			item.setText(styleTexts[i]);
			item.setValue(styles[i]);
			comboData_borderStyle.addDataItem(item);
		}
		wd.getViewModels().addComboData(comboData_borderStyle);
		
		String key1 = "borderWidth";
		String label1 = "宽度(px)：";
		Field field1 = new Field(key1);
		field1.setText(label1);
		field1.setDataType(StringDataTypeConst.STRING);
		ds.addField(field1);		
		
		FormElement formElement1 = new FormElement(key1);
		formElement1.setField(key1);
		formElement1.setLabel(label1);
		formElement1.setDataType(StringDataTypeConst.INT);
		formElement1.setEditorType(EditorTypeConst.INTEGERTEXT);
		formElement1.setEdit(true);
		formElement1.setMaxLength("9");
		form.addElement(formElement1);	
		
		String key3 = "borderStyle";
		String label3 = "边框样式：";
		Field field3 = new Field(key3);
		field3.setText(label3);
		field3.setDataType(StringDataTypeConst.STRING);
		ds.addField(field3);
		
		FormElement formElement3 = new FormElement(key3);
		formElement3.setField(key3);
		formElement3.setLabel(label3);
		formElement3.setDataType(StringDataTypeConst.STRING);
		formElement3.setEditorType(EditorTypeConst.COMBODATA);
		formElement3.setEdit(true);
		formElement3.setRefComboData(comboData_borderStyle.getId());
		form.addElement(formElement3);		
		
		String key2 = "borderColor";
		String label2 = "边框颜色：";
		Field field2 = new Field(key2);
		field2.setText(label2);
		field2.setDataType(StringDataTypeConst.STRING);
		ds.addField(field2);
		
		FormElement formElement2 = new FormElement(key2);
		formElement2.setField(key2);
		formElement2.setLabel(label2);
		formElement2.setDataType(StringDataTypeConst.STRING);
		formElement2.setEditorType(EditorTypeConst.COMBODATA);
		formElement2.setEdit(true);
		formElement2.setRefComboData(comboData_borderColor.getId());
		form.addElement(formElement2);
		
		wd.getViewComponents().addComponent(form);
		
		//提交规则
		EventSubmitRule sr = new EventSubmitRule();
		Map<String, Parameter> params = new HashMap<String,Parameter>();
		params.put("nodeId", new Parameter("nodeId",""));
		sr.setParams(params);
		WidgetRule wr = new WidgetRule();
		wr.setId(WIDGET_ID);
		DatasetRule dsRule = new DatasetRule();
		dsRule.setType(DatasetRule.TYPE_CURRENT_LINE);
		dsRule.setId(DS_ID);
		wr.addDsRule(dsRule);
		sr.addWidgetRule(wr);
		
		
		LuiEventConf event = new LuiEventConf();
		event.setEventName(DatasetEvent.ON_DATA_LOAD);
		event.setMethod("onDataLoad");
		event.setSubmitRule(sr);
		event.setEventType(DatasetEvent.class.getSimpleName());
		ds.addEventConf(event);
		
		
		//确定按钮事件
		LuiEventConf saveEvent = new LuiEventConf();
		saveEvent.setEventName(MouseEvent.ON_CLICK);
		saveEvent.setMethod("onOkEvent");
		saveEvent.setEventType(MouseEvent.class.getSimpleName());
		saveEvent.setOnserver(true);
		saveEvent.setSubmitRule(sr);
		
		 
		ButtonComp okbtn = new ButtonComp();
		okbtn.setId(OK_BTN);
		okbtn.setText("确定");
		okbtn.addEventConf(saveEvent);
		wd.setController(BorderStyleViewController.class.getName());
		wd.getViewComponents().addComponent(okbtn);
		 
		//取消按钮事件
		LuiEventConf cancelEvent = new LuiEventConf();
		cancelEvent.setEventName(MouseEvent.ON_CLICK);
		cancelEvent.setOnserver(true);
		cancelEvent.setSubmitRule(sr);
		cancelEvent.setMethod("onCancelEvent");
		cancelEvent.setEventType(MouseEvent.class.getSimpleName());
		
		ButtonComp cancelBtn = new ButtonComp();
		cancelBtn.setId(CANCEL_BTN);
		cancelBtn.setText("取消");
		cancelBtn.addEventConf(cancelEvent);
		wd.getViewComponents().addComponent(cancelBtn);
		
		pm.addWidget(wd);
		
		return pm;
	}

	@SuppressWarnings("unused")
	private Map<String,String> getExtAuthFields() {
		Map<String,String> map = new LinkedHashMap<String,String>();
//		map.put("topPadding", NCLangRes4VoTransl.getNCLangRes().getStrByID("pa", "LayoutPanelInfo-000000")+"："/*上边距*/);
//		map.put("bottomPadding", NCLangRes4VoTransl.getNCLangRes().getStrByID("pa", "LayoutPanelInfo-000001")+"："/*下边距*/);
//		map.put("leftPadding", NCLangRes4VoTransl.getNCLangRes().getStrByID("pa", "BorderStylePageModel-000009")+"："/*左边距*/);
//		map.put("rightPadding", NCLangRes4VoTransl.getNCLangRes().getStrByID("pa", "BorderStylePageModel-000002")+"："/*右边距*/);
//		map.put("topBorder", NCLangRes4VoTransl.getNCLangRes().getStrByID("pa", "BorderStylePageModel-000005")+"："/*上边框*/);
//		map.put("bottomBorder", NCLangRes4VoTransl.getNCLangRes().getStrByID("pa", "BorderStylePageModel-000006")+"："/*下边框*/);
//		map.put("leftBorder", NCLangRes4VoTransl.getNCLangRes().getStrByID("pa", "BorderStylePageModel-000003")+"："/*左边框*/);
//		map.put("rightBorder", NCLangRes4VoTransl.getNCLangRes().getStrByID("pa", "BorderStylePageModel-000004")+"："/*右边框*/);
//		map.put("border", NCLangRes4VoTransl.getNCLangRes().getStrByID("pa", "GridLayoutInfo-000000")+"："/*边框*/);
		map.put("borderWidth", "边框宽度");
		map.put("borderColor", "边框颜色");
		map.put("borderStyle", "边框样式");
		return map;
	}
	
	
	 
}
