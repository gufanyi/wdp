package xap.lui.psn.java;

import java.util.ArrayList;
import java.util.List;

import xap.lui.psn.cmd.LuiAddOrEditBeforeShowCmd;

public class Model_AddOrEditBeforeShow implements ModelCmdConf<LuiAddOrEditBeforeShowCmd>{
	@Override
	public Class<LuiAddOrEditBeforeShowCmd> getLuiCmdClazz() {
		return LuiAddOrEditBeforeShowCmd.class;
	}
	@Override
	public String getCmdTpl() {
		return "AddOrEditBeforeShowTpl";
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
