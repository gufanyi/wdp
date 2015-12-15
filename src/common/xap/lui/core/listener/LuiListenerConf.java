package xap.lui.core.listener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
public abstract class LuiListenerConf implements Cloneable {
	public static final String TYPE = "type";
	private String confType;
	private String from;
	private String id;
	private String serverClazz;
	private Map<String, LuiEventConf> eventMap = new HashMap<String, LuiEventConf>();
	public String getConfType() {
		return confType;
	}
	public void setConfType(String confType) {
		this.confType = confType;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public void addEventHandler(LuiEventConf event) {
		eventMap.put(event.getName(), event);
	}
	public Map<String, LuiEventConf> getEventHandlerMap() {
		return eventMap;
	}
	public LuiEventConf getEventHandler(String s) {
		return eventMap.get(s);
	}
	public void removeEventHandler(String s) {
		eventMap.remove(s);
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public abstract String getJsClazz();
	
	public Object clone() {
		LuiListenerConf ele;
		try {
			ele = (LuiListenerConf) super.clone();
			if (this.eventMap != null) {
				ele.eventMap = new HashMap<String, LuiEventConf>();
				Iterator<LuiEventConf> it = this.eventMap.values().iterator();
				while (it.hasNext()) {
					ele.addEventHandler((LuiEventConf) it.next().clone());
				}
			}
			return ele;
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}
	public String getServerClazz() {
		return serverClazz;
	}
	public void setServerClazz(String serverClazz) {
		this.serverClazz = serverClazz;
	}
	public LuiEventConf getEventTemplate(String key) {
		return new LuiEventConf();
	}
	
	public String getJsParam(String eventName) {
		return null;
	}
}
