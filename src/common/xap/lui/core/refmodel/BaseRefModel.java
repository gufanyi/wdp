package xap.lui.core.refmodel;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import ca.krasnay.sqlbuilder.SelectBuilder;
import xap.lui.core.comps.ViewElement;
import xap.lui.core.dao.CRUDHelper;
import xap.lui.core.dataset.PaginationInfo;
import xap.lui.core.refrence.RefResultHandler;
import xap.lui.core.refrence.RefResultSet;

public class BaseRefModel extends ViewElement implements IXmlRefModel {
	private static final long serialVersionUID = -8981343502557229959L;
	/*
	 * 显示模型
	 */
	@XmlAttribute
	private String tableName;// 表名
	@XmlAttribute
	private String showFields;// 显示字段
	@XmlAttribute
	private String showFieldNames;// 显示字段中文名
	@XmlAttribute
	private String hiddenFields;// 隐藏字段
	@XmlAttribute
	private String blurFields;// 定位配置字段(过滤字段)
	@XmlAttribute
	private String wherePart;// where子句
	@XmlAttribute
	private String groupPart;// group by子句
	@XmlAttribute
	private String orderPart;// order by子句
	@XmlAttribute
	private int pageSize = -1;// 分页大小
	@XmlAttribute
	private String enviranmentPart;// 环境变量
	/*
	 * 回写信息
	 */
	@XmlAttribute
	private String refTitle;// 参照标题
	@XmlAttribute
	private String pkField;// 主键字段
	@XmlAttribute
	private String codeField;// 编码字段
	@XmlAttribute
	private String nameField;// 名称字段

	/**
	 * 环境变量
	 */
	private String pk_group;
	private String pk_org;
	private String pk_user;

	public String getPk_group() {
		return pk_group;
	}

	public void setPk_group(String pk_group) {
		this.pk_group = pk_group;
	}

	public String getPk_org() {
		return pk_org;
	}

	public void setPk_org(String pk_org) {
		this.pk_org = pk_org;
	}

	public String getPk_user() {
		return pk_user;
	}

	public void setPk_user(String pk_user) {
		this.pk_user = pk_user;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getShowFields() {
		return showFields;
	}

	public void setShowFields(String showFields) {
		this.showFields = showFields;
	}

	public String getShowFieldNames() {
		return showFieldNames;
	}

	public void setShowFieldNames(String showFieldNames) {
		this.showFieldNames = showFieldNames;
	}

	public String getHiddenFields() {
		return hiddenFields;
	}

	public void setHiddenFields(String hiddenFields) {
		this.hiddenFields = hiddenFields;
	}

	public String getBlurFields() {
		return blurFields;
	}

	public void setBlurFields(String blurFields) {
		this.blurFields = blurFields;
	}

	public String getWherePart() {
		return wherePart;
	}

	public void setWherePart(String wherePart) {
		this.wherePart = wherePart;
	}

	public String getGroupPart() {
		return groupPart;
	}

	public void setGroupPart(String groupPart) {
		this.groupPart = groupPart;
	}

	public String getOrderPart() {
		return orderPart;
	}

	public void setOrderPart(String orderPart) {
		this.orderPart = orderPart;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public String getEnviranmentPart() {
		return enviranmentPart;
	}

	public void setEnviranmentPart(String enviranmentPart) {
		this.enviranmentPart = enviranmentPart;
	}

	public String getRefTitle() {
		return refTitle;
	}

	public void setRefTitle(String refTitle) {
		this.refTitle = refTitle;
	}

	public String getPkField() {
		return pkField;
	}

	public void setPkField(String pkField) {
		this.pkField = pkField;
	}

	public String getCodeField() {
		return codeField;
	}

	public void setCodeField(String codeField) {
		this.codeField = codeField;
	}

	public String getNameField() {
		return nameField;
	}

	public void setNameField(String nameField) {
		this.nameField = nameField;
	}

	@Override
	public RefResultSet getRefData(int pageIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RefResultSet filterRefValue(String filterValue, PaginationInfo pInfo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RefResultSet getRefData(PaginationInfo pInfo) {
		// TODO Auto-generated method stub
		return null;
	}

	public String afterBuilderSql(String sql) {
		return sql;
	}

	public int getFieldIndex(String filedName) {
		return -1;
	}

	public String[] getAllFields() {
		if (StringUtils.isNotBlank(this.getHiddenFields()))
			return (String[]) ArrayUtils.addAll(this.getShowFields().split(","), this.getHiddenFields().split(","));
		else
			return (String[]) ArrayUtils.addAll(this.getShowFields().split(","), null);
	}

	public int getAllFieldCount() {
		return this.getAllFields().length;
	}

	public String getRefSql() {
		SelectBuilder builder = new SelectBuilder();
		String[] allFiled = this.getAllFields();
		String tableName = this.getTableName();
		String wherePart = this.getWherePart();
		String groupPart = this.getGroupPart();
		String orderPart = this.getOrderPart();

		for (String str1 : allFiled) {
			builder.column(tableName + "." + str1);
		}
		builder.from(tableName + " " + tableName);
		if (StringUtils.isNotBlank(wherePart))
			builder.where("1=1 and " + wherePart);
		else
			builder.where("1=1");

		// if(StringUtils.isNotBlank(this.getEnviranmentPart())){
		// String[] ens = this.getEnviranmentPart().split(",");
		// for(String str : ens){
		// if(StringUtils.equals(str, LuiRefConstant.CURR_GROUP))
		// this.setPk_group(str);
		// else if(StringUtils.equals(str, LuiRefConstant.CURR_ORG))
		// this.setPk_org(str);
		// else if(StringUtils.equals(str, LuiRefConstant.CURR_USER))
		// this.setPk_user(str);
		// }
		// String pk_group = this.getPk_group();
		// if(StringUtils.isNotBlank(pk_group))
		// builder.and("Id_group=" + Context.get().getGroupId());
		// String pk_org = this.getPk_org();
		// if(StringUtils.isNotBlank(pk_org))
		// builder.and("Id_org=" + Context.get().getOrgId());
		// String pk_user = this.getPk_user();
		// if(StringUtils.isNotBlank(pk_user))
		// builder.and("Id_user = " + Context.get().getUserId());
		// }

		if (StringUtils.isNotBlank(orderPart))
			builder.orderBy(orderPart);
		if (StringUtils.isNotBlank(groupPart))
			builder.groupBy(groupPart);

		String sql = builder.toString();
		sql = this.afterBuilderSql(sql);
		return sql;
	}

	public RefResultSet getRefData() {
		String refSql = this.getRefSql();
		try {
			@SuppressWarnings("unchecked")
			List<List<Object>> data = (List<List<Object>>) CRUDHelper.getCRUDService().query(refSql, new RefResultHandler(-1, -1));
			RefResultSet result = new RefResultSet();
			result.setData(data);
			if (data != null)
				result.setTotalCount(data.size());
			return result;
		} catch (Throwable e) {
			throw new RuntimeException(e.getMessage());
		}
	}
}
