package xap.lui.core.render;

import xap.lui.core.comps.LuiElement;
import xap.lui.core.event.IEventSupport;
import xap.lui.core.layout.UIMenuGroupItem;
import xap.lui.core.logger.LuiLogger;
import xap.lui.core.model.LuiPageContext;

@SuppressWarnings("unchecked")
public class PCMenuGroupItemRender extends UILayoutPanelRender<UIMenuGroupItem, LuiElement> {

	public PCMenuGroupItemRender(UIMenuGroupItem uiEle,PCMenuGroupRender parentRender) {
		super(uiEle);
		UIMenuGroupItem item = this.getUiElement();
		try {
			this.state = item.getState().toString();
		} catch (Exception e) {
			LuiLogger.error(e);
		}

	}

	private String state = null;

	public String addMenuBar(String menubarId) {
		StringBuilder sb = new StringBuilder();
		PCMenuGroupRender tab = (PCMenuGroupRender) this.getParentRender();
		String groupId = COMP_PRE + tab.getId();
		sb.append(groupId).append(".addItem('").append(getState()).append("', ").append(menubarId).append(");\n");
		return sb.toString();
	}

	public String getState() {
		return state;
	}

	
	protected String getSourceType(IEventSupport ele) {
		return LuiPageContext.SOURCE_TYPE_MENU_GROUP_ITEM;
	}

	@Override
	public String placeSelf() {
		return "";
	}

}
