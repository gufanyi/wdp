package xap.lui.psn.guided;

import java.util.Map;
import org.apache.commons.lang.StringUtils;
import xap.lui.core.comps.ToolBarComp;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.model.RequestLifeCycleContext;
import xap.lui.core.model.ViewPartMeta;

public class SigTabFullOper extends CommBaseCreateComp{
	private UiSigTabPopUpLayout sigTabLayout=null;
	private CommBaseCreateFile files;
	private ToolBarComp toolBarComp;
	
	
	
	public ToolBarComp getToolBarComp() {
		return toolBarComp;
	}

	public CommBaseCreateFile getFiles(){
		return files;
	}
	
	public UiSigTabPopUpLayout getSigTabLayout() {
		return sigTabLayout;
	}

	public SigTabFullOper(){

	}
	public SigTabFullOper(Map<String,String>map){
		
	    files=new CommBaseCreateFile(map);
		ViewPartMeta viewPartMeta= files.getMapViewPartMeta().get("main");
		
		LifeCyclePhase phase = RequestLifeCycleContext.get().getPhase();
		RequestLifeCycleContext.get().setPhase(LifeCyclePhase.nullstatus);
		SigTabCardOper sigTabCardOper=new SigTabCardOper();
		
		DatasetConfExpand datasetConf=sigTabCardOper.getDatasetConf(map,viewPartMeta);
		String dsId=datasetConf.getDsId();
		
		AddMdDataSetToViewPartMeta(datasetConf);
		map.put("masterKey", datasetConf.getMasterKey());
		String	uiLayoutType=(String)map.get("uiLayoutType");
		if(StringUtils.equals(uiLayoutType, "UiSigTabFullLayout")){
			sigTabLayout= new UiSigTabPopUpLayout();
		}else if(StringUtils.equals(uiLayoutType, "UiParChiTabFullFullLayout")){
			sigTabLayout= new UiParChiTabPopUpFullLayout();	
		}else if(StringUtils.equals(uiLayoutType, "UiTreeTabFullLayout")){
			sigTabLayout= new UiTreeTabPopUpLayout();	
		}else if(StringUtils.equals(uiLayoutType, "UiTreeTabTabFullFullLayout")){
			sigTabLayout= new UiTreeTabTabPopUpFullLayout();	
		}else if(StringUtils.equals(uiLayoutType, "UiParChiTabFullCardLayout")){
			sigTabLayout= new UiParChiTabPopUpCardLayout();	
		}else if(StringUtils.equals(uiLayoutType, "UiTreeTabTabFullCardLayout")){
			sigTabLayout= new UiTreeTabTabPopUpCardLayout();	
		}
		else if(StringUtils.equals(uiLayoutType, "UiParChiTabFullPopUpLayout")){
			sigTabLayout= new UiParChiTabPopUpFullLayout();	
		}else if(StringUtils.equals(uiLayoutType, "UiTreeTabTabFullPopUpLayout")){
			sigTabLayout= new UiTreeTabTabPopUpFullLayout();	
		}
		
		 
		String ctrlId= addCtrlToViewPartMeta(files.getPagePartMeta(), viewPartMeta, dsId, SigTabCardOper.ctrlType_ToolBarComp);
		sigTabLayout.getToolBar().setId(ctrlId);//布局ToolBar
		toolBarComp= (ToolBarComp)viewPartMeta.getViewComponents().getComponent(ctrlId);

		ctrlId= addCtrlToViewPartMeta(files.getPagePartMeta(), viewPartMeta, dsId,SigTabCardOper.ctrlType_GridComp);
		sigTabLayout.getGridComp_Par().setId(ctrlId);//布局表格
		files.getMapViewUiPartMeta().get("main").setElement(sigTabLayout.getLayoutMain());
		
		//配置 toolBarItem的点击事件
	    String controller=viewPartMeta.getController();
	    map.put("controller",controller );
	    setEventConf_sigTabFullToolBar(toolBarComp, map);
		RequestLifeCycleContext.get().setPhase(phase);
	}

}
