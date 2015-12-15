package lui.ctrl;
import xap.lui.core.command.CmdInvoker;
import xap.lui.psn.cmd.LuiWindowDestroyCmd;
public class MethodTemplate {

	public void LuiWindowDestroyCmd() {
		CmdInvoker.invoke(new LuiWindowDestroyCmd());
	}
}
