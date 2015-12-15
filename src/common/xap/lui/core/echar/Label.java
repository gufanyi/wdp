package xap.lui.core.echar;

//线条（线段）样式
public class Label {
	@FieldMeta(desc = " 是否显示")
	private boolean show = true;
	@FieldMeta(desc = "挑选间隔,默认为'auto'")
	private String interval = "auto";
	@FieldMeta(desc = "旋转角度,默认为0,不旋转,正值为逆时针,负值为顺时针,可选为:-90 ~ 90")
	private int rotate = 0;
	@FieldMeta(desc = "间隔名称格式器")
	private String formatter = null;
	@FieldMeta(desc = "文字样式")
	private TextStyle textStyle = null;

	public Label() {
		textStyle = new TextStyle("#333");
	}

	public boolean isShow() {
		return show;
	}

	public void setShow(boolean show) {
		this.show = show;
	}

	public String getInterval() {
		return interval;
	}

	public void setInterval(String interval) {
		this.interval = interval;
	}

	public int getRotate() {
		return rotate;
	}

	public void setRotate(int rotate) {
		this.rotate = rotate;
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
