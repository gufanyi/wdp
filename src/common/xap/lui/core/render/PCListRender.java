package xap.lui.core.render;

import xap.lui.core.comps.ComboBoxComp;
import xap.lui.core.comps.LuiElement;
import xap.lui.core.constant.EditorTypeConst;
import xap.lui.core.event.IEventSupport;
import xap.lui.core.layout.UIComponent;
import xap.lui.core.layout.UITextField;
import xap.lui.core.model.LuiPageContext;

/**
 * @author renxh 组合框渲染器
 * @param <T>
 * @param <K>
 */
public class PCListRender extends UINormalComponentRender<UITextField, ComboBoxComp> {

	public PCListRender(ComboBoxComp webEle) {
		super(webEle);
	}

	@Override
	public String createBody() {
		StringBuffer buf = new StringBuffer();
		ComboBoxComp comboComp = this.getWebElement();
		UIComponent uiComp = this.getUiElement();
		String comboId = getVarId();
		buf.append("var ").append(comboId);
		if (comboComp.isShowMark())
			buf.append(" = new TextMarkComp(");
		else
			buf.append(" = new ComboComp(");
		buf.append("$ge('").append(getDivId()).append("'),'").append(comboComp.getId());
		buf.append("','0','0','100%','relative',");
		buf.append(comboComp.isOnlySelect()).append(",{'disabled':");
		buf.append(comboComp.isEnabled() ? "false" : "true").append(",'readOnly':");
		buf.append(comboComp.isReadOnly() ? "true" : "false");

		if (comboComp.getDataDivHeight() != null && !"".equals(comboComp.getDataDivHeight()))
			buf.append(",'dataDivHeight':'" + comboComp.getDataDivHeight() + "'");
		if (comboComp.isAllowExtendValue() == true)
			buf.append(",'allowExtendValue':true");

		if (null != comboComp.getText() && !"".equals(comboComp.getText())) { // 有标签属性
			buf.append(",'labelText':'").append(comboComp.getText()).append("','labelAlign':'").append(comboComp.getAlign()).append("','labelWidth':").append(comboComp.getTextWidth());

			if (comboComp.getValue() != null && !"".equals(comboComp.getValue())) {
				if (buf.length() > 1)
					buf.append(",");
				buf.append("'value':'").append(comboComp.getValue()).append("'");
			}
		}

		buf.append("}");

		buf.append(",'").append(uiComp.getClassName() == null ? "" : uiComp.getClassName()).append("');\n");

		// 隐藏下拉框
		if (comboComp.isVisible() == false) {
			buf.append(getVarId()).append(".hideV();\n");
		}

		buf.append(addRefItemScript(comboComp));

		buf.append("pageUI.getViewPart('" + this.getCurrWidget().getId() + "').addComponent(" + comboId + ");\n");
		return buf.toString();
	}

	/**
	 * 2011-8-2 下午08:12:33 renxh des：添加引用条目的脚本
	 * 
	 * @param combo
	 * @return
	 */
	private String addRefItemScript(ComboBoxComp combo) {

		StringBuffer buf = new StringBuffer();
		if (!combo.isShowMark()) {
			buf.append(getVarId()).append(".setShowImgOnly(").append(combo.isImageOnly() ? "true" : "false").append(");\n");
			if (combo.getRefComboData() != null) {
				String cbId = COMBO_PRE + getCurrWidget().getId() + "_" + combo.getRefComboData();
				buf.append(getVarId()).append(".setComboData(").append(cbId).append(");\n");
			}

			if (combo.getValue() != null && !"".equals(combo.getValue())) {
				buf.append(getVarId()).append(".setValue('").append(combo.getValue()).append("');\n");
			}

			if (combo.isReadOnly() == true)
				buf.append(getVarId()).append(".setReadOnly(true);\n");

			if (combo.isEnabled() == false)
				buf.append(getVarId()).append(".setActive(false);\n");

			if (combo.isVisible() == false)
				buf.append(getVarId()).append(".hideV();\n");
		}
		return buf.toString();
	}

	protected String getSourceType(IEventSupport ele) {
		return LuiPageContext.SOURCE_TYPE_TEXT;
	}

	@Override
	public String getRenderType(LuiElement ele) {
		return EditorTypeConst.COMBODATA;
	}

}
