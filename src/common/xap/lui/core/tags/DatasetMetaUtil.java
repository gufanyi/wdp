package xap.lui.core.tags;

import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.Field;

public class DatasetMetaUtil {
//	private static FormulaParse fp = new FormulaParse();
	public static String generateMeta(Dataset ds) {
		StringBuffer buf = new StringBuffer();
		buf.append("[");
		
		int count = ds.getFieldCount();
		for(int i = 0; i < count; i ++)
		{
			Field field = ds.getField(i);
			buf.append(generateField(field));
			if(i != count - 1)
				buf.append(",");
		}
		buf.append("]");
		return buf.toString();
	}

	/**
	 * 构造field
	 * @param buf
	 * @param field
	 */
	public static String generateField(Field field) {
		StringBuffer buf = new StringBuffer();
		buf.append("{key:\"")
		   .append(field.getId())
		   .append("\",value:\"")
		   .append(field.getText())
		   .append("\",dftValue:");
			Object defValue = field.getDefaultValue();
//			if(defValue != null){
//				if("Memo".equals(field.getDataType()) || "String".equals(field.getDataType()))
//					defValue = JsURLEncoder.encode((String)defValue,"UTF-8");
//				if(defValue instanceof String){
//					if(((String)defValue).toUpperCase().equals(field.DEFAULT_VALUE_SYSDATE)) {
//						FDate currentDate = new FDate(Calendar.getInstance().getTime());
//						defValue = currentDate.toString();
//					}
//				}
//			}
		buf.append(defValue == null? null : "\"" + defValue + "\"")
		   .append(",isRequired:")
		   .append(field.isRequire())
		   .append(",dataType:\"")
		   .append(field.getDataType())
		   .append("\"");
		if(field.getField() != null)
		   buf.append(",field:\"")
			   .append(field.getField()) 
			   .append("\"");
		else
			buf.append(",field:null");
		// 是否主键
		if(field.isPK()){
			buf.append(",isPrimaryKey:true");
		}
		// 精度
		if(field.getPrecision() != null){
			buf.append(",precision:")
				.append(field.getPrecision());
		}
		//格式化校验类型
		if(field.getFormater() != null)
			buf.append(",formater:'"+field.getFormater()+"'");
		else
			buf.append(",formater:null");

		if(field.getMaxValue() != null)
		   buf.append(",maxValue:")
			   .append(field.getMaxValue());
		else
			buf.append(",maxValue:null");

		if(field.getMinValue() != null)
		   buf.append(",minValue:")
			   .append(field.getMinValue());
		else
			buf.append(",minValue:null");
		
		if(field.isLock() == true)
			buf.append(",isLock:true");
		else
			buf.append(",isLock:false");
//		//自定义编辑公式
//		if(field.getDefEditFormular() != null)
//			buf.append(",defEditFormular:\"" + field.getDefEditFormular() + "\"");
		
		String validateFormula = field.getValidateFormula();
		//验证公式
		if(validateFormula != null && !validateFormula.equals("")){
//			String vf = field.getValidateFormula();
//			vf = JsURLEncoder.encode(vf,"UTF-8"); 
			buf.append(",validateFormular:true");
		}
		
		String editFormular = field.getEditFormular();
		if(editFormular != null && !editFormular.equals("")){
			buf.append(",editFormular:true");
		}
		

		
		buf.append("}");
		return buf.toString();
	}
	

	
	
}
