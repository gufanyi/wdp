package xap.lui.core.serializer;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import xap.lui.core.constant.DataTypeTranslator;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.EmptyRow;
import xap.lui.core.dataset.Field;
import xap.lui.core.dataset.PageData;
import xap.lui.core.dataset.Row;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.util.ClassUtil;
import xap.mw.core.data.BaseDO;
/**
 * Dataset到SuperVO的序列化器
 * 
 * @author gd 2010-1-28
 * @version NC6.0
 * @since NC6.0
 */
public class Dataset2SuperVOSerializer<T extends BaseDO> implements IDataset2ObjectSerializer<BaseDO[]> {
	public T[] serialize(Dataset ds, Row[] selRows) {
		// 对于superVO只序列化一个dataset
		if (ds == null || ds.getVoMeta() == null)
			throw new IllegalArgumentException("dataset的voMeta没有配置!dsId=" + ds.getId());
		ArrayList<T> vos = new ArrayList<T>();
		Row[] serRows = null;
		if (selRows != null && selRows.length > 0) {
			serRows = selRows;
		} else {
			PageData rd = ds.getCurrentPageData();
			if (rd == null)
				return null;
			serRows = rd.getRows();
		}
		int count = serRows.length;
		for (int i = 0; i < count; i++) {
			Row row = serRows[i];
			if (row instanceof EmptyRow)
				continue;
			T vo = getSuperVO(ds, row);
			List<CellInfoVO> list = processRow2CellValue(ds, vo, row);
			int size = list.size();
			for (int j = 0; j < size; j++) {
				row2VoAttributeValue(vo, list.get(j));
			}
			vo.setStatus(row.getState());
			vos.add(vo);
		}
		if (vos.size() > 0)
			return (T[]) vos.toArray((Object[]) Array.newInstance(vos.get(0).getClass(), 0));
		else
			return (T[]) vos.toArray(new BaseDO[0]);
	}
	protected T getSuperVO(Dataset ds, Row row) {
		if (StringUtils.isNotBlank(ds.getVoMeta())) {
			try {
				Class c = ClassUtil.forName(ds.getVoMeta());
				T vo = (T) c.newInstance();
				return vo;
			} catch (Throwable e) {
				throw new LuiRuntimeException(e.getMessage());
			}
		}
		return null;
		// if (ds.getVoMeta() != null && !ds.getVoMeta().equals("")) {
		//
		// //String uiid = row.getRowId();
		// //if (uiid == null || uiid.equals(""))
		// //throw new LuiRuntimeException("row id can not be null");
		// //bu.setUIID(vo, uiid);
		// return vo;
		// }
		// return null;
	}
	/**
	 * 将ds中的某行数据转换成vo
	 * 
	 * @param ds
	 * @param vo
	 * @param row
	 */
	protected List<CellInfoVO> processRow2CellValue(Dataset ds, Object vo, Row row) {
		int count = ds.getFieldCount();
		Field field = null;
		String attribute = null, dataType = null;
		Object value = null;
		List<CellInfoVO> list = new ArrayList<CellInfoVO>();
		for (int i = 0; i < count; i++) {
			field = ds.getField(i);
			attribute = field.getField();
			if(attribute==null){
				attribute=field.getId();
			}
			dataType = field.getDataType();
			value = row.getValue(i);
			if (attribute != null && !attribute.trim().equals("")) {
				CellInfoVO info = new CellInfoVO();
				info.setVoAttributeName(attribute);
				info.setVoAttributeValue(value);
				info.setDataType(dataType);
				info.setIndex(i);
				list.add(info);
			}
		}
		return list;
	}
	/**
	 * 为VO设置某个属性的值
	 * 
	 * @param vo
	 * @param attribute
	 * @param dataType
	 * @param value
	 */
	protected void row2VoAttributeValue(Object vo, CellInfoVO info) {
		String attribute = info.getVoAttributeName();
		String dataType = info.getDataType();
		Object value = info.getVoAttributeValue();
		Class clazz = DataTypeTranslator.translateString2Class(dataType);
		String methodName = "set" + attribute.substring(0, 1).toUpperCase() + attribute.substring(1, attribute.length());
		try {
			Method method = null;
			try {
				method = vo.getClass().getMethod(methodName, new Class[] {
					clazz
				});
			} catch (NoSuchMethodException ne) {}
			// 为Object hack
			if (method == null) {
				try {
					method = vo.getClass().getMethod(methodName, new Class[] {
						Object.class
					});
				} catch (NoSuchMethodException ne) {}
			}
			if (method != null)
				method.invoke(vo, new Object[] {
					value
				});
			else if (vo instanceof BaseDO) {
				try {
					((BaseDO) vo).setAttrVal(attribute, value);
				} catch (Exception e) {
					throw new LuiRuntimeException(e.getMessage());
				}
			}
		} catch (SecurityException e) {
			throw new LuiRuntimeException(e.getMessage());
		}
		// catch (NoSuchMethodException e) {
		// //该VO可能没有此属性，因此此处没有外抛异常信息。
		// Logger.warn(e.getMessage(), e);
		//
		// }
		catch (IllegalArgumentException e) {
			throw new LuiRuntimeException("convertor error:" + e.getMessage());
		} catch (IllegalAccessException e) {
			throw new LuiRuntimeException(e.getMessage());
		} catch (InvocationTargetException e) {
			throw new LuiRuntimeException(e.getMessage());
		}
	}
	public T[] serialize(Dataset dataset, Row selRow) {
		if (selRow == null)
			return serialize(dataset, new Row[] {});
		return serialize(dataset, new Row[] {
			selRow
		});
	}
	public T[] serialize(Dataset dataset) {
		return serialize(dataset, new Row[0]);
	}
}
