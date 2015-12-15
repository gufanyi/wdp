package xap.lui.core.event;
import xap.lui.core.dataset.LuiParameter;
import xap.lui.core.listener.EventSubmitRule;
import xap.lui.core.listener.LuiEventConf;
public class FocusEvent<T> extends AbstractServerEvent<T> {
	public static final String ON_BLUR = "onBlur";
	public static final String ON_FOCUS = "onFocus";
	public FocusEvent(T webElement) {
		super(webElement);
	}
	public FocusEvent() {}
	public static LuiEventConf getOnFocusEvent() {
		LuiEventConf event = new LuiEventConf();
		event.setEventName(ON_FOCUS);
		LuiParameter param = new LuiParameter();
		param.setName("focusEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	public static LuiEventConf getOnBlurEvent() {
		LuiEventConf event = new LuiEventConf();
		event.setEventName(ON_BLUR);
		LuiParameter param = new LuiParameter();
		param.setName("focusEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	@Override
	public String getJsClazz() {
		return "FocusListener";
	}
}
