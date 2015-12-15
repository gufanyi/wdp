package xap.lui.core.plugins;

import xap.lui.core.comps.WebComp;
import xap.lui.core.context.BaseContext;
import xap.lui.core.layout.UIComponent;
import xap.lui.core.render.UINormalComponentRender;

public interface ILuiPaltformExtProvier {

	Class<? extends WebComp> getWebCompClazz();
	Class<? extends UIComponent> getUICompClazz();
	Class<? extends UINormalComponentRender> getRenderClazz();
	Class<? extends BaseContext> getContextClazz();
	String getCompTypeName();
	String getLayoutTagName();
	String[] getResoucesName();
	String getText();
	String getImgIcon();
	String getSourceType();
	
}
