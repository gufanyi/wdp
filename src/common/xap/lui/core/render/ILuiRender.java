package xap.lui.core.render;

import xap.lui.core.comps.LuiElement;
import xap.lui.core.layout.UIElement;

/**
 * @author renxh 渲染接口
 *
 */
public interface ILuiRender {

	public String create();

	public void destroy();

	public String place();

//	public String getType();

	public String getDivId();

	public String getNewDivId();

	public void setDivId(String divId);

	public void removeChild(UIElement obj);

	public void addChild(UIElement obj);

	public ILuiRender getParentRender();

	public String getId();

	public <T extends UIElement> T getUiElement();

	public <K extends LuiElement> K getWebElement();

}
