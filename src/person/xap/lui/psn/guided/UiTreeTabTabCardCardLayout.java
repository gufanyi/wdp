package xap.lui.psn.guided;

import xap.lui.core.layout.UIFlowhPanel;
import xap.lui.core.layout.UITreeComp;

public class UiTreeTabTabCardCardLayout extends UiParChiTabCardCardLayout{
	private UITreeComp treeComp;
	/**
	 * 右侧UI树控件
	 * @return treeComp UI树控件对象
	 */
    public UITreeComp getTreeComp(){
    	return treeComp;
    }
	public UiTreeTabTabCardCardLayout(){
		
		UiTreeTabCardLayout uiTreeTab=new UiTreeTabCardLayout();
		UIFlowhPanel panelHLeftTree=uiTreeTab.getPanelHLeftTree();
		treeComp=(UITreeComp)panelHLeftTree.getElement();
		UIFlowhPanel PanelHRightMain=(UIFlowhPanel)getLayoutHTreeTab().getPanelList().get(0);
		getLayoutHTreeTab().addPanel(panelHLeftTree, PanelHRightMain, false);
	}

}
