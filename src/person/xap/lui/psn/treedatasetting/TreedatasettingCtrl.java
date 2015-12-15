package xap.lui.psn.treedatasetting;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import xap.lui.core.cache.PaCache;
import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.common.LuiWebSession;
import xap.lui.core.comps.ContextMenuComp;
import xap.lui.core.comps.FormComp;
import xap.lui.core.comps.RecursiveTreeLevel;
import xap.lui.core.comps.TreeLevel;
import xap.lui.core.comps.TreeViewComp;
import xap.lui.core.control.IWindowCtrl;
import xap.lui.core.dataset.ComboData;
import xap.lui.core.dataset.DataItem;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.Field;
import xap.lui.core.dataset.PageData;
import xap.lui.core.dataset.RefMdDataset;
import xap.lui.core.dataset.Row;
import xap.lui.core.event.DatasetCellEvent;
import xap.lui.core.event.DatasetEvent;
import xap.lui.core.event.MouseEvent;
import xap.lui.core.event.PageEvent;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.model.AppSession;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.model.LuiAppUtil;
import xap.lui.core.model.LuiPlugoutCmd;
import xap.lui.core.model.RequestLifeCycleContext;
import xap.lui.core.model.ViewPartMeta;

public class TreedatasettingCtrl implements IWindowCtrl {
	
	private static final String ID = "id";
	private static final String DATASET = "Dataset";
	private static final String TYPE = "type";
	private static final String MAIN = "main";
	private static final String REF_OK_PLUGOUT = "refOkPlugout";
	private static final String WRITE_FIELDS = "writeFields";	
	
	private String refIdOrWriteField = null;
	private String treeId = null;
	
