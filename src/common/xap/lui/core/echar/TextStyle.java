package xap.lui.core.echar;

//文字样式
public class TextStyle {
	@FieldMeta(desc = "颜色")
	private String color = null;
	@FieldMeta(desc = "修饰，仅对tooltip.textStyle生效")
	private String decoration = "none";
	@TypeEnum(types = { "left", "right", "center" })
	@FieldMeta(desc = "水平对齐方式")
	private String align = null;
	@TypeEnum(types = { "top", "bottom", "middle" })
	@FieldMeta(desc = "垂直对齐方式")
	private String baseline = null;
	@TypeEnum(types = { "Arial", "Verdana", "sans-serif" })
	@FieldMeta(desc = "字体系列")
	private String fontFamily = "Arial";
	@FieldMeta(desc = "字号 ,单位px")
	private int fontSize = 12;
	@TypeEnum(types = { "normal", "italic", "oblique" })
	@FieldMeta(desc = "样式")
	private String fontstyle = "noraml";
	@TypeEnum(types = { "normal", "bold", "bolder", "lighter" })
	@FieldMeta(desc = "粗细")
	private String fontweight = "noraml";

	public TextStyle(String color) {
		this.color = color;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getDecoration() {
		return decoration;
	}

	public void setDecoration(String decoration) {
		this.decoration = decoration;
	}

	public String getAlign() {
		return align;
	}

	public void setAlign(String align) {
		this.align = align;
	}

	public String getBaseline() {
		return baseline;
	}

	public void setBaseline(String baseline) {
		this.baseline = baseline;
	}

	public String getFontFamily() {
		return fontFamily;
	}

	public void setFontFamily(String fontFamily) {
		this.fontFamily = fontFamily;
	}

	public int getFontSize() {
		return fontSize;
	}

	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}

	public String getFontstyle() {
		return fontstyle;
	}

	public void setFontstyle(String fontstyle) {
		this.fontstyle = fontstyle;
	}

	public String getFontweight() {
		return fontweight;
	}

	public void setFontweight(String fontweight) {
		this.fontweight = fontweight;
	}
	
	

}
