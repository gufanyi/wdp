package xap.lui.psn.guided;

import xap.lui.core.layout.UIFlowhPanel;
import xap.lui.core.layout.UITreeComp;

public class UiTreeTabCardLayout extends UiSigTabCardLayout{
	private UITreeComp treeComp;
	/**
	 * 右侧UI树控件
	 * @return treeComp UI树控件对象
	 */
    public UITreeComp getTreeComp(){
    	return treeComp;
    }
	/**
	 * 获取左侧存放树的panel对象
	 */
    public UiTreeTabCardLayout(){
		UIFlowhPanel PanelHRightMain=(UIFlowhPanel)getLayoutHTreeTab().getPanelList().get(0);
		UIFlowhPanel panelHLeftTree=getPanelHLeftTree();
		getLayoutHTreeTab().addPanel(panelHLeftTree, PanelHRightMain, false);
    }
    
	public UIFlowhPanel getPanelHLeftTree(){
		UIFlowhPanel panelHLeftTree=new UIFlowhPanel();//左侧树panel
		panelHLeftTree.setId("panelh"+CommTool.getRndNum(5));
		panelHLeftTree.setWidth("180");
		panelHLeftTree.setRightBorder("2px gray solid");
		
		treeComp=new UITreeComp();//右侧树控件
		panelHLeftTree.setElement(treeComp);
		return panelHLeftTree;
	}
}
