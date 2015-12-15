package xap.lui.core.render;

import org.apache.commons.lang.StringUtils;
import xap.lui.core.comps.LuiElement;
import xap.lui.core.event.IEventSupport;
import xap.lui.core.layout.UIElement;
import xap.lui.core.layout.UIGridRowLayout;
import xap.lui.core.layout.UILayout;
import xap.lui.core.layout.UILayoutPanel;
import xap.lui.core.layout.UIPartMeta;
import xap.lui.core.model.LifeCyclePhase;

/**
 * @author renxh layout panel 渲染类
 * @param <T>
 * @param <K>
 */
@SuppressWarnings("unchecked")
public abstract class UILayoutPanelRender<T extends UILayoutPanel, K extends LuiElement> extends ShapeRender<T, K> {
	
	public UILayoutPanelRender(T uiEle) {
		super(uiEle);
	}

	@Override
	protected String mockDivId() {
		UILayoutPanel panel = getUiElement();
		UILayout layout = panel.getLayout();
		String divId = null;
		if(layout == null)
			divId = DIV_PRE + id;
		else{
			divId = createDivId(layout.getViewId(), layout);
			divId += id;
		}
		return divId;
	}
	
	@Override
	protected String mockId() {
		UILayoutPanel panel = getUiElement();
		if(panel.getId() == null || panel.getId().equals("")){
			UILayout layout = panel.getLayout();
			int index = layout.getPanelList().indexOf(panel);
			return layout.getId() + index;
		}
		else
			return super.mockId();
	}

	public String place() {
		StringBuilder buf = new StringBuilder();
		buf.append(this.placeSelf());
		UILayoutPanel panel = this.getUiElement();

		// 渲染子节点
		if(panel.getElement() != null) {
			ILuiRender render =panel.getElement().getRender();
			if (render != null) {
				buf.append(render.place());
				buf.append(getDivId()).append(".append(").append(render.getNewDivId()).append(");\n");
			}
			
			if(panel instanceof UIPartMeta) {
				if(StringUtils.isNotBlank(((UIPartMeta)panel).getUiprovider()) && this.isEditMode()) {
					buf.append(getDivId()).append(".append('<H1>代码VIEW</H1>');\n");
				}
			}
		}

		return buf.toString();
	}


	public String create() {
		StringBuilder buf = new StringBuilder();
		buf.append(this.createDesignHead());
		buf.append(this.createHead());
		
		// 渲染子节点
		UILayoutPanel panel = this.getUiElement();
		if(panel.getElement()!=null)  {
			ILuiRender render = panel.getElement().getRender();
			if (render != null) {
				buf.append(render.create());
			}
			
		}
		buf.append(this.createTail());
		buf.append(this.createDesignTail());
		return buf.toString();
	}
	
	

	
	public abstract String placeSelf();



	/*
	 * (non-Javadoc)
	 * 
	 */
	public String createHead() {

		return "";
	}

	public String createTail() {

		return "";
	}

	protected String getSourceType(LuiElement ele) {
		return null;
	}

	/**
	 * Object obj 要删除的对象
	 */
	@Override
	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void removeChild(UIElement obj) {
		String divId = this.getDivId();
		StringBuilder buf = new StringBuilder();
//		UILayoutPanel panel = this.getUiElement();
		if (divId != null) {
			UIElement child = (UIElement) obj;
			if (child != null) {
				child.getRender().destroy();
			}
			buf.append("$(window).triggerHandler('resize');\n");
		} else {
			buf.append("alert('删除panel失败！未获得divId')");
		}
		addDynamicScript(buf.toString());
	}

	/**
	 * Object obj 父节点
	 */
	@Override
	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void destroy() {
		String divId = this.getDivId();
		StringBuilder buf = new StringBuilder();
		UILayoutPanel panel = this.getUiElement();
		if (divId != null) {
			UIElement child = panel.getElement();
			if (child != null) {
				child.getRender().destroy();
			}
			buf.append("window.execDynamicScript2RemovePanel('" + divId + "');\n");
			buf.append("$(window).triggerHandler('resize');\n");
		} else {
			buf.append("alert('删除panel失败！未获得divId')");
		}

		addDynamicScript(buf.toString());
	}

