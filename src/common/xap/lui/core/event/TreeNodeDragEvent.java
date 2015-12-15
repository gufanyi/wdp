package xap.lui.core.event;
import xap.lui.core.comps.TreeViewComp;
import xap.lui.core.dataset.LuiParameter;
import xap.lui.core.listener.EventSubmitRule;
import xap.lui.core.listener.LuiEventConf;
public class TreeNodeDragEvent extends AbstractServerEvent<TreeViewComp> {
	public static final String ON_DRAG_END = "onDragEnd";
	public static final String ON_DRAG_START = "onDragStart";
	private String sourceNodeRowId;
	private String targetNodeRowId;
	public TreeNodeDragEvent(TreeViewComp webElement) {
		super(webElement);
	}
	public TreeNodeDragEvent() {}
	public String getSourceNodeRowId() {
		return sourceNodeRowId;
	}
	public void setSourceNodeRowId(String sourceNodeRowId) {
		this.sourceNodeRowId = sourceNodeRowId;
	}
	public String getTargetNodeRowId() {
		return targetNodeRowId;
	}
	public void setTargetNodeRowId(String targetNodeRowId) {
		this.targetNodeRowId = targetNodeRowId;
	}
	public static LuiEventConf getOnDragStartEvent() {
		LuiEventConf event = new LuiEventConf();
		event.setEventName(ON_DRAG_START);
		LuiParameter param = new LuiParameter();
		param.setName("treeNodeDragEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	public static LuiEventConf getOnDragEndEvent() {
		LuiEventConf event = new LuiEventConf();
		event.setEventName(ON_DRAG_END);
		LuiParameter param = new LuiParameter();
		param.setName("treeNodeDragEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	@Override
	public String getJsClazz() {
		return "TreeNodeListener";
	}
}
