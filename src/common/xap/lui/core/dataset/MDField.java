package xap.lui.core.dataset;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import xap.lui.core.dataset.Field;

/**
 * 从元数据得到的field
 * @author zhangxya
 *
 */
@XmlRootElement(name="MDField")
@XmlAccessorType(XmlAccessType.NONE)
public class MDField extends Field {
	private static final long serialVersionUID = 8666636558540326682L;
}
