package xap.lui.core.common;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Attribute")
@XmlAccessorType(XmlAccessType.NONE)
public class ExtAttribute implements Serializable, Cloneable {
	private static final long serialVersionUID = -7894011456596287487L;
	@XmlAttribute(name="key")
	private String key;
	//@XmlElement(name="Value")
	private Serializable value;
	@XmlAttribute(name="desc")
	private String desc;
	public ExtAttribute(){
		
	}
	public ExtAttribute(String key,String value){
		this.key = key;
		this.value = value;
	}
	public ExtAttribute(String key,String value,String desc){
		this.key = key;
		this.value = value;
		this.desc = desc;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public Serializable getValue() {
		return value;
	}
	public void setValue(Serializable value) {
		this.value = value;
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
			return null;
		}
	}
	
}
