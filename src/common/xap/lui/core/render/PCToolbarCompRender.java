package xap.lui.core.render;

import org.apache.commons.lang.StringUtils;
import xap.lui.core.comps.ToolBarComp;
import xap.lui.core.comps.ToolBarItem;
import xap.lui.core.event.IEventSupport;
import xap.lui.core.layout.UIToolBar;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.model.LuiPageContext;

/**
 * @author renxh 工具条渲染器
 */
@SuppressWarnings("unchecked")
public class PCToolbarCompRender extends UINormalComponentRender<UIToolBar, ToolBarComp> {

	public PCToolbarCompRender(ToolBarComp webEle) {
		super(webEle);
	}

	public String createBody() {
		ToolBarComp toolbar = this.getWebElement();
		StringBuilder buf = new StringBuilder();
		String widgetId = this.getCurrWidget().getId();
		String toolbarId = getVarId();
		buf.append("var ").append(toolbarId).append(" = $(\"<div id='" + toolbar.getId() + "'>\").appendTo($('#" + getDivId() + "')).toolbar({\n");
		buf.append("height:'").append(this.getUiElement().getHeight()).append("',");
		buf.append("transparent:").append(toolbar.isTransparent());
		buf.append("}).toolbar('instance');\n");
		buf.append("pageUI.getViewPart('" + widgetId + "').addComponent('" + toolbar.getId() + "'," + toolbarId + ");\n");
		ToolBarItem[] elements = toolbar.getElements();
		// 加载子项
		if (elements != null) {
			for (int i = 0, n = elements.length; i < n; i++) {
				ToolBarItem item = elements[i];
				if (item.getType() != null && ToolBarItem.BUTTON_TYPE.equals(item.getType()) && "left".equals(item.getAlign())) { // 按钮子项（左对齐）
					// 提示信息
					String tip = item.getTip();
					String displayHotKey = item.getDisplayHotKey();
					if (displayHotKey != null && !"".equals(displayHotKey)) {
						tip += "(" + displayHotKey + ")";
					}

					// function(id, text, tip, refImg, align, withSep, width,
					// disabled) {
					buf.append(toolbarId).append(".addButton({id:\"").append(item.getId()).append("\", text:\"").append(item.getText()).append("\", tip:\"").append(tip).append("\",");
					// .append(getRealImgPath(item.getRefImg()))
					if (StringUtils.isEmpty(item.getRealRefImg(getToolbarImgSize()))) {
						buf.append("refImg:null").append(",");
					} else {
						buf.append("refImg:\"").append(item.getRealRefImg(getToolbarImgSize())).append("\",");
					}

					buf.append("align:\"").append(item.getAlign()) // 此处配置的align为按钮在Toolbar中的位置
							.append("\", withSep:").append(item.isWithSep());
					// if (!"100%".equals(item.getWidth()))
					// buf.append(", \"" + item.getWidth() + "\"");
					// else
					buf.append(",width: ''");
					buf.append(",disabled: " + !item.isEnabled());
					buf.append(", visible:" + item.isVisible());
					String widget = this.getCurrWidget().getId();
					buf.append(",viewPartId:'" + widget + "'");
					String menu = item.getContextMenu();
					if (menu != null && !"".equals(menu)) {
						buf.append(",droplist:'" + menu + "'");
					}
					buf.append("});\n");

					String itemShowId = toolbarId + ".getButton('" + item.getId() + "')";

					// 为子项设置快捷键
					String hotKey = item.getHotKey();
					String modifier = String.valueOf(item.getModifiers());
					if (hotKey != null && !"".equals(hotKey)) {
						buf.append(itemShowId).append(".setHotKey(\"").append(hotKey + modifier).append("\");\n");
					}

					// 加下拉菜单
					if (menu != null && !"".equals(menu)) {
						buf.append(addDropList(menu));
					}
					buf.append(addEventSupport(item, getViewId(), itemShowId, getId()));
				}
			}
			for (ToolBarItem item : elements) {
				if (item.getType() != null || !ToolBarItem.BUTTON_TYPE.equals(item.getType())) {// TODO
																								// 其他子项

				}
			}
		}

		return buf.toString();
	}
	
	private String addDropList(String menuId) {
		if (this.isEditMode())
			return "";
		String menuShowId = COMP_PRE + getViewId() + "_" + menuId;
		StringBuilder buf = new StringBuilder();
		buf.append(createMenuRender(menuId));
		// 解决ContextMenu上的ContextMenu叠加问题
		buf.append(menuShowId).append(".addZIndex();\n");
		return wrapByRequired("contextmenu", buf.toString());
	}
	
	private String getToolbarImgSize() {
		String imgSize = "24";
		String _height = this.getUiElement().getHeight();
		//_height.indexOf("%")<0//是否是纯数字
		if(_height!=null && _height.matches("[0-9]+") && Integer.parseInt(_height)<40) {
			imgSize = "16";
		}
		return imgSize;
	}

