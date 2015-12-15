package lui.ctrl;

import xap.lui.core.command.CmdInvoker;
import xap.lui.core.event.MouseEvent;
import xap.lui.psn.cmd.LuiSaveAggCmd;
import xap.lui.core.comps.ToolBarComp;
import xap.lui.core.model.LuiAppUtil;
public class MethodTemplate {

 <#if isEditView?exists && parentViewId?exists && parentDsId?exists && parentDetailDsIds?exists && aggVoClazz?exists && CardLayoutIds?exists>	
		public void LuiSaveAggCmd(){
			Boolean isEditView = ${isEditView};
			String  parentViewId = "${parentViewId}";
			String  parentDsId = "${parentDsId}";
			String parentDetailDsIdsString= "${parentDetailDsIds}";
			String[] parentDetailDsIds = parentDetailDsIdsString.split(","); 
			String aggVoClazz = "${aggVoClazz}";
			String cardLayoutId="${CardLayoutIds}";
		  	CmdInvoker.invoke(new LuiSaveAggCmd(isEditView, parentViewId, parentDsId,parentDetailDsIds,aggVoClazz,cardLayoutId));
		}
 <#elseif isEditView?exists && parentViewId?exists && parentDsId?exists && parentDetailDsIds?exists && aggVoClazz?exists>	
		public void LuiSaveAggCmd(){
			Boolean isEditView = ${isEditView};
			String  parentViewId = "${parentViewId}";
			String  parentDsId = "${parentDsId}";
			String parentDetailDsIdsString= "${parentDetailDsIds}";
			String[] parentDetailDsIds = parentDetailDsIdsString.split(","); 
			String aggVoClazz = "${aggVoClazz}";
		  	CmdInvoker.invoke(new LuiSaveAggCmd(isEditView, parentViewId, parentDsId,parentDetailDsIds,aggVoClazz));
		}
  <#elseif masterDsId?exists && detailDsIds?exists && aggVoClazz?exists && CardLayoutIds?exists && ToolBarCompId?exists >
		public void LuiSaveAggCmd(){
			String masterDsId = "${masterDsId}";
			String  detailDsString = "${detailDsIds}";
			String[] detailDsIds = detailDsString.split(","); 
			String aggVoClazz = "${aggVoClazz}";
			String cardLayoutIds = "${CardLayoutIds}";
			String toolBarCompId = "${ToolBarCompId}";
			ToolBarComp toolBarComp= (ToolBarComp)LuiAppUtil.getCntView().getViewComponents().getComponent(toolBarCompId);
		  	CmdInvoker.invoke(new LuiSaveAggCmd(masterDsId, detailDsIds, aggVoClazz,cardLayoutIds,toolBarComp));
		}
  <#elseif masterDsId?exists && detailDsIds?exists && aggVoClazz?exists && CardLayoutIds?exists && MenuBarCompId?exists >
		public void LuiSaveAggCmd(){
			String masterDsId = "${masterDsId}";
			String  detailDsString = "${detailDsIds}";
			String[] detailDsIds = detailDsString.split(","); 
			String aggVoClazz = "${aggVoClazz}";
			String cardLayoutId = "${CardLayoutIds}";
			String menuBarCompId = "${MenuBarCompId}";
		  	CmdInvoker.invoke(new LuiSaveAggCmd(masterDsId, detailDsIds, aggVoClazz,cardLayoutId,menuBarCompId));
		}
  <#elseif masterDsId?exists && detailDsIds?exists && aggVoClazz?exists && CardLayoutIds?exists>
		public void LuiSaveAggCmd(){
			String masterDsId = "${masterDsId}";
			String  detailDsString = "${detailDsIds}";
			String[] detailDsIds = detailDsString.split(","); 
			String aggVoClazz = "${aggVoClazz}";
			String cardLayoutId = "${CardLayoutIds}";
		  	CmdInvoker.invoke(new LuiSaveAggCmd(masterDsId, detailDsIds, aggVoClazz,cardLayoutId));
		}
  <#elseif masterDsId?exists && detailDsIds?exists && aggVoClazz?exists && MenuBarCompId?exists>	
		public void LuiSaveAggCmd(){
			String masterDsId = "${masterDsId}";
			String  detailDsString = "${detailDsIds}";
			String[] detailDsIds = detailDsString.split(","); 
			String aggVoClazz = "${aggVoClazz}";
			String menuBarCompId = "${MenuBarCompId}";
		  	CmdInvoker.invoke(new LuiSaveAggCmd(menuBarCompId,masterDsId, detailDsIds, aggVoClazz));
		}		
  <#elseif masterDsId?exists && detailDsIds?exists && aggVoClazz?exists && ToolBarCompId?exists>	
		public void LuiSaveAggCmd(){
			String masterDsId = "${masterDsId}";
			String  detailDsString = "${detailDsIds}";
			String[] detailDsIds = detailDsString.split(","); 
			String aggVoClazz = "${aggVoClazz}";
			String toolBarCompId = "${ToolBarCompId}";
			ToolBarComp toolBarComp= (ToolBarComp)LuiAppUtil.getCntView().getViewComponents().getComponent(toolBarCompId);
		  	CmdInvoker.invoke(new LuiSaveAggCmd(masterDsId, detailDsIds, aggVoClazz,toolBarComp));
		}
  <#elseif masterDsId?exists && detailDsIds?exists && aggVoClazz?exists>	
		public void LuiSaveAggCmd(){
			String masterDsId = "${masterDsId}";
			String  detailDsString = "${detailDsIds}";
			String[] detailDsIds = detailDsString.split(","); 
			String aggVoClazz = "${aggVoClazz}";
		  	CmdInvoker.invoke(new LuiSaveAggCmd(masterDsId, detailDsIds, aggVoClazz));
		}
   </#if>	
}