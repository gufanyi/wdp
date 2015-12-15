/**
 * 
 */
package xap.lui.core.layout;

import xap.lui.core.render.ILuiRender;
import xap.lui.core.render.PCPanelRightRender;
import xap.lui.core.render.notify.RenderProxy;

/**
 * @author chouhl
 *
 */
public class UIPanelRightPanel extends UILayoutPanel {

	private static final long serialVersionUID = 1L;

	public static final String WIDTH = "width";
	private String width;

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	@Override
	public ILuiRender getRender() {
		if (render == null) {
			render = RenderProxy.getRender(new PCPanelRightRender(this));
		}
		return render;
	}

}
