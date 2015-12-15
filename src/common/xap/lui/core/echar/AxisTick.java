package xap.lui.core.echar;

public class AxisTick {

	@FieldMeta(desc = "是否显示")
	private boolean show = true;
	@FieldMeta(desc = "小标记显示挑选间隔")
	private String interval = "auto";
	@FieldMeta(desc = "小标记是否显示为间隔")
	private boolean onGap = false;
	@FieldMeta(desc = "小标记是否显示为在grid内部")
	private boolean inside = false;
	@FieldMeta(desc = "控制线长")
	private int length = 5;
	@FieldMeta(desc = "控制线条样式")
	private LineStyle lineStyle = null;

	public AxisTick() {
		lineStyle = new LineStyle();
		lineStyle.setColor("#333");
		lineStyle.setType("solid");
		lineStyle.setWidth(1);
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

	public boolean isOnGap() {
		return onGap;
	}

	public void setOnGap(boolean onGap) {
		this.onGap = onGap;
	}

	public boolean isInside() {
		return inside;
	}

	public void setInside(boolean inside) {
		this.inside = inside;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public LineStyle getLineStyle() {
		return lineStyle;
	}

	public void setLineStyle(LineStyle lineStyle) {
		this.lineStyle = lineStyle;
	}

}
