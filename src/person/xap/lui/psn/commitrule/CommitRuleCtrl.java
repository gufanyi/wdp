package xap.lui.psn.commitrule;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import xap.lui.core.builder.LuiHashSet;
import xap.lui.core.builder.LuiSet;
import xap.lui.core.builder.PagePartMetaBuilder;
import xap.lui.core.cache.PaCache;
import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.comps.ComboBoxComp;
import xap.lui.core.control.IWindowCtrl;
import xap.lui.core.dataset.ComboData;
import xap.lui.core.dataset.DataItem;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.PageData;
import xap.lui.core.dataset.Row;
import xap.lui.core.event.DatasetCellEvent;
import xap.lui.core.event.DatasetEvent;
import xap.lui.core.event.IEventSupport;
import xap.lui.core.event.MouseEvent;
import xap.lui.core.event.PageEvent;
import xap.lui.core.event.TextEvent;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.listener.DatasetRule;
import xap.lui.core.listener.EventSubmitRule;
import xap.lui.core.listener.LuiEventConf;
import xap.lui.core.listener.WidgetRule;
import xap.lui.core.model.AppSession;
import xap.lui.core.model.LuiAppUtil;
import xap.lui.core.model.LuiPlugoutCmd;
import xap.lui.core.model.PagePartMeta;
import xap.lui.core.model.ViewPartConfig;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.core.refrence.SelfDefRefNode;
import xap.lui.psn.setting.PaSettingDsListener;
public class CommitRuleCtrl implements IWindowCtrl {
	private static final String ID = "id";
	private static final String DATASET = "Dataset";
	private static final String TYPE = "type";
	private static final String MAIN = "main";
	private static final String REF_OK_PLUGOUT = "refOkPlugout";
	private static final String WRITE_FIELDS = "writeFields";
	
