package xap.lui.core.model;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import xap.lui.core.builder.LuiSet;
import xap.lui.core.builder.SubmitRuleMergeUtil;
import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.comps.ViewElement;
import xap.lui.core.dataset.Parameter;
import xap.lui.core.event.AutoformEvent;
import xap.lui.core.event.ContextMenuEvent;
import xap.lui.core.event.DatasetEvent;
import xap.lui.core.event.DialogEvent;
import xap.lui.core.event.FocusEvent;
import xap.lui.core.event.GridCellEvent;
import xap.lui.core.event.GridEvent;
import xap.lui.core.event.GridRowEvent;
import xap.lui.core.event.LinkEvent;
import xap.lui.core.event.MouseEvent;
import xap.lui.core.event.PageEvent;
import xap.lui.core.event.SelfDefEvent;
import xap.lui.core.event.TextEvent;
import xap.lui.core.event.TreeCtxMenuEvent;
import xap.lui.core.event.TreeNodeDragEvent;
import xap.lui.core.event.TreeNodeEvent;
import xap.lui.core.event.TreeRowEvent;
import xap.lui.core.listener.DatasetRule;
import xap.lui.core.listener.EventSubmitRule;
import xap.lui.core.listener.FormRule;
import xap.lui.core.listener.GridRule;
import xap.lui.core.listener.LuiEventConf;
import xap.lui.core.listener.TreeRule;
import xap.lui.core.listener.WidgetRule;
import xap.lui.core.pluginout.IPlugoutType;
import xap.lui.core.pluginout.PlugoutTypeFactory;
import xap.lui.core.refrence.AppRefDftOkCtrl;
public final class PlugEventAdjuster {
	public static final String CONTROL_TYPE_PAGE = "Page";
	public static final String CONTROL_TYPE_VIEWPART = "ViewPart";
	public static final String CONTROL_TYPE_MENU = "Menu";
	public static final String CONTROL_TYPE_POPUPMENU = "PopupMenu";
	public static final String CONTROL_TYPE_DATASET = "Dataset";
	public static final String CONTROL_TYPE_BUTTON = "Button";
	public static final String CONTROL_TYPE_FORM = "Form";
	public static final String CONTROL_TYPE_GRID = "Grid";
	public static final String CONTROL_TYPE_TREE = "Tree";
	public static final String CONTROL_TYPE_EXCEL = "Excel";
	public static final String CONTROL_TYPE_IFRAMECOMP = "Iframe";
	public static final String CONTROL_TYPE_LABEL = "Label";
	public static final String CONTROL_TYPE_IMAGE = "Image";
	public static final String CONTROL_TYPE_LINKCOMP = "Link";
	public static final String CONTROL_TYPE_CUSTOMCONIZOL = "CustomConizol";
	
	public static final String CONTROL_TYPE_COMBOBOX = "ComboBox";
	public static final String CONTROL_TYPE_CHECKBOX = "CheckBox";
	public static final String CONTROL_TYPE_CHECKBOXGROUP = "CheckBoxGroup";
	public static final String CONTROL_TYPE_STRINGTEXT = "StringText";
	public static final String CONTROL_TYPE_INTEGERTEXT = "IntegerText";
	public static final String CONTROL_TYPE_DECIMAL = "Decimal";
	public static final String CONTROL_TYPE_PWDTEXT = "PwdText";
	public static final String CONTROL_TYPE_TEXTAREA = "TextArea";
	public static final String CONTROL_TYPE_RADIO = "Radio";
	public static final String CONTROL_TYPE_RADIOGROUP = "RadioGroup";
	public static final String CONTROL_TYPE_DATETEXT = "DateText";
	public static final String CONTROL_TYPE_REFERENCE = "Reference";
	
