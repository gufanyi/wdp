package xap.lui.psn.setting;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import xap.lui.core.builder.LuiSet;
import xap.lui.core.cache.PaCache;
import xap.lui.core.common.InteractionUtil;
import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.comps.StringTextComp;
import xap.lui.core.dataset.DataItem;
import xap.lui.core.dataset.DataList;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.PageData;
import xap.lui.core.dataset.Row;
import xap.lui.core.event.DatasetEvent;
import xap.lui.core.event.MouseEvent;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.model.AppSession;
import xap.lui.core.model.LuiAppUtil;
import xap.lui.core.model.PagePartMeta;
import xap.lui.core.model.PipeOut;
import xap.lui.core.model.PipeOutItem;
import xap.lui.core.model.TriggerItem;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.core.xml.StringUtils;
import xap.lui.psn.cmd.LuiAddRowCmd;
import xap.lui.psn.cmd.LuiRemoveRowCmd;
import xap.lui.psn.pamgr.PaPropertyDatasetListener;

public class PaPipeOutViewCtrl {
	public void onDataLoad_ds_pipeout(DatasetEvent e){
        DataList srcDl = (DataList) LuiAppUtil.getCntView().getViewModels().getComboData("outitemsourceList");
        srcDl.removeAllDataItems();
        DataList srcDl2 = (DataList) LuiAppUtil.getCntView().getViewModels().getComboData("triggersourceList");
        srcDl2.removeAllDataItems();
        LuiSet<Dataset> datasets = null;
        PagePartMeta pagePart = PaCache.getEditorPagePartMeta();
		PaSettingDsListener psetting = new PaSettingDsListener();
		ViewPartMeta viewPart = psetting.getViewPartMeta(pagePart);
        if(viewPart != null){
        	//获取当前编辑view的所有dataset
            datasets = viewPart.getViewModels().getDatasetsList();
            setDatasetList(srcDl, srcDl2, datasets);
        }else if(pagePart != null){
        	//获取当前节点的所有dataset
        	ViewPartMeta[] viewParts = pagePart.getWidgets();
        	String pageId = pagePart.getId();
        	for (ViewPartMeta viewPartMeta : viewParts) {
//        		if(!viewPartMeta.isParsed())//若为解析，则解析
//        			viewPartMeta = parseView(viewPartMeta, pageId, viewPartMeta.getId());
        		datasets = viewPartMeta.getViewModels().getDatasetsList();
        		setDatasetList(srcDl, srcDl2, datasets);
    		}
        }else{
        	return;
        }
		String pipeout_status = (String) LuiAppUtil.getAppAttr("pipeout_status");
		StringTextComp strtext = (StringTextComp) LuiAppUtil.getCntView().getViewComponents().getComponent("pipeout_strtext"); 
		if("add".equals(pipeout_status)){
			strtext.setEnabled(true);
			LuiAppUtil.addAppAttr("ok_status", "addok");
		}else{
			LuiAppUtil.addAppAttr("ok_status", "editok");
			Dataset itemDs = e.getSource();
			itemDs.clear();
			Dataset triggerDs = LuiAppUtil.getCntView().getViewModels().getDataset("triggerds");
			triggerDs.clear();
			Dataset inDs = LuiRuntimeContext.getWebContext().getPageMeta().getWidget("settings").getViewModels().getDataset("ds_pipeout");
			Row selRow = inDs.getSelectedRow();
			String pipeOutId = (String) selRow.getValue(inDs.nameToIndex("Value"));
			if(pipeOutId == null)
				return;
			PipeOut pipeOut = null;
			if(viewPart != null){
				pipeOut = viewPart.getPipeOut(pipeOutId);
			}else{
				pipeOut = pagePart.getPipeOut(pipeOutId);
			}
			if(pipeOut != null){	
				strtext.setValue(pipeOutId);
				strtext.setEnabled(false);
				List<PipeOutItem> outItems = pipeOut.getItemList();
				if(outItems != null && outItems.size() > 0){
					for(PipeOutItem outItem : outItems){
						Row row = itemDs.getEmptyRow();
						row.setValue(itemDs.nameToIndex("Name"), outItem.getName());
						row.setValue(itemDs.nameToIndex("Clazztype"), outItem.getClazztype());
						row.setValue(itemDs.nameToIndex("Source"), outItem.getSource());
						row.setValue(itemDs.nameToIndex("Type"), outItem.getType());
						row.setValue(itemDs.nameToIndex("Desc"), outItem.getDesc());
						itemDs.addRow(row);
					}
				}
				List<TriggerItem> triggerItems = pipeOut.getEmitList();
				if(triggerItems != null && triggerItems.size() > 0){
					for(TriggerItem triggerItem : triggerItems){
						Row row = triggerDs.getEmptyRow();
						row.setValue(triggerDs.nameToIndex("Id"), triggerItem.getId());
						row.setValue(triggerDs.nameToIndex("Source"), triggerItem.getSource());
						row.setValue(triggerDs.nameToIndex("Desc"), triggerItem.getDesc());
						row.setValue(triggerDs.nameToIndex("Type"), triggerItem.getType());
						triggerDs.addRow(row);
					}
				}
			}
		}
	}
	
