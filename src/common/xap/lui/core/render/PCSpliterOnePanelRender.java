package xap.lui.core.render;

import xap.lui.core.comps.LuiElement;
import xap.lui.core.event.IEventSupport;
import xap.lui.core.layout.UISplitterOne;
import xap.lui.core.model.LuiPageContext;

/**
 * @author renxh 分割面板左边的panel
 */
@SuppressWarnings("unchecked")
public class PCSpliterOnePanelRender extends UILayoutPanelRender<UISplitterOne, LuiElement> {

	public PCSpliterOnePanelRender(UISplitterOne uiEle,PCSpliterLayoutRender parentRender) {
		super(uiEle);
		this.parentRender=parentRender;
		// this.divId = parentRender.getDivId() + "_div_" + getDivIndex();

	}

	public String createHead() {
		PCSpliterLayoutRender parent = (PCSpliterLayoutRender) this.getParentRender();
		StringBuffer buf = new StringBuffer();
		buf.append(parent.getVarId()).append(".getDiv").append(getDivIndex());
		buf.append("().add($(\"#").append(getNewDivId()).append("\")[0]);\n");
		return buf.toString();
	}

	protected String getDivIndex() {
		return "1";
	}

	protected String getSourceType(IEventSupport ele) {
		return LuiPageContext.SOURCE_TYPE_SPLITERONEPANEL;
	}

	@Override
	public String placeSelf() {
		StringBuffer buf = new StringBuffer();
		buf.append("var ").append(getNewDivId()).append(" = $('<div>').attr('id','").append(getNewDivId()).append("').css({\n");
		buf.append("'width':'100%',\n");
		buf.append("'height':'100%'});\n");

		if (this.isEditMode()) {
			buf.append(this.placeDesign());
			buf.append(getNewDivId()).append(".append(" + getDivId() + ");\n");
		}

		return buf.toString();
	}

}
