package xap.lui.core.echar;

public class Legend {
	@FieldMeta(desc = "显示策略")
	private Boolean show = null;
	@FieldMeta(desc = "一级层叠控制")
	private Integer zlevel = null;
	@FieldMeta(desc = "二级层叠控制")
	private Integer z = null;
	@TypeEnum(types = { "horizontal", "vertical" })
	@FieldMeta(desc = "布局方式")
	private String orient = null;
	@TypeEnum(types = { "center", "left", "right" })
	@FieldMeta(desc = "水平安放位置")
	private String x = null;
	@TypeEnum(types = { "top", "bottom", "center" })
	@FieldMeta(desc = "垂直安放位置")
	private String y = null;
	@FieldMeta(desc = "图例背景颜色，默认透明")
	private String backgroundColor = null;
	@FieldMeta(desc = "#ccc")
	private String borderColor = null;
	@FieldMeta(desc = "borderWidth")
	private Integer borderWidth = 0;
	@FieldMeta(desc = "图例内边距，单位px")
	private Integer padding = null;
	@FieldMeta(desc = "各个item之间的间隔，单位px")
	private Integer itemGap = null;
	@FieldMeta(desc = "图例图形宽度")
	private Integer itemWidth = null;
	@FieldMeta(desc = "图例图形高度")
	private Integer itemHeight = null;
	@FieldMeta(desc = "图例文字")
	private TextStyle textStyle = null;
	@FieldMeta(desc = "文本格式器")
	private String formatter = null;
	@FieldMeta(desc = "选择模式")
	private Boolean selectedMode = null;
	private String[] data = null;
	public Boolean getShow() {
		return show;
	}
	public void setShow(Boolean show) {
		this.show = show;
	}
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
	public String getOrient() {
		return orient;
	}
	public void setOrient(String orient) {
		this.orient = orient;
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
	public Integer getPadding() {
		return padding;
	}
	public void setPadding(Integer padding) {
		this.padding = padding;
	}
	public Integer getItemGap() {
		return itemGap;
	}
	public void setItemGap(Integer itemGap) {
		this.itemGap = itemGap;
	}
	public Integer getItemWidth() {
		return itemWidth;
	}
	public void setItemWidth(Integer itemWidth) {
		this.itemWidth = itemWidth;
	}
	public Integer getItemHeight() {
		return itemHeight;
	}
	public void setItemHeight(Integer itemHeight) {
		this.itemHeight = itemHeight;
	}
	public TextStyle getTextStyle() {
		return textStyle;
	}
	public void setTextStyle(TextStyle textStyle) {
		this.textStyle = textStyle;
	}
	public String getFormatter() {
		return formatter;
	}
	public void setFormatter(String formatter) {
		this.formatter = formatter;
	}
	public Boolean getSelectedMode() {
		return selectedMode;
	}
	public void setSelectedMode(Boolean selectedMode) {
		this.selectedMode = selectedMode;
	}
	public String[] getData() {
		return data;
	}
	public void setData(String[] data) {
		this.data = data;
	}

	


}
