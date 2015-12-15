package xap.lui.psn.formula;

import xap.lui.core.comps.ButtonComp;
import xap.lui.core.layout.UIButton;
import xap.lui.core.layout.UITabItem;
import xap.lui.core.model.ViewPartComps;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.psn.refence.FormulaPageModel;

public class ExtendFormula extends FormulaPageModel {
	@Override
	public void addTabItem(){
		ViewPartMeta formulaView = getFormulaView();
		UITabItem item = createTabItem("测试");
		
		ButtonComp btn = new ButtonComp();
		btn.setId("btn22222");
		btn.setText("按钮");
		ViewPartComps viewPartComps = formulaView.getViewComponents();
		viewPartComps.addComponent(btn);
		
		UIButton uibtn = new UIButton();
		uibtn.setId(btn.getId());
		uibtn.setViewId(VIEWID);//ui控件和布局一定要加viewId
		item.setElement(uibtn);
	}
}
