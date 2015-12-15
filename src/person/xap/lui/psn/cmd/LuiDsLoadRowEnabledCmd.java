package xap.lui.psn.cmd;

import xap.lui.core.dataset.Dataset;


public class LuiDsLoadRowEnabledCmd extends LuiDatasetLoadRowCmd {

	@Override
	protected void postProcessRowSelect(Dataset ds) {
		if(ds.getCurrentPageRowCount() > 0){
			ds.setRowSelectIndex(0);
		}
		ds.setEdit(true);
	}

	public LuiDsLoadRowEnabledCmd(String dsId, String billId) {
		super(dsId, billId);
	}

	@Override
	public void execute() {
		super.execute();
	}

}
