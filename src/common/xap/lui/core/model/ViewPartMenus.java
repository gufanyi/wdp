package xap.lui.core.model;
import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import xap.lui.core.builder.LuiHashSet;
import xap.lui.core.builder.LuiSet;
import xap.lui.core.comps.ContextMenuComp;
import xap.lui.core.comps.MenubarComp;
import xap.lui.core.exception.LuiRuntimeException;
@XmlRootElement(name = "ViewPartMenus")
@XmlAccessorType(XmlAccessType.NONE)
public class ViewPartMenus implements Serializable, Cloneable {
	private static final long serialVersionUID = -7761705359086487592L;
	@XmlElement(name = "MenuBar")
	private LuiSet<MenubarComp> menuBarComp = new LuiHashSet<MenubarComp>();
	@XmlElement(name = "ContextMenu")
	private LuiSet<ContextMenuComp> contextMenuComp = new LuiHashSet<ContextMenuComp>();
	private ViewPartMeta widget;
	private PagePartMeta pagemeta;
	public ViewPartMenus(ViewPartMeta luiWidget) {
		this.widget = luiWidget;
	}
	public ViewPartMenus() {}
	public ViewPartMenus(PagePartMeta pm) {
		this.pagemeta = pm;
	}
	public void addMenuBar(MenubarComp menu) {
		menu.setPageMeta(this.pagemeta);
		this.menuBarComp.add(menu);
	}
	public MenubarComp getMenuBar(String id) {
		return this.menuBarComp.find(id);
	}
	public MenubarComp[] getMenuBars() {
		return this.menuBarComp.toArray(new MenubarComp[0]);
	}
	public void removeMenuBar(String id) {
		this.menuBarComp.remove(id);
	}
	public void addContextMenu(ContextMenuComp menu) {
		menu.setWidget(widget);
		this.contextMenuComp.add(menu);
	}
	public ContextMenuComp[] getContextMenus() {
		return this.contextMenuComp.toArray(new ContextMenuComp[0]);
	}
	public ContextMenuComp getContextMenu(String id) {
		return this.contextMenuComp.find(id);
	}
	public void removeContextMenu(String id) {
		this.contextMenuComp.remove(id);
	}
	public Object clone() {
		try {
			ViewPartMenus viewMenu = (ViewPartMenus) super.clone();
			viewMenu.menuBarComp = new LuiHashSet<MenubarComp>();
			viewMenu.contextMenuComp = new LuiHashSet<ContextMenuComp>();
			for(MenubarComp inner:this.menuBarComp){
				viewMenu.menuBarComp.add((MenubarComp)inner.clone());
			}
			for(ContextMenuComp inner:this.contextMenuComp){
				viewMenu.contextMenuComp.add((ContextMenuComp)inner.clone());
			}
			return viewMenu;
		} catch (CloneNotSupportedException e) {
			throw new LuiRuntimeException(e.getMessage(), e);
		}
	}
	public ViewPartMeta getWidget() {
		return widget;
	}
	public void setWidget(ViewPartMeta widget) {
		this.widget = widget;
		ContextMenuComp[] ctxMenus = getContextMenus();
		for (int i = 0; i < ctxMenus.length; i++) {
			ctxMenus[i].setWidget(widget);
		}
	}
	public PagePartMeta getPagemeta() {
		return pagemeta;
	}
	public void setPagemeta(PagePartMeta pagemeta) {
		this.pagemeta = pagemeta;
	}
	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		MenubarComp[] dss = this.getMenuBars();
		if (dss != null) {
			buf.append("menubar:{");
			for (int i = 0; i < dss.length; i++) {
				buf.append(dss[i].getId());
				buf.append(",");
			}
			buf.append("},");
		}
		return buf.toString();
	}
	public LuiSet<MenubarComp> getMenuBarComp() {
		return menuBarComp;
	}
	public void setMenuBarComp(LuiSet<MenubarComp> menuBarComp) {
		this.menuBarComp = menuBarComp;
	}
	public LuiSet<ContextMenuComp> getContextMenuComp() {
		return contextMenuComp;
	}
	public void setContextMenuComp(LuiSet<ContextMenuComp> contextMenuComp) {
		this.contextMenuComp = contextMenuComp;
	}
}
