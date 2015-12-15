package xap.lui.psn.java;
import java.util.ArrayList;
import java.util.List;

import xap.lui.psn.cmd.LuiDatasetAfterSelectCmd;
public class Model_DatasetAfterSelectCmd implements ModelCmdConf<LuiDatasetAfterSelectCmd> {
	
	@Override
	public Class<LuiDatasetAfterSelectCmd> getLuiCmdClazz() {
		return LuiDatasetAfterSelectCmd.class;
	}
	@Override
	public String getCmdTpl() {
		return "DatasetAfterRowSelectTpl";
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
//		{
//			ExtAttrConf conf = new ExtAttrConf();
//			conf.setAttrId("ds");
//			conf.setAttrName("操作数据集");
//			list.add(conf);
//			
		//}
//		{
//			ExtAttrConf conf = new ExtAttrConf();
//			conf.setAttrId("widget");
//			conf.setAttrName("视图 ID");r
//			list.add(conf);
//		}
		return list.toArray(new ExtAttrConf[0]);
	}
}
