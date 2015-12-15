package xap.lui.core.event;
import xap.lui.core.comps.GridComp;
import xap.lui.core.dataset.LuiParameter;
import xap.lui.core.listener.EventSubmitRule;
import xap.lui.core.listener.LuiEventConf;
/**
 * @author guoweic
 *
 */
public class GridEvent extends AbstractServerEvent<GridComp> {
	public static final String ON_DATA_OUTER_DIV_CONTEXT_MENU = "onDataOuterDivContextMenu";
	public static final String ON_LAST_CELL_ENTER = "onLastCellEnter";
	public GridEvent(GridComp webElement) {
		super(webElement);
	}
	public static LuiEventConf getOnDataOuterDivContextMenuEvent() {
		LuiEventConf event = new LuiEventConf();
		event.setEventName(ON_DATA_OUTER_DIV_CONTEXT_MENU);
		LuiParameter param = new LuiParameter();
		param.setName("mouseEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	public static LuiEventConf getOnLastCellEnterEvent() {
		LuiEventConf event = new LuiEventConf();
		event.setEventName(ON_LAST_CELL_ENTER);
		LuiParameter param = new LuiParameter();
		param.setName("gridEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	
	@Override
	public String getJsClazz() {
		return "GridListener";
	}
}
