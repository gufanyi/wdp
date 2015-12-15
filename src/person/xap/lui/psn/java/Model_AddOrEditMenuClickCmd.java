package xap.lui.psn.java;

import java.util.ArrayList;
import java.util.List;

import xap.lui.psn.cmd.LuiAddOrEditMenuClickCmd;

public class Model_AddOrEditMenuClickCmd implements ModelCmdConf<LuiAddOrEditMenuClickCmd> {
	@Override
	public Class<LuiAddOrEditMenuClickCmd> getLuiCmdClazz() {
		return LuiAddOrEditMenuClickCmd.class;
	}
	@Override
	public String getCmdTpl() {
		return "AddOrEditTpl";
	}
	@Override
	public ExtAttrConf[] getExtAttrConf() {
		List<ExtAttrConf> list = new ArrayList<ExtAttrConf>();
		{
			ExtAttrConf conf = new ExtAttrConf();
			conf.setAttrId("TabID");
			conf.setAttrName("页签Id");
			list.add(conf);
		}
		{
			ExtAttrConf conf = new ExtAttrConf();
			conf.setAttrId("OperatorDs");
			conf.setAttrName("操作数据集");
			list.add(conf);
		}
		{
			ExtAttrConf conf = new ExtAttrConf();
			conf.setAttrId("OperType");
			conf.setAttrName("操作类型");
			list.add(conf);
		}
		{
			ExtAttrConf conf = new ExtAttrConf();
			conf.setAttrId("EditViewId");
			conf.setAttrName("编辑窗口ID");
			list.add(conf);
		}
		return list.toArray(new ExtAttrConf[0]);
	}
}
