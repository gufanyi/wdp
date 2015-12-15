package xap.lui.core.plugins;

import xap.lui.core.echar.FieldMeta;
import xap.lui.core.echar.TextStyle;

public class Detail {

	@FieldMeta(desc="显示与否")
	private Boolean show = null;
	@FieldMeta(desc="边框颜色")
	private String backgroundColor = null;
	@FieldMeta(desc="边框线宽")
	private Integer borderWidth = null;
	@FieldMeta(desc="边框颜色")
	private String borderColor = null;
	@FieldMeta(desc="宽度")
	private Integer width = null;
	@FieldMeta(desc="高度")
	private Integer height = null;
	@FieldMeta(desc="详情定位")
	private String[] offsetCenter = null;
	@FieldMeta(desc="格式化文本")
	private String formatter = null;
	@FieldMeta(desc="文本样式")
	private TextStyle textStyle = null;

	public Boolean getShow() {
		return show;
	}

	public void setShow(Boolean show) {
		this.show = show;
	}

	public String getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public Integer getBorderWidth() {
		return borderWidth;
	}

	public void setBorderWidth(Integer borderWidth) {
		this.borderWidth = borderWidth;
	}

	public String getBorderColor() {
		return borderColor;
	}

	public void setBorderColor(String borderColor) {
		this.borderColor = borderColor;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public String[] getOffsetCenter() {
		return offsetCenter;
	}

	public void setOffsetCenter(String[] offsetCenter) {
		this.offsetCenter = offsetCenter;
	}

	public String getFormatter() {
		return formatter;
	}

	public void setFormatter(String formatter) {
		this.formatter = formatter;
	}

	public TextStyle getTextStyle() {
		return textStyle;
	}

	public void setTextStyle(TextStyle textStyle) {
		this.textStyle = textStyle;
	}

}
