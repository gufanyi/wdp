package xap.lui.psn.guided;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import xap.lui.core.comps.MenubarComp;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.model.RequestLifeCycleContext;


public class ParChiTabFullFullOper extends SigTabFullOper{
	public	ParChiTabFullFullOper(Map<String,String> map,List<Map<String,String>> listMap,List<Map<String,String>> listMap_Rela){
		   super(map);
		   LifeCyclePhase phase = RequestLifeCycleContext.get().getPhase();
		   RequestLifeCycleContext.get().setPhase(LifeCyclePhase.nullstatus);
		   
		   ParChiTabPopUpFullOper parchiTabPopWinFullOper=new ParChiTabPopUpFullOper();
		   Map<String,Object>objMap=new HashMap<String, Object>();
		   objMap.put("files", this.getFiles());
		   objMap.put("sigTabLayout", this.getSigTabLayout());
		   objMap.put("toolBarComp", this.getToolBarComp());
		   Map<String,String> mapMenuItemPara=parchiTabPopWinFullOper.chiTabHandle(objMap, map, listMap, listMap_Rela);
		   
		   String menuBarCompId=mapMenuItemPara.get("menuBarCompId");
		   MenubarComp  menuBarComp= (MenubarComp)getFiles().getMapViewPartMeta().get("main").getViewMenus().getMenuBar(menuBarCompId);
		   setEventConf_parChiTabFullMenuBar(menuBarComp, mapMenuItemPara);
		   
		   RequestLifeCycleContext.get().setPhase(phase);
		}

}
