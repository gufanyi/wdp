package xap.lui.core.echar;

import xap.lui.core.dataset.MarkLineData;

public class MarkLine {
	@FieldMeta(desc = "数据图形是否可点击")
	private Boolean clickable = null;
	@FieldMeta(desc = "标线起始和结束的symbol介绍类型")
	private String[] symbol =null;
	@FieldMeta(desc = "标线起始和结束的symbol大小")
	private Integer[] symbolSize = null;
	@FieldMeta(desc = "标线起始和结束的symbol旋转控制")
	private Integer[] symbolRotate = null;
	@FieldMeta(desc = "是否启用大规模标线模式")
	private Boolean large = null;
	@FieldMeta(desc = "平滑曲线显示")
	private Boolean smooth = null;
	@FieldMeta(desc = "平滑曲线弧度")
	private Float smoothness = null;
	@FieldMeta(desc = "小数精度")
	private Integer precision = null;
	@FieldMeta(desc = "边捆绑")
	private Bundling bundling = null;
	@FieldMeta(desc = "标线图形炫光特效")
	private Effect effect = null;
	@FieldMeta(desc = "标线图形样式属性")
	private ItemStyle itemStyle = null;
	@FieldMeta(desc = "标线图形数据")
	private MarkLineData[] data = null;

	public Boolean getClickable() {
		return clickable;
	}

	public void setClickable(Boolean clickable) {
		this.clickable = clickable;
	}

	public String[] getSymbol() {
		return symbol;
	}

	public void setSymbol(String[] symbol) {
		this.symbol = symbol;
	}

	public Integer[] getSymbolSize() {
		return symbolSize;
	}

	public void setSymbolSize(Integer[] symbolSize) {
		this.symbolSize = symbolSize;
	}

	public Integer[] getSymbolRotate() {
		return symbolRotate;
	}

	public void setSymbolRotate(Integer[] symbolRotate) {
		this.symbolRotate = symbolRotate;
	}

	public Boolean getLarge() {
		return large;
	}

	public void setLarge(Boolean large) {
		this.large = large;
	}

	public Boolean getSmooth() {
		return smooth;
	}

	public void setSmooth(Boolean smooth) {
		this.smooth = smooth;
	}

	public Float getSmoothness() {
		return smoothness;
	}

	public void setSmoothness(Float smoothness) {
		this.smoothness = smoothness;
	}

	public Integer getPrecision() {
		return precision;
	}

	public void setPrecision(Integer precision) {
		this.precision = precision;
	}

	public Bundling getBundling() {
		return bundling;
	}

	public void setBundling(Bundling bundling) {
		this.bundling = bundling;
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

	public MarkLineData[] getData() {
		return data;
	}

	public void setData(MarkLineData[] data) {
		this.data = data;
	}

}
