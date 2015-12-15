package xap.lui.core.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import xap.lui.core.exception.LuiRuntimeException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * 输入描述器
 * @author 
 *
 */
@XmlRootElement(name="PipeIn")
@XmlAccessorType(XmlAccessType.NONE)
public class PipeIn implements Serializable, Cloneable {
	private static final long serialVersionUID = 6615952170085829290L;
	
	//映射类型
	public static String PLUGINTYPEMAPPING = "mapping";
	
	//事件类型
	public static String PLUGINTYPEEVENT = "event";

	//plugin类型
	private String pluginType = PLUGINTYPEEVENT;
	
	//plugin提交规则
//	private EventSubmitRule submitRule = null;
	
	public String getPluginType() {
		return pluginType;
	}
	public void setPluginType(String pluginType) {
		this.pluginType = pluginType;
	}
    @XmlElement(name="PipeInItem")
	private List<PipeInItem> itemList;
	@XmlAttribute
	private String id;
	public List<PipeInItem> getItemList() {
		return itemList;
	}
	public void setItemList(List<PipeInItem> itemList) {
		this.itemList = itemList;
	}
	
	public void addDescItem(PipeInItem item){
		if(itemList == null)
			itemList = new ArrayList<PipeInItem>();
		this.itemList.add(item);
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

//	public EventSubmitRule getSubmitRule() {
//		return submitRule;
//	}
//
//	public void setSubmitRule(EventSubmitRule submitRule) {
//		this.submitRule = submitRule;
//	}

	
	public Object clone(){
		try {
			PipeIn pluginDesc = (PipeIn)super.clone();
			pluginDesc.itemList = new ArrayList<PipeInItem>();
			
			if (this.itemList != null){
				for (PipeInItem item : this.itemList){
					pluginDesc.addDescItem(item);
				}
				
			}
			
//			if(submitRule != null)
//				pluginDesc.submitRule = (EventSubmitRule) submitRule.clone();
			
			return pluginDesc;
		} catch (CloneNotSupportedException e) {
			throw new LuiRuntimeException(e.getMessage(), e);
		}
	}
}
