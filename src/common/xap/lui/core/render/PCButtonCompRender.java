package xap.lui.core.render;

import java.util.HashMap;
import java.util.Map;

import xap.lui.core.comps.ButtonComp;
import xap.lui.core.event.IEventSupport;
import xap.lui.core.layout.UIButton;
import xap.lui.core.layout.UIComponent;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.model.LuiPageContext;

import com.alibaba.fastjson.JSON;

/**
 * @author renxh button控件渲染器
 * @param <T>
 * @param <K>
 */
@SuppressWarnings("unchecked")
public class PCButtonCompRender extends UINormalComponentRender<UIButton, ButtonComp> {

	public PCButtonCompRender(ButtonComp webEle) {
		super(webEle);
	}

	/*
	 * (non-Javadoc)
	 */
	public String createBody() {
		ButtonComp button = (ButtonComp) this.getWebElement();
		StringBuilder buf = new StringBuilder();

		String buttonId = getVarId();

		buf.append("var ");
		buf.append(buttonId);
		buf.append(" = $(\"<div id='").append(button.getId()).append("'>\")");
		buf.append(".appendTo($('#").append(getDivId()).append("'))");
		buf.append(".button(\n");

		buf.append(generateBtnParam(button));

		buf.append("\n).button('instance');\n");

		String hotKey = button.getHotKey();
		if (hotKey != null && !"".equals(hotKey)) {
			String modifier = String.valueOf(button.getModifiers());
			buf.append(buttonId).append(".setHotKey(\"").append(hotKey + modifier).append("\");\n");
		}

		buf.append("pageUI.getViewPart('" + getViewId() + "').addComponent('" + button.getId() + "'," + buttonId + ");\n");

		if (button.isVisible() == false)
			buf.append(buttonId + ".hide();\n");
		return buf.toString();
	}

	/**
	 * 构造按钮参数
	 * 
	 * @param button
	 * @return
	 */
	private String generateBtnParam(ButtonComp button) {
		UIComponent uiComp = this.getUiElement();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		if (uiComp != null)
			paramMap.put("width", uiComp.getWidth());
		else if (button.getWidth() != null)
			paramMap.put("width", button.getWidth());
		else
			paramMap.put("width", 60);

		if (uiComp != null)
			paramMap.put("height", uiComp.getHeight());
		else
			paramMap.put("height", 24);
		paramMap.put("text", translate(button.getI18nName(), button.getText(), button.getLangDir()));

		String tip = button.getTip() == null ? "" : translate(button.getTip(), button.getTip(), button.getLangDir());
		String displayHotKey = button.getDisplayHotKey();
		if (displayHotKey != null && !"".equals(displayHotKey)) {
			tip += "(" + displayHotKey + ")";
		}
		paramMap.put("tip", tip);
		if (!(button.getRefImg() == null || button.getRefImg().equals("")))
			paramMap.put("refImg", button.getRealRefImg());
		paramMap.put("position", "relative");
		paramMap.put("align", "left");
		paramMap.put("disabled", !button.isEnabled());

		if (uiComp != null && uiComp.getClassName() != null) {
			paramMap.put("className", uiComp.getClassName());
		}

		return JSON.toJSONString(paramMap);
	}

	protected String getSourceType(IEventSupport ele) {
		return LuiPageContext.SOURCE_TYPE_BUTTON;
	}

	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void setText(String text) {
		StringBuilder buf = new StringBuilder();
		buf.append("var comp = pageUI.getViewPart('" + this.viewId + "').getComponent('" + this.getId() + "');\n");
		buf.append("if(comp.changeText) comp.changeText('" + text + "');\n");
		addDynamicScript(buf.toString());
	}
	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void setWidth(int width) {
		StringBuilder buf = new StringBuilder();
		buf.append("var comp = pageUI.getViewPart('" + this.viewId + "').getComponent('" + this.getId() + "');\n");
		buf.append("if(comp.changeWidth) comp.changeWidth('" + width + "');\n");
		addDynamicScript(buf.toString());
	}
	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void setHeight(int height) {
		StringBuilder buf = new StringBuilder();
		buf.append("var comp = pageUI.getViewPart('" + this.viewId + "').getComponent('" + this.getId() + "');\n");
		buf.append("if(comp.changeHeight) comp.changeHeight('" + height + "');\n");
		addDynamicScript(buf.toString());
	}



}
