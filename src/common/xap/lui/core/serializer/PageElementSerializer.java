package xap.lui.core.serializer;

import xap.lui.core.design.UIMetaToXml;
import xap.lui.core.layout.UIPartMeta;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.core.model.PagePartMeta;
import xap.lui.core.serializer.IPageElementSerializer;
import xap.lui.core.serializer.PageMetaToXml;

public class PageElementSerializer implements IPageElementSerializer{

	@Override
	public String serializeUIMeta(UIPartMeta childUIMeta) {
		return UIMetaToXml.toString(childUIMeta);
	}

	@Override
	public String serializeWidget(ViewPartMeta widget) {
		return null;
		//return WidgetToXml.toString(widget);
	}

	@Override
	public String serializePageMeta(PagePartMeta pagemeta) {
		return PageMetaToXml.toString(pagemeta);
	}

}
