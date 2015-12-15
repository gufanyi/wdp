package xap.lui.core.comps;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import xap.lui.core.exception.LuiRuntimeException;

/**
 * 递归Level定义
 * 递归树Level指此level对应的dataset行数据间有父子关系
 * @author dengjt
 *
 */
@XmlRootElement(name = "RecursiveTree")
@XmlAccessorType(XmlAccessType.NONE)
public class RecursiveTreeLevel extends TreeLevel {

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

	
	public String getType() {
		return TREE_LEVEL_TYPE_RECURSIVE;
	}

	public String getRecursiveField() {
		return recursiveField;
	}
	
	public void setRecursiveField(String recursiveKeyField) {
		this.recursiveField = recursiveKeyField;
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
	
}
