package xap.lui.psn.setting;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import xap.lui.core.builder.LuiSet;
import xap.lui.core.cache.PaCache;
import xap.lui.core.common.InteractionUtil;
import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.comps.ButtonComp;
import xap.lui.core.comps.FormComp;
import xap.lui.core.comps.FormElement;
import xap.lui.core.comps.MenuItem;
import xap.lui.core.dataset.DataItem;
import xap.lui.core.dataset.DataList;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.PageData;
import xap.lui.core.dataset.Row;
import xap.lui.core.event.DatasetCellEvent;
import xap.lui.core.event.DatasetEvent;
import xap.lui.core.event.MouseEvent;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.model.AppSession;
import xap.lui.core.model.Application;
import xap.lui.core.model.Connector;
import xap.lui.core.model.DataModels;
import xap.lui.core.model.LuiAppUtil;
import xap.lui.core.model.PagePartMeta;
import xap.lui.core.model.PipeIn;
import xap.lui.core.model.PipeOut;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.core.xml.StringUtils;
import xap.lui.psn.cmd.LuiAddRowCmd;
import xap.lui.psn.cmd.LuiRemoveRowCmd;
import xap.lui.psn.designer.CreateDesignModel;
import xap.lui.psn.pamgr.PaPropertyDatasetListener;

public class PaConnectorViewCtrl {
	public void onDataLoad_ds_conn(DatasetEvent e){
        Dataset connds = e.getSource();
    	connds.setEdit(true);
    	String conn_status = (String) LuiAppUtil.getAppAttr("conn_status");
		Application app = (Application) PaCache.getInstance().get("_editApp");
		FormComp form = getFormComp();
		DataList outViews = getDataList("outViewList");
        DataList inViews = getDataList("inViewList");
		if(app != null){//app
//			//将输出view和接收view禁掉
//			FormElement srcviewele = form.getElementById("Source");
//			FormElement tarviewele = form.getElementById("Target");
//			srcviewele.setEnabled(false);
//			tarviewele.setEnabled(false);
			
			DataList outPages = getDataList("outPageList");
			outPages.removeAllDataItems();
			DataList inPages = getDataList("inPageList");
			inPages.removeAllDataItems();
			//pagelist放入下拉框
			xap.lui.core.builder.LuiSet<PagePartMeta> pages = app.getWindowList();
			if(pages!=null && pages.size()>0){
				DataItem item =null;
				for(PagePartMeta page : pages){
					item = new DataItem();
//	        		item.setText(page.getCaption() == null ? page.getId():page.getCaption());
					item.setText(page.getId());
	        		item.setValue(page.getId());
	        		outPages.addDataItem(item);
	        		inPages.addDataItem(item);
				}
			}
			if("add".equals(conn_status)){
    			onloadAdd(connds);
    		}else{
    			setEditData(connds, null, app);
    			//viewlist、pipeInList、PipeOutList
    			int index1 = connds.nameToIndex("Source");
    			int index2 = connds.nameToIndex("Target");
    			int index3 = connds.nameToIndex("sourceWindow");
    			int index4 = connds.nameToIndex("targetWindow");
    			Row selRow = connds.getSelectedRow();
    			String outPageId = selRow.getString(index3);
    			PagePartMeta outPagePart = app.getWin(outPageId);
    			outPagePart = parsePage(app, outPageId, outPagePart);
    			String inPageId = selRow.getString(index4);
    			PagePartMeta inPagePart = app.getWin(inPageId);
    			inPagePart = parsePage(app, inPageId, inPagePart);
    			setViewList(outViews, outPagePart, "isapp");
            	setViewList(inViews, inPagePart, "isapp");
    			
    			DataList pipeouts = getDataList("pipeoutList");
        		setOutPipeList(connds, outPagePart, pipeouts, "isapp");
        		DataList pipeins = getDataList("pipeinList");
            	setInPipeList(connds, inPagePart, pipeins, "isapp");
    		}
		}else{//
			//将输出window和接收window禁掉
			FormElement srcele = form.getElementById("sourceWindow");
			FormElement tarele = form.getElementById("targetWindow");
			srcele.setEnabled(false);
			tarele.setEnabled(false);
	        PagePartMeta pagePart = PaCache.getEditorPagePartMeta();
	        if(pagePart != null){
	        	setViewList(outViews, pagePart, null);
	        	setViewList(inViews, pagePart, null);
	    		if("add".equals(conn_status)){
	    			onloadAdd(connds);
	    		}else{
	    			setEditData(connds, pagePart, null);
		    		//pipeInList和PipeOutList
		    		DataList pipeouts = getDataList("pipeoutList");
		    		setOutPipeList(connds, pagePart, pipeouts, null);
		    		DataList pipeins = getDataList("pipeinList");
		        	setInPipeList(connds, pagePart, pipeins, null);
	    		}
	        }
		}
	}

