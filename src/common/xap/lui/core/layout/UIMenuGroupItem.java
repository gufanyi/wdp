package xap.lui.core.layout;

import java.io.Serializable;

import xap.lui.core.render.ILuiRender;
import xap.lui.core.render.PCMenuGroupItemRender;
import xap.lui.core.render.PCMenuGroupRender;
import xap.lui.core.render.notify.RenderProxy;

public class UIMenuGroupItem extends UILayoutPanel {
	private static final long serialVersionUID = 9211264854706138210L;
	public static final String STATE = "state";
	private Integer state;

	public Integer getState() {
		return state;
	}

	public void setState(Integer value) {
		this.state = state;
	}

	@Override
	public Serializable getAttribute(String key) {
		Serializable obj = super.getAttribute(key);
		if (key.equals(STATE)) {
			if (obj == null) {
				Integer i = Integer.valueOf(-1);
				return i;
			}
		}
		return obj;
	}

	@Override
	public ILuiRender getRender() {
		if (render == null) {
			PCMenuGroupRender parentRender = (PCMenuGroupRender) this
					.getLayout().getRender();
			render = RenderProxy.getRender(new PCMenuGroupItemRender(this,
					parentRender));
		}
		return render;
	}

}
