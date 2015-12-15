package xap.lui.core.render;

import xap.lui.core.common.LuiRenderContext;
import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.comps.LuiElement;
import xap.lui.core.comps.WebComp;
import xap.lui.core.event.IEventSupport;
import xap.lui.core.layout.UIComponent;
import xap.lui.core.layout.UIElement;
import xap.lui.core.logger.LuiLogger;
import xap.lui.core.model.PagePartMeta;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.core.services.IPaEditorService;
import xap.lui.core.services.PaEditorServiceImpl;

/**
 * @author renxh 容器类控件渲染器
 * @param <T>
 * @param <K>
 */
@SuppressWarnings("unchecked")
public abstract class UIContainerComponentRender<T extends UIComponent, K extends WebComp> extends UIComponentRender<T, K> {

	public UIContainerComponentRender(K webEle) {
		super(webEle);
	}

	@Override
	protected String createDivId(String widget, UIElement ele) {
		if (this.viewId == null) {
			return DIV_PRE + this.id;
		} else {
			return DIV_PRE + this.viewId + "_" + this.id;
		}

	}

	@Override
	protected String createVarId(String widget, UIElement ele) {
		if (this.viewId == null) {
			return COMP_PRE + id;
		} else {
			return COMP_PRE + this.viewId + "_" + getId();
		}

	}

	@Override
	protected String getSourceType(IEventSupport ele) {
		// TODO Auto-generated method stub
		return null;
	}

	public final String place() {
		WebComp comp = this.getWebElement();
		StringBuffer buf = new StringBuffer();

		// 将控件实例设入pageContext中
		// getJspContext().setAttribute(getId(), comp); // 被下面的代码替换
		this.setContextAttribute(getId(), comp);

		String headStr = this.placeSelf();
		if (headStr != null)
			buf.append(headStr);
		return buf.toString();
	}

	/**
	 * 将Widget属性绑定到控件上
	 */
	protected String setWidgetToComponent() {
		if (this.getViewId() != null) {
			StringBuffer buf = new StringBuffer();
			buf.append(getVarId() + ".viewpart = " + WIDGET_PRE + this.getViewId()).append("\n");
			return buf.toString();
		}
		return "";
	}

	public String createHead() {

		return "";
	}

	/**
	 * @return 动态创建脚本
	 */
	public final String create() {
		WebComp comp = this.getWebElement();
		StringBuffer buf = new StringBuffer();
		buf.append(this.getAttribute(BEFORE_SCRIPT));
		buf.append(this.createDesignHead());
		String script = this.createHead();
		buf.append(script);
		// 此处加入处理子节点的代码

		script = this.createTail();
		buf.append(script);
		if (comp.getContextMenu() != null && !comp.getContextMenu().equals("")) {
			script = addContextMenu(comp.getContextMenu(), COMP_PRE + getId());
			buf.append(script);
		}

		// 添加控件事件绑定
		script = addEventSupport(comp, getCurrWidget() == null ? null : getCurrWidget().getId(), getVarId(), null);
		buf.append(script);
		String wstr = setWidgetToComponent();
		if (wstr != null)
			buf.toString();
		buf.append(this.createDesignTail());
		buf.append(this.getAttribute(AFTER_SCRIPT));
		return buf.toString();
	}

	public String placeSelf() {
		// WebComponent tab = this.getWebElement();
		StringBuffer buf = new StringBuffer();
		buf.append("var ").append(getNewDivId()).append(" = $('<div>').attr('id','").append(getNewDivId()).append("').css({\n");
		buf.append("'top':'0px',\n");
		buf.append("'left':'0px',\n");
		buf.append("'width':'100%',\n");
		buf.append("'height':'100%',\n");
		buf.append("'overflow':'hidden'});\n");
		if (this.isEditMode()) {
			buf.append(this.placeDesign());
			if (!getNewDivId().equals(getDivId()))
				buf.append(getNewDivId()).append(".append(" + getDivId() + ");\n");
		}

		return buf.toString();
	}

