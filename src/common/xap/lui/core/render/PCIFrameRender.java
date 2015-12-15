package xap.lui.core.render;

import xap.lui.core.comps.IFrameComp;
import xap.lui.core.event.IEventSupport;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.layout.UIIFrame;
import xap.lui.core.model.LuiPageContext;

/**
 * @author renxh IFrame渲染器
 * @param <T>
 * @param <K>
 */
@SuppressWarnings("unchecked")
public class PCIFrameRender extends UINormalComponentRender<UIIFrame, IFrameComp> {

	public PCIFrameRender( IFrameComp webEle ) {
		super(webEle);
	}


	protected String getSourceType(IEventSupport ele) {
		return LuiPageContext.SOURCE_TYPE_IFRAME;
	}
	
	public String createBody() {
		//.iframe({"id":"maingrid4","src":"https://www.baidu.com","width":"600","height":"100%"});
		StringBuilder buf = new StringBuilder();
		IFrameComp comp = this.getWebElement();

		if (comp.getId() == null) {
			throw new LuiRuntimeException("id can not be null for iframe");
		}
//		String width = (comp.getWidth() == null || "".equals(comp.getWidth())) ? "100%" : comp.getWidth();
//		String height = (comp.getHeight() == null || "".equals(comp.getHeight())) ? "100%" : comp.getHeight();
		String border = (comp.getBorder() == null || "".equals(comp.getBorder())) ? "0" : comp.getBorder();
		String frameBorder = (comp.getFrameBorder() == null || "".equals(comp.getFrameBorder())) ? "0" : comp.getFrameBorder();
		String scrolling = (comp.getScrolling() == null || "".equals(comp.getScrolling())) ? "auto" : comp.getScrolling();

		String id = getVarId();

		buf.append("var ").append(id);
		buf.append(" = $('#"+getDivId()+"').iframe({");
		buf.append("id:\""+comp.getId()).append("\",");
		buf.append("name:\""+comp.getId()).append("\",");
		buf.append(comp.getSrc() == null ? "" : ("src:\""+comp.getSrc()+"\",")).append("width:\"100%\",height:\"100%\",");
		buf.append("border:\""+border).append("\",");
		buf.append("frameBorder:\""+frameBorder).append("\",");
		buf.append("scrolling:\""+scrolling).append("\"}).iframe(\"instance\");\n");

		if (null != this.getCurrWidget()) {
			buf.append("pageUI.getViewPart('" + this.getCurrWidget().getId() + "').addComponent(" + id + ");\n");
		}

		return buf.toString();
	}


	

}
