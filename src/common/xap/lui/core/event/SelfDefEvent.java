package xap.lui.core.event;
import xap.lui.core.dataset.LuiParameter;
import xap.lui.core.listener.EventSubmitRule;
import xap.lui.core.listener.LuiEventConf;
public class SelfDefEvent<SelfDefComp> extends AbstractServerEvent<SelfDefComp> {
	public static final String ON_SELF_DEF_EVENT = "onSelfDefEvent";
	public static final String ON_CREATE_EVENT = "oncreate";
	private String triggerId;
	public SelfDefEvent(SelfDefComp webElement) {
		super(webElement);
	}
	public SelfDefEvent() {}
	public String getTriggerId() {
		return triggerId;
	}
	public void setTriggerId(String triggerId) {
		this.triggerId = triggerId;
	}
	public static LuiEventConf getOnClickEvent() {
		LuiEventConf event = new LuiEventConf();
		event.setEventName(ON_SELF_DEF_EVENT);
		LuiParameter param = new LuiParameter();
		param.setName("selfDefEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	@Override
	public String getJsClazz() {
		return "SelfDefListener";
	}
}
