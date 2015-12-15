package xap.lui.psn.cmd;

import java.util.ArrayList;
import java.util.List;

import xap.lui.core.command.LuiCommand;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.DatasetRelation;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.core.state.ButtonStateManager;

public class LuiCancelCmd extends LuiCommand {

	private String masterDsId;
	public LuiCancelCmd(String masterDsId){
		this.masterDsId = masterDsId;
	}
	 
	public void execute() {
		boolean pageUndo = false;
		boolean widgetUndo = false;
		
		ViewPartMeta widget = getLifeCycleContext().getViewContext().getView();
		if(widget == null)
			throw new LuiRuntimeException("片段为空!");
		
		if(this.masterDsId == null)
			throw new LuiRuntimeException("未指定主数据集id!");
		Dataset masterDs = widget.getViewModels().getDataset(masterDsId);
		if(masterDs == null)
			throw new LuiRuntimeException("数据集为空,数据集id=" + masterDsId + "!");
		
		List<String> idList = new ArrayList<String>();
		idList.add(masterDsId);
//		if (masterDs.isControlwidgetopeStatus())
//			widgetUndo = true;
		
		if(widget.getViewModels().getDsrelations()!=null){
			DatasetRelation[] rels =  widget.getViewModels().getDsrelations().getDsRelations(masterDsId);
			if(rels != null){
				for (int i = 0; i < rels.length; i++) {
					String detailDsId = rels[i].getDetailDataset();
					Dataset detailDs = widget.getViewModels().getDataset(detailDsId);
					detailDs.setEdit(false);
					idList.add(detailDsId);
//					if (detailDs.isControlwidgetopeStatus())
//						widgetUndo = true;
				}
			}
		}
		for (int i = 0; i < idList.size(); i++) {
			getLifeCycleContext().getAppContext().addExecScript("pageUI.getWidget('" + widget.getId() + "').getDataset('" + idList.get(i) + "').undo();\n");
		}
		if (widgetUndo)
			getLifeCycleContext().getAppContext().addBeforeExecScript("pageUI.getWidget('" + widget.getId() + "').undo();\n");
		if (pageUndo)
			getLifeCycleContext().getAppContext().addBeforeExecScript("pageUI.undo();\n");
		
		masterDs.setEdit(false);
		updateButtons();
	}

	protected void updateButtons() {
		ButtonStateManager.updateButtons();
	}
	
	

}
