package xap.lui.psn.formula;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import org.eclipse.persistence.oxm.annotations.XmlCDATA;

@XmlAccessorType(XmlAccessType.NONE)
public class Item {

	@XmlAttribute
	private String id;
	@XmlAttribute
	private String name;
	@XmlAttribute
	private String desc;
	@XmlElement
	@XmlCDATA
	private String demo;
	
	private Category category;
	
	@XmlElement(name="item")
	private List<Item> childItemList = null;
	
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
	
	public List<Item> getChildItemList() {
		return childItemList;
	}
	public void setChildItemList(List<Item> childItemList) {
		this.childItemList = childItemList;
	}
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
		List<Item> chItem = this.getChildItemList();
		if (chItem != null && chItem.size() > 0){
			for(Item it : chItem){
				it.setCategory(category);
			}
		}
	}
	
}
