package xap.lui.core.comps;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import org.apache.commons.lang.StringUtils;

import xap.lui.core.builder.LuiObj;
import xap.lui.core.common.ExtAttriSupport;
import xap.lui.core.context.BaseContext;
import xap.lui.core.event.EventUtil;
import xap.lui.core.event.IEventSupport;
import xap.lui.core.exception.LuiPluginException;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.listener.JsEventDesc;
import xap.lui.core.listener.LuiEventConf;
import xap.lui.core.model.ILifeCycleSupport;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.model.RequestLifeCycleContext;

import com.alibaba.fastjson.annotation.JSONField;
@XmlAccessorType(XmlAccessType.NONE)
public abstract class LuiElement extends ExtAttriSupport implements LuiObj, IEventSupport, Serializable, Cloneable, ILifeCycleSupport {
	public static final String CONF_ADD = "add";
	public static final String CONF_REF = "ref";
	public static final String CONF_DEL = "del";
	private static final long serialVersionUID = -8330041983034564169L;
	// 组件唯一标识
	@XmlAttribute
	private String id;
	// 标识是否被渲染过
	private boolean rendered = false;
	// 用于处于附加配置环境的文件，比如针对NC模板，由配置文件补充配置时的类型。如果是ADD，表示配置元素为新加。如果为DEL，表示删除原模板中对应的元素。如果是REF，表示是通过此配置更改原有配置。
	private String confType = CONF_ADD;
	// 用于处于附加配置环境的文件，指明某些对位置敏感的元素具体插入的位置。-1标识追加到尾部。从0开始
	private int confPos = -1;
	private List<JsEventDesc> acceptEventList = null;
	
