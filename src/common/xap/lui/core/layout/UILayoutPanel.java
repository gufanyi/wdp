package xap.lui.core.layout;

import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;

import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.render.UILayoutPanelRender;

public class UILayoutPanel extends UIElement {
	private static final long serialVersionUID = 1013723661597693848L;
	public static final String LEFTPADDING = "leftPadding";
	public static final String RIGHTPADDING = "rightPadding";
	public static final String LEFTBORDER = "leftBorder";
	public static final String RIGHTBORDER = "rightBorder";
	public static final String TOPBORDER = "topBorder";
	public static final String BOTTOMBORDER = "bottomBorder";
	public static final String BORDER = "border";
	public static final String TOPPADDING = "topPadding";
	public static final String BOTTOMPADDING = "bottomPadding";
	public static final String CSSSTYLE = "cssStyle";
	public static final String FLOAT = "float";

	private String leftPadding;
	private String rightPadding;
	private String leftBorder;
	private String rightBorder;
	private String topBorder;
	private String bottomBorder;
	private String border;
	private String topPadding;
	private String bottomPadding;
	private String cssStyle;
	private String flaot;

	// private UILayout layout;
	//
	// private UIElement element;

	@XmlElementRefs({
			@XmlElementRef(name = "FlowHLayout", type = UIFlowhLayout.class),
			@XmlElementRef(name = "FlowVLayout", type = UIFlowvLayout.class),
			@XmlElementRef(name = "Splitter", type = UISplitter.class) })
	private UILayout layout;
	@XmlElementRefs({
			@XmlElementRef(name = "FlowHLayout", type = UIFlowhLayout.class),
			@XmlElementRef(name = "FlowVLayout", type = UIFlowvLayout.class),
			@XmlElementRef(name = "ViewPart", type = UIViewPart.class),
			@XmlElementRef(name = "AbsoluteLayout", type = UIAbsoluteLayout.class)})
	private UIElement element;

	public UIElement getElement() {
		return element;
	}

	public void setElement(UIElement element) {
		this.element = element;
		super.addElement(element);
	}

	@Override
	public void removeElement(UIElement ele) {
		super.removeElement(ele);
		this.element = null;
	}

	@Override
	public String getViewId() {
		String widgetId = super.getViewId();
		if (widgetId == null && layout != null) {
			return layout.getViewId();
		}
		return widgetId;
	}

	@Override
	public UILayoutPanel doClone() {
		UILayoutPanel panel = (UILayoutPanel) super.doClone();
		if (this.element != null) {
			panel.element = (UIElement) this.element.doClone();
		}
		return panel;
	}

	public String getLeftPadding() {
		return leftPadding;
	}

	public String getRightPadding() {
		return rightPadding;
	}

	public void setLeftBorder(String leftBorder, boolean notRender) {
		this.leftBorder = leftBorder;
	}
	
	public void setLeftBorder(String leftBorder) {
		this.leftBorder = leftBorder;
		if(LifeCyclePhase.ajax.equals(getPhase())) {
			((UILayoutPanelRender)this.getRender()).setBorder();
		}
	}

	public void setRightBorder(String rightBorder, boolean notRender) {
		this.rightBorder = rightBorder;
	}
	
	public void setRightBorder(String rightBorder) {
		this.rightBorder = rightBorder;
		if(LifeCyclePhase.ajax.equals(getPhase())) {
			((UILayoutPanelRender)this.getRender()).setBorder();
		}
	}

	public void setTopBorder(String topBorder, boolean notRender) {
		this.topBorder = topBorder;
	}
	
	public void setTopBorder(String topBorder) {
		this.topBorder = topBorder;
		if(LifeCyclePhase.ajax.equals(getPhase())) {
			((UILayoutPanelRender)this.getRender()).setBorder();
		}
	}

	public void setBottomBorder(String bottomBorder, boolean notRender) {
		this.bottomBorder = bottomBorder;
	}
	
	public void setBottomBorder(String bottomBorder) {
		this.bottomBorder = bottomBorder;
		if(LifeCyclePhase.ajax.equals(getPhase())) {
			((UILayoutPanelRender)this.getRender()).setBorder();
		}
	}

	public String getLeftBorder() {
		return leftBorder;
	}

	public String getRightBorder() {
		return rightBorder;
	}

	public String getBottomBorder() {
		return bottomBorder;
	}

	public String getTopBorder() {
		return topBorder;
	}

	@SuppressWarnings("rawtypes")
	public void setBorder(String border) {
		this.border = border;
		if(LifeCyclePhase.ajax.equals(getPhase())) {
			((UILayoutPanelRender)this.getRender()).setBorder();
		}
	}

	public String getBorder() {
		return border;
	}

	public String getBottomPadding() {
		return bottomPadding;
	}

	public String getTopPadding() {
		return topPadding;
	}

	public String getCssStyle() {
		return cssStyle;
	}

	@SuppressWarnings("rawtypes")
	public void setCssStyle(String cssStyle) {
		this.cssStyle = cssStyle;
		if(LifeCyclePhase.ajax.equals(getPhase())) {
			((UILayoutPanelRender)this.getRender()).setCssStyle();
		}
	}

	public void setTopPadding(String topPadding) {
		this.topPadding = topPadding;
		if(LifeCyclePhase.ajax.equals(getPhase())) {
			((UILayoutPanelRender)this.getRender()).setTopPadding();
		}
	}
	public void setBottomPadding(String bottomPadding) {
		this.bottomPadding = bottomPadding;
		if(LifeCyclePhase.ajax.equals(getPhase())){
			((UILayoutPanelRender)this.getRender()).setBottomPadding();
		}
	}
	public void setLeftPadding(String leftPadding) {
		this.leftPadding = leftPadding;
		if(LifeCyclePhase.ajax.equals(getPhase())){
			((UILayoutPanelRender)this.getRender()).setLeftPadding();
		}
	}
	public void setRightPadding(String rightPadding) {
		this.rightPadding = rightPadding;
		if(LifeCyclePhase.ajax.equals(getPhase())){
			((UILayoutPanelRender)this.getRender()).setRightPadding();
		}
	}
	
	public String getFloat() {
		String value = flaot;
		if (value == null) {
			value = "left";
		}
		return value;
	}

	public void setFloat(String value) {
		this.flaot = value;
	}

	public UILayout getLayout() {
		return layout;
	}

	public void setLayout(UILayout layout) {
		this.layout = layout;
	}
	
}
