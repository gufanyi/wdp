package lui.ctrl;
import xap.lui.core.command.CmdInvoker;
import xap.lui.core.event.MouseEvent;
import xap.lui.psn.cmd.LuiDelBaseDOCmd;
import xap.lui.core.comps.ToolBarComp;
import xap.lui.core.model.LuiAppUtil;
public class MethodTemplate {

<#if ToolBarCompId?exists>
	public void LuiDelBaseDOCmd() {
		String masterDsId = "${OperatorDs}";
		String toolBarCompId = "${ToolBarCompId}";
		ToolBarComp toolBarComp= (ToolBarComp)LuiAppUtil.getCntView().getViewComponents().getComponent(toolBarCompId);
		CmdInvoker.invoke(new LuiDelBaseDOCmd(masterDsId,toolBarComp));
	}
<#else>
	public void LuiDelBaseDOCmd() {
		String masterDsId = "${OperatorDs}";
		CmdInvoker.invoke(new LuiDelBaseDOCmd(masterDsId));
	}
</#if>
}
