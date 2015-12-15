package xap.lui.core.comps;

import xap.lui.core.constant.EditorTypeConst;
import xap.lui.core.context.BaseContext;
import xap.lui.core.context.ComboBoxContext;
import xap.lui.core.dataset.ComboData;

/**
 * 多语下拉框类型编辑控件
 */
public class LanguageComboBoxComp extends TextComp {
	private static final long serialVersionUID = 2847503923257565737L;
	private String refComboData;
	// 是否为仅显示图片
	private boolean imageOnly = false;
	private boolean selectOnly = false;
	// 数据区的高度,可以不指定该参数
	private String dataDivHeight = null;
	// 是否允许存在下拉数据之外的值
	private boolean allowExtendValue = false;
	// 是否替换为不可见图片显示
	private boolean showMark = false;
	
	public LanguageComboBoxComp() {
		super();
		setEditorType(EditorTypeConst.LANGUAGECOMBODATA);
	}
	public LanguageComboBoxComp(String id) {
		super(id);
		setEditorType(EditorTypeConst.LANGUAGECOMBODATA);
	}
	public boolean isShowMark() {
		return showMark;
	}
	public void setShowMark(boolean showMark) {
		this.showMark = showMark;
	}
	public boolean isImageOnly() {
		return imageOnly;
	}
	public void setImageOnly(boolean imageOnly) {
		this.imageOnly = imageOnly;
	}
	public boolean isSelectOnly() {
		return selectOnly;
	}
	public void setSelectOnly(boolean selectOnly) {
		this.selectOnly = selectOnly;
	}
	public String getDataDivHeight() {
		return dataDivHeight;
	}
	public void setDataDivHeight(String dataDivHeight) {
		this.dataDivHeight = dataDivHeight;
	}
	public String getRefComboData() {
		return refComboData;
	}
	public void setRefComboData(String refComboData) {
		this.refComboData = refComboData;
	}
	public ComboData getComboData() {
		ComboData cb = getWidget().getViewModels().getComboData(this.getRefComboData());
		return cb;
	}
	public boolean isAllowExtendValue() {
		return allowExtendValue;
	}
	public void setAllowExtendValue(boolean allowExtendValue) {
		this.allowExtendValue = allowExtendValue;
	}
	
	@Override
	public BaseContext getContext() {
		ComboBoxContext comboCtx = new ComboBoxContext();
//		comboCtx.setEnabled(isEnabled());
//		comboCtx.setValue(getValue());
//		comboCtx.setVisible(this.isVisible());
		return comboCtx;
	}
	
	@Override
	public void setContext(BaseContext ctx) {
		ComboBoxContext comboCtx = (ComboBoxContext)ctx;
		this.setEnabled(comboCtx.isEnabled());
		this.setValue(comboCtx.getValue());
		this.setVisible(comboCtx.isVisible());
		setCtxChanged(false);
	}
}
