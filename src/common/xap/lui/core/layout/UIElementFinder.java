package xap.lui.core.layout;

import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import xap.lui.core.comps.IContainerComp;
import xap.lui.core.comps.LuiElement;
import xap.lui.core.comps.MenuItem;
import xap.lui.core.comps.MenubarComp;
import xap.lui.core.comps.WebComp;
import xap.lui.core.model.PagePartMeta;
import xap.lui.core.model.ViewPartMeta;



public final class UIElementFinder {

	/**
	 * 在被查找元素中找到对应的元素的父元素,被查找元素是布局或者控件
	 * @param targetEle 被查找元素
	 * @param searchEle 查找元素
	 * @return
	 */
	public static UIElement findParent(UIElement targetEle, UIElement searchEle) {
		if (targetEle == null || targetEle instanceof UIComponent)
			return null;
		if(searchEle instanceof UILayoutPanel){
			if(searchEle instanceof UIGridPanel){
				UIGridPanel panel = (UIGridPanel)searchEle;
				return findParent(targetEle, panel.getParent(), (UILayoutPanel) searchEle);
			}else{
				return findParent(targetEle, ((UILayoutPanel) searchEle).getLayout(), (UILayoutPanel) searchEle);
			}
		}
		
		//布局需按照panel查找
		if (targetEle instanceof UILayout) {
			Iterator<UILayoutPanel> it = ((UILayout)targetEle).getPanelList().iterator();
			while(it.hasNext()){
				UILayoutPanel panel = it.next();
				UIElement ele = findParent(panel, searchEle);
				if(ele != null)
					return ele;
			}
			if(targetEle instanceof UITabComp) {
				UIElement ele = findParent(((UITabComp)targetEle).getRightPanel(), searchEle);
				if(ele != null)
					return ele;
			}
		} 
		else if (targetEle instanceof UILayoutPanel) {
			UIElement childEle = ((UILayoutPanel)targetEle).getElement();
			if(isEqual(childEle, searchEle))
				return targetEle;
			return findParent(childEle, searchEle);
		}else if (targetEle instanceof UIAbsoluteLayout){
			List<UIComponent> childEles=((UIAbsoluteLayout)targetEle).getComponentList();
			Iterator<UIComponent> it = childEles.iterator();
			while(it.hasNext()){
				UIComponent comp=it.next();
				if(isEqual(comp, searchEle))
					return targetEle;
			}
		}
		return null;
	}
	
	/**
	 * 在被查找元素中找到对应的元素的父元素,被查找元素是布局Panel
	 * @param targetEle 被查找元素
	 * @param searchEle 查找元素
	 * @return
	 */
	public static UIElement findParent(UIElement targetEle, UILayout searchPEle, UILayoutPanel searchEle) {
		if (targetEle == null || targetEle instanceof UIComponent)
			return null;
		
		//布局需按照panel查找
		if (targetEle instanceof UILayout) {
			Iterator<UILayoutPanel> it = ((UILayout)targetEle).getPanelList().iterator();
			while(it.hasNext()){
				UILayoutPanel panel = it.next();
				if(isEqual(targetEle, panel, searchPEle, searchEle))
					return targetEle;
				UIElement ele = findParent(panel, searchPEle, searchEle);
				if(ele != null)
					return ele;
			}
			if(targetEle instanceof UITabComp) {
				UIElement ele = findParent(((UITabComp)targetEle).getRightPanel(), searchPEle, searchEle);
				if(ele != null)
					return ele;
			}
		} 
		else if (targetEle instanceof UILayoutPanel) {
			UIElement childEle = ((UILayoutPanel)targetEle).getElement();
			return findParent(childEle, searchPEle, searchEle);
		}
		return null;
	}
	
	public static boolean isEqual(UIElement targetPEle, UIElement targetEle, UIElement searchPEle, UIElement searchEle){
		return isEqual(targetPEle, searchPEle) && isEqual(targetEle, searchEle);
	}
	
	public static boolean isEqual(UIElement targetEle, UIElement searchEle){
		if(targetEle == null || searchEle == null)
			return false;
		if(targetEle.getClass().equals(searchEle.getClass())){
			if(targetEle.getId() != null){
				return targetEle.getId().equals(searchEle.getId());
			}
		}
		return false;
	}


