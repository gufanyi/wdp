package xap.lui.psn.guided;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import xap.lui.core.comps.ContextMenuComp;
import xap.lui.core.comps.RecursiveTreeLevel;
import xap.lui.core.comps.TreeViewComp;
import xap.lui.core.layout.UIPartMeta;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.model.PagePartMeta;
import xap.lui.core.model.RequestLifeCycleContext;
import xap.lui.core.model.ViewPartMeta;

public class TreeTabCardOper extends SigTabCardOper{
	public static final String ctrlType_TreeViewComp="TreeViewComp";
	public static final String treeEditView_Id="treeEdit";
	public static final String ctrlItem_ContextMenu_new="contextMenu_menuitem_new";
	public static final String ctrlItem_ContextMenu_edit="contextMenu_menuitem_edit";
	public static final String ctrlItem_ContextMenu_delete="contextMenu_menuitem_delete";
	public TreeTabCardOper(){}
	public TreeTabCardOper(Map<String,String> treeMap,Map<String,String> tabMap,List<Map<String,String>> listMap_Rela){
		super(tabMap);
		
		LifeCyclePhase phase = RequestLifeCycleContext.get().getPhase();
		RequestLifeCycleContext.get().setPhase(LifeCyclePhase.nullstatus);
		
		UiTreeTabCardLayout  treeTabCardLayout=(UiTreeTabCardLayout)getSigTabLayout();
		
		treeMap.put("tabDsId", tabMap.get("dsId"));
		String treeCompId= processTree(treeMap,getFiles(),listMap_Rela);
		
		treeTabCardLayout.getTreeComp().setId(treeCompId);
	   
		RequestLifeCycleContext.get().setPhase(phase);
		
		
	}
	
	public String processTree(Map<String,String> treeMap,CommBaseCreateFile files,List<Map<String,String>> listMap_Rela){
		ViewPartMeta viewPartMeta=files.getMapViewPartMeta().get("main");
		Boolean isNoRelation=listMap_Rela.size()!=0;
	   	DatasetConfExpand datasetConf=getDatasetConf(treeMap, viewPartMeta);
   		datasetConf.setIsNORelation(isNoRelation);
	   	datasetConf.setTreePTabDs(treeMap.get("tabDsId"));
	 	String dsId=datasetConf.getDsId();
	   	datasetConf.setMasterDs(dsId);//用于树ds 和 表ds关联
		AddMdDataSetToViewPartMeta(datasetConf);
		if(isNoRelation){
			configDatasetRelation(viewPartMeta, listMap_Rela);
		}
        PagePartMeta pagePartMeta=files.getPagePartMeta();
		String ctrlId= addCtrlToViewPartMeta(pagePartMeta, viewPartMeta, dsId, ctrlType_TreeViewComp);//main.view.xml添加树控件
		TreeViewComp treeViewComp=(TreeViewComp)viewPartMeta.getViewComponents().getComponent(ctrlId);//树对象
		
		treeViewComp.setText(datasetConf.getDispName());//树控件 设置跟节点
		RecursiveTreeLevel treeLevel1=getTreeLevel1();
		treeViewComp.setTopLevel(treeLevel1);//树控件 level1设置
		
		if(StringUtils.equals(treeMap.get("treeIsEdit"), "true")){//判断是否编辑树
			setContextMenu();
			ContextMenuComp contextMenu=getContextMenu();//获取树的右键菜单
			viewPartMeta.getViewMenus().addContextMenu(contextMenu);//将ContextMenuComp 添加到 视图
			treeLevel1.setContextMenu(contextMenu.getId());
			{//main.view.xml的树添加事件
				String controller=viewPartMeta.getController();
				Map<String,String>map=new HashMap<String, String>();
				map.put("EditViewId", treeEditView_Id);
				map.put("dsId", treeMap.get("dsId"));
				map.put("controller",controller);
				
				map.put("aggDo", treeMap.get("aggDo"));
				map.put("tabDsId", treeMap.get("tabDsId"));
				
				setEventConf_treeTabCardTreeContextMenuComp(contextMenu, map);
			}	
			{
				files._new_view(treeEditView_Id,"");
			    ViewPartMeta treeEditViewPartMeta=files.getMapViewPartMeta().get(treeEditView_Id);//树编辑的ViewPartMeta
			    UIPartMeta treeEditViewUIPartMeta=files.getMapViewUiPartMeta().get(treeEditView_Id);//树编辑的UIPartMeta
			    UiCtrlEditLayout treeEditLayout=new  UiCtrlEditLayout();//树的编辑弹出框布局对象
			    treeEditViewUIPartMeta.setElement(treeEditLayout.getLayoutMain());//将布局对象添加到layout.xml里
			   	DatasetConfExpand datasetConf_Tree=getDatasetConf(treeMap, treeEditViewPartMeta);
			   	datasetConf_Tree.setIsAfterSelRowEvent(false);
			  
			   	datasetConf_Tree.setTreePTabDs(treeMap.get("tabDsId"));
			    dsId=datasetConf_Tree.getDsId();
			    datasetConf_Tree.setMasterDs("");//用于树ds 和 表ds关联,此处设置为空不关联
				AddMdDataSetToViewPartMeta(datasetConf_Tree);
				{//重新配置 treeEditView 的onload事件
					String operDsId=treeMap.get("dsId");
					setEventConf_AddOrEditBeforeShow(treeEditViewPartMeta,operDsId);
				}
				String formCompId= addCtrlToViewPartMeta(pagePartMeta, treeEditViewPartMeta, dsId, ctrlType_FormComp);
				treeEditLayout.getFormComp().setId(formCompId);
				String buttonSaveId= addCtrlToViewPartMeta(pagePartMeta, treeEditViewPartMeta, SigTabPopUpOper.editView_btnSaveId, SigTabPopUpOper.ctrlType_ButtonComp);
				{
					Map<String,String> map_btn=new HashMap<String, String>();
					map_btn.put("dsId",  treeMap.get("dsId"));
					map_btn.put("btnId", buttonSaveId);
					setEventConf_Par_PopUpWinButtonSave(treeEditViewPartMeta, map_btn);
				}
				String buttonCancelId= addCtrlToViewPartMeta(pagePartMeta, treeEditViewPartMeta, SigTabPopUpOper.editView_btnCancelId, SigTabPopUpOper.ctrlType_ButtonComp);
				{
					Map<String,String> map1=new HashMap<String, String>();
					map1.put("viewId", treeEditView_Id);
					map1.put("btnId", buttonCancelId);
					setEventConf_PopUpWinButtonCancel(treeEditViewPartMeta, map1);
				}
			}
		}
		return ctrlId;
	}
}
