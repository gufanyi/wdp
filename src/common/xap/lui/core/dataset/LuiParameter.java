package xap.lui.core.dataset;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.logger.LuiLogger;

@XmlRootElement(name="Param")
@XmlAccessorType(XmlAccessType.NONE)
public class LuiParameter implements Cloneable, Serializable {
	private static final long serialVersionUID = -2848436767660730017L;
	/*参数名称*/
	@XmlAttribute(name="name")
	private String name = "";
	/*参数描述*/
	@XmlAttribute(name="desc")
	private String desc = "";
	@XmlAttribute(name="value")
	private String value = "";
	
	public LuiParameter(String name, String desc) {
		this.name = name;
		this.desc = desc;
	}
	
	public LuiParameter(String name){
		this(name, null);
	}
	
	public LuiParameter() {
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	public Object clone() {
		try {
			return super.clone();
		} 
		catch (CloneNotSupportedException e) {
			LuiLogger.error(e);
			throw new LuiRuntimeException(e);
		}
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
}
