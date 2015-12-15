package xap.lui.core.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

//@XmlRootElement(name="CtrlState")
@XmlAccessorType(XmlAccessType.NONE)
public class CtrlState implements Serializable, Cloneable  {
	private static final long serialVersionUID = 6709198397443157852L;
	@XmlAttribute
	private String id;
	@XmlAttribute
	private String pid;
	@XmlAttribute
	private boolean isVisible = true;
	@XmlAttribute
	private  boolean enabled = true;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public boolean isVisible() {
		return isVisible;
	}
	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}
}
