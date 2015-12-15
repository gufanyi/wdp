package xap.lui.core.echar;

public class LinkStyle {
	@FieldMeta(desc = "颜色")
	private String color = "#5182ab";// 颜色
	@FieldMeta(desc = "线条样式")
	@TypeEnum(types = { "curve", "line" })
	private String type = "line";// 线条样式，可选为：'solid' |
	@FieldMeta(desc = "线宽")
	private int width = 1;// 线宽
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	
	
}