	public String placeDesign() {

		return "";
	}
	
	public String createDesignHead() {
		return "";
	}

	public String createTail() {
		return "";
	}

	protected String getSourceType(LuiElement ele) {
		return null;
	}

	public String generalEditableTailScript() {
		StringBuffer buf = new StringBuffer();
		if (this.isEditMode()) {
			if (this.isGenEditableTail()) {
				return "";
			}
			String widgetId = this.getViewId() == null ? "" : this.getViewId();
			String uiid = this.getUiElement() == null ? "" : (String) this.getUiElement().getId();
			String eleid = this.getWebElement() == null ? "" : this.getWebElement().getId();
			String type = this.getRenderType(this.getWebElement());
			if (type == null)
				type = "";

			if (getDivId() == null) {
				LuiLogger.error("div id is null!" + this.getClass().getName());
			} else {
				buf.append("var params = {};\n");
				buf.append("params.widgetid ='").append(widgetId).append("';\n");
				buf.append("params.uiid ='").append(uiid).append("';\n");
				buf.append("params.eleid ='").append(eleid).append("';\n");
				buf.append("params.type ='").append(type).append("';\n");
				buf.append("$.design.getObj({divObj:$('#" + getDivId() + "')[0],params:params,objType:'component'});\n");
			}
			if (this.getDivId() != null) {
				buf.append("$('#").append(this.getDivId()).append("').css('padding','0px');\n");
			}
		}
		return buf.toString();
	}

	public String createDesignTail() {
		StringBuffer buf = new StringBuffer();
		if (this.isEditMode()) {
			if (this.getViewId() != null && LuiRuntimeContext.isWindowEditorMode()) {
				return "";
			}
			String widgetId = this.getViewId() == null ? "" : this.getViewId();
			String uiid = this.getUiElement() == null ? "" : (String) this.getUiElement().getId();
			String eleid = this.getWebElement() == null ? "" : this.getWebElement().getId();
			String type = this.getRenderType(this.getWebElement());
			if (type == null)
				type = "";

			if (getDivId() == null) {
				LuiLogger.error("div id is null!" + this.getClass().getName());
			} else {
				buf.append("var params = {};\n");
				buf.append("params.widgetid ='").append(widgetId).append("';\n");
				buf.append("params.uiid ='").append(uiid).append("';\n");
				buf.append("params.eleid ='").append(eleid).append("';\n");
				buf.append("params.type ='").append(type).append("';\n");
				buf.append("$.design.getObj({divObj:$('#" + getDivId() + "')[0],params:params,objType:'component'});\n");
			}
			if (this.getDivId() != null) {
				buf.append("if(").append(this.getDivId()).append(")");
				buf.append("$('#").append(this.getDivId()).append("').css('padding','0px');\n");
			}
		}
		return buf.toString();
	}

	@Override
	public void destroy() {
		String divId = this.getDivId();
		UIComponent uiEle = this.getUiElement();
		if (this.getUiElement() != null) {
			ViewPartMeta widget = LuiRenderContext.current().getPagePartMeta().getWidget(this.getViewId());
			if (widget != null) {
				WebComp webButton = (WebComp) widget.getViewComponents().getComponent(uiEle.getId());
				StringBuffer buf = new StringBuffer();
				if (divId != null) {
					if (isEditMode()) {
						buf.append("window.execDynamicScript2RemoveComponent('" + divId + "','" + uiEle.getViewId() + "','" + webButton.getId() + "');");
						buf.append("window.execDynamicScript2RemoveComponent('" + divId + "_raw','" + uiEle.getViewId() + "','" + webButton.getId() + "');");
					} else {
						buf.append("window.execDynamicScript2RemoveComponent('" + divId + "','" + uiEle.getViewId() + "','" + webButton.getId() + "');");
					}

					this.removeComponent(widget.getId(), uiEle.getId(), isMenu(uiEle));
				} else {
					buf.append("alert('删除控件失败！未获得divId')");
				}
				addDynamicScript(buf.toString());
			}
		}
	}

}
