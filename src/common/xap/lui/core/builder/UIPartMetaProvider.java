package xap.lui.core.builder;

import xap.lui.core.layout.UIPartMeta;
import xap.lui.core.model.ViewPartMeta;

/**
 * Widget UI构建器
 *
 */
public interface UIPartMetaProvider {
	public UIPartMeta getDefaultUIMeta(ViewPartMeta widget);
}
