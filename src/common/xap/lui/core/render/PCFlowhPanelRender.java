package xap.lui.core.render;

import xap.lui.core.comps.LuiElement;
import xap.lui.core.event.IEventSupport;
import xap.lui.core.layout.UIFlowhPanel;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.model.LuiPageContext;

/**
 * @author renxh 横向布局的panel渲染器
 * @param <T>
 * @param <K>
 */
@SuppressWarnings("unchecked")
public class PCFlowhPanelRender extends UILayoutPanelRender<UIFlowhPanel, LuiElement> {

	private String width;

	/**
	 * @param uiEle
	 * @param uimeta
	 * @param pageMeta
	 * @param parentRender
	 */
	public PCFlowhPanelRender(UIFlowhPanel uiEle) {
		super(uiEle);

		UIFlowhPanel flowhPanel = this.getUiElement();
		width = this.getFormatSize(flowhPanel.getWidth());
		if (width.equals("0px"))
			width = null;
	}

	protected String getSourceType(IEventSupport ele) {
		return LuiPageContext.SOURCE_TYPE_FLOWHPANEL;
	}

	@Override
	public String placeSelf() {
		UIFlowhPanel flowhPanel = this.getUiElement();
		StringBuilder buf = new StringBuilder();
		String newDivId = getNewDivId();
		String paddingTop = getPaddingTopString(false);
		String paddingBottom = getPaddingBottomString(false);
		// String height = "100%;";
		// if(!paddingTop.equals("") || !paddingBottom.equals(""))
		// height = "";
		buf.append("var ").append(newDivId).append(" = $('<div>')").append(".attr('id','").append(newDivId).append("').css({");
		if (paddingTop.equals("") && paddingBottom.equals(""))
			buf.append("'height':'100%',\n");
		buf.append("'position':'relative'});\n");
		// buf.append(newDivId).append(".style[CSSFLOAT] = 'left';\n");
		buf.append(newDivId).append(".css({'float':'" + flowhPanel.getFloat() + "',\n");
		buf.append("'min-height':'").append(MIN_HEIGHT).append("'})");
		buf.append(".addClass('").append(flowhPanel.getClassName()).append("');\n");

		getBorderScript(buf, getNewDivId());
		getCssStylesScript(buf, getNewDivId());
		getPaddingLeftScript(buf, getNewDivId());
		getPaddingRightScript(buf, getNewDivId());
		getPaddingTopScript(buf, getNewDivId());
		getPaddingBottomScript(buf, getNewDivId());

		if (width != null) {
			buf.append(newDivId).append(".css({'width':'" + width + "'})\n");
			buf.append(".attr('haswidth','1');\n");
		} else {
			buf.append(newDivId).append(".attr('haswidth','0');\n");
		}

		if (this.isEditMode()) {
			buf.append(this.placeDesign());

			buf.append(newDivId).append(".append(" + getDivId() + ");\n");
		}
		return buf.toString();
	}

	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void setWidth(int width) {
		StringBuilder buf = new StringBuilder();
		buf.append("var ").append(getNewDivId()).append(" = $('#" + getNewDivId() + "')\n");
		if (width > 0) {
			buf.append(".css({'width':'" + width + "px',\n");
			buf.append("'float':'left',\n");
			buf.append("'margin-left':'0px'})\n");
			buf.append(".attr('haswidth','1');\n");

		} else {
			buf.append(getNewDivId()).append(".attr('haswidth','0');\n");
		}
		buf.append("$(window).triggerHandler('resize');\n");
		addDynamicScript(buf.toString());
	}

}
