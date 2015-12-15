package xap.lui.core.constant;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

import xap.mw.coreitf.d.FBoolean;


/**
* 该类实现由配置文件的数据类型的String型转换成WebDataType中的类型的int型
* 
 */

public class DataTypeTranslator {

	private static Map<Integer, Class<?>> dataTypeMap = new HashMap<Integer, Class<?>>();
	static{
		dataTypeMap.put(IntDataTypeConst.int_TYPE, int.class);
		dataTypeMap.put(IntDataTypeConst.String_TYPE, String.class);
		dataTypeMap.put(IntDataTypeConst.double_TYPE, double.class);
		dataTypeMap.put(IntDataTypeConst.float_TYPE, float.class);
		dataTypeMap.put(IntDataTypeConst.byte_TYPE, byte.class);
		dataTypeMap.put(IntDataTypeConst.boolean_TYPE, boolean.class);
		dataTypeMap.put(IntDataTypeConst.Date_TYPE, Date.class);
		dataTypeMap.put(IntDataTypeConst.BigDecimal_TYPE, BigDecimal.class);
		dataTypeMap.put(IntDataTypeConst.long_TYPE, long.class);
		dataTypeMap.put(IntDataTypeConst.char_TYPE, char.class);
		

		dataTypeMap.put(IntDataTypeConst.Integer_TYPE, Integer.class);	
		dataTypeMap.put(IntDataTypeConst.Double_TYPE, Double.class);	
		dataTypeMap.put(IntDataTypeConst.Float_TYPE, Float.class);	
		dataTypeMap.put(IntDataTypeConst.Byte_TYPE, Byte.class);	
		dataTypeMap.put(IntDataTypeConst.Boolean_TYPE, Boolean.class);	
		dataTypeMap.put(IntDataTypeConst.Long_TYPE, Long.class);	
		dataTypeMap.put(IntDataTypeConst.Character_TYPE, Character.class);	
		dataTypeMap.put(IntDataTypeConst.Entity_TYPE, String.class);
		
		dataTypeMap.put(IntDataTypeConst.FBoolean_TYPE, FBoolean.class);
//		dataTypeMap.put(IntDataTypeConst.FDouble_TYPE, FDouble.class);
//		dataTypeMap.put(IntDataTypeConst.FDateTime_TYPE, FDateTime.class);
//		dataTypeMap.put(IntDataTypeConst.FDate_TYPE, FDate.class);
//		dataTypeMap.put(IntDataTypeConst.FTime_TYPE, FTime.class);
//		dataTypeMap.put(IntDataTypeConst.FNumberFormat_TYPE, FNumberFormat.class);		
//
//		dataTypeMap.put(IntDataTypeConst.FLiteralDate, FLiteralDate.class);
//		//新加的FDate_begin,FDate_end类型
//		dataTypeMap.put(IntDataTypeConst.FDate_end, FDate.class);
//		dataTypeMap.put(IntDataTypeConst.FDate_begin, FDate.class);
	}
	
	/**
	 * 将配置文件中配置的数据类型转换成int型数据类型标识
	 * @param dataType
	 * @return
	 */
	public static int translateString2Int(String dataType)
	{
		if(dataType == null || dataType.trim().equals("") || dataType.trim().equals(StringDataTypeConst.STRING))
			return IntDataTypeConst.String_TYPE;
		else if(dataType.trim().equals(StringDataTypeConst.INT))
			return IntDataTypeConst.int_TYPE;
		else if(dataType.trim().equals(StringDataTypeConst.INTEGER))
			return IntDataTypeConst.Integer_TYPE;
		else if(dataType.trim().equals(StringDataTypeConst.DOUBLE))
			return IntDataTypeConst.Double_TYPE;
		else if(dataType.trim().equals(StringDataTypeConst.dOUBLE))
			return IntDataTypeConst.double_TYPE;
		else if(dataType.trim().equals(StringDataTypeConst.FDOUBLE))
			return IntDataTypeConst.FDouble_TYPE;
		else if(dataType.trim().equals(StringDataTypeConst.FLOATE))
			return IntDataTypeConst.Float_TYPE;
		else if(dataType.trim().equals(StringDataTypeConst.bYTE))
		    return IntDataTypeConst.byte_TYPE;
		else if(dataType.trim().equals(StringDataTypeConst.BYTE))
		    return IntDataTypeConst.Byte_TYPE;
	    else if(dataType.trim().equals(StringDataTypeConst.BOOLEAN))
		    return IntDataTypeConst.Boolean_TYPE;
    	else if(dataType.trim().equals(StringDataTypeConst.bOOLEAN))
		    return IntDataTypeConst.boolean_TYPE;
    	else if(dataType.trim().equals(StringDataTypeConst.FBOOLEAN))
    		return IntDataTypeConst.FBoolean_TYPE;
       	else if(dataType.trim().equals(StringDataTypeConst.DATE))
       		return IntDataTypeConst.Date_TYPE;
       	else if(dataType.trim().equals(StringDataTypeConst.BIGDECIMAL))
       		return IntDataTypeConst.BigDecimal_TYPE;
       	else if(dataType.trim().equals(StringDataTypeConst.lONG))
       		return IntDataTypeConst.long_TYPE;
       	else if(dataType.trim().equals(StringDataTypeConst.LONG))
       		return IntDataTypeConst.Long_TYPE;
       	else if(dataType.trim().equals(StringDataTypeConst.CHAR))
       		return IntDataTypeConst.char_TYPE;
       	else if(dataType.trim().equals(StringDataTypeConst.CHARACTER))
          	return IntDataTypeConst.Character_TYPE;                       		
        else if(dataType.trim().equals(StringDataTypeConst.FDATETIME))
        	return IntDataTypeConst.FDateTime_TYPE;
        else if(dataType.trim().equals(StringDataTypeConst.FDATE))
           	return IntDataTypeConst.FDate_TYPE;  
        else if(dataType.trim().equals(StringDataTypeConst.FLiteralDate))
           	return IntDataTypeConst.FLiteralDate; 
        else if(dataType.trim().equals(StringDataTypeConst.FDate_end))
           	return IntDataTypeConst.FDate_end; 
        else if(dataType.trim().equals(StringDataTypeConst.FDate_begin))
           	return IntDataTypeConst.FDate_begin; 
       	else if(dataType.trim().equals(StringDataTypeConst.FTIME))
       		return IntDataTypeConst.FTime_TYPE;
       	else if(dataType.trim().equals(StringDataTypeConst.FNUMBERFORMAT))
          	return IntDataTypeConst.FNumberFormat_TYPE;                       		
       	else if(dataType.trim().equals(StringDataTypeConst.Decimal))
       		return IntDataTypeConst.Decimal_TYPE;
       	else if(dataType.trim().equals(StringDataTypeConst.ENTITY))
       		return IntDataTypeConst.Entity_TYPE;
       	else if(dataType.trim().equals(StringDataTypeConst.OBJECT))
       		return IntDataTypeConst.Entity_TYPE;
        else 
            return IntDataTypeConst.String_TYPE;         		
	}
	