	/**
	 * Object obj 要添加的对象
	 */
	@Override
	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void addChild(UIElement obj) {
		StringBuilder dsBuf = (StringBuilder) this.getContextAttribute(UIRender.DS_SCRIPT);
		if(dsBuf == null){
			dsBuf = new StringBuilder();
			this.setContextAttribute(UIRender.DS_SCRIPT, dsBuf);
		}
		String divId = this.getDivId();
		UIElement ele = (UIElement) obj;
		ILuiRender render = ele.getRender();
		
		StringBuilder buf = new StringBuilder();
		String html = render.place();
		buf.append(html);
		
		buf.append("var tmpdiv = ").append("$('#" + divId + "');\n");
		buf.append("if(tmpdiv.size() == 0) \n tmpdiv = $('body');\n");
		buf.append("tmpdiv.append(" + render.getNewDivId() + ");\n");
		buf.append(render.create());
		
		addDynamicScript(buf.toString());
	}

	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void setVisible(boolean visible) {
		if(this instanceof PCGridRowPanelRender&&!this.isEditMode()){
			UIElement element=this.getUiElement().getElement();
			if(element instanceof UIGridRowLayout){
				ILuiRender render =element.getRender();
				String varId=render.getDivId();
				addDynamicScript("$('#"+ varId +"')."+ (visible? "show()" : "hide()") +";\n");
			}
		}
		addDynamicScript("$('#"+ getNewDivId() +"')."+ (visible? "show()" : "hide()") +";\n");
	}
	
	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void setCssStyle() {
		StringBuilder buf = new StringBuilder();
		getCssStylesScript(buf, "$('#" + getNewDivId() + "')");
		addDynamicScript(buf.toString());
	}
	
	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void setBorder() {
		StringBuilder buf = new StringBuilder();
		getBorderScript(buf, "$('#" + getNewDivId() + "')");
		addDynamicScript(buf.toString());
	}
	
	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void setTopPadding() {
		StringBuilder buf = new StringBuilder();
		this.getPaddingTopScript(buf, "$('#" + getNewDivId() + "')");
		addDynamicScript(buf.toString());
	}
	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void setBottomPadding() {
		StringBuilder buf = new StringBuilder();
		this.getPaddingBottomScript(buf, "$('#" + getNewDivId() + "')");
		addDynamicScript(buf.toString());
	}
	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void setLeftPadding() {
		StringBuilder buf = new StringBuilder();
		this.getPaddingLeftScript(buf, "$('#" + getNewDivId() + "')");
		addDynamicScript(buf.toString());
	}
	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void setRightPadding() {
		StringBuilder buf = new StringBuilder();
		this.getPaddingRightScript(buf, "$('#" + getNewDivId() + "')");
		addDynamicScript(buf.toString());
	}

	protected String getBorderString(boolean script) {
		UILayoutPanel panel = getUiElement();
		StringBuilder buf = new StringBuilder();
		String border = panel.getBorder();
		if(border != null && !border.equals("")){
			buf.append("border:").append(getThemeBorder(border)).append(";");
		}
		
		String leftBorder = panel.getLeftBorder();
		if(leftBorder != null && !leftBorder.equals("")){
			buf.append("border-left:").append(getThemeBorder(leftBorder)).append(";");
		}
		String rightBorder = panel.getRightBorder();
		if(rightBorder != null && !rightBorder.equals("")){
			buf.append("border-right:").append(getThemeBorder(rightBorder)).append(";");
		}
		String topBorder = panel.getTopBorder();
		if(topBorder != null && !topBorder.equals("")){
			buf.append("border-top:").append(getThemeBorder(topBorder)).append(";");
		}
		String bottomBorder = panel.getBottomBorder();
		if(bottomBorder != null && !bottomBorder.equals("")){
			buf.append("border-bottom:").append(getThemeBorder(bottomBorder)).append(";");
		}
		if(buf.length() != 0){
			return buf.toString() + ";";
		}
		return "";
	}
	
	protected void getBorderScript(StringBuilder buf, String divId){
		//标识是否使用border样式
		UILayoutPanel panel = getUiElement();
		buf.append(divId).append(".css('border','0px');\n");
		//border样式最后渲染
		String leftBorder = panel.getLeftBorder();
		if(leftBorder != null && !leftBorder.equals("")){
			buf.append(divId).append(".css('border-left','").append(getThemeBorder(leftBorder)).append("');\n;");
		}
		String rightBorder = panel.getRightBorder();
		if(rightBorder != null && !rightBorder.equals("")){
			buf.append(divId).append(".css('border-right','").append(getThemeBorder(rightBorder)).append("');\n");
		}
		String topBorder = panel.getTopBorder();
		if(topBorder != null && !topBorder.equals("")){
			buf.append(divId).append(".css('border-top','").append(getThemeBorder(topBorder)).append("');\n");
		}
		String bottomBorder = panel.getBottomBorder();
		if(bottomBorder != null && !bottomBorder.equals("")){
			buf.append(divId).append(".css('border-bottom','").append(getThemeBorder(bottomBorder)).append("');\n");
		}
		String border = panel.getBorder();
		if(border != null && !border.equals("")){
			//如果border样式存在，则取border样式
			buf.append(divId).append(".css('border','").append(getThemeBorder(border)).append("');\n");
		}
	}
	
