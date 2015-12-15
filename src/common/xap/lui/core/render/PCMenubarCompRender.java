package xap.lui.core.render;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import com.alibaba.fastjson.JSON;
import xap.lui.core.common.LuiRenderContext;
import xap.lui.core.comps.LuiElement;
import xap.lui.core.comps.MenuItem;
import xap.lui.core.comps.MenubarComp;
import xap.lui.core.comps.WebComp;
import xap.lui.core.event.IEventSupport;
import xap.lui.core.layout.UIComponent;
import xap.lui.core.layout.UIMenubarComp;
import xap.lui.core.logger.LuiLogger;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.model.LuiPageContext;
import xap.lui.core.model.PagePartMeta;
import xap.lui.core.model.ViewPartMeta;

/**
 * @author renxh 菜单栏控件渲染器
 */
@SuppressWarnings("unchecked")
public class PCMenubarCompRender extends UINormalComponentRender<UIMenubarComp, MenubarComp> {

	public PCMenubarCompRender(MenubarComp webEle) {
		super(webEle);
	}

	public PCMenubarCompRender(MenubarComp webEle, PCMenuGroupItemRender parentRender) {
		super(webEle);
		this.parentRender = parentRender;
	}

	private String menubarVarId = "";

	@Override
	public String place() {
		// 该处position改为absolute,解决多个menubar时第二个开始的menubar无法显示在左上角的问题
		// position:static,解决IE7中菜单项有时自动消失的问题
		StringBuffer buf = new StringBuffer();
		buf.append("var ").append(getDivId()).append(" = $('<div>').attr('id','").append(getDivId()).append("').css({\n");
		buf.append("'position':'static',\n");
		buf.append("'width':'100%'});\n");
		// buf.append(getDivId()).append(".style.height = '100%';\n");
		// buf.append(getDivId()).append(".style.overflow = 'hidden';\n");
		return buf.toString();
	}

	private String generateParam(UIMenubarComp uicomp) {
		String left = uicomp.getLeft() == null ? "0" : uicomp.getLeft().toString();
		String top = uicomp.getTop() == null ? "0" : uicomp.getTop().toString();
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("left", left);
		param.put("top", top);
		if (!StringUtils.isEmpty(uicomp.getClassName())) {
			param.put("className", uicomp.getClassName());
		}

		return JSON.toJSONString(param);
	}

	public String createBody() {
		MenubarComp menubar = this.getWebElement();
		UIMenubarComp uicomp = this.getUiElement();
		StringBuilder buf = new StringBuilder();
		menubarVarId = getVarId();
		buf.append("var ").append(menubarVarId).append("= $(\"<div id='" + menubar.getId() + "'>\")");
		buf.append(".appendTo($('#" + getDivId() + "'))\n");
		buf.append(".menubar(\n");
		buf.append(generateParam(uicomp));
		buf.append("\n).menubar('instance');\n");
		addMenuItem(menubar, buf);
		if (this.getParentRender() instanceof PCMenuGroupItemRender) {
			PCMenuGroupItemRender groupItem = (PCMenuGroupItemRender) this.getParentRender();
			buf.append(groupItem.addMenuBar(menubarVarId));
		} else {
			if (this.getCurrWidget() != null) {
				String widget = WIDGET_PRE + this.getViewId();
				buf.append("var ").append(widget).append(" = pageUI.getViewPart('" + this.getCurrWidget().getId() + "');\n");
				buf.append(widget + ".addMenu(" + menubarVarId + ");\n");
			} else
				buf.append("pageUI.addMenubar(" + menubarVarId + ");\n");
		}
		return buf.toString();
	}

	private void addMenuItem(MenubarComp menubar, StringBuilder buf) {
		// 构建菜单项
		if (menubar.getMenuList() != null) {
			Iterator<MenuItem> it = menubar.getMenuList().iterator();
			while (it.hasNext()) {
				MenuItem item = it.next();
				genOneMenuItem(menubar, buf, item);
			}
		}
	}

	private void genOneMenuItem(MenubarComp menubar, StringBuilder buf, MenuItem item) {
		item.setWidget(menubar.getWidget());
		if (menubarVarId == null || menubarVarId.length() == 0) {
			menubarVarId = this.getVarId();
			buf.append("var ").append(menubarVarId).append(" = ").append("pageUI.getViewPart('" + this.getViewId() + "').getMenu('" + this.getWebElement().getId() + "')").append(";\r\n");
		}
		renderItem(buf, item);
	}
	private void genMenuItem(MenubarComp menubar, StringBuilder buf, MenuItem item) {
		item.setWidget(menubar.getWidget());
		List<String> parentIds = item.getAllParents();
		menubarVarId = this.getVarId();
		buf.append("var ").append(menubarVarId).append(" = ").append("pageUI.getViewPart('" + this.getViewId() + "').getMenu('" + this.getWebElement().getId() + "')").append(";\r\n");
		if(parentIds!=null&&parentIds.size()!=0){
			buf.append(menubarVarId).append(" = ").append(menubarVarId);
			for(int i=parentIds.size() -1; i>=0; i--){
				buf.append(".getMenu('").append(parentIds.get(i)).append("')");
			}
			buf.append(";\n");
		}
		renderItem(buf, item);
	}

