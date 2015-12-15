package lui.ctrl;
import xap.lui.core.command.CmdInvoker;
import xap.lui.psn.cmd.LuiEditOkClickCmd;
public class MethodTemplate {

	public void LuiEditOkClickCmd() {
		String dsId ="${dsId}";
		String parentViewId = "${parentViewId}";
		String parentDsId = "${parentDsId}";
		CmdInvoker.invoke(new LuiEditOkClickCmd(dsId, parentViewId, parentDsId));
	}
}
