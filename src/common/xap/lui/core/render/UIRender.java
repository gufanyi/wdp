package xap.lui.core.render;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.lang.StringUtils;
import xap.lui.core.builder.LuiSet;
import xap.lui.core.common.LuiRenderContext;
import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.comps.ContextMenuComp;
import xap.lui.core.comps.FormElement;
import xap.lui.core.comps.LuiElement;
import xap.lui.core.comps.MenuItem;
import xap.lui.core.comps.ToolBarItem;
import xap.lui.core.comps.ViewElement;
import xap.lui.core.control.ModePhase;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.LuiParameter;
import xap.lui.core.dataset.Parameter;
import xap.lui.core.event.DatasetEvent;
import xap.lui.core.event.IEventSupport;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.layout.UIElement;
import xap.lui.core.layout.UIElementFinder;
import xap.lui.core.layout.UILayout;
import xap.lui.core.layout.UILayoutPanel;
import xap.lui.core.layout.UIPartMeta;
import xap.lui.core.layout.UIViewPart;
import xap.lui.core.listener.DatasetRule;
import xap.lui.core.listener.EventSubmitRule;
import xap.lui.core.listener.FormRule;
import xap.lui.core.listener.GridRule;
import xap.lui.core.listener.LuiEventConf;
import xap.lui.core.listener.LuiListenerConf;
import xap.lui.core.listener.TreeRule;
import xap.lui.core.listener.WidgetRule;
import xap.lui.core.logger.LuiLogger;
import xap.lui.core.model.AppSession;
import xap.lui.core.model.LuiPageContext;
import xap.lui.core.model.PagePartMeta;
import xap.lui.core.model.ViewPartComps;
import xap.lui.core.model.ViewPartMenus;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.core.util.ClassUtil;
import com.alibaba.fastjson.JSON;
public abstract class UIRender<T extends UIElement, K extends LuiElement> implements ILuiRender, IDynamicAttributes {
	public static final String DIV_INDEX = "$DIV_INDEX";
	// 占位DIV前缀，防止此DIV id和控件实际DIV冲突
	public static final String DIV_PRE = "__d_";
	public static final String COMP_PRE = "__c_";
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
	public static final String PLACEINDIV_KEY = "placeInDiv_key";
	public static final String PLACEINDIV_VALUE = "placeInDiv_value";
	public static final String PLACEINDIV_SCRIPT = "placeInDiv_script";
	public static final String DS_SCRIPT = "dsScript";
	public static final String ALL_SCRIPT = "allScript";
	// JS字符串引号
	public static final String QUOTE = "\"";
	protected String id; // 布局、容器、控件的Id
	protected String divId; // 占位div的id
	protected String varId; // 定义js变量id
	protected String viewId; // view应的Id
	// protected IUIRenderGroup group = null; // 对应的渲染组
	private T uiElement; //
	private K webElement; //
	protected ILuiRender parentRender; // 父渲染器
	// private PagePartMeta pageMeta; // 页面元数据
	// private UIPartMeta uiMeta; // 页面元数据
	private HashMap<String, Object> attributeMap = null; // 渲染器的属性
	protected static final String MIN_HEIGHT = "23px";
	/**
	 * 构造函数
	 * 
	 * @param uiEle
	 * @param webEle
	 */
	public UIRender(K webEle) {
		// uiElement = uiEle;
		if (webEle != null) {
			webElement = webEle;
			// this.id = webEle.getId();
		}
	}
	public UIRender(T uiEle) {
		if (uiEle != null) {
			uiElement = uiEle;
			// this.id=uiEle.getId();
		}
	}
	public UIRender(K webEle, T uiEle) {
		if (webEle != null) {
			webElement = webEle;
			// this.id = webEle.getId();
		}
		if (uiEle != null) {
			uiElement = uiEle;
			// this.id=uiEle.getId();
		}
	}
	@SuppressWarnings("unchecked")
	public T getUiElement() {
		if (uiElement == null) {
			uiElement = (T) UIElementFinder.findElementById(LuiRenderContext.current().getUiPartMeta(), webElement.getId());
		}
		return uiElement;
	}
	public void destroyUiElement() {
		this.uiElement = null;
	}
	@SuppressWarnings("unchecked")
	public K getWebElement() {
		return webElement;
	}
	public String getDivId() {
		return divId;
	}
	public void setDivId(String divId) {
		this.divId = divId;
	}
	private UIPartMeta getTargetUiMeta() {
		UIPartMeta uiMeta = LuiRenderContext.current().getUiPartMeta();
		if (this.viewId != null && (!this.viewId.equals("") && !this.viewId.equals("pagemeta")) && uiMeta != null) {
			UIViewPart uiw = (UIViewPart) UIElementFinder.findElementById(uiMeta, this.viewId);
			if (uiw == null) {
				uiw = uiMeta.getDialog(this.viewId);
			}
			if (uiw != null) {
				return uiw.getUimeta();
			}
			return null;
		}
		return uiMeta;
	}
	/**
	 * 2011-7-29 下午04:47:48 renxh des：varId为该控件js的定义的变量名
	 * 
	 * @return
	 */
	public String getVarId() {
		if (varId == null) {
			if (getCurrWidget() == null)
				varId = COMP_PRE + getId();
			else
				varId = COMP_PRE + getCurrWidget().getId() + "_" + getId();
		}
		return varId;
	}
	public void setVarId(String varId) {
		this.varId = varId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getViewId() {
		return viewId;
	}
	public void setViewId(String viewId) {
		this.viewId = viewId;
	}
	public ILuiRender getParentRender() {
		return parentRender;
	}
	public void setParentRender(ILuiRender parentRender) {
		this.parentRender = parentRender;
	}
	public Object getAttribute(String key) {
		if (attributeMap != null && attributeMap.containsKey(key))
			return attributeMap.get(key);
		return "";
	}
	public void setAttribute(String key, Object value) {
		if (attributeMap == null)
			attributeMap = new HashMap<String, Object>();
		attributeMap.put(key, value);
	}
	public void removeAttribute(String key) {
		if (attributeMap == null)
			return;
		if (attributeMap.containsKey(key))
			attributeMap.remove(key);
	}
	/**
	 * 进行多语翻译,如果不能翻译,返回原i18nName
	 * 
	 * @param i18nName
	 * @return
	 */
	public String translate(String i18nName, String defaultI18nName, String langDir) {
		return defaultI18nName;
	}
	/**
	 * 2011-7-26 下午02:56:44 renxh des：根据key获得唯一的id, 当id为空时，可以用此方法获得Id
	 * 
	 * @param key
	 * @return
	 */
	public String getUniqueId(String key) {
		String id = (String) LuiRuntimeContext.getWebContext().getAttribute(key);
		if (id == null) {
			id = "0";
		} else {
			id = String.valueOf(Integer.parseInt(id) + 1);
		}
		LuiRuntimeContext.getWebContext().setAttribute(key, id);
		return id;
	}
	/**
	 * 2011-7-26 下午02:57:01 renxh des：设置上下文属性，全局
	 * 
	 * @param key
	 * @param obj
	 */
	public void setContextAttribute(String key, Object obj) {
		LuiRuntimeContext.getWebContext().setAttribute(key, obj);
	}
	/**
	 * 2011-7-26 下午02:57:26 renxh des：获得上下文属性，全局
	 * 
	 * @param key
	 * @return
	 */
	public Object getContextAttribute(String key) {
		return LuiRuntimeContext.getWebContext().getAttribute(key);
	}
	/**
	 * 2011-7-26 下午02:57:39 renxh des：删除上下文属性 全局
	 * 
	 * @param key
	 */
	public void removeContextAttribute(String key) {
		LuiRuntimeContext.getWebContext().removeAttribute(key);
	}
	/**
	 * 2011-7-29 下午04:50:56 renxh des：添加事件支持
	 * 
	 * @param element
	 *            web元素
	 * @param widgetId
	 *            片段Id
	 * @param showId
	 *            js变量的id
	 * @param parentId
	 *            父Id
	 * @return
	 */
	protected String addEventSupport(IEventSupport element, String widgetId, String showId, String parentId) {
		StringBuilder buf = new StringBuilder("");
		// 获得控件暴露的事件map
		List<LuiListenerConf> listeners = new ArrayList<LuiListenerConf>();
		Collection<LuiListenerConf> eventConfListeners = Controller2Event(element.getEventConfs(), element);
		if (eventConfListeners != null)
			listeners.addAll(eventConfListeners);
		if (listeners == null || listeners.isEmpty())
			return "";
		for (LuiListenerConf listener : listeners) {
			LuiEventConf event = listener.getEventHandler("valueChanged");
			if (event != null) {
				if (StringUtils.equals("validate_method", event.getMethod()) || StringUtils.equals("editor_method", event.getMethod())) {
					continue;
				}
			}
			String listenerJsId = null;
			if (widgetId != null)
				listenerJsId = LISTENER_PRE + widgetId + "_" + listener.getId();
			else
				listenerJsId = LISTENER_PRE + listener.getId();
			String listenerStr = generateListener(listener, listenerJsId, widgetId, element, parentId);
			buf.append(listenerStr);
		}
		return buf.toString();
	}
	/**
	 * 将Controller转换为js监听器配置
	 * 
	 * @param events
	 * @return
	 */
	private Collection<LuiListenerConf> Controller2Event(LuiEventConf[] events, IEventSupport element) {
		if (events != null && events.length > 0) {
			Map<String, LuiListenerConf> jsListeners = new HashMap<String, LuiListenerConf>();
			PagePartMeta ppm = LuiRenderContext.current().getPagePartMeta();
			for (LuiEventConf event : events) {
				String jsEventClazz = event.getEventType();
				if (jsEventClazz.endsWith("null"))
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
					if (we.getWidget() != null) {
						String trueId = null;
						if (element instanceof ViewPartMeta) {
							trueId = we.getId();
						} else {
							trueId = we.getWidget().getId();
						}
						parentCtrl = ppm.getWidget(trueId).getController();
						el = AppSession.EVENT_LEVEL_VIEW;
					} else {
						if (ppm.getWidget(we.getId()) != null) {
							parentCtrl = ppm.getWidget(we.getId()).getController();
						}
					}
				}
				if (element instanceof PagePartMeta) {
					PagePartMeta we = (PagePartMeta) element;
					parentCtrl = we.getController();
					el = AppSession.EVENT_LEVEL_WIN;
				}
				if (element instanceof UIElement) {
					UIElement ue = (UIElement) element;
					String widgetId = ue.getViewId();
					if (widgetId == null) {
						parentCtrl = ppm.getController();
						el = AppSession.EVENT_LEVEL_WIN;
					} else {
						parentCtrl = ppm.getWidget(widgetId).getController();
						el = AppSession.EVENT_LEVEL_VIEW;
					}
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
		return null;
	}
	/**
	 * 2011-7-29 下午04:51:25 renxh des：生成监听器
	 * 
	 * @param listener
	 *            监听
	 * @param listenerJsId
	 *            监听对应的变量id
	 * @param widgetId
	 *            片段Id
	 * @param ele
	 *            web元素
	 * @param parentId
	 *            父id
	 * @return
	 */
	protected String generateListener(LuiListenerConf listener, String listenerJsId, String widgetId, IEventSupport ele, String parentId) {
		StringBuilder buf = new StringBuilder();
		Map<String, LuiEventConf> eventMap = listener.getEventHandlerMap();
		String id = "";
		if (ele instanceof MenuItem) {
			id = this.getVarId() + ele.getId();
		} else if (ele instanceof ToolBarItem) {
			buf.append("var ").append(listenerJsId).append(" = ");
			buf.append("pageUI.getViewPart('").append(widgetId).append("').getComponent('").append(getId()).append("')");
			buf.append(".getButton('").append(ele.getId()).append("');");
			id = listenerJsId;
		} else if (ele instanceof Dataset) {
			String dsId = getDatasetVarShowId(ele.getId(), widgetId);
			id = dsId;
		} else if (ele instanceof PagePartMeta) {
			id = "pageUI";
		} else if (ele instanceof FormElement) {
			id = COMP_PRE + ele.getId() + "_ele";
		} else {
			id = this.getVarId();
		}
		Iterator<LuiEventConf> eit = eventMap.values().iterator();
		while (eit.hasNext()) {
			LuiEventConf jsEvent = eit.next();
			buf.append(id + ".element.on('" + ele.getWidgetName() + jsEvent.getName().toLowerCase() + "',function(");
			StringBuilder params = new StringBuilder();
			params.append("e");
			String jsParam = jsEvent.getJsParam();
			if (StringUtils.isNotBlank(jsParam)) {
				params.append(",").append(jsParam);
			}
			buf.append(params.toString());
			buf.append("){\n");
			if (listener.getServerClazz() != null && jsEvent.isOnserver()) {
				buf.append(generateServerProxy_new(widgetId, ele, listener, jsEvent, parentId));
			} else if (!jsEvent.isOnserver() && !StringUtils.isEmpty(jsEvent.getScript())) {
				buf.append(jsEvent.getScript());
			}
			buf.append("});");
		}
		return buf.toString();
	}
	/**
	 * 2011-7-29 下午04:51:43 renxh des： 生成提交规则
	 * 
	 * @param listener
	 * @param listenerShowId
	 * @param buf
	 * @param jsEvent
	 * @param widgetId
	 */
	protected void generateSubmitRule(LuiListenerConf listener, String listenerShowId, StringBuilder buf, LuiEventConf jsEvent, String widgetId) {
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
			String jsScript = createJsSubmitRule(submitRule, ruleId);
			buf.append(jsScript);
			EventSubmitRule pSubmitRule = submitRule.getParentSubmitRule();
			if (pSubmitRule != null) {
				String pRuleId = ruleId + "_parent";
				buf.append("var " + pRuleId + " = $.submitrule.getObj();\n");
				String pJsScript = createJsSubmitRule(pSubmitRule, pRuleId);
				buf.append(pJsScript);
				buf.append(ruleId + ".setParentSubmitRule(" + pRuleId + ");\n");
			}
			buf.append(listenerShowId).append(".").append(jsEvent.getName()).append(".submitRule = (").append(ruleId).append(");\n");
		}
	}
	/**
	 * 2011-7-29 下午04:51:43 renxh des： 生成提交规则
	 * 
	 * @param listener
	 * @param listenerShowId
	 * @param buf
	 * @param jsEvent
	 * @param widgetId
	 */
	protected String generateSubmitRule(LuiListenerConf listener, StringBuilder buf, LuiEventConf jsEvent, String widgetId) {
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
			String jsScript = createJsSubmitRule(submitRule, ruleId);
			buf.append(jsScript);
			EventSubmitRule pSubmitRule = submitRule.getParentSubmitRule();
			if (pSubmitRule != null) {
				String pRuleId = ruleId + "_parent";
				buf.append("var " + pRuleId + " = $.submitrule.getObj();\n");
				String pJsScript = createJsSubmitRule(pSubmitRule, pRuleId);
				buf.append(pJsScript);
				buf.append(ruleId + ".setParentSubmitRule(" + pRuleId + ");\n");
			}
			return ruleId;
		}
		return null;
	}
	/**
	 * 2011-7-29 下午04:51:58 renxh des：创建js的提交规则
	 * 
	 * @param submitRule
	 * @param ruleId
	 * @return
	 */
	protected String createJsSubmitRule(EventSubmitRule submitRule, String ruleId) {
		StringBuilder sb = new StringBuilder();
		LuiSet<WidgetRule> widgetRuleMap = submitRule.getWidgetRules();
		if (widgetRuleMap != null && !widgetRuleMap.isEmpty()) {
			Iterator<WidgetRule> it = widgetRuleMap.iterator();
			while (it.hasNext()) {
				WidgetRule widgetRule = it.next();
				String wstr = getWidgetRule(widgetRule);
				sb.append(wstr);
				String widgetId = widgetRule.getId();
				sb.append(ruleId).append(".addViewPartRule('").append(widgetId).append("', wdr_").append(widgetId).append(");\n");
			}
		}
		return sb.toString();
	}
	/**
	 * 2011-7-29 下午04:52:10 renxh 得到片段的规则
	 * 
	 * @param widgetRule
	 * @return
	 */
	protected String getWidgetRule(WidgetRule widgetRule) {
		StringBuilder buf = new StringBuilder();
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
	 * 2011-7-29 下午04:52:39 renxh des：渲染服务代理
	 * 
	 * @param widgetId
	 * @param listenerShowId
	 * @param ele
	 * @param listener
	 * @param jsEvent
	 * @param parentId
	 * @return
	 */
	protected String generateServerProxy_old(String widgetId, String listenerShowId, IEventSupport ele, LuiListenerConf listener, LuiEventConf jsEvent, String parentId) {
		StringBuilder buf = new StringBuilder();
		String eventName = jsEvent.getName();
		// for dialog close
		if (eventName.equals("onclose")) {
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
			// 为解决点击关闭和保存的时候source_type为null而添加，直接设置
			buf.append("proxy.addParam('").append(LuiPageContext.SOURCE_TYPE).append("','").append(getSourceType(ele)).append("');\n");
			buf.append("proxy.addParam('").append(LuiPageContext.EVENT_NAME).append("','").append(eventName).append("');\n");
		}
		if (!jsEvent.isNmc()) {
			buf.append("proxy.setNmc(false);\n");
		}
		String extendStr = getExtendProxyStr(ele, listener, jsEvent);
		if (extendStr != null)
			buf.append(extendStr);
		// 显示加载提示条
		// buf.append("showDefaultLoadingBar();\n");
		//
		// buf.append("return proxy.execute();\n");
		buf.append("$.serverproxy.wrapProxy(proxy);\n");
		return buf.toString();
	}
	/**
	 * 2015-6-3 下午04:52:39 renxh des：渲染服务代理
	 * 
	 * @param widgetId
	 * @param listenerShowId
	 * @param ele
	 * @param listener
	 * @param jsEvent
	 * @param parentId
	 * @return
	 */
	protected String generateServerProxy_new(String widgetId, IEventSupport ele, LuiListenerConf listener, LuiEventConf jsEvent, String parentId) {
		StringBuilder buf = new StringBuilder();
		String eventName = jsEvent.getName();
		// for dialog close
		if (eventName.equals("onclose")) {
			jsEvent.setAsync(false);
		}
		String ruleId = generateSubmitRule(listener, buf, jsEvent, widgetId);
		Map<String, Object> listenerMap = new HashMap<String, Object>();
		listenerMap.put("source_id", ele.getId());
		listenerMap.put("widget_id", widgetId);
		listenerMap.put("source_type", getSourceType(ele));
		listenerMap.put("parent_source_id", parentId);
		// listenerMap.put("submitRule", ruleId);
		buf.append("var event=" + JSON.toJSONString(listenerMap) + "\n");
		buf.append("event." + eventName + "={};");
		buf.append("event." + eventName + "['submitRule']=" + ruleId + "\n");
		buf.append("$.serverproxy.suspend = true;\n if(window.editMode) return;");
		buf.append("var proxy = $.serverproxy.getObj({listener:event,eventName:'" + eventName + "',async:" + jsEvent.isAsync() + "});\n");
		buf.append("if(typeof beforeCallServer != 'undefined')\n").append("beforeCallServer(proxy, '" + listener.getId() + "', '" + eventName + "','" + ele.getId() + "');\n");
		addProxyParam(buf, eventName);
		if (jsEvent.getExtendMap().size() > 0) {
			String[] keys = jsEvent.getExtendMap().keySet().toArray(new String[0]);
			Map<String, Object> param = new HashMap<String, Object>();
			for (int i = 0; i < keys.length; i++) {
				if (keys[i].startsWith(LuiEventConf.SUBMIT_PRE)) {
					Object value = jsEvent.getExtendAttributeValue(keys[i]);
					if (value == null)
						value = "";
					param.put(keys[i].substring(LuiEventConf.SUBMIT_PRE.length()), value.toString());
				}
			}
			// 为解决点击关闭和保存的时候source_type为null而添加，直接设置
			param.put(LuiPageContext.SOURCE_TYPE, getSourceType(ele));
			param.put(LuiPageContext.EVENT_NAME, eventName);
			buf.append("proxy.addParam(").append(JSON.toJSONString(param)).append(");\n");
		}
		if (!jsEvent.isNmc()) {
			buf.append("proxy.setNmc(false);\n");
		}
		String extendStr = getExtendProxyStr(ele, listener, jsEvent);
		if (extendStr != null)
			buf.append(extendStr);
		buf.append("$.serverproxy.wrapProxy(proxy);\n");
		return buf.toString();
	}
	/**
	 * 2011-7-29 下午04:53:08 renxh des：获得扩展得代理字符串
	 * 
	 * @param ele
	 * @param listener
	 * @param jsEvent
	 * @return
	 */
	protected String getExtendProxyStr(IEventSupport ele, LuiListenerConf listener, LuiEventConf jsEvent) {
		return null;
	}
	/**
	 * 增加前台Event的提交参数到后台
	 * 
	 * @param buf
	 *            JS代码字符串
	 * @param eventName
	 *            事件名称
	 */
	protected void addProxyParam(StringBuilder buf, String eventName) {
		if (DatasetEvent.ON_AFTER_DATA_CHANGE.equals(eventName)) {
			buf.append("proxy.addParam('cellRowIndex', datasetCellEvent.cellRowIndex);\n");
			buf.append("proxy.addParam('cellColIndex', datasetCellEvent.cellColIndex);\n");
			buf.append("proxy.addParam('newValue', datasetCellEvent.currentValue);\n");
			buf.append("proxy.addParam('oldValue', datasetCellEvent.oldValue);\n");
		}
		if(DatasetEvent.ON_AFTER_ROW_UN_SELECT.equals(eventName)){
			buf.append("proxy.addParam('currentRowIndex', rowUnSelectEvent.currentRowIndex);\n");
		}
	}
	protected void addAppParam(StringBuilder buf, String eventName, LuiEventConf jsEvent) {
		// buf.append("proxy.addParam('cellRowIndex',
		// dataChangeEvent.cellRowIndex);\n");
		// buf.append("proxy.addParam('cellColIndex',
		// dataChangeEvent.cellColIndex);\n");
		// buf.append("proxy.addParam('newValue',
		// dataChangeEvent.currentValue);\n");
	}
	/**
	 * 生成Dataset的onAfterDataChange事件的JS判断语句
	 * 
	 * @param buf
	 * @param jsEvent
	 * @param ele
	 * @param widgetId
	 */
	private void generateAfterDataChangeEventHeadScript(StringBuilder buf, LuiEventConf jsEvent, IEventSupport ele, String widgetId) {
		LuiParameter fieldParam = jsEvent.getExtendParam(LuiEventConf.PARAM_DATASET_FIELD_ID);
		String fields = fieldParam.getDesc();
		buf.append(getDatasetVarShowId(ele.getId(), widgetId)).append(".afterDataChangeAcceptFields = \"").append(fields).append("\".split(\",\");\n");
	}
	/**
	 * 生成Dataset的onBeforeDataChange事件的JS判断语句
	 * 
	 * @param buf
	 * @param jsEvent
	 * @param ele
	 * @param widgetId
	 */
	private void generateBeforeDataChangeEventHeadScript(StringBuilder buf, LuiEventConf jsEvent, IEventSupport ele, String widgetId) {
		LuiParameter fieldParam = jsEvent.getExtendParam(LuiEventConf.PARAM_DATASET_FIELD_ID);
		String fields = fieldParam.getDesc();
		buf.append(getDatasetVarShowId(ele.getId(), widgetId));
		buf.append(".beforeDataChangeAcceptFields = \"");
		buf.append(fields).append("\".split(\",\");\n");
	}
	/**
	 * 2011-8-16 下午03:33:55 renxh des：生成数据集的js变量
	 * 
	 * @param dsId
	 * @param widgetId
	 * @return
	 */
	protected String getDatasetVarShowId(String dsId, String widgetId) {
		return DS_PRE + widgetId + "_" + dsId;
	}
	/**
	 * 返回带单位的高度或宽度
	 * 
	 * @param size
	 *            : 高度或宽度
	 * @return
	 */
	public String getFormatSize(String size) {
		if (size == null)
			return "0px";
		else if (size.indexOf("%") != -1 || size.indexOf("px") != -1 || size.equals("auto"))
			return size;
		else
			return size + "px";
	}
	/**
	 * 添加右键菜单
	 * 
	 * @param menuId
	 * @param id
	 * @return
	 */
	protected String addContextMenu(String menuId, String id) {
		if (this.isEditMode())
			return "";
		String menuShowId = COMP_PRE + getViewId() + "_" + menuId;
		StringBuilder buf = new StringBuilder();
		buf.append(createMenuRender(menuId));
		buf.append(id).append(".setContextMenu(").append(menuShowId).append(");\n");
		// 解决ContextMenu上的ContextMenu叠加问题
		buf.append(menuShowId).append(".addZIndex();\n");
		return wrapByRequired("contextmenu", buf.toString());
	}
	/**
	 * 2011-7-29 下午04:53:32 renxh des： 获得当前的片段
	 * 
	 * @return
	 */
	public ViewPartMeta getCurrWidget() {
		PagePartMeta ppm = LuiRenderContext.current().getPagePartMeta();
		if (ppm == null)
			return null;
		if (this.getViewId() == null) {
			ViewPartMeta[] widgets = ppm.getWidgets();
			int doubleId = 0;
			ViewPartMeta ownerWidget = null;
			if (widgets != null && widgets.length > 0) {
				for (int i = 0; i < widgets.length; i++) {
					ViewPartComps views = widgets[i].getViewComponents();
					ViewPartMenus contextMenus = widgets[i].getViewMenus();
					if (views.getComponent(getId()) != null) {
						doubleId++;
					}
					if (contextMenus.getContextMenu(getId()) != null)
						doubleId++;
					if (contextMenus.getMenuBar(getId()) != null)
						doubleId++;
					if (doubleId >= 2)
						throw new LuiRuntimeException("组件id和已有组件id重复,id=" + getId());
					else if (doubleId == 1 && ownerWidget == null)
						ownerWidget = widgets[i];
				}
				if (doubleId == 0) {
					return null;
				}
			}
			this.setViewId(ownerWidget.getId());
			return ownerWidget;
		} else {
			return ppm.getWidget(this.getViewId());
		}
	}
	/**
	 * 创建Menu的渲染器
	 * 
	 * @param menuId
	 */
	protected String createMenuRender(String menuId) {
		ViewPartMeta currWidget = this.getCurrWidget();
		if (currWidget == null) {
			throw new LuiRuntimeException("无法获得当前的 widget");
		}
		ContextMenuComp ctxMenu = currWidget.getViewMenus().getContextMenu(menuId);
		// if (ctxMenu!=null&&(!ctxMenu.isRendered())) {
		if (ctxMenu != null) {
			PCContextMenuCompRender render = ctxMenu.getRender();
			return render.create();
		}
		return "";
	}
	/**
	 * 2011-8-4 下午03:51:38 renxh des：判断是否为可编辑状态
	 * 
	 * @return
	 */
	public boolean isEditMode() {
		return LuiRuntimeContext.isEditMode();
	}
	// 这个增加了对编辑模式的处理
	public String getNewDivId() {
		if (isEditMode()) {
			return this.getDivId() + "_raw";
		}
		return this.getDivId();
	}
	/**
	 * 2011-8-2 下午07:02:06 renxh des：编辑态时，需加入此div
	 * 
	 * @return
	 */
	public String placeDesign() {
		if (isEditMode()) {
			StringBuilder buf = new StringBuilder("");
			buf.append("var ").append(getDivId()).append(" = $('<div>').attr('id','").append(getDivId()).append("').css({\n");
			buf.append("'position':'relative',\n");
			buf.append("'height':'100%'});\n");
			if (isFlowMode()) {
				buf.append(getDivId()).append(".css('min-height','").append(MIN_HEIGHT).append("').attr('flowmode',true);\n");
			}
			return buf.toString();
		}
		return "";
	}
	public String createDesignHead() {
		if (!isFlowMode()) {
			if (this.isEditMode() && getDivId() != null) {
				return toResize(getDivId(), "editableDivResize");
			}
		}
		return "";
	}
	/**
	 * 调用方法 生成 重新布局的脚本
	 * 
	 * @param obj
	 * @param func
	 * @return
	 */
	protected String toResize(String obj, String func) {
		StringBuilder buf = new StringBuilder();
		buf.append("\nif(" + func + "){\n");
		buf.append("$(window).on('resize',function(e){");
		buf.append(func).append(".call(").append(obj).append(",e);");
		buf.append("});");
		buf.append("}else{");
		buf.append("setTimeout(function(){");
		buf.append("$(window).on('resize',function(e){");
		buf.append(func).append(".call(").append(obj).append(",e);");
		buf.append("});");
		buf.append("},200);\n");
		buf.append("};\n");
		buf.append("$(window).triggerHandler(\"resize\");");
		return buf.toString();
	}
	protected boolean isGenEditableTail() {
		String oriWidgetId = LuiRuntimeContext.getWebContext().getPageWebSession().getOriginalParameter("viewId");
		String widgetId = this.getViewId() == null ? "" : this.getViewId();
		if (StringUtils.isBlank(oriWidgetId)) {
			if (LuiRuntimeContext.getModePhase().equals(ModePhase.persona)) {
				return false;
			}
			if (StringUtils.isBlank(widgetId)) {
				return false;
			}
		}
		return this.getViewId() != null && (LuiRuntimeContext.isWindowEditorMode() || !widgetId.equals(oriWidgetId));
	}
	/**
	 * 2011-8-4 下午06:52:23 renxh des：渲染编辑态 尾 脚本
	 * 
	 * @return
	 */
	public String createDesignTail() {
		String widgetId = this.getViewId() == null ? "" : this.getViewId();
		if (isEditMode()) {
			if (isGenEditableTail()) {
				return "";
			}
			String uiid = null;
			String subuiid = null;
			String eleid = null;
			String subeleid = null;
			if (uiElement instanceof UILayoutPanel) {
				if (uiElement != null) {
					UILayoutPanel panel = (UILayoutPanel) uiElement;
					UILayout parent = panel.getLayout();
					if (parent != null)
						uiid = parent.getId();
					subuiid = uiElement.getId();
				}
				if (webElement != null && parentRender != null) {
					LuiElement parentEle = ((UIRender) parentRender).webElement;
					eleid = parentEle.getId();
					subeleid = webElement.getId();
				}
			} else {
				uiid = uiElement == null ? "" : (String) uiElement.getId();
				eleid = webElement == null ? "" : webElement.getId();
			}
			String type = this.getRenderType(webElement);
			if (uiElement instanceof UIViewPart)
				type = LuiPageContext.SOURCE_TYPE_WIDGT;
			if (type == null)
				type = "";
			StringBuilder buf = new StringBuilder();
			if (getDivId() == null) {
				LuiLogger.error("div id is null!" + this.getClass().getName());
			} else {
				buf.append(this.addEditableListener(getDivId(), widgetId, uiid, subuiid, eleid, subeleid, type));
			}
			return buf.toString();
		}
		return "";
	}
	/**
	 * 2011-9-14 上午10:18:37 renxh des：为布局添加可编辑的样式，点击以后变成可编辑样式
	 * 
	 * @param divId
	 * @param widgetId
	 * @param uiId
	 * @param eleId
	 * @param type
	 * @return
	 */
	protected String addEditableListener(String divId, String widgetId, String uiId, String subuiId, String eleId, String subEleId, String type) {
		StringBuilder buf = new StringBuilder();
		buf.append("var params = {");
		buf.append("widgetid:'").append(widgetId).append("'");
		buf.append(",uiid:'").append(uiId).append("'");
		buf.append(",subuiid:'").append(subuiId).append("'");
		buf.append(",eleid:'").append(eleId).append("'");
		buf.append(",subeleid:'").append(subEleId).append("'");
		buf.append(",type:'").append(type).append("'");
		buf.append("};\n");
		buf.append("$.design.getObj({divObj:$('#" + getDivId() + "')[0],params:params,objType:'layout'});\n");
		buf.append(addDragableListener(getDivId()));
		return buf.toString();
	}
	protected String addDragableListener(String divId) {
		StringBuilder buf = new StringBuilder();
		buf.append("$.draglistener.getObj($('#" + divId + "')[0]);");
		return buf.toString();
	}
	/**
	 * add renxh
	 * 
	 * @param buf
	 *            当执行完动态修改以后，需要执行此方法进行重新布局，如果涉及到数据集的脚本操作，也会在这里执行
	 *            对于动态调价带数据集的需要执行此方法，而只有panel中会添加 此方法在所有脚本的末尾执行
	 */
	protected void dynamicScriptTail(StringBuilder buf) {
		// 下面的代码以后得抽出来
		StringBuilder buft = (StringBuilder) this.getContextAttribute(DS_SCRIPT);
		if (buft != null) {
			String script = buft.toString();
			buft.delete(0, buft.length());
			buf.append(script);
		}
		buf.append("\n$(window).triggerHandler('resize');\n");
		buf.append("window.isRenderDone = true;\n");
	}
	/**
	 * 向叶面中添加动态脚本
	 * 
	 * @param script
	 */
	public void addDynamicScript(String script) {
		if (script == null || script.equals(""))
			return;
		AppSession.current().getAppContext().addExecScript(script);
	}
	/**
	 * 在setContext之前执行的脚本
	 * 
	 * @param script
	 */
	public void addBeforeExeScript(String script) {
		if (script == null || script.equals(""))
			return;
		AppSession.current().getAppContext().addBeforeExecScript(script);
	}
	/**
	 * 2011-8-16 下午03:42:56 renxh des：获得web元素的元素类型
	 * 
	 * @param ele
	 * @return
	 */
	protected abstract String getSourceType(IEventSupport ele);
	/**
	 * 定义渲染器类型，默认为sourceType类型
	 * 
	 * @param ele
	 * @return
	 */
	public String getRenderType(LuiElement ele) {
		return this.getSourceType(ele);
	}
	protected boolean isFlowMode() {
		UIPartMeta um = getTargetUiMeta();
		if (um != null) {
			Boolean flowMode = um.getFlowmode();
			return flowMode.booleanValue();
		}
		return false;
	}
	/**
	 * 按需加载js library
	 * 
	 * @param id
	 * @param func
	 * @return
	 */
	protected String wrapByRequired(String id, String func) {
		return func;
	}
	// public String getType() {
	// return "";
	// }
}
