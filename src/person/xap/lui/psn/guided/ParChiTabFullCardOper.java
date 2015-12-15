package xap.lui.psn.guided;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParChiTabFullCardOper extends SigTabFullOper{
	public ParChiTabFullCardOper(){}
	public	ParChiTabFullCardOper(Map<String,String> map,List<Map<String,String>> listMap,List<Map<String,String>> listMap_Rela){
	   super(map);
	   Map<String,Object>objMap=new HashMap<String, Object>();
	   objMap.put("files", this.getFiles());
	   objMap.put("sigTabLayout", this.getSigTabLayout());
	   objMap.put("toolBarComp", this.getToolBarComp());
	    ParChiTabPopUpCardOper parchiTabPopUpCardOper=new ParChiTabPopUpCardOper();
	    parchiTabPopUpCardOper.chiTabHandle(objMap, map, listMap, listMap_Rela);

	}

}