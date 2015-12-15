package xap.lui.core.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import xap.lui.core.exception.LuiRuntimeException;
/**
 * view
 * @author wang.liheng
 *
 */
//@XmlRootElement(name="ViewState")
@XmlAccessorType(XmlAccessType.NONE)
public class ViewState implements Serializable, Cloneable {
	private static final long serialVersionUID = 7407425683118993135L;
	@XmlAttribute
	private String viewId;
	@XmlElement(name="CtrlState")
	private List<CtrlState> ctrlStateList;
	
	public String getViewId() {
		return viewId;
	}
	public void setViewId(String viewId) {
		this.viewId = viewId;
	}
	public List<CtrlState> getCtrlStateList() {
		return ctrlStateList;
	}
	public void setCtrlStateList(List<CtrlState> ctrlStateList) {
		this.ctrlStateList = ctrlStateList;
	}
	public void addCtrlState(CtrlState ctrlState){
		if(this.ctrlStateList == null)
			this.ctrlStateList = new ArrayList<CtrlState>();
		this.ctrlStateList.add(ctrlState);
	}
	public void removeCtrlState(CtrlState ctrlState){
		this.ctrlStateList.remove(ctrlState);
	}
	public void removeCtrlState(String id){
		for (int i=0; i<ctrlStateList.size(); i++){//若存在子元素，将其移除
			if (id.equals(ctrlStateList.get(i).getPid())) {
				this.ctrlStateList.remove(ctrlStateList.get(i));
				i--;
			}
		}
		this.ctrlStateList.remove(this.getCtrlState(id));
	}
	public CtrlState getCtrlState(String id) {
		if (ctrlStateList == null)
			return null;
		for (CtrlState cs : ctrlStateList) {
			if (id.equals(cs.getId())) {
				return cs;
			}
		}
		return null;
	}
	
	public Object clone(){
		try{
			ViewState viewState = (ViewState) super.clone();
			viewState.ctrlStateList = new ArrayList<CtrlState>();
			if(ctrlStateList != null){
				for(CtrlState ctrlState : this.ctrlStateList){
					viewState.addCtrlState(ctrlState);
				}
			}
			return viewState;
		}catch (CloneNotSupportedException e) {
			throw new LuiRuntimeException(e.getMessage(), e);
		}
	}
}
