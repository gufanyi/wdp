package xap.lui.core.common;
import java.util.concurrent.ConcurrentHashMap;
public class WdpWebAppContainer {
	public static WdpWebAppContainer instance = null;
	public static String DefaultPortlAppName = "/portal";
	public static String DefaultWFWAppName = "/wfw";
	public static String DefaultWebContainerName = "webapps";
	public ConcurrentHashMap<String, String> ctxs = new ConcurrentHashMap<String, String>();
	private WdpWebAppContainer() {}
	public static WdpWebAppContainer getInstace() {
		if (instance == null) {
			synchronized (WdpWebAppContainer.class) {
				if (instance == null) {
					instance = new WdpWebAppContainer();
				}
			}
		}
		return instance;
	}
	public void addWebApp(String ctx, String path) {
		ctxs.put(ctx, path);
	}
	public String getWebApp(String key) {
		return ctxs.get(key);
	}
}
