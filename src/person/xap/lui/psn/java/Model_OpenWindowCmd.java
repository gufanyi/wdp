package xap.lui.psn.java;
import java.util.ArrayList;
import java.util.List;

import xap.lui.psn.cmd.LuiOpenWindowCmd;
public class Model_OpenWindowCmd implements ModelCmdConf<LuiOpenWindowCmd> {
	@Override
	public Class<LuiOpenWindowCmd> getLuiCmdClazz() {
		return LuiOpenWindowCmd.class;
	}
	@Override
	public String getCmdTpl() {
		return "OpenWindowTpl";
	}
	@Override
	public ExtAttrConf[] getExtAttrConf() {
		List<ExtAttrConf> list = new ArrayList<ExtAttrConf>();
		{
			ExtAttrConf conf = new ExtAttrConf();
			conf.setAttrId("winId");
			conf.setAttrName("操作win");
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
