package xap.lui.psn.java;
import java.util.ArrayList;
import java.util.List;

import xap.lui.psn.cmd.LuiDelAggDOCmd;
public class Model_DelAggDOCmd implements ModelCmdConf<LuiDelAggDOCmd> {
	@Override
	public Class<LuiDelAggDOCmd> getLuiCmdClazz() {
		return LuiDelAggDOCmd.class;
	}
	@Override
	public String getCmdTpl() {
		return "DeleteAggDOTpl";
	}
	@Override
	public ExtAttrConf[] getExtAttrConf() {
		List<ExtAttrConf> list = new ArrayList<ExtAttrConf>();
		{
			ExtAttrConf conf = new ExtAttrConf();
			conf.setAttrId("masterDsId");
			conf.setAttrName("操作数据集");
			list.add(conf);
		}
		{
			ExtAttrConf conf = new ExtAttrConf();
			conf.setAttrId("aggVoClazz");
			conf.setAttrName("操作AggDO类");
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
