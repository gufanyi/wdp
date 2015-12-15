package xap.lui.psn.guided;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import xap.lui.core.cache.PaCache;
import xap.lui.core.common.InteractionUtil;
import xap.lui.core.comps.FormElement;
import xap.lui.core.comps.ImageComp;
import xap.lui.core.dataset.ComboData;
import xap.lui.core.dataset.DataItem;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.Field;
import xap.lui.core.dataset.Row;
import xap.lui.core.event.DatasetCellEvent;
import xap.lui.core.event.DatasetEvent;
import xap.lui.core.event.MouseEvent;
import xap.lui.core.event.TextEvent;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.layout.UICardLayout;
import xap.lui.core.layout.UIElement;
import xap.lui.core.layout.UIElementFinder;
import xap.lui.core.layout.UITabComp;
import xap.lui.core.model.AppSession;
import xap.lui.core.model.LuiAppUtil;
import xap.lui.core.model.ViewPartContext;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.core.refrence.SelfDefRefNode;
import xap.lui.psn.cmd.LuiAddRowCmd;
import xap.lui.psn.cmd.LuiEditRowCmd;
import xap.lui.psn.cmd.LuiRemoveRowCmd;
import xap.lui.psn.top.TopMainViewController;

public class GuidedCfgController {
	public static final String editType_fullTab="fullTab";
	public static final String editType_cardTab="cardTab";
	public static final String editType_popUpWin="popUpWin";
	
	public static final String nodeType_sigTab="sigTab";
	public static final String nodeType_parChiTab="parChiTab";
	public static final String nodeType_treeTab="treeTab";
	public static final String nodeType_treeTabTab="treeTabTab";
	
	public static final String dsType_treeDataset="treeDataset";
	public static final String dsType_parTabDataset="parTabDataset";
	public static final String dsType_chiTabDataset="chiTabDataset";
	


	public static final String ctrlType_GridComp="GridComp";
	public static final String ctrlType_ToolBarComp="ToolBarComp";
	public static final String ctrlType_MenuBarComp="MenubarComp";
	public static final String ctrlType_FormComp="FormComp";
	
	public static final String ctrlFlagType_GridComp_Par="GridComp_Par";//主表控件类型
	public static final String ctrlFlagType_GridComp_Chi="GridComp_Chi";//子表控件类型
	public static final String ctrlFlagType_FormComp_Par="FormComp_Par";
	public static final String ctrlFlagType_FormComp_Chi="FormComp_Chi";
	
	public static final String ctrlItem_ToolBar_new="toolbaritem_new";
	public static final String ctrlItem_ToolBar_edit="toolbaritem_edit";
	public static final String ctrlItem_ToolBar_delete="toolbaritem_delete";
	public static final String ctrlItem_ToolBar_save="toolbaritem_save";
	public static final String ctrlItem_ToolBar_cancel="toolbaritem_cancel";
	public static final String ctrlItem_MenuBar_new="_menuitem_new";
	public static final String ctrlItem_MenuBar_edit="_menuitem_edit";
	public static final String ctrlItem_MenuBar_delete="_menuitem_delete";
	
	
	List<Map<String,String>> listMap=new ArrayList<Map<String,String>>();
	private Dataset sourceDateset;
	