	public void sysWindowClosed(PageEvent event) {
		LuiRuntimeContext.getWebContext().destroyWebSession();
	}
	//初始化参数
	private void initParams() {
		LuiWebSession session = LuiRuntimeContext.getWebContext().getPageWebSession();
		String refIdOrWriteField = session.getOriginalParameter("writeField");
		this.refIdOrWriteField = refIdOrWriteField;
	}
	public void onDataLoad_formds(DatasetEvent e)  {
		initParams();
		Dataset dsMiddle = AppSession.current().getAppContext().getWindowContext("pa").getViewContext("settings").getView().getViewModels().getDataset("ds_middle");
		Row dsMiddleRow = dsMiddle.getSelectedRow();
		String treeLevelId = (String) dsMiddleRow.getValue(dsMiddle.nameToIndex("ref_ext1"));
		Dataset ds = e.getSource();
		ds.setEdit(true);
		Row row = ds.getEmptyRow();
		if(StringUtils.equals(this.refIdOrWriteField, "ref_ext2")){//二级树
			if(StringUtils.isBlank(treeLevelId))
				throw new LuiRuntimeException("请先设置一级树！");
			String treeLevelId2 = (String) dsMiddleRow.getValue(dsMiddle.nameToIndex("ref_ext2"));
			if(StringUtils.isNotBlank(treeLevelId2)){
				row.setValue(ds.nameToIndex("TreeLevelID"), treeLevelId2);
			}else{
				row.setValue(ds.nameToIndex("TreeLevelID"), "level2");
			}
		}else{//一级树
			if(StringUtils.isNotBlank(treeLevelId)){
				row.setValue(ds.nameToIndex("TreeLevelID"), treeLevelId);
			}else{
				row.setValue(ds.nameToIndex("TreeLevelID"), "level1");
			}
		}
		ds.addRow(row);
		ds.setSelectedIndex(ds.getRowIndex(row));
		
		ViewPartMeta widget = PaCache.getEditorViewPartMeta();//当前编辑的视图
		Dataset[] datasets = widget.getViewModels().getDatasets();
		ComboData datasetList = null;
		if (ArrayUtils.isEmpty(datasets)){
			throw new LuiRuntimeException("请先创建数据集！");
		} else {
			datasetList = LuiAppUtil.getCntView().getViewModels().getComboData("datasetList");
			datasetList.removeAllDataItems();
			for (Dataset dss : datasets) {
				if(!(dss instanceof RefMdDataset)) {
					DataItem item = new DataItem();
					item.setText( StringUtils.isEmpty(dss.getCaption()) ? dss.getId() : dss.getCaption());
					item.setValue(dss.getId());
					datasetList.addDataItem(item);
				}
			}
		}
		
		FormComp form = (FormComp) LuiAppUtil.getCntView().getViewComponents().getComponent("datasetting_form");
		form.getElementById("foreignId").setEnabled(false);
		
		Row selRow = ds.getSelectedRow();
		String treeId = (String)dsMiddleRow.getValue(dsMiddle.nameToIndex("string_ext1"));
		this.treeId = treeId;
		TreeViewComp tree = getTree(treeId);
		if(StringUtils.equals(this.refIdOrWriteField, "ref_ext2")){//二级树
			TreeLevel topLevel = tree.getTopLevel();
			TreeLevel childLevel = topLevel.getChildTreeLevel();
			if(childLevel != null && StringUtils.isNotBlank(childLevel.getDataset())){
				selRow.setValue(ds.nameToIndex("datasetId"), childLevel.getDataset());
			}else{//如果是新增的level，则把下拉的第一个数据集作为默认值
				DataItem[] dis = datasetList.getAllDataItems();
				if(dis.length > 0)
					selRow.setValue(ds.nameToIndex("datasetId"), dis[0].getValue());
			}
		}else{//一级树
			TreeLevel topLevel = tree.getTopLevel();
			if(topLevel != null && StringUtils.isNotBlank(topLevel.getDataset())){
				selRow.setValue(ds.nameToIndex("datasetId"), topLevel.getDataset());
			}else{//如果是新增的level，则把下拉的第一个数据集作为默认值
				DataItem[] dis = datasetList.getAllDataItems();
				if(dis.length > 0)
					selRow.setValue(ds.nameToIndex("datasetId"), dis[0].getValue());
			}
		}
		
		String datasetId = (String) row.getValue(ds.nameToIndex("datasetId"));
		Dataset dataset0 = widget.getViewModels().getDataset(datasetId);//当前选中模型的dataset
		Dataset gridDs = LuiAppUtil.getCntView().getViewModels().getDataset("ds_grid");
		gridDs.clear();
		
		ComboData rightMenuList = LuiAppUtil.getCntView().getViewModels().getComboData("rightMenuList");
		//右键菜单combobox初始化
//		{
//			DataItem item = new DataItem("请选择","请选择");
//			item.setText("请选择");
//			rightMenuList.addDataItem(item);
//		}
		ContextMenuComp[] contextMenus=widget.getViewMenus().getContextMenus();//获取当前编辑视图的 右键菜单（ContextMenus）
		if(contextMenus!=null&&!ArrayUtils.isEmpty(contextMenus)){
			//右键菜单项目不为空的情况
			for(ContextMenuComp contextMenu:contextMenus){
				DataItem item = new DataItem(contextMenu.getId(),contextMenu.getId());
				item.setText(contextMenu.getId());
				rightMenuList.addDataItem(item);//向右键菜单字段combobox的参考数据集list里添加 list项目
			}
			selRow.setValue(ds.nameToIndex("rightMenuId"), rightMenuList.getAllDataItems()[0].getText());//递归字段设置默认值
		}
		
		
		ComboData recIdList = LuiAppUtil.getCntView().getViewModels().getComboData("recIdList");
		ComboData recPIdList = LuiAppUtil.getCntView().getViewModels().getComboData("recPIdList");
		ComboData priIdList = LuiAppUtil.getCntView().getViewModels().getComboData("priIdList");

		recIdList.removeAllDataItems();
		recPIdList.removeAllDataItems();
		priIdList.removeAllDataItems();
		for(Field fl : dataset0.getFieldList()){
			DataItem item = new DataItem(fl.getId(),fl.getText());
			item.setValue(fl.getId());
//			item.setText(StringUtils.isBlank(fl.getText()) ? fl.getId():fl.getText());
			item.setText(fl.getId());
			recIdList.addDataItem(item);//向递归字段combobox的参考数据集list里添加 list项目
			recPIdList.addDataItem(item);//向父递归字段combobox的参考数据集list里添加 list项目
			priIdList.addDataItem(item);//向主键combobox的参考数据集list里添加 list项目
			
			Row emprow = gridDs.getEmptyRow();
			emprow.setValue(gridDs.nameToIndex("showFields"), fl.getId());
			gridDs.addRow(emprow);
		}
		
		DataItem[] recItems = recIdList.getAllDataItems();
		if(!ArrayUtils.isEmpty(recItems)){
			RecursiveTreeLevel recTreeLevel = null;
			if(StringUtils.equals(this.refIdOrWriteField, "ref_ext2")){//二级树
				TreeLevel topLevel = tree.getTopLevel();
				recTreeLevel =  (RecursiveTreeLevel) topLevel.getChildTreeLevel();
				if(recTreeLevel != null && StringUtils.isNotBlank(recTreeLevel.getDetailKeyParameter()))
					selRow.setValue(ds.nameToIndex("foreignId"), recTreeLevel.getDetailKeyParameter());
				else
					selRow.setValue(ds.nameToIndex("foreignId"), topLevel.getMasterField());
			}else{
				recTreeLevel =  (RecursiveTreeLevel) tree.getTopLevel();
			}
			if(recTreeLevel != null){
				if(StringUtils.isNotBlank(recTreeLevel.getRecursiveField()))
					selRow.setValue(ds.nameToIndex("recursiveId"), recTreeLevel.getRecursiveField());
				else
					selRow.setValue(ds.nameToIndex("recursiveId"), recItems[0].getText());
				if(StringUtils.isNotBlank(recTreeLevel.getRecursiveParentField()))
					selRow.setValue(ds.nameToIndex("recursivePId"), recTreeLevel.getRecursiveParentField());
				else
					selRow.setValue(ds.nameToIndex("recursivePId"), recItems[0].getText());
				if(StringUtils.isNotBlank(recTreeLevel.getMasterField()))
					selRow.setValue(ds.nameToIndex("primaryId"), recTreeLevel.getMasterField());
				else
					selRow.setValue(ds.nameToIndex("primaryId"), recItems[0].getText());
				if(StringUtils.isNotBlank(recTreeLevel.getLabelDelims()))
					selRow.setValue(ds.nameToIndex("splitFlag"), recTreeLevel.getLabelDelims());
				else
					selRow.setValue(ds.nameToIndex("splitFlag"), ",");
				if(StringUtils.isNotBlank(recTreeLevel.getContextMenu()))
					selRow.setValue(ds.nameToIndex("rightMenuId"), recTreeLevel.getContextMenu());
				else
					if(!ArrayUtils.isEmpty(rightMenuList.getAllDataItems()))
						selRow.setValue(ds.nameToIndex("rightMenuId"), rightMenuList.getAllDataItems()[0].getText());
				String labelFields = "";
				if(StringUtils.isNotBlank(recTreeLevel.getLabelFields())){
					labelFields = recTreeLevel.getLabelFields();
				}
				//显示字段
				String[] labelFieldString = null;
				if(StringUtils.isNotBlank(labelFields)){
					labelFieldString = StringUtils.split(labelFields, recTreeLevel.getLabelDelims());//labelFields.split(recTreeLevel.getLabelDelims());
				}
				if(labelFieldString != null && labelFieldString.length > 0){
					Integer[] indices = new Integer[labelFieldString.length];
					PageData pageData = gridDs.getCurrentPageData();
					if(pageData != null){
						Row[] rows = pageData.getRows();
						int fieldId = gridDs.nameToIndex("showFields");
						for(int i=0; i<labelFieldString.length; i++){
							for(Row r : rows){
								if(labelFieldString[i].equals(r.getValue(fieldId))){
									indices[i] = gridDs.getRowIndex(r);
								}
							}
						}
					}
					gridDs.setRowSelectIndices(indices);
				}
			}else{
				selRow.setValue(ds.nameToIndex("recursiveId"), recItems[0].getText());//递归字段设置默认值
				selRow.setValue(ds.nameToIndex("recursivePId"), recItems[0].getText());//父递归字段设置默认值
				selRow.setValue(ds.nameToIndex("primaryId"), recItems[0].getText());//主键设置默认值
				selRow.setValue(ds.nameToIndex("splitFlag"), ",");
				if(!ArrayUtils.isEmpty(rightMenuList.getAllDataItems()))
					selRow.setValue(ds.nameToIndex("rightMenuId"), rightMenuList.getAllDataItems()[0].getText());
			}
		}
	}
	private TreeViewComp getTree(String treeId) {
		ViewPartMeta viewPart = PaCache.getEditorViewPartMeta();
		TreeViewComp tree = (TreeViewComp)viewPart.getViewComponents().getComponent(treeId);
		return tree;
	}
	
