package xap.lui.core.services;
import xap.lui.core.layout.UIPartMeta;
import xap.lui.core.model.PagePartMeta;
public interface IPaEditorService {
	public UIPartMeta getOriUIMeta(String pageId, String sessionId);
	public void setOriUIMeta(String pageId, String sessionId, UIPartMeta uiMeta);
	public PagePartMeta getOriPageMeta(String pageId, String sessionId);
	public void setOriPageMeta(String pageId, String sessionId, PagePartMeta pageMeta);
	public void clearSessionCache(String sessionId);
	
	
//	public UIPartMeta getOutUIMeta(String pageId, String sessionId);
//	public void setOutUIMeta(String pageId, String sessionId, UIPartMeta uiMeta);
//	public PagePartMeta getOutPageMeta(String pageId, String sessionId);
//	public void setOutPageMeta(String pageId, String sessionId, PagePartMeta pageMeta);
	
}