	public void switchCard(MouseEvent e) {
		ViewPartContext viewPart = LuiAppUtil.getCntWindowCtx().getViewContext("main");
		UICardLayout cardLayout = (UICardLayout) viewPart.getUIMeta().findChildById("card_TabCat");
		ImageComp img = (ImageComp) e.getSource();
		String imgId = img.getId();
		switch (imgId) {
		case "img_sigTab": {
			imgId = "0";
			break;
		}
		case "img_parChiTab": {
			imgId = "1";
			break;
		}
		case "img_treeTab": {
			imgId = "2";
			break;
		}
		case "img_treeTabTab": {
			imgId = "3";
			break;
		}
		}
		cardLayout.setCurrentItem(imgId);
	}
	/**
	 * 数据加载，初始化表单数据
	 * 
	 * @param DatasetEvent
	 */
	public void form_SigTab_OnDataLoad(DatasetEvent e) {
		Dataset ds = e.getSource();
		Row row = ds.getEmptyRow();
		row.setValue(ds.nameToIndex("editType"), editType_cardTab);//单表设置默认编辑类型
		row.setEdit(true);
		ds.addRow(row);
		ds.setRowSelectIndex(0);
	}
	public void ds_treeTabTab_tree_onDataLoad(DatasetEvent e) {
		Dataset ds = e.getSource();
		Row row = ds.getEmptyRow();
		row.setValue(ds.nameToIndex("editType"), editType_popUpWin);//单表设置默认编辑类型
		row.setEdit(true);
		ds.addRow(row);
		ds.setRowSelectIndex(0);
	}
	public void ds_page_OnDataLoad(DatasetEvent e) {
		Dataset ds = e.getSource();
		Row row = ds.getEmptyRow();
		row.setEdit(true);
		ds.addRow(row);
		ds.setRowSelectIndex(0);
	}
	
	
	public void isSimpQuery_valueChanged(TextEvent e){
		FormElement isSimpQuery=(FormElement)e.getSource();
		Dataset ds=LuiAppUtil.getDataset("ds_Page");
		Boolean value=(Boolean)ds.getRowValue("isSimpQuery");
		FormElement simpQueryNodeCode= isSimpQuery.getParent().getElementById("simpQueryNodeCode");
		simpQueryNodeCode.setEnabled(value);
		simpQueryNodeCode.setFocus(value);
	}
	

	
	//单表保存逻辑
	public void btn_sigTab_Save_onclick(MouseEvent e){
		Dataset ds=	LuiAppUtil.getDataset("ds_sigTab", "main");
		Row row=ds.getSelectedRow();
//		Map<String,String> map=new HashMap<String, String>();
//		map.put("pageId", strTxt_PageId);
//		map.put("viewId", "main");
//		map.put("dsId",(String)row.getValue(ds.nameToIndex("dsid")));
//		map.put("name",(String)row.getValue(ds.nameToIndex("name")));
//		map.put("ref",(String)row.getValue(ds.nameToIndex("ref")));
//		map.put("isAfterSelRowEvent","false");
//	    map.put("pageSize", "20");
//	    map.put("uiLayoutType", "UiSigTabCardLayout");
	
	    Map<String,String> map=getMapPara(ds,row);
	    map.put("isParTabStatus", "true");
	    map.put("isDatasetLoad","true");
		String editType=(String)row.getValue(ds.nameToIndex("editType"));
		
		CommBaseCreateComp sigTabOper=null;
		CommBaseCreateFile files=null;
		if(StringUtils.equals(editType,editType_cardTab)){
			map.put("uiLayoutType","UiSigTabCardLayout");
		    sigTabOper=new SigTabCardOper(map);
		    files=((SigTabCardOper)sigTabOper).getFiles();
		}else if(StringUtils.equals(editType,editType_popUpWin)){
		    map.put("uiLayoutType","UiSigTabPopUpLayout");
		    sigTabOper=new SigTabPopUpOper(map);
			files=((SigTabPopUpOper)sigTabOper).getFiles();
		}else if(StringUtils.equals(editType,editType_fullTab)){
			map.put("uiLayoutType","UiSigTabFullLayout");
			sigTabOper=new SigTabFullOper(map);
			files=((SigTabFullOper)sigTabOper).getFiles();
		}
		sigTabOper.saveFiles(files);
		AppSession.current().getAppContext().closeWinDialog();//关闭当前弹出的窗口
		InteractionUtil.showMessageDialog("单表保存成功！");
		
	}
	//主子表保存逻辑
	public void btn_parChiTab_onclick(MouseEvent e){
		Dataset ds_Par=	LuiAppUtil.getDataset("ds_parChiTab","main");//主表配置信息
		Row row_Par=ds_Par.getSelectedRow();
		Map<String,String> map=getMapPara(ds_Par,row_Par);
	    {
		    map.put("isAfterSelRowEvent","true");
		    map.put("isDatasetLoad","true");
		    map.put("isParTabStatus", "true");
			map.put("aggDo", (String)row_Par.getValue(ds_Par.nameToIndex("aggDo")));
		    map.put("pageSize", "5");
	    }
		Dataset ds_Chi=	LuiAppUtil.getDataset("ds_parChiTab_Chi","main");//子表表配置信息，可能多行
		Row[] rows_Chi=ds_Chi.getCurrentPageData().getRows();
		List<Map<String,String>> listMap=new ArrayList<Map<String,String>>();
		for(Row row:rows_Chi){
			Map<String,String> map_Chi=getMapPara(ds_Chi,row);
			map_Chi.put("pageSize", "5");
			listMap.add(map_Chi);
		}
		List<Map<String,String>> listMap_Rela=getDsRelaMap("relationsds");
		
		CommBaseCreateComp parChiTabOper=null;
		CommBaseCreateFile files=null;
		String editType_Par=(String)row_Par.getValue(ds_Par.nameToIndex("editType"));
		String editType_Chi=(String)rows_Chi[0].getValue(ds_Chi.nameToIndex("editType"));
		if(StringUtils.equals(editType_Par,editType_cardTab)){
			if(StringUtils.equals(editType_Chi,editType_cardTab)){
				map.put("uiLayoutType", "UiParChiTabCardCardLayout");
				parChiTabOper=new ParChiTabCardCardOper(map, listMap,listMap_Rela);
				files=((ParChiTabCardCardOper)parChiTabOper).getFiles();
			}else if(StringUtils.equals(editType_Chi,editType_fullTab)){
				map.put("uiLayoutType", "UiParChiTabCardFullLayout");
				parChiTabOper=new ParChiTabCardFullOper(map, listMap,listMap_Rela);
				files=((ParChiTabCardFullOper)parChiTabOper).getFiles();
				
			}else if(StringUtils.equals(editType_Chi,editType_popUpWin)){
				map.put("uiLayoutType", "UiParChiTabCardPopUpLayout");
				parChiTabOper=new ParChiTabCardPopUpOper(map, listMap,listMap_Rela);
				files=((ParChiTabCardPopUpOper)parChiTabOper).getFiles();
			}
		}else if(StringUtils.equals(editType_Par,editType_popUpWin)){
			if(StringUtils.equals(editType_Chi,editType_cardTab)){
				map.put("uiLayoutType", "UiParChiTabPopUpCardLayout");
				parChiTabOper=new ParChiTabPopUpCardOper(map, listMap,listMap_Rela);
				files=((ParChiTabPopUpCardOper)parChiTabOper).getFiles();
			}else if(StringUtils.equals(editType_Chi,editType_popUpWin)){
				map.put("uiLayoutType", "UiParChiTabPopUpPopUpLayout");
				parChiTabOper=new ParChiTabPopUpPopUpOper(map, listMap,listMap_Rela);
				files=((ParChiTabPopUpPopUpOper)parChiTabOper).getFiles();
			}else if(StringUtils.equals(editType_Chi,editType_fullTab)){
				map.put("uiLayoutType", "UiParChiTabPopUpFullLayout");
				parChiTabOper=new ParChiTabPopUpFullOper(map, listMap,listMap_Rela);
				files=((ParChiTabPopUpFullOper)parChiTabOper).getFiles();
			}

		}else if(StringUtils.equals(editType_Par,editType_fullTab)){
			if(StringUtils.equals(editType_Chi,editType_cardTab)){
				map.put("uiLayoutType", "UiParChiTabFullCardLayout");
				parChiTabOper=new ParChiTabFullCardOper(map, listMap,listMap_Rela);
				files=((ParChiTabFullCardOper)parChiTabOper).getFiles();
			}else if(StringUtils.equals(editType_Chi,editType_popUpWin)){
				map.put("uiLayoutType", "UiParChiTabFullPopUpLayout");
				parChiTabOper=new ParChiTabFullPopUpOper(map, listMap,listMap_Rela);
				files=((ParChiTabFullPopUpOper)parChiTabOper).getFiles();
			}else if(StringUtils.equals(editType_Chi,editType_fullTab)){
				map.put("uiLayoutType", "UiParChiTabFullFullLayout");
				parChiTabOper=new ParChiTabFullFullOper(map, listMap,listMap_Rela);
				files=((ParChiTabFullFullOper)parChiTabOper).getFiles();
			}
		}
		parChiTabOper.saveFiles(files);
		AppSession.current().getAppContext().closeWinDialog();//关闭当前弹出的窗口
		InteractionUtil.showMessageDialog("主子保存逻辑");
	}
		
