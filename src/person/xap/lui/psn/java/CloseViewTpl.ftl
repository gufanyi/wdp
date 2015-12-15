package lui.ctrl;
import xap.lui.core.command.CmdInvoker;
import xap.lui.psn.cmd.LuiCloseViewCmd;
import xap.lui.core.event.MouseEvent;
public class MethodTemplate {

	public void LuiCloseViewCmd() {
		String viewId = "${viewId}";
		CmdInvoker.invoke(new LuiCloseViewCmd(viewId));
	}
}
