package xap.lui.core.builder;

import xap.lui.core.cache.CacheMgr;
import xap.lui.core.cache.LUICache;
import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.constant.WebConstant;
import xap.lui.core.model.IUIPartMeta;
import xap.lui.core.model.PagePartMeta;

public class Window extends BaseWindow {
	public Window() {
		super();
	}

	@Override
	protected IUIPartMeta createUIMeta(PagePartMeta pm) {
		return getUIMetaBuilder().buildUIMeta(pm);
	}

	protected UIPartMetaBuilder getUIMetaBuilder() {
		return new UIPartMetaDftBuilder();
	}

	@Override
	protected void initUIMetaStruct() {
		super.initUIMetaStruct();
	}

	@Override
	protected void initPageMetaStruct() {
		super.initPageMetaStruct();
		this.initPageMetaListeners();
	}

	@Override
	protected void afterInternalInitialize() {
		boolean editMode = LuiRuntimeContext.isEditMode();
		if (editMode) {
		}

		super.afterInternalInitialize();
	}

	/**
	 * 2011-8-17 上午11:42:17 renxh des：初始化 listeners ，如果是编辑态 则删除原来的监听，并加入新的监听
	 */
	private void initPageMetaListeners() {
		boolean editMode = LuiRuntimeContext.isEditMode();
		if (editMode) {
			PagePartMeta pageMeta = getPageMeta();
			LUICache session = CacheMgr.getSessionCache();
			// 外部的uimeta 和 pageMeta
			String pageId = LuiRuntimeContext.getWebContext().getPageId();
			session.put(pageId + WebConstant.SESSION_PAGEMETA_OUT, pageMeta.clone());
			session.put(pageId + WebConstant.SESSION_UIMETA_OUT, getUIMeta().doClone());
		}
	}

	public IUIPartMeta getUIMeta() {
		return getWebSesssion().getUIMeta();
	}

	public String getEtag() {
		IUIPartMeta um = getUIMeta();
		if (um != null && um.getEtag() != null)
			return super.getEtag() + um.getEtag();
		return super.getEtag();
	}
}
