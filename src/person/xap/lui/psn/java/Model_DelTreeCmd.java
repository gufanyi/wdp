package xap.lui.psn.java;

import java.util.ArrayList;
import java.util.List;

import xap.lui.psn.cmd.LuiDelTreeCmd;

public class Model_DelTreeCmd implements ModelCmdConf<LuiDelTreeCmd> {
	@Override
	public Class<LuiDelTreeCmd> getLuiCmdClazz() {
		return LuiDelTreeCmd.class;
	}
	@Override
	public String getCmdTpl() {
		return "DeleteTreeTpl";
	}
	@Override
	public ExtAttrConf[] getExtAttrConf() {
		List<ExtAttrConf> list = new ArrayList<ExtAttrConf>();
		{
			ExtAttrConf conf = new ExtAttrConf();
			conf.setAttrId("treeDsId");
			conf.setAttrName("树数据集");
			list.add(conf);
		}
		{
			ExtAttrConf conf = new ExtAttrConf();
			conf.setAttrId("masterDsId");
			conf.setAttrName("主表数据集");
			list.add(conf);
		}
		{
			ExtAttrConf conf = new ExtAttrConf();
			conf.setAttrId("aggVoClazz");
			conf.setAttrName("操作AggDO类");
			list.add(conf);
		}
		return list.toArray(new ExtAttrConf[0]);
	}

}
