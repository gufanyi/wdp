package xap.lui.psn.java;
import java.util.ArrayList;
import java.util.List;

import xap.lui.psn.cmd.LuiAddRowCmd;
public class Model_AddRowCmd implements ModelCmdConf<LuiAddRowCmd> {
	@Override
	public Class<LuiAddRowCmd> getLuiCmdClazz() {
		return LuiAddRowCmd.class;
	}
	@Override
	public String getCmdTpl() {
		return "AddRowTpl";
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
		{
			ExtAttrConf conf = new ExtAttrConf();
			conf.setAttrId("NavDsId");
			conf.setAttrName("导航数据集");
			list.add(conf);
			
		}
		{
			ExtAttrConf conf = new ExtAttrConf();
			conf.setAttrId("TabID");
			conf.setAttrName("页签ID");
			list.add(conf);
			
		}
		{
			ExtAttrConf conf = new ExtAttrConf();
			conf.setAttrId("ItemDS1");
			conf.setAttrName("页签1数据集");
			list.add(conf);
			
		}
		{
			ExtAttrConf conf = new ExtAttrConf();
			conf.setAttrId("ItemDS2");
			conf.setAttrName("页签2数据集");
			list.add(conf);
			
		}
		{
			ExtAttrConf conf = new ExtAttrConf();
			conf.setAttrId("ItemDS3");
			conf.setAttrName("页签3数据集");
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
		
		//return null;
	}
}
