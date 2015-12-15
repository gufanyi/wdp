package xap.lui.core.comps;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="PropertyGroup")
@XmlAccessorType(XmlAccessType.NONE)
public class PropertyGroup extends GridColumnGroup {

	private static final long serialVersionUID = -2763899566483887276L;
	
	public List<IGridColumn> getChildPropertyList() {
		return super.getChildColumnList();
	}

	public void setChildPropertyList(List<IGridColumn> childPropertyList) {
		super.setChildColumnList(childPropertyList);
	}
	
	public void addProperty(IGridColumn prop) {
		super.addColumn(prop);
		List<IGridColumn> propList = new ArrayList<IGridColumn>();
		propList.add(prop);
		((PropertyGridComp)this.grid).generatePropertyDatas(propList, this.getId(), ((PropertyGridComp)this.grid).getPropertyDataset());
	}

}
