package xap.lui.core.serializer;

import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.PaginationInfo;

public interface IObject2DatasetSerializer<T> {
	/**
	 * 本方法默认为对象自身包含状态定义
	 * @param obj
	 * @param ds
	 */
	public void serialize(T obj, Dataset ds);
	public void serialize(T obj, Dataset ds, int state);
	public void serialize(PaginationInfo paginationInfo, T obj, Dataset ds, int state);
}
