package xap.lui.psn.guided;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import xap.lui.core.comps.MenubarComp;
import xap.lui.core.comps.ToolBarComp;
import xap.lui.core.layout.UITabComp;
import xap.lui.core.layout.UITabItem;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.model.RequestLifeCycleContext;
import xap.lui.core.model.ViewPartMeta;


public class ParChiTabCardFullOper extends SigTabCardOper{
	public static final String ctrlType_MenuBarComp="MenubarComp";
	public ParChiTabCardFullOper(){}
	public	ParChiTabCardFullOper(Map<String,String> map,List<Map<String,String>> listMap,List<Map<String,String>> listMap_Rela){
	   super(map);
	   LifeCyclePhase phase = RequestLifeCycleContext.get().getPhase();
		RequestLifeCycleContext.get().setPhase(LifeCyclePhase.nullstatus);
	   Map<String,Object>objMap=new HashMap<String, Object>();
	   objMap.put("files", this.getFiles());
	   objMap.put("sigTabLayout", this.getSigTabLayout());
	   objMap.put("toolBarComp", this.getToolBarComp());
	    
	   Map<String,String> mapMenuItemPara=chiTabHandle(objMap, map, listMap, listMap_Rela);
	   String menuBarCompId=mapMenuItemPara.get("menuBarCompId");
	   MenubarComp  menuBarComp= (MenubarComp)getFiles().getMapViewPartMeta().get("main").getViewMenus().getMenuBar(menuBarCompId);
	   setEventConf_parChiTabFullMenuBar(menuBarComp,mapMenuItemPara);
	   
	   
		RequestLifeCycleContext.get().setPhase(phase);
	}
	
	//返回MenuBarComp控件配置事件所需要的参数；
	public Map<String,String> chiTabHandle(Map<String,Object>objMap, Map<String,String> map,List<Map<String,String>> listMap,List<Map<String,String>> listMap_Rela){

		UiParChiTabCardFullLayout  parChiTabCardFullLayout=(UiParChiTabCardFullLayout)objMap.get("sigTabLayout");
		CommBaseCreateFile files=(CommBaseCreateFile)objMap.get("files");
		ToolBarComp toolBarComp=(ToolBarComp)objMap.get("toolBarComp");
		ViewPartMeta viewPartMeta=files.getMapViewPartMeta().get("main");
		
		int i=0;
		String dsId_Chis="", ctrlId="",chiTabEditView="";
		Map<String,String> mapMenuItemPara=new HashMap<String, String>();
		Boolean isNoRelation= listMap_Rela.size()!=0;
		
		UITabComp tabComp=parChiTabCardFullLayout.getTabComp();
	
		for(Map<String,String> map_chi:listMap){
		   	i++;
		   	DatasetConfExpand datasetConf=getDatasetConf(map_chi, viewPartMeta);
		   	datasetConf.setMasterKey(map.get("masterKey"));
		   	datasetConf.setMasterDs(map.get("dsId"));
		   	String dsId=datasetConf.getDsId();
		   	dsId_Chis+=","+dsId;//ToolBarComp的保存事件用
		   	chiTabEditView+=","+ParChiTabCardPopUpOper.chiTabEditView_Id+i;
		   	mapMenuItemPara.put("dsId_Chi"+i, dsId);//记录 每个子表dsid 配置menuItem事件时使用
		   	datasetConf.setIsNORelation(isNoRelation);//控制是否配置数据集关联,isNoRelation为true不配置，为false配置
			AddMdDataSetToViewPartMeta(datasetConf);
			UITabItem tabPanel=null;
			if(i==1){
			    ctrlId= addCtrlToViewPartMeta(files.getPagePartMeta(), viewPartMeta, dsId, ctrlType_MenuBarComp);
				parChiTabCardFullLayout.getMenuBarComp_Chi().setId(ctrlId);//menuBar布局
				mapMenuItemPara.put("menuBarCompId", ctrlId);
				ctrlId= addCtrlToViewPartMeta(files.getPagePartMeta(), viewPartMeta, dsId, ctrlType_GridComp);
			    tabPanel= (UITabItem)tabComp.getPanelList().get(0);
			}else{
				ctrlId= addCtrlToViewPartMeta(files.getPagePartMeta(), viewPartMeta, dsId, ctrlType_GridComp);
			    tabPanel= parChiTabCardFullLayout.getOneTabItem();
			    tabComp.addPanel(tabPanel);
			}
			tabPanel.setText(datasetConf.getDispName());
			tabPanel.getElement().setId(ctrlId);
	   }
		if(isNoRelation){
			//若isNoRelation==true则上边AddMdDataSetToViewPartMeta操作没有配置数据集关系,则在此处处理
			configDatasetRelation(viewPartMeta, listMap_Rela);
		}

		dsId_Chis=dsId_Chis.substring(1, dsId_Chis.length());
		chiTabEditView=chiTabEditView.substring(1, chiTabEditView.length());
	    String controller=viewPartMeta.getController();
	    map.put("dsId_Chis", dsId_Chis);
	    //配置 toolBarItem的点击事件
		setEventConf_parChiTabCardToolBar(toolBarComp, controller, map);
		
		mapMenuItemPara.put("dsId", map.get("dsId"));
		mapMenuItemPara.put("aggDo", map.get("aggDo"));
		mapMenuItemPara.put("cardLayoutId", map.get("cardLayoutId"));
		mapMenuItemPara.put("tabCompId", tabComp.getId());//记录 tabCompId 配置menuItem事件时使用
		mapMenuItemPara.put("dsId_Chis", dsId_Chis);
		mapMenuItemPara.put("EditViewId", chiTabEditView);
		mapMenuItemPara.put("controller", controller);
		mapMenuItemPara.put("toolBarCompId", toolBarComp.getId());
		return 	mapMenuItemPara;
	}
	
	
	
}
