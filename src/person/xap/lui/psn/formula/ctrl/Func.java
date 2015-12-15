package xap.lui.psn.formula.ctrl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import org.eclipse.persistence.oxm.annotations.XmlCDATA;

@XmlAccessorType(XmlAccessType.NONE)
public class Func {

	@XmlAttribute
	private String id;
	@XmlAttribute
	private String name;
	@XmlAttribute
	private String desc;
	@XmlElement
	@XmlCDATA
	private String demo;
	
	private CtrlType ctrlType;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getDemo() {
		return demo;
	}

	public void setDemo(String demo) {
		this.demo = demo;
	}

	public CtrlType getCtrlType() {
		return ctrlType;
	}

	public void setCtrlType(CtrlType ctrlType) {
		this.ctrlType = ctrlType;
	}
	
}
