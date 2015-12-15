package xap.lui.core.render;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import xap.lui.core.builder.LuiSet;
import xap.lui.core.builder.Window;
import xap.lui.core.comps.LuiElement;
import xap.lui.core.comps.MenuItem;
import xap.lui.core.comps.ToolBarItem;
import xap.lui.core.comps.ViewElement;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.LuiParameter;
import xap.lui.core.dataset.Parameter;
import xap.lui.core.event.DatasetEvent;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.listener.DatasetRule;
import xap.lui.core.listener.EventSubmitRule;
import xap.lui.core.listener.FormRule;
import xap.lui.core.listener.GridRule;
import xap.lui.core.listener.LuiEventConf;
import xap.lui.core.listener.LuiListenerConf;
import xap.lui.core.listener.TreeRule;
import xap.lui.core.listener.WidgetRule;
import xap.lui.core.model.AppSession;
import xap.lui.core.model.LuiPageContext;
import xap.lui.core.model.PagePartMeta;
import xap.lui.core.model.ViewPartComps;
import xap.lui.core.model.ViewPartMenus;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.core.util.ClassUtil;
import com.alibaba.fastjson.JSON;
import freemarker.template.TemplateDirectiveModel;
public abstract class BaseLuiDirectiveModel implements TemplateDirectiveModel {
	// private static final String DIV_INDEX = "$DIV_INDEX";
	public static final String DIV_PRE = "$d_";
	public static final String COMP_PRE = "$c_";
	public static final String TAB_PRE = "$tab_";
	public static final String DS_PRE = "$ds_";
	public static final String DS_RELATION_PRE = "$dsr_";
	public static final String COMMAND_PRE = "$cm_";
	public static final String COMBO_PRE = "$cb_";
	public static final String SERVICE_PRE = "$s_";
	public static final String CS_PRE = "$cs_";
	public static final String RF_RELATION_PRE = "$rfr_";
	public static final String RF_PRE = "$rf_";
	public static final String TL_PRE = "$tl_";
	public static final String SLOT_PRE = "$sl_";
	public static final String WIDGET_PRE = "$wd_";
	public static final String LISTENER_PRE = "$ls_";
	protected static final String DS_SCRIPT = "dsScript";
	protected static final String ALL_SCRIPT = "allScript";
	protected static final String BODY_SCRIPT_MAP = "bodyScriptMap";
	private String widgetId = null;
	private String compId = null;
	private String varCompShowId = null;
	private String divCompShowId = null;
	private ViewPartMeta currWidget = null;
	private Window window = null;
	public Window getWindow() {
		return window;
	}
	public void setWindow(Window window) {
		this.window = window;
	}
	protected String getVarShowId() {
		if (varCompShowId == null) {
			if (getCntWidget() == null)
				varCompShowId = COMP_PRE + getCompId();
			else
				varCompShowId = COMP_PRE + getCntWidget().getId() + "_" + getCompId();
		}
		return varCompShowId;
	}
	protected String getVarShowId(String id) {
		String varShowId = "";
		if (getCntWidget() == null)
			varShowId = COMP_PRE + id;
		else
			varShowId = COMP_PRE + getCntWidget().getId() + "_" + id;
		return varShowId;
	}
	protected String getDivShowId() {
		if (divCompShowId == null) {
			Integer index = null;
			if (index == null) {
				index = Integer.valueOf(0);
			}
			divCompShowId = DIV_PRE + getCompId() + index;
		}
		return divCompShowId;
	}
	protected ViewPartMeta getCntWidget() {
		if (currWidget != null) {
			return currWidget;
		}
		if (this.widgetId != null) {
			currWidget = this.window.getPageMeta().getWidget(this.widgetId);
			return currWidget;
		}
		ViewPartMeta[] widgets = this.window.getPageMeta().getWidgets();
		if (widgets == null || widgets.length == 0) {
			return currWidget;
		}
		int doubleId = 0;
		ViewPartMeta ownerWidget = null;
		for (int i = 0; i < widgets.length; i++) {
			ViewPartComps views = widgets[i].getViewComponents();
			ViewPartMenus contextMenus = widgets[i].getViewMenus();
			if (views.getComponent(getCompId()) != null) {
				doubleId++;
			}
			if (contextMenus.getContextMenu(getCompId()) != null) {
				doubleId++;
			}
			if (contextMenus.getMenuBar(getCompId()) != null)
				doubleId++;
			if (doubleId >= 2)
				throw new LuiRuntimeException("组件id和已有组件id重复,id=" + getCompId());
			else if (doubleId == 1 && ownerWidget == null)
				ownerWidget = widgets[i];
		}
		if (doubleId == 0) {
			return null;
		}
		currWidget = ownerWidget;
		return currWidget;
	}
	/**
	 * 给相应元素增加js事件.事件参数根据当前Tag的事件映射map获得
	 * 
	 * @param element
	 * @param showId
	 *            控件的当前渲染ID
	 */
	protected String addEventSupport(LuiElement element, String widgetId, String showId, String parentId) {
		StringBuffer buf = new StringBuffer("");
		List<LuiListenerConf> listeners = new ArrayList<LuiListenerConf>();
		Collection<LuiListenerConf> eventConfListeners = controller2Event(element.getEventConfs(), element);
		if (eventConfListeners != null)
			listeners.addAll(eventConfListeners);
		if (listeners == null || listeners.isEmpty())
			return "";
		for (LuiListenerConf listener : listeners) {
			String listenerId = null;
			if (widgetId != null)
				listenerId = LISTENER_PRE + widgetId + "_" + listener.getId();
			else
				listenerId = LISTENER_PRE + listener.getId();
			String listenerStr = generateListener(listener, listenerId, widgetId, element, parentId);
			buf.append(listenerStr);
			if(element instanceof PagePartMeta){}
			else{
				buf.append(showId).append(".addListener(").append(listenerId).append(");\n");
			}
			
		}
		return buf.toString();
	}
	private Collection<LuiListenerConf> controller2Event(LuiEventConf[] events, LuiElement element) {
		if (events == null || events.length == 0) {
			return null;
		}
		Map<String, LuiListenerConf> jsListeners = new HashMap<String, LuiListenerConf>();
		for (LuiEventConf event : events) {
			String jsEventClazz = event.getEventType();
			if (jsEventClazz == null)
				continue;
			String listenerKey = jsEventClazz + "_" + event.getMethod();
			LuiListenerConf jslc = jsListeners.get(listenerKey);
			if (jslc == null)
				jslc = (LuiListenerConf) ClassUtil.newInstance(event.getEventType());
			if (jslc == null)
				continue;
			String ctrl = event.getControllerClazz();
			String parentCtrl = null;
			String el = AppSession.EVENT_LEVEL_APP;
			if (element instanceof ViewElement) {
				ViewElement we = (ViewElement) element;
				parentCtrl =  this.window.getPageMeta().getWidget(we.getWidget().getId()).getController();
				el = AppSession.EVENT_LEVEL_VIEW;
			}
			if (element instanceof PagePartMeta) {
				PagePartMeta we = (PagePartMeta) element;
				parentCtrl = we.getController();
				el = AppSession.EVENT_LEVEL_WIN;
			}
			if (ctrl == null || ctrl.isEmpty()) {
				ctrl = parentCtrl;
			}
			event.setJsParam(jslc.getJsParam(event.getName()));
			event.setExtendAttribute(LuiEventConf.SUBMIT_PRE + AppSession.METHOD_NAME, event.getMethod());
			event.setExtendAttribute(LuiEventConf.SUBMIT_PRE + LuiPageContext.SOURCE_ID, element.getId());
			event.setExtendAttribute(LuiEventConf.SUBMIT_PRE + "clc", ctrl);
			event.setExtendAttribute(LuiEventConf.SUBMIT_PRE + AppSession.EVENT_LEVEL, el);
			jslc.setId(event.getMethod() + "_" + jslc.getClass().getSimpleName());
			jslc.setServerClazz(ctrl);
			jslc.addEventHandler(event);
			jsListeners.put(listenerKey, jslc);
		}
		return jsListeners.values();
	}
	protected String generateListener(LuiListenerConf listener, String listenerShowId, String widgetId, LuiElement ele, String parentId) {
		StringBuffer buf = new StringBuffer();
		if(ele instanceof PagePartMeta) {
			buf.append("pageUI");
			Map<String, LuiEventConf> eventMap = listener.getEventHandlerMap();
			Iterator<LuiEventConf> eit = eventMap.values().iterator();
			while (eit.hasNext()) {
				LuiEventConf jsEvent = eit.next();
				buf.append(".element.on('").append(jsEvent.getName()).append("' ,function(e");
				StringBuffer params = new StringBuffer();
				int pSize = jsEvent.getParamList().size();
				if (jsEvent.getParamList() != null && pSize > 0) {
					params.append(",");
					for (int i = 0; i < pSize; i++) {
						params.append(jsEvent.getParamList().get(i).getName());
						if (i != pSize - 1)
							params.append(",");
					}
				}
				buf.append(params.toString() + "){\n");
				if (listener.getServerClazz() != null && jsEvent.isOnserver()) {
					buf.append(generateServerProxy_new(widgetId,  ele, listener, jsEvent, parentId));
				} else if (!jsEvent.isOnserver()&&jsEvent.getScript() != null && !jsEvent.getScript().equals("")) {
					buf.append(jsEvent.getScript());
				}
				buf.append("\n});\n");
			}
		} else {
			buf.append("var ").append(listenerShowId).append(" = new ").append(listener.getJsClazz()).append("();\n");
			if (ele instanceof MenuItem) {
				buf.append(getVarShowId());
				MenuItem currEle = (MenuItem) ele;
				currEle = currEle.getParentItem();
				StringBuffer bf = new StringBuffer();
				while (currEle != null) {
					String str = "";
					str += ".getMenu('" + currEle.getId() + "')";
					currEle = currEle.getParentItem();
					bf.insert(0, str);
				}
				buf.append(bf.toString());
				buf.append(".getMenu('").append(ele.getId()).append("')");
			} else if (ele instanceof ToolBarItem) {
				// 这样写是为了可重复使用
				buf.append("pageUI.getViewPart('").append(widgetId).append("').getComponent('").append(getCompId()).append("')");
				buf.append(".getButton('").append(ele.getId()).append("')");
			} else if (ele instanceof PagePartMeta) {
				buf.append("pageUI");
			} else if (ele instanceof Dataset) {
				String dsId = getVarDsShowId(ele.getId(), widgetId);
				buf.append(dsId);
			} else if (ele instanceof ViewPartMeta) {
				ViewPartMeta widget = (ViewPartMeta) ele;
				if (widget.isDialog())
					buf.append("window.pageUI.getDialog('").append(widgetId).append("')");
			} else
				buf.append(getVarShowId());
			buf.append(".$TEMP_").append(listener.getId()).append(" = ").append(listenerShowId).append(";\n");
			if (!(ele instanceof PagePartMeta)) {
				buf.append(listenerShowId).append(".").append(LuiPageContext.SOURCE_ID).append(" = '").append(ele.getId()).append("';\n");
			}
			buf.append(listenerShowId).append(".").append(LuiPageContext.LISTENER_ID).append(" = '").append(listener.getId()).append("';\n");
			if (widgetId != null) {
				buf.append(listenerShowId).append(".").append(LuiPageContext.WIDGET_ID).append(" = '").append(widgetId).append("';\n");
			}
			buf.append(listenerShowId).append(".").append(LuiPageContext.SOURCE_TYPE).append(" = '").append(getSourceType(ele)).append("';\n");
			if (parentId != null) {
				buf.append(listenerShowId).append(".").append(LuiPageContext.PARENT_SOURCE_ID).append(" = '").append(parentId).append("';\n");
			}
			Map<String, LuiEventConf> eventMap = listener.getEventHandlerMap();
			Iterator<LuiEventConf> eit = eventMap.values().iterator();
			while (eit.hasNext()) {
				LuiEventConf jsEvent = eit.next();
				buf.append(listenerShowId).append(".").append(jsEvent.getName()).append(" = function(");
				StringBuffer params = new StringBuffer();
				int pSize = jsEvent.getParamList().size();
				if (jsEvent.getParamList() != null && pSize > 0) {
					for (int i = 0; i < pSize; i++) {
						params.append(jsEvent.getParamList().get(i).getName());
						if (i != pSize - 1)
							params.append(",");
					}
				}
				buf.append(params.toString() + "){\n");
				if (jsEvent.getExtendParam(LuiEventConf.PARAM_DATASET_FIELD_ID) != null) {
					if ("onBeforeDataChange".equals(jsEvent.getName())) { // Dataset的onBeforeDataChange
						generateBeforeDataChangeEventHeadScript(buf, jsEvent, ele, widgetId);
					} else if ("onAfterDataChange".equals(jsEvent.getName())) { // Dataset的onAfterDataChange事件
						generateAfterDataChangeEventHeadScript(buf, jsEvent, ele, widgetId);
					}
				}
				if (listener.getServerClazz() != null && jsEvent.isOnserver()) {
					buf.append(generateServerProxy(widgetId, listenerShowId, ele, listener, jsEvent, parentId));
				} else if (jsEvent.getScript() != null && !jsEvent.getScript().equals("")) {
					buf.append(jsEvent.getScript());
				}
				buf.append("\n};\n");
				generateSubmitRule(listener, listenerShowId, buf, jsEvent, widgetId);
			}
		}
		return buf.toString();
	}
	/**
	 * 生成Dataset的onAfterDataChange事件的JS判断语句
	 * 
	 * @param buf
	 * @param jsEvent
	 * @param ele
	 * @param widgetId
	 */
	private void generateAfterDataChangeEventHeadScript(StringBuffer buf, LuiEventConf jsEvent, LuiElement ele, String widgetId) {
		LuiParameter fieldParam = jsEvent.getExtendParam(LuiEventConf.PARAM_DATASET_FIELD_ID);
		String fields = fieldParam.getDesc();
		buf.append(getVarDsShowId(ele.getId(), widgetId)).append(".afterDataChangeAcceptFields = \"").append(fields).append("\".split(\",\");\n");
	}
	/**
	 * 生成Dataset的onBeforeDataChange事件的JS判断语句
	 * 
	 * @param buf
	 * @param jsEvent
	 * @param ele
	 * @param widgetId
	 */
	private void generateBeforeDataChangeEventHeadScript(StringBuffer buf, LuiEventConf jsEvent, LuiElement ele, String widgetId) {
		LuiParameter fieldParam = jsEvent.getExtendParam(LuiEventConf.PARAM_DATASET_FIELD_ID);
		String fields = fieldParam.getDesc();
		buf.append(getVarDsShowId(ele.getId(), widgetId)).append(".beforeDataChangeAcceptFields = \"").append(fields).append("\".split(\",\");\n");
	}
	protected void generateSubmitRule(LuiListenerConf listener, String listenerShowId, StringBuffer buf, LuiEventConf jsEvent, String widgetId) {
		// 提交规则
		EventSubmitRule submitRule = jsEvent.getSubmitRule();
		if (widgetId != null) {
			if (submitRule == null) {
				submitRule = new EventSubmitRule();
			}
			if (submitRule.getWidgetRule(widgetId) == null) {
				WidgetRule wr = new WidgetRule();
				wr.setId(widgetId);
				submitRule.addWidgetRule(wr);
			}
		}
		if (submitRule != null) {
			String ruleId = "sr_" + jsEvent.getName() + "_" + listener.getId();
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
			String jsScript = createSubmitRule(submitRule, ruleId);
			buf.append(jsScript);
			EventSubmitRule pSubmitRule = submitRule.getParentSubmitRule();
			if (pSubmitRule != null) {
				String pRuleId = ruleId + "_parent";
				buf.append("var " + pRuleId + " = $.submitrule.getObj();\n");
				String pJsScript = createSubmitRule(pSubmitRule, pRuleId);
				buf.append(pJsScript);
				buf.append(ruleId + ".setParentSubmitRule(" + pRuleId + ");\n");
			}
			buf.append(listenerShowId).append(".").append(jsEvent.getName()).append(".submitRule = (").append(ruleId).append(");\n");
		}
	}
	
