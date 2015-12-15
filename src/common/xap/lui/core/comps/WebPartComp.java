package xap.lui.core.comps;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import xap.lui.core.context.WebPartContext;
import xap.lui.core.render.PCPartCompRender;
import xap.lui.core.render.notify.RenderProxy;
import xap.lui.core.util.JsURLEncoder;
@XmlRootElement(name="HtmlContent")
@XmlAccessorType(XmlAccessType.NONE)
public class WebPartComp extends WebComp {
	private static final long serialVersionUID = 6341974963624217504L;
	@XmlAttribute
	private String contentFetcher;//提取的内容
	@XmlAttribute
	private String innerHTML;//HTML当前标签的起始和结束里面的内容
	private PCPartCompRender render;
	
	public String getContentFetcher() {
		return contentFetcher;
	}
	public void setContentFetcher(String contentFetcher) {
		this.contentFetcher = contentFetcher;
	}
	 
	public String getInnerHTML() {
		return innerHTML;
	}
	public void setInnerHTML(String innerHTML) {
		this.innerHTML = innerHTML;
		
		setCtxChanged(true);
	}
	public void setContext(WebPartContext context){
	}
	public WebPartContext getContext() {
		WebPartContext ctx = new WebPartContext();
		ctx.setId(this.getId());
		ctx.setInnerHTML(JsURLEncoder.encode(this.getInnerHTML(),"UTF-8"));
		return ctx;
	}
	
	public PCPartCompRender getRender() {
		if(this.render == null) {
			this.render = RenderProxy.getRender(new PCPartCompRender(this));
		}
		return this.render;
	}
	
}
