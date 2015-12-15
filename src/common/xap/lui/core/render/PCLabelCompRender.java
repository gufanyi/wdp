package xap.lui.core.render;

import java.util.HashMap;
import java.util.Map;

import xap.lui.core.comps.LabelComp;
import xap.lui.core.event.IEventSupport;
import xap.lui.core.layout.UILabelComp;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.model.LuiPageContext;

import com.alibaba.fastjson.JSON;

/**
 * @author renxh 标签渲染器
 * @param <T>
 * @param <K>
 */
@SuppressWarnings("unchecked")
public class PCLabelCompRender extends UINormalComponentRender<UILabelComp, LabelComp> {

	public PCLabelCompRender(LabelComp webEle) {
		super(webEle);
	}


	@Override
	public String createBody() {
		StringBuilder buf = new StringBuilder();
		LabelComp label = this.getWebElement();
		UILabelComp uiComp = this.getUiElement();
		String labelId = getVarId();
		buf.append("var ").append(labelId).append(" = $(\"<div id='" + label.getId() + "'></div>\")");
		buf.append(".appendTo($('#" + getDivId() + "'))");
		buf.append(".label(\n");
		buf.append(generateParam(label, uiComp));
		buf.append("\n).label('instance');\n");
		if (label.getColor() != null) {
			buf.append(labelId + ".setColor('" + label.getColor() + "');\n");
		}
		if (uiComp.getMaxWidth() != null) {
			buf.append(labelId + ".setMaxWidth('" + uiComp.getMaxWidth() + "');\n");
		}
		if (label.getInnerHTML() != null) {
			buf.append(labelId + ".setInnerHTML('" + label.getInnerHTML() + "');\n");
		}
		if (uiComp.getSize() != null) {
			buf.append(labelId + ".setSize('" + uiComp.getSize() + "');\n");
		}
		if (uiComp.getStyle() != null) {
			buf.append(labelId + ".setStyle('" + uiComp.getStyle() + "');\n");
		}
		if (uiComp.getFamily() != null) {
			buf.append(labelId + ".setFamily('" + uiComp.getFamily() + "');\n");
		}

		if (label.getDecoration() != null) {
			buf.append(labelId + ".setDecoration('" + label.getDecoration() + "');\n");
		}
		if (uiComp.getHeight() != null) {
			buf.append(labelId + ".setHeight('" + uiComp.getHeight() + "');\n");
		}
		if (!label.isVisible())
			buf.append(labelId + ".hide();\n");
		else
			buf.append(labelId + ".show();\n");
		buf.append(labelId).append(".ctxChanged = false;\n");
		buf.append("pageUI.getViewPart('" + this.getCurrWidget().getId() + "').addComponent('" + label.getId() + "'," + labelId + ");\n");
		return buf.toString();
	}

	protected String getSourceType(IEventSupport ele) {
		return LuiPageContext.SOURCE_TYPE_LABEL;
	}

	/**
	 * 构造参数
	 * 
	 * @param radio
	 * @return
	 */
	private String generateParam(LabelComp label, UILabelComp uiComp) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("text", translate(label.getI18nName(), label.getText(), label.getLangDir()));
		paramMap.put("position", "relative");
		if (uiComp.getClassName() != null) {
			paramMap.put("className", uiComp.getClassName());
		}
		paramMap.put("textAlign", uiComp.getTextAlign());
		return JSON.toJSONString(paramMap);
	}
	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void setSize(int size) {
		StringBuffer buf = new StringBuffer();
		buf.append("var text = pageUI.getViewPart('" + this.viewId + "').getComponent('" + this.getId() + "');\n");
		buf.append("text.setSize('" + size + "');\n");
		addDynamicScript(buf.toString());
	}
	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void setStyle(String style) {
		StringBuffer buf = new StringBuffer();
		buf.append("var text = pageUI.getViewPart('" + this.viewId + "').getComponent('" + this.getId() + "');\n");
		buf.append("text.setStyle('" + style + "');\n");
		addDynamicScript(buf.toString());
	}
	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void setFamily(String family) {
		StringBuffer buf = new StringBuffer();
		buf.append("var text = pageUI.getViewPart('" + this.viewId + "').getComponent('" + this.getId() + "');\n");
		buf.append("text.setFamily('" + family + "');\n");
		addDynamicScript(buf.toString());
	}
	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void setdDecoration(String decoration) {
		StringBuffer buf = new StringBuffer();
		buf.append("var text = pageUI.getViewPart('" + this.viewId + "').getComponent('" + this.getId() + "');\n");
		buf.append("text.setFamily('" + decoration + "');\n");
		addDynamicScript(buf.toString());
	}
	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void setTextAlign(String textAlign) {
		StringBuffer buf = new StringBuffer();
		buf.append("var text = pageUI.getViewPart('" + this.viewId + "').getComponent('" + this.getId() + "');\n");
		buf.append("text.setTextAlign('" + textAlign + "');\n");
		addDynamicScript(buf.toString());
	}

}
