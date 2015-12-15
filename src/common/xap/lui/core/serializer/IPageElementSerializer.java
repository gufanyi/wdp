package xap.lui.core.serializer;

import xap.lui.core.layout.UIPartMeta;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.core.model.PagePartMeta;

public interface IPageElementSerializer {

	String serializeWidget(ViewPartMeta widget);

	String serializeUIMeta(UIPartMeta childUIMeta);

	String serializePageMeta(PagePartMeta pagemeta);

}
