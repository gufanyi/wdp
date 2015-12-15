package xap.lui.core.echar;

/**
 * 坐标轴文本标签
 * 
 * @author tianchw
 */
public class AxisLabel {
	@FieldMeta(desc = "是否显示")
	private Boolean show = true;
	@FieldMeta(desc = "标签显示挑选间隔")
	private String interval = null;
	@FieldMeta(desc = "标签旋转角度")
	private Integer rotate = null;
	@FieldMeta(desc = "坐标轴文本标签与坐标轴的间距")
	private Integer margin = null;
	@FieldMeta(desc = "坐标轴文本标签是否可点击")
	private Boolean clickable = null;
	@FieldMeta(desc = "间隔名称格式器")
	private String formatter = null;
	@FieldMeta(desc = "文本样式")
	private TextStyle textStyle = null;

	public Boolean getShow() {
		return show;
	}

	public void setShow(Boolean show) {
		this.show = show;
	}

	public String getInterval() {
		return interval;
	}

	public void setInterval(String interval) {
		this.interval = interval;
	}

	public Integer getRotate() {
		return rotate;
	}

	public void setRotate(Integer rotate) {
		this.rotate = rotate;
	}

	public Integer getMargin() {
		return margin;
	}

	public void setMargin(Integer margin) {
		this.margin = margin;
	}

	public Boolean getClickable() {
		return clickable;
	}

	public void setClickable(Boolean clickable) {
		this.clickable = clickable;
	}

	public String getFormatter() {
		return formatter;
	}

	public void setFormatter(String formatter) {
		this.formatter = formatter;
	}

	public TextStyle getTextStyle() {
		return textStyle;
	}

	public void setTextStyle(TextStyle textStyle) {
		this.textStyle = textStyle;
	}

}
