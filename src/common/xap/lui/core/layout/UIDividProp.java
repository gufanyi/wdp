package xap.lui.core.layout;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import xap.lui.core.render.ILuiRender;
import xap.lui.core.render.PCDividLayoutRender;
import xap.lui.core.render.PCDividPropPanelRender;
import xap.lui.core.render.notify.RenderProxy;

@XmlRootElement(name="DividProp")
@XmlAccessorType(XmlAccessType.NONE)
public class UIDividProp extends UIDividCenter {
	private static final long serialVersionUID = 8718714988877077211L;
	
	@Override
	public ILuiRender getRender() {
		if(render==null){
			PCDividLayoutRender parentRender=(PCDividLayoutRender)this.getLayout().getRender();
			render = RenderProxy.getRender(new PCDividPropPanelRender(this,parentRender));
		}
		return render;
	}
}
