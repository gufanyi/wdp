package xap.lui.psn.guided;

import java.util.List;
import java.util.Map;

import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.model.RequestLifeCycleContext;

public class TreeTabPopUpOper extends SigTabPopUpOper{
	public TreeTabPopUpOper(){}
	public TreeTabPopUpOper(Map<String,String> treeMap,Map<String,String> tabMap,List<Map<String,String>> listMap_Rela){
		super(tabMap);
		
		LifeCyclePhase phase = RequestLifeCycleContext.get().getPhase();
		RequestLifeCycleContext.get().setPhase(LifeCyclePhase.nullstatus);
		
		UiTreeTabPopUpLayout  treeTabCardLayout=(UiTreeTabPopUpLayout)getSigTabLayout();
		treeMap.put("tabDsId", tabMap.get("dsId"));
		TreeTabCardOper treeTabCardOper=new TreeTabCardOper();
		String treeCompId= treeTabCardOper.processTree(treeMap,getFiles(),listMap_Rela);
		treeTabCardLayout.getTreeComp().setId(treeCompId);

		RequestLifeCycleContext.get().setPhase(phase);
		
		
	}

}
