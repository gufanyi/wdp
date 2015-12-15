package xap.lui.core.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.listener.EventSubmitRule;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;


/**
 * 输出描述器，一个输出至少包含一个出发时机，可包含0到多个参数
 * @author dengjt
 *
 */
@XmlRootElement(name="PipeOut")
@XmlAccessorType(XmlAccessType.FIELD)
public class PipeOut implements Serializable, Cloneable {
	private static final long serialVersionUID = 6615952170085829290L;
	//普通类型
	public static String PLUGOUTNORMAL = "normal";
	
	//widget弹出类型
	public static String PLUGOUTWIDGET = "widget";

	//plugout类型
	private String plugoutType = PLUGOUTNORMAL;
	
	//submitRule
	private EventSubmitRule submitRule = null;
	
	
	public String getPlugoutType() {
		return plugoutType;
	}
	public void setPlugoutType(String plugoutType) {
		this.plugoutType = plugoutType;
	}
	
	@XmlElement(name="PipeOutItem")
	private List<PipeOutItem> itemList;
	@XmlElement(name="TriggerItem")
	private List<TriggerItem> emitList;
	@XmlAttribute
	private String id;
	public List<PipeOutItem> getItemList() {
		return itemList;
	}
	public void setItemList(List<PipeOutItem> itemList) {
		this.itemList = itemList;
	}
	
	public void addItem(PipeOutItem item){
		if(itemList == null)
			itemList = new ArrayList<PipeOutItem>();
		this.itemList.add(item);
	}
	
	public List<TriggerItem> getEmitList() {
		return emitList;
	}
	public void setEmitList(List<TriggerItem> emitList) {
		this.emitList = emitList;
	}
	
	public void addEmitItem(TriggerItem item) {
		if(emitList == null)
			emitList = new ArrayList<TriggerItem>();
		emitList.add(item);
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public EventSubmitRule getSubmitRule() {
		return submitRule;
	}
	public void setSubmitRule(EventSubmitRule submitRule) {
		this.submitRule = submitRule;
	}
	public Object clone(){
		try {
			PipeOut pipeOut = (PipeOut)super.clone();
			pipeOut.itemList = new ArrayList<PipeOutItem>();
			pipeOut.emitList = new ArrayList<TriggerItem>();
			
			if (this.itemList != null){
				for (PipeOutItem item : this.itemList){
					pipeOut.addItem(item);
				}
			}
			if (this.emitList != null){
				for (TriggerItem item : this.emitList){
					pipeOut.addEmitItem(item);
				}
			}
			if (this.submitRule != null){
				pipeOut.submitRule = (EventSubmitRule)this.submitRule.clone(); 
			}
			return pipeOut;
		} catch (CloneNotSupportedException e) {
			throw new LuiRuntimeException(e.getMessage(), e);
		}
	}
}
