package xap.lui.psn.refence;
import xap.lui.core.comps.ButtonComp;
import xap.lui.core.comps.WebPartComp;
import xap.lui.core.event.AppRequestProcessor;
import xap.lui.core.event.MouseEvent;
import xap.lui.core.listener.EventSubmitRule;
import xap.lui.core.listener.LuiEventConf;
import xap.lui.core.model.PagePartMeta;
import xap.lui.core.model.ViewPartMeta;
public class BorderCssStylePageModel extends CssStylePageModel {
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
		wd.setController(BorderCssStyleViewController.class.getName());
		// 构造WebPartComp
		WebPartComp webComp = new WebPartComp();
		webComp.setId("cssDesigner");
		webComp.setContentFetcher(BorderCssStyleContentFetcherImpl.class.getName());
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
		scriptBug.append("var bordereditor = document.getElementById('bordereditor');\n");
		scriptBug.append("var borderstyle = bordereditor.contentWindow.document.getElementById('borderstyle').value;\n");
		scriptBug.append("var selectedIdsStr = bordereditor.contentWindow.document.getElementById('selectedIdsStr').value;\n");
		scriptBug.append("var proxy = $.serverproxy.getObj({async:false});\n");
		scriptBug.append("proxy.addParam('clc', '" + BorderCssStyleViewController.class.getName() + "');\n");
		scriptBug.append("proxy.addParam('m_n', 'onOkEvent');\n");
		scriptBug.append("proxy.addParam('borderstyle', borderstyle);\n");
		scriptBug.append("proxy.addParam('selectedids', selectedIdsStr);\n");
		scriptBug.append("var sbr = $.submitrule.getObj();\n");
		scriptBug.append("proxy.setSubmitRule(sbr);\n");
		scriptBug.append("proxy.execute();\n");
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
		cancelEvent.setEventType(MouseEvent.class.getName());
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
