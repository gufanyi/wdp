package xap.lui.core.echar;

public class ItemStyle {
	@FieldMeta(desc = "主色")
	private String color = null;
	@FieldMeta(desc = "线条样式")
	private LineStyle lineStyle = null;
	@FieldMeta(desc = "区域样式")
	private AreaStyle areaStyle =null;
	@FieldMeta(desc = "弦样式")
	private ChordStyle chordStyle = null;
	@FieldMeta(desc = "节点样式")
	private NodeStyle nodeStyle =null;
	@FieldMeta(desc = "边样式")
	private LinkStyle linkStyle =null;
	@FieldMeta(desc = "边框颜色")
	private String borderColor = null;
	@FieldMeta(desc = "边框线宽,单位px")
	private String borderWidth = null;
	@FieldMeta(desc = "边框颜色")
	private String barBorderColor = null;
	@FieldMeta(desc = "柱形边框圆角")
	private Integer[] barBorderRadius = { 0 };
	@FieldMeta(desc="柱形边框线宽")
	private Integer barBorderWidth=null;
	@FieldMeta(desc="标签")
	private Label label=null;
	@FieldMeta(desc="标签视觉引导线")
	private LabelLine labelLine=null;
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public LineStyle getLineStyle() {
		return lineStyle;
	}
	public void setLineStyle(LineStyle lineStyle) {
		this.lineStyle = lineStyle;
	}
	public AreaStyle getAreaStyle() {
		return areaStyle;
	}
	public void setAreaStyle(AreaStyle areaStyle) {
		this.areaStyle = areaStyle;
	}
	public ChordStyle getChordStyle() {
		return chordStyle;
	}
	public void setChordStyle(ChordStyle chordStyle) {
		this.chordStyle = chordStyle;
	}
	public NodeStyle getNodeStyle() {
		return nodeStyle;
	}
	public void setNodeStyle(NodeStyle nodeStyle) {
		this.nodeStyle = nodeStyle;
	}
	public LinkStyle getLinkStyle() {
		return linkStyle;
	}
	public void setLinkStyle(LinkStyle linkStyle) {
		this.linkStyle = linkStyle;
	}
	public String getBorderColor() {
		return borderColor;
	}
	public void setBorderColor(String borderColor) {
		this.borderColor = borderColor;
	}
	public String getBorderWidth() {
		return borderWidth;
	}
	public void setBorderWidth(String borderWidth) {
		this.borderWidth = borderWidth;
	}
	public String getBarBorderColor() {
		return barBorderColor;
	}
	public void setBarBorderColor(String barBorderColor) {
		this.barBorderColor = barBorderColor;
	}
	public Integer[] getBarBorderRadius() {
		return barBorderRadius;
	}
	public void setBarBorderRadius(Integer[] barBorderRadius) {
		this.barBorderRadius = barBorderRadius;
	}
	public Integer getBarBorderWidth() {
		return barBorderWidth;
	}
	public void setBarBorderWidth(Integer barBorderWidth) {
		this.barBorderWidth = barBorderWidth;
	}
	public Label getLabel() {
		return label;
	}
	public void setLabel(Label label) {
		this.label = label;
	}
	public LabelLine getLabelLine() {
		return labelLine;
	}
	public void setLabelLine(LabelLine labelLine) {
		this.labelLine = labelLine;
	}
	
	

}
