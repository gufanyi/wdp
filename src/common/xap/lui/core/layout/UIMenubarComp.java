package xap.lui.core.layout;

import xap.lui.core.comps.MenubarComp;
import xap.lui.core.render.PCMenubarCompRender;
import xap.lui.core.render.notify.RenderProxy;


public class UIMenubarComp extends UIComponent {
	private static final long serialVersionUID = -1827220590078901474L;
	public UIMenubarComp(){
	}
	public PCMenubarCompRender getRender() {
		if(render == null) {
			render = RenderProxy.getRender(new PCMenubarCompRender((MenubarComp)this.getWebComp()));
		}
		return (PCMenubarCompRender)render;
	}
	
}
