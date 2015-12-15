package xap.lui.core.plugins;

import xap.lui.core.echar.FieldMeta;

//指针样式
public class Pointer {

	@FieldMeta(desc="指针长度")
	private String length=null;
	@FieldMeta(desc="指针宽度")
	private Integer width=null;
	@FieldMeta(desc="指着颜色")
	private String color=null;
	public String getLength() {
		return length;
	}
	public void setLength(String length) {
		this.length = length;
	}
	public Integer getWidth() {
		return width;
	}
	public void setWidth(Integer width) {
		this.width = width;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	
	
	
}
