package lui.ctrl;
import xap.lui.core.command.CmdInvoker;
import xap.lui.psn.cmd.LuiOpenWindowCmd;
public class MethodTemplate {

	public void LuiOpenWindowCmd() {
		String viewId ="${viewId}";
			String width ="${width}";
			String height ="${height}";
			String title ="${title}";
			CmdInvoker.invoke(new LuiOpenWindowCmd(viewId, width, height, title));
	}
}
