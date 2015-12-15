package xap.lui.core.echar;

public class Option {

	@FieldMeta(desc = "全图默认背景")
	private String backgroundColor = null;
	@FieldMeta(desc = "数值系列")
	private String[] color = null;
	@FieldMeta(desc = "支持渲染为图片")
	private Boolean renderAsImage = null;
	@FieldMeta(desc = "是否启用拖拽重计算特性")
	private Boolean calculable = null;
	@FieldMeta(desc = "是否开启动画")
	private Boolean animation = null;
	@FieldMeta(desc = "时间轴")
	private TimeLine timeline = null;
	@FieldMeta(desc = "标题")
	private Title title =null;
	@FieldMeta(desc = "提示框")
	private Tooltip tooltip = null;
	@FieldMeta(desc = "图例")
	private Legend legend = null;
	@FieldMeta(desc = "值域选择")
	private DataRange dataRange = null;
	@FieldMeta(desc = "数据区域缩放")
	private DataZoom dataZoom = null;
	@FieldMeta(desc = "漫游缩放组件")
	private RoamController roamController = null;
	@FieldMeta(desc = "绘图网格")
	private Grid grid = null;
	@FieldMeta(desc = "横轴数组")
	private Axis[] xAxis = null;
	@FieldMeta(desc = "纵轴数组")
	private Axis[] yAxis = null;
	@FieldMeta(desc = "序列")
	private Series[] series = null;
	public String getBackgroundColor() {
		return backgroundColor;
	}
	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}
	public String[] getColor() {
		return color;
	}
	public void setColor(String[] color) {
		this.color = color;
	}
	public Boolean getRenderAsImage() {
		return renderAsImage;
	}
	public void setRenderAsImage(Boolean renderAsImage) {
		this.renderAsImage = renderAsImage;
	}
	public Boolean getCalculable() {
		return calculable;
	}
	public void setCalculable(Boolean calculable) {
		this.calculable = calculable;
	}
	public Boolean getAnimation() {
		return animation;
	}
	public void setAnimation(Boolean animation) {
		this.animation = animation;
	}
	public TimeLine getTimeline() {
		return timeline;
	}
	public void setTimeline(TimeLine timeline) {
		this.timeline = timeline;
	}
	public Title getTitle() {
		return title;
	}
	public void setTitle(Title title) {
		this.title = title;
	}
	public Tooltip getTooltip() {
		return tooltip;
	}
	public void setTooltip(Tooltip tooltip) {
		this.tooltip = tooltip;
	}
	public Legend getLegend() {
		return legend;
	}
	public void setLegend(Legend legend) {
		this.legend = legend;
	}
	public DataRange getDataRange() {
		return dataRange;
	}
	public void setDataRange(DataRange dataRange) {
		this.dataRange = dataRange;
	}
	public DataZoom getDataZoom() {
		return dataZoom;
	}
	public void setDataZoom(DataZoom dataZoom) {
		this.dataZoom = dataZoom;
	}
	public RoamController getRoamController() {
		return roamController;
	}
	public void setRoamController(RoamController roamController) {
		this.roamController = roamController;
	}
	public Grid getGrid() {
		return grid;
	}
	public void setGrid(Grid grid) {
		this.grid = grid;
	}
	public Axis[] getXAxis() {
		return xAxis;
	}
	public void setXAxis(Axis[] xAxis) {
		this.xAxis = xAxis;
	}
	public Axis[] getYAxis() {
		return yAxis;
	}
	public void setYAxis(Axis[] yAxis) {
		this.yAxis = yAxis;
	}
	public Series[] getSeries() {
		return series;
	}
	public void setSeries(Series[] series) {
		this.series = series;
	}

	
	
	

	

}
