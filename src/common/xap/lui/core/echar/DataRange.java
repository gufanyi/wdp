package xap.lui.core.echar;

public class DataRange {
	@FieldMeta(desc = "显示策略")
	private Boolean show = false;
	@FieldMeta(desc = "一级层叠控制")
	private Integer zlevel = 1;
	@FieldMeta(desc = "二级层叠控制")
	private Integer z = 8;
	@TypeEnum(types = { "horizontal", "vertical" })
	@FieldMeta(desc = "布局方式，默认为垂直布局")
	private String orient = "horizontal";
	@TypeEnum(types = { "center", "left", "right" })
	@FieldMeta(desc = "水平安放位置，默认为全图左对齐")
	private String x = "left";
	@TypeEnum(types = { "top", "bottom", "center" })
	@FieldMeta(desc = "垂直安放位置，默认为全图底部")
	private String y = "top";
	@FieldMeta(desc = "值域控件背景颜色")
	private String backgroundColor = "RGBA(0, 0, 0, 0)";
	@FieldMeta(desc = "值域控件边框颜色")
	private String borderColor = "#ccc";
	@FieldMeta(desc = "值域控件边框线宽")
	private Integer borderWidth = 0;
	@FieldMeta(desc = "值域控件内边距，单位px")
	private Integer padding = 5;
	@FieldMeta(desc = "各个item之间的间隔，单位px")
	private Integer itemGap = 10;
	@FieldMeta(desc = "值域控件图形宽度")
	private Integer itemWidth = 20;
	@FieldMeta(desc = "值域控件图形高度")
	private Integer itemHeight = 14;
	@FieldMeta(desc = "指定的最小值")
	private Integer min = 0;
	@FieldMeta(desc = "指定的最大值")
	private Integer max = 0;
	@FieldMeta(desc = "小数精度")
	private Integer precision = 0;
	@FieldMeta(desc = "分割段数")
	private Integer splitNumber = 5;
	@FieldMeta(desc = "初始选中范围")
	private Object range = null;
	@FieldMeta(desc = "选择模式")
	private Boolean selectedMode = false;
	@FieldMeta(desc = "是否启用值域漫游")
	private Boolean calculable = false;
	@FieldMeta(desc = "是否启用地图hover时的联动响应")
	private Boolean hoverLink = true;
	@FieldMeta(desc = "值域漫游是否实时显示")
	private Boolean realtime = true;
	@FieldMeta(desc = "值域颜色标识")
	private String[] color = { "#1e90ff", "#f0ffff" };
	@FieldMeta(desc = "内容格式器")
	private String formatter = null;
	@FieldMeta(desc = "值域文字显示")
	private String[] text = null;
	@FieldMeta(desc = "值域控件文字颜色")
	private TextStyle textStyle = null;

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

	public Integer getMin() {
		return min;
	}

	public void setMin(Integer min) {
		this.min = min;
	}

	public Integer getMax() {
		return max;
	}

	public void setMax(Integer max) {
		this.max = max;
	}

	public Integer getPrecision() {
		return precision;
	}

	public void setPrecision(Integer precision) {
		this.precision = precision;
	}

	public Integer getSplitNumber() {
		return splitNumber;
	}

	public void setSplitNumber(Integer splitNumber) {
		this.splitNumber = splitNumber;
	}

	public Object getRange() {
		return range;
	}

	public void setRange(Object range) {
		this.range = range;
	}

	public Boolean getSelectedMode() {
		return selectedMode;
	}

	public void setSelectedMode(Boolean selectedMode) {
		this.selectedMode = selectedMode;
	}

	public Boolean getCalculable() {
		return calculable;
	}

	public void setCalculable(Boolean calculable) {
		this.calculable = calculable;
	}

	public Boolean getHoverLink() {
		return hoverLink;
	}

	public void setHoverLink(Boolean hoverLink) {
		this.hoverLink = hoverLink;
	}

	public Boolean getRealtime() {
		return realtime;
	}

	public void setRealtime(Boolean realtime) {
		this.realtime = realtime;
	}

	public String[] getColor() {
		return color;
	}

	public void setColor(String[] color) {
		this.color = color;
	}

	public String getFormatter() {
		return formatter;
	}

	public void setFormatter(String formatter) {
		this.formatter = formatter;
	}

	public String[] getText() {
		return text;
	}

	public void setText(String[] text) {
		this.text = text;
	}

	public TextStyle getTextStyle() {
		return textStyle;
	}

	public void setTextStyle(TextStyle textStyle) {
		this.textStyle = textStyle;
	}

}
