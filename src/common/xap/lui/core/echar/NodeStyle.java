package xap.lui.core.echar;

public class NodeStyle {
	@FieldMeta(desc = "填充颜色")
	private String color = "#f08c2e";// 线条颜色
	@FieldMeta(desc = "描边颜色")
	private String borderColor = "#5182ab";
	@FieldMeta(desc = "描边线宽")
	private int width = 1;// 线宽

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getBorderColor() {
		return borderColor;
	}

	public void setBorderColor(String borderColor) {
		this.borderColor = borderColor;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

}
