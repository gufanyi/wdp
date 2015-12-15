package xap.lui.core.comps;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import xap.lui.core.context.BaseContext;
import xap.lui.core.context.FileContext;
import xap.lui.core.context.TextContext;
import xap.lui.core.model.FileValue;

@XmlRootElement(name = "File")
@XmlAccessorType(XmlAccessType.NONE)
public class FileComp extends TextComp {
	private static final long serialVersionUID = 1547017856179884134L;
	public static final String WIDGET_NAME = "file";
	
	private FileValue fileValue;
	
	@Override
	public String getWidgetName() {
		return WIDGET_NAME;
	}

	public FileValue getFileValue() {
		return fileValue;
	}

	public void setFileValue(FileValue fileValue) {
		this.fileValue = fileValue;
	}
	@Override
	public BaseContext getContext() {
		TextContext fileCtx = new TextContext();
		this.getContext(fileCtx);
		return fileCtx;
	}
	@Override
	public void setContext(BaseContext ctx) {
		FileContext fileCtx = (FileContext) ctx;
		FileValue value = fileCtx.getFileValue();
		this.setFileValue(value);
		this.setReadOnly(fileCtx.isReadOnly());
		this.setEnabled(fileCtx.isEnabled());
		this.setVisible(fileCtx.isVisible());

		this.setCtxChanged(false);
	}
	
}
