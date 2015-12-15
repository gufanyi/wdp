package xap.lui.core.event;
import xap.lui.core.comps.LinkComp;
import xap.lui.core.dataset.LuiParameter;
import xap.lui.core.listener.EventSubmitRule;
import xap.lui.core.listener.LuiEventConf;
public class LinkEvent extends AbstractServerEvent<LinkComp> {
	public static final String ON_CLICK = "onclick";
	public LinkEvent(LinkComp webElement) {
		super(webElement);
	}
	public LinkEvent() {}
	public static LuiEventConf getOnClickEvent() {
		LuiEventConf event = new LuiEventConf();
		event.setEventName(ON_CLICK);
		LuiParameter param = new LuiParameter();
		param.setName("linkEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	@Override
	public String getJsClazz() {
		return "LinkListener";
	}
}
