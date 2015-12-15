package xap.lui.core.event;
import java.util.HashMap;
import java.util.Map;

import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.LuiParameter;
import xap.lui.core.listener.EventSubmitRule;
import xap.lui.core.listener.LuiEventConf;
public class DatasetEvent extends AbstractServerEvent<Dataset> {
	public static final String ON_DATA_LOAD = "onDataLoad";
	public static final String ON_AFTER_PAGE_CHANGE = "onAfterPageChange";
	public static final String ON_BEFORE_PAGE_CHANGE = "onBeforePageChange";
	public static final String ON_AFTER_ROW_DELETE = "onAfterRowDelete";
	public static final String ON_BEFORE_ROW_DELETE = "onBeforeRowDelete";
	public static final String ON_AFTER_DATA_CHANGE = "onAfterDataChange";
	public static final String ON_BEFORE_DATA_CHANGE = "onBeforeDataChange";
	public static final String ON_AFTER_ROW_INSERT = "onAfterRowInsert";
	public static final String ON_BEFORE_ROW_INSERT = "onBeforeRowInsert";
	public static final String ON_AFTER_ROW_UN_SELECT = "onAfterRowUnSelect";
	public static final String ON_AFTER_ROW_SELECT = "onAfterRowSelect";
	public static final String ON_BEFORE_ROW_SELECT = "onBeforeRowSelect";
	
	public static final Map<String,String> JSPARAM = new HashMap<String,String>();
	static {
		JSPARAM.put(ON_DATA_LOAD, "dataLoadEvent");
		JSPARAM.put(ON_AFTER_PAGE_CHANGE, "pageChangeEvent");
		JSPARAM.put(ON_BEFORE_PAGE_CHANGE, "dsBeforePageChangeEvent");
		JSPARAM.put(ON_AFTER_ROW_DELETE, "rowDeleteEvent");
		JSPARAM.put(ON_BEFORE_ROW_DELETE, "dsBeforeRowDeleteEvent");
		JSPARAM.put(ON_AFTER_DATA_CHANGE, "datasetCellEvent");
		JSPARAM.put(ON_BEFORE_DATA_CHANGE, "dsBeforeDataChangeEvent");
		JSPARAM.put(ON_AFTER_ROW_INSERT, "rowInsertEvent");
		JSPARAM.put(ON_BEFORE_ROW_INSERT, "dsBeforeRowInsertEvent");
		JSPARAM.put(ON_AFTER_ROW_UN_SELECT, "rowUnSelectEvent");
		JSPARAM.put(ON_AFTER_ROW_SELECT, "rowSelectEvent");
		JSPARAM.put(ON_BEFORE_ROW_SELECT, "dsIndexEvent");
	}
	
	public DatasetEvent(Dataset webElement) {
		super(webElement);
	}
	public DatasetEvent() {}
	
	
	@Override
	public String getJsParam(String eventName) {
		return JSPARAM.get(eventName);
	}
	public static LuiEventConf getOnBeforeRowSelectEvent() {
		LuiEventConf event = new LuiEventConf();
		event.setEventName(ON_BEFORE_ROW_SELECT);
		LuiParameter param = new LuiParameter();
		param.setName("dsIndexEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	public static LuiEventConf getOnAfterRowSelectEvent() {
		LuiEventConf event = new LuiEventConf();
		event.setEventName(ON_AFTER_ROW_SELECT);
		LuiParameter param = new LuiParameter();
		param.setName("rowSelectEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	public static LuiEventConf getOnAfterRowUnSelectEvent() {
		LuiEventConf event = new LuiEventConf();
		event.setEventName(ON_AFTER_ROW_UN_SELECT);
		LuiParameter param = new LuiParameter();
		param.setName("rowUnSelectEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	public static LuiEventConf getOnBeforeRowInsertEvent() {
		LuiEventConf event = new LuiEventConf();
		event.setEventName(ON_BEFORE_ROW_INSERT);
		LuiParameter param = new LuiParameter();
		param.setName("dsBeforeRowInsertEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	public static LuiEventConf getOnAfterRowInsertEvent() {
		LuiEventConf event = new LuiEventConf();
		event.setEventName(ON_AFTER_ROW_INSERT);
		LuiParameter param = new LuiParameter();
		param.setName("rowInsertEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	public static LuiEventConf getOnBeforeDataChangeEvent() {
		LuiEventConf event = new LuiEventConf();
		event.setEventName(ON_BEFORE_DATA_CHANGE);
		LuiParameter param = new LuiParameter();
		param.setName("dsBeforeDataChangeEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	public static LuiEventConf getOnAfterDataChangeEvent() {
		LuiEventConf event = new LuiEventConf();
		event.setEventName(ON_AFTER_DATA_CHANGE);
		LuiParameter param = new LuiParameter();
		param.setName("datasetCellEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	public static LuiEventConf getOnBeforeRowDeleteEvent() {
		LuiEventConf event = new LuiEventConf();
		event.setEventName(ON_BEFORE_ROW_DELETE);
		LuiParameter param = new LuiParameter();
		param.setName("dsBeforeRowDeleteEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	public static LuiEventConf getOnAfterRowDeleteEvent() {
		LuiEventConf event = new LuiEventConf();
		event.setEventName(ON_AFTER_ROW_DELETE);
		LuiParameter param = new LuiParameter();
		param.setName("rowDeleteEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	public static LuiEventConf getOnBeforePageChangeEvent() {
		LuiEventConf event = new LuiEventConf();
		event.setEventName(ON_BEFORE_PAGE_CHANGE);
		LuiParameter param = new LuiParameter();
		param.setName("dsBeforePageChangeEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	public static LuiEventConf getOnAfterPageChangeEvent() {
		LuiEventConf event = new LuiEventConf();
		event.setEventName(ON_AFTER_PAGE_CHANGE);
		LuiParameter param = new LuiParameter();
		param.setName("pageChangeEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	public static LuiEventConf getOnDataLoadEvent() {
		LuiEventConf event = new LuiEventConf();
		event.setEventName(ON_DATA_LOAD);
		LuiParameter param = new LuiParameter();
		param.setName("dataLoadEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	@Override
	public String getJsClazz() {
		return "DatasetListener";
	}
	@Override
	public LuiEventConf getEventTemplate(String key) {
		if (key.equals(ON_DATA_LOAD))
			return getOnDataLoadEvent();
		if (key.equals(ON_AFTER_DATA_CHANGE))
			return getOnAfterDataChangeEvent();
		if (key.equals(ON_AFTER_ROW_SELECT)) {
			return getOnAfterRowSelectEvent();
		} else
			return super.getEventTemplate(key);
	}
}