	public void sysWindowClosed(PageEvent event) {
		LuiRuntimeContext.getWebContext().destroyWebSession();
	}
	public void onDataLoad_grid(DatasetEvent e)  {
		Dataset dsEvent = AppSession.current().getAppContext().getWindowContext("pa").getViewContext("settings").getView().getViewModels().getDataset("ds_event");
		Row eventRow = dsEvent.getSelectedRow();
		String subRuleId = (String) eventRow.getValue(dsEvent.nameToIndex("ParamValue"));
		EventSubmitRule submitRule = null;
		if (StringUtils.isNotBlank(subRuleId)) {
			if (dsEvent.getExtendAttribute(subRuleId) != null) {
				submitRule = (EventSubmitRule) dsEvent.getExtendAttribute(subRuleId).getValue();
			} else {
				PaSettingDsListener pDListener = new PaSettingDsListener();
				IEventSupport editor = pDListener.getEditorComp(dsEvent);
				LuiEventConf[] eventConfs = editor.getEventConfs();
				for (LuiEventConf eventConf : eventConfs) {
					if (eventConf.getSubmitRule() != null) {
						if (subRuleId.equals(eventConf.getSubmitRule().getId())) {
							submitRule = eventConf.getSubmitRule();
						}
					}
				}
			}
			if (submitRule == null)
				return;
			Dataset ruleDs = e.getSource();
			//
			PagePartMeta pagemeta = PaCache.getEditorPagePartMeta();
			setViewList(pagemeta);
			LuiSet<WidgetRule> widgetRulesList = submitRule.getWidgetRules();
			if (widgetRulesList.size() > 0) {
				for (WidgetRule widgetRule : widgetRulesList) {
					DatasetRule[] drules = widgetRule.getDatasetRules();
					for (DatasetRule drule : drules) {
						String viewId = widgetRule.getId();
						String dsId = drule.getId();
						String submitType = drule.getType();
						Row row = ruleDs.getEmptyRow();
						row.setValue(ruleDs.nameToIndex("viewId"), viewId);
					//	setDatasetList(viewId, row, "datasetList", ruleDs, pagemeta);
						SelfDefRefNode refNode = (SelfDefRefNode) (LuiAppUtil.getCntView().getViewModels().getRefNode("ref_dataset"));
						String path = refNode.getPath();
						String value=(String)LuiAppUtil.getAppAttr("hahaahhaah");
						if(StringUtils.isNotBlank(value)){
							path=value;
						}else{
							LuiAppUtil.addAppAttr("hahaahhaah", path);
						}
						String url = path + "&sourceWinId=" + pagemeta.getId() + "&sourceView=" + viewId + "&pi="+UUID.randomUUID().toString();
						refNode.setPath(url);
						
						row.setValue(ruleDs.nameToIndex("datasetId"), dsId);
						row.setValue(ruleDs.nameToIndex("submitRule"), submitType);
						ruleDs.addRow(row);
					}
				}
			}
		}
	}
	public ViewPartMeta getCurrentWidget() {
		return AppSession.current().getWindowContext().getCurrentViewContext().getView();
	}
	public void onDataLoad_grid2(DatasetEvent e)  {
		Dataset dsEvent = AppSession.current().getAppContext().getWindowContext("pa").getViewContext("settings").getView().getViewModels().getDataset("ds_event");
		Row eventRow = dsEvent.getSelectedRow();
		String subRuleId = (String) eventRow.getValue(dsEvent.nameToIndex("ParamValue"));
		ComboBoxComp combo = (ComboBoxComp) LuiAppUtil.getCntView().getViewComponents().getComponent("fCombo");
		// 父提交规则下拉框控件加载现有所有节点
		ComboData allPageList = LuiAppUtil.getCntView().getViewModels().getComboData("allPageList");
		try {
//			File file = new File("c:\\tmp\\lui\\nodes");
//			if (file.exists() && file.isDirectory()) {
//				File[] pageFolders = file.listFiles();
//				for (File pageFolder : pageFolders) {
//					if (pageFolder.isFile())
//						continue;
//					File[] onePageFiles = pageFolder.listFiles();
//					for (File pageFile : onePageFiles) {
//						if (pageFile.getName().contains(".page.meta")) {
//							String pageName = pageFolder.getName();
//							DataItem item = new DataItem(pageName, pageName);
//							item.setText(pageName);
//							allPageList.addDataItem(item);
//						}
//					}
//				}
//			}
			{
				PaCache cache = PaCache.getInstance();
				String resourceFolder = (String)cache.get("_resourceFolder");
				String nodespath = resourceFolder + "/lui/nodes/";
				File root_file = new File(nodespath);
				if (!root_file.exists() || root_file.isFile())
					return;
				File[] arr_page_folder = root_file.listFiles();
				for (File page_folder : arr_page_folder) {
					if (page_folder.isFile())
						continue;
					//增肌pagepart的逻辑  只有是pagepart才可以显示
					File[] page_folder_files = page_folder.listFiles();
					for (File page_folder_file : page_folder_files) {
						if (page_folder_file.getName().contains(".page.meta")) {
							String pageName = page_folder.getName();
							DataItem item = new DataItem(pageName, pageName);
							item.setText(pageName);
							allPageList.addDataItem(item);
						}
					}
				}
			}
			
			EventSubmitRule submitRule = null;
			if (StringUtils.isNotBlank(subRuleId)) {
				if (dsEvent.getExtendAttribute(subRuleId) != null) {
					submitRule = (EventSubmitRule) dsEvent.getExtendAttribute(subRuleId).getValue();
				} else {
					PaSettingDsListener pDListener = new PaSettingDsListener();
					IEventSupport editor = pDListener.getEditorComp(dsEvent);
					LuiEventConf[] eventConfs = editor.getEventConfs();
					for (LuiEventConf eventConf : eventConfs) {
						if (eventConf.getSubmitRule() != null) {
							if (subRuleId.equals(eventConf.getSubmitRule().getId())) {
								submitRule = eventConf.getSubmitRule();
							}
						}
					}
				}
				if (submitRule == null)
					return;
				if (submitRule.getParentSubmitRule() != null) {
					EventSubmitRule parentSubmitRule = submitRule.getParentSubmitRule();
					LuiSet<WidgetRule> pWidgetRulesList = parentSubmitRule.getWidgetRules();
					if (pWidgetRulesList.size() > 0) {
						Dataset pDuleDs = e.getSource();
						// 
						String pagePartId = parentSubmitRule.getPageId();
						if (("").equals(pagePartId))
							throw new LuiRuntimeException("pageId不能为空！");
						combo.setValue(pagePartId);
						PagePartMeta pagemeta = PagePartMetaBuilder.createPageMeta(pagePartId);
						setViewList(pagemeta);
						for (WidgetRule pRule : pWidgetRulesList) {
							DatasetRule[] drules = pRule.getDatasetRules();
							for (DatasetRule drule : drules) {
								String viewId = pRule.getId();
								String dsId = drule.getId();
								String submitType = drule.getType();
								Row row = pDuleDs.getEmptyRow();
								row.setValue(pDuleDs.nameToIndex("viewId"), viewId);
								
								SelfDefRefNode refNode = (SelfDefRefNode) (LuiAppUtil.getCntView().getViewModels().getRefNode("ref_dataset2"));
								String path = refNode.getPath();
								String value=(String)LuiAppUtil.getAppAttr("haha2");
								if(StringUtils.isNotBlank(value)){
									path=value;
								}else{
									LuiAppUtil.addAppAttr("haha2", path);
								}
								String url = path + "&sourceWinId=" + pagemeta.getId() + "&sourceView=" + viewId + "&pi="+UUID.randomUUID().toString();
								refNode.setPath(url);
								
								row.setValue(pDuleDs.nameToIndex("datasetId"), dsId);
								row.setValue(pDuleDs.nameToIndex("submitRule"), submitType);
								pDuleDs.addRow(row);
							}
						}
					}
				}
			}
		} catch (Throwable e1) {
			e1.printStackTrace();
		}
	}
	// new1
	public void onclickAdd(MouseEvent e) {
		Dataset ruleDs = LuiAppUtil.getCntView().getViewModels().getDataset("ds_grid1");
		ruleDs.setEdit(true);
		Row row = ruleDs.getEmptyRow();
		PagePartMeta pagemeta = PaCache.getEditorPagePartMeta();
		setViewList(pagemeta);
		ruleDs.addRow(row);
		ruleDs.setSelectedIndex(ruleDs.getRowIndex(row));
	}
	private void setViewList(PagePartMeta pagemeta) {
		ComboData viewList = LuiAppUtil.getCntView().getViewModels().getComboData("viewList");
		viewList.removeAllDataItems();
		List<ViewPartConfig> viewPartList = pagemeta.getViewPartList();
		for (ViewPartConfig viewPartConfig : viewPartList) {
			String viewId = viewPartConfig.getId();
			DataItem item = new DataItem();
			item.setValue(viewId);
			item.setText(viewId);
			viewList.addDataItem(item);
		}
	}
	// edit1
	public void onclickEdit(MouseEvent e) {
		Dataset ruleDs = LuiAppUtil.getCntView().getViewModels().getDataset("ds_grid1");
		ruleDs.setEdit(true);
	}
	// del1
	public void onclickDel(MouseEvent e) {
		Dataset ruleDs = LuiAppUtil.getCntView().getViewModels().getDataset("ds_grid1");
		del(ruleDs);
	}
	// new2
	public void onclickAdd2(MouseEvent e) {
		// 将上个表格设为不可编辑
		Dataset ruleDs = LuiAppUtil.getCntView().getViewModels().getDataset("ds_grid1");
		ruleDs.setEdit(false);
		Dataset rule2Ds = LuiAppUtil.getCntView().getViewModels().getDataset("ds_grid2");
		rule2Ds.setEdit(true);
		Row row = rule2Ds.getEmptyRow();
		ComboData viewList = LuiAppUtil.getCntView().getViewModels().getComboData("viewList2");
		viewList.removeAllDataItems();
		// 获取下拉框当前选中的节点
		ComboBoxComp combo = (ComboBoxComp) LuiAppUtil.getCntView().getViewComponents().getComponent("fCombo");
		String pagePartId = combo.getValue();
		PagePartMeta pagemeta = PagePartMetaBuilder.createPageMeta(pagePartId);//.buildPageMeta(param);
		List<ViewPartConfig> viewPartList = pagemeta.getViewPartList();
		for (ViewPartConfig viewPartConfig : viewPartList) {
			String viewId = viewPartConfig.getId();
			DataItem item = new DataItem();
			item.setValue(viewId);
			item.setText(viewId);
			viewList.addDataItem(item);
		}
		rule2Ds.addRow(row);
		rule2Ds.setSelectedIndex(rule2Ds.getRowIndex(row));
	}
	// edit2
	public void onclickEdit2(MouseEvent e) {
		Dataset rule2Ds = LuiAppUtil.getCntView().getViewModels().getDataset("ds_grid2");
		rule2Ds.setEdit(true);
	}
	// del2
	public void onclickDel2(MouseEvent e) {
		Dataset ruleDs = LuiAppUtil.getCntView().getViewModels().getDataset("ds_grid2");
		del(ruleDs);
	}
	public void datasetValueChanged(DatasetCellEvent e) {
		int index0 = e.getColIndex();
		Dataset ruleDs = e.getSource();
		int index1 = ruleDs.nameToIndex("viewId");
		if (index0 != index1) {
			return;
		}
		String pageId = PaCache.getEditorPageId();//.getEditorPagePartMeta();
		SelfDefRefNode refNode = (SelfDefRefNode) (LuiAppUtil.getCntView().getViewModels().getRefNode("ref_dataset"));
		String path = refNode.getPath();
		String value=(String)LuiAppUtil.getAppAttr("hahav1");
		if(StringUtils.isNotBlank(value)){
			path=value;
		}else{
			LuiAppUtil.addAppAttr("hahav1", path);
		}
		valueChanged(e, pageId, "ref_dataset", path, false);
	}
	public void pDatasetValueChanged(DatasetCellEvent e) {
		int index0 = e.getColIndex();
		Dataset ruleDs = e.getSource();
		int index1 = ruleDs.nameToIndex("viewId");
		if (index0 != index1) {
			return;
		}
		// 获取下拉框当前选中的节点
		ComboBoxComp combo = (ComboBoxComp) LuiAppUtil.getCntView().getViewComponents().getComponent("fCombo");
		String pagePartId = combo.getValue();
		SelfDefRefNode refNode = (SelfDefRefNode) (LuiAppUtil.getCntView().getViewModels().getRefNode("ref_dataset2"));
		String path = refNode.getPath();
		String value=(String)LuiAppUtil.getAppAttr("hahav");
		if(StringUtils.isNotBlank(value)){
			path=value;
		}else{
			LuiAppUtil.addAppAttr("hahav", path);
		}
		valueChanged(e, pagePartId, "ref_dataset2", path, true);
	}
	private void valueChanged(DatasetCellEvent e, String pageId, String refNodeId, String path, boolean isParent) {
		Dataset ruleDs = e.getSource();
		Row selRow = ruleDs.getSelectedRow();
		String viewColValue = (String) selRow.getValue(ruleDs.nameToIndex("viewId"));
		if (StringUtils.isNotBlank(viewColValue)) {
			SelfDefRefNode refNode = (SelfDefRefNode) (LuiAppUtil.getCntView().getViewModels().getRefNode(refNodeId));
			String url = path + "&sourceWinId=" + pageId + "&sourceView=" + viewColValue + "&isParent=" + isParent + "&pi="+UUID.randomUUID().toString();
			refNode.setPath(url);
		}
	}