	protected String generateServerProxy_new(String widgetId, LuiElement ele, LuiListenerConf listener, LuiEventConf jsEvent, String parentId) {
		StringBuffer buf = new StringBuffer();
		String eventName = jsEvent.getName();
		if (eventName.equals("onClosed")) {
			jsEvent.setAsync(false);
		}
		Map<String, Object> listenerMap = new HashMap<String, Object>();
		listenerMap.put("listener_id", listener.getId());
		listenerMap.put("source_type", getSourceType(ele));
		//listenerMap.put("submitRule", ruleId);
		buf.append("var event="+ JSON.toJSONString(listenerMap)+"\n");
		buf.append("if(window.editMode) return;");
		buf.append("var proxy = $.serverproxy.getObj({listener:event,eventName:'" + eventName + "',async:" + jsEvent.isAsync() + "});\n");
		buf.append("if(typeof beforeCallServer != 'undefined')\n").append("beforeCallServer(proxy, '" + listener.getId() + "', '" + eventName + "','" + ele.getId() + "');\n");
		addProxyParam(buf, eventName);
		if (jsEvent.getExtendMap().size() > 0) {
			String[] keys = jsEvent.getExtendMap().keySet().toArray(new String[0]);
			Map<String,Object> param = new HashMap<String,Object>();
			for (int i = 0; i < keys.length; i++) {
				if (keys[i].startsWith(LuiEventConf.SUBMIT_PRE)) {
					Object value = jsEvent.getExtendAttributeValue(keys[i]);
					if (value == null)
						value = "";
					param.put(keys[i].substring(LuiEventConf.SUBMIT_PRE.length()), value.toString());
				}
			}
			buf.append("proxy.addParam(").append(JSON.toJSONString(param)).append(");\n");
		}
		if (!jsEvent.isNmc()) {
			buf.append("proxy.setNmc(false);\n");
		}
		String extendStr = getExtendProxyStr(ele, listener, jsEvent);
		if (extendStr != null)
			buf.append(extendStr);
		// 显示加载提示条
		if ("loginListener".equals(listener.getId()) && "onclick".equals(eventName) && "submitBtn".equals(ele.getId())) {
			buf.append("showLoginLoadingBar();\n");
		} else {
			buf.append("showDefaultLoadingBar();\n");
		}
		buf.append("$.serverproxy.wrapProxy(proxy);\n");
		return buf.toString();
	}
	
