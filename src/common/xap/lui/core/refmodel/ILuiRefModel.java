package xap.lui.core.refmodel;

import xap.lui.core.dataset.PaginationInfo;
import xap.lui.core.refrence.RefResultSet;

public interface ILuiRefModel {
	public RefResultSet getRefData(int pageIndex);// 获取参照数据
	public RefResultSet filterRefValue(String filterValue,PaginationInfo pInfo);// 针对所有字段
	public RefResultSet getRefData(PaginationInfo pInfo);// 获取参照数据
}
