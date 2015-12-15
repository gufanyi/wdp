package xap.lui.core.model;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import xap.lui.core.common.ClientSession;
import xap.lui.core.common.LuiWebContext;
import xap.lui.core.common.LuiWebSession;
import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.constant.ParamConstant;
import xap.lui.core.event.AbstractServerEvent;
import xap.lui.core.exception.LuiRuntimeException;
import com.alibaba.fastjson.annotation.JSONField;
public class AppContext {
	public transient static final String TYPE_DIALOG = "TYPE_DIALOG";
	public transient static final String TYPE_WINDOW = "TYPE_WINDOW";
	public transient static final String OUTER_WIN_WIDTH = "1200";
	private List<String> afterExecScriptList;
	private AbstractServerEvent<?> currentEvent;
	private ClientSession clientSession = new ClientSession();
	private List<WindowContext> windowCtxs;
	private WindowContext currentWindowCtx;
	public void reset() {}
	@JSONField(serialize = false)
	public Application getApplication() {
		return (Application) getAppSession().getAttribute(LuiWebContext.APP_CONF);
	}
	@JSONField(serialize = false)
	public List<String> getExecScript() {
		return getCurrentWindowContext().getExecScript();
	}
	public void removeExecScript(int index) {
		getCurrentWindowContext().removeExecScript(index);
	}
	public void removeBeforeExecScript(int index) {
		getCurrentWindowContext().removeBeforeExecScript(index);
	}
	public void addBeforeExecScript(String beforeExecScript) {
		getCurrentWindowContext().addBeforeExecScript(beforeExecScript);
	}
	public void addAfterExecScript(String beforeExecScript) {
		if (this.afterExecScriptList == null) {
			this.afterExecScriptList = new ArrayList<String>();
		}
		this.afterExecScriptList.add(beforeExecScript);
	}
	public int addExecScript(String execScript) {
		return getCurrentWindowContext().addExecScript(execScript);
	}
	@JSONField(serialize = false)
	public List<String> getBeforeExecScript() {
		return getCurrentWindowContext().getBeforeExecScript();
	}
	@JSONField(serialize = true)
	public List<String> getAfterExecScript() {
		return afterExecScriptList;
	}
	@JSONField(serialize = true)
	public AbstractServerEvent<?> getCurrentEvent() {
		return currentEvent;
	}
	public void setCurrentEvent(AbstractServerEvent<?> currentEvent) {
		this.currentEvent = currentEvent;
	}
	@JSONField(serialize = true)
	public ClientSession getClientSession() {
		return clientSession;
	}
	public void setClientSession(ClientSession clientSession) {
		this.clientSession = clientSession;
	}
	public void setClientAttribute(String key, String value) {
		clientSession.setAttribute(key, value);
	}
	@JSONField(serialize = false)
	public String getAppId() {
		return (String) LuiRuntimeContext.getWebContext().getAppWebSession().getAttribute(LuiWebContext.APP_ID);
	}
	public void addAppAttribute(String key, Serializable value) {
		getAppSession().setAttribute(key, value);
	}
	public void removeAppAttribute(String key) {
		getAppSession().removeAttribute(key);
	}
	@JSONField(serialize = false)
	public Object getAppAttribute(String key) {
		return getAppSession().getAttribute(key);
	}
	@JSONField(serialize = false)
	private LuiWebSession getAppSession() {
		return LuiRuntimeContext.getWebContext().getAppWebSession();
	}
	public Map<String, Object> getPlug(String key) {
		return getPlugMap().get(key);
	}
	public void addPlug(String key, Map<String, Object> value) {
		getPlugMap().put(key, value);
	}
	@SuppressWarnings("unchecked")
	private Map<String, Map<String, Object>> getPlugMap() {
		Map<String, Map<String, Object>> map = (Map<String, Map<String, Object>>) getAppSession().getAttribute("PLUGMAP");
		if (map == null) {
			map = new HashMap<String, Map<String, Object>>();
			getAppSession().setAttribute("PLUGMAP", (Serializable) map);
		}
		return map;
	}
	public void navgateTo(String winId, String title, String width, String height) {
		navgateTo(winId, title, width, height, null, null);
	}
	public void navgateTo(String winId, String title, String width, String height, Map<String, String> paramMap) {
		navgateTo(winId, title, width, height, paramMap, null);
	}
	public void navgateTo(String winId, String title, String width, String height, Map<String, String> paramMap, String type) {
		navgateTo(winId, title, width, height, paramMap, type, false);
	}
	public void navgateTo(String winId, String title, String width, String height, Map<String, String> paramMap, String type, boolean isPopclose) {
		navgateTo(winId, title, width, height, paramMap, type, isPopclose, true);
	}
	public void navgateTo(String winId, String title, String width, String height, Map<String, String> paramMap, String type, boolean isPopclose, boolean buttonZone) {
		PagePartMeta win = getApplication().getWindowConf(winId);
		if (win == null)
			throw new LuiRuntimeException("当前APP中没有对应的window:" + winId);
		String url = LuiRuntimeContext.getRootPath() + "/app/" + getAppId() + "/" + winId + "?";
		String appUniqueId = LuiRuntimeContext.getWebContext().getAppUniqueId();
		url += ParamConstant.APP_UNIQUE_ID + "=" + appUniqueId;
		if (paramMap != null) {
			Iterator<Entry<String, String>> entryIt = paramMap.entrySet().iterator();
			while (entryIt.hasNext()) {
				Entry<String, String> entry = entryIt.next();
				url += ("&" + entry.getKey() + "=" + entry.getValue());
			}
		}
		if (TYPE_WINDOW.equals(type)) {
			this.addExecScript("$.pageutils.openWindowInCenter('" + url + "', '" + title + "', '" + height + "', '" + width + "', true);\n");
		} else {
			this.addExecScript("$.pageutils.showDialog(\"" + url + "\", \"" + title + "\", '" + width + "','" + height + "', \"" + winId + "\", '', {isConfirmClose:" + isPopclose + ",isShowLine:" + buttonZone + "});");
		}
	}
	public void dynNavgateTo(String winId, String title, String width, String height, Map<String, String> paramMap, String type, boolean isPopclose) {
		PagePartMeta win = getApplication().getWindowConf(winId);
		if (win == null) {
			PagePartMeta pm = new PagePartMeta();
			pm.setId(winId);
			pm.setCaption(title);
			getApplication().addWindow(pm);
		}
		navgateTo(winId, title, width, height, paramMap, type, isPopclose);
	}
	public void dynNavgateTo(String winId, String title, String width, String height, Map<String, String> paramMap, String type, boolean isPopclose, boolean buttonZone) {
		PagePartMeta win = getApplication().getWindowConf(winId);
		if (win == null) {
			PagePartMeta pm = new PagePartMeta();
			pm.setId(winId);
			pm.setCaption(title);
			getApplication().addWindow(pm);
		}
		navgateTo(winId, title, width, height, paramMap, type, isPopclose, buttonZone);
	}
	public void dynNavgateTo(String winId, String title, String width, String height, Map<String, String> paramMap) {
		PagePartMeta win = getApplication().getWindowConf(winId);
		if (win == null) {
			PagePartMeta pm = new PagePartMeta();
			pm.setId(winId);
			pm.setCaption(title);
			getApplication().addWindow(pm);
		}
		navgateTo(winId, title, width, height, paramMap, TYPE_DIALOG, false);
	}
	public void redirectTo(String winId, Map<String, String> paramMap) {
		PagePartMeta win = getApplication().getWindowConf(winId);
		if (win == null)
			throw new LuiRuntimeException("当前APP中没有对应的window:" + winId);
		String url = LuiRuntimeContext.getRootPath() + "/app/" + getAppId() + "/" + winId + "?";
		String appUniqueId = LuiRuntimeContext.getWebContext().getAppUniqueId();
		url += ParamConstant.APP_UNIQUE_ID + "=" + appUniqueId;
		if (paramMap != null) {
			Iterator<Entry<String, String>> entryIt = paramMap.entrySet().iterator();
			while (entryIt.hasNext()) {
				Entry<String, String> entry = entryIt.next();
				url += ("&" + entry.getKey() + "=" + entry.getValue());
			}
		}
		sendRedirect(url);
	}
	public void showModalDialog(String url, String title, String width, String height, String id, String type) {
		String appUniqueId = LuiRuntimeContext.getWebContext().getAppUniqueId();
		if (url.indexOf("?") != -1)
			url += "&";
		else
			url += "?";
		url += ParamConstant.APP_UNIQUE_ID + "=" + appUniqueId;
		if (TYPE_WINDOW.equals(type)) {
			this.addExecScript("$.pageutils.openWindowInCenter('" + url + "', '" + title + "', '" + height + "', '" + width + "');\n");
		} else {
			this.addExecScript("$.pageutils.showDialog(\"" + url + "\", \"" + title + "\", '" + width + "','" + height + "', \"" + id + "\");");
		}
	}
	public void closeWindow() {
		this.addAfterExecScript("$.pageutils.closeWindow();");
	}
	public void closeWinDialog() {
		this.addAfterExecScript("parent.$.pageutils.hideDialog();");
	}
	public void closeWinDialog(String winId) {
		this.addAfterExecScript("parent.$.pageutils.hideDialog('" + winId + "');");
	}
	public void popNoramlOuterWindow(String url, String title, String width, String height) {
		this.addExecScript("$.pageutils.openNormalWindowInCenter('" + url + "', '" + title + "', '" + height + "', '" + width + "', true);\n");
	}
	public void popOuterWindow(String url, String title, String width, String height, String type) {
		this.popOuterWindow(url, title, width, height, type, true);
	}
	public void popOuterWindow(String url, String title, String width, String height, String type, boolean buttonZone) {
		popOuterWindow(url, title, width, height, type, buttonZone, title);
	}
	public void popOuterWindow(String url, String title, String width, String height, String type, boolean buttonZone, String id) {
		if (title != null) {
			title = title.trim();
		}
		if (id != null) {
			id = id.trim();
		}
		if (TYPE_WINDOW.equals(type)) {
			this.addExecScript("$.pageutils.openWindowInCenter('" + url + "', '" + title + "', '" + height + "', '" + width + "', true);\n");
		} else {
			this.addExecScript("$.pageutils.showDialog('" + url + "', '" + title + "', '" + width + "','" + height + "', '" + id + "', null, {isShowLine:" + buttonZone + "});");
		}
	}
	public void popOuterWindow(String url, String title, boolean buttonZone) {
		popOuterWindow(url, title, OUTER_WIN_WIDTH, "100%");
	}
	public void popOuterWindow(String url, String title, String width, String height) {
		this.popOuterWindow(url, title, width, height, TYPE_WINDOW, true);
	}
	public void popOuterWindow(String url, String title, String width, String height, boolean buttonZone) {
		this.popOuterWindow(url, title, width, height, TYPE_WINDOW, buttonZone);
	}
	public void popOuterWindow(String url, String title) {
		popOuterWindow(url, title, OUTER_WIN_WIDTH, "100%");
	}
	@JSONField(serialize = false)
	public WindowContext getCurrentWindowContext() {
		if (currentWindowCtx == null) {
			if (windowCtxs != null && windowCtxs.size() > 0) {
				currentWindowCtx = windowCtxs.get(0);
			}
		}
		return currentWindowCtx;
	}
	public void setCurrentWindowContext(WindowContext ctx) {
		currentWindowCtx = ctx;
	}
	public void refreshWindow() {
		this.addExecScript("window.location.href = window.location.href");
	}
	public void sendRedirect(String url) {
		if (url != null) {
			String rdmId = ("" + System.currentTimeMillis()).substring(9);
			if (url.indexOf("?") != -1) {
				url += "&lrid=" + rdmId;
			} else {
				url += "?lrid=" + rdmId;
			}
		}
		this.addExecScript("sendRedirect('" + url + "'); \n");
	}
	public void historyBack() {
		String referer = (String) LuiRuntimeContext.getWebContext().getPageWebSession().getAttribute(LuiWebSession.REFERER);
		if (referer != null)
			sendRedirect(referer);
		else
			this.addExecScript("window.history.go(-1);");
	}
	public void addWindowContext(WindowContext ctx) {
		if (windowCtxs == null)
			windowCtxs = new ArrayList<WindowContext>();
		windowCtxs.add(ctx);
	}
	public WindowContext getWindowContext(String id) {
		if (windowCtxs == null)
			return null;
		Iterator<WindowContext> it = windowCtxs.iterator();
		while (it.hasNext()) {
			WindowContext ctx = it.next();
			if (ctx.getId().equals(id))
				return ctx;
		}
		return null;
	}
	@JSONField(serialize = true, name = "windows")
	public WindowContext[] getWindowCtxs() {
		return windowCtxs == null ? null : windowCtxs.toArray(new WindowContext[0]);
	}
	@JSONField(deserialize = true, name = "windows")
	public void setWindowCtxs(WindowContext[] winCtx) {
		windowCtxs = Arrays.asList(winCtx);
	}
	@JSONField(serialize = true)
	public boolean isPlug() {
		AppSession session = AppSession.current();
		String str = session.getParameter(AppSession.PLUGOUT_SIGN);
		if (str == null) {
			return false;
		} else {
			return true;
		}
	}
}
