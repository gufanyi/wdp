package xap.lui.core.common;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import xap.lui.core.cache.CacheMgr;
import xap.lui.core.cache.LUICache;
import xap.lui.core.constant.CookieConstant;
import xap.lui.core.constant.WebConstant;
import xap.lui.core.control.ModePhase;
import xap.lui.core.control.ResourceFrom;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.model.PagePartMeta;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.core.sso.SessionBean;
import xap.lui.core.util.CookieUtil;

public final class LuiRuntimeContext {
	public static final String MODEPHASE = "ModePhase";
	public static final String RESOUCEFROM = "ResouceFrom";
	public static final String VESION = "Vesion";
	public static final String PersonaCode = "PersonaCode";
	public static final String DESIGNWINID="DesignWinId";
	//public static final String SOURCE_WINDOW = "sourceWinId";
	private static String serverAddr = null;
	private static String luiPath;
	private static ServletContext luiServletContext;
	private static ServerConfig luiServerConfig = null;
	private static Map<String, ModelServerConfig> modelConfigMap = new HashMap<String, ModelServerConfig>();
	private static LuiState luiState = LuiState.STARTING;
	private static Map<String, PagePartMeta> pagePartMetaPool = null;
	private static Map<String, ViewPartMeta> viewPartMetaPool = null;
	private static Map<String, Properties> pagePropMetaPool = null;
	public static boolean isStartCache = true;
	private static ThreadLocal<EnviromentObject> threadLocal = new ThreadLocal<EnviromentObject>() {
		protected EnviromentObject initialValue() {
			return new EnviromentObject();
		}
	};

	public static Map<String, PagePartMeta> getPagePartMetaPool() {
		if (pagePartMetaPool == null) {
			LUICache cache = CacheMgr.getStrongCache(WebConstant.LUI_CORE_CACHE, "lui");
			pagePartMetaPool = (Map<String, PagePartMeta>) cache.get(WebConstant.PAGEMETA_POOL_KEY);
			if (pagePartMetaPool == null) {
				pagePartMetaPool = new ConcurrentHashMap<String, PagePartMeta>();
				cache.put(WebConstant.PAGEMETA_POOL_KEY, pagePartMetaPool);
			}
		}
		return pagePartMetaPool;
	}

	public static Map<String, ViewPartMeta> getViewPartMetaPool() {
		if (viewPartMetaPool == null) {
			LUICache cache = CacheMgr.getStrongCache(WebConstant.LUI_CORE_CACHE, "lui");
			viewPartMetaPool = (Map<String, ViewPartMeta>) cache.get(WebConstant.WIDGET_POOL_KEY);
			if (viewPartMetaPool == null) {
				viewPartMetaPool = new ConcurrentHashMap<String, ViewPartMeta>();
				cache.put(WebConstant.WIDGET_POOL_KEY, viewPartMetaPool);
			}
		}
		return viewPartMetaPool;
	}

	public static Map<String, Properties> getPagePropMetaPool() {
		if (pagePropMetaPool == null) {
			LUICache cache = CacheMgr.getStrongCache(WebConstant.LUI_CORE_CACHE, "lui");
			pagePropMetaPool = (Map<String, Properties>) cache.get(WebConstant.PROP_POOL_KEY);
			if (pagePropMetaPool == null) {
				pagePropMetaPool = new ConcurrentHashMap<String, Properties>();
				cache.put(WebConstant.PROP_POOL_KEY, pagePropMetaPool);
			}
		}
		return pagePropMetaPool;
	}

	public static PagePartMeta getOriPagePartMeta(String pageId) {
		if (StringUtils.isBlank(pageId)) {
			throw new LuiRuntimeException("获取原生的PagePart需要提供节点ID");
		}
		return getPagePartMetaPool().get(pageId);
	}

	public static ViewPartMeta getOriViewPartMeta(String pageId, String viewId) {
		if (StringUtils.isBlank(pageId)) {
			throw new LuiRuntimeException("获取原生的ViewPart需要提供节点ID");
		}
		if (StringUtils.isBlank(viewId)) {
			throw new LuiRuntimeException("获取原生的ViewPart需要提供视图ID");
		}
		String cacheId = pageId + "_$_" + viewId;
		return getViewPartMetaPool().get(cacheId);
	}

	public static Properties getOriPropByPageId(String pageId) {
		if (StringUtils.isBlank(pageId)) {
			throw new LuiRuntimeException("获取原生的属性配置需要提供节点ID");
		}
		return getPagePropMetaPool().get(pageId);
	}

	public static ModelServerConfig getModelServerConfig() {
		LuiWebContext ctx = getWebContext();
		if (ctx == null)
			return null;
		String ctxPath = ctx.getRequest().getContextPath();
		ModelServerConfig modelConfig = modelConfigMap.get(ctxPath);
		if (modelConfig == null) {
			modelConfig = new ModelServerConfig(ctx.getRequest().getSession().getServletContext());
			modelConfigMap.put(ctxPath, modelConfig);
		}
		return modelConfig;
	}

