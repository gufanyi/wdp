package xap.lui.core.refrence;


public interface IRefTreeGridModel extends IRefTreeModel {
	public RefResultSet getClassData();// 获取已经读出的参照数据－－二维Vector
	public String getClassPkFieldCode();
	public String[] getClassFieldCode();// 分类表字段数组
	public String getClassTableName();
	public int getClassFieldIndex(String field);// 得到一个字段在所有字段中的下标
	public String getClassJoinField();// 分类表中和档案连接的字段---一般是分类主键
	public String getClassJoinValue();// 分类表中和档案连接的字段---一般是分类主键
	public void setClassJoinValue(String strClassJoinValue);// 分类表中和档案连接的字段选择后的值
	public String[] getClassOrderPart();// 分类表Where子句
	public String getClassRefCodeField();// 分类编码字段
	public String getClassRefNameField();// 分类名称字段
	public String[] getClassWherePart();// 分类表Where子句
	public void setClassWherePart(Object where);//
	public String getDocJoinField();// 档案表中和分类连接的字段--(表格的连接字段)
}
