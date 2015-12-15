package xap.lui.psn.java;

import java.util.ArrayList;
import java.util.List;

import xap.lui.psn.cmd.LuiDelTabBaseDOCmd;

public class Model_DelTabBaseDOCmd implements ModelCmdConf<LuiDelTabBaseDOCmd> {
	@Override
	public Class<LuiDelTabBaseDOCmd> getLuiCmdClazz() {
		return LuiDelTabBaseDOCmd.class;
	}
	@Override
	public String getCmdTpl() {
		return "DeleteTabBaseDOTpl";
	}
	@Override
	public ExtAttrConf[] getExtAttrConf() {
		List<ExtAttrConf> list = new ArrayList<ExtAttrConf>();
		{
			ExtAttrConf conf = new ExtAttrConf();
			conf.setAttrId("TabID");
			conf.setAttrName("TabID");
			list.add(conf);
		
		}
		{
			ExtAttrConf conf = new ExtAttrConf();
			conf.setAttrId("OperatorDs");
			conf.setAttrName("操作数据集IDs");
			list.add(conf);
		}
		{
			ExtAttrConf conf = new ExtAttrConf();
			conf.setAttrId("MenuBarCompId");
			conf.setAttrName("菜单栏ID");
			list.add(conf);
		}
		{
			ExtAttrConf conf = new ExtAttrConf();
			conf.setAttrId("ToolBarCompId");
			conf.setAttrName("工具栏ID");
			list.add(conf);
		}
		return list.toArray(new ExtAttrConf[0]);
	}
}
