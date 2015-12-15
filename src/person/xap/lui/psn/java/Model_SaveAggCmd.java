package xap.lui.psn.java;

import java.util.ArrayList;
import java.util.List;

import xap.lui.psn.cmd.LuiSaveAggCmd;

public class Model_SaveAggCmd implements ModelCmdConf<LuiSaveAggCmd> {

	@Override
	public Class<LuiSaveAggCmd> getLuiCmdClazz() {
		return LuiSaveAggCmd.class;
	}

	@Override
	public String getCmdTpl() {
		return "SaveAggTpl";
	}

	@Override
	public ExtAttrConf[] getExtAttrConf() {
		List<ExtAttrConf> list = new ArrayList<ExtAttrConf>();
		{
			ExtAttrConf conf = new ExtAttrConf();
			conf.setAttrId("masterDsId");
			conf.setAttrName("主数据集");
			list.add(conf);
		}
		{
			ExtAttrConf conf = new ExtAttrConf();
			conf.setAttrId("detailDsIds");
			conf.setAttrName("子数据集");
			list.add(conf);
		}
		{
			ExtAttrConf conf = new ExtAttrConf();
			conf.setAttrId("aggVoClazz");
			conf.setAttrName("AggDO类");
			list.add(conf);
		}
		{
			ExtAttrConf conf = new ExtAttrConf();
			conf.setAttrId("CardLayoutIds");
			conf.setAttrName("卡片ID");
			list.add(conf);
		}
		{
			ExtAttrConf conf = new ExtAttrConf();
			conf.setAttrId("isEditView");
			conf.setAttrName("是否弹出框");
			list.add(conf);
		}
		{
			ExtAttrConf conf = new ExtAttrConf();
			conf.setAttrId("parentViewId");
			conf.setAttrName("父窗口ID");
			list.add(conf);
		}
		{
			ExtAttrConf conf = new ExtAttrConf();
			conf.setAttrId("parentDsId");
			conf.setAttrName("父窗口主表DsId");
			list.add(conf);
		}
		{
			ExtAttrConf conf = new ExtAttrConf();
			conf.setAttrId("parentDetailDsIds");
			conf.setAttrName("父窗口子表DsId");
			list.add(conf);
		}
		{
			ExtAttrConf conf = new ExtAttrConf();
			conf.setAttrId("ToolBarCompId");
			conf.setAttrName("工具栏ID");
			list.add(conf);
			
		}
		{
			ExtAttrConf conf = new ExtAttrConf();
			conf.setAttrId("MenuBarCompId");
			conf.setAttrName("菜单栏ID");
			list.add(conf);
		}
		
//		{
//			ExtAttrConf conf = new ExtAttrConf();
//			conf.setAttrId("bodyNotNull");
//			conf.setAttrName("bodyNotNull");
//			list.add(conf);
//		}
		return list.toArray(new ExtAttrConf[0]);
	}
}