	public static void setServerConfig(ServerConfig config) {
		luiServerConfig = config;
	}

	public static ServerConfig getServerConfig() {
		return luiServerConfig;
	}

	public static void setWebContext(LuiWebContext ctx) {
		threadLocal.get().webCtx = ctx;
	}

	public static LuiWebContext getWebContext() {
		return threadLocal.get().webCtx;
	}

	public static void setRealPath(String realPath) {
		threadLocal.get().realPath = realPath;
	}

	public static void setLuiPath(String luiRealPath) {
		luiPath = luiRealPath;
	}

	public static String getLuiPath() {
		return luiPath;
	}

	public static String getLuiCtx() {
		return "/lui";
	}

	public static ServletContext getLuiServletContext() {
		return luiServletContext;
	}

	public static void setLuiServletContext(ServletContext servletContext) {
		luiServletContext = servletContext;
	}

	public static String getRealPath() {
		return threadLocal.get().realPath;
	}

	public static void setRootPath(String rootPath) {
		threadLocal.get().rootPath = rootPath;
	}

	public static String getCorePath() {
		return threadLocal.get().corePath;
	}

	public static void setCorePath(String corePath) {
		threadLocal.get().corePath = corePath;
	}

	public static String getRootPath() {
		return threadLocal.get().rootPath;
	}

	public static void setMode(String debug) {
		threadLocal.get().mode = debug;
	}

	public static String getMode() {
		if (threadLocal.get().mode == null) {
			if (isDevelopMode())
				return WebConstant.MODE_DEBUG;
			return WebConstant.MODE_DEBUG;
		}
		return threadLocal.get().mode;
	}


	public static ModePhase getModePhase() {
		String modePhase = LuiRuntimeContext.getWebContext().getParameter(LuiRuntimeContext.MODEPHASE);
		if ((ModePhase.eclipse.toString()).equals(modePhase))
			return ModePhase.eclipse;
		if ((ModePhase.nodedef.toString()).equals(modePhase))
			return ModePhase.nodedef;
		if ((ModePhase.persona.toString()).equals(modePhase))
			return ModePhase.persona;
		if ((ModePhase.normal.toString()).equals(modePhase))
			return ModePhase.normal;
		return ModePhase.normal;
	}

	public static ResourceFrom getResourceFrom() {
		String resouceFrom = LuiRuntimeContext.getWebContext().getParameter(LuiRuntimeContext.RESOUCEFROM);
		if (StringUtils.isBlank(resouceFrom)) {
			return ResourceFrom.classjar;
		}
		if (ResourceFrom.nodedef.toString().equals(resouceFrom)) {
			return ResourceFrom.nodedef;
		}
		if (ResourceFrom.persona.toString().equals(resouceFrom)) {
			return ResourceFrom.persona;
		}
		if (ResourceFrom.classjar.toString().equals(resouceFrom)) {
			return ResourceFrom.classjar;
		}
		return ResourceFrom.classjar;
	}

	public static String getPageId() {
		return getWebContext().getPageId();
	}

	public static String getPersonaCode() {
		return getWebContext().getParameter(PersonaCode);
	}

	public static String getVesion() {
		return getWebContext().getParameter(VESION);
	}

	public static boolean isEditMode() {
		LuiWebSession ses = getWebContext().getPageWebSession();
		if (ses == null)
			return false;
		String editModeStr = (String) ses.getAttribute(WebConstant.EDIT_MODE_KEY);
		boolean editMode = editModeStr == null ? false : editModeStr.equals(WebConstant.MODE_PERSONALIZATION);
		return editMode;
	}

	public static boolean isWindowEditorMode() {
		LuiWebSession ses = getWebContext().getPageWebSession();
		if (ses == null)
			return false;
		String editModeStr = (String) ses.getAttribute(WebConstant.WINDOW_MODE_KEY);
		boolean editMode = editModeStr == null ? false : editModeStr.equals(WebConstant.MODE_PERSONALIZATION);
		return editMode;
	}

	public static boolean isDevelopMode() {
		return true;
	}

	public static void setFromServerName(String serverName) {
		threadLocal.get().serverName = serverName;
	}

	public static String getFromServerName() {
		return threadLocal.get().serverName;
	}

	public static Integer getSysId() {
		return getModelServerConfig().getSysId();
	}

