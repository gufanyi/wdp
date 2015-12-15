package xap.lui.core.comps;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import xap.lui.core.constant.EditorTypeConst;

@XmlRootElement(name = "IntegerText")
@XmlAccessorType(XmlAccessType.NONE)
public class IntegerTextComp extends TextComp {
	private static final long serialVersionUID = 3554449844418773834L;
	public static final String WIDGET_NAME = "integertext";
	
	@Override
	public String getWidgetName() {
		return WIDGET_NAME;
	}
	
}
