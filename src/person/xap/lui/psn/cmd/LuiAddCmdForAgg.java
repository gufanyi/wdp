package xap.lui.psn.cmd;

import xap.lui.core.command.CmdInvoker;
import xap.lui.core.command.LuiCommand;


public class LuiAddCmdForAgg extends LuiCommand{

	private String dsId;
	
	public LuiAddCmdForAgg(String dsId){
		this.dsId = dsId;
	}
	public LuiAddCmdForAgg(String dsId, String navDatasetId, String navStr){
		this.dsId = dsId;
	}
	
	@Override
	public void execute() {
		CmdInvoker.invoke(new LuiAddRowCmd(dsId));
		CmdInvoker.invoke(new LuiDatasetAfterSelectCmd(dsId));
	}

}
