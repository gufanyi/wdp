package xap.lui.core.echar;

import xap.lui.core.dataset.ChartDataWithName;

public class PieSeries extends Series {

	@FieldMeta(desc = "圆心坐标")
	private String[] center = null;
	@FieldMeta(desc = "半径")
	private String[] radius = null;
	@FieldMeta(desc = "开始角度")
	private Integer startAngle = null;
	@FieldMeta(desc = "最小角度")
	private Integer minAngle = null;
	@FieldMeta(desc = "显示是否顺时针")
	private Boolean clockWise = null;
	@TypeEnum(types = { "radius", "area" })
	@FieldMeta(desc = "南丁格尔玫瑰图模式")
	private String roseType = null;
	@FieldMeta(desc = "选中是扇区偏移量")
	private Boolean selectedOffset = null;
	@FieldMeta(desc = "选中模式")
	private Boolean selectedMode = null;
	@FieldMeta(desc = "是否启用图例")
	private Boolean legendHoverLink = null;
	@FieldMeta(desc = "宽度")
	private String width = null;
	@FieldMeta(desc = "最大值")
	private Integer max = null;
	@FieldMeta(desc = "最小值")
	private Integer min = null;
	@FieldMeta(desc = "横坐标")
	private String x = null;
	@FieldMeta(desc = "纵坐标")
	private String y = null;
	
	private String sort=null;

	private ChartDataWithName[] data = null;

	public String[] getCenter() {
		return center;
	}

	public void setCenter(String[] center) {
		this.center = center;
	}

	public String[] getRadius() {
		return radius;
	}

	public void setRadius(String[] radius) {
		this.radius = radius;
	}

	public Integer getStartAngle() {
		return startAngle;
	}

	public void setStartAngle(Integer startAngle) {
		this.startAngle = startAngle;
	}

	public Integer getMinAngle() {
		return minAngle;
	}

	public void setMinAngle(Integer minAngle) {
		this.minAngle = minAngle;
	}

	public Boolean getClockWise() {
		return clockWise;
	}

	public void setClockWise(Boolean clockWise) {
		this.clockWise = clockWise;
	}

	public String getRoseType() {
		return roseType;
	}

	public void setRoseType(String roseType) {
		this.roseType = roseType;
	}

	public Boolean getSelectedOffset() {
		return selectedOffset;
	}

	public void setSelectedOffset(Boolean selectedOffset) {
		this.selectedOffset = selectedOffset;
	}

	public Boolean getSelectedMode() {
		return selectedMode;
	}

	public void setSelectedMode(Boolean selectedMode) {
		this.selectedMode = selectedMode;
	}

	public Boolean getLegendHoverLink() {
		return legendHoverLink;
	}

	public void setLegendHoverLink(Boolean legendHoverLink) {
		this.legendHoverLink = legendHoverLink;
	}

	public ChartDataWithName[] getData() {
		return data;
	}

	public void setData(ChartDataWithName[] data) {
		this.data = data;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public Integer getMax() {
		return max;
	}

	public void setMax(Integer max) {
		this.max = max;
	}

	public Integer getMin() {
		return min;
	}

	public void setMin(Integer min) {
		this.min = min;
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

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}
	
	
	
	

}