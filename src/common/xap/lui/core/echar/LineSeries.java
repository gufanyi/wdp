package xap.lui.core.echar;

public class LineSeries extends AngleSeries {
	@FieldMeta(desc = "组合名称")
	private String stack = null;
	@FieldMeta(desc = "坐标轴数组的索引")
	private Integer xAxisIndex = null;
	@FieldMeta(desc = "坐标轴数组的索引")
	private Integer yAxisIndex = null;
	@TypeEnum(types = { "circle", "rectangle", "triangle", "diamond", "emptyCircle", "emptyRectangle", "emptyTriangle", "emptyDiamond" })
	@FieldMeta(desc = "标志图形类型")
	private String symbol = null;
	@FieldMeta(desc = "标志图形大小")
	private Integer symbolSize = null;
	@FieldMeta(desc = "标志图形旋转角度")
	private Integer symbolRotate = null;
	@FieldMeta(desc = "标志图形默认只有主轴显示")
	private Boolean showAllSymbol = null;
	@FieldMeta(desc = "平滑曲线")
	private Boolean smooth = null;
	@TypeEnum(types = { "nearest", "min", "max", "average" })
	@FieldMeta(desc = "筛选函数")
	private String dataFilter = null;
	@FieldMeta(desc = "是否启用图例")
	private Boolean legendHoverLink = null;
	
	private String[] data=null;
	

	public String getStack() {
		return stack;
	}

	public void setStack(String stack) {
		this.stack = stack;
	}

	public Integer getxAxisIndex() {
		return xAxisIndex;
	}

	public void setxAxisIndex(Integer xAxisIndex) {
		this.xAxisIndex = xAxisIndex;
	}

	public Integer getyAxisIndex() {
		return yAxisIndex;
	}

	public void setyAxisIndex(Integer yAxisIndex) {
		this.yAxisIndex = yAxisIndex;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public Integer getSymbolSize() {
		return symbolSize;
	}

	public void setSymbolSize(Integer symbolSize) {
		this.symbolSize = symbolSize;
	}

	public Integer getSymbolRotate() {
		return symbolRotate;
	}

	public void setSymbolRotate(Integer symbolRotate) {
		this.symbolRotate = symbolRotate;
	}

	public Boolean getShowAllSymbol() {
		return showAllSymbol;
	}

	public void setShowAllSymbol(Boolean showAllSymbol) {
		this.showAllSymbol = showAllSymbol;
	}

	public Boolean getSmooth() {
		return smooth;
	}

	public void setSmooth(Boolean smooth) {
		this.smooth = smooth;
	}

	public String getDataFilter() {
		return dataFilter;
	}

	public void setDataFilter(String dataFilter) {
		this.dataFilter = dataFilter;
	}

	public Boolean getLegendHoverLink() {
		return legendHoverLink;
	}

	public void setLegendHoverLink(Boolean legendHoverLink) {
		this.legendHoverLink = legendHoverLink;
	}

	public String[] getData() {
		return data;
	}

	public void setData(String[] data) {
		this.data = data;
	}
	
	

}
