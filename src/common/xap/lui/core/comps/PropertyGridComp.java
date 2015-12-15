package xap.lui.core.comps;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import xap.lui.core.constant.StringDataTypeConst;
import xap.lui.core.control.PropertyDatasetCtrl;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.Field;
import xap.lui.core.dataset.Row;
import xap.lui.core.event.DatasetEvent;
import xap.lui.core.listener.EventSubmitRule;
import xap.lui.core.listener.LuiEventConf;
import xap.lui.core.render.PCPropertyGridCompRender;
import xap.lui.core.render.notify.RenderProxy;

import com.alibaba.fastjson.annotation.JSONField;

@XmlRootElement(name = "PropertyGrid")
@XmlAccessorType(XmlAccessType.NONE)
public class PropertyGridComp extends GridComp {
	
	private static final long serialVersionUID = -7657610877000700825L;
	
	public static final String PROPERTYDATASET_PREFIX = "proprtyGridCompDataset__";
	public static final String WIDGET_NAME = "propertygrid";
	private static final String[] PROPERTY_FIELD = {"id", "text","value","dataType","editorType",
		"renderType","refComboData","refNode","maxValue","minValue","precision",
		"maxLength","textAlign","ext"};
	private static final String[] PROPERTY_DATASET_FIELD = {"id","pid","text","value", "type" ,"dataType","editorType",
			"renderType","refComboData","refNode","maxValue","minValue","precision",
			"maxLength","isRequire","textAlign","ext"};
	
	//属性名称文字
	@XmlAttribute
	private String propNameText;
	@XmlAttribute
	//属性值文字
	private String propValueText;
	
	@JSONField(serialize = false)
	private PCPropertyGridCompRender render;
	
	@JSONField(serialize = false)
	private Dataset propertyDataset;
	
	@Override
	public String getWidgetName() {
		return WIDGET_NAME;
	}
	
	
	public String getPropNameText() {
		return propNameText;
	}

	public void setPropNameText(String propNameText) {
		this.propNameText = propNameText;
	}



	public String getPropValueText() {
		return propValueText;
	}



	public void setPropValueText(String propValueText) {
		this.propValueText = propValueText;
	}



	public List<IGridColumn> getPropertyList() {
		return super.getColumnList();
	}
	
	public IGridColumn getPropertyById(String id) {
		return super.getColumnById(id);
	}
	

	public IGridColumn getProperty(int index) {
		return super.getColumn(index);
	}

	public IGridColumn getPropertyByField(String field) {
		return super.getColumnByField(field);
	}
	
	/**
	 * 增加属性
	 * @param col gridCoulumn
	 */
	public void addProperty(IGridColumn prop) {
		if (this.columnList == null) {
			this.columnList = new ArrayList<IGridColumn>();
		}
		this.columnList.add(prop);
		prop.setGridComp(this);
		
		Dataset ds = this.getPropertyDataset();
		Row row = ds.getEmptyRow();
		if(prop instanceof Property) {
			this.setPropertyDatasetValues((Property)prop, row, ds);
			row.setString(ds.nameToIndex("type"), "property");
			row.setBoolean(ds.nameToIndex("isRequire"), ((Property) prop).isRequire());
			ds.addRow(row);
		} else {
			this.setPropertyDatasetValues((PropertyGroup)prop, row, ds);
			row.setString(ds.nameToIndex("type"), "propertyGroup");
			row.setEdit(false);
			ds.addRow(row);
			List<IGridColumn> children = ((PropertyGroup)prop).getChildPropertyList();
			if(!CollectionUtils.isEmpty(children)) {
				generatePropertyDatas(children,prop.getId(),ds);
			}
		}
	}

	public void addProperties(List<IGridColumn> properties) {
		for (IGridColumn prop : properties)
			addProperty(prop);
	}
	
	public void removeProperty(IGridColumn prop) {
		columnList.remove(prop);
		Dataset ds = this.getPropertyDataset();
		ds.removeRow(ds.getRow("id", prop.getId()));
	}

