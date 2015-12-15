package xap.lui.core.services;
import xap.lui.core.cache.CacheMgr;
import xap.lui.core.cache.PaCache;
import xap.lui.core.constant.WebConstant;
import xap.lui.core.layout.UIPartMeta;
import xap.lui.core.logger.LuiLogger;
import xap.lui.core.model.PagePartMeta;
public class PaEditorServiceImpl implements IPaEditorService {
	@Override
	public PagePartMeta getOriPageMeta(String pageId, String sessionId) {
		String key = sessionId + pageId + WebConstant.SESSION_PAGEMETA_ORI;
		PagePartMeta pageMeta = (PagePartMeta) PaCache.getInstance().get(key);
		return pageMeta;
	}
	@Override
	public void setOriPageMeta(String pageId, String sessionId, PagePartMeta pageMeta) {
		String key = sessionId + pageId + WebConstant.SESSION_PAGEMETA_ORI;
		PaCache.getInstance().pub(key, pageMeta);
	}
	@Override
	public UIPartMeta getOriUIMeta(String pageId, String sessionId) {
		String key = sessionId + pageId + WebConstant.SESSION_UIMETA_ORI;
		UIPartMeta uiMeta = (UIPartMeta) PaCache.getInstance().get(key);
		return uiMeta;
	}
	@Override
	public void setOriUIMeta(String pageId, String sessionId, UIPartMeta uiMeta) {
		// ILuiCache sessionCache = LuiCacheManager.getSessionCache(sessionId);
		// LuiCacheManager.getStrongCache("eclipse",
		// "design").put(pageId+sessionId+WebConstant.SESSION_UIMETA_ORI,
		// uiMeta);
		String key = sessionId + pageId + WebConstant.SESSION_UIMETA_ORI;
		PaCache.getInstance().pub(key, uiMeta);
		// sessionCache.put(pageId + WebConstant.SESSION_UIMETA_ORI, uiMeta);
	}
	
	@Override
	public void clearSessionCache(String sessionId) {
		try {
			CacheMgr.removeSessionCache(sessionId);
		} catch (Exception e) {
			LuiLogger.error(e);
		}
	}
	
	
//	@Override
//	public PagePartMeta getOutPageMeta(String pageId, String sessionId) {
//		LUICache sessionCache = CacheMgr.getSessionCache(sessionId);
//		PagePartMeta pageMeta = (PagePartMeta) sessionCache.get(pageId + WebConstant.SESSION_PAGEMETA_OUT);
//		return pageMeta;
//	}
//	@Override
//	public void setOutPageMeta(String pageId, String sessionId, PagePartMeta pageMeta) {
//		LUICache sessionCache = CacheMgr.getSessionCache(sessionId);
//		sessionCache.put(pageId + WebConstant.SESSION_PAGEMETA_OUT, pageMeta);
//	}
//	@Override
//	public UIPartMeta getOutUIMeta(String pageId, String sessionId) {
//		LUICache sessionCache = CacheMgr.getSessionCache(sessionId);
//		UIPartMeta uiMeta = (UIPartMeta) sessionCache.get(pageId + WebConstant.SESSION_UIMETA_OUT);
//		return uiMeta;
//	}
//	@Override
//	public void setOutUIMeta(String pageId, String sessionId, UIPartMeta uiMeta) {
//		LUICache sessionCache = CacheMgr.getSessionCache(sessionId);
//		sessionCache.put(pageId + WebConstant.SESSION_UIMETA_OUT, uiMeta);
//	}

}
