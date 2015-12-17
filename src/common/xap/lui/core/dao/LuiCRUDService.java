package xap.lui.core.dao;

import java.util.Map;

import xap.lui.core.dataset.PaginationInfo;
import xap.sys.jdbc.handler.RsHandler;
import xap.sys.jdbc.kernel.SqlParam;

public interface LuiCRUDService {
	public Object saveBean(Object obj);

	public Object[] saveBeans(Object[] objs);

	public void deleteBean(Object obj);

	public void deleteBeans(Object[] objs);

	public void deleteBeansById(Class<?> clazz, String Id);

	public void deleteBeansById(Class<?> clazz, String[] Ids);

	public void deleteBeansByWherePart(Class<?> clazz, String wherePart);

	public void deleteBeansByWherePart(Class<?> clazz, String wherePart, SqlParam param);

	public <C> C getBeanById(Class<C> clazz, String Id);

	public <C> C[] getBeans(Class<C> c);

	public <C> C[] getBeans(Class<C> c, Map<String, Object> extMap);

	public <C> C[] getBeans(Class<C> c, PaginationInfo pg, Map<String, Object> extMap);

	public <C> C[] getBeansByFullSql(Class<C> c, String sql);

	public <C> C[] getBeansByFullSql(Class<C> c, String sql, PaginationInfo pageInfo);

	public <C> C[] getBeansByFullSql(Class<C> c, String sql, PaginationInfo pageInfo, Map<String, Object> extMap);

	public <C> C[] getBeansByFullSql(Class<C> c, String sql, PaginationInfo pageInfo, String orderBy, Map<String, Object> extMap);

	public <C> C[] getBeansByWherePart(Class<C> c, String wherePart);

	public <C> C[] getBeansByWherePart(Class<C> c, String wherePart, PaginationInfo pageInfo);

	public <C> C[] getBeansByWherePart(Class<C> c, String wherePart, PaginationInfo pageInfo, Map<String, Object> extMap);

	public <C> C[] getBeansByWherePart(Class<C> c, String wherePart, PaginationInfo pageInfo, String orderBy, Map<String, Object> extMap);

	public Object executeQuery(String sql, RsHandler processor);

	public Object executeQuery(String sql, PaginationInfo pg, RsHandler processor);

	public int executeUpdate(String sql, SqlParam parameter);

	public int executeUpdate(String sql);

}
