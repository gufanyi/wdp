package xap.lui.core.dataset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.ObjectUtils;

import xap.lui.core.common.LuiRenderContext;
import xap.lui.core.comps.IDetachable;
import xap.lui.core.comps.ViewElement;
import xap.lui.core.constant.DataTypeTranslator;
import xap.lui.core.constant.IntDataTypeConst;
import xap.lui.core.exception.LuiPluginException;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.layout.UIElementFinder;
import xap.lui.core.layout.UIPartMeta;
import xap.lui.core.layout.UIViewPart;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.core.render.PCViewLayOutRender;
import xap.lui.core.render.notify.RenderProxy;

import com.alibaba.fastjson.annotation.JSONField;
/**
 * 数据载体，包含结构信息（元数据信息）即fieldList,FieldRelation等，另外 包含数据部分，即pageDatas部分。
 * 
 */
@XmlRootElement(name = "Dataset")
@XmlAccessorType(XmlAccessType.NONE)
public class Dataset extends ViewElement implements IDetachable {
	
	private static final long serialVersionUID = 71816449687525005L;
	
	public static int ALL_NOT_SAVE = 2;
	public static int ALL_SAVE = 1;
	public static int UPDATE_SAVE = 0;
	public static final String WIDGET_NAME = "";
	public static final String MASTER_KEY = "MASTER_KEY";
	public static final String FROM_DO = "DO";
	public static final String FROM_NC = "NC";
	public static final String CODE_LEVEL_CLAZZ = "$codelevelclazz";
	public static final String CODE_LEVEL_PPK = "$codelevelppk";
	public static final String CODE_LEVEL_PK = "$codelevelpk";
	public static final String CODE_LEVEL_RULE = "$coderule";
	public static final String CODE_LEVEL_CODEFIELD = "$codefield";
	// 编码树自动增加父列名称
	public static final String PARENT_PK_COLUMN = "$PARENT_PK";
	/* 分页信息 */
	private PaginationInfo paginationInfo = new PaginationInfo();
	/* 记录集信息 */
	private Map<Integer, PageData> pageDatas = new HashMap<Integer, PageData>();
	/* field信息 */
	@XmlElementWrapper(name = "Fields")
	@XmlElementRefs({ @XmlElementRef(name = "ModifyField", type = ModifyField.class),
		              @XmlElementRef(name = "PubField", type = PubField.class),
		              @XmlElementRef(name = "Field", type = Field.class)
    })	
	private List<Field> fieldList = new ArrayList<Field>();
	/* 关联数据信息 */
	@XmlElement(name = "FieldRelations")
	private FieldRelations fieldRelations = new FieldRelations();
	/* 请求参数集合 */
	private ParameterSet reqParameters = new ParameterSet();
	/* 应答参数集合 */
	private ParameterSet resParameters = new ParameterSet();
	/* 是否缓加载 */
	@XmlAttribute
	private boolean isLazyLoad = false;
	@XmlAttribute
	private int pageSize = -1;
	/* VO类名 */
	@XmlAttribute
	protected String voMeta;
	@XmlAttribute
	private String tableName;
	@XmlAttribute
	private boolean isEdit = false;
	// 行数量计数器，用于产生行随机ID
	private int randomRowIndex = 0;
	// 客户端是否维持分页,由业务判断,维持分页将增加客户端的内存使用量，不维持则无法获取之前页面的选中情况等信息
	private boolean stickPage = true;
	@XmlAttribute
	private String refId;

	// 标识此次服务端响应操作是否执行过dataset的clear方法
	@XmlAttribute
	private boolean isCleared = false;
	// 当前聚焦行
	@XmlAttribute
	private int focusIndex = -1;
	@XmlAttribute
	private String caption;
	
	private PCViewLayOutRender render = null;

	public PCViewLayOutRender getRender() {
		if (render == null) {
			ViewPartMeta  webElement=  this.getWidget();
	        UIPartMeta  uiPartMeta=LuiRenderContext.current().getUiPartMeta();
	        UIViewPart uiEle = UIElementFinder.findUIWidget(uiPartMeta, webElement.getId());
			render = RenderProxy.getRender(new PCViewLayOutRender(uiEle));
		}
		return render;
	}

	
	public Dataset() {
		this("");
	}
	
