package xap.lui.psn.java;

import java.util.ArrayList;
import java.util.List;

import xap.lui.psn.cmd.LuiEditTabCardLayoutCmd;

public class Model_EditTabCardLayoutCmd implements ModelCmdConf<LuiEditTabCardLayoutCmd>  {
	@Override
	public Class<LuiEditTabCardLayoutCmd> getLuiCmdClazz() {
		return LuiEditTabCardLayoutCmd.class;
	}
	@Override
	public String getCmdTpl() {
		return "EditTabCardLayoutTpl";
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
			conf.setAttrId("CardLayoutIds");
			conf.setAttrName("卡片ID");
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

