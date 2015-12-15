package xap.lui.core.render;

import xap.lui.core.comps.LuiElement;
import xap.lui.core.event.IEventSupport;
import xap.lui.core.layout.UIBorderTrue;
import xap.lui.core.model.LuiPageContext;

@SuppressWarnings("unchecked")
public class PCBorderTrueRender extends UILayoutPanelRender<UIBorderTrue, LuiElement> {

	public PCBorderTrueRender(UIBorderTrue uiEle) {
		super(uiEle);

	}



	@Override
	public String placeSelf() {
		StringBuffer buf = new StringBuffer();
		buf.append("var ").append(getNewDivId()).append(" = $('<div>').attr('id','").append(getNewDivId()).append("').css({\n");
		buf.append("'width' : '100%',\n");
		buf.append("'height' : '100%',\n");
		buf.append("'overflow' : 'hidden'});\n");
		if(this.isEditMode()){
			buf.append(this.placeDesign());
			buf.append(getNewDivId()).append(".append("+getDivId()+");\n");
		}
		return buf.toString();
	}


	public String createHead() {
		return "";
	}

	public String createTail() {
		return "";
	}

	protected String getSourceType(IEventSupport ele) {
		return LuiPageContext.SOURCE_TYPE_BORDERTRUE;
	}
}
