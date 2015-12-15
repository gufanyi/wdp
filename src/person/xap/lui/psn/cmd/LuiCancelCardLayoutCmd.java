package xap.lui.psn.cmd;

import java.util.ArrayList;
import java.util.List;

import xap.lui.core.command.LuiCommand;
import xap.lui.core.comps.ToolBarComp;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.DatasetRelation;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.layout.UICardLayout;
import xap.lui.core.model.ViewPartContext;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.core.state.ButtonStateManager;
import xap.mw.core.data.DOStatus;

public class LuiCancelCardLayoutCmd extends LuiCommand {

	private String masterDsId;
	private String cardLayoutId;
	private ToolBarComp toolBarComp;
	
	public LuiCancelCardLayoutCmd(String masterDsId,String cardLayoutId){
		this.masterDsId = masterDsId;
		this.cardLayoutId = cardLayoutId;
	}
	public LuiCancelCardLayoutCmd(String masterDsId,String cardLayoutId,ToolBarComp toolBarComp){
		this.masterDsId = masterDsId;
		this.cardLayoutId = cardLayoutId;
		this.toolBarComp=toolBarComp;
	}
	 
	public void execute() {
		ViewPartContext   viewPartContext = getLifeCycleContext().getViewContext();
		ViewPartMeta widget = viewPartContext.getView();
		if(widget == null)
			throw new LuiRuntimeException("片段为空!");
		if(this.masterDsId == null)
			throw new LuiRuntimeException("未指定主数据集id!");
		Dataset masterDs = widget.getViewModels().getDataset(masterDsId);
		if(masterDs == null)
			throw new LuiRuntimeException("数据集为空,数据集id=" + masterDsId + "!");
		UICardLayout cardLayout = (UICardLayout) viewPartContext.getUIMeta().findChildById(this.cardLayoutId);
		
		if (this.cardLayoutId == null)
			throw new LuiRuntimeException("未指定卡片布局id");
		if (cardLayout == null)
			throw new LuiRuntimeException("卡片不存在,id=" + cardLayoutId + "!");
		cardLayout.setCurrentItem("0");
		List<String> idList = new ArrayList<String>();
		idList.add(masterDsId);
		if(masterDs.getSelectedRow().getState()==DOStatus.NEW){
			masterDs.removeRow(0);
		}else{
			masterDs.getSelectedRow().setState(DOStatus.UNCHANGED);
		}
		masterDs.setEdit(false);	
		if(widget.getViewModels().getDsrelations()!=null){
			DatasetRelation[] rels =  widget.getViewModels().getDsrelations().getDsRelations(masterDsId);
			if(rels != null){
				for (int i = 0; i < rels.length; i++) {
					String detailDsId = rels[i].getDetailDataset();
					Dataset detailDs = widget.getViewModels().getDataset(detailDsId);
					detailDs.setEdit(false);
					idList.add(detailDsId);
				}
			}
		}
		if(this.toolBarComp!=null)
			new ToolBarItemStatusCtrl(this.toolBarComp,"cancel","card");
//		for (int i = 0; i < idList.size(); i++) {
//			getLifeCycleContext().getAppContext().addExecScript("pageUI.getWidget('" + widget.getId() + "').getDataset('" + idList.get(i) + "').undo();\n");
//		}
//		if (widgetUndo)
//			getLifeCycleContext().getAppContext().addBeforeExecScript("pageUI.getWidget('" + widget.getId() + "').undo();\n");
//		if (pageUndo)
//			getLifeCycleContext().getAppContext().addBeforeExecScript("pageUI.undo();\n");
//		
//		updateButtons();
	}

	protected void updateButtons() {
		ButtonStateManager.updateButtons();
	}
	
	

}
