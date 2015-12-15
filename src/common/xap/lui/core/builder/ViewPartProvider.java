package xap.lui.core.builder;

import java.util.Map;

import xap.lui.core.model.ViewPartMeta;
import xap.lui.core.model.PagePartMeta;


/**
 * Widget 内容构建器
 *
 */
public interface ViewPartProvider {
	public ViewPartMeta buildWidget(PagePartMeta pm, ViewPartMeta conf, Map<String, Object> paramMap, String currWidgetId);
}
