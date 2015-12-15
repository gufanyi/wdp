package xap.lui.core.builder;

import org.apache.commons.lang.StringUtils;

import xap.lui.core.cache.PaCache;
import xap.lui.core.common.ContextResourceUtil;
import xap.lui.core.common.LuiRenderContext;
import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.layout.UIPartMeta;
import xap.lui.core.layout.UIViewPart;
import xap.lui.core.model.PagePartMeta;
import xap.lui.core.parser.UIMetaParserUtil;
import xap.lui.psn.designer.CreateDesignModel;

public class UIPartMetaDftBuilder implements UIPartMetaBuilder {
	public UIPartMeta buildUIMeta(PagePartMeta pm) {
		UIMetaParserUtil parserUtil = new UIMetaParserUtil(false);
		parserUtil.setPagemeta(pm);
		UIPartMeta meta = null;
		if (ContextResourceUtil.isFreeBillOrPersona(LuiRuntimeContext.getResourceFrom())) {
			String folderPath = pm.getFoldPath();
			String pageId= LuiRuntimeContext.getPageId();
			folderPath=ContextResourceUtil.getLuiFolderPath(pageId);
			pm.setFoldPath(folderPath);
			meta = parserUtil.parseUIMeta(folderPath, pm.getId(), null);
		} else {
			meta = parserUtil.parseUIMeta(pm.getId(), null);
		}
		LuiRenderContext.current().setUiPartMeta(meta);
		return meta;
	}

	public UIPartMeta buildUIMeta(PagePartMeta pm, String pageId, String viewId) {
		if (LuiRuntimeContext.isEditMode()) {
			UIPartMeta uiPartMeta = null;
			if (StringUtils.isNotBlank(viewId)) {
				PagePartMeta pagMeta = PaCache.getEditorPagePartMeta();
				uiPartMeta = CreateDesignModel.createDesignUIMeta(pagMeta, pageId, viewId);
				if (uiPartMeta != null) {
					UIViewPart uiViewPart = (UIViewPart) uiPartMeta.getElement();
					return uiViewPart.getUimeta();
				}
			}
			uiPartMeta = PaCache.getEditorUIPartMeta();
			if (uiPartMeta != null) {
				return uiPartMeta;
			}
		}
		UIMetaParserUtil parserUtil = new UIMetaParserUtil(false);
		parserUtil.setPagemeta(pm);
		UIPartMeta meta = parserUtil.parseUIMeta(pm.getId(), viewId);
		LuiRenderContext.current().setUiPartMeta(meta);
		return meta;
	}
}
