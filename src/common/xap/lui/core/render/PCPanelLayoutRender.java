package xap.lui.core.render;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import xap.lui.core.comps.LuiElement;
import xap.lui.core.event.IEventSupport;
import xap.lui.core.layout.UILayoutPanel;
import xap.lui.core.layout.UIPanel;
import xap.lui.core.layout.UIPanelPanel;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.model.LuiPageContext;

import com.alibaba.fastjson.JSON;

/**
 * @author renxh Panel布局渲染器
 */
@SuppressWarnings("unchecked")

public  class PCPanelLayoutRender extends UILayoutRender<UIPanel, LuiElement> {
// class PCPanelLayoutRender extends UIPanelRender<UIPanel, LuiElement> {
	private String title;
	public PCPanelLayoutRender(UIPanel uiEle) {
		super(uiEle);
		UIPanel panelLayout = this.getUiElement();
		this.title = translate(panelLayout.getI18nName(), panelLayout.getTitle(), panelLayout.getLangDir());
		
	}
	
	

	public String create() {
		StringBuilder buf = new StringBuilder();
		buf.append(this.createDesignHead());
		buf.append(this.createHead());		
		
		// 子节点
		UIPanel panel = this.getUiElement();
		List<UILayoutPanel> panelList = panel.getPanelList();
		for(int i=0;i< panelList.size();i++){
			UILayoutPanel uiLayoutPanel = panelList.get(i);uiLayoutPanel.getRender();
			ILuiRender render = uiLayoutPanel.getRender();
			if(render!=null)
				buf.append(render.create());
		}	
		buf.append(this.createTail());
		buf.append(this.createDesignTail());
		return buf.toString();
	}
	
	private String generateParam(UIPanel panel,UIPanelPanel cp) {
		String topPadding = cp.getTopPadding();
		String bottomPadding = cp.getBottomPadding();
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("parent", this.getDivId());
		param.put("position", "relative");
		param.put("title", title);
		param.put("flowmode", isFlowMode());
		if(isFlowMode() && !StringUtils.isEmpty(topPadding)) {
			param.put("topPadding", topPadding);
		}
		if(isFlowMode() && !StringUtils.isEmpty(bottomPadding)) {
			param.put("bottomPadding", bottomPadding);
		}
		if(StringUtils.isNotBlank(panel.getClassName())){
			param.put("className", panel.getClassName());
		}
		String selfDefRender = panel.getRenderType();
		if(!StringUtils.isBlank(selfDefRender)){
			param.put("selfDefRender", selfDefRender);
		}
		Boolean expand = panel.isExpand();
		param.put("expand", !(expand!=null && false==expand));
		param.put("isCanExpand", panel.getIsCanExpand());
		return JSON.toJSONString(param);
	}

	public String createHead() {
//		String parentDivId = this.getDivId();
		StringBuilder buf = new StringBuilder();
		String showId = this.getVarId();
		if(title == null)
			title = "";
		
		UIPanel panel = getUiElement();
		UIPanelPanel cp = (UIPanelPanel) panel.getPanelList().get(0);
		
		//(parent, name, left, top, width, height, position, title, attrArr, className, renderType) {
		buf.append("var ").append(showId).append(" = $(\"<div id='"+id+"'>\").panel(\n");
		buf.append(generateParam(panel,cp));
		buf.append("\n).panel('instance');\n");

		//将临时变量的HTML转移到新建的PanelComp对象的内容DIV内部
		//buf.append(showId + ".contentDiv.empty().append($('#"+parentDivId+"').children());\n");
		
		if (this.getViewId() != null) {
			String widget = WIDGET_PRE + this.getCurrWidget().getId();
			buf.append("var ").append(widget).append(" = pageUI.getViewPart(").append(QUOTE).append(this.getCurrWidget().getId()).append(QUOTE).append(");\n");
			buf.append(widget + ".addPanel(" + showId + ");\n");
		} else
			buf.append("pageUI.addPanel(" + showId + ");\n");

		return buf.toString();
	}



	public String createTail() {
		StringBuilder buf = new StringBuilder();
		buf.append(this.getVarId()).append(".initByExpandState = function()\n{;");
		String funcId = "$"+getId()+"_content_init";
		buf.append("var tmpFunc = window."+funcId+";\n");
		buf.append("if(tmpFunc){tmpFunc();");
		buf.append("window."+funcId+" = null;}\n};\n");
		
		UIPanel panel = (UIPanel)this.getUiElement();
		//获取uimeta.um中的对应的panel的expand属性，设置js控件默认显示与否
		Boolean expand = panel.isExpand();
		if(expand==null || true==expand){
			buf.append(this.getVarId()).append(".initByExpandState();\n");
		}
		return buf.toString();
	}
	
	protected String getSourceType(IEventSupport ele) {
		return LuiPageContext.SOURCE_TYPE_PANELLAYOUT;
	}

	@Override
	public String placeSelf() {
		StringBuilder buf = new StringBuilder();
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
	
	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void setRenderTypeFunc(String renderType) {
		StringBuilder buf = new StringBuilder();
		String showId = this.getVarId();
		if (this.getViewId() != null) {
			String widget = WIDGET_PRE + this.getCurrWidget().getId();
			buf.append("var ").append(widget).append(" = pageUI.getViewPart(" + QUOTE + this.getCurrWidget().getId() + QUOTE + ");\n");
			buf.append("var "+showId+" = ").append(widget + ".getPanel(" + QUOTE + id + QUOTE + ");\n");
		} else{
			buf.append("var "+showId+" = ").append("pageUI.getPanel(" + QUOTE + id + QUOTE + ");\n");
		}
		buf.append(showId).append(".setRenderTypeFunc("+renderType+");\n");
		this.addDynamicScript(buf.toString());
	}
	
	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void setTitle(String title) {
		StringBuilder buf = new StringBuilder();
		String showId = this.getVarId();
		if (this.getViewId() != null) {
			String widget = WIDGET_PRE + this.getCurrWidget().getId();
			buf.append("var ").append(widget).append(" = pageUI.getViewPart(" + QUOTE + this.getCurrWidget().getId() + QUOTE + ");\n");
			buf.append("var "+showId+" = ").append(widget + ".getPanel(" + QUOTE + id + QUOTE + ");\n");
		} else{
			buf.append("var "+showId+" = ").append("pageUI.getPanel(" + QUOTE + id + QUOTE + ");\n");
		}
		buf.append(showId).append(".setTitle(" + QUOTE + title + QUOTE + ");\n");
		this.addDynamicScript(buf.toString());
	}
	
	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void setExpand(boolean expand) {
		StringBuilder buf = new StringBuilder();
		String showId = this.getVarId();
		if (this.getViewId() != null) {
			String widget = WIDGET_PRE + this.getCurrWidget().getId();
			buf.append("var ").append(widget).append(" = pageUI.getViewPart(" + QUOTE + this.getCurrWidget().getId() + QUOTE + ");\n");
			buf.append("var "+showId+" = ").append(widget + ".getPanel(" + QUOTE + id + QUOTE + ");\n");
		} else{
			buf.append("var "+showId+" = ").append("pageUI.getPanel(" + QUOTE + id + QUOTE + ");\n");
		}
		buf.append(showId).append(".setExpand(" + expand +  ");\n");
		this.addDynamicScript(buf.toString());
	}

}
