package xap.lui.core.dataset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="MDDataList")
@XmlAccessorType(XmlAccessType.NONE)
public class MDComboDataConf extends ComboData {
	private static final long serialVersionUID = 1L;
	@XmlAttribute
	private String fullclassName;
	private List<DataItem> listCombItem;
	public String getFullclassName() {
		return fullclassName;
	}
	public void setFullclassName(String fullclassName) {
		this.fullclassName = fullclassName;
	}
	@Override
	public DataItem[] getAllDataItems() {
		if (this.listCombItem == null) {

		}
		return this.listCombItem.toArray(new DataItem[0]);
	}
	public void removeDataItem(String itemId) {
		if (itemId == null)
			return;
		if (this.listCombItem == null)
			getAllDataItems();
		Iterator<DataItem> it = this.listCombItem.iterator();
		while (it.hasNext()) {
			DataItem combo = it.next();
			if (combo.getValue() != null && combo.getValue().equals(itemId)) {
				it.remove();
				super.removeDataItem(itemId);
				break;
			}
		}
	}
	@Override
	public void removeAllDataItems() {
		if (listCombItem == null) {
			listCombItem = new ArrayList<DataItem>();
			return;
		}
		listCombItem.clear();
		super.removeAllDataItems();
	}
	@Override
	public void addDataItem(DataItem item) {
		if (this.listCombItem == null)
			getAllDataItems();
		this.listCombItem.add(item);
		super.addDataItem(item);
	}
	public Object clone() {
		MDComboDataConf combo = (MDComboDataConf) super.clone();
		if (this.listCombItem != null) {
			combo.listCombItem = new ArrayList<DataItem>();
			for (DataItem item : this.listCombItem) {
				combo.listCombItem.add((DataItem) item.clone());
			}
		}
		return combo;
	}
}
