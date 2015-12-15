package xap.lui.core.builder;
import xap.lui.core.layout.UIPartMeta;
import xap.lui.core.model.PagePartMeta;
public interface UIPartMetaBuilder {
	public UIPartMeta buildUIMeta(PagePartMeta pagemeta);
	public UIPartMeta buildUIMeta(PagePartMeta pagemeta, String pageId, String widgetId);
}