	public void removeProperties(List<IGridColumn> properties) {
		for (int i = properties.size() - 1; i >= 0; i--) {
			IGridColumn col = properties.get(i);
			removeProperty(col);
		}
	}
	
	public Property getSelectProperty() {
		Dataset ds = this.getPropertyDataset();
		Row row = ds.getSelectedRow();
		if(row!=null) {
			return (Property)this.getPropertyById(row.getString(ds.nameToIndex("id")));
		}
		return null;
	}


	public PCPropertyGridCompRender getRender() {
		if (this.render == null) {
			render = RenderProxy.getRender(new PCPropertyGridCompRender(this));
		}
		return this.render;
	}
	
	
	public Dataset getPropertyDataset() {
		if(propertyDataset == null) {
			propertyDataset = createPropDataset();
		}
		return propertyDataset;
	}

	private Dataset createPropDataset() {
		Dataset propDataset = new Dataset(PROPERTYDATASET_PREFIX + this.getId());
		propDataset.setEdit(true);
		
		for(String property : PROPERTY_DATASET_FIELD) {
			propDataset.addField(generateField(property));
		}
		
		EventSubmitRule sr = new EventSubmitRule();
		{
			LuiEventConf event = new LuiEventConf();
			event.setEventType(DatasetEvent.class.getSimpleName());
			event.setOnserver(true);
			event.setSubmitRule(sr);
			event.setEventName("onDataLoad");
			event.setControllerClazz(PropertyDatasetCtrl.class.getName());
			event.setMethod("prop_ondataload");
			propDataset.addEventConf(event);
		}
		{
			LuiEventConf event = new LuiEventConf();
			event.setEventType(DatasetEvent.class.getSimpleName());
			event.setOnserver(true);
			event.setSubmitRule(sr);
			event.setEventName("onBeforeDataChange");
			event.setControllerClazz(PropertyDatasetCtrl.class.getName());
			event.setMethod("prop_ondatachange");
			propDataset.addEventConf(event);
		}
		
		return propDataset;
	}
	
	private Field generateField(String id) {
		Field field = new Field();
		field.setId(id);
		field.setText(id);
		field.setDataType(StringDataTypeConst.STRING);
		return field;
	}
	
	public void generatePropertyDatas(List<IGridColumn> propList, String pid, Dataset ds) {
		for(IGridColumn prop : propList) {
			Row row = ds.getEmptyRow();
			row.setString(ds.nameToIndex("pid"), pid);
			if(prop instanceof Property) {
				setPropertyDatasetValues((Property)prop, row, ds);
				row.setString(ds.nameToIndex("type"), "property");
				row.setBoolean(ds.nameToIndex("isRequire"), ((Property) prop).isRequire());
				ds.addRow(row);
			} else {
				setPropertyDatasetValues((PropertyGroup)prop, row, ds);
				row.setString(ds.nameToIndex("type"), "propertyGroup");
				row.setEdit(false);
				ds.addRow(row);
				List<IGridColumn> children = ((PropertyGroup)prop).getChildPropertyList();
				if(!CollectionUtils.isEmpty(children)) {
					generatePropertyDatas(children,prop.getId(),ds);
				}
			}
		}
	}
	
	private void setPropertyDatasetValues(IGridColumn prop, Row row, Dataset ds) {
		for(String propField : PROPERTY_FIELD) {
			String method = "get" + StringUtils.capitalize(propField);
			try {
				Method met = getDeclaredMethod(prop,method);
				if(met != null) {
					String val = String.valueOf(met.invoke(prop));
					row.setValue(ds.nameToIndex(propField), val);
				}
			} catch (SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			}
		}
		
	}

	private Method getDeclaredMethod(Object object, String methodName) {
		Method method = null;
		for (Class<?> clazz = object.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
			try {
				method = clazz.getDeclaredMethod(methodName);
				return method;
			} catch (Exception e) {
			}
		}
		return null;
	}
}
