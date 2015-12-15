package xap.lui.psn.cmd;

import xap.lui.core.dataset.Dataset;
import xap.lui.core.device.IBodyInfo;


public class LuiLineAddCmd extends LuiLineInsertCmd {
	public LuiLineAddCmd(IBodyInfo bodyInfo) {
		super(bodyInfo);
	}
	protected int getInsertIndex(Dataset ds) {
		return ds.getCurrentPageRowCount();
	}

}
