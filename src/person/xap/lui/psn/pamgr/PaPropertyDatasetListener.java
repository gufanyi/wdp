package xap.lui.psn.pamgr;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.eclipse.persistence.jpa.jpql.parser.DateTime;

import xap.lui.core.cache.PaCache;
import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.comps.ButtonComp;
import xap.lui.core.comps.ChartBaseComp;
import xap.lui.core.comps.CheckBoxComp;
import xap.lui.core.comps.CheckboxGroupComp;
import xap.lui.core.comps.ComboBoxComp;
import xap.lui.core.comps.ContextMenuComp;
import xap.lui.core.comps.DateTextComp;
import xap.lui.core.comps.DecimalTextComp;
import xap.lui.core.comps.FormComp;
import xap.lui.core.comps.FormElement;
import xap.lui.core.comps.GridColumn;
import xap.lui.core.comps.GridComp;
import xap.lui.core.comps.IFrameComp;
import xap.lui.core.comps.IntegerTextComp;
import xap.lui.core.comps.LabelComp;
import xap.lui.core.comps.LinkComp;
import xap.lui.core.comps.LuiElement;
import xap.lui.core.comps.MenuItem;
import xap.lui.core.comps.MenubarComp;
import xap.lui.core.comps.PwdTextComp;
import xap.lui.core.comps.RadioComp;
import xap.lui.core.comps.RadioGroupComp;
import xap.lui.core.comps.ReferenceComp;
import xap.lui.core.comps.StringTextComp;
import xap.lui.core.comps.TextAreaComp;
import xap.lui.core.comps.ToolBarComp;
import xap.lui.core.comps.ToolBarItem;
import xap.lui.core.comps.TreeLevel;
import xap.lui.core.comps.TreeViewComp;
import xap.lui.core.comps.WebPartComp;
import xap.lui.core.constant.PaConstant;
import xap.lui.core.constant.UIElementTreeBuilder;
import xap.lui.core.dataset.ComboData;
import xap.lui.core.dataset.DataItem;
import xap.lui.core.dataset.DataList;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.Field;
import xap.lui.core.dataset.PageData;
import xap.lui.core.dataset.Row;
import xap.lui.core.echar.FieldMeta;
import xap.lui.core.echar.Option;
import xap.lui.core.event.ContextMenuEvent;
import xap.lui.core.event.DatasetEvent;
import xap.lui.core.event.DialogEvent;
import xap.lui.core.event.FocusEvent;
import xap.lui.core.event.GridCellEvent;
import xap.lui.core.event.GridEvent;
import xap.lui.core.event.GridRowEvent;
import xap.lui.core.event.IEventSupport;
import xap.lui.core.event.KeyEvent;
import xap.lui.core.event.LinkEvent;
import xap.lui.core.event.MouseEvent;
import xap.lui.core.event.PageEvent;
import xap.lui.core.event.ScriptEvent;
import xap.lui.core.event.TextEvent;
import xap.lui.core.event.TreeCtxMenuEvent;
import xap.lui.core.event.TreeNodeDragEvent;
import xap.lui.core.event.TreeNodeEvent;
import xap.lui.core.event.TreeRowEvent;
import xap.lui.core.event.ViewPartEvent;
import xap.lui.core.layout.UICardLayout;
import xap.lui.core.layout.UIChartComp;
import xap.lui.core.layout.UIElement;
import xap.lui.core.layout.UIElementFinder;
import xap.lui.core.layout.UIFormElement;
import xap.lui.core.layout.UIGridLayout;
import xap.lui.core.layout.UIGridPanel;
import xap.lui.core.layout.UIGridRowLayout;
import xap.lui.core.layout.UIGridRowPanel;
import xap.lui.core.layout.UILayout;
import xap.lui.core.layout.UILayoutPanel;
import xap.lui.core.layout.UIPartMeta;
import xap.lui.core.listener.LuiEventConf;
import xap.lui.core.logger.LuiLogger;
import xap.lui.core.model.AppSession;
import xap.lui.core.model.Application;
import xap.lui.core.model.Connector;
import xap.lui.core.model.DataModels;
import xap.lui.core.model.LuiAppUtil;
import xap.lui.core.model.LuiPageContext;
import xap.lui.core.model.PagePartMeta;
import xap.lui.core.model.PipeIn;
import xap.lui.core.model.PipeInItem;
import xap.lui.core.model.PipeOut;
import xap.lui.core.model.PipeOutItem;
import xap.lui.core.model.TriggerItem;
import xap.lui.core.model.UIState;
import xap.lui.core.model.ViewPartComps;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.core.refrence.BaseRefNode;
import xap.lui.core.refrence.SelfDefRefNode;
import xap.lui.core.render.RaParameter;
import xap.lui.core.services.IPaEditorService;
import xap.lui.core.services.PaEditorServiceImpl;
import xap.lui.psn.command.PaHelper;
import xap.lui.psn.context.BaseInfo;
import xap.lui.psn.context.ComboPropertyInfo;
import xap.lui.psn.context.IPropertyInfo;
import xap.lui.psn.context.InfoCategory;
import xap.lui.psn.context.SelfDefRefPropertyInfo;
import xap.lui.psn.setting.MethodCacheInfo;
import xap.mw.coreitf.d.FBoolean;

public class PaPropertyDatasetListener {
	public static final String FIELD_CHILDID = "childid";

	/**
	 * 前台脚本监听主控制类
	 * 
	 * @param ScriptEvent
	 *            event
	 */
	public void handlerEvent(ScriptEvent event) {

		// 获取从前台传过来的数据属性
		AppSession ctx = AppSession.current();
		String oper = replaceNullString(ctx.getParameter(RaParameter.PARAM_OPER));

		// 设置所有FormElement为不可见
		setFormElementInvisible();
		if (!oper.equals(RaParameter.INIT)) {
			//ctx.getAppContext().addExecScript("setEditorState();");
		}
		if (oper.equals(RaParameter.DELETE)) {
			deleteHandler();
		} else if (oper.equals(RaParameter.ADD)) {
			addHandler();
		} else if (oper.equals(RaParameter.INIT)) {

			pipeInSetting();// 输入管道
			pipeOutSetting();// 输出管道
			connectorSetting();// 连接器
			paramSetting();// 参数
			// 判断是选中的控件是否获得焦点
			boolean mark = focusSelectDs();
			if (!mark)
				return;
			setFormElementVisible();// 属性
			setEventGridElementVisible();// 事件
		}
	}

	// 设置所有FormElement为不可见
	public void setFormElementInvisible() {
		ViewPartMeta widget = LuiRuntimeContext.getWebContext().getPageMeta().getWidget("settings");
		FormComp form = (FormComp) widget.getViewComponents().getComponent("adhintform");
		if (form != null) {
			for (FormElement fc : form.getElementList()) {
				fc.setVisible(false);
			}
		}
	}

	public void setFormElementVisible() {
		AppSession ctx = AppSession.current();
		String type = replaceNullString(ctx.getParameter(RaParameter.PARAM_TYPE));
		BaseInfo pbi = InfoCategory.getInfo(type);
		if (pbi == null) {
			return;
		}
		IPropertyInfo[] ipi = pbi.getPropertyInfos();

		ViewPartMeta widget = LuiRuntimeContext.getWebContext().getPageMeta().getWidget("settings");
		setFormElementByProperty(widget, ipi, type);
	}

	public void setEventGridElementVisible() {
		ViewPartMeta widget = LuiRuntimeContext.getWebContext().getPageMeta().getWidget("settings");
		setGridColumnByProperty(widget);
	}

