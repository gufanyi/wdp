package xap.lui.psn.java;
import java.util.ArrayList;
import java.util.List;

import xap.lui.psn.cmd.LuiSaveCmd;
public class Model_SaveCmd implements ModelCmdConf<LuiSaveCmd> {
	@Override
	public Class<LuiSaveCmd> getLuiCmdClazz() {
		return LuiSaveCmd.class;
	}
	@Override
	public String getCmdTpl() {
		return "SaveTpl";
	}
	@Override
	public ExtAttrConf[] getExtAttrConf() {
		List<ExtAttrConf> list = new ArrayList<ExtAttrConf>();
		{
			ExtAttrConf conf = new ExtAttrConf();
			conf.setAttrId("OperatorDs");
			conf.setAttrName("操作数据集");
			list.add(conf);
		}
		return list.toArray(new ExtAttrConf[0]);
	}
}
