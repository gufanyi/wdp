package xap.lui.core.common;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import xap.lui.core.cache.CacheMgr;
import xap.lui.core.cache.LUICache;
import xap.lui.core.constant.ApplicationConstant;
import xap.lui.core.constant.ParamConstant;
import xap.lui.core.constant.WebConstant;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.j2eesvr.IWebSessionListener;
import xap.lui.core.logger.LuiLogger;
import xap.lui.core.model.AppSession;
import xap.lui.core.model.IUIPartMeta;
import xap.lui.core.model.PagePartMeta;
import xap.lui.core.util.ClassUtil;

/**
 * Web上下文信息
 * 
 * @author dengjt
 * 
 */
public class LuiWebContext implements Serializable {
	private static final String CHILDLIST = "childlist";
	public static final String LOGIN_DID = "login_did";
	// public static final String LOGIN_DESTROY = "login_destroy";
	private static final long serialVersionUID = -2362958999118316626L;
	private PagePartMeta pageMeta = null;
	private IUIPartMeta uiMeta = null;
	private String pageId = null;
	private String pageUniqueId = null;
	private String parentUniqueId = null;
	private String parentPageId = null;
	// user define attributes
	private Object userObject = null;
	private String appUniqueId = null;
	/* 存放交互过程中request对象信息，通过此信息可以获取外部Web交互环境。 */
	private HttpServletRequest request = null;
	private HttpServletResponse response = null;
	public static final String WEB_SESSION_PRE = "$WS_ID_";
	private static final String PARENT_UNIQUE_ID = "$PARENT_UNIQUE_ID";
	private static final String PARENT_PAGE_ID = "$PARENT_PAGE_ID";
	public static final String PAGEMETA_KEY = "$PAGEMETA_KEY";
	public static final String UIMETA_KEY = "$UIMETA_KEY";
	public static final String APP_CONF = "APP_CONF";
	public static final String APP_ID = "appId";
	public static final String APP_UNIQUE_ID = "appUniqueId";
	public static final String APP_SES = "$APP_SES";
	public static final String Http_Session_ID = "$Http_Session_ID";
	public static final String WIDGET_ID = "widgetId";
	public static final String REF_NODE_ID = "nodeId";
	public static final String PAGEINDEX = "pi";
	private LuiWebSession ses;
	private LuiWebSession appSes;
	
	

	public LuiWebContext() {
	}

	public LuiWebContext(HttpServletRequest request) {
		this.request = request;
		// 从参数中获得pageId
		this.pageId = request.getParameter(ParamConstant.PAGE_ID);
		// 从参数中获得pageUniqueId
		this.pageUniqueId = request.getParameter(ParamConstant.PAGE_UNIQUE_ID);
		this.appUniqueId = request.getParameter(ParamConstant.APP_UNIQUE_ID);
		this.parentUniqueId = request.getParameter(ParamConstant.OTHER_PAGE_UNIQUE_ID);
		this.parentPageId = request.getParameter(ParamConstant.OTHER_PAGE_ID);
	}

