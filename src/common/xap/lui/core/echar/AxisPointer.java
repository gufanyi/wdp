package xap.lui.core.echar;

public class AxisPointer {
	@FieldMeta(desc = "坐标轴指示器")
	private String type = "line";
	@FieldMeta(desc = "直线指示器")
	private LineStyle lineStyle = null;
	@FieldMeta(desc = "十字准星指示器")
	private LineStyle crossStyle =null;
	@FieldMeta(desc = "阴影指示器")
	private AreaStyle shadowStyle = null;

	public AxisPointer() {
//		shadowStyle = new AreaStyle();
//		String[] color = { "rgba(150,150,150,0.3)" };
//		shadowStyle.setColor(color);
//		lineStyle.setColor("#48b");
//		lineStyle.setWidth(2);
//		lineStyle.setType("solid");
//
//		crossStyle.setColor("#1e90ff");
//		crossStyle.setType("dashed");
//		crossStyle.setWidth(1);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public LineStyle getLineStyle() {
		return lineStyle;
	}

	public void setLineStyle(LineStyle lineStyle) {
		this.lineStyle = lineStyle;
	}

	public LineStyle getCrossStyle() {
		return crossStyle;
	}

	public void setCrossStyle(LineStyle crossStyle) {
		this.crossStyle = crossStyle;
	}

	public AreaStyle getShadowStyle() {
		return shadowStyle;
	}

	public void setShadowStyle(AreaStyle shadowStyle) {
		this.shadowStyle = shadowStyle;
	}
	
	

}
