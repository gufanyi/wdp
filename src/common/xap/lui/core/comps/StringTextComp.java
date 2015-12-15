package xap.lui.core.comps;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "StringText")
@XmlAccessorType(XmlAccessType.NONE)
public class StringTextComp extends TextComp {
	private static final long serialVersionUID = 1547017856179884134L;
	public static final String WIDGET_NAME = "stringtext";
	@Override
	public String getWidgetName() {
		return WIDGET_NAME;
	}

}
