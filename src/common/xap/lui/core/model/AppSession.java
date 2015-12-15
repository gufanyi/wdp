package xap.lui.core.model;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class AppSession {
	public static final String EVENT_LEVEL = "el";
	public static final String EVENT_LEVEL_APP = "0";
	public static final String EVENT_LEVEL_WIN = "1";
	public static final String EVENT_LEVEL_VIEW = "2";
	public static final String EVENT_LEVEL_SCRIPT = "3";
	public static final String METHOD_NAME = "m_n";
	public static final String PLUGOUT_SIGN = "plug";
	public static final String PLUGOUT_ID = "plugid";
	public static final String PLUGOUT_SOURCE = "plugsource";
	public static final String PLUGOUT_FROMSERVER = "fromServer";
	public static final String PLUGOUT_PARAMS = "plugparams";
	private static ThreadLocal<AppSession> threadLocal = new ThreadLocal<AppSession>();
	private Map<String, String> param;
	private AppContext appCtx;
	private List<Map<String, String>> groupParam;
	public AppSession() {}
	public static AppSession current() {
		return threadLocal.get();
	}
	public static void current(AppSession current) {
		threadLocal.set(current);
	}
	
	public String getParameter(String key) {
		if (param == null)
			param = new HashMap<String, String>();
		return param.get(key);
	}
	public void setParameter(String key, String value) {
		if (param == null)
			param = new HashMap<String, String>();
		param.put(key, value);
	}
	public static void reset() {
		threadLocal.remove();
	}
	
	public WindowContext getWindowContext() {
		return appCtx.getCurrentWindowContext();
	}
	
	public ViewPartContext getViewContext() {
		return getWindowContext().getCurrentViewContext();
	}
	public void setAppContext(AppContext appCtx) {
		this.appCtx = appCtx;
	}
	public AppContext getAppContext() {
		return appCtx;
	}
	/**
	 * 打开一个新URL
	 * 
	 * @param url
	 * @param title
	 */
	public void redirectTo(String url, String title) {
		String script = "$.pageutils.restoreContainerFramesHeight();openFrame_(url, title);";
		AppSession.current().getAppContext().addExecScript(script);
	}
	
	public Map<String, String> getParam() {
		return param;
	}
	
	public void setParam(Map<String, String> paramMap) {
		this.param = paramMap;
	}
	
	public List<Map<String, String>> getGroupParam() {
		return groupParam;
	}
	
	public void setGroupParam(List<Map<String, String>> groupParamMap) {
		this.groupParam = groupParamMap;
	}
}
