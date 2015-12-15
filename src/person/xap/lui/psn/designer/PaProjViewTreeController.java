package xap.lui.psn.designer;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import xap.lui.core.builder.RaSelfWindow;
import xap.lui.core.cache.PaCache;
import xap.lui.core.common.InteractionUtil;
import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.common.LuiWebContext;
import xap.lui.core.comps.ComboBoxComp;
import xap.lui.core.comps.IFrameComp;
import xap.lui.core.comps.MenuItem;
import xap.lui.core.constant.ParamConstant;
import xap.lui.core.control.IWindowCtrl;
import xap.lui.core.control.ModePhase;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.LuiParameter;
import xap.lui.core.dataset.Row;
import xap.lui.core.event.MouseEvent;
import xap.lui.core.exception.InputItem;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.exception.StringInputItem;
import xap.lui.core.layout.UICardLayout;
import xap.lui.core.layout.UIPartMeta;
import xap.lui.core.layout.UITabComp;
import xap.lui.core.layout.UIViewPart;
import xap.lui.core.listener.LuiEventConf;
import xap.lui.core.logger.LuiLogger;
import xap.lui.core.model.AppSession;
import xap.lui.core.model.Application;
import xap.lui.core.model.DataModels;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.model.LuiAppUtil;
import xap.lui.core.model.LuiMeta;
import xap.lui.core.model.PagePartMeta;
import xap.lui.core.model.RequestLifeCycleContext;
import xap.lui.core.model.UIState;
import xap.lui.core.model.ViewPartConfig;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.core.model.ViewState;
import xap.lui.core.services.IPaEditorService;
import xap.lui.core.services.PaEditorServiceImpl;
import xap.lui.psn.java.CreateJavaUtil;
import xap.lui.psn.pamgr.PaAppDsListener;
import xap.lui.psn.pamgr.PaEntityDsListener;
import xap.lui.psn.pamgr.PaPalletDsListener;
import xap.lui.psn.pamgr.PaPropertyDatasetListener;
import xap.lui.psn.top.TopMainViewController;
/**
 * 定义项目浏览树控件的右键菜单命令响应方法 <br/>
 * 使用参数 opNameEn 和 opObjInfo 进行分支处理
 * 
 * @author Yongsheng.Qi
 */
