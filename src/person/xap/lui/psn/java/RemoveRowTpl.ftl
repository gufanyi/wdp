package lui.ctrl;

import xap.lui.core.event.MouseEvent;
import xap.lui.core.command.CmdInvoker;
import xap.lui.psn.cmd.LuiRemoveRowCmd;

public class MethodTemplate {

    <#if TabID?exists>
    public void LuiRemoveRowCmd() {
		String tabID = "${TabID}";
		String ds1 = "${ItemDS1}";
		String ds2 = "${ItemDS2}";
		CmdInvoker.invoke(new LuiRemoveRowCmd(tabID,ds1,ds2,""));
	}
    <#else>	
	public void LuiRemoveRowCmd() {
		String dsId ="${OperatorDs}";
		CmdInvoker.invoke(new LuiRemoveRowCmd(dsId));
	}
    </#if>	
}
