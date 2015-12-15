package xap.lui.core.control;

import java.util.List;

import xap.lui.core.comps.IGridColumn;
import xap.lui.core.comps.Property;
import xap.lui.core.comps.PropertyGridComp;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.Row;
import xap.lui.core.event.DatasetCellEvent;
import xap.lui.core.event.DatasetEvent;
import xap.lui.core.model.LuiAppUtil;

public class PropertyDatasetCtrl {
	
	public void prop_ondataload(DatasetEvent e) {
		Dataset dataset = e.getSource();
		PropertyGridComp propGrid = getPropertyGridComp(dataset);
		List<IGridColumn> propList = propGrid.getPropertyList();
		propGrid.generatePropertyDatas(propList,null,dataset);
	}	
	
	public void prop_ondatachange(DatasetCellEvent e) {
		Dataset dataset = e.getSource();
		PropertyGridComp propGrid = getPropertyGridComp(dataset);
		Row row = dataset.getSelectedRow();
		if(row!=null) {
			Property prop = (Property)propGrid.getPropertyById(row.getString(dataset.nameToIndex("id")));
			prop.setValue(row.getValue(dataset.nameToIndex("value")));
			prop.setExt(row.getValue(dataset.nameToIndex("ext")));	
		}
	}
	
	private PropertyGridComp getPropertyGridComp(Dataset ds) {
		String id = ds.getId();
		String gridId = id.substring(PropertyGridComp.PROPERTYDATASET_PREFIX.length());
		return (PropertyGridComp)LuiAppUtil.getControl(gridId, ds.getWidget().getId());
	}
	
}
