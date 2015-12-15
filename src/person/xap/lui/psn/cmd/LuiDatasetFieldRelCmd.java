package xap.lui.psn.cmd;

import xap.lui.core.command.LuiCommand;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.json.Dataset2JsonSerializer;
import xap.lui.core.model.ViewPartMeta;


public class LuiDatasetFieldRelCmd extends LuiCommand{

	
	private String datasetId;
	
	public LuiDatasetFieldRelCmd(String dsId) {
		this.datasetId = dsId;
	}
	@Override
	public void execute() {
		ViewPartMeta widget = getLifeCycleContext().getViewContext().getView();
		Dataset ds = widget.getViewModels().getDataset(datasetId);
		new Dataset2JsonSerializer(ds).processFieldRelation(ds, ds.getFieldRelations().getFieldRelations());
	}

	
	
	
}
