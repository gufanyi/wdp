package xap.lui.core.cache;

import java.lang.reflect.Method;
import java.util.HashMap;

import javax.servlet.ServletContext;

import org.apache.commons.lang.StringUtils;

import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.design.UIMetaToXml;
import xap.lui.core.layout.UIPartMeta;
import xap.lui.core.layout.UIViewPart;
import xap.lui.core.model.Application;
import xap.lui.core.model.LuiMeta;
import xap.lui.core.model.PagePartMeta;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.core.services.IPaEditorService;
import xap.lui.core.services.PaEditorServiceImpl;
import xap.lui.psn.designer.CreateDesignModel;
import xap.lui.psn.designer.PaProjViewTreeController;
import xap.lui.psn.setting.MethodCacheInfo;

public class PaCache {
	public static final String ModePhase = "_ModePhase";
	public static final String FreeBillCtrlClassName = "xap.lui.portal.freeform.runtime.DftFreeBillController";
	public static final String FreeBillGrdiClassName = "xap.lui.portal.freeform.runtime.DftFreeGridController";
	public static final String NodeVersion = "_version";
	public static final String PersonaCode="_personacode";

	public static PaCache instance = null;
	// public Map<String, Object> map = new ConcurrentHashMap<String, Object>();
	public static LuiMeta luiMeta = null;
	public static LuiMeta nowMeta = null;
	// 还原点
	public static PagePartMeta recovePointPagePartMeta = null;
	public static UIPartMeta recovePointUIPartMeta = null;

	public static PaCache getInstance() {
		if (instance == null) {
			synchronized (PaCache.class) {
				if (instance == null) {
					synchronized (PaCache.class) {
						instance = new PaCache();
					}
				}
			}
		}
		return instance;
	}

	public void pub(String key, Object value) {
		CacheMgr.getSessionCache().put(key, value);
	}

	public void putCacheMethod(String fieldId, MethodCacheInfo method) {
		HashMap<String, MethodCacheInfo> methods = (HashMap<String, MethodCacheInfo>) PaCache.getInstance().get("method");
		if (methods == null) {
			methods = new HashMap<String, MethodCacheInfo>();
			CacheMgr.getSessionCache().put("method", methods);
		}
		methods.put(fieldId, method);
	}

	public void clearCacheMethod() {
		CacheMgr.getSessionCache().remove("method");
	}

	public MethodCacheInfo getCacheMethod(String fieldId) {
		HashMap<String, MethodCacheInfo> methods = (HashMap<String, MethodCacheInfo>) this.get("method");
		if (methods == null) {
			return null;
		}
		MethodCacheInfo cacheInfo = methods.get(fieldId);
		return cacheInfo;
	}

	public Object get(String key) {
		// WeakReference<Object> refrence = map.get(key);
		return CacheMgr.getSessionCache().get(key);
	}

	public void remove(String key) {
		CacheMgr.getSessionCache().remove(key);
	}

	public static ViewPartMeta getEditorViewPartMeta() {
		IPaEditorService ipaService = new PaEditorServiceImpl();
		String sessionId = (String) PaCache.getInstance().get("eclipse_sesionId");
		String pageId = (String) PaCache.getInstance().get("_pageId");
		String viewId = (String) PaCache.getInstance().get("_viewId");
		PagePartMeta pagePartMeta = ipaService.getOriPageMeta(pageId, sessionId);
		ViewPartMeta viewPartMeta = null;
		if (StringUtils.isNotBlank(viewId)) {
			viewPartMeta = pagePartMeta.getWidget(viewId);
		}
		return viewPartMeta;
	}

	public static PagePartMeta getEditorPagePartMeta() {
		IPaEditorService ipaService = new PaEditorServiceImpl();
		String sessionId = (String) PaCache.getInstance().get("eclipse_sesionId");
		String pageId = (String) PaCache.getInstance().get("_pageId");
		PagePartMeta pagePartMeta = ipaService.getOriPageMeta(pageId, sessionId);
		return pagePartMeta;
	}

	public static String getEditorVersion() {
		return (String) PaCache.getInstance().get("_version");
	}

	public static void addMethod(String fieldName, Method method) {
		PaCache.getInstance().pub(fieldName, method);
	}

	public static Method getMethod(String fieldName) {
		Method method = (Method) PaCache.getInstance().get(fieldName);
		return method;
	}

