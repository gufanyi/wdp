package xap.lui.core.comps;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.alibaba.fastjson.annotation.JSONField;

import xap.lui.core.context.BaseContext;
import xap.lui.core.context.ContextMenuContext;
import xap.lui.core.context.MenuItemContext;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.render.PCContextMenuCompRender;
import xap.lui.core.render.notify.RenderProxy;

/**
 * 右键菜单
 * ContextMenu的后台控件类
 */

@XmlRootElement(name = "ContextMenu")
@XmlAccessorType(XmlAccessType.NONE)
public class ContextMenuComp extends WebComp {

	private static final long serialVersionUID = -6485980427266955507L;
	
	public static final String WIDGET_NAME = "contextmenu";
	public static final String NOTIYF_UPDATE_ADDMENU = "notifyUpdateType_addmenu";
	public static final String NOTIYF_DELETE_REMOVEMENU = "notifyUpdateType_removeChildrenMenu";
	public static final String NOTIFY_UPDATE_TYPE = "notifyUpdateType";
	public static final String WIDGET_ID = "widgetId";
	public static final String CONTEXTMENU_ID = "contextmenuId";
	
	@JSONField(serialize=false)
	private PCContextMenuCompRender render = null;
	
	
	public final static String TRIGGER_ID = "TRIGGER_ID";
	@XmlElement(name="MenuItem")
    private List<MenuItem> itemList = null;
	
	@Override
	public String getWidgetName() {
		return WIDGET_NAME;
	}
    
    public ContextMenuComp() {}
    
    public ContextMenuComp(String id)
    {
    	super(id);
    }
    
    public List<MenuItem> getItemList()
    {
    	
    	return this.itemList;
    }
    
    public void setItemList(List<MenuItem> list)
    {
    	this.itemList = list;
    }
    
    public void addMenuItem(MenuItem item)
    {
    	if(this.itemList == null)
    		this.itemList = new ArrayList<MenuItem>();
    	this.itemList.add(item);
    	if(LifeCyclePhase.ajax.equals(this.getPhase())) {
    		this.getRender().addMenuItem(item);
    	}
    }
    public MenuItem getItem(String id) {
		for (int i = 0; i < itemList.size(); i++) {
			if (itemList.get(i).getId().equals(id))
				return itemList.get(i);
		}
		return null;
	}
    
    /**
     * 显示或隐藏右键菜单中的项目
     * @param isShow
     */
    public void setAllItemsVisible(boolean isShow) {
    	if(this.itemList!=null) {
    		for(MenuItem item : this.itemList) {
    			item.setVisible(isShow);
    		}
    	}
    }
    
    public Object clone()
    {
    	
    	ContextMenuComp contextMenu = (ContextMenuComp)super.clone();
    	contextMenu.itemList = new ArrayList<MenuItem>();
    	if(this.itemList != null)
    	{
	    	for(MenuItem child : this.itemList)
	    	{
	    		contextMenu.itemList.add((MenuItem)child.clone());
	    	}
    	}
    	return contextMenu;
    }

	@Override
	public void mergeProperties(LuiElement ele) {
		throw new LuiRuntimeException("not implemented");
	}
	
	public void removeChildrenItem() {
		if(this.itemList != null) {
			this.itemList.clear();
			if(LifeCyclePhase.ajax.equals(this.getPhase())) {
				this.getRender().removeAllChild();
			}
		}
	}
	
	/**
	 * 查看ContextMenuComp的Context是否发生改变
	 * @param menu
	 * @return
	 */
	private boolean checkContextMenuCtxChanged(ContextMenuComp menu, List<MenuItem> menuList) {
		if (menu != null) {
			if (menu.isMenuCtxChanged())
				return true;
			List<MenuItem> list = menu.getItemList();
			if (list != null) {
				boolean result = checkContextMenuCtxChanged(null, list);
				if (result == true)
					return true;
			}
		} else if (menuList != null) {
			for (MenuItem menuItem : menuList) {
				if (menuItem.isCtxChanged())
					return true;
				List<MenuItem> subItemList = menuItem.getChildList();
				if (subItemList != null) {
					boolean result = checkContextMenuCtxChanged(null, subItemList);
					if (result == true)
						return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean isCtxChanged() {
		boolean result = checkContextMenuCtxChanged(this, null);
		if (result == true)
			return true;
		return super.isCtxChanged();
	}
	
	private boolean isMenuCtxChanged() {
		return super.isCtxChanged();
	}

	@Override
	public BaseContext getContext() {
		ContextMenuContext ctx = new ContextMenuContext();
		ctx.setId(this.getId());
		if (this.itemList.size() > 0) {
			MenuItemContext[] childItemContexts = new MenuItemContext[this.itemList.size()];
			for (int i = 0, n = this.itemList.size(); i < n; i++) {
				childItemContexts[i] = (MenuItemContext) this.itemList.get(i).getContext();
			}
			ctx.setChildItemContexts(childItemContexts);
		}
//		if (this.childMenuList.size() > 0) {
//			ContextMenuContext[] childMenuContexts = new ContextMenuContext[this.childMenuList.size()];
//			for (int i = 0, n = this.childMenuList.size(); i < n; i++) {
//				childMenuContexts[i] = (ContextMenuContext) this.childMenuList.get(i).getContext();
//			}
//			ctx.setChildMenuContexts(childMenuContexts);
//		}
		return ctx;
	}
	
	@Override
	public void setContext(BaseContext ctx) {
		ContextMenuContext miCtx = (ContextMenuContext) ctx;
		
		// 触发对象ID
		if (miCtx.getTriggerId() != null)
			this.setExtendAttribute(TRIGGER_ID, miCtx.getTriggerId());
		
		MenuItemContext[] childItemContexts = miCtx.getChildItemContexts();
		if(childItemContexts != null){
			for (int i = 0, n = childItemContexts.length; i < n; i++) {
				MenuItemContext itemCtx = childItemContexts[i];
				if(itemCtx == null)
					continue;
				for (int j = 0, m = this.itemList.size(); j < m; j++) {
					if (itemList.get(j).getId().equals(itemCtx.getId())) {
						itemList.get(j).setContext(itemCtx);
						break;
					}
				}
			}
		}
//		ContextMenuContext[] childMenuContexts = miCtx.getChildMenuContexts();
//		for (int i = 0, n = childMenuContexts.length; i < n; i++) {
//			ContextMenuContext menuCtx = childMenuContexts[i];
//			for (int j = 0, m = this.childMenuList.size(); j < m; j++) {
//				if (childMenuList.get(j).getId().equals(menuCtx.getId())) {
//					childMenuList.get(j).setContext(menuCtx);
//					break;
//				}
//			}
//		}
		this.setCtxChanged(false);
	}
	

	public PCContextMenuCompRender getRender() {
		if(render==null){
			render = RenderProxy.getRender(new PCContextMenuCompRender(this));
		}
		return render;
	}


	
}

