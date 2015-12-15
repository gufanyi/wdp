package xap.lui.core.plugins;

import xap.lui.core.context.BaseContext;

public class TextButtonContext extends BaseContext {
	private static final long serialVersionUID = 5560381058077222414L;
    private String width;
    private String height;
    private String value;
	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = width;
	}
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
