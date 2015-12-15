package xap.lui.psn.formula;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.NONE)
public class Category {

	@XmlAttribute
	private String id;
	
	@XmlAttribute
	private String name;
	
	@XmlElement(name = "item")
	private List<Item> itemList = null;
	
	public void addItem(Item item) {
		if (this.itemList == null) {
			this.itemList = new ArrayList<Item>();
		}
		this.itemList.add(item);
		item.setCategory(this);
		List<Item> childitmList = item.getChildItemList();
		if (childitmList != null && childitmList.size() > 0){
			for(Item it : childitmList){
				it.setCategory(this);
			}
		}
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
	public List<Item> getItemList() {
		return itemList;
	}
	public void setItemList(List<Item> itemList) {
		this.itemList = itemList;
	}
}
