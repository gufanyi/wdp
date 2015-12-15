package xap.lui.core.layout;

import xap.lui.core.render.ILuiRender;
import xap.lui.core.render.PCGridRowPanelRender;
import xap.lui.core.render.notify.RenderProxy;


public class UIGridRowPanel extends UILayoutPanel {
	private static final long serialVersionUID = 2047434545903659116L;
	protected UIGridRowLayout row;
	
	public UIGridRowPanel(UIGridRowLayout row){
		setRow(row);
	}
	
	public UIGridRowLayout getRow() {
		return (UIGridRowLayout) getElement();
	}
	
	public void setRow(UIGridRowLayout row) {
		setElement(row);
	}

	@Override
	public ILuiRender getRender() {
		if(render==null){
			render = RenderProxy.getRender(new PCGridRowPanelRender(this));
		}
		return render;
	}
	
	
}