	public Dataset(String id) {
		super(id);
	}
	
	@Override
	public String getWidgetName() {
		return WIDGET_NAME;
	}
	
	/**************************PageData数据**********************************/
	@JSONField(serialize = false)
	public PageData getCurrentPageData() {
		if (pageDatas.size() == 0){
			int index = paginationInfo.getPageIndex();
			PageData pageData = new PageData(index);
			pageDatas.put(index, pageData);
			return pageData;
		}else{
			//真分页 进行页数的增加  数据会缓存在前台
			PageData  pageData = pageDatas.get(paginationInfo.getPageIndex());
			if (pageData == null) {
				int index = paginationInfo.getPageIndex();
				pageData = new PageData(index);
				pageDatas.put(index, pageData);
			}
			return pageData;
		}
	}
	
	
	public Row[] getCntPageRows(){
		return this.getCurrentPageData().getRows();
	}
	
	public Row getRowByIndex(int index){
	   Row[] rows=	this.getCntPageRows();
	   if(rows.length>index){
		   return rows[index];
	   }
	   return null;
	}

	public void addPageData(Integer pageIndex, PageData pageData) {
		pageDatas.put(pageIndex, pageData);
	}
	
	public PageData getCurrentIndexPageData(Integer pageIndex) {
		return pageDatas.get(pageIndex);
	}
	
	/**********************clear  clone   detach**************************************/
	public void clear() {
		this.setCtxChanged(true);
		this.reqParameters.clear();
		this.resParameters.clear();
		this.focusIndex = -1;
		this.isCleared = true;
		for(PageData pageData:pageDatas.values()){
			pageData.clear();
		}
		paginationInfo.setPageCount(1);
		paginationInfo.setRecordsCount(0);
		paginationInfo.setPageIndex(0);
	}
	
	public Object clone() {
		
		Dataset ds = (Dataset) super.clone();
		ds.reqParameters = (ParameterSet) this.reqParameters.clone();
		ds.resParameters = (ParameterSet) this.resParameters.clone();
		ds.paginationInfo = (PaginationInfo)this.paginationInfo.clone();
		
		List<Field> fields = new ArrayList<Field>();
		for(int i = 0;i < this.getFieldCount(); i++)
		{
			fields.add((Field)getField(i).clone());
		}
		ds.fieldList = fields;
		
		Map<Integer, PageData>  rds = new HashMap<Integer, PageData>();
		Iterator<Entry<Integer, PageData>> it = this.pageDatas.entrySet().iterator();
		while (it.hasNext()) {
			Entry<Integer, PageData> entry = it.next();
			PageData newRd = (PageData) entry.getValue().clone();
			rds.put(entry.getKey(), newRd);
		}
		
		ds.pageDatas = rds;
		return ds;
	}
	
	
	@Override
	public void detach() {
		pageDatas.clear();
		paginationInfo.setPageCount(1);
		paginationInfo.setRecordsCount(0);
		paginationInfo.setPageIndex(0);
		reqParameters.clear();
		resParameters.clear();
		this.setCtxChanged(true);
	}
	/**
	 * 该方法用于生成前后台交互所用的recordId，前台在交互过程中通过它来 建立记录的对应关系。产生规则： 0/1 + datasetId +
	 * random(10位) 其中由后台产生的record id，第一位是 0 由前台产生的record id，第一位是 1
	 */
	@JSONField(serialize = false)
	public String getRandomRowId() {
		return "0" + this.getId() + this.randomRowIndex;
	}
	

	public Row getRowById(String id) {
		PageData rd = getCurrentPageData();
		Row row = rd.getRowById(id);
		if (row != null)
			return row;
		PageData[] rds = this.getAllPageData();
		for (int i = 0; i < rds.length; i++) {
			row = rds[i].getRowById(id);
			if (row != null)
				return row;
		}
		return null;
	}
	
