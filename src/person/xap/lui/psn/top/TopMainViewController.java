package xap.lui.psn.top;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import xap.lui.core.builder.RaSelfWindow;
import xap.lui.core.cache.CacheMgr;
import xap.lui.core.cache.LUICache;
import xap.lui.core.cache.PaCache;
import xap.lui.core.common.InteractionUtil;
import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.common.LuiWebContext;
import xap.lui.core.comps.BarChartComp;
import xap.lui.core.comps.ChartBaseComp;
import xap.lui.core.comps.ContextMenuComp;
import xap.lui.core.comps.IFrameComp;
import xap.lui.core.comps.LineChartComp;
import xap.lui.core.comps.MenuItem;
import xap.lui.core.comps.MenubarComp;
import xap.lui.core.comps.PieChartComp;
import xap.lui.core.comps.ToolBarComp;
import xap.lui.core.comps.ToolBarItem;
import xap.lui.core.comps.WebComp;
import xap.lui.core.constant.WebConstant;
import xap.lui.core.control.ModePhase;
import xap.lui.core.control.ResourceFrom;
import xap.lui.core.dataset.ComboData;
import xap.lui.core.dataset.DataItem;
import xap.lui.core.dataset.DataList;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.Row;
import xap.lui.core.event.DatasetEvent;
import xap.lui.core.event.DialogEvent;
import xap.lui.core.event.MouseEvent;
import xap.lui.core.exception.ComboInputItem;
import xap.lui.core.exception.InputItem;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.exception.StringInputItem;
import xap.lui.core.layout.UIPartMeta;
import xap.lui.core.layout.UITabComp;
import xap.lui.core.layout.UITabItem;
import xap.lui.core.listener.EventSubmitRule;
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
import xap.lui.core.model.ViewPartComps;
import xap.lui.core.model.ViewPartConfig;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.core.plugins.ILuiPaltformExtProvier;
import xap.lui.core.plugins.LuiPaltformContranier;
import xap.lui.core.services.IPaEditorService;
import xap.lui.core.services.PaEditorServiceImpl;
import xap.lui.core.util.JsURLEncoder;
import xap.lui.psn.command.PaHelper;
import xap.lui.psn.designer.CreateDesignModel;
import xap.lui.psn.designer.DesignerMainViewController;
import xap.lui.psn.designer.PaProjViewTreeController;
import xap.lui.psn.java.Command2TplCache;
import xap.lui.psn.java.CreateJavaUtil;
import xap.lui.psn.pamgr.PaEntityDsListener;
import xap.lui.psn.pamgr.PaModelOperateController;
import xap.lui.psn.pamgr.PaPalletDsListener;
import xap.lui.psn.setting.PaSettingDsListener;

public class TopMainViewController {
	// 配置向导 需要使用的 配置ds关系的ViewPartMeta;
	public static final String vPM_cfgDsRela = "ViewPartMeta_ConfigDsRelation";
	private ViewPartMeta sourceView = null;
	private PagePartMeta sourceWin = null;

	public void setWSCtrl(MouseEvent event) {
		File file = getWorkSpaceFile();
		Properties prop = getWorkspace(file);
		String _resourceFolder = prop.getProperty("_resourceFolder");
		String _javaFolder = prop.getProperty("_javaFolder");
		String _prjRoot = prop.getProperty("_prjRoot");
		PaCache cache = PaCache.getInstance();
		if (StringUtils.isNotBlank(_resourceFolder)) {
			// cache.pub("_resourceFolder",
			// "E:\\xap_dev\\workspace3\\lui\\xap.lui.dinner.1.0\\src\\main\\resource");
			cache.pub("_resourceFolder", _resourceFolder);
		}
		if (StringUtils.isNotBlank(_javaFolder)) {
			// cache.pub("_javaFolder",
			// "E:\\xap_dev\\workspace3\\lui\\xap.lui.dinner.1.0\\src\\main\\java");
			cache.pub("_javaFolder", _javaFolder);
		}
		if (StringUtils.isNotBlank(_prjRoot)) {
			cache.pub("_prjRoot", _prjRoot);
		}
		InputItem projectNameItem = new StringInputItem("projectName", "项目名称：", true);
		projectNameItem.setValue(cache.get("_projName"));
		InputItem projectRootItem = new StringInputItem("projectRoot", "项目根路径：", true);
		projectRootItem.setValue(_prjRoot);
		InputItem javaPathItem = new StringInputItem("javaPath", "java路径：", true);
		javaPathItem.setValue(_javaFolder);
		InputItem resourcePathItem = new StringInputItem("resourcePath", "资源路径：", true);
		resourcePathItem.setValue(_resourceFolder);
		InteractionUtil.showInputDialog("目录设定", new InputItem[] { projectNameItem, projectRootItem, javaPathItem, resourcePathItem });
		Map<String, String> rs = InteractionUtil.getInputDialogResult();
		cache.pub("_projName", rs.get("projectName"));
		_resourceFolder = rs.get("resourcePath");
		_javaFolder = rs.get("javaPath");
		_prjRoot = rs.get("projectRoot");
		cache.pub("_resourceFolder", _resourceFolder);
		prop.put("_resourceFolder", _resourceFolder);
		cache.pub("_javaFolder", _javaFolder);
		prop.put("_javaFolder", _javaFolder);
		cache.pub("_prjRoot", _prjRoot);
		prop.put("_prjRoot", _prjRoot);
		FileOutputStream output = null;
		try {
			output = new FileOutputStream(file);
			prop.store(output, null);
		} catch (Throwable e) {
			LuiLogger.error(e.getMessage(), e);
		} finally {
			IOUtils.closeQuietly(output);
		}
		DesignerMainViewController desinerMainViewController = new DesignerMainViewController();
		DatasetEvent DatasetEvent = new DatasetEvent(LuiAppUtil.getDataset("projViewTreeDs", "project"));
		desinerMainViewController.onDataLoad(DatasetEvent);
	}

