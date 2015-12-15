package xap.lui.core.echar;

public class TimeLine {

	@FieldMeta(desc = "显示策略")
	private Boolean show = null;
	@FieldMeta(desc = "一级层叠控制")
	private Integer zlevel = null;
	@FieldMeta(desc = "二级层叠控制")
	private Integer z = null;
	@FieldMeta(desc = "模式是时间类型")
	final private String type = "time";
	@FieldMeta(desc = "时间轴上多个option切换时是否进行merge操作")
	private Boolean notMerge = null;
	@FieldMeta(desc = "拖拽或点击改变时间轴是否实时显示")
	private Boolean realtime = null;
	@FieldMeta(desc = "时间轴左上角横坐标，数值单位px，支持百分比(字符串)")
	private Object x = Integer.valueOf("80");
	@FieldMeta(desc = "时间轴左上角纵坐标，数值单位px，支持百分比(字符串),默认无,随y2定位")
	private Object y = null;
	@FieldMeta(desc = "时间轴右下角横坐标，数值单位px，支持百分比(字符串)")
	private Object x2 = Integer.valueOf("80");
	@FieldMeta(desc = "时间轴右下角纵坐标，数值单位px，支持百分比(字符串)")
	private Object y2 = Integer.valueOf("0");
	@FieldMeta(desc = "时间轴宽度，默认为总宽度 - x - x2，数值单位px，指定width后将忽略x2,支持百分比(字符串)")
	private Object width = null;
	@FieldMeta(desc = "时间轴高度，数值单位px，支持百分比(字符串)")
	private Object height = Integer.valueOf("50");
	@FieldMeta(desc = "背景颜色，默认透明")
	private String backgroundColor = "rgba(0,0,0,0)";
	@FieldMeta(desc = "边框线宽")
	private int borderWidth = 0;
	@FieldMeta(desc = "边框颜色")
	private String borderColor = "#ccc";
	@FieldMeta(desc = "内边距,单位px，默认各方向内边距为5")
	private int[] padding = { 5 };
	@TypeEnum(types = { "left", "right", "none" })
	@FieldMeta(desc = "播放控制器位置")
	private String controlPosition = "left";
	@FieldMeta(desc = "是否自动播放")
	private boolean autoPlay = false;
	@FieldMeta(desc = "是否循环播放")
	private boolean loop = true;
	@FieldMeta(desc = "播放时间间隔，单位ms")
	private int playInterval = 2000;
	@FieldMeta(desc = "时间轴轴线样式，lineStyle控制线条样式")
	private LineStyle lineStyle = new LineStyle();
	@FieldMeta(desc = "时间轴标签文本")
	private Label label = null;
	@FieldMeta(desc = "时间轴当前点")
	private CheckPointStyle checkpointStyle = null;
	@FieldMeta(desc = "时间轴控制器样式")
	private ControlStyle controlStyle = null;
	@FieldMeta(desc = "轴点symbol")
	private String symbol = "emptyDiamond";
	@FieldMeta(desc = "轴点symbol")
	private int symbolSize = 4;
	@FieldMeta(desc = "当前索引位置，对应options数组")
	private int currentIndex = 0;// 当前索引位置，对应options数组，用于指定显示特定系列
	@FieldMeta(desc = "时间轴列表")
	private String[] data = null;

	public TimeLine() {
		label = new Label();
		lineStyle = new LineStyle();
		lineStyle.setColor("#666");
		lineStyle.setWidth(1);
	}

	public boolean isShow() {
		return show;
	}

	public void setShow(boolean show) {
		this.show = show;
	}

	public int getZlevel() {
		return zlevel;
	}

	public void setZlevel(int zlevel) {
		this.zlevel = zlevel;
	}

	public int getZ() {
		return z;
	}

	public void setZ(int z) {
		this.z = z;
	}

	public boolean isNotMerge() {
		return notMerge;
	}

	public void setNotMerge(boolean notMerge) {
		this.notMerge = notMerge;
	}

	public boolean isRealtime() {
		return realtime;
	}

	public void setRealtime(boolean realtime) {
		this.realtime = realtime;
	}

	public Object getX() {
		return x;
	}

	public void setX(Object x) {
		this.x = x;
	}

	public Object getY() {
		return y;
	}

	public void setY(Object y) {
		this.y = y;
	}

	public Object getX2() {
		return x2;
	}

	public void setX2(Object x2) {
		this.x2 = x2;
	}

	public Object getY2() {
		return y2;
	}

	public void setY2(Object y2) {
		this.y2 = y2;
	}

	public Object getWidth() {
		return width;
	}

	public void setWidth(Object width) {
		this.width = width;
	}

	public Object getHeight() {
		return height;
	}

	public void setHeight(Object height) {
		this.height = height;
	}

	public String getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public int getBorderWidth() {
		return borderWidth;
	}

	public void setBorderWidth(int borderWidth) {
		this.borderWidth = borderWidth;
	}

	public String getBorderColor() {
		return borderColor;
	}

	public void setBorderColor(String borderColor) {
		this.borderColor = borderColor;
	}

	public int[] getPadding() {
		return padding;
	}

	public void setPadding(int[] padding) {
		this.padding = padding;
	}

	public String getControlPosition() {
		return controlPosition;
	}

	public void setControlPosition(String controlPosition) {
		this.controlPosition = controlPosition;
	}

	public boolean isAutoPlay() {
		return autoPlay;
	}

	public void setAutoPlay(boolean autoPlay) {
		this.autoPlay = autoPlay;
	}

	public boolean isLoop() {
		return loop;
	}

	public void setLoop(boolean loop) {
		this.loop = loop;
	}

	public int getPlayInterval() {
		return playInterval;
	}

	public void setPlayInterval(int playInterval) {
		this.playInterval = playInterval;
	}

	public LineStyle getLineStyle() {
		return lineStyle;
	}

	public void setLineStyle(LineStyle lineStyle) {
		this.lineStyle = lineStyle;
	}

	public Label getLabel() {
		return label;
	}

	public void setLabel(Label label) {
		this.label = label;
	}

	public CheckPointStyle getCheckpointStyle() {
		return checkpointStyle;
	}

	public void setCheckpointStyle(CheckPointStyle checkpointStyle) {
		this.checkpointStyle = checkpointStyle;
	}

	public ControlStyle getControlStyle() {
		return controlStyle;
	}

	public void setControlStyle(ControlStyle controlStyle) {
		this.controlStyle = controlStyle;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public int getSymbolSize() {
		return symbolSize;
	}

	public void setSymbolSize(int symbolSize) {
		this.symbolSize = symbolSize;
	}

	public int getCurrentIndex() {
		return currentIndex;
	}

	public void setCurrentIndex(int currentIndex) {
		this.currentIndex = currentIndex;
	}

	public String[] getData() {
		return data;
	}

	public void setData(String[] data) {
		this.data = data;
	}

	public String getType() {
		return type;
	}

}
