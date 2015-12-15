package xap.lui.core.layout;

import xap.lui.core.render.ILuiRender;
import xap.lui.core.render.PCPanelLayoutRender;
import xap.lui.core.render.PCPanelPanelRender;
import xap.lui.core.render.notify.RenderProxy;


public class UIPanelPanel extends UILayoutPanel {
	private static final long serialVersionUID = 3063729814443707258L;
	public UIPanelPanel() {
		super();
		setTopPadding("20");
		setBottomPadding("10");
	}
	@Override
	public ILuiRender getRender() {
		if(render==null){
			PCPanelLayoutRender parentRender=(PCPanelLayoutRender)this.getLayout().getRender();
			render = RenderProxy.getRender(new PCPanelPanelRender(this,parentRender));
		}
		return render;
	}
	
}
