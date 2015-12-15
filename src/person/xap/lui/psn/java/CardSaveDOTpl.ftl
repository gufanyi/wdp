package lui.ctrl;
import xap.lui.core.command.CmdInvoker;
import xap.lui.psn.cmd.LuiCardSaveDOCmd;
import xap.lui.core.event.MouseEvent;
import xap.lui.core.comps.ToolBarComp;
import xap.lui.core.model.LuiAppUtil;
public class MethodTemplate {

   <#if OperatorDs?exists && NavDsId?exists &&CardLayoutId?exists>
	   public void LuiCardSaveDOCmd() {
			String dsId = "${OperatorDs}";
			String cardLayoutId = "${CardLayoutId}";
			String navDsId = "${NavDsId}";
			CmdInvoker.invoke(new LuiCardSaveDOCmd(dsId,cardLayoutId,navDsId));
		}
   <#elseif	ToolBarCompId?exists>	
		public void LuiCardSaveDOCmd() {
			String dsId = "${OperatorDs}";
			String cardLayoutId = "${CardLayoutId}";
			String toolBarCompId = "${ToolBarCompId}";
			ToolBarComp toolBarComp= (ToolBarComp)LuiAppUtil.getCntView().getViewComponents().getComponent(toolBarCompId);
			CmdInvoker.invoke(new LuiCardSaveDOCmd(dsId,cardLayoutId,toolBarComp));
		}
   <#else>	
		public void LuiCardSaveDOCmd() {
			String dsId = "${OperatorDs}";
			String cardLayoutId = "${CardLayoutId}";
			CmdInvoker.invoke(new LuiCardSaveDOCmd(dsId,cardLayoutId));
		}
    </#if>	
}
