package xap.lui.core.dataset;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import xap.lui.core.dataset.Field;

/**
 * 来自公共池ds中的字段
 * @author gd 2010-3-3
 *
 */
@XmlRootElement(name="PubField")
@XmlAccessorType(XmlAccessType.NONE)
public class PubField extends Field {
	private static final long serialVersionUID = 8666636558540326682L;
	public static String[] getCanModifyFIelds() {
		return null;
	}
}