	@XmlElementWrapper(name ="Events")
	@XmlElement(name="Event")
	private List<LuiEventConf> eventConfList =null;
	// context是否改变
	private boolean ctxChanged = false;
	// 已改变的context属性列表
	private List<String> ctxChangedProperties = new ArrayList<String>();
	@XmlAttribute
	private String from;
	@XmlAttribute(name="srcFolder")
	private String srcFolder;
	// 真实路径
	private String realPath;
	public LuiElement() {}
	public LuiElement(String id) {
		this.id = id;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@JSONField(serialize = false)
	public boolean isRendered() {
		return rendered;
	}
	public void setRendered(boolean rendered) {
		this.rendered = rendered;
	}
	@Override
	public Object clone() {
		LuiElement ele = (LuiElement) super.clone();
		ele.ctxChanged = false;
		ele.ctxChangedProperties = new ArrayList<String>();
		LuiEventConf[] eventConfs = this.getEventConfs();
		if (eventConfs != null && eventConfs.length > 0) {
			ele.eventConfList = new ArrayList<LuiEventConf>();
			for (LuiEventConf eventConf : eventConfs) {
				ele.addEventConf((LuiEventConf) eventConf.clone());
			}
		}
		return ele;
	}
	@JSONField(serialize = false)
	public String getConfType() {
		return confType;
	}
	public void setConfType(String confType) {
		this.confType = confType;
	}
	public void mergeProperties(LuiElement ele) {
		if (ele == null)
			return;
		if (!ele.getClass().isAssignableFrom(this.getClass())) {
			throw new LuiRuntimeException("指定的元素并非来自同一继承体系");
		}
		int confPos = ele.getConfPos();
		if (confPos != -1)
			this.confPos = confPos;
		String confType = ele.getConfType();
		if (confType != null)
			this.confType = ele.getConfType();
	}
	@JSONField(serialize = false)
	public int getConfPos() {
		return confPos;
	}
	public void setConfPos(int confPos) {
		this.confPos = confPos;
	}
	@JSONField(serialize = false)
	public boolean isCtxChanged() {
		return ctxChanged;
	}
	public void setCtxChanged(boolean changed) {
		this.ctxChanged = changed;
		if (!changed)
			this.clearCtxChangedProperties();
	}
	@JSONField(serialize = true)
	public BaseContext getContext() {
		return null;
	}
	public void setContext(BaseContext ctx) {}
	@JSONField(serialize = false)
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public void validate() {
		if (this.getId() == null || this.getId().equals(""))
			throw new LuiPluginException("ID不能为空");
	}
	@JSONField(serialize = false)
	public List<String> getCtxChangedProperties() {
		return ctxChangedProperties;
	}
	public void setCtxChangedProperties(List<String> ctxChangedProperties) {
		this.ctxChangedProperties = ctxChangedProperties;
	}
	/**
	 * 增加已改变的context属性
	 * 
	 * @param propertyName
	 */
	public void addCtxChangedProperty(String propertyName) {
		this.ctxChangedProperties.add(propertyName);
	}
	/**
	 * 清空已改变的context属性列表
	 */
	public void clearCtxChangedProperties() {
		this.ctxChangedProperties.clear();
	}
	/**
	 * 查看context的某个属性是否发生变化
	 * 
	 * @param propertyName
	 * @return
	 */
	public boolean checkCtxPropertyChanged(String propertyName) {
		if (this.ctxChangedProperties.indexOf(propertyName) != -1)
			return true;
		return false;
	}

	@JSONField(serialize = false)
	public LifeCyclePhase getPhase() {
		return RequestLifeCycleContext.get().getPhase();
	}
	public LuiEventConf removeEventConf(String id) {
		if (eventConfList != null) {
			Iterator<LuiEventConf> it = eventConfList.iterator();
			while (it.hasNext()) {
				LuiEventConf conf = it.next();
				if (id.equals(conf.getId())) {
					it.remove();
					return conf;
				}
			}
		}
		return null;
	}
	public LuiEventConf getEventConf(String id) {
		if (eventConfList != null) {
			Iterator<LuiEventConf> it = eventConfList.iterator();
			while (it.hasNext()) {
				LuiEventConf conf = it.next();
				if (id.equals(conf.getId())) {
					return conf;
				}
			}
		}
		return null;
	}
	@Override
	public void addEventConf(LuiEventConf event) {
		if (eventConfList == null)
			eventConfList = new ArrayList<LuiEventConf>();
		if(this.getEventConf(event.getName(), event.getMethod()) == null)
			eventConfList.add(event);
	}
	public void removeEventConf(LuiEventConf event) {
		if (eventConfList != null)
			eventConfList.remove(event);
	}
	public void removeAllEventConf() {
		if (eventConfList != null) {
			eventConfList.clear();
		}
	}
	@JSONField(serialize = false)
	public List<LuiEventConf> getEventConfList() {
		return eventConfList;
	}
	public void setEventConfList(List<LuiEventConf> eventConfList) {
		this.eventConfList = eventConfList;
	}
	@JSONField(serialize = false)
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
	public LuiEventConf getEventConf(String eventName, String methodName) {
		LuiEventConf event = null;
		if (eventConfList != null) {
			Iterator<LuiEventConf> it = eventConfList.iterator();
			while (it.hasNext()) {
				event = it.next();
				if(StringUtils.isNotBlank(methodName)){
					if (event.getName().equals(eventName) && event.getMethod().equals(methodName)) {
						break;
					}
				}else{
					if (event.getName().equals(eventName)) {
						break;
					}
				}
				event = null;
			}
		}
		return event;
	}
	@JSONField(serialize = false)
	public List<JsEventDesc> getAcceptEventDescs() {
		if (acceptEventList == null)
			acceptEventList = createAcceptEventDescs();
		return acceptEventList;
	}
	protected List<JsEventDesc> createAcceptEventDescs() {
		return EventUtil.createAcceptEventDescs(this);
	}
	@JSONField(serialize = false)
	public String getSrcFolder() {
		return srcFolder;
	}
	public void setSrcFolder(String srcFolder) {
		this.srcFolder = srcFolder;
	}
	@JSONField(serialize = false)
	public String getRealPath() {
		return realPath;
	}
	public void setRealPath(String realPath) {
		this.realPath = realPath;
	}
	@Override
	public String getWidgetName() {
		return null;
	}
}
