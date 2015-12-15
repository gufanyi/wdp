package xap.lui.core.dataset;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import xap.lui.core.dataset.Field;

/**
 * 对公共池中字段进行修改后的字段
 * @author gd 2010-3-3
 *
 */
@XmlRootElement(name="ModifyField")
@XmlAccessorType(XmlAccessType.NONE)
public class ModifyField extends Field {
	private static final long serialVersionUID = -2366079094155692848L;
}
