package xap.lui.core.event;
import xap.lui.core.dataset.LuiParameter;
import xap.lui.core.layout.UITabComp;
import xap.lui.core.listener.EventSubmitRule;
import xap.lui.core.listener.LuiEventConf;
/**
 *
 */
public class TabEvent extends AbstractServerEvent<UITabComp> {
	public static final String AFTER_ITEM_INIT = "afterItemInit";
	public static final String BEFORE_ITEM_INIT = "beforeItemInit";
	public static final String AFTER_CLOSE_ITEM = "afterCloseItem";
	public static final String BEFORE_ACTIVED_CHANGE = "beforeActivedTabItemChange";
	public static final String AFTER_ACTIVED_CHANGE = "afterActivedTabItemChange";
	public TabEvent(UITabComp uiElement) {
		super(uiElement);
	}
	public TabEvent() {}
	public static LuiEventConf getBeforeCloseItemEvent() {
		LuiEventConf event = new LuiEventConf();
		event.setEventName(BEFORE_ITEM_INIT);
		LuiParameter param = new LuiParameter();
		param.setName("tabItemEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	public static LuiEventConf getAfterCloseItemEvent() {
		LuiEventConf event = new LuiEventConf();
		event.setEventName(AFTER_CLOSE_ITEM);
		LuiParameter param = new LuiParameter();
		param.setName("tabItemEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	public static LuiEventConf getAfterItemInitEvent() {
		LuiEventConf event = new LuiEventConf();
		event.setEventName(AFTER_ITEM_INIT);
		LuiParameter param = new LuiParameter();
		param.setName("tabItemEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	public static LuiEventConf getBeforeActivedTabItemChange() {
		LuiEventConf event = new LuiEventConf();
		event.setEventName(BEFORE_ACTIVED_CHANGE);
		LuiParameter param = new LuiParameter();
		param.setName("tabItemChangeEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	public static LuiEventConf getAfterActivedTabItemChange() {
		LuiEventConf event = new LuiEventConf();
		event.setEventName(AFTER_ACTIVED_CHANGE);
		LuiParameter param = new LuiParameter();
		param.setName("tabItemEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	@Override
	public String getJsClazz() {
		return "TabListener";
	}
}
