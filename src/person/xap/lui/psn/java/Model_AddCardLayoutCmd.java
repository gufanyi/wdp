package xap.lui.psn.java;
import java.util.ArrayList;
import java.util.List;

import xap.lui.psn.cmd.LuiAddCardLayoutCmd;
public class Model_AddCardLayoutCmd implements ModelCmdConf<LuiAddCardLayoutCmd> {
	@Override
	public Class<LuiAddCardLayoutCmd> getLuiCmdClazz() {
		return LuiAddCardLayoutCmd.class;
	}
	@Override
	public String getCmdTpl() {
		return "AddCardLayoutTpl";
	}
	@Override
	public ExtAttrConf[] getExtAttrConf() {
		List<ExtAttrConf> list = new ArrayList<ExtAttrConf>();
		{
			ExtAttrConf conf = new ExtAttrConf();
			conf.setAttrId("CardLayoutId");
			conf.setAttrName("卡片ID");
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
			conf.setAttrId("NavDsId");
			conf.setAttrName("导航数据集");
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