	private void genOneToolbarItem(ToolBarComp toolbar, StringBuilder buf, ToolBarItem item) {
		item.setWidget(toolbar.getWidget());
		varId = this.getVarId();
		buf.append("var ").append(varId).append(" = ").append("pageUI.getViewPart('" + this.getViewId() + "').getComponent('" + this.getWebElement().getId() + "')").append(";\r\n");
		if (item.getType() != null && ToolBarItem.BUTTON_TYPE.equals(item.getType()) && "left".equals(item.getAlign())) { // 按钮子项（左对齐）
			// 提示信息
			String tip = item.getTip();
			String displayHotKey = item.getDisplayHotKey();
			if (displayHotKey != null && !"".equals(displayHotKey)) {
				tip += "(" + displayHotKey + ")";
			}

			// function(id, text, tip, refImg, align, withSep, width,
			// disabled) {
			buf.append(varId).append(".addButton({id:\"").append(item.getId()).append("\", text:\"").append(item.getText()).append("\", tip:\"").append(tip).append("\",");
			// .append(getRealImgPath(item.getRefImg()))
			if (StringUtils.isEmpty(item.getRealRefImg(getToolbarImgSize()))) {
				buf.append("refImg:null").append(",");
			} else {
				buf.append("refImg:\"").append(item.getRealRefImg(getToolbarImgSize())).append("\",");
			}

			buf.append("align:\"").append(item.getAlign()) // 此处配置的align为按钮在Toolbar中的位置
					.append("\", withSep:").append(item.isWithSep());
			// if (!"100%".equals(item.getWidth()))
			// buf.append(", \"" + item.getWidth() + "\"");
			// else
			buf.append(",width: ''");
			buf.append(",disabled: " + !item.isEnabled());
			buf.append(", visible:" + item.isVisible());
			String widget = this.getCurrWidget().getId();
			buf.append(",viewPartId:'" + widget + "'");
			String menu = item.getContextMenu();
			if (menu != null && !"".equals(menu)) {
				buf.append(",droplist:'" + menu + "'");
			}
			buf.append("});\n");

			String itemShowId = varId + ".getButton('" + item.getId() + "')";

			// 为子项设置快捷键
			String hotKey = item.getHotKey();
			String modifier = String.valueOf(item.getModifiers());
			if (hotKey != null && !"".equals(hotKey)) {
				buf.append(itemShowId).append(".setHotKey(\"").append(hotKey + modifier).append("\");\n");
			}

			// 加下拉菜单
			if (menu != null && !"".equals(menu)) {
				buf.append(addDropList(menu));
			}
			buf.append(addEventSupport(item, getViewId(), itemShowId, getId()));
		}
	}

	protected String getSourceType(IEventSupport ele) {

		return LuiPageContext.SOURCE_TYPE_TOOLBAR_BUTTON;
	}
	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void addToolBarItem(ToolBarItem toolBarItem) {
		StringBuilder script = new StringBuilder();
		ToolBarComp toolbar = this.getWebElement();
		this.genOneToolbarItem(toolbar, script, toolBarItem);
		if (script.length() > 0) {
			addDynamicScript(script.toString());
		}
	}
	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void setToolBarItemVisible(ToolBarItem toolBarItem) {
		StringBuilder script = new StringBuilder();
		script.append(getToolbarItemJSObject(this.getViewId(), this.getWebElement().getId(), toolBarItem.getId()));
		script.append(getVarId()).append(toolBarItem.getId());
		Boolean visible = toolBarItem.isVisible();
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
	public void setToolBarItemEnabled(ToolBarItem toolBarItem) {
		StringBuilder script = new StringBuilder();
		script.append(getToolbarItemJSObject(this.getViewId(), this.getWebElement().getId(), toolBarItem.getId()));
		script.append(getVarId()).append(toolBarItem.getId());
		script.append(".setActive(" + toolBarItem.isEnabled() + ");\n");
		if (script.length() > 0) {
			addDynamicScript(script.toString());
		}
	}
	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void setToolBarItemText(ToolBarItem toolBarItem) {
		StringBuilder script = new StringBuilder();
		script.append(getToolbarItemJSObject(this.getViewId(), this.getWebElement().getId(), toolBarItem.getId()));
		script.append(getVarId()).append(toolBarItem.getId());
		script.append(".changeCaption('" + toolBarItem.getText() + "');\n");
		if (script.length() > 0) {
			addDynamicScript(script.toString());
		}
	}

	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void setToolBarItemRefImg(ToolBarItem toolBarItem) {
		StringBuilder script = new StringBuilder();
		script.append(getToolbarItemJSObject(this.getViewId(), this.getWebElement().getId(), toolBarItem.getId()));
		script.append(getVarId()).append(toolBarItem.getId());
		script.append(".changeImage('" + toolBarItem.getRealRefImg(getToolbarImgSize()) + "');\n");
		if (script.length() > 0) {
			addDynamicScript(script.toString());
		}
	}

	/**
	 * 获取toolbarItem JS对象
	 * 
	 * @param widgetId
	 * @param toolBarId
	 * @param itemId
	 * @return
	 */
	private String getToolbarItemJSObject(String widgetId, String toolBarId, String itemId) {
		StringBuffer script = new StringBuffer();
		script.append("var ").append(getVarId()).append(itemId).append(" = ").append("pageUI.getViewPart('" + widgetId + "').getComponent('" + toolBarId + "')");
		script.append(".getButton('" + itemId + "');\n");
		return script.toString();
	}

}
