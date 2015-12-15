package xap.lui.core.render;

import java.util.Iterator;
import java.util.List;

import xap.lui.core.comps.ContextMenuComp;
import xap.lui.core.comps.LuiElement;
import xap.lui.core.comps.MenuItem;
import xap.lui.core.event.IEventSupport;
import xap.lui.core.layout.UIComponent;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.model.LuiPageContext;
import xap.lui.core.model.ViewPartMeta;

public class PCContextMenuCompRender extends UINormalComponentRender<UIComponent, ContextMenuComp> {

	// 其本身的右键菜单
	private String theContextMenu = null;

	private static final String CONTEXTMENU_BASE = "pc_contextmenu_";

	public PCContextMenuCompRender(ContextMenuComp webEle) {
		super(webEle);
	}

	@Override
	public String place() {
		return "";
	}

	public String createBody() {

		ContextMenuComp contextMenu = (ContextMenuComp) this.getWebElement();
		// 其本身的右键菜单
		theContextMenu = contextMenu.getContextMenu();

		StringBuilder buf = new StringBuilder();
		buf.append("window.").append(getVarId()).append(" = $(\"<div id='" + this.getId() + "'>\").contextmenu().contextmenu('instance');\n");

		if (this.getCurrWidget() != null) {
			//String widget = WIDGET_PRE + this.getCurrWidget().getId();
			buf.append("pageUI.getViewPart('" + this.getCurrWidget().getId() + "')");
			buf.append(".addMenu(window." + getVarId() + ");\n");
		}

		// if (!contextMenu.getWidth().equals("100%")) {
		// 是否应该增加width属性？
		// buf.append("window.").append(getVarId()).append(".setWidth(80);\n");
		// }
		List<MenuItem> itemList = contextMenu.getItemList();
		if (itemList != null) {
			for (MenuItem item : itemList) {
				this.genOneMenuItem(contextMenu, buf, item);
//				if (item.isSep()) {
//					buf.append(getVarId()).append(".addSep();\n");
//				} else {
//					String itemShowId = getVarId() + item.getId();
//					buf.append("var ").append(itemShowId).append(" = ").append("window.").append(getVarId()).append(".addMenu(\"").append(item.getId()).append("\",\"")
//							.append(translate(item.getI18nName(), item.getText(), item.getLangDir())).append("\");\n");
//
//					// 为子项增加快捷键
//					String hotKey = item.getHotKey();
//					String modifier = String.valueOf(item.getModifiers());
//					if (hotKey != null && !"".equals(hotKey)) {
//						buf.append(itemShowId).append(".setHotKey(\"").append(hotKey + modifier).append("\");\n");
//					}
//
//					genScriptForItem(buf, item);
//
//					// 加右键菜单
//					if (theContextMenu != null && !"".equals(theContextMenu)) {
//						buf.append(addContextMenu(theContextMenu, itemShowId));
//					}
//
//					buf.append(this.addEventSupport(item, contextMenu.getWidget().getId(), itemShowId, getId()));
//				}
			}
		}

		return buf.toString();
	}

	/**
	 * 为子项增加子菜单的脚本
	 * 
	 * @param buf
	 * @param item
	 */
	private void genScriptForItem(StringBuffer buf, MenuItem item) {
		List<MenuItem> itemList = item.getChildList();
		if (itemList != null && itemList.size() > 0) {
			String itemShowId = "$MI_" + item.getId();
			for (MenuItem subItem : itemList) {
				if (subItem.isSep()) {
					buf.append(itemShowId).append(".addSep();\n");
				} else {
					String subItemShowId = "$MI_" + subItem.getId();
					buf.append("var ").append(subItemShowId).append(" = ").append(itemShowId).append(".addMenu(\"").append(subItem.getId()).append("\",\"")
							.append(translate(subItem.getI18nName(), subItem.getText(), subItem.getLangDir())).append("\");\n");

					// 为子项增加快捷键
					String hotKey = subItem.getHotKey();
					String modifier = String.valueOf(subItem.getModifiers());
					if (hotKey != null && !"".equals(hotKey)) {
						buf.append(subItemShowId).append(".setHotKey(\"").append(hotKey + modifier).append("\");\n");
					}

					// 加右键菜单
					if (theContextMenu != null && !"".equals(theContextMenu)) {
						buf.append(addContextMenu(theContextMenu, subItemShowId));
					}

					buf.append(this.addEventSupport(subItem, getCurrWidget().getId(), subItemShowId, getId()));

					genScriptForItem(buf, subItem);
				}
			}
		}
	}

