package xap.lui.psn.refence;
import xap.lui.core.builder.Window;
import xap.lui.core.comps.ButtonComp;
import xap.lui.core.comps.WebPartComp;
import xap.lui.core.event.AppRequestProcessor;
import xap.lui.core.event.MouseEvent;
import xap.lui.core.layout.UIButton;
import xap.lui.core.layout.UIFlowhLayout;
import xap.lui.core.layout.UIFlowhPanel;
import xap.lui.core.layout.UIFlowvLayout;
import xap.lui.core.layout.UIFlowvPanel;
import xap.lui.core.layout.UIPartComp;
import xap.lui.core.layout.UIPartMeta;
import xap.lui.core.layout.UIViewPart;
import xap.lui.core.listener.EventSubmitRule;
import xap.lui.core.listener.LuiEventConf;
import xap.lui.core.model.IUIPartMeta;
import xap.lui.core.model.PagePartMeta;
import xap.lui.core.model.ViewPartMeta;
/**
 * CSS样式自定义参照弹出的View，用来设置样式
 * 
 * @author liujmc
 * @date 2012-06-12
 */
public class CssStylePageModel extends Window {
	public static final String WIDGET_ID = "main";
	public static final String OK_BTN = "okbtn";
	public static final String CANCEL_BTN = "cancelbtn";
	/**
	 * 重写createUIMeta方法，创建CSS样式编辑器的展现
	 */
	@Override
	protected IUIPartMeta createUIMeta(PagePartMeta pm) {
		UIPartMeta uimeta = new UIPartMeta();
		UIViewPart uiWidget = new UIViewPart();
		uiWidget.setId(WIDGET_ID);
		UIPartMeta widgetUIMeta = new UIPartMeta();
		widgetUIMeta.setId(WIDGET_ID + "_um");
		uiWidget.setUimeta(widgetUIMeta);
		// 展示具体实现
		constructViewUI(widgetUIMeta);
		// uimeta.setReference(1);
		uimeta.setElement(uiWidget);
		return uimeta;
	}
	/**
	 * 构造具体样式布局
	 * 
	 * @param widgetUIMeta
	 */
	private void constructViewUI(UIPartMeta widgetUIMeta) {
		// 纵向布局，三个纵向Panel
		UIFlowvLayout fvlayout = new UIFlowvLayout();
		fvlayout.setId("fvlayout1");
		fvlayout.setViewId(WIDGET_ID);
		widgetUIMeta.setElement(fvlayout);
		// 第一个panel的高
		UIFlowvPanel fvp1 = new UIFlowvPanel();
		fvp1.setId("fvp1");
		fvp1.setHeight("10");
		fvp1.setViewId(WIDGET_ID);
		fvlayout.addPanel(fvp1);
		// 第二个panel要添加三个横向布局，其中一、三为布局，第二个添加form
		UIFlowvPanel fvp2 = new UIFlowvPanel();
		fvp2.setId("fvp2");
		fvp2.setViewId(WIDGET_ID);
		UIFlowhLayout fhlayout1 = new UIFlowhLayout();
		fhlayout1.setId("fvhlayout1");
		fhlayout1.setViewId(WIDGET_ID);
		UIFlowhPanel fhp1 = new UIFlowhPanel();
		fhp1.setId("fhp1");
		fhp1.setViewId(WIDGET_ID);
		fhp1.setWidth("10");
		// 填充具体控件
		UIFlowhPanel fhp2 = new UIFlowhPanel();
		fhp2.setId("fhp2");
		fhp2.setViewId(WIDGET_ID);
		// 构造WebPart展示控件
		UIPartComp uiPart = new UIPartComp();
		uiPart.setId("cssDesigner");
		uiPart.setViewId(WIDGET_ID);
		fhp2.setElement(uiPart);
		UIFlowhPanel fhp3 = new UIFlowhPanel();
		fhp3.setId("fhp3");
		fhp3.setViewId(WIDGET_ID);
		fhp3.setWidth("10");
		fhlayout1.addPanel(fhp1);
		fhlayout1.addPanel(fhp2);
		fhlayout1.addPanel(fhp3);
		fvp2.setElement(fhlayout1);
		fvlayout.addPanel(fvp2);
		// 第三个panel要添加五个横向布局，其中四、五放置按钮，其他为布局
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
	@Override
	protected PagePartMeta createPageMeta() {
		PagePartMeta pm = new PagePartMeta();
		// 设置页面请求处理类
		pm.setProcessorClazz(AppRequestProcessor.class.getName());
		pm.setId("cdref");
		// 构造Widget
		ViewPartMeta wd = new ViewPartMeta();
		wd.setId("main");
		// 设置Controller处理类
		wd.setController(CssStyleViewController.class.getName());
		// 构造WebPartComp
		WebPartComp webComp = new WebPartComp();
		webComp.setId("cssDesigner");
		webComp.setContentFetcher(CssStyleDesignerContentFetcherImpl.class.getName());
		wd.getViewComponents().addComponent(webComp);
		// 设置提交规则
		EventSubmitRule sr = new EventSubmitRule();
		// 确定按钮点击事件监听
		LuiEventConf saveEvent = new LuiEventConf();
		saveEvent.setEventName(MouseEvent.ON_CLICK);
		saveEvent.setMethod("onOkEvent");
		saveEvent.setEventType(MouseEvent.class.getSimpleName());
		saveEvent.setOnserver(false);
		StringBuffer scriptBug = new StringBuffer();
		// 得到编辑器生成的值
		scriptBug.append("var cssDeitorFrame = document.getElementById('csseditor');");
		// 获取css编辑器中textarea中的值
		scriptBug.append("var cssStr = cssDeitorFrame.contentWindow.document.getElementById('areaOutPut').value;");
		scriptBug.append("var proxy = $.serverproxy.getObj({async:false});");
		scriptBug.append("proxy.addParam('clc', '" + CssStyleViewController.class.getName() + "');");
		scriptBug.append("proxy.addParam('m_n', 'onOkEvent');");
		scriptBug.append("proxy.addParam('cssStr', cssStr);");
		scriptBug.append("var sbr = $.submitrule.getObj();");
		scriptBug.append("proxy.setSubmitRule(sbr);");
		scriptBug.append("proxy.execute();");
		saveEvent.setScript(scriptBug.toString());
		saveEvent.setSubmitRule(sr);
		// 创建确定按钮控件
		ButtonComp okbtn = new ButtonComp();
		okbtn.setId(OK_BTN);
		okbtn.setText("确定");
		okbtn.addEventConf(saveEvent);
		wd.getViewComponents().addComponent(okbtn);
		// 创建取消按钮事件监听
		LuiEventConf cancelEvent = new LuiEventConf();
		cancelEvent.setEventName(MouseEvent.ON_CLICK);
		cancelEvent.setOnserver(true);
		cancelEvent.setSubmitRule(sr);
		cancelEvent.setMethod("onCancelEvent");
		cancelEvent.setEventType(MouseEvent.class.getSimpleName());
		// 创建取消按钮控件
		ButtonComp cancelBtn = new ButtonComp();
		cancelBtn.setId(CANCEL_BTN);
		cancelBtn.setText("取消");
		cancelBtn.addEventConf(cancelEvent);
		wd.getViewComponents().addComponent(cancelBtn);
		// 将Widget添加到PageMeta中
		pm.addWidget(wd);
		return pm;
	}
}
