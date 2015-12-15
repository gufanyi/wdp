package xap.lui.psn.setting;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import xap.lui.core.cache.PaCache;
import xap.lui.core.common.InteractionUtil;
import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.comps.ButtonComp;
import xap.lui.core.comps.MenuItem;
import xap.lui.core.comps.StringTextComp;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.PageData;
import xap.lui.core.dataset.Row;
import xap.lui.core.event.DatasetEvent;
import xap.lui.core.event.MouseEvent;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.model.AppSession;
import xap.lui.core.model.LuiAppUtil;
import xap.lui.core.model.PagePartMeta;
import xap.lui.core.model.PipeIn;
import xap.lui.core.model.PipeInItem;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.core.xml.StringUtils;
import xap.lui.psn.cmd.LuiAddRowCmd;
import xap.lui.psn.cmd.LuiRemoveRowCmd;
import xap.lui.psn.pamgr.PaPropertyDatasetListener;

public class PaPipeInViewCtrl {
	public void onDataLoad_ds_pipein(DatasetEvent e){
		String status = (String) LuiAppUtil.getAppAttr("pipein_status");
		StringTextComp strtext = (StringTextComp) LuiAppUtil.getCntView().getViewComponents().getComponent("pipein_strtext");
		if("add".equals(status)){
			strtext.setEnabled(true);
			LuiAppUtil.addAppAttr("ok_status", "addok");
		}else{
			LuiAppUtil.addAppAttr("ok_status", "editok");
			Dataset ds = e.getSource();
			ds.clear();
			Dataset inDs = LuiRuntimeContext.getWebContext().getPageMeta().getWidget("settings").getViewModels().getDataset("ds_pipein");
			Row selRow = inDs.getSelectedRow();
			String pipeInId = (String) selRow.getValue(inDs.nameToIndex("Value"));
			PipeIn pipeIn = null;
			PagePartMeta pagePart = PaCache.getEditorPagePartMeta();
			PaSettingDsListener psetting = new PaSettingDsListener();
			ViewPartMeta viewPartMeta = psetting.getViewPartMeta(pagePart);
			if(viewPartMeta != null){
				pipeIn = viewPartMeta.getPipeIn(pipeInId);
			}else if(pagePart != null){
				pipeIn = pagePart.getPipeIn(pipeInId);
			}else{
				return;
			}
			if(pipeIn != null){
				strtext.setValue(pipeInId);
				strtext.setEnabled(false);
				List<PipeInItem> inItems = pipeIn.getItemList();
				if(inItems != null && inItems.size() > 0){
					for(PipeInItem inItem : inItems){
						 Row row = ds.getEmptyRow();
						 row.setValue(ds.nameToIndex("Id"), inItem.getId());
						 row.setValue(ds.nameToIndex("Clazztype"), inItem.getClazztype());
						 ds.addRow(row);
					}
				}
			}
		}
	}

	public void onclickNew(xap.lui.core.event.MouseEvent<MenuItem> event) {
		new LuiAddRowCmd("pipeinds").execute();
	}
	
	public void onclickDel(MouseEvent<ButtonComp> event) {
		if(InteractionUtil.showConfirmDialog("确认", "确定删除？"))
			new LuiRemoveRowCmd("pipeinds").execute();
	}
	