	public static String translateInt2String(int dataType) {
		switch(dataType){
			case IntDataTypeConst.int_TYPE:
				return StringDataTypeConst.INT;
			case IntDataTypeConst.Integer_TYPE:
				return StringDataTypeConst.INTEGER;
			case IntDataTypeConst.BigDecimal_TYPE:
				return StringDataTypeConst.BIGDECIMAL;
			case IntDataTypeConst.boolean_TYPE:
				return StringDataTypeConst.bOOLEAN;
			case IntDataTypeConst.Boolean_TYPE:
				return StringDataTypeConst.BOOLEAN;
			case IntDataTypeConst.byte_TYPE:
				return StringDataTypeConst.bYTE;
			case IntDataTypeConst.Byte_TYPE:
				return StringDataTypeConst.BYTE;
			case IntDataTypeConst.char_TYPE:
				return StringDataTypeConst.CHAR;
			case IntDataTypeConst.Character_TYPE:
				return StringDataTypeConst.CHARACTER;
			case IntDataTypeConst.Date_TYPE:
				return StringDataTypeConst.DATE;
			case IntDataTypeConst.Decimal_TYPE:
				return StringDataTypeConst.Decimal;
			case IntDataTypeConst.double_TYPE:
				return StringDataTypeConst.dOUBLE;
			case IntDataTypeConst.Double_TYPE:
				return StringDataTypeConst.DOUBLE;
			case IntDataTypeConst.float_TYPE:
				return StringDataTypeConst.fLOATE;
			case IntDataTypeConst.Float_TYPE:
				return StringDataTypeConst.FLOATE;
			case IntDataTypeConst.long_TYPE:
				return StringDataTypeConst.lONG;
			case IntDataTypeConst.Long_TYPE:
				return StringDataTypeConst.LONG;
			case IntDataTypeConst.String_TYPE:
				return StringDataTypeConst.STRING;
			case IntDataTypeConst.FBoolean_TYPE:
				return StringDataTypeConst.FBOOLEAN;
			case IntDataTypeConst.FDate_TYPE:
				return StringDataTypeConst.FDATE;
			case IntDataTypeConst.FDateTime_TYPE:
				return StringDataTypeConst.FDATETIME;
			case IntDataTypeConst.FDouble_TYPE:
				return StringDataTypeConst.FDOUBLE;
			case IntDataTypeConst.FTime_TYPE:
				return StringDataTypeConst.FTIME;
			case IntDataTypeConst.FNumberFormat_TYPE:
				return StringDataTypeConst.FNUMBERFORMAT;
			case IntDataTypeConst.FLiteralDate:
				return StringDataTypeConst.FLiteralDate;
			case IntDataTypeConst.FDate_begin:
				return StringDataTypeConst.FDate_begin;
			case IntDataTypeConst.FDate_end:
				return StringDataTypeConst.FDate_end;
			default:
				return StringDataTypeConst.STRING;
		}
	}
	
//	/**
//	 * 从NC模板转化的字段类型
//	 * @param dataType
//	 * @return
//	 */
//	public static String translateBillInt2String(int dataType) {
//		switch(dataType){
//			case IBillItem.STRING:
//				return StringDataTypeConst.STRING;
//			case IBillItem.INTEGER:
//				return StringDataTypeConst.INTEGER;
//			case IBillItem.DECIMAL:
//				return StringDataTypeConst.Decimal;
//			case IBillItem.DATE:
//				return StringDataTypeConst.DATE;
//			case IBillItem.BOOLEAN:
//				return StringDataTypeConst.FBOOLEAN;
//			case IBillItem.TIME:
//				return StringDataTypeConst.FDATETIME;
//			default:
//				return StringDataTypeConst.STRING;
//		}
//	}
	
	/**
	 * 根据配置文件中配置的String数据类型转换获得相应的类型类（Class对象）
	 * @param dataType
	 * @return
	 */
	public static Class<?> translateString2Class(String dataType)
	{
		int type = translateString2Int(dataType);
		return dataTypeMap.get(Integer.valueOf(type));
	}
}

