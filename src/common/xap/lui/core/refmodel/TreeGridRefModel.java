package xap.lui.core.refmodel;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.StringUtils;

import ca.krasnay.sqlbuilder.SelectBuilder;
import xap.lui.core.dao.CRUDHelper;
import xap.lui.core.refrence.RefResultHandler;
import xap.lui.core.refrence.RefResultSet;
@XmlRootElement(name="TreeGridRefModel")
@XmlAccessorType(XmlAccessType.NONE)
public class TreeGridRefModel extends TreeRefModel {
	private static final long serialVersionUID = 3763461972329362356L;
	@XmlAttribute
	private String classTableName;//树表名
	@XmlAttribute
	private String classPKField;  //树主键
	@XmlAttribute
	private String classNameField;
	@XmlAttribute
	private String classCodeField;
	/*
	 * select ... from classTableName where classJoinField = classJoinValue
	 * docJoinField相当于表格的过滤字段docJoinField = classJoinValue
	 */
	@XmlAttribute
	private String classJoinField; 
	@XmlAttribute
	private String classJoinValue;
	@XmlAttribute
	private String classOrderPart;
	@XmlAttribute
	private String classWherePart;
	@XmlAttribute
	private String docJoinField;   //表格连接字段
	
	public String[] getClassFieldCode() {
		return new String[]{this.getClassPKField(),this.getClassNameField()};
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
		String str3 = this.getClassWherePart();
		if(StringUtils.isNotBlank(str3))
			builder.where(str3);
		String str4 = this.getClassOrderPart();
		if(StringUtils.isNotBlank(str4))
			builder.orderBy(str4);
		return builder.toString();
	}
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
		String wherePart = this.getWherePart();
		if(StringUtils.isNotBlank(wherePart))
			builder.where("1=1 and " + wherePart);
		else
			builder.where("1=1");
		if (this.getDocJoinField() != null && this.getClassJoinValue() != null) {
			builder.and(this.getDocJoinField() + "='" + this.getClassJoinValue()+"'");
		}
		
		String orderPart = this.getOrderPart();
		if(StringUtils.isNotBlank(orderPart))
			builder.orderBy(orderPart);
		String groupPart = this.getGroupPart();
		if(StringUtils.isNotBlank(groupPart))
			builder.groupBy(groupPart);
		
		String sql = builder.toString();
		sql = this.afterBuilderSql(sql);
		return sql;
	}
	
	public String getClassTableName() {
		return classTableName;
	}
	public void setClassTableName(String classTableName) {
		this.classTableName = classTableName;
	}
	public String getClassPKField() {
		return classPKField;
	}
	public void setClassPKField(String classPKField) {
		this.classPKField = classPKField;
	}
	public String getClassNameField() {
		return classNameField;
	}
	public void setClassNameField(String classNameField) {
		this.classNameField = classNameField;
	}
	public String getClassCodeField() {
		return classCodeField;
	}
	public void setClassCodeField(String classCodeField) {
		this.classCodeField = classCodeField;
	}
	public String getClassJoinField() {
		return classJoinField;
	}
	public void setClassJoinField(String classJoinField) {
		this.classJoinField = classJoinField;
	}
	public String getClassJoinValue() {
		return classJoinValue;
	}
	public void setClassJoinValue(String classJoinValue) {
		this.classJoinValue = classJoinValue;
	}
	public String getClassOrderPart() {
		return classOrderPart;
	}
	public void setClassOrderPart(String classOrderPart) {
		this.classOrderPart = classOrderPart;
	}
	public String getClassWherePart() {
		return classWherePart;
	}
	public void setClassWherePart(String classWherePart) {
		this.classWherePart = classWherePart;
	}
	public String getDocJoinField() {
		return docJoinField;
	}
	public void setDocJoinField(String docJoinField) {
		this.docJoinField = docJoinField;
	}
}
