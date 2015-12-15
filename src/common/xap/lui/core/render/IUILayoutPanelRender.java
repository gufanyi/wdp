package xap.lui.core.render;

import xap.lui.core.layout.UIElement;

public interface IUILayoutPanelRender extends IUIElementRender {

	void setElement(UIElement element);

	void removeElement(UIElement element);
}
