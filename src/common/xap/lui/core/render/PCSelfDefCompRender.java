package xap.lui.core.render;

import xap.lui.core.comps.SelfDefComp;
import xap.lui.core.event.IEventSupport;
import xap.lui.core.layout.UIComponent;
import xap.lui.core.layout.UISelfDefComp;
import xap.lui.core.model.LuiPageContext;

/**
 * @author renxh
 * 自定义控件渲染器
 */
@SuppressWarnings("unchecked")
public class PCSelfDefCompRender extends UINormalComponentRender<UISelfDefComp, SelfDefComp> {

	public PCSelfDefCompRender(SelfDefComp webEle) {
		super( webEle);
	}
	
	@Override
	public String createBody() {

		StringBuilder buf = new StringBuilder();
		SelfDefComp sdComp = this.getWebElement();
		UIComponent uiComp = this.getUiElement();
		
		String id = getVarId();
		buf.append("var ").append(id).append(" = new SelfDefComp(document.getElementById('");
		buf.append(getDivId()).append("'),'").append(sdComp.getId());
		buf.append("','0','0','100%','100%','relative',");
		buf.append(sdComp.isVisible()).append(",'");
		
		String className = null;
		if(uiComp != null){
			className = uiComp.getClassName();
		}
		buf.append(className == null ? "" : className).append("');\n");

		buf.append("pageUI.getViewPart('" + this.getCurrWidget().getId() + "').addComponent(" + id + ");\n");

		if (sdComp.isVisible() == false)
			buf.append(id + ".setVisible(false);\n");
		
		return buf.toString();
	}
	

	protected String getSourceType(IEventSupport ele) {

		return LuiPageContext.SOURCE_TYPE_SELF_DEF_COMP;
	}
	
	public String getType(){
		return "selfdefcomp";
	}
}
