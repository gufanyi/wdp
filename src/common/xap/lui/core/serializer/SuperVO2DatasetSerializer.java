package xap.lui.core.serializer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import xap.lui.core.constant.DataTypeTranslator;
import xap.lui.core.constant.IntDataTypeConst;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.PageData;
import xap.lui.core.dataset.PaginationInfo;
import xap.lui.core.dataset.Row;
import xap.lui.core.exception.LuiRuntimeException;
import xap.mw.core.data.BaseDO;
public class SuperVO2DatasetSerializer implements IObject2DatasetSerializer<BaseDO[]> {
	private static final String UIID_FIELD = "$UIID_FIELD";
	@Override 
	public void serialize(BaseDO[] vos, Dataset ds) {
		serialize(vos, ds, -1);
	}
	public void serialize(BaseDO[] vos, Dataset ds, int state) {
		PaginationInfo pInfo = null;
		try {
			pInfo = ds.getPaginationInfo();
			if (vos != null && ds.getPageSize() == -1) { // 不分页时，设置recordsCount为vos的数量
				pInfo.setPageSize(ds.getPageSize());
				pInfo.setRecordsCount(vos.length);
			}
			else if(vos != null && ds.getPageSize() != -1 && vos.length > pInfo.getPageSize()){
				pInfo.setPageSize(ds.getPageSize());
			    pInfo.setRecordsCount(vos.length);
			}
		} catch (Exception e) {
			throw new LuiRuntimeException(e.getMessage());
		}
		serialize(pInfo, vos, ds, state);
	}
	public void serialize(PaginationInfo pInfo, BaseDO[] vos, Dataset ds, int state) {
		serialize(pInfo, vos, ds, state, true);
	}
	public void serialize(BaseDO[] vos, Dataset ds, boolean clean) {
		serialize(vos, ds, -1, clean);
	}
	public void serialize(BaseDO[] vos, Dataset ds, int state, boolean clean) {
		PaginationInfo pInfo = null;
		try {
			pInfo = ds.getPaginationInfo();
			if (vos != null && pInfo.getPageSize() == -1) { // 不分页时，设置recordsCount为vos的数量
				pInfo.setPageSize(ds.getPageSize());
				pInfo.setRecordsCount(vos.length);
			}
		} catch (Exception e) {
			throw new LuiRuntimeException(e.getMessage());
		}
		serialize(pInfo, vos, ds, state, clean);
	}
	public void serialize(PaginationInfo pInfo, BaseDO[] vos, Dataset ds, int state, boolean clean) {
		ds.setFocusIndex(-1);
		if (ds != null)
			ds.setPaginationInfo(pInfo);
//		ds.setRowSetChanged(true);
		PageData pageData = ds.getCurrentPageData();
		if (clean){
			pageData.clear();
		} 
		if(vos != null && vos.length > pInfo.getPageSize() && pInfo != null && pInfo.getPageSize() != -1){
			for (int i = 0; i < pInfo.getPageCount(); i++) {
				for (int j = i * pInfo.getPageSize(); j < pInfo.getPageSize() * (i + 1) && j < vos.length; j++) {
					PageData pageDatanew = ds.getCurrentIndexPageData(i);
					if(pageDatanew == null){
						pageDatanew = new PageData(i);
						ds.addPageData(i, pageDatanew);
					}
					BaseDO vo = vos[j];
					if (state == -1) {
						state = vo.getStatus();
					}
					Row row = ds.getEmptyRow();
					if (state == 3)
						row.setState(Row.STATE_FALSE_DELETED);
					else
						row.setState(state);
					pageDatanew.addRow(row);
					vo2DataSet(vos[j], ds, row);
				}
			}
			
		}
		else{
			if (vos != null) {
				for (int i = 0; i < vos.length; i++) {
					BaseDO vo = vos[i];
					if (state == -1) {
						state = vo.getStatus();
					}
					Row row = ds.getEmptyRow();
					if (state == 3)
						row.setState(Row.STATE_FALSE_DELETED);
					else
						row.setState(state);
					pageData.addRow(row);
					String rowId =vo.getPkVal();
					//TODO: rowid的处理方式需要思考，暂时这么处理
					if(rowId==null)
						rowId = (String)vo.getAttrVal(UIID_FIELD);
					if(rowId!=null)
						row.setRowId(rowId);
					vo2DataSet(vos[i], ds, row);
				}
			}
		}
//		if (vos != null && vos.length != 0) {
//			Row[] selectedRows = ds.getSelectedRows();
//			if (selectedRows == null || selectedRows.length == 0) {
//				ds.setRowSelectIndex(0);
//			}
//		}
	}
	/**
	 * 将多个VO值转换成Dataset中的多行数据，即RowSet中的多个Row对象。 注意：该方法没有进行FieldRelation的处理
	 * 
	 * @param vos
	 * @param ds
	 * @param rows
	 * @return
	 */
	public Row[] vo2DataSet(Object[] vos, Dataset ds, Row[] rows) {
		if (vos == null || ds == null || rows == null || vos.length != rows.length)
			throw new IllegalArgumentException("参数错误！");
		for (int i = 0; i < vos.length; i++) {
			vo2DataSet(vos[i], ds, rows[i]);
		}
		return rows;
	}
	/**
	 * 将一个VO值对象根据给定的DataSet结构设置Row对象，并将Row对象 添加到Dataset中返回。
	 * 因为Row对象有很多比如状态，id,parentid等相关属性，因此需要通过 外部传入，否则需要传入很多Row对象的特征属性信息，参数会变得很多。
	 * 
	 * 注意：该方法没有进行FieldRelation的处理。
	 * 
	 * @param vo
	 * @param ds
	 * @param row
	 * @return
	 */
	public Row vo2DataSet(Object vo, Dataset ds, Row row) {
//		BaseDO basedo = (BaseDO) vo;
//		String rowId =basedo.getPkVal();
//		if(rowId==null)rowId = (String)basedo.getAttrVal(UIID_FIELD);
//		if (rowId != null)
//			row.setRowId(rowId);
		if (vo == null || ds == null || row == null)
			throw new IllegalArgumentException("参数错误！");
		List<CellInfoVO> list = this.voDs2CellInfoVO(vo, ds);
		for (int i = 0; i < list.size(); i++) {
			this.voAttributeValue2Row(vo, ds, row, list.get(i));
		}
		modifyRow(row,ds);
		return row;
	}
	/**
	 * 获取vo与row的所有对应关系
	 * <Strong>注意：此处是以Row的元素个数为准，该ds存在多少个field，就返回多少元素，vo中不存在的属性则设置为null
	 * 。</Strong>
	 * 
	 * @param vo
	 * @param ds
	 * @param row
	 */
	public List<CellInfoVO> voDs2CellInfoVO(Object vo, Dataset ds) {
		List<CellInfoVO> list = new ArrayList<CellInfoVO>();
		String key = null;
		Object value = null;
		int count = ds.getFieldCount();
		for (int i = 0; i < count; i++) {
			CellInfoVO cellInfo = new CellInfoVO();
			list.add(cellInfo);
			cellInfo.setIndex(i);
			key = ds.getField(i).getField();
			if (key == null || key.trim().equals("")){
				key=ds.getField(i).getId();
			}
			if(StringUtils.isBlank(key)){
				continue;
			}
				
			cellInfo.setVoAttributeName(key);
			if (vo instanceof BaseDO)
				value = ((BaseDO) vo).getAttrVal(StringUtils.capitalize(key));
			else {
				String type = ds.getField(i).getDataType();
				boolean isBooleanType = false;
				if (DataTypeTranslator.translateString2Int(type) == IntDataTypeConst.boolean_TYPE)
					isBooleanType = true;
				value = getAttributeValue(vo, key, isBooleanType);
			}
			cellInfo.setVoAttributeValue(value);
			cellInfo.setIndex(i);
		}
		return list;
	}
	/**
	 * 该方法实现向row中设置特定Cell的值信息，当前涉及的属性信息在CellInfoVO中 进行说明。
	 * 
	 * @param vo
	 * @param ds
	 * @param row
	 * @param cellInfo
	 */
	public void voAttributeValue2Row(Object vo, Dataset ds, Row row, CellInfoVO cellInfo) {
		row.setValue(cellInfo.getIndex(), cellInfo.getVoAttributeValue());// 可能需要塞null值，所以将判断null去掉
	}
	/**
	 * 获取某个对象的某个属性值
	 * 
	 * @param vo
	 * @param attribute
	 * @param isBooleanType
	 * @return
	 */
	private Object getAttributeValue(Object vo, String attribute, boolean isBooleanType) {
		/* 此处特殊处理NC的m_属性，有待确定是否合适和通用。 */
		if (attribute.startsWith("m_"))
			attribute = attribute.substring(2);
		String postfix = attribute.substring(0, 1).toUpperCase() + attribute.substring(1, attribute.length());
		String methodName;
		if (isBooleanType)
			methodName = "is" + postfix;
		else
			methodName = "get" + postfix;
		Method method;
		try {
			method = vo.getClass().getMethod(methodName, new Class[] {});
			Object value = null;
			if(method != null){
				value = method.invoke(vo, new Object[] {});
			}
			else if(vo instanceof BaseDO){
				value = ((BaseDO)vo).getAttrVal(attribute);
			}
				
			return value;
		} catch (SecurityException e) {
			throw new LuiRuntimeException(e.getMessage());
		} catch (NoSuchMethodException e) {
			return null;
		} catch (IllegalArgumentException e) {
			throw new LuiRuntimeException(e.getMessage());
		} catch (IllegalAccessException e) {
			throw new LuiRuntimeException(e.getMessage());
		} catch (InvocationTargetException e) {
			throw new LuiRuntimeException(e.getMessage());
		}
	}
	
	/**
	 * 进行选中行的数据回写
	 * @param vos
	 * @param ds
	 * @param selecttrows
	 */
	public void update(BaseDO[] vos, Dataset ds,Row[] selecttrows) {
		for(Row row :selecttrows){
			if (vos != null) {
				for (int i = 0; i < vos.length; i++) {
					BaseDO vo = vos[i];
					row.setState(vo.getStatus());
					vo2DataSet(vo, ds, row);
				}
			}
		}
		
	}
	protected void modifyRow(Row row,Dataset ds){
		return;
	}
}
