package lui.ctrl;
import xap.lui.core.command.CmdInvoker;
import xap.lui.psn.cmd.LuiAddOrEditBeforeShowCmd;
import xap.lui.core.event.DatasetEvent;
public class MethodTemplate {

   public void LuiAddOrEditBeforeShowCmd() {
		String dsId = "${OperatorDs}";
		CmdInvoker.invoke(new LuiAddOrEditBeforeShowCmd(dsId));
	}
}
