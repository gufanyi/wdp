package xap.lui.core.event;

import xap.lui.core.dataset.LuiParameter;
import xap.lui.core.layout.UICardLayout;
import xap.lui.core.listener.EventSubmitRule;
import xap.lui.core.listener.LuiEventConf;

/**
 * @author guoweic
 *
 */
public class CardEvent extends AbstractServerEvent<UICardLayout> {
	
	public static final String BEFORE_PAGE_CHANGE = "beforePageChange";
	public static final String AFTER_PAGE_INIT = "afterPageInit";
	public static final String BEFORE_PAGE_INIT = "beforePageInit";

	public CardEvent(UICardLayout webElement) {
		super(webElement);
	}
	public CardEvent() {}
	public static LuiEventConf getBeforePageInitEvent() {
		LuiEventConf event = new LuiEventConf();
		event.setEventName(BEFORE_PAGE_INIT);
		LuiParameter param = new LuiParameter();
		param.setName("cardEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	public static LuiEventConf getAfterPageInitEvent() {
		LuiEventConf event = new LuiEventConf();
		event.setEventName(AFTER_PAGE_INIT);
		LuiParameter param = new LuiParameter();
		param.setName("cardEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	public static LuiEventConf getBeforePageChangeEvent() {
		LuiEventConf event = new LuiEventConf();
		event.setEventName(BEFORE_PAGE_CHANGE);
		LuiParameter param = new LuiParameter();
		param.setName("cardEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	@Override
	public String getJsClazz() {
		return "CardListener";
	}



}
