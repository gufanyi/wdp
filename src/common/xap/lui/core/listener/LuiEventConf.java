package xap.lui.core.listener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.eclipse.persistence.oxm.annotations.XmlCDATA;

import xap.lui.core.common.ExtAttriSupport;
import xap.lui.core.dataset.LuiParameter;

@XmlRootElement(name="Event")
@XmlAccessorType(XmlAccessType.NONE)
public class LuiEventConf extends ExtAttriSupport implements Serializable, Cloneable {
	
	private static final long serialVersionUID = 5332995004954913304L;
	//事件正常状态
	public static final int NORMAL_STATUS = 1;
	//事件增加状态
	public static final int ADD_STATUS = 2;
	//事件删除状态
	public static final int DEL_STATUS = 3;
	public static final String SUBMIT_PRE = "SP_";
	public static final String PARAM_DATASET_FIELD_ID = "dataset_field_id";
	
	//事件类包名
	private static final String EVENT_PACKAGE = "xap.lui.core.event.";
	
	@XmlAttribute(name="eventName")
	private String eventName = null;
	@XmlElement(name="Action")
	@XmlCDATA
	private String script = null;
	@XmlAttribute(name="method")
	private String method = null;
	@XmlAttribute
	private boolean onserver = true;
	@XmlAttribute
	private boolean async = true;
	@XmlAttribute
	private boolean nmc = true;
	@XmlAttribute
	private String id;
	//@XmlElementWrapper(name ="Params")
	//@XmlElement(name="Param") 
	private List<LuiParameter> paramList = null;
	private List<LuiParameter> extendParamList = null;
	@XmlElement(name="SubmitRule") 
	private EventSubmitRule submitRule = null;
	@XmlAttribute(name="controller")
	private String controllerClazz = null;
	@XmlAttribute(name="eventType")
	private String eventType = null;
	//事件状态
	@XmlAttribute
	private int eventStatus = NORMAL_STATUS;
	//类文件路径
	@XmlAttribute
	private String classFilePath = null;
	//类文件名称
	@XmlAttribute
	private String classFileName = null;
	
	private String modelCmd=null;
	
	private String jsParam;
	
	//目标UI状态
	@XmlAttribute(name="uistateId")
	private String uIStateId = null;
	
	//事件语言
	@XmlAttribute
	private String eventLang = null;
	
	public LuiEventConf(){}
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	public String getEventType() {
		return EVENT_PACKAGE + eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public LuiEventConf(String eventName, LuiParameter param, String script) {
		this.eventName = eventName;
		this.paramList = new ArrayList<LuiParameter>();
		paramList.add(param);
		this.script = script;
	}
	
	public List<LuiParameter> getParamList() {
		if(this.paramList == null)
			this.paramList = new ArrayList<LuiParameter>();
		return this.paramList;
	}

	public void setParamList(List<LuiParameter> paramList) {
		this.paramList = paramList;
	}

	public void addParam(LuiParameter param) {
		LuiParameter p = getParam(param.getName());
		if(p == null)
			getParamList().add(param);
	}
	
	public LuiParameter getParam(String name) {
		for(int i = 0; i < getParamList().size(); i++)
		{
			if(getParamList().get(i).getName().equals(name))
				return getParamList().get(i);
		}
		return null;
	}


	public void setExtendParamList(List<LuiParameter> extendParamList) {
		this.extendParamList = extendParamList;
	}

	public void addExtendParam(LuiParameter extendParam) {
		if(extendParamList==null){
			extendParamList=new ArrayList<LuiParameter>();
		}
		extendParamList.add(extendParam);
	}
	
	public LuiParameter getExtendParam(String name) {
		for(int i = 0; i < getExtendParamList().size(); i++)
		{
			if(extendParamList.get(i).getName().equals(name))
				return extendParamList.get(i);
		}
		return null;
	}

	public List<LuiParameter> getExtendParamList() {
		if(this.extendParamList == null)
			this.extendParamList = new ArrayList<LuiParameter>();
		return extendParamList;
	}
	
	public String getName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	public Object clone() {
		LuiEventConf eventConf = (LuiEventConf) super.clone();
		eventConf.paramList = new ArrayList<LuiParameter>();
		eventConf.extendParamList = new ArrayList<LuiParameter>();
		for(int i = 0, n =  getParamList().size(); i < n; i++)
			eventConf.paramList.add((LuiParameter) getParamList().get(i).clone());
		for(int i = 0, n = getExtendParamList().size(); i < n; i++)
			eventConf.extendParamList.add((LuiParameter) getExtendParamList().get(i).clone());
		if(submitRule != null)
			eventConf.submitRule = (EventSubmitRule) submitRule.clone();
		
		return eventConf;
	}

	public boolean isOnserver() {
		return onserver;
	}

	public void setOnserver(boolean onserver) {
		this.onserver = onserver;
	}

	public EventSubmitRule getSubmitRule() {
		return submitRule;
	}

	public void setSubmitRule(EventSubmitRule submitRule) {
		this.submitRule = submitRule;
	}

	public boolean isAsync() {
		return async;
	}

	public void setAsync(boolean async) {
		this.async = async;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getControllerClazz() {
		return controllerClazz == null ? "" : controllerClazz;
	}

	public void setControllerClazz(String controllerClazz) {
		this.controllerClazz = controllerClazz;
	}

	public int getEventStatus() {
		return eventStatus;
	}

	public void setEventStatus(int eventStatus) {
		this.eventStatus = eventStatus;
	}

	public String getClassFilePath() {
		return classFilePath;
	}

	public void setClassFilePath(String classFilePath) {
		this.classFilePath = classFilePath;
	}

	public String getClassFileName() {
		return classFileName;
	}
	
	public String getModelCmd() {
		return modelCmd;
	}
	public void setModelCmd(String modelCmd) {
		this.modelCmd = modelCmd;
	}
	public void setClassFileName(String classFileName) {
		this.classFileName = classFileName;
	}
	public boolean isNmc() {
		return nmc;
	}
	public void setNmc(boolean nmc) {
		this.nmc = nmc;
	}
	public String getJsParam() {
		return jsParam;
	}
	public void setJsParam(String jsParam) {
		this.jsParam = jsParam;
	}
	public String getuIStateId() {
		return uIStateId;
	}
	public void setuIStateId(String uIStateId) {
		this.uIStateId = uIStateId;
	}
	public String getEventLang() {
		return eventLang;
	}
	public void setEventLang(String eventLang) {
		this.eventLang = eventLang;
	}
}