	protected String generateServerProxy(String widgetId, String listenerShowId, LuiElement ele, LuiListenerConf listener, LuiEventConf jsEvent, String parentId) {
		StringBuffer buf = new StringBuffer();
		String eventName = jsEvent.getName();
		if (eventName.equals("onClosed")) {
			jsEvent.setAsync(false);
		}
		buf.append("if(window.editMode) return;");
		buf.append("var proxy = $.serverproxy.getObj({listener:this.$TEMP_" + listener.getId() + ",eventName:'" + eventName + "',async:" + jsEvent.isAsync() + "});\n");
		buf.append("if(typeof beforeCallServer != 'undefined')\n").append("beforeCallServer(proxy, '" + listener.getId() + "', '" + eventName + "','" + ele.getId() + "');\n");
		addProxyParam(buf, eventName);
		if (jsEvent.getExtendMap().size() > 0) {
			String[] keys = jsEvent.getExtendMap().keySet().toArray(new String[0]);
			for (int i = 0; i < keys.length; i++) {
				if (keys[i].startsWith(LuiEventConf.SUBMIT_PRE)) {
					buf.append("proxy.addParam('").append(keys[i].substring(LuiEventConf.SUBMIT_PRE.length())).append("', '");
					Object value = jsEvent.getExtendAttributeValue(keys[i]);
					if (value == null)
						value = "";
					buf.append(value.toString()).append("');\n");
				}
			}
		}
		if (!jsEvent.isNmc()) {
			buf.append("proxy.setNmc(false);\n");
		}
		String extendStr = getExtendProxyStr(ele, listener, jsEvent);
		if (extendStr != null)
			buf.append(extendStr);
		// 显示加载提示条
		if ("loginListener".equals(listener.getId()) && "onclick".equals(eventName) && "submitBtn".equals(ele.getId())) {
			buf.append("showLoginLoadingBar();\n");
		} else {
			buf.append("showDefaultLoadingBar();\n");
		}
		buf.append("$.serverproxy.wrapProxy(proxy);\n");
		return buf.toString();
	}
	/**
	 * 增加前台Event的提交参数到后台
	 * 
	 * @param buf
	 *            JS代码字符串
	 * @param eventName
	 *            事件名称
	 */
	protected void addProxyParam(StringBuffer buf, String eventName) {
		if (DatasetEvent.ON_AFTER_DATA_CHANGE.equals(eventName)) {
			buf.append("if(dataChangeEvent.isColSingle == null || dataChangeEvent.isColSingle == false){\n");
			buf.append("proxy.addParam('cellRowIndex', dataChangeEvent.cellRowIndex);\n");
			buf.append("proxy.addParam('cellColIndex', dataChangeEvent.cellColIndex);\n");
			buf.append("proxy.addParam('newValue', dataChangeEvent.currentValue);\n");
			buf.append("proxy.addParam('oldValue', dataChangeEvent.oldValue);\n");
			buf.append("proxy.addParam('isColSingle', 'false');\n");
			buf.append("}else{\n");
			buf.append("proxy.addParam('cellRowIndices', dataChangeEvent.cellRowIndices);\n");
			buf.append("proxy.addParam('cellColIndex', dataChangeEvent.cellColIndex);\n");
			buf.append("proxy.addParam('newValues', dataChangeEvent.currentValues);\n");
			buf.append("proxy.addParam('oldValues', dataChangeEvent.oldValues);\n");
			buf.append("proxy.addParam('isColSingle', 'true');\n");
			buf.append("}\n");
		}
	}
	private String createSubmitRule(EventSubmitRule submitRule, String ruleId) {
		StringBuffer sb = new StringBuffer();
		LuiSet<WidgetRule> widgetRuleMap = submitRule.getWidgetRules();
		if (widgetRuleMap != null && !widgetRuleMap.isEmpty()) {
			Iterator<WidgetRule> it = widgetRuleMap.iterator();
			while (it.hasNext()) {
				WidgetRule widgetRule = it.next();
				String wstr = createWidgetRule(widgetRule);
				sb.append(wstr);
				String widgetId = widgetRule.getId();
				sb.append(ruleId).append(".addViewPartRule('").append(widgetId).append("', wdr_").append(widgetId).append(");\n");
			}
		}
		return sb.toString();
	}
	private String createWidgetRule(WidgetRule widgetRule) {
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
	public String getFormatSize(String size) {
		if (size == null)
			return "0px";
		else if (size.indexOf("%") != -1 || size.indexOf("px") != -1)
			return size;
		else
			return size + "px";
	}
	protected String getVarDsShowId(String dsId, String widgetId) {
		return DS_PRE + widgetId + "_" + dsId;
	}
	public String getWidgetId() {
		return widgetId;
	}
	public void setWidgetId(String widget) {
		this.widgetId = widget;
	}
	public String getCompId() {
		return compId;
	}
	public void setCompId(String id) {
		this.compId = id;
	}
	protected String getExtendProxyStr(LuiElement ele, LuiListenerConf listener, LuiEventConf jsEvent) {
		return "";
	}
	protected abstract String getSourceType(LuiElement ele);
}
