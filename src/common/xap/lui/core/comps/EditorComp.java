package xap.lui.core.comps;

import xap.lui.core.exception.LuiRuntimeException;

/**
 * 编辑器控件配置类
 * 
 * @author dengjt
 * 
 */
public class EditorComp extends WebComp {

	private static final long serialVersionUID = 2731851377588527207L;
	
	public static final String WIDGET_NAME = "editor";
	private String hideBarIndices;
	private String hideImageIndices;
	private String value = "";
	private boolean readOnly = false;

	@Override
	public String getWidgetName() {
		return WIDGET_NAME;
	}
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		if (value != null && !value.equals(this.value)) {
			this.value = value;
			setCtxChanged(true);
		}
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
		setCtxChanged(true);
	}

	public String getHideBarIndices() {
		return hideBarIndices;
	}

	public void setHideBarIndices(String hideBarIndices) {
		this.hideBarIndices = hideBarIndices;
	}

	public String getHideImageIndices() {
		return hideImageIndices;
	}

	public void setHideImageIndices(String hideImageIndices) {
		this.hideImageIndices = hideImageIndices;
	}

	public void mergeProperties(LuiElement ele) {
		throw new LuiRuntimeException("not implemented");
	}

}
