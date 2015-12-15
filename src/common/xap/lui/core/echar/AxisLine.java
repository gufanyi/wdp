package xap.lui.core.echar;

/**
 * 坐标轴文本标签
 * 
 * @author tianchw
 *
 */
public class AxisLine {
	@FieldMeta(desc = "是否显示")
	private boolean show = true;
	@FieldMeta(desc = "定位到垂直方向的0值坐标")
	private boolean onZero = true;
	@FieldMeta(desc = "控制线条样式")
	private LineStyle lineStyle = null;

	public boolean isShow() {
		return show;
	}

	public void setShow(boolean show) {
		this.show = show;
	}

	public boolean isOnZero() {
		return onZero;
	}

	public void setOnZero(boolean onZero) {
		this.onZero = onZero;
	}

	public LineStyle getLineStyle() {
		return lineStyle;
	}

	public void setLineStyle(LineStyle lineStyle) {
		this.lineStyle = lineStyle;
	}

}
