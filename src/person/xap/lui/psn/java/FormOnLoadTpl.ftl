package lui.ctrl;
import xap.lui.psn.cmd.LuiFormLoadCmd;
import xap.lui.core.command.CmdInvoker;
import xap.lui.core.event.DatasetEvent;
public class MethodTemplate {
	public void LuiFormLoadCmd() {
		String operDsId="${OperatorDs}";
		CmdInvoker.invoke(new LuiFormLoadCmd(operDsId));
	}
}