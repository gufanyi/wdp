package xap.lui.core.comps;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.Row;

@XmlRootElement(name = "Property")
@XmlAccessorType(XmlAccessType.NONE)
public class Property extends GridColumn {

	private static final long serialVersionUID = 1L;
	
	@XmlAttribute
	private Object value;
	
	private Object ext;

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
		Dataset ds = ((PropertyGridComp)this.grid).getPropertyDataset();
		Row row = ds.getRow("id", this.getId());
		if(row!= null) {
			row.setValue(ds.nameToIndex("value"), value);
		}
	}

	public Object getExt() {
		return ext;
	}

	public void setExt(Object ext) {
		this.ext = ext;
		Dataset ds = ((PropertyGridComp)this.grid).getPropertyDataset();
		Row row = ds.getRow("id", this.getId());
		if(row!= null) {
			row.setValue(ds.nameToIndex("ext"), ext);
		}
	}
	
	

}
