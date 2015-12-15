package xap.lui.core.refrence;

import java.util.List;

import xap.lui.core.dao.CRUDHelper;
import xap.lui.core.dataset.PaginationInfo;


public class RefGridModel extends AbstractRefModel implements IRefGridModel {
	
	@Override
	public String[] getShowFieldCode() {
		return null;
	}
	@Override
	public String[] getHiddenFieldCode() {
		return null;
	}
	@Override
	public String[] getShowFieldName() {
		return null;
	}
	@Override
	public String[] getBlurFields() {
		return null;
	}
	@Override
	public String getRefTitle() {
		return null;
	}
	@Override
	public String getPkFieldCode() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getRefCodeField() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getRefNameField() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public RefResultSet getRefData(PaginationInfo pInfo) {
		String refSql = this.getRefSql();
		@SuppressWarnings("unchecked")
		List<List<Object>> data = (List<List<Object>>)CRUDHelper.getCRUDService().query(refSql,pInfo, new RefResultHandler(-1, -1));
		RefResultSet result = new RefResultSet();
		result.setData(data);
		return result;
	}
	@Override
	public List<String[]> getWhereFieldCode() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<String> getWriteFieldCode() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<String> getRefTableName() {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
