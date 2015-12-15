package xap.lui.core.layout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import xap.lui.core.common.ExtAttriSupport;
import xap.lui.core.event.EventUtil;
import xap.lui.core.event.IEventSupport;
import xap.lui.core.listener.JsEventDesc;
import xap.lui.core.listener.LuiEventConf;
import xap.lui.core.model.ILifeCycleSupport;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.model.RequestLifeCycleContext;
import xap.lui.core.render.ILuiRender;

public class UIElement extends ExtAttriSupport implements IEventSupport, Serializable, ILifeCycleSupport, Cloneable {
	private static final long serialVersionUID = 1083409119485229582L;
	private Map<String, Serializable> attrMap = null;
	public static final String ID = "id";
	public static final String WIDGET_ID = "widgetId";
	public static final String STYLECLASSNAME = "className";
	public static final String DELETE = "delete";
	public static final String ADD = "add";
	public static final String UPDATE = "update";
	private List<JsEventDesc> acceptEventList = null;
	private List<LuiEventConf> eventConfList = null;

	private String id;
	private String widgetId;
	private Boolean visible;
	private String styleClassName;
	
	protected ILuiRender render = null;

	public void setId(String id) {
		String oriId = getId();
		if (oriId == null || !oriId.equals(id)) {
		    this.id=id;
			UpdatePair pair = new UpdatePair(ID, id);
			pair.setOldValue(oriId);
		}
	}

	public String getId() {
		return id;
	}

	public String getViewId() {
		return widgetId;
	}

	public void setViewId(String widgetId) {
		this.widgetId=widgetId;
	}

	public void setVisible(boolean visibility) {
		boolean oriVisible = getVisible();
		if (oriVisible != visibility) {
			this.visible=visibility;
		}
	}

	public boolean getVisible() {
		return visible== null ? true : (Boolean) visible;
	}

	public String getClassName() {
		return styleClassName;
	}

	public void setClassName(String className) {
		if(StringUtils.isBlank(className)){
			return ;
		}
		String oriCn = getClassName();
		if (!className.equals(oriCn)) {
			this.styleClassName=className;
		}
	}

	public void setAttribute(String key, Serializable value) {
		getAttrMap().put(key, value);
	}

	public Serializable getAttribute(String key) {
		return getAttrMap().get(key);
	}

	protected Map<String, Serializable> getAttrMap() {
		if (attrMap == null) {
			attrMap = createAttrMap();
		}
		return attrMap;
	}

	protected Map<String, Serializable> createAttrMap() {
		return new HashMap<String, Serializable>();
	}

	@Override
	public LifeCyclePhase getPhase() {
		return RequestLifeCycleContext.get().getPhase();
	}

	/**
	 * @param ele
	 *            添加子元素
	 */
	protected void addElement(UIElement ele) {
		if (ele != null && LifeCyclePhase.ajax.equals(getPhase())) {
			this.getRender().addChild(ele);
		}
	}

	/**
	 * @param ele
	 *            删除子元素
	 */
	protected void removeElement(UIElement ele) {
		if (ele != null && LifeCyclePhase.ajax.equals(getPhase())) {
			this.getRender().removeChild(ele);
		}
	}

	public Object doClone() {
		UIElement ele = (UIElement) this.clone();
		if (this.attrMap != null) {
			ele.attrMap = ele.createAttrMap();
			Iterator<String> keys = this.attrMap.keySet().iterator();
			while (keys.hasNext()) {
				String key = keys.next();
				Serializable s = this.attrMap.get(key);
				ele.attrMap.put(key, s);
			}
		}
		return ele;
	}

	@Override
	public void addEventConf(LuiEventConf event) {
		if (eventConfList == null)
			eventConfList = new ArrayList<LuiEventConf>();
		eventConfList.add(event);
	}

	@Override
	public List<JsEventDesc> getAcceptEventDescs() {
		if (acceptEventList == null)
			acceptEventList = createAcceptEventDescs();
		return acceptEventList;
	}

	protected List<JsEventDesc> createAcceptEventDescs() {
		return EventUtil.createAcceptEventDescs(this);
	}

	@Override
	public LuiEventConf[] getEventConfs() {
		return eventConfList == null ? null : eventConfList.toArray(new LuiEventConf[0]);
	}

	@Override
	public void removeEventConf(String eventName, String method) {
		if (eventConfList != null) {
			Iterator<LuiEventConf> it = eventConfList.iterator();
			while (it.hasNext()) {
				LuiEventConf event = it.next();
				if (event.getName().equals(eventName) && event.getMethod().equals(method)) {
					it.remove();
					break;
				}
			}
		}
	}

	public void removeAllEventConf() {
		if (eventConfList != null) {
			eventConfList.clear();
		}
	}

	/**
	 * 查找当前元素下的子元素。注意：被查找元素必须是独立元素，比如布局或者控件。布局Panel请参见2个参数的方法
	 * 
	 * @param id
	 *            独立元素ID
	 * @return
	 */
	public UIElement findChildById(String id) {
		return UIElementFinder.findElementById(this, id);
	}

	/**
	 * 查找当前元素下的子元素
	 * @param pid
	 *            子元素的容器元素ID，比如 UIFlowvLayout->pid UIFlowvPanel->id
	 * @param id
	 *            子元素ID
	 * @return
	 */
	public UIElement findChildById(String pid, String id) {
		return UIElementFinder.findElementById(this, pid, id);
	}

	@Override
	public String getWidgetName() {
		// TODO Auto-generated method stub
		return null;
	}

	public ILuiRender getRender() {
		// TODO Auto-generated method stub
		return null;
	}

}