	private String getThemeBorder(String border){
		if(border.equals("#"))
			border = "1px solid #d1e2d8";
		else if(border.trim().matches("[0-9]{1,}")){
			border = border.trim() + "px";
		}
		return border;
	}
	
	protected String getPaddingLeftString(boolean script) {
		UILayoutPanel panel = getUiElement();
		String leftPadding = panel.getLeftPadding();
		if(leftPadding == null || leftPadding.equals(""))
			return "";
		return "padding-left:" + leftPadding + "px;";
	}
	
	protected void getPaddingLeftScript(StringBuilder buf, String divId) {
		UILayoutPanel panel = getUiElement();
		String leftPadding = panel.getLeftPadding();
		if(leftPadding == null || leftPadding.equals(""))
			return;
		buf.append(divId).append(".css('padding-left','").append(leftPadding).append("px');\n");
	}

	protected String getPaddingRightString(boolean script) {
		UILayoutPanel panel = getUiElement();
		String rightPadding = panel.getRightPadding();
		if(rightPadding == null || rightPadding.equals(""))
			return "";
		return "padding-right:" + rightPadding + "px;";
	}
	
	protected void getPaddingRightScript(StringBuilder buf, String divId) {
		UILayoutPanel panel = getUiElement();
		String rightPadding = panel.getRightPadding();
		if(rightPadding == null || rightPadding.equals(""))
			return;
		buf.append(divId).append(".css('padding-right','").append(rightPadding).append("px');\n");
	}
	
	protected String getPaddingBottomString(boolean script) {
		UILayoutPanel panel = getUiElement();
		String bottomPadding = panel.getBottomPadding();
		if(bottomPadding == null || bottomPadding.equals(""))
			return "";
		return "padding-bottom:" + bottomPadding + "px;";
	}

	protected void getPaddingBottomScript(StringBuilder buf, String divId) {
		UILayoutPanel panel = getUiElement();
		String bottomPadding = panel.getBottomPadding();
		if(bottomPadding == null || bottomPadding.equals(""))
			return;
		buf.append(divId).append(".css('padding-bottom','").append(bottomPadding).append("px');\n");
	}

	
	protected String getPaddingTopString(boolean script) {
		UILayoutPanel panel = getUiElement();
		String topPadding = panel.getTopPadding();
		if(topPadding == null || topPadding.equals(""))
			return "";
		return "padding-top:" + topPadding + "px;";
	}
	
	protected void getPaddingTopScript(StringBuilder buf, String divId) {
		UILayoutPanel panel = getUiElement();
		String topPadding = panel.getTopPadding();
		if(topPadding == null || topPadding.equals(""))
			return;
		buf.append(divId).append(".css('padding-top','").append(topPadding).append("px');\n");
	}

	/**
	 * 获取cssStyle属性
	 * 
	 * @return
	 */
	protected String getCssStylesString(){
		UILayoutPanel panel = getUiElement();
		String cssStyles = panel.getCssStyle();
		if(cssStyles == null || cssStyles.equals(""))
			return "";
		return cssStyles;
	}
	
	protected void getCssStylesScript(StringBuilder buf, String divId){
		UILayoutPanel panel = getUiElement();
		String cssStyle = panel.getCssStyle();
		if(cssStyle == null || cssStyle.equals(""))
			return;
		String[] cssStyles = cssStyle.split(";");
		for (int i = 0; i < cssStyles.length; i++){
			String style = cssStyles[i].trim();
			String[] styleEles = style.split(":");
			if (styleEles.length != 2)
				continue;
			buf.append(divId).append(".css('").append(styleEles[0]).append("','").append(styleEles[1]).append("');\n");
		}
	}
	

	@Override
	protected String getSourceType(IEventSupport ele) {
		return null;
	}



}
