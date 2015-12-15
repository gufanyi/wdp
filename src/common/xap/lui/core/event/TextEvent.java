package xap.lui.core.event;
import java.util.HashMap;
import java.util.Map;

import xap.lui.core.dataset.LuiParameter;
import xap.lui.core.listener.EventSubmitRule;
import xap.lui.core.listener.LuiEventConf;
/**
 * @author guoweic
 *
 */
public class TextEvent<T> extends AbstractServerEvent<T> {
	public static final String ON_SELECT = "onselect";
	public static final String VALUE_CHANGED = "valueChanged";
	
	public static final Map<String,String> JSPARAM = new HashMap<String,String>();
	static {
		JSPARAM.put(ON_SELECT, "simpleEvent");
		JSPARAM.put(VALUE_CHANGED, "valueChangeEvent");
	}
	public TextEvent(T webElement) {
		super(webElement);
	}
	public TextEvent() {}
	
	@Override
	public String getJsParam(String eventName) {
		return JSPARAM.get(eventName);
	}
	public static LuiEventConf getOnSelectEvent() {
		LuiEventConf event = new LuiEventConf();
		event.setEventName(ON_SELECT);
		LuiParameter param = new LuiParameter();
		param.setName("simpleEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	public static LuiEventConf getValueChangedEvent() {
		LuiEventConf event = new LuiEventConf();
		event.setEventName(VALUE_CHANGED);
		LuiParameter param = new LuiParameter();
		param.setName("valueChangeEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	@Override
	public String getJsClazz() {
		return "TextListener";
	}
}
