package xap.lui.core.render;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import xap.lui.core.comps.LuiElement;
import xap.lui.core.event.IEventSupport;
import xap.lui.core.layout.UIConstant;
import xap.lui.core.layout.UILayoutPanel;
import xap.lui.core.layout.UITabComp;
import xap.lui.core.layout.UITabItem;
import xap.lui.core.layout.UITabRightPanel;
import xap.lui.core.model.AppSession;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.model.LuiPageContext;

/**
 * @author renxh 页签布局渲染器
 * 
 */
@SuppressWarnings("unchecked")
public class PCTabLayoutRender extends UILayoutRender<UITabComp, LuiElement> {
	public static final String TAB_LAYOUT_BASE = "tab_layout_";
	private String className;
	private int currentIndex = 0;
	private boolean oneTabHide = false;
	private String tabType = UITabComp.TABTYPE_TOP;
	private List<String> itemList = new ArrayList<String>();

	public PCTabLayoutRender(UITabComp uiEle) {
		super(uiEle);
		UITabComp tabLayout = this.getUiElement();
		this.oneTabHide = tabLayout.getOneTabHide();
		this.tabType = (tabLayout.getTabType() == null || tabLayout.getTabType().equals("")) ? "top" : tabLayout.getTabType();
		try {
			this.currentIndex = tabLayout.getCurrentItem();
		} catch (Exception e) {
			this.currentIndex = 0;
		}

	}



	public String place() {
		StringBuilder buf = new StringBuilder();
		buf.append(this.placeSelf());

		UITabComp layout = getUiElement();
		List<UILayoutPanel> pList = layout.getPanelList();
		Iterator<UILayoutPanel> it = pList.iterator();
		while (it.hasNext()) {
			UILayoutPanel panel = it.next();
			ILuiRender render = panel.getRender();
			if (render != null) {
				buf.append(render.place());
				buf.append(getDivId()).append(".append(").append(render.getNewDivId()).append(");\n");
			}
		}
		UITabRightPanel rightPanel = layout.getRightPanel();
		if (rightPanel != null) {
			ILuiRender render = rightPanel.getRender();
			if (render != null)
				buf.append(render.place());
		}
		return buf.toString();
	}

	public String create() {
		StringBuilder buf = new StringBuilder();
		buf.append(this.createDesignHead());
		buf.append(this.createHead());
		// 子节点
		UITabComp layout = getUiElement();
		List<UILayoutPanel> pList = layout.getPanelList();
		Iterator<UILayoutPanel> it = pList.iterator();
		while (it.hasNext()) {
			UILayoutPanel panel = it.next();
			ILuiRender render = panel.getRender();
			if (render != null)
				buf.append(render.create());
		}
		buf.append(this.createTail());
		UITabRightPanel rightPanel = layout.getRightPanel();
		if (rightPanel != null) {
			ILuiRender render = rightPanel.getRender();
			if (render != null)
				buf.append(render.create());
		}
		buf.append(this.createDesignTail());
		return buf.toString();
	}





