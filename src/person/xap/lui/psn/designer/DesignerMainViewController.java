package xap.lui.psn.designer;
import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import xap.lui.core.cache.PaCache;
import xap.lui.core.common.ContextResourceUtil;
import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.comps.ContextMenuComp;
import xap.lui.core.comps.MenuItem;
import xap.lui.core.comps.MenubarComp;
import xap.lui.core.comps.TreeViewComp;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.Row;
import xap.lui.core.event.DatasetEvent;
import xap.lui.core.event.TreeNodeEvent;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.logger.LuiLogger;
import xap.lui.core.model.AppSession;
import xap.lui.core.model.LuiAppUtil;
import xap.lui.core.model.PagePartMeta;
import xap.lui.core.model.ViewPartConfig;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.psn.top.TopMainViewController;
public class DesignerMainViewController {
	public final static String NODE_TYPE_PROJ = "project";
	public final static String NODE_TYPE_VFOLDER = "vfolder"; // 虚拟目录
	public final static String NODE_TYPE_BIZMDATE = "bizmdata"; // 业务元数据
	public final static String NODE_TYPE_UI = "ui";
	public final static String NODE_TYPE_APPS = "apps"; // 应用集
	public final static String NODE_TYPE_WINS = "wins"; // 窗口集
	public final static String NODE_TYPE_PUBVIEWS = "views"; // 公共View
	public final static String NODE_TYPE_PAGE = "page"; // 页面，或者称窗口
	public final static String NODE_TYPE_VIEW = "view";
	public final static String NODE_TYPE_MCTRLLER = "mainctrller"; // 主控制类
	public final static String NODE_TYPE_APP = "app"; // 应用集
	/*
	 * public final static String NODE_TYPE_DATAMODEL = "datamodel"; // 数据模型
	 * public final static String NODE_TYPE_DS_LIST = "dslist"; // 数据集列表 public
	 * final static String NODE_TYPE_DS = "dataset"; // 数据集 public final static
	 * String NODE_TYPE_REF_LIST = "reflist"; // 参照列表 public final static String
	 * NODE_TYPE_REF = "refnode"; // 参照 public final static String
	 * NODE_TYPE_COMBO_LIST = "combolist"; // 下拉数据集列表 public final static String
	 * NODE_TYPE_COMBO = "combo"; // 下拉数据集
	 * 
	 * public final static String NODE_TYPE_VIEW_CTRLIST = "viewctrlist"; //
	 * 控件列表 public final static String NODE_TYPE_VIEW_CTRL = "viewctrl"; // 控件
	 * public final static String NODE_TYPE_VIEW_MCTRLLER = "viewmctrller"; //
	 * view主控制类
	 */
	// private final String proj_base_folder =
	// "F:/WorkHome/EclipseHome/SVN/XapHome/";
//	private String proj_base_folder = null;
	private String lui_proj_name = "dinner";
	private String lui_nodes_sub_folder = null;//"xap.lui.dinner/src/main/resource";
	private String lui_pubviews_sub_folder = null;
	private String lui_apps_sub_folder = null;
	private static final String nodePath = "/lui/nodes/";
	private PaProjViewTreeDsCtxMenu ds_assist = null;
	public DesignerMainViewController() {
		{
			TopMainViewController tc = new TopMainViewController();
			File file = tc.getWorkSpaceFile();
			Properties prop = tc.getWorkspace(file);
			String _resourceFolder = prop.getProperty("_resourceFolder");
			String _javaFolder = prop.getProperty("_javaFolder");
			String _prjRoot = prop.getProperty("_prjRoot");
			PaCache cache = PaCache.getInstance();
			if (StringUtils.isNotBlank(_resourceFolder)) {
				cache.pub("_resourceFolder", _resourceFolder);
			}
			if (StringUtils.isNotBlank(_javaFolder)) {
				cache.pub("_javaFolder", _javaFolder);
			}
			if(StringUtils.isNotBlank(_prjRoot)) {
				cache.pub("_prjRoot", _prjRoot);
			}
			this.lui_nodes_sub_folder = _resourceFolder;
		}
		// TODO: 正式使用应该从外部对项目根路径和节点子路径进行设置
		String cls_path = this.getClass().getResource("").getPath();
		int pos = cls_path.indexOf("xap.lui.core");
		if (pos > 0) {
			this.lui_nodes_sub_folder = cls_path.substring(0, pos) + this.lui_nodes_sub_folder;
		}
		PaCache cache = PaCache.getInstance();
		String projName = (String)cache.get("_projName");
		String resourceFolder = (String)cache.get("_resourceFolder");
		if(StringUtils.isEmpty(projName)) {
			cache.pub("_projName", this.lui_proj_name);
		} else {
			this.lui_proj_name = projName;
		}
		if(StringUtils.isEmpty(resourceFolder)) {
			cache.pub("_resourceFolder", this.lui_nodes_sub_folder);
		} else {
			this.lui_nodes_sub_folder = resourceFolder;
			this.lui_pubviews_sub_folder = resourceFolder;
			this.lui_apps_sub_folder = resourceFolder;
		}
		this.lui_nodes_sub_folder += nodePath;
		this.lui_pubviews_sub_folder += "/lui/views";
		this.lui_apps_sub_folder += "/lui/apps";
		this.ds_assist = new PaProjViewTreeDsCtxMenu();
	}
	/**
	 * 加载UI设计器左侧项目树数据
	 */
	public void onDataLoad(DatasetEvent evt) {
		try {
			Dataset ds = evt.getSource();
			ds.setEdit(true);
			if (!ds.getId().equals("projViewTreeDs"))
				return;
			this._fill_projtree_ds(this.lui_proj_name, ds);
		} catch (Throwable e) {
			LuiLogger.error(e.getMessage(), e);
		}
		{//设计器界面刷新时，将PaCache的page和view清空
			PaCache cache = PaCache.getInstance();
			cache.remove("_appId");
			cache.remove("_editApp");
			cache.remove("_pageId");
			cache.remove("_viewId");
		}
	}
	private void _fill_projtree_ds(String projName, Dataset dataSet) {
		dataSet.clear();
		dataSet.clearCtxChangedProperties();
		if(this.lui_nodes_sub_folder == null) {
			return;
		}
		File root_file = new File(this.lui_nodes_sub_folder);
		if (!root_file.exists() || root_file.isFile())
			return;
		this._add_proj_as_row(projName, dataSet);
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
			     this._add_page_as_row(page_folder, projName + "_ui_wins", dataSet);
		}//winds
		{//pubviews
			File root_viewfile = new File(this.lui_pubviews_sub_folder);
			if (!root_viewfile.exists() || root_viewfile.isFile())
				return;
			File[] arr_pubview_folder = root_viewfile.listFiles();
			for (File view_folder : arr_pubview_folder) {
				if (view_folder.isFile())
					continue;
				//增肌pagepart的逻辑  只有是pagepart才可以显示
				File[] view_folder_files = view_folder.listFiles();
				boolean result_view = false;
				for (File view_folder_file : view_folder_files) {
					if(view_folder_file.getName().contains(".view.xml"))result_view=true;
				}
				if(result_view)
				     this._add_view_as_row(view_folder.getName(), "pub_ui_views", dataSet);
			}
		}
		{//apps
			File root_appfile = new File(this.lui_apps_sub_folder);
			if (!root_appfile.exists() || root_appfile.isFile())
				return;
			File[] arr_app_folder = root_appfile.listFiles();
			for (File app_folder : arr_app_folder) {
				if (app_folder.isFile())
					continue;
				//增肌pagepart的逻辑  只有是pagepart才可以显示
				File[] app_folder_files = app_folder.listFiles();
				boolean result_app = false;
				for (File app_folder_file : app_folder_files) {
					if(app_folder_file.getName().contains(".app.xml"))result_app=true;
				}
				if(result_app)
					this._add_app_as_row(app_folder.getName(), projName + "_ui_apps", dataSet);
			}
		}
		
//		ClassLoader loader=DftAllServerListener.class.getClassLoader();
//		{
//			//Set<String>  viewFiles=new HashSet<String>();
//			Set<String> resources=ClassScan.resourceScaner("lui/views", loader);
//			for(String inner:resources){
//				if(inner.endsWith(".view.xml")){
//					String pid = "pub_ui_views";
//					this._add_view_as_row(inner, pid, dataSet);
//				}
//			}
//		}//pubviews
//		{
//			Set<String> resources=ClassScan.resourceScaner("lui/apps", loader);
//			for(String inner:resources){
//				if(inner.endsWith(".app.xml")){
//					String pid = projName + "_ui_apps";
//					this._add_app_as_row(inner, pid, dataSet);
//				}
//			}
//		}//apps
	}
	public Row _add_app_as_row(String fileName, String pid, Dataset dataSet) {
		String id=fileName;//F:\xaplui2\xap.lui.dinner\src\main\resource\lui\apps\daydft
//		String[] idArray=id.split("/");
//		id=idArray[2];
		Row row_app = dataSet.getEmptyRow();
		row_app.setValue(dataSet.nameToIndex("id"), id);
		row_app.setValue(dataSet.nameToIndex("pid"), pid);
		row_app.setValue(dataSet.nameToIndex("name"), id);
		row_app.setValue(dataSet.nameToIndex("type"), DesignerMainViewController.NODE_TYPE_APP);
		dataSet.addRow(row_app);
		return row_app;
	}
	public Row _add_view_as_row(String fileName, String pid, Dataset dataSet) {
		String id=fileName;
//		String[] idArray=id.split("/");
//		id=idArray[2];
		Row row_page = dataSet.getEmptyRow();
		row_page.setValue(dataSet.nameToIndex("id"), id);
		row_page.setValue(dataSet.nameToIndex("pid"), pid);
		row_page.setValue(dataSet.nameToIndex("name"), id);
		row_page.setValue(dataSet.nameToIndex("type"), DesignerMainViewController.NODE_TYPE_VIEW);
		dataSet.addRow(row_page);
		return row_page;
	}
	
	private void _add_proj_as_row(String projName, Dataset dataSet) {
		// Project
		String id_proj = projName;
		Row row_proj = dataSet.getEmptyRow();
		row_proj.setValue(dataSet.nameToIndex("id"), id_proj);
		row_proj.setValue(dataSet.nameToIndex("name"), id_proj);
		row_proj.setValue(dataSet.nameToIndex("type"), DesignerMainViewController.NODE_TYPE_PROJ);
		dataSet.addRow(row_proj);
		// Project.业务元数据
		// String id_bizmdata = id_proj + "_bizmdata";
		// Row row_bizmdata = dataSet.getEmptyRow();
		// row_bizmdata.setValue(dataSet.nameToIndex("id"), id_bizmdata);
		// row_bizmdata.setValue(dataSet.nameToIndex("pid"), id_proj);
		// row_bizmdata.setValue(dataSet.nameToIndex("name"), "业务元数据");
		// row_bizmdata.setValue(
		// dataSet.nameToIndex("type"),
		// DesignerMainViewController.NODE_TYPE_BIZMDATE);
		// dataSet.addRow(row_bizmdata);
		// Project.UI
		String id_ui = id_proj + "_ui";
		// Row row_ui = dataSet.getEmptyRow();
		// row_ui.setValue(dataSet.nameToIndex("id"), id_ui);
		// row_ui.setValue(dataSet.nameToIndex("pid"), id_proj);
		// row_ui.setValue(dataSet.nameToIndex("name"), "UI");
		// row_ui.setValue(
		// dataSet.nameToIndex("type"),
		// DesignerMainViewController.NODE_TYPE_UI);
		// dataSet.addRow(row_ui);
		// Project.UI.Applications
		String id_apps = id_ui + "_apps";
		Row row_apps = dataSet.getEmptyRow();
		row_apps.setValue(dataSet.nameToIndex("id"), id_apps);
		row_apps.setValue(dataSet.nameToIndex("pid"), id_proj);
		row_apps.setValue(dataSet.nameToIndex("name"), "应用集");
		row_apps.setValue(dataSet.nameToIndex("type"), DesignerMainViewController.NODE_TYPE_APPS);
		dataSet.addRow(row_apps);
		// Project.UI.Windows
		String id_wins = id_ui + "_wins";
		Row row_wins = dataSet.getEmptyRow();
		row_wins.setValue(dataSet.nameToIndex("id"), id_wins);
		row_wins.setValue(dataSet.nameToIndex("pid"), id_proj);
		row_wins.setValue(dataSet.nameToIndex("name"), "窗口集");
		row_wins.setValue(dataSet.nameToIndex("type"), DesignerMainViewController.NODE_TYPE_WINS);
		dataSet.addRow(row_wins);
		// Project.UI.PublicViews
		String id_pubview = "pub_ui_views";
		Row row_pubview = dataSet.getEmptyRow();
		row_pubview.setValue(dataSet.nameToIndex("id"), id_pubview);
		row_pubview.setValue(dataSet.nameToIndex("pid"), id_proj);
		row_pubview.setValue(dataSet.nameToIndex("name"), "视图集");
		row_pubview.setValue(dataSet.nameToIndex("type"), DesignerMainViewController.NODE_TYPE_PUBVIEWS);
		dataSet.addRow(row_pubview);
	}
	/**
	 * Window.Page
	 */
	public Row _add_page_as_row(File file, String winId, Dataset dataSet) {
		String fname = file.getName();
		String id_page = winId + '_' + fname;
		Row row_page = dataSet.getEmptyRow();
		row_page.setValue(dataSet.nameToIndex("id"), id_page);
		row_page.setValue(dataSet.nameToIndex("pid"), winId);
		row_page.setValue(dataSet.nameToIndex("name"), fname);
		row_page.setValue(dataSet.nameToIndex("type"), DesignerMainViewController.NODE_TYPE_PAGE);
		dataSet.addRow(row_page);
		return row_page;
	}
	public Row _add_app_as_row(File file, String pid, Dataset dataSet) {
		String fname = file.getName();
		String id_app = pid + '_' + fname;
		Row row_app = dataSet.getEmptyRow();
		row_app.setValue(dataSet.nameToIndex("id"), id_app);
		row_app.setValue(dataSet.nameToIndex("pid"), pid);
		row_app.setValue(dataSet.nameToIndex("name"), fname);
		row_app.setValue(dataSet.nameToIndex("type"), DesignerMainViewController.NODE_TYPE_APP);
		dataSet.addRow(row_app);
		return row_app;
	}
	public void ondbclick_project(TreeNodeEvent event) {
		TreeViewComp treeView = event.getSource();
		String dsId = treeView.getDataset();
		Dataset dataset = LuiAppUtil.getCntView().getViewModels().getDataset(dsId);
		String rowId = AppSession.current().getParameter("nodeRowId");
		Row row = dataset.getRowById(rowId);
		String type=(String)row.getValue(dataset.nameToIndex("type"));
		String pageId=(String)row.getValue(dataset.nameToIndex("name"));
		if("page".equalsIgnoreCase(type)){
			String nodePath = "/lui/nodes/" + pageId + "/";
			PagePartMeta oldPm = null;
			InputStream input = null;
			try {
				input = ContextResourceUtil.getResourceAsStream(nodePath + (pageId + ".page.meta.xml"));
				if(input == null){
					 throw new LuiRuntimeException("请检查是否启动编辑项目以及目录"+pageId + ".page.meta文件是否存在");
				}
			} catch (Throwable e) {
				throw new LuiRuntimeException(e.getMessage());
			}
			oldPm = PagePartMeta.parse(input);
			this._add_view_as_row(oldPm, pageId, dataset);
		}
	}
	/**
	 * Page.View 只能加载对应Page之后才能显示
	 */
	public boolean _add_view_as_row(PagePartMeta editPagePart, String pageId, Dataset dataSet) {
		if (null == editPagePart)
			return false;
		// Page.View
		List<ViewPartConfig> viewpart_list = editPagePart.getViewPartList();
		for (ViewPartConfig viewpart : viewpart_list) {
			String id_view = pageId + '_' + viewpart.getId();
			Row row_view = dataSet.getEmptyRow();
			row_view.setValue(dataSet.nameToIndex("id"), id_view);
			row_view.setValue(dataSet.nameToIndex("pid"), "dinner_ui_wins_"+pageId);
			row_view.setValue(dataSet.nameToIndex("name"), viewpart.getId());
			row_view.setValue(dataSet.nameToIndex("type"), DesignerMainViewController.NODE_TYPE_VIEW);
			dataSet.addRow(row_view);
		}
		dataSet.setSelectedIndex(dataSet.getSelectedIndex()+viewpart_list.size());
		// Page.MainController
//		String id_mainctrl = pageId + "_mainctrl";
//		Row row_mainctrl = dataSet.getEmptyRow();
//		row_mainctrl.setValue(dataSet.nameToIndex("id"), id_mainctrl);
//		row_mainctrl.setValue(dataSet.nameToIndex("pid"), pageId);
//		row_mainctrl.setValue(dataSet.nameToIndex("name"), "主控制类");
//		row_mainctrl.setValue(dataSet.nameToIndex("type"), DesignerMainViewController.NODE_TYPE_MCTRLLER);
//		dataSet.addRow(row_mainctrl);
		return true;
	}
	/**
	 * View.DataModels 只能加载对应View之后才能显示
	 */
	/*
	 * private void _add_view_sub_datamodel(ViewPartMeta editViewPart, String
	 * treeViewId, Dataset dataSet) { if(null == editViewPart) return;
	 * 
	 * String id_datamodel = treeViewId + "_datamodel"; Row row_datamodel =
	 * dataSet.getEmptyRow(); row_datamodel.setValue(dataSet.nameToIndex("id"),
	 * id_datamodel); row_datamodel.setValue(dataSet.nameToIndex("pid"),
	 * treeViewId); row_datamodel.setValue(dataSet.nameToIndex("name"), "数据模型");
	 * row_datamodel.setValue( dataSet.nameToIndex("type"),
	 * DesignerMainViewController.NODE_TYPE_DATAMODEL);
	 * dataSet.addRow(row_datamodel);
	 * 
	 * String id_dslist = id_datamodel + "_dslist"; Row row_dslist =
	 * dataSet.getEmptyRow(); row_dslist.setValue(dataSet.nameToIndex("id"),
	 * id_dslist); row_dslist.setValue(dataSet.nameToIndex("pid"),
	 * id_datamodel); row_dslist.setValue(dataSet.nameToIndex("name"), "数据集");
	 * row_dslist.setValue( dataSet.nameToIndex("type"),
	 * DesignerMainViewController.NODE_TYPE_DS_LIST);
	 * dataSet.addRow(row_dslist);
	 * 
	 * String id_reflist = id_datamodel + "_reflist"; Row row_reflist =
	 * dataSet.getEmptyRow(); row_reflist.setValue(dataSet.nameToIndex("id"),
	 * id_reflist); row_reflist.setValue(dataSet.nameToIndex("pid"),
	 * id_datamodel); row_reflist.setValue(dataSet.nameToIndex("name"), "参照");
	 * row_reflist.setValue( dataSet.nameToIndex("type"),
	 * DesignerMainViewController.NODE_TYPE_REF_LIST);
	 * dataSet.addRow(row_reflist);
	 * 
	 * String id_combolist = id_datamodel + "_combolist"; Row row_combolist =
	 * dataSet.getEmptyRow(); row_combolist.setValue(dataSet.nameToIndex("id"),
	 * id_combolist); row_combolist.setValue(dataSet.nameToIndex("pid"),
	 * id_datamodel); row_combolist.setValue(dataSet.nameToIndex("name"),
	 * "下拉数据集"); row_combolist.setValue( dataSet.nameToIndex("type"),
	 * DesignerMainViewController.NODE_TYPE_COMBO_LIST);
	 * dataSet.addRow(row_combolist); }
	 */
	/**
	 * View.Controls
	 */
	/*
	 * private void _add_view_sub_ctrl(ViewPartMeta editViewPart, String
	 * treeViewId, Dataset dataSet) { if(null == editViewPart) return;
	 * 
	 * String id_ctrlist = treeViewId + "_ctrlist"; Row row_ctrlist =
	 * dataSet.getEmptyRow(); row_ctrlist.setValue(dataSet.nameToIndex("id"),
	 * id_ctrlist); row_ctrlist.setValue(dataSet.nameToIndex("pid"),
	 * treeViewId); row_ctrlist.setValue(dataSet.nameToIndex("name"), "控件");
	 * row_ctrlist.setValue( dataSet.nameToIndex("type"),
	 * DesignerMainViewController.NODE_TYPE_VIEW_CTRLIST);
	 * dataSet.addRow(row_ctrlist); }
	 */
	/**
	 * View.MainController
	 */
	/*
	 * private void _add_view_sub_mctrller(ViewPartMeta editViewPart, String
	 * treeViewId, Dataset dataSet) { if(null == editViewPart) return;
	 * 
	 * String id_mctrllor = treeViewId + "_mctrller"; Row row_mctrllor =
	 * dataSet.getEmptyRow(); row_mctrllor.setValue(dataSet.nameToIndex("id"),
	 * id_mctrllor); row_mctrllor.setValue(dataSet.nameToIndex("pid"),
	 * treeViewId); row_mctrllor.setValue(dataSet.nameToIndex("name"), "主控制类");
	 * row_mctrllor.setValue( dataSet.nameToIndex("type"),
	 * DesignerMainViewController.NODE_TYPE_VIEW_MCTRLLER);
	 * dataSet.addRow(row_mctrllor); }
	 */
	public void onAfterRowSelect(DatasetEvent evt) {
		Dataset data_set = evt.getSource();
		Row row_sel = data_set.getSelectedRow();
		if (null == row_sel)
			return;
		String type = row_sel.getString(data_set.nameToIndex("type"));
		PagePartMeta design_page = LuiRuntimeContext.getWebContext().getPageMeta();
		ViewPartMeta topView = design_page.getWidget("top");
		MenubarComp menubar = topView.getViewMenus().getMenuBar("menu_top");
		MenuItem newViewItem = menubar.getItem("file_newview");//新建视图
		if(type.equals("page")){//若选中page则
			newViewItem.setEnabled(true);
		}else{
			newViewItem.setEnabled(false);
		}
		ViewPartMeta viewpart = LuiAppUtil.getCntView();
		ContextMenuComp ctx_menu = (ContextMenuComp) viewpart.getViewMenus().getContextMenu("projViewTreeDsCtxMenu");
		this.ds_assist.refillCtxMenu(data_set, ctx_menu);
		/*
		 * viewpart.dataToJaxb();
		 * java.util.logging.Logger.getAnonymousLogger().info(
		 * xap.lui.core.util.JaxbUtil.toXml(viewpart));
		 */
	}
}
