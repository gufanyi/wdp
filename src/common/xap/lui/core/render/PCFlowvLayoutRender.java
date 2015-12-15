package xap.lui.core.render;

import xap.lui.core.comps.LuiElement;
import xap.lui.core.event.IEventSupport;
import xap.lui.core.layout.UIFlowvLayout;
import xap.lui.core.model.LuiPageContext;

/**
 * @author renxh 纵向布局渲染器
 * @param <T>
 * @param <K>
 */
@SuppressWarnings("unchecked")
public class PCFlowvLayoutRender extends UILayoutRender<UIFlowvLayout, LuiElement> {

	// FLOW-V 布局ID基础字符串
	protected static final String FLOW_V_ID_BASE = "pc_flowv_layout_";

	// // 没设置高度子项是否自动填充
	// private boolean autoFill = true;

	public PCFlowvLayoutRender(UIFlowvLayout uiEle) {
		super(uiEle);
	}

	public String createHead() {
		StringBuffer buf = new StringBuffer();
		if (!isFlowMode()) {
			buf.append(toResize("$(\"#" + getDivId() + "\")[0]", "flowvResize"));
			buf.append("addLayoutMonitor($('#").append(getDivId()).append("')[0]);\n");
		}
		return buf.toString();
	}

	public String createTail() {

		return "";
	}

	protected String getSourceType(IEventSupport ele) {
		return LuiPageContext.SOURCE_TYPE_FLOWVLAYOUT;
	}

	@Override
	public String placeSelf() {
		StringBuilder buf = new StringBuilder();
		buf.append("var ").append(getNewDivId()).append(" = $('<div>').attr('id','").append(getNewDivId()).append("').css({\n");
		if (!isFlowMode())
			buf.append("'height':'100%',\n");
		buf.append("'width':'100%',\n");
		buf.append("'position':'relative',\n");
		//buf.append(getNewDivId()).append(".style.overflowY = 'auto';\n");
		buf.append("'overflow':'hidden'});\n");
		if (this.isEditMode()) {
			buf.append(this.placeDesign());
			buf.append(getNewDivId()).append(".append(" + getDivId() + ");\n");
			this.getCssStylesScript(buf,getDivId());
		} else {
			this.getCssStylesScript(buf,getNewDivId());
		}
		
		return buf.toString();
	}
}