	private void setViewList(DataList viewdl, PagePartMeta pagePart, String appflag) {
		viewdl.removeAllDataItems();
		//获取当前节点的所有view
		ViewPartMeta[] viewParts = pagePart.getWidgets();
		DataItem item =null;
		if(StringUtils.isNotBlank(appflag)){
			for (ViewPartMeta viewPartMeta : viewParts) {
				if(!viewPartMeta.isParsed()){
					parseView(viewPartMeta, pagePart.getId());
				}
				item = new DataItem();
				item.setText(viewPartMeta.getId());
				item.setValue(viewPartMeta.getId());
				viewdl.addDataItem(item);
			}
		}else{
			for (ViewPartMeta viewPartMeta : viewParts) {
				item = new DataItem();
				item.setText(viewPartMeta.getId());
				item.setValue(viewPartMeta.getId());
				viewdl.addDataItem(item);
			}
		}
	}

	private FormComp getFormComp() {
		ViewPartMeta view = LuiAppUtil.getCntView();
		FormComp form = (FormComp) view.getViewComponents().getComponent("form_conn");
		return form;
	}
	
	private void setEditData(Dataset connds, PagePartMeta pagePart, Application app){
		LuiAppUtil.addAppAttr("ok_status", "editok");
		Dataset mapDs = LuiAppUtil.getCntView().getViewModels().getDataset("mapds");
		mapDs.clear();
		Dataset settingsConnDs = LuiRuntimeContext.getWebContext().getPageMeta().getWidget("settings").getViewModels().getDataset("ds_connector");
		Row selRow = settingsConnDs.getSelectedRow();
		String connId = (String) selRow.getValue(settingsConnDs.nameToIndex("Value"));
		Connector conn = null;
		if(app != null){
			conn = getAppConnById(app, connId);
		}else{
			LuiSet<Connector> connmap = pagePart.getConnectorMap();
			conn = connmap.find(connId);
		}
		if(conn != null){
			Row connRow = connds.getEmptyRow();
			connRow.setValue(connds.nameToIndex("Id"), connId);
			connRow.setValue(connds.nameToIndex("Source"), conn.getSource());
			connRow.setValue(connds.nameToIndex("PipeoutId"), conn.getPipeoutId());
			connRow.setValue(connds.nameToIndex("Target"), conn.getTarget());
			connRow.setValue(connds.nameToIndex("PipeinId"), conn.getPipeinId());
			connRow.setValue(connds.nameToIndex("ConnType"), conn.getConnType());
			connRow.setValue(connds.nameToIndex("sourceWindow"), conn.getSourceWindow());
			connRow.setValue(connds.nameToIndex("targetWindow"), conn.getTargetWindow());
			connds.addRow(connRow);
			connds.setSelectedIndex(0);
			
			Map<String, String> map = conn.getMapping();
			if(map != null){
				 Set<Entry<String, String>> entrys = map.entrySet();
					 for(Entry<String, String> entry : entrys){
						 Row mapRow = mapDs.getEmptyRow();
						 mapRow.setValue(mapDs.nameToIndex("outValue"), entry.getKey());
						 mapRow.setValue(mapDs.nameToIndex("inValue"), entry.getValue());
						 mapDs.addRow(mapRow);
					 }
			}
		}
		
	}

	private Connector getAppConnById(Application app, String connId) {
		Connector conn = null;
		List<Connector> connectors = app.getConnectorList();
		if(connectors != null && connectors.size() > 0){
			for(Connector c : connectors){
				if(StringUtils.equals(c.getId(), connId))
					conn = c;
			}
		}
		return conn;
	}
	
	private DataList getDataList(String id){
		DataModels dataModels = LuiAppUtil.getCntView().getViewModels();
		DataList datalist = (DataList) dataModels.getComboData(id);
		return datalist;
	}

