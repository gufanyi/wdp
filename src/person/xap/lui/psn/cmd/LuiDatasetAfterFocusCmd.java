package xap.lui.psn.cmd;

import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.Row;

public class LuiDatasetAfterFocusCmd extends LuiDatasetAfterSelectCmd {

	public LuiDatasetAfterFocusCmd(String dsId) {
		super(dsId);
	}
	
	@Override
	protected Row getMasterRow(Dataset masterDs) {
		return masterDs.getFocusRow();
	}

}
