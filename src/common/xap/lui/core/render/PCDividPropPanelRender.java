package xap.lui.core.render;

import xap.lui.core.comps.LuiElement;
import xap.lui.core.layout.UIDividProp;
import xap.lui.core.model.LuiPageContext;

/**
 * @author lxl
 * 分割面板
 */
public class PCDividPropPanelRender extends PCDividCenterPanelRender {

	public PCDividPropPanelRender(UIDividProp uiEle,PCDividLayoutRender parentRender) {
		super(uiEle,parentRender);
	}

	protected String getDivIndex() {
		return "PropDiv";
	}

	protected String getSourceType(LuiElement ele) {
		return LuiPageContext.SOURCE_TYPE_DIVIDPROPPANEL;
	}
	
	
	
}
