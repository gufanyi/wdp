package xap.lui.core.echar;

public class Grid {

	@FieldMeta(desc = "一级层叠控制")
	private Integer zlevel = null;
	@FieldMeta(desc = "二级层叠控制")
	private Integer z = null;
	@FieldMeta(desc = "左上角横坐标")
	private String x = null;// 直角坐标系内绘图网格左上角横坐标
	@FieldMeta(desc = "左上角纵坐标")
	private String y = null;// 直角坐标系内绘图网格左上角纵坐标，数值单位px
	@FieldMeta(desc = "右下角横坐标")
	private String x2 = null;// 直角坐标系内绘图网格右下角横坐标
	@FieldMeta(desc = "右下角纵坐标")
	private String y2 = null;// 直角坐标系内绘图网格右下角纵坐标
	@FieldMeta(desc = "直角坐标系内绘图网格(不含坐标轴)宽度")
	private Integer width = null;
	@FieldMeta(desc = "直角坐标系内绘图网格(不含坐标轴)高度")
	private Integer height = null;
	@FieldMeta(desc = "背景颜色")
	private String backgroundColor = null;
	@FieldMeta(desc = "边框颜色")
	private String borderColor = null;
	@FieldMeta(desc = "边框线宽")
	private Integer borderWidth = null;

	public Integer getZlevel() {
		return zlevel;
	}

	public void setZlevel(Integer zlevel) {
		this.zlevel = zlevel;
	}

	public Integer getZ() {
		return z;
	}

	public void setZ(Integer z) {
		this.z = z;
	}

	public String getX() {
		return x;
	}

	public void setX(String x) {
		this.x = x;
	}

	public String getY() {
		return y;
	}

	public void setY(String y) {
		this.y = y;
	}

	public String getX2() {
		return x2;
	}

	public void setX2(String x2) {
		this.x2 = x2;
	}

	public String getY2() {
		return y2;
	}

	public void setY2(String y2) {
		this.y2 = y2;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public String getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public String getBorderColor() {
		return borderColor;
	}

	public void setBorderColor(String borderColor) {
		this.borderColor = borderColor;
	}

	public Integer getBorderWidth() {
		return borderWidth;
	}

	public void setBorderWidth(Integer borderWidth) {
		this.borderWidth = borderWidth;
	}

}
