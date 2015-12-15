package xap.lui.core.render;
import java.util.Iterator;
import java.util.List;

import xap.lui.core.comps.LuiElement;
import xap.lui.core.layout.UIConstant;
import xap.lui.core.layout.UIElement;
import xap.lui.core.layout.UILayout;
import xap.lui.core.layout.UILayoutPanel;
import xap.lui.core.layout.UIPanel;
import xap.lui.core.model.LifeCyclePhase;
@SuppressWarnings("unchecked")
public abstract class UILayoutRender<T extends UILayout, K extends LuiElement> extends ShapeRender<T, K> {
	private int childCount = 0; // 子节点数量，默认为0
	public UILayoutRender(T uiEle) {
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
		UILayout layout = getUiElement();
		List<UILayoutPanel> pList = layout.getPanelList();
		Iterator<UILayoutPanel> it = pList.iterator();
		while (it.hasNext()) {
			UILayoutPanel panel = it.next();
			ILuiRender render =panel.getRender();
			if (render != null) {
				buf.append(render.place());
				String cDivId = render.getNewDivId();
				if (cDivId != null && !cDivId.equals(""))
					buf.append(getDivId()).append(".append(").append(cDivId).append(");\n");
			}
		}
		return buf.toString();
	}
	/**
	 * 动态创建动态脚本
	 * 
	 * @return String
	 */
	public String create() {
		StringBuilder buf = new StringBuilder();
		buf.append(this.createDesignHead());
		buf.append(this.createHead());
		// 渲染子节点
		UILayout layout = getUiElement();
		List<UILayoutPanel> pList = layout.getPanelList();
		Iterator<UILayoutPanel> it = pList.iterator();
		while (it.hasNext()) {
			UILayoutPanel panel = it.next();
			ILuiRender render =panel.getRender();
			if (render != null) {
				buf.append(render.create());
			}
		}
		buf.append(this.createTail());
		buf.append(this.createDesignTail());
		return buf.toString();
	}
	public String createHead() {
	
		return "";
	}
	public String createTail() {
		return "";
	}
	protected String getSourceType(LuiElement ele) {
		return null;
	}
	public int getChildCount() {
		return childCount;
	}
	public void setChildCount(int childCount) {
		this.childCount = childCount;
	}
	/**
	 * @param obj为子元素
	 */
	@Override
	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void removeChild( UIElement obj) {
		StringBuilder buf = new StringBuilder();
		UILayout uilayout = this.getUiElement();
		if (this.getDivId() != null) {
			List<UILayoutPanel> children = uilayout.getPanelList();
			for (int i = 0; i < children.size(); i++) {
				UILayoutPanel panel = children.get(i);
				if (obj == panel) {
					panel.getRender().destroy();
					break;
				}
			}
		} else {
			buf.append("alert('删除layout失败！未获得divId')");
		}
		addDynamicScript(buf.toString());
	}
	/**
	 * Object obj 为父节点
	 */
	@Override
	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void destroy() {
		StringBuilder buf = new StringBuilder();
		UILayout uilayout = this.getUiElement();
		if (this.getDivId() != null) {
			List<UILayoutPanel> children = uilayout.getPanelList();
			for (int i = 0; i < children.size(); i++) {
				UILayoutPanel panel = children.get(i);
				panel.getRender().destroy();
			}
			buf.append("window.execDynamicScript2RemoveLayout('" + this.getDivId() + "');");
		} else {
			buf.append("alert('删除layout失败！未获得divId')");
		}
		addDynamicScript(buf.toString());
	}
	@Override
	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void addChild( UIElement obj) {
		UILayout layout = getUiElement();
		int size = layout.getPanelList().size();
		UILayoutPanel targetPanel = (UILayoutPanel) obj;
		int index = 0;
		for (int i = 0; i < size; i++) {
			UILayoutPanel panel = layout.getPanelList().get(i);
			if (panel.getId().equals(targetPanel.getId())) {
				index = i;
				break;
			}
		}
		ILuiRender render = targetPanel.getRender();
		StringBuilder buf = new StringBuilder();
		//buf.append(" debugger;");
		String html = render.place();
		buf.append(html);
		buf.append("var div = $('#" + divId + "');\n");
		if (index == (size - 1)) {
			buf.append("div.append(" + render.getNewDivId() + ");\n");
		} else {
			// childNodes会出错，使用children。childNodes会返回TextNodes等
			buf.append(render.getNewDivId()).append(".insertBefore( div.children(':eq(" + (index) + ")'));\n");
		}
		buf.append(render.create());
		buf.append("$(window).triggerHandler('resize');\n");
		addDynamicScript(buf.toString());
	}
	
	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void setExpand() {
		StringBuilder buf = new StringBuilder();
		UIPanel panel = (UIPanel) getUiElement();
		Boolean isexpand = panel.isExpand();
		buf.append(" var tmppanel = pageUI.getViewPart('" + this.viewId + "').getPanel('" + this.getId() + "');\n");
		if (UIConstant.TRUE.equals(isexpand)) {
			buf.append("if(tmppanel.setExpand){ tmppanel.setExpand(true);}\n");
		} else {
			buf.append("if(tmppanel.setExpand){ tmppanel.setExpand(false);}\n");
		}
		addDynamicScript(buf.toString());
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
		buf.append("'overflow':'hidden'});");
		if (this.isEditMode()) {
			buf.append(this.placeDesign());
			buf.append(newDivId).append(".append(").append(getDivId()).append(");\n");
		}
		return buf.toString();
	}
	
	protected String getCssStylesScript() {
		UILayout layOut = (UILayout) this.getUiElement();
		String cssStyle = layOut.getCssStyle();
		if (cssStyle == null || cssStyle.equals("")) {
			return "";
		} else {
			return cssStyle;
		}
	}
	
	protected void getCssStylesScript(StringBuilder buf, String divId){
		UILayout panel = (UILayout) this.getUiElement();
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
}
