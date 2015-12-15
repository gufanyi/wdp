package xap.lui.psn.designer;

import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.event.PageEvent;

public class DesignerWindowCtrl  {
	
	public void sysWindowClosed(PageEvent event){
	    LuiRuntimeContext.getWebContext().destroyWebSession();
	  }

}
