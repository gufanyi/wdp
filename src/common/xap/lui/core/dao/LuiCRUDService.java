package xap.lui.core.dao;

import java.util.Map;

import xap.lui.core.dataset.PaginationInfo;
import xap.sys.jdbc.handler.RsHandler;
import xap.sys.jdbc.kernel.SqlParam;

public interface LuiCRUDService {
	public Object saveBean(Object obj);

	public Object[] saveBeans(Object[] objs);

	public void deleteBeans(Object[] objs);

	public void deleteBean(Object obj, boolean trueDel);

	public void deleteBean(Object obj);

	public <C> C[] queryBeans(Class<C> c, PaginationInfo pg, Map<String, Object> extMap);

	public <C> C[] queryBeanss(Class<C> clazz, String sql, PaginationInfo pg, Map<String, Object> extMap);

	public <C> C[] queryBeans(Class<C> clazz, String sql, PaginationInfo pg, String orderBy, Map<String, Object> extMap);

	public <C> C[] queryBeans(Class<C> c, PaginationInfo pg, String wherePart, Map<String, Object> extMap);

	public <C> C[] queryBeans(Class<C> c, PaginationInfo pg, String wherePart, Map<String, Object> extMap, String orderByPart);

	public <C> C[] queryBeans(Class<C> c, String sql);

	public <C> C queryBeanById(Class<C> clazz, String Id);

	public Object query(String sql, RsHandler processor);

	public Object query(String sql, PaginationInfo pg, RsHandler processor);

	public int executeUpdate(String sql, SqlParam parameter);

	public int executeUpdate(String sql);

}
