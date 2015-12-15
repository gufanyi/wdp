package xap.lui.core.render;

import java.util.HashMap;
import java.util.Map;

import xap.lui.core.comps.LinkComp;
import xap.lui.core.event.IEventSupport;
import xap.lui.core.layout.UIComponent;
import xap.lui.core.layout.UILinkComp;
import xap.lui.core.model.LuiPageContext;

import com.alibaba.fastjson.JSON;

/**
 * @author renxh 链接渲染器
 * @param <T>
 * @param <K>
 */
@SuppressWarnings("unchecked")
public class PCLinkCompRender extends UINormalComponentRender<UILinkComp, LinkComp> {

	public PCLinkCompRender(LinkComp webEle) {
		super(webEle);

	}

	@Override
	public String createBody() {

		StringBuilder buf = new StringBuilder();
		UILinkComp uicomp = this.getUiElement();
		LinkComp link = this.getWebElement();
		String linkId = getVarId();
		buf.append("window.").append(linkId).append(" = $(\"<div id='" + link.getId() + "'></div>\")");
		buf.append(".appendTo($('#" + getDivId() + "'))");
		buf.append(".link(\n");
		buf.append(generateParam(link, uicomp));
		buf.append("\n).link('instance');\n");

		if (!link.isEnabled()) {
			buf.append(linkId + ".setActive(false);\n");
		}

		if (!link.isVisible()) {
			buf.append(linkId + ".setVisible(false);\n");
		}
		buf.append("pageUI.getViewPart('" + this.getCurrWidget().getId() + "').addComponent('" + link.getId() + "'," + linkId + ");\n");

		return buf.toString();
	}

	/**
	 * 构造参数
	 * 
	 * @param radio
	 * @return
	 */
	private String generateParam(LinkComp link, UIComponent uiComp) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		if (link.getHref() != null) {
			paramMap.put("href", link.getHref());
		}
		paramMap.put("text", translate(link.getI18nName(), link.getText(), link.getLangDir()));
		paramMap.put("hasImg", link.isHasImg());
		paramMap.put("srcImg", link.getRealImage());
		paramMap.put("target", link.getTarget());
		paramMap.put("position", "relative");
		if (uiComp.getClassName() != null) {
			paramMap.put("className", uiComp.getClassName());
		}
		return JSON.toJSONString(paramMap);
	}

	protected String getSourceType(IEventSupport ele) {
		return LuiPageContext.SOURCE_TYPE_LINKCOMP;
	}

}
