package xap.lui.psn.java;
import java.util.ArrayList;
import java.util.List;

import xap.lui.psn.cmd.LuiEditViewCmd;
public class Model_EditViewCmd implements ModelCmdConf<LuiEditViewCmd> {
	@Override
	public Class<LuiEditViewCmd> getLuiCmdClazz() {
		return LuiEditViewCmd.class;
	}
	@Override
	public String getCmdTpl() {
		return "EditViewTpl";
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
		{
			ExtAttrConf conf = new ExtAttrConf();
			conf.setAttrId("isclose");
			conf.setAttrName("是否关闭");
			list.add(conf);
		}
		return list.toArray(new ExtAttrConf[0]);
	}
}
