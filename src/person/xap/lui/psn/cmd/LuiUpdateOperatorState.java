
package xap.lui.psn.cmd;

import xap.lui.core.command.LuiCommand;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.model.ViewPartMeta;


public class LuiUpdateOperatorState extends LuiCommand {

	private Dataset ds;
	
	private ViewPartMeta widget;
	
	public LuiUpdateOperatorState(Dataset ds, ViewPartMeta widget){
		this.ds = ds;
		this.widget = widget;
	}
	
	@Override
	public void execute() {
	}

}
