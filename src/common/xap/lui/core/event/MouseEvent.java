package xap.lui.core.event;
import java.util.HashMap;
import java.util.Map;

import xap.lui.core.dataset.LuiParameter;
import xap.lui.core.listener.EventSubmitRule;
import xap.lui.core.listener.LuiEventConf;
public class MouseEvent<T> extends AbstractServerEvent<T> {
	public static final String ON_MOUSE_OUT = "onmouseout";
	public static final String ON_MOUSE_OVER = "onmouseover";
	public static final String ON_DB_CLICK = "ondbclick";
	public static final String ON_CLICK = "onclick";
	
	public static final Map<String,String> JSPARAM = new HashMap<String,String>();
	static {
		JSPARAM.put(ON_MOUSE_OUT, "mouseEvent");
		JSPARAM.put(ON_MOUSE_OVER, "mouseEvent");
		JSPARAM.put(ON_DB_CLICK, "mouseEvent");
		JSPARAM.put(ON_CLICK, "mouseEvent");
	}
	
	public MouseEvent(T webElement) {
		super(webElement);
	}
	public MouseEvent() {}
	
	@Override
	public String getJsParam(String eventName) {
		return JSPARAM.get(eventName);
	}
	public static LuiEventConf getOnClickEvent() {
		LuiEventConf event = new LuiEventConf();
		event.setEventName(ON_CLICK);
		LuiParameter param = new LuiParameter();
		param.setName("mouseEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	public static LuiEventConf getOnDbClickEvent() {
		LuiEventConf event = new LuiEventConf();
		event.setEventName(ON_DB_CLICK);
		LuiParameter param = new LuiParameter();
		param.setName("mouseEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	public static LuiEventConf getOnMouseOverEvent() {
		LuiEventConf event = new LuiEventConf();
		event.setEventName(ON_MOUSE_OVER);
		LuiParameter param = new LuiParameter();
		param.setName("mouseEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	public static LuiEventConf getOnMouseOutEvent() {
		LuiEventConf event = new LuiEventConf();
		event.setEventName(ON_MOUSE_OUT);
		LuiParameter param = new LuiParameter();
		param.setName("mouseEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	@Override
	public String getJsClazz() {
		return "MouseEvent";
	}
}
