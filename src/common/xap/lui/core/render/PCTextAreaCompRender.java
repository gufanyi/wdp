package xap.lui.core.render;

import java.util.HashMap;
import java.util.Map;

import xap.lui.core.comps.LuiElement;
import xap.lui.core.comps.TextAreaComp;
import xap.lui.core.comps.WebComp;
import xap.lui.core.constant.EditorTypeConst;
import xap.lui.core.event.IEventSupport;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.layout.UIComponent;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.model.LuiPageContext;
import xap.lui.core.util.JsURLEncoder;

import com.alibaba.fastjson.JSON;

/**
 * @author renxh TextAreaComp 渲染器 对应 对应 html 的标签 <textarea/>
 */
public class PCTextAreaCompRender extends PCTextCompRender {
	public PCTextAreaCompRender(TextAreaComp webEle) {
		super(webEle);
	}

	public String createBody() {
		WebComp component = this.getWebElement();
		UIComponent uiComp = this.getUiElement();
		if (!(component instanceof TextAreaComp))
			throw new LuiRuntimeException(this.getId() + "应该为TextAreaComp类型");
		TextAreaComp textArea = (TextAreaComp) component;
		StringBuffer buf = new StringBuffer();
		// TextAreaComp(parent, name, left, top, rows, cols, position, readOnly,
		// value, width, height)
		String id = getVarId();
		buf.append("window.").append(id);
		if (textArea.isShowMark())
			buf.append(" = $(\"<div id='").append(this.getId()).append("'>\").appendTo($('#" + getDivId() + "')).textMark(\n");
		else
			buf.append(" = $(\"<div id='").append(this.getId()).append("'>\").appendTo($('#" + getDivId() + "')).textarea(\n");

		buf.append(generateParam(textArea, uiComp));

		buf.append(").");
		if (textArea.isShowMark())
			buf.append("textMark");
		else
			buf.append("textarea");
		buf.append("('instance');\n");

		String value = textArea.getValue();
		value = (value == null ? "" : value);
		value = JsURLEncoder.encode(value, "UTF-8");
		buf.append(id + ".setValue(decodeURIComponent('" + value + "'));\n");
		buf.append("pageUI.getViewPart('" + this.getCurrWidget().getId() + "').addComponent('" + this.getId() + "'," + id + ");\n");
		if (!textArea.isEnabled()) {
			buf.append(id).append(".setActive(false);\n");
		}
		return buf.toString();
	}

	/**
	 * 构造参数
	 * 
	 * @param radio
	 * @return
	 */
	private String generateParam(TextAreaComp textArea, UIComponent uiComp) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		String className = uiComp.getClassName();
		String width = "100%";
		try {
			width = String.valueOf(Integer.parseInt(uiComp.getWidth()) - 4);
		} catch (NumberFormatException e) {
		}
		String height = "100%";
		try {
			height = String.valueOf(Integer.parseInt(uiComp.getHeight()) - 4);
		} catch (NumberFormatException e) {
		}
		paramMap.put("rows", textArea.getRows());
		paramMap.put("cols", textArea.getCols());
		paramMap.put("position", "relative");
		paramMap.put("readOnly", textArea.isReadOnly());
		paramMap.put("width", width);
		paramMap.put("height", height);
		paramMap.put("tip", textArea.getTip());
		paramMap.put("className", className == null ? "text_div" : className);
		return JSON.toJSONString(paramMap);
	}

	protected String getSourceType(IEventSupport ele) {
		return LuiPageContext.SOURCE_TYPE_TEXTAREA;
	}

	public String getRenderType(LuiElement ele) {
		return EditorTypeConst.TEXTAREA;
	}

	public String getType() {
		return "textarea";
	}

	@LuiPhase(phase = { LifeCyclePhase.ajax })
	public void setValue(String value) {
		StringBuilder buf = new StringBuilder();
		buf.append("var text = pageUI.getViewPart('" + this.viewId + "').getComponent('" + this.getId() + "');\n");
		if (value == null) {
			buf.append("text.setValue('');\n");
		} else {
			value = value == null ? "" : value;
			value = JsURLEncoder.encode(value, "UTF-8");
			buf.append("text.setValue(decodeURIComponent('" + value + "'));\n");
		}
		addDynamicScript(buf.toString());
	}

}
