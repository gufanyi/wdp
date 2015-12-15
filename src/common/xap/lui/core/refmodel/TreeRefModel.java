package xap.lui.core.refmodel;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(name="TreeRefModel")
@XmlAccessorType(XmlAccessType.NONE)
public class TreeRefModel extends GridRefModel{
	private static final long serialVersionUID = -1260013275275477131L;
	
	@XmlAttribute
	private String childField;//递归字段
	@XmlAttribute
	private String fatherField;//递归父字段
	@XmlAttribute
	private String mark;//树节点显示之间隔字符
	
	public String getChildField() {
		return childField;
	}
	public void setChildField(String childField) {
		this.childField = childField;
	}
	public String getFatherField() {
		return fatherField;
	}
	public void setFatherField(String fatherField) {
		this.fatherField = fatherField;
	}
	public String getMark() {
		return mark;
	}
	public void setMark(String mark) {
		this.mark = mark;
	}
	
	public String getRootName() {
		return null;
	}
}
