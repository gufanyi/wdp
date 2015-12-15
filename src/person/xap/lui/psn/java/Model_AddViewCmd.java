package xap.lui.psn.java;
import java.util.ArrayList;
import java.util.List;

import xap.lui.psn.cmd.LuiAddViewCmd;
public class Model_AddViewCmd implements ModelCmdConf<LuiAddViewCmd> {
	@Override
	public Class<LuiAddViewCmd> getLuiCmdClazz() {
		return LuiAddViewCmd.class;
	}
	@Override
	public String getCmdTpl() {
		return "AddViewTpl";
	}
	@Override
	public ExtAttrConf[] getExtAttrConf() {
		List<ExtAttrConf> list = new ArrayList<ExtAttrConf>();
		{
			ExtAttrConf conf = new ExtAttrConf();
			conf.setAttrId("viewId");
			conf.setAttrName("操作窗口");
			list.add(conf);
		}
		{
			ExtAttrConf conf = new ExtAttrConf();
			conf.setAttrId("height");
			conf.setAttrName("对话框高");
			list.add(conf);
		}
		{
			ExtAttrConf conf = new ExtAttrConf();
			conf.setAttrId("width");
			conf.setAttrName("对话框宽");
			list.add(conf);
		}
		{
			ExtAttrConf conf = new ExtAttrConf();
			conf.setAttrId("title");
			conf.setAttrName("标题");
			list.add(conf);
		}
		return list.toArray(new ExtAttrConf[0]);
	}
}