	public static UIPartMeta getEditorUIPartMeta() {
		IPaEditorService ipaService = new PaEditorServiceImpl();
		String sessionId = (String) PaCache.getInstance().get("eclipse_sesionId");
		String pageId = (String) PaCache.getInstance().get("_pageId");
		String viewId = (String) PaCache.getInstance().get("_viewId");
		{
			UIPartMeta uiPartMeta = ipaService.getOriUIMeta(pageId, sessionId);
			if (uiPartMeta == null)
				return null;
			if (StringUtils.isNotBlank(viewId)) {
				UIViewPart uiViewPart = (UIViewPart) uiPartMeta.getElement();
				if (uiViewPart != null)
					uiPartMeta = uiViewPart.getUimeta();
			}
			return uiPartMeta;
		}
	}

	public static String getNowViewOrPageMetaXml() {
		IPaEditorService ipaService = new PaEditorServiceImpl();
		String sessionId = (String) PaCache.getInstance().get("eclipse_sesionId");
		String pageId = (String) PaCache.getInstance().get("_pageId");
		String viewId = (String) PaCache.getInstance().get("_viewId");
		PagePartMeta pagePartMeta = ipaService.getOriPageMeta(pageId, sessionId);
		ViewPartMeta viewPartMeta = null;
		if (StringUtils.isNotBlank(viewId) && pagePartMeta != null) {
			viewPartMeta = pagePartMeta.getWidget(viewId);
		}
		if (viewPartMeta != null) {
			String xml0 = viewPartMeta.toXml();
			return xml0;
		}
		if (pagePartMeta != null) {
			String xml0 = pagePartMeta.toXml();
			return xml0;
		}
		return "";
	}

	public static String getEditState() {
		IPaEditorService ipaService = new PaEditorServiceImpl();
		String sessionId = (String) PaCache.getInstance().get("eclipse_sesionId");
		String pageId = (String) PaCache.getInstance().get("_pageId");
		String viewId = (String) PaCache.getInstance().get("_viewId");
		String appId = (String) PaCache.getInstance().get("_appId");
		PagePartMeta pagePartMeta = ipaService.getOriPageMeta(pageId, sessionId);
		ViewPartMeta viewPartMeta = null;
		if (StringUtils.isBlank(appId)) {
			if (StringUtils.isNotBlank(viewId) && pagePartMeta != null) {
				viewPartMeta = pagePartMeta.getWidget(viewId);
			}
			if (viewPartMeta != null) {
				return "view";
			}
			if (pagePartMeta != null) {
				return "page";
			}
		} else {
			return "app";
		}

		return null;
	}

	public static String getNowPage() {
		IPaEditorService ipaService = new PaEditorServiceImpl();
		String sessionId = (String) PaCache.getInstance().get("eclipse_sesionId");
		String pageId = (String) PaCache.getInstance().get("_pageId");
		PagePartMeta pagePartMeta = ipaService.getOriPageMeta(pageId, sessionId);
		if (pagePartMeta != null) {
			String xml0 = pagePartMeta.toXml();
			return xml0;
		}
		return "";
	}

	public static String getNowApp() {
		Application application = (Application) PaCache.getInstance().get("_editApp");
		if (application != null) {
			String xml0 = application.toXml();
			return xml0;
		}
		return "";
	}

	public static String getNowUiMetaXml() {
		IPaEditorService ipaService = new PaEditorServiceImpl();
		String sessionId = (String) PaCache.getInstance().get("eclipse_sesionId");
		String pageId = (String) PaCache.getInstance().get("_pageId");
		String viewId = (String) PaCache.getInstance().get("_viewId");
		{
			UIPartMeta uiPartMeta = ipaService.getOriUIMeta(pageId, sessionId);
			if (StringUtils.isNotBlank(viewId) && uiPartMeta != null) {
				UIViewPart uiViewPart = (UIViewPart) uiPartMeta.getElement();
				uiPartMeta = uiViewPart.getUimeta();
			}
			if (uiPartMeta != null) {
				String xml1 = UIMetaToXml.toString(uiPartMeta);
				return xml1;
			}
			return "";
		}
	}

	public static String getController() {
		String controller = null;
		ViewPartMeta viewPartMeta = PaCache.getEditorViewPartMeta();
		if (viewPartMeta == null) {
			PagePartMeta pagePartMeta = PaCache.getEditorPagePartMeta();
			if (pagePartMeta == null)
				return null;
			controller = pagePartMeta.getController();
		} else {
			controller = viewPartMeta.getController();
		}
		return controller;
	}

	public static String getEditorUIMetaXmlPath() {
		PaCache cache = PaCache.getInstance();
		String pageId = (String) cache.get("_pageId");
		String viewId = (String) cache.get("_viewId");
		// String version = (String) cache.get("_version");
		return CreateDesignModel.getLuiViewLayoutPath(pageId, viewId);
	}

