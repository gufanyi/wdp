package xap.lui.core.render;

import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.common.Theme;
import xap.lui.core.comps.LuiElement;
import xap.lui.core.event.IEventSupport;
import xap.lui.core.layout.UIBorder;
import xap.lui.core.model.LuiPageContext;

/**
 * @author renxh border布局，只包含一个panel
 * @param <T>
 * @param <K>
 */
@SuppressWarnings("unchecked")
public class PCBorderLayoutRender extends UILayoutRender<UIBorder, LuiElement> {
	// 边框基础字符串
	protected static final String BORDER_ID_BASE = "border_";

	private String color = LuiRuntimeContext.getTheme().getThemeElement(Theme.LUI_BORDER_COLOR);
	private String leftColor = null;
	private String rightColor = null;
	private String topColor = null;
	private String bottomColor = null;

	private int width = 0;
	private int leftWidth = -1;
	private int rightWidth = -1;
	private int topWidth = -1;
	private int bottomWidth = -1;

	private boolean showLeft = true;
	private boolean showRight = true;
	private boolean showTop = true;
	private boolean showBottom = true;

	// div的class属性
	private String className = null;
	public PCBorderLayoutRender(final UIBorder uiEle) {
		super(uiEle);
		final UIBorder border = this.getUiElement();
//		divId = DIV_PRE + (widget == null || widget.equals("") ? "" : (widget + "_")) + id;
		this.leftColor = border.getLeftColor();
		this.rightColor = border.getRightColor();
		this.topColor = border.getTopColor();
		this.bottomColor = border.getBottomColor();
	
		//TODO : 为了掩饰暂时这么处理
		this.width = 1;//getInteger(border.getWidth());
		this.leftWidth = -1;//getInteger(border.getLeftWidth());
		this.rightWidth = -1;//getInteger(border.getRightWidth());
		this.topWidth = -1;//getInteger(border.getTopWidth());
		this.bottomWidth = -1;//getInteger(border.getBottomWidth());
		
		this.showLeft = border.getShowLeft() == 0 ? true : false;
		this.showRight = border.getShowRight() == 0 ? true : false;
		this.showTop = border.getShowTop() == 0 ? true : false;
//		this.roundBorder = border.getRoundBorder() == 0 ? true : false;
		this.showBottom = border.getShowBottom() == 0 ? true : false;
		this.className = border.getClassName();
		
		this.color = LuiRuntimeContext.getTheme().getThemeElement(Theme.LUI_BORDER_COLOR);

	}



	public String getColor() {
		return color;
	}

	public void setColor(final String color) {
		this.color = color;
	}

	public String getLeftColor() {
		return leftColor;
	}

	public void setLeftColor(final String leftColor) {
		this.leftColor = leftColor;
	}

	public String getRightColor() {
		return rightColor;
	}

	public void setRightColor(final String rightColor) {
		this.rightColor = rightColor;
	}

	public String getTopColor() {
		return topColor;
	}

	public void setTopColor(final String topColor) {
		this.topColor = topColor;
	}

	public String getBottomColor() {
		return bottomColor;
	}

	public void setBottomColor(final String bottomColor) {
		this.bottomColor = bottomColor;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(final int width) {
		this.width = width;
	}

	public int getLeftWidth() {
		return leftWidth;
	}

	public void setLeftWidth(final int leftWidth) {
		this.leftWidth = leftWidth;
	}

	public int getRightWidth() {
		return rightWidth;
	}

	public void setRightWidth(final int rightWidth) {
		this.rightWidth = rightWidth;
	}

	public int getTopWidth() {
		return topWidth;
	}

	public void setTopWidth(final int topWidth) {
		this.topWidth = topWidth;
	}

	public int getBottomWidth() {
		return bottomWidth;
	}

	public void setBottomWidth(final int bottomWidth) {
		this.bottomWidth = bottomWidth;
	}

	public boolean isShowLeft() {
		return showLeft;
	}

	public void setShowLeft(final boolean showLeft) {
		this.showLeft = showLeft;
	}

	public boolean isShowRight() {
		return showRight;
	}

	public void setShowRight(final boolean showRight) {
		this.showRight = showRight;
	}

	public boolean isShowTop() {
		return showTop;
	}

	public void setShowTop(final boolean showTop) {
		this.showTop = showTop;
	}

	public boolean isShowBottom() {
		return showBottom;
	}

	public void setShowBottom(final boolean showBottom) {
		this.showBottom = showBottom;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(final String className) {
		this.className = className;
	}

	protected String getSourceType(final IEventSupport ele) {
		return LuiPageContext.SOURCE_TYPE_BORDER;
	}
//
//	@Override
//	public String placeHead() {
//
//		final StringBuffer buf = new StringBuffer();
//		buf.append("var ").append(getNewDivId()).append(" = $ce('DIV');\n");
//		buf.append(getNewDivId()).append(".style.width = '100%';\n");
//		buf.append(getNewDivId()).append(".style.height = '100%';\n");
//		buf.append(getNewDivId()).append(".style.left = '0px';\n");
//		buf.append(getNewDivId()).append(".style.position = 'relative';\n");
////		buf.append(getNewDivId()).append(".style.border = '1px solid red';\n");
//		buf.append(getNewDivId()).append(".id = '" + getNewDivId() + "';\n");
////		
////		if (height != null) {
////			buf.append(getNewDivId()).append(".style.height = '"+height+"';\n");
////			buf.append(getNewDivId()).append(".setAttribute('hasheight','1');\n");
////		} else {
////			buf.append(getNewDivId()).append(".setAttribute('hasheight','0');\n");
//////			buf.append(getNewDivId()).append(".style.minHeight = '30px';\n");
////		}
////		if (anchor != null)
////			buf.append(getNewDivId()).append(".anchor='").append(anchor).append("';\n");
//		
//		if(this.isEditMode()){
//			buf.append(this.generalEditableHeadHtmlDynamic());
//			buf.append(getNewDivId()).append(".appendChild(" + getDivId() + ");\n");
//		}
//		return buf.toString();
//		
//	}
//	
//	public String generalTailHtmlDynamic(){
//		return "";
//	}



}