	private void renderItem(StringBuilder buf, MenuItem item) {
		if (item.isSep()) {
			buf.append(menubarVarId).append(".addSep();\n");
		} else {
			String menuId = menubarVarId + item.getId();
			// 提示信息
			String tip = item.getTip() == null ? translate(item.getI18nName(), item.getText(), item.getLangDir()) : translate(item.getTip(), item.getTip(), item.getLangDir());
			String displayHotKey = item.getDisplayHotKey();
			if (displayHotKey != null && !"".equals(displayHotKey)) {
				tip += "(" + displayHotKey + ")";
			}

			buf.append("\nvar ").append(menuId).append("=");
			buf.append(menubarVarId).append(".addMenu(\"").append(item.getId()).append("\",\"");
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
			buf.append(genItemScript(item));
		}
	}

	/**
	 * 生成子项菜单的JS代码
	 * 
	 * @param item
	 * @return
	 */
	private String genItemSubMenuScript(MenuItem item) {
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

					if (!cItem.isEnabled()) {
						buf.append(menubarVarId + ".setActive(false);\n");
					}
					if (!cItem.isVisible()) {
						buf.append(menubarVarId + ".hide();\n");
					}
					buf.append(addEventSupport(cItem, null, menubarVarId + cItem.getId(), getId()));

					buf.append(genItemSubMenuScript(cItem));
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
	private String genItemScript(MenuItem item) {
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

					if (!cItem.isEnabled()) {
						buf.append(menuId + ".setActive(false);\n");
					}
					if (!cItem.isVisible()) {
						buf.append(menuId + ".hide();\n");
					}
					
					buf.append(addEventSupport(cItem, this.getViewId(), menubarVarId + cItem.getId(), getId()));

					buf.append(genItemSubMenuScript(cItem));
				}
			}
		}

		return buf.toString();
	}

	public String createDesignTail() {

		StringBuffer buf = new StringBuffer();
		if (this.isEditMode()) {
			if (this.isGenEditableTail()) {
				return "";
			}

			String widgetId = this.getViewId() == null ? "" : this.getViewId();
			String uiid = this.getUiElement() == null ? "" : (String) this.getUiElement().getId();
			String eleid = this.getWebElement() == null ? "" : this.getWebElement().getId();
			MenubarComp menubar = this.getWebElement();
			String type = this.getRenderType(menubar);
			if (type == null)
				type = "";

			if (getDivId() == null) {
				LuiLogger.error("div id is null!" + this.getClass().getName());
			} else {
				buf.append("var params = {widgetid:'" + widgetId + "',uiid:'" + uiid + "',eleid:'" + eleid + "',type:'" + type + "'};\n");
				buf.append("$.design.getObj({divObj:$('#" + getDivId() + "')[0],params:params,objType:'component'});\n");

				// 创建item的EditableListener
				this.getWebElement();
				if (menubar.getMenuList() != null) {
					for (int i = 0; i < menubar.getMenuList().size(); i++) {
						MenuItem item = menubar.getMenuList().get(i);

						buf.append("var params_menu_div_" + item.getId() + " = {widgetid:'" + widgetId + "',uiid:'" + uiid + "',eleid:'" + eleid + "',subeleid:'" + item.getId()
								+ "',type:'menubar_menuitem'};\n");
						// buf.append(" alert(document.getElementById('menu_div_"
						// + item.getId()+"').innerHTML);");
						buf.append("$.design.getObj({divObj:$('#menu_div_" + item.getId() + "')[0]," + "params:params_menu_div_" + item.getId() + ",objType:'menubar_menuitem'});\n");
					}
				}
			}
			if (this.getDivId() != null) {
				buf.append("$('#").append(this.getDivId()).append("').css('padding','0px');\n");
			}
		}
		return buf.toString();
	}

	protected String getSourceType(IEventSupport ele) {
		return LuiPageContext.SOURCE_TYPE_MENUBAR_MENUITEM;
	}

	@Override
	public String getRenderType(LuiElement ele) {
		return LuiPageContext.SOURCE_TYPE_MENUBAR;
	}

	// 销毁menubar自身
	@Override
	public void destroy() {
		String divId = this.getDivId();
		UIComponent uiEle = this.getUiElement();
		PagePartMeta pagePartMeta = LuiRenderContext.current().getPagePartMeta();
		if (pagePartMeta != null && this.getUiElement() != null) {
			ViewPartMeta widget = pagePartMeta.getWidget(this.getUiElement().getViewId());
			if (widget != null) {
				WebComp webButton = (WebComp) widget.getViewMenus().getMenuBar(uiEle.getId());
				if (webButton == null) {
					webButton = widget.getViewComponents().getComponent(uiEle.getId());
				}
				StringBuffer buf = new StringBuffer();
				if (webButton != null) {
					if (divId != null) {
						buf.append("window.execDynamicScript2RemoveComponent('" + divId + "','" + uiEle.getViewId() + "','" + webButton.getId() + "');");
						widget.getViewMenus().removeMenuBar(uiEle.getId());
					} else {
						buf.append("alert('删除控件失败！未获得divId')");
					}
				}
				addDynamicScript(buf.toString());
			} else {

			}
		}
	}
	
	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void setItemVisible(MenuItem menuItem) {
		StringBuilder script = new StringBuilder();
		script.append(getMenuItemJSObject(this.getViewId(), this.getWebElement().getId(), menuItem.getId(), null));
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
		String pitemId = getPItemId(this.getWebElement(),menuItem.getId());
		script.append(getMenuItemJSObject(this.getViewId(), this.getWebElement().getId(), menuItem.getId(), pitemId));
		script.append(getVarId()).append(menuItem.getId());
		script.append(".setActive(" + menuItem.isEnabled() + ");\n");
		if (script.length() > 0) {
			addDynamicScript(script.toString());
		}
	}
	private String getPItemId(MenubarComp menubar, String menuItemId) {
		List<MenuItem> items = menubar.getMenuList();
		for(MenuItem it : items){
			List<MenuItem> childs = it.getChildList();
			if(childs != null && childs.size()>0){
				for(MenuItem chit : childs){
					if(StringUtils.equals(chit.getId(), menuItemId))
						return it.getId();
					else{
						List<MenuItem> childss = chit.getChildList();
						if(getPMItemid(childss, menuItemId, chit.getId()) != null)
							return getPMItemid(childss, menuItemId, chit.getId());
					}
				}
			}
		}
		return null;
	}

	private String getPMItemid(List<MenuItem> childs, String menuItemId, String pItemId) {
		if(childs != null && childs.size()>0){
			for(MenuItem chit : childs){
				if(StringUtils.equals(chit.getId(), menuItemId))
					return pItemId;
				else{
					List<MenuItem> childss = chit.getChildList();
					if(getPMItemid(childss, menuItemId, chit.getId()) != null)
						return getPMItemid(childss, menuItemId, chit.getId());
				}
			}
		}
		return null;
	}

	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void setItemText(MenuItem menuItem) {
		StringBuilder script = new StringBuilder();
		script.append(getMenuItemJSObject(this.getViewId(), this.getWebElement().getId(), menuItem.getId(), null));
		script.append(getVarId()).append(menuItem.getId());
		script.append(".changeCaption('" + menuItem.getText() + "');\n");
		if (script.length() > 0) {
			addDynamicScript(script.toString());
		}
	}
	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void setItemImg(MenuItem menuItem) {
		StringBuilder script = new StringBuilder();
		script.append(getMenuItemJSObject(this.getViewId(), this.getWebElement().getId(), menuItem.getId(), null));
		script.append(getVarId()).append(menuItem.getId());
		script.append(".changeImg('" + menuItem.getRealImgIcon() + "');\n");
		if (script.length() > 0) {
			addDynamicScript(script.toString());
		}
	}
	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void addItem(MenuItem menuItem) {
		StringBuilder script = new StringBuilder();
		MenubarComp menubar = this.getWebElement();
//		this.genOneMenuItem(menubar, script, menuItem);
		this.genMenuItem(menubar, script, menuItem);
		if (script.length() > 0) {
			addDynamicScript(script.toString());
		}
	}
	
	
	public void removeItem(MenuItem menuItem){
		StringBuilder script = new StringBuilder();
		MenubarComp menubar = this.getWebElement();
		//script.append("")
		
		menuItem.setWidget(menubar.getWidget());
		if (menubarVarId == null || menubarVarId.length() == 0) {
			menubarVarId = this.getVarId();
		}
		script.append("\nvar ").append(menubarVarId).append(" = ").append("pageUI.getViewPart('" + this.getViewId() + "').getMenu('" + this.getWebElement().getId() + "')").append(";\r\n");
		script.append( menubarVarId+".removeMenu('"+menuItem.getId()+"');\r\n");
		if (script.length() > 0) {
			addDynamicScript(script.toString());
		}

	}

	/**
	 * 获取menuItem JS对象
	 * 
	 * @param widgetId
	 * @param menuBarId
	 * @param itemId
	 * @return
	 */
	private String getMenuItemJSObject(String widgetId, String menuBarId, String itemId, String pItemId) {
		StringBuffer script = new StringBuffer();
		script.append("var ").append(getVarId()).append(itemId).append(" = ").append("pageUI.getViewPart('" + widgetId + "').getMenu('" + menuBarId + "')");
		if (pItemId != null) {
			script.append(".getMenu('" + pItemId + "')");
		}
		script.append(".getMenu('" + itemId + "');\n");
		return script.toString();
	}

}
