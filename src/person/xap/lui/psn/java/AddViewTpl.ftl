package lui.ctrl;
import xap.lui.core.command.CmdInvoker;
import xap.lui.psn.cmd.LuiAddViewCmd;
import xap.lui.core.event.MouseEvent;
public class MethodTemplate {

    public void LuiAddViewCmd() {
		String viewId ="${viewId}";
		String width ="${width}";
		String height ="${height}";
		String title ="${title}";
		CmdInvoker.invoke(new LuiAddViewCmd(viewId, width,height,title));
	}
	
}
