package xap.lui.core.plugins;

import xap.lui.core.dataset.ChartDataWithName;
import xap.lui.core.echar.AxisLabel;
import xap.lui.core.echar.AxisLine;
import xap.lui.core.echar.AxisTick;
import xap.lui.core.echar.FieldMeta;
import xap.lui.core.echar.Series;
import xap.lui.core.echar.SplitLine;

public class GaugeSeries extends Series {

	@FieldMeta(desc = "圆心坐标")
	private String[] center = null;
	@FieldMeta(desc = "半径")
	private String[] radius = null;
	@FieldMeta(desc = "开始角度")
	private Integer startAngle = null;
	@FieldMeta(desc = "结束角度")
	private Integer endAngle = null;

	@FieldMeta(desc = "最大值")
	private Integer max = null;
	@FieldMeta(desc = "最小值")
	private Integer min = null;
	@FieldMeta(desc = "分段格数")
	private Integer splitNumber = null;
	@FieldMeta(desc = "坐标轴线")
	private AxisLine axisLine = null;
	@FieldMeta(desc = "坐标轴小标记")
	private AxisTick axisTick = null;
	@FieldMeta(desc = "坐标轴文本标签")
	private AxisLabel axisLabel = null;
	@FieldMeta(desc = "分割线")
	private SplitLine splitLine = null;
	@FieldMeta(desc = "指针")
	private Pointer pointer = null;
	@FieldMeta(desc = "仪表盘详情 ")
	private Detail detail = null;
	@FieldMeta(desc = "是否启用图例")
	private Boolean legendHoverLink = null;

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

	public Integer getEndAngle() {
		return endAngle;
	}

	public void setEndAngle(Integer endAngle) {
		this.endAngle = endAngle;
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

	public Integer getSplitNumber() {
		return splitNumber;
	}

	public void setSplitNumber(Integer splitNumber) {
		this.splitNumber = splitNumber;
	}

	public AxisLine getAxisLine() {
		return axisLine;
	}

	public void setAxisLine(AxisLine axisLine) {
		this.axisLine = axisLine;
	}

	public AxisTick getAxisTick() {
		return axisTick;
	}

	public void setAxisTick(AxisTick axisTick) {
		this.axisTick = axisTick;
	}

	public AxisLabel getAxisLabel() {
		return axisLabel;
	}

	public void setAxisLabel(AxisLabel axisLabel) {
		this.axisLabel = axisLabel;
	}

	public SplitLine getSplitLine() {
		return splitLine;
	}

	public void setSplitLine(SplitLine splitLine) {
		this.splitLine = splitLine;
	}

	public Pointer getPointer() {
		return pointer;
	}

	public void setPointer(Pointer pointer) {
		this.pointer = pointer;
	}

	public Detail getDetail() {
		return detail;
	}

	public void setDetail(Detail detail) {
		this.detail = detail;
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

}