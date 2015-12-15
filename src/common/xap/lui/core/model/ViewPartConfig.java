package xap.lui.core.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

import xap.lui.core.comps.LuiElement;

//@XmlRootElement(name="ViewPartRule")
@XmlAccessorType(XmlAccessType.FIELD)
public class ViewPartConfig extends LuiElement implements Serializable {
	private static final long serialVersionUID = -2311286196923157648L;
	
	@XmlAttribute(name="refId")
	private String refId;
	@XmlAttribute(name="canFreeDesign")
	private boolean canFreeDesign = true;
	@XmlAttribute(name="sourcePackage")
	private String sourcePackage;
	
	public String getRefId() {
		return refId;
	}
	
	public void setRefId(String refId) {
		this.refId = refId;
	}
	
	public boolean isCanFreeDesign() {
		return canFreeDesign;
	}
	public void setCanFreeDesign(boolean canFreeDesign) {
		this.canFreeDesign = canFreeDesign;
	}
	public void setSrcFolder(String refId2) {
		// TODO Auto-generated method stub
		
	}
	public String getSrcFolder() {
		return sourcePackage;
	}
}
