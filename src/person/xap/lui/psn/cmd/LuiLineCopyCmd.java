package xap.lui.psn.cmd;

import java.util.UUID;

import xap.lui.core.command.LuiCommand;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.Field;
import xap.lui.core.dataset.Row;
import xap.lui.core.device.IBodyInfo;
import xap.lui.core.device.MultiBodyInfo;
import xap.lui.core.device.SingleBodyInfo;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.model.ViewPartContext;



public class LuiLineCopyCmd extends LuiCommand {
	private IBodyInfo bodyInfo;
	public LuiLineCopyCmd(IBodyInfo bodyInfo) {
		this.bodyInfo = bodyInfo;
	}
	
	public void execute() {
		ViewPartContext widgetctx = getLifeCycleContext().getViewContext();
		String dsId = getSlaveDataset(widgetctx);
		Dataset ds = widgetctx.getView().getViewModels().getDataset(dsId);
		Row row = ds.getSelectedRow();
		if(row == null)
			throw new LuiRuntimeException("请选则要复制的行!");
		
		Row corpyRow = (Row) row.clone();
		Field primaryField = null;
		for(int i = 0, count = ds.getFieldCount(); i < count; i ++)
		{
			if(ds.getField(i).isPK())
			{
				primaryField = ds.getField(i);
				break;
			}
		}
		if(primaryField == null)
			throw new LuiRuntimeException("数据集" + ds.getId() +"没有配置主键字段!");
		corpyRow.setValue(ds.nameToIndex(primaryField.getId()), null);
		corpyRow.setRowId(row.getRowId() + UUID.randomUUID());
		getLifeCycleContext().getAppContext().addAppAttribute("$copyRow", corpyRow);
	}

	
	
	protected String getSlaveDataset(ViewPartContext widgetCtx) {
		String dsId = null;
		if(bodyInfo != null){
			if(bodyInfo instanceof MultiBodyInfo){
				MultiBodyInfo mbi = (MultiBodyInfo) bodyInfo;
				String bodyTabId = mbi.getBodyTabId();
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