	/**
	 * 选中控件数据集获取焦点
	 * 
	 * @return boolean
	 */
	public boolean focusSelectDs() {
		AppSession ctx = AppSession.current();
		String eleId = replaceNullString(ctx.getParameter(RaParameter.PARAM_ELEID));
		String uiId = replaceNullString(ctx.getParameter(RaParameter.PARAM_UIID));
		String subuiId = replaceNullString(ctx.getParameter(RaParameter.PARAM_SUBUIID));
		String subEleId = replaceNullString(ctx.getParameter(RaParameter.PARAM_SUBELEID));
		String widgetId = replaceNullString(ctx.getParameter(RaParameter.PARAM_WIDGETID));
		String colIndex = replaceNullString(ctx.getParameter(RaParameter.PARAM_COLINDEX));
		String rowIndex = replaceNullString(ctx.getParameter(RaParameter.PARAM_ROWINDEX));
		PagePartMeta pagemeta = PaCache.getEditorPagePartMeta();
		Dataset dsMiddle = LuiRuntimeContext.getWebContext().getPageMeta().getWidget("settings").getViewModels().getDataset("ds_middle");
		String type = replaceNullString(ctx.getParameter(RaParameter.PARAM_TYPE));
		if (eleId == null && uiId == null && subEleId == null && subuiId == null)
			return false;
		String pid = (eleId != null) ? eleId : uiId;
		String cid = (subuiId == null) ? subEleId : subuiId;
		boolean find = false;
		setNavTreeDs(pid, cid, null, null);
		dsMiddle.clear();
		//new SuperVO2DatasetSerializer().serialize(null, dsMiddle);
		find = false;
		if (!find) {
			UIPartMeta uimeta = PaCache.getEditorUIPartMeta();
			UIElement uiEle = null;
			LuiElement webEle = null;
			// 表格数据
			if (rowIndex != null) {
				uiEle = LuiFinder.getGridElement(uimeta, uiId, rowIndex, colIndex);
			} else {
				if (subuiId != null) {
					uiEle = UIElementFinder.findElementById(uimeta, uiId, subuiId);
				} else {
					uiEle = UIElementFinder.findElementById(uimeta, uiId);
				}
				if (eleId != null) {
					if (subEleId != null)
						webEle = LuiFinder.getWebElement(pagemeta, widgetId, eleId, subEleId);
					else
						webEle = LuiFinder.getWebElement(pagemeta, widgetId, eleId);
				}
			}
			if(webEle == null && uiEle == null) return false;
			if (uiEle instanceof UIChartComp) {
				handlerChartOption(webEle);
			} else {
				UIPartMeta settingUIPart = LuiAppUtil.getViewCtx("settings").getUIMeta();
				UICardLayout cardLayOut = (UICardLayout) UIElementFinder.findElementById(settingUIPart, "cardlayout7773");
				cardLayOut.setCurrentItem("0");
				doAdd(uiEle, webEle, pid, cid, widgetId, uimeta, type, false);
			}
		}
		if (dsMiddle.getCurrentPageRowCount() != 0) {
			dsMiddle.setSelectedIndex(0);
		}
		return true;
	}

