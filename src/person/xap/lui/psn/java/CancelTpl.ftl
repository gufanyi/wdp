package lui.ctrl;
import xap.lui.core.command.CmdInvoker;
import xap.lui.psn.cmd.LuiCancelCmd;
import xap.lui.core.event.MouseEvent;
public class MethodTemplate {

	public void LuiCancelCmd() {
		String dsId ="${dsId}";
		CmdInvoker.invoke(new LuiCancelCmd(dsId));
	}
}
