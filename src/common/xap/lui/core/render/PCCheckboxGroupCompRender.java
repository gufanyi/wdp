package xap.lui.core.render;

import java.util.HashMap;
import java.util.Map;

import xap.lui.core.comps.CheckboxGroupComp;
import xap.lui.core.comps.LuiElement;
import xap.lui.core.comps.WebComp;
import xap.lui.core.constant.EditorTypeConst;
import xap.lui.core.event.IEventSupport;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.layout.UIComponent;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.model.LuiPageContext;

import com.alibaba.fastjson.JSON;

/**
 * @author renxh
 * checkbox组渲染器
 * @param <T>
 * @param <K>
 */
public class PCCheckboxGroupCompRender extends PCTextCompRender {

	public PCCheckboxGroupCompRender(CheckboxGroupComp webEle) {
		super(webEle);
	}
	


	@Override
	public String createBody() {
		WebComp component = this.getWebElement();
		UIComponent uiComp = this.getUiElement();
		if (!(component instanceof CheckboxGroupComp))
			throw new LuiRuntimeException("标签配置出错，"+this.getId()+"不是RadioComp类型！");

		CheckboxGroupComp cbg = (CheckboxGroupComp) component;
		String id = getVarId();
		String widgetId = this.getCurrWidget().getId();
		StringBuilder buf = new StringBuilder();
		// CheckboxGroupComp(parent, name, left, top, width, position, attrArr,
		// className)
		buf.append("window.").append(id);
		buf.append(" = $(\"<div id='").append(this.getId()).append("'>\")");
		buf.append(".appendTo($('#"+getDivId()+"'))");
		buf.append(".checkboxgroup(\n");
		buf.append(generateParam(cbg,uiComp));
		buf.append("\n).checkboxgroup('instance');\n");

		buf.append("pageUI.getViewPart('" + widgetId + "').addComponent('" + this.getId() + "'," + id + ");\n");
		if(cbg.getDataListId() != null){
			// 加载子项
//			String cbId = COMBO_PRE + widgetId + "_" + cbg.getComboDataId();
//			buf.append(id).append(".setComboData(").append(cbId).append(",").append(cbg.getSepWidth()).append(");\n");
			buf.append("var comboData = pageUI.getViewPart('" + widgetId + "').getComboData('" + cbg.getDataListId() + "');\n");
			buf.append(id).append(".setComboData(comboData,").append(cbg.getSepWidth()).append(");\n");
			
		}
		buf.append(id + ".viewpart = pageUI.getViewPart('" + widgetId + "');\n");
		if (cbg.getValue() != null) {
			buf.append(id).append(".setValue('").append(cbg.getValue()).append("');\n");
		}
		if (!cbg.isVisible()) {
			buf.append(id).append(".hideV();\n");
		}
		if (cbg.isReadOnly()) {
			buf.append(id).append(".setReadOnly(true);\n");
		}
		
		if(hasFormula(cbg,"validate_method") || hasFormula(cbg,"editor_method")) {
			buf.append(id).append(".setFormular(true);\n");
		}
		
		if ("Y".equals(getUiElement().getValgin())){
			buf.append(id).append(".setValgin(true);\n");
		}
		return buf.toString();
	}
	

	/**
	 * 构造参数
	 * @param radio
	 * @return
	 */
	private String generateParam(CheckboxGroupComp cbg ,UIComponent uiComp) {
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("position", "relative");
		paramMap.put("labelText", cbg.getText() == null ? null : cbg.getText());
		paramMap.put("labelAlign", cbg.getAlign() == null ? null : cbg.getAlign());
		paramMap.put("labelWidth", cbg.getTextWidth());
		paramMap.put("disabled", !cbg.isEnabled());
		paramMap.put("readOnly", cbg.isReadOnly());
		paramMap.put("tabIndex", cbg.getTabIndex());
		paramMap.put("changeLine", cbg.isChangeLine());
		paramMap.put("width", uiComp.getWidth());
		if(uiComp.getClassName() != null){
			paramMap.put("className", uiComp.getClassName());
		} else {
			paramMap.put("className", "checkboxgroup_div");
		}
		return JSON.toJSONString(paramMap);
	}
	
	protected String getSourceType(IEventSupport ele) {
		return LuiPageContext.SOURCE_TYPE_TEXT;
	}


	@Override
	public String getRenderType(LuiElement ele) {
		return EditorTypeConst.CHECKBOXGROUP;
	}
	
	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void setDataList(String dataListId){
		String cbId = COMBO_PRE + getViewId() + "_" + dataListId;
		StringBuffer buf = new StringBuffer();
		buf.append("pageUI.getViewPart('").append(this.getViewId()).append("').getComponent('").append(this.getId()).append("')");
		buf.append(".setComboData(").append(cbId).append(",").append(((CheckboxGroupComp)this.getWebElement()).getSepWidth()).append(");\n");
		addDynamicScript(buf.toString());
	}
	


}