	/**
	 * 分页用
	 * @return
	 */
	@JSONField(serialize = false)
	public String getLastCondition() {
		return (String) this.getExtendAttributeValue("LAST_CONDITION");
	}
	public void setLastCondition(String condition) {
		setLastCondition(false, condition);
	}
	public void setLastCondition(boolean parent, String condition) {
		this.setExtendAttribute("LAST_CONDITION", condition);//  TODO  不知道含义
	}
	
	public void validate() {
		StringBuffer buffer = new StringBuffer();
		if (this.getId() == null || this.getId().equals("")) {
			buffer.append("数据集的ID不能为空!");
		}
		if (buffer.length() > 0)
			throw new LuiPluginException(buffer.toString());
	}
	
	public void setSelectedIndex(int rowIndex) {
		setRowSelectIndex(rowIndex);
	}
	

	
	/********************状态控制*******************************/
	@JSONField(serialize = false)
	public boolean isCtxChanged() {
		PageData[] rds = getAllPageData();
		for (int i = 0; i < rds.length; i++) {
			if (rds[i].isPageDataChanged())
				return true;
		}
		return super.isCtxChanged();
	}
	
	@JSONField(serialize = true)
	public boolean isEdit() {
		return isEdit;
	}
	public void setEdit(boolean isEdit) {
		if (this.isEdit != isEdit) {
			this.isEdit = isEdit;
			setCtxChanged(true);
		}
	}
	
	
	/***********************行操作**************************/
	//获取空行
	@JSONField(serialize = false)
	public Row getEmptyRow() {
		return getEmptyRow(Row.STATE_ADD);
	}
	//带状态的
	public Row getEmptyRow(int state) {
		this.randomRowIndex++;
		int fieldCount = this.getFieldCount();
		String id = getRandomRowId();
		Row row = new Row(id, fieldCount);
		row.setState(state);
		for (int i = 0; i < fieldCount; i++) {
			Field f = this.getField(i);
			Object defaultValue = f.getDefaultValue();
			if (defaultValue != null) {
				row.setValue(i, defaultValue);
			} else {
				int dataType = DataTypeTranslator.translateString2Int(f.getDataType());
				if (IntDataTypeConst.FBoolean_TYPE == dataType || IntDataTypeConst.boolean_TYPE == dataType || IntDataTypeConst.Boolean_TYPE == dataType)
					row.setValue(i, new Boolean(false));
			}
		}
		return row;
	}
	

	/**
	 * 获取当前页的行数	
	 * @return
	 */
	@JSONField(serialize = false)
	public int getCurrentPageRowCount() {
		PageData rd = getCurrentPageData();
		if (rd == null)
			return 0;
		return rd.getCurrentPageRowCount();
	}
	/**
	 * 获取总行数	
	 * @return
	 */
	@JSONField(serialize = false)
	public int getAllRowCount() {
//		return paginationInfo.getRecordsCount();
		int count = 0;
		PageData[] pageDataS = pageDatas.values().toArray(new PageData[0]);
		for (PageData pageData : pageDataS) {
			count += pageData.getCurrentPageRowCount();
		}
		return count; 
	}
	
	@JSONField(serialize = false)
	public Row getSelectedRow() {
		if (pageDatas.size() != 0){
			PageData pageData = pageDatas.get(paginationInfo.getPageIndex());
			if (null != pageData)
				return pageData.getSelectedRow();
		}
		return null;
	}
	
	@JSONField(serialize = false)
	public Row getFocusRow() {
		if (pageDatas.size() != 0){
			PageData pageData = pageDatas.get(paginationInfo.getPageIndex());
			if (null != pageData && focusIndex != -1 && pageData.getCurrentPageRowCount() >= focusIndex + 1)
				return pageData.getRow(focusIndex);
		}
			return null;
	}
	
	@JSONField(serialize = false)
	public Row[] getStatusRows(int state) {
		PageData pageData = this.getCurrentPageData();
		if (null != pageData){
			List<Row> rowList = new ArrayList<Row>();
			for (Row r : pageData.getRows()) {
				if (state == r.getState()) {
					rowList.add(r);
				}
			}
			return rowList.toArray(new Row[0]);
		}
		return null;
	}
	
