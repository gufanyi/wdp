package xap.lui.psn.cmd;

import xap.lui.core.command.LuiCommand;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.Row;
import xap.lui.core.model.ViewPartContext;


public class LuiDatasetEnableCmd extends LuiCommand {
	private String datasetId;
	public LuiDatasetEnableCmd(String dsId){
		datasetId = dsId;
	}
	@Override
	public void execute() {
		ViewPartContext widgetctx = getLifeCycleContext().getViewContext();
		Dataset ds = widgetctx.getView().getViewModels().getDataset(datasetId);
		Row row = ds.getEmptyRow();
		processRow(ds, row);
		ds.addRow(row);
		ds.setRowSelectIndex(ds.getCurrentPageRowCount() - 1);
		ds.setEdit(true);
	}
	
	
	protected void processRow(Dataset ds, Row row) {
		
	}

}
