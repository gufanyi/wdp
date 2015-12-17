package xap.lui.core.refrence;
import java.util.List;

import xap.lui.core.dataset.PaginationInfo;
public interface IRefModel extends IClassRefModel  {
	/**
	 * 基础配置
	 */
	public String getId();
	public String[] getAllFields();
	public int getAllFieldCount();
	public int getFieldIndex(String filedName);
	public String[] getShowFieldCode();// 显示字段列表
	public String[] getHiddenFieldCode();// 隐参字段名
	public String[] getShowFieldName();// 显示字段中文名
	public List<String[]> getWhereFieldCode(); //翻译匹配字段名称数组 index0:翻译表字段名，index1:主表字段名
	public List<String> getWriteFieldCode(); //翻译表翻译字段名
	
	public String[] getBlurFields();// 定位配置字段
	public String getTableName();
	public List<String> getRefTableName(); //翻译表名称
	public String getRefTitle();// 参照标题
	public String getPkFieldCode();
	public String getRefCodeField();
	public String getRefNameField();
	public void addWherePart(String wherePart);// 增加where子句
	public List<String> getWherePart();// 获取where语句
	public List<String> getGroupPart();// 获取分组语句
	public List<String> getOrderPart();// 获取排序语句
	boolean isUseDataPower();// 是否启动数据权限
	public boolean isIncludeBlurPart();// 是否包含定位信息
	public boolean isIncludeEnvPart();// 是否包含环境变量
	public String afterBuilderSql(String sql);
	public String value2ShowValue(String value);//参照翻译
	/**
	 * 环境变量
	 */
	public String getPk_group();
	void setPk_group(String pk_group);
	public String getPk_org();
	void setPk_org(String pk_org);
	public String getPk_user();
	void setPk_user(String pk_user);
	/**
	 * 标准服务
	 */
	public String getRefSql();
	public RefResultSet getRefData();// 获取参照数据
	public RefResultSet filterRefPks(String[] filterPks);// 只过滤pkfield
	public RefResultSet filterRefValue(String filterValue);// 针对所有字段
	public RefResultSet filterRefBlurValue(String blurValue);// 针对所有字段
	
	/**
	 * 感觉应该室refgrid
	 * @return
	 */
	public int getPageSize();// 获取当前参照页大小
	public RefResultSet getRefData(int pageIndex);// 获取参照数据
	public RefResultSet filterRefValue(String filterValue,PaginationInfo pInfo);// 针对所有字段
	public RefResultSet getRefData(PaginationInfo pInfo);// 获取参照数据
	
	
	

}
