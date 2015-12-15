package lui.ctrl;

import xap.lui.core.event.MouseEvent;
import xap.lui.core.command.CmdInvoker;
import xap.lui.psn.cmd.LuiDelTreeCmd;

public class MethodTemplate {


  <#if aggVoClazz?exists>
	public void LuiDelTreeCmd() {
		String aggVoClazz="${aggVoClazz}";
		String masterDsId="${masterDsId}";
		String treeDsId ="${treeDsId}";
		CmdInvoker.invoke(new LuiDelTreeCmd(treeDsId,masterDsId,aggVoClazz));
	}
 <#else>
	public void LuiDelTreeCmd() {
		String treeDsId ="${treeDsId}";
		CmdInvoker.invoke(new LuiDelTreeCmd(treeDsId));
	}
 </#if>
 }
