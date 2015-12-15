package xap.lui.psn.guided;

import xap.lui.core.layout.UITabItem;

public class UiParChiTabPopUpCardLayout extends UiParChiTabPopUpFullLayout{
	public UiParChiTabPopUpCardLayout(){
		UITabItem tabItemOne=(UITabItem)getTabComp().getPanelList().get(0);
		tabItemOne.removeElement(getGridComp_Chi_One());
		UiParChiTabCardCardLayout parChiCardCardOper=new UiParChiTabCardCardLayout();
		UITabItem tabItemOneClone= parChiCardCardOper.getOneTabItem();
		tabItemOne.setElement(tabItemOneClone.getElement());
	}

}
