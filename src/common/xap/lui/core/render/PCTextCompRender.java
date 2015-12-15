package xap.lui.core.render;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import xap.lui.core.comps.ComboBoxComp;
import xap.lui.core.comps.DateTextComp;
import xap.lui.core.comps.DecimalTextComp;
import xap.lui.core.comps.IntegerTextComp;
import xap.lui.core.comps.RefHelper;
import xap.lui.core.comps.ReferenceComp;
import xap.lui.core.comps.TextComp;
import xap.lui.core.comps.WebComp;
import xap.lui.core.constant.EditorTypeConst;
import xap.lui.core.event.IEventSupport;
import xap.lui.core.layout.UITextField;
import xap.lui.core.listener.LuiEventConf;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.refrence.BaseRefNode;
import xap.lui.core.refrence.GenericRefNode;
import xap.lui.core.refrence.IRefConst;
import xap.lui.core.refrence.IRefModel;
import xap.lui.core.refrence.RefSelfUtil;

import com.alibaba.fastjson.JSON;
/**
 * @author renxh UITextField 渲染器 数字类型 密码类型 日期类型 等等
 */
@SuppressWarnings("unchecked")
public class PCTextCompRender extends UINormalComponentRender<UITextField, TextComp> {
	public PCTextCompRender(TextComp webEle) {
		super( webEle);
	}
	

	public String createBody() {
		TextComp text = this.getWebElement();

		StringBuffer textBuf = new StringBuffer();
		String type = text.getEditorType();
		String id = this.getVarId();
		
		textBuf.append("var ").append(id);
		textBuf.append(" = $(\"<div id='").append(this.getId()).append("'>\").");
		textBuf.append("appendTo($('#").append(getDivId()).append("')).");
		if (text.isShowMark())
			textBuf.append("textMark(");
		else
			textBuf.append(text.getWidgetName()).append("(");
		
		
		textBuf.append("$.extend({},").append(generateParam(text,type));
		
		if (text instanceof ReferenceComp) {
			String refcode = ((ReferenceComp) text).getRefcode();
			if (refcode != null) { //&& !LuiRuntimeEnvironment.isEditMode()
				String refId = RF_PRE + getCurrWidget().getId() + "_" + refcode;
				BaseRefNode refNode = (BaseRefNode) getCurrWidget().getViewModels().getRefNode(refcode);
				if(refNode != null)
					textBuf.append(",{nodeInfo : ").append(refId).append("}");
			} 
		}
		textBuf.append(")");
		
		textBuf.append("\n).");
		if (text.isShowMark())
			textBuf.append("textMark(");
		else 
			textBuf.append(text.getWidgetName()).append("(");
		textBuf.append("'instance');\n");
		textBuf.append("var widget = pageUI.getViewPart('" + this.getCurrWidget().getId() + "');\n");
		textBuf.append("widget.addComponent('" + this.getId() + "'," + id + ");\n");
		
		//TODO:widget需要处理
		textBuf.append(id + ".viewpart = widget;\n");
		if (!text.isShowMark()) {
			String value = text.getValue();
			if (type.equals(EditorTypeConst.REFERENCE)) {
				ReferenceComp reference = (ReferenceComp) text;
				//String value = reference.getValue();
				if (value != null && !value.equals("")) {
					// 确保显示值
					RefHelper.fetchRefShowValue(getCurrWidget(), reference);
					textBuf.append(id).append(".setValue('").append(text.getValue()).append("');\n");
					if(reference.getShowValue() != null && !reference.getShowValue().equals(""))
						textBuf.append(id).append(".setShowValue('").append(reference.getShowValue()).append("');\n");
				}
			}

			else if (type.equals(EditorTypeConst.DATETIMETEXT)) {
				textBuf.append(id).append(".setShowTimeBar(true);");
				textBuf.append(id).append(".setValue('").append(text.getValue()).append("');\n");

			}else{
				if (value != null && !value.equals("")) {
					textBuf.append(id).append(".setValue('").append(text.getValue()).append("');\n");
				}
			}
			
			if(hasFormula(text,"validate_method") || hasFormula(text,"editor_method")) {
				textBuf.append(id).append(".setFormular(true);\n");
			}
			
			if (text.isReadOnly() == true)
				textBuf.append(id).append(".setReadOnly(true);\n");

			if (text.isEnabled() == false)
				textBuf.append(id).append(".setActive(false);\n");
			if ("Y".equals(getUiElement().getValgin())){
				textBuf.append(id).append(".setValgin(true);\n");
			}
			if (text.isVisible() == false)
				textBuf.append(id).append(".hideV();\n");
		}
		return textBuf.toString();
	}
	
