package xap.lui.psn.guided;

import xap.lui.core.layout.UIGridComp;

public class UiSigTabPopUpLayout extends UiCommLayout{
	private UIGridComp gridComp_Par;
	/**
	 * 获取主表 弹出窗口编辑类型的 grid表格控件
	 */
	public UIGridComp getGridComp_Par() {
		return gridComp_Par;
	}
	public UiSigTabPopUpLayout(){
	    gridComp_Par=new UIGridComp();
		getPanelVTopTab().setElement(gridComp_Par);
	}

}