	@JSONField(serialize = false)
	public Row[] getChangedRows() {
		PageData pageData = this.getCurrentPageData();
		if (null != pageData){
			List<Row> rowList = new ArrayList<Row>();
			for (Row r : pageData.getRows()) {
				if (Row.STATE_ADD == r.getState() || Row.STATE_UPDATE == r.getState()) {
					rowList.add(r);
				}
			}
			return rowList.toArray(new Row[0]);
		}
		return null;
		
	}
	
	@JSONField(serialize = false)
	public Row[] getAllStatusRows(int state) {
		List<Row> allRowList = new ArrayList<Row>();
		PageData[] pageDatas = this.getAllPageData();
		for (int j = 0; j < pageDatas.length; j++) {
			PageData pageData = pageDatas[j];
			for (Row r : pageData.getRows()) {
				if (state == r.getState()) {
					allRowList.add(r);
				}
			}
		}
		return allRowList.toArray(new Row[0]);
	}
	
	@JSONField(serialize = false)
	public Row[] getAllChangedRows() {
		List<Row> allRowList = new ArrayList<Row>();
		PageData[] pageDatas = this.getAllPageData();
		for (int j = 0; j < pageDatas.length; j++) {
			PageData pageData = pageDatas[j];
			for (Row r : pageData.getRows()) {
				if (Row.STATE_ADD == r.getState() || Row.STATE_UPDATE == r.getState()) {
					allRowList.add(r);
				}
			}
		}
		return allRowList.toArray(new Row[0]);
	}
	
	@JSONField(serialize = false)
	public Row[] getCurrentPageSelectedRows() {
		if (this.pageDatas.size() != 0){
			PageData pageData = this.getCurrentPageData();
			if (null != pageData)
				return this.getCurrentPageData().getSelectedRows();
		}
		return null;
	}
	
	@JSONField(serialize = false)
	public Row[] getAllSelectedRows() {
		List<Row> allRowList = new ArrayList<Row>();
		PageData[] pageDatas = this.getAllPageData();
		for (int j = 0; j < pageDatas.length; j++) {
			PageData pageData = pageDatas[j];
			Row[] selRows = pageData.getSelectedRows();
			if (selRows != null)
				allRowList.addAll(Arrays.asList(selRows));
		}
		return allRowList.toArray(new Row[0]);
	}
	
	public Object getRowValue(String key) {
		Row r = getSelectedRow();
		if (r == null)
			return null;
		int index = this.nameToIndex(key);
		if (index == -1)
			throw new IllegalArgumentException("不存在key:" + key);
		return r.getValue(index);
	}
	
	public Object getRowValue(int index) {
		Row r = getSelectedRow();
		if (r == null)
			return null;
		if (index < 0 || index > this.getFieldCount() - 1)
			throw new IllegalArgumentException("index 越界:" + index);
		return r.getValue(index);
	}
	
	public void setRowValue(String key, String value) {
		Row r = getSelectedRow();
		if (r == null) {
			throw new LuiRuntimeException("没有选中行");
		}
		int index = this.nameToIndex(key);
		if (index == -1)
			throw new IllegalArgumentException("不存在key:" + key);
		r.setValue(index, value);
	}
	
	public void setRowValue(int index, String value) {
		Row r = getSelectedRow();
		if (r == null) {
			throw new LuiRuntimeException("没有选中行");
		}
		if (index < 0 || index > this.getFieldCount() - 1)
			throw new IllegalArgumentException("index 越界:" + index);
		r.setValue(index, value);
	}

	/**
	 * 设置所有行的edit状态
	 * 
	 * @param isEdit
	 */
	public void setAllRowsEdit(boolean isEdit) {
		PageData[] pageDatas = this.getAllPageData();
		for (PageData pageData : pageDatas) {
			Row[] rows = pageData.getRows();
			for (Row row : rows) {
				row.setEdit(isEdit);
			}
		}
	}
	
	
	@JSONField(serialize = false)
	public Integer getSelectedIndex() {
		Integer[] indices = getSelectedIndices();
		if (indices == null || indices.length == 0)
			return -1;
		return indices[0];
	}
	