	protected String getSourceType(LuiElement ele) {
		return LuiPageContext.SOURCE_TYPE_CONTEXTMENU_MENUITEM;
	}

	@Override
	protected String mockDivId() {
		String divId = DIV_PRE + ((this.getViewId() == null || this.getViewId().equals("")) ? CONTEXTMENU_BASE : this.getViewId() + "_") + id;
		return divId;
	}

	/**
	 * 生成子项菜单的JS代码
	 * 
	 * @param item
	 * @return
	 */
	private String genItemSubMenuScript(MenuItem item, String menubarVarId) {
		StringBuffer buf = new StringBuffer();

		if (item.getChildList() != null && item.getChildList().size() > 0) {
			// 其本身的右键菜单
			String theContextMenu = this.getWebElement().getContextMenu();
			for (MenuItem cItem : item.getChildList()) {
				if (cItem.isSep()) {
					buf.append(menubarVarId).append(item.getId()).append(".addSep();\n");
				} else {
					buf.append("var ").append(menubarVarId).append(cItem.getId()).append(" = ").append(menubarVarId);
					buf.append(item.getId()).append(".addMenu(\"").append(cItem.getId()).append("\", \"");
					buf.append(translate(cItem.getI18nName(), cItem.getText(), cItem.getLangDir())).append("\", \"");
					buf.append(cItem.getTip() == null ? translate(cItem.getI18nName(), cItem.getText(), cItem.getLangDir()) : translate(cItem.getTip(), cItem.getTip(), cItem.getLangDir())).append(
							"\", ");
					if (cItem.getImgIcon() != null) {
						buf.append("'").append(cItem.getRealImgIcon()).append("', ");
					} else {
						buf.append("null, ");
					}
					buf.append(cItem.isCheckBoxGroup()).append(", ").append(cItem.isSelected()).append(", ").append("{imgIconOn:")
							.append(cItem.getImgIconOn() == null ? "''" : "'" + cItem.getRealImgIconOn() + "'").append(",imgIconDisable:")
							.append(cItem.getImgIconDisable() == null ? "''" : "'" + cItem.getRealImgIconDisable() + "'").append("}").append(");\n");
					// 加右键菜单
					if (theContextMenu != null && !"".equals(theContextMenu)) {
						buf.append(addContextMenu(theContextMenu, menubarVarId + cItem.getId()));
					}

					buf.append(addEventSupport(cItem, null, menubarVarId + cItem.getId(), getId()));

					buf.append(genItemSubMenuScript(cItem, menubarVarId));
				}
			}
		}
		return buf.toString();
	}

