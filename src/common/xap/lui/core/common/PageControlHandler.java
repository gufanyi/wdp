package xap.lui.core.common;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import xap.lui.core.builder.BaseWindow;
import xap.lui.core.builder.Window;
import xap.lui.core.cache.CacheMgr;
import xap.lui.core.cache.LUICache;
import xap.lui.core.constant.WebConstant;
import xap.lui.core.control.ModePhase;
import xap.lui.core.control.ResourceFrom;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.model.RequestLifeCycleContext;
import xap.lui.core.plugins.ILuiPaltformExtProvier;
import xap.lui.core.plugins.LuiPaltformContranier;
import xap.lui.core.refrence.AppRefDftWindow;
import xap.lui.core.render.FreeMarkerUtil;
import xap.lui.core.util.ClassUtil;

public class PageControlHandler implements ControlHandler {
	protected static final String MODEL = "model";

	@Override
	public void handle(HttpServletRequest req, HttpServletResponse res, String path) throws Exception {
		try {
			RequestLifeCycleContext appCtx = new RequestLifeCycleContext();
			RequestLifeCycleContext.set(appCtx);
			appCtx.setPhase(LifeCyclePhase.render);
			addRequestReferer(req);
			String pageId = req.getParameter("pageId");
			if (PageControlHandler.isGetCache(pageId)) {
				LUICache cache = CacheMgr.getStrongCache(WebConstant.LUI_CORE_CACHE, "design");
				String cacheStr = (String) cache.get(pageId);
				if (StringUtils.isNotBlank(cacheStr)) {
					res.setContentType("text/html;charset=utf-8");
					res.setCharacterEncoding("utf-8");
					res.getOutputStream().write(cacheStr.getBytes("utf-8"));
					return;
				}
			}
			if (path.lastIndexOf("/") != -1 && builderPageModel(req, res)) {
				Map<String, Object> params = this.initRenderMap(req, pageId);
				String str = FreeMarkerUtil.processCreateHtmlName("luipage", params);
				if (PageControlHandler.isAddCache(pageId)) {
					LUICache cache = CacheMgr.getStrongCache(WebConstant.LUI_CORE_CACHE, "design");
					cache.put(pageId, str);
				}
				res.setContentType("text/html;charset=utf-8");
				res.setCharacterEncoding("utf-8");
				res.getOutputStream().write(str.getBytes("utf-8"));
				return;
			} else {
				RequestDispatcher dispatcher = req.getSession().getServletContext().getRequestDispatcher(path);
				dispatcher.forward(req, res);
			}
		} finally {
			RequestLifeCycleContext.set(null);
		}
	}

	public LuiWebContext getWebContext() {
		return LuiRuntimeContext.getWebContext();
	}

	public void getClientSession() {
		// ClientSession clientSession = new ClientSession();
		// String appUniqueId = getWebContext().getAppUniqueId();
	}

	public Map<String, Object> initRenderMap(HttpServletRequest req, String pageId) {
		String nodeId = (String) pageId;
		String themeId = LuiRuntimeContext.getThemeId();
		String appPath = LuiRuntimeContext.getRootPath();
		String nodePath = "/lui/nodes/" + nodeId;
		String nodeThemePath = nodePath + "/themes/" + themeId;
		String nodeStyleSheetPath = nodeThemePath + "/stylesheets";
		String nodeImagePath = nodeThemePath + "/images";
		String frameGlobalPath = appPath + "/platform/script";
		String themePath = appPath + "/platform/theme/" + themeId;
		BaseWindow pageModel = (BaseWindow) req.getAttribute("pageModel");
		pageModel.getPageMeta().setNodeImagePath(nodeImagePath);
		pageModel.getPageMeta().setPlatformImagePath(themePath + "/global/images");
		pageModel.getPageMeta().setThemePath(themePath);
		LuiRenderContext.current().setPagePartMeta(pageModel.getPageMeta());
		LuiRenderContext.current().setUiPartMeta(pageModel.getUIPartMeta());
		LuiRenderContext.current().setPhase(LifeCyclePhase.render);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(WebConstant.ROOT_PATH, appPath);
		params.put(WebConstant.NODE_PATH, nodePath);
		params.put(WebConstant.NODE_THEME_PATH, nodeThemePath);
		params.put(WebConstant.NODE_STYLE_PATH, nodeStyleSheetPath);
		params.put(WebConstant.NODE_IMAGE_PATH, nodeImagePath);
		params.put(WebConstant.NODE_ID, nodeId);
		params.put(WebConstant.FRAME_GLOBAL_PATH, frameGlobalPath);
		params.put(WebConstant.THEME_PATH, themePath);
		params.put("pageModel", pageModel);
		ILuiPaltformExtProvier[] providers = LuiPaltformContranier.getInstance().getProvideres();
		List<String> resoucesList = new ArrayList<String>();
		for (int i = 0; i < providers.length; i++) {
			ILuiPaltformExtProvier inner = providers[i];
			String[] resouoreName = inner.getResoucesName();
			if (resouoreName != null && resouoreName.length != 0) {
				for (String str : resouoreName) {
					if (StringUtils.isNotBlank(str)) {
						resoucesList.add(str);
					}
				}
			}
		}
		params.put("luiextcomp", resoucesList);
		String includeCssName = "lui/nodes/" + pageId + "/" + pageId + ".include.css";
		File file0 = ContextResourceUtil.getCntWebAppFile(includeCssName);
		if (file0 != null) {
			params.put("includecss", file0.getName());
		}
		String includeJsName = "lui/nodes/" + pageId + "/" + pageId + ".include.js";
		File file1 = ContextResourceUtil.getCntWebAppFile(includeJsName);
		if (file1 != null) {
			params.put("includejs", file1.getName());
		}
		LuiWebSession session = LuiRuntimeContext.getWebContext().getPageWebSession();
		String modePhase = session.getOriginalParameter(LuiRuntimeContext.MODEPHASE);
		if (ContextResourceUtil.isDesignerNode(pageId)) {
			params.put(LuiRuntimeContext.MODEPHASE, "runtime");
		} else {
			if ("eclipse".equalsIgnoreCase(modePhase)) {
				params.put(LuiRuntimeContext.MODEPHASE, "eclipse");
			} else {
				params.put(LuiRuntimeContext.MODEPHASE, "runtime");
			}
		}
		return params;
	}

