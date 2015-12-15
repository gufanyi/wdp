package xap.lui.core.comps;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "DateText")
@XmlAccessorType(XmlAccessType.NONE)
public class DateTextComp extends TextComp {
	private static final long serialVersionUID = 1547017856179884134L;
	public static final String WIDGET_NAME = "datetext";
	
	@XmlAttribute
	private Boolean multiple = false;
	@XmlAttribute
	private String multiSplitChar = ",";
	
	@Override
	public String getWidgetName() {
		return WIDGET_NAME;
	}

	public Boolean getMultiple() {
		return multiple;
	}

	public void setMultiple(Boolean multiple) {
		this.multiple = multiple;
	}

	public String getMultiSplitChar() {
		return multiSplitChar;
	}

	public void setMultiSplitChar(String multiSplitChar) {
		this.multiSplitChar = multiSplitChar;
	}
	
	
}
