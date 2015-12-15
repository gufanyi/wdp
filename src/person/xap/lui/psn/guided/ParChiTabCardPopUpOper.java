package xap.lui.psn.guided;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import xap.lui.core.comps.MenubarComp;
import xap.lui.core.layout.UIPartMeta;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.model.PagePartMeta;
import xap.lui.core.model.RequestLifeCycleContext;
import xap.lui.core.model.ViewPartMeta;

public class ParChiTabCardPopUpOper extends SigTabCardOper{
	public static final String chiTabEditView_Id="chiTabEdit";
	public ParChiTabCardPopUpOper(){}
	public	ParChiTabCardPopUpOper(Map<String,String> map,List<Map<String,String>> listMap,List<Map<String,String>> listMap_Rela){
	   super(map);
	   	LifeCyclePhase phase = RequestLifeCycleContext.get().getPhase();
		RequestLifeCycleContext.get().setPhase(LifeCyclePhase.nullstatus);
		
		ParChiTabCardFullOper parchiTabCardFullOper=new ParChiTabCardFullOper();
		Map<String,Object>objMap=new HashMap<String, Object>();
	    objMap.put("files", this.getFiles());
	    objMap.put("sigTabLayout", this.getSigTabLayout());
	    objMap.put("toolBarComp", this.getToolBarComp());
	    
	    Map<String,String> mapMenuItemPara=parchiTabCardFullOper.chiTabHandle(objMap, map, listMap, listMap_Rela);
	    String menuBarCompId=mapMenuItemPara.get("menuBarCompId");
	    MenubarComp  menuBarComp= (MenubarComp)getFiles().getMapViewPartMeta().get("main").getViewMenus().getMenuBar(menuBarCompId);
	    setEventConf_parChiTabPopUpMenuBar(menuBarComp,mapMenuItemPara);
	    
	    objMap.put("mapMenuItemPara", mapMenuItemPara);
	    popUpwinHandle(objMap, listMap);
		RequestLifeCycleContext.get().setPhase(phase);

	}
	public void popUpwinHandle(Map<String,Object>objMap,List<Map<String,String>> listMap){
		CommBaseCreateFile files= (CommBaseCreateFile)objMap.get("files");
		Map<String,String> mapPara= (Map<String, String>)objMap.get("mapMenuItemPara");
				
		
		int i=0;
		for(Map<String,String> map_chi:listMap){
			i++;
			String viewId=chiTabEditView_Id+i;
			files._new_view(viewId, "");
			ViewPartMeta viewPartMeta=files.getMapViewPartMeta().get(viewId);//弹出框的ViewPartMeta
		    UIPartMeta uiPartMeta=files.getMapViewUiPartMeta().get(viewId);//弹出框的UIPartMeta
			PagePartMeta pagePartMeta=files.getPagePartMeta();
			
		    UiCtrlEditLayout editLayout=new  UiCtrlEditLayout();//编辑弹出框的布局对象
		    uiPartMeta.setElement(editLayout.getLayoutMain());//将布局对象添加到layout.xml里
		   	DatasetConfExpand datasetConf=getDatasetConf(map_chi, viewPartMeta);
		   	datasetConf.setIsAfterSelRowEvent(false);
		   	datasetConf.setIsNORelation(true);
		    String dsId=datasetConf.getDsId();
		    
			AddMdDataSetToViewPartMeta(datasetConf);
			{//重新配置 treeEditView 的onload事件
				String operDsId=map_chi.get("dsId");
				setEventConf_AddOrEditBeforeShow(viewPartMeta,operDsId);
			}
			String formCompId= addCtrlToViewPartMeta(pagePartMeta, viewPartMeta, dsId, ctrlType_FormComp);
			editLayout.getFormComp().setId(formCompId);
			String buttonSaveId= addCtrlToViewPartMeta(pagePartMeta, viewPartMeta, SigTabPopUpOper.editView_btnSaveId, SigTabPopUpOper.ctrlType_ButtonComp);
			{
				mapPara.put("btnId", buttonSaveId);
				setEventConf_PopUpWinButtonSave(viewPartMeta, mapPara);
			}
			String buttonOkId= addCtrlToViewPartMeta(pagePartMeta, viewPartMeta, SigTabPopUpOper.editView_btnOkId, SigTabPopUpOper.ctrlType_ButtonComp);
			{
				editLayout.getOkButton();
				mapPara.put("btnId", buttonOkId);
				setEventConf_PopUpWinButtonSave(viewPartMeta, mapPara);
			}
			String buttonCancelId= addCtrlToViewPartMeta(pagePartMeta, viewPartMeta, SigTabPopUpOper.editView_btnCancelId,SigTabPopUpOper.ctrlType_ButtonComp);
			{
				Map<String,String> map=new HashMap<String, String>();
				map.put("viewId", viewId);
				map.put("btnId", buttonCancelId);
				setEventConf_PopUpWinButtonCancel(viewPartMeta, map);
			}
	   }
	}
}
