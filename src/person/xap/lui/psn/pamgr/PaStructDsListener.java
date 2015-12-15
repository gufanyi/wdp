/**
 * 
 */
package xap.lui.psn.pamgr;

import xap.lui.core.control.IWindowCtrl;
import xap.lui.core.event.ScriptEvent;
import xap.lui.core.layout.UIElement;
import xap.lui.core.layout.UIElementFinder;
import xap.lui.core.layout.UIPartMeta;
import xap.lui.core.model.AppSession;
import xap.lui.core.model.PagePartMeta;
import xap.lui.core.util.ObjSelectedUtil;
import xap.lui.psn.command.PaHelper;

/**
 * 页面导航数据集的加载监听类
 * @author wupeng1
 * @version 6.0 2011-8-29
 * @since 1.6
 */
public class PaStructDsListener implements IWindowCtrl{
 

	public void onSelect(ScriptEvent e) {
		PagePartMeta pagemeta = PaHelper.getPageMeta();
		UIPartMeta uimeta = PaHelper.getUIMeta();
		String id = AppSession.current().getParameter("id");
		UIElement ele = UIElementFinder.findElementById(uimeta, id);	
		if(ele != null){
			id = ObjSelectedUtil.toSelected(uimeta, pagemeta, ele);
			AppSession.current().getAppContext().addExecScript("onLoadStruct('"+id+"')");
		}
	}

}
