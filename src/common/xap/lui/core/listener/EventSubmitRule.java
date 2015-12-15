package xap.lui.core.listener;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import xap.lui.core.adapter.MapParamsAdapter;
import xap.lui.core.builder.LuiHashSet;
import xap.lui.core.builder.LuiSet;
import xap.lui.core.dataset.Parameter;
/**
 * 事件后台提交规则
 * 
 * @author guoweic
 *
 */
@XmlRootElement(name = "SubmitRule")
@XmlAccessorType(XmlAccessType.NONE)
public class EventSubmitRule implements Cloneable, Serializable {
	private static final long serialVersionUID = 1762817124593849777L;
	// Widget的规则集合，值为Dataset提交类型集合
	// private Map<String, WidgetRule> widgetRules = new HashMap<String,
	// WidgetRule>();
	@XmlAttribute
	private String id;
	
	@XmlAttribute
	private String pageId;
	
	@XmlElement(name = "ViewPartRule")
	LuiSet<WidgetRule> widgetRulesList = new LuiHashSet<WidgetRule>();
	// 父的提交规则
	@XmlElement(name = "ParentSubmitRule")
	private EventSubmitRule parentSubmitRule = null;
	// 参数集合
	@XmlElement(name = "Params")
	@XmlJavaTypeAdapter(MapParamsAdapter.class)
	private Map<String, Parameter> params = new HashMap<String, Parameter>();
	// Pagemeta的Card是否提交
	@XmlAttribute
	private boolean cardSubmit = false;
	// 所属Pagemeta的ID
	@XmlAttribute
	private String pagemeta;
	public Object clone() {
		EventSubmitRule eventSubmitRule = null;
		try {
			eventSubmitRule = (EventSubmitRule) super.clone();
			for (WidgetRule inner : this.widgetRulesList) {
				eventSubmitRule.getWidgetRules().add((WidgetRule) inner.clone());
			}
			if (this.params != null) {
				for (String name : this.params.keySet()) {
					eventSubmitRule.addParam((Parameter) this.params.get(name).clone());
				}
			}
			if (this.parentSubmitRule != null){
				eventSubmitRule.setParentSubmitRule((EventSubmitRule) this.parentSubmitRule.clone());
			}
		} catch (CloneNotSupportedException e) {}
		return eventSubmitRule;
	}
	public LuiSet<WidgetRule> getWidgetRules() {
		return this.widgetRulesList;
	}
	public WidgetRule getWidgetRule(String id) {
		return this.widgetRulesList.find(id);
	}
	public void setWidgetRules(LuiSet<WidgetRule> widgetRules) {
		this.widgetRulesList = widgetRules;
	}
	/**
	 * 增加WidgetRule
	 * 
	 * @param widgetId
	 */
	public void addWidgetRule(WidgetRule widgetRule) {
		this.widgetRulesList.add(widgetRule);
	}
	/**
	 * 增加参数
	 * 
	 * @param name
	 * @param value
	 */
	public void addParam(Parameter param) {
		params.put(param.getName(), param);
	}
	public Map<String, Parameter> getParamMap() {
		return params;
	}
	public Parameter[] getParams() {
		return params.values().toArray(new Parameter[0]);
	}
	public void setParams(Map<String, Parameter> params) {
		this.params = params;
	}
	public LuiSet<WidgetRule> getWidgetRulesList() {
		return widgetRulesList;
	}
	public void setWidgetRulesList(LuiSet<WidgetRule> widgetRulesList) {
		this.widgetRulesList = widgetRulesList;
	}
	public Parameter getParam(String id) {
		return params.get(id);
	}
	public EventSubmitRule getParentSubmitRule() {
		return parentSubmitRule;
	}
	public void setParentSubmitRule(EventSubmitRule parentSubmitRule) {
		this.parentSubmitRule = parentSubmitRule;
	}
	public boolean isCardSubmit() {
		return cardSubmit;
	}
	public void setCardSubmit(boolean cardSubmit) {
		this.cardSubmit = cardSubmit;
	}
	public String getPagemeta() {
		return pagemeta;
	}
	public void setPagemeta(String pagemeta) {
		this.pagemeta = pagemeta;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPageId() {
		return pageId;
	}
	public void setPageId(String pageId) {
		this.pageId = pageId;
	}
}
