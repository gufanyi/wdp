package xap.lui.core.render;

import java.util.HashMap;
import java.util.Map;

import xap.lui.core.comps.LuiElement;
import xap.lui.core.comps.RadioGroupComp;
import xap.lui.core.comps.WebComp;
import xap.lui.core.constant.EditorTypeConst;
import xap.lui.core.event.IEventSupport;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.layout.UIComponent;
import xap.lui.core.model.LuiPageContext;

import com.alibaba.fastjson.JSON;

/**
 * @author renxh Radio集合组件渲染器
 * 
 */
public class PCRadioGroupCompRender extends PCTextCompRender{

	public PCRadioGroupCompRender(RadioGroupComp webEle) {
		super(webEle);
	}


	@Override
	public String createBody() {
		WebComp component = this.getWebElement();
		UIComponent uiComp = this.getUiElement();
		if (!(component instanceof RadioGroupComp))
			throw new LuiRuntimeException("标签配置出错，" + this.getId() + "不是RadioComp类型！");

		RadioGroupComp rg = (RadioGroupComp) component;
		String id = getVarId();
		String widgetId = this.getCurrWidget().getId();
		StringBuilder buf = new StringBuilder();
		buf.append("window.").append(id).append(" = $(\"<div id='" + this.getId() + "'></div>\").appendTo($('#" + getDivId() + "')).radiogroup(\n");
		buf.append(generateParam(rg, uiComp));
		buf.append(").radiogroup('instance');\n");

		buf.append("pageUI.getViewPart('" + widgetId + "').addComponent('" + this.getId() + "'," + id + ");\n");
		
		buf.append(id + ".viewpart = pageUI.getViewPart('" + widgetId + "');\n");
		if (!rg.isVisible()) {
			buf.append(id).append(".hideV();\n");
		}
		if (rg.isReadOnly()) {
			buf.append(id).append(".setReadOnly(true);\n");
		}
		if(hasFormula(rg,"validate_method") || hasFormula(rg,"editor_method")) {
			buf.append(id).append(".setFormular(true);\n");
		}
		if ("Y".equals(this.getUiElement().getValgin())) {
			buf.append(id).append(".setValgin(true);\n");
		}

		return buf.toString();
	}

	@Override
	protected String creatBodyTail() {
		StringBuilder buf = new StringBuilder();
		WebComp component = this.getWebElement();
		RadioGroupComp rg = (RadioGroupComp) component;
		String widgetId = this.getCurrWidget().getId();
		if (rg.getDataListId() != null) {
			// 加载子项
			String cbId = COMBO_PRE + widgetId + "_" + rg.getDataListId();
			buf.append(getVarId()).append(".setComboData(").append(cbId).append(",").append(rg.getSepWidth()).append(");\n");
		}
//		if (rg.getValue() != null) {
//			buf.append(getVarId()).append(".setValue('").append(rg.getValue()).append("');\n");
//		}
		return buf.toString();
	}
	
	/**
	 * 构造参数
	 * 
	 * @param rg
	 * @param uiComp
	 * @return
	 */
	private String generateParam(RadioGroupComp rg, UIComponent uiComp) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("position", "relative");
		paramMap.put("labelText", rg.getText());
		paramMap.put("labelAlign", rg.getAlign());
		paramMap.put("labelWidth", rg.getTextWidth());
		paramMap.put("disabled", !rg.isEnabled());
		paramMap.put("readOnly", rg.isReadOnly());
		paramMap.put("tabIndex", rg.getTabIndex());
		paramMap.put("visible", rg.isVisible());
		paramMap.put("changeLine", rg.isChangeLine());
		if (rg.getValue() != null) {
			paramMap.put("radioValue", rg.getValue());
		}
		if (uiComp.getClassName() != null) {
			paramMap.put("className", uiComp.getClassName());
		}

		return JSON.toJSONString(paramMap);
	}

	protected String getSourceType(IEventSupport ele) {
		return LuiPageContext.SOURCE_TYPE_TEXT;
	}

	@Override
	public String getRenderType(LuiElement ele) {
		return EditorTypeConst.RADIOGROUP;
	}

	public String getType() {
		return "radiogrouptext";
	}
	
	public void bindDataList(String dataListId) {
		String cbId = COMBO_PRE + getViewId() + "_" + dataListId;
		StringBuilder buf = new StringBuilder();
		buf.append("pageUI.getViewPart('").append(this.getViewId()).append("').getComponent('").append(this.getId()).append("')");
		int stepWidht=((RadioGroupComp)this.getWebElement()).getSepWidth();
		buf.append(".setComboData(").append(cbId).append(",").append(stepWidht).append(");\n");
		addDynamicScript(buf.toString());
	}

}
