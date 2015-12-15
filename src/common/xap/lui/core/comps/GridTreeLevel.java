package xap.lui.core.comps;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import xap.lui.core.exception.LuiRuntimeException;


/**
 * 递归Level定义
 * 递归树Level指此level对应的dataset行数据间有父子关系
 *
 */

@XmlAccessorType(XmlAccessType.NONE)
public class GridTreeLevel extends LuiElement implements Serializable,Cloneable {

	private static final long serialVersionUID = 5754554948104868625L;
	/**
	 * 递归主键字段(level层内的参数)
	 */
	@XmlAttribute(name="recursiveField")
	protected String recursiveField;
	
	/**
	 * 递归父主键字段(level层内的参数)
	 */
	@XmlAttribute(name="recursiveParentField")
	protected String recursiveParentField;
	
	//标识当前级缓加载字段
	@XmlAttribute
	private String loadField;
	
	//标识是否是叶子节点
	@XmlAttribute
	private String leafField;
	/**
	 * 通过该属性指定显示字段,树节点显示的时候自动从该字段中取值并作为节点的标题显示,
	 * 字段可多个,中间以逗号分隔	.
	 */
	@XmlAttribute
	private String labelFields;
	@XmlAttribute
	private String dataset;
	
	// 本层级的父层级
	private GridTreeLevel parentLevel;

	// 本层级的孩子层级,孩子层级可以有并列的多级
	private GridTreeLevel childTreeLevel;

	public String getRecursiveField() {
		return recursiveField;
	}
	
	public void setRecursiveField(String recursiveField) {
		this.recursiveField = recursiveField;
	}
	
	public String getRecursiveParentField() {
		return recursiveParentField;
	}
	
	public void setRecursiveParentField(String recursiveKeyParameters) {
		this.recursiveParentField = recursiveKeyParameters;
	}
	
	public void mergeProperties(LuiElement ele) {
		throw new LuiRuntimeException("not implemented");
	}
	

	public String getLoadField() {
		return loadField;
	}

	public void setLoadField(String loadField) {
		this.loadField = loadField;
	}

	public String getLeafField() {
		return leafField;
	}

	public void setLeafField(String leafField) {
		this.leafField = leafField;
	}

	public String getLabelFields() {
		return labelFields;
	}

	public void setLabelFields(String labelFields) {
		this.labelFields = labelFields;
	}

	public String getDataset() {
		return dataset;
	}

	public void setDataset(String dataset) {
		this.dataset = dataset;
	}

	public GridTreeLevel getParentLevel() {
		return parentLevel;
	}

	public void setParentLevel(GridTreeLevel parentLevel) {
		this.parentLevel = parentLevel;
	}

	public GridTreeLevel getChildTreeLevel() {
		return childTreeLevel;
	}

	public void setChildTreeLevel(GridTreeLevel childTreeLevel) {
		this.childTreeLevel = childTreeLevel;
	}
	
}
