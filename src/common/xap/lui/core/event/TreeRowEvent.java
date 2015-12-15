package xap.lui.core.event;
import xap.lui.core.comps.TreeViewComp;
import xap.lui.core.dataset.LuiParameter;
import xap.lui.core.listener.EventSubmitRule;
import xap.lui.core.listener.LuiEventConf;
public class TreeRowEvent extends AbstractServerEvent<TreeViewComp> {
	public static final String BEFORE_NODE_CREATE = "beforeNodeCreate";
	public TreeRowEvent(TreeViewComp webElement) {
		super(webElement);
	}
	public TreeRowEvent() {
		super();
	}
	public static LuiEventConf getBeforeNodeCreateEvent() {
		LuiEventConf event = new LuiEventConf();
		event.setEventName(BEFORE_NODE_CREATE);
		LuiParameter param = new LuiParameter();
		param.setName("treeRowEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	@Override
	public String getJsClazz() {
		return "TreeRowListener";
	}
}
