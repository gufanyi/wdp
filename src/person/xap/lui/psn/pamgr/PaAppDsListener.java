package xap.lui.psn.pamgr;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import xap.lui.core.builder.LuiSet;
import xap.lui.core.cache.PaCache;
import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.common.LuiWebContext;
import xap.lui.core.comps.ButtonComp;
import xap.lui.core.comps.ComboBoxComp;
import xap.lui.core.comps.ContextMenuComp;
import xap.lui.core.comps.MenuItem;
import xap.lui.core.constant.ParamConstant;
import xap.lui.core.dataset.DataItem;
import xap.lui.core.dataset.DataList;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.Row;
import xap.lui.core.event.DatasetEvent;
import xap.lui.core.event.MouseEvent;
import xap.lui.core.event.TabEvent;
import xap.lui.core.event.TextEvent;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.layout.UITabComp;
import xap.lui.core.layout.UITabItem;
import xap.lui.core.listener.EventSubmitRule;
import xap.lui.core.listener.LuiEventConf;
import xap.lui.core.model.AppSession;
import xap.lui.core.model.Application;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.model.LuiAppUtil;
import xap.lui.core.model.PagePartMeta;
import xap.lui.core.model.RequestLifeCycleContext;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.psn.designer.DesignerMainViewController;
import xap.lui.psn.designer.PaProjViewTreeController;

public class PaAppDsListener {

	private static final String DEFAULT = " ";
	
	public void onDataLoad(DatasetEvent e) {
		Dataset ds = e.getSource();
		Application application = (Application) PaCache.getInstance().get("_editApp");
		fillWinsData(ds, application);
	}

	public static void fillWinsData(Dataset ds, Application application) {
		if(application == null)return;
		ds.clear();
		Row row = ds.getEmptyRow();
		row.setValue(ds.nameToIndex("id"), DEFAULT);
		row.setValue(ds.nameToIndex("name"), "所有节点");
		ds.addRow(row);
		application.jaxbToData();
		LuiSet<PagePartMeta> wins = application.getWindowList();
		if(CollectionUtils.isNotEmpty(wins)){
			int idIndex = ds.nameToIndex("id");
			int pidIndex = ds.nameToIndex("pid");
			int nameIndex = ds.nameToIndex("name");
			int typeIndex = ds.nameToIndex("type");
			for(PagePartMeta pagemeta : wins){
				Row r = ds.getEmptyRow();
				r.setValue(idIndex, pagemeta.getId());
				r.setValue(pidIndex, row.getString(idIndex));
				r.setValue(nameIndex, pagemeta.getCaption());
				r.setValue(typeIndex, DesignerMainViewController.NODE_TYPE_PAGE);
				ds.addRow(r);
			}
		}
	}
	
	//窗口数据集行选中事件
	public void onWinAfterRowSelect(DatasetEvent e){
		Dataset ds = e.getSource();
		ContextMenuComp conetxtMenu = (ContextMenuComp) LuiAppUtil.getContextMenu("winsTreeContextMenu");
		conetxtMenu.removeChildrenItem();
		Row row = ds.getSelectedRow();
		String treeId = (String) row.getValue(ds.nameToIndex("id"));
		MenuItem menuItem = null;
		if (DEFAULT.equals(treeId)) {
			menuItem = new MenuItem();
			menuItem.setId("importNodes");
			menuItem.setText("导入节点");
			genContextMenuEvent(menuItem);
			conetxtMenu.addMenuItem(menuItem);
		}  else {
			menuItem = new MenuItem();
			menuItem.setId("delNode");
			menuItem.setText("删除");
			genContextMenuEvent(menuItem);
			conetxtMenu.addMenuItem(menuItem);
		}
	}
	/**
	 * 构造右键菜单项事件
	 */
	private void genContextMenuEvent(MenuItem menuItem) {
		LuiEventConf event = new LuiEventConf();
		event.setEventType(MouseEvent.class.getSimpleName());
		event.setOnserver(true);
		EventSubmitRule submitRule = new EventSubmitRule();
		event.setSubmitRule(submitRule);
		event.setEventName("onclick");
		event.setMethod("ContextMenuClickHandler");
		event.setControllerClazz(this.getClass().getName());
		menuItem.addEventConf(event);
	}
	
