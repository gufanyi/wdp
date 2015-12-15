package xap.lui.psn.commitrule;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import xap.lui.core.builder.LuiSet;
import xap.lui.core.cache.PaCache;
import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.common.LuiWebSession;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.RefMdDataset;
import xap.lui.core.dataset.Row;
import xap.lui.core.event.DatasetEvent;
import xap.lui.core.event.MouseEvent;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.mock.MockTreeViewController;
import xap.lui.core.model.AppSession;
import xap.lui.core.model.LuiPlugoutCmd;
import xap.lui.core.model.PagePartMeta;
import xap.lui.core.model.ViewPartMeta;

/**
 * 
 * 节点控制类，即事件触发等均由此类控制
 * 需要重写MockTreeViewCtroller类中的方法
 * 
 */
@SuppressWarnings("rawtypes")
public class DatasetlistRefController extends MockTreeViewController {
	
	private static final String ID = "id";
	private static final String MASTER_DS = "masterDs";
	private static final String DATASET = "Dataset";
	private static final String TYPE = "type";
	private static final String MAIN = "main";
	private static final String REF_OK_PLUGOUT = "refOkPlugout";
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
		row.setValue(2, "数据集");
		ds.addRow(row);
		
		LuiWebSession session = LuiRuntimeContext.getWebContext().getPageWebSession();
		// 获取orginParamMap的参数
		String viewId = session.getOriginalParameter("sourceView");
		String pageId = session.getOriginalParameter("sourceWinId");
		String isParent = session.getOriginalParameter("isParent");
		PagePartMeta pagemeta = null;
		if(StringUtils.equals(isParent, "false")){
			pagemeta = PaCache.getEditorPagePartMeta();
		}else{
			//pagemeta = PagePartMetaBuilder.createPageMeta(pageId);
		}
		ViewPartMeta viewPartMeta = pagemeta.getWidget(viewId);
		if(viewPartMeta == null)
			throw new LuiRuntimeException("请先选择视图！");
		LuiSet<Dataset> datasets = viewPartMeta.getViewModels().getDatasetsList();
		for (Dataset dataset : datasets) {
			if (dataset instanceof RefMdDataset) {
				continue;
			}
			Row empRow = ds.getEmptyRow();
			empRow.setValue(0, dataset.getId());
			empRow.setValue(1, "main");
			empRow.setValue(2, dataset.getId());
			ds.addRow(empRow);	
		}
	}
	
	public void onAfterRowSelect(DatasetEvent e){
	}	
	
	@Override
	public void okButtonClick(MouseEvent e) {
		LuiWebSession session = LuiRuntimeContext.getWebContext().getPageWebSession();
		String writeDs = session.getOriginalParameter("writeDs");
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
		valueMap.put("datasetId", value);

		paramMap.put(TYPE, DATASET);
		paramMap.put(ID, writeDs);
		paramMap.put(WRITE_FIELDS,valueMap);
		LuiPlugoutCmd uifPluOutCmd = new LuiPlugoutCmd(MAIN,REF_OK_PLUGOUT,paramMap);
		uifPluOutCmd.execute();		
	}
}
