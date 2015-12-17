package xap.lui.core.refrence;

import java.util.List;

import ca.krasnay.sqlbuilder.SelectBuilder;
import xap.lui.core.dao.CRUDHelper;
import xap.lui.core.dataset.PaginationInfo;

public class RefTreeGridModel extends AbstractRefModel implements IRefTreeGridModel {
	private String classJoinValue = null;

	@Override
	public String getChildField() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCodingRule() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getFatherField() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getMark() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRootName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isNotLeafNodeSelected() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<List<Object>> filterNotLeafNode(List<List<Object>> data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getShowFieldCode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getHiddenFieldCode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getShowFieldName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getBlurFields() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRefTitle() {
		// TODO Auto-generated method stub
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

	public String getClassRefSql() {
		SelectBuilder builder = new SelectBuilder();
		String[] str1 = this.getClassFieldCode();
		if (str1 != null) {
			for (String str : str1) {
				builder.column(str);
			}
		}
		builder.where("1=1");
		String str2 = this.getClassTableName();
		builder.from(str2);
		String[] str3 = this.getClassWherePart();
		if (str3 != null) {
			for (String str : str3) {
				builder.where(str);
			}
		}
		String[] str4 = this.getClassOrderPart();
		if (str4 != null) {
			for (String str : str4) {
				builder.orderBy(str);
			}
		}
		return builder.toString();
	}

	@Override
	public RefResultSet getClassData() {
		String sql = this.getClassRefSql();
		try {
			@SuppressWarnings("unchecked")
			List<List<Object>> data = (List<List<Object>>) CRUDHelper.getCRUDService().executeQuery(sql, new RefResultHandler(-1, -1));
			RefResultSet result = new RefResultSet();
			result.setData(data);
			return result;
		} catch (Throwable e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	public String getRefSql() {
		SelectBuilder builder = new SelectBuilder();
		String[] allFiled = this.getAllFields();
		for (String str1 : allFiled) {
			builder.column(str1);
		}
		builder.from(this.getTableName());
		builder.where("1=1");
		List<String> wherePart = this.getWherePart();
		for (String str2 : wherePart) {
			builder.and(str2);
		}
		if (this.getDocJoinField() != null && this.getClassJoinValue() != null) {
			builder.and(this.getDocJoinField() + "='" + this.getClassJoinValue() + "'");
		}
		List<String> orderPart = this.getOrderPart();
		for (String str3 : orderPart) {
			builder.orderBy(str3);
		}
		List<String> groupPart = this.getGroupPart();
		for (String str4 : groupPart) {
			builder.groupBy(str4);
		}
		String sql = builder.toString();
		sql = this.afterBuilderSql(sql);
		return sql;
	}

	@Override
	public String getClassPkFieldCode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getClassFieldCode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getClassFieldIndex(String field) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getClassJoinField() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getClassJoinValue() {
		// TODO Auto-generated method stub
		return this.classJoinValue;
	}

	@Override
	public void setClassJoinValue(String strClassJoinValue) {
		this.classJoinValue = strClassJoinValue;
		// TODO Auto-generated method stub
	}

	@Override
	public String[] getClassOrderPart() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getClassRefCodeField() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getClassRefNameField() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getClassWherePart() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setClassWherePart(Object where) {
		// TODO Auto-generated method stub
	}

	@Override
	public String getDocJoinField() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getClassTableName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RefResultSet getRefData(PaginationInfo pInfo) {
		// TODO Auto-generated method stub
		return null;
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

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

}
