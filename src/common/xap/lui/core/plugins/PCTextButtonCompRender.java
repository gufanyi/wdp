package xap.lui.core.plugins;

import java.util.HashMap;
import java.util.Map;

import xap.lui.core.event.IEventSupport;
import xap.lui.core.layout.UIComponent;
import xap.lui.core.model.LuiPageContext;
import xap.lui.core.render.UINormalComponentRender;

import com.alibaba.fastjson.JSON;

@SuppressWarnings("unchecked")
public class PCTextButtonCompRender extends UINormalComponentRender<UITextButton, TextButtonComp> {

	public PCTextButtonCompRender(TextButtonComp webEle) {
		super(webEle);
	}

	@Override
	public String createBody() {
		TextButtonComp tbutton = (TextButtonComp) this.getWebElement();
		StringBuilder buf = new StringBuilder();
		String tbuttonId = getVarId();

		buf.append("var ");
		buf.append(tbuttonId);
		buf.append(" = $(\"<div id='").append(tbutton.getId()).append("'>\")");
		buf.append(".appendTo($('#").append(getDivId()).append("'))");
		buf.append(".textbutton(\n");// textbutton为控件名

		buf.append(generateTextBtnParam(tbutton));
		buf.append("\n).textbutton('instance');\n");
		buf.append("pageUI.getViewPart('" + getViewId() + "').addComponent('" + tbutton.getId() + "'," + tbuttonId + ");\n");

		return buf.toString();
	}

	/**
	 * 构造文本按钮参数
	 * 
	 * @param textbutton
	 * @return
	 */
	private String generateTextBtnParam(TextButtonComp textbutton) {
		UIComponent uiComp = this.getUiElement();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		if (uiComp != null)
			paramMap.put("width", uiComp.getWidth());
		else if (textbutton.getWidth() != null)
			paramMap.put("width", textbutton.getWidth());
		else
			paramMap.put("width", 60);

		if (uiComp != null)
			paramMap.put("height", uiComp.getHeight());
		else
			paramMap.put("height", 24);
		paramMap.put("value", textbutton.getValue());

		return JSON.toJSONString(paramMap);
	}

	@Override
	protected String getSourceType(IEventSupport ele) {
		return LuiPageContext.SOURCE_TYPE_TEXTBUTTON;
	}
}