	private void setDatasetList(DataList srcDl, DataList srcDl2, LuiSet<Dataset> datasets) {
		for(Dataset dataset : datasets){
			DataItem item = new DataItem();
			item.setText(StringUtils.isNotBlank(dataset.getCaption()) ? dataset.getCaption():dataset.getId());
			item.setValue(dataset.getId());
			srcDl.addDataItem(item);
			srcDl2.addDataItem(item);
		}
	}
	
	//新增outitem
	public void newOutItem_onclick(MouseEvent e){
		new LuiAddRowCmd("pipeoutitemds").execute();
	}
	//移除outitem
	public void deleteOutItem_onclick(MouseEvent e){
		if(InteractionUtil.showConfirmDialog("确认", "确定删除？"))
			new LuiRemoveRowCmd("pipeoutitemds").execute();
	}
	
	//新增trigger
	public void newTrigger_onclick(MouseEvent e){
		new LuiAddRowCmd("triggerds").execute();
	}
	//移除trigger
	public void deleteTrigger_onclick(MouseEvent e){
		if(InteractionUtil.showConfirmDialog("确认", "确定删除？"))
			new LuiRemoveRowCmd("triggerds").execute();
	}
	
	//确定
	public void onclickConfirm(MouseEvent e) {
		StringTextComp strtext = (StringTextComp) LuiAppUtil.getCntView().getViewComponents().getComponent("pipeout_strtext");
		if (StringUtils.isBlank(strtext.getValue()))
			throw new LuiRuntimeException("输出描述id不能为空！");
		PagePartMeta pagePart = PaCache.getEditorPagePartMeta();
		PaSettingDsListener psetting = new PaSettingDsListener();
		ViewPartMeta viewPartMeta = psetting.getViewPartMeta(pagePart);
		if (viewPartMeta == null && pagePart == null)
			return;
		Dataset settingsOutDs = AppSession.current().getAppContext().getWindowContext("pa").getViewContext("settings").getView().getViewModels().getDataset("ds_pipeout");
		settingsOutDs.setEdit(true);

		String okstatus = (String) LuiAppUtil.getAppAttr("ok_status");
		if ("addok".equals(okstatus)) {//添加
			PipeOut newPipeOut = new PipeOut();
			String newPipeOutId = strtext.getValue();
			newPipeOut.setId(newPipeOutId);
			
			UUID pipeOutUUid = UUID.randomUUID();
			Row stOutRow = settingsOutDs.getEmptyRow();
			stOutRow.setValue(settingsOutDs.nameToIndex("Id"), pipeOutUUid);
			stOutRow.setValue(settingsOutDs.nameToIndex("Name"), "输出管道");
			stOutRow.setValue(settingsOutDs.nameToIndex("Value"), newPipeOutId);
			settingsOutDs.addRow(stOutRow);
			
			List<PipeOutItem> itemList = new ArrayList<PipeOutItem>();
			Dataset itemDs = LuiAppUtil.getCntView().getViewModels().getDataset("pipeoutitemds");
			PageData[] pageDatas = itemDs.getAllPageData();
			List<TriggerItem> triggerList = new ArrayList<TriggerItem>();
			Dataset triggerDs = LuiAppUtil.getCntView().getViewModels().getDataset("triggerds");
			PageData[] triPageDatas = triggerDs.getAllPageData();
			if((pageDatas == null || pageDatas.length <= 0) && (triPageDatas == null || triPageDatas.length <= 0))
				return;
			if (pageDatas != null && pageDatas.length > 0) {
				for (PageData pageData : pageDatas) {
					Row[] rows = pageData.getRows();
					if (rows != null && rows.length > 0) {
						for (Row row : rows) {
							UUID uuid = UUID.randomUUID();
							String outItemName = (String) row.getValue(itemDs.nameToIndex("Name"));
							String outItemCtype = (String) row.getValue(itemDs.nameToIndex("Clazztype"));
							String outItemSource = (String) row.getValue(itemDs.nameToIndex("Source"));
							String outItemType = (String) row.getValue(itemDs.nameToIndex("Type"));
							String outItemDesc = (String) row.getValue(itemDs.nameToIndex("Desc"));
							Row itemRow = settingsOutDs.getEmptyRow();
							itemRow.setValue(settingsOutDs.nameToIndex("Id"), uuid);
							itemRow.setValue(settingsOutDs.nameToIndex("Pid"), pipeOutUUid);
							itemRow.setValue(settingsOutDs.nameToIndex("Name"), "输出项");
							itemRow.setValue(settingsOutDs.nameToIndex("Value"), outItemName);
							settingsOutDs.addRow(itemRow);
							{
								Row nameRow = settingsOutDs.getEmptyRow();
								nameRow.setValue(settingsOutDs.nameToIndex("Id"), outItemName);
								nameRow.setValue(settingsOutDs.nameToIndex("Pid"), itemRow.getValue(settingsOutDs.nameToIndex("Id")));
								nameRow.setValue(settingsOutDs.nameToIndex("Name"), "输出键名称");
								nameRow.setValue(settingsOutDs.nameToIndex("Value"), outItemName);
								settingsOutDs.addRow(nameRow);
								
								Row ctypeRow = settingsOutDs.getEmptyRow();
								ctypeRow.setValue(settingsOutDs.nameToIndex("Id"), outItemCtype);
								ctypeRow.setValue(settingsOutDs.nameToIndex("Pid"), itemRow.getValue(settingsOutDs.nameToIndex("Id")));
								ctypeRow.setValue(settingsOutDs.nameToIndex("Name"), "输出对象类型");
								ctypeRow.setValue(settingsOutDs.nameToIndex("Value"), outItemCtype);
								settingsOutDs.addRow(ctypeRow);
								
								Row sourceRow = settingsOutDs.getEmptyRow();
								sourceRow.setValue(settingsOutDs.nameToIndex("Id"), outItemSource);
								sourceRow.setValue(settingsOutDs.nameToIndex("Pid"), itemRow.getValue(settingsOutDs.nameToIndex("Id")));
								sourceRow.setValue(settingsOutDs.nameToIndex("Name"), "取数来源");
								sourceRow.setValue(settingsOutDs.nameToIndex("Value"), outItemSource);
								settingsOutDs.addRow(sourceRow);
								
								Row typeRow = settingsOutDs.getEmptyRow();
								typeRow.setValue(settingsOutDs.nameToIndex("Id"), outItemType);
								typeRow.setValue(settingsOutDs.nameToIndex("Pid"), itemRow.getValue(settingsOutDs.nameToIndex("Id")));
								typeRow.setValue(settingsOutDs.nameToIndex("Name"), "取数类型");
								typeRow.setValue(settingsOutDs.nameToIndex("Value"), outItemType);
								settingsOutDs.addRow(typeRow);
								
								Row descRow = settingsOutDs.getEmptyRow();
								descRow.setValue(settingsOutDs.nameToIndex("Id"), outItemDesc);
								descRow.setValue(settingsOutDs.nameToIndex("Pid"), itemRow.getValue(settingsOutDs.nameToIndex("Id")));
								descRow.setValue(settingsOutDs.nameToIndex("Name"), "描述");
								descRow.setValue(settingsOutDs.nameToIndex("Value"), outItemDesc);
								settingsOutDs.addRow(descRow);
							}
							PipeOutItem outItem = new PipeOutItem();
							outItem.setName(outItemName);
							outItem.setClazztype(outItemCtype);
							outItem.setSource(outItemSource);
							outItem.setType(outItemType);
							outItem.setDesc(outItemDesc);
							itemList.add(outItem);
						}
					}
				}
			}
			
			//trigger
			if (triPageDatas != null && triPageDatas.length > 0) {
				for (PageData pageData : triPageDatas) {
					Row[] rows = pageData.getRows();
					if (rows != null && rows.length > 0) {
						for(Row row : rows){
							UUID uuid = UUID.randomUUID();
							String triggerId = (String) row.getValue(triggerDs.nameToIndex("Id"));
							String triggerSource = (String) row.getValue(triggerDs.nameToIndex("Source"));
							String triggerType = (String) row.getValue(triggerDs.nameToIndex("Type"));
							String triggerDesc = (String) row.getValue(triggerDs.nameToIndex("Desc"));
							Row itemRow = settingsOutDs.getEmptyRow();
							itemRow.setValue(settingsOutDs.nameToIndex("Id"), uuid);
							itemRow.setValue(settingsOutDs.nameToIndex("Pid"), pipeOutUUid);
							itemRow.setValue(settingsOutDs.nameToIndex("Name"), "触发器");
							itemRow.setValue(settingsOutDs.nameToIndex("Value"), triggerId);
							settingsOutDs.addRow(itemRow);
							{
								Row idRow = settingsOutDs.getEmptyRow();
								 idRow.setValue(settingsOutDs.nameToIndex("Pid"), itemRow.getValue(settingsOutDs.nameToIndex("Id")));
								 idRow.setValue(settingsOutDs.nameToIndex("Id"), triggerId);
								 idRow.setValue(settingsOutDs.nameToIndex("Name"), "触发器名称");
								 idRow.setValue(settingsOutDs.nameToIndex("Value"), triggerId);
								 settingsOutDs.addRow(idRow);
								 
								 Row typeRow = settingsOutDs.getEmptyRow();
								 typeRow.setValue(settingsOutDs.nameToIndex("Pid"), itemRow.getValue(settingsOutDs.nameToIndex("Id")));
								 typeRow.setValue(settingsOutDs.nameToIndex("Id"), triggerType);
								 typeRow.setValue(settingsOutDs.nameToIndex("Name"), "触发类型");
								 typeRow.setValue(settingsOutDs.nameToIndex("Value"), triggerType);
								 settingsOutDs.addRow(typeRow);
								 
								 Row sourceRow = settingsOutDs.getEmptyRow();
								 sourceRow.setValue(settingsOutDs.nameToIndex("Pid"), itemRow.getValue(settingsOutDs.nameToIndex("Id")));
								 sourceRow.setValue(settingsOutDs.nameToIndex("Id"), triggerSource);
								 sourceRow.setValue(settingsOutDs.nameToIndex("Name"), "触发来源");
								 sourceRow.setValue(settingsOutDs.nameToIndex("Value"), triggerSource);
								 settingsOutDs.addRow(sourceRow);
								 
								 Row descRow = settingsOutDs.getEmptyRow();
								 descRow.setValue(settingsOutDs.nameToIndex("Pid"), itemRow.getValue(settingsOutDs.nameToIndex("Id")));
								 descRow.setValue(settingsOutDs.nameToIndex("Id"), triggerDesc);
								 descRow.setValue(settingsOutDs.nameToIndex("Name"), "描述");
								 descRow.setValue(settingsOutDs.nameToIndex("Value"), triggerDesc);
								 settingsOutDs.addRow(descRow);
							}
							TriggerItem triggerItem = new TriggerItem();
							triggerItem.setId(triggerId);
							triggerItem.setSource(triggerSource);
							triggerItem.setType(triggerType);
							triggerItem.setDesc(triggerDesc);
							triggerList.add(triggerItem);
						}
					}
				}
			}
			settingsOutDs.setEdit(false);
			newPipeOut.setItemList(itemList);
			newPipeOut.setEmitList(triggerList);
			if(viewPartMeta != null)
				viewPartMeta.addPipeOuts(newPipeOut);
			else
				pagePart.addPipeOuts(newPipeOut);
		}else{//编辑
			Row selRow = settingsOutDs.getSelectedRow();
			String pipeOutId = (String) selRow.getValue(settingsOutDs.nameToIndex("Value"));
			PipeOut pipeOut = null;
			if(viewPartMeta != null)
				pipeOut = viewPartMeta.getPipeOut(pipeOutId);
			else
				pipeOut = pagePart.getPipeOut(pipeOutId);
			
			if (pipeOut == null)
				throw new LuiRuntimeException("没找到对应pipeout!");
			// 将原来的outitem全部移除
			List<PipeOutItem> itemList = pipeOut.getItemList();
			if(itemList != null){
				itemList.clear();
				Dataset outItemDs = LuiAppUtil.getCntView().getViewModels().getDataset("pipeoutitemds");
				PageData pageData = outItemDs.getCurrentPageData();
				Row[] rows = pageData.getRows();
				if (rows != null && rows.length > 0) {
					for (Row row : rows) {
						PipeOutItem outItem = new PipeOutItem();
						outItem.setName((String) row.getValue(outItemDs.nameToIndex("Name")));
						outItem.setClazztype((String) row.getValue(outItemDs.nameToIndex("Clazztype")));
						outItem.setSource((String)row.getValue(outItemDs.nameToIndex("Source")));
						outItem.setType((String)row.getValue(outItemDs.nameToIndex("Type")));
						outItem.setDesc((String)row.getValue(outItemDs.nameToIndex("Desc")));
						itemList.add(outItem);
					}
				}
				pipeOut.setItemList(itemList);
			}
			//triggeritem
			//将原triggeritem全部移除
			 List<TriggerItem> triggerItemList = pipeOut.getEmitList();
			 if(triggerItemList != null){
				 triggerItemList.clear();
					Dataset triggerDs = LuiAppUtil.getCntView().getViewModels().getDataset("triggerds");
					PageData triPageData = triggerDs.getCurrentPageData();
					Row[] tgRows = triPageData.getRows();
					if(tgRows != null && tgRows.length > 0){
						for(Row row : tgRows){
							TriggerItem triItem = new TriggerItem();
							triItem.setId((String)row.getValue(triggerDs.nameToIndex("Id")));
							triItem.setSource((String)row.getValue(triggerDs.nameToIndex("Source")));
							triItem.setType((String)row.getValue(triggerDs.nameToIndex("Type")));
							triItem.setDesc((String)row.getValue(triggerDs.nameToIndex("Desc")));
							triggerItemList.add(triItem);
						}
					}
					pipeOut.setEmitList(triggerItemList);
			 }
			
			//将settingsOutDs全部清除，重新加载
			settingsOutDs.clear();
			PaPropertyDatasetListener pa = new PaPropertyDatasetListener();
			pa.loadPipeOutDsData(settingsOutDs, viewPartMeta, pagePart);
			settingsOutDs.setEdit(false);
		}
		LuiAppUtil.getCntWindowCtx().closeView("pipeout");
	}
	//取消
	public void onclickCancel(MouseEvent e){
		LuiAppUtil.getCntWindowCtx().closeView("pipeout");
	}	
}
