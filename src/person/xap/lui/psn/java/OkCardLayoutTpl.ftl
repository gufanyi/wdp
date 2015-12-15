package lui.ctrl;
import xap.lui.core.command.CmdInvoker;
import xap.lui.psn.cmd.LuiOkCardLayoutCmd;
import xap.lui.core.event.MouseEvent;
import xap.lui.core.model.LuiAppUtil;
public class MethodTemplate{

	<#if MenuBarCompId?exists>
		public void LuiOkCardLayoutCmd() {
			String dsId ="${OperatorDs}";
			String cardLayoutId ="${CardLayoutIds}";
			String tabId="${TabID}";
			String menuBarCompId="${MenuBarCompId}";
			CmdInvoker.invoke(new LuiOkCardLayoutCmd(dsId,cardLayoutId,tabId,menuBarCompId));
		}
	<#else>
		public void LuiOkCardLayoutCmd() {
			String dsId ="${OperatorDs}";
			String cardLayoutId ="${CardLayoutIds}";
			String tabId="${TabID}";
			CmdInvoker.invoke(new LuiOkCardLayoutCmd(dsId,cardLayoutId,tabId));
		}
	</#if>

}
