package xap.lui.psn.pamgr;
import xap.lui.core.cache.PaCache;
import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.common.LuiWebSession;
import xap.lui.core.comps.FormElement;
import xap.lui.core.comps.MenuItem;
import xap.lui.core.constant.WebConstant;
import xap.lui.core.control.IWindowCtrl;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.Row;
import xap.lui.core.event.MouseEvent;
import xap.lui.core.layout.UIElement;
import xap.lui.core.layout.UIElementFinder;
import xap.lui.core.layout.UIPartMeta;
import xap.lui.core.layout.UIViewPart;
import xap.lui.core.logger.LuiLogger;
import xap.lui.core.model.AppSession;
import xap.lui.core.model.PagePartMeta;
import xap.lui.core.services.IPaEditorService;
import xap.lui.core.services.PaEditorServiceImpl;
import xap.lui.core.util.ObjSelectedUtil;
public class PaStructDeleteContentMenuListener implements IWindowCtrl {
	public void onclick(MouseEvent<MenuItem> e) {
		IPaEditorService service =  new PaEditorServiceImpl();
		String pageId = (String) PaCache.getInstance().get("_pageId");
		String sessionId =(String)PaCache.getInstance().get("eclipse_sesionId");
		UIPartMeta uimeta = service.getOriUIMeta(pageId, sessionId);
		PagePartMeta pageMeta = service.getOriPageMeta(pageId, sessionId);
		Dataset ds = LuiRuntimeContext.getWebContext().getPageMeta().getWidget("nav").getViewModels().getDataset("ds_struct");
		int idIndex = ds.nameToIndex("id");
		Row row = ds.getSelectedRow();
		String id = (String) row.getValue(idIndex);
		String pid = (String) row.getString(ds.nameToIndex("pid"));
		String type = row.getString(ds.nameToIndex("imgtype"));
		UIElement ele = UIElementFinder.findElementById(uimeta, id);
		LuiWebSession session = LuiRuntimeContext.getWebContext().getPageWebSession();
		String mode = (String) session.getAttribute(WebConstant.WINDOW_MODE_KEY);
		if (mode != null && (WebConstant.MODE_PERSONALIZATION).equals(mode)) {
			id = ObjSelectedUtil.toSelected(uimeta, pageMeta, ele);
			AppSession.current().getAppContext().addExecScript("deleteFromStruct('" + id + "')");
		} else {
			if (ele instanceof UIPartMeta || ele instanceof UIViewPart)
				return;
			if (FormElement.class.getName().equals(type)) {
				id = pid + id;
			} else {
				id = ObjSelectedUtil.toSelected(uimeta, pageMeta, ele);
			}
			AppSession.current().getAppContext().addExecScript("deleteFromStruct('" + id + "')");
		}
	}
	private void waitForInit(String sessionId, String pageId) {
		int count = 0;
		PagePartMeta pagemeta = null;
		while (pagemeta == null && count < 5) {
			IPaEditorService service =  new PaEditorServiceImpl();
			pagemeta = service.getOriPageMeta(pageId, sessionId);
			if (pagemeta == null) {
				count++;
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					LuiLogger.error(e);
				}
			}
		}
	}
}
