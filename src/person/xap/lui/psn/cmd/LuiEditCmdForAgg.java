package xap.lui.psn.cmd;

import xap.lui.core.command.CmdInvoker;
import xap.lui.core.command.LuiCommand;


public class LuiEditCmdForAgg extends LuiCommand {
	
	private String datasetId;
	private String billId;
	

	public LuiEditCmdForAgg(String dsId, String billId){
		this.datasetId = dsId;
		this.billId = billId;
	}
	
	public void execute() {
		CmdInvoker.invoke(new LuiDsLoadRowEnabledCmd(datasetId, billId));
		CmdInvoker.invoke(new LuiDatasetAfterSelectCmd(datasetId));
	}

}
