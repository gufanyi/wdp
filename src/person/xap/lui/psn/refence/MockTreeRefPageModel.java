package xap.lui.psn.refence;

import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.comps.TreeViewComp;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.LuiParameter;
import xap.lui.core.event.AppRequestProcessor;
import xap.lui.core.event.DatasetEvent;
import xap.lui.core.event.TreeNodeEvent;
import xap.lui.core.listener.LuiEventConf;
import xap.lui.core.mock.MockTreePageModel;
import xap.lui.core.model.PagePartMeta;
import xap.lui.core.model.ViewPartMeta;

/**
 * 开发工具使用的树形参照
 * @author licza
 *
 */
public class MockTreeRefPageModel extends MockTreePageModel {
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
			tree.setEdit(true);
			
			LuiEventConf event = new LuiEventConf();
			event.setEventName(TreeNodeEvent.ON_DBCLICK);
			event.setMethod("onTreeDbClick");
			event.setEventType(TreeNodeEvent.class.getSimpleName());
			LuiParameter param = new LuiParameter();
			param.setName("treeNodeEvent");
			event.addParam(param);		
			tree.addEventConf(event);
		}
	}

	@Override
	protected void constructDataset(Dataset ds) {
		super.constructDataset(ds);
		LuiEventConf event = new LuiEventConf();
		event.setEventName(DatasetEvent.ON_AFTER_ROW_SELECT);
		event.setMethod("onAfterRowSelect");
		event.setEventType(DatasetEvent.class.getSimpleName());
		ds.addEventConf(event);
	}
	
}