	//树表保存逻辑
	public void btn_treeTab_Save_onclick(MouseEvent e){
		Dataset ds_Tree=LuiAppUtil.getDataset("ds_treeTab_tree","main");//主表配置信息
		Row row_Tree=ds_Tree.getSelectedRow();
		Map<String,String> map_Tree=getMapPara(ds_Tree,row_Tree);
	    {
	    	map_Tree.put("isDatasetLoad","true");
	    	map_Tree.put("isParTabStatus", "true");
	    	map_Tree.put("treeIsEdit", (Boolean)ds_Tree.getSelectedRow().getValue(ds_Tree.nameToIndex("isEditTree"))?"true":"false");
	    	map_Tree.put("isAfterSelRowEvent","true");
	    	map_Tree.put("pageSize", "-1");
	    }

		Dataset ds_Par=	LuiAppUtil.getDataset("ds_treeTab_tab","main");//主表配置信息
		Row row_Par=ds_Par.getSelectedRow();
		Map<String,String> map=getMapPara(ds_Par,row_Par);
		
	    
		String editType_Par=(String)row_Par.getValue(ds_Par.nameToIndex("editType"));
		CommBaseCreateComp treeTabOper=null;
		CommBaseCreateFile files=null;
		if(StringUtils.equals(editType_Par,editType_cardTab)){
			map.put("uiLayoutType", "UiTreeTabCardLayout");
			treeTabOper=new TreeTabCardOper(map_Tree,map,getDsRelaMap("relationsTreeDs"));
			files=((TreeTabCardOper)treeTabOper).getFiles();
		}else if(StringUtils.equals(editType_Par,editType_popUpWin)){
			map.put("uiLayoutType", "UiTreeTabPopUpLayout");
			treeTabOper=new TreeTabPopUpOper(map_Tree,map,getDsRelaMap("relationsTreeDs"));
			files=((TreeTabPopUpOper)treeTabOper).getFiles();
		}else if(StringUtils.equals(editType_Par,editType_fullTab)){
			map.put("uiLayoutType", "UiTreeTabFullLayout");
			treeTabOper=new TreeTabFullOper(map_Tree,map,getDsRelaMap("relationsTreeDs"));
			files=((TreeTabFullOper)treeTabOper).getFiles();
		}
		treeTabOper.saveFiles(files);
		AppSession.current().getAppContext().closeWinDialog();//关闭当前弹出的窗口
		InteractionUtil.showMessageDialog("树表保存逻辑");
		
	}
	//树表表 保存逻辑
	public void btn_treeTabTab_Save_onclick(MouseEvent e){
		
		Dataset ds_Tree=LuiAppUtil.getDataset("ds_treeTabTab_tree","main");//主表配置信息
		Row row_Tree=ds_Tree.getSelectedRow();
		Map<String,String> map_Tree=getMapPara(ds_Tree,row_Tree);
	    {	
	    	map_Tree.put("isDatasetLoad","true");
	    	map_Tree.put("isParTabStatus", "true");
	    	map_Tree.put("isAfterSelRowEvent","true");
	    	map_Tree.put("pageSize", "-1");
	    	map_Tree.put("treeIsEdit", (Boolean)ds_Tree.getSelectedRow().getValue(ds_Tree.nameToIndex("isEditTree"))?"true":"false");
	    }
	    
		Dataset ds_Par=	LuiAppUtil.getDataset("ds_treeTabTab_pTab","main");//主表配置信息
		Row row_Par=ds_Par.getSelectedRow();
		Map<String,String> map=getMapPara(ds_Par,row_Par);
	    {
		    map.put("isAfterSelRowEvent","true");
			map.put("aggDo", (String)row_Par.getValue(ds_Par.nameToIndex("aggDo")));
		    map.put("pageSize", "5");
	    }
	    
		Dataset ds_Chi=	LuiAppUtil.getDataset("ds_treeTabTab_ChiTab","main");//子表表配置信息，可能多行
		Row[] rows_Chi=ds_Chi.getCurrentPageData().getRows();
		List<Map<String,String>> listMap=new ArrayList<Map<String,String>>();
		for(Row row:rows_Chi){
			Map<String,String> map_Chi=getMapPara(ds_Chi,row);
			map_Chi.put("pageSize", "5");
			listMap.add(map_Chi);
		}
		
		String editType_Par=(String)row_Par.getValue(ds_Par.nameToIndex("editType"));
		String editType_Chi=(String)rows_Chi[0].getValue(ds_Chi.nameToIndex("editType"));
		CommBaseCreateComp treeTabTabOper=null;
		CommBaseCreateFile files=null;
		if(StringUtils.equals(editType_Par,editType_cardTab)){
			if(StringUtils.equals(editType_Chi,editType_cardTab)){
				 map.put("uiLayoutType", "UiTreeTabTabCardCardLayout");
				 treeTabTabOper=new TreeTabTabCardCardOper(map_Tree,map,listMap,getDsRelaMap("relationsds"),getDsRelaMap("relationsTreeDs"));
				 files=((TreeTabTabCardCardOper)treeTabTabOper).getFiles();
			}else if(StringUtils.equals(editType_Chi,editType_fullTab)){
				 map.put("uiLayoutType", "UiTreeTabTabCardFullLayout");
				 treeTabTabOper=new TreeTabTabCardFullOper(map_Tree,map,listMap,getDsRelaMap("relationsds"),getDsRelaMap("relationsTreeDs"));
				 files=((TreeTabTabCardFullOper)treeTabTabOper).getFiles();
			}else if(StringUtils.equals(editType_Chi,editType_popUpWin)){
				 map.put("uiLayoutType", "UiTreeTabTabCardPopUpLayout");
				 treeTabTabOper=new TreeTabTabCardPopUpOper(map_Tree,map,listMap,getDsRelaMap("relationsds"),getDsRelaMap("relationsTreeDs"));
				 files=((TreeTabTabCardPopUpOper)treeTabTabOper).getFiles();
			}
		}else if(StringUtils.equals(editType_Par,editType_popUpWin)){
			if(StringUtils.equals(editType_Chi,editType_cardTab)){
				 map.put("uiLayoutType", "UiTreeTabTabPopUpCardLayout");
				 treeTabTabOper=new TreeTabTabPopUpCardOper(map_Tree,map,listMap,getDsRelaMap("relationsds"),getDsRelaMap("relationsTreeDs"));
				 files=((TreeTabTabPopUpCardOper)treeTabTabOper).getFiles();
			}else if(StringUtils.equals(editType_Chi,editType_fullTab)){
				 map.put("uiLayoutType", "UiTreeTabTabPopUpFullLayout");
				 treeTabTabOper=new TreeTabTabPopUpFullOper(map_Tree,map,listMap,getDsRelaMap("relationsds"),getDsRelaMap("relationsTreeDs"));
				 files=((TreeTabTabPopUpFullOper)treeTabTabOper).getFiles();
			}else if(StringUtils.equals(editType_Chi,editType_popUpWin)){
				 map.put("uiLayoutType", "UiTreeTabTabPopUpPopUpLayout");
				 treeTabTabOper=new TreeTabTabPopUpPopUpOper(map_Tree,map,listMap,getDsRelaMap("relationsds"),getDsRelaMap("relationsTreeDs"));
				 files=((TreeTabTabPopUpPopUpOper)treeTabTabOper).getFiles();
			}
		}else if(StringUtils.equals(editType_Par,editType_fullTab)){
			if(StringUtils.equals(editType_Chi,editType_cardTab)){
				 map.put("uiLayoutType", "UiTreeTabTabFullCardLayout");
				 treeTabTabOper=new TreeTabTabFullCardOper(map_Tree,map,listMap,getDsRelaMap("relationsds"),getDsRelaMap("relationsTreeDs"));
				 files=((TreeTabTabFullCardOper)treeTabTabOper).getFiles();
			}else if(StringUtils.equals(editType_Chi,editType_fullTab)){
				 map.put("uiLayoutType", "UiTreeTabTabFullFullLayout");
				 treeTabTabOper=new TreeTabTabFullFullOper(map_Tree,map,listMap,getDsRelaMap("relationsds"),getDsRelaMap("relationsTreeDs"));
				 files=((TreeTabTabFullFullOper)treeTabTabOper).getFiles();
			}else if(StringUtils.equals(editType_Chi,editType_popUpWin)){
				map.put("uiLayoutType", "UiTreeTabTabFullPopUpLayout");
				treeTabTabOper=new TreeTabTabFullPopUpOper(map_Tree,map,listMap,getDsRelaMap("relationsds"),getDsRelaMap("relationsTreeDs"));
				files=((TreeTabTabFullPopUpOper)treeTabTabOper).getFiles();
				
			}
		}
		treeTabTabOper.saveFiles(files);
		AppSession.current().getAppContext().closeWinDialog();//关闭当前弹出的窗口
		InteractionUtil.showMessageDialog("树表表保存逻辑");
	}
	
