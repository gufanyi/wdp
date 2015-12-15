package xap.lui.psn.formula;

import xap.lui.core.builder.LuiSet;
import xap.lui.core.comps.GridComp;
import xap.lui.core.comps.TextAreaComp;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.Field;
import xap.lui.core.dataset.IRefDataset;
import xap.lui.core.dataset.Row;
import xap.lui.core.event.DatasetEvent;
import xap.lui.core.event.GridRowEvent;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.model.LuiAppUtil;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.core.xml.StringUtils;

public class DesignerFormulaController extends FormulaViewController {
	// 加载父窗口中所有dataset
	public void onDataLoad_ds(DatasetEvent e)  {
		initParams();
		if (this.sourceWidget != null) {
			ViewPartMeta view = getFormulaEditView();
			initDsData(view);
		}
	}

	/**
	 * 加载数据集下字段
	 * 
	 * @param datasetEvent
	 */
	public void onDatasetDsAfterSelect(DatasetEvent datasetEvent) {
		this.initParams();
		ViewPartMeta widget = getFormulaEditView();

		Dataset datasetds = datasetEvent.getSource();
		Row row = datasetds.getSelectedRow();

		if (sourceWidget == null)
			return;

		// 取得源ds
		Dataset ds = this.sourceWidget.getViewModels().getDataset(row.getString(datasetds.nameToIndex("id")));

		Dataset propertyds = widget.getViewModels().getDataset("propertyds");
		propertyds.clear();
		Field[] fields = ds.getFields();
		if (fields != null && fields.length > 0) {
			for (int i = 0; i < fields.length; i++) {
				Row tmprow = propertyds.getEmptyRow();
				tmprow.setValue(propertyds.nameToIndex("id"), fields[i].getId());
				String text = fields[i].getText();
				tmprow.setValue(propertyds.nameToIndex("description"), "获取选中行" + text + "列的值。");
				tmprow.setValue(propertyds.nameToIndex("text"), text);
				propertyds.addRow(tmprow);
			}
		}
	}
	
	// 数据集属性区双击事件
	public void onPropertyGirdDbClick(GridRowEvent e) {
		this.initParams();
		GridComp grid = e.getSource();
		Dataset ds = LuiAppUtil.getDataset(grid.getDataset());
		ViewPartMeta view = getFormulaEditView();
		Row sRow = ds.getSelectedRow();
		String formula = "";
		if (sRow != null) {
			String fieldId = sRow.getString(ds.nameToIndex("id"));
			Dataset dsDs = LuiAppUtil.getDataset("datasetds");// 数据集列表的ds
			Row dsSelRow = dsDs.getSelectedRow();
			String dsId = dsSelRow.getString(dsDs.nameToIndex("id"));// 选中的数据集id

			formula = "Dataset ds = LuiAppUtil.getDataset(\"" + dsId + "\");\n";
			formula = formula + "Row selRow = ds.getSelectedRow();\nif(selRow != null)\n";
			formula = formula + "  String " + fieldId + "_value = selRow.getString(ds.nameToIndex(\"" + fieldId + "\"));\n";
		}
		setEditAreaValue(formula);
	}
	
	//设置描述
	public void setDescription2(GridRowEvent e){
		GridComp grid = e.getSource();
		Dataset ds = LuiAppUtil.getDataset(grid.getDataset());
		Row sRow = ds.getSelectedRow();
		String description = "";
		if(sRow != null){
			description = sRow.getString(ds.nameToIndex("description"));
			setDescAreaValue(description);
		}
	}
	
	//控件表格双击事件
	public void onCtrlGirdDbClick(GridRowEvent gridRowEvent) {
		initParams();
		GridComp ctrlComp = gridRowEvent.getSource();
		String dsId = ctrlComp.getDataset();
		ViewPartMeta view = getFormulaEditView();
		Dataset ds = view.getViewModels().getDataset(dsId);
		Row selRow = ds.getSelectedRow();
		String funcId = selRow.getString(ds.nameToIndex("id"));
		if(HasChild(ds,funcId)){
			throw new LuiRuntimeException("请选中叶子节点行！");
		}
		TextAreaComp textArea = getEditTextArea(view,"edit_textarea");
		String oldvalue = textArea.getValue();
		String funvalue = selRow.getString(ds.nameToIndex("demo"))+"\n";
		textArea.setValue(oldvalue + funvalue);
	}
	

	public void onPropertyGridSelect(GridRowEvent gridRowEvent) {

	}
	
	private boolean HasChild(Dataset ds, String ctrlId) {
		Row[] rows = ds.getCurrentPageData().getRows();
		if(rows != null && rows.length > 0){
			int pidIndex = ds.nameToIndex("pid");
			for(Row row : rows){
				String pid = row.getString(pidIndex);
				if(StringUtils.equals(ctrlId, pid))
					return true;
			}
		}
		return false;
	}
	/**
	 * 加载父窗口中所有dataset
	 * 
	 * @param editorview
	 */
	private void initDsData(ViewPartMeta editorview) {
		Dataset datasetds = editorview.getViewModels().getDataset("datasetds");
		int idIndex = datasetds.nameToIndex("id");
		int descIndex = datasetds.nameToIndex("description");
		if(sourceWidget == null)
			return;
		LuiSet<Dataset> dsList = sourceWidget.getViewModels().getDatasetsList();
		if(dsList != null && dsList.size() > 0){
			for(Dataset ds : dsList){
				if (ds instanceof IRefDataset)
					continue;
				Row row = datasetds.getEmptyRow();
				row.setValue(idIndex, ds.getId());
				row.setValue(descIndex, ds.getCaption());
				datasetds.addRow(row);
			}
			datasetds.setSelectedIndex(0);
		}
	}

}