	private void initParam(LuiWebSession ses) {
		Iterator<Entry<String, String[]>> it = this.request.getParameterMap().entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, String[]> entry = it.next();
			// 参数限定不会出现多个
			String[] values = entry.getValue();
			if (values != null && values.length > 0)
				ses.addOriginalParameter(entry.getKey(), values[0]);
		}
	}

	public Map getParamMap() {
		if (request == null)
			return null;
		return request.getParameterMap();
	}

	public String getParameter(String key) {
		String value=null;
		if(value==null&&request != null){
			value=request.getParameter(key);
		}
		if(value==null&&this.ses!=null){
			 value=this.ses.getOriginalParameter(key);
		}
		if(value==null&&this.appSes!=null){
			value=this.appSes.getOriginalParameter(key);
		}
		if(value==null&&AppSession.current()!=null){
			value=AppSession.current().getParameter(key);
		}
		return value;
	}

	public void setAttribute(String key, Object obj) {
		request.setAttribute(key, obj);
	}

	public Object getAttribute(String key) {
		return request.getAttribute(key);
	}

	public void removeAttribute(String key) {
		request.removeAttribute(key);
	}

	public String getPageId() {
		return pageId;
	}

	/**
	 * 获得页面初始参数，即请求页面的URL参数
	 * 
	 * @param key
	 * @return
	 */
	public String getOriginalParameter(String key) {
		return getPageWebSession().getOriginalParameter(key);
	}
	
	public IUIPartMeta getCrossUm(String crossPageId) {
		
		return this.getOriginalUm(crossPageId).doClone();
	}

	public PagePartMeta getCrossPageMeta(String crossPageId) {
		return (PagePartMeta)this.getOriginalPageMeta(crossPageId).clone();
	}
	
	public IUIPartMeta getOriginalUm(String pageId) {
		String pageUniqueId = getUniqueIdByPageId(pageId);
		String sid = WEB_SESSION_PRE + pageUniqueId;
		LuiWebSession ws=null;
		LUICache cache = CacheMgr.getSessionCache();
		if (cache.get(sid) != null) {
			ws = (LuiWebSession) cache.get(sid);
		}
		if(ws==null){
			throw new LuiRuntimeException("");
		}
		IUIPartMeta um = (IUIPartMeta) ws.getAttribute(LuiWebContext.UIMETA_KEY);
		return um;
	}

	public PagePartMeta getOriginalPageMeta(String pageId) {
		String pageUniqueId = getUniqueIdByPageId(pageId);
		if (pageUniqueId == null)
			return null;
		String sid = WEB_SESSION_PRE + pageUniqueId;
		LUICache cache = CacheMgr.getSessionCache();
		LuiWebSession ws=null;
		if (cache.get(sid) != null) {
			ws = (LuiWebSession) cache.get(sid);
		}
		if(ws==null){
			throw new LuiRuntimeException("");
		}
		PagePartMeta pm = (PagePartMeta) ws.getAttribute(LuiWebContext.PAGEMETA_KEY);
		if (pm == null) {
			getResponse().setStatus(308);
			return null;
		}
		return pm;
	}

	@SuppressWarnings("unchecked")
	private String getUniqueIdByPageId(String pageId) {
		LuiWebSession appSes = getAppWebSession();
		Map<String, String> pageMap = (Map<String, String>) appSes.getAttribute(CHILDLIST);
		if (pageMap == null)
			return null;
		return pageMap.get(pageId);
	}

	public PagePartMeta getPageMeta() {
		if (pageMeta == null) {
			LuiWebSession ws = getPageWebSession();
			PagePartMeta pm = (PagePartMeta) ws.getAttribute(LuiWebContext.PAGEMETA_KEY);
			pageMeta = pm;
		}
		return pageMeta;
	}

	public IUIPartMeta getUIMeta() {
		if (uiMeta == null) {
			LuiWebSession ws = getPageWebSession();
			IUIPartMeta um = (IUIPartMeta) ws.getAttribute(LuiWebContext.UIMETA_KEY);
			uiMeta = um;
		}
		return uiMeta;
	}

	public void setPageMeta(PagePartMeta pageMeta) {
		this.pageMeta = pageMeta;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	/**
	 * 消除这个页面所对应的session区域及子区域
	 */
	public void destroyWebSession() {
		// doDestroyPage(this.pageId, this.pageUniqueId);
	}

	@SuppressWarnings("unchecked")
	private void doDestroyPage(String pageId, String pageUniqueId) {
		HttpSession session = request.getSession(true);
		LUICache cache = CacheMgr.getSessionCache(session.getId());
		doDelayDestroyPage(pageId, pageUniqueId, cache, session);
		if (appSes != null) {
			Map<String, String> childIds = (Map<String, String>) appSes.getAttribute(CHILDLIST);
			if (childIds != null && childIds.size() != 0) {
				return;
			} else {
				DelayRunnable run = genDelayRunnable(pageId, pageUniqueId, session, appSes);
				// Cleaner.getInstance().runList.add(run);
			}
		}
	}

	protected DelayRunnable genDelayRunnable(String pageId, String pageUniqueId, HttpSession session, LuiWebSession appSes) {
		// 为了确保页面的正常逻辑，延迟5秒钟之后执行销毁
		DelayRunnable run = new DelayRunnable() {
			@SuppressWarnings("unchecked")
			public void run() {
				LUICache cache = CacheMgr.getSessionCache(session.getId());
				LuiRuntimeContext.setDatasource(datasource);
				if (appSes != null) {
					Map<String, String> childIds = (Map<String, String>) appSes.getAttribute(CHILDLIST);
					if (childIds == null || childIds.size() == 0) {
						String id = WEB_SESSION_PRE + appSes.getPageSessionId();
						cache.remove(id);
						appSes.destroy();
					}
				}
				// 尝试销毁会话
				doClearSession(cache, session);
			}
		};
		run.pageId = pageId;
		run.pageUniqueId = pageUniqueId;
		run.session = session;
		run.datasource = LuiRuntimeContext.getDatasource();
		run.quetime = System.currentTimeMillis();
		run.appSes = appSes;
		return run;
	}

	/**
	 * 确保在浏览器直接叉掉时，缓存被迅速删掉
	 * 
	 * @param session
	 */
	private void doClearSession(LUICache cache, HttpSession session) {
		try {
			// 如果系统设置心跳标识，则不触发销毁
			if (session.getAttribute(WebConstant.KEEP_HEART_BEAT) != null)
				return;
			// 如果系统中不再包含页面内容，则销毁
			Iterator<Object> em = cache.getKeys().iterator();
			boolean find = false;
			while (em.hasNext()) {
				String key = em.next().toString();
				if (key.startsWith(WEB_SESSION_PRE)) {
					find = true;
					break;
				}
			}
			// 如果是login页面，并且没有websession对象，可以确定是login销毁并没有导向到其它页面，可直接注销session
			if (!find)
				session.invalidate();
		}
		/*
		 * 如果是此Exception，表明是因为注销而导致session过期，实际上对应的变量已经确保删除。
		 * 由于session没有提供判断是否是过期的直接API ，此处通过catch异常来处理，此异常是正常情况，不进行记录及抛出。以免造成大量日志
		 */
		catch (IllegalStateException e) {
		}
	}

	/**
	 * 真正进行删除
	 * 
	 * @param pageUniqueId
	 * @param session
	 */
	@SuppressWarnings("unchecked")
	private void doDelayDestroyPage(String pageId, String pageUniqueId, LUICache cache, HttpSession session) {
		try {
			// 递归消除子页面内存
			Iterator em = cache.getKeys().iterator();
			while (em.hasNext()) {
				String key = em.next().toString();
				if (key.startsWith(WEB_SESSION_PRE)) {
					LuiWebSession ws = (LuiWebSession) cache.get(key);
					if (ws == null)
						continue;
					String pId = (String) ws.getAttribute(PARENT_UNIQUE_ID);
					String pPageId = (String) ws.getAttribute(PARENT_PAGE_ID);
					if (pId != null) {
						if (pId.equals(pageUniqueId)) {
							doDelayDestroyPage(pPageId, ws.getPageSessionId(), cache, session);
						}
					}
				}
			}
		}
		/*
		 * 如果是此Exception，表明是因为注销而导致session过期，实际上对应的变量已经确保删除。
		 * 由于session没有提供判断是否是过期的直接API ，此处通过catch异常来处理，此异常是正常情况，不进行记录及抛出。以免造成大量日志
		 */
		catch (IllegalStateException e) {
		}
		String uid = WEB_SESSION_PRE + pageUniqueId;
		LuiWebSession ws = (LuiWebSession) cache.remove(uid);
		if (ws != null) {
			if (this.appUniqueId != null && !this.appUniqueId.equals(pageUniqueId)) {
				LuiWebSession appSes = getAppWebSession();
				Map<String, String> childIds = (Map<String, String>) appSes.getAttribute(CHILDLIST);
				if (childIds != null) {
					childIds.remove(pageId);
				}
			}
			ws.destroy();
		}
		LuiLogger.info("destroy websession:" + pageUniqueId);
	}

	public LuiWebSession getAppWebSession() {
		if (appSes == null) {
			if (this.appUniqueId == null)
				throw new LuiRuntimeException("参数不正确");
			appSes = getWebSessionByAppId(this.appUniqueId);
		}
		return appSes;
	}

	public LuiWebSession getPageWebSession() {
		if (ses == null) {
			String pageUniqueId = getPageUniqueId();
			if (pageUniqueId == null)
				throw new LuiRuntimeException("此处不能获取PageSession");
			ses = getWebSessionByPageUniqueId(getPageUniqueId());
		}
		return ses;
	}

	public LuiWebSession getParentSession() {
		return getWebSessionByPageUniqueId(getParentPageUniqueId());
	}

	public LuiWebSession getWebSessionByAppId(String appId) {
		if (appSes == null) {
			if (this.appUniqueId == null) {
				this.appUniqueId = createAppUniqueId(true);
			}
			String sid = WEB_SESSION_PRE + appUniqueId;
			LUICache cache = CacheMgr.getSessionCache();
			if (cache.get(sid) != null) {
				appSes = (LuiWebSession) cache.get(sid);
				return appSes;
			}
			appSes = createAppWebSession(this.appUniqueId, appId);
		}
		return appSes;
	}

	private LuiWebSession getWebSessionByPageUniqueId(String pageUniqueId) {
		if (ses == null) {
			String sid = WEB_SESSION_PRE + pageUniqueId;
			LUICache cache = CacheMgr.getSessionCache();
			if (cache.get(sid) != null) {
				ses = (LuiWebSession) cache.get(sid);
				return ses;
			}
			ses = createPageSession(pageUniqueId);
		}
		return ses;
	}

	public String getAppUniqueId() {
		if (this.appUniqueId == null) {
			this.appUniqueId = createAppUniqueId(false);
		}
		return this.appUniqueId;
	}

	// //这个方法是用来产生AppUniqueId
	private String createAppUniqueId(boolean ignore) {
		if (!ignore) {
			String lrid = request.getParameter("lrid");// js 前面 Math.UUID()
			if (lrid != null && !lrid.equals("")) {
				if (lrid.indexOf("'") == -1 && lrid.indexOf("<") == -1) {
					return lrid;
				}
			}
		}
		return UUID.randomUUID().toString();
	}

	public String getPageUniqueId() {
		if (this.pageUniqueId == null) {
			this.pageUniqueId = this.createPageUniqueId();
		}
		return this.pageUniqueId;
	}

	// 这个方法是用来产生pageUniqueId
	private String createPageUniqueId() {
		String pageIndex = this.getParameter(PAGEINDEX);
		if (pageIndex == null) {
			//pageIndex = UUID.randomUUID().toString();
		}
		this.pageUniqueId = this.appUniqueId + "_" + this.pageId + pageIndex;
		// this.pageUniqueId = this.appUniqueId + "_" + this.pageId;
		return this.pageUniqueId;
	}

	private LuiWebSession createAppWebSession(String appUniqueId, String appId) {
		String cacheId = WEB_SESSION_PRE + appUniqueId;
		LUICache cache = CacheMgr.getSessionCache();
		LuiWebSession ws = createWebSessionImpl();
		ws.setWebSessionId(appUniqueId);
		ws.setPageId(getPageId());
		ws.setAttribute(APP_UNIQUE_ID, appUniqueId);
		ws.setAttribute(CHILDLIST, new HashMap<String, String>());
		ws.setAttribute(APP_SES, Boolean.TRUE);
		ws.setAttribute(APP_ID, appId);
		cache.put(cacheId, ws);
		if (ws.getOriginalParamMap() == null) {
			initParam(ws);
		}
		ws.created();
		return ws;
	}

	private LuiWebSession createPageSession(String pageUniqueId) {
		String cacheId = WEB_SESSION_PRE + pageUniqueId;
		LUICache cache = CacheMgr.getSessionCache();
		LuiWebSession ws = createWebSessionImpl();
		ws.setWebSessionId(pageUniqueId);
		ws.setPageId(getPageId());
		if (parentUniqueId != null) {
			ws.setAttribute(PARENT_UNIQUE_ID, parentUniqueId);
			ws.setAttribute(PARENT_PAGE_ID, parentPageId);
		}
		ws.setAttribute(APP_UNIQUE_ID, appUniqueId);
		if (this.appUniqueId != null) {
			LuiWebSession appSes = getAppWebSession();
			if (appSes != null) {
				Map<String, String> childIds = (Map<String, String>) appSes.getAttribute(CHILDLIST);
				if (childIds != null && pageUniqueId != null) {
					childIds.put(pageId, pageUniqueId);
				}
			}
		}
		if (ws.getOriginalParamMap() == null) {
			initParam(ws);
		}
		ws.created();
		cache.put(cacheId, ws);
		return ws;
	}

	/**
	 * 创建WebSession实例。从配置文件中获取。默认取内存实现
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private LuiWebSession createWebSessionImpl() {
		String clazz = LuiRuntimeContext.getModelServerConfig().getWebSessionImpl();
		if (clazz == null || clazz.equals(""))
			clazz = LocalWebSession.class.getName();
		IWebSessionListener[] wsListener = getWebSessionListeners();
		Class<LuiWebSession> sesClazz = (Class<LuiWebSession>) ClassUtil.forName(clazz);
		try {
			Constructor<LuiWebSession> cons = sesClazz.getConstructor(new Class[] { IWebSessionListener[].class });
			ses = cons.newInstance(new Object[] { wsListener });
		} catch (Throwable e) {
			LuiLogger.error(e);
			throw new LuiRuntimeException(e.getMessage());
		}
		return ses;
	}

	private IWebSessionListener[] getWebSessionListeners() {
		Object result = request.getSession().getServletContext().getAttribute(ApplicationConstant.WEBSESSION_LISTENER);
		if (result == null) {
			createWebSessionListeners();
			result = request.getSession().getServletContext().getAttribute(ApplicationConstant.WEBSESSION_LISTENER);
		}
		if (result instanceof String)
			return null;
		return (IWebSessionListener[]) result;
	}

	private void createWebSessionListeners() {
		String[] listeners = LuiRuntimeContext.getModelServerConfig().getWebSessionListeners();
		if (listeners == null || listeners.length == 0)
			request.getSession().getServletContext().setAttribute(ApplicationConstant.WEBSESSION_LISTENER, "EMPTY");
		else {
			IWebSessionListener[] listenerClazz = new IWebSessionListener[listeners.length];
			for (int i = 0; i < listenerClazz.length; i++) {
				listenerClazz[i] = (IWebSessionListener) ClassUtil.newInstance(listeners[i]);
			}
			request.getSession().getServletContext().setAttribute(ApplicationConstant.WEBSESSION_LISTENER, listenerClazz);
		}
	}

	public void adjustPageUniqueId(String newUniqueId) {
		if (newUniqueId == null || newUniqueId.equals(""))
			return;
		LuiWebSession ws = getWebSessionByPageUniqueId(this.pageUniqueId);
		ws.setWebSessionId(newUniqueId);
		String sid = WEB_SESSION_PRE + this.pageUniqueId;
		LUICache cache = CacheMgr.getSessionCache();
		cache.remove(sid);
		cache.put(WEB_SESSION_PRE + newUniqueId, ws);
		this.pageUniqueId = newUniqueId;
	}

	private LuiWebSession getParentWebSession(String id) {
		String sid = WEB_SESSION_PRE + id;
		LUICache cache = CacheMgr.getSessionCache();
		return (LuiWebSession) cache.get(sid);
	}

	/**
	 * 获取此处请求是由哪个pagemeta发起的。
	 * 
	 * @return
	 */
	public PagePartMeta getParentPageMeta() {
		String parentPageId = getParentPageUniqueId();
		LuiWebSession parentWs = getParentWebSession(parentPageId);
		return (PagePartMeta) parentWs.getAttribute(PAGEMETA_KEY);
	}

	public String getParentPageId() {
		return this.parentPageId;
	}

	public String getParentPageUniqueId() {
		return this.parentUniqueId;
	}

	public void setPageId(String pageId) {
		this.pageId = pageId;
	}

	public Object getUserObject() {
		return userObject;
	}

	public void setUserObject(Object userObject) {
		this.userObject = userObject;
	}

	public void setPageUniqueId(String pageUniqueId) {
		this.pageUniqueId = pageUniqueId;
	}

	public void setResponse(HttpServletResponse res) {
		this.response = res;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setUIMeta(IUIPartMeta uiMeta) {
		this.uiMeta = uiMeta;
	}




}

abstract class DelayRunnable {
	protected String pageId;
	protected String pageUniqueId;
	protected HttpSession session;
	protected String datasource;
	protected LuiWebSession appSes;
	protected long quetime;

	protected void run() {
	}
}

class Cleaner implements Runnable {
	ConcurrentLinkedQueue<DelayRunnable> runList = new ConcurrentLinkedQueue<DelayRunnable>();
	private static Cleaner cleaner;

	@Override
	public void run() {
		while (true) {
			try {
				long now = System.currentTimeMillis();
				DelayRunnable run = runList.peek();
				while (run != null) {
					if (now - run.quetime >= 5000) {
						runList.remove();
						run.run();
					} else {
						break;
					}
					run = runList.peek();
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					LuiLogger.error(e);
				}
			} catch (Throwable e) {
				LuiLogger.error(e);
			}
		}
	}
	// protected static Cleaner getInstance() {
	// Executor ex = null;
	// if (cleaner == null) {
	// synchronized (Cleaner.class) {
	// if (cleaner == null) {
	// cleaner = new Cleaner();
	// ex = new Executor(cleaner);
	// }
	// }
	// if (ex != null)
	// ex.start();
	// }
	// return cleaner;
	// }
}
