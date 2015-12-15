package xap.lui.core.event;
import java.util.HashMap;
import java.util.Map;

import xap.lui.core.comps.TreeViewComp;
import xap.lui.core.dataset.LuiParameter;
import xap.lui.core.listener.EventSubmitRule;
import xap.lui.core.listener.LuiEventConf;
/**
 * @author guoweic
 *
 */
public class TreeNodeEvent extends AbstractServerEvent<TreeViewComp> {
	public static final String ON_NODE_LOAD = "onNodeLoad";
	public static final String ON_CHECKED = "onChecked";
	public static final String ON_NODE_DELETE = "onNodeDelete";
	public static final String BEFORE_SEL_NODE_CHANGE = "beforeSelNodeChange";
	public static final String AFTER_SEL_NODE_CHANGE = "afterSelNodeChange";
	public static final String ROOT_NODE_CREATED = "rootNodeCreated";
	public static final String NODE_CREATED = "nodeCreated";
	public static final String BEFORE_NODE_CAPTION_CHANGE = "beforeNodeCaptionChange";
	public static final String ON_DBCLICK = "ondbclick";
	public static final String ON_CLICK = "onclick";
	
	public static final Map<String,String> JSPARAM = new HashMap<String,String>();
	static {
		JSPARAM.put(ON_NODE_LOAD, "treeNodeEvent");
		JSPARAM.put(ON_CHECKED, "treeNodeEvent");
		JSPARAM.put(ON_NODE_DELETE, "treeNodeEvent");
		JSPARAM.put(BEFORE_SEL_NODE_CHANGE, "treeNodeEvent");
		JSPARAM.put(AFTER_SEL_NODE_CHANGE, "treeNodeEvent");
		JSPARAM.put(ROOT_NODE_CREATED, "treeNodeEvent");
		JSPARAM.put(NODE_CREATED, "treeNodeEvent");
		JSPARAM.put(BEFORE_NODE_CAPTION_CHANGE, "treeNodeEvent");
		JSPARAM.put(ON_DBCLICK, "treeNodeEvent");
		JSPARAM.put(ON_CLICK, "treeNodeEvent");
	}
	
	private String nodeRowId;
	private String currentdsId;
	public TreeNodeEvent() {}
	public String getCurrentdsId() {
		return currentdsId;
	}
	public void setCurrentdsId(String currentdsId) {
		this.currentdsId = currentdsId;
	}
	public TreeNodeEvent(TreeViewComp webElement) {
		super(webElement);
	}
	public String getNodeRowId() {
		return nodeRowId;
	}
	public void setNodeRowId(String nodeRowId) {
		this.nodeRowId = nodeRowId;
	}
	@Override
	public String getJsParam(String eventName) {
		return JSPARAM.get(eventName);
	}
	public static LuiEventConf getOnClickEvent() {
		LuiEventConf event = new LuiEventConf();
		event.setEventName(ON_CLICK);
		LuiParameter param = new LuiParameter();
		param.setName("treeNodeMouseEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	public static LuiEventConf getOnDbclickEvent() {
		LuiEventConf event = new LuiEventConf();
		event.setEventName(ON_DBCLICK);
		LuiParameter param = new LuiParameter();
		param.setName("treeNodeEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	public static LuiEventConf getNodeCreatedEvent() {
		LuiEventConf event = new LuiEventConf();
		event.setEventName(NODE_CREATED);
		LuiParameter param = new LuiParameter();
		param.setName("treeNodeEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	public static LuiEventConf getRootNodeCreatedEvent() {
		LuiEventConf event = new LuiEventConf();
		event.setEventName(ROOT_NODE_CREATED);
		LuiParameter param = new LuiParameter();
		param.setName("treeNodeEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	public static LuiEventConf getAfterSelNodeChangeEvent() {
		LuiEventConf event = new LuiEventConf();
		event.setEventName(AFTER_SEL_NODE_CHANGE);
		LuiParameter param = new LuiParameter();
		param.setName("treeNodeEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	public static LuiEventConf getBeforeSelNodeChangeEvent() {
		LuiEventConf event = new LuiEventConf();
		event.setEventName(BEFORE_SEL_NODE_CHANGE);
		LuiParameter param = new LuiParameter();
		param.setName("treeNodeEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	public static LuiEventConf getOnNodeLoadEvent() {
		LuiEventConf event = new LuiEventConf();
		event.setEventName(ON_NODE_LOAD);
		LuiParameter param = new LuiParameter();
		param.setName("treeNodeEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	public static LuiEventConf getOnCheckedEvent() {
		LuiEventConf event = new LuiEventConf();
		event.setEventName(ON_CHECKED);
		LuiParameter param = new LuiParameter();
		param.setName("treeNodeEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	public static LuiEventConf getOnNodeDeleteEvent() {
		LuiEventConf event = new LuiEventConf();
		event.setEventName(ON_NODE_DELETE);
		LuiParameter param = new LuiParameter();
		param.setName("treeNodeEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	public static LuiEventConf getBeforeNodeCaptionChangeEvent() {
		LuiEventConf event = new LuiEventConf();
		event.setEventName(BEFORE_NODE_CAPTION_CHANGE);
		LuiParameter param = new LuiParameter();
		param.setName("treeNodeEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	@Override
	public String getJsClazz() {
		return "TreeNodeListener";
	}
	@Override
	public LuiEventConf getEventTemplate(String key) {
		if (key.equals(ON_DBCLICK))
			return getOnDbclickEvent();
		else
			return super.getEventTemplate(key);
	}
}
