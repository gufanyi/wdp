package xap.lui.core.event;
import xap.lui.core.comps.TreeViewComp;
import xap.lui.core.dataset.LuiParameter;
import xap.lui.core.listener.EventSubmitRule;
import xap.lui.core.listener.LuiEventConf;
/**
 * @author guoweic
 *
 */
public class TreeCtxMenuEvent extends AbstractServerEvent<TreeViewComp> {
	public static final String BEFORE_CONTEXT_MENU = "beforeContextMenu";
	public TreeCtxMenuEvent(TreeViewComp webElement) {
		super(webElement);
	}
	public TreeCtxMenuEvent() {}
	public static LuiEventConf getBeforeContextMenuEvent() {
		LuiEventConf event = new LuiEventConf();
		event.setEventName(BEFORE_CONTEXT_MENU);
		LuiParameter param = new LuiParameter();
		param.setName("treeContextMenuEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	@Override
	public String getJsClazz() {
		return "TreeContextMenuListener";
	}
}
