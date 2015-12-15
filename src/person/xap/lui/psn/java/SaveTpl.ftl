package lui.ctrl;
import xap.lui.core.command.CmdInvoker;
import xap.lui.psn.cmd.LuiSaveCmd;
import xap.lui.core.event.MouseEvent;
public class MethodTemplate {

 <#if isEditView?exists && parentViewId?exists && parentDsId?exists >	
	public void LuiSaveCmd() {
		Boolean isEditView=${isEditView};
		String parentViewId ="${parentViewId}";
		String parentDsId ="${parentDsId}";
		CmdInvoker.invoke(new LuiSaveCmd(isEditView,parentViewId,parentDsId));
	}
<#else>	
	  public void LuiSaveCmd() {
		String dsId ="${OperatorDs}";
		CmdInvoker.invoke(new LuiSaveCmd(dsId));
	}
  </#if>	
	  
}