	public String createHead() {
		UITabComp tab = this.getUiElement();

		StringBuilder buf = new StringBuilder();
		String showId = getVarId();
		buf.append("var ").append(showId).append(" = $('<div id=\"" + tab.getId() + "\">').appendTo(").append("$('#" + getDivId() + "')").append(").tab({name:\"");
		String tabWidth = tab.getTabWidth();
		if (tabWidth != null && !"".equals(tabWidth)) {
			if (tabWidth.indexOf("%") == -1) {
				if (tabWidth.lastIndexOf("px") == -1) {
					tabWidth += "px";
				}
			}
		} else {
			tabWidth = "100%";
		}

		String tabHeight = tab.getTabHeight();
		if (tabHeight != null && !"".equals(tabHeight)) {
			if (tabHeight.indexOf("%") == -1) {
				if (tabHeight.lastIndexOf("px") == -1) {
					tabHeight += "px";
				}
			}
		} else {
			tabHeight = "";
		}

		buf.append(getId()).append("\",left: 0, top:0, width:\"100%\", height:\"100%\",tabWidth:\"" + tabWidth + "\",tabHeight:\"" + tabHeight + "\", tabItemWidth:'-1',");
		String itemWidth = addPX(tab.getTabItemWidth());
		buf.append("itemWidth:\"" + itemWidth + "\",");
		String itemHeight = addPX(tab.getTabItemHeight());
		buf.append("itemHeight:\"" + itemHeight + "\",");

		String bgColor = tab.getBgColor();
		if (StringUtils.isNotEmpty(bgColor) && !"null".equals(bgColor)) {
			buf.append("bgColor:\"" + bgColor + "\",");
		}

		String activeItemColor = tab.getActiveItemColor();
		if (StringUtils.isNotEmpty(activeItemColor) && !"null".equals(activeItemColor)) {
			buf.append("activeItemColor:\"" + activeItemColor + "\",");
		}

		String normalItemColor = tab.getNormalItemColor();
		if (StringUtils.isNotEmpty(normalItemColor) && !"null".equals(normalItemColor)) {
			buf.append("normalItemColor:\"" + normalItemColor + "\",");
		}

		String activeLineColor = tab.getActiveLineColor();
		if (StringUtils.isNotEmpty(activeLineColor) && !"null".equals(activeLineColor)) {
			buf.append("activeLineColor:\"" + activeLineColor + "\",");
		}

		String normalLineColor = tab.getNormalLineColor();
		if (StringUtils.isNotEmpty(normalLineColor) && !"null".equals(normalLineColor)) {
			buf.append("normalLineColor:\"" + normalLineColor + "\",");
		}

		String normalFontColor = tab.getNormalFontColor();
		if (StringUtils.isNotEmpty(normalFontColor) && !"null".equals(normalFontColor)) {
			buf.append("normalFontColor:\"" + normalFontColor + "\",");
		}

		String activeFontColor = tab.getActiveFontColor();
		if (StringUtils.isNotEmpty(activeFontColor) && !"null".equals(activeFontColor)) {
			buf.append("activeFontColor:\"" + activeFontColor + "\",");
		}

		String isOuterTab = tab.getIsOuterTab();
		if (StringUtils.isNotEmpty(isOuterTab) && !"null".equals(isOuterTab)) {
			buf.append("isOuterTab:" + isOuterTab + ",");
		}

		String fontSize = tab.getFontSize();
		if (StringUtils.isNotEmpty(fontSize) && !"null".equals(fontSize)) {
			buf.append("fontSize:\"" + fontSize + "\",");
		}

		buf.append("attrArr:{tabType:'").append(this.tabType).append("'").append(",flowmode:").append(isFlowMode()).append(",hideTabBar:").append(tab.isHideTabBar()).append("},className:");
		if (className != null && !className.equals(""))
			buf.append("\"").append(className).append("\"}).tab('instance');\n");
		else
			buf.append("''}).tab('instance');\n");

		if (getViewId() != null && getViewId().length() > 0) {
			String widget = WIDGET_PRE + this.getCurrWidget().getId();
			buf.append("var ").append(widget).append(" = pageUI.getViewPart('").append(this.getCurrWidget().getId()).append("');\n");
			buf.append(widget + ".addTab(" + getVarId() + ");\n");
		} else {
			buf.append("pageUI.addTab(" + getVarId() + ");\n");
		}

		if (tab != null) {
			buf.append(addEventSupport(tab, getViewId(), showId, null));
		}
		// TabLayout tab =getTab();
		// if (tab != null) {
		// buf.append(addEventSupport(tab, getWidget(), showId, null));
		// }
		return buf.toString();
	}

	private String addPX(String value) {
		String retValue = "";
		if (value != null && !"".equals(value)) {
			if (value.indexOf("%") == -1) {
				if (value.lastIndexOf("px") == -1) {
					retValue = value + "px";
				}
			} else {
				retValue = value;
			}
		}

		return retValue;
	}



