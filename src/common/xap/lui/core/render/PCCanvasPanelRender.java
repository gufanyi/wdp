package xap.lui.core.render;

import xap.lui.core.comps.LuiElement;
import xap.lui.core.event.IEventSupport;
import xap.lui.core.layout.UICanvas;
import xap.lui.core.layout.UICanvasPanel;
import xap.lui.core.layout.UIElement;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.model.LuiPageContext;
import xap.lui.core.model.ViewPartMeta;

/**
 * @author renxh
 * 布局中的panel 渲染器
 */
@SuppressWarnings("unchecked")
public class PCCanvasPanelRender extends UILayoutPanelRender<UICanvasPanel, LuiElement> {

	public PCCanvasPanelRender(UICanvasPanel uiEle,PCCanvasLayoutRender parentRender) {
		super(uiEle);
		this.parentRender=parentRender;
	}
	
	@Override
	protected String mockDivId() {
		UICanvas canvas = (UICanvas) getUiElement().getLayout();
		return (viewId == null || viewId.equals("")) ? (DIV_PRE + canvas.getId()) : (DIV_PRE + viewId + "_" + canvas.getId());
	}
	
	
	protected String getSourceType(IEventSupport ele) {
		return LuiPageContext.SOURCE_TYPE_CANVASPANEL;
	}

	@Override
	public String createHead() {
		return "";
	}

	@Override
	public String getNewDivId() {
		return "";
	}

	@Override
	protected String addEditableListener(String divId, String widgetId, String uiId, String subuiId, String eleId, String subEleId, String type) {
		StringBuffer buf = new StringBuffer();
		PCCanvasLayoutRender parent = (PCCanvasLayoutRender) this.getParentRender();
		ViewPartMeta luiWidget =  this.getCurrWidget();
		String showId = parent.getVarId();
		
		buf.append("var contentDiv = "+showId+".getContentDiv();\n");
		buf.append("var params = {");
		buf.append("widgetid:'").append(widgetId).append("'");
		buf.append(",uiid:'").append(uiId).append("'");
		buf.append(",uiid:'").append(subuiId).append("'");
		buf.append(",eleid:'").append(eleId).append("'");
		buf.append(",type:'").append(type).append("'");
		buf.append("};\n");
		buf.append("$.design.getObj({divObj:contentDiv, params:params, objType:'panel'});\n");
		buf.append("$.draglistener.getObj(contentDiv);\n;");
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
		ILuiRender render = ele.getRender();
		
		StringBuffer buf = new StringBuffer();
		String html = render.place();
		buf.append(html);
		
		UICanvasPanel panel = this.getUiElement();
		UICanvas canvas = (UICanvas) panel.getLayout();
		
		
		String widgetId = ele.getViewId();
		if(widgetId != null){
			buf.append("pageUI.getViewPart('" + widgetId + "').getPanel('" + canvas.getId()+ "').getContentDiv().appendChild(" + render.getNewDivId() + ");\n");
		}
		else{
			buf.append("pageUI.getPanel('" + canvas.getId()+ "').getContentDiv().appendChild(" + render.getNewDivId() + ");\n");
		}
		buf.append(render.create());
		
		addDynamicScript(buf.toString());
	}

	@Override
	public String placeSelf() {
		// TODO Auto-generated method stub
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
