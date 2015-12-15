package xap.lui.core.layout;

import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.render.PCTabLayoutRender;
import xap.lui.core.render.PCTabRightRender;
import xap.lui.core.render.notify.RenderProxy;

public class UITabRightPanel extends UILayoutPanel {
	private static final long serialVersionUID = 1L;

	public static final String WIDTH = "width";
	private String width;

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		String oriWidth = this.getWidth();
		if (oriWidth == null || !oriWidth.equals(width)) {
			this.width = width;
			if (LifeCyclePhase.ajax.equals(getPhase())) {
				this.getRender().setTabRightPanelWidth(Integer.parseInt(width));
			}
		}
		this.width = width;
	}

	@Override
	public PCTabRightRender getRender() {
		if (render == null) {
			PCTabLayoutRender parentRender = (PCTabLayoutRender) this
					.getLayout().getRender();
			render = RenderProxy.getRender(new PCTabRightRender(this,
					parentRender));
		}
		return (PCTabRightRender) render;
	}

}