	protected boolean hasFormula(WebComp comp, String method) {
		List<LuiEventConf> eventList = comp.getEventConfList();
		if(CollectionUtils.isNotEmpty(eventList)) {
			for(LuiEventConf event : eventList) {
				if(StringUtils.equals(event.getMethod(), method)) {
					return true;
				}
			}
		}
		return false;
	}
	
	protected String getSourceType(IEventSupport ele) {
		TextComp t = getWebElement();
		return t.getEditorType();
	}
	
	/**
	 * 构造参数
	 * @param radio
	 * @return
	 */
	private String generateParam(TextComp text ,String type) {
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("width", "100%");
		paramMap.put("position", "relative");
		//if (type.equals(EditorTypeConst.INTEGERTEXT)) {
		if (text instanceof IntegerTextComp) {
			String maxValue = text.getMaxValue();
			String minValue = text.getMinValue();
			if (maxValue != null)
				paramMap.put("maxValue", maxValue);
			if (minValue != null)
				paramMap.put("minValue", minValue);
//		} else if (type.equals(EditorTypeConst.DECIMALTEXT)) {
		} else if (text instanceof DecimalTextComp) {
			String pre = text.getPrecision();
			if (pre != null)
				paramMap.put("precision", pre);
			
			String maxValue = text.getMaxValue();
			String minValue = text.getMinValue();
			if (maxValue != null)
				paramMap.put("maxValue", maxValue);
			if (minValue != null)
				paramMap.put("minValue", minValue);
		} else if(text instanceof DateTextComp) {
			paramMap.put("multiple", ((DateTextComp) text).getMultiple());
			paramMap.put("multiSplitChar", ((DateTextComp) text).getMultiSplitChar());
		} else if (type.equals(EditorTypeConst.COMBODATA)) {
			paramMap.put("selectOnly", ((ComboBoxComp) text).isOnlySelect());
		}

		String i18nTip = translate(text.getTip(), text.getTip(), text.getLangDir());
		if (!StringUtils.isEmpty(i18nTip)) {
			paramMap.put("tip", i18nTip);
		}
		if(text.getSizeLimit()!=null){
			paramMap.put("maxSize", text.getSizeLimit());
		}
		if (!StringUtils.isEmpty(text.getValue())) {
			paramMap.put("value", text.getValue());
		}
		
		String i18nText = translate(text.getI18nName(), text.getText(), text.getLangDir());
		if (!StringUtils.isEmpty(i18nText)) { // 有标签属性
			paramMap.put("labelText", i18nText);
			paramMap.put("labelAlign", text.getAlign());
			paramMap.put("labelWidth", text.getTextWidth());
		}
		
		if (text instanceof ReferenceComp) {
			String refcode = ((ReferenceComp) text).getRefcode();
			if (refcode != null) {
				BaseRefNode refNode = (BaseRefNode) getCurrWidget().getViewModels().getRefNode(refcode);
				if (refNode != null) {
					if (refNode instanceof GenericRefNode) {
						GenericRefNode ncRefNode = (GenericRefNode) refNode;
						IRefModel refModel = RefSelfUtil.getRefModel(ncRefNode);
						//refModel.getRefData();
						int refType = RefSelfUtil.getRefType(refModel);
						String reftype = "0";
						if (refType == IRefConst.GRID)
							reftype = "2";
						else if (refType == IRefConst.TREE)
							reftype = "1";
						else if (refType == IRefConst.GRIDTREE)
							reftype = "3";
						paramMap.put("refType", reftype);
					}
					String refHeight = refNode.getHeight();
					if (!StringUtils.isEmpty(refHeight)){
						paramMap.put("refHeight", refHeight);
					}
					String refWidth = refNode.getWidth();
					if (!StringUtils.isEmpty(refWidth)){
						paramMap.put("refWidth", refWidth);
					}
				}
			}
		}
		return JSON.toJSONString(paramMap);
	}
	public String getType() {
		TextComp t = getWebElement();
		if (t.getEditorType().equals(EditorTypeConst.REFERENCE)) {
			return "reftext";
		} else if (t.getEditorType().equals(EditorTypeConst.COMBODATA)) {
			return "combotext";
		} else if (t.getEditorType().equals(EditorTypeConst.STRINGTEXT)) {
			return "stringtext";
		} else if (t.getEditorType().equals(EditorTypeConst.CHECKBOX)) {
			return "checkboxtext";
		} else if (t.getEditorType().equals(EditorTypeConst.CHECKBOXGROUP)) {
			return "checkboxgrouptext";
		} else if (t.getEditorType().equals(EditorTypeConst.INTEGERTEXT)) {
			return "integertext";
		} else if (t.getEditorType().equals(EditorTypeConst.DATETEXT)) {
			return "datetext";
		} else if (t.getEditorType().equals(EditorTypeConst.DATETIMETEXT)) {
			return "datetext";
		} else if (t.getEditorType().equals(EditorTypeConst.DECIMALTEXT)) {
			return "floattext";
		} else if (t.getEditorType().equals(EditorTypeConst.FILECOMP)) {
			return "filetext";
		}
		return "";
	}
	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void setMatchValues(String matchValues) {
		StringBuffer buf = new StringBuffer();
		buf.append("var text = pageUI.getViewPart('" + this.viewId + "').getComponent('" + this.getId() + "');\n");
		buf.append("text.setMatchValues('").append(matchValues + "');\n");
		addDynamicScript(buf.toString());
	}
	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void beforeOpenParam(String beforeOpenParam) {
		StringBuffer buf = new StringBuffer();
		buf.append("var text = pageUI.getViewPart('" + this.viewId + "').getComponent('" + this.getId() + "');\n");
		buf.append("text.beforeOpenParam('").append(beforeOpenParam + "');\n");
		addDynamicScript(buf.toString());
	}
	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void setShowValue(String showValue) {
		StringBuffer buf = new StringBuffer();
		buf.append("var text = pageUI.getViewPart('" + this.viewId + "').getComponent('" + this.getId() + "');\n");
		if (showValue == null)
			buf.append("text.setShowValue(null);\n");
		else
			buf.append("text.setShowValue('").append(showValue).append("');\n");
		addDynamicScript(buf.toString());
	}
	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void setReadOnly(boolean readOnly) {
		StringBuffer buf = new StringBuffer();
		buf.append("var text = pageUI.getViewPart('" + this.viewId + "').getComponent('" + this.getId() + "');\n");
		buf.append("if(text.setReadOnly) text.setReadOnly(" + readOnly + ");\n");
		addDynamicScript(buf.toString());
	}
	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void setFocus() {
		StringBuffer buf = new StringBuffer();
		buf.append("var text = pageUI.getViewPart('" + this.viewId + "').getComponent('" + this.getId() + "');\n");
		buf.append("if(text.setFocus) text.setFocus();\n");
		addDynamicScript(buf.toString());
	}
	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void setMaxValue(String maxValue) {
		StringBuffer buf = new StringBuffer();
		buf.append("var text = pageUI.getViewPart('" + this.viewId + "').getComponent('" + this.getId() + "');\n");
		buf.append("if(text.setIntegerMaxValue) text.setIntegerMaxValue(" + maxValue + ");\n");
		addDynamicScript(buf.toString());
	}
	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void setMinValue(String minValue) {
		StringBuffer buf = new StringBuffer();
		buf.append("var text = pageUI.getViewPart('" + this.viewId + "').getComponent('" + this.getId() + "');\n");
		buf.append("if(text.setIntegerMinValue) text.setIntegerMinValue(" + minValue + ");\n");
		addDynamicScript(buf.toString());
	}
	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void setPrecision(String precision) {
		StringBuffer buf = new StringBuffer();
		buf.append("var text = pageUI.getViewPart('" + this.viewId + "').getComponent('" + this.getId() + "');\n");
		buf.append("if(text.setPrecision) text.setPrecision(" + precision + ");\n");
		addDynamicScript(buf.toString());
	}
	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void isShowLabel(boolean isShowLabel) {
		StringBuffer buf = new StringBuffer();
		buf.append("var text = pageUI.getViewPart('" + this.viewId + "').getComponent('" + this.getId() + "');\n");
		buf.append("if(text.isShowLabel) text.isShowLabel(" + isShowLabel + ");\n");
		addDynamicScript(buf.toString());
	}
	@Override
	public void setWidth(String width){
		super.setWidth(width);
		StringBuffer buf = new StringBuffer();
		buf.append("var text = pageUI.getViewPart('" + this.viewId + "').getComponent('" + this.getId() + "');\n");
		buf.append("if(text.setWidth) text.setWidth(" + width + ");\n");
		addDynamicScript(buf.toString());
	}
}
