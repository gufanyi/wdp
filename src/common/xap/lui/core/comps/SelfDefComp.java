package xap.lui.core.comps;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import xap.lui.core.context.BaseContext;
import xap.lui.core.context.SelfDefCompContext;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.render.ILuiRender;
import xap.lui.core.render.PCSelfDefCompRender;
import xap.lui.core.render.notify.RenderProxy;


/**
 * 自定义控件
 *  
 *
 */
@XmlRootElement(name = "SelfDefComp")
@XmlAccessorType(XmlAccessType.NONE)
public class SelfDefComp extends WebComp {

	private static final long serialVersionUID = 6926917271833717792L;

	public final static String TRIGGER_ID = "TRIGGER_ID";
	
	// 可见性
	@XmlAttribute
	private boolean visible = true;
	
	// 自定义的额外Context对象
	private Serializable otherCtx = null;
	
	private PCSelfDefCompRender render=null;
	
	public Object clone() {
		return super.clone();
	}

	@Override
	public void mergeProperties(LuiElement ele) {
		throw new LuiRuntimeException("not implemented");
	}

	
	@Override
	public BaseContext getContext() {
		SelfDefCompContext ctx = new SelfDefCompContext();
		ctx.setId(this.getId());
		ctx.setVisible(this.visible);
		ctx.setOtherCtx(this.otherCtx);
		return ctx;
	}
	
	@Override
	public void setContext(BaseContext ctx) {
		SelfDefCompContext sdCtx = (SelfDefCompContext) ctx;
		this.setVisible(sdCtx.isVisible());
		this.setOtherCtx(sdCtx.getOtherCtx());
		if (sdCtx.getTriggerId() != null)
			this.setExtendAttribute(TRIGGER_ID, sdCtx.getTriggerId());
		this.setCtxChanged(false);
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		if (visible != this.visible) {
			this.visible = visible;
			setCtxChanged(true);
		}
	}

	public Serializable getOtherCtx() {
		return otherCtx;
	}

	public void setOtherCtx(Serializable otherCtx) {
		if (otherCtx != this.otherCtx) {
			this.otherCtx = otherCtx;
			setCtxChanged(true);
		}
	}

	@Override
	public ILuiRender getRender() {
		if(render==null){
			render = RenderProxy.getRender(new PCSelfDefCompRender(this));
		}
		return render;
	}
	
	

}