	private void del(Dataset ds) {
		Row selRow = ds.getSelectedRow();
		if (selRow == null)
			throw new LuiRuntimeException("请选中要删除的数据");
		ds.removeRow(selRow);
	}
	public void onOkClick(MouseEvent e) {
		EventSubmitRule submitRule = new EventSubmitRule();
		LuiSet<WidgetRule> widgetRulesList = new LuiHashSet<WidgetRule>();
		Dataset ruleDs = LuiAppUtil.getCntView().getViewModels().getDataset("ds_grid1");
		PageData rowDatas = ruleDs.getCurrentPageData();
		if (rowDatas != null) {
			Row[] rows = rowDatas.getRows();
			Map<String, WidgetRule> map = new HashMap<String, WidgetRule>();
			for (Row row : rows) {
				String viewId = (String) row.getValue(ruleDs.nameToIndex("viewId"));
				String dsId = (String) row.getValue(ruleDs.nameToIndex("datasetId"));
				String submitType = (String) row.getValue(ruleDs.nameToIndex("submitRule"));
				WidgetRule widgetRule = map.get(viewId);
				if (widgetRule == null) {
					widgetRule = new WidgetRule();
					widgetRule.setId(viewId);
					widgetRule.setCardSubmit(false);
					widgetRulesList.add(widgetRule);
					map.put(viewId, widgetRule);
				}
				DatasetRule drule = new DatasetRule();
				drule.setId(dsId);
				drule.setType(submitType);
				widgetRule.addDsRule(drule);
			}
			submitRule.setWidgetRules(widgetRulesList);
		}
		// 父提交规则
		Dataset PRuleDs = LuiAppUtil.getCntView().getViewModels().getDataset("ds_grid2");
		PageData pRowDatas = PRuleDs.getCurrentPageData();
		ComboBoxComp combo = (ComboBoxComp) LuiAppUtil.getCntView().getViewComponents().getComponent("fCombo");
		if (pRowDatas != null) {
			Row[] pRows = pRowDatas.getRows();
			EventSubmitRule parentSubmitRule = new EventSubmitRule();
			LuiSet<WidgetRule> pWidgetRulesList = new LuiHashSet<WidgetRule>();
			Map<String, WidgetRule> map = new HashMap<String, WidgetRule>();
			for (Row row : pRows) {
				String viewId = (String) row.getValue(PRuleDs.nameToIndex("viewId"));
				String dsId = (String) row.getValue(PRuleDs.nameToIndex("datasetId"));
				String submitType = (String) row.getValue(PRuleDs.nameToIndex("submitRule"));
				WidgetRule widgetRule = map.get(viewId);
				if (widgetRule == null) {
					widgetRule = new WidgetRule();
					widgetRule.setId(viewId);
					widgetRule.setCardSubmit(false);
					map.put(viewId, widgetRule);
				}
				DatasetRule drule = new DatasetRule();
				drule.setId(dsId);
				drule.setType(submitType);
				widgetRule.addDsRule(drule);
				pWidgetRulesList.add(widgetRule);
			}
			parentSubmitRule.setPageId(combo.getValue());
			parentSubmitRule.setWidgetRules(pWidgetRulesList);
			submitRule.setParentSubmitRule(parentSubmitRule);
		}
		PRuleDs.setEdit(false);
		
		
		// 将submitRule放进ds_event
		Dataset dsEvent = AppSession.current().getAppContext().getWindowContext("pa").getViewContext("settings").getView().getViewModels().getDataset("ds_event");
		Row selRow = dsEvent.getSelectedRow();
		String id = "";
		String pv = (String) selRow.getValue(dsEvent.nameToIndex("ParamValue"));
		if (StringUtils.isNotBlank(pv)) {
			id = pv;
		} else {
			//id = "submitRule" + String.valueOf(new Random().nextLong());
			id = "submitRule"; //+ String.valueOf(new Random().nextLong());
		}
		submitRule.setId(id);
		selRow.setValue(dsEvent.nameToIndex("ParamValue"), id);
		
		
		
		AppSession.current().getAppContext().closeWinDialog();
		dsEvent.setExtendAttribute(id, submitRule);
		
		{
			PaSettingDsListener listener=new PaSettingDsListener();
			IEventSupport eventSupport=	listener.getEditorComp(dsEvent);
			LuiEventConf[] confs=	eventSupport.getEventConfs();
			if(confs!=null&&confs.length!=0){
				for(LuiEventConf inner:confs){
					EventSubmitRule rule= 	inner.getSubmitRule();
					if(rule==null){
						continue;
					}
					if(id.equals(rule.getId())){
						inner.setSubmitRule(submitRule);
						break;
					}
				}
			}
		}
		// 获取下拉框当前选中的节点
		// dsEvent.setExtendAttribute(id+"combo", combo.getValue());
		Map<String, Object> paramMap = new HashMap<String, Object>();
		Map<String, String> valueMap = new HashMap<String, String>();
		valueMap.put(selRow.getString(dsEvent.nameToIndex("ParamValue")), id);
		paramMap.put(TYPE, DATASET);
		paramMap.put(ID, "ds_event");
		paramMap.put(WRITE_FIELDS, valueMap);
		LuiPlugoutCmd uifPluOutCmd = new LuiPlugoutCmd(MAIN, REF_OK_PLUGOUT, paramMap);
		uifPluOutCmd.execute();
	}
	public void comboValueChanged(TextEvent e) {
		// e.getSource();
		Dataset dsEvent = AppSession.current().getAppContext().getWindowContext("pa").getViewContext("settings").getView().getViewModels().getDataset("ds_event");
		Row eventRow = dsEvent.getSelectedRow();
		String subRuleId = (String) eventRow.getValue(dsEvent.nameToIndex("ParamValue"));
		ComboBoxComp combo = (ComboBoxComp) LuiAppUtil.getCntView().getViewComponents().getComponent("fCombo");
		String value = combo.getValue();
		EventSubmitRule submitRule = null;
		String pagePartId = null;
		if (StringUtils.isNotBlank(subRuleId)) {
			if (dsEvent.getExtendAttribute(subRuleId) != null) {
				submitRule = (EventSubmitRule) dsEvent.getExtendAttribute(subRuleId).getValue();
			} else {
				PaSettingDsListener pDListener = new PaSettingDsListener();
				IEventSupport editor = pDListener.getEditorComp(dsEvent);
				LuiEventConf[] eventConfs = editor.getEventConfs();
				for (LuiEventConf eventConf : eventConfs) {
					if (eventConf.getSubmitRule() != null) {
						if (subRuleId.equals(eventConf.getSubmitRule().getId())) {
							submitRule = eventConf.getSubmitRule();
						}
					}
				}
			}
			if (submitRule == null)
				return;
			if (submitRule.getParentSubmitRule() != null) {
				EventSubmitRule parentSubmitRule = submitRule.getParentSubmitRule();
				LuiSet<WidgetRule> pWidgetRulesList = parentSubmitRule.getWidgetRules();
				if (pWidgetRulesList.size() > 0) {
					// 下拉框设置
					pagePartId = parentSubmitRule.getPageId();
				}
			}
		}
		if (value.equalsIgnoreCase(pagePartId)) {
			return;
		}
		Dataset ruleDs = LuiAppUtil.getCntView().getViewModels().getDataset("ds_grid2");
		ruleDs.clear();
		//new SuperVO2DatasetSerializer().serialize(null, ruleDs);
	}
	public void onCancelClick(MouseEvent e) {
		AppSession.current().getAppContext().closeWinDialog();
	}
}
