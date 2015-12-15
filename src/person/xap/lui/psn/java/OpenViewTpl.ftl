package lui.ctrl;
import xap.lui.core.command.CmdInvoker;
import xap.lui.psn.cmd.LuiOpenViewCmd;
public class MethodTemplate {

	public void LuiOpenViewCmd() {
		String viewId ="${viewId}";
			String width ="${width}";
			String height ="${height}";
			String title ="${title}";
			boolean isclose ="${isclose}";
			CmdInvoker.invoke(new LuiOpenViewCmd(viewId, width, height, title, isclose));
	}
}
