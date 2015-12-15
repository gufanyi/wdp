package xap.lui.core.echar;

public class BarSeries extends AngleSeries {
	@FieldMeta(desc = "柱间距离")
	private String barGap = "30%";
	@FieldMeta(desc = "类目间柱形距离")
	private String barCategoryGap = "20%";
	@FieldMeta(desc = "柱条最小高度")
	private int barMinHeight = 0;
	@FieldMeta(desc = "柱条宽度")
	private int barWidth = 0;
	@FieldMeta(desc = "柱条最大宽度")
	private int barMaxWidth = 0;
	@FieldMeta(desc = "是否启用图例")
	private boolean legendHoverLink = true;

	public String getBarGap() {
		return barGap;
	}

	public void setBarGap(String barGap) {
		this.barGap = barGap;
	}

	public String getBarCategoryGap() {
		return barCategoryGap;
	}

	public void setBarCategoryGap(String barCategoryGap) {
		this.barCategoryGap = barCategoryGap;
	}

	public int getBarMinHeight() {
		return barMinHeight;
	}

	public void setBarMinHeight(int barMinHeight) {
		this.barMinHeight = barMinHeight;
	}

	public int getBarWidth() {
		return barWidth;
	}

	public void setBarWidth(int barWidth) {
		this.barWidth = barWidth;
	}

	public int getBarMaxWidth() {
		return barMaxWidth;
	}

	public void setBarMaxWidth(int barMaxWidth) {
		this.barMaxWidth = barMaxWidth;
	}

	public boolean isLegendHoverLink() {
		return legendHoverLink;
	}

	public void setLegendHoverLink(boolean legendHoverLink) {
		this.legendHoverLink = legendHoverLink;
	}

}
