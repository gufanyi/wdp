package xap.lui.core.event;
import java.util.HashMap;
import java.util.Map;

import xap.lui.core.comps.TextComp;
import xap.lui.core.dataset.LuiParameter;
import xap.lui.core.listener.EventSubmitRule;
import xap.lui.core.listener.LuiEventConf;
public class KeyEvent extends AbstractServerEvent<TextComp> {
	public static final String ON_KEY_UP = "onKeyUp";
	public static final String ON_ENTER = "onEnter";
	public static final String ON_KEY_DOWN = "onKeyDown";
	
	public static final Map<String,String> JSPARAM = new HashMap<String,String>();
	static {
		JSPARAM.put(ON_KEY_UP, "keyEvent");
		JSPARAM.put(ON_ENTER, "keyEvent");
		JSPARAM.put(ON_KEY_DOWN, "keyEvent");
	}
	public KeyEvent(TextComp webElement) {
		super(webElement);
	}
	public KeyEvent() {}
	
	
	@Override
	public String getJsParam(String eventName) {
		return JSPARAM.get(eventName);
	}
	public static LuiEventConf getOnKeyDownEvent() {
		LuiEventConf event = new LuiEventConf();
		event.setEventName(ON_KEY_DOWN);
		LuiParameter param = new LuiParameter();
		param.setName("keyEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	public static LuiEventConf getOnEnterEvent() {
		LuiEventConf event = new LuiEventConf();
		event.setEventName(ON_ENTER);
		LuiParameter param = new LuiParameter();
		param.setName("keyEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	public static LuiEventConf getOnKeyUpEvent() {
		LuiEventConf event = new LuiEventConf();
		event.setEventName(ON_KEY_UP);
		LuiParameter param = new LuiParameter();
		param.setName("keyEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	@Override
	public String getJsClazz() {
		return "KeyListener";
	}
}
