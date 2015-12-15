package xap.lui.psn.java;

import java.util.ArrayList;
import java.util.List;

import xap.lui.psn.cmd.LuiFormLoadCmd;

public class Model_FormLoadCmd implements ModelCmdConf<LuiFormLoadCmd>{
	
	@Override
	public Class<LuiFormLoadCmd> getLuiCmdClazz() {
		return LuiFormLoadCmd.class;
	}
	@Override
	public String getCmdTpl() {
		return "FormOnLoadTpl";
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
			return list.toArray(new ExtAttrConf[0]);
		}

}
