package xap.lui.core.render;

import xap.lui.core.comps.LuiElement;
import xap.lui.core.layout.UISplitterTwo;
import xap.lui.core.model.LuiPageContext;

/**
 * @author renxh
 * 分割面板右边的panel
 */
public class PCSpliterTwoPanelRender extends PCSpliterOnePanelRender {

	public PCSpliterTwoPanelRender(UISplitterTwo uiEle,PCSpliterLayoutRender parentRender) {
		super(uiEle,parentRender);
	}

	protected String getDivIndex() {
		return "2";
	}

	protected String getSourceType(LuiElement ele) {
		return LuiPageContext.SOURCE_TYPE_SPLITERTWOPANLE;
	}
}
