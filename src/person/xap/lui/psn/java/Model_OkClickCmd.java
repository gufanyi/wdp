package xap.lui.psn.java;
import java.util.ArrayList;
import java.util.List;

import xap.lui.psn.cmd.LuiOkClickCmd;
public class Model_OkClickCmd implements ModelCmdConf<LuiOkClickCmd> {
	@Override
	public Class<LuiOkClickCmd> getLuiCmdClazz() {
		return LuiOkClickCmd.class;
	}
	@Override
	public String getCmdTpl() {
		return "OkClickTpl";
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
			conf.setAttrId("parentViewId");
			conf.setAttrName("父窗口");
			list.add(conf);
		}
		{
			ExtAttrConf conf = new ExtAttrConf();
			conf.setAttrId("parentDsId");
			conf.setAttrName("父窗口数据集");
			list.add(conf);
		}
		return list.toArray(new ExtAttrConf[0]);
	}
}
