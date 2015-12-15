
package xap.lui.core.comps;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.alibaba.fastjson.annotation.JSONField;

import xap.lui.core.context.BaseContext;
import xap.lui.core.context.MenuItemContext;
import xap.lui.core.context.MenubarContext;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.listener.LuiEventConf;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.model.PagePartMeta;
import xap.lui.core.render.PCMenubarCompRender;
import xap.lui.core.render.notify.RenderProxy;

/**
 * 
 *
 */
@XmlRootElement(name = "MenuBar")
@XmlAccessorType(XmlAccessType.NONE)
public class MenubarComp extends WebComp implements IContainerComp<MenuItem> {

	private static final long serialVersionUID = 2754368999187161913L;
	public static final String WIDGET_NAME = "menubar";

	// //标识此Menubar是在哪个位置，因为menubar管理器会自动使处于同一个位置的menubar同时只显示一个，例如管理类型卡片。这个值没有特定常量，只需要有区别即可
	// private String posIdentity = "head";
	@XmlElement(name = "MenuItem")
	private List<MenuItem> menuList = null;
	public static final String NOTIFY_UPDATE_TYPE = "notifyUpdateType";
	public static final String NOTIYF_UPDATE_ADDMENUITEM = "notifyUpdateType_addmenuitem";
	public static final String WIDGET_ID = "widgetId";
	public static final String MENU_ID = "menuId";
	public static final String MENU_ITEM_ID = "menuItemId";
	private PagePartMeta pagemeta;

	@JSONField(serialize=false)
	private PCMenubarCompRender render = null;

	public MenubarComp() {
		super();
	}

	public MenubarComp(String id) {
		super(id);
	}

	@Override
	public String getWidgetName() {
		return WIDGET_NAME;
	}

	public List<MenuItem> getMenuList() {
		return menuList;
	}

	public void setMenuList(List<MenuItem> menuList) {
		this.menuList = menuList;
	}

	public void addMenuItem(MenuItem item) {
		if (menuList == null)
			menuList = new ArrayList<MenuItem>();
		item.setWidget(this.getWidget());
		item.setMenu(this);
		menuList.add(item);
		if (LifeCyclePhase.ajax.equals(getPhase())) {
			this.getRender().addItem(item);
		}
	}

	public Object clone() {
		MenubarComp menubar = new MenubarComp(this.getId());
		menubar.setEnabled(this.isEnabled());
		// menubar.setHeight(this.getHeight());
		// menubar.setLeft(this.getLeft());
		if (this.getMenuList() != null) {
			List<MenuItem> list = new ArrayList<MenuItem>();
			List<MenuItem> sourceList = this.getMenuList();

			MenuItem item = null;
			for (Iterator<MenuItem> it = sourceList.iterator(); it.hasNext();) {
				item = it.next();
				if (item != null)
					list.add((MenuItem) (item.clone()));
			}
			menubar.setMenuList(list);
		}
		// menubar.setMenuList((List<MenuItem>)
		// ((ArrayList<MenuItem>)this.getMenuList()).clone());
		menubar.setRendered(this.isRendered());
		// menubar.setTop(this.getTop());
		menubar.setVisible(this.isVisible());
		// menubar.setWidth(this.getWidth());
		// menubar.setClassName(this.getClassName());
		// menubar.setPosIdentity(this.getPosIdentity());
		menubar.setContextMenu(this.getContextMenu());
		//zww 加
		menubar.setWidget(this.getWidget());
		// Events
		LuiEventConf[] eventConfs = this.getEventConfs();
		if (eventConfs != null && eventConfs.length > 0) {
			for (LuiEventConf eventConf : eventConfs) {
				menubar.addEventConf(eventConf);
			}
		}
		return menubar;
	}

	public void mergeProperties(LuiElement ele) {
		throw new LuiRuntimeException("not implemented");
	}

	public void setPageMeta(PagePartMeta pagemeta) {
		this.pagemeta = pagemeta;
	}

	public PagePartMeta getPageMeta() {
		return this.pagemeta;
	}

