package xap.lui.core.event;

import java.util.List;
import xap.lui.core.listener.LuiEventConf;
import xap.lui.core.listener.JsEventDesc;

public interface IEventSupport {
	public void addEventConf(LuiEventConf event);
	public void removeEventConf(String eventName, String method);
	public LuiEventConf[] getEventConfs();
	public List<JsEventDesc> getAcceptEventDescs();
	public String getId();
	public String getWidgetName();//组件名称
}
