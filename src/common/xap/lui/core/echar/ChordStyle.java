package xap.lui.core.echar;

//和弦图中的弦样式
public class ChordStyle {
	@FieldMeta(desc = "贝塞尔曲线的线宽")
	private int width = 1;
	@FieldMeta(desc = "贝塞尔曲线的颜色")
	private String color = "1";// 贝塞尔曲线的颜色, ribbonType是false时有效
	@FieldMeta(desc = "ribbon的描边线宽")
	private int borderWidth = 1;// ribbon的描边线宽, ribbonType是true时有效
	@FieldMeta(desc = "ribbon的描边颜色")
	private String borderColor = "1";// ribbon的描边颜色, ribbonType是true时有效

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public int getBorderWidth() {
		return borderWidth;
	}

	public void setBorderWidth(int borderWidth) {
		this.borderWidth = borderWidth;
	}

	public String getBorderColor() {
		return borderColor;
	}

	public void setBorderColor(String borderColor) {
		this.borderColor = borderColor;
	}

}
