package xap.lui.core.render;

import xap.lui.core.comps.ContextMenuComp;
import xap.lui.core.comps.LuiElement;
import xap.lui.core.layout.UIElement;
import xap.lui.core.layout.UIPartMeta;
import xap.lui.core.model.PagePartMeta;

/**
 * @author renxh
 * 渲染组接口，可以获得一组与设备相关的渲染器
 *
 */
public interface IUIRenderGroup {
	
	public ILuiRender getUIRender(UIElement uiEle);
	
	public ILuiRender getUIRender(UIElement uiEle, ILuiRender parentRender);
	
	/**
	 * 2011-7-19 下午01:20:05 renxh des：根据类型获得相应的uirender
	 * 
	 * @param uiEle
	 * @param pageMeta
	 * @return
	 */
	public ILuiRender getUIRender(UIPartMeta uimeta, UIElement uiEle, PagePartMeta pageMeta, ILuiRender parentRender);
	
	/**
	 * 2011-8-2 下午07:15:42 renxh
	 * des：根据webElement类型获得对应的render
	 * @param webEle
	 * @param pageMeta
	 * @param parentRender
	 * @return
	 */
	public ILuiRender getUIRender(UIPartMeta uimeta, UIElement uiEle,LuiElement webEle, PagePartMeta pageMeta, ILuiRender parentRender);

	/**
	 * 2011-7-19 下午01:36:32 renxh des：弹出窗口的render
	 * 
	 * @param webEle
	 * @param pageMeta
	 * @return
	 */
	public ILuiRender getContextMenuUIRender(UIPartMeta uimeta, UIElement uiEle,ContextMenuComp webEle, PagePartMeta pageMeta, ILuiRender parentRender);
}