	private void handlerChartOption(LuiElement webEle) {
		UIPartMeta settingUIPart = LuiAppUtil.getViewCtx("settings").getUIMeta();
		ViewPartComps viewPartComps = LuiAppUtil.getView("settings").getViewComponents();
		DataModels dataModels = LuiAppUtil.getView("settings").getViewModels();
		GridComp gridComp = (GridComp) viewPartComps.getComponent("grid_chart_option");
		UICardLayout cardLayOut = (UICardLayout) UIElementFinder.findElementById(settingUIPart, "cardlayout7773");
		cardLayOut.setCurrentItem("1");
		ViewPartMeta viewPartMeta = PaCache.getEditorViewPartMeta();
		ChartBaseComp chartComp = (ChartBaseComp) viewPartMeta.getViewComponents().getComponent(webEle.getId());
		Option option = chartComp.getOption();
		String datasetId = gridComp.getDataset();
		Dataset gridDataset = dataModels.getDataset(datasetId);
		gridDataset.clear();
		// 每次需要清除所有的缓存方法
		PaCache.getInstance().clearCacheMethod();
		Option chartOption = this.getChartOption();
		java.lang.reflect.Field[] fields = option.getClass().getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			this.addDatasetRow(chartOption, gridDataset, null, fields[i]);
		}
	}

	private Option option = null;

	public Option getChartOption() {
		if (option == null) {
			AppSession ctx = AppSession.current();
			String eleId = replaceNullString(ctx.getParameter(RaParameter.PARAM_ELEID));
			ViewPartMeta viewPartMeta = PaCache.getEditorViewPartMeta();
			ChartBaseComp chartComp = (ChartBaseComp) viewPartMeta.getViewComponents().getComponent(eleId);
			//String prop = chartComp.getProp();
			option = chartComp.getOption();
		}
		return option;

	}

	private int count = 0;

	public void addDatasetRow(Object instance, Dataset ds, String pid, java.lang.reflect.Field field) {
		String Id = field.getName() + "_" + (count++);
		if(Id.equalsIgnoreCase("text_75")){
			System.out.println(11111);
		}
		AppSession ctx = AppSession.current();
		FieldMeta fieldMeta = field.getAnnotation(FieldMeta.class);
		if (fieldMeta == null) {
			return;
		}
		try {
			Method method = this.getWriteMethod(instance, field);
			MethodCacheInfo cacheInfo = new MethodCacheInfo(instance, method);
			PaCache.getInstance().putCacheMethod(Id, cacheInfo);
		} catch (Throwable e) {
			LuiLogger.error(e.getMessage(), e);
		}
		String eleId = replaceNullString(ctx.getParameter(RaParameter.PARAM_ELEID));
		String uiId = replaceNullString(ctx.getParameter(RaParameter.PARAM_UIID));
		String subuiId = replaceNullString(ctx.getParameter(RaParameter.PARAM_SUBUIID));
		String subEleId = replaceNullString(ctx.getParameter(RaParameter.PARAM_SUBELEID));
		String widgetId = replaceNullString(ctx.getParameter(RaParameter.PARAM_WIDGETID));
		String colIndex = replaceNullString(ctx.getParameter(RaParameter.PARAM_COLINDEX));
		String rowIndex = replaceNullString(ctx.getParameter(RaParameter.PARAM_ROWINDEX));
		String type = replaceNullString(ctx.getParameter(RaParameter.PARAM_TYPE));
		Row row = ds.getEmptyRow();
		Class clazz = field.getType();
		row.setValue(ds.nameToIndex("Id"), Id);
		row.setValue(ds.nameToIndex("Name"), fieldMeta.desc());
		row.setValue(ds.nameToIndex("eleId"), eleId);
		row.setValue(ds.nameToIndex("uiId"), uiId);
		row.setValue(ds.nameToIndex("subuiId"), subuiId);
		row.setValue(ds.nameToIndex("subEleId"), subEleId);
		row.setValue(ds.nameToIndex("widgetId"), widgetId);
		row.setValue(ds.nameToIndex("colIndex"), colIndex);
		row.setValue(ds.nameToIndex("rowIndex"), rowIndex);
		row.setValue(ds.nameToIndex("type"), type);
		row.setValue(ds.nameToIndex("Pid"), pid);
		ds.addRow(row);
		if (!this.isBaseDataType(clazz)) {
			Object innerInstance = null;
			try {
				Method method = this.getReadMethod(instance, field);
				Object[] args = null;
			    innerInstance = method.invoke(instance, args);
			} catch (Throwable e) {
				LuiLogger.error(e.getMessage());
			}
			java.lang.reflect.Field[] fields = clazz.getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				//Method innerMethod = this.getReadMethod(instance, fields[i]);
				try {
					this.addDatasetRow(innerInstance, ds, Id, fields[i]);
				} catch (Throwable e) {
					LuiLogger.error(e.getMessage());
				}

			}
		}
	}

	public Method getReadMethod(Object instance, java.lang.reflect.Field field) {
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(instance.getClass());
			PropertyDescriptor[] propDesc = beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor inner : propDesc) {
				String name = inner.getName();
				if (field.getName().equalsIgnoreCase(name)) {
					return inner.getReadMethod();
				}
			}

		} catch (Throwable e) {
			LuiLogger.error(e.getMessage());
		}
		return null;
	}

	public Method getWriteMethod(Object instance, java.lang.reflect.Field field) {
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(instance.getClass());
			PropertyDescriptor[] propDesc = beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor inner : propDesc) {
				String name = inner.getName();
				if (field.getName().equalsIgnoreCase(name)) {
					return inner.getWriteMethod();
				}
			}
		} catch (Throwable e) {
			LuiLogger.error(e.getMessage());
		}
		return null;
	}

	private boolean isBaseDataType(Class clazz) {
		return (clazz.equals(String.class) || clazz.equals(Integer.class) || clazz.equals(Byte.class) || clazz.equals(Long.class) || clazz.equals(Double.class) || clazz.equals(Float.class) || clazz.equals(Character.class) || clazz.equals(Short.class) || clazz.equals(BigDecimal.class) || clazz.equals(BigInteger.class) || clazz.equals(Boolean.class) || clazz.equals(Date.class) || clazz.equals(DateTime.class) || clazz.isPrimitive());
	}

	private void doAdd(UIElement uiEle, LuiElement webEle, String uiId, String subuiId, String widgetId, UIPartMeta uimeta, String type, boolean isRefreshDS) {
		if (webEle != null) {
			String itemId = uiId;
			String pid = null;
			if (subuiId != null) {
				pid = uiId;
				itemId += "." + subuiId;
				setNavTreeDs(pid, subuiId, "add", webEle.getClass().getName());
			} else {
				pid = getParentId(uiEle, uimeta);
				setNavTreeDs(pid, itemId, "add", webEle.getClass().getName());
			}
			setDsInfo(widgetId, type, webEle, uiEle, PaConstant.DS_UPDATE, itemId, pid);
			if (isRefreshDS) {
				setCtrlCompDs(type, itemId);
			}
		} else {
			String itemId = uiId;
			if (subuiId != null)
				itemId += "." + subuiId;
			UIElement parentEle = UIElementFinder.findParent(uimeta, uiEle);
			String prtId = null;
			if (parentEle != null) {
				if (parentEle instanceof UILayoutPanel) {
					if (((UILayoutPanel) parentEle).getLayout() == null) {
						prtId = parentEle.getId();
					} else
						prtId = ((UILayoutPanel) parentEle).getLayout().getId() + "." + parentEle.getId();
				} else
					prtId = parentEle.getId();
			}
			if (uiEle == null) {
				return;
			}
			// throw new LuiRuntimeException("没有取到对应 元素");
			type = getTypeByEle(uiEle);
			setDsInfo(widgetId, type, null, uiEle, PaConstant.DS_UPDATE, itemId, prtId);
			if (parentEle == null)
				setNavTreeDs(null, uiId, "add", uiEle.getClass().getName());
			else
				setNavTreeDs(parentEle.getId(), uiEle.getId(), "add", uiEle.getClass().getName());
			if (uiEle instanceof UILayout) {
				if (uiEle instanceof UIGridLayout) {
					setGridLayoutInfo(uiEle, widgetId);
				} else {
					Iterator<UILayoutPanel> it = ((UILayout) uiEle).getPanelList().iterator();
					while (it.hasNext()) {
						UILayoutPanel panel = it.next();
						String cItemId = itemId + "." + panel.getId();
						type = getTypeByEle(panel);
						setDsInfo(widgetId, type, null, panel, PaConstant.DS_UPDATE, cItemId, itemId);
						setNavTreeDs(itemId, panel.getId(), "add", panel.getClass().getName());
					}
				}
			}
			if (isRefreshDS) {
				setLayoutDs(type, itemId);
			}
		}
	}

	private void setGridLayoutInfo(UIElement uiEle, String widgetId) {
		String type;
		String itemId;
		UIGridLayout uiGrid = (UIGridLayout) uiEle;
		int rowCount = uiGrid.getPanelList().size();
		List<UILayoutPanel> rowPanels = uiGrid.getPanelList();
		for (int p = 0; p < rowCount; p++) {
			UIGridRowPanel rowPanel = (UIGridRowPanel) rowPanels.get(p);
			itemId = uiGrid.getId();
			String cItemId = itemId + "." + rowPanel.getId();
			type = getTypeByEle(rowPanel);
			setDsInfo(widgetId, type, null, rowPanel, PaConstant.DS_UPDATE, cItemId, itemId);
			setNavTreeDs(itemId, rowPanel.getId(), "add", rowPanel.getClass().getName());
			UIGridRowLayout gridRow = rowPanel.getRow();
			itemId = rowPanel.getId();
			cItemId = itemId + "." + gridRow.getId();
			type = getTypeByEle(gridRow);
			setDsInfo(widgetId, type, null, gridRow, PaConstant.DS_UPDATE, cItemId, itemId);
			setNavTreeDs(itemId, gridRow.getId(), "add", gridRow.getClass().getName());
			List<UILayoutPanel> gridPanels = gridRow.getPanelList();
			int colCount = gridPanels.size();
			for (int q = 0; q < colCount; q++) {
				UIGridPanel gridPanel = (UIGridPanel) gridPanels.get(q);
				itemId = gridRow.getId();
				cItemId = itemId + "." + gridPanel.getId();
				type = getTypeByEle(gridPanel);
				setDsInfo(widgetId, type, null, gridPanel, PaConstant.DS_UPDATE, cItemId, itemId);
				setNavTreeDs(itemId, gridPanel.getId(), "add", gridPanel.getClass().getName());
			}
		}
	}

	private void setCtrlCompDs(String type, String itemId) {
		ViewPartMeta widget = LuiRuntimeContext.getWebContext().getPageMeta().getWidget(PaConstant.VIEW_DATA);
		Dataset currDs = widget.getViewModels().getDataset("ctrlds");
		// 添加新建控件，ds中新增行
		Row row = currDs.getEmptyRow();
		row.setValue(currDs.nameToIndex("id"), itemId);
		row.setValue(currDs.nameToIndex("name"), PaConstantMap.labelNameMap.get(type));
		row.setValue(currDs.nameToIndex("type"), type);
		row.setValue(currDs.nameToIndex("pid"), " ");
		currDs.addRow(row);
		// currDs.setRowSelectIndex(-1);
		currDs.setRowUnSelect();
		currDs.setFocusIndex(-1);
		// AppLifeCycleContext.current().getApplicationContext().addExecScript("reloadEntityds('ctrlds');");
	}

	private void setLayoutDs(String type, String itemId) {
		ViewPartMeta widget = LuiRuntimeContext.getWebContext().getPageMeta().getWidget(PaConstant.VIEW_DATA);
		Dataset currDs = widget.getViewModels().getDataset("layoutds");
		// currDs.setRowSelectIndex(-1);
		// currDs.setFocusIndex(-1);
	}

	@SuppressWarnings("restriction")
	private void addHandler() {
		PagePartMeta pagemeta = PaCache.getEditorPagePartMeta();
		UIPartMeta uimeta = PaCache.getEditorUIPartMeta();
		AppSession ctx = AppSession.current();
		String widgetId = replaceNullString(ctx.getParameter(RaParameter.PARAM_WIDGETID));
		String eleId = replaceNullString(ctx.getParameter(RaParameter.PARAM_ELEID));
		String uiId = replaceNullString(ctx.getParameter(RaParameter.PARAM_UIID));
		String subeleId = replaceNullString(ctx.getParameter(RaParameter.PARAM_SUBELEID));
		String subuiId = replaceNullString(ctx.getParameter(RaParameter.PARAM_SUBUIID));
		String type = replaceNullString(ctx.getParameter(RaParameter.PARAM_TYPE));
		UIElement uiEle = null;
		LuiElement webEle = null;
		if (subuiId != null) {
			uiEle = UIElementFinder.findElementById(uimeta, uiId, subuiId);
		} else {
			uiEle = UIElementFinder.findElementById(uimeta, uiId);
		}
		if (eleId != null) {
			if (subeleId != null) {
				webEle = UIElementFinder.findWebElementById(pagemeta, widgetId, eleId, subeleId);
			} else {
				webEle = LuiFinder.getWebElement(pagemeta, widgetId, eleId);
			}
		}
		doAdd(uiEle, webEle, uiId, subuiId, widgetId, uimeta, type, true);
	}

	/**
	 * 删除操作的处理类
	 */
	private void deleteHandler() {
		AppSession ctx = AppSession.current();
		String uiId = replaceNullString(ctx.getParameter(RaParameter.PARAM_UIID));
		String subuiId = replaceNullString(ctx.getParameter(RaParameter.PARAM_SUBUIID));
		IPaEditorService ipaService = new PaEditorServiceImpl();
		String pageId = (String) PaCache.getInstance().get("_pageId");
		String sessionId = (String) PaCache.getInstance().get("eclipse_sesionId");
		UIPartMeta uimeta = ipaService.getOriUIMeta(pageId, sessionId);
		ViewPartMeta widget = LuiRuntimeContext.getWebContext().getPageMeta().getWidget("settings");
		Dataset ds = widget.getViewModels().getDataset("ds_middle");
		// LuiWidget navWidget =
		// LuiRuntimeEnvironment.getWebContext().getPageMeta().getWidget(PaConstant.VIEW_NAV);
		// Dataset navDs = navWidget.getViewModels().getDataset("ds_struct");

		// ViewPartMeta palletWidget =
		// LuiRuntimeContext.getWebContext().getPageMeta().getWidget(PaConstant.VIEW_DATA);
		// Dataset ctrlDs = palletWidget.getViewModels().getDataset("ctrlds");
		String itemId = uiId;
		if (subuiId != null && !subuiId.equals("")) {
			itemId += "." + subuiId;
			// removeNavRow(subuiId, navDs);
			/**
			 * 删除已定义的控件的行
			 */
			// removeNavRow(subuiId, ctrlDs);
		} else {
			UIElement uiEle = UIElementFinder.findElementById(uimeta, uiId);
			if (uiEle instanceof UILayout) {
				Iterator<UILayoutPanel> it = ((UILayout) uiEle).getPanelList().iterator();
				while (it.hasNext()) {
					UILayoutPanel panel = it.next();
					String cItemId = itemId + "." + panel.getId();
					removeRow(cItemId, ds);
					// removeNavRow(panel.getId(), navDs);
					/**
					 * 删除已定义的控件的行
					 */
					if (panel.getElement() != null) {
						// removeNavRow(panel.getElement().getId(), ctrlDs);
					}
					// removeNavRow(panel.getId(), ctrlDs);
				}
			}
		}
		removeRow(itemId, ds);
		setNavTreeDs(uiId, subuiId, "delete", null);
		AppSession.current().getAppContext().addExecScript("reloadEntityds('ctrlds');");
	}

	private void removeNavRow(String itemId, Dataset navDs) {
		PageData rd = navDs.getCurrentPageData();
		if (rd == null)
			return;
		Row[] rows = rd.getRows();
		if (rows == null || rows.length == 0)
			return;
		for (int i = 0; i < rows.length; i++) {
			Row row = rows[i];
			int idIndex = navDs.nameToIndex("id");
			String value = (String) row.getValue(idIndex);
			if (value != null && value.equals(itemId)) {
				navDs.removeRow(row);
			}
		}
	}

	private void removeRow(String itemId, Dataset ds) {
		PageData rd = ds.getCurrentPageData();
		if (rd == null) {
			return;
		}
		Row[] rows = rd.getRows();
		for (int i = 0; i < rows.length; i++) {
			Row row = rows[i];
			int idIndex = ds.nameToIndex(FIELD_CHILDID);
			String value = (String) row.getValue(idIndex);
			if (itemId != null && itemId.equals(value)) {
				ds.removeRow(row);
			}
			ds.setRowSelectIndex(-1);
		}
	}

	private String getParentId(UIElement uiEle, UIPartMeta uimeta) {
		String prtId = null;
		UIElement parentEle = UIElementFinder.findParent(uimeta, uiEle);
		if (parentEle != null) {
			prtId = getElementId(parentEle);
		}
		return prtId;
	}

	public LuiElement getFormElement(PagePartMeta pagemeta, UIFormElement fe) {
		FormComp formc = (FormComp) pagemeta.getWidget(fe.getViewId()).getViewComponents().getComponent(fe.getFormId());
		return formc.getElementById(fe.getId());
	}

	private void setDsInfo(String widgetId, String type, LuiElement ele, UIElement uiEle, String dsState, String itemId, String parentId) {
		ViewPartMeta widget = LuiRuntimeContext.getWebContext().getPageMeta().getWidget("settings");
		Dataset ds = widget.getViewModels().getDataset("ds_middle");
		if (type == null) {
			return;
			// throw new LuiRuntimeException("获取类型为空");
		}
		if (type.equals(LuiPageContext.SOURCE_TYPE_FORMELE)) {
			FormElement formEle = (FormElement) ele;
			if (formEle != null && StringUtils.equals(formEle.getEditorType(), "Reference"))
				type = LuiPageContext.SOURCE_TYPE_FORMELE + "_ref";
		} else if (type.equals(LuiPageContext.SOURCE_TYPE_MENUBAR_MENUITEM)) {
		}
		BaseInfo pbi = InfoCategory.getInfo(type);
		if (pbi != null) {
			IPropertyInfo[] ipi = pbi.getPropertyInfos();
			Row row = ds.getEmptyRow();
			setRowValue(ds, type, uiEle, ele, ipi, row, dsState, widgetId, itemId, parentId);
			ds.addRow(row);
		}
	}

	private void setNavTreeDs(String pId, String id, String oper, String imgtype) {
		// if(true){
		// return;
		// }
		ViewPartMeta navWidget = LuiRuntimeContext.getWebContext().getPageMeta().getWidget(PaConstant.VIEW_NAV);
		WebPartComp navcontent = (WebPartComp) navWidget.getViewComponents().getComponent("navcontent");
		UIPartMeta uimeta = PaHelper.getUIMeta();
		UIElementTreeBuilder builder = new UIElementTreeBuilder();
		List<String> list = builder.buildUITree(uimeta, pId, id);
		StringBuffer sb = new StringBuffer(256);
		sb.append("<div class='nav_panel'>");
		Iterator<String> iterator = list.iterator();
		while (iterator.hasNext()) {
			Object obj = iterator.next();
			if (obj != null) {
				if (iterator.hasNext()) {
					sb.append("<a class='nav_ele' onclick=\"selectStruct('" + obj + "')\">");
					sb.append(obj);
					sb.append("</a>");
					sb.append(" > ");
				} else {
					sb.append("<b style='color: red;line-height:26px;'>").append(obj).append("</b>");
				}
			}
		}
		sb.append("</div>");
		navcontent.setInnerHTML(sb.toString());
	}

	// 根据获取VO和属性信息向对应数据集中设置显示数据
	private void setRowValue(Dataset dss, String type, UIElement uiEle, LuiElement ele, IPropertyInfo[] pi, Row row, String dsState, String widgetId, String itemId, String parentId) {
		// LuiWidget widget =
		// LuiRuntimeEnvironment.getWebContext().getPageMeta().getWidget("settings");
		// FormComp form = (FormComp)
		// widget.getViewComponents().getComponent("adhintform");
		Field[] fss = dss.getFields();
		for (int i = 0; i < fss.length; i++) {
			Field fdd = fss[i];
			String id = fdd.getId();
			/*
			 * FormElement formEle = form.getElementById(id);
			 * if(!formEle.isVisible() || formEle == null) continue;
			 */
			Object value = getElementValue(ele, uiEle, pi, id);
			row.setValue(i, value);
		}
		if (ele == null && uiEle == null)
			return;
		String id = uiEle == null ? ele.getId() : uiEle.getId();
		int typeIndex = dss.nameToIndex("comptype");
		row.setValue(typeIndex, type);
		if (ele instanceof MenuItem) {
			int idIndex = dss.nameToIndex("string_ext1");
			row.setValue(idIndex, ((MenuItem) ele).getId());
		} else {
			int idIndex = dss.nameToIndex("string_ext1");
			row.setValue(idIndex, id);
		}
		int widgetIndex = dss.nameToIndex("string_ext2");
		row.setValue(widgetIndex, widgetId);
		int stateIndex = dss.nameToIndex("ds_state");
		row.setValue(stateIndex, dsState);
		int prtIndex = dss.nameToIndex("parentid");
		row.setValue(prtIndex, parentId);
		int itemIdIndex = dss.nameToIndex(FIELD_CHILDID);
		row.setValue(itemIdIndex, itemId);

		// 给一级树和二级树属性设置显示值
		if (ele instanceof TreeViewComp) {
			if (((TreeViewComp) ele).getTopLevel() != null) {
				TreeLevel topLevel = ((TreeViewComp) ele).getTopLevel();
				String treeLevelId = topLevel.getId();
				if (StringUtils.isNotBlank(treeLevelId))
					row.setValue(dss.nameToIndex("ref_ext1"), treeLevelId);
				TreeLevel childLevel = topLevel.getChildTreeLevel();
				if (childLevel != null){
					String childLevelId = childLevel.getId();
					row.setValue(dss.nameToIndex("ref_ext2"), childLevelId);
				}
			}

		}
	}

	private Object getElementValue(Object ele, UIElement uiEle, IPropertyInfo[] pi, String id) {
		Object newValue = null;
		for (int i = 0; i < pi.length; i++) {
			IPropertyInfo pinfo = pi[i];
			if (pinfo.getDsField().equals(id)) {
				Object value = null;
				if (pinfo.getId() != "") {
					if ((ele instanceof GridColumn || ele instanceof FormElement) && ("validateFormula".equals(pinfo.getId()) || "precision".equals(pinfo.getId()) || "editFormular".equals(pinfo.getId()))) {
						Field field = null;
						if (ele instanceof GridColumn) {
							GridColumn gc = (GridColumn) ele;
							if (gc.getField() != null) {
								ViewPartMeta widget = gc.getGridComp().getWidget();
								Dataset ds = widget.getViewModels().getDataset(gc.getGridComp().getDataset());
								field = ds.getField(gc.getField());
							}
						}
						if (ele instanceof FormElement) {
							FormElement fe = (FormElement) ele;
							if ("validateFormula".equals(pinfo.getId())) {
								value = fe.getValidateFormula();
							} else if ("editFormular".equals(pinfo.getId())) {
								value = fe.getEditFormular();
							} else if ("precision".equals(pinfo.getId())) {
								value = fe.getPrecision();
							}

						}
						if (field != null) {
							if ("validateFormula".equals(pinfo.getId())) {
								value = field.getValidateFormula();
							} else if ("editFormular".equals(pinfo.getId())) {
								value = field.getEditFormular();
							} else if ("precision".equals(pinfo.getId())) {
								value = field.getPrecision();
							}
						}

					} else {
						try {
							value = getValue(ele, pinfo.getId());
						} catch (Exception e) {
							if (uiEle != null) {
								try {
									value = getValue(uiEle, pinfo.getId());
								} catch (Exception e1) {
								}
							} else {
							}
						}
					}
				}
				if (value instanceof Boolean)
					newValue = FBoolean.valueOf((Boolean) value);
				else
					newValue = value;
				if (newValue != null)
					return newValue;
			}
		}
		return newValue;
	}

	// 根据ele获取其ID
	private String getElementId(Object ele) {
		String uiEleId = null;
		if (ele instanceof LuiElement) {
			uiEleId = ((LuiElement) ele).getId();
		}
		if (ele instanceof UIElement) {
			uiEleId = (String) ((UIElement) ele).getId();
		}
		return uiEleId;
	}

	/**
	 * 根据页面元素，返回其类型
	 */
	private String getTypeByEle(Object ele) {
		String temp = ele.getClass().getSimpleName();
		String type = PaConstantMap.web2ui.get(temp);
		return type;
	}

	/**
	 * 设置元素是否可见，根据具体类型设置，对FormElement元素属性进行特殊处理
	 * 
	 * @param widget
	 * @param pi
	 * @param type
	 * @param uiEle
	 */
	private void setFormElementByProperty(ViewPartMeta widget, IPropertyInfo[] pi, String type) {
		FormComp form = (FormComp) widget.getViewComponents().getComponent("adhintform");
		for (int i = 0; i < pi.length; i++) {
			IPropertyInfo pinfo = pi[i];
			boolean editable = pinfo.editable(LuiRuntimeContext.getModePhase());
			boolean visible = pinfo.isVisible(LuiRuntimeContext.getModePhase());
			String filedName = pinfo.getDsField();
			FormElement fe = form.getElementById(filedName);
			ComboData scd = null;
			if (pinfo instanceof ComboPropertyInfo) {
				scd = widget.getViewModels().getComboData(pinfo.getDsField());
				scd.removeAllDataItems();
				String[] keys = ((ComboPropertyInfo) pinfo).getKeys();
				String[] values = ((ComboPropertyInfo) pinfo).getValues();

				for (int j = 0; j < keys.length; j++) {
					DataItem item = new DataItem();
					item.setText(keys[j]);
					item.setValue(values[j]);
					scd.addDataItem(item);
				}
			} else if (pinfo instanceof SelfDefRefPropertyInfo) {
				String widgetid = AppSession.current().getParameter(RaParameter.PARAM_WIDGETID);
				String sourceWin = (String) PaCache.getInstance().get("_pageId");// PaHelper.getCurrentEditWindowId();

				Map<String, String> paramMap = AppSession.current().getParam();
				StringBuffer sb = new StringBuffer();
				Set<String> keyset = paramMap.keySet();
				if (keyset != null && !keyset.isEmpty()) {
					for (String ke : keyset) {
						String val = paramMap.get(ke);
						if (val != null && !("".equals(val))) {
							sb.append("&").append(ke).append("=").append(val);
						}
					}
				}

				String dsId = getEditCompDs();
				if (dsId != null)
					sb.append("&writeDs=").append(dsId);

				SelfDefRefPropertyInfo pinfo2 = (SelfDefRefPropertyInfo) pinfo;
				fe.setRefNode(pinfo2.getDsField());
				BaseRefNode refnode = (BaseRefNode) widget.getViewModels().getRefNode(pinfo2.getDsField());
				if (refnode != null) {
					refnode.setTitle(pinfo.getLabel());
					if (refnode instanceof SelfDefRefNode) {
						sb.append("&formulatype=").append(pinfo2.getDsField());// 编辑公式
						sb.append("&wherefrom=designer");// 编辑公式
						String url = LuiRuntimeContext.getRootPath() + "/" + pinfo2.getUrl() + "&" + LuiRuntimeContext.DESIGNWINID + "=" + sourceWin + "&sourceView=" + widgetid + sb;
						url = url + "&pi=" + UUID.randomUUID().toString();
						((SelfDefRefNode) refnode).setPath(url);
						if (pinfo2.getHeight() != null) {
							((SelfDefRefNode) refnode).setHeight(pinfo2.getHeight());
						}
						if (pinfo2.getWidth() != null) {
							((SelfDefRefNode) refnode).setWidth(pinfo2.getWidth());
						}
					}
				}
			}

			fe.setLabel(pinfo.getLabel());
			// fe.setText(pinfo.getLabel());
			// fe.setEditable(editable);
			fe.setEnabled(editable);
			fe.setVisible(visible);
			if (pinfo.getChangeMonitor() != null) {
				Dataset dsMiddle = LuiRuntimeContext.getWebContext().getPageMeta().getWidget("settings").getViewModels().getDataset("ds_middle");
				Row row = dsMiddle.getSelectedRow();
				Object newValue = row.getValue(dsMiddle.nameToIndex(pinfo.getDsField()));
				pinfo.getChangeMonitor().on(form, pinfo, newValue);
			}
		}
	}

	/**
	 * 设置事件的表格是否可见，根据具体类型设置，对GridColumn元素属性进行特殊处理
	 * 
	 * @param widget
	 * @param pi
	 * @param type
	 * @param uiEle
	 */
	private void setGridColumnByProperty(ViewPartMeta widget) {
		Dataset ds = widget.getViewModels().getDataset("ds_event");
		ds.setEdit(true);
		ds.clear();
		AppSession ctx = AppSession.current();
		String widgetId = replaceNullString(ctx.getParameter(RaParameter.PARAM_WIDGETID));
		String eleId = replaceNullString(ctx.getParameter(RaParameter.PARAM_ELEID));
		String subeleId = replaceNullString(ctx.getParameter(RaParameter.PARAM_SUBELEID));
		PagePartMeta pagemeta = PaCache.getInstance().getEditorPagePartMeta();
		LuiElement webEle = null;
		if (eleId != null) {
			if (subeleId != null) {
				webEle = UIElementFinder.findWebElementById(pagemeta, widgetId, eleId, subeleId);
			} else {
				webEle = LuiFinder.getWebElement(pagemeta, widgetId, eleId);
			}
		}
		List<Class> list = new ArrayList<Class>();
		if (webEle instanceof PagePartMeta) { // PagePartMeta
			list.add(PageEvent.class);
		} else if (webEle instanceof ViewPartMeta) { // ViewPartMeta
			list.add(DialogEvent.class);
			list.add(ViewPartEvent.class);
		} else if (webEle instanceof GridComp) { // 表格
			list.add(GridEvent.class);
			list.add(GridCellEvent.class);
			list.add(GridRowEvent.class);
		} else if (webEle instanceof FormComp) { // 表单
			list.add(TextEvent.class);
		} else if (webEle instanceof FormElement) { // 表单项
			list.add(TextEvent.class);
		} else if (webEle instanceof TreeViewComp) { // 树
			list.add(TreeCtxMenuEvent.class);
			list.add(TreeNodeDragEvent.class);
			list.add(TreeNodeEvent.class);
			list.add(TreeRowEvent.class);
		} else if (webEle instanceof ToolBarComp) {// 工具栏
			list.add(MouseEvent.class);
		} else if (webEle instanceof ToolBarItem) { // ToolBarItem
			list.add(MouseEvent.class);
		} else if (webEle instanceof IFrameComp) { // 浮动框架
			list.add(MouseEvent.class);
		} else if (webEle instanceof LinkComp) { // 链接
			list.add(LinkEvent.class);
		} else if (webEle instanceof ContextMenuComp) { // 右键菜单
			list.add(MouseEvent.class);
			list.add(ContextMenuEvent.class);
		} else if (webEle instanceof MenubarComp) { // 菜单
			list.add(MouseEvent.class);
			list.add(ContextMenuEvent.class);
		} else if (webEle instanceof MenuItem) { // MenuItem
			list.add(MouseEvent.class);
			list.add(ContextMenuEvent.class);
		} else if (webEle instanceof LabelComp) { // 标签
			list.add(MouseEvent.class);
		} else if (webEle instanceof ReferenceComp) { // 参照
			list.add(TextEvent.class);
			list.add(MouseEvent.class);
			list.add(KeyEvent.class);
			list.add(FocusEvent.class);
		} else if (webEle instanceof DateTextComp) { // 日历
			list.add(TextEvent.class);
			list.add(MouseEvent.class);
			list.add(KeyEvent.class);
			list.add(FocusEvent.class);
		} else if (webEle instanceof RadioComp) { // 单选按钮
			list.add(TextEvent.class);
			list.add(MouseEvent.class);
			list.add(KeyEvent.class);
			list.add(FocusEvent.class);
		} else if (webEle instanceof RadioGroupComp) { // 单选按钮组
			list.add(TextEvent.class);
			list.add(MouseEvent.class);
			list.add(KeyEvent.class);
			list.add(FocusEvent.class);
		} else if (webEle instanceof TextAreaComp) { // 文本域
			list.add(TextEvent.class);
			list.add(MouseEvent.class);
			list.add(KeyEvent.class);
			list.add(FocusEvent.class);
		} else if (webEle instanceof PwdTextComp) { // Pwd文本框
			list.add(TextEvent.class);
			list.add(MouseEvent.class);
			list.add(KeyEvent.class);
			list.add(FocusEvent.class);
		} else if (webEle instanceof DecimalTextComp) { // Decimal文本框
			list.add(TextEvent.class);
			list.add(MouseEvent.class);
			list.add(KeyEvent.class);
			list.add(FocusEvent.class);
		} else if (webEle instanceof IntegerTextComp) { // Integer文本框
			list.add(TextEvent.class);
			list.add(MouseEvent.class);
			list.add(KeyEvent.class);
			list.add(FocusEvent.class);
		} else if (webEle instanceof StringTextComp) { // String文本框
			list.add(TextEvent.class);
			list.add(MouseEvent.class);
			list.add(KeyEvent.class);
			list.add(FocusEvent.class);
		} else if (webEle instanceof CheckBoxComp) { // 复选框
			list.add(TextEvent.class);
			list.add(MouseEvent.class);
			list.add(KeyEvent.class);
			list.add(FocusEvent.class);
		} else if (webEle instanceof CheckboxGroupComp) { // 复选框组
			list.add(TextEvent.class);
			list.add(MouseEvent.class);
			list.add(KeyEvent.class);
			list.add(FocusEvent.class);
		} else if (webEle instanceof ComboBoxComp) { // 下拉框
			list.add(TextEvent.class);
			list.add(MouseEvent.class);
			list.add(KeyEvent.class);
			list.add(FocusEvent.class);
		} else if (webEle instanceof ButtonComp) { // 按钮
			list.add(TextEvent.class);
			list.add(MouseEvent.class);
			list.add(KeyEvent.class);
			list.add(FocusEvent.class);
		} else if (webEle instanceof Dataset) { // Dataset
			list.add(DatasetEvent.class);
		} else if (webEle instanceof BaseRefNode) { // RefNode
			list.add(MouseEvent.class);
		} else if (webEle instanceof ComboData) { // ComboData
			list.add(MouseEvent.class);
		}

		fillEvent(ds, list, (IEventSupport) webEle);
	}

	public void addEditorAttr(Dataset ds, Row row) {
		AppSession ctx = AppSession.current();
		String eleId = replaceNullString(ctx.getParameter(RaParameter.PARAM_ELEID));
		String uiId = replaceNullString(ctx.getParameter(RaParameter.PARAM_UIID));
		String subuiId = replaceNullString(ctx.getParameter(RaParameter.PARAM_SUBUIID));
		String subEleId = replaceNullString(ctx.getParameter(RaParameter.PARAM_SUBELEID));
		String widgetId = replaceNullString(ctx.getParameter(RaParameter.PARAM_WIDGETID));
		String colIndex = replaceNullString(ctx.getParameter(RaParameter.PARAM_COLINDEX));
		String rowIndex = replaceNullString(ctx.getParameter(RaParameter.PARAM_ROWINDEX));
		String typeIndex = replaceNullString(ctx.getParameter(RaParameter.PARAM_TYPE));
		row.setValue(ds.nameToIndex("eleId"), eleId);
		row.setValue(ds.nameToIndex("uiId"), uiId);
		row.setValue(ds.nameToIndex("subuiId"), subuiId);
		row.setValue(ds.nameToIndex("subEleId"), subEleId);
		row.setValue(ds.nameToIndex("widgetId"), widgetId);
		row.setValue(ds.nameToIndex("colIndex"), colIndex);
		row.setValue(ds.nameToIndex("rowIndex"), rowIndex);
		row.setValue(ds.nameToIndex("type"), typeIndex);
	}

	private void fillEvent(Dataset ds, List<Class> eventList, IEventSupport editor) {
		String controller = PaCache.getController();
		List<String> list = new ArrayList<String>();
		for (Class innerEvent : eventList) {
			java.lang.reflect.Field[] fields = innerEvent.getFields();

			for (java.lang.reflect.Field inner : fields) {
				int modifiy = inner.getModifiers();
				if (Modifier.isStatic(modifiy)) {// 获取字段的修饰符，public 1,static 8
					String typeName = inner.getType().getName();
					if ("TYPE".equalsIgnoreCase(inner.getName())) {
						continue;
					}
					if (typeName.indexOf("String") != -1) {
						try {
							Object value = inner.get(null);
							list.add(value.toString() + "__" + innerEvent.getSimpleName());
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						}

					}
				}
			}
		}
		java.util.Collections.sort(list);
		// 目标状态DataList设值
		setStateList();
		for (int i = 0; i < list.size(); i++) {
			Row parnetRow = ds.getEmptyRow();
			String eventInfo = list.get(i);
			String eventName = eventInfo.split("__")[0];
			// LuiEventConf eventConf=editor.getEventConf(eventName);
			LuiEventConf eventConf = null;
			if (editor.getEventConfs() != null) {
				for (LuiEventConf etConf : editor.getEventConfs()) {
					if (eventName.equals(etConf.getName())) {
						eventConf = etConf;
					}
				}
			}
			String onserv = null;
			String eventLang = null;

			int idIdex = ds.nameToIndex("Id");
			int nameIndex = ds.nameToIndex("Name");
			int pidIndex = ds.nameToIndex("Pid");
			int valueIndex = ds.nameToIndex("ParamValue");
			{
				parnetRow.setValue(idIdex, eventName);
				parnetRow.setValue(nameIndex, eventName);
				ds.addRow(parnetRow);
				this.addEditorAttr(ds, parnetRow);
			}
			{
				{
					Row row = ds.getEmptyRow();
					row.setValue(idIdex, eventName + "_EventType");
					row.setValue(pidIndex, eventName);
					row.setValue(nameIndex, "事件类型(eventType)");
					row.setValue(valueIndex, eventInfo.split("__")[1]);
					ds.addRow(row);
					this.addEditorAttr(ds, row);
				}
				{
					Row row = ds.getEmptyRow();
					row.setValue(idIdex, eventName + "_Method");
					row.setValue(pidIndex, eventName);
					row.setValue(nameIndex, "方法名(method)");
					if (eventConf != null && StringUtils.isNotBlank(eventConf.getMethod())) {
						row.setValue(valueIndex, eventConf.getMethod());
					} else {
						String _method = editor.getId() + "_" + eventName;
						row.setValue(valueIndex, _method);
					}

					ds.addRow(row);
					this.addEditorAttr(ds, row);
				}
				{
					Row row = ds.getEmptyRow();
					row.setValue(idIdex, eventName + "_Controller");
					row.setValue(pidIndex, eventName);
					row.setValue(nameIndex, "控制类(controller)");
					if (eventConf != null && StringUtils.isNotBlank(eventConf.getControllerClazz())) {
						row.setValue(valueIndex, eventConf.getControllerClazz());
					} else {
						row.setValue(valueIndex, controller);
					}
					ds.addRow(row);
					this.addEditorAttr(ds, row);
				}
				{
					Row row = ds.getEmptyRow();
					row.setValue(idIdex, eventName + "_Onserver");
					row.setValue(pidIndex, eventName);
					row.setValue(nameIndex, "服务器上运行(onserver)");
					if (eventConf != null) {
						row.setValue(valueIndex, eventConf.isOnserver());
					} else {
						row.setValue(valueIndex, "true");
					}
					ds.addRow(row);
					this.addEditorAttr(ds, row);
				}
				{
					Row row = ds.getEmptyRow();
					row.setValue(idIdex, eventName + "_Async");
					row.setValue(pidIndex, eventName);
					row.setValue(nameIndex, "同步(async)");
					if (eventConf != null) {
						row.setValue(valueIndex, eventConf.isAsync());
					} else {
						row.setValue(valueIndex, "true");
					}
					ds.addRow(row);
					this.addEditorAttr(ds, row);
					onserv = row.getValue(valueIndex).toString();
				}
				{
					Row row = ds.getEmptyRow();
					row.setValue(idIdex, eventName + "_CommitRule");
					row.setValue(pidIndex, eventName);
					row.setValue(nameIndex, "提交规则");
					if (eventConf != null && eventConf.getSubmitRule() != null) {
						row.setValue(valueIndex, eventConf.getSubmitRule().getId());
					}
					ds.addRow(row);
					this.addEditorAttr(ds, row);
				}
				{
					Row row = ds.getEmptyRow();
					row.setValue(idIdex, eventName + "_State");
					row.setValue(pidIndex, eventName);
					row.setValue(nameIndex, "目标状态");
					if (eventConf != null) {
						row.setValue(valueIndex, eventConf.getuIStateId());
					}
					ds.addRow(row);
					this.addEditorAttr(ds, row);
				}
				{
					Row row = ds.getEmptyRow();
					row.setValue(idIdex, eventName + "_Lang");
					row.setValue(pidIndex, eventName);
					row.setValue(nameIndex, "事件语言");
					if (eventConf != null && eventConf.getEventLang() != null) {
						row.setValue(valueIndex, eventConf.getEventLang());
					} else {
						row.setValue(valueIndex, "Java语言");
					}
					ds.addRow(row);
					this.addEditorAttr(ds, row);
					eventLang = row.getString(valueIndex);
				}
				// 当onserver为true时显示，默认脚本为java语言，此时显示模式化命令
				if (StringUtils.equals(onserv, "true")) {
					{
						if (StringUtils.equals(eventLang, "Java语言")) {
							Row row = ds.getEmptyRow();
							row.setValue(idIdex, eventName + "_Command");
							row.setValue(pidIndex, eventName);
							row.setValue(nameIndex, "模式化命令");
							if (eventConf != null) {
								row.setValue(valueIndex, eventConf.getModelCmd());
							}
							ds.addRow(row);
							this.addEditorAttr(ds, row);
						} else {// mvel脚本
							Row row = ds.getEmptyRow();
							row.setValue(idIdex, eventName + "_LangExattr");
							row.setValue(pidIndex, eventName + "_Lang");
							row.setValue(nameIndex, "执行脚本");
							if (eventConf != null) {
								row.setValue(valueIndex, eventConf.getScript());
							}
							ds.addRow(row);
							this.addEditorAttr(ds, row);
						}
					}
				} else {

				}

			}
		}
	}

	// 目标状态DataList设值
	private void setStateList() {
		DataModels dataModels = LuiRuntimeContext.getWebContext().getPageMeta().getWidget("settings").getViewModels();
		DataList datalist = (DataList) dataModels.getComboData("stateList");
		datalist.removeAllDataItems();
		PagePartMeta pagemeta = PaCache.getEditorPagePartMeta();
		if(pagemeta == null) return;
		List<UIState> uistates = pagemeta.getuIStates();
		DataItem item = null;
		if (uistates != null && uistates.size() > 0) {
			for (UIState uistate : uistates) {
				String uistateId = uistate.getId();
				String uistateName = StringUtils.isNotBlank(uistate.getName()) ? uistate.getName() : uistate.getId();
				item = new DataItem();
				item.setText(uistateName);
				item.setValue(uistateId);
				datalist.addDataItem(item);
			}
		}
	}

	private String getEditCompDs() {
		String pageId = (String) PaCache.getInstance().get("_pageId");
		String sessionId = (String) PaCache.getInstance().get("eclipse_sesionId");
		IPaEditorService ipaService = new PaEditorServiceImpl();
		PagePartMeta pagemeta = ipaService.getOriPageMeta(pageId, sessionId);

		AppSession ctx = AppSession.current();
		String widgetId = replaceNullString(ctx.getParameter(RaParameter.PARAM_WIDGETID));
		String eleId = replaceNullString(ctx.getParameter(RaParameter.PARAM_ELEID));
		String subeleId = replaceNullString(ctx.getParameter(RaParameter.PARAM_SUBELEID));

		LuiElement webEle = null;

		if (eleId != null) {
			if (subeleId != null) {
				webEle = UIElementFinder.findWebElementById(pagemeta, widgetId, eleId, subeleId);
			} else {
				webEle = LuiFinder.getWebElement(pagemeta, widgetId, eleId);
			}
		}

		String dsId = null;
		if (webEle instanceof FormElement) {
			FormComp form1 = (FormComp) LuiFinder.getWebElement(pagemeta, widgetId, eleId);
			dsId = form1.getDataset();

		} else if (webEle instanceof GridColumn) {
			GridComp grid1 = (GridComp) LuiFinder.getWebElement(pagemeta, widgetId, eleId);
			dsId = grid1.getDataset();
		}
		return dsId;
	}

	private String replaceNullString(String arg) {
		if (arg != null) {
			if (arg.equals("null") || arg.equals(""))
				arg = null;
		}
		return arg;
	}

	private static String upperCase(String str) {
		if (str == null) {
			return null;
		}
		return str.toUpperCase();
	}

	public static final Object getValue(Object o, String fieldName) throws Exception {
		if (o == null)
			throw new IllegalArgumentException();
		// if (o instanceof UIElement) {
		// Object value=((UIElement) o).getAttribute(fieldName);
		// if(value==null){
		// fieldName="is"+StringUtils.capitalize(fieldName);
		// value=((UIElement) o).getAttribute(fieldName);
		// }
		// return value;
		// } else {
		Method m = null;
		String upCaseFieldName = upperCase(fieldName.substring(0, 1)) + fieldName.substring(1);
		try {
			m = o.getClass().getMethod("get" + upCaseFieldName, null);
		} catch (SecurityException e) {
			LuiLogger.error(e.getMessage(), e);
		} catch (NoSuchMethodException e) {
			try {
				m = o.getClass().getMethod("is" + upCaseFieldName, null);
			} catch (SecurityException e1) {
				LuiLogger.error(e.getMessage(), e);
			}
		}
		if (m != null) {
			return m.invoke(o, null);
		}
		// }
		return null;
	}

	// 输入管道
	public void pipeInSetting() {
		String app = (String) PaCache.getInstance().get("_appId");
		if (app != null) {
			return;
		}
		Dataset inDs = LuiRuntimeContext.getWebContext().getPageMeta().getWidget("settings").getViewModels().getDataset("ds_pipein");
		inDs.clear();
		PagePartMeta pagePart = PaCache.getEditorPagePartMeta();
		ViewPartMeta viewPartMeta = PaCache.getEditorViewPartMeta();
		loadPipeInDsData(inDs, viewPartMeta, pagePart);
	}

	public void loadPipeInDsData(Dataset inDs, ViewPartMeta viewPartMeta, PagePartMeta pagePart) {
		List<PipeIn> pipeIns = null;
		if (viewPartMeta != null) {
			pipeIns = viewPartMeta.getPipeIns();
		} else if (pagePart != null) {
			pipeIns = pagePart.getPipeIns();
		} else {
			return;
		}
		inDs.setEdit(true);
		if (pipeIns != null && pipeIns.size() > 0) {
			for (PipeIn pipeIn : pipeIns) {
				String pipeInUUid = UUID.randomUUID().toString();
				List<PipeInItem> inItems = pipeIn.getItemList();
				if (inItems == null)
					return;
				addRow(inDs, pipeInUUid, null, "输入管道", pipeIn.getId());
				// 加PipeInItem
				if (inItems.size() > 0) {
					for (PipeInItem inItem : inItems) {
						String uuid = UUID.randomUUID().toString();
						Row itemRow = addRow(inDs, uuid, pipeInUUid, "输入项", inItem.getId());
						{
							String pid = itemRow.getString(inDs.nameToIndex("Id"));
							addRow(inDs, inItem.getId(), pid, "id", inItem.getId());
							addRow(inDs, inItem.getClazztype(), pid, "输入类型", inItem.getClazztype());
						}
					}
				}
			}
		}
		inDs.setEdit(false);
	}

	// 输出管道
	public void pipeOutSetting() {
		String app = (String) PaCache.getInstance().get("_appId");
		if (app != null) {
			return;
		}
		Dataset outDs = LuiRuntimeContext.getWebContext().getPageMeta().getWidget("settings").getViewModels().getDataset("ds_pipeout");
		outDs.clear();
		PagePartMeta pagePart = PaCache.getEditorPagePartMeta();
		ViewPartMeta viewPartMeta = PaCache.getEditorViewPartMeta();
		loadPipeOutDsData(outDs, viewPartMeta, pagePart);
	}

	public void loadPipeOutDsData(Dataset outDs, ViewPartMeta viewPartMeta, PagePartMeta pagePart) {
		List<PipeOut> pipeOuts = null;
		if (viewPartMeta != null) {
			pipeOuts = viewPartMeta.getPipeOuts();
		} else if (pagePart != null) {
			pipeOuts = pagePart.getPipeOuts();
		} else {
			return;
		}
		outDs.setEdit(true);
		if (pipeOuts != null && pipeOuts.size() > 0) {
			for (PipeOut pipeOut : pipeOuts) {
				List<PipeOutItem> outItems = pipeOut.getItemList();
				List<TriggerItem> triggerItems = pipeOut.getEmitList();
				if ((outItems != null && outItems.size() > 0) || (triggerItems != null && triggerItems.size() > 0)) {
					String pipeOutUUid = UUID.randomUUID().toString();
					addRow(outDs, pipeOutUUid, null, "输出管道", pipeOut.getId());
					// 加Item、trigger
					{
						if (outItems != null && outItems.size() > 0) {
							for (PipeOutItem outItem : outItems) {
								String uuid = UUID.randomUUID().toString();
								Row itemRow = addRow(outDs, uuid, pipeOutUUid, "输出项", outItem.getName());
								{
									String pid = itemRow.getString(outDs.nameToIndex("Id"));
									addRow(outDs, outItem.getName(), pid, "输出键名称", outItem.getName());
									addRow(outDs, outItem.getType(), pid, "取数类型", outItem.getType());
									addRow(outDs, outItem.getSource(), pid, "取数来源", outItem.getSource());
									addRow(outDs, outItem.getDesc(), pid, "描述", outItem.getDesc());
									addRow(outDs, outItem.getClazztype(), pid, "输出对象类型", outItem.getClazztype());
								}
							}
						}
						if (triggerItems != null && triggerItems.size() > 0) {
							for (TriggerItem triggerItem : triggerItems) {
								String uuid = UUID.randomUUID().toString();
								Row itemRow = addRow(outDs, uuid, pipeOutUUid, "触发器", triggerItem.getId());
								{
									String pid = itemRow.getString(outDs.nameToIndex("Id"));
									addRow(outDs, triggerItem.getId(), pid, "触发器名称", triggerItem.getId());
									addRow(outDs, triggerItem.getType(), pid, "触发类型", triggerItem.getType());
									addRow(outDs, triggerItem.getSource(), pid, "触发来源", triggerItem.getSource());
									addRow(outDs, triggerItem.getDesc(), pid, "描述", triggerItem.getDesc());
								}
							}
						}
					}
				}
			}
		}
		outDs.setEdit(false);
	}

	// 连接器
	public void connectorSetting() {
		Dataset connDs = LuiRuntimeContext.getWebContext().getPageMeta().getWidget("settings").getViewModels().getDataset("ds_connector");
		connDs.clear();
		Application app = (Application) PaCache.getInstance().get("_editApp");
		if (app != null) {
			loadConnectorDsData(connDs, null, app);
		} else {
			PagePartMeta pagePart = PaCache.getEditorPagePartMeta();
			loadConnectorDsData(connDs, pagePart, null);
		}
	}

	public void loadConnectorDsData(Dataset connDs, PagePartMeta pagePart, Application app) {
		Connector[] connectors = null;
		if (app == null) {
			if (pagePart != null) {
				connectors = pagePart.getConnectors();
			} else {
				return;
			}
		} else {
			connectors = (app.getConnectorList()).toArray(new Connector[0]);
		}
		connDs.setEdit(true);
		if (connectors != null && connectors.length > 0) {
			for (Connector conn : connectors) {
				String connIdUUid = UUID.randomUUID().toString();
				Row connRow = addRow(connDs, connIdUUid, null, "连接器", conn.getId());
				{
					String src = conn.getSource();
					String tar = conn.getTarget();
					String srcWin = conn.getSourceWindow();
					String tarWin = conn.getTargetWindow();
					String pid = connRow.getString(connDs.nameToIndex("Id"));
					addRow(connDs, conn.getId(), pid, "id", conn.getId());
					addRow(connDs, src, pid, "输出view", src);
					addRow(connDs, conn.getPipeoutId(), pid, "pipeout", conn.getPipeoutId());
					addRow(connDs, tar, pid, "接收view", tar);
					addRow(connDs, conn.getPipeinId(), pid, "pipein", conn.getPipeinId());
					addRow(connDs, conn.getConnType(), pid, "连接类型", conn.getConnType());
					addRow(connDs, srcWin, pid, "输出win", srcWin);
					addRow(connDs, tarWin, pid, "接收win", tarWin);

					// map
					Map<String, String> map = conn.getMapping();
					if (map != null) {
						String mapsUuid = UUID.randomUUID().toString();
						Row mapsRow = addRow(connDs, mapsUuid, pid, "关系映射", "");
						Set<Entry<String, String>> entrys = map.entrySet();
						for (Entry<String, String> entry : entrys) {
							String mapUuid = UUID.randomUUID().toString();
							String outValue = entry.getKey();
							String inValue = entry.getValue();
							Row mapRow = addRow(connDs, mapUuid, mapsRow.getString(connDs.nameToIndex("Id")), "map", "");
							{
								String pid2 = mapRow.getString(connDs.nameToIndex("Id"));
								addRow(connDs, outValue, pid2, "输出键值", outValue);
								addRow(connDs, inValue, pid2, "输入键值", inValue);
							}
						}
					}
				}
			}
		}
		connDs.setEdit(false);
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

	// 参数
	public void paramSetting() {
		Dataset paramDs = LuiRuntimeContext.getWebContext().getPageMeta().getWidget("settings").getViewModels().getDataset("ds_param");
		Application app = (Application) PaCache.getInstance().get("_editApp");
		Properties props = null;
		if (app != null) {
			props = app.getProps();
			loadParamDsData(paramDs, props);
		} else {
			PagePartMeta pagePart = PaCache.getEditorPagePartMeta();
			if (pagePart != null) {
				if (pagePart.getWindow() != null) {
					props = pagePart.getWindow().getProps();
					loadParamDsData(paramDs, props);
				}
			}
		}
	}

	public void loadParamDsData(Dataset paramDs, Properties props) {
		paramDs.clear();
		int keyIndex = paramDs.nameToIndex("Name");
		int valueIndex = paramDs.nameToIndex("Value");
		if (props != null) {
			Set<Entry<Object, Object>> entrys = props.entrySet();
			for (Entry<Object, Object> entry : entrys) {
				Row row = paramDs.getEmptyRow();
				row.setValue(keyIndex, entry.getKey());
				row.setValue(valueIndex, entry.getValue());
				paramDs.addRow(row);
			}
		}
		paramDs.setEdit(false);
	}
}
