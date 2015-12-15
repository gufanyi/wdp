package xap.lui.core.event;
import xap.lui.core.comps.ListComp;
import xap.lui.core.dataset.LuiParameter;
import xap.lui.core.listener.EventSubmitRule;
import xap.lui.core.listener.LuiEventConf;
public class ListEvent extends AbstractServerEvent<ListComp> {
	public static final String DB_VALUE_CHANGE = "dbValueChange";
	public static final String VALUE_CHANGED = "valueChanged";
	public ListEvent(ListComp webElement) {
		super(webElement);
	}
	public ListEvent() {}
	public static LuiEventConf getValueChangedEvent() {
		LuiEventConf event = new LuiEventConf();
		event.setEventName(VALUE_CHANGED);
		LuiParameter param = new LuiParameter();
		param.setName("listEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	public static LuiEventConf getDbValueChangeEvent() {
		LuiEventConf event = new LuiEventConf();
		event.setEventName(DB_VALUE_CHANGE);
		LuiParameter param = new LuiParameter();
		param.setName("listEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	@Override
	public String getJsClazz() {
		return "ListListener";
	}
}