	public void datasetValueChanged(DatasetCellEvent e) {
		initParams();
		ViewPartMeta widget = PaCache.getEditorViewPartMeta();
		
		int index0 = e.getColIndex();
		Dataset formDs = e.getSource();//LuiAppUtil.getCntView().getViewModels().getDataset("ds_form");
		int index1 = formDs.nameToIndex("datasetId");
		if(index0 != index1){
			return;
		}
		Row row = formDs.getSelectedRow();
		String datasetId = row.getString(formDs.nameToIndex("datasetId"));
		Dataset dataset0 = widget.getViewModels().getDataset(datasetId);
		Dataset gridDs = LuiAppUtil.getCntView().getViewModels().getDataset("ds_grid");
		gridDs.clear();
		ComboData recIdList = LuiAppUtil.getCntView().getViewModels().getComboData("recIdList");
		ComboData recPIdList = LuiAppUtil.getCntView().getViewModels().getComboData("recPIdList");
		ComboData priIdList = LuiAppUtil.getCntView().getViewModels().getComboData("priIdList");
		recIdList.removeAllDataItems();
		recPIdList.removeAllDataItems();
		priIdList.removeAllDataItems();
		for(Field fl : dataset0.getFieldList()){
			DataItem item = new DataItem();
			item.setValue(fl.getId());
//			item.setText(StringUtils.isBlank(fl.getText()) ? fl.getId():fl.getText());
			item.setText(fl.getId());
			recIdList.addDataItem(item);
			recPIdList.addDataItem(item);
			priIdList.addDataItem(item);
			Row emprow = gridDs.getEmptyRow();
			emprow.setValue(gridDs.nameToIndex("showFields"), fl.getId());
			gridDs.addRow(emprow);
		}
		DataItem[] recItems = recIdList.getAllDataItems();
		if(recItems.length > 0){
			row.setValue(formDs.nameToIndex("recursiveId"), recItems[0].getText());
			row.setValue(formDs.nameToIndex("recursivePId"), recItems[0].getText());
			row.setValue(formDs.nameToIndex("primaryId"), recItems[0].getText());
//			if(StringUtils.equals(this.refIdOrWriteField, "ref_ext1")){//一级树
//				row.setValue(formDs.nameToIndex("foreignId"), recItems[0].getText());
//			}else{
//				TreeViewComp tree = getTree(this.treeId);
//				TreeLevel topLevel = tree.getTopLevel();
//				row.setValue(formDs.nameToIndex("foreignId"), topLevel.getMasterField());
//			}
		}
		row.setValue(formDs.nameToIndex("splitFlag"), ",");//分隔符
	}
	
