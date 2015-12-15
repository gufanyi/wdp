package xap.lui.core.echar;

public class SplitLine {

	@FieldMeta(desc = "是否显示")
	private boolean show = true;
	@FieldMeta(desc = "分隔线是否显示为间隔")
	private boolean onGap = false;
	@FieldMeta(desc = "控制线条样式")
	private LineStyle lineStyle = null;

	public boolean isShow() {
		return show;
	}

	public void setShow(boolean show) {
		this.show = show;
	}

	public boolean isOnGap() {
		return onGap;
	}

	public void setOnGap(boolean onGap) {
		this.onGap = onGap;
	}

	public LineStyle getLineStyle() {
		return lineStyle;
	}

	public void setLineStyle(LineStyle lineStyle) {
		this.lineStyle = lineStyle;
	}

}
