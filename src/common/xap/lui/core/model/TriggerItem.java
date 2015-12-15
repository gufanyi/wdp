package xap.lui.core.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
/**
 * 输出触发器定义，一个输出描述可包含多个触发器
 * @author dengjt
 *
 */
@XmlRootElement(name="TriggerItem")
@XmlAccessorType(XmlAccessType.FIELD)
public class TriggerItem implements Serializable, Cloneable {
	private static final long serialVersionUID = 2291351982569466595L;
	public static final String TYPE_DS_ROW_SELECT = "TYPE_DS_ROW_SELECT";
	public static final String TYPE_BUTTON_CLICK = "BUTTON_CLICK";
	public static final String TYPE_TEXT_VALUE_CHANGE = "TYPE_TEXT_VALUE_CHANGE";
	//触发器名称
	@XmlAttribute
	private String id;
	//触发来源
	@XmlAttribute
	private String source;
	//触发类型
	@XmlAttribute
	private String type;
	//描述
	@XmlAttribute
	private String desc;
	public String getId() {
		return id;
	}
	public void setId(String name) {
		this.id = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}
}
