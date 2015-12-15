package xap.lui.core.event;
import xap.lui.core.comps.TextComp;
import xap.lui.core.dataset.LuiParameter;
import xap.lui.core.listener.EventSubmitRule;
import xap.lui.core.listener.LuiEventConf;
public class RefTextEvent extends AbstractServerEvent<TextComp> {
	public static final String BEFORE_OPEN_REF_PAGE = "beforeOpenReference";
	public RefTextEvent(TextComp webElement) {
		super(webElement);
	}
	public RefTextEvent() {}
	public static LuiEventConf getBeforeOpenRefDialogEvent() {
		LuiEventConf event = new LuiEventConf();
		event.setEventName(BEFORE_OPEN_REF_PAGE);
		LuiParameter param = new LuiParameter();
		param.setName("simpleEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	@Override
	public String getJsClazz() {
		return "ReferenceTextListener";
	}
}
