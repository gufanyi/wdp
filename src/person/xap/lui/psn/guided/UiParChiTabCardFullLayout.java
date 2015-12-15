package xap.lui.psn.guided;

import xap.lui.core.layout.UIFlowvPanel;
import xap.lui.core.layout.UIGridComp;
import xap.lui.core.layout.UIMenubarComp;
import xap.lui.core.layout.UITabComp;
import xap.lui.core.layout.UITabItem;
import xap.lui.core.layout.UITabRightPanel;

//主子表 主表卡片，子表全表编辑 
public class UiParChiTabCardFullLayout extends UiSigTabCardLayout {
	private UITabComp tabComp;
	private  UIGridComp gridComp_Chi_One;
	private UIMenubarComp menuBarComp_Chi;
	
	/**
	 * 主子表关系的 第一项子表 grid表格控件
	 */
	public UIGridComp getGridComp_Chi_One() {
		return gridComp_Chi_One;
	}
	/**
	 * 主子表关系的子表存放容器，暂时里边只有一项。用于需要多个页签的处理
	 */
	public UITabComp getTabComp() {
		return tabComp;
	}
	/**
	 * 主子表关系的 menuBar菜单控件
	 */
	public UIMenubarComp getMenuBarComp_Chi() {
		return menuBarComp_Chi;
	}

	
	/**
	 *主子表 主表卡片，子表全表编辑 
	 */
	public UiParChiTabCardFullLayout(){
		UIFlowvPanel panelVBottomTab=new UIFlowvPanel();//右侧主子表 子表主panel
		panelVBottomTab.setId("panelv"+CommTool.getRndNum(5));
		panelVBottomTab.setHeight("45%");
		getLayoutVTabTab().addPanel(panelVBottomTab);
		//右侧 主子表 子表 主TabLayout;由于不知道要生成几个页签，所以默认生成1个页签 和 右侧容器，如需要多个页签，另行处理
		tabComp=new UITabComp();
		tabComp.setId("tab"+CommTool.getRndNum(4));
		panelVBottomTab.setElement(tabComp);
		
		UITabItem tabItemOne=new UITabItem();//TabLayout的第一项
		tabItemOne.setId("UITabItem"+CommTool.getRndNum(5));
	    gridComp_Chi_One=new UIGridComp();
		tabItemOne.setElement(gridComp_Chi_One);
		tabComp.addPanel(tabItemOne);
		
		UITabRightPanel tabRightPanel=new UITabRightPanel();//TabLayout的 右侧容器
		tabRightPanel.setId("tabspace"+CommTool.getRndNum(5));
		tabRightPanel.setWidth("200");
		menuBarComp_Chi=new UIMenubarComp();
		menuBarComp_Chi.setViewId("main");
		tabRightPanel.setElement(menuBarComp_Chi);
		tabComp.addPanel(tabRightPanel);
	}
	
	
}