	public static String getThemeId() {
		// 首先从session中获取用户的个性设置信息
		String themeId = null;
		SessionBean ses = getSessionBean();
		if (ses != null)
			themeId = ses.getThemeId();
		if (themeId != null)
			return themeId;
		String sysId = "" + LuiRuntimeContext.getSysId();
		// 首先从cookie中获取用户设置的主题id
		themeId = CookieUtil.get(((HttpServletRequest) getWebContext().getRequest()).getCookies(), CookieConstant.THEME_CODE + sysId);
		if (themeId == null || themeId.length() == 0 || themeId.equals("null")) {
			// 从模块获取默认的配置属性信息
			themeId = getModelServerConfig().getDefaultThemeId();
		}
		// 最后获取lui默认配置
		if (themeId == null)
			themeId = getServerConfig().getDefaultThemeId();
		if (themeId == null)
			return "default";
		return themeId;
	}

	public static String getDomain() {
		return LuiRuntimeContext.getServerConfig().getDomain();
	}

	public static Theme getTheme() {
		return ThemeManager.getLuiTheme(LuiRuntimeContext.getRootPath(), "default");
	}

	public static ServletContext getServletContext() {
		return threadLocal.get().servletCtx;
	}

	public static void setServletContext(ServletContext ctx) {
		threadLocal.get().servletCtx = ctx;
	}

	public static void setLangDir(String langDir) {
		threadLocal.get().langDir = langDir;
	}

	public static String getLangDir() {
		String langDir = threadLocal.get().langDir;
		return langDir;
	}

	/**
	 * 获取语言编码
	 * 
	 * @return
	 */
	public static String getLangCode() {
		return "simpchn";
	}

	/**
	 * 设置语言编码
	 * 
	 * @param langCode
	 */
	public static void setLangCode(String langCode) {
		// InvocationInfoProxy.getInstance().setLangCode(langCode);
	}

	public static String getServerAddr() {
		return serverAddr;
	}

	public static void setServerAddr(String serverAddr) {
		LuiRuntimeContext.serverAddr = serverAddr;
	}

	public static String getDatasource() {
		String dsName = threadLocal.get().datasource;
		return dsName;
	}

	public static boolean isCompressStream() {
		return threadLocal.get().compressStream;
	}

	public static void setCompressStream(boolean compress) {
		threadLocal.get().compressStream = compress;
	}

	public static boolean isClientMode() {
		LuiWebSession ses = getWebContext().getPageWebSession();
		if (ses != null)
			return ses.getAttribute(WebConstant.CLIENT_MODE) != null;
		return false;
	}

	public static void setDatasource(String ds) {
		threadLocal.get().datasource = ds;
	}

	/**
	 * 是否LUI公共节点
	 */
	public static boolean isFromLui() {
		return threadLocal.get().isFromLui;
	}

	public static void setFromLui(boolean fromLui) {
		threadLocal.get().isFromLui = fromLui;
	}

	public static void reset() {
		threadLocal.get().reset();
	}

	public static boolean isLoadRunner() {
		return LuiRuntimeContext.getServerConfig().getLoadRunnerAdpater().equals("1");
	}

	public static BrowserSniffer getBrowserInfo() {
		BrowserSniffer sniffer = threadLocal.get().browserSniffer;
		if (sniffer == null) {
			sniffer = new BrowserSniffer(getWebContext().getRequest().getHeader("user-agent"));
			threadLocal.get().browserSniffer = sniffer;
		}
		return sniffer;
	}

	public static Object getPropertyValue(String key) {
		Map<String, Object> map = threadLocal.get().userObjects;
		if (map == null)
			return null;
		return map.get(key);
	}

	public static void setProperty(String key, Object value) {
		Map<String, Object> map = threadLocal.get().userObjects;
		if (map == null) {
			map = new HashMap<String, Object>();
			threadLocal.get().userObjects = map;
		}
		if (value == null)
			map.remove(key);
		else
			map.put(key, value);
	}

	public static SessionBean getSessionBean() {
		return threadLocal.get().loginBean;
	}

	public static void setSessionBean(SessionBean bean) {
		threadLocal.get().loginBean = bean;
	}

	public static void setLuiState(LuiState state) {
		luiState = state;
	}

	public static LuiState getLuiState() {
		return luiState;
	}
}

/**
 * 环境变量包装类。内部使用且调用频繁，不做get set方法调用
 */
class EnviromentObject {
	protected LuiWebContext webCtx;
	protected ServletContext servletCtx;
	protected String realPath;
	protected String rootPath;
	protected String corePath;
	protected String mode = null;
	protected String serverName;
	protected String langDir;
	protected String themeId;
	protected String datasource;
	protected BrowserSniffer browserSniffer;
	protected Map<String, Object> userObjects;
	protected boolean compressStream = true;
	protected boolean isFromLui = false;
	protected boolean isFromDB = false;
	protected SessionBean loginBean;

	protected void reset() {
		webCtx = null;
		realPath = null;
		rootPath = null;
		corePath = null;
		mode = null;
		serverName = null;
		servletCtx = null;
		langDir = null;
		themeId = null;
		datasource = null;
		browserSniffer = null;
		userObjects = null;
		compressStream = true;
		loginBean = null;
		isFromLui = false;
		isFromDB = false;
	}
}
