package xap.lui.core.render;

import xap.lui.core.comps.LuiElement;
import xap.lui.core.event.IEventSupport;
import xap.lui.core.layout.UIElement;
import xap.lui.core.layout.UIPanel;
import xap.lui.core.layout.UIPanelPanel;
import xap.lui.core.layout.UIViewPart;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.model.LuiPageContext;
import xap.lui.core.model.ViewPartMeta;


/**
 * @author renxh
 * 布局中的panel 渲染器
 */
@SuppressWarnings("unchecked")
public class PCPanelPanelRender extends UILayoutPanelRender<UIPanelPanel, LuiElement> {

	public PCPanelPanelRender(UIPanelPanel uiEle,PCPanelLayoutRender parentRender) {
		super(uiEle);
		this.parentRender = parentRender;
	}
	
	@Override
	protected String mockId() {
		UIPanel panel = (UIPanel) getUiElement().getLayout();
		return panel.getId() + "_p";
	}
	
	@Override
	protected String mockDivId() {
		UIPanel panel = (UIPanel) getUiElement().getLayout();
		return createDivId(panel.getViewId(), panel);
	}


	public String createHead() {
		PCPanelLayoutRender parent = (PCPanelLayoutRender)this.getParentRender();
		String panelLayoutId = parent.getId();		
		StringBuilder buf = new StringBuilder();
		buf.append("window.$").append(panelLayoutId).append("_content_init = function(){\n");
		// 将已有的脚本暂存在临时变量中
		StringBuilder dsScript = (StringBuilder) this.getContextAttribute(DS_SCRIPT);

		if (dsScript == null) {
			dsScript = new StringBuilder();
			this.setContextAttribute(DS_SCRIPT, dsScript);
		}

		this.setContextAttribute("$panel_" + id + "$tmpScript", dsScript.toString());
		dsScript.delete(0, dsScript.length());
		
		return buf.toString();
	}


	public String createTail() {
		StringBuilder tmpBuf = new StringBuilder();
		StringBuilder dsScript = (StringBuilder) this.getContextAttribute(DS_SCRIPT);
		
		dsScript.append(PcFormRenderUtil.getAllFormDsScript(this.viewId));
		
		
		String tmpScript = (String) this.getContextAttribute("$panel_" + id + "$tmpScript");
		PCPanelLayoutRender parentRender = (PCPanelLayoutRender) this.getParentRender();
		
		UIPanel panel = parentRender.getUiElement();
		// 如果 不显示
		Boolean expand = panel.isExpand();
		if (expand!=null && false==expand) {
			// 将dsScript中的内容写入页面，并恢复原来的脚本
			tmpBuf.append(dsScript.toString());
			dsScript.delete(0, dsScript.length());
			if (tmpScript != null)
				dsScript.append(tmpScript);
		} else {
			if (tmpScript != null)
				dsScript.insert(0, tmpScript);
		}
		this.removeContextAttribute("$panel_" + id + "$tmpScript");
		
		
		tmpBuf.append("\n};\n");
		return tmpBuf.toString();
	}
	
	protected String getSourceType(IEventSupport ele) {
		return LuiPageContext.SOURCE_TYPE_PANELPANEL;
	}


	@Override
	public String getNewDivId() {
		return "";
	}

	@Override
	protected String addEditableListener(String divId, String widgetId, String uiId, String subuiId, String eleId, String subEleId, String type) {
		StringBuilder buf = new StringBuilder();
		PCPanelLayoutRender parent = (PCPanelLayoutRender) this.getParentRender();
		ViewPartMeta luiWidget =  this.getCurrWidget();
		String showId = parent.getVarId();
		
		buf.append("var contentDiv = "+showId+".getContentDiv();\n");
		buf.append("var params = {");
		buf.append("widgetid:'").append(widgetId).append("'");
		buf.append(",uiid:'").append(uiId).append("'");
		buf.append(",subuiid:'").append(subuiId).append("'");
		buf.append(",eleid:'").append(eleId).append("'");
		buf.append(",type:'").append(type).append("'");
		buf.append("};\n");
		buf.append("$.design.getObj({divObj:contentDiv[0], params:params, objType:'panel'});\n");
		buf.append("$.draglistener.getObj(contentDiv[0]);\n");
		return buf.toString();
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
//		String divId = parent.getDivId();
		UIElement ele = (UIElement) obj;
		ILuiRender render =ele.getRender();
		
		StringBuilder buf = new StringBuilder();
		String html = render.place();
		buf.append(html);
		
		UIPanelPanel panel = this.getUiElement();
		String widgetId = ele.getViewId();
		if(widgetId != null && !(ele instanceof UIViewPart)){
			buf.append("pageUI.getViewPart('" + widgetId + "').getPanel('" + panel.getLayout().getId()+ "').getContentDiv().append(" + render.getNewDivId() + ");\n");
		}
		else{
			buf.append("pageUI.getPanel('" + panel.getLayout().getId()+ "').getContentDiv().append(" + render.getNewDivId() + ");\n");
		}
		buf.append(render.create());
		
		addDynamicScript(buf.toString());
	}

	@Override
	public String placeSelf() {
		return "";
	}

	@Override
	public void setTopPadding() {
		StringBuilder buf = new StringBuilder();
		this.getPaddingTopScript(buf, "$('#" + getNewDivId2() + "')");
		addDynamicScript(buf.toString());
	}
	@Override
	public void setBottomPadding() {
		StringBuilder buf = new StringBuilder();
		this.getPaddingBottomScript(buf, "$('#" + getNewDivId2() + "')");
		addDynamicScript(buf.toString());
	}
	@Override
	public void setLeftPadding() {
		StringBuilder buf = new StringBuilder();
		this.getPaddingLeftScript(buf, "$('#" + getNewDivId2() + "')");
		addDynamicScript(buf.toString());
	}
	@Override
	public void setRightPadding() {
		StringBuilder buf = new StringBuilder();
		this.getPaddingRightScript(buf, "$('#" + getNewDivId2() + "')");
		addDynamicScript(buf.toString());
	}

	public String getNewDivId2() {
		if (isEditMode()) {
			return this.getDivId() + "_raw";
		}
		return this.getDivId();
	}
}