	public static final String PAGEID = "pageId";
	/**
	 * 针对window之间的plug增加plugEvent
	 * 
	 * @param app
	 */
	public void addAppPlugEventConf(AppContext appCtx, PagePartMeta pageMeta) {
		if (LuiRuntimeContext.getWebContext().getAppWebSession() == null)
			return;
		if (appCtx.getApplication() == null)
			return;
		// 对每个pageMeta都增加一个参照Plugin
		addRefPlugin(pageMeta);
		// 如果页面包含公共的查询模板则给此查询模板添加一个一个plugin
		addpubSimpleQueryPlugin(pageMeta);
		// 打开的为参照pageMeta时，增加参照plugout，注：参照的view的id定为"main"
		String pageId = (String) LuiRuntimeContext.getWebContext().getPageWebSession().getOriginalParameter(PAGEID);
		if (pageId == null || "".equals(pageId) || "reference".equals(pageId) || "null".equals(pageId))
			pageId = LuiRuntimeContext.getWebContext().getPageId();
		String isReference = (String) LuiRuntimeContext.getWebContext().getPageWebSession().getOriginalParameter("isReference");
		if (isReference != null && isReference.equals("true")) {
			String sourceViewId = "main";
			if (pageMeta.getWidget("main") == null && pageMeta.getWidgetIds().length > 0)
				sourceViewId = pageMeta.getWidgetIds()[0];
			// LuiWidget refWidget = pageMeta.getWidget("main");
			ViewPartMeta refWidget = pageMeta.getWidget(sourceViewId);
			if (refWidget != null) {
				addRefPlugout(refWidget);
				// 创建连接
				Connector conn = new Connector();
				conn.setSourceWindow(pageId);
				conn.setSource(refWidget.getId());
				conn.setPipeoutId(AppRefDftOkCtrl.PLUGOUT_ID);
				String parentPageId = (String) LuiRuntimeContext.getWebContext().getPageWebSession().getOriginalParameter("otherPageId");
				conn.setTargetWindow(parentPageId);
				String widgetId = (String) LuiRuntimeContext.getWebContext().getPageWebSession().getOriginalParameter("widgetId");
				conn.setTarget(widgetId);
				conn.setPipeinId(AppRefDftOkCtrl.PLUGIN_ID);
				String connId = conn.getSourceWindow() + "$" + conn.getSource() + "$" + conn.getTargetWindow() + "$" + conn.getTarget();
				boolean existConn = false;
				for (Connector c : appCtx.getApplication().getConnectorList()) {
					if (c.getId() != null && c.getId().equals(connId)) {
						existConn = true;
						break;
					}
				}
				if (!existConn) {
					conn.setId(connId);
					appCtx.getApplication().addConnector(conn);
				}
			}
		}
		// 是否是弹出查询模板
		String isqeury = (String) LuiRuntimeContext.getWebContext().getPageWebSession().getOriginalParameter("isAdvanceQuery");
		if (isqeury != null && isqeury.equals("true")) {
			generateQueryConnection(pageMeta, pageId);
		}
		// 生成plugout对象上的提交规则
		for (Connector conn : appCtx.getApplication().getConnectorList()) {
			String sourceWindowId = conn.getSourceWindow();
			String targetWindowId = conn.getTargetWindow();
			// 处理plugout
			if (sourceWindowId.equals(pageId)) {
				PagePartMeta inPagemeta = LuiRuntimeContext.getWebContext().getCrossPageMeta(targetWindowId);
				genPlugoutSubmitRule(pageMeta, inPagemeta, conn);
			}
		}
		// 绑定触发器事件
		for (Connector conn : appCtx.getApplication().getConnectorList()) {
			String sourceWindowId = conn.getSourceWindow();
			String targetWindowId = conn.getTargetWindow();
			// 处理plugout
			// String pageId =
			// LuiRuntimeEnvironment.getWebContext().getPageId();
			if (sourceWindowId.equals(pageId)) {
				PagePartMeta inPagemeta = LuiRuntimeContext.getWebContext().getCrossPageMeta(targetWindowId);
				addEvent(pageMeta, inPagemeta, conn);
			}
		}
	}
	/**
	 * 产生包含查询模板的window和查询模板间的connection
	 * 
	 * @param appCtx
	 * @param pageMeta
	 * @param pageId
	 */
	private void generateQueryConnection(PagePartMeta pageMeta, String pageId) {
		AppContext appCtx = AppSession.current().getAppContext();
		String sourceViewId = "main";
		if (pageMeta.getWidget("main") == null)
			return;
		// LuiWidget refWidget = pageMeta.getWidget("main");
		ViewPartMeta refWidget = pageMeta.getWidget(sourceViewId);
		if (refWidget != null) {
			addAdvanceQueryPlugout(refWidget);
			// 创建连接
			Connector conn = new Connector();
			conn.setSourceWindow(pageId);
			conn.setSource(refWidget.getId());
			conn.setPipeoutId("advanceQueryPlugOut");
			String parentPageId = (String) LuiRuntimeContext.getWebContext().getPageWebSession().getOriginalParameter("otherPageId");
			conn.setTargetWindow(parentPageId);
			String widgetId = (String) LuiRuntimeContext.getWebContext().getPageWebSession().getOriginalParameter("widgetId");
			conn.setTarget(widgetId);
			conn.setPipeinId("advancePlugin");
			String connId = conn.getSourceWindow() + "$" + conn.getSource() + "$" + conn.getTargetWindow() + "$" + conn.getTarget();
			boolean existConn = false;
			for (Connector c : appCtx.getApplication().getConnectorList()) {
				if (c.getId() != null && c.getId().equals(connId)) {
					existConn = true;
					break;
				}
			}
			if (!existConn) {
				conn.setId(connId);
				appCtx.getApplication().addConnector(conn);
			}
		}
	}
	/**
	 * 给包含公共简单查询模板的widget添加plugin, plugin的id
	 * 
	 * @param pageMeta
	 */
	private void addpubSimpleQueryPlugin(PagePartMeta pageMeta) {
		boolean iscontains = false;
		List<ViewPartConfig> widgetConfigs = pageMeta.getViewPartList();
		for (int i = 0; i < widgetConfigs.size(); i++) {
			ViewPartConfig widgetConfig = widgetConfigs.get(i);
			if ("../pubview_simplequery".equals(widgetConfig.getRefId())) {
				iscontains = true;
				break;
			}
		}
		if (iscontains == false)
			return;
		PipeIn pluginDesc = new PipeIn();
		pluginDesc.setId("advancePlugin");
		List<PipeInItem> descItemList = new ArrayList<PipeInItem>();
		PipeInItem typeItem = new PipeInItem();
		typeItem.setId("queryParam");
		descItemList.add(typeItem);
		pluginDesc.setItemList(descItemList);
		for (ViewPartMeta widget : pageMeta.getWidgets()) {
			if (widget.getPipeIn("advancePlugin") == null)
				widget.addPipeIns(pluginDesc);
		}
		// 是否包含查询方案
		iscontains = false;
		for (int i = 0; i < widgetConfigs.size(); i++) {
			ViewPartConfig widgetConfig = widgetConfigs.get(i);
			if ("../pubview_queryplan".equals(widgetConfig.getRefId())) {
				iscontains = true;
				break;
			}
		}
		if (iscontains) {
			Connector conn = new Connector();
			conn.setSource("queryplan");
			conn.setPipeoutId("queryPlanPlugout");
			conn.setTarget("simplequery");
			conn.setPipeinId("queryPlanPlugin");
			String connId = conn.getSourceWindow() + "$" + conn.getSource() + "$" + conn.getTargetWindow() + "$" + conn.getTarget();
			boolean existConn = false;
			for (Connector c : pageMeta.getConnectors()) {
				if (c.getId() != null && c.getId().equals(connId)) {
					existConn = true;
					break;
				}
			}
			if (!existConn) {
				conn.setId(connId);
				pageMeta.addConnector(conn);
			}
		}
	}
	/**
	 * 针对window内plug增加plugEvent
	 * 
	 * @param pageMeta
	 */
	public void addPlugEventConf(PagePartMeta pageMeta) {
		// 生成plugout对象上的提交规则
		Iterator<Connector> it = pageMeta.getConnectorMap().iterator();
		while (it.hasNext()) {
			Connector conn = it.next();
			genPlugoutSubmitRule(pageMeta, pageMeta, conn);
		}
		// 绑定触发器事件
		Iterator<Connector> it2 = pageMeta.getConnectorMap().iterator();
		while (it2.hasNext()) {
			Connector conn = it2.next();
			addEvent(pageMeta, pageMeta, conn);
		}
	}
	/**
	 * 根据Connector对plugout对象生成或合并提交规则
	 * 
	 * @param outPageMeta
	 * @param inPageMeta
	 * @param conn
	 */
	private void genPlugoutSubmitRule(PagePartMeta outPageMeta, PagePartMeta inPageMeta, Connector conn) {
		// String sourceWindowId = conn.getSourceWindow();
		String sourceWidgetId = conn.getSource();
		String plugoutId = conn.getPipeoutId();
		String targetWidgetId = conn.getTarget();
		String pluginId = conn.getPipeinId();
		ViewPartMeta outWidget = outPageMeta.getWidget(sourceWidgetId);
		if (outWidget == null)
			return;
		PipeOut plugoutDesc = outWidget.getPipeOut(plugoutId);
		if (plugoutDesc == null)
			return;
		EventSubmitRule sr = null;
		// 设置提交规则
		sr = new EventSubmitRule();
		WidgetRule sourceWr = new WidgetRule();
		sourceWr.setId(sourceWidgetId);
		List<PipeOutItem> descDescList = plugoutDesc.getItemList();
		if (descDescList != null && descDescList.size() > 0) {
			for (PipeOutItem item : descDescList) {
				String type = item.getType();
				// type = type.split("\\.")[1];
				String source = item.getSource();
				IPlugoutType plugoutType = PlugoutTypeFactory.getPlugoutType(type);
				if (plugoutType != null)
					plugoutType.buildSourceWidgetRule(sourceWr, source);
			}
		}
		sr.addWidgetRule(sourceWr);
		// if (sourceWindowId != null)
		// sr.addParam(new
		// Parameter(AppLifeCycleContext.PLUGOUT_SOURCE_WINDOW,sourceWindowId));
		sr.addParam(new Parameter(AppSession.PLUGOUT_SIGN, "1"));
		sr.addParam(new Parameter(AppSession.PLUGOUT_SOURCE, sourceWidgetId));
		sr.addParam(new Parameter(AppSession.PLUGOUT_ID, plugoutId));
		// 增加plugin的提交规则
		if (inPageMeta != null) {
			// 不同window
			if (!inPageMeta.getId().equals(outPageMeta.getId())) {
				ViewPartMeta targetWidget = inPageMeta.getWidget(targetWidgetId);
				if (targetWidget != null) {
					EventSubmitRule targetsubmitRule = getTargetWidgetSubmitRule(targetWidget, pluginId);
					if (targetsubmitRule == null) {
						targetsubmitRule = new EventSubmitRule();
						WidgetRule pwr = new WidgetRule();
						pwr.setId(targetWidgetId);
						targetsubmitRule.addWidgetRule(pwr);
					}
					sr.setParentSubmitRule(targetsubmitRule);
				}
			}
			// 同window内
			else {
				ViewPartMeta targetWidget = inPageMeta.getWidget(targetWidgetId);
				if (targetWidget != null) {
					EventSubmitRule targetsubmitRule = getTargetWidgetSubmitRule(targetWidget, pluginId);
					if (targetsubmitRule != null) {
						sr.addWidgetRule(targetsubmitRule.getWidgetRule(targetWidgetId));
					}
				}
			}
		}
		if (plugoutDesc.getSubmitRule() == null)
			plugoutDesc.setSubmitRule(sr);
		else
			SubmitRuleMergeUtil.mergeSubmitRule(plugoutDesc.getSubmitRule(), sr);
	}
	/**
	 * 对于有触发器的plugout，往触发器上绑定plug事件
	 * 
	 * @param outPageMeta
	 * @param inPageMeta
	 * @param conn
	 */
	private void addEvent(PagePartMeta outPageMeta, PagePartMeta inPageMeta, Connector conn) {
		// String sourceWindowId = conn.getSourceWindow();
		String sourceWidgetId = conn.getSource();
		String plugoutId = conn.getPipeoutId();
		// String targetWidgetId = conn.getTarget();
		// String pluginId = conn.getPluginId();
		ViewPartMeta outWidget = outPageMeta.getWidget(sourceWidgetId);
		if (outWidget == null)
			return;
		PipeOut plugoutDesc = outWidget.getPipeOut(plugoutId);
		if (plugoutDesc == null)
			return;
		List<TriggerItem> descEmitList = plugoutDesc.getEmitList();
		if (descEmitList == null)
			return;
		EventSubmitRule sr = plugoutDesc.getSubmitRule();
		for (TriggerItem item : descEmitList) {
			// TODO 增加异常处理
			String type = item.getType();
			String source = item.getSource();
			String controlType = type.split("\\.")[0];
			String eventType = type.split("\\.")[1];
			String sourceItem = null;
			if (source.indexOf(".") != -1) {
				sourceItem = source.split("\\.")[1];
				source = source.split("\\.")[0];
			}
			LuiEventConf event = new LuiEventConf();
			event.setEventType(getJsEventClaszz(controlType, eventType));
			event.setOnserver(true);
			event.setSubmitRule(sr);
			event.setEventName(eventType);
			// StringBuffer scriptBuf = new StringBuffer();
			// scriptBuf.append(generateSubmitRule(sr));
			// scriptBuf.append("$.pageutils.triggerPlugout(submitRule);\n");
			// event.setScript(scriptBuf.toString());
			if (sourceItem == null)
				event.setMethod(controlType + "_" + source + "_plugevent");
			else
				event.setMethod(controlType + "_" + source + "_" + sourceItem + "_plugevent");
			List<LuiEventConf> eventList = null;
			if (controlType.equals(CONTROL_TYPE_PAGE)) {
				eventList = outPageMeta.getEventConfList();
				if (eventList == null) {
					eventList = new ArrayList<LuiEventConf>();
					outPageMeta.setEventConfList(eventList);
				}
			} else if (controlType.equals(CONTROL_TYPE_VIEWPART)) {
				eventList = getEventList(outPageMeta.getWidget(sourceWidgetId));
			} else if (controlType.equals(CONTROL_TYPE_MENU) || controlType.equals(CONTROL_TYPE_POPUPMENU)) {
				eventList = getEventList(outPageMeta.getWidget(sourceWidgetId).getViewMenus().getMenuBar(source).getItem(sourceItem));
			}
			// else if (controlType.equals(CONTROL_TYPE_CONINTER)){
			// eventList =
			// getEventList(outPageMeta.getWidget(sourceWidgetId).getViewConinters().getContainer(source));
			// }
			else if (isComponentType(controlType)) {
				eventList = getEventList(outPageMeta.getWidget(sourceWidgetId).getViewComponents().getComponent(source));
			} else if (controlType.equals(CONTROL_TYPE_DATASET)) {
				eventList = getEventList(outPageMeta.getWidget(sourceWidgetId).getViewModels().getDataset(source));
			}
			mergeEvent(eventList, event);
		}
	}
	/**
	 * 合并event
	 * */
	private void mergeEvent(List<LuiEventConf> eventList, LuiEventConf event) {
		if (null != eventList) {
			boolean merged = false;
			for (LuiEventConf e : eventList) {
				if (e.isOnserver() && e.getName().equals(event.getName())) {
					// TODO 临时处理，submitRule以后是否可以都放到前台
					if (e.getSubmitRule() != null && e.getSubmitRule().getParentSubmitRule() != null) {
						if (e.getSubmitRule().getParentSubmitRule().getWidgetRules() != null) {
							e.getSubmitRule().getParentSubmitRule().getWidgetRules().clear();
						}
					}
					mergeSubmitRules(e, event);
					merged = true;
					break;
				}
			}
			// for (EventConf e : eventList){
			// if (e.getMethodName().equals(event.getMethodName())){
			// mergeSubmitRules(e, event);
			// merged = true;
			// break;
			// }
			// }
			if (!merged)
				eventList.add(event);
		}
	}
	/**
	 * 合并提交规则
	 * 
	 * @param e
	 * @param event
	 */
	private void mergeSubmitRules(LuiEventConf e, LuiEventConf event) {
		if (event.getSubmitRule() == null || event.getSubmitRule().getWidgetRules().size() == 0)
			return;
		if (e.getSubmitRule() == null)
			e.setSubmitRule(event.getSubmitRule());
		SubmitRuleMergeUtil.mergeSubmitRule(e.getSubmitRule(), event.getSubmitRule());
		// Iterator<WidgetRule> it =
		// event.getSubmitRule().getWidgetRules().values().iterator();
		// while (it.hasNext()){
		// e.getSubmitRule().addWidgetRule(it.next());
		// }
	}
	public EventSubmitRule getTargetWidgetSubmitRule(ViewPartMeta targetWidget, String pluginId) {
		if (targetWidget == null || targetWidget.getEventConfs() == null)
			return null;
		if (targetWidget.getEventConfs().length < 1)
			return null;
		for (LuiEventConf event : targetWidget.getEventConfs()) {
			if (event.getMethod().equals("plugin" + pluginId)) {
				return event.getSubmitRule();
			}
		}
		return null;
	}
	/**
	 * 获取控件事件对应监听类
	 * 
	 * @param controlType
	 * @param eventType
	 * @return
	 */
	private String getJsEventClaszz(String controlType, String eventType) {
		// page
		if (controlType.equals(CONTROL_TYPE_PAGE)) {
			return PageEvent.class.getSimpleName();
		}
		// widget
		else if (controlType.equals(CONTROL_TYPE_VIEWPART)) {
			return DialogEvent.class.getSimpleName();
		}
		// 只有mouseListener
		else if (controlType.equals(CONTROL_TYPE_MENU) || controlType.equals(CONTROL_TYPE_BUTTON) || controlType.equals(CONTROL_TYPE_IFRAMECOMP) || controlType.equals(CONTROL_TYPE_LABEL)) {
			return MouseEvent.class.getSimpleName();
		}
		// 右键菜单
		else if (controlType.equals(CONTROL_TYPE_POPUPMENU)) {
			if (isMouseEvent(eventType))
				return MouseEvent.class.getSimpleName();
			else
				return ContextMenuEvent.class.getSimpleName();
		}
		// 数据集
		else if (controlType.equals(CONTROL_TYPE_DATASET))
			return DatasetEvent.class.getSimpleName();
		// grid
		else if (controlType.equals(CONTROL_TYPE_GRID)) {
			if (isMouseEvent(eventType))
				return MouseEvent.class.getSimpleName();
			else if (eventType.equals(GridEvent.ON_DATA_OUTER_DIV_CONTEXT_MENU))
				return GridEvent.class.getSimpleName();
			else if (eventType.equals(GridRowEvent.BEFORE_ROW_SELECTED) || eventType.equals(GridRowEvent.ON_ROW_DB_CLICK) || eventType.equals(GridRowEvent.ON_ROW_SELECTED))
				return GridRowEvent.class.getSimpleName();
			else
				return GridCellEvent.class.getSimpleName();
		} else if (controlType.equals(CONTROL_TYPE_TREE)) {
			if (isMouseEvent(eventType))
				return MouseEvent.class.getSimpleName();
			else if (eventType.equals(TreeNodeDragEvent.ON_DRAG_END) || eventType.equals(TreeNodeDragEvent.ON_DRAG_START) || eventType.equals(TreeNodeEvent.ON_CLICK) || eventType.equals(TreeNodeEvent.ON_DBCLICK) || eventType.equals(TreeNodeEvent.ON_NODE_LOAD) || eventType.equals(TreeNodeEvent.ON_CHECKED) || eventType.equals(TreeNodeEvent.BEFORE_SEL_NODE_CHANGE) || eventType.equals(TreeNodeEvent.AFTER_SEL_NODE_CHANGE) || eventType.equals(TreeNodeEvent.ROOT_NODE_CREATED)
					|| eventType.equals(TreeNodeEvent.NODE_CREATED) || eventType.equals(TreeNodeEvent.BEFORE_NODE_CAPTION_CHANGE))
				return TreeNodeEvent.class.getSimpleName();
			else if (eventType.equals(TreeRowEvent.BEFORE_NODE_CREATE))
				return TreeNodeEvent.class.getSimpleName();
			else
				return TreeCtxMenuEvent.class.getSimpleName();
		}
		// form
		else if (controlType.equals(CONTROL_TYPE_FORM)) {
			if (isMouseEvent(eventType))
				return MouseEvent.class.getSimpleName();
			else if (eventType.equals(FocusEvent.ON_BLUR) || eventType.equals(FocusEvent.ON_FOCUS))
				return FocusEvent.class.getSimpleName();
			else
				return AutoformEvent.class.getSimpleName();
		}
		// 复选框
		else if (controlType.equals(CONTROL_TYPE_CHECKBOX)) {
			if (isMouseEvent(eventType))
				return MouseEvent.class.getSimpleName();
			else if (eventType.equals(FocusEvent.ON_BLUR) || eventType.equals(FocusEvent.ON_FOCUS))
				return FocusEvent.class.getSimpleName();
			else if (eventType.equals(TextEvent.ON_SELECT) || eventType.equals(TextEvent.ON_SELECT))
				return TextEvent.class.getSimpleName();
			else
				return KeyListener.class.getSimpleName();
		}
		// 超链接
		else if (controlType.equals(CONTROL_TYPE_LINKCOMP))
			return LinkEvent.class.getSimpleName();
		// 自定义控件
		else if (controlType.equals(CONTROL_TYPE_CUSTOMCONIZOL))
			return SelfDefEvent.class.getSimpleName();
		else
			return MouseEvent.class.getSimpleName();
	}
	/**
	 * 判断是否为鼠标事件
	 * 
	 * @param eventType
	 * @return
	 */
	private boolean isMouseEvent(String eventType) {
		if (eventType.equals(MouseEvent.ON_CLICK) || eventType.equals(MouseEvent.ON_DB_CLICK) || eventType.equals(MouseEvent.ON_MOUSE_OUT) || eventType.equals(MouseEvent.ON_MOUSE_OVER))
			return true;
		else
			return false;
	}
	/**
	 * 触发器是否为Component
	 * 
	 * @param controlType
	 * @return
	 */
	private boolean isComponentType(String controlType) {
		if (controlType.equals(CONTROL_TYPE_BUTTON) || controlType.equals(CONTROL_TYPE_FORM) || controlType.equals(CONTROL_TYPE_GRID) || controlType.equals(CONTROL_TYPE_TREE) || controlType.equals(CONTROL_TYPE_EXCEL) || controlType.equals(CONTROL_TYPE_CHECKBOX) || controlType.equals(CONTROL_TYPE_IFRAMECOMP) || controlType.equals(CONTROL_TYPE_LABEL) || controlType.equals(CONTROL_TYPE_IMAGE) || controlType.equals(CONTROL_TYPE_LINKCOMP) || controlType.equals(CONTROL_TYPE_CUSTOMCONIZOL))
			return true;
		else
			return false;
	}
	/**
	 * 生成提交规则
	 * 
	 */
	public String generateSubmitRuleScript(EventSubmitRule submitRule) {
		if (submitRule != null) {
			// String ruleId = "sr_" + jsEvent.getName() + "_" +
			// listener.getId();
			String ruleId = "submitRule";
			StringBuffer buf = new StringBuffer();
			buf.append("var ").append(ruleId).append(" = $.submitrule.getObj();\n");
			if (submitRule.getParamMap() != null && submitRule.getParamMap().size() > 0) {
				Iterator<Entry<String, Parameter>> it = submitRule.getParamMap().entrySet().iterator();
				while (it.hasNext()) {
					Entry<String, Parameter> entry = it.next();
					buf.append(ruleId).append(".addParam('").append(entry.getKey()).append("', '").append(entry.getValue().getValue()).append("');\n");
				}
			}
			if (submitRule.isCardSubmit()) {
				buf.append(ruleId).append(".setCardSubmit(true);\n");
			}
			String jsScript = generateWidgetRulesScript(submitRule, ruleId);
			buf.append(jsScript);
			EventSubmitRule pSubmitRule = submitRule.getParentSubmitRule();
			if (pSubmitRule != null) {
				String pRuleId = ruleId + "_parent";
				buf.append("var " + pRuleId + " = $.submitrule.getObj();\n");
				String pJsScript = generateWidgetRulesScript(pSubmitRule, pRuleId);
				buf.append(pJsScript);
				buf.append(ruleId + ".setParentSubmitRule(" + pRuleId + ");\n");
			}
			return buf.toString();
			// buf.append(listenerShowId).append(".").append(jsEvent.getName()).append(".submitRule = (").append(ruleId).append(");\n");
		} else
			return "";
	}
	/**
	 * 创建js的提交规则
	 * 
	 * @param submitRule
	 * @param ruleId
	 * @return
	 */
	private String generateWidgetRulesScript(EventSubmitRule submitRule, String ruleId) {
		StringBuffer sb = new StringBuffer();
		LuiSet<WidgetRule> widgetRuleMap = submitRule.getWidgetRules();
		if (widgetRuleMap != null && !widgetRuleMap.isEmpty()) {
			Iterator<WidgetRule> it = widgetRuleMap.iterator();
			while (it.hasNext()) {
				WidgetRule widgetRule = it.next();
				String wstr = generateWidgetRuleScript(widgetRule);
				sb.append(wstr);
				String widgetId = widgetRule.getId();
				sb.append(ruleId).append(".addViewPartRule('").append(widgetId).append("', wdr_").append(widgetId).append(");\n");
			}
		}
		return sb.toString();
	}
	/**
	 * 得到片段的规则
	 * 
	 * @param widgetRule
	 * @return
	 */
	private String generateWidgetRuleScript(WidgetRule widgetRule) {
		StringBuffer buf = new StringBuffer();
		String wid = "wdr_" + widgetRule.getId();
		buf.append("var ").append(wid).append(" = $.viewpartrule.getObj('").append(widgetRule.getId()).append("');\n");
		if (widgetRule.isCardSubmit()) {
			buf.append(wid).append(".setCardSubmit(true);\n");
		}
		if (widgetRule.isTabSubmit()) {
			buf.append(wid).append(".setTabSubmit(true);\n");
		}
		if (widgetRule.isPanelSubmit()) {
			buf.append(wid).append(".setPanelSubmit(true);\n");
		}
		DatasetRule[] dsRules = widgetRule.getDatasetRules();
		if (dsRules != null) {
			for (int i = 0; i < dsRules.length; i++) {
				DatasetRule dsRule = dsRules[i];
				String id = "dsr_" + dsRule.getId();
				buf.append("var ").append(id).append(" = $.datasetrule.getObj('").append(dsRule.getId()).append("','").append(dsRule.getType()).append("');\n");
				buf.append(wid).append(".addDsRule('").append(dsRule.getId()).append("',").append(id).append(");\n");
			}
		}
		TreeRule[] treeRules = widgetRule.getTreeRules();
		if (treeRules != null) {
			for (int i = 0; i < treeRules.length; i++) {
				TreeRule treeRule = treeRules[i];
				String id = "treer_" + treeRule.getId();
				buf.append("var ").append(id).append(" = $.treerule.getObj('").append(treeRule.getId()).append("','").append(treeRule.getType()).append("');\n");
				buf.append(wid).append(".addTreeRule('").append(treeRule.getId()).append("',").append(id).append(");\n");
			}
		}
		GridRule[] gridRules = widgetRule.getGridRules();
		if (gridRules != null) {
			for (int i = 0; i < gridRules.length; i++) {
				GridRule gridRule = gridRules[i];
				String id = "gridr_" + gridRule.getId();
				buf.append("var ").append(id).append(" = $.gridrule.getObj('").append(gridRule.getId()).append("','").append(gridRule.getType()).append("');\n");
				buf.append(wid).append(".addGridRule('").append(gridRule.getId() + "',").append(id).append(");\n");
			}
		}
		FormRule[] formRules = widgetRule.getFormRules();
		if (formRules != null) {
			for (int i = 0; i < formRules.length; i++) {
				FormRule formRule = formRules[i];
				String id = "formr_" + formRule.getId();
				buf.append("var ").append(id).append(" = $.formrule.getObj('").append(formRule.getId()).append("','").append(formRule.getType()).append("');\n");
				buf.append(wid).append(".addFormRule('").append(formRule.getId() + "',").append(id).append(");\n");
			}
		}
		return buf.toString();
	}
	/**
	 * 对每个pageMeta都增加一个参照Plugin
	 * 
	 * @param pageMeta
	 */
	private void addRefPlugin(PagePartMeta pageMeta) {
		PipeIn pluginDesc = new PipeIn();
		pluginDesc.setId(AppRefDftOkCtrl.PLUGIN_ID);
		List<PipeInItem> descItemList = new ArrayList<PipeInItem>();
		PipeInItem typeItem = new PipeInItem();
		typeItem.setId(AppRefDftOkCtrl.TYPE);
		PipeInItem idItem = new PipeInItem();
		idItem.setId(AppRefDftOkCtrl.ID);
		PipeInItem writefieldsItem = new PipeInItem();
		writefieldsItem.setId(AppRefDftOkCtrl.WRITEFIELDS);
		descItemList.add(typeItem);
		descItemList.add(idItem);
		descItemList.add(writefieldsItem);
		pluginDesc.setItemList(descItemList);
		for (ViewPartMeta widget : pageMeta.getWidgets()) {
			if (widget.getPipeIn(AppRefDftOkCtrl.PLUGIN_ID) == null)
				widget.addPipeIns(pluginDesc);
		}
	}
	/**
	 * 对每个pageMeta都增加一个参照Plugout
	 * 
	 * @param refWidget
	 */
	private void addAdvanceQueryPlugout(ViewPartMeta refWidget) {
		PipeOut plugoutDesc = new PipeOut();
		plugoutDesc.setId("advanceQueryPlugOut");
		List<PipeOutItem> plugoutDescItemList = new ArrayList<PipeOutItem>();
		PipeOutItem typeItem = new PipeOutItem();
		typeItem.setName(AppRefDftOkCtrl.TYPE);
		plugoutDescItemList.add(typeItem);
		plugoutDesc.setItemList(plugoutDescItemList);
		if (refWidget.getPipeOut("advanceQueryPlugOut") == null)
			refWidget.addPipeOuts(plugoutDesc);
	}
	/**
	 * 对每个pageMeta都增加一个参照Plugout
	 * 
	 * @param refWidget
	 */
	private void addRefPlugout(ViewPartMeta refWidget) {
		PipeOut plugoutDesc = new PipeOut();
		plugoutDesc.setId(AppRefDftOkCtrl.PLUGOUT_ID);
		List<PipeOutItem> plugoutDescItemList = new ArrayList<PipeOutItem>();
		PipeOutItem typeItem = new PipeOutItem();
		typeItem.setName(AppRefDftOkCtrl.TYPE);
		PipeOutItem idItem = new PipeOutItem();
		idItem.setName(AppRefDftOkCtrl.ID);
		PipeOutItem writefieldsItem = new PipeOutItem();
		writefieldsItem.setName(AppRefDftOkCtrl.WRITEFIELDS);
		PipeOutItem valuesItem = new PipeOutItem();
		plugoutDescItemList.add(typeItem);
		plugoutDescItemList.add(idItem);
		plugoutDescItemList.add(writefieldsItem);
		plugoutDescItemList.add(valuesItem);
		plugoutDesc.setItemList(plugoutDescItemList);
		if (refWidget.getPipeOut(AppRefDftOkCtrl.PLUGOUT_ID) == null)
			refWidget.addPipeOuts(plugoutDesc);
	}
	/**
	 * 获取WidgetElement上的事件列表
	 * 
	 * @param ele
	 * @return
	 */
	private List<LuiEventConf> getEventList(ViewElement ele) {
		if (ele == null)
			return null;
		List<LuiEventConf> eventList = null;
		eventList = ele.getEventConfList();
		if (eventList == null) {
			eventList = new ArrayList<LuiEventConf>();
			ele.setEventConfList(eventList);
		}
		return eventList;
	}
}
