package xap.lui.core.layout;

import xap.lui.core.render.ILuiRender;
import xap.lui.core.render.PCCardLayoutRender;
import xap.lui.core.render.PCCardPanelRender;
import xap.lui.core.render.notify.RenderProxy;

public class UICardPanel extends UILayoutPanel {
	private static final long serialVersionUID = 4116930870463526713L;
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public ILuiRender getRender() {

		if (render == null) {
			PCCardLayoutRender parentRender = (PCCardLayoutRender) this.getLayout().getRender();
			render = RenderProxy.getRender(new PCCardPanelRender(this,parentRender));
		}
		return render;
	}

}
