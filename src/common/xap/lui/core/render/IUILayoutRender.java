package xap.lui.core.render;

import xap.lui.core.comps.LuiElement;
import xap.lui.core.layout.UIElement;

public interface IUILayoutRender<T extends UIElement, K extends LuiElement> extends IUIElementRender {

	void addChild(UIElement element);
	void removeChild(UIElement element);
	
}
