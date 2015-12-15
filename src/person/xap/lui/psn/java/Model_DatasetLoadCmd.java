package xap.lui.psn.java;
import java.util.ArrayList;
import java.util.List;

import xap.lui.psn.cmd.LuiDatasetLoadCmd;
public class Model_DatasetLoadCmd implements ModelCmdConf<LuiDatasetLoadCmd> {
	
	@Override
	public Class<LuiDatasetLoadCmd> getLuiCmdClazz() {
		return LuiDatasetLoadCmd.class;
	}
	@Override
	public String getCmdTpl() {
		return "DatasetOnLoadTpl";
	}
	@Override
	public ExtAttrConf[] getExtAttrConf() {
		List<ExtAttrConf> list = new ArrayList<ExtAttrConf>();
			{
				ExtAttrConf conf = new ExtAttrConf();
				conf.setAttrId("OperatorDs");
				conf.setAttrName("数据集");
				list.add(conf);
			}
			{
				ExtAttrConf conf = new ExtAttrConf();
				conf.setAttrId("ToolBarStatus");
				conf.setAttrName("是否配置工具栏状态");
				list.add(conf);
			}
			return list.toArray(new ExtAttrConf[0]);
		}
}
