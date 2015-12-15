/**
 * 
 */
package xap.lui.core.services;

import xap.lui.core.layout.UIPartMeta;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.core.model.PagePartMeta;

/**
 * @author wupeng1
 * @version 6.0 2011-10-11
 * @since 1.6
 */
public interface IEditorSaveHandler {
	public void save(PagePartMeta pm, UIPartMeta meta, ViewPartMeta lwidget, String pagemetaPath);
}
