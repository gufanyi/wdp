package xap.lui.psn.cmd;

import xap.lui.core.command.LuiCommand;
import xap.lui.core.common.LuiRuntimeContext;

public class LuiWindowDestroyCmd extends LuiCommand {

	
	@Override
	public void execute() {
		LuiRuntimeContext.getWebContext().destroyWebSession();
	}

}
