package xap.lui.core.event;
import xap.lui.core.comps.WebComp;
import xap.lui.core.dataset.LuiParameter;
import xap.lui.core.listener.LuiEventConf;
public class ContainerEvent extends AbstractServerEvent<WebComp> {
	public static final String ON_CONTAINER_CREATE = "onContainerCreate";
	public ContainerEvent(WebComp webElement) {
		super(webElement);
	}
	public ContainerEvent() {}
	public static LuiEventConf getOnContainerCreateEvent() {
		LuiEventConf event = new LuiEventConf();
		event.setEventName(ON_CONTAINER_CREATE);
		LuiParameter param = new LuiParameter();
		param.setName("simpleEvent");
		event.addParam(param);
		return event;
	}
	public String getJsClazz() {
		return "ContainerListener";
	}
}
