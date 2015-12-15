package lui.ctrl;
import xap.lui.core.command.CmdInvoker;
import xap.lui.psn.cmd.LuiDelTabBaseDOCmd;
import xap.lui.core.event.MouseEvent;
public class MethodTemplate {

<#if MenuBarCompId?exists && ToolBarCompId?exists>
public void LuiDelTabBaseDOCmd() {
		String TabLayoutId = "${TabID}";
		String dsIds = "${OperatorDs}";
		String menuBarCompId= "${MenuBarCompId}";
		String toolBarCompId= "${ToolBarCompId}";
		CmdInvoker.invoke(new LuiDelTabBaseDOCmd(TabLayoutId,dsIds,menuBarCompId,toolBarCompId));
	}
<#elseif MenuBarCompId?exists>
	public void LuiDelTabBaseDOCmd() {
		String TabLayoutId = "${TabID}";
		String dsIds = "${OperatorDs}";
		String menuBarCompId= "${MenuBarCompId}";
		CmdInvoker.invoke(new LuiDelTabBaseDOCmd(TabLayoutId,dsIds,menuBarCompId));
	}
<#else>
	public void LuiDelTabBaseDOCmd() {
			String TabLayoutId = "${TabID}";
			String dsIds = "${OperatorDs}";
			CmdInvoker.invoke(new LuiDelTabBaseDOCmd(TabLayoutId,dsIds));
		}
</#if>


}