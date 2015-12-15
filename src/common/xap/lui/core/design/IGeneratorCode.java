package xap.lui.core.design;

import xap.lui.core.dataset.Dataset;

/**
 * 根据类名生成代码
 * 
 * @author zhangxya
 *
 */
public interface IGeneratorCode {

	public String generatorVO(String fullPath, String tableName, String primaryKey, Dataset ds);

	// public String generatorCode(String fullPath, String extendClass,
	// Map<String, Object> param) throws LuiBusinessException;

	public String generateRefNodeClass(String refType, String modelClass, String tableName, String refPk, String refCode, String refName, String visibleFields, String pfield, String childfield);

}
