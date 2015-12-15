package xap.lui.core.layout;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import xap.lui.core.render.ILuiRender;
import xap.lui.core.render.PCSpliterLayoutRender;
import xap.lui.core.render.PCSpliterTwoPanelRender;
import xap.lui.core.render.notify.RenderProxy;


@XmlRootElement(name="SplitterTwo")
@XmlAccessorType(XmlAccessType.NONE)
public class UISplitterTwo extends UISplitterOne {
	private static final long serialVersionUID = 6565082173585143132L;
	

	@Override
	public ILuiRender getRender() {
		if(render==null){
			PCSpliterLayoutRender parentRender=(PCSpliterLayoutRender)this.getLayout().getRender();
			render = RenderProxy.getRender(new PCSpliterTwoPanelRender(this,parentRender));
		}
		return render;
	}
	

}
