package xap.lui.psn.java;
import java.util.ArrayList;
import java.util.List;

import xap.lui.psn.cmd.LuiEditRowCmd;
public class Model_EditRowCmd implements ModelCmdConf<LuiEditRowCmd> {
	@Override
	public Class<LuiEditRowCmd> getLuiCmdClazz() {
		return LuiEditRowCmd.class;
	}
	@Override
	public String getCmdTpl() {
		return "EditRowTpl";
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
		{
			ExtAttrConf conf = new ExtAttrConf();
			conf.setAttrId("TabID");
			conf.setAttrName("TAB页ID");
			list.add(conf);
			
		}
		{
			ExtAttrConf conf = new ExtAttrConf();
			conf.setAttrId("ItemDS1");
			conf.setAttrName("Tab页1对应的数据集");
			list.add(conf);
			
		}
		{
			ExtAttrConf conf = new ExtAttrConf();
			conf.setAttrId("ItemDS2");
			conf.setAttrName("Tab页2对应的数据集");
			list.add(conf);
			
		}
		{
			ExtAttrConf conf = new ExtAttrConf();
			conf.setAttrId("ItemDS3");
			conf.setAttrName("Tab页3对应的数据集");
			list.add(conf);
			
		}
		{
			ExtAttrConf conf = new ExtAttrConf();
			conf.setAttrId("ToolBarCompId");
			conf.setAttrName("工具栏ID");
			list.add(conf);
		}
		{
			ExtAttrConf conf = new ExtAttrConf();
			conf.setAttrId("MenuBarCompId");
			conf.setAttrName("菜单栏ID");
			list.add(conf);
		}
		return list.toArray(new ExtAttrConf[0]);
	}
}
