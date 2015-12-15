package xap.lui.core.render;

import xap.lui.core.comps.LuiElement;
import xap.lui.core.event.IEventSupport;
import xap.lui.core.layout.UIFlowhLayout;
import xap.lui.core.model.LuiPageContext;

/**
 * @author renxh
 * 横向布局渲染器
 * @param <T>
 * @param <K>
 */
@SuppressWarnings("unchecked")
public class PCFlowhLayoutRender extends UILayoutRender<UIFlowhLayout, LuiElement> {

	public PCFlowhLayoutRender(UIFlowhLayout uiEle) {
		super(uiEle);
	}


	public String createHead() {
		return toResize("$(\"#"+getDivId()+"\")[0]", "flowhResize");
	}


	public String createTail() {
		return "";
	}

	protected String getSourceType(IEventSupport ele) {
		return LuiPageContext.SOURCE_TYPE_FLOWHLAYOUT;
	}



}
