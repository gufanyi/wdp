package xap.lui.psn.appwinlist;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import xap.lui.core.cache.PaCache;
import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.common.LuiWebSession;
import xap.lui.core.comps.ButtonComp;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.Row;
import xap.lui.core.event.DatasetEvent;
import xap.lui.core.event.MouseEvent;
import xap.lui.core.j2eesvr.ClassScan;
import xap.lui.core.j2eesvr.DftAllServerListener;
import xap.lui.core.logger.LuiLogger;
import xap.lui.core.model.AppSession;
import xap.lui.core.model.Application;
import xap.lui.core.model.LuiAppUtil;
import xap.lui.core.model.LuiPlugoutCmd;
import xap.lui.core.model.PagePartMeta;
import xap.lui.core.xml.StringUtils;
import xap.lui.psn.designer.CreateDesignModel;
import xap.lui.psn.designer.DesignerMainViewController;
import xap.lui.psn.pamgr.PaAppDsListener;

public class AppWinListCtrl {
	private static final String DEFAULT = " ";
	
	public void ds_onDataLoad(DatasetEvent e){
		String reqfrom = getReqfrom();
//		if(StringUtils.isNotBlank(reqfrom)){//newapp时表格单选
//			GridComp grid = (GridComp)LuiAppUtil.getCntView().getViewComponents().getComponent("winlist_grid");
//			grid.setMultiple(false);
//		}
		Dataset ds = e.getSource();
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
			boolean result_page = false;
			for (File page_folder_file : page_folder_files) {
				if(page_folder_file.getName().contains(".page.meta"))result_page=true;
			}
			if(result_page)
				this._add_app_as_row(page_folder.getName(), DEFAULT, ds);
		}//winds
		
		
//		ClassLoader loader = DftAllServerListener.class.getClassLoader();
//		Set<String> resources = ClassScan.resourceScaner("lui/nodes", loader);
//		for(String inner:resources){
//			if(inner.endsWith(".page.meta.xml")){
//				String pid = DEFAULT;
//				this._add_app_as_row(inner, pid, ds);
//			}
//		}
	}

	private String getReqfrom() {
		LuiWebSession session = LuiRuntimeContext.getWebContext().getPageWebSession();
		return session.getOriginalParameter("reqfrom");
	}
	
	public void ok_onclick(MouseEvent<ButtonComp> e){
		Dataset ds = LuiAppUtil.getCntView().getViewModels().getDataset("winlist_ds");
		Row[] rows = ds.getAllSelectedRows();
		if(rows != null && rows.length > 0){
			String reqfrom = getReqfrom();
			int idIx = ds.nameToIndex("id");
			if(StringUtils.isNotBlank(reqfrom)){//newapp时表格单选
				for(Row row : rows){
					String id = row.getString(idIx);
					Map<String,Object> map=new HashMap<String, Object>();
					map.put("dftwinid", id);
					new LuiPlugoutCmd("main", "plugout_newapp",map).execute();
				}
			}else{//编辑app
				Application editApp = (Application) PaCache.getInstance().get("_editApp");
				Dataset dataDs = AppSession.current().getAppContext().getWindowContext("pa").getViewContext("data").getView().getViewModels().getDataset("winsds");
				
				int idIndex = dataDs.nameToIndex("id");
				int pidIndex = dataDs.nameToIndex("pid");
				int nameIndex = dataDs.nameToIndex("name");
				int typeIndex = dataDs.nameToIndex("type");
				for(Row row : rows){
					String id = row.getString(idIx);
					if(editApp.getWin(id) == null){//若不在app中则添加
						PagePartMeta pagemeta = createPageMeta(id);
						editApp.addWindow(pagemeta);
//						editApp.dataToJaxb();
						Row dataRow = dataDs.getEmptyRow();
						dataRow.setValue(idIndex, id);
						dataRow.setValue(pidIndex, DEFAULT);
						dataRow.setValue(nameIndex, pagemeta.getCaption());
						dataRow.setValue(typeIndex, DesignerMainViewController.NODE_TYPE_PAGE);
						dataDs.addRow(dataRow);
					}
				}
				PaCache.getInstance().pub("_editApp", editApp);
				new PaAppDsListener().setComboList(editApp);
			}
		}
		closepage();
	}
	public PagePartMeta createPageMeta(String pageId) {
			InputStream input = null;
			try {
				String path0 = getPageXmlPath(pageId);
				input = new FileInputStream(new File(path0));
			} catch (Throwable e) {
				LuiLogger.error(e.getMessage(), e);
			}
			return CreateDesignModel.createDesignPageMeta(input);
	}
	private String getPageXmlPath(String pageId) {
		PaCache cache = PaCache.getInstance();
		String resourcePath = (String) cache.get("_resourceFolder");
		resourcePath = resourcePath + "/lui/nodes/";
		String compMetaPath = resourcePath + "/" + pageId + "/" + pageId + ".page.meta.xml";
		return compMetaPath;
	}

	public void cancel_onclick(MouseEvent<ButtonComp> e){
		closepage();
	}
	
	private void closepage(){
		AppSession.current().getAppContext().closeWinDialog();
	}
	
	public Row _add_app_as_row(String fileName, String pid, Dataset dataSet) {
		String id=fileName;
//		String[] idArray=id.split("/");
//		id=idArray[2];
		Row row_app = dataSet.getEmptyRow();
		row_app.setValue(dataSet.nameToIndex("id"), id);
		row_app.setValue(dataSet.nameToIndex("caption"), id);
		dataSet.addRow(row_app);
		return row_app;
	}
}
