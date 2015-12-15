package xap.lui.core.comps;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.alibaba.fastjson.annotation.JSONField;

import xap.lui.core.context.BaseContext;
import xap.lui.core.context.IFrameContext;
import xap.lui.core.render.ILuiRender;
import xap.lui.core.render.PCIFrameRender;
import xap.lui.core.render.notify.RenderProxy;

/**
 * iframe控件
 * @author zhangxya
 *
 */
@XmlRootElement(name = "Iframe")
@XmlAccessorType(XmlAccessType.NONE)
public class IFrameComp extends WebComp{
	private static final long serialVersionUID = 1L;
	public static final String WIDGET_NAME = "iframe";
	@XmlAttribute
	private String src;
	@XmlAttribute
	private String name;
	@XmlAttribute
	private String border;
	@XmlAttribute
	private String frameBorder;
	@XmlAttribute
	private String scrolling;
	@XmlAttribute
	private boolean isVisible = true;
	
	@JSONField(serialize=false)
	public PCIFrameRender render=null;
	
	@Override
	public String getWidgetName() {
		return WIDGET_NAME;
	}
	public String getSrc() {
		return src;
	}
	public void setSrc(String src) {
		if (src != null && src != this.src) {
			this.src = src;
			setCtxChanged(true);
		}
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBorder() {
		return border;
	}
	public void setBorder(String border) {
		this.border = border;
	}
	public String getFrameBorder() {
		return frameBorder;
	}
	public void setFrameBorder(String frameBorder) {
		this.frameBorder = frameBorder;
	}
	public String getScrolling() {
		return scrolling;
	}
	public void setScrolling(String scrolling) {
		this.scrolling = scrolling;
	}
	public boolean isVisible() {
		return isVisible;
	}
	public void setVisible(boolean isVisible) {
		if (this.isVisible != isVisible) {
			this.isVisible = isVisible;
			setCtxChanged(true);
		}
	}
	
	@Override
	public BaseContext getContext() {
		IFrameContext ctx = new IFrameContext();
		ctx.setSrc(this.getSrc());
		ctx.setVisible(this.isVisible());
		return ctx;
	}
	
	@Override
	public void setContext(BaseContext ctx) {
		IFrameContext iframeCtx = (IFrameContext) ctx;
		this.setSrc(iframeCtx.getSrc());
		this.setVisible(iframeCtx.isVisible());
		this.setCtxChanged(false);
	}
	@Override
	public ILuiRender getRender() {
		if(render==null){
			render=RenderProxy.getRender(new PCIFrameRender(this));
		}
		return render;
	}
	
	

}
