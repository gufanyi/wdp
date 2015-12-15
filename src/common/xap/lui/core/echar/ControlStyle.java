package xap.lui.core.echar;

//时间轴控制器样式
public class ControlStyle {

	@FieldMeta(desc = "按钮大小")
	private int itemSize = 15;
	@FieldMeta(desc = "按钮间隔")
	private int itemGap = 5;
	@FieldMeta(desc = "正常颜色")
	private Normal normal = new Normal();
	@FieldMeta(desc = "高亮颜色")
	private Emphasis emphasis = new Emphasis();
	@FieldMeta(desc = "轴点symbol")
	private String symbol = "emptyDiamond";
	@FieldMeta(desc = "轴点symbol")
	private int symbolSize = 4;
	@FieldMeta(desc = "当前索引位置")
	private int currentIndex = 0;

	class Normal {
		private String color = "#333";

		public String getColor() {
			return color;
		}

		public void setColor(String color) {
			this.color = color;
		}

	}

	class Emphasis {
		private String color = "#1e90ff";

		public String getColor() {
			return color;
		}

		public void setColor(String color) {
			this.color = color;
		}

	}

	public int getItemSize() {
		return itemSize;
	}

	public void setItemSize(int itemSize) {
		this.itemSize = itemSize;
	}

	public int getItemGap() {
		return itemGap;
	}

	public void setItemGap(int itemGap) {
		this.itemGap = itemGap;
	}

	public Normal getNormal() {
		return normal;
	}

	public void setNormal(Normal normal) {
		this.normal = normal;
	}

	public Emphasis getEmphasis() {
		return emphasis;
	}

	public void setEmphasis(Emphasis emphasis) {
		this.emphasis = emphasis;
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

}