	public Map<String,String> getMapPara(Dataset ds,Row row){
		Map<String,String>map=new HashMap<String, String>();
		Dataset dsPage=LuiAppUtil.getDataset("ds_Page");
		Row rowP=dsPage.getSelectedRow();
		String pageId=(String)rowP.getValue(dsPage.nameToIndex("txtPageId"));
		if(StringUtils.isBlank(pageId))
			throw new LuiRuntimeException("未指定节点id");
		map.put("pageId", pageId);
		if((Boolean)rowP.getValue(dsPage.nameToIndex("isSimpQuery"))){
			String simpQueryNodeCode=(String)rowP.getValue(dsPage.nameToIndex("simpQueryNodeCode"));
			if(StringUtils.isBlank(simpQueryNodeCode))
				throw new LuiRuntimeException("请指定查询模版NodeCode");
			map.put("simpQueryNodeCode", simpQueryNodeCode);
		}
		map.put("viewId", "main");
		map.put("dsId",(String)row.getValue(ds.nameToIndex("dsid")));
		map.put("name",(String)row.getValue(ds.nameToIndex("name")));
		map.put("ref",(String)row.getValue(ds.nameToIndex("ref")));
		map.put("isAfterSelRowEvent","false");
	    map.put("pageSize", "20");
	    map.put("uiLayoutType", "UiSigTabCardLayout");
	    return map;
	}
	