	public static String getEditorCompMetaXmlPath() {
		PaCache cache = PaCache.getInstance();
		String pageId = (String) cache.get("_pageId");
		String viewId = (String) cache.get("_viewId");
		// String version = (String) cache.get("_version");
		return CreateDesignModel.getLuiViewPartMetaPath(pageId, viewId);

	}

	public static String getEditorPageXmlPath() {
		PaCache cache = PaCache.getInstance();
		String pageId = (String) cache.get("_pageId");
		// String version = (String) cache.get("_version");
		String path = CreateDesignModel.getLuiPagePartMetaPath(pageId);
		return path;

	}

	// public static String getEditorPageXmlPath(String pageId) {
	// PaCache cache = PaCache.getInstance();
	// String version = (String) cache.get("_version");
	// if (version == null) {
	// String resourcePath = (String) cache.get("_resourceFolder");
	// resourcePath = resourcePath + "/lui/nodes/";
	// String compMetaPath = resourcePath + pageId + "/" + pageId +
	// ".page.meta.xml";
	// return compMetaPath;
	// }
	// ServletContext servletContext = LuiRuntimeContext.getServletContext();
	// String contextPath = servletContext.getRealPath("/");
	// String folderPath = contextPath + "/lui/nodes/" + pageId + "/" + version
	// + "/";
	// String path0 = folderPath + "/" + pageId + ".page.meta.xml";
	// return path0;
	//
	// }

	public static String getEditorAppXmlPath() {
		PaCache cache = PaCache.getInstance();
		String appId = (String) cache.get("_appId");
		String resourcePath = (String) cache.get("_resourceFolder");
		resourcePath = resourcePath + "/lui/apps/";
		String compMetaPath = resourcePath + "/" + appId + "/" + appId + ".app.xml";
		return compMetaPath;
	}

	public static String getEditorControllerPath(String javaFullClassName) {
		if (javaFullClassName == null)
			return null;
		PaCache cache = PaCache.getInstance();
		if (javaFullClassName.endsWith(".java")) {
			javaFullClassName = javaFullClassName.substring(0, javaFullClassName.indexOf(".java"));
		}
		String javaClassName = PaProjViewTreeController.getJavaClassName(javaFullClassName);
		String javaPackageName = PaProjViewTreeController.getPackageName(javaFullClassName);
		javaPackageName = javaPackageName.replace(".", "/");
		String resourceFolder = (String) cache.get("_javaFolder");
		javaPackageName = resourceFolder + "/" + javaPackageName;
		javaFullClassName = javaPackageName + "/" + javaClassName + ".java";
		return javaFullClassName;
	}

	public static String getEditorPageId() {
		String pageId = (String) PaCache.getInstance().get("_pageId");
		return pageId;
	}

	public static String getEditorViewId() {
		String viewId = (String) PaCache.getInstance().get("_viewId");
		return viewId;
	}

	public static String getEditorResourFolder() {
		String resourceFolder = (String) PaCache.getInstance().get("_resourceFolder");
		return resourceFolder;
	}

	// public static String getEditorResourceViewPath(String pageId, String
	// viewId) {
	// String resourceFolder = (String)
	// PaCache.getInstance().get("_resourceFolder");
	// if (StringUtils.isBlank(pageId)) {
	// pageId = (String) PaCache.getInstance().get("_pageId");
	// }
	// if (StringUtils.isBlank(viewId)) {
	// viewId = (String) PaCache.getInstance().get("_viewId");
	// }
	// if (StringUtils.isNotBlank(viewId)) {
	// return resourceFolder + "/lui/nodes/" + pageId + "/" + pageId + "." +
	// viewId + ".view.xml";
	// } else {
	// return resourceFolder + "/lui/nodes/" + pageId + "/" + pageId +
	// ".page.meta.xml";
	// }
	// }

	// public static String getEditorResourceLayOutPath(String pageId, String
	// viewId) {
	// String resourceFolder = (String)
	// PaCache.getInstance().get("_resourceFolder");
	// if (StringUtils.isBlank(pageId)) {
	// pageId = (String) PaCache.getInstance().get("_pageId");
	// }
	// if (StringUtils.isBlank(viewId)) {
	// viewId = (String) PaCache.getInstance().get("_viewId");
	// }
	// if (StringUtils.isNotBlank(viewId)) {
	// return resourceFolder + "/lui/nodes/" + pageId + "/" + pageId + "." +
	// viewId + ".layout.xml";
	// } else {
	// return resourceFolder + "/lui/nodes/" + pageId + "/" + pageId +
	// ".page.layout.xml";
	// }
	// }

}