	public String createTail() {

		StringBuilder buf = new StringBuilder();
		UITabComp tab = getUiElement();
		// List<TabItem> items = getTab().getItemList();
		List<UILayoutPanel> items = tab.getPanelList();
		int size = items.size();
		for (int i = 0; i < size; i++) {
			if (currentIndex == i)
				continue;
			UITabItem tabItem = (UITabItem) items.get(i);
			if (tabItem.getActive() != null)
				if (tabItem.getActive().intValue() == 1) {
					buf.append(getVarId()).append(".activeTab(").append(i).append(");\n");
				}
		}
		if (tab.getNeedEvent().equals(UIConstant.FALSE)) {
			buf.append(getVarId()).append(".activeTab(").append(currentIndex).append(",null,true);\n");
		} else
			buf.append(getVarId()).append(".activeTab(").append(currentIndex).append(");\n");

		// 如果仅仅有一个Tab，则隐藏
		// if (items.size() == 1 && oneTabHide) { //
		// buf.append(getVarId()).append(".hideTabHead();\n");
		// }
		if (oneTabHide) {
			buf.append(getVarId()).append(".setOnTabHide(true);\n");
		}
		for (int i = 0; i < size; i++) {
			UITabItem tabItem = (UITabItem) items.get(i);
			if (!tabItem.getVisible()) {
				buf.append(getVarId()).append(".hideItem('"+tabItem.getId()+"');\n");
			}
		}
		

		return buf.toString();
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public int getCurrentIndex() {
		return currentIndex;
	}

	public boolean isOneTabHide() {
		return oneTabHide;
	}

	public List<String> getItemList() {
		return itemList;
	}

	public void setItemList(List<String> itemList) {
		this.itemList = itemList;
	}

	@Override
	protected String getSourceType(IEventSupport ele) {
		return LuiPageContext.SOURCE_TYPE_TAG;
	}

	@Override
	public String placeSelf() {
		StringBuilder buf = new StringBuilder();
		buf.append("var ").append(getNewDivId()).append(" = $('<div>').attr('id','").append(getNewDivId()).append("').css({\n");
		buf.append("'width':'100%',\n");
		buf.append("'height':'100%',\n");
		buf.append("'overflow':'hidden',\n");
		buf.append("'top':'0px',\n");
		buf.append("'left':'0px',\n");
		buf.append("'padding':'0px'});\n");
		if (this.isEditMode()) {
			buf.append(this.placeDesign());
			buf.append(getNewDivId()).append(".append(" + getDivId() + ");\n");
		}
		return buf.toString();
	}
	
	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void setCurrentItem(int currentItem) {
		String widgetId = getViewId();
		StringBuilder buf = new StringBuilder();
		buf.append("pageUI");
		if (widgetId != null && widgetId.length() > 0)
			buf.append(".getViewPart(\"" + getViewId() + "\")");
		buf.append(".getTab(\"" + getId() + "\")").append(".activeTab(").append(currentItem).append(");\n");
		addDynamicScript(buf.toString());
	}
	
	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void setOneTabHide(boolean isOneTabHide) {
		String widgetId = getViewId();
		StringBuilder buf = new StringBuilder();
		buf.append("pageUI");
		if (widgetId != null && widgetId.length() > 0)
			buf.append(".getViewPart(\"" + getViewId() + "\")");
		buf.append(".getTab(\"" + getId() + "\")").append(".setOnTabHide(").append(isOneTabHide).append(");\n");
		addBeforeExeScript(buf.toString());
	}
	
	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void addRightPanel(){
		String widgetId = getViewId();
		StringBuilder buf = new StringBuilder();
		UITabComp tabLayout = this.getUiElement();
		UITabRightPanel rightPanel = tabLayout.getRightPanel();
		String rightPanelID = (String) rightPanel.getId();
		String divId = DIV_PRE + widgetId + "_" + getId() + getId() + "_right";
		buf.append("pageUI");
		if (widgetId != null && widgetId.length() > 0)
			buf.append(".getViewPart(\"" + getViewId() + "\")");
		buf.append(".getTab(\"" + getId() + "\")").append(".createTabRightPanel('").append(divId).append("',").append(rightPanel.getWidth()).append(");\n");

		if (isEditMode()) {
			buf.append("var params = {");
			buf.append("widgetid:'").append(widgetId).append("'");
			buf.append(",uiid:'").append(getId()).append("'");
			buf.append(",subuiid:'").append(rightPanelID).append("'");
			buf.append(",type:'").append(LuiPageContext.SOURCE_TYPE_TABSPACE).append("'");
			buf.append("};\n");
			buf.append("$.design.getObj({divObj:$('#" + divId + "')[0],params:params,objType:'layout'});\n");
			buf.append("$.draglistener.getObj($('#" + divId + "')[0]);");
		}
		addDynamicScript(buf.toString());
	}
	
	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void removeRightPanel(){
		String widgetId = getViewId();
		StringBuilder buf = new StringBuilder();
		buf.append("pageUI");
		if (widgetId != null && widgetId.length() > 0)
			buf.append(".getViewPart(\"" + getViewId() + "\")");
		buf.append(".getTab(\"" + getId() + "\")").append(".removeTabRightPanel();\n");
		addBeforeExeScript(buf.toString());
	}

	@Override
	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void destroy() {
		super.destroy();
		String widgetId = getViewId();
		StringBuilder buf = new StringBuilder();
		buf.append("pageUI");
		if (widgetId != null && widgetId.length() > 0)
			buf.append(".getViewPart(\"" + getViewId() + "\").removeTab('" + getId() + "');");
		AppSession.current().getAppContext().addAfterExecScript(buf.toString());
	}

}
