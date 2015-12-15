package xap.lui.psn.cmd;

import java.util.UUID;

import xap.lui.core.command.LuiCommand;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.Row;
import xap.lui.core.device.IBodyInfo;
import xap.lui.core.device.MultiBodyInfo;
import xap.lui.core.device.SingleBodyInfo;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.model.ViewPartContext;



public class LuiLinePasteCmd extends LuiCommand { 
	private IBodyInfo bodyInfo;
	public LuiLinePasteCmd(IBodyInfo bodyInfo) {
		this.bodyInfo = bodyInfo;
	}
	
	public void execute() {
		ViewPartContext widgetCtx = getLifeCycleContext().getViewContext();
		
		String dsId = getSlaveDataset(widgetCtx);
		Dataset ds = widgetCtx.getView().getViewModels().getDataset(dsId);
		
		Row copyRow = (Row) getLifeCycleContext().getAppContext().getAppAttribute("$copyRow");
		if(copyRow == null)
			throw new LuiRuntimeException("请先复制行!");
		copyRow.setRowId(UUID.randomUUID().toString());
		ds.addRow(copyRow);
	}

	
	protected String getSlaveDataset(ViewPartContext widgetCtx) {
		String dsId = null;
		if(bodyInfo != null){
			if(bodyInfo instanceof MultiBodyInfo){
				MultiBodyInfo mbi = (MultiBodyInfo) bodyInfo;
			}
			else{
				SingleBodyInfo sbi = (SingleBodyInfo) bodyInfo;
				dsId = sbi.getBodyDataset();
			}
		}
		if(dsId == null)
			throw new LuiRuntimeException("没有找到待操作数据集,请确认配置正确", "没有找到待操作数据集");
		return dsId;
	}
}
