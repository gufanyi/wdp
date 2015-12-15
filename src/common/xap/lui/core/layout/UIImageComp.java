package xap.lui.core.layout;

public class UIImageComp extends UIComponent {
	private static final long serialVersionUID = 1L;

	private String image_height;
	private String image_width;

	public UIImageComp() {
		this.image_height = "100%";
		this.image_width = "100%";
	}

	public void setImageWidth(String width) {
		this.image_width = width;
	}

	public void setImageHeight(String height) {
		this.image_height = height;
	}

	public String getImageWidth() {
		return image_width;
	}

	public String getImageHeight() {
		return image_height;
	}
}
