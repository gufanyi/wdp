package xap.lui.core.echar;

public class Tooltip {
	@FieldMeta(desc = "显示策略")
	private Boolean show = null;
	@FieldMeta(desc = "一级层叠控制")
	private Integer zlevel = null;
	@FieldMeta(desc = "二级层叠控制")
	private Integer z = null;
	@FieldMeta(desc = "tooltip主体内容显示策略")
	private Boolean showContent = null;
	@FieldMeta(desc = "触发类型，默认数据触发")
	private String trigger = "item";
	@FieldMeta(desc = "位置指定")
	private String[] position = null;
	@FieldMeta(desc = "内容格式器")
	private String formatter = null;
	@FieldMeta(desc = "拖拽重计算独有")
	private String islandFormatter = null;
	@FieldMeta(desc = "显示延迟，添加显示延迟可以避免频繁切换")
	private Integer showDelay = null;
	@FieldMeta(desc = "隐藏延迟，单位ms")
	private Integer hideDelay = null;
	@FieldMeta(desc = "动画变换时长，单位s")
	private Float transitionDuration = null;
	@FieldMeta(desc = "鼠标是否可进入详情气泡中")
	private Boolean enterable = null;
	@FieldMeta(desc = "提示背景颜色，默认为透明度为0.7的黑色")
	private String backgroundColor = null;
	@FieldMeta(desc = "提示边框颜色")
	private String borderColor = null;
	@FieldMeta(desc = "提示边框圆角,单位px")
	private Integer borderRadius = null;
	@FieldMeta(desc = "提示边框线宽,单位px")
	private Integer borderWidth = null;
	@FieldMeta(desc = "提示内边距,单位px,默认各方向内边距为5")
	private Integer padding = null;
	@FieldMeta(desc = "坐标轴指示器")
	private AxisPointer axisPointer = null;
	@FieldMeta(desc = "文本样式")
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

	public Boolean getShowContent() {
		return showContent;
	}

	public void setShowContent(Boolean showContent) {
		this.showContent = showContent;
	}

	public String getTrigger() {
		return trigger;
	}

	public void setTrigger(String trigger) {
		this.trigger = trigger;
	}

	public String[] getPosition() {
		return position;
	}

	public void setPosition(String[] position) {
		this.position = position;
	}

	public String getFormatter() {
		return formatter;
	}

	public void setFormatter(String formatter) {
		this.formatter = formatter;
	}

	public String getIslandFormatter() {
		return islandFormatter;
	}

	public void setIslandFormatter(String islandFormatter) {
		this.islandFormatter = islandFormatter;
	}

	public Integer getShowDelay() {
		return showDelay;
	}

	public void setShowDelay(Integer showDelay) {
		this.showDelay = showDelay;
	}

	public Integer getHideDelay() {
		return hideDelay;
	}

	public void setHideDelay(Integer hideDelay) {
		this.hideDelay = hideDelay;
	}

	public Float getTransitionDuration() {
		return transitionDuration;
	}

	public void setTransitionDuration(Float transitionDuration) {
		this.transitionDuration = transitionDuration;
	}

	public Boolean getEnterable() {
		return enterable;
	}

	public void setEnterable(Boolean enterable) {
		this.enterable = enterable;
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

	public Integer getBorderRadius() {
		return borderRadius;
	}

	public void setBorderRadius(Integer borderRadius) {
		this.borderRadius = borderRadius;
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

	public AxisPointer getAxisPointer() {
		return axisPointer;
	}

	public void setAxisPointer(AxisPointer axisPointer) {
		this.axisPointer = axisPointer;
	}

	public TextStyle getTextStyle() {
		return textStyle;
	}

	public void setTextStyle(TextStyle textStyle) {
		this.textStyle = textStyle;
	}

}
