package xap.lui.psn.formula.ctrl;

import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import xap.lui.core.common.ContextResourceUtil;
import xap.lui.core.exception.LuiRuntimeException;

@XmlRootElement(name = "ctrlfunc")
@XmlAccessorType(XmlAccessType.NONE)
public class CtrlFunc {
	
	@XmlElement(name = "ctrltype")
	private List<CtrlType> ctrltypeList = null;

	public void addCtrlType(CtrlType ctrlType){
		if(this.ctrltypeList == null)
			this.ctrltypeList = new ArrayList<CtrlType>();
		this.ctrltypeList.add(ctrlType);
	}
	
	public CtrlType getCtrlType(String id){
		if(ctrltypeList == null)
			return null;
		for(CtrlType ct : ctrltypeList){
			if(id.equals(ct.getId()))
				return ct;
		}
		return null;
	}
	
	public List<CtrlType> getCtrltypeList() {
		return ctrltypeList;
	}

	public void setCtrltypeList(List<CtrlType> ctrltypeList) {
		this.ctrltypeList = ctrltypeList;
	}

	public static CtrlFunc parse(InputStream input) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(CtrlFunc.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			CtrlFunc conf = (CtrlFunc) jaxbUnmarshaller.unmarshal(new StringReader(ContextResourceUtil.inputStream2String(input)));
			return conf;
		} catch (Throwable e) {
			throw new LuiRuntimeException(e);
		}
	}
	
}
