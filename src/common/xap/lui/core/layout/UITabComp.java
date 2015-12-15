package xap.lui.core.layout;

import java.lang.reflect.Array;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.render.PCTabLayoutRender;
import xap.lui.core.render.notify.RenderProxy;

public class UITabComp extends UILayout {
	private static final long serialVersionUID = 9197424119017867646L;
	// public static final Integer ONETAB_HIDE_HIDE = 1;
	// public static final Integer ONETAB_HIDE_SHOW = 0;
	public static final String CURRENT_ITEM = "currentItem";
	public static final String ISONETAB_HIDE = "isOneTabHide";
	public static final String TAB_TYPE = "tabType";
	public static final String TABTYPE_TOP = "top";
	public static final String TABTYPE_BOTTOM = "bottom";
	public static final String TAB_BAR_HIDE = "tabBarHide";
	public static final String TAB_WIDTH = "width";
	public static final String TAB_HEIGHT = "height";
	public static final String TAB_ITEMWIDTH = "itemWidth";
	public static final String TAB_ITEMHEIGHT = "itemHeight";
	public static final String TAB_BGCOLOR = "bgColor";
	public static final String TAB_ACTITEMCOLOR = "activeItemColor";
	public static final String TAB_NORITEMCOLOR = "normalItemColor";
	public static final String TAB_ACTLINECOLOR = "activeLineColor";
	public static final String TAB_NORLINECOLOR = "normalLineColor";
	public static final String TAB_FONTSIZE = "fontSize";
	public static final String TAB_ACTIVEFONTCOLOR = "activeFontColor";
	public static final String TAB_NORMALFONTCOLOR = "normalFontColor";
	public static final String IS_OUTERTAB = "isOuterTab";
	public static final String TAB_ADDRIGHTPANEL = "addRightPanel";
	public static final String TAB_REMOVERIGHTPANEL = "removeRightPanel";
	public static final String CURRENT_NEEDEVENT = "needevent";
	public static final String WIDGET_NAME = "tab";
	
	private String id;
	private int currentItem;
	private String widgetId;
	private Boolean isOneTabHide;
	private String tabType;
	private String height;
	private String width;
	private String itemWidth;
	private String itemHeight;
	private String bgColor;
	private String activeItemColor;
	private String normalItemColor;
	private String activeLineColor;
	private String normalLineColor;
	private String normalFontColor;
	private String activeFontColor;
	private String isOuterTab;
	private String fontSize;
	private Boolean tabBarHide;
	private Integer needevent;

	private UITabRightPanel rightPanel;

	public UITabComp() { 
		this.isOneTabHide = false;
		this.tabType = TABTYPE_TOP;
		this.currentItem = 0;
		this.needevent = UIConstant.TRUE;
	}

	public UITabItem[] getTabItems() {
		List<UILayoutPanel> layOutList = this.getPanelList();
		return layOutList.toArray((UITabItem[]) Array.newInstance(
				UITabItem.class, 0));
		// List<UITabItem> tabItems=new ArrayList<UITabItem>(layOutList.size());
		// for(int i=0;i<layOutList.size();i++){
		// tabItems.add((UITabItem)layOutList.get(i));
		// }
		// //Array.newInstance(componentType, dimensions);
		// //System.arraycopy(src, srcPos, dest, destPos, length);
		// return tabItems.toArray(new UITabItem[0]);
	}
	
	@Override
	public String getWidgetName() {
		return WIDGET_NAME;
	}

	public UITabItem getTabItemById(String id) {
		UITabItem[] tabItems = getTabItems();
		for (UITabItem tabItem : tabItems) {
			if (StringUtils.equals(id, tabItem.getId())) {
				return tabItem;
			}
		}
		return null;
	}

	public UITabRightPanel getRightPanel() {
		return rightPanel;
	}

	public void setRightPanel(UITabRightPanel rightPanel) {
		rightPanel.setLayout(this);
		this.rightPanel = rightPanel;
		if (LifeCyclePhase.ajax.equals(getPhase())) {
			this.getRender().addRightPanel();
		}
		// this.getRender().
	}

