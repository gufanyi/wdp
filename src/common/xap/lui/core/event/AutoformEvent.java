package xap.lui.core.event;
import xap.lui.core.comps.WebComp;
import xap.lui.core.dataset.LuiParameter;
import xap.lui.core.listener.EventSubmitRule;
import xap.lui.core.listener.LuiEventConf;
/**
 * @author guoweic
 *
 */
public class AutoformEvent extends AbstractServerEvent<WebComp> {
	public static final String IN_ACTIVE = "inActive";
	public static final String GET_VALUE = "getValue";
	public static final String ACTIVE = "active";
	public static final String SET_VALUE = "setValue";
	// TODO
	public AutoformEvent(WebComp webElement) {
		super(webElement);
	}
	public AutoformEvent() {
		super();
	}
	public static LuiEventConf getInActiveEvent() {
		LuiEventConf event = new LuiEventConf();
		event.setEventName(IN_ACTIVE);
		LuiParameter param = new LuiParameter();
		param.setName("simpleEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	public static LuiEventConf getGetValueEvent() {
		LuiEventConf event = new LuiEventConf();
		event.setEventName(GET_VALUE);
		LuiParameter param = new LuiParameter();
		param.setName("simpleEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	public static LuiEventConf getActiveEvent() {
		LuiEventConf event = new LuiEventConf();
		event.setEventName(ACTIVE);
		LuiParameter param = new LuiParameter();
		param.setName("simpleEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	public static LuiEventConf getSetValueEvent() {
		LuiEventConf event = new LuiEventConf();
		event.setEventName(SET_VALUE);
		LuiParameter param = new LuiParameter();
		param.setName("autoformSetValueEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	@Override
	public String getJsClazz() {
		return "AutoformListener";
	}
}
