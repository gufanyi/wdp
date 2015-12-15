package xap.lui.core.layout;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import xap.lui.core.render.ILuiRender;
import xap.lui.core.render.PCBorderLayoutRender;
import xap.lui.core.render.notify.RenderProxy;

public class UIBorder extends UILayout {
	private static final long serialVersionUID = 3305526108711586464L;

	public static final String COLOR = "color";
	public static final String ISSHOWLEFT = "isShowLeft";
	public static final String ISSHOWRIGHT = "isShowRight";
	public static final String ISSHOWTOP = "isShowTop";
	public static final String ISSHOWBOTTOM = "isShowBottom";
	public static final String LEFTCOLOR = "leftColor";
	public static final String RIGHTCOLOR = "rightColor";
	public static final String TOPCOLOR = "topColor";
	public static final String BOTTOMCOLOR = "bottomColor";
	public static final String WIDTH = "width";
	public static final String LEFTWIDTH = "leftWidth";
	public static final String RIGHTWIDTH = "rightWidth";
	public static final String TOPWIDTH = "topWidth";
	public static final String BOTTOMWIDTH = "bottomWidth";
	public static final String CLASSNAME = "className";
	public static final String ROUNDBORDER = "roundBorder";

	private String id;
	private String widgetId;
	private String width;
	private String leftWidth;
	private String rightWidth;
	private String topWidth;
	private String bottomWidth;
	private String leftColor;
	private String rightColor;
	private String topColor;
	private String bottomColor;
	private String className;
	private String color;
	private int isShowLeft;
	private int isShowRight;
	private int isShowTop;
	private int isShowBottom;
	private int roundBorder;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getViewId() {
		return widgetId;
	}

	public void setViewId(String widgetId) {
		this.widgetId = widgetId;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getLeftWidth() {
		return leftWidth;
	}

	public void setLeftWidth(String leftWidth) {
		this.leftWidth = leftWidth;
	}

	public String getRightWidth() {
		return rightWidth;
	}

	public void setRightWidth(String rightWidth) {
		this.rightWidth = rightWidth;
	}

	public String getTopWidth() {
		return topWidth;
	}

	public void setTopWidth(String topWidth) {
		this.topWidth = topWidth;
	}

	public String getBottomWidth() {
		return bottomWidth;
	}

	public void setBottomWidth(String bottomWidth) {
		this.bottomWidth = bottomWidth;
	}

	public String getLeftColor() {
		return leftColor;
	}

	public void setLeftColor(String leftColor) {
		this.leftColor = leftColor;
	}

	public String getRightColor() {
		return rightColor;
	}

	public void setRightColor(String rightColor) {
		this.rightColor = rightColor;
	}

	public String getTopColor() {
		return topColor;
	}

	public void setTopColor(String topColor) {
		this.topColor = topColor;
	}

	public String getBottomColor() {
		return bottomColor;
	}

	public void setBottomColor(String bottomColor) {
		this.bottomColor = bottomColor;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public int getShowLeft() {
		return isShowLeft;
	}

	public void setShowLeft(int isShowLeft) {
		this.isShowLeft = isShowLeft;
	}

	public int getShowRight() {
		return isShowRight;
	}

	public void setShowRight(int isShowRight) {
		this.isShowRight = isShowRight;
	}

	public int getShowTop() {
		return isShowTop;
	}

	public void setShowTop(int isShowTop) {
		this.isShowTop = isShowTop;
	}

	public int getShowBottom() {
		return isShowBottom;
	}

	public void setShowBottom(int isShowBottom) {
		this.isShowBottom = isShowBottom;
	}

	public int getRoundBorder() {
		return roundBorder;
	}

	public void setRoundBorder(int roundBorder) {
		this.roundBorder = roundBorder;
	}

	public void addElementToPanel(UIElement ele) {
		UIBorderTrue gbordert = new UIBorderTrue();
		gbordert.setId("bt");
		this.addPanel(gbordert);
		gbordert.setElement(ele);
	}

	protected Map<String, Serializable> createAttrMap() {
		Map<String, Serializable> map = new HashMap<String, Serializable>();
		map.put(ISSHOWLEFT, 0);
		map.put(ISSHOWRIGHT, 0);
		map.put(ISSHOWTOP, 0);
		map.put(ISSHOWBOTTOM, 0);
		return map;
	}

	@Override
	public ILuiRender getRender() {
		if (render == null) {
			render = RenderProxy.getRender(new PCBorderLayoutRender(this));
		}
		return render;
	}
}