	private void onloadAdd(Dataset connds) {
		LuiAppUtil.addAppAttr("ok_status", "addok");
		Row empRow = connds.getEmptyRow();
		connds.addRow(empRow);
		connds.setSelectedIndex(0);
	}
	
	public void onclickNew(MouseEvent<MenuItem> event) {
		new LuiAddRowCmd("mapds").execute();
	}
	
	public void onclickDel(MouseEvent<ButtonComp> event)	{
		if(InteractionUtil.showConfirmDialog("确认", "确定删除？"))
			new LuiRemoveRowCmd("mapds").execute();
	}
	
	//connector数据集：下拉框改变时，加载相应的Pipein和PipeOut
	public void datasetValueChanged(DatasetCellEvent e) {
		int index0 = e.getColIndex();
		Dataset ds = e.getSource();
		int index1 = ds.nameToIndex("Source");
		int index2 = ds.nameToIndex("Target");
		int index3 = ds.nameToIndex("sourceWindow");
		int index4 = ds.nameToIndex("targetWindow");
		Application app = (Application) PaCache.getInstance().get("_editApp");
		DataList pipeouts = getDataList("pipeoutList");
		DataList pipeins = getDataList("pipeinList");
		if(app == null){
			PagePartMeta pagePart = PaCache.getEditorPagePartMeta();
			if (index0 == index1) {
				setOutPipeList(ds, pagePart, pipeouts, null);
			}else if(index0 == index2){
				setInPipeList(ds, pagePart, pipeins, null);
			}
		}else{  //app
			if(index0==index1 || index0==index2 || index0==index3 || index0==index4){
				Row selRow = ds.getSelectedRow();
				DataList outViews = getDataList("outViewList");
		        DataList inViews = getDataList("inViewList");
		        String outPageId = selRow.getString(index3);
		        PagePartMeta outPagePart = null;
		        if(StringUtils.isNotBlank(outPageId)){
		        	outPagePart = app.getWin(outPageId);
					outPagePart = parsePage(app, outPageId, outPagePart);//待改善
		        }
				String inPageId = selRow.getString(index4);
				PagePartMeta inPagePart = null;
				if(StringUtils.isNotBlank(inPageId)){
					inPagePart = app.getWin(inPageId);
					inPagePart = parsePage(app, inPageId, inPagePart);
				}
				
		        if(index0 == index1){//outview
		        	if(outPagePart == null)
		        		return;
		        	setOutPipeList(ds, outPagePart, pipeouts, "isapp");
		        }else if(index0 == index2){//inview
		        	if(inPagePart == null)
		        		return;
		        	setInPipeList(ds, inPagePart, pipeins, "isapp");
		        }
		        else if(index0 == index3){//outpage
					if(outPagePart != null){
						setViewList(outViews, outPagePart, "isapp");
					}
				}else if(index0 == index4){//inpage
					if(inPagePart != null){
						setViewList(inViews, inPagePart, "isapp");
					}
				}
			}
		}
	}

	private PagePartMeta parsePage(Application app, String outPageId, PagePartMeta outPagePart) {
		if(!outPagePart.isParsed()){   
			outPagePart = CreateDesignModel.createDesignPageMeta(outPageId);
			outPagePart.setParsed(true);
			app.addWindow(outPagePart);
		}
		return outPagePart;
	}

	private void setInPipeList(Dataset ds, PagePartMeta pagePart, DataList datalist, String appflag) {
		Row sRow = ds.getSelectedRow();
		DataItem item;
		datalist.removeAllDataItems();
		List<PipeIn> pipeIns = null;
		String inViewId = (String) sRow.getValue(ds.nameToIndex("Target"));
		ViewPartMeta inView = pagePart.getWidget(inViewId);
		if(StringUtils.isNotBlank(appflag)){
			if(!inView.isParsed()){
				parseView(inView, pagePart.getId());
			}
		}
		if(inView != null){
			pipeIns = inView.getPipeIns();
		}
		if(pipeIns != null && pipeIns.size() > 0){
			for(PipeIn pipein : pipeIns){
				item = new DataItem();
				item.setText(pipein.getId());
				item.setValue(pipein.getId());
				datalist.addDataItem(item);
			}
		}
	}

