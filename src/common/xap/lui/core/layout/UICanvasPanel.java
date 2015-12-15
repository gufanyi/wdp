package xap.lui.core.layout;

import xap.lui.core.render.ILuiRender;
import xap.lui.core.render.PCCanvasLayoutRender;
import xap.lui.core.render.PCCanvasPanelRender;
import xap.lui.core.render.notify.RenderProxy;

public class UICanvasPanel extends UILayoutPanel {
	private static final long serialVersionUID = 3063729814443707258L;

	public UICanvasPanel() {
		super();
	}

	@Override
	public ILuiRender getRender() {
		if (render == null) {
			PCCanvasLayoutRender parentRender = (PCCanvasLayoutRender)this.getLayout().getRender();
			render = RenderProxy.getRender(new PCCanvasPanelRender(this,parentRender));
		}
		return render;
	}

}