	//确定
	public void onclickConfirm(MouseEvent e) {
		StringTextComp strtext = (StringTextComp) LuiAppUtil.getCntView().getViewComponents().getComponent("pipein_strtext");
		if (StringUtils.isBlank(strtext.getValue()))
			throw new LuiRuntimeException("输入描述id不能为空！");
		PagePartMeta pagePart = PaCache.getEditorPagePartMeta();
		PaSettingDsListener psetting = new PaSettingDsListener();
		ViewPartMeta viewPartMeta = psetting.getViewPartMeta(pagePart);
		if (viewPartMeta == null && pagePart == null)
			return;
		Dataset settingsInDs = AppSession.current().getAppContext().getWindowContext("pa").getViewContext("settings").getView().getViewModels().getDataset("ds_pipein");
		settingsInDs.setEdit(true);

		String okstatus = (String) LuiAppUtil.getAppAttr("ok_status");
		if ("addok".equals(okstatus)) {
			PipeIn newPipeIn = new PipeIn();
			String newPipeInId = strtext.getValue();
			newPipeIn.setId(newPipeInId);

			List<PipeInItem> itemList = new ArrayList<PipeInItem>();
			Dataset pipeInDs = LuiAppUtil.getCntView().getViewModels().getDataset("pipeinds");
			PageData[] pageDatas = pipeInDs.getAllPageData();
			if (pageDatas == null || pageDatas.length <= 0) 
				return;
			UUID pipeInUUid = UUID.randomUUID();
			Row stInRow = settingsInDs.getEmptyRow();
			stInRow.setValue(settingsInDs.nameToIndex("Id"), pipeInUUid);
			stInRow.setValue(settingsInDs.nameToIndex("Name"), "输入管道");
			stInRow.setValue(settingsInDs.nameToIndex("Value"), newPipeInId);
			settingsInDs.addRow(stInRow);

			if (pageDatas != null && pageDatas.length > 0) {
				for (PageData pageData : pageDatas) {
					Row[] rows = pageData.getRows();
					if (rows != null && rows.length > 0) {
						for (Row row : rows) {
							UUID uuid = UUID.randomUUID();
							String inItemId = (String) row.getValue(pipeInDs.nameToIndex("Id"));
							String inItemCtype = (String) row.getValue(pipeInDs.nameToIndex("Clazztype"));
							Row itemRow = settingsInDs.getEmptyRow();
							itemRow.setValue(settingsInDs.nameToIndex("Id"), uuid);
							itemRow.setValue(settingsInDs.nameToIndex("Pid"), pipeInUUid);
							itemRow.setValue(settingsInDs.nameToIndex("Name"), "输入项");
							itemRow.setValue(settingsInDs.nameToIndex("Value"), inItemId);
							settingsInDs.addRow(itemRow);
							{
								Row idRow = settingsInDs.getEmptyRow();
								idRow.setValue(settingsInDs.nameToIndex("Pid"), itemRow.getValue(settingsInDs.nameToIndex("Id")));
								idRow.setValue(settingsInDs.nameToIndex("Id"), inItemId);
								idRow.setValue(settingsInDs.nameToIndex("Name"), "id");
								idRow.setValue(settingsInDs.nameToIndex("Value"), inItemId);
								settingsInDs.addRow(idRow);

								Row clazztypeRow = settingsInDs.getEmptyRow();
								clazztypeRow.setValue(settingsInDs.nameToIndex("Pid"), itemRow.getValue(settingsInDs.nameToIndex("Id")));
								clazztypeRow.setValue(settingsInDs.nameToIndex("Id"), inItemCtype);
								clazztypeRow.setValue(settingsInDs.nameToIndex("Name"), "输入类型");
								clazztypeRow.setValue(settingsInDs.nameToIndex("Value"), inItemCtype);
								settingsInDs.addRow(clazztypeRow);
							}
							PipeInItem inItem = new PipeInItem();
							inItem.setId(inItemId);
							inItem.setClazztype(inItemCtype);
							itemList.add(inItem);
						}
					}
				}
			}
			settingsInDs.setEdit(false);
			newPipeIn.setItemList(itemList);
			if(viewPartMeta != null)
				viewPartMeta.addPipeIns(newPipeIn);
			else
				pagePart.addPipeIns(newPipeIn);
		} else if ("editok".equals(okstatus)) {
			Dataset pipeInDs = LuiAppUtil.getCntView().getViewModels().getDataset("pipeinds");
			PageData pageData = pipeInDs.getCurrentPageData();
			Row selRow = settingsInDs.getSelectedRow();
			String pipeInId = (String) selRow.getValue(settingsInDs.nameToIndex("Value"));
			PipeIn pipeIn = null;
			if(viewPartMeta != null)
				pipeIn = viewPartMeta.getPipeIn(pipeInId);
			else
				pipeIn = pagePart.getPipeIn(pipeInId);
			
			if (pipeIn == null)
				throw new LuiRuntimeException("没找到对应pipein!");
			// 将原来的item全部移除
			List<PipeInItem> itemList = pipeIn.getItemList();
			itemList.clear();
			
			Row[] rows = pageData.getRows();
			if (rows != null && rows.length > 0) {
				for (Row row : rows) {
					PipeInItem inItem = new PipeInItem();
					inItem.setId((String) row.getValue(pipeInDs.nameToIndex("Id")));
					inItem.setClazztype((String) row.getValue(pipeInDs.nameToIndex("Clazztype")));
					itemList.add(inItem);
				}
			}
			pipeIn.setItemList(itemList);
			
			//将settingsInDs全部清除，重新加载
			settingsInDs.clear();
			PaPropertyDatasetListener pa = new PaPropertyDatasetListener();
			pa.loadPipeInDsData(settingsInDs, viewPartMeta, pagePart);
			settingsInDs.setEdit(false);
		}
		LuiAppUtil.getCntWindowCtx().closeView("pipein");
	}
	
	//取消
	public void onclickCancel(MouseEvent e){
		LuiAppUtil.getCntWindowCtx().closeView("pipein");
	}
}
