package xap.lui.core.render;

import xap.lui.core.comps.LuiElement;
import xap.lui.core.comps.ViewElement;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.layout.UIElement;
import xap.lui.core.model.ViewPartMeta;

@SuppressWarnings("unchecked")
public abstract class ShapeRender<T extends UIElement, K extends LuiElement> extends UIRender<T, K> {
	
	
	public ShapeRender(K webEle) {
		this(webEle,null,null);
	}
	
	public ShapeRender(T uiEle) {
		this(null,uiEle,null);
	}


	public ShapeRender(K webEle,T uiEle, ILuiRender parentRender) {
		super(webEle,uiEle);
		this.setParentRender(parentRender);

		this.id = mockId();
		if(this.id == null){
			if(uiEle != null)
				this.id = (String) uiEle.getId();
			else
				this.id = webEle.getId();
			if (this.id == null) {
				throw new LuiRuntimeException(this.getClass().getName() +":id不能为空！");
			}
		}
		
		this.viewId = mockWidget();
		if(this.viewId == null){
			if(uiEle != null)
				this.viewId = (String) uiEle.getViewId();//.getAttribute(UILayout.WIDGET_ID);
			else{
				if(webEle instanceof ViewElement){
					ViewPartMeta widget = ((ViewElement)webEle).getWidget();
					if(widget!=null)
						this.viewId = widget.getId();
				}
			}
		}
		
		if (this.viewId != null && uiEle != null) {
			uiEle.setViewId(this.viewId);//.setAttribute(UILayout.WIDGET_ID, this.viewId);
		}
		
		this.divId = mockDivId();
		if(this.divId == null)
			this.divId = createDivId(viewId, uiEle);
		this.varId = createVarId(viewId, uiEle);
	}
	
	protected String createDivId(String widget, UIElement ele){
		if(ele != null)
			return (widget == null || widget.equals("")) ? (DIV_PRE + ele.getId()) : (DIV_PRE + widget + "_" + ele.getId());
		else
			return (widget == null || widget.equals("")) ? (DIV_PRE + getId()) : (DIV_PRE + widget + "_" + getId());
	}
	
	protected String createVarId(String widget, UIElement ele) {
		if(ele != null)
			return (widget == null || widget.equals("")) ? (COMP_PRE + ele.getId()) : (COMP_PRE + widget + "_" + ele.getId());
		else
			return (widget == null || widget.equals("")) ? (COMP_PRE + getId()) : (COMP_PRE + widget + "_" + getId());
	}

	protected String mockDivId() {
		return null;
	}

	protected String mockWidget() {
		return null;
	}

	protected String mockId() {
		return null;
	}

}