	@JSONField(serialize = false)
	public Integer[] getSelectedIndices() {
		PageData pageData = this.getCurrentPageData();
		if (null != pageData) {
			Integer[] indices = this.getCurrentPageData().getSelectIndices();
			return indices;
		} else
			return null;
	}
	public void setRowSelectIndex(Integer index) {
		getCurrentPageData().setRowSelectIndex(index);
		setFocusIndex(index);
	}
	@JSONField(serialize = false)
	public void setRowUnSelect() {
		getCurrentPageData().setRowSelectIndices(null);
		setFocusIndex(-1);
	}
	public void setAllRowUnSelect() {
		PageData[] pageDatas = this.getAllPageData();
		for (int j = 0; j < pageDatas.length; j++) {
			PageData pageData = pageDatas[j];
			pageData.setRowSelectIndices(null);
		}
		setFocusIndex(-1);
	}
	public void setRowSelectIndices(Integer[] indices) {
		getCurrentPageData().setRowSelectIndices(indices);
	}
	
	public void removeRow(Row row) {
		this.getCurrentPageData().removeRow(row);
		this.setFocusIndex(-1);
	}
	
	public void removeRow(int index) {
		this.getCurrentPageData().removeRow(index);
		this.setFocusIndex(-1);
	}
	public void removeRow(Row row, boolean isTrueRemove) {
		this.getCurrentPageData().removeRow(row, isTrueRemove);
		this.setFocusIndex(-1);
	}
	public void removeRow(int index, boolean isTrueRemove) {
		this.getCurrentPageData().removeRow(index, isTrueRemove);
		this.setFocusIndex(-1);
	}
	
	public void addRow(Row row) {
		PageData rd = this.getCurrentPageData();
		rd.addRow(row);
	}
	
	public void insertRow(int index, Row row) {
		PageData rd = this.getCurrentPageData();
		rd.insertRow(index, row);
	}
	
	public int getRowIndex(Row row) {
		if (this.pageDatas == null)
			return -1;
		PageData pageData = this.getCurrentPageData();
		if (pageData == null)
			return -1;
		return pageData.getRowIndex(row);
	}
	
	/***************************getset方法*******************************/
	@JSONField(serialize = false)
	public boolean isLazyLoad() {
		return isLazyLoad;
	}
	public void setLazyLoad(boolean isLazyLoad) {
		this.isLazyLoad = isLazyLoad;
	}
	
	public Parameter getReqParameter(String key) {
		return this.reqParameters.getParameter(key);
	}

	@JSONField(serialize = true)
	public ParameterSet getResParameters() {
		return this.resParameters;
	}
	public void setReqParameters(ParameterSet reqParams) {
		this.reqParameters = reqParams;
	}
	@JSONField(serialize = false)
	public ParameterSet getReqParameters() {
		return this.reqParameters;
	}
	public void setResParameters(ParameterSet resParams) {
		this.resParameters = resParams;
	}
	
