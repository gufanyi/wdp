package xap.lui.core.layout;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import xap.lui.core.render.ILuiRender;
import xap.lui.core.render.PCDividCenterPanelRender;
import xap.lui.core.render.PCDividLayoutRender;
import xap.lui.core.render.notify.RenderProxy;


@XmlRootElement(name="DividCenter")
@XmlAccessorType(XmlAccessType.NONE)
public class UIDividCenter extends UILayoutPanel {
	private static final long serialVersionUID = 6565082173585143132L;

	@Override
	public ILuiRender getRender() {
		if(render==null){
			PCDividLayoutRender parentRender=(PCDividLayoutRender)this.getLayout().getRender();
			render = RenderProxy.getRender(new PCDividCenterPanelRender(this,parentRender));
		}
		return render;
	}
	
}
