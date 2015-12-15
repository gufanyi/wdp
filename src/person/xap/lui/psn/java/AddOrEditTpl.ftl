package lui.ctrl;
import xap.lui.core.command.CmdInvoker;
import xap.lui.psn.cmd.LuiAddOrEditMenuClickCmd;
import xap.lui.core.event.MouseEvent;
public class MethodTemplate {



<#if TabID?exists>
   public void LuiAddOrEditMenuClickCmd() {
   		String tabId= "${TabID}";
		String dsId = "${OperatorDs}";
		String operType= "${OperType}";
		String editViewId= "${EditViewId}";
		CmdInvoker.invoke(new LuiAddOrEditMenuClickCmd(tabId,dsId,operType,editViewId));
	}
 <#else>	
   public void LuiAddOrEditMenuClickCmd() {
		String dsId = "${OperatorDs}";
		String operType= "${OperType}";
		String editViewId= "${EditViewId}";
		CmdInvoker.invoke(new LuiAddOrEditMenuClickCmd(dsId,operType,editViewId));
	}
 </#if>	
}
