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
public class GridCellEvent extends AbstractServerEvent<GridComp> {
	public static final String BEFORE_EDIT = "beforeEdit";
	public static final String AFTER_EDIT = "afterEdit";
	public static final String CELL_EDIT = "cellEdit";
	public static final String ON_CELL_CLICK = "onCellClick";
	public static final String CELL_VALUE_CHANGED = "cellValueChanged";
	
	public static final Map<String,String> JSPARAM = new HashMap<String,String>();
	static {
		JSPARAM.put(BEFORE_EDIT, "gridCellEvent");
		JSPARAM.put(AFTER_EDIT, "gridCellEvent");
		JSPARAM.put(CELL_EDIT, "gridCellEvent");
		JSPARAM.put(ON_CELL_CLICK, "gridCellEvent");
		JSPARAM.put(CELL_VALUE_CHANGED, "gridCellEvent");
	}
	
	private int rowIndex;
	private int colIndex;
	private Object newValue;
	private Object oldValue;
	public GridCellEvent(GridComp webElement) {
		super(webElement);
	}
	public GridCellEvent() {}
	
	@Override
	public String getJsParam(String eventName) {
		return JSPARAM.get(eventName);
	}
	
	public int getRowIndex() {
		return rowIndex;
	}
	public void setRowIndex(int rowIndex) {
		this.rowIndex = rowIndex;
	}
	public int getColIndex() {
		return colIndex;
	}
	public void setColIndex(int colIndex) {
		this.colIndex = colIndex;
	}
	public Object getOldValue() {
		return oldValue;
	}
	public void setOldValue(Object oldValue) {
		this.oldValue = oldValue;
	}
	public Object getNewValue() {
		return newValue;
	}
	public void setNewValue(Object newValue) {
		this.newValue = newValue;
	}
	public static LuiEventConf getOnCellClickEvent() {
		LuiEventConf event = new LuiEventConf();
		event.setEventName(ON_CELL_CLICK);
		LuiParameter param = new LuiParameter();
		param.setName("cellEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	public static LuiEventConf getCellEditEvent() {
		LuiEventConf event = new LuiEventConf();
		event.setEventName(CELL_EDIT);
		LuiParameter param = new LuiParameter();
		param.setName("cellEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	public static LuiEventConf getAfterEditEvent() {
		LuiEventConf event = new LuiEventConf();
		event.setEventName(AFTER_EDIT);
		LuiParameter param = new LuiParameter();
		param.setName("cellEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	public static LuiEventConf getBeforeEditEvent() {
		LuiEventConf event = new LuiEventConf();
		event.setEventName(BEFORE_EDIT);
		LuiParameter param = new LuiParameter();
		param.setName("cellEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	public static LuiEventConf getCellValueChangedEvent() {
		LuiEventConf event = new LuiEventConf();
		event.setEventName(CELL_VALUE_CHANGED);
		LuiParameter param = new LuiParameter();
		param.setName("cellEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	@Override
	public String getJsClazz() {
		return "GridCellListener";
	}
}
