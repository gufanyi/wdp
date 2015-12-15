package lui.ctrl;

import xap.lui.core.command.CmdInvoker;
import xap.lui.psn.cmd.LuiDatasetLoadCmd;
import xap.lui.core.event.DatasetEvent;

public class MethodTemplate {

	<#if ToolBarStatus?exists>
		public void LuiDatasetLoadCmd(){
				String dsId = "${OperatorDs}";
				boolean toolBarStatus = ${ToolBarStatus};
			  	CmdInvoker.invoke(new LuiDatasetLoadCmd(dsId,toolBarStatus));
			}
	<#else>
		public void LuiDatasetLoadCmd(){
			String dsId = "${OperatorDs}";
		  	CmdInvoker.invoke(new LuiDatasetLoadCmd(dsId));
		}
	</#if>
}
