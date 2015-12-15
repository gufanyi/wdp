package xap.lui.core.render;

import org.apache.commons.lang.StringUtils;

import xap.lui.core.comps.LuiElement;
import xap.lui.core.event.IEventSupport;
import xap.lui.core.layout.UICanvas;
import xap.lui.core.model.LuiPageContext;

/**
 * @author renxh Panel 布局渲染器
 */
@SuppressWarnings("unchecked")
public class PCCanvasLayoutRender extends UILayoutRender<UICanvas, LuiElement> {
	private String title;
	public PCCanvasLayoutRender(UICanvas uiEle) {
		super(uiEle);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}


	public String createHead() {
		String parentDivId = this.getDivId();
		StringBuffer buf = new StringBuffer();
		String showId = this.getVarId();
		if(StringUtils.isBlank(title)){
			title = "背景标题";
		}
		UICanvas panel = getUiElement();
		//(parent, name, left, top, width, height, position, title, attrArr, className) {
		buf.append("var ").append(showId).append(" = $(\"<div id='"+id+"'>\").canvas({");
		buf.append("parent:").append("$('#"+parentDivId+"')[0],");
		buf.append("position:'relative',");
		String title =  panel.getTitle();
		if(title==null){
			title="背景标题";
		}
		buf.append("title:'").append(title).append("',");
		buf.append("className:'").append(panel.getClassName()).append("'");
		buf.append("}).canvas('instance');\n");
		
		if (this.getViewId() != null) {
			String widget = WIDGET_PRE + this.getCurrWidget().getId();
			buf.append("var ").append(widget).append(" = pageUI.getViewPart('").append(this.getCurrWidget().getId()).append("');\n");
			buf.append(widget + ".addPanel(" + showId + ");\n");
		} else
			buf.append("pageUI.addPanel(" + showId + ");\n");

		return buf.toString();
	}



	
	protected String getSourceType(IEventSupport ele) {
		return LuiPageContext.SOURCE_TYPE_CANVASLAYOUT;
	}

	public String placeSelf() {
		StringBuffer buf = new StringBuffer();
		String newDivId = getNewDivId();
		buf.append("var ").append(newDivId).append(" = $('<div>').attr('id','").append(newDivId).append("').css({\n");
		buf.append("'width':'100%',\n");
		if(!isFlowMode())
			buf.append("'height':'100%',\n");
		buf.append("'overflow':'hidden',\n");
		buf.append("'position':'relative'});\n");
		if (this.isEditMode()) {
			buf.append(this.placeDesign());
			buf.append(newDivId).append(".append(" + getDivId() + ");\n");
		}
		return buf.toString();
	}

	public void setCssClass(String className) {
		StringBuffer buf = new StringBuffer();
		String showId = this.getVarId();
		if (this.getViewId() != null) {
			String widget = WIDGET_PRE + this.getCurrWidget().getId();
			buf.append("var ").append(widget).append(" = pageUI.getViewPart('"+this.getCurrWidget().getId()+"');\n");
			buf.append("var "+showId+" = ").append(widget + ".getPanel('" + id + "');\n");
		} else{
			buf.append("var "+showId+" = ").append("pageUI.getPanel('" + id + "');\n");
		}
		buf.append(showId).append(".changeClass('" + className + "');\n");
		this.addDynamicScript(buf.toString());
	}
	
}
