package xap.lui.core.layout;

import xap.lui.core.render.ILuiRender;
import xap.lui.core.render.PCMenuGroupRender;
import xap.lui.core.render.notify.RenderProxy;


public class UIMenuGroup extends UILayout {
	private static final long serialVersionUID = -2462390247359218359L;
	private String id;
	public String getId(){
		return id;
	}
	
	public void setId(String value){
		this.id=value;
	}

	@Override
	public ILuiRender getRender() {
		if(render==null){
			render = RenderProxy.getRender(new PCMenuGroupRender(this));
		}
		return render;
	}
	
	
}
