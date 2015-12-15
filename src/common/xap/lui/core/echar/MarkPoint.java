package xap.lui.core.echar;

import xap.lui.core.dataset.MarkPointData;

//系列中的数据标注内容
public class MarkPoint {
	@FieldMeta(desc = "数据图形是否可点击")
	private Boolean clickable = null;
	@FieldMeta(desc = "标注类型")
	private String symbol = null;
	@FieldMeta(desc = "标注大小")
	private Integer[] symbolSize = { 10 };
	@FieldMeta(desc = "标注图形旋转角度")
	private Integer symbolRotate = 0;
	@FieldMeta(desc = "是否启动大规模标注模式")
	private Boolean large = false;
	@FieldMeta(desc = "标注图形炫光特效")
	private Effect effect = null;
	@FieldMeta(desc = "标注图形样式属性")
	private ItemStyle itemStyle = null;
	@FieldMeta(desc = "标注图形数据")
	private MarkPointData[] data = null;

	public Boolean getClickable() {
		return clickable;
	}

	public void setClickable(Boolean clickable) {
		this.clickable = clickable;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public Integer[] getSymbolSize() {
		return symbolSize;
	}

	public void setSymbolSize(Integer[] symbolSize) {
		this.symbolSize = symbolSize;
	}

	public Integer getSymbolRotate() {
		return symbolRotate;
	}

	public void setSymbolRotate(Integer symbolRotate) {
		this.symbolRotate = symbolRotate;
	}

	public Boolean getLarge() {
		return large;
	}

	public void setLarge(Boolean large) {
		this.large = large;
	}

	public Effect getEffect() {
		return effect;
	}

	public void setEffect(Effect effect) {
		this.effect = effect;
	}

	public ItemStyle getItemStyle() {
		return itemStyle;
	}

	public void setItemStyle(ItemStyle itemStyle) {
		this.itemStyle = itemStyle;
	}

	public MarkPointData[] getData() {
		return data;
	}

	public void setData(MarkPointData[] data) {
		this.data = data;
	}

}
