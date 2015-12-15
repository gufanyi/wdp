package lui.ctrl;
import xap.lui.core.command.CmdInvoker;
import xap.lui.psn.cmd.LuiSavePopUpCmd;
import xap.lui.core.event.MouseEvent;
public class MethodTemplate {


 <#if masterDsId?exists && detailDsIds?exists && aggVoClazz?exists>
	public void LuiSavePopUpCmd(){
			String masterDsId = "${masterDsId}";
			String  detailDsString = "${detailDsIds}";
			String aggVoClazz = "${aggVoClazz}";
		  	CmdInvoker.invoke(new LuiSavePopUpCmd(masterDsId, detailDsString, aggVoClazz));
		}
  <#else>	
	public void LuiSavePopUpCmd() {
		String dsId ="${masterDsId}";
		CmdInvoker.invoke(new LuiSavePopUpCmd(dsId));
	}
 </#if>
}
