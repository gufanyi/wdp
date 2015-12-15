package xap.lui.psn.cmd;

import xap.lui.core.command.LuiCommand;
import xap.lui.core.model.AppSession;

public class LuiCloseViewCmd extends LuiCommand {
	private String viewId;
	public LuiCloseViewCmd(String viewId) {
		this.viewId = viewId;
	}

	@Override
	public void execute() {
		AppSession.current().getWindowContext().closeView(viewId);
	}

}
