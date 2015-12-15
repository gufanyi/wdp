package xap.lui.core.dao;

import java.util.Map;

import xap.lui.core.dataset.PaginationInfo;
import xap.sys.jdbc.handler.RsHandler;
import xap.sys.jdbc.kernel.SqlParam;

public class LuiCRUDServiceImpl implements LuiCRUDService {

	@Override
	public Object saveBean(Object obj) {
		return null;
	}

	@Override
	public Object[] saveBeans(Object[] objs) {
		return null;
	}

	@Override
	public void deleteBeans(Object[] objs) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteBean(Object obj, boolean trueDel) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteBean(Object obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <C> C[] queryBeans(Class<C> c, PaginationInfo pg, Map<String, Object> extMap) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <C> C[] queryBeanss(Class<C> clazz, String sql, PaginationInfo pg, Map<String, Object> extMap) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <C> C[] queryBeans(Class<C> clazz, String sql, PaginationInfo pg, String orderBy, Map<String, Object> extMap) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <C> C[] queryBeans(Class<C> c, PaginationInfo pg, String wherePart, Map<String, Object> extMap) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <C> C[] queryBeans(Class<C> c, PaginationInfo pg, String wherePart, Map<String, Object> extMap, String orderByPart) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <C> C[] queryBeans(Class<C> c, String sql) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <C> C queryBeanById(Class<C> clazz, String Id) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public int executeUpdate(String sql, SqlParam parameter) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int executeUpdate(String sql) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object query(String sql, RsHandler processor) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object query(String sql, PaginationInfo pg, RsHandler processor) {
		// TODO Auto-generated method stub
		return null;
	}

	

}
