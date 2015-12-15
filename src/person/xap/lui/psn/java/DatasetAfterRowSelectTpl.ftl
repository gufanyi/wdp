package lfw.ctrl;

import xap.lui.core.command.CmdInvoker;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.psn.cmd.LuiDatasetAfterSelectCmd;

public class MethodTemplate {

	<#if OperatorDs?exists && widget?exists>
	public void LuiDatasetAfterSelectCmd(){
		String dsId = "${OperatorDs}";
		String widget = "${widget}";
	  	CmdInvoker.invoke(new LuiDatasetAfterSelectCmd(dsId,widget));
	}
	<#else>
	public void LuiDatasetAfterSelectCmd(){
		String dsId = "${OperatorDs}";
	  	CmdInvoker.invoke(new LuiDatasetAfterSelectCmd(dsId));
	}
	</#if>
}
