package xap.lui.core.echar;

public class Title {
	@FieldMeta(desc = "显示策略")
	private Boolean show = null;
	@FieldMeta(desc = "一级层叠控制")
	private Integer zlevel = null;
	@FieldMeta(desc = "二级层叠控制")
	private Integer z = null;
	@FieldMeta(desc = "主标题文本")
	private String text = null;
	@FieldMeta(desc = "主标题文本超链接")
	private String link = null;
	@FieldMeta(desc = "指定窗口打开主标题超链接")
	private String target = null;
	@FieldMeta(desc = "副标题文本")
	private String subtext = null;
	@FieldMeta(desc = "副标题文本超链接")
	private String sublink = null;
	@FieldMeta(desc = "指定窗口打开副标题超链接")
	private String subtarget = null;
	@TypeEnum(types = { "center", "left", "right" })
	@FieldMeta(desc = "水平安放位置")
	private String x = "left";
	@TypeEnum(types = { "top", "bottom", "center" })
	@FieldMeta(desc = "垂直安放位置，默认为全图顶端")
	private String y = null;
	@TypeEnum(types = { "left", "right", "center" })
	@FieldMeta(desc = "水平对齐方式")
	private String textAlign = null;
	@FieldMeta(desc = "标题背景颜色，默认透明")
	private String backgroundColor = null;
	@FieldMeta(desc = "标题边框颜色")
	private String borderColor = "#ccc";
	@FieldMeta(desc = "标题边框线宽,单位px")
	private Integer borderWidth = null;
	@FieldMeta(desc = "标题内边距，单位px，默认各方向内边距为5")
	private Integer padding = null;
	@FieldMeta(desc = "主副标题纵向间隔，单位px，默认为10")
	private Integer itemGap = null;
	@FieldMeta(desc = "主标题文本样式")
	private TextStyle textStyle = null;
	@FieldMeta(desc = "副标题文本样式")
	private TextStyle subtextStyle = null;

	public Boolean getShow() {
		return show;
	}

	public void setShow(Boolean show) {
		this.show = show;
	}

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

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getSubtext() {
		return subtext;
	}

	public void setSubtext(String subtext) {
		this.subtext = subtext;
	}

	public String getSublink() {
		return sublink;
	}

	public void setSublink(String sublink) {
		this.sublink = sublink;
	}

	public String getSubtarget() {
		return subtarget;
	}

	public void setSubtarget(String subtarget) {
		this.subtarget = subtarget;
	}

	public String getX() {
		return x;
	}

	public void setX(String x) {
		this.x = x;
	}

	public String getY() {
		return y;
	}

	public void setY(String y) {
		this.y = y;
	}

	public String getTextAlign() {
		return textAlign;
	}

	public void setTextAlign(String textAlign) {
		this.textAlign = textAlign;
	}

	public String getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public String getBorderColor() {
		return borderColor;
	}

	public void setBorderColor(String borderColor) {
		this.borderColor = borderColor;
	}

	public Integer getBorderWidth() {
		return borderWidth;
	}

	public void setBorderWidth(Integer borderWidth) {
		this.borderWidth = borderWidth;
	}

	public Integer getPadding() {
		return padding;
	}

	public void setPadding(Integer padding) {
		this.padding = padding;
	}

	public Integer getItemGap() {
		return itemGap;
	}

	public void setItemGap(Integer itemGap) {
		this.itemGap = itemGap;
	}

	public TextStyle getTextStyle() {
		return textStyle;
	}

	public void setTextStyle(TextStyle textStyle) {
		this.textStyle = textStyle;
	}

	public TextStyle getSubtextStyle() {
		return subtextStyle;
	}

	public void setSubtextStyle(TextStyle subtextStyle) {
		this.subtextStyle = subtextStyle;
	}

}
