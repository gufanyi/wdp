package xap.lui.core.layout;

import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.render.PCFlowhPanelRender;
import xap.lui.core.render.notify.RenderProxy;

public class UIFlowhPanel extends UILayoutPanel {
	private static final long serialVersionUID = 3063729814443707258L;
	public static final String WIDTH = "width";

	private String width;
	public void setWidth(String width) {
		String oriWidth = getWidth();
		if (oriWidth == null || !oriWidth.equals(width)) {
			this.width=width;
			//UpdatePair pair = new UpdatePair(WIDTH, width);
			//notifyChange(UPDATE, pair);
			if(LifeCyclePhase.ajax.equals(getPhase())) {
				this.getRender().setWidth(Integer.parseInt(width));
			}
		}
	}

	public String getWidth() {
		return width;
	}

	@Override
	public PCFlowhPanelRender getRender() {
		if (render == null) {
			render = RenderProxy.getRender(new PCFlowhPanelRender(this));
		}
		return (PCFlowhPanelRender)render;
	}

}
