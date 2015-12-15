package lui.ctrl;
import xap.lui.core.command.CmdInvoker;
import xap.lui.psn.cmd.LuiAddRowCmd;
import xap.lui.core.event.MouseEvent;
import xap.lui.core.comps.ToolBarComp;
import xap.lui.core.model.LuiAppUtil;
public class MethodTemplate {

  <#if OperatorDs?exists && NavDsId?exists>
	   public void LuiAddRowCmd() {
			String dsId = "${OperatorDs}";
			String navDatasetId = "${NavDsId}";
			CmdInvoker.invoke(new LuiAddRowCmd(dsId, navDatasetId));
		}
  <#elseif TabID?exists  && MenuBarCompId?exists>
	   public void LuiAddRowCmd() {
			String tabID = "${TabID}";
			String ds1 = "${ItemDS1}";
			String ds2 = "${ItemDS2}";
			String menuBarCompId = "${MenuBarCompId}";
			CmdInvoker.invoke(new LuiAddRowCmd(tabID,ds1,ds2,"",menuBarCompId));
		}
  <#elseif TabID?exists && ToolBarCompId?exists>
	   public void LuiAddRowCmd() {
			String tabID = "${TabID}";
			String ds1 = "${ItemDS1}";
			String ds2 = "${ItemDS2}";
			String toolBarCompId = "${ToolBarCompId}";
			ToolBarComp toolBarComp= (ToolBarComp)LuiAppUtil.getCntView().getViewComponents().getComponent(toolBarCompId);
			CmdInvoker.invoke(new LuiAddRowCmd(tabID,ds1,ds2,"",toolBarComp));
		}
   <#elseif TabID?exists>
	   public void LuiAddRowCmd() {
			String tabID = "${TabID}";
			String ds1 = "${ItemDS1}";
			String ds2 = "${ItemDS2}";
			CmdInvoker.invoke(new LuiAddRowCmd(tabID,ds1,ds2,""));
		}
   <#elseif OperatorDs?exists && ToolBarCompId?exists>	
		public void LuiAddRowCmd() {
			String dsId = "${OperatorDs}";
			String toolBarCompId = "${ToolBarCompId}";
			ToolBarComp toolBarComp= (ToolBarComp)LuiAppUtil.getCntView().getViewComponents().getComponent(toolBarCompId);
			CmdInvoker.invoke(new LuiAddRowCmd(dsId,toolBarComp));
		}
	<#else>	
		public void LuiAddRowCmd() {
			String dsId = "${OperatorDs}";
			CmdInvoker.invoke(new LuiAddRowCmd(dsId));
		}
    </#if>	
    
    
    
    
}
