package xap.lui.core.echar;

public class Axis {
	@TypeEnum(types = { "category", "value", "time", "log" })
	@FieldMeta(desc = "坐标轴类型")
	private String type = null;
	@FieldMeta(desc = "显示策略")
	private Boolean show = null;
	@FieldMeta(desc = "一级层叠控制")
	private Integer zlevel = null;
	@FieldMeta(desc = "二级层叠控制")
	private Integer z = null;
	@TypeEnum(types = { "bottom", "top", "left", "right" })
	@FieldMeta(desc = "坐标轴类型")
	private String position = null;
	@FieldMeta(desc = "坐标轴名称")
	private String name = "";
	@TypeEnum(types = { "start", "end" })
	@FieldMeta(desc = "坐标轴名称位置")
	private String nameLocation = null;
	@FieldMeta(desc = "两端空白策略")
	private Boolean boundaryGap0 = null;
	@FieldMeta(desc = "两端空白策略")
	private Integer[] boundaryGap1 = null;
	@FieldMeta(desc = "坐标轴名称文字样式")
	private TextStyle nameTextStyle = null;
	@FieldMeta(desc = "最小值")
	private Integer mix = null;
	@FieldMeta(desc = "最大值")
	private Integer max = null;
	@FieldMeta(desc = "脱离0值比例")
	private Boolean scale = false;
	@FieldMeta(desc = "分割段数")
	private Integer splitNumber = null;
	@FieldMeta(desc = "坐标轴线")
	private AxisLine axisLine = null;
	@FieldMeta(desc = "坐标轴小标记")
	private AxisTick axisTick = null;
	@FieldMeta(desc = "坐标轴文本标签")
	private AxisLabel axisLabel = null;
	@FieldMeta(desc = "分隔线")
	private SplitLine splitLine = null;
	@FieldMeta(desc = "分隔区域")
	private SplitArea splitArea = null;
	private String[] data = null;

	public String[] getData() {
		return data;
	}

	public void setData(String[] data) {
		this.data = data;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

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

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNameLocation() {
		return nameLocation;
	}

	public void setNameLocation(String nameLocation) {
		this.nameLocation = nameLocation;
	}

	public Boolean getBoundaryGap0() {
		return boundaryGap0;
	}

	public void setBoundaryGap0(Boolean boundaryGap0) {
		this.boundaryGap0 = boundaryGap0;
	}

	public Integer[] getBoundaryGap1() {
		return boundaryGap1;
	}

	public void setBoundaryGap1(Integer[] boundaryGap1) {
		this.boundaryGap1 = boundaryGap1;
	}

	public TextStyle getNameTextStyle() {
		return nameTextStyle;
	}

	public void setNameTextStyle(TextStyle nameTextStyle) {
		this.nameTextStyle = nameTextStyle;
	}

	public Integer getMix() {
		return mix;
	}

	public void setMix(Integer mix) {
		this.mix = mix;
	}

	public Integer getMax() {
		return max;
	}

	public void setMax(Integer max) {
		this.max = max;
	}

	public Boolean getScale() {
		return scale;
	}

	public void setScale(Boolean scale) {
		this.scale = scale;
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

	public SplitArea getSplitArea() {
		return splitArea;
	}

	public void setSplitArea(SplitArea splitArea) {
		this.splitArea = splitArea;
	}

}