	@JSONField(serialize = false)
	public FieldRelations getFieldRelations() {
		return fieldRelations;
	}
	public void setFieldRelations(FieldRelations fieldRelations) {
		this.fieldRelations = fieldRelations;
	}
	@JSONField(serialize = false)
	public Row[] getFalseDeleteRows() {
		return getCurrentPageData().getFalseDeleteRows();
	}
	@JSONField(serialize = false)
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		//设置分页信息  并且设置在paginationInfo对象中
		this.pageSize = pageSize;
//		this.paginationInfo.setPageSize(pageSize);
	}
	@JSONField(serialize = false)
	public String getVoMeta() {
		return voMeta;
	}
	public void setVoMeta(String voMeta) {
		this.voMeta = voMeta;
	}
	
	@JSONField(serialize = false)
	public boolean isStickPage() {
		return stickPage;
	}
	public void setStickPage(boolean stickPage) {
		this.stickPage = stickPage;
	}
	@JSONField(serialize = false)
	public String getRefId() {
		return refId;
	}
	public void setRefId(String refId) {
		this.refId = refId;
	}

	@JSONField(serialize = true)
	public boolean isCleared() {
		return isCleared;
	}
	public void setCleared(boolean isCleared) {
		this.isCleared = isCleared;
	}
	
	@JSONField(serialize = false)
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	@JSONField(serialize = true)
	public int getRandomRowIndex() {
		return randomRowIndex;
	}
	public void setRandomRowIndex(int randomRowIndex) {
		this.randomRowIndex = randomRowIndex;
	}
	
	@JSONField(serialize = false)
	public String getCaption() {
		return caption;
	}
	public void setCaption(String caption) {
		this.caption = caption;
	}
	
	@JSONField(serialize = true)
	public int getFocusIndex() {
		return focusIndex;
	}
	public void setFocusIndex(int focusIndex) {
		this.focusIndex = focusIndex;
		setCtxChanged(true);
	}
		
	@JSONField(serialize = true)
	public PaginationInfo getPaginationInfo() {
		return paginationInfo;
	}
	public void setPaginationInfo(PaginationInfo paginationInfo) {
		this.paginationInfo = paginationInfo;
	}
	
	public PageData getCurrentPageData(Integer pageIndex) {
		return pageDatas.get(pageIndex);
	}

	public void removepageData(Integer pageIndex) {
		pageDatas.remove(pageIndex);
	}
	
	@JSONField(serialize = true, name = "pageSet")
	public PageData[] getAllPageData() {
		return pageDatas.values().toArray(new PageData[0]);
	}

	public void removeRowInAllRowSets(Row row) {
		PageData[] pageDatas = this.getAllPageData();
		for (int j = 0; j < pageDatas.length; j++) {
			PageData pageData = pageDatas[j];
			int index = pageData.getRowIndex(row);
			if (index != -1) {
				pageData.removeRow(row);
				return;
			}
	     }
    }
	/****************************原FieldSet属性***************************************/
	/**
	 * 获取指定位置的字段信息
	 * 
	 * @param index
	 * @return
	 */
	public Field getField(int index) {

		return fieldList.get(index);
	}
	/**
	 * 获取指定标识（Id）的字段信息
	 * 
	 * @param s
	 * @return
	 */
	public Field getField(String id) {
		for (int i = 0; i < fieldList.size(); i++) {
			if (fieldList.get(i).getId().trim().equals(id))
				return fieldList.get(i);
		}
		return null;
	}
	
	public Field[] getFields()
	{
		if(fieldList == null || fieldList.size() == 0)
			return null;
		else
			return fieldList.toArray(new Field[0]);
	}
	
	
	public void adjustField(Field f) {
		this.addField(f);
		this.getRender().adjustField(this, f);
	}

