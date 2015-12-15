package xap.lui.core.comps;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import xap.lui.core.exception.LuiRuntimeException;

/**
 * 简单树Level定义
 * 简单树Level即此Level对应的dataset行数据间没有父子关系
 * @author dengjt
 *
 */
@XmlRootElement(name = "SimpleTreeLevel")
@XmlAccessorType(XmlAccessType.NONE)
public class SimpleTreeLevel extends TreeLevel {

	private static final long serialVersionUID = -8998177771506215135L;

	public String getType() {
		return TREE_LEVEL_TYPE_SIMPLE;
	}
	
	public void mergeProperties(LuiElement ele) {
		throw new LuiRuntimeException("not implemented");
	}
}
