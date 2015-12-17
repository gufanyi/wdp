package xap.lui.psn.cmd;

import xap.lui.core.command.LuiCommand;
import xap.lui.core.common.InteractionUtil;
import xap.lui.core.comps.ToolBarComp;
import xap.lui.core.dao.CRUDHelper;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.Row;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.logger.LuiLogger;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.core.serializer.Dataset2SuperVOSerializer;
import xap.mw.core.data.BaseDO;


public class LuiDelBaseDOCmd extends LuiCommand{
	private String masterDsId;
	private ToolBarComp toolBarComp;
	
	public LuiDelBaseDOCmd(String masterDsId){
		this.masterDsId = masterDsId;
	}
	public LuiDelBaseDOCmd(String masterDsId,ToolBarComp toolBarComp){
		this.masterDsId = masterDsId;
		this.toolBarComp=toolBarComp;		
	}
	
	public void execute() {
		InteractionUtil.showConfirmDialog("确认对话框", "确认删除吗?");
		if (InteractionUtil.getConfirmDialogResult().equals(Boolean.FALSE))
			return;
		ViewPartMeta widget = getLifeCycleContext().getViewContext().getView();
		if(widget == null)
			throw new LuiRuntimeException("片段为空!");
		if(this.masterDsId == null)
			throw new LuiRuntimeException("未指定数据集id!");
		Dataset masterDs = widget.getViewModels().getDataset(masterDsId);
		if(masterDs == null)
			throw new LuiRuntimeException("数据集为空,数据集id=" + masterDsId + "!");
		Row[] delRows = masterDs.getCurrentPageSelectedRows();
		if(delRows != null && delRows.length > 0){
			Dataset2SuperVOSerializer ser = new Dataset2SuperVOSerializer();
			BaseDO[] superVOs = ser.serialize(masterDs, delRows);
			onDeleteVO(superVOs);
			for (int j = 0; j < delRows.length; j++) {
				masterDs.removeRow(masterDs.getRowIndex(delRows[j]));
			}
		
		}
		updateButtons();
		if(this.toolBarComp!=null)
			new ToolBarItemStatusCtrl(this.toolBarComp,"delete","");
	}
	
	protected void onDeleteVO(BaseDO[] vos){
		try {
			CRUDHelper.getCRUDService().deleteBeans(vos);
		} catch (Exception e) {
			LuiLogger.error(e.getMessage(), e);
			throw new LuiRuntimeException(e);
		}
	}
}


