package xap.lui.psn.dsmgr;

import xap.lui.core.comps.TreeViewComp;
import xap.lui.core.event.AppRequestProcessor;
import xap.lui.core.layout.UIButton;
import xap.lui.core.layout.UIFlowhLayout;
import xap.lui.core.layout.UIFlowhPanel;
import xap.lui.core.layout.UIFlowvLayout;
import xap.lui.core.layout.UIFlowvPanel;
import xap.lui.core.layout.UIPartMeta;
import xap.lui.core.layout.UITreeComp;
import xap.lui.core.layout.UIViewPart;
import xap.lui.core.mock.MockTreePageModel;
import xap.lui.core.model.IUIPartMeta;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.core.model.PagePartMeta;

public class MockFieldRefPageModel extends MockTreePageModel {
	protected void constructWidget(ViewPartMeta widget) {
		widget.setController(MockFieldRefController.class.getName());
	}
	
	@Override
	protected PagePartMeta createPageMeta() {
		PagePartMeta pm =  super.createPageMeta();		
		pm.setProcessorClazz(AppRequestProcessor.class.getName());
		return pm;
	}
	
	@Override
	protected IUIPartMeta createUIMeta(PagePartMeta pm) {
		UIPartMeta uimeta = new UIPartMeta();
		UIViewPart widget = new UIViewPart();
		widget.setId(WIDGET_ID);
		UIPartMeta wuimeta = new UIPartMeta();
		wuimeta.setId(WIDGET_ID + "_meta");
		widget.setUimeta(wuimeta);
		constructViewUI(wuimeta);
		uimeta.setIsReference(1);
		uimeta.setElement(widget);
		uimeta.setFlowmode(false);
		return uimeta;
	}	
	
	private void constructViewUI(UIPartMeta wuimeta) {
		UIFlowvLayout flvLayout = new UIFlowvLayout();
		flvLayout.setId("flowv1");
		flvLayout.setViewId(WIDGET_ID);
		wuimeta.setElement(flvLayout);
		
		UIFlowvPanel flvPanel1 = new UIFlowvPanel();
		flvPanel1.setId("flowvp1");
		flvLayout.addPanel(flvPanel1);
		
		UITreeComp uiTree = new UITreeComp();
		uiTree.setViewId(WIDGET_ID);
		uiTree.setId(TREE);
		flvPanel1.setElement(uiTree);
		
		UIFlowvPanel flvPanel2 = new UIFlowvPanel();
		flvPanel2.setId("flowvp2");
		flvPanel2.setHeight("45");
		flvLayout.addPanel(flvPanel2);
		wuimeta.setElement(flvLayout);
		
		UIFlowhLayout flhLayout = new UIFlowhLayout();
		flhLayout.setId("flowh1");
		flhLayout.setViewId(WIDGET_ID);
		UIFlowhPanel flhP1 = new UIFlowhPanel();
		flhP1.setId("flowhp1");
		flhLayout.addPanel(flhP1);
		
		UIFlowhPanel flhP2 = new UIFlowhPanel();
		flhP2.setId("flowhp2");
		flhLayout.addPanel(flhP2);
		
		UIFlowhPanel flhP3 = new UIFlowhPanel();
		flhP3.setId("flowhp3");
		flhLayout.addPanel(flhP3);
		
		flvPanel2.setElement(flhLayout);
		flhP2.setWidth("84");
		flhP2.setTopPadding("12");		
		UIButton okbt = new UIButton();
		okbt.setViewId(WIDGET_ID);
		okbt.setId(OKBT);
		okbt.setWidth("74");
		okbt.setClassName("blue_button_div");
		flhP2.setElement(okbt);
		
		flhP3.setWidth("94");
		flhP3.setTopPadding("12");			
		UIButton cancelbt = new UIButton();
		cancelbt.setViewId(WIDGET_ID);
		cancelbt.setId(CANCELBT);
		cancelbt.setWidth("74");
		flhP3.setElement(cancelbt);
	}	
	
	protected void constructTree(TreeViewComp tree) {
		if(tree != null){
			tree.setShowRoot(false);
			tree.setExpand(true);
			tree.setOpenLevel(2);
			tree.setShowCheckBox(false);	
			tree.setCheckboxType(0);
		}
	}
}
