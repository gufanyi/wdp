package xap.lui.core.echar;

public class ScatterSeries   extends AngleSeries{
	@FieldMeta(desc="启动大规模散点图")
	private boolean large=false;
	@FieldMeta(desc="大规模散点图自动切换阀值")
	private int largeThreshold=2000;
	@FieldMeta(desc="是否启用图例")
	private boolean legendHoverLink=true;
	@FieldMeta(desc="标志图形旋转角度")
	private int[] symbolRotate=null;
	@FieldMeta(desc="坐标轴数组的索引")
	private int xAxisIndex=0;
	@FieldMeta(desc="坐标轴数组的索引")
	private int yAxisIndex=0;
	@TypeEnum(types = { "circle", "rectangle", "triangle", "diamond", "emptyCircle", "emptyRectangle", "emptyTriangle", "emptyDiamond" })
	@FieldMeta(desc = "标志图形类型")
	private String symbol=null;

}