	public File getWorkSpaceFile() {
		String userHome = System.getProperty("user.home");
		String workspacePath = userHome + "/lui/workspace.properties";
		File folder = new File(userHome + "/lui/");
		if (folder.exists()) {
			folder.mkdirs();
		}
		File file = new File(workspacePath);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				LuiLogger.error(e.getMessage(), e);
			}
		}
		return file;
	}

	public Properties getWorkspace(File file) {
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(file));
		} catch (Throwable e) {
			LuiLogger.error(e.getMessage(), e);
		}
		return prop;
	}

	public void configGuided(MouseEvent event) {
		ViewPartMeta viewPartMeta = new ViewPartMeta();
		viewPartMeta.setId(vPM_cfgDsRela);// 配置数据集关联的ViewPartMeta;
		PaCache.getInstance().pub(vPM_cfgDsRela, viewPartMeta);
		PagePartMeta pagePartMeta = new PagePartMeta();
		String pageId = "guided_Relation";
		pagePartMeta.setId(pageId);
		// ViewPartConfig viewPartConif = new ViewPartConfig();
		// viewPartConif.setId(vPM_cfgDsRela);
		// viewPartConif.setRefId(vPM_cfgDsRela);
		// pagePartMeta.addViewPartConf(viewPartConif);
		pagePartMeta.addWidget(viewPartMeta);
		PaCache.getInstance().pub("_pageId", pageId);
		PaCache.getInstance().pub("_viewId", vPM_cfgDsRela);
		IPaEditorService ipaService = new xap.lui.core.services.PaEditorServiceImpl();
		String sessionId = (String) PaCache.getInstance().get("eclipse_sesionId");
		ipaService.setOriPageMeta(pageId, sessionId, pagePartMeta);
		AppSession.current().getAppContext().navgateTo("guided", "配置向导", "950", "600", null);
	}

	public void graphicMenuClickHandler(MouseEvent<MenuItem> event) {
		MenuItem menuItem = (MenuItem) event.getSource();
		String menuItemId = menuItem.getId();
		InputItem chartIdItem = new StringInputItem("chartId", "图表ID：", true);
		InputItem datamodelItem = new StringInputItem("datamodel", "数据模型：", true);
		switch (menuItemId) {
		case "bzzx_gxh":
			InteractionUtil.showInputDialog("新建折线图", new InputItem[] { chartIdItem, datamodelItem });
			break;
		case "bzzz":
			InteractionUtil.showInputDialog("新建柱状图", new InputItem[] { chartIdItem, datamodelItem });
			break;
		case "bzbt":
			InteractionUtil.showInputDialog("新建饼状图", new InputItem[] { chartIdItem, datamodelItem });
			break;
		}
		Map<String, String> rs = InteractionUtil.getInputDialogResult();
		String chartId = (String) rs.get("chartId");
		Dataset chartDs = LuiAppUtil.getView("data").getViewModels().getDataset("ctrlds");
		Row row = chartDs.getEmptyRow();
		ChartBaseComp comp = null;
		switch (menuItemId) {
		case "bzzx_gxh":
			comp = new LineChartComp();
			row.setValue(chartDs.nameToIndex("type"), "line");
			break;
		case "bzzz":
			comp = new BarChartComp();
			row.setValue(chartDs.nameToIndex("type"), "bar");
			break;
		case "bzbt":
			comp = new PieChartComp();
			row.setValue(chartDs.nameToIndex("type"), "pie");
			break;
		}
		comp.setId(chartId);
		PaCache.getEditorViewPartMeta().getViewComponents().addComponent(comp);
		row.setValue(chartDs.nameToIndex("id"), chartId);
		row.setValue(chartDs.nameToIndex("name"), chartId);
		row.setValue(chartDs.nameToIndex("pid"), PaPalletDsListener.DEFINED);
		chartDs.addRow(row);
	}

	public void edit_online(MouseEvent<MenuItem> event) {
		LuiAppUtil.getCntWindowCtx().popView("file", "800", "600", "程序文件");
	}

	public void editwin(MouseEvent event) {
		ModePhase modePhase = (ModePhase) PaCache.getInstance().get(PaCache.ModePhase);
		PagePartMeta design_page = getDesignPage();
		if (ModePhase.nodedef.equals(modePhase)) {// 自由表单
			IFrameComp iframe = (IFrameComp) design_page.getWidget("editor").getViewComponents().getComponent("iframe_tmp");
			PagePartMeta pagePartMeta = PaCache.getEditorPagePartMeta();
			String pageId = pagePartMeta.getId();
			UIPartMeta uiPartMeta = CreateDesignModel.createDesignUIMeta(pagePartMeta, pageId, null);

			{
				PaCache.luiMeta = null;
				LuiMeta luiMeta = new LuiMeta();
				luiMeta.setPagePartMeta(pagePartMeta);
				luiMeta.setuIPartMeta(uiPartMeta);
				PaCache.luiMeta = luiMeta;
				String otherPageId = LuiRuntimeContext.getWebContext().getPageUniqueId();
				String url1 = "/portal/app/mockapp/" + pageId;
				url1 += "?" + LuiRuntimeContext.MODEPHASE + "=eclipse";
				url1 += "&emode=1&model=" + RaSelfWindow.class.getName() + "&otherPageUniqueId=" + otherPageId;
				iframe.setSrc(url1);
			}
			{
				DataModels dataModels = design_page.getWidget("data").getViewModels();
				Dataset ctrlDs = dataModels.getDataset("ctrlds");
				PaPalletDsListener.fillCtrlDs(ctrlDs, pagePartMeta, null);
				Dataset layOutDs = dataModels.getDataset("layoutds");
				PaPalletDsListener.fillLayoutDs(layOutDs);
				Dataset entityDs = dataModels.getDataset("entityds");
				PaEntityDsListener.setModelData(entityDs, null);
			}
		} else {
			removeAppCache();
			Dataset dataset = design_page.getWidget("project").getViewModels().getDataset("projViewTreeDs");
			Row row = dataset.getSelectedRow();
			if (row == null)
				throw new LuiRuntimeException("请选中行！");
			String type = (String) row.getValue(dataset.nameToIndex("type"));
			if (!type.equals("page"))// 若选中page则
				throw new LuiRuntimeException("请选中page!");
			String node_name = (String) row.getValue(dataset.nameToIndex("name"));
			PaCache.getInstance().pub("_pageId", node_name);
			PaProjViewTreeController pctrl = new PaProjViewTreeController();
			pctrl._edit_page(node_name);
			pctrl.setTabItemStatus(dataset, row, type);
		}
	}

	public void switchView(MouseEvent event) {
		LifeCyclePhase phase = RequestLifeCycleContext.get().getPhase();
		RequestLifeCycleContext.get().setPhase(LifeCyclePhase.nullstatus);
		InputItem projectNameItem = new ComboInputItem("view", "视图：", true);
		PagePartMeta pagePartMeta = PaCache.getEditorPagePartMeta();
		ComboData comboData = new DataList();
		ViewPartConfig[] configs = pagePartMeta.getViewPartConfs();
		for (int i = 0; i < configs.length; i++) {
			DataItem dataItem = new DataItem(configs[i].getId(), configs[i].getId());
			comboData.addDataItem(dataItem);
		}
		projectNameItem.setComboData(comboData);
		RequestLifeCycleContext.get().setPhase(phase);
		InteractionUtil.showInputDialog("视图切换", new InputItem[] { projectNameItem });
		{
			Map<String, String> rs = InteractionUtil.getInputDialogResult();
			String viewId = rs.get("view");
			switchViewDeal(pagePartMeta, viewId);
		}
	}

	public static void switchViewDeal(PagePartMeta pagePartMeta, String viewId) {
		PaCache.getInstance().pub("_viewId", viewId);
		String pageId = pagePartMeta.getId();
		PagePartMeta design_page = getDesignPage();
		IFrameComp iframe = (IFrameComp) design_page.getWidget("editor").getViewComponents().getComponent("iframe_tmp");
		UIPartMeta uiPartMeta = CreateDesignModel.createDesignUIMeta(pagePartMeta, pageId, viewId);
		{
			PaCache.luiMeta = null;
			LuiMeta luiMeta = new LuiMeta();
			luiMeta.setPagePartMeta(pagePartMeta);
			luiMeta.setuIPartMeta(uiPartMeta);
			PaCache.luiMeta = luiMeta;
			String otherPageId = LuiRuntimeContext.getWebContext().getPageUniqueId();
			String url1 = "/portal/app/mockapp/" + pageId;
		      
			url1 += "?" + LuiRuntimeContext.MODEPHASE + "="+ModePhase.eclipse.toString()+"&viewId=" + viewId;
			url1 += "&emode=1&model=" + RaSelfWindow.class.getName() + "&otherPageUniqueId=" + otherPageId;
			iframe.setSrc(url1);
		}
		{
			DataModels dataModels = design_page.getWidget("data").getViewModels();
			Dataset ctrlDs = dataModels.getDataset("ctrlds");
			PaPalletDsListener.fillCtrlDs(ctrlDs, pagePartMeta, viewId);
			Dataset layOutDs = dataModels.getDataset("layoutds");
			PaPalletDsListener.fillLayoutDs(layOutDs);
			Dataset entityDs = dataModels.getDataset("entityds");
			PaEntityDsListener.setModelData(entityDs, pagePartMeta.getWidget(viewId));
		}
	}

	private static PagePartMeta getDesignPage() {
		LuiWebContext web_ctx = LuiRuntimeContext.getWebContext();
		PagePartMeta design_page = web_ctx.getPageMeta();
		return design_page;
	}

	public void newView(MouseEvent event) {
		ModePhase modePhase = (ModePhase) PaCache.getInstance().get(PaCache.ModePhase);
		if (ModePhase.nodedef.equals(modePhase)) {// 自由表单
			String nodeType = LuiRuntimeContext.getWebContext().getParameter("nodeType");
			LuiAppUtil.addAppAttr("nodeType", nodeType);
			LuiAppUtil.addAppAttr("pageId", PaCache.getEditorPageId());
			LuiAppUtil.getCntWindowCtx().popView("newview", "400", "280", "新增视图");
		} else {
			PagePartMeta design_page = getDesignPage();
			Dataset dataset = design_page.getWidget("project").getViewModels().getDataset("projViewTreeDs");
			Row row = dataset.getSelectedRow();
			if (row == null)
				throw new LuiRuntimeException("请选中行！");
			String type = (String) row.getValue(dataset.nameToIndex("type"));
			if (!type.equals("page"))// 若选中非page
				throw new LuiRuntimeException("请选中page!");
			removeAppCache();
			String node_name = (String) row.getValue(dataset.nameToIndex("name"));
			PaCache.getInstance().pub("_pageId", node_name);
			PaProjViewTreeController pctrl = new PaProjViewTreeController();
			pctrl._new_view(node_name);
			pctrl.setTabItemStatus(dataset, row, type);
		}
	}

	public void save_file(MouseEvent event) {
		String editstate = PaCache.getEditState();
		if (StringUtils.isNotBlank(editstate)) {// 保存view时再将page保存一次
			if (!StringUtils.equals(editstate, "app")) {
				if (StringUtils.equals(editstate, "view")) {
					String xml1 = PaCache.getNowPage();
					String path = PaCache.getEditorPageXmlPath();
					writeFile(xml1, path);
				}
				String xml0 = PaCache.getNowViewOrPageMetaXml();
				String compMetaPath = PaCache.getEditorCompMetaXmlPath();
				writeFile(xml0, compMetaPath);
				String xml1 = PaCache.getNowUiMetaXml();
				String uiMetaPath = PaCache.getEditorUIMetaXmlPath();
				writeFile(xml1, uiMetaPath);
				this.saveEventToJavaFile();
			} else {// 保存app
				String xml = PaCache.getNowApp();
				String path = PaCache.getEditorAppXmlPath();
				writeFile(xml, path);
			}
			InteractionUtil.showMessageDialog("保存成功");
		}
	}

	private void writeFile(String xml, String path) {
		try {
			File file = new File(path);
			if (file.exists()) {
				FileUtils.write(file, xml, "utf-8");
			}
		} catch (IOException e) {
			LuiLogger.error(e.getMessage(), e);
		}
	}

	public void saveEventToJavaFile() {
		ViewPartMeta viewPartMeta = PaCache.getEditorViewPartMeta();
		if (viewPartMeta != null) {
			ViewPartComps viewComps = viewPartMeta.getViewComponents();
			WebComp[] comps = viewComps.getComps();
			for (WebComp inner : comps) {
				if (inner instanceof ToolBarComp) {
					WebComp[] toolbaritems = ((ToolBarComp) inner).getElements();
					for (WebComp toolbaritem : toolbaritems) {
						List<LuiEventConf> eventList = toolbaritem.getEventConfList();
						if (eventList != null) {
							for (LuiEventConf event : eventList) {
								String cmdName = event.getModelCmd();
								if (cmdName == null) {
									continue;
								}
								event.setEventStatus(LuiEventConf.ADD_STATUS);
								String controller = PaCache.getEditorControllerPath(event.getControllerClazz());
								File file = new File(controller);
								String oldContent = null;
								try {
									oldContent = FileUtils.readFileToString(file, "utf-8");
									String tplName = Command2TplCache.getTplName(cmdName);
									{
										String allContent = CreateJavaUtil.operateMethod(cmdName, oldContent, tplName, event);
										FileUtils.write(file, allContent, "utf-8");
									}
								} catch (IOException e1) {
									LuiLogger.error(e1.getMessage(), e1);
								}
							}
						}
					}
				} else {
					List<LuiEventConf> eventList = inner.getEventConfList();
					if (eventList != null) {
						for (LuiEventConf event : eventList) {
							String cmdName = event.getModelCmd();
							if (cmdName == null) {
								continue;
							}
							event.setEventStatus(LuiEventConf.ADD_STATUS);
							String controller = PaCache.getEditorControllerPath(event.getControllerClazz());
							File file = new File(controller);
							String oldContent = null;
							try {
								oldContent = FileUtils.readFileToString(file, "utf-8");
								String tplName = Command2TplCache.getTplName(cmdName);
								{
									String allContent = CreateJavaUtil.operateMethod(cmdName, oldContent, tplName, event);
									FileUtils.write(file, allContent, "utf-8");
								}
							} catch (IOException e1) {
								LuiLogger.error(e1.getMessage(), e1);
							}
						}
					}
				}
			}
			Dataset[] datasets = viewPartMeta.getViewModels().getDatasets();
			for (Dataset inner : datasets) {
				List<LuiEventConf> eventList = inner.getEventConfList();
				if (eventList == null) {
					continue;
				}
				for (LuiEventConf event : eventList) {
					String cmdName = event.getModelCmd();
					if (cmdName == null) {
						continue;
					}
					event.setEventStatus(LuiEventConf.ADD_STATUS);
					String tplName = Command2TplCache.getTplName(cmdName);
					// tplName=tplName+".ftl";
					String controller = PaCache.getEditorControllerPath(event.getControllerClazz());
					File file = new File(controller);
					String content = null;
					try {
						content = FileUtils.readFileToString(file, "utf-8");
						content = CreateJavaUtil.operateMethod(cmdName, content, tplName, event);
						FileUtils.write(file, content, "utf-8");
					} catch (IOException e1) {
						LuiLogger.error(e1.getMessage(), e1);
					}
				}
			}
			MenubarComp[] menus = viewPartMeta.getViewMenus().getMenuBars();
			for (MenubarComp menu : menus) {
				List<MenuItem> menuitems = menu.getMenuList();
				for (MenuItem item : menuitems) {
					List<LuiEventConf> eventList = item.getEventConfList();
					if (eventList == null) {
						continue;
					}
					for (LuiEventConf event : eventList) {
						String cmdName = event.getModelCmd();
						if (cmdName == null) {
							continue;
						}
						event.setEventStatus(LuiEventConf.ADD_STATUS);
						String tplName = Command2TplCache.getTplName(cmdName);
						String controller = PaCache.getEditorControllerPath(event.getControllerClazz());
						File file = new File(controller);
						String content = null;
						try {
							content = FileUtils.readFileToString(file, "utf-8");
							content = CreateJavaUtil.operateMethod(cmdName, content, tplName, event);
							FileUtils.write(file, content, "utf-8");
						} catch (IOException e1) {
							LuiLogger.error(e1.getMessage(), e1);
						}
					}
				}
			}
			ContextMenuComp[] contextMenus = viewPartMeta.getViewMenus().getContextMenus();
			for (ContextMenuComp menu : contextMenus) {
				List<MenuItem> menuitems = menu.getItemList();
				for (MenuItem item : menuitems) {
					List<LuiEventConf> eventList = item.getEventConfList();
					if (eventList == null) {
						continue;
					}
					for (LuiEventConf event : eventList) {
						String cmdName = event.getModelCmd();
						if (cmdName == null) {
							continue;
						}
						event.setEventStatus(LuiEventConf.ADD_STATUS);
						String tplName = Command2TplCache.getTplName(cmdName);
						String controller = PaCache.getEditorControllerPath(event.getControllerClazz());
						File file = new File(controller);
						String content = null;
						try {
							content = FileUtils.readFileToString(file, "utf-8");
							content = CreateJavaUtil.operateMethod(cmdName, content, tplName, event);
							FileUtils.write(file, content, "utf-8");
						} catch (IOException e1) {
							LuiLogger.error(e1.getMessage(), e1);
						}
					}
				}
			}
		}
	}

	public void refresh_file(MouseEvent event) {
	}

	// 保存还原点
	public void save_point(MouseEvent event) {
		PagePartMeta recovePagePart = PaCache.getEditorPagePartMeta();
		UIPartMeta recoveUIPart = PaCache.getEditorUIPartMeta();
		PaCache.recovePointPagePartMeta = recovePagePart;
		PaCache.recovePointUIPartMeta = recoveUIPart;
	}

	public void view_file(MouseEvent event) {
		LuiAppUtil.getCntWindowCtx().popView("file", "800", "600", "程序文件");
	}

	public void export_file(MouseEvent<MenuItem> event) {
	}

	// 还原文件
	public void recove_file(MouseEvent event) {
		PagePartMeta recovePagePart = PaCache.recovePointPagePartMeta;
		UIPartMeta recoveUIPart = PaCache.recovePointUIPartMeta;
		if (recovePagePart == null)
			throw new LuiRuntimeException("没有还原点！");
		IPaEditorService ipaService = new PaEditorServiceImpl();
		String sessionId = (String) PaCache.getInstance().get("eclipse_sesionId");
		ipaService.setOriPageMeta(recovePagePart.getId(), sessionId, recovePagePart);
		ipaService.setOriUIMeta(recoveUIPart.getId(), sessionId, recoveUIPart);
	}

	public void run_app(MouseEvent event) {
		PaCache cache = PaCache.getInstance();
		String pageId = (String) cache.get("_pageId");
		String url = "/portal/app/mockapp/" + pageId + "";
		ModePhase phase=LuiRuntimeContext.getModePhase();
		if (ModePhase.nodedef.equals(phase)) {
			String version = (String) cache.get("_version");
			url = url + "?"+LuiRuntimeContext.VESION+"=" + version+"&"+LuiRuntimeContext.RESOUCEFROM+'='+ResourceFrom.nodedef.toString();
		}
		if(ModePhase.persona.equals(phase)){
			String personcode=(String)cache.get("_personcode");
			url = url + "?"+LuiRuntimeContext.PersonaCode+"=" + personcode+"&"+LuiRuntimeContext.RESOUCEFROM+'='+ResourceFrom.persona.toString();
		}
		LuiAppUtil.getCntAppCtx().addExecScript("$.pageutils.showMaxWin('" + url + "','',false)");
	}

	public void switch_theme(MouseEvent event) {
	}

	// 新建app
	public void newApp(MouseEvent e) {
		PagePartMeta design_page = getDesignPage();
		Dataset dataset = design_page.getWidget("project").getViewModels().getDataset("projViewTreeDs");
		Row row = dataset.getSelectedRow();
		if (row == null)
			throw new LuiRuntimeException("请选中行！");
		String type = (String) row.getValue(dataset.nameToIndex("type"));
		PaProjViewTreeController pctrl = new PaProjViewTreeController();
		pctrl._new_app();
		pctrl.setTabItemStatus(dataset, row, type);
	}

	// 新建page
	public void newPage(MouseEvent e) {
		removeAppCache();
		PagePartMeta design_page = getDesignPage();
		Dataset dataset = design_page.getWidget("project").getViewModels().getDataset("projViewTreeDs");
		Row row = dataset.getSelectedRow();
		if (row == null)
			throw new LuiRuntimeException("请选中行！");
		String type = (String) row.getValue(dataset.nameToIndex("type"));
		PaProjViewTreeController pctrl = new PaProjViewTreeController();
		pctrl._new_page();
		pctrl.setTabItemStatus(dataset, row, type);
	}

	// 编辑视图
	public void editView(MouseEvent e) {
		PagePartMeta design_page = getDesignPage();
		Dataset dataset = design_page.getWidget("project").getViewModels().getDataset("projViewTreeDs");
		Row row = dataset.getSelectedRow();
		if (row == null)
			throw new LuiRuntimeException("请选中行！");
		String type = (String) row.getValue(dataset.nameToIndex("type"));
		if (!type.equals("view"))// 若选中非view
			throw new LuiRuntimeException("请选中view!");
		removeAppCache();
		String node_name = (String) row.getValue(dataset.nameToIndex("name"));
		String pageId = (String) row.getValue(dataset.nameToIndex("id"));
		pageId = pageId.substring(0, pageId.lastIndexOf(node_name) - 1);
		PaCache.getInstance().pub("_pageId", pageId);
		PaCache.getInstance().pub("_viewId", node_name);
		PaProjViewTreeController pctrl = new PaProjViewTreeController();
		pctrl._edit_view(pageId, node_name);
		pctrl.setTabItemStatus(dataset, row, type);
	}

	// 编辑应用
	public void editApp(MouseEvent e) {
		PagePartMeta design_page = getDesignPage();
		Dataset dataset = design_page.getWidget("project").getViewModels().getDataset("projViewTreeDs");
		Row row = dataset.getSelectedRow();
		if (row == null)
			throw new LuiRuntimeException("请选中行！");
		String type = (String) row.getValue(dataset.nameToIndex("type"));
		if (!type.equals("app"))// 若选中非page
			throw new LuiRuntimeException("请选中app!");
		String node_name = (String) row.getValue(dataset.nameToIndex("name"));
		PaCache.getInstance().pub("_appId", node_name);
		PaProjViewTreeController pctrl = new PaProjViewTreeController();
		pctrl._edit_app(node_name);
		pctrl.setTabItemStatus(dataset, row, type);
	}

	// 添加容器
	public void addLayout(MouseEvent<MenuItem> e) {
	}

	// 窗口出现前增加控件的菜单子项
	public void onBeforeShow(DialogEvent e) {
		// PagePartMeta design_page = getDesignPage();
		// 菜单
		// MenubarComp menu =
		// design_page.getWidget("top").getViewMenus().getMenuBar("menu_top");
		// MenuItem ctrlItem = menu.getItem("menu_top_ctrl");
		// Map<String, Object[]> map =
		// PaPalletDsListener.getAttrForTreeNodeMap();
		// genCtrlTreeCxtMenu(ctrlItem, map, "");
		// toolbar
		// ToolBarComp toolbar = (ToolBarComp)
		// design_page.getWidget("top").getViewComponents().getComponent("toptoolbar");
		// genCtrlToolItem(toolbar, map);
	}

	// private void genCtrlToolItem(ToolBarComp toolbar, Map<String, Object[]>
	// map) {
	// for (String key : map.keySet()) {
	// Object[] treeNodeMap = map.get(key);
	// String _pid = (String) treeNodeMap[6];
	// if (StringUtils.isNotBlank(_pid)) {
	// ToolBarItem pitem = toolbar.getElementById(_pid+"_ctrl");
	// genCtrlChildItem(pitem, key, treeNodeMap, _pid);
	// } else {
	// ToolBarItem item = new ToolBarItem();
	// item.setId(key + "_ctrl");
	// item.setTip((String) treeNodeMap[1]);
	// String imgIcon = null;
	// String img = (String) treeNodeMap[7];
	// if (StringUtils.isNotBlank(img)) {
	// imgIcon = img;
	// } else {
	// imgIcon = "icon/" + (String) treeNodeMap[0] + ".png";
	// }
	// item.setRefImg(imgIcon);
	// genCtrlToolItemEvent(item, treeNodeMap[2] != null);
	// toolbar.addElement(item);
	// }
	// }
	// }

	// private void genCtrlChildItem(ToolBarItem item, String key, Object[]
	// treeNodeMap, String _pid) {
	// PagePartMeta design_page = getDesignPage();
	// ContextMenuComp menu =
	// design_page.getWidget("top").getViewMenus().getContextMenu(item.getContextMenu());
	// if(menu == null){
	// String ctxmenuId = _pid+"_ctxmenu";
	// item.setContextMenu(ctxmenuId);
	// menu = new ContextMenuComp(ctxmenuId);
	// }
	// MenuItem menuItem = new MenuItem();
	// menuItem.setId(key);
	// menuItem.setText((String) treeNodeMap[1]);
	// menuItem.setTip((String) treeNodeMap[1]);
	// String imgIcon = null;
	// String img = (String) treeNodeMap[7];
	// if (StringUtils.isNotBlank(img)) {
	// imgIcon = img;
	// } else {
	// imgIcon = "platform/theme/${theme}/global/images/icon/12/icon/" +
	// (String) treeNodeMap[0] + ".png";
	// }
	// menuItem.setImgIcon(imgIcon);
	// genCtxItemEvent(item, treeNodeMap[2] != null);
	// menu.addMenuItem(menuItem);
	// }
	//
	// private void genCtxItemEvent(ToolBarItem item, boolean hasEvent) {
	// if (hasEvent) {
	// LuiEventConf event = new LuiEventConf();
	// event.setEventType(MouseEvent.class.getSimpleName());
	// event.setOnserver(true);
	// EventSubmitRule submitRule = new EventSubmitRule();
	// event.setSubmitRule(submitRule);
	// event.setEventName("onclick");
	// event.setMethod("ctrlMenuClickHandler");
	// event.setControllerClazz(this.getClass().getName());
	// item.addEventConf(event);
	// }
	// }
	// private void genCtrlToolItemEvent(ToolBarItem item, boolean hasEvent) {
	// if (hasEvent) {
	// LuiEventConf event = new LuiEventConf();
	// event.setEventType(MouseEvent.class.getSimpleName());
	// event.setOnserver(true);
	// EventSubmitRule submitRule = new EventSubmitRule();
	// event.setSubmitRule(submitRule);
	// event.setEventName("onclick");
	// event.setMethod("addComp");
	// event.setControllerClazz(this.getClass().getName());
	// item.addEventConf(event);
	// }
	// }
	// private void genCtrlTreeCxtMenu(MenuItem menuItem, Map<String, Object[]>
	// map, String pid) {
	// for (String key : map.keySet()) {
	// Object[] treeNodeMap = map.get(key);
	// String _id = (String) key;
	// String _pid = (String) treeNodeMap[6];
	// if (_pid.equals(pid)) {
	// MenuItem _menuItem = new MenuItem();
	// _menuItem.setId(key);
	// _menuItem.setText((String) treeNodeMap[1]);
	// String imgIcon = null;
	// String img = (String) treeNodeMap[7];
	// if (StringUtils.isNotBlank(img)) {
	// imgIcon = img;
	// } else {
	// imgIcon = LuiRuntimeContext.getWebContext().getPageMeta().getThemePath()
	// + "/comps/tree/images/icon/" + (String) treeNodeMap[0] + ".png";
	// }
	// _menuItem.setImgIcon(imgIcon);
	// genCtrlCtxMenuEvent(_menuItem, treeNodeMap[2] != null);
	// genCtrlTreeCxtMenu(_menuItem, map, _id);
	// menuItem.addMenuItem(_menuItem);
	// }
	// }
	// }
	//
	// private void genCtrlCtxMenuEvent(MenuItem menuItem, boolean hasEvent) {
	// if (hasEvent) {
	// LuiEventConf event = new LuiEventConf();
	// event.setEventType(MouseEvent.class.getSimpleName());
	// event.setOnserver(true);
	// EventSubmitRule submitRule = new EventSubmitRule();
	// event.setSubmitRule(submitRule);
	// event.setEventName("onclick");
	// event.setMethod("ctrlMenuClickHandler");
	// event.setControllerClazz(this.getClass().getName());
	// menuItem.addEventConf(event);
	// }
	// }

	public void ctrlMenuClickHandler(MouseEvent<MenuItem> e) {
		String menuItemId = e.getSource().getId();
		dealaddComp(menuItemId);
	}

	private void dealaddComp(String menuItemId) {
		Dataset ds = LuiRuntimeContext.getWebContext().getPageMeta().getWidget("data").getViewModels().getDataset("ctrlds");
		LifeCyclePhase phase = RequestLifeCycleContext.get().getPhase();
		RequestLifeCycleContext.get().setPhase(LifeCyclePhase.design);
		IPaEditorService ipaService = new PaEditorServiceImpl();
		String sessionId = (String) PaCache.getInstance().get("eclipse_sesionId");
		String pageId = (String) PaCache.getInstance().get("_pageId");
		String viewId = (String) PaCache.getInstance().get("_viewId");
		PagePartMeta pagemeta = ipaService.getOriPageMeta(pageId, sessionId);
		if (pagemeta == null)
			return;
		ViewPartMeta widget = pagemeta.getWidget(viewId);
		if (widget == null) {
			throw new LuiRuntimeException("该窗口无法创建控件");
		}
		PaPalletDsListener pds = new PaPalletDsListener();
		if (StringUtils.equals(menuItemId, "ContextMenuComp")) {
			pds.newContextMenu();
		} else {
			pds.newCtrl(menuItemId, ds, pagemeta, widget);
		}
		RequestLifeCycleContext.get().setPhase(phase);
	}

	// 工具栏的控件
	public void addComp(MouseEvent<ToolBarItem> e) {
		String menuItemId = e.getSource().getId();
		if (menuItemId.endsWith("_ctrl")) {
			menuItemId = menuItemId.split("_")[0];
			dealaddComp(menuItemId);
		}
	}

	//
	public void addStringText(MouseEvent<ToolBarItem> e) {
		String menuItemId = "StringTextComp";
		dealaddComp(menuItemId);
	}

	// app参数
	public void addParam(MouseEvent<MenuItem> e) {
		Application app = (Application) PaCache.getInstance().get("_editApp");
		PagePartMeta pagePart = PaCache.getEditorPagePartMeta();
		if (app == null && pagePart == null)
			return;
		LuiAppUtil.getCntWindowCtx().popView("param", "300", "300", "app参数");
	}

	// 管道
	public void addPipeIn(MouseEvent<MenuItem> e) {
		if (allNull())
			return;
		LuiAppUtil.addAppAttr("pipein_status", "add");
		LuiAppUtil.getCntWindowCtx().popView("pipein", "400", "300", "添加输入管道");
	}

	public void addPipeOut(MouseEvent<MenuItem> e) {
		if (allNull())
			return;
		LuiAppUtil.addAppAttr("pipeout_status", "add");
		LuiAppUtil.getCntWindowCtx().popView("pipeout", "600", "600", "添加输出管道");
	}

	public void addConn(MouseEvent<MenuItem> e) {
		if (allNull())
			return;
		LuiAppUtil.addAppAttr("conn_status", "add");
		LuiAppUtil.getCntWindowCtx().popView("connector", "500", "400", "添加连接器");
	}

	// 状态
	public void statesCtrl(MouseEvent<MenuItem> e) {
		PagePartMeta pagemeta = PaCache.getEditorPagePartMeta();
		if (pagemeta == null)
			return;
		LuiAppUtil.getCntWindowCtx().popView("uistate", "300", "260", "状态控制");
	}

	// 参照
	public void addRef(MouseEvent<MenuItem> e) {
		initParam();
		if (this.sourceView == null)
			return;
		new PaModelOperateController().pubAddRef(this.sourceWin.getId(), this.sourceView.getId());
	}

	// 元数据集
	public void addYuanDs(MouseEvent<MenuItem> e) {
		initParam();
		if (this.sourceView == null)
			return;
		new PaModelOperateController().addds(this.sourceWin.getId(), this.sourceView.getId());
	}

	// 元数据集
	public void addPtDs(MouseEvent<MenuItem> e) {
		initParam();
		if (this.sourceView == null)
			return;
		new PaModelOperateController().addNormalDs(this.sourceWin.getId(), this.sourceView.getId());
	}

	// 枚举
	public void addCombo(MouseEvent<MenuItem> e) {
		initParam();
		if (this.sourceView == null)
			return;
		new PaModelOperateController().pubAddCombo(this.sourceWin.getId(), this.sourceView.getId());
	}

	// 图表数据集
	public void addChartDs(MouseEvent<MenuItem> e) {

	}

	// 导出
	public void exportPrj(MouseEvent<MenuItem> e) {
		StringBuilder sb = new StringBuilder();
		String prjRoot = (String) PaCache.getInstance().get("_prjRoot");
		prjRoot = JsURLEncoder.encode(prjRoot, "UTF-8");
		String url = LuiRuntimeContext.getRootPath() + "/pt/file/pack?prjRoot=" + prjRoot;
		sb.append("$.pageutils.sysDownloadFile('").append(url).append("')");
		AppSession.current().getAppContext().addExecScript(sb.toString());
	}

	private void initParam() {
		IPaEditorService pes = new PaEditorServiceImpl();
		PaCache cache = PaCache.getInstance();
		String sessionId = (String) cache.get("eclipse_sesionId");
		String viewId = (String) cache.get("_viewId");
		String pageId = (String) cache.get("_pageId");
		ModePhase modePhase = LuiRuntimeContext.getModePhase();
		PagePartMeta pagemeta = null;
		ViewPartMeta widget = null;
		if (modePhase == null) {
			throw new LuiRuntimeException("设计器没有设置正确的状态!");
		}
		if (modePhase.equals(ModePhase.nodedef) || modePhase.equals(ModePhase.persona)) {
			String sourceWinId = PaHelper.getCurrentEditWindowId();
			pagemeta = pes.getOriPageMeta(sourceWinId, sessionId);
			if (pagemeta == null)
				return;
			widget = pagemeta.getWidget(viewId);
		} else {
			pagemeta = pes.getOriPageMeta(pageId, sessionId);
			if (pagemeta == null)
				return;
			widget = pagemeta.getWidget(viewId);
		}
		this.sourceWin = pagemeta;
		this.sourceView = widget;
	}

	private boolean allNull() {
		PagePartMeta pagePart = PaCache.getEditorPagePartMeta();
		ViewPartMeta viewPartMeta = new PaSettingDsListener().getViewPartMeta(pagePart);
		if (viewPartMeta == null && pagePart == null)
			return true;
		return false;
	}

	private void removeAppCache() {
		PaCache cache = PaCache.getInstance();
		cache.remove("_appId");
		cache.remove("_editApp");
	}
}
