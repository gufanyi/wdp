package xap.lui.core.builder;

import xap.lui.core.model.PagePartMeta;

/**
 * 空Pagemeta实现
 *
 */
public class EmptyWindow extends Window {

	@Override
	protected PagePartMeta createPageMeta() {
		return new PagePartMeta();
	}

}
