package xap.lui.core.echar;

public class Series {
	@FieldMeta(desc = "一级层叠控制")
	private Integer zlevel = null;
	@FieldMeta(desc = "二级层叠控制")
	private Integer z = null;
	@TypeEnum(types = { "line", "bar", "scatter", "k", "pie", "radar", "chord", "force", "map" })
	@FieldMeta(desc = "图表类型")
	private String type = null;
	@FieldMeta(desc = "系列名称")
	private String name = null;
	@FieldMeta(desc = "提示框样式")
	private Tooltip tooltip = null;
	@FieldMeta(desc = "数据图形是否可点击")
	private Boolean clickable = null;
	@FieldMeta(desc = "图形样式")
	private ItemStyle itemStyle = null;
	@FieldMeta(desc = "标注")
	private MarkPoint markPoint = null;
	@FieldMeta(desc = "标线")
	private MarkLine markLine = null;

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Tooltip getTooltip() {
		return tooltip;
	}

	public void setTooltip(Tooltip tooltip) {
		this.tooltip = tooltip;
	}

	public Boolean getClickable() {
		return clickable;
	}

	public void setClickable(Boolean clickable) {
		this.clickable = clickable;
	}

	public ItemStyle getItemStyle() {
		return itemStyle;
	}

	public void setItemStyle(ItemStyle itemStyle) {
		this.itemStyle = itemStyle;
	}

	public MarkPoint getMarkPoint() {
		return markPoint;
	}

	public void setMarkPoint(MarkPoint markPoint) {
		this.markPoint = markPoint;
	}

	public MarkLine getMarkLine() {
		return markLine;
	}

	public void setMarkLine(MarkLine markLine) {
		this.markLine = markLine;
	}

}
