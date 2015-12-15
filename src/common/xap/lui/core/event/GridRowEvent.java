package xap.lui.core.event;

import java.util.HashMap;
import java.util.Map;

import xap.lui.core.comps.GridComp;
import xap.lui.core.dataset.LuiParameter;
import xap.lui.core.listener.EventSubmitRule;
import xap.lui.core.listener.LuiEventConf;

/**
 * @author guoweic
 *
 */
public class GridRowEvent extends AbstractServerEvent<GridComp> {
	
	public static final String ON_ROW_SELECTED = "onRowSelected";
	public static final String ON_ROW_DB_CLICK = "onRowDbClick";
	public static final String BEFORE_ROW_SELECTED = "beforeRowSelected";
	public static final Map<String,String> JSPARAM = new HashMap<String,String>();
	static {
		JSPARAM.put(ON_ROW_SELECTED, "rowSelectedEvent");
		JSPARAM.put(ON_ROW_DB_CLICK, "rowEvent");
		JSPARAM.put(BEFORE_ROW_SELECTED, "rowEvent");
	}
	public GridRowEvent(GridComp webElement) {
		super(webElement);
	}
	
	public GridRowEvent() {
	}
	
	
	@Override
	public String getJsParam(String eventName) {
		return JSPARAM.get(eventName);
	}

	public static LuiEventConf getBeforeRowSelectedEvent() {
		LuiEventConf event = new LuiEventConf();
		event.setEventName(BEFORE_ROW_SELECTED);
		LuiParameter param = new LuiParameter();
		param.setName("rowEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	public static LuiEventConf getOnRowDbClickEvent() {
		LuiEventConf event = new LuiEventConf();
		event.setEventName(ON_ROW_DB_CLICK);
		LuiParameter param = new LuiParameter();
		param.setName("rowEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	public static LuiEventConf getOnRowSelectedEvent() {
		LuiEventConf event = new LuiEventConf();
		event.setEventName(ON_ROW_SELECTED);
		LuiParameter param = new LuiParameter();
		param.setName("rowSelectedEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	
	@Override
	public String getJsClazz() {
		return "GridRowListener";
	}
	

}
