package xap.lui.core.context;

public class GridContext extends BaseContext {
	private static final long serialVersionUID = -1969504825610051676L;
	private boolean enabled;
	private boolean editable;
	// 显示列ID字符串，用“,”分割，中间不能有空格，用于在后台中向前台传递context
	private String showColumns;
	private boolean isShowTip;
	
	/**
	 * 当前选列的ID
	 */
	private String currentColID;
	
	private boolean multiSelect;
	
	private boolean isAllowMouseoverChange;
	
	public String getCurrentColID() {
		return currentColID;
	}
	public void setCurrentColID(String currentColID) {
		this.currentColID = currentColID;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public boolean isEditable() {
		return editable;
	}
	public void setEditable(boolean editable) {
		this.editable = editable;
	}
	public String getShowColumns() {
		return showColumns;
	}
	public void setShowColumns(String showColumns) {
		this.showColumns = showColumns;
	}
	public boolean isMultiSelect() {
		return multiSelect;
	}
	public void setMultiSelect(boolean multiSelect) {
		this.multiSelect = multiSelect;
	}
	public boolean isAllowMouseoverChange() {
		return isAllowMouseoverChange;
	}
	public void setAllowMouseoverChange(boolean isAllowMouseoverChange) {
		this.isAllowMouseoverChange = isAllowMouseoverChange;
	}
	public boolean isShowTip() {
		return isShowTip;
	}
	public void setShowTip(boolean isShowTip) {
		this.isShowTip = isShowTip;
	}
}
