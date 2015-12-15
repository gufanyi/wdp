package xap.lui.core.design;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import xap.lui.core.constant.StringDataTypeConst;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.Field;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.util.RuntimeEnv;
import xap.lui.core.util.StringPool;
import xap.mw.coreitf.d.FBoolean;
import xap.mw.coreitf.d.FDate;
import xap.mw.coreitf.d.FDouble;
import xap.mw.coreitf.d.FNumberFormat;
import xap.mw.coreitf.d.FTime;

/**
 * 产生代码
 * 
 * @author zhangxya
 *
 */
public class GeneratorCodeImpl implements IGeneratorCode {

	public List<String> packageList = new ArrayList<String>();
	private static final List<String> excludePackList = new ArrayList<String>();
	static {
		excludePackList.add(String.class.getName());
		excludePackList.add(int.class.getName());
		excludePackList.add(void.class.getName());
		excludePackList.add(String[].class.getName());
		excludePackList.add(int[].class.getName());
		excludePackList.add(Integer.class.getName());
		excludePackList.add(boolean.class.getName());
		excludePackList.add(Boolean.class.getName());
	}

	private void addPackageByType(String type) {
		if (type.equals(StringDataTypeConst.DOUBLE) || type.equals(StringDataTypeConst.dOUBLE)) {
			if (!packageList.contains(Double.class.getName()))
				packageList.add(Double.class.getName());
		} else if (type.equals(StringDataTypeConst.FDOUBLE)) {
			if (!packageList.contains(FDouble.class.getName()))
				packageList.add(FDouble.class.getName());
		} else if (type.equals(StringDataTypeConst.FLOATE) || type.equals(StringDataTypeConst.fLOATE)) {
			if (!packageList.contains(Float.class.getName()))
				packageList.add(Float.class.getName());
		} else if (type.equals(StringDataTypeConst.bYTE) || type.equals(StringDataTypeConst.BYTE)) {
			if (!packageList.contains(Byte.class.getName()))
				packageList.add(Byte.class.getName());
		} else if (type.equals(StringDataTypeConst.FBOOLEAN)) {
			if (!packageList.contains(FBoolean.class.getName()))
				packageList.add(FBoolean.class.getName());
		} else if (type.equals(StringDataTypeConst.DATE)) {
			if (!packageList.contains(Date.class.getName()))
				packageList.add(Date.class.getName());
		} else if (type.equals(StringDataTypeConst.BIGDECIMAL)) {
			if (!packageList.contains(BigDecimal.class.getName()))
				packageList.add(BigDecimal.class.getName());
		} else if (type.equals(StringDataTypeConst.lONG) || type.equals(StringDataTypeConst.lONG)) {
			if (!packageList.contains(Long.class.getName()))
				packageList.add(Long.class.getName());
		} else if (type.equals(StringDataTypeConst.FDATE)) {
			if (!packageList.contains(FDate.class.getName()))
				packageList.add(FDate.class.getName());
		} else if (type.equals(StringDataTypeConst.FTIME)) {
			if (!packageList.contains(FTime.class.getName()))
				packageList.add(FTime.class.getName());
		} else if (type.equals(StringDataTypeConst.FNUMBERFORMAT)) {
			if (!packageList.contains(FNumberFormat.class.getName()))
				packageList.add(FNumberFormat.class.getName());
		} else if (type.equals(StringDataTypeConst.OBJECT)) {
			if (!packageList.contains(Object.class.getName()))
				packageList.add(Object.class.getName());
		}
	}

	public String generatorPackageCode(String extendClass, String fullPath) {
		StringBuffer packageBuffer = new StringBuffer();
		packageBuffer.append("package " + fullPath + ";\n");
		if (packageList.size() > 0) {
			for (int j = 0; j < packageList.size(); j++) {
				String pname = packageList.get(j);
				if (excludePackList.contains(pname))
					continue;
				packageBuffer.append("import " + pname + ";\n");
			}
		}
		packageBuffer.append("\n");
		return packageBuffer.toString();
	}

