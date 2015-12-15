package xap.lui.psn.formula.ctrl;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.NONE)
public class CtrlType {

	@XmlAttribute
	private String id;
	@XmlAttribute
	private String name;
	@XmlElement(name="func")
	private List<Func> funcList = null;
	
	public void addFunc(Func func){
		if(this.funcList != null)
			this.funcList = new ArrayList<Func>();
		this.funcList.add(func);
		func.setCtrlType(this);
	}
	
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
	public List<Func> getFuncList() {
		return funcList;
	}
	public void setFuncList(List<Func> funcList) {
		this.funcList = funcList;
	}
	
}