	private List<Map<String,String>> getDsRelaMap(String relationDsId){
	     Dataset ds=LuiAppUtil.getDataset(relationDsId,"main");
		 List<Map<String,String>> listMap= new ArrayList<Map<String,String>>();
		 Row[] rows=ds.getCurrentPageData().getRows();
		 for(Row row:rows){
			 Map<String,String> map=new HashMap<String, String>();
			 map.put("masterDsId", (String)row.getValue(ds.nameToIndex("masterds")));
			 map.put("masterKey", (String)row.getValue(ds.nameToIndex("masterkey")));
			 map.put("detailDsId", (String)row.getValue(ds.nameToIndex("detailds")));
			 map.put("detailKey", (String)row.getValue(ds.nameToIndex("detailkey")));
			 if(StringUtils.equals(relationDsId, "relationsTreeDs"))
				 map.put("isTree", "true");
			 listMap.add(map);
		 }
		return listMap;
	}

    //添加行-主子结构子表配置
	public void menu_parChiTab_menuitem_new_onclick(MouseEvent e){
		new LuiAddRowCmd("ds_parChiTab_Chi").execute();
        Dataset ds=	LuiAppUtil.getDataset("ds_parChiTab_Chi");
        ds.getSelectedRow().setValue(ds.nameToIndex("editType"), editType_fullTab);
	}
	//编辑行
	public void menu_parChiTab_menuitem_edit_onclick(MouseEvent e){
		new LuiEditRowCmd("ds_parChiTab_Chi").execute();
	}
	//删除行
	public void menu_parChiTab_menuitem_delete_onclick(MouseEvent e){
		new LuiRemoveRowCmd("ds_parChiTab_Chi").execute();
	}
	//添加行
	public void menu_treeTabTab_menuitem_new_onclick(MouseEvent e){
		new LuiAddRowCmd("ds_treeTabTab_ChiTab").execute();
	    Dataset ds=	LuiAppUtil.getDataset("ds_treeTabTab_ChiTab");
    	ds.getSelectedRow().setValue(ds.nameToIndex("editType"), editType_fullTab);
	}
	//编辑行
	public void menu_treeTabTab_menuitem_edit_onclick(MouseEvent e){
		new  LuiEditRowCmd("ds_treeTabTab_ChiTab").execute();
	}
	
