package xap.lui.core.render;

import java.util.Iterator;
import java.util.List;

import xap.lui.core.comps.LuiElement;
import xap.lui.core.event.IEventSupport;
import xap.lui.core.layout.UIAbsoluteLayout;
import xap.lui.core.layout.UIComponent;
import xap.lui.core.layout.UIElement;
import xap.lui.core.layout.UILayout;
import xap.lui.core.layout.UILayoutPanel;
import xap.lui.core.model.LuiPageContext;

public class UIAbsoluteLayoutRender<T extends UIAbsoluteLayout, K extends LuiElement> extends ShapeRender<T, K> {
	private int childCount = 0; // 子节点数量，默认为0
	public UIAbsoluteLayoutRender(T uiEle) {
		super(uiEle);
	}
	
	/**
	 * 渲染占位符
	 * 
	 * @return String
	 */
	public String place() {
		StringBuilder buf = new StringBuilder();
		buf.append(this.placeSelf());
		// 渲染子节点
		UIAbsoluteLayout layout = getUiElement();
		List<UIComponent> compList = layout.getComponentList();
		Iterator<UIComponent> it = compList.iterator();
		while (it.hasNext()) {
			UIComponent component = it.next();
			ILuiRender compRender =component.getRender();
			if (compRender != null) {
				buf.append(compRender.place());
				String cDivId = compRender.getNewDivId();
				if (cDivId != null && !cDivId.equals(""))
					buf.append(getDivId()).append(".append(").append(cDivId).append(");\n");
			}
		}
		return buf.toString();
	}
	
	/**
	 * 利用dom创建DIV
	 * 
	 * @return
	 */
	public String placeSelf() {
		StringBuilder buf = new StringBuilder();
		String newDivId = getNewDivId();
		buf.append("var ").append(newDivId).append(" = $('<div>').attr({\n");
		buf.append("'id':'").append(newDivId).append("'");
		if (isFlowMode()) {
			buf.append(",\n'flowmode':").append(isFlowMode()).append("}).css({\n");
		} else {
			buf.append("}).css({\n'height':'100%',\n");
		}
		buf.append("'width':'100%',\n");
		buf.append("'-webkit-user-select':'none',\n");
		buf.append("'overflow':'hidden'});");
		buf.append("if(window.bindAbsoluteLayoutEvent!=null) bindAbsoluteLayoutEvent('"+newDivId+"');");
		if (this.isEditMode()) {
			buf.append(this.placeDesign());
			buf.append(newDivId).append(".append(").append(getDivId()).append(");\n");
		}
		return buf.toString();
	}

	@Override
	public String placeDesign() {
		if (isEditMode()) {
			StringBuilder buf = new StringBuilder("");
			buf.append("var ").append(getDivId()).append(" = $('<div onmousedown=\"absoluteLayoutMouseDownHandler(event);\">').attr('id','").append(getDivId()).append("').css({\n");
			buf.append("'position':'relative',\n");
			buf.append("'overflow':'auto',\n");
			buf.append("'height':'100%'});\n");
			if (isFlowMode()) {
				buf.append(getDivId()).append(".css('min-height','").append(MIN_HEIGHT).append("').attr('flowmode',true);\n");
			}
			return buf.toString();
		}
		return "";
	}
	
	@Override
	public String create() {
		StringBuilder buf = new StringBuilder();
		buf.append(this.createDesignHead());
		buf.append(this.createHead());
		// 渲染子节点
		UIAbsoluteLayout layout = getUiElement();
		List<UIComponent> compList = layout.getComponentList();
		Iterator<UIComponent> it = compList.iterator();
		while (it.hasNext()) {
			UIComponent component = it.next();
			ILuiRender render =component.getRender();
			if (render != null) {
				buf.append(render.create());
			}
		}
		buf.append(this.createTail());
		buf.append(this.createDesignTail());
		return buf.toString();
	}
	
	public String createHead(){
		return "";
	}
	
	public String createTail(){
		return "";
	}
	
	@Override
	public void destroy() {
		StringBuilder buf = new StringBuilder();
		UIAbsoluteLayout uilayout = this.getUiElement();
		if (this.getDivId() != null) {
			List<UIComponent> children = uilayout.getComponentList();
			for (int i = 0; i < children.size(); i++) {
				UIComponent component = children.get(i);
				component.getRender().destroy();
				component.getRender().destroyUiElement();
			}
			buf.append("window.execDynamicScript2RemovePanel('" + this.getDivId() + "');");
		} else {
			buf.append("alert('删除layout失败！未获得divId')");
		}
		addDynamicScript(buf.toString());
	}

	@Override
	public void removeChild(UIElement obj) {
		StringBuilder buf = new StringBuilder();
		UIAbsoluteLayout uilayout = this.getUiElement();
		if (this.getDivId() != null) {
			List<UIComponent> children = uilayout.getComponentList();
			for (int i = 0; i < children.size(); i++) {
				UIComponent component = children.get(i);
				if (obj == component) {
					component.getRender().destroy();
					component.getRender().destroyUiElement();
					break;
				}
			}
		} else {
			buf.append("alert('删除控件失败！未获得divId')");
		}
		addDynamicScript(buf.toString());
		childCount--;
	}

	@Override
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
		childCount++;
	}


	@Override
	protected  String getSourceType(IEventSupport ele){
		return LuiPageContext.SOURCE_TYPE_ABSOLUTELAYOUT;
	}
}
