package xap.lui.core.layout;

import xap.lui.core.comps.ToolBarComp;
import xap.lui.core.render.PCToolbarCompRender;
import xap.lui.core.render.notify.RenderProxy;


public class UIToolBar extends UIComponent {
	private static final long serialVersionUID = 1656422141563952123L;
	
	public PCToolbarCompRender getRender() {
		if(render == null) {
			render = RenderProxy.getRender(new PCToolbarCompRender((ToolBarComp)this.getWebComp()));
		}
		return (PCToolbarCompRender)render;
	}
}
