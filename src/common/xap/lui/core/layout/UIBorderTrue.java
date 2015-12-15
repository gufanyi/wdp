package xap.lui.core.layout;

import xap.lui.core.render.ILuiRender;
import xap.lui.core.render.PCBorderTrueRender;
import xap.lui.core.render.notify.RenderProxy;

public class UIBorderTrue extends UILayoutPanel {
	private static final long serialVersionUID = -31627637123770387L;
	private PCBorderTrueRender render = null;

	@Override
	public ILuiRender getRender() {
		if (render == null) {
			render = RenderProxy.getRender(new PCBorderTrueRender(this));
		}
		return render;
	}

}
