package xap.lui.core.render;

import xap.lui.core.comps.LuiElement;
import xap.lui.core.event.IEventSupport;
import xap.lui.core.layout.UIDividCenter;
import xap.lui.core.model.LuiPageContext;

/**
 * 分割面板中心
 * 
 * @author lxl
 */
@SuppressWarnings("unchecked")
public class PCDividCenterPanelRender extends UILayoutPanelRender<UIDividCenter, LuiElement> {

	public PCDividCenterPanelRender(UIDividCenter uiEle, PCDividLayoutRender parentRender) {
		super(uiEle);
		this.parentRender = parentRender;
	}

	public String createHead() {
		PCDividLayoutRender parent = (PCDividLayoutRender) this.getParentRender();
		StringBuffer buf = new StringBuffer();
		buf.append(parent.getVarId()).append(".set").append(getDivIndex()).append("('").append(getNewDivId()).append("');\n");
		return buf.toString();
	}

	protected String getDivIndex() {
		return "CenterDiv";
	}

	protected String getSourceType(IEventSupport ele) {
		return LuiPageContext.SOURCE_TYPE_DIVIDCENTERPANEL;
	}

	@Override
	public String placeSelf() {
		StringBuffer buf = new StringBuffer();
		buf.append("var ").append(getNewDivId()).append(" = $('<div>')").append(".attr('id','").append(getNewDivId()).append("').css({");
		buf.append("'width':'100%',\n");
		buf.append("'height':'100%'\n});");

		if (this.isEditMode()) {
			buf.append(this.placeDesign());
			buf.append(getNewDivId()).append(".append(" + getDivId() + ");\n");
		}

		return buf.toString();
	}

}
