package xap.lui.core.render;

import xap.lui.core.comps.LuiElement;
import xap.lui.core.event.IEventSupport;
import xap.lui.core.layout.UITabComp;
import xap.lui.core.layout.UITabRightPanel;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.model.LuiPageContext;

/**
 * @author renxh ab控件的左右空间填充部分
 */
@SuppressWarnings("unchecked")
public class PCTabRightRender extends UILayoutPanelRender<UITabRightPanel, LuiElement> {
	protected static final String CHILD_SCRIPT = "$CHILD_SCRIPT";
	private String width;
	private String position = "right";

	public PCTabRightRender(UITabRightPanel uiEle,PCTabLayoutRender parentRender) {
		super(uiEle);
//		UIRender pRender = (UIRender) this.getParentRender();
//		this.id = pRender.getId() + "_rightspace";
//		this.divId = DIV_PRE + getId();
		UITabRightPanel panel = this.getUiElement();
		this.width = panel.getWidth();
		this.parentRender=parentRender;
	}

	@Override
	protected String mockId() {
		UITabComp tab = (UITabComp) getUiElement().getLayout();
		return tab.getId() + "_right";
	}


	public String createHead() {
		return "";
	}

	

	public String createTail() {
		PCTabLayoutRender parent = (PCTabLayoutRender) this.getParentRender();
		UITabComp tabComp = (UITabComp) parent.getUiElement();
		String tabId = tabComp.getId();
		StringBuilder buf = new StringBuilder();
		if(tabComp.getViewId() != null)
			buf.append("var tab = pageUI.getViewPart('").append(tabComp.getViewId()).append("').getTab('").append(tabId).append("');\n");
		else
			buf.append("var tab = pageUI.getTab('").append(tabId).append("');\n");
		buf.append("tab.rightBarSpace");
		buf.append(".append($('#").append(getNewDivId()).append("').show());\n");
	    buf.append("tab.rightBarSpace.show();\n");
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

	protected String getSourceType(IEventSupport ele) {
		return LuiPageContext.SOURCE_TYPE_TABSPACE;
	}

	@Override
	public String placeSelf() {
		StringBuilder buf = new StringBuilder();
		buf.append("var ").append(getNewDivId()).append(" = $('<div>').attr('id','").append(getNewDivId()).append("').css({\n");
		if (width != null)
			buf.append("'width':'" + width + "px',\n");
		buf.append("'height':'100%',\n");
		buf.append("'overflow':'hidden'}).hide();\n");
		if (this.isEditMode()) {
			buf.append(this.placeDesign());
			buf.append(getNewDivId()).append(".append(" + getDivId() + ");\n");
			buf.append("$('body').append(" + getNewDivId() + ");\n");
		}
		else
			buf.append("$('body').append(" + getDivId() + ");\n");
		return buf.toString();
	}
	
	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void setTabRightPanelWidth(int width) {
		
		UITabRightPanel panel = this.getUiElement();
		UITabComp tabLayout = (UITabComp)panel.getLayout();
		String widgetId = getViewId();
		StringBuilder buf = new StringBuilder();
		buf.append("pageUI");
		if(widgetId != null && widgetId.length() > 0)
			buf.append(".getViewPart(\""+ getViewId() +"\")");
		buf.append(".getTab(\""+ tabLayout.getId() +"\")").append(".changeTabRightPanelWidth("+width+");\n");
		addDynamicScript(buf.toString());
	}

}
