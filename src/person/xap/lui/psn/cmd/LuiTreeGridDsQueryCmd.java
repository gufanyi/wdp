package xap.lui.psn.cmd;

import xap.lui.core.model.LuiAppUtil;


public class LuiTreeGridDsQueryCmd extends QueryCmd{

	public LuiTreeGridDsQueryCmd(String viewId, String dsId, String whereSql) {
		super(viewId, dsId, whereSql);
	}
	public void excute(){
		LuiAppUtil.addAppAttr(LuiTreeGridDatasetLoadCmd.OPERATE_STATUS,LuiTreeGridDatasetLoadCmd.TREEGRID_DSQUERY_OPER);
		super.execute();
	}
}
