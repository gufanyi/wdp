package lui.ctrl;

import xap.lui.core.event.MouseEvent;
import xap.lui.core.command.CmdInvoker;
import xap.lui.psn.cmd.LuiEditRowCmd;
import xap.lui.core.comps.ToolBarComp;
import xap.lui.core.model.LuiAppUtil;

public class MethodTemplate {

	<#if TabID?exists  && MenuBarCompId?exists>
	    public void LuiEditRowCmd() {
			String tabID = "${TabID}";
			String ds1 = "${ItemDS1}";
			String ds2 = "${ItemDS2}";
			String menuBarCompId = "${MenuBarCompId}";
			CmdInvoker.invoke(new LuiEditRowCmd(tabID,ds1,ds2,"",menuBarCompId));
		}
	<#elseif TabID?exists && ToolBarCompId?exists>
	    public void LuiEditRowCmd() {
			String tabID = "${TabID}";
			String ds1 = "${ItemDS1}";
			String ds2 = "${ItemDS2}";
			String toolBarCompId = "${ToolBarCompId}";
			ToolBarComp toolBarComp= (ToolBarComp)LuiAppUtil.getCntView().getViewComponents().getComponent(toolBarCompId);
			CmdInvoker.invoke(new LuiEditRowCmd(tabID,ds1,ds2,"",toolBarComp));
		}
    <#elseif TabID?exists>
	    public void LuiEditRowCmd() {
			String tabID = "${TabID}";
			String ds1 = "${ItemDS1}";
			String ds2 = "${ItemDS2}";
			CmdInvoker.invoke(new LuiEditRowCmd(tabID,ds1,ds2,""));
		}
    <#elseif OperatorDs?exists && ToolBarCompId?exists>	
		public void LuiEditRowCmd() {
			String dsId ="${OperatorDs}";
			String toolBarCompId = "${ToolBarCompId}";
			ToolBarComp toolBarComp= (ToolBarComp)LuiAppUtil.getCntView().getViewComponents().getComponent(toolBarCompId);
			CmdInvoker.invoke(new LuiEditRowCmd(dsId,toolBarComp));
		}
    <#else>	
		public void LuiEditRowCmd() {
			String dsId ="${OperatorDs}";
			CmdInvoker.invoke(new LuiEditRowCmd(dsId));
		}
    </#if>	
}
