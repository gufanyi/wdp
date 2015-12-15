package xap.lui.core.event;
import xap.lui.core.dataset.LuiParameter;
import xap.lui.core.listener.EventSubmitRule;
import xap.lui.core.listener.LuiEventConf;
import xap.lui.core.model.ViewPartMeta;
/**
 * @author guoweic
 *
 */
public class ViewPartEvent extends AbstractServerEvent<ViewPartMeta> {
	public static final String BEFORE_INIT_DATA = "beforeInitData";
	public static final String ON_INITIALIZING = "onInitializing";
	public static final String ON_CLOSED = "onClosed";
	public ViewPartEvent(ViewPartMeta webElement) {
		super(webElement);
	}
	public ViewPartEvent() {}
	public static LuiEventConf getOnInitializingEvent() {
		LuiEventConf event = new LuiEventConf();
		event.setEventName(ON_INITIALIZING);
		LuiParameter param = new LuiParameter();
		param.setName("event");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	public static LuiEventConf getBeforeInitDataEvent() {
		LuiEventConf event = new LuiEventConf();
		event.setEventName(BEFORE_INIT_DATA);
		LuiParameter param = new LuiParameter();
		param.setName("loader");
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
		return "WidgetListener";
	}
}