//	@SuppressWarnings("unchecked")
//	public void notify(String type, Object obj) {
//		if (LifeCyclePhase.ajax.equals(getPhase())) {
//			Map<String, Object> map = new HashMap<String, Object>();
//			String widgetId = this.getWidget().getId();
//			map.put("widgetId", widgetId);
//			map.put("datasetId", this.getId());
//			map.put("type", type);
//			if (type.equals("adjustField"))
//				map.put("field", obj);
//			if ("setError".equals(type)) {
//				map.putAll((Map<String, Object>) obj);
//			}
//			this.getWidget().notifyChange(UIElement.UPDATE, map);
//		}
//	}
	
	
//	//Field修改同步
//	public Object notifyChange(String type, Field field){
//		if(LifeCyclePhase.ajax.equals(getPhase())){			
//			try{
//				Map<String,Object> map = new HashMap<String,Object>();
//				map.put("datasetId", this.getId());
//				map.put("field", field);
//				map.put("type", type);
//				map.put("widgetId", this.getWidget().getId());
//				this.getWidget().notifyChange(UIElement.UPDATE, map);
//			}
//			//因此类在插件中用到，插件中无法包含日志工具。
//			catch(Throwable e){
//				throw new LuiRuntimeException(e.getMessage());
//			}
//		}
//		
//		return null;
//	}

	/**
	 * 添加一个字段信息
	 * @param field
	 */
	public void addField(Field field) {
		fieldList.add(field);
		if(LifeCyclePhase.ajax.equals(getPhase())) {
			this.getRender().adjustField(this, field);
		}
	}

	/**
	 * 删除某个字段对象
	 * 
	 * @param field
	 */
	public void removeField(Field field) {
		if(fieldList.indexOf(field)>-1){
			fieldList.remove(field);
			if(LifeCyclePhase.ajax.equals(getPhase())) {
				this.getRender().removeField(this, field);
			}
		}
	}

	/**
	 * 获取当前具有的字段数目
	 * 
	 * @return
	 */
	public int getFieldCount() {
		return fieldList.size();
	}

	/**
	 * 获取指定位置的字段名称
	 * 如果不存在，则返回null
	 * 
	 * @param index
	 * @return
	 */
	public String indexToName(int index) {
		Field field = fieldList.get(index);
		if (field != null)
			return field.getId();
		else
			return null;
	}

	/**
	 * 获取指定名称的字段所在位置。
	 * 如果不存在则返回-1。
	 * 
	 * @return
	 */
	public int nameToIndex(String id) {
		int size = fieldList.size();
		for (int i = 0; i < size; i++) {
			if (fieldList.get(i).getId().trim().equalsIgnoreCase(id))
				return i;
		}
		return -1;
	}
	
	public int fieldToIndex(String field) {
		int size = fieldList.size();
		for (int i = 0; i < size; i++) {
			if (field.equals(fieldList.get(i).getField().trim()))
				return i;
		}
		return -1;
	}
	
	public int fieldToIndexById(String id) {
		int size = fieldList.size();
		if(id != null){
			for (int i = 0; i < size; i++) {
				if (id.equals(fieldList.get(i).getId()))
					return i;
			}
		}
		return -1;
	}
	
	/**
	 * 通过Field的id获取其field属性
	 * @param id
	 * @return
	 */
	public String getFieldById(String id)
	{
		Field field = getField(id);
		if(field != null)
			return field.getField();
		else
			return null;
		
	}
	

	public List<Field> getFieldList() {
		return fieldList;
	}

	public void setFieldList(List<Field> fieldList) {
		this.fieldList = fieldList;
	}
	
	public Field getPKField(){
		Field pkField = null;
		for (int i = 0; i < this.getFieldList().size(); i++) {
			Field field = this.getFieldList().get(i);
			if(field.isPK()){
				pkField = field;
			}
		}
		return pkField;
	}
	
	public Row[] getRows(String field, Object value) {
		int fieldIndex = this.nameToIndex(field);
		if(fieldIndex==-1) {
			return null;
		}
		List<Row> rowList = new ArrayList<Row>();
		PageData[] rds = this.getAllPageData();
		for (int i = 0; i < rds.length; i++) {
			Row[] rows = rds[i].getRows();
			for(Row row :rows) {
				Object val = row.getValue(fieldIndex);
				if(ObjectUtils.equals(val, value)) {
					rowList.add(row);
				}
			}
		}
		return rowList.toArray(new Row[0]);
	}
	
	public Row getRow(String field, Object value) {
		Row[] rows = this.getRows(field, value);
		if(rows == null || rows.length == 0) {
			return null;
		} else if(rows.length == 1) {
			return rows[0];
		} else {
			throw new LuiRuntimeException(field+" 对应"+rows.length+"个 "+value+" 的值！");
		}
	}
	
	
//	@XmlAttribute
//	private String from;
//	@JSONField(serialize = false)
//	public String getFrom() {
//		return from;
//	}
//	public void setFrom(String from) {
//		this.from = from;
//	}
	
//	/**
//	 * 当没有当前行的时候返回空行 而不是空
//	 * @param createnew
//	 * @return
//	 */
//	public PageData getCurrentPagepageData(boolean createnew) {
//		PageData pageData = getCurrentPagepageData();
//		if (pageData == null) {
//			int index = paginationInfo.getPageIndex();
//			pageData = new PageData(index);
//			addpageData(index, pageData);
//		}
//		return pageData;
//	}
//	public int getRowIndexInAllRowSets(Row row) {
//	int rowIndex = -1;
//	PageData[] pageDatas = this.getAllPageData();
//	for (int j = 0; j < pageDatas.length; j++) {
//		PageData pageData = pageDatas[j];
//		if (pageData == null)
//			continue;
//		rowIndex = pageData.getRowIndex(row);
//		if (rowIndex != -1)
//			return rowIndex;
//	}
//	return rowIndex;
//}

}
