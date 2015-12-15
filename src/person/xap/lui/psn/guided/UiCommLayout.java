package xap.lui.psn.guided;

import xap.lui.core.layout.UIFlowhLayout;
import xap.lui.core.layout.UIFlowhPanel;
import xap.lui.core.layout.UIFlowvLayout;
import xap.lui.core.layout.UIFlowvPanel;
import xap.lui.core.layout.UIGridComp;
import xap.lui.core.layout.UITabItem;

public class UiCommLayout extends UiCommBaseLayout {
	
	private UIFlowhLayout layoutHTreeTab;
	private UIFlowvLayout layoutVTabTab;
	private UIFlowvPanel panelVTopTab;
	

	/**
	 * 用于布局树和表（表）横向layout
	 */
	public UIFlowhLayout getLayoutHTreeTab() {
		return layoutHTreeTab;
	}
	/**
	 * 用于布局主子表的纵向layout
	 */
	public UIFlowvLayout getLayoutVTabTab() {
		return layoutVTabTab;
	}
	/**
	 * 用于布局主表的Panel
	 */
	public UIFlowvPanel getPanelVTopTab() {
		return panelVTopTab;
	}

	public UiCommLayout(){
		
		layoutHTreeTab= new UIFlowhLayout();
		layoutHTreeTab.setId("flowhlayout"+CommTool.getRndNum(4));
		getPanelvMain().setElement(layoutHTreeTab);//UiBaseLayout 过来的主Panel
		
		
		UIFlowhPanel panelHRightMain=new UIFlowhPanel();
		panelHRightMain.setId("panelh"+CommTool.getRndNum(5));
		layoutHTreeTab.addPanel(panelHRightMain);//右侧主子表 主panel
		
	    layoutVTabTab=new UIFlowvLayout();
	    layoutVTabTab.setId("flowvlayout"+CommTool.getRndNum(4));
		panelHRightMain.setElement(layoutVTabTab);//右侧主子表 主layout
		
		
	    panelVTopTab=new UIFlowvPanel();
		panelVTopTab.setId("panelv"+CommTool.getRndNum(5));
		layoutVTabTab.addPanel(panelVTopTab);//右侧主子表 主表panel
	}
	/** 
	 * @return
	 * TabLayout里边的一项TabPanel
	 */
	public UITabItem getOneTabItem(){
		UITabItem tabItem=new UITabItem();//TabLayout的第一项
		tabItem.setId("UITabItem"+CommTool.getRndNum(5));
		UIGridComp gridComp_Chi=new UIGridComp();
	    tabItem.setElement(gridComp_Chi);
	    //tabComp.addPanel(tabItem);
		return tabItem;
		
	}

}
