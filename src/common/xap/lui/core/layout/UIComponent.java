package xap.lui.core.layout;

import xap.lui.core.common.LuiRenderContext;
import xap.lui.core.comps.FormComp;
import xap.lui.core.comps.WebComp;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.model.PagePartMeta;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.core.render.ILuiRender;
import xap.lui.core.render.UIComponentRender;
import xap.lui.core.render.UINormalComponentRender;

public class UIComponent extends UIElement {
	private static final long serialVersionUID = 8431336052516488514L;
	
	private String align;
	protected String width;
	private String height;
	private Integer top;
	private Integer left;
	private String position;
	private String maxWidth;
	
	public static final String ALIGN_LEFT = "left";
	public static final String ALIGN_RIGHT = "right";

	public UIComponent(String width, String height) {
		this(0, 0, width, height);
	}

	private ViewPartMeta viewPart = null;
	private WebComp webComp = null;

	public UIComponent(Integer left, Integer top, String width, String height) {
		this.top = top;
		this.left = left;
		this.width = width;
		this.height = height;
		this.position = "relative";
	}

	public UIComponent() {
		this("100%", "100%");
	}

	public void setHeight(String height) {
		this.height = height;
		if (LifeCyclePhase.ajax.equals(getPhase())) {
			render = this.getRender();
			if (render instanceof UINormalComponentRender) {
				((UINormalComponentRender) render).setHeight(height);
			}
		}
	}

	public void setViewId(String viewId) {
		super.setViewId(viewId);
		if (this.viewPart == null) {
			PagePartMeta pagePartMeta = LuiRenderContext.current().getPagePartMeta();
			if (pagePartMeta != null) {
				this.viewPart = LuiRenderContext.current().getPagePartMeta().getWidget(viewId);
			}
		}
	}

	public String getHeight() {
		return this.height;
	}

	public void setWidth(String width) {
		this.width = width;
		if (LifeCyclePhase.ajax.equals(getPhase())) {
			ILuiRender render = this.getRender();
			if (render instanceof UINormalComponentRender) {
				((UINormalComponentRender) render).setWidth(width);
			}
		}

	}

	public WebComp getWebComp() {

		//if(viewPart==null){
			PagePartMeta pagePartMeta = LuiRenderContext.current().getPagePartMeta();
			if (pagePartMeta != null) {
				this.viewPart = LuiRenderContext.current().getPagePartMeta().getWidget(this.getViewId());
			}
		//}
		//if (webComp == null) {
			if (this instanceof UIMenubarComp) {
				webComp = this.viewPart.getViewMenus().getMenuBar(this.getId());
			} else if(this instanceof UIFormElement) {
				String formId = ((UIFormElement)this).getFormId();
				FormComp form = (FormComp)this.viewPart.getViewComponents().getComponent(formId);
				webComp = form.getElementById(this.getId());
			} else {
				
				webComp = this.viewPart.getViewComponents().getComponent(this.getId());
			}
		//}
		return webComp;

	}

	public String getWidth() {
		return this.width;
	}

	public void setTop(Integer top) {
		this.top = top;
	}

	public Integer getTop() {
		return this.top;
	}

	public void setLeft(Integer left) {
		this.left = left;
	}

	public Integer getLeft() {
		return this.left;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getPosition() {
		return this.position;
	}

	public String getAlign() {
		return this.align;
	}

	public void setAlign(String align) {
		this.align = align;
		if(LifeCyclePhase.ajax.equals(getPhase())) {
			render = this.getRender();
			if (render instanceof UINormalComponentRender) {
				((UINormalComponentRender) render).setAlign(align);
			}
		}
		
	}

	public String getMaxWidth() {
		return this.maxWidth != null ? this.maxWidth : "100%";
	}

	public void setMaxWidth(String maxWidth) {
		this.maxWidth = maxWidth;
	}

	@Override
	public void addElement(UIElement ele) {
		throw new LuiRuntimeException("错误的方法调用，不能添加子元素");
	}

	@Override
	public void removeElement(UIElement ele) {
		throw new LuiRuntimeException("错误的方法调用，不能删除子元素");
	}

	@Override
	public UIComponent doClone() {
		UIComponent comp = (UIComponent) super.doClone();
		return comp;
	}

	public UIComponentRender getRender() {
		return (UIComponentRender) this.getWebComp().getRender();
	}

}
