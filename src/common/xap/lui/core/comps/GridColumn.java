package xap.lui.core.comps;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import xap.lui.core.constant.StringDataTypeConst;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.render.PCGridCompRender;

/**
 * Grid表头配置类
 * 
 */
@XmlRootElement(name = "Column")
@XmlAccessorType(XmlAccessType.NONE)
public class GridColumn extends LuiElement implements IGridColumn
{
	public static final String COLUMN_BGCOLOR = "COLUMN_BGCOLOR";
	public static final String COLUMN_TEXTCOLOR = "COLUMN_TEXTCOLOR";
	public static final String COLUMN_VISIBLE = "COLUMN_VISIBLE";
	public static final String COLUMN_EDITABLE = "COLUMN_EDITABLE";
	public static final String COLUMN_PRECISION = "COLUMN_PRECISION";
	public static final int DEFAULT_WIDTH = 120;
	private static final long serialVersionUID = -958941388322108998L;
	
	@XmlAttribute
	private String field;
	@XmlAttribute
	private String langDir;
	@XmlAttribute
	private String i18nName;
	@XmlAttribute
	private String text;
	@XmlAttribute
	private int width = 120;
	@XmlAttribute
	private String dataType = StringDataTypeConst.STRING;
	@XmlAttribute(name="isSort")
	private boolean isSort = true; 
	@XmlAttribute(name="isVisible")
	private boolean isVisible = true;
	@XmlAttribute(name="isEdit")
	private boolean isEdit = true;
	@XmlAttribute
	private String columBgColor;
	@XmlAttribute(name="align")
	private String align;   //textAlign文字对齐方式
	@XmlAttribute
	private String textColor;
	@XmlAttribute(name="isFixed")
	private boolean isFixed = false;
	@XmlAttribute
	private String editorType;
	@XmlAttribute
	private String renderType;
	@XmlAttribute
	private String refComboData;
	@XmlAttribute
	private String refNode;
	@XmlAttribute
	private String maxValue;
	@XmlAttribute
	private String minValue;
	@XmlAttribute
	private String precision;
	@XmlAttribute
	private String maxLength;
	@XmlAttribute(name="isRequire")
	private boolean isRequire = true;
	// 是否是合计列
	@XmlAttribute
	private boolean sumCol = false;
	// 该表头是否是自动扩展列
	@XmlAttribute(name="isFitWidth")
	private boolean isFitWidth = false;
	// 该表头是否显示复选框
	@XmlAttribute
	private boolean showCheckBox = true;
	// 合计单元格的自定义渲染
	@XmlAttribute
	private String sumColRenderFunc;
	@XmlAttribute
	private String colmngroup;
	
	protected GridComp grid;
	@XmlAttribute
	private String selfDefRef;
	@XmlAttribute
	private String matchValues;
	@XmlAttribute
	private boolean mergedShown;
	@XmlAttribute
	private String ext1;
	@XmlAttribute
	private String ext2;
	@XmlAttribute
	private String ext3;
	
	private PCGridCompRender render;
	
	public String getMatchValues() {
		return matchValues;
	}
	public void setMatchValues(String matchValues) {
		if(this.matchValues == null && matchValues == null)
			return;
		//if((this.matchValues == null && matchValues != null) || !this.matchValues.equals(matchValues)){
			this.matchValues = matchValues;
			if(LifeCyclePhase.ajax.equals(getPhase())) {
				this.getRender().setMatchValues(matchValues);
			}
//		}
	}
	
	
	private String beforeOpenParam;
	
	public String getBeforeOpenParam() {
		return beforeOpenParam;
	}
	public void setBeforeOpenParam(String beforeOpenParam) {
		if(this.beforeOpenParam == null && beforeOpenParam == null)
			return;
		this.beforeOpenParam = beforeOpenParam;
		if(LifeCyclePhase.ajax.equals(getPhase())) {
			this.getRender().setBeforeOpenParam(beforeOpenParam);
		}
	}
	
	public boolean isRequire() {
		return isRequire;
	}

