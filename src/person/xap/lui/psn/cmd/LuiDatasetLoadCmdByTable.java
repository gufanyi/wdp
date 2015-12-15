package xap.lui.psn.cmd;

import java.util.ArrayList;
import java.util.Map;

import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.Field;
import xap.lui.core.dataset.Row;
import xap.lui.core.exception.LuiBusinessException;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.model.ViewPartMeta;
import xap.mw.log.logging.Logger;



public class LuiDatasetLoadCmdByTable extends DatasetCmd {
	public static final String OPEN_BILL_ID = "openBillId";
	private String datasetId;
	
	public LuiDatasetLoadCmdByTable(String dsId) {
		this.datasetId = dsId;
	}
	
	
	public void execute() {
		ViewPartMeta widget = getLifeCycleContext().getViewContext().getView();
		Dataset ds = widget.getViewModels().getDataset(datasetId);
		String tableName = ds.getTableName();
		if(tableName == null)
			return;
		String pk = ds.getReqParameters().getParameterValue(OPEN_BILL_ID);
		if(pk != null){
			
		}
		try {
			String sql = "select * from " + tableName;
			if(pk != null){
				String primaryKey = null;
				Field[] fields = ds.getFields();
				for (int i = 0; i < fields.length; i++) {
					Field fr = fields[i];
					if(fr.isPK()){
						primaryKey = fr.getId();
						break;
					}
				}
				if(primaryKey != null)
					sql += " where " + primaryKey + "='" + pk + "'";
			}
			Object object  = queryVOs(null, sql);
			ArrayList<Object> list = (ArrayList<Object>) object;
			Row row = ds.getEmptyRow();
			Map<String, String> resuleMap = (Map<String, String>) list.get(0);
			for (int i = 0; i < ds.getFieldCount(); i++) {
				Field field = ds.getField(i);
				row.setValue(ds.nameToIndex(field.getId()), resuleMap.get(field.getId()));
			}
			postProcessRowSelect(ds);
		} 
		catch (LuiBusinessException e) {
			Logger.error(e.getMessage(), e);
			throw new LuiRuntimeException("查询对象出错," + e.getMessage() + ",ds id:" + ds.getId(),"查询过程出现错误");
		}
		ds.setEdit(false);
		updateButtons();
	}
 

}