	public void onOkClick(MouseEvent e){
		initParams();
		RecursiveTreeLevel recTreeLevel = new RecursiveTreeLevel();
		Dataset formDs = LuiAppUtil.getCntView().getViewModels().getDataset("ds_form");
		Row formSel = formDs.getSelectedRow();
		String treeLevelId = formSel.getString(formDs.nameToIndex("TreeLevelID"));
		recTreeLevel.setId(treeLevelId);
		recTreeLevel.setDataset(formSel.getString(formDs.nameToIndex("datasetId")));
		recTreeLevel.setRecursiveField(formSel.getString(formDs.nameToIndex("recursiveId")));
		recTreeLevel.setRecursiveParentField(formSel.getString(formDs.nameToIndex("recursivePId")));
		recTreeLevel.setMasterKeyField(formSel.getString(formDs.nameToIndex("primaryId")));
		if(StringUtils.equals(this.refIdOrWriteField, "ref_ext2")){//二级树
			recTreeLevel.setDetailKeyParameter(formSel.getString(formDs.nameToIndex("foreignId")));
		}
		String sFlag = formSel.getString(formDs.nameToIndex("splitFlag"));
		recTreeLevel.setLabelDelims(sFlag);
		String rightMenuId=formSel.getString(formDs.nameToIndex("rightMenuId"));
		if(!StringUtils.isBlank(rightMenuId))
			recTreeLevel.setContextMenu(rightMenuId);
		
		Dataset gridDs = LuiAppUtil.getCntView().getViewModels().getDataset("ds_grid");
		Row[] rows = gridDs.getCurrentPageSelectedRows();
		String[] labelFieldString = new String[rows.length];
		int index = 0;
		for(Row ro : rows){
			labelFieldString[index] = ro.getString(gridDs.nameToIndex("showFields"));
			index ++;
		}
		String rowStringAll  = StringUtils.join(labelFieldString, sFlag);
		recTreeLevel.setLabelFields(rowStringAll);
		Dataset dsMiddle = AppSession.current().getAppContext().getWindowContext("pa").getViewContext("settings").getView().getViewModels().getDataset("ds_middle");
		String treeId = (String)dsMiddle.getSelectedRow().getValue(dsMiddle.nameToIndex("string_ext1"));
		
		TreeViewComp tree = getTree(treeId);
		LifeCyclePhase phase = RequestLifeCycleContext.get().getPhase();
		RequestLifeCycleContext.get().setPhase(LifeCyclePhase.nullstatus);
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		Map<String, String> valueMap = new HashMap<String, String>();
		if(StringUtils.equals(this.refIdOrWriteField, "ref_ext2")){//二级树
			TreeLevel topLevel = tree.getTopLevel();
			topLevel.setChildTreeLevel(recTreeLevel);
			valueMap.put("ref_ext2", treeLevelId);
		}else{
			tree.setTopLevel(recTreeLevel);
			valueMap.put("ref_ext1", treeLevelId);
		}
		
		RequestLifeCycleContext.get().setPhase(phase);
		
		paramMap.put(TYPE, DATASET);
		paramMap.put(ID, "ds_middle");
		paramMap.put(WRITE_FIELDS,valueMap);
		LuiPlugoutCmd uifPluOutCmd = new LuiPlugoutCmd(MAIN,REF_OK_PLUGOUT,paramMap);
		uifPluOutCmd.execute();	
	}
	
	public void onCancelClick(MouseEvent e){
		AppSession.current().getAppContext().closeWinDialog();
	}
}
