package xap.lui.core.echar;

//数据区域缩放
public class DataZoom {
	@FieldMeta(desc = "一级层叠控制")
	private Integer zlevel = null;
	@FieldMeta(desc = "二级层叠控制")
	private Integer z = null;
	@FieldMeta(desc = "是否显示")
	private Boolean show = null;
	@TypeEnum(types = { "horizontal", "vertical" })
	@FieldMeta(desc = "水平安放位置")
	private String orient = null;
	@FieldMeta(desc = "水平安放位置")
	private Integer x = null;
	@FieldMeta(desc = "垂直安放位置")
	private Integer y = null;
	@FieldMeta(desc = "指定宽度")
	private Integer width = null;
	@FieldMeta(desc = "指定高度")
	private Integer height = null;
	@FieldMeta(desc = "背景颜色")
	private String backgroundColor = null;
	@FieldMeta(desc = "数据缩略背景颜色")
	private String dataBackgroundColor = null;
	@FieldMeta(desc = "选择区域填充颜色")
	private String fillerColor = null;
	@FieldMeta(desc = "控制手柄颜色")
	private String handleColor = null;
	@FieldMeta(desc = "控制手柄大小")
	private Integer handleSize = null;
	@FieldMeta(desc = "横向类目坐标轴Index")
	private Integer xAxisIndex = null;
	@FieldMeta(desc = "纵向类目坐标轴Index")
	private Integer yAxisIndex = null;
	@FieldMeta(desc = "起始比例")
	private Integer start = null;
	@FieldMeta(desc = "结束比例")
	private Integer end = null;
	@FieldMeta(desc = "否显示定位详情")
	private Boolean showDetail = null;
	@FieldMeta(desc = "是否实时显示")
	private Boolean realtime = null;
	@FieldMeta(desc = "数据缩放锁")
	private Boolean zoomLock = null;

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

	public Boolean getShow() {
		return show;
	}

	public void setShow(Boolean show) {
		this.show = show;
	}

	public String getOrient() {
		return orient;
	}

	public void setOrient(String orient) {
		this.orient = orient;
	}

	public Integer getX() {
		return x;
	}

	public void setX(Integer x) {
		this.x = x;
	}

	public Integer getY() {
		return y;
	}

	public void setY(Integer y) {
		this.y = y;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public String getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public String getDataBackgroundColor() {
		return dataBackgroundColor;
	}

	public void setDataBackgroundColor(String dataBackgroundColor) {
		this.dataBackgroundColor = dataBackgroundColor;
	}

	public String getFillerColor() {
		return fillerColor;
	}

	public void setFillerColor(String fillerColor) {
		this.fillerColor = fillerColor;
	}

	public String getHandleColor() {
		return handleColor;
	}

	public void setHandleColor(String handleColor) {
		this.handleColor = handleColor;
	}

	public Integer getHandleSize() {
		return handleSize;
	}

	public void setHandleSize(Integer handleSize) {
		this.handleSize = handleSize;
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

	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public Integer getEnd() {
		return end;
	}

	public void setEnd(Integer end) {
		this.end = end;
	}

	public Boolean getShowDetail() {
		return showDetail;
	}

	public void setShowDetail(Boolean showDetail) {
		this.showDetail = showDetail;
	}

	public Boolean getRealtime() {
		return realtime;
	}

	public void setRealtime(Boolean realtime) {
		this.realtime = realtime;
	}

	public Boolean getZoomLock() {
		return zoomLock;
	}

	public void setZoomLock(Boolean zoomLock) {
		this.zoomLock = zoomLock;
	}

}
