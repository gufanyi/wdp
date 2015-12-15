package xap.lui.core.dataset;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;



@XmlRootElement(name="DataList")
@XmlAccessorType(XmlAccessType.NONE)
public class DataList  extends ComboData{
	private static final long serialVersionUID = 960819024151735416L;
	@XmlElement(name="DataItem")
	private List<DataItem> listDataItem;

	public DataItem[] getAllDataItems() {
		if(listDataItem!=null && !listDataItem.isEmpty())
			return listDataItem.toArray(new DataItem[0]);
		return null;
	}
	public void addDataItem(DataItem item) {
		if(item==null)
			return;
		if(listDataItem==null)
			listDataItem=new ArrayList<DataItem>();
		listDataItem.add(item);
		super.addDataItem(item);
	}
	
	public void removeDataItem(String itemId)
	{
		if(itemId == null)
			return;
		if(this.listDataItem != null)
		{
			for(int i = 0; i < this.listDataItem.size(); i++)
			{
				if(this.listDataItem.get(i) instanceof DataItem)
				{
					DataItem combo = (DataItem)this.listDataItem.get(i);
					if(combo.getValue() != null && combo.getValue().equals(itemId))
						this.listDataItem.remove(i);
				}
			}
		}
		super.removeDataItem(itemId);
	}
	public void removeDataItem(DataItem combo)
	{
		if(combo == null)
			return;
		if(this.listDataItem != null)
		{
			Iterator<DataItem> ite = this.listDataItem.iterator();
			while(ite.hasNext()){
				if(ite.next() instanceof DataItem){
				DataItem tmp = (DataItem)ite.next();
				if(tmp.getValue() != null && tmp.getValue().equals(combo.getValue())){
					ite.remove();
					break;
				}
			}
			}
		}
		super.removeDataItem(combo.getValue());
	}
	
	public Object clone()
	{
		DataList combo = (DataList) super.clone();
		if(this.listDataItem != null){
			combo.listDataItem = new ArrayList<DataItem>();
			for(DataItem item : this.listDataItem)
			{
				combo.listDataItem.add((DataItem)item.clone());
			}
		}
		return combo;
	}
	@Override
	public void removeAllDataItems() {
		if(this.listDataItem != null){
			this.listDataItem.clear();
		}
		super.removeAllDataItems();
	}
}
