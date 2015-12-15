package xap.lui.psn.guided;

import xap.lui.core.layout.UICardLayout;
import xap.lui.core.layout.UICardPanel;
import xap.lui.core.layout.UIFormComp;
import xap.lui.core.layout.UIGridComp;
import xap.lui.core.layout.UITabItem;

public class UiParChiTabCardCardLayout extends UiParChiTabCardFullLayout{
	public UiParChiTabCardCardLayout(){
		UITabItem tabItemOne=(UITabItem)getTabComp().getPanelList().get(0);
		tabItemOne.removeElement(getGridComp_Chi_One());
		UITabItem tabItemOneClone= getOneTabItem();
		tabItemOne.setElement(tabItemOneClone.getElement());
	}
	
	@Override
	public UITabItem getOneTabItem() {
		UITabItem oneTabItem=new UITabItem();
		oneTabItem.setId("UITabItem"+CommTool.getRndNum(5));
		
		UICardLayout cardLayout_Chi=new UICardLayout();
		cardLayout_Chi.setId("cardlayout"+CommTool.getRndNum(4));
		oneTabItem.setElement(cardLayout_Chi);//卡片的 cardlayout
     
     
	     UICardPanel cardPanelLeftGrid=new UICardPanel();
	     cardPanelLeftGrid.setId("cardpanel"+CommTool.getRndNum(5));
	     UIGridComp gridComp_Chi=new UIGridComp();
	     cardPanelLeftGrid.setElement(gridComp_Chi);
	     cardLayout_Chi.addPanel(cardPanelLeftGrid);//卡片的 左卡 表格
	     
	     UICardPanel cardPanelRightForm=new UICardPanel();
	     cardPanelRightForm.setId("cardpanel"+CommTool.getRndNum(5));
	     UIFormComp formComp_Chi=new UIFormComp();
	     cardPanelRightForm.setElement(formComp_Chi);
	     cardLayout_Chi.addPanel(cardPanelRightForm);//卡片的右卡 表单
		return oneTabItem;
	}

	

}
