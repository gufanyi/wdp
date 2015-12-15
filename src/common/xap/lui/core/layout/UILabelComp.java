package xap.lui.core.layout;


import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.render.PCLabelCompRender;

public class UILabelComp extends UIComponent {

	public static final String STYLE = "style";
	public static final String FAMILY = "family";
	public static final String SIZE = "size";
	private static final long serialVersionUID = 1L;
	public static final String TEXTALIGN = "textAlign";
	public static final String CSSSTYLE = "cssStyle";

	private String textAlign;
	private String cssStyle;
	private String size;
	private String family;
	private String style;

	public UILabelComp() {
		super("100","22");
	}


	public void setTextAlign(String textAlign) {
		this.textAlign = textAlign;
		if (LifeCyclePhase.ajax.equals(getPhase())) {
			((PCLabelCompRender)this.getRender()).setTextAlign(textAlign);
		}
	}

	public String getTextAlign() {
		return textAlign;
	}

	public void setCssStyle(String cssStyle) {
		this.cssStyle = cssStyle;
	}

	public String getCssStyle() {
		return cssStyle;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size=size;
		if (LifeCyclePhase.ajax.equals(getPhase())) {
			((PCLabelCompRender)this.getRender()).setSize(Integer.parseInt(size));
		}
	}

	public String getFamily() {
		return family;
	}

	public void setFamily(String family) {
		this.family=family;
		if (LifeCyclePhase.ajax.equals(getPhase())) {
			((PCLabelCompRender)this.getRender()).setFamily(family);
		}
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style=style;
		if (LifeCyclePhase.ajax.equals(getPhase())) {
			((PCLabelCompRender)this.getRender()).setStyle(style);
		}
	}

}
