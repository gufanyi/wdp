package xap.lui.core.echar;

//标签视觉引导线
public class LabelLine {

	@FieldMeta(desc = "显示策略")
	private boolean show = true;
	@FieldMeta(desc = "线长")
	private int length = 40;
	@FieldMeta(desc = "线条样式")
	private LineStyle lineStyle = new LineStyle();

	public boolean isShow() {
		return show;
	}

	public void setShow(boolean show) {
		this.show = show;
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
