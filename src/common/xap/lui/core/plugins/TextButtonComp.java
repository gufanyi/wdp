package xap.lui.core.plugins;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import xap.lui.core.comps.LuiElement;
import xap.lui.core.comps.WebComp;
import xap.lui.core.context.BaseContext;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.render.notify.RenderProxy;

@XmlRootElement(name = "TextButton")
@XmlAccessorType(XmlAccessType.NONE)
public class TextButtonComp extends WebComp {
	private static final long serialVersionUID = -2587831991583629686L;
	public static final String WIDGET_NAME = "textbutton";
	@XmlAttribute
	private String value;
	@XmlAttribute
	private String width;
	@XmlAttribute
	private String height;
	
	private PCTextButtonCompRender render=null;

	@Override
	public String getWidgetName() {
		return WIDGET_NAME;
	}
	@Override
	public void mergeProperties(LuiElement ele) {
		throw new LuiRuntimeException("not implemented");
	}
	@Override
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
		this.getRender().setEnable(enabled);
	}
	@Override
	public void setVisible(boolean visible) {
		if (visible != this.isVisible) {
			this.isVisible = visible;
			this.getRender().setVisible(visible);
		}
	}
	@Override
	public BaseContext getContext() {
		TextButtonContext ctx = new TextButtonContext();
		ctx.setId(this.getId());
		return ctx;
	}
	@Override
	public void setContext(BaseContext ctx) {
		TextButtonContext btCtx = (TextButtonContext) ctx;
		this.setWidth(btCtx.getWidth());
		this.setHeight(btCtx.getHeight());
		this.setValue(btCtx.getValue());
		this.setCtxChanged(false);
	}

	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		if (width != this.width) {
			this.width = width;
			this.getRender().setWidth(width);
		}
	}
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		if (height != this.height) {
			this.height = height;
			this.getRender().setWidth(height);
		}
	}
	@Override
	public PCTextButtonCompRender getRender() {
		if(render==null){
			render=RenderProxy.getRender(new PCTextButtonCompRender(this));
		}
		return render;
	}
	
	
}
