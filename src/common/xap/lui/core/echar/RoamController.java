package xap.lui.core.echar;

//缩放漫游组件，仅对地图有效
public class RoamController {
	@FieldMeta(desc = "显示策略")
	private Boolean show = null;
	@FieldMeta(desc = "一级层叠控制")
	private Integer zlevel =null;
	@FieldMeta(desc = "二级层叠控制")
	private Integer z = null;
	@TypeEnum(types = { "center", "left", "right" })
	@FieldMeta(desc = "水平安放位置")
	private String x = null;
	@TypeEnum(types = { "top", "bottom", "center" })
	@FieldMeta(desc = "垂直安放位置")
	private String y = null;
	@FieldMeta(desc = "指定宽度")
	private Integer width = null;
	@FieldMeta(desc = "指定高度")
	private Integer height =null;
	@FieldMeta(desc = "缩放漫游组件背景颜色")
	private String backgroundColor = null;
	@FieldMeta(desc = "缩放漫游组件边框颜色")
	private String borderColor = null;
	@FieldMeta(desc = "缩放漫游组件边框线宽")
	private Integer borderWidth = null;
	@FieldMeta(desc = "缩放漫游组件内边距")
	private Integer padding = null;
	@FieldMeta(desc = "漫游组件文字填充颜色")
	private String fillerColor = null;
	@FieldMeta(desc = "控制手柄主体颜色")
	private String handleColor = null;
	@FieldMeta(desc = "漫游移动步伐")
	private Integer step = null;

	public boolean isShow() {
		return show;
	}

	public void setShow(boolean show) {
		this.show = show;
	}

	public int getZlevel() {
		return zlevel;
	}

	public void setZlevel(int zlevel) {
		this.zlevel = zlevel;
	}

	public int getZ() {
		return z;
	}

	public void setZ(int z) {
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

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
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

	public int getBorderWidth() {
		return borderWidth;
	}

	public void setBorderWidth(int borderWidth) {
		this.borderWidth = borderWidth;
	}

	public int getPadding() {
		return padding;
	}

	public void setPadding(int padding) {
		this.padding = padding;
	}

	public String getFillerColor() {
		return fillerColor;
	}

	public void setFillerColor(String fillerColor) {
		this.fillerColor = fillerColor;
	}

	public String getHandleColor() {
		return handleColor;
	}

	public void setHandleColor(String handleColor) {
		this.handleColor = handleColor;
	}

	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
	}

}
