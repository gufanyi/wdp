package xap.lui.psn.guided;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import xap.lui.core.comps.ToolBarComp;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.model.RequestLifeCycleContext;
import xap.lui.core.model.ViewPartMeta;
import xap.mw.coreitf.d.FBoolean;



public class SigTabCardOper extends CommBaseCreateComp{
	public static final String ctrlType_GridComp="GridComp";
	public static final String ctrlType_ToolBarComp="ToolBarComp";
	public static final String ctrlType_FormComp="FormComp";
	
	private UiSigTabCardLayout sigTabLayout=null;
	private CommBaseCreateFile files;
	private ToolBarComp toolBarComp;
	
	
	
	public ToolBarComp getToolBarComp() {
		return toolBarComp;
	}

	public CommBaseCreateFile getFiles(){
		return files;
	}
	
	public UiSigTabCardLayout getSigTabLayout() {
		return sigTabLayout;
	}

	public SigTabCardOper(){

	}
	public SigTabCardOper(Map<String,String>map){
		
	    files=new CommBaseCreateFile(map);
		ViewPartMeta viewPartMeta= files.getMapViewPartMeta().get("main");
		
		LifeCyclePhase phase = RequestLifeCycleContext.get().getPhase();
		RequestLifeCycleContext.get().setPhase(LifeCyclePhase.nullstatus);
		DatasetConfExpand datasetConf=getDatasetConf(map,viewPartMeta);
		String dsId=datasetConf.getDsId();
		
		AddMdDataSetToViewPartMeta(datasetConf);
		map.put("masterKey", datasetConf.getMasterKey());
		String	uiLayoutType=(String)map.get("uiLayoutType");
		if(StringUtils.equals(uiLayoutType, "UiSigTabCardLayout")){
			sigTabLayout= new UiSigTabCardLayout();
		}else if(StringUtils.equals(uiLayoutType, "UiParChiTabCardFullLayout")){
			sigTabLayout= new UiParChiTabCardFullLayout();	
		}else if(StringUtils.equals(uiLayoutType, "UiTreeTabCardLayout")){
			sigTabLayout= new UiTreeTabCardLayout();	
		}else if(StringUtils.equals(uiLayoutType, "UiTreeTabTabCardFullLayout")){
			sigTabLayout= new UiTreeTabTabCardFullLayout();	
		}
		else if(StringUtils.equals(uiLayoutType, "UiParChiTabCardCardLayout")){
			sigTabLayout= new UiParChiTabCardCardLayout();	
		}else if(StringUtils.equals(uiLayoutType, "UiTreeTabTabCardCardLayout")){
			sigTabLayout= new UiTreeTabTabCardCardLayout();	
		}
		else if(StringUtils.equals(uiLayoutType, "UiParChiTabCardPopUpLayout")){
			sigTabLayout= new UiParChiTabCardFullLayout();	
		}else if(StringUtils.equals(uiLayoutType, "UiTreeTabTabCardPopUpLayout")){
			sigTabLayout= new UiTreeTabTabCardFullLayout();	
		}
		
		
		 
		String ctrlId= addCtrlToViewPartMeta(files.getPagePartMeta(), viewPartMeta, dsId, ctrlType_ToolBarComp);
		sigTabLayout.getToolBar().setId(ctrlId);//布局ToolBar
		toolBarComp= (ToolBarComp)viewPartMeta.getViewComponents().getComponent(ctrlId);
		ctrlId= addCtrlToViewPartMeta(files.getPagePartMeta(), viewPartMeta, dsId, ctrlType_GridComp);
		sigTabLayout.getGridComp_Par().setId(ctrlId);//布局表格
	    ctrlId= addCtrlToViewPartMeta(files.getPagePartMeta(), viewPartMeta, dsId, ctrlType_FormComp);
		sigTabLayout.getFormComp_Par().setId(ctrlId);//布局表单
		files.getMapViewUiPartMeta().get("main").setElement(sigTabLayout.getLayoutMain());
		String cardLayoutId= sigTabLayout.getCardLayout_Par().getId();
		map.put("cardLayoutId", cardLayoutId);
	    String controller=viewPartMeta.getController();
	    //配置 toolBarItem的点击事件
		setEventConf_sigTabCardToolBar(dsId, toolBarComp, cardLayoutId, controller);
		RequestLifeCycleContext.get().setPhase(phase);
	}
	
	public DatasetConfExpand getDatasetConf(Map<String,String> map,ViewPartMeta viewPartMeta){
		DatasetConfExpand datasetConf=new DatasetConfExpand();
		String dsId=map.get("dsId");
		String name=map.get("name");
		String voMeta=map.get("ref");
		String objMeta = name.substring(name.indexOf('(')+1,name.length()-1);
		String displayname =name.substring(0,name.indexOf('('));
		String pageSize=map.get("pageSize");
	    Boolean isAfterSelRowEvent=StringUtils.equals(map.get("isAfterSelRowEvent"), "true");
	    Boolean isSetParTabStatus=StringUtils.equals(map.get("isParTabStatus"), "true");
	    Boolean isDataLoad=StringUtils.equals(map.get("isDatasetLoad"), "true");
		name=dsId;
		{
			datasetConf.setDsId(dsId);
			datasetConf.setDispName(displayname);
			datasetConf.setIsAfterSelRowEvent(isAfterSelRowEvent);
			datasetConf.setIsDataLoad(isDataLoad);
			datasetConf.setIsParTabStatus(isSetParTabStatus);
			datasetConf.setIsLasyLoad(new FBoolean(false));
			datasetConf.setName(name);
			datasetConf.setObjMeta(objMeta);
			datasetConf.setPageSize(pageSize);
			datasetConf.setVoMeta(voMeta);
			datasetConf.setWidget(viewPartMeta);
		}
		return datasetConf;
	}
	

	
}
