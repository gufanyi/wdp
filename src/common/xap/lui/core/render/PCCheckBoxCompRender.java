package xap.lui.core.render;

import java.util.HashMap;
import java.util.Map;

import xap.lui.core.comps.CheckBoxComp;
import xap.lui.core.comps.LuiElement;
import xap.lui.core.comps.WebComp;
import xap.lui.core.constant.EditorTypeConst;
import xap.lui.core.constant.StringDataTypeConst;
import xap.lui.core.event.IEventSupport;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.layout.UIComponent;
import xap.lui.core.layout.UITextField;
import xap.lui.core.model.LuiPageContext;

import com.alibaba.fastjson.JSON;

/**
 * @author renxh checkbox控件渲染器
 * @param <T>
 * @param <K>
 */
public class PCCheckBoxCompRender extends PCTextCompRender {

	public PCCheckBoxCompRender(CheckBoxComp webEle) {
		super(webEle);
	}

	@Override
	public String createBody() {

		WebComp component = this.getWebElement();
		UIComponent uiComp = this.getUiElement();
		if (!(component instanceof CheckBoxComp))
			throw new LuiRuntimeException(this.getId() + "不是CheckBoxComp类型！");

		CheckBoxComp cb = (CheckBoxComp) component;
		StringBuilder buf = new StringBuilder();
		String checkboxId = getVarId();

		buf.append("window.").append(checkboxId).append(" = $(\"<div id='").append(this.getId()).append("'>\")");
		buf.append(".appendTo($('#" + getDivId() + "'))");
		buf.append(".checkbox(\n");
		buf.append(generateParam(cb, uiComp));
		buf.append("\n).checkbox('instance');\n");

		String dataType = cb.getDataType();
		buf.append(checkboxId).append(".setValuePair(");
		if (dataType.equals(StringDataTypeConst.BOOLEAN) || dataType.equals(StringDataTypeConst.bOOLEAN))
			buf.append("[\"true\",\"false\"]");
		else if (dataType.equals(StringDataTypeConst.FBOOLEAN))
			buf.append("['Y','N']");

		buf.append(");\n");
		if (cb.isEnabled() == false) {
			buf.append(checkboxId).append(".setActive('false');\n");
		}
		if (!cb.isVisible())
			buf.append(checkboxId).append(".setVisible(false);\n");

		buf.append("pageUI.getViewPart('" + this.getCurrWidget().getId() + "').addComponent('" + this.getId() + "'," + checkboxId + ");\n");

		return buf.toString();

	}

	protected String getSourceType(IEventSupport ele) {
		return LuiPageContext.SOURCE_TYPE_TEXT;
	}

	@Override
	public String getRenderType(LuiElement ele) {
		return EditorTypeConst.CHECKBOX;
	}



	/**
	 * 构造参数
	 * 
	 * @param radio
	 * @return
	 */
	private String generateParam(CheckBoxComp cb, UIComponent uiComp) {

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("text", translate(cb.getI18nName(), cb.getText(), cb.getLangDir()));
		paramMap.put("checked", cb.isChecked());
		paramMap.put("position", "relative");
		paramMap.put("width", uiComp.getWidth());
		if (uiComp.getClassName() != null) {
			paramMap.put("className", uiComp.getClassName());
		}
		if (uiComp.getAttribute(UITextField.IMG_SRC) != null) {
			paramMap.put("imgsrc", uiComp.getAttribute(UITextField.IMG_SRC));
		}
		return JSON.toJSONString(paramMap);
	}

}
