package lui.ctrl;
import xap.lui.core.command.CmdInvoker;
import xap.lui.psn.cmd.LuiCancelTabCardLayoutCmd;
import xap.lui.core.event.MouseEvent;
public class MethodTemplate {

<#if MenuBarCompId?exists && ToolBarCompId?exists>
	public void LuiCancelTabCardLayoutCmd() {
		String TabLayoutId = "${TabID}";
		String dsIds = "${OperatorDs}";
		String cardLayoutIds = "${CardLayoutIds}";
		String menuBarCompId="${MenuBarCompId}";
		String toolBarCompId= "${ToolBarCompId}";
		CmdInvoker.invoke(new LuiCancelTabCardLayoutCmd(TabLayoutId,dsIds,cardLayoutIds,menuBarCompId,toolBarCompId));
	}
<#elseif MenuBarCompId?exists>
	public void LuiCancelTabCardLayoutCmd() {
		String TabLayoutId = "${TabID}";
		String dsIds = "${OperatorDs}";
		String cardLayoutIds = "${CardLayoutIds}";
		String menuBarCompId="${MenuBarCompId}";
		CmdInvoker.invoke(new LuiCancelTabCardLayoutCmd(TabLayoutId,dsIds,cardLayoutIds,menuBarCompId));
	}
<#else>
	public void LuiCancelTabCardLayoutCmd() {
			String TabLayoutId = "${TabID}";
			String dsIds = "${OperatorDs}";
			String cardLayoutIds = "${CardLayoutIds}";
			CmdInvoker.invoke(new LuiCancelTabCardLayoutCmd(TabLayoutId,dsIds,cardLayoutIds));
		}
</#if>
	



}