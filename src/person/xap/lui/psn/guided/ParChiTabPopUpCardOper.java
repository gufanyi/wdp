package xap.lui.psn.guided;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import xap.lui.core.comps.MenubarComp;
import xap.lui.core.comps.ToolBarComp;
import xap.lui.core.layout.UICardLayout;
import xap.lui.core.layout.UITabComp;
import xap.lui.core.layout.UITabItem;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.model.RequestLifeCycleContext;
import xap.lui.core.model.ViewPartMeta;

public class ParChiTabPopUpCardOper extends SigTabPopUpOper{
	public ParChiTabPopUpCardOper(){}
	public	ParChiTabPopUpCardOper(Map<String,String> map,List<Map<String,String>> listMap,List<Map<String,String>> listMap_Rela){
	   super(map);
	   Map<String,Object>objMap=new HashMap<String, Object>();
	   objMap.put("files", this.getFiles());
	   objMap.put("sigTabLayout", this.getSigTabLayout());
	   objMap.put("toolBarComp", this.getToolBarComp());
	    
	  chiTabHandle(objMap, map, listMap, listMap_Rela);

	}
	
	
	public void chiTabHandle(Map<String,Object>objMap, Map<String,String> map,List<Map<String,String>> listMap,List<Map<String,String>> listMap_Rela){
		
		LifeCyclePhase phase = RequestLifeCycleContext.get().getPhase();
		RequestLifeCycleContext.get().setPhase(LifeCyclePhase.nullstatus);
		
		UiParChiTabPopUpCardLayout  parChiTab_CardLayout=(UiParChiTabPopUpCardLayout)objMap.get("sigTabLayout");
		CommBaseCreateFile files=(CommBaseCreateFile)objMap.get("files");
		ToolBarComp toolBarComp=(ToolBarComp)objMap.get("toolBarComp");
		ViewPartMeta viewPartMeta=files.getMapViewPartMeta().get("main");
		MenubarComp menuBarComp=null;
		int i=0;
		String dsId_Chis="",cardLayoutId_Chis="";
		Map<String,String> mapMenuItemPara=new HashMap<String, String>();
		Boolean isNoRelation= listMap_Rela.size()!=0;
		UITabComp tabComp=parChiTab_CardLayout.getTabComp();
		
		for(Map<String,String> map_chi:listMap){
		   	i++;
		   	SigTabCardOper sigTabCardOper=new SigTabCardOper();
		   	DatasetConfExpand datasetConf=sigTabCardOper.getDatasetConf(map_chi, viewPartMeta);
		   	datasetConf.setMasterKey(map.get("masterKey"));
		   	datasetConf.setMasterDs(map.get("dsId"));
		   	String dsId=datasetConf.getDsId();
		   	dsId_Chis+=","+dsId;//ToolBarComp的保存事件用
		   	mapMenuItemPara.put("dsId_Chi"+i, dsId);//记录 每个子表dsid 配置menuItem事件时使用
		   	datasetConf.setIsNORelation(isNoRelation);//控制是否配置数据集关联,isNoRelation为true不配置，为false配置
			AddMdDataSetToViewPartMeta(datasetConf);
			String ctrlId="",ctrlFormId="";
			UITabItem tabPanel=null;
			if(i==1){
			    ctrlId= addCtrlToViewPartMeta(files.getPagePartMeta(), viewPartMeta, dsId, ParChiTabCardFullOper.ctrlType_MenuBarComp);
				parChiTab_CardLayout.getMenuBarComp_Chi().setId(ctrlId);//menuBar布局
			    menuBarComp= (MenubarComp)viewPartMeta.getViewMenus().getMenuBar(ctrlId);
				ctrlId= addCtrlToViewPartMeta(files.getPagePartMeta(), viewPartMeta, dsId, SigTabCardOper.ctrlType_GridComp);
				ctrlFormId= addCtrlToViewPartMeta(files.getPagePartMeta(), viewPartMeta, dsId, SigTabCardOper.ctrlType_FormComp);
			    tabPanel= (UITabItem)tabComp.getPanelList().get(0);
				//parChiTabCardFullLayout.getGridComp_Chi_One().setId(ctrlId);
			}else{
				ctrlId= addCtrlToViewPartMeta(files.getPagePartMeta(), viewPartMeta, dsId, SigTabCardOper.ctrlType_GridComp);
				ctrlFormId= addCtrlToViewPartMeta(files.getPagePartMeta(), viewPartMeta, dsId, SigTabCardOper.ctrlType_FormComp);
				UiParChiTabCardCardLayout parChiCardCardOper=new UiParChiTabCardCardLayout();
				tabPanel= parChiCardCardOper.getOneTabItem();
			    tabComp.addPanel(tabPanel);
			}
			tabPanel.setText(datasetConf.getDispName());
			
			UICardLayout cardLayout= (UICardLayout)tabPanel.getElement();
			String	cardLayoutId=cardLayout.getId();
			cardLayoutId_Chis+=","+cardLayoutId;
			cardLayout.getPanelList().get(0).getElement().setId(ctrlId);//grid
			cardLayout.getPanelList().get(1).getElement().setId(ctrlFormId);//grid
	   }
		if(isNoRelation){
			//若isNoRelation==true则上边AddMdDataSetToViewPartMeta操作没有配置数据集关系,则在此处处理
			configDatasetRelation(viewPartMeta, listMap_Rela);
		}

	    dsId_Chis=dsId_Chis.substring(1, dsId_Chis.length());
	    cardLayoutId_Chis=cardLayoutId_Chis.substring(1, cardLayoutId_Chis.length());
	    
	    String controller=viewPartMeta.getController();
	    map.put("dsId_Chis", dsId_Chis);
	    map.put("cardLayoutId", cardLayoutId_Chis);
	    //配置 toolBarItem的点击事件
		setEventConf_parChiTabCardToolBar(toolBarComp, controller, map);//重新配置ToolBarItem的删除事件
		
		mapMenuItemPara.put("dsId", map.get("dsId"));
		mapMenuItemPara.put("aggDo", map.get("aggDo"));
		mapMenuItemPara.put("tabCompId", tabComp.getId());//记录 tabCompId 配置menuItem事件时使用
		mapMenuItemPara.put("dsId_Chis", dsId_Chis);
	    mapMenuItemPara.put("cardLayoutId_Chis", cardLayoutId_Chis);
	    mapMenuItemPara.put("controller", controller);
	    mapMenuItemPara.put("editType", map.get("uiLayoutType"));
	    mapMenuItemPara.put("toolBarCompId",toolBarComp.getId());
	    
	    setEventConf_parChiTabCardMenuBar(menuBarComp, mapMenuItemPara);//配置子表的MenuBarItem事件
	    
		RequestLifeCycleContext.get().setPhase(phase);

	
	}

}
