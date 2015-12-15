package lui.ctrl;
import xap.lui.core.event.MouseEvent;
import xap.lui.core.command.CmdInvoker;
import xap.lui.psn.cmd.LuiDelAggDOCmd;
import xap.lui.core.comps.ToolBarComp;
import xap.lui.core.model.LuiAppUtil;
public class MethodTemplate {

<#if ToolBarCompId?exists>
	public void LuiDelAggDOCmd() {
		String masterDsId = "${masterDsId}";
		String aggVoClazz = "${aggVoClazz}";
		String toolBarCompId = "${ToolBarCompId}";
		ToolBarComp toolBarComp= (ToolBarComp)LuiAppUtil.getCntView().getViewComponents().getComponent(toolBarCompId);
		CmdInvoker.invoke(new LuiDelAggDOCmd(masterDsId,aggVoClazz,toolBarComp));
	}
<#else>
	public void LuiDelAggDOCmd() {
		String masterDsId = "${masterDsId}";
		String aggVoClazz = "${aggVoClazz}";
		CmdInvoker.invoke(new LuiDelAggDOCmd(masterDsId,aggVoClazz));
	}
</#if>
}
