package xap.lui.core.echar;

//线条样式
public class LineStyle {

	@FieldMeta(desc = "颜色")
	private String color = null;// 颜色
	@FieldMeta(desc = "线条样式")
	@TypeEnum(types = { "solid", "dotted", "dashed", "curve", "broken" })
	private String type = "solid";// 线条样式，可选为：'solid' |
	@FieldMeta(desc = "线宽")
	private int width = 0;// 线宽
	@FieldMeta(desc = "阴影色彩")
	private String shadowColor = "rgba(0,0,0,0)";// 折线主线(IE8+)有效，阴影色彩，支持rgba
	@FieldMeta(desc = "阴影模糊度，大于0有效")
	private int shadowBlur = 5;// 折线主线(IE8+)有效，阴影模糊度，大于0有效
	@FieldMeta(desc = "阴影横向偏移，正值往右，负值往左")
	private int shadowOffsetX = 3;// 折线主线(IE8+)有效，阴影横向偏移，正值往右，负值往左
	@FieldMeta(desc = "阴影纵向偏移，正值往下，负值往上")
	private int shadowOffsetY = 3;// 折线主线(IE8+)有效，阴影纵向偏移，正值往下，负值往上

	public LineStyle() {
		super();
	}

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

	public String getShadowColor() {
		return shadowColor;
	}

	public void setShadowColor(String shadowColor) {
		this.shadowColor = shadowColor;
	}

	public int getShadowBlur() {
		return shadowBlur;
	}

	public void setShadowBlur(int shadowBlur) {
		this.shadowBlur = shadowBlur;
	}

	public int getShadowOffsetX() {
		return shadowOffsetX;
	}

	public void setShadowOffsetX(int shadowOffsetX) {
		this.shadowOffsetX = shadowOffsetX;
	}

	public int getShadowOffsetY() {
		return shadowOffsetY;
	}

	public void setShadowOffsetY(int shadowOffsetY) {
		this.shadowOffsetY = shadowOffsetY;
	}

}