	public String generatorVO(String fullPath, String tableName, String primaryKey, Dataset ds) {
		String packagePath = fullPath.substring(0, fullPath.lastIndexOf("."));
		String className = fullPath.substring(fullPath.lastIndexOf(".") + 1);
		String extendClass = "com.haiyou.core.framework.common.SuperVO";
		StringBuffer buf = new StringBuffer();
		buf.append("public class " + className + " extends " + extendClass.substring(extendClass.lastIndexOf(".") + 1) + " {\n");
		Field[] fields = ds.getFields();
		// 产生所有的Field字段
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			String voField = field.getField();
			if (voField != null && !voField.equals("")) {
				String fieldType = field.getDataType();
				if (fieldType == null || fieldType.equals(""))
					fieldType = "String";
				buf.append("	private " + fieldType + " " + voField + ";\n");
				addPackageByType(fieldType);
			}
		}
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			String voField = field.getField();
			if (voField != null && !voField.equals("")) {
				String fieldType = field.getDataType();
				if (fieldType == null || fieldType.equals(""))
					fieldType = "String";
				// getMethod()
				buf.append("\tpublic " + fieldType + " get" + voField.substring(0, 1).toUpperCase() + voField.substring(1) + "(){" + "\n");
				buf.append("\t\treturn " + voField + ";\n");
				buf.append("\t}\n");
				// setMethod()
				buf.append("\tpublic void set" + voField.substring(0, 1).toUpperCase() + voField.substring(1) + "(" + fieldType + " " + voField + ") {\n");
				buf.append("\t\tthis." + voField + "=" + voField + ";\n");
				buf.append("\t}\n");
			}
		}
		buf.append("\tpublic String getPrimaryKey(){\n");
		buf.append("\t\treturn " + primaryKey + ";\n");
		buf.append("\t}\n");
		buf.append("\tpublic String getTableName() {\n");
		buf.append("\t\treturn \"" + tableName + "\";\n");
		buf.append("\t}\n");
		// getPKFieldName
		buf.append("\tpublic String getPKFieldName() {\n");
		buf.append("\t\treturn \"" + primaryKey + "\";\n");
		buf.append("\t}\n");
		//
		buf.append("}");
		// 产生package
		if (!packageList.contains(extendClass))
			packageList.add(extendClass);
		String packageString = generatorPackageCode(extendClass, packagePath);
		return packageString + buf.toString();
	}

	public String generateRefNodeClass(String refType, String modelClass, String tableName, String refPk, String refCode, String refName, String visibleFields, String pfield, String childfield) {
		StringBuffer buf = new StringBuffer();
		BufferedReader reader = null;
		String cs = null;
		try {
			// this.getClass().getClassLoader().getResourceAsStream("nc/bs/pub/action/N_XXXX_"
			// + action + ".java")
			String path = RuntimeEnv.getInstance().getNCHome() + "/resources/wfw/refnode";
			String templatePath = null;
			if (refType.equals("grid")) {
				templatePath = "GridModel.java";
			} else {
				templatePath = "TreeModel.java";
			}
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(path + "/" + templatePath)));
			String line = reader.readLine();
			while (line != null) {
				buf.append(line);
				buf.append("\r\n");
				line = reader.readLine();
			}
			cs = buf.toString();
			String className = modelClass.substring(modelClass.lastIndexOf(".") + 1);
			String packageName = modelClass.substring(0, modelClass.lastIndexOf("."));
			cs = cs.replaceAll(GenRefCodeConstant.PACKAGE, packageName);
			cs = cs.replaceAll(GenRefCodeConstant.MODEL_CLASS, className);
			cs = cs.replaceAll(GenRefCodeConstant.REF_PK, refPk);
			cs = cs.replaceAll(GenRefCodeConstant.REF_NAME, refName);
			cs = cs.replaceAll(GenRefCodeConstant.REF_CODE, refCode);
			cs = cs.replaceAll(GenRefCodeConstant.TABLE_NAME, tableName);
			cs = cs.replaceAll(GenRefCodeConstant.VISIBLE_FIELDS, mergeArray(visibleFields));
			cs = cs.replaceAll(GenRefCodeConstant.PARENT_FIELD, pfield);
			cs = cs.replaceAll(GenRefCodeConstant.CHILD_FIELD, childfield);
		} catch (IOException e) {
			throw new LuiRuntimeException(e.getMessage());
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
				}
			}
		}
		return cs;
	}

	private String mergeArray(String visibleFields) {
		String[] strArr = visibleFields.split(StringPool.COMMA);
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < strArr.length; i++) {
			buf.append("\"");
			buf.append(strArr[i]);
			buf.append("\"");
			if (i != strArr.length - 1)
				buf.append(",");
		}
		return buf.toString();
	}
}
