package xap.lui.core.layout;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.render.PCFlowvPanelRender;
import xap.lui.core.render.notify.RenderProxy;

@XmlRootElement(name = "FlowVPanel")
@XmlAccessorType(XmlAccessType.NONE)
public class UIFlowvPanel extends UILayoutPanel {
	private static final long serialVersionUID = -4186440303901937178L;
	public static final String HEIGHT = "height";
	public static final String ANCHOR = "anchor";

	// public static final String AUTO_FILL = "autoFill";
	private String height;
	private String anchor;

	public String getHeight() {
		return (String) height;
	}

	public void setHeight(String height) {
		String oriHeight = getHeight();
		if (oriHeight == null || !oriHeight.equals(height)) {
			this.height = height;
			if (LifeCyclePhase.ajax.equals(getPhase())) {
				this.getRender().setHeight(height);
			}
		}
		this.height = height;
	}

	public String getAnchor() {
		return (String) anchor;
	}

	public void setAnchor(String anchor) {
		this.anchor = anchor;
	}

	@Override
	public PCFlowvPanelRender getRender() {
		if (render == null) {
			render = RenderProxy.getRender(new PCFlowvPanelRender(this));
		}
		return (PCFlowvPanelRender) render;
	}

}
