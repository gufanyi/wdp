package xap.lui.psn.services;

import xap.lui.core.control.ModePhase;
import xap.lui.core.layout.UIPartMeta;
import xap.lui.core.model.PagePartMeta;

/**
 * 个性化保存时操作
 * 
 * @author licza
 * 
 */
public interface IPaSaveHandler {
	/**
	 * 保存前操作
	 * 
	 * @param pagemeta
	 * @param um
	 */
	void before(PagePartMeta pagemeta, UIPartMeta um);

	/**
	 * 保存后操作
	 * 
	 * @param pagemeta
	 * @param um
	 */
	void after(PagePartMeta pagemeta, UIPartMeta um);
	
	/**
	 * 支持模式
	 * @param mode
	 * @return
	 */
	boolean accept(ModePhase mode);
	
}
