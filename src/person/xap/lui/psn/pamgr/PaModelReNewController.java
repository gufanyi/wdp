package xap.lui.psn.pamgr;

import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.control.IWindowCtrl;
import xap.lui.core.dataset.ComboData;
import xap.lui.core.event.ScriptEvent;
import xap.lui.core.model.AppSession;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.core.refrence.IRefNode;

/**
 * 刷新个性化设计区的模型
 * @author licza
 *
 */
public class PaModelReNewController implements IWindowCtrl {
	public void handlerEvent(ScriptEvent event) {
		String id = AppSession.current().getParameter("id");
		String type = AppSession.current().getParameter("typo");
		String widgetid = AppSession.current().getParameter("wid");
		ViewPartMeta wd = LuiRuntimeContext.getWebContext().getPageMeta().getWidget(widgetid);
		if(PaModelOperateController.TYPE_COMBODATA.equals(type)){
			ComboData cd = wd.getViewModels().getComboData(id);
			wd.getViewModels().addComboData(cd);
		}
		
		if(PaModelOperateController.TYPE_REFNODE.equals(type)){
			IRefNode refnode = wd.getViewModels().getRefNode(id);
			wd.getViewModels().addRefNode(refnode);
		}
	}
}
