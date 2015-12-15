package xap.lui.psn.guided;

import xap.lui.core.layout.UIFlowvLayout;
import xap.lui.core.layout.UIFlowvPanel;
import xap.lui.core.layout.UIToolBar;

public class UiCommBaseLayout {
	private UIFlowvLayout layoutMain;
	private UIToolBar toolBar;
	private UIFlowvPanel panelvMain;
	
	/**
	 * 主vpanel，可以向里边添加各项布局，比如 左侧树右侧表（表） 的布局 添加到此panel
	 */
	public UIFlowvPanel getPanelvMain(){
		return panelvMain;
	}
	/**
	 * 最外围的Layout，用于加入UiPageMeta
	 */
	public UIFlowvLayout getLayoutMain(){
		return layoutMain;
	}
	/**
	 * 获取toolBar 控件
	 */
	public UIToolBar getToolBar() {
		return toolBar;
	}
	public UiCommBaseLayout(){
		
	   layoutMain = new UIFlowvLayout();
	   layoutMain.setId("flowvlayout"+CommTool.getRndNum(4));
		     
	   	UIFlowvPanel panelvTop=new UIFlowvPanel();
  	    panelvTop.setId("panelv"+CommTool.getRndNum(5));
	    panelvTop.setHeight("40");
	    toolBar=new UIToolBar();
	    toolBar.setHeight("40");
	    panelvTop.setElement(toolBar);
	    layoutMain.addPanel(panelvTop);
		     
	    panelvMain=new UIFlowvPanel();
	    panelvMain.setId("panelv"+CommTool.getRndNum(5));
	    layoutMain.addPanel(panelvMain);
	}
}