	/**
	 * 查找元素，本元素仅包含控件与布局
	 */
	public static UIElement findElementById(UIElement ele, String id) {
		UIElement uiEle = null;
		if (ele == null || id == null)
			return null;
		
		if(ele.getId() != null && ele.getId().equals(id))
			return ele;
		
		if(ele instanceof UILayoutPanel){
			uiEle = findElementById(((UILayoutPanel) ele).getElement(), id);
		}
		else if (ele instanceof UILayout) {
			Iterator<UILayoutPanel> it = ((UILayout)ele).getPanelList().iterator();
			while(it.hasNext()){
				UILayoutPanel panel = it.next();
				uiEle = findElementById(panel, id);
				if(uiEle != null)
					break;
			}
			if(uiEle == null) {
				if(ele instanceof UITabComp) {
					UITabRightPanel rightPanel = ((UITabComp)ele).getRightPanel();
					uiEle = findElementById(rightPanel, id);
				}
			}
			
		}else if (ele instanceof UIAbsoluteLayout) {
			Iterator<UIComponent> it = ((UIAbsoluteLayout) ele).getComponentList().iterator();
			while(it.hasNext()){
				UIComponent component = it.next();
				uiEle = findElementById(component,id);
				if(uiEle != null)
					break;
			}
		}
		return uiEle;
	}
	
	public static LuiElement findWebElementById(PagePartMeta pagemeta, String widgetId, String id){
		ViewPartMeta widget = pagemeta.getWidget(widgetId);
		if(widget == null)
			return null;
		return findWebElementById(widget, id);
	}
	
	public static LuiElement findWebElementById(PagePartMeta pagemeta, String widgetId, String parentId, String id){
		ViewPartMeta widget = pagemeta.getWidget(widgetId);
		if(widget == null)
			return null;
		return findWebElementById(widget, parentId, id);
	}

	public static LuiElement findWebElementById(ViewPartMeta widget, String id){
		WebComp[] components = widget.getViewComponents().getComps();
		for (int i = 0; i < components.length; i++) {
			if(components[i].getId().equals(id))
				return components[i];
		}
		WebComp[] menubars = widget.getViewMenus().getMenuBars();
		for (int i = 0; i < menubars.length; i++) {
			if(menubars[i].getId().equals(id))
				return menubars[i];
		}
		return null;
	}
	
	@SuppressWarnings("rawtypes")
	public static LuiElement findWebElementById(ViewPartMeta widget, String parentId, String id){
		LuiElement ele = findWebElementById(widget, parentId);
		if(ele instanceof IContainerComp)
			return ((IContainerComp)ele).getElementById(id);
		return null;
	}
	
	public static MenuItem findMenuItemById(PagePartMeta pagemeta, String widgetId, String parentId, String id){
		ViewPartMeta widget = pagemeta.getWidget(widgetId);
		MenubarComp menubar= widget.getViewMenus().getMenuBar(parentId);
		if(menubar.getMenuList()!=null && menubar.getMenuList().size()>0){
			for (MenuItem item : menubar.getMenuList()) {
				if(item.getId().equals(id)){
					return item;
				}
			}
		}
		return null;
	}
	
	public static UIElement findElementById(UIElement ele, String pid, String id) {
		if (ele == null || id == null)
			return null;
		UIElement uiEle = findElementById(ele, pid);

		if(!(uiEle instanceof UILayout))
			return null;
		
		Iterator<UILayoutPanel> it = ((UILayout)uiEle).getPanelList().iterator();
		while(it.hasNext()){
			UILayoutPanel panel = it.next();
			if(id.equals(panel.getId()))
				return panel;
		}
		if(uiEle instanceof UITabComp) {
			UITabRightPanel rightPanel = ((UITabComp)uiEle).getRightPanel();
			if(StringUtils.equals(rightPanel.getId(), id)) {
				return rightPanel;
			}
		}
		return null;
	}
	
	/**
	 * 按照WidgetId， UIMetaID 查找UIMeta
	 * @param uiEle
	 * @param widgetId
	 * @param id
	 * @return
	 */
	public static UIPartMeta findUIMeta(UIElement uiEle, String widgetId, String id){
		UIElement ele = findElementById(uiEle, widgetId);
		if(ele instanceof UIViewPart){
			UIPartMeta um = ((UIViewPart)ele).getUimeta();
			if(um == null)
				return null;
			if(id.equals(um.getId()))
				return um;
		}
		return null;
	}
	
	/**
	 * 按照WidgetId， UIMetaID 查找UIMeta
	 * @param uiEle
	 * @param widgetId
	 * @param id
	 * @return
	 */
	public static UIPartMeta findUIMeta(UIElement uiEle, String widgetId){
		UIElement ele = findElementById(uiEle, widgetId);
		if(ele != null && ele instanceof UIViewPart){
			return ((UIViewPart)ele).getUimeta();
		}
		return null;
	}
	
	/**
	 * 按照WidgetId， UIMetaID 查找UIMeta
	 * @param uiEle
	 * @param widgetId
	 * @param id
	 * @return
	 */
	public static UIViewPart findUIWidget(UIPartMeta uiEle, String widgetId){
		if(uiEle == null){
			return null;
		}
		UIViewPart um = uiEle.getDialog(widgetId);
		if(um != null)
			return um;
		UIElement ele = findElementById(uiEle, widgetId);
		if(ele != null && ele instanceof UIViewPart){
			return (UIViewPart) ele;
		}
		return null;
	}


}
