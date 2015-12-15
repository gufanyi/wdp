package xap.lui.core.command;

import xap.lui.core.model.AppSession;
import xap.lui.core.state.ButtonStateManager;

public abstract class LuiCommand implements ICommand {
	protected AppSession getLifeCycleContext() {
		return AppSession.current();
	}
	
	protected void updateButtons() {
		ButtonStateManager.updateButtons();
	}
}
