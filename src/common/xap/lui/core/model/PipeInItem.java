package xap.lui.core.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="PipeInItem")
@XmlAccessorType(XmlAccessType.FIELD)
public class PipeInItem implements Cloneable, Serializable {
	private static final long serialVersionUID = 1564636173007514266L;
	public static final String TYPE_DS_LOAD = "TYPE_DS_LOAD";
	public static final String TYPE_IFRAME_REDIRECT = "TYPE_IFRAME_REDIRECT";
	// 用户自定义操作
	public static final String TYPE_SELFDEF = "TYPE_SELFDEF";
	@XmlAttribute
	private String id;
	@XmlAttribute
	private String value;
	//输入对象类型
	@XmlAttribute(name="clazztype")
	private String clazztype = "java.lang.String";
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getClazztype() {
		return clazztype;
	}

	public void setClazztype(String clazztype) {
		this.clazztype = clazztype;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}
}
