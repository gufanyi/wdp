package xap.lui.core.refrence;
import java.util.List;
/**
 * 树型参照接口
 */
public interface IRefTreeModel extends IRefGridModel {
	public String getChildField();//指示上下级关系－－子字段
 	public String getCodingRule();//指示编码规则（2212形式）。如果为空表示采用上下级关系构造树
	public String getFatherField();//指示上下级关系－－父字段
	public String getMark();//树节点显示之间隔字符
	public String getRootName();// 树根名－－为空不显示树根
	public boolean isNotLeafNodeSelected();//非叶子结点是否 可以选中 
	public List<List<Object>> filterNotLeafNode(List<List<Object>> data);//过滤非叶子结点
}