	public void removeRightPanel(String rightPanelId, boolean withNotify) {
		this.rightPanel.getAttrMap().remove(rightPanelId);
		this.rightPanel = null;
		if (LifeCyclePhase.ajax.equals(getPhase())) {
			this.getRender().removeRightPanel();
		}
		// if(withNotify) {
		// this.notifyChange(UPDATE, TAB_REMOVERIGHTPANEL);
		// }
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getCurrentItem() {
		return currentItem;
	}

	public void setCurrentItem(int value) {
		this.currentItem = value;
		if (LifeCyclePhase.ajax.equals(getPhase())) {
			this.getRender().setCurrentItem(value);
		}
	}

	public String getViewId() {
		return widgetId;
	}

	public void setViewId(String widgetId) {
		this.widgetId = widgetId;
	}

	public void setOneTabHide(boolean oneTabHide) {
		this.isOneTabHide = oneTabHide;
		if (LifeCyclePhase.ajax.equals(getPhase())) {
			this.getRender().setOneTabHide(oneTabHide);
		}
	}

	public boolean getOneTabHide() {
		return isOneTabHide;
	}

	public void addPanel(UILayoutPanel panel) {
		if (panel == null)
			return;
		if (panel instanceof UITabRightPanel)
			this.setRightPanel((UITabRightPanel) panel);
		else
			super.addPanel(panel);
	}

	public String getTabType() {
		return tabType;
	}

	public void setTabType(String tabType) {
		this.tabType = tabType;
	}

	public boolean isHideTabBar() {
		return tabBarHide != null ? tabBarHide : false;
	}

	public void setHideTabBar(boolean isHideTabBar) {
		this.tabBarHide = isHideTabBar;
	}

	public Integer getNeedEvent() {
		return needevent;
	}

	public String getTabHeight() {
		return height;
	}

	public void setTabHeight(String tabHeight) {
		this.height = tabHeight;
	}

	public String getTabWidth() {
		return width;
	}

	public void setTabWidth(String tabWidth) {
		this.width = tabWidth;
	}

	public String getTabItemWidth() {
		return itemWidth;
	}

	public void setTabItemWidth(String itemWidth) {
		this.itemWidth = itemWidth;
	}

	public String getTabItemHeight() {
		return itemHeight;
	}

	public void setTabItemHeight(String itemHeight) {
		this.itemHeight = itemHeight;
	}

	public String getBgColor() {
		return bgColor;
	}

	public void setBgColor(String bgColor) {
		this.bgColor = bgColor;
	}

	public String getActiveItemColor() {
		return activeItemColor;
	}

	public void setActiveItemColor(String activeItemColor) {
		this.activeItemColor = activeItemColor;
	}

	public String getNormalItemColor() {
		return normalItemColor;
	}

	public void setNormalItemColor(String normalItemColor) {
		this.normalItemColor = normalItemColor;
	}

	public String getActiveLineColor() {
		return activeLineColor;
	}

	public void setActiveLineColor(String activeLineColor) {
		this.activeLineColor = activeLineColor;
	}

	public String getNormalLineColor() {
		return normalLineColor;
	}

	public void setNormalLineColor(String normalLineColor) {
		this.normalLineColor = normalLineColor;
	}

	public String getNormalFontColor() {
		return normalFontColor;
	}

	public void setNormalFontColor(String fontColor) {
		this.normalFontColor = fontColor;
	}

	public String getActiveFontColor() {
		return activeFontColor;
	}

	public void setActiveFontColor(String fontColor) {
		this.activeFontColor = fontColor;
	}

	public String getIsOuterTab() {
		return isOuterTab;
	}

	public void setIsOuterTab(String fontColor) {
		this.isOuterTab = fontColor;
	}

	public String getFontSize() {
		return fontSize;
	}

	public void setFontSize(String fontSize) {
		this.fontSize = fontSize;
	}

	/**
	 * 设置当前页签是否触发事件
	 * 
	 * @param active
	 */
	public void setNeedEvent(Integer needevent) {
		this.needevent = needevent;
	}

	@Override
	public UITabComp doClone() {
		UITabComp tabComp = (UITabComp) super.doClone();
		if (this.rightPanel != null) {
			tabComp.rightPanel = (UITabRightPanel) this.rightPanel.doClone();
		}
		return tabComp;
	}

	@Override
	public PCTabLayoutRender getRender() {
		if (render == null) {
			render = RenderProxy.getRender(new PCTabLayoutRender(this));
		}
		return (PCTabLayoutRender) render;
	}

}