	public ViewPartMeta parseView(ViewPartMeta view, String pageId){
		InputStream inputStream=null;
		try {
			String nodePath =CreateDesignModel.getLuiViewPartMetaPath(pageId, view.getId()); 
			inputStream=new FileInputStream(new File(nodePath));
		} catch (Throwable e1) {
			throw new LuiRuntimeException(e1.getMessage());
		}
		view = ViewPartMeta.parse(inputStream);
		view.setParsed(true);
		return view;
	}
	private void setOutPipeList(Dataset ds, PagePartMeta pagePart, DataList datalist, String appflag) {
		Row sRow = ds.getSelectedRow();
		DataItem item;
		datalist.removeAllDataItems();
		List<PipeOut> pipeOuts = null;
		String outViewId = (String) sRow.getValue(ds.nameToIndex("Source"));
		ViewPartMeta outView = pagePart.getWidget(outViewId);
		if(StringUtils.isNotBlank(appflag)){
			if(!outView.isParsed()){
				parseView(outView, pagePart.getId());
			}
		}
		if(outView != null){
			pipeOuts = outView.getPipeOuts();
		}
		if(pipeOuts != null && pipeOuts.size() > 0){
			for(PipeOut pipeOut : pipeOuts){
				item = new DataItem();
				item.setText(pipeOut.getId());
				item.setValue(pipeOut.getId());
				datalist.addDataItem(item);
			}
		}
	}
	
