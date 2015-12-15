package xap.lui.core.state;

import java.util.Iterator;
import java.util.List;
import xap.lui.core.comps.MenuItem;
import xap.lui.core.comps.MenubarComp;
import xap.lui.core.comps.WebComp;
import xap.lui.core.model.AppSession;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.core.model.PagePartMeta;
import xap.lui.core.model.ViewPartContext;
import xap.lui.core.util.ClassUtil;


public final class ButtonStateManager {
	public static void updateButtons() {
		AppSession ctx = AppSession.current();
		ViewPartContext viewCtx = ctx.getAppContext().getCurrentWindowContext().getCurrentViewContext();
		updateButtons(viewCtx.getView());
	}
	
	public static void updateButtons(PagePartMeta pm) {
		ViewPartMeta[] widgets = pm.getWidgets();
		for (int i = 0; i < widgets.length; i++) {
			updateButtons(widgets[i]);
		}
	}
	
	public static void updateButtons(ViewPartMeta widget){
		WebComp[] mbs = widget.getViewComponents().getComponentByType(MenubarComp.class);
		if(mbs == null || mbs.length == 0){
			mbs = widget.getViewMenus().getMenuBars();
		}
		
		if(mbs != null && mbs.length > 0){
			for (int i = 0; i < mbs.length; i++) {
				MenubarComp mb = (MenubarComp) mbs[i];
				List<MenuItem> items = mb.getMenuList();
				updateItems(items,widget);
			}
		}
	}
	
	private static void updateItems(List<MenuItem> items,ViewPartMeta widget){
		if(items != null){
			Iterator<MenuItem> it = items.iterator();
			while(it.hasNext()){
				MenuItem item = it.next();
				String stateMgrStr = item.getStateManager();
				if(stateMgrStr != null && !stateMgrStr.equals("")){
					IStateManager stateMgr = (IStateManager) ClassUtil.newInstance(stateMgrStr);
					IStateManager.State enabled = stateMgr.getState(item, widget);
					if(enabled.equals(IStateManager.State.ENABLED)){
//						item.setVisible(true);
						item.setEnabled(true);
					}
					else if(enabled.equals(IStateManager.State.DISABLED)){
//						item.setVisible(true);
						item.setEnabled(false);
					}
					else if(enabled.equals(IStateManager.State.HIDDEN))
						item.setVisible(false);
					else if(enabled.equals(IStateManager.State.VISIBLE))
						item.setVisible(true);
					else if (enabled.equals(IStateManager.State.ENABLED_VISIBLE)){
						item.setVisible(true);
						item.setEnabled(true);
					} 
					else if (enabled.equals(IStateManager.State.DISABLED_VISIBLE)){
						item.setVisible(true);
						item.setEnabled(false);
					} 
				}
				 List<MenuItem> list = item.getChildList();
				 updateItems(list,widget);
			}
		}
	}
}
