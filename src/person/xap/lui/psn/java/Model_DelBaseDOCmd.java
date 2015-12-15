package xap.lui.psn.java;
import java.util.ArrayList;
import java.util.List;

import xap.lui.psn.cmd.LuiDelBaseDOCmd;
public class Model_DelBaseDOCmd implements ModelCmdConf<LuiDelBaseDOCmd> {
	@Override
	public Class<LuiDelBaseDOCmd> getLuiCmdClazz() {
		return LuiDelBaseDOCmd.class;
	}
	@Override
	public String getCmdTpl() {
		return "DeleteBaseDOTpl";
	}
	@Override
	public ExtAttrConf[] getExtAttrConf() {
		List<ExtAttrConf> list = new ArrayList<ExtAttrConf>();
		{
			ExtAttrConf conf = new ExtAttrConf();
			conf.setAttrId("OperatorDs");//规定dsId只能是OperatorDs表示，不然CreateJavaUtil的getMethodTemplate过不去
			conf.setAttrName("操作数据集");
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