	//删除行
	public void menu_treeTabTab_menuitem_delete_onclick(MouseEvent e){
		new LuiRemoveRowCmd("ds_treeTabTab_ChiTab").execute();
	}
	
	//配置数据集关系
	public void onAfterDataChange(DatasetCellEvent datasetCell) {
		ViewPartMeta mainWidget =LuiAppUtil.getCntView();
		Dataset ds = datasetCell.getSource();
		String writeDsid=ds.getId();
		Row row = ds.getSelectedRow();
		String pageId = "guided";
		String viewId =TopMainViewController.vPM_cfgDsRela;
		if (row == null) return;
		String dsId = String.valueOf(row.getValue(ds.nameToIndex("detailds")));
		SelfDefRefNode refNode = (SelfDefRefNode)(mainWidget.getViewModels().getRefNode("refnode_fields"));
		String path = refNode.getPath();
		if (path.indexOf("&writeDs") != -1) {
			path = path.substring(0, path.indexOf("&writeDs"));
		}
		String url = path+"&writeDs="+writeDsid + "&currentDsId=" + dsId + "&sourceWinId=" + pageId + "&sourceView=" + viewId + "&pi=" + UUID.randomUUID().toString();
		refNode.setPath(url);
	}
	
	//数据集关联 的 MenuBar MenuItem 删除 事件
	public void onDelRelation(MouseEvent mouseEvent) {
		UIElement ele = (UIElement) AppSession.current().getWindowContext().getUIPartMeta();
		UICardLayout card = (UICardLayout) UIElementFinder.findElementById(ele, "card_TabCat");
		if (StringUtils.equals(card.getCurrentItem(), "1")) {
			new LuiRemoveRowCmd("relationsds").execute();
		}else if(StringUtils.equals(card.getCurrentItem(), "2")) {
			new LuiRemoveRowCmd("relationsTreeDs").execute();
		}else if(StringUtils.equals(card.getCurrentItem(), "3")) {
			UITabComp tab = (UITabComp) UIElementFinder.findElementById(ele, "tag4637");
			if (new Integer(1).equals(tab.getCurrentItem())){
				new LuiRemoveRowCmd("relationsds").execute();
			}else if(new Integer(2).equals(tab.getCurrentItem())){
				new LuiRemoveRowCmd("relationsTreeDs").execute();
			}
		}
	}
	/**
	 * Tab增加
	 */
	public void onAddRelation(MouseEvent mouseEvent) {
		Dataset ds=null;
		UIElement ele = (UIElement) AppSession.current().getWindowContext().getUIPartMeta();
		UICardLayout card = (UICardLayout) UIElementFinder.findElementById(ele, "card_TabCat");
		if (StringUtils.equals(card.getCurrentItem(), "1")) {
			UITabComp tab = (UITabComp) UIElementFinder.findElementById(ele, "tag2637");
			if (new Integer(1).equals(tab.getCurrentItem())) {
				 ds = getMasterDs("ds_parChiTab", "ds_parChiTab_Chi", "relationsds");
			}
		}else if(StringUtils.equals(card.getCurrentItem(), "2")) {
			UITabComp tab = (UITabComp) UIElementFinder.findElementById(ele, "tag3637");
			if (new Integer(1).equals(tab.getCurrentItem())) {
				 ds = getMasterDs("ds_treeTab_tree", "ds_treeTab_tab", "relationsTreeDs");
			}
		}else if(StringUtils.equals(card.getCurrentItem(), "3")) {
			UITabComp tab = (UITabComp) UIElementFinder.findElementById(ele, "tag4637");
			if (new Integer(1).equals(tab.getCurrentItem())){
				 ds = getMasterDs("ds_treeTabTab_pTab", "ds_treeTabTab_ChiTab", "relationsds");
			}else if(new Integer(2).equals(tab.getCurrentItem())){
				 ds = getMasterDs("ds_treeTabTab_tree", "ds_treeTabTab_pTab", "relationsTreeDs");
			}
		}

		//initParams();
		String masterkey = getPrimaryKey(this.sourceDateset);
		if (masterkey == null) {
			throw new LuiRuntimeException("请先设置数据集的主键!");
		}
		addItemToDataList(this.sourceDateset);//主数据集Fields
		ds.setEdit(true);
		Row row = ds.getEmptyRow();
		row.setRowId(UUID.randomUUID().toString());
		row.setValue(ds.nameToIndex("id"), this.sourceDateset.getId() + "_" + UUID.randomUUID().toString().substring(0, 4));
		row.setValue(ds.nameToIndex("masterds"), this.sourceDateset.getId());
		row.setValue(ds.nameToIndex("masterkey"),masterkey);
		ds.addRow(row);
		// 选中新添加的行
		ds.setRowSelectIndex(ds.getRowIndex(row));
	}
	
