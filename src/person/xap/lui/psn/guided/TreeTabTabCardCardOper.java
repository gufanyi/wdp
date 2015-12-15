package xap.lui.psn.guided;

import java.util.List;
import java.util.Map;

import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.model.RequestLifeCycleContext;

public class TreeTabTabCardCardOper extends ParChiTabCardCardOper{
	public TreeTabTabCardCardOper(Map<String,String> treeMap,Map<String,String> tab_ParMap,List<Map<String,String>> listMap,List<Map<String,String>> listMap_Rela,List<Map<String,String>> listMap_TreeRela){
		super(tab_ParMap,listMap,listMap_Rela);
		
		LifeCyclePhase phase = RequestLifeCycleContext.get().getPhase();
		RequestLifeCycleContext.get().setPhase(LifeCyclePhase.nullstatus);
		
		UiTreeTabTabCardCardLayout  treeTabTabCardCardLayout=(UiTreeTabTabCardCardLayout)getSigTabLayout();
		treeMap.put("tabDsId", tab_ParMap.get("dsId"));
		treeMap.put("aggDo", tab_ParMap.get("aggDo"));
		TreeTabCardOper treeTabCardOper=new TreeTabCardOper();
		String treeCompId= treeTabCardOper.processTree(treeMap,getFiles(),listMap_TreeRela);
		treeTabTabCardCardLayout.getTreeComp().setId(treeCompId);

		RequestLifeCycleContext.get().setPhase(phase);
	}

}
