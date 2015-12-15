package lui.ctrl;

import xap.lui.core.event.MouseEvent;
import xap.lui.core.command.CmdInvoker;
import xap.lui.psn.cmd.LuiEditViewCmd;
public class MethodTemplate {

		public void LuiEditViewCmd() {
			String viewId ="${viewId}";
			String width ="${width}";
			String height ="${height}";
			String title ="${title}";
			boolean isclose = ${isclose};
			CmdInvoker.invoke(new LuiEditViewCmd(viewId, width, height, title, isclose));
		}
}
