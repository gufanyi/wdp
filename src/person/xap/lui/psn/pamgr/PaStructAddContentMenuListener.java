package xap.lui.psn.pamgr;

import xap.lui.core.cache.PaCache;
import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.comps.MenuItem;
import xap.lui.core.control.IWindowCtrl;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.Row;
import xap.lui.core.event.MouseEvent;
import xap.lui.core.layout.UIElement;
import xap.lui.core.layout.UIElementFinder;
import xap.lui.core.layout.UILayoutPanel;
import xap.lui.core.layout.UIPartMeta;
import xap.lui.core.model.AppSession;
import xap.lui.core.model.PagePartMeta;
import xap.lui.core.services.IPaEditorService;
import xap.lui.core.services.PaEditorServiceImpl;
import xap.lui.core.util.ObjSelectedUtil;

public class PaStructAddContentMenuListener implements IWindowCtrl{


	public void onclick(MouseEvent<MenuItem> e) {
		IPaEditorService service = new PaEditorServiceImpl();
		String pageId = (String) PaCache.getInstance().get("_pageId");
		String sessionId =(String)PaCache.getInstance().get("eclipse_sesionId");
		UIPartMeta uimeta = service.getOriUIMeta(pageId, sessionId);		
		PagePartMeta pageMeta = service.getOriPageMeta(pageId, sessionId);
		Dataset ds = LuiRuntimeContext.getWebContext().getPageMeta().getWidget("nav").getViewModels().getDataset("ds_struct");
		int idIndex = ds.nameToIndex("id");
		Row row = ds.getSelectedRow();
		String id = (String) row.getValue(idIndex);
		UIElement ele = UIElementFinder.findElementById(uimeta, id);		
		id = ObjSelectedUtil.toSelected(uimeta, pageMeta, ele);
		if(ele instanceof UILayoutPanel){
			AppSession.current().getAppContext().addExecScript("var obj = {id : '" + id + "', currentDropObjType2 : 'isPanel'}; \n");
		}
		else{
			AppSession.current().getAppContext().addExecScript("var obj = {id : '" + id + "', currentDropObjType2 : 'isLayout'}; \n");
		}
		AppSession.current().getAppContext().addExecScript("addFromStruct(obj); \n");
	}

}
