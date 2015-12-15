package xap.lui.psn.designer;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.servlet.ServletContext;

import org.apache.commons.lang.StringUtils;

import xap.lui.core.cache.PaCache;
import xap.lui.core.common.ContextResourceUtil;
import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.control.ModePhase;
import xap.lui.core.layout.UIPartMeta;
import xap.lui.core.layout.UIViewPart;
import xap.lui.core.logger.LuiLogger;
import xap.lui.core.model.Application;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.model.PagePartMeta;
import xap.lui.core.model.PoolObjectManager;
import xap.lui.core.model.RequestLifeCycleContext;
import xap.lui.core.model.ViewPartConfig;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.core.parser.UIMetaParserUtil;
import xap.lui.core.services.IPaEditorService;
import xap.lui.core.services.PaEditorServiceImpl;

public class CreateDesignModel {

	public static String getLuiFolderPath(String pageId) {
		ServletContext servletContext = LuiRuntimeContext.getServletContext();
		String contextPath = servletContext.getRealPath("/");
		ModePhase modePhase = LuiRuntimeContext.getModePhase();
		String folderPath = null;
		if (ModePhase.nodedef.equals(modePhase)) {
			String version = PaCache.getEditorVersion();
			folderPath = contextPath + "/lui/nodes/" + pageId + "/" + version ;
		}
		if (ModePhase.persona.equals(modePhase)) {
			String pesonaCode = LuiRuntimeContext.getPersonaCode();
			folderPath = contextPath + "/lui/nodes/" + pageId + "/" + pesonaCode;

		}
		if (ModePhase.eclipse.equals(modePhase)) {
			String resourcePath = (String) PaCache.getEditorResourFolder();
			folderPath = resourcePath + "/lui/nodes/" + pageId;
		}
		return folderPath;
	}

	public static String getLuiPropertyPath(String pageId) {
		return CreateDesignModel.getLuiFolderPath(pageId) + "/node.properties";
	}

	public static String getLuiPageLayOutPath(String pageId) {
		return CreateDesignModel.getLuiViewLayoutPath(pageId, null);
	}

	public static String getLuiViewLayoutPath(String pageId, String viewId) {
		String folderPath = CreateDesignModel.getLuiFolderPath(pageId);
		String path0 = null;
		if (StringUtils.isNotBlank(viewId)) {
			path0 = folderPath + "/" + pageId + "." + viewId + ".layout.xml";
		} else {
			path0 = folderPath + "/" + pageId + ".page.layout.xml";
		}
		return path0;
	}

	public static String getLuiPagePartMetaPath(String pageId) {
		return CreateDesignModel.getLuiViewPartMetaPath(pageId, null);
	}

	public static String getLuiViewPartMetaPath(String pageId, String viewId) {
		String folderPath = CreateDesignModel.getLuiFolderPath(pageId);
		String path0 = null;
		if (StringUtils.isNotBlank(viewId)) {
			path0 = folderPath + "/" + pageId + "." + viewId + ".view.xml";
		} else {
			path0 = folderPath + "/" + pageId + ".page.meta.xml";
		}
		return path0;
	}

	public static UIPartMeta createDesignUIMeta(PagePartMeta pageMeta, String pageId, String viewId) {
		LifeCyclePhase phase = RequestLifeCycleContext.get().getPhase();
		RequestLifeCycleContext.get().setPhase(LifeCyclePhase.design);
		UIPartMeta uimeta = null;
		UIMetaParserUtil parserUtil = new UIMetaParserUtil(true);
		String folderPath = CreateDesignModel.getLuiFolderPath(pageId);

		if (viewId == null) {
			parserUtil.setPagemeta(pageMeta);
			uimeta = parserUtil.parseUIMeta(folderPath+"/", pageId, null);
		} else {
			uimeta = new UIPartMeta();
			UIViewPart uiViewPart = new UIViewPart();
			uiViewPart.setId(viewId);
			uimeta.setElement(uiViewPart);
			parserUtil.setPagemeta(pageMeta);
			UIPartMeta viewPartUiMeta = parserUtil.parseUIMeta(folderPath+"/", pageId, viewId);
			uiViewPart.setUimeta(viewPartUiMeta);
		}
		IPaEditorService ipaService = new PaEditorServiceImpl();
		String sessionId = (String) PaCache.getInstance().get("eclipse_sesionId");
		ipaService.setOriUIMeta(pageId, sessionId, uimeta);
		RequestLifeCycleContext.get().setPhase(phase);
		return uimeta;
	}

	public static PagePartMeta createDesignPageMeta(String pageId) {

		InputStream input = null;
		try {
			String path0 = CreateDesignModel.getLuiPagePartMetaPath(pageId);
			input = new FileInputStream(new File(path0));
		} catch (Throwable e) {
			LuiLogger.error(e.getMessage(), e);
		}

		return CreateDesignModel.createDesignPageMeta(input);
	}

	public static PagePartMeta createDesignPageMeta(InputStream input) {
		LifeCyclePhase phase = RequestLifeCycleContext.get().getPhase();
		RequestLifeCycleContext.get().setPhase(LifeCyclePhase.design);
		PagePartMeta oldPm = PagePartMeta.parse(input);
		ViewPartConfig[] widgetConfs = oldPm.getViewPartConfs();
		ViewPartMeta widget = null;
		for (int i = 0; i < widgetConfs.length; i++) {
			ViewPartConfig widgetConfig = widgetConfs[i];
			try {
				if (StringUtils.startsWith(widgetConfig.getRefId(), "..")) {
					widget = PoolObjectManager.getWidget(widgetConfig.getId());
				} else {
				    String fiePath=CreateDesignModel.getLuiViewPartMetaPath(oldPm.getId(), widgetConfig.getId());
					widget = ViewPartMeta.parse(ContextResourceUtil.getResourceAsStream(fiePath, true));
				}
			} catch (Throwable e) {
				LuiLogger.error(e.getMessage(), e);
			}
			if (widget != null) {
				widget.setId(widgetConfig.getId());
				oldPm.addWidget(widget);
				widget = null;
			}

		}
		PagePartMeta pageMeta = (PagePartMeta) oldPm.clone();
		IPaEditorService ipaService = new xap.lui.core.services.PaEditorServiceImpl();
		String sessionId = (String) PaCache.getInstance().get("eclipse_sesionId");
		ipaService.setOriPageMeta(oldPm.getId(), sessionId, pageMeta);
		RequestLifeCycleContext.get().setPhase(phase);
		return pageMeta;
	}

	public static Application createDesignApp(String appId) {

		InputStream input = null;
		try {
			String path0 = PaCache.getEditorAppXmlPath();
			input = new FileInputStream(new File(path0));
		} catch (Throwable e) {
			LuiLogger.error(e.getMessage(), e);
		}
		return CreateDesignModel.createDesignApp(input);
	}

	public static Application createDesignApp(InputStream input) {
		LifeCyclePhase phase = RequestLifeCycleContext.get().getPhase();
		RequestLifeCycleContext.get().setPhase(LifeCyclePhase.design);

		Application oldApp = Application.parse(input);
		PaCache.getInstance().pub("_editApp", oldApp);
		RequestLifeCycleContext.get().setPhase(phase);
		return oldApp;
	}
}
