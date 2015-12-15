package xap.lui.core.control;

import java.io.Serializable;
import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.event.PageEvent;


public class DftWindowCtrl implements IWindowCtrl, Serializable {
	private static final long serialVersionUID = 7532916478964732880L;
	public void sysWindowClosed(PageEvent event){
		LuiRuntimeContext.getWebContext().destroyWebSession();
	}
}
