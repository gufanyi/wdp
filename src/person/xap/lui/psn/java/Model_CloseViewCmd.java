package xap.lui.psn.java;
import java.util.ArrayList;
import java.util.List;

import xap.lui.psn.cmd.LuiCloseViewCmd;
public class Model_CloseViewCmd implements ModelCmdConf<LuiCloseViewCmd> {
	@Override
	public Class<LuiCloseViewCmd> getLuiCmdClazz() {
		return LuiCloseViewCmd.class;
	}
	@Override
	public String getCmdTpl() {
		return "CloseViewTpl";
	}
	@Override
	public ExtAttrConf[] getExtAttrConf() {
		List<ExtAttrConf> list = new ArrayList<ExtAttrConf>();
		{
			ExtAttrConf conf = new ExtAttrConf();
			conf.setAttrId("viewId");
			conf.setAttrName("操作窗口");
			list.add(conf);
		}
		return list.toArray(new ExtAttrConf[0]);
	}
}
