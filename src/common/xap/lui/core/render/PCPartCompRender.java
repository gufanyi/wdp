package xap.lui.core.render;
import org.apache.commons.lang.StringUtils;
import xap.lui.core.common.LuiRenderContext;
import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.comps.IWebPartContentFetcher;
import xap.lui.core.comps.WebPartComp;
import xap.lui.core.control.ModePhase;
import xap.lui.core.event.IEventSupport;
import xap.lui.core.layout.UIComponent;
import xap.lui.core.layout.UIPartComp;
import xap.lui.core.layout.UIPartMeta;
import xap.lui.core.model.LuiPageContext;
import xap.lui.core.model.PagePartMeta;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.core.util.ClassUtil;
import xap.lui.core.util.JsURLEncoder;
@SuppressWarnings("unchecked")
public class PCPartCompRender extends UINormalComponentRender<UIPartComp, WebPartComp> {
	public PCPartCompRender(WebPartComp webEle) {
		super(webEle);
	}
	@Override
	public String place() {
		WebPartComp comp = (WebPartComp) this.getWebElement();
		String content = fetcherContent(comp);
		comp.setInnerHTML(content);
		comp.setCtxChanged(false);
		StringBuilder buf = new StringBuilder();
		buf.append("var ").append(getDivId()).append(" = $('<div>').attr('id','").append(getDivId()).append("').css({\n");
		buf.append("'height':'100%',\n");
		buf.append("'width':'100%',\n");
		buf.append("'overflow':'hidden'})");
		if (StringUtils.isNotBlank(comp.getInnerHTML())) {
			buf.append(".html(").append(comp.getInnerHTML()).append(");\n");
		} else {
			buf.append(";\n");
		}
		return buf.toString();
	}
	@Override
	public String createBody() {
		String parentDivId = this.getDivId();
		StringBuilder buf = new StringBuilder();
		UIComponent uiComp = this.getUiElement();
		String showId = this.getVarId();
		buf.append("var ").append(showId).append(" = $('<div id = \"").append(id).append("\">').appendTo($('#" + parentDivId + "')).htmlcontent({className:'");
		buf.append(uiComp.getClassName() + "'}).htmlcontent('instance');\n");
		buf.append("pageUI.getViewPart('" + getViewId() + "').addComponent('" + id + "'," + showId + ");\n");
		String fetchscript = this.fetcherBodyScript(this.getWebElement());
		if (fetchscript != null)
			buf.append(fetchscript);
		WebPartComp comp = (WebPartComp) this.getWebElement();
		buf.append(showId + ".setContent('" + JsURLEncoder.encode(comp.getInnerHTML(), "UTF-8") + "');\n");
		return buf.toString();
	}
	private String fetcherContent(WebPartComp webEle) {
		IWebPartContentFetcher fetcher = (IWebPartContentFetcher) ClassUtil.newInstance(webEle.getContentFetcher());
		PagePartMeta pm = LuiRenderContext.current().getPagePartMeta();
		ViewPartMeta widget = pm.getWidget(this.viewId);
		UIPartMeta um = LuiRenderContext.current().getUiPartMeta();
		String content = fetcher.fetchHtml(um, pm, widget);
		return content;
	}
	private String fetcherBodyScript(WebPartComp webEle) {
		IWebPartContentFetcher fetcher = (IWebPartContentFetcher) ClassUtil.newInstance(webEle.getContentFetcher());
		PagePartMeta pm = LuiRenderContext.current().getPagePartMeta();
		ViewPartMeta widget = pm.getWidget(this.viewId);
		UIPartMeta um = LuiRenderContext.current().getUiPartMeta();
		if (!LuiRuntimeContext.getModePhase().equals(ModePhase.normal)) {
			return "";
		}
		String content = fetcher.fetchBodyScript(um, pm, widget);
		return content;
	}
	@Override
	protected String getSourceType(IEventSupport ele) {
		return LuiPageContext.SOURCE_TYPE_HTMLCONTENT;
	}
}
