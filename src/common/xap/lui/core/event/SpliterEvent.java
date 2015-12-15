package xap.lui.core.event;
import xap.lui.core.comps.WebComp;
import xap.lui.core.dataset.LuiParameter;
import xap.lui.core.listener.EventSubmitRule;
import xap.lui.core.listener.LuiEventConf;
/**
 * @author guoweic
 *
 */
public class SpliterEvent extends AbstractServerEvent<WebComp> {
	public static final String RESIZE_DIV2 = "resizeDiv2";
	public static final String RESIZE_DIV1 = "resizeDiv1";
	public SpliterEvent(WebComp webElement) {
		super(webElement);
	}
	// TODO
	public SpliterEvent() {}
	public static LuiEventConf getResizeDiv1Event() {
		LuiEventConf event = new LuiEventConf();
		event.setEventName(RESIZE_DIV1);
		LuiParameter param = new LuiParameter();
		param.setName("spliterEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	public static LuiEventConf getResizeDiv2Event() {
		LuiEventConf event = new LuiEventConf();
		event.setEventName(RESIZE_DIV2);
		LuiParameter param = new LuiParameter();
		param.setName("spliterEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	@Override
	public String getJsClazz() {
		return "SpliterListener";
	}
}