	private void addRequestReferer(HttpServletRequest req) {
		String referer = req.getHeader(LuiWebSession.REFERER);
		if (StringUtils.isNotBlank(referer)) {
			LuiRuntimeContext.getWebContext().getPageWebSession().setAttribute(LuiWebSession.REFERER, referer);
		}
	}

	public static boolean isGetCache(String pageId) {
		if (!LuiRuntimeContext.isStartCache) {
			return false;
		}
		ModePhase phase = LuiRuntimeContext.getModePhase();
		if (ModePhase.normal.equals(phase)) {
			return true;
		}
		switch (phase) {
		case normal:
			return true;
		case eclipse:
			if (ContextResourceUtil.isDesignerNode(pageId)) {
				return true;
			} else {
				return false;
			}
		case nodedef:
			if (ContextResourceUtil.isDesignerNode(pageId)) {
				return true;
			} else {
				return false;
			}
		case persona:
			if (ContextResourceUtil.isDesignerNode(pageId)) {
				return true;
			} else {
				return false;
			}
		}
		return true;
	}

	public static boolean isAddCache(String pageId) {
		if (!LuiRuntimeContext.isStartCache) {
			return false;
		}
		ModePhase phase = LuiRuntimeContext.getModePhase();
		if (ModePhase.normal.equals(phase)) {
			return true;
		}
		switch (phase) {
		case normal:
			return true;
		case eclipse:
			if (ContextResourceUtil.isDesignerNode(pageId)) {
				return true;
			} else {
				return false;
			}
		case nodedef:
			if (ContextResourceUtil.isDesignerNode(pageId)) {
				return true;
			} else {
				return false;
			}
		case persona:
			if (ContextResourceUtil.isDesignerNode(pageId)) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	private boolean builderPageModel(HttpServletRequest req, HttpServletResponse res) {
		beforeInitPageModel(req, res);
		String className = (String) req.getAttribute(MODEL);
		if (className == null || className.equals("")) {
			className = req.getParameter(MODEL);
		}
		String pageId = LuiRuntimeContext.getPageId();
		ResourceFrom resouceFrom = LuiRuntimeContext.getResourceFrom();
		// 这个地方用来处理属性文件表
		Properties props = LuiRuntimeContext.getOriPropByPageId(pageId);
		if (props == null) {
			String nodePropPath = ContextResourceUtil.getLuiPropertyPath(pageId);
			if (ContextResourceUtil.isFreeBillOrPersona(resouceFrom)) {
				props = PageControlHandler.loadNodePropertie(nodePropPath, true);
			} else {
				props = PageControlHandler.loadNodePropertie(nodePropPath, false);
			}
			LuiRuntimeContext.getPagePropMetaPool().put(pageId, props);
		}

		if (StringUtils.isBlank(className)) {
			className = props.getProperty(MODEL);
		}
		if (StringUtils.isBlank(className)) {
			className = Window.class.getName();
		}
		BaseWindow model = (BaseWindow) ClassUtil.newInstance(className);
		if (props != null) {
			model.setProps(props);
		}
		model.internalInitialize();
		String newEtag = model.getEtag();
		req.setAttribute("pageModel", model);
		if (newEtag != null) {
			res.addHeader("ETag", newEtag);
		}
		return true;
	}

	public static Properties loadNodePropertie(String properties, boolean isAbsloute) {
		Properties nodeProps = new Properties();
		InputStream input = null;
		try {
			input = ContextResourceUtil.getResourceAsStream(properties, isAbsloute);
			if (input == null) {
				return nodeProps;
			}
			nodeProps.load(input);
		} catch (IOException e) {
		} finally {
			IOUtils.closeQuietly(input);
		}
		return nodeProps;
	}

	private void beforeInitPageModel(HttpServletRequest req, HttpServletResponse res) {
		String pageId = LuiRuntimeContext.getWebContext().getPageId();
		if (pageId.equals("reference")) {
			String isReference = (String) LuiRuntimeContext.getWebContext().getPageWebSession().getOriginalParameter("isReference");
			if (StringUtils.isNotBlank(isReference) && isReference.equals("true")) {
				req.setAttribute(MODEL, AppRefDftWindow.class.getName());
			}
		}
	}
}
