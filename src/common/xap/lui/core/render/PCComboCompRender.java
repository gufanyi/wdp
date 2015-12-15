package xap.lui.core.render;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import xap.lui.core.comps.ComboBoxComp;
import xap.lui.core.comps.LuiElement;
import xap.lui.core.constant.EditorTypeConst;
import xap.lui.core.event.IEventSupport;
import xap.lui.core.layout.UIComponent;
import xap.lui.core.model.LuiPageContext;

import com.alibaba.fastjson.JSON;

/**
 * @author renxh 组合框渲染器
 * @param <T>
 * @param <K>
 */
public class PCComboCompRender extends PCTextCompRender {

	public PCComboCompRender(ComboBoxComp webEle) {
		super(webEle);
	}

	@Override
	public String createBody() {
		StringBuilder buf = new StringBuilder();
		ComboBoxComp comboComp = (ComboBoxComp)this.getWebElement();
		UIComponent uiComp = this.getUiElement();
		String comboId = getVarId();
		buf.append("var ").append(comboId);
		buf.append(" = $(\"<div id='").append(comboComp.getId()).append("'>\").");
		buf.append("appendTo($('#" + getDivId() + "')).");
		if (comboComp.isShowMark())
			buf.append("textMarkComp(");
		else
			buf.append("combo(");

		buf.append(generateParam(comboComp, uiComp));

		buf.append("\n).");
		if (comboComp.isShowMark())
			buf.append("textMarkComp('instance');\n");
		else
			buf.append("combo('instance');\n");

		// 隐藏下拉框
		if (comboComp.isVisible() == false) {
			buf.append(getVarId()).append(".hideV();\n");
		}
		if(hasFormula(comboComp,"validate_method") || hasFormula(comboComp,"editor_method")) {
			buf.append(comboId).append(".setFormular(true);\n");
		}
		buf.append(addRefItemScript(comboComp));

		buf.append(comboId).append(".ctxChanged = false;\n");

		buf.append("pageUI.getViewPart('" + this.getCurrWidget().getId() + "').addComponent('" + comboComp.getId() + "'," + comboId + ");\n");
		buf.append(comboId + ".viewpart = pageUI.getViewPart('" + this.getCurrWidget().getId() + "');\n");

		return buf.toString();
	}

	/**
	 * 构造参数
	 * 
	 * @param radio
	 * @return
	 */
	private String generateParam(ComboBoxComp comboComp, UIComponent uiComp) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("selectOnly", comboComp.isOnlySelect());
		paramMap.put("disabled", !comboComp.isEnabled());
		paramMap.put("readOnly", comboComp.isReadOnly());
		if (!StringUtils.isEmpty(comboComp.getDataDivHeight()))
			paramMap.put("dataDivHeight", comboComp.getDataDivHeight());
		paramMap.put("allowExtendValue", comboComp.isAllowExtendValue());
		if (StringUtils.isNotBlank(comboComp.getText())) { // 有标签属性
			// String combText = translate(comboComp.getI18nName(),
			// comboComp.getText(), comboComp.getLangDir()).trim();
			paramMap.put("labelText", comboComp.getText());
			paramMap.put("labelAlign", comboComp.getAlign());
			paramMap.put("labelWidth", comboComp.getTextWidth());

			if (!StringUtils.isEmpty(comboComp.getValue())) {
				paramMap.put("value", comboComp.getValue());
			}
		}
		paramMap.put("visibleOptionsNum", comboComp.getVisibleOptionsNum());
		paramMap.put("position", "relative");
		if (!StringUtils.isEmpty(uiComp.getClassName()))
			paramMap.put("className", uiComp.getClassName());
		paramMap.put("width", uiComp.getWidth());
		paramMap.put("multiple", comboComp.getMultiple());
		paramMap.put("multiSplitChar", comboComp.getMultiSplitChar());
		return JSON.toJSONString(paramMap);
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

	public String getType() {
		return "combotext";
	}
}
