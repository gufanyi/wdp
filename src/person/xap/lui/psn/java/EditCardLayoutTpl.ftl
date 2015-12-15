package lui.ctrl;

import xap.lui.core.command.CmdInvoker;
import xap.lui.psn.cmd.LuiEditCardLayoutCmd;
import xap.lui.core.event.MouseEvent;
import xap.lui.core.comps.ToolBarComp;
import xap.lui.core.model.LuiAppUtil;
public class MethodTemplate {


<#if ToolBarCompId?exists>
   public void LuiEditCardLayoutCmd() {
		String dsId = "${OperatorDs}";
		String cardLayoutId = "${CardLayoutId}";
		String toolBarCompId = "${ToolBarCompId}";
		ToolBarComp toolBarComp= (ToolBarComp)LuiAppUtil.getCntView().getViewComponents().getComponent(toolBarCompId);
		CmdInvoker.invoke(new LuiEditCardLayoutCmd(dsId,cardLayoutId,toolBarComp));
	}
<#else>
	public void LuiEditCardLayoutCmd() {
		String dsId = "${OperatorDs}";
		String cardLayoutId = "${CardLayoutId}";
		CmdInvoker.invoke(new LuiEditCardLayoutCmd(dsId,cardLayoutId));
	}
</#if>	
	
	
}
