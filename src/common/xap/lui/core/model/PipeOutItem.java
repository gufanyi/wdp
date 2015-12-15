package xap.lui.core.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 输出项描述，每个项描述输出中的一个值。支持表达式，支持自定义
 * @author 
 *
 */
@XmlRootElement(name="PipeOutItem")
@XmlAccessorType(XmlAccessType.FIELD)
public class PipeOutItem implements Serializable, Cloneable {
	private static final long serialVersionUID = -8798072449173452354L;
	public static final String TYPE_FOMULAR = "TYPE_FOMULAR";
	public static final String TYPE_DS_FIELD = "TYPE_DS_FIELD";
	public static final String TYPE_TEXT_VALUE = "TYPE_TEXT_VALUE";
	//输出键名称
	@XmlAttribute
	private String name;
	//取数类型
	@XmlAttribute
	private String type;
	//取数来源
	@XmlAttribute
	private String source;
	//取得的值，一般情况下，只有静态值和表达式需要记录。
	@XmlAttribute
	private String value;
	//描述
	@XmlAttribute
	private String desc;
	//输出对象类型
	@XmlAttribute
	private String clazztype = "java.lang.String";
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
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
