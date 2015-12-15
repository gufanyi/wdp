package xap.lui.core.echar;

public class SplitArea {

	@FieldMeta(desc = "是否显示")
	private boolean show = true;
	@FieldMeta(desc = "分隔区域是否显示为间隔")
	private boolean onGap = false;
	@FieldMeta(desc = "控制区域样式")
	private AreaStyle areaStyle = null;

	public SplitArea() {
		areaStyle = new AreaStyle();
		String[] color = { "rgba(250,250,250,0.3)", "rgba(200,200,200,0.3)" };
		areaStyle.setColor(color);
	}

	public boolean isShow() {
		return show;
	}

	public void setShow(boolean show) {
		this.show = show;
	}

	public boolean isOnGap() {
		return onGap;
	}

	public void setOnGap(boolean onGap) {
		this.onGap = onGap;
	}

	public AreaStyle getAreaStyle() {
		return areaStyle;
	}

	public void setAreaStyle(AreaStyle areaStyle) {
		this.areaStyle = areaStyle;
	}

}
