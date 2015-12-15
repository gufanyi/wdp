package xap.lui.core.echar;

//区域填充样式
public class AreaStyle {// 区域填充样式
	@FieldMeta(desc = "颜色")
	private String[] color = null;
	@FieldMeta(desc = "填充样式")
	private String type = "default";
	@FieldMeta(desc = "宽度")
	private String width = "auto";

	public AreaStyle() {
		super();
	}

	public String[] getColor() {
		return color;
	}

	public void setColor(String[] color) {
		this.color = color;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

}
