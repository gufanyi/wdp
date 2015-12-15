package xap.lui.psn.guided;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import xap.lui.core.comps.ToolBarComp;
import xap.lui.core.layout.UIPartMeta;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.model.PagePartMeta;
import xap.lui.core.model.RequestLifeCycleContext;
import xap.lui.core.model.ViewPartMeta;

public class SigTabPopUpOper extends CommBaseCreateComp{
	
	public static final String ctrlType_ButtonComp="ButtonComp";
	public static final String editView_btnSaveId="btn_Save";
	public static final String editView_btnOkId="btn_Ok";
	public static final String editView_btnCancelId="btn_Cancel";
	public static final String parTabEditView_Id="parTabEdit";
	
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

	public SigTabPopUpOper(){

	}
	public SigTabPopUpOper(Map<String,String>map){
		
	    files=new CommBaseCreateFile(map);
	    PagePartMeta pagePartMeta=files.getPagePartMeta();
		ViewPartMeta viewPartMeta= files.getMapViewPartMeta().get("main");
		
		LifeCyclePhase phase = RequestLifeCycleContext.get().getPhase();
		RequestLifeCycleContext.get().setPhase(LifeCyclePhase.nullstatus);
		SigTabCardOper sigTabCardOper=new SigTabCardOper();
		
		DatasetConfExpand datasetConf=sigTabCardOper.getDatasetConf(map,viewPartMeta);
		String dsId=datasetConf.getDsId();
		
		AddMdDataSetToViewPartMeta(datasetConf);
		map.put("masterKey", datasetConf.getMasterKey());
		String	uiLayoutType=(String)map.get("uiLayoutType");
		if(StringUtils.equals(uiLayoutType, "UiSigTabPopUpLayout")){
			sigTabLayout= new UiSigTabPopUpLayout();
		}else if(StringUtils.equals(uiLayoutType, "UiParChiTabPopUpFullLayout")||StringUtils.equals(uiLayoutType, "UiParChiTabPopUpPopUpLayout")){
			sigTabLayout= new UiParChiTabPopUpFullLayout();	
		}else if(StringUtils.equals(uiLayoutType, "UiTreeTabPopUpLayout")){
			sigTabLayout= new UiTreeTabPopUpLayout();	
		}else if(StringUtils.equals(uiLayoutType, "UiTreeTabTabPopUpFullLayout")||StringUtils.equals(uiLayoutType, "UiTreeTabTabPopUpPopUpLayout")){
			sigTabLayout= new UiTreeTabTabPopUpFullLayout();	
		}
		else if(StringUtils.equals(uiLayoutType, "UiParChiTabPopUpCardLayout")){
			sigTabLayout= new UiParChiTabPopUpCardLayout();
		}else if(StringUtils.equals(uiLayoutType, "UiTreeTabTabPopUpCardLayout")){
			sigTabLayout= new UiTreeTabTabPopUpCardLayout();
		}
		 
		String ctrlId= addCtrlToViewPartMeta(files.getPagePartMeta(), viewPartMeta, dsId, SigTabCardOper.ctrlType_ToolBarComp);
		sigTabLayout.getToolBar().setId(ctrlId);//布局ToolBar
		toolBarComp= (ToolBarComp)viewPartMeta.getViewComponents().getComponent(ctrlId);

		ctrlId= addCtrlToViewPartMeta(files.getPagePartMeta(), viewPartMeta, dsId,SigTabCardOper.ctrlType_GridComp);
		sigTabLayout.getGridComp_Par().setId(ctrlId);//布局表格
		files.getMapViewUiPartMeta().get("main").setElement(sigTabLayout.getLayoutMain());
		
		//配置 toolBarItem的点击事件
	    String controller=viewPartMeta.getController();
	    Map<String,String>map_TBPara=new HashMap<String, String>();
	    map_TBPara.put("controller",controller );
	    map_TBPara.put("dsId",map.get("dsId"));
	    map_TBPara.put("viewId",parTabEditView_Id);
	    setEventConf_sigTabPopUpToolBar(toolBarComp, map_TBPara);
		{
			files._new_view(parTabEditView_Id,"");
		    ViewPartMeta parTabEditViewPartMeta=files.getMapViewPartMeta().get(parTabEditView_Id);//树编辑的ViewPartMeta
		    UIPartMeta parTabEditViewUIPartMeta=files.getMapViewUiPartMeta().get(parTabEditView_Id);//树编辑的UIPartMeta
		    UiCtrlEditLayout parTabEditLayout=new  UiCtrlEditLayout();//树的编辑弹出框布局对象
		    parTabEditViewUIPartMeta.setElement(parTabEditLayout.getLayoutMain());//将布局对象添加到layout.xml里
		   	DatasetConfExpand datasetConf_ParTab=sigTabCardOper.getDatasetConf(map, parTabEditViewPartMeta);
		   	datasetConf_ParTab.setIsAfterSelRowEvent(false);
		  
		    dsId=datasetConf_ParTab.getDsId();
		    datasetConf.setIsNORelation(true);
			AddMdDataSetToViewPartMeta(datasetConf_ParTab);
			{//重新配置 parTabEditView 的onload事件
				String operDsId=map.get("dsId");
				setEventConf_AddOrEditBeforeShow(parTabEditViewPartMeta,operDsId);
			}
			String formCompId= addCtrlToViewPartMeta(pagePartMeta, parTabEditViewPartMeta, dsId,SigTabCardOper.ctrlType_FormComp);
			parTabEditLayout.getFormComp().setId(formCompId);
			
			String buttonSaveId= addCtrlToViewPartMeta(pagePartMeta, parTabEditViewPartMeta, editView_btnSaveId, ctrlType_ButtonComp);
			{
				Map<String,String> map_btn=new HashMap<String, String>();
				map_btn.put("dsId", map.get("dsId"));
				map_btn.put("btnId", buttonSaveId);
				setEventConf_Par_PopUpWinButtonSave(parTabEditViewPartMeta, map_btn);
			}
			String buttonCancelId= addCtrlToViewPartMeta(pagePartMeta, parTabEditViewPartMeta, editView_btnCancelId, ctrlType_ButtonComp);
			{
				Map<String,String> map1=new HashMap<String, String>();
				map1.put("viewId", parTabEditView_Id);
				map1.put("btnId", buttonCancelId);
				setEventConf_PopUpWinButtonCancel(parTabEditViewPartMeta, map1);
			}
		}
	
		RequestLifeCycleContext.get().setPhase(phase);
	}
	

}
