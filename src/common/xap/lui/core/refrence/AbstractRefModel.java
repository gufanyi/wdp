package xap.lui.core.refrence;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import ca.krasnay.sqlbuilder.SelectBuilder;
import xap.lui.core.dao.CRUDHelper;
import xap.lui.core.dataset.PaginationInfo;
public abstract class AbstractRefModel  implements IRefModel {
	private List<String> wheres = new ArrayList<String>();
	private List<String> orders = new ArrayList<String>();
	private List<String> groups = new ArrayList<String>();
	private String userPk = null;
	private String groupPk = null;
	private String orgPk = null;
	public int getFieldIndex(String filedName) {
		return -1;
	}
	@Override
	public String[] getAllFields() {
		return (String[]) ArrayUtils.addAll(this.getShowFieldCode(), this.getHiddenFieldCode());
	}
	@Override
	public int getAllFieldCount() {
		return this.getAllFields().length;
	}
	@Override
	public String getRefSql() {
		SelectBuilder builder = new SelectBuilder();
		String[] allFiled = this.getAllFields();
		String tableName = this.getTableName();
		List<String[]> whereFieldCode = this.getWhereFieldCode();
		List<String> writeFieldCode = this.getWriteFieldCode();
		List<String> refTableNameList = this.getRefTableName();
		for (String str1 : allFiled) {
			if((whereFieldCode!=null&&whereFieldCode.size()!=0) && (writeFieldCode!=null&&writeFieldCode.size()!=0) && (refTableNameList!=null&&refTableNameList.size()!=0)) {
				boolean hasWriteField = false;
				for(int i=0;i<refTableNameList.size();i++) {
					String tmpRefTableName = refTableNameList.get(i);
					String[] tmpWhereField = whereFieldCode.get(i);
					if(StringUtils.equals(tmpWhereField[1], str1)) {
						builder.column(tmpRefTableName + "." + writeFieldCode.get(i) + " " + str1);
						hasWriteField = true;
						break;
					}
				}
				if(!hasWriteField) {
					builder.column(tableName + "." + str1);
				}
			} else{
				builder.column(tableName + "." + str1);
			}
		}
		builder.from(tableName+" "+tableName);
		
		if((whereFieldCode!=null&&whereFieldCode.size()!=0) && (refTableNameList!=null&&refTableNameList.size()!=0)) {
			for(int i=0;i<refTableNameList.size();i++) {
				String tmpRefTableName = refTableNameList.get(i);
				StringBuilder buf = new StringBuilder();
				buf.append(tmpRefTableName).append(" ").append(tmpRefTableName).append(" on ");
				String[] tmpWhereField = whereFieldCode.get(i);
				buf.append(tableName).append(".").append(tmpWhereField[1]).append("=").append(tmpRefTableName).append(".").append(tmpWhereField[0]);
				builder.leftJoin(buf.toString());
			}
		}
		
		builder.where("1=1");
		List<String> wherePart = this.getWherePart();
		for (String str2 : wherePart) {
			builder.and(str2);
		}
		
		List<String> orderPart = this.getOrderPart();
		for (String str3 : orderPart) {
			builder.orderBy(this.getTableName()+"."+str3);
		}
		List<String> groupPart = this.getGroupPart();
		for (String str4 : groupPart) {
			builder.groupBy(this.getTableName()+"."+str4);
		}
		String sql = builder.toString();
		sql = this.afterBuilderSql(sql);
		return sql;
	}
	@Override
	public RefResultSet getRefData() {
		String refSql = this.getRefSql();
		try {
			@SuppressWarnings("unchecked")
			List<List<Object>> data = (List<List<Object>>)CRUDHelper.getCRUDService().executeQuery(refSql, new RefResultHandler(-1, -1));
			RefResultSet result = new RefResultSet();
			result.setData(data);
			if(data!=null)
				result.setTotalCount(data.size());
			return result;
		} catch (Throwable e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	@Override
	public RefResultSet getRefData(PaginationInfo pInfo) {
		return null;
	}
	public static String joinQryArrays(String str[]) {
		if (str == null || str.length == 0) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		int length = str.length;
		for (int i = 0; i < length; i++) {
			sb.append("'").append(str[i]).append("'");
			if (i == length - 1) {
				break;
			} else {
				sb.append(",");
			}
		}
		return sb.toString();
	}
	@Override
	public RefResultSet filterRefPks(String[] filterPks) {
		String fileterPks = joinQryArrays(filterPks);
		String pkFiled = this.getPkFieldCode();
		String str = this.getTableName() + "." + pkFiled + " in (" + fileterPks + ")";
		this.addWherePart(str);
		try {
			return this.getRefData();
		} finally {
			this.removeWherePart(str);
		}
	}
	private String cutOffAs(String oriField) {
		int index = oriField.indexOf(" as ");
		if (index != -1) {
			oriField = oriField.substring(0, index);
		}
		return oriField;
	}
	@Override
	public RefResultSet filterRefValue(String filterValue) {
		return filterRefValue(filterValue,null);
	}
	@Override
	public RefResultSet filterRefBlurValue(String blurValue) {
		String[] fields = this.getBlurFields();
		StringBuilder buf = new StringBuilder();
		buf.append("(");
		for (int i = 0; i < fields.length; i++) {
			String field = cutOffAs(fields[i]);
			buf.append("lower(");
			buf.append(this.getTableName()).append(".");
			buf.append(field);
			buf.append(")");
			buf.append(" like '%");
			buf.append(blurValue.toLowerCase());
			buf.append("%'");
			if (i != fields.length - 1)
				buf.append(" or ");
		}
		buf.append(")");
		String str = buf.toString();
		this.addWherePart(str);
		try {
			return this.getRefData();
		} finally {
			this.removeWherePart(str);
		}
	}
	@Override
	public boolean isUseDataPower() {
		return false;
	}
	@Override
	public boolean isIncludeBlurPart() {
		return false;
	}
	@Override
	public boolean isIncludeEnvPart() {
		return false;
	}
	@Override
	public String afterBuilderSql(String sql) {
		return sql;
	}
	@Override
	public void addWherePart(String wherePart) {
		if(StringUtils.isNotBlank(wherePart)) {
			wheres.add(wherePart);
		}
	}
	public void removeWherePart(String wherePart) {
		wheres.remove(wherePart);
	}
	@Override
	public List<String> getWherePart() {
		return wheres;
	}
	@Override
	public List<String> getGroupPart() {
		return groups;
	}
	@Override
	public List<String> getOrderPart() {
		return orders;
	}
	@Override
	public String getPk_group() {
		return groupPk;
	}
	public void setPk_group(String pk_group) {
		this.groupPk = pk_group;
	}
	@Override
	public String getPk_org() {
		return orgPk;
	}
	@Override
	public String getPk_user() {
		return userPk;
	}
	public void setPk_user(String pk_user) {
		this.userPk = pk_user;
	}
	public void setPk_org(String pk_org) {
		this.orgPk = pk_org;
	}
	public String value2ShowValue(String value) {
		return null;
	}
	
	@Override
	public int getPageSize() {
		return -1;
	}
	
	@Override
	public RefResultSet getRefData(int pageIndex) {
		return null;
	}
	@Override
	public RefResultSet filterRefValue(String filterValue, PaginationInfo pInfo) {
		String[] fields = this.getShowFieldCode();
		StringBuilder buf = new StringBuilder();
		buf.append("(");
		for (int i = 0; i < fields.length; i++) {
			String field = cutOffAs(fields[i]);
			List<String[]> whereFieldCode = this.getWhereFieldCode();
			List<String> writeFieldCode = this.getWriteFieldCode();
			List<String> refTableNameList = this.getRefTableName();
			if((whereFieldCode!=null&&whereFieldCode.size()!=0) && (writeFieldCode!=null&&writeFieldCode.size()!=0) && (refTableNameList!=null&&refTableNameList.size()!=0)) {
				boolean hasWriteField = false;
				for(int j=0;j<refTableNameList.size();j++) {
					String tmpRefTableName = refTableNameList.get(j);
					String[] tmpWhereField = whereFieldCode.get(j);
					if(StringUtils.equals(tmpWhereField[1], field)) {
						buf.append("lower(");
						buf.append(tmpRefTableName).append(".");
						buf.append(writeFieldCode.get(j));
						buf.append(")");
						buf.append(" like '%");
						buf.append(filterValue.toLowerCase());
						buf.append("%'");
						if (i != fields.length - 1)
							buf.append(" or ");
						hasWriteField = true;
						break;
					}
				}
				if(!hasWriteField) {
					buf.append("lower(");
					buf.append(this.getTableName()).append(".");
					buf.append(field);
					buf.append(")");
					buf.append(" like '%");
					buf.append(filterValue.toLowerCase());
					buf.append("%'");
					if (i != fields.length - 1)
						buf.append(" or ");
				}
			} else{
				buf.append("lower(");
				buf.append(this.getTableName()).append(".");
				buf.append(field);
				buf.append(")");
				buf.append(" like '%");
				buf.append(filterValue.toLowerCase());
				buf.append("%'");
				if (i != fields.length - 1)
					buf.append(" or ");
			}
			
		}
		buf.append(")");
		String str = buf.toString();
		this.addWherePart(str);
		try {
			if(pInfo == null || pInfo.getPageSize() == -1)
				return this.getRefData();
			else
				return this.getRefData(pInfo);
		} finally {
			this.removeWherePart(str);
		}
	}
}
