package xap.lui.psn.guided;

import xap.lui.core.layout.UICardLayout;
import xap.lui.core.layout.UICardPanel;
import xap.lui.core.layout.UIFormComp;
import xap.lui.core.layout.UIGridComp;

public class UiSigTabCardLayout extends UiCommLayout{

//	private UICardPanel cardPanelLeftGrid;
//	private UICardPanel cardPanelRightForm;
//	public UICardPanel getCardPanelLeftGrid() {
//		return cardPanelLeftGrid;
//	}
//	public UICardPanel getCardPanelRightForm() {
//		return cardPanelRightForm;
//	}
	private UICardLayout cardLayout_Par;
	private UIGridComp gridComp_Par;
	private UIFormComp formComp_Par;
	/**
	 * 由于卡片编辑的操作需要记录 layoutId，所以提供该对象
	 */
	public UICardLayout getCardLayout_Par() {
		return cardLayout_Par;
	}
	/**
	 * 获取主表 卡片编辑类型的 grid表格控件
	 */
	public UIGridComp getGridComp_Par() {
		return gridComp_Par;
	}
	/**
	 * 获取主表 卡片编辑类型的 form表单控件
	 */
	public UIFormComp getFormComp_Par() {
		return formComp_Par;
	}
	


	public UiSigTabCardLayout(){
		cardLayout_Par=new UICardLayout();
		cardLayout_Par.setId("cardlayout"+CommTool.getRndNum(4));
		getPanelVTopTab().setElement(cardLayout_Par);//卡片的 cardlayout
     
     
	     UICardPanel cardPanelLeftGrid=new UICardPanel();
	     cardPanelLeftGrid.setId("cardpanel"+CommTool.getRndNum(5));
	     gridComp_Par=new UIGridComp();
	     cardPanelLeftGrid.setElement(gridComp_Par);
	     cardLayout_Par.addPanel(cardPanelLeftGrid);//卡片的 左卡 表格
	     
	     UICardPanel cardPanelRightForm=new UICardPanel();
	     cardPanelRightForm.setId("cardpanel"+CommTool.getRndNum(5));
	     formComp_Par=new UIFormComp();
	     cardPanelRightForm.setElement(formComp_Par);
	     cardLayout_Par.addPanel(cardPanelRightForm);//卡片的右卡 表单
	}
}
