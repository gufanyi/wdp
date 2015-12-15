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
 * UI状态
 *
 */
//@XmlRootElement(name="UIState")
@XmlAccessorType(XmlAccessType.FIELD)
public class UIState implements Serializable, Cloneable  {
	private static final long serialVersionUID = -1104378195583882355L;
	@XmlAttribute
	private String id;
	@XmlAttribute
	private String name;
	@XmlElement(name="ViewState")
	private List<ViewState> viewStateList;
	
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
	public List<ViewState> getViewStateList() {
		return viewStateList;
	}
	public void setViewStateList(List<ViewState> viewStateList) {
		this.viewStateList = viewStateList;
	}
	public void addViewState(ViewState viewState){
		if(this.viewStateList == null)
			viewStateList = new ArrayList<ViewState>();
		this.viewStateList.add(viewState);
	}
	public ViewState getViewState(String id){
		if (viewStateList == null)
			return null;
		for (ViewState vs : viewStateList) {
			if (id.equals(vs.getViewId())) {
				return vs;
			}
		}
		return null;
	}
	public void removeViewState(ViewState viewState){
		this.viewStateList.remove(viewState);
	}
	public void removeViewState(String id){
		this.viewStateList.remove(this.getViewState(id));
	}
	
	public Object clone(){
		try{
			UIState uIState = (UIState) super.clone();
			uIState.viewStateList = new ArrayList<ViewState>();
			if(viewStateList != null){
				for(ViewState viewState : this.viewStateList){
					uIState.addViewState(viewState);
				}
			}
			return uIState;
		}catch (CloneNotSupportedException e) {
			throw new LuiRuntimeException(e.getMessage(), e);
		}
	}
}
