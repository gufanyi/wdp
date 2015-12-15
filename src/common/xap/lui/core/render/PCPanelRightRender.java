/**
 * 
 */
package xap.lui.core.render;

import xap.lui.core.comps.LuiElement;
import xap.lui.core.event.IEventSupport;
import xap.lui.core.layout.UIPanelRightPanel;
import xap.lui.core.model.LuiPageContext;

/**
 * @author chouhl
 *
 */
@SuppressWarnings("unchecked")
public class PCPanelRightRender extends UILayoutPanelRender<UIPanelRightPanel, LuiElement> {
	
	private String width;
	private String position = "right";

	public PCPanelRightRender(UIPanelRightPanel uiEle) {
		super(uiEle);
		this.width = this.getUiElement().getWidth();
	}
	
	@Override
	protected String mockId() {
		return this.getUiElement().getLayout().getId() + "_right";
	}
	
	
	
	@Override
	public String createTail() {
		UILayoutRender<?, ?> parent = (UILayoutRender<?, ?>) this.getParentRender();
		String panelLayoutId = parent.getUiElement().getId();
		StringBuilder buf = new StringBuilder();
		if(parent.getUiElement().getViewId() != null)
			buf.append("var panelLayout = pageUI.getViewPart('").append(parent.getUiElement().getViewId()).append("').getPanel('").append(panelLayoutId).append("');\n");
		else
			buf.append("var panelLayout = pageUI.getPanel('").append(panelLayoutId).append("');\n");
		buf.append("panelLayout.customDiv");
		buf.append(".appendChild($('#").append(getNewDivId()).append("')[0]);\n");
		return buf.toString();
	}
	
	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}
	
	@Override
	protected String getSourceType(IEventSupport ele) {
		return LuiPageContext.SOURCE_TYPE_PANELSPACE;
	}

	@Override
	public String placeSelf() {
		StringBuilder buf = new StringBuilder();
		buf.append("var ").append(getNewDivId()).append(" = $('<div>').attr('id','").append(getNewDivId()).append("').css({\n");
		if (width != null)
			buf.append("'width':'" + width + "px',\n");
		buf.append("'height':'100%',\n");
		buf.append("'overflow':'hidden'});\n");
		if (this.isEditMode()) {
			buf.append(this.placeDesign());
			buf.append(getNewDivId()).append(".append(" + getDivId() + ");\n");
		}
		else
			buf.append("$('body').append(" + getDivId() + ");\n");
		return buf.toString();
	}

}