	/**
	 * 产生每一个菜单项的script脚本
	 * 
	 * @param item
	 * @return
	 */
	private String genItemScript(MenuItem item, String menubarVarId) {
		String parentId = menubarVarId + item.getId();
		StringBuffer buf = new StringBuffer();
		if (item.getChildList() != null && item.getChildList().size() > 0) {
			// 其本身的右键菜单
			String theContextMenu = this.getWebElement().getContextMenu();
			Iterator<MenuItem> it = item.getChildList().iterator();
			while (it.hasNext()) {
				MenuItem cItem = it.next();
				if (cItem.isSep()) {
					buf.append(parentId).append(".addSep();\n");
				} else {
					String menuId = menubarVarId + cItem.getId();
					// 提示信息
					String tip = cItem.getTip() == null ? translate(cItem.getI18nName(), cItem.getText(), cItem.getLangDir()) : translate(cItem.getTip(), cItem.getTip(), cItem.getLangDir());
					String displayHotKey = cItem.getDisplayHotKey();
					if (displayHotKey != null && !"".equals(displayHotKey)) {
						tip += "(" + displayHotKey + ")";
					}
					buf.append("var ").append(menuId).append(" = ").append(parentId).append(".addMenu(\"").append(cItem.getId()).append("\", \"")
							.append(translate(cItem.getI18nName(), cItem.getText(), cItem.getLangDir())).append("\",'").append(tip).append("', ");
					if (cItem.getImgIcon() != null) {
						buf.append("'").append(cItem.getRealImgIcon()).append("', ");
					} else
						buf.append("null, ");
					buf.append(cItem.isCheckBoxGroup()).append(", ").append(cItem.isSelected()).append(", ").append("{imgIconOn:")
							.append(cItem.getImgIconOn() == null ? "''" : "'" + cItem.getRealImgIconOn() + "'").append(",imgIconDisable:")
							.append(cItem.getImgIconDisable() == null ? "''" : "'" + cItem.getRealImgIconDisable() + "'").append("}").append(");\n");

					// 为子项设置快捷键
					String hotKey = cItem.getHotKey();
					String modifier = String.valueOf(cItem.getModifiers());
					if (hotKey != null && !"".equals(hotKey)) {
						buf.append(menuId).append(".setHotKey(\"").append(hotKey + modifier).append("\");\n");
					}

					// 加右键菜单
					if (theContextMenu != null && !"".equals(theContextMenu)) {
						buf.append(addContextMenu(theContextMenu, menubarVarId + cItem.getId()));
					}

					ViewPartMeta currentWidget = getCurrWidget();
					cItem.setWidget(currentWidget);

					buf.append(addEventSupport(cItem, this.getViewId(), menubarVarId + cItem.getId(), getId()));

					buf.append(genItemSubMenuScript(cItem, menubarVarId));
				}
			}
		}

		return buf.toString();
	}

	private void genOneMenuItem(ContextMenuComp ctxMenu, StringBuilder buf, MenuItem item) {
		item.setWidget(ctxMenu.getWidget());
		String contextmenuVarId = this.getVarId();
		buf.append("var ").append(contextmenuVarId).append(" = ").append("pageUI.getViewPart('" + this.getViewId() + "').getMenu('" + this.getWebElement().getId() + "')").append(";\r\n");

		if (item.isSep()) {
			buf.append(contextmenuVarId).append(".addSep();\n");
		} else {
			String menuId = contextmenuVarId + item.getId();
			// 提示信息
			String tip = item.getTip() == null ? translate(item.getI18nName(), item.getText(), item.getLangDir()) : translate(item.getTip(), item.getTip(), item.getLangDir());
			String displayHotKey = item.getDisplayHotKey();
			if (displayHotKey != null && !"".equals(displayHotKey)) {
				tip += "(" + displayHotKey + ")";
			}

			buf.append("var ").append(menuId).append("=");
			buf.append(contextmenuVarId).append(".addMenu(\"").append(item.getId()).append("\",\"");
			buf.append(translate(item.getI18nName(), item.getText(), item.getLangDir())).append("\",'").append(tip).append("',");
			if (item.getImgIcon() != null) {
				buf.append("'").append(item.getRealImgIcon()).append("', ");
			} else {
				buf.append("null,");
			}
			buf.append(item.isCheckBoxGroup()).append(",");
			buf.append(item.isSelected()).append(",");
			buf.append("{imgIconOn:").append(item.getImgIconOn() == null ? "''" : "'" + item.getRealImgIconOn() + "'");
			buf.append(",imgIconDisable:").append(item.getImgIconDisable() == null ? "''" : "'" + item.getRealImgIconDisable() + "'");
			buf.append("}").append(");\n");

			// 为子项设置快捷键
			String hotKey = item.getHotKey();
			String modifier = String.valueOf(item.getModifiers());
			if (hotKey != null && !"".equals(hotKey)) {
				buf.append(menuId).append(".setHotKey(\"").append(hotKey + modifier).append("\");\n");
			}
			if (!item.isEnabled()) {
				buf.append(menuId + ".setActive(false);\n");
			}
			if (!item.isVisible()) {
				buf.append(menuId + ".hide();\n");
			}
			buf.append(menuId).append(".ctxChanged = false;\n");
			buf.append(addEventSupport(item, this.getViewId(), menuId, getId()));
			buf.append(genItemScript(item, contextmenuVarId));
		}
	}
	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void addMenuItem(MenuItem menuItem) {
		StringBuilder script = new StringBuilder();
		ContextMenuComp ctxMenu = this.getWebElement();
		this.genOneMenuItem(ctxMenu, script, menuItem);
		if (script.length() > 0) {
			addDynamicScript(script.toString());
		}
	}
	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void removeAllChild() {
		StringBuilder buf = new StringBuilder();
		String menubarVarId = this.getVarId();
		buf.append("var ").append(menubarVarId).append(" = ").append("pageUI.getViewPart('" + this.getViewId() + "').getMenu('" + this.getWebElement().getId() + "')").append(";\r\n");
		buf.append(menubarVarId).append(".removeChildrenMenu();");
		if (buf.length() > 0) {
			addDynamicScript(buf.toString());
		}
	}

