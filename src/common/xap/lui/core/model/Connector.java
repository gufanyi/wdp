package xap.lui.core.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import xap.lui.core.adapter.MapMappingAdapter;
import xap.lui.core.exception.LuiRuntimeException;

import xap.lui.core.builder.LuiObj;

/**
 * 信号-槽连接器配置
 * @author 
 *
 */
@XmlRootElement(name="Connector")
@XmlAccessorType(XmlAccessType.NONE)
public class Connector implements Cloneable, Serializable, LuiObj {
	private static final long serialVersionUID = 6792907017833197559L;
	public static final String WINDOW_WINDOW = "1";
	public static final String VIEW_VIEW = "2";
	public static final String VIEW_INLINEWINDOW = "3";
	public static final String VIEW_WINDOW = "4";
	public static final String WINDOW_VIEW = "5";
	public static final String INLINEWINDOW_VIEW = "6";
	@XmlAttribute
	private String id;
	@Deprecated
	//发起信号window
	@XmlAttribute
	private String sourceWindow;
	//发起信号对象
	@XmlAttribute
	private String source;
	@Deprecated
	//接受对象 window
	@XmlAttribute
	private String targetWindow;
	//接受对象
	@XmlAttribute
	private String target;
	@XmlAttribute
	private String pipeinId;
	@XmlAttribute
	private String pipeoutId;
	@XmlAttribute
	private String connType;
	
	//输入与输出值描述项映射，如果1对1完全对应，则此mapping 不需要录入
	@XmlElement(name="Maps")
	@XmlJavaTypeAdapter(MapMappingAdapter.class)
	private Map<String, String> mapping = null;
	
	public Connector() {
		
	}
	
	public String getSourceWindow() {
		return sourceWindow;
	}
	public void setSourceWindow(String sourceWindow) {
		this.sourceWindow = sourceWindow;
	}
	public String getTargetWindow() {
		return targetWindow;
	}
	public void setTargetWindow(String targetWindow) {
		this.targetWindow = targetWindow;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPipeinId() {
		return pipeinId;
	}
	public void setPipeinId(String pipeinId) {
		this.pipeinId = pipeinId;
	}
	public String getPipeoutId() {
		return pipeoutId;
	}
	public void setPipeoutId(String pipeoutId) {
		this.pipeoutId = pipeoutId;
	}
	public Map<String, String> getMapping() {
		return mapping;
	}
	public void setMapping(Map<String, String> mapping) {
		this.mapping = mapping;
	}
	public void putMapping(String key, String value){
		if (this.mapping == null)
			this.mapping = new HashMap<String, String>();
		this.mapping.put(key, value);
	}
	
	public Object clone(){
		try {
			Connector connector = (Connector)super.clone();
//			Map<String, String> mapping = null;
			if (this.mapping != null){
				connector.mapping = new HashMap<String, String>();
				Iterator<String>  it = this.mapping.keySet().iterator();
				while(it.hasNext()){
					String key = it.next();
					connector.mapping.put(key, this.mapping.get(key));
				}
			}
			
			return connector;
		} catch (CloneNotSupportedException e) {
			throw new LuiRuntimeException(e.getMessage(), e);
		}
	}

	public String getConnType() {
		return connType;
	}

	public void setConnType(String connType) {
		this.connType = connType;
	}
	
}
