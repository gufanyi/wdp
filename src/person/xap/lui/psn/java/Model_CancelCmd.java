package xap.lui.psn.java;
import java.util.ArrayList;
import java.util.List;

import xap.lui.psn.cmd.LuiCancelCmd;
public class Model_CancelCmd implements ModelCmdConf<LuiCancelCmd> {
	@Override
	public Class<LuiCancelCmd> getLuiCmdClazz() {
		return LuiCancelCmd.class;
	}
	@Override
	public String getCmdTpl() {
		return "CancelTpl";	
	}
	@Override
	public ExtAttrConf[] getExtAttrConf() {
		List<ExtAttrConf> list = new ArrayList<ExtAttrConf>();
		{
			ExtAttrConf conf = new ExtAttrConf();
			conf.setAttrId("dsId");
			conf.setAttrName("操作数据集");
			list.add(conf);
		}
		return list.toArray(new ExtAttrConf[0]);
	}
}
