package xap.lui.core.echar;

//时间轴当前点
public class CheckPointStyle {

	@FieldMeta(desc="当前点symbol，默认随轴上的symbol ")
	private String symbol ="auto";
	@FieldMeta(desc=" 当前点symbol大小，默认随轴上symbol大小")
	private String symbolSize ="auto";
	@FieldMeta(desc="当前点symbol颜色，默认为随当前点颜色，可指定具体颜色")
	private String color="auto";
	@FieldMeta(desc="当前点symbol边线颜色")
	private String borderColor="auto";
	@FieldMeta(desc="当前点symbol边线宽度 ")
	private String borderWidth ="auto";
	private Label label=null;
	public CheckPointStyle() {
		label=new Label();
		label.setShow(false);
		label.getTextStyle().setColor("auto");
	}
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public String getSymbolSize() {
		return symbolSize;
	}
	public void setSymbolSize(String symbolSize) {
		this.symbolSize = symbolSize;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
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
	public Label getLabel() {
		return label;
	}
	public void setLabel(Label label) {
		this.label = label;
	}
	
	
	
			
}
