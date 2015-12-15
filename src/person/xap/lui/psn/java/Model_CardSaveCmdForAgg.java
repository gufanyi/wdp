package xap.lui.psn.java;

import java.util.ArrayList;
import java.util.List;

import xap.lui.psn.cmd.LuiCardSaveCmdForAgg;



public class Model_CardSaveCmdForAgg implements ModelCmdConf<LuiCardSaveCmdForAgg> {

	@Override
	public Class<LuiCardSaveCmdForAgg> getLuiCmdClazz() {
		return LuiCardSaveCmdForAgg.class;
	}

	@Override
	public String getCmdTpl() {
		return "CardSaveCmdForAggTpl";
	}
	
	@Override
	public ExtAttrConf[] getExtAttrConf() {
		List<ExtAttrConf> list = new ArrayList<ExtAttrConf>();
//		{
//			ExtAttrConf conf = new ExtAttrConf();
//			conf.setAttrId("CardLayoutId");
//			conf.setAttrName("卡片ID");
//			list.add(conf);
//			
//		}
		{
			ExtAttrConf conf = new ExtAttrConf();
			conf.setAttrId("OperatorDs");
			conf.setAttrName("操作数据集");
			list.add(conf);
		}
		{
			ExtAttrConf conf = new ExtAttrConf();
			conf.setAttrId("NavDsId");
			conf.setAttrName("导航数据集");
			list.add(conf);
			
		}
		return list.toArray(new ExtAttrConf[0]);
	}

}