public class PaProjViewTreeController implements IWindowCtrl {
	private LuiEventConf _get_1st_evt_(MouseEvent<MenuItem> evt) {
		// 获取菜单项
		MenuItem mitem = evt.getSource();
		// 获取 Event 子项列表
		List<LuiEventConf> evt_conf_list = mitem.getEventConfList();
		if (null == evt_conf_list || evt_conf_list.isEmpty())
			return null;
		LuiEventConf evt_conf = evt_conf_list.get(0);
		return evt_conf;
	}
	public void onClickCtxMenu(MouseEvent<MenuItem> evt) {
		String sesionId = LuiRuntimeContext.getWebContext().getRequest().getSession().getId();
		String eclipse_sesionId = (String) PaCache.getInstance().get("eclipse_sesionId");
		if (StringUtils.isBlank(eclipse_sesionId)) {
			PaCache.getInstance().pub("eclipse_sesionId", sesionId);
		}
		// 一般只需要第一个 Event
		LuiEventConf evt_conf = this._get_1st_evt_(evt);
		if (null == evt_conf)
			return;
		String node_name = null;
		String op_name = null;
		// 获取事件参数
		String str_ref = null;
		List<LuiParameter> evt_param_list = evt_conf.getParamList();
		for (LuiParameter param : evt_param_list) {
			str_ref = param.getName();
			if (str_ref.equals("opNameEn")) {
				op_name = param.getValue().toLowerCase();
			}
		}
		Dataset dataset = LuiAppUtil.getCntView().getViewModels().getDataset("projViewTreeDs");
		Row row = dataset.getSelectedRow();
		String type = (String) row.getValue(dataset.nameToIndex("type"));
		node_name = (String) row.getValue(dataset.nameToIndex("name"));
		
		// 调用处理逻辑
		PaCache cache = PaCache.getInstance();
		if(type.equals("apps") || type.equals("app")){
			cache.remove("_pageId");
			cache.remove("_viewId");
			cache.pub("_appId", node_name);
		}else{
			cache.remove("_appId");
			cache.remove("_editApp");
			cache.pub("_pageId", node_name);
			cache.pub("_viewId", "");
		}
		if (op_name.equals("new") && type.equals("wins")) {
			//this._new_page(node_name);
			this._new_page();
		} else if (op_name.equals("edit") && type.equals("page")) {
			this._edit_page(node_name);
		} else if (op_name.equals("save") && type.equals("page")) {
			this._save_page(node_name);
		} else if (op_name.equals("delete") && type.equals("page")) {
			this._delete_page(node_name);
		} else if (op_name.equals("new") && type.equals("page")) {
			// String pageId = (String) row.getValue(dataset.nameToIndex("id"));
			// pageId = pageId.substring(0, pageId.indexOf(node_name) - 1);
			PaCache.getInstance().pub("_pageId", node_name);
			this._new_view(node_name);
		} else if (op_name.equals("edit") && type.equals("view")) {
			String pageId = (String) row.getValue(dataset.nameToIndex("id"));
			pageId = pageId.substring(0, pageId.lastIndexOf(node_name) - 1);
			PaCache.getInstance().pub("_pageId", pageId);
			PaCache.getInstance().pub("_viewId", node_name);
			this._edit_view(pageId, node_name);
		} else if (op_name.equals("delete") && type.equals("view")) {
			String pageId = (String) row.getValue(dataset.nameToIndex("id"));
			pageId = pageId.substring(0, pageId.indexOf(node_name) - 1);
			PaCache.getInstance().pub("_pageId", pageId);
			PaCache.getInstance().pub("_viewId", node_name);
			this._delete_view(pageId, node_name);
		}
		//apps
		else if (op_name.equals("new") && type.equals("apps")) {
			this._new_app();
		}
		else if(op_name.equals("edit") && type.equals("app")) {
			this._edit_app(node_name);
		}
		setTabItemStatus(dataset, row, type);
	}
	public void setTabItemStatus(Dataset dataset, Row row, String type) {
//		UITabComp dataTabLayout = (UITabComp) LuiAppUtil.getCntWindowCtx().getViewContext("data").getUIMeta().findChildById("outlookbar1");
//		UITabItem tabItemCtrl = (UITabItem) dataTabLayout.findChildById("item_ctrl");
//		UITabItem tabItemEntity = (UITabItem) dataTabLayout.findChildById("item_entity");
//		UITabItem tabItemState = (UITabItem) dataTabLayout.findChildById("item_state");
//		UITabItem tabItemCurr = (UITabItem) dataTabLayout.findChildById("item_curr");
//		UITabItem tabItemWins = (UITabItem) dataTabLayout.findChildById("item_wins");
////		UITabItem tabItemLayout = (UITabItem) dataTabLayout.findChildById("item_layout");
//		UITabComp settingTabLayout = (UITabComp) LuiAppUtil.getCntWindowCtx().getViewContext("settings").getUIMeta().findChildById("tab2");
//		UITabItem tabItemSet = (UITabItem) settingTabLayout.findChildById("item_set");//win属性
//		UITabItem tabItemAppset = (UITabItem) settingTabLayout.findChildById("item_appset");//app属性
//		UITabItem tabItemEvent = (UITabItem) settingTabLayout.findChildById("item_event");//事件
//		UITabComp pipeTabLayout = (UITabComp) LuiAppUtil.getCntWindowCtx().getViewContext("settings").getUIMeta().findChildById("pipetag");
//		UITabItem tabItemPipeIn = (UITabItem) pipeTabLayout.findChildById("tabitem_pipein_setting");
//		UITabItem tabItemPipeOut = (UITabItem) pipeTabLayout.findChildById("tabitem_pipeout_setting");
//		
//		String pid = (String) row.getValue(dataset.nameToIndex("pid"));
//		if(pid.endsWith("_ui_apps")){//apps
//			tabItemWins.setVisible(true);
//			tabItemCtrl.setVisible(false);
//			tabItemEntity.setVisible(false);
//			tabItemState.setVisible(false);
//			tabItemCurr.setVisible(false);
////			tabItemLayout.setVisible(false);
//			
//			tabItemSet.setVisible(false);
//			tabItemAppset.setVisible(true);
//			tabItemEvent.setVisible(false);
//			
//			tabItemPipeIn.setVisible(false);
//			tabItemPipeOut.setVisible(false);
//		}else {
//			tabItemSet.setVisible(true);
//			tabItemAppset.setVisible(false);
//			tabItemEvent.setVisible(true);
//			
//			tabItemWins.setVisible(false);
//			tabItemCurr.setVisible(true);
////			tabItemLayout.setVisible(true);
//			if (type.equals("page")) {
//				tabItemCtrl.setVisible(false);
//				tabItemEntity.setVisible(false);
//				tabItemState.setVisible(true);
//			} else {
//				tabItemCtrl.setVisible(true);
//				tabItemEntity.setVisible(true);
//				tabItemState.setVisible(false);
//			}
//			
//			tabItemPipeIn.setVisible(true);
//			tabItemPipeOut.setVisible(true);
//		}
	}
	public void _edit_app(String nodeName) {
		LuiWebContext web_ctx = LuiRuntimeContext.getWebContext();
		PagePartMeta design_page = web_ctx.getPageMeta();
		Application application = CreateDesignModel.createDesignApp(nodeName);
		application.jaxbToData();
		{
			if(StringUtils.isNotBlank(application.getDefaultWindowId())){//若默认窗口不为空则在编辑器展现
				String dftwin = application.getDefaultWindowId();
				appEditWin(design_page, dftwin, application);
			}else{
				
			}
		}
		PaCache.getInstance().pub("_editApp", application);
		{   //加载窗口、属性内容
			DataModels dataModels = design_page.getWidget("data").getViewModels();
			Dataset currDs = dataModels.getDataset("winsds");
			PaAppDsListener.fillWinsData(currDs, application);
		}
		{
			//连接器加载
			new PaPropertyDatasetListener().connectorSetting();
			//加载默认窗口
			UIPartMeta uimeta = LuiAppUtil.getCntWindowCtx().getViewContext("settings").getUIMeta();
			UITabComp settingTabLayout = (UITabComp) uimeta.findChildById("tab2");
			settingTabLayout.setCurrentItem(0);
//			UITabItem tabItemSet = (UITabItem) settingTabLayout.findChildById("item_appset");
//			tabItemSet.setActive(0);
			UICardLayout card = (UICardLayout) uimeta.findChildById("cardlayout7773");
			card.setCurrentItem("2");
			
			ViewPartMeta settingView = design_page.getWidget("settings");
			ComboBoxComp combo = (ComboBoxComp) settingView.getViewComponents().getComponent("dftwin");
			new PaAppDsListener().setComboList(application);
			if (StringUtils.isNotBlank(application.getDefaultWindowId())) {
				combo.setValue(application.getDefaultWindowId());
			}
		}
	}
	public static void appEditWin(PagePartMeta design_page, String dftwin, Application app) {
		IFrameComp iframe = (IFrameComp) design_page.getWidget("editor").getViewComponents().getComponent("iframe_tmp");
		IPaEditorService ipaService = new PaEditorServiceImpl();
		PaCache.getInstance().pub("_pageId", dftwin);
		String sessionId = (String) PaCache.getInstance().get("eclipse_sesionId");
		PagePartMeta dftpage = app.getWin(dftwin);
		if(dftpage.isParsed()){
			ipaService.setOriPageMeta(dftwin, sessionId, dftpage);
			CreateDesignModel.createDesignUIMeta(dftpage, dftwin, null);
		}else{
			PagePartMeta pagemeta = CreateDesignModel.createDesignPageMeta(dftwin);
			ipaService.setOriPageMeta(dftwin, sessionId, pagemeta);
			CreateDesignModel.createDesignUIMeta(pagemeta, dftwin, null);
			pagemeta.setParsed(true);
//			app.removeWin(dftwin);
			app.addWindow(pagemeta);
		}
		
		
		String otherPageId = LuiRuntimeContext.getWebContext().getPageUniqueId();
		String url1 = "/portal/app/mockapp/" + dftwin;
		url1 += "?"+LuiRuntimeContext.MODEPHASE+"=eclipse";
		url1 += "&emode=1&model=" + RaSelfWindow.class.getName() + "&otherPageUniqueId=" + otherPageId;
		iframe.setSrc(url1);
	}
	public void _new_app() {
		
		Map<String, String> paramMap = new HashMap<String, String>();
		String otherPageId = LuiRuntimeContext.getWebContext().getPageUniqueId();
		paramMap.put(ParamConstant.OTHER_PAGE_UNIQUE_ID, otherPageId);
		paramMap.put("pi", UUID.randomUUID().toString());
		AppSession.current().getAppContext().navgateTo("newapp", "新建App", "300", "240", paramMap);
//		InputItem projectNameItem = new StringInputItem("appId", "app ID：", true);
//		InputItem javaPathItem = new StringInputItem("javaFullClassName", "控制类名：", true);
//		InteractionUtil.showInputDialog("新建App", new InputItem[] {
//				projectNameItem, javaPathItem
//		});
//		Map<String, String> rs = InteractionUtil.getInputDialogResult();
//		String appId = rs.get("appId");
//		PaCache cache = PaCache.getInstance();
//		cache.pub("_appId", appId);
//		String resourceFolder = (String) cache.get("_resourceFolder");
//		resourceFolder = resourceFolder + "/lui/apps/";
//		String nodePath = resourceFolder + "/" + appId;
////		String javaFullClassName = rs.get("javaFullClassName");
//		File nodefile = new File(nodePath);
//		if (!nodefile.exists()) {
//			nodefile.mkdirs();
//		}
//		{
//			Application application = new Application();
//			application.setId(appId);
//			application.setControllerClazz(javaFullClassName);
//			{
//				String xml0 = application.toXml();
//				File file = this.createAppFile(appId);
//				try {
//					FileUtils.write(file, xml0, "utf-8");
//				} catch (IOException e) {
//					LuiLogger.error(e.getMessage(), e);
//				}
//			}
//			
//			PaCache.getInstance().pub("_editApp", application);
//		}
//		{
//			try {
//				String javaClassName = PaProjViewTreeController.getJavaClassName(javaFullClassName);
//				String javaPackageName = PaProjViewTreeController.getPackageName(javaFullClassName);
//				String javaContent = CreateJavaUtil.getControllerClazz(javaPackageName, javaClassName);
//				File file = PaProjViewTreeController.createJavaFile(javaFullClassName, null);
//				FileUtils.write(file, javaContent, "utf-8");
//			} catch (IOException e) {
//				LuiLogger.error(e.getMessage(), e);
//			}
//		}
//		DesignerMainViewController desinerMainViewController = new DesignerMainViewController();
//		Dataset projectDs = LuiAppUtil.getDataset("projViewTreeDs", "project");
//		String projectName = (String) cache.get("_projName");
//		desinerMainViewController._add_app_as_row(nodefile, projectName + "_ui_apps", projectDs);
	}
	
	
//	private void _new_page(String nodeName) {
	public void _new_page() {
		InputItem projectNameItem = new StringInputItem("pageId", "新窗口ID：", true);
		InputItem javaPathItem = new StringInputItem("javaFullClassName", "控制类名：", true);
		InteractionUtil.showInputDialog("新建窗口", new InputItem[] {
				projectNameItem, javaPathItem
		});
		
		removeAppCache();
		Map<String, String> rs = InteractionUtil.getInputDialogResult();
		String pageId = rs.get("pageId");
		PaCache cache = PaCache.getInstance();
		cache.pub("_pageId", pageId);
		String resourceFolder = (String) cache.get("_resourceFolder");
		resourceFolder = resourceFolder + "/lui/nodes/";
		String nodePath = resourceFolder + "/" + pageId;
		String javaFullClassName = rs.get("javaFullClassName");
		File nodefile = new File(nodePath);
		if (!nodefile.exists()) {
			nodefile.mkdirs();
		}
		{
			PagePartMeta pagePartMeta = new PagePartMeta();
			pagePartMeta.setId(pageId);
			pagePartMeta.setController(javaFullClassName);
			//新建page后添加UIState状态
			addUIStates(pagePartMeta);
			{
				String xml0 = pagePartMeta.toXml();
				File file = this.createPagePartMetaFile(pageId);
				try {
					FileUtils.write(file, xml0, "utf-8");
				} catch (IOException e) {
					LuiLogger.error(e.getMessage(), e);
				}
			}
			IPaEditorService ipaService = new xap.lui.core.services.PaEditorServiceImpl();
			String sessionId = (String) PaCache.getInstance().get("eclipse_sesionId");
			ipaService.setOriPageMeta(pageId, sessionId, pagePartMeta);
		}
		{
			UIPartMeta pageUIPartMeta = new UIPartMeta();
			pageUIPartMeta.setId(pageId + "_um");
			IPaEditorService ipaService = new xap.lui.core.services.PaEditorServiceImpl();
			String sessionId = (String) PaCache.getInstance().get("eclipse_sesionId");
			ipaService.setOriUIMeta(pageId, sessionId, pageUIPartMeta);
			try {
				String xml1 = pageUIPartMeta.toXml();
				File file = this.createUIPartMetaFile(pageId, null);
				FileUtils.write(file, xml1, "utf-8");
			} catch (IOException e) {
				LuiLogger.error(e.getMessage(), e);
			}
		}
		{
			try {
				String javaClassName = PaProjViewTreeController.getJavaClassName(javaFullClassName);
				String javaPackageName = PaProjViewTreeController.getPackageName(javaFullClassName);
				String javaContent = CreateJavaUtil.getControllerClazz(javaPackageName, javaClassName);
				File file = PaProjViewTreeController.createJavaFile(javaFullClassName, null);
				FileUtils.write(file, javaContent, "utf-8");
			} catch (IOException e) {
				LuiLogger.error(e.getMessage(), e);
			}
		}
		DesignerMainViewController desinerMainViewController = new DesignerMainViewController();
		Dataset projectDs = LuiAppUtil.getDataset("projViewTreeDs", "project");
		String projectName = (String) cache.get("_projName");
		desinerMainViewController._add_page_as_row(nodefile, projectName + "_ui_wins", projectDs);
		
		//this._edit_page(pageId);
	}
	//新建page后添加UIState状态,添加默认态（编辑态和浏览态）
	private void addUIStates(PagePartMeta pagePartMeta) {
		UIState uIState = new UIState();
		uIState.setId("editstate");
		uIState.setName("编辑态");
		pagePartMeta.addUIStates(uIState);
		
		UIState uiState2 = new UIState();
		uiState2.setId("viewstate");
		uiState2.setName("浏览态");
		pagePartMeta.addUIStates(uiState2);
	}
	public static String getPackageName(String javaFullClassName) {
		int index = javaFullClassName.lastIndexOf(".");
		if (index == -1)
			throw new LuiRuntimeException("类全名称输入有误");
		String javaPackageName = javaFullClassName.substring(0, index);
		return javaPackageName;
	}
	public static String getJavaClassName(String javaFullClassName) {
		if (javaFullClassName.endsWith(".java")) {
			javaFullClassName = javaFullClassName.substring(0, javaFullClassName.indexOf(".java"));
		}
		int index = javaFullClassName.lastIndexOf(".");
		String javaClassName = javaFullClassName.substring(index + 1, javaFullClassName.length());
		return javaClassName;
	}
	public static File createJavaFile(String javaFullClassName, String javaPath) {
		PaCache cache = PaCache.getInstance();
		String javaClassName = PaProjViewTreeController.getJavaClassName(javaFullClassName);
		String javaPackageName = PaProjViewTreeController.getPackageName(javaFullClassName);
		javaPackageName = javaPackageName.replace(".", "/");
		String resourceFolder = null;
		if(StringUtils.isNotBlank(javaPath)){
			resourceFolder = javaPath;
		}else{
			resourceFolder = (String) cache.get("_javaFolder");
		}
		javaPackageName = resourceFolder + "/" + javaPackageName;
		File packAgeFile = new File(javaPackageName);
		if (!packAgeFile.exists()) {
			packAgeFile.mkdirs();
		}
		javaFullClassName = javaPackageName + "/" + javaClassName + ".java";
		File javaFile = new File(javaFullClassName);
		if (!javaFile.exists()) {
			try {
				javaFile.createNewFile();
			} catch (IOException e) {
				LuiLogger.error(e.getMessage(), e);
			}
		}
		return javaFile;
	}
	public File createPagePartMetaFile(String pageId) {
		String compMetaPath =PaCache.getEditorPageXmlPath();
		File file = new File(compMetaPath);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				LuiLogger.error(e.getMessage(), e);
			}
		}
		return file;
	}
	public File createViewPartMetaFile() {
		String path=PaCache.getEditorCompMetaXmlPath();
		File file = new File(path);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				LuiLogger.error(e.getMessage(), e);
			}
		}
		return file;
	}
	public File createUIPartMetaFile(String pageId, String viewId) {
		String uiMetaPath=PaCache.getEditorUIMetaXmlPath();
		File file = new File(uiMetaPath);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				LuiLogger.error(e.getMessage(), e);
			}
		}
		return file;
	}
	public void _edit_page(String nodeName) {
		PaCache.getInstance().remove("_version");
		LuiWebContext web_ctx = LuiRuntimeContext.getWebContext();
		PagePartMeta design_page = web_ctx.getPageMeta();
		IFrameComp iframe = (IFrameComp) design_page.getWidget("editor").getViewComponents().getComponent("iframe_tmp");
		PagePartMeta pagePartMeta = CreateDesignModel.createDesignPageMeta(nodeName);
//		Connector conn=new Connector();
//		conn.putMapping("haha0", "haha0");
//		conn.putMapping("haha1", "haha1");
//		pagePartMeta.addConnector(conn);
//		System.out.println(pagePartMeta.toXml());
		
		
		UIPartMeta uiPartMeta = CreateDesignModel.createDesignUIMeta(pagePartMeta, nodeName, null);
		{
			PaCache.luiMeta = null;
			LuiMeta luiMeta = new LuiMeta();
			luiMeta.setPagePartMeta(pagePartMeta);
			luiMeta.setuIPartMeta(uiPartMeta);
			PaCache.luiMeta = luiMeta;
		}
		{
			String otherPageId = LuiRuntimeContext.getWebContext().getPageUniqueId();
			String url1 = "/portal/app/mockapp/" + nodeName;
			url1 += "?"+LuiRuntimeContext.MODEPHASE+"=eclipse";
			url1 += "&emode=1&model=" + RaSelfWindow.class.getName() + "&otherPageUniqueId=" + otherPageId;
			iframe.setSrc(url1);
		}
		{
			DataModels dataModels = design_page.getWidget("data").getViewModels();
//			Dataset ctrlDs = dataModels.getDataset("ctrlds");
//			PaPalletDsListener.fillCtrlDs(ctrlDs, pagePartMeta, null);
			Dataset currDs = dataModels.getDataset("currds");
			PaPalletDsListener.fillCurrDs(currDs, pagePartMeta, uiPartMeta);
			Dataset layOutDs = dataModels.getDataset("layoutds");
			PaPalletDsListener.fillLayoutDs(layOutDs);
//			Dataset entityDs = dataModels.getDataset("entityds");
//			PaEntityDsListener.setModelData(entityDs, null);
		}
	}
	private void _save_page(String nodeName) {}
	private void _delete_page(String nodeName) {
		PaCache cache = PaCache.getInstance();
		String pageId = (String) cache.get("_pageId");
		String resourceFolder = (String) cache.get("_resourceFolder");
		resourceFolder = resourceFolder + "/lui/nodes/";
		String nodePath = resourceFolder + "/" + pageId;
		File nodefile = new File(nodePath);
		if (nodefile.exists()) {
			try {
				FileUtils.deleteDirectory(nodefile);
			} catch (IOException e) {
				LuiLogger.error(e.getMessage(), e);
			}
		}
		Dataset projectDs = LuiAppUtil.getDataset("projViewTreeDs", "project");
		Row row = projectDs.getSelectedRow();
		projectDs.removeRow(row);
		InteractionUtil.showMessageDialog("删除窗口" + pageId + "成功!!");
	}
	
	private void _delete_view(String pageId, String nodeName) {
		PaCache cache = PaCache.getInstance();
		String resourceFolder = (String) cache.get("_resourceFolder");
		resourceFolder = resourceFolder + "/lui/nodes/";
		String compMetaPath = resourceFolder + "/" + pageId + "/" + pageId + "." + nodeName + ".view";
		File nodefile = new File(compMetaPath);
		if (nodefile.exists()) {
			try {
				FileUtils.deleteDirectory(nodefile);
			} catch (IOException e) {
				LuiLogger.error(e.getMessage(), e);
			}
		}
		//删除视图后从UIState删除view状态
		PagePartMeta pagePartMeta = CreateDesignModel.createDesignPageMeta(pageId);
		delViewStateFromUIState(pagePartMeta, nodeName);
		
		Dataset projectDs = LuiAppUtil.getDataset("projViewTreeDs", "project");
		Row row = projectDs.getSelectedRow();
		projectDs.removeRow(row);
		InteractionUtil.showMessageDialog("删除View" + pageId + "成功!!");
	}
	public void _new_view(String pageId) {
		InputItem projectNameItem = new StringInputItem("viewId", "新视图ID：", true);
		InputItem fullClassName = new StringInputItem("javaFullClassName", "控制类名：", true);
		//InputItem javaPathItem=new StringInputItem("javaPath", "java路径:", false);
		ModePhase modePahse=(ModePhase)PaCache.getInstance().get(PaCache.ModePhase);
		String nodeType=LuiRuntimeContext.getWebContext().getParameter("nodeType");
		
		if(ModePhase.nodedef.equals(modePahse)){
			if("FreeBill".equalsIgnoreCase(nodeType)){
				fullClassName.setValue(PaCache.FreeBillCtrlClassName);
			}
			if("FreeGrid".equalsIgnoreCase(nodeType)){
				fullClassName.setValue(PaCache.FreeBillGrdiClassName);
			}
		}
		InteractionUtil.showInputDialog("新建视图", new InputItem[] {
				projectNameItem, fullClassName
		});
		
		Map<String, String> rs = InteractionUtil.getInputDialogResult();
		String viewId = rs.get("viewId");
		String javaFullClassName = rs.get("javaFullClassName");
		String javaPath=rs.get("javaPath");
		
		removeAppCache();
		PaCache.getInstance().pub("_pageId", pageId);
		
		dealNewView(pageId, viewId, javaFullClassName,javaPath);
	}
	private void removeAppCache() {
		PaCache cache = PaCache.getInstance();
		cache.remove("_appId");
		cache.remove("_editApp");
	}
	public void dealNewView(String pageId, String viewId, String javaFullClassName, String javaPath) {
		PaCache cache = PaCache.getInstance();
		cache.pub("_viewId", viewId);
		
		ModePhase modePhase=(ModePhase)PaCache.getInstance().get(PaCache.ModePhase);
		if(ModePhase.nodedef.equals(modePhase)){
			if(!PaCache.FreeBillCtrlClassName.equalsIgnoreCase(javaFullClassName) && !PaCache.FreeBillGrdiClassName.equalsIgnoreCase(javaFullClassName)){
				if(StringUtils.isBlank(javaPath)){
					throw new LuiRuntimeException("不使用默认提供的controller,请提供java文件的生成路径!");
				}
			}
		}
		PagePartMeta pagePartMeta = null;
		{
			ViewPartMeta viewPartMeta = new ViewPartMeta();
			viewPartMeta.setId(viewId);
			viewPartMeta.setController(javaFullClassName);
			
			String xml0 = viewPartMeta.toXml();
			File file0 = this.createViewPartMetaFile();
			try {
				FileUtils.write(file0, xml0, "utf-8");
			} catch (IOException e) {
				LuiLogger.error(e.getMessage(), e);
			}
			
			String nodePath =PaCache.getEditorPageXmlPath();
			InputStream inputStream=null;
			try {
				inputStream=new FileInputStream(new File(nodePath));
			} catch (Throwable e1) {
				throw new LuiRuntimeException(e1.getMessage());
			}
			pagePartMeta = PagePartMeta.parse(inputStream);
			ViewPartConfig viewPartConif = new ViewPartConfig();
			viewPartConif.setId(viewId);
			viewPartConif.setRefId(viewId);
			pagePartMeta.addViewPartConf(viewPartConif);
			pagePartMeta.addWidget(viewPartMeta);
			//新建视图后添加到UIState
			addViewStateToUIState(pagePartMeta, viewId);
			
			File file1 = this.createPagePartMetaFile(pageId);
			try {
				FileUtils.write(file1, pagePartMeta.toXml(), "utf-8");
			} catch (IOException e) {
				LuiLogger.error(e.getMessage(), e);
			}
			IPaEditorService ipaService = new PaEditorServiceImpl();
			String sessionId = (String) PaCache.getInstance().get("eclipse_sesionId");
			ipaService.setOriPageMeta(pageId, sessionId, pagePartMeta);
		}
		{
			
			LifeCyclePhase phase = RequestLifeCycleContext.get().getPhase();
			RequestLifeCycleContext.get().setPhase(LifeCyclePhase.design);
			UIPartMeta viewUIPartMeta = new UIPartMeta();
			viewUIPartMeta.setId(pageId + "_um");
			try {
				String xml1 = viewUIPartMeta.toXml();
				File file = this.createUIPartMetaFile(pageId, viewId);
				FileUtils.write(file, xml1, "utf-8");
			} catch (IOException e) {
				LuiLogger.error(e.getMessage(), e);
			}
			
			UIPartMeta	pageUIPartMeta = new UIPartMeta();
			UIViewPart uiViewPart = new UIViewPart();
			uiViewPart.setId(viewId);
			pageUIPartMeta.setElement(uiViewPart);
			uiViewPart.setUimeta(viewUIPartMeta);
			RequestLifeCycleContext.get().setPhase(phase);
			IPaEditorService ipaService = new PaEditorServiceImpl();
			String sessionId = (String) PaCache.getInstance().get("eclipse_sesionId");
			ipaService.setOriUIMeta(pageId, sessionId, pageUIPartMeta);
		}
		{
			
			try {
				//生成java文件
				String javaClassName = PaProjViewTreeController.getJavaClassName(javaFullClassName);
				String javaPackageName = PaProjViewTreeController.getPackageName(javaFullClassName);
				String javaContent = CreateJavaUtil.getControllerClazz(javaPackageName, javaClassName);
				if(ModePhase.eclipse.equals(modePhase)){
					File file = PaProjViewTreeController.createJavaFile(javaFullClassName, null);
					FileUtils.write(file, javaContent, "utf-8");
				}
				//如果是自由表单的，并且是用的默认的控制类，就不做生成
				if(ModePhase.nodedef.equals(modePhase)&&(PaCache.FreeBillCtrlClassName.equals(javaFullClassName) || PaCache.FreeBillGrdiClassName.equals(javaFullClassName))){
					
				}else if(ModePhase.nodedef.equals(modePhase)&& !PaCache.FreeBillCtrlClassName.equals(javaFullClassName)&& !PaCache.FreeBillGrdiClassName.equals(javaFullClassName)){
					File file = PaProjViewTreeController.createJavaFile(javaFullClassName, javaPath);
					FileUtils.write(file, javaContent, "utf-8");
				}
			} catch (IOException e) {
				LuiLogger.error(e.getMessage(), e);
			}
		}
		//切换视图
		if(ModePhase.nodedef.equals(modePhase)){
			if(PaCache.FreeBillCtrlClassName.equalsIgnoreCase(javaFullClassName) || PaCache.FreeBillGrdiClassName.equalsIgnoreCase(javaFullClassName)){
				TopMainViewController.switchViewDeal(pagePartMeta,viewId);
			}
		}
		
		//增加导航树的视图，只有设计
		if(ModePhase.eclipse.equals(modePhase)){
			DesignerMainViewController desinerMainViewController = new DesignerMainViewController();
			Dataset projectDs = LuiAppUtil.getDataset("projViewTreeDs", "project");
			desinerMainViewController._add_view_as_row(pagePartMeta, pageId, projectDs);
		}
	}
	//新建视图后添加到UIState
	private void addViewStateToUIState(PagePartMeta pagePartMeta, String viewId) {
		 List<UIState> uiStates = pagePartMeta.getuIStates();
		 if(uiStates != null && uiStates.size() > 0){
			 for(UIState uiState : uiStates){
				 ViewState viewState = new ViewState();
				 viewState.setViewId(viewId);
				 uiState.addViewState(viewState);
			 }
		 }
	}
	//删除视图后从UIState删除
	private void delViewStateFromUIState(PagePartMeta pagePartMeta, String viewId){
		List<UIState> uiStates = pagePartMeta.getuIStates();
		if (uiStates != null && uiStates.size() > 0) {
			for (UIState uiState : uiStates) {
				uiState.removeViewState(viewId);
			}
		}
	}
	
	public void _edit_view(String pageId, String viewId) {
		PaCache.getInstance().remove("_version");
		LuiWebContext web_ctx = LuiRuntimeContext.getWebContext();
		PagePartMeta design_page = web_ctx.getPageMeta();
		IFrameComp iframe = (IFrameComp) design_page.getWidget("editor").getViewComponents().getComponent("iframe_tmp");
		PagePartMeta pagePartMeta = CreateDesignModel.createDesignPageMeta(pageId);
		UIPartMeta uiPartMeta = CreateDesignModel.createDesignUIMeta(pagePartMeta, pageId, viewId);
		{
			PaCache.luiMeta = null;
			LuiMeta luiMeta = new LuiMeta();
			luiMeta.setPagePartMeta(pagePartMeta);
			luiMeta.setuIPartMeta(uiPartMeta);
			PaCache.luiMeta = luiMeta;
			String otherPageId = LuiRuntimeContext.getWebContext().getPageUniqueId();
			String url1 = "/portal/app/mockapp/" + pageId;
			url1 += "?"+LuiRuntimeContext.MODEPHASE+"=eclipse&viewId=" + viewId;
			url1 += "&emode=1&model=" + RaSelfWindow.class.getName() + "&otherPageUniqueId=" + otherPageId;
			iframe.setSrc(url1);
		}
		{
			DataModels dataModels = design_page.getWidget("data").getViewModels();
			Dataset ctrlDs = dataModels.getDataset("ctrlds");
			PaPalletDsListener.fillCtrlDs(ctrlDs, pagePartMeta, viewId);
//			Dataset currDs = dataModels.getDataset("currds");
//			PaPalletDsListener.fillCurrDs(currDs, pagePartMeta, uiPartMeta);
			Dataset layOutDs = dataModels.getDataset("layoutds");
			PaPalletDsListener.fillLayoutDs(layOutDs);
			Dataset entityDs = dataModels.getDataset("entityds");
			PaEntityDsListener.setModelData(entityDs, pagePartMeta.getWidget(viewId));
		}
	}
}
