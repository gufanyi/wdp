package xap.lui.psn.refence;

import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.comps.TreeViewComp;
import xap.lui.core.event.AppRequestProcessor;
import xap.lui.core.mock.MockTreeGridPageModel;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.core.model.PagePartMeta;

/**
 * 开发工具使用的树形参照
 * @author licza
 *
 */
public class MockRefPageModel extends MockTreeGridPageModel {
	protected void constructWidget(ViewPartMeta widget) {
		String ctrl = LuiRuntimeContext.getWebContext().getParameter("ctrl");
		if(ctrl != null)
			widget.setController(ctrl);
	}
	
	@Override
	protected PagePartMeta createPageMeta() {
		PagePartMeta pm =  super.createPageMeta();		
		pm.setProcessorClazz(AppRequestProcessor.class.getName());
		return pm;
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
