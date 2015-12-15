package xap.lui.psn.cmd;

import xap.lui.core.command.CmdInvoker;
import xap.lui.core.dataset.Dataset;



public class LuiSaveAddCmd extends LuiSaveAggCmd {
	public LuiSaveAddCmd(String masterDsId,String[] detailDsIds, String aggVoClazz) {
		super(masterDsId, detailDsIds, aggVoClazz);
	}
	
	public LuiSaveAddCmd(String masterDsId,String[] detailDsIds, String aggVoClazz, boolean bodyNotNull) {
		super(masterDsId, detailDsIds, aggVoClazz, bodyNotNull,"",null);
	}

	@Override
	protected void onAfterSave(Dataset masterDs, Dataset[] detailDss) {
		super.onAfterSave(masterDs, detailDss);
		CmdInvoker.invoke(new LuiDatasetEnableCmd(masterDs.getId()));
	}
}
