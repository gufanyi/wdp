package xap.lui.core.layout;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import xap.lui.core.render.ILuiRender;
import xap.lui.core.render.PCSpliterLayoutRender;
import xap.lui.core.render.PCSpliterOnePanelRender;
import xap.lui.core.render.notify.RenderProxy;

@XmlRootElement(name="SplitterOne")
@XmlAccessorType(XmlAccessType.NONE)
public class UISplitterOne extends UILayoutPanel {
	private static final long serialVersionUID = 8718714988877077211L;

	@Override
	public ILuiRender getRender() {
		if(render==null){
			PCSpliterLayoutRender parentRender=(PCSpliterLayoutRender)this.getLayout().getRender();
			render = RenderProxy.getRender(new PCSpliterOnePanelRender(this,parentRender));
		}
		return render;
	}
	
	
}