	//确定
	public void onclickConfirm(MouseEvent e) {
		Dataset settingsConnDs = AppSession.current().getAppContext().getWindowContext("pa").getViewContext("settings").getView().getViewModels().getDataset("ds_connector");
		settingsConnDs.setEdit(true);
		Dataset connDs = LuiAppUtil.getCntView().getViewModels().getDataset("connectords");
		Dataset mapDs = LuiAppUtil.getCntView().getViewModels().getDataset("mapds");
		String okstatus = (String) LuiAppUtil.getAppAttr("ok_status");
		Application app = (Application) PaCache.getInstance().get("_editApp");
		PagePartMeta pagePart = PaCache.getEditorPagePartMeta();
		if ("addok".equals(okstatus)) {// 添加
			Row row = connDs.getSelectedRow();
			if (row != null) {
				Connector newConnector = new Connector();
				String newConnectorId = (String) row.getValue(connDs.nameToIndex("Id"));
				if (StringUtils.isBlank(newConnectorId))
					throw new LuiRuntimeException("连接器id不能为空");
				String source = (String) row.getValue(connDs.nameToIndex("Source"));
				String pipeoutId = (String) row.getValue(connDs.nameToIndex("PipeoutId"));
				String target = (String) row.getValue(connDs.nameToIndex("Target"));
				String pipeinId = (String) row.getValue(connDs.nameToIndex("PipeinId"));
				String connType = (String) row.getValue(connDs.nameToIndex("ConnType"));
				String srcWin = (String) row.getValue(connDs.nameToIndex("sourceWindow"));
				String tarWin = (String) row.getValue(connDs.nameToIndex("targetWindow"));
				newConnector.setSource(source);
				newConnector.setPipeinId(pipeinId);
				newConnector.setTarget(target);
				newConnector.setPipeoutId(pipeoutId);
				newConnector.setConnType(connType);
				newConnector.setId(newConnectorId);
				newConnector.setSourceWindow(srcWin);
				newConnector.setTargetWindow(tarWin);

				String connUUid = UUID.randomUUID().toString();
				Row connRow = addRow(settingsConnDs, connUUid, null, "连接器", newConnectorId);
				String pid = connRow.getString(settingsConnDs.nameToIndex("Id"));
				{
					addRow(settingsConnDs, newConnectorId, pid, "id", newConnectorId);
					addRow(settingsConnDs, source, pid, "输出view", source);
					addRow(settingsConnDs, pipeoutId, pid, "pipeout", pipeoutId);
					addRow(settingsConnDs, target, pid, "接收view", target);
					addRow(settingsConnDs, pipeinId, pid, "pipein", pipeinId);
					addRow(settingsConnDs, connType, pid, "连接类型", connType);
					if(StringUtils.isNotBlank(srcWin))
						addRow(settingsConnDs, srcWin, pid, "输出win", srcWin);
					if(StringUtils.isNotBlank(tarWin))
						addRow(settingsConnDs, tarWin, pid, "接收win", tarWin);
				}

				// map
				Map<String, String> map = new HashMap<String, String>();
				PageData mapPageDatas = mapDs.getCurrentPageData();
				Row[] mapRows = mapPageDatas.getRows();
				if (mapRows != null && mapRows.length > 0) {
					String mapsUuid = UUID.randomUUID().toString();
					Row mapsRow = addRow(settingsConnDs, mapsUuid, pid, "关系映射", "");
					for (Row r : mapRows) {
						String mapUuid = UUID.randomUUID().toString();
						String pid2 = mapsRow.getString(settingsConnDs.nameToIndex("Id"));
						Row mapRow = addRow(settingsConnDs, mapUuid, pid2, "map", "");
						{
							String outValue = (String) r.getValue(mapDs.nameToIndex("outValue"));
							String inValue = (String) r.getValue(mapDs.nameToIndex("inValue"));
							map.put(outValue, inValue);

							String pid3 = mapRow.getString(settingsConnDs.nameToIndex("Id"));
							addRow(settingsConnDs, outValue, pid3, "输出键值", outValue);
							addRow(settingsConnDs, inValue, pid3, "输入键值", inValue);
						}
					}

				}
				newConnector.setMapping(map);
				settingsConnDs.setEdit(false);
				if (app != null) {// app
					app.addConnector(newConnector);
				} else {
					if (pagePart == null)
						return;
					pagePart.addConnector(newConnector);
				}
			}
		} else {// 编辑
			Row selRow = settingsConnDs.getSelectedRow();
			String connId = (String) selRow.getValue(settingsConnDs.nameToIndex("Value"));
			Connector conn = null;
			if (app != null) {// app
				conn = getAppConnById(app, connId);
			}else{
				LuiSet<Connector> connmap = pagePart.getConnectorMap();
				conn = connmap.find(connId);
			}
			if (conn != null) {
				Row row = connDs.getSelectedRow();
				if (row != null) {
					conn.setId((String) row.getValue(connDs.nameToIndex("Id")));
					conn.setSource((String) row.getValue(connDs.nameToIndex("Source")));
					conn.setPipeoutId((String) row.getValue(connDs.nameToIndex("PipeoutId")));
					conn.setTarget((String) row.getValue(connDs.nameToIndex("Target")));
					conn.setPipeinId((String) row.getValue(connDs.nameToIndex("PipeinId")));
					conn.setConnType((String) row.getValue(connDs.nameToIndex("ConnType")));
					conn.setSourceWindow((String) row.getValue(connDs.nameToIndex("sourceWindow")));
					conn.setTargetWindow((String) row.getValue(connDs.nameToIndex("targetWindow")));
				}

				Map<String, String> map = conn.getMapping();
				boolean mapisNew = false;
				if (map != null) {
					// 将map移除，再添加
					map.clear();
				}else{
					map = new HashMap<String, String>();
					mapisNew = true;
				}
				PageData pageData = mapDs.getCurrentPageData();
				Row[] rows = pageData.getRows();
				if (rows != null && rows.length > 0) {
					for (Row rw : rows) {
						String outValue = (String) rw.getValue(mapDs.nameToIndex("outValue"));
						String inValue = (String) rw.getValue(mapDs.nameToIndex("inValue"));
						map.put(outValue, inValue);
					}
					if(mapisNew)//如果map是新new的则加入conn
						conn.setMapping(map);
				}

				// 将settingsConnDs全部清除，重新加载
				settingsConnDs.clear();
				PaPropertyDatasetListener pa = new PaPropertyDatasetListener();
				if (app != null) {// app
					pa.loadConnectorDsData(settingsConnDs, null, app);
				} else {
					pa.loadConnectorDsData(settingsConnDs, pagePart, null);
				}
				settingsConnDs.setEdit(false);
			}
		}
		LuiAppUtil.getCntWindowCtx().closeView("connector");
	}

	private Row addRow(Dataset settingsConnDs, String id, String pid, String name, String value) {
		Row idRow = settingsConnDs.getEmptyRow();
		idRow.setValue(settingsConnDs.nameToIndex("Pid"), pid);
		idRow.setValue(settingsConnDs.nameToIndex("Id"), id);
		idRow.setValue(settingsConnDs.nameToIndex("Name"), name);
		idRow.setValue(settingsConnDs.nameToIndex("Value"), value);
		settingsConnDs.addRow(idRow);
		return idRow;
	}
	//取消
	public void onclickCancel(MouseEvent e){
		LuiAppUtil.getCntWindowCtx().closeView("connector");
	}
}
