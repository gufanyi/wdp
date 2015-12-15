package xap.lui.core.builder;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Arrays;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import xap.lui.core.common.ContextResourceUtil;
import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.exception.LuiParseException;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.logger.LuiLogger;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.model.PagePartMeta;
import xap.lui.core.model.PoolObjectManager;
import xap.lui.core.model.RequestLifeCycleContext;
import xap.lui.core.model.ViewPartConfig;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.core.util.HashFunctionLibrary;

public class PagePartMetaBuilder {

	public static PagePartMeta buildPageMeta(Window window) {
		String pageId = LuiRuntimeContext.getPageId();
		PagePartMeta pageMeta = (PagePartMeta) LuiRuntimeContext.getOriPagePartMeta(pageId);
		boolean isAbsloute = ContextResourceUtil.isAbsolutePath();
		String filePath = ContextResourceUtil.getLuiPagePartMetaPath(pageId);
		String lastModified = ContextResourceUtil.getLastModified(filePath, isAbsloute);
		boolean flag = false;
		if (pageMeta == null) {
			flag = true;
		} else {
			String oldModified = (String) pageMeta.getExtendAttributeValue(PagePartMeta.MODIFY_TS);
			if (lastModified.equals("-1") || !lastModified.equals(oldModified)) {
				flag = true;
			}
		}
		if (flag) {
			pageMeta = fetchPageMeta(pageId, lastModified);
			LuiRuntimeContext.getPagePartMetaPool().put(pageId, pageMeta);
			pageMeta = (PagePartMeta) pageMeta.clone();
		} else {
			pageMeta = (PagePartMeta) pageMeta.clone();
		}
		try {
			pageMeta.setWindow(window);
			ViewPartConfig[] widgetConfs = pageMeta.getViewPartConfs();
			for (int i = 0; i < widgetConfs.length; i++) {
				ViewPartConfig widgetConfig = widgetConfs[i];
				ViewPartMeta widget = ViewPartMetaBuilder.builderWidget(pageMeta, widgetConfig, pageId);
				widget.setId(widgetConfig.getId());
				pageMeta.addWidget(widget);
			}
		} catch (Exception e) {
			LuiLogger.error(e.getMessage(), e);
			if (e instanceof LuiRuntimeException)
				throw (LuiRuntimeException) e;
			throw new LuiRuntimeException(e.getMessage());
		}
		String[] wds = pageMeta.getWidgetIds();
		if (wds != null && wds.length > 0) {
			Arrays.sort(wds);
			String etag = "";
			for (int i = 0; i < wds.length; i++) {
				ViewPartMeta wd = pageMeta.getWidget(wds[i]);
				String uniId = (String) wd.getExtendAttributeValue(ViewPartMeta.UNIQUE_ID);
				String uniTs = (String) wd.getExtendAttributeValue(ViewPartMeta.UNIQUE_TS);
				etag += uniId + uniTs;
			}
			String hashCode = "" + HashFunctionLibrary.ELFHash(etag);
			pageMeta.setEtag(hashCode);
		}
		return pageMeta;
	}

	private static PagePartMeta fetchPageMeta(String pageId, String lastModified) {
		InputStream input = null;
		PagePartMeta pageMetaConf = null;
		String filePath = ContextResourceUtil.getLuiPagePartMetaPath(pageId);
		try {
			input = ContextResourceUtil.getResourceAsStream(filePath);
			if (input == null) {
				throw new LuiParseException("从路径:" + filePath + ",没有找到PageMeta");
			}
			pageMetaConf = PagePartMeta.parse(input);
			if (pageMetaConf == null) {
				throw new LuiParseException("获取PageMeta时出错，pageid=" + pageId);
			}
			pageMetaConf.setExtendAttribute(PagePartMeta.MODIFY_TS, lastModified);
			pageMetaConf.setFoldPath(ContextResourceUtil.getLuiFolderPath(pageId));
		} catch (Exception e) {
			throw new LuiRuntimeException(e.getMessage());
		} finally {
			IOUtils.closeQuietly(input);
		}
		return pageMetaConf;

	}

	public static PagePartMeta createPageMeta(String pageId) {
		InputStream input = null;
		try {
			String path = ContextResourceUtil.getLuiPagePartMetaPath(pageId);
			input = new FileInputStream(new File(path));
		} catch (Throwable e) {
			LuiLogger.error(e.getMessage(), e);
		}
		return createPageMeta(input);
	}

	private static PagePartMeta createPageMeta(InputStream input) {
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
					String path = ContextResourceUtil.getLuiViewpartMetaPath(oldPm.getId(), widgetConfig.getId());
					input = new FileInputStream(new File(path));
					widget = ViewPartMeta.parse(input);
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
		RequestLifeCycleContext.get().setPhase(phase);
		return oldPm;
	}
}
