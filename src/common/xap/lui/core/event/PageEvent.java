package xap.lui.core.event;
import java.util.HashMap;
import java.util.Map;

import xap.lui.core.dataset.LuiParameter;
import xap.lui.core.listener.EventSubmitRule;
import xap.lui.core.listener.LuiEventConf;
import xap.lui.core.model.PagePartMeta;
public class PageEvent extends AbstractServerEvent<PagePartMeta> {
	public static final String ON_CLOSED = "onClosed";
	public static final String ON_CLOSING = "onClosing";
	public static final String BEFORE_ACTIVE = "beforeActive";
	public static final String AFTER_PAGE_INIT = "afterPageInit";
	
	public static final Map<String,String> JSPARAM = new HashMap<String,String>();
	static {
		JSPARAM.put(ON_CLOSED, "");
		JSPARAM.put(ON_CLOSING, "");
		JSPARAM.put(BEFORE_ACTIVE, "");
		JSPARAM.put(AFTER_PAGE_INIT, "");
	}
	
	public PageEvent(PagePartMeta webElement) {
		super(webElement);
	}
	public PageEvent() {
		super();
	}
	
	@Override
	public String getJsParam(String eventName) {
		return JSPARAM.get(eventName);
	}
	public static LuiEventConf getAfterPageInitEvent() {
		LuiEventConf event = new LuiEventConf();
		event.setEventName(AFTER_PAGE_INIT);
		LuiParameter param = new LuiParameter();
		param.setName("");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	public static LuiEventConf getBeforeActiveEvent() {
		LuiEventConf event = new LuiEventConf();
		event.setEventName(BEFORE_ACTIVE);
		LuiParameter param = new LuiParameter();
		param.setName("");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	public static LuiEventConf getOnClosingEvent() {
		LuiEventConf event = new LuiEventConf();
		event.setEventName(ON_CLOSING);
		LuiParameter param = new LuiParameter();
		param.setName("");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	public static LuiEventConf getOnClosedEvent() {
		LuiEventConf event = new LuiEventConf();
		event.setEventName(ON_CLOSED);
		LuiParameter param = new LuiParameter();
		param.setName("");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	@Override
	public String getJsClazz() {
		return "PageListener";
	}
}