	/**
	 * 查看MenubarComp的Context是否发生改变
	 * 
	 * @param menubar
	 * @return
	 */
	private boolean checkMenubarCtxChanged(MenubarComp menubar, List<MenuItem> menuList) {
		if (menubar != null) {
			if (menubar.isMenubarCtxChanged())
				return true;
			List<MenuItem> list = menubar.getMenuList();
			if (list != null) {
				boolean result = checkMenubarCtxChanged(null, list);
				if (result == true)
					return true;
			}
		} else if (menuList != null) {
			for (MenuItem menuItem : menuList) {
				if (menuItem.isCtxChanged())
					return true;
				List<MenuItem> subItemList = menuItem.getChildList();
				if (subItemList != null) {
					boolean result = checkMenubarCtxChanged(null, subItemList);
					if (result == true)
						return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean isCtxChanged() {
		boolean result = checkMenubarCtxChanged(this, null);
		if (result == true)
			return true;
		return super.isCtxChanged();
	}

	private boolean isMenubarCtxChanged() {
		return super.isCtxChanged();
	}

	@Override
	public BaseContext getContext() {
		MenubarContext ctx = new MenubarContext();
		ctx.setId(this.getId());
		if (this.menuList != null && this.menuList.size() > 0) {
			MenuItemContext[] childItemContexts = new MenuItemContext[this.menuList.size()];
			for (int i = 0, n = this.menuList.size(); i < n; i++) {
				childItemContexts[i] = (MenuItemContext) this.menuList.get(i).getContext();
			}
			ctx.setChildItemContexts(childItemContexts);
		}
		return ctx;
	}

	@Override
	public void setContext(BaseContext ctx) {
		MenubarContext miCtx = (MenubarContext) ctx;
		MenuItemContext[] childItemContexts = miCtx.getChildItemContexts();
		for (int i = 0, n = childItemContexts.length; i < n; i++) {
			MenuItemContext itemCtx = childItemContexts[i];
			for (int j = 0, m = this.menuList.size(); j < m; j++) {
				if (menuList.get(j).getId().equals(itemCtx.getId())) {
					menuList.get(j).setContext(itemCtx);
					break;
				}
			}
		}
		this.setCtxChanged(false);
	}

	public MenuItem getItem(String id) {
		for (int i = 0; i < menuList.size(); i++) {
			if (menuList.get(i).getId().equals(id))
				return menuList.get(i);
			else{
				MenuItem ele =  menuList.get(i);
				List<MenuItem> childs = ele.getChildList();
				if(childs != null && childs.size() > 0){
					if(ele.getItem(id) == null)
						continue;
					return ele.getItem(id);
				}
			}
		}
		return null;
	}

	public void removeItem(MenuItem menuItem, boolean withNotify) {
		menuList.remove(menuItem);
		if (LifeCyclePhase.ajax.equals(getPhase())) {
			//menuItem.setVisible(false);
		    this.getRender().removeItem(menuItem);
		}
	}

	public void removeItem(MenuItem menuItem) {
		menuList.remove(menuItem);
	}

	@Override
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
		if (!enabled) {
			if (this.menuList != null) {
				for (MenuItem item : menuList) {
					item.setEnabled(enabled);
				}
			}
		}
	}
	
	@Override
	public void setVisible(boolean visible) {
		this.isVisible = visible;
		if (!visible) {
			if (this.menuList != null) {
				for (MenuItem item : menuList) {
					item.setVisible(visible);
				}
			}
		}
	}
	
	@Override
	public MenuItem getElementById(String id) {
		Iterator<MenuItem> it = menuList.iterator();
		while (it.hasNext()) {
			MenuItem ele = it.next();
			if (ele.getId().equals(id))
				return ele;
			else{
				List<MenuItem> childs = ele.getChildList();
				if(childs != null && childs.size() > 0){
					if(ele.getItem(id) == null)
						continue;
					return ele.getItem(id);
				}
			}
		}
		return null;
	}

	@Override
	public PCMenubarCompRender getRender() {
		if (render == null) {
			render = RenderProxy.getRender(new PCMenubarCompRender(this));
		}
		return render;
	}

}
