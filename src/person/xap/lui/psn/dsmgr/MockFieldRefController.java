package xap.lui.psn.dsmgr;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import xap.lui.core.cache.PaCache;
import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.common.LuiWebSession;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.Field;
import xap.lui.core.dataset.Row;
import xap.lui.core.event.DatasetEvent;
import xap.lui.core.event.MouseEvent;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.mock.MockTreeViewController;
import xap.lui.core.model.AppSession;
import xap.lui.core.model.LuiPlugoutCmd;
import xap.lui.core.model.ViewPartMeta;


@SuppressWarnings("rawtypes")
public class MockFieldRefController extends MockTreeViewController {
	
	private static final String ID = "id";
	private static final String MASTER_DS = "masterDs";
	private static final String DATASET = "Dataset";
	private static final String TYPE = "type";
	private static final String MAIN = "main";
	private static final String REF_OK_PLUGOUT = "refOkPlugout";
	private static final String WRITE_DSNAME = "relationsds";
	private static final String WRITE_FIELDS = "writeFields";	
	
	@Override
	public void cancelButtonClick(MouseEvent e) {
		super.cancelButtonClick(e);
	}
	
	@Override
	@SuppressWarnings("restriction")
	public void dataLoad(DatasetEvent e) {
		Dataset ds = e.getSource();
		ds.clear();
		Row row = ds.getEmptyRow();
		row.setValue(0, "main");
		row.setValue(1, "");
		row.setValue(2, "字段");
		ds.addRow(row);
		
		LuiWebSession session = LuiRuntimeContext.getWebContext().getPageWebSession();
		// 获取orginParamMap的参数
		String viewId = session.getOriginalParameter("sourceView");
		
		
		
		String currentDsId = session.getOriginalParameter("currentDsId");
		//List<DataItem> list = new ArrayList<DataItem>();
		if(currentDsId!=null && !"null".equals(currentDsId)){
			ViewPartMeta sourceWidget = null;
			// 源window
			Dataset sourceDateset = null;		
			// 源widget
			if (viewId != null) {
				sourceWidget = PaCache.getEditorViewPartMeta();
			}	
			// 源ds
			if (sourceWidget != null) {
				sourceDateset = sourceWidget.getViewModels().getDataset(currentDsId);
			}
			List<Field> fields = sourceDateset.getFieldList();
			for (Field field : fields) {
				//DataItem item = new DataItem();
				String fid = field.getId();
				String fname = field.getText();
				if(!"Ds".equals(fid) && !"Status".equals(fid) && !"Sv".equals(fid) && !(fid.startsWith("vdef"))){
					//item.setText(fname);
					//item.setValue(fid);
					//list.add(item);
				    if(fid.endsWith("_name")){
				    	continue;
				    }
					Row fieldRow = ds.getEmptyRow();
					fieldRow.setValue(0, fid);
					fieldRow.setValue(1, "main");
					fieldRow.setValue(2, fid);
					ds.addRow(fieldRow);			
				}
			}			
		}
	}
	
	public void onAfterRowSelect(DatasetEvent e){
	}	
	
	@Override
	public void okButtonClick(MouseEvent e) {
		ViewPartMeta widget = AppSession.current().getViewContext().getView();
		Dataset currentDs =  widget.getViewModels().getDataset(MASTER_DS);
		Row currRow = null;
		currRow = currentDs.getSelectedRow();
		if(currRow==null){
			throw new LuiRuntimeException("请选择字段!");
		}		
		String value =  currRow.getString(0);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		Map<String, String> valueMap = new HashMap<String, String>();
		valueMap.put("detailkey", value);
		LuiWebSession session = LuiRuntimeContext.getWebContext().getPageWebSession();
		String writeDs = session.getOriginalParameter("writeDs");
		paramMap.put(TYPE, DATASET);
		paramMap.put(ID, writeDs);
		paramMap.put(WRITE_FIELDS,valueMap);
		LuiPlugoutCmd uifPluOutCmd = new LuiPlugoutCmd(MAIN,REF_OK_PLUGOUT,paramMap);
		uifPluOutCmd.execute();		
	}	
}
