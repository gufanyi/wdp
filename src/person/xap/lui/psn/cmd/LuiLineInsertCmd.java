package xap.lui.psn.cmd;

import xap.lui.core.command.LuiCommand;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.DatasetRelation;
import xap.lui.core.dataset.Field;
import xap.lui.core.dataset.Row;
import xap.lui.core.device.IBodyInfo;
import xap.lui.core.device.MultiBodyInfo;
import xap.lui.core.device.SingleBodyInfo;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.model.ViewPartContext;


public class LuiLineInsertCmd extends LuiCommand {
	private IBodyInfo bodyInfo;
	public LuiLineInsertCmd(IBodyInfo bodyInfo) {
		this.bodyInfo = bodyInfo;
	}

	public void execute() {
		ViewPartContext widgetctx = getLifeCycleContext().getViewContext();
		
		String dsId = getSlaveDataset(widgetctx);
		if(dsId == null)
			throw new LuiRuntimeException("没有获得当前增行数据集");
		
		Dataset ds = widgetctx.getView().getViewModels().getDataset(dsId);
		
		Row row = ds.getEmptyRow();
		processRow(ds, row);
		
		Integer index = getInsertIndex(ds);
		if(index.intValue() >= 1){
			Row row1 = ds.getCurrentPageData().getRow(index - 1);
			if(row1 != null){
				Field[] fields = ds.getFields();
				for (Field field : fields) {
					if(field.isLock()){
						String value = (String) row1.getValue(ds.nameToIndex(field.getId()));
						if(value == null)
							continue;
						row.setValue(ds.nameToIndex(field.getId()), value);
					}
				}
			}
		}
		
		ds.insertRow(index, row);
		ds.setEdit(true);
		ds.setRowSelectIndex(index);
	}
	
	protected int getInsertIndex(Dataset ds) {
		Integer index = ds.getSelectedIndex();
		if(index == -1)
			index = ds.getCurrentPageRowCount();
		return index;
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

	protected String getKeyValue(ViewPartContext widgetCtx, String dsId) {
		String keyValue = Dataset.MASTER_KEY;
		String masterDsId = widgetCtx.getView().getViewModels().getDsrelations().getMasterDsByDetailDs(dsId);
		if(masterDsId != null){
			DatasetRelation dr = widgetCtx.getView().getViewModels().getDsrelations().getDsRelation(masterDsId, dsId);
			Dataset masterDs = widgetCtx.getView().getViewModels().getDataset(masterDsId);
			Row masterSelRow = masterDs.getSelectedRow();
			if(masterSelRow == null)
				throw new LuiRuntimeException("主表未选中行");
			String masterKeyField = dr.getMasterKeyField();
			keyValue = (String) masterSelRow.getValue(masterDs.nameToIndex(masterKeyField));
			if(keyValue == null || keyValue.equals(""))
				keyValue = masterSelRow.getRowId();
		}
		return keyValue;
	}

	protected void processRow(Dataset ds, Row row) {
	}
}