	public void ContextMenuClickHandler(MouseEvent<MenuItem> e){
		String menuItemId = e.getSource().getId();
		Dataset ds = LuiAppUtil.getDataset("winsds");

		Application application = (Application) PaCache.getInstance().get("_editApp");
		if(StringUtils.equals(menuItemId, "importNodes")){//导入节点
			Map<String, String> paramMap = new HashMap<String, String>();
			String otherPageId = LuiRuntimeContext.getWebContext().getPageUniqueId();
			paramMap.put(ParamConstant.OTHER_PAGE_UNIQUE_ID, otherPageId);
			paramMap.put("pi", UUID.randomUUID().toString());
			AppSession.current().getAppContext().navgateTo("appwinlist", "所有节点", "300", "360", paramMap);
		}else{//删除
			LifeCyclePhase phase = RequestLifeCycleContext.get().getPhase();
			RequestLifeCycleContext.get().setPhase(LifeCyclePhase.design);
			Row row = ds.getSelectedRow();
			String id = (String) row.getValue(ds.nameToIndex("id"));
			String dftwin = application.getDefaultWindowId();
			if(StringUtils.equals(id, dftwin))
				throw new LuiRuntimeException("该节点是默认节点，请更改默认节点后再删！");
			application.removeWin(id);
//			application.dataToJaxb();
			ds.removeRow(row);
			RequestLifeCycleContext.get().setPhase(phase);
			PaCache.getInstance().pub("_editApp", application);
			setComboList(application);
		}
	}
	
	public void comboValueChanged(TextEvent<ButtonComp> e){
		ComboBoxComp combo = (ComboBoxComp) LuiAppUtil.getCntWindow().getWidget("settings").getViewComponents().getComponent("dftwin");
		String dftId = combo.getValue();
		PaCache cache = PaCache.getInstance();
		Application editApp = (Application) cache.get("_editApp");
		editApp.setDefaultWindowId(dftId);
		cache.pub("_editApp", editApp);
		LuiWebContext web_ctx = LuiRuntimeContext.getWebContext();
		PagePartMeta design_page = web_ctx.getPageMeta();
		PaProjViewTreeController.appEditWin(design_page, dftId, editApp);
	}
	
	public void afterItemInit_initAppWin(TabEvent e) {
		UITabComp tab = e.getSource();
		Application application = (Application) PaCache.getInstance().get("_editApp");
		if(application == null) return;
		if (tab.getCurrentItem() == 2) {
			ViewPartMeta settingView = getSettingView();
			ComboBoxComp combo = (ComboBoxComp) settingView.getViewComponents().getComponent("dftwin");
			setComboList(application);
			if (StringUtils.isNotBlank(application.getDefaultWindowId())) {
				combo.setValue(application.getDefaultWindowId());
			}
		}
		if (tab.getCurrentItem() == 3) {
			UITabComp pipeTabLayout = (UITabComp) tab.findChildById("pipetag");
			UITabItem tabItemPipeIn = (UITabItem) pipeTabLayout.findChildById("tabitem_pipein_setting");
			UITabItem tabItemPipeOut = (UITabItem) pipeTabLayout.findChildById("tabitem_pipeout_setting");
			if (application != null) {
				tabItemPipeIn.setVisible(false);
				tabItemPipeOut.setVisible(false);
			}
		}
	}

	private ViewPartMeta getSettingView() {
		return AppSession.current().getAppContext().getWindowContext("pa").getViewContext("settings").getView();
	}

	private DataList getDatalist(ViewPartMeta settingView) {
		DataList list = (DataList) settingView.getViewModels().getComboData("dftwincombo");
		return list;
	}

	public void setComboList(Application application) {
		ViewPartMeta settingView = getSettingView();
		DataList list = getDatalist(settingView);
		list.removeAllDataItems();
		xap.lui.core.builder.LuiSet<PagePartMeta> pageList = application.getWindowList();
		if(pageList != null && pageList.size() > 0){
			for(PagePartMeta pm : pageList){
				DataItem item = new DataItem();
				item.setValue(pm.getId());
				item.setText(pm.getCaption() == null ? pm.getId():pm.getCaption());
				list.addDataItem(item);
			}
		}
	}
}
