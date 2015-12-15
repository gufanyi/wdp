package xap.lui.core.event;
import xap.lui.core.comps.ContextMenuComp;
import xap.lui.core.dataset.LuiParameter;
import xap.lui.core.listener.EventSubmitRule;
import xap.lui.core.listener.LuiEventConf;
/**
 * @author guoweic
 *
 */
public class ContextMenuEvent extends AbstractServerEvent<ContextMenuComp> {
	public static final String ON_MOUSE_OUT = "onmouseout";
	public static final String ON_CLOSE = "onclose";
	public static final String BEFORE_CLOSE = "beforeClose";
	public static final String ON_SHOW = "onshow";
	public static final String BEFORE_SHOW = "beforeShow";
	public ContextMenuEvent(ContextMenuComp webElement) {
		super(webElement);
	}
	public ContextMenuEvent() {}
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
	public static LuiEventConf getOnShowEvent() {
		LuiEventConf event = new LuiEventConf();
		event.setEventName(ON_SHOW);
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
	public static LuiEventConf getOnCloseEvent() {
		LuiEventConf event = new LuiEventConf();
		event.setEventName(ON_CLOSE);
		LuiParameter param = new LuiParameter();
		param.setName("simpleEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	public static LuiEventConf getOnMouseOutEvent() {
		LuiEventConf event = new LuiEventConf();
		event.setEventName(ON_MOUSE_OUT);
		LuiParameter param = new LuiParameter();
		param.setName("menuItemEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	@Override
	public String getJsClazz() {
		return "ContextMenuListener";
	}
}
