package xap.lui.core.context;

import xap.lui.core.model.FileValue;

public class FileContext extends BaseContext {
	private static final long serialVersionUID = -7121257978050697866L;
	private FileValue fileValue;
	private boolean readOnly;
	private boolean enabled = true;
	private boolean focus = false;
	private boolean visible = true;
	public boolean isVisible() {
		return visible;
	}
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	public FileValue getFileValue() {
		return fileValue;
	}
	public void setFileValue(FileValue fileValue) {
		this.fileValue = fileValue;
	}
	public boolean isReadOnly() {
		return readOnly;
	}
	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public boolean isFocus() {
		return focus;
	}
	public void setFocus(boolean focus) {
		this.focus = focus;
	}
}
