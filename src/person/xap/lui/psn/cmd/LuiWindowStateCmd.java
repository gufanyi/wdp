package xap.lui.psn.cmd;

import xap.lui.core.command.LuiCommand;


public class LuiWindowStateCmd extends LuiCommand {
	private Boolean hasChanged;

	public LuiWindowStateCmd(Boolean hasChanged) {
		this.hasChanged = hasChanged;
	}

	@Override
	public void execute() {
		StringBuffer buf = new StringBuffer("pageUI.setChanged(");
		buf.append(this.hasChanged);
		buf.append(");\n");
		String script = buf.toString();
		getLifeCycleContext().getAppContext().addExecScript(script);
	}

}