	public void setRequire(boolean isRequire) {
		this.isRequire = isRequire;
	}

	
	public String getColmngroup() {
		return colmngroup;
	}
	public void setColmngroup(String colmngroup) {
		this.colmngroup = colmngroup;
	}
	public String getColumBgColor() {
		return columBgColor;
	}
	public void setColumBgColor(String columBgColor) {
		if(this.columBgColor != columBgColor) {
			this.columBgColor = columBgColor;
			if(LifeCyclePhase.ajax.equals(getPhase())) {
				this.getRender().setColumnBgColor(this);
			}
		}
	}
	public boolean isEdit() {
		return isEdit;
	}
	public void setEdit(boolean columEditable) {
		if(this.isEdit != columEditable){
			this.isEdit = columEditable;
			if(LifeCyclePhase.ajax.equals(getPhase())) {
				this.getRender().setColumnEditable(this);
			}
		}
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public boolean isVisible() {
		return isVisible;
	}
	public void setVisible(boolean isVisible) {
//		if(this.visible != visible){
//		}
		this.isVisible = isVisible;
		if(LifeCyclePhase.ajax.equals(getPhase())) {
			this.getRender().setColumnVisible(this);
		}
	}
	public String getField() {
		return field;
	}
	public void setField(String keyName) {
		this.field = keyName;
	}
	public String getI18nName() {
		return i18nName;
	}
	public void setI18nName(String showName) {
		this.i18nName = showName;
	}
	public boolean isSort() {
		return isSort;
	}
	public void setSort(boolean isSort) {
		this.isSort = isSort;
	}
	public String getAlign() {
		return align;
	}
	public void setAlign(String align) {
		this.align = align;
	}
	public String getTextColor() {
		return textColor;
	}
	public void setTextColor(String textColor) {
		if(this.textColor != textColor) {
			this.textColor = textColor;
			if(LifeCyclePhase.ajax.equals(getPhase())) {
				this.getRender().setColumnTextColor(this);
			}
		}
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public String getEditorType() {
		return editorType;
	}
	public void setEditorType(String editorType) {
		this.editorType = editorType;
	}
	public String getRefComboData() {
		return refComboData;
	}
	public void setRefComboData(String refDataId) {
		this.refComboData = refDataId;
	}
	public String getRenderType() {
		return renderType;
	}
	public void setRenderType(String renderType) {
		this.renderType = renderType;
	}
	public String getRefNode() {
		return refNode;
	}
	public void setRefNode(String refNode) {
		this.refNode = refNode;
	}
	public String getMaxValue() {
		return maxValue;
	}
	public void setMaxValue(String maxValue) {
		this.maxValue = maxValue;
	}
	public String getMinValue() {
		return minValue;
	}
	public void setMinValue(String minValue) {
		this.minValue = minValue;
	}
	public String getPrecision() {
		return precision;
	}
	
	public void setPrecision(String precision) {
		if(this.precision != precision){
			this.precision = precision;
			if(LifeCyclePhase.ajax.equals(getPhase())) {
				this.getRender().setColumnPrecision(this);
			}
		}
	}
	public String getMaxLength() {
		return maxLength;
	}
	public void setMaxLength(String maxLength) {
		this.maxLength = maxLength;
	}
	public boolean isSumCol() {
		return sumCol;
	}
	public void setSumCol(boolean sumCol) {
		this.sumCol = sumCol;
	}
	public boolean isFixed() {
		return isFixed;
	}
	public void setFixed(boolean isFixed) {
		this.isFixed = isFixed;
	}
	public boolean isRequired() {
		return !isRequire;
	}
	public void setRequired(boolean required) {
		setRequire(!required);
	}
	public boolean isFitWidth() {
		return isFitWidth;
	}
	public void setFitWidth(boolean isFitWidth) {
		this.isFitWidth = isFitWidth;
	}
	
	public void mergeProperties(LuiElement ele) {
		super.mergeProperties(ele);
		GridColumn column = (GridColumn) ele;
		String i18nName = column.getI18nName();
		if(i18nName != null)
			this.setI18nName(i18nName);
		
		boolean autoExpand = column.isFitWidth();
		this.setFitWidth(autoExpand);
		
		boolean sortable = column.isSort();
		this.setSort(sortable);
		boolean visiable = column.isVisible();
		this.setVisible(visiable);
		boolean editable = column.isEdit();
		this.setEdit(editable);
		boolean fixedHeader = column.isFixed();
		this.setFixed(fixedHeader);
		
		String columnBgColor = column.getColumBgColor();
		if(columnBgColor != null)
			this.columBgColor = columnBgColor;
		
		String dataType = column.getDataType();
		if(dataType != null)
			this.dataType = column.getDataType();
		
		String editorType = column.getEditorType();
		if(editorType != null)
			this.editorType = column.getEditorType();
		
//		Map<String, JsEventHandler> handlerMap = column.getEventHandlerMap();
//		if(handlerMap.size() > 0){
//			Iterator<JsEventHandler> it = handlerMap.values().iterator();
//			while(it.hasNext()){
//				this.addEventHandler(it.next());
//			}
//		}
		
		String field = column.getField();
		if(field != null)
			this.field = field;
		String maxLength = column.getMaxLength();
		if(maxLength != null)
			this.maxLength = maxLength;
		
		String maxValue = column.getMaxValue();
		if(maxValue != null)
			this.maxValue = maxValue;
		
		String minValue = column.getMinValue();
		if(minValue != null)
			this.minValue = minValue;
		
		String precision = column.getPrecision();
		if(precision != null)
			this.precision = precision;
		
		String refComboData = column.getRefComboData();
		if(refComboData != null)
			this.refComboData = refComboData;
		
		String refNode = column.getRefNode();
		if(refNode != null)
			this.refNode = refNode;
		
		String renderType = column.getRenderType();
		if(renderType != null)
			this.renderType = renderType;
		
		String align = column.getAlign();
		if(align != null)
			this.align = align;
		
		String textColor = column.getTextColor();
		if(textColor != null)
			this.textColor = textColor;
		
		int width = column.getWidth();
		if(width != -1)
			this.width = width;
	}
	
//	public void addProperty(Property p)
//	{
//		if(p == null)
//			return ;
//		if(columnProperty == null)
//			columnProperty = new HashMap<String, Property>();
//		columnProperty.put(p.getName(), p);
//	}
//	
//	public String getPropertyValueByName(String name)
//	{
//		Property p = getProperty(name);
//		if(p == null)
//			return null;
//		return p.getValue();
//	}
	
//	public Property getProperty(String name)
//	{
//		if(name == null || name.trim().equals(""))
//			return null;
//		if(columnProperty == null)
//			return null;
//		return columnProperty.get(name);
//	}
//	
//	public Property[] getProperties()
//	{
//		if(columnProperty == null)
//			return new Property[0];
//		return columnProperty.values().toArray(new Property[0]);
//	} 
//	
//	public void removeProperty(String name)
//	{
//		if(columnProperty == null || name == null || name.trim().equals(""))
//			return ;
//		columnProperty.remove(name);
//	}
	public String getLangDir() {
		return langDir;
	}
	public void setLangDir(String langDir) {
		this.langDir = langDir;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		if(this.text != text) {
			this.text = text;
			if(LifeCyclePhase.ajax.equals(getPhase())) {
				this.getRender().setColumnText(this);
			}
		}
	}

	public GridComp getGridComp() {
		return this.grid;
	}

	public void setGridComp(GridComp grid) {
		this.grid = grid;
	}

	public boolean isShowCheckBox() {
		return showCheckBox;
	}

	public void setShowCheckBox(boolean showCheckBox) {
		this.showCheckBox = showCheckBox;
	}
	
	public String getSumColRenderFunc() {
		return sumColRenderFunc;
	}

	public void setSumColRenderFunc(String sumColRenderFunc) {
		this.sumColRenderFunc = sumColRenderFunc;
	}

	public String getSelfDefRef() {
		return selfDefRef;
	}

	public void setSelfDefRef(String selfDefRef) {
		this.selfDefRef = selfDefRef;
	}
	public String getExt1() {
		return ext1;
	}
	public void setExt1(String ext1) {
		this.ext1 = ext1;
	}
	public String getExt2() {
		return ext2;
	}
	public void setExt2(String ext2) {
		this.ext2 = ext2;
	}
	public String getExt3() {
		return ext3;
	}
	public void setExt3(String ext3) {
		this.ext3 = ext3;
	}
	public boolean isMergedShown() {
		return mergedShown;
	}
	public void setMergedShown(boolean mergedShown) {
		this.mergedShown = mergedShown;
	}
	public PCGridCompRender getRender() {
		if(this.render == null) {
			this.render =this.getGridComp().getRender();
		}
		return this.render;
	}
}
