package xap.lui.core.render;

import xap.lui.core.comps.LuiElement;
import xap.lui.core.event.IEventSupport;
import xap.lui.core.layout.UIMenuGroup;
import xap.lui.core.model.LuiPageContext;

/**
 * @author renxh 菜单组控件 渲染器
 */
public class PCMenuGroupRender extends UILayoutRender<UIMenuGroup, LuiElement> {

	public PCMenuGroupRender(UIMenuGroup uiEle) {
		super(uiEle);
	}

	public String createHead() {

		StringBuilder buf = new StringBuilder();
		buf.append("window." + varId + " = $(\"<div>\").menuBarGroup({id : '" + getId() + "'}).menuBarGroup('instance');\n");
		buf.append("pageUI.addMenubarGroup(window." + varId + ");\n");
		return buf.toString();
	}

	public String createTail() {

		return "";
	}

	protected String getSourceType(IEventSupport ele) {
		return LuiPageContext.SOURCE_TYPE_MENU_GROUP;
	}

	@Override
	public String placeSelf() {
		StringBuilder buf = new StringBuilder();
		buf.append("var ").append(getNewDivId()).append(" = $('<div>').attr('id','").append(getNewDivId()).append("').css({\n");
		buf.append("'width':'100%',\n");
		buf.append("'height':'30px',\n");
		buf.append("'top':'0px',\n");
		buf.append("'position':'relative'});\n");
		if (this.isEditMode()) {
			buf.append(this.placeDesign());
			buf.append(getNewDivId()).append(".append(" + getDivId() + ");\n");
		}

		return buf.toString();
	}
}