	protected String getSourceType(IEventSupport ele) {
		return LuiPageContext.SOURCE_TYPE_CONTEXTMENU_MENUITEM;
	}

	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void setItemVisible(MenuItem menuItem) {
		StringBuilder script = new StringBuilder();
		script.append(getCtxMenuItemJSObject(this.getViewId(), this.getWebElement().getId(), menuItem.getId(), null));
		script.append(getVarId()).append(menuItem.getId());
		Boolean visible = menuItem.isVisible();
		if (visible != null && visible) {
			script.append(".show();\n");
		} else {
			script.append(".hide();\n");
		}
		if (script.length() > 0) {
			addDynamicScript(script.toString());
		}
	}
	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void setItemEnabled(MenuItem menuItem) {
		StringBuilder script = new StringBuilder();
		script.append(getCtxMenuItemJSObject(this.getViewId(), this.getWebElement().getId(), menuItem.getId(), null));
		script.append(getVarId()).append(menuItem.getId());
		script.append(".setActive(" + menuItem.isEnabled() + ");\n");
		if (script.length() > 0) {
			addDynamicScript(script.toString());
		}
	}
	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void setItemImg(MenuItem menuItem) {
		StringBuilder script = new StringBuilder();
		script.append(getCtxMenuItemJSObject(this.getViewId(), this.getWebElement().getId(), menuItem.getId(), null));
		script.append(getVarId()).append(menuItem.getId());
		script.append(".changeImg('" + menuItem.getRealImgIcon() + "');\n");
		if (script.length() > 0) {
			addDynamicScript(script.toString());
		}
	}
	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void setItemText(MenuItem menuItem) {
		StringBuilder script = new StringBuilder();
		script.append(getCtxMenuItemJSObject(this.getViewId(), this.getWebElement().getId(), menuItem.getId(), null));
		script.append(getVarId()).append(menuItem.getId());
		script.append(".changeCaption('" + menuItem.getText() + "');\n");
		if (script.length() > 0) {
			addDynamicScript(script.toString());
		}
	}
	/**
	 * 获取menuItem JS对象
	 * 
	 * @param widgetId
	 * @param meId
	 * @param itemId
	 * @return
	 */
	private String getCtxMenuItemJSObject(String widgetId, String ctxMenuId, String itemId, String pItemId) {
		StringBuffer script = new StringBuffer();
		script.append("var ").append(getVarId()).append(itemId).append(" = ").append("pageUI.getViewPart('" + widgetId + "').getMenu('" + ctxMenuId + "')");
		if (pItemId != null) {
			script.append(".getMenu('" + pItemId + "')");
		}
		script.append(".getMenu('" + itemId + "');\n");
		return script.toString();
	}

}
