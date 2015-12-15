package xap.lui.core.event;
import xap.lui.core.dataset.LuiParameter;
import xap.lui.core.listener.EventSubmitRule;
import xap.lui.core.listener.LuiEventConf;
import xap.lui.core.model.ViewPartMeta;
/**
 * @author guoweic
 *
 */
public class DialogEvent extends AbstractServerEvent<ViewPartMeta> {
	public static final String ON_CLOSE = "onclose";
	public static final String AFTER_CLOSE = "afterClose";
	public static final String BEFORE_CLOSE = "beforeClose";
	public static final String BEFORE_SHOW = "beforeShow";
	public static final String ON_CANCEL = "onCancel";
	public static final String ON_OK = "onOk";
	public DialogEvent(ViewPartMeta webElement) {
		super(webElement);
	}
	public DialogEvent() {}
	public static LuiEventConf getOnOkEvent() {
		LuiEventConf event = new LuiEventConf();
		event.setEventName(ON_OK);
		LuiParameter param = new LuiParameter();
		param.setName("simpleEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		event.setEventType(DialogEvent.class.getSimpleName());
		return event;
	}
	public static LuiEventConf getOnCancelEvent() {
		LuiEventConf event = new LuiEventConf();
		event.setEventName(ON_CANCEL);
		LuiParameter param = new LuiParameter();
		param.setName("simpleEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	public static LuiEventConf getBeforeShowEvent() {
		LuiEventConf event = new LuiEventConf();
		event.setEventName(BEFORE_SHOW);
		LuiParameter param = new LuiParameter();
		param.setName("simpleEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	public static LuiEventConf getBeforeCloseEvent() {
		LuiEventConf event = new LuiEventConf();
		event.setEventName(BEFORE_CLOSE);
		LuiParameter param = new LuiParameter();
		param.setName("simpleEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	public static LuiEventConf getAfterCloseEvent() {
		LuiEventConf event = new LuiEventConf();
		event.setEventName(AFTER_CLOSE);
		LuiParameter param = new LuiParameter();
		param.setName("simpleEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	public static LuiEventConf getOncloseEvent() {
		LuiEventConf event = new LuiEventConf();
		event.setEventName(ON_CLOSE);
		LuiParameter param = new LuiParameter();
		param.setName("simpleEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	@Override
	public String getJsClazz() {
		return "DialogEvent";
	}
}