	private Dataset getMasterDs(String masterDsId,String detailDsId,String relationDsId){
		ViewPartMeta viewPartMeta=(ViewPartMeta)PaCache.getInstance().get(TopMainViewController.vPM_cfgDsRela);
		ViewPartMeta mainWidget = LuiAppUtil.getCntView();
		String dsid=(String)LuiAppUtil.getDataset(masterDsId).getSelectedRow().getValue(0);
		if(StringUtils.isBlank(dsid)) throw new LuiRuntimeException("请选配置数据集");	
		
		addItemToDataList(viewPartMeta, detailDsId);
		
		this.sourceDateset=viewPartMeta.getViewModels().getDataset(dsid);
		Dataset ds = mainWidget.getViewModels().getDataset(relationDsId);
		return ds;
	}
	
	/**
	 * 主数据集的Fileds
	 */
	private void addItemToDataList(Dataset ds) {
		ComboData  dataList= (ComboData) LuiAppUtil.getCntView().getViewModels().getComboData("masterfield");
		dataList.removeAllDataItems();
		List<Field> list = ds.getFieldList();
		for (Field field : list) {
			DataItem item = new DataItem();
			String fid = field.getId();
			//String fname = field.getText();
			if(!"ts".equals(fid) && !"status".equals(fid) && !"dr".equals(fid) && !(fid.startsWith("vdef"))){
				item.setText(fid);
				item.setValue(fid);
				dataList.addDataItem(item);
			}
		}
	}
	/**
	 * 子数据集的Fields
	 */
	private void addItemToDataList(ViewPartMeta viewPartMeta,String dsId_chiTab) {
		ComboData  dataList= (ComboData) LuiAppUtil.getCntView().getViewModels().getComboData("dscombodata");
		Dataset ds=LuiAppUtil.getDataset(dsId_chiTab, "main") ;
			Row[] rows=ds.getCurrentPageData().getRows();
		dataList.removeAllDataItems();
		for (Row row : rows) {
			DataItem item = new DataItem();
			String dsId = (String)row.getValue(0);
			if(viewPartMeta.getViewModels().getDataset(dsId)!=null){
				item.setText(dsId);
				item.setValue(dsId);
				dataList.addDataItem(item);
			}
		}
	}
	private String getPrimaryKey(Dataset ds) {
		List<Field> list = ds.getFieldList();
		for (Field field : list) {
			if (field.isPK()) {
				return field.getId();
			}
		}
		return null;
	}
	

}
