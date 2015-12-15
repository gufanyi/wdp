package xap.lui.core.builder;

import java.io.InputStream;

import org.apache.commons.lang.StringUtils;

import xap.lui.core.common.ContextResourceUtil;
import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.exception.LuiParseException;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.logger.LuiLogger;
import xap.lui.core.model.PagePartMeta;
import xap.lui.core.model.PoolObjectManager;
import xap.lui.core.model.ViewPartConfig;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.core.util.ClassUtil;

public class ViewPartMetaBuilder {
	public static ViewPartMeta builderWidget(PagePartMeta pagePart, ViewPartConfig conf, String pageId) {
		String cacheId = pageId + "_$_" + conf.getId();
		ViewPartMeta viewPart = null;
		String refViewId = conf.getRefId();
		if (StringUtils.isNotBlank(refViewId) && conf.getRefId().startsWith("..")) {
			refViewId = conf.getRefId().substring(3);
			conf.setSrcFolder(refViewId);
			viewPart = PoolObjectManager.getWidget(refViewId);
			if (viewPart == null) {
				throw new LuiRuntimeException("can not find widget from pool:" + refViewId);
			}
			viewPart.setRefId(conf.getRefId());
		} else {
			viewPart = (ViewPartMeta) LuiRuntimeContext.getOriViewPartMeta(pageId, conf.getId());
			String filePath = ContextResourceUtil.getLuiViewpartMetaPath(pageId, conf.getId());
			boolean isAbsloute = ContextResourceUtil.isAbsolutePath();
			String lastModified = ContextResourceUtil.getLastModified(filePath, isAbsloute);
			boolean flag = false;
			if (viewPart == null) {
				flag = true;
			} else {
				String oldModified = (String) viewPart.getExtendAttributeValue(ViewPartMeta.MODIFY_TS);
				if (lastModified.equals("-1") || !lastModified.equals(oldModified)) {
					flag = true;
				}
			}
			if (flag) {
				viewPart = getViewPartFromFile(pagePart, conf, filePath, lastModified);
				LuiRuntimeContext.getViewPartMetaPool().put(cacheId, viewPart);
				viewPart = (ViewPartMeta) viewPart.clone();
			} else {
				viewPart = (ViewPartMeta) viewPart.clone();
			}
		}
		if (conf.getExtendMap() != null) {
			viewPart.getExtendMap().putAll(conf.getExtendMap());
		}
		ViewPartMeta resultWidget = fetchDynamicViewPart(pagePart, viewPart);
		resultWidget.setRefId(conf.getRefId());
		return resultWidget;
	}

	private static ViewPartMeta fetchDynamicViewPart(PagePartMeta pagePart, ViewPartMeta viewPart) {
		try {
			ViewPartMeta result = null;
			ViewPartProvider provider = getViewPartProvider(viewPart);
			if (provider != null) {
				ViewPartMeta otherWidget = provider.buildWidget(pagePart, viewPart, null, viewPart.getId());
				otherWidget.setIsCustom(false);
				ViewPartMergeUtil.merge(otherWidget, viewPart);
				result = otherWidget;
			} else {
				result = viewPart;
			}
			return result;
		} catch (Exception e) {
			LuiLogger.error(e.getMessage(), e);
			if (e instanceof LuiRuntimeException)
				throw new LuiRuntimeException(e);
			throw new LuiParseException(e.getMessage());
		}
	}

	private static ViewPartMeta getViewPartFromFile(PagePartMeta pagePart, ViewPartConfig widgetConfig, String filePath, String lastModified) {
		InputStream input = null;
		ViewPartMeta widget = null;
		try {
			input = ContextResourceUtil.getResourceAsStream(filePath);
			if (input == null) {
				throw new LuiParseException("没有找到Widget配置，pageId=" + pagePart.getId() + ",viewId=" + widgetConfig.getId());
			}
			widget = ViewPartMeta.parse(input);
			widget.setId(widgetConfig.getId());
			if (!lastModified.equals("-1")) {
				widget.setExtendAttribute(ViewPartMeta.MODIFY_TS, lastModified);
				widget.setExtendAttribute(ViewPartMeta.UNIQUE_TS, lastModified);
			}
		} catch (Throwable e) {
			throw e;
		} finally {
			try {
				if (input != null)
					input.close();
			} catch (Exception e) {
				LuiLogger.error(e.getMessage(), e);
			}
		}
		return widget;
	}

	private static ViewPartProvider getViewPartProvider(ViewPartMeta confWidget) {
		String providerClass = confWidget.getProvider();
		ViewPartProvider provider = null;
		if (StringUtils.isNotBlank(providerClass)) {
			provider = (ViewPartProvider) ClassUtil.newInstance(providerClass);
		}
		return provider;
	}

}
