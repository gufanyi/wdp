package xap.lui.psn.java;
import java.util.HashMap;
import java.util.Map;

import xap.lui.psn.cmd.LuiAddCardLayoutCmd;
import xap.lui.psn.cmd.LuiAddOrEditBeforeShowCmd;
import xap.lui.psn.cmd.LuiAddOrEditMenuClickCmd;
import xap.lui.psn.cmd.LuiAddRowCmd;
import xap.lui.psn.cmd.LuiAddTabCardLayoutCmd;
import xap.lui.psn.cmd.LuiAddViewCmd;
import xap.lui.psn.cmd.LuiCancelCardLayoutCmd;
import xap.lui.psn.cmd.LuiCancelCmd;
import xap.lui.psn.cmd.LuiCancelTabCardLayoutCmd;
import xap.lui.psn.cmd.LuiCardSaveCmdForAgg;
import xap.lui.psn.cmd.LuiCardSaveDOCmd;
import xap.lui.psn.cmd.LuiCloseViewCmd;
import xap.lui.psn.cmd.LuiDatasetAfterSelectCmd;
import xap.lui.psn.cmd.LuiDatasetLoadCmd;
import xap.lui.psn.cmd.LuiDelAggDOCmd;
import xap.lui.psn.cmd.LuiDelBaseDOCmd;
import xap.lui.psn.cmd.LuiDelTabBaseDOCmd;
import xap.lui.psn.cmd.LuiDelTreeCmd;
import xap.lui.psn.cmd.LuiEditCardLayoutCmd;
import xap.lui.psn.cmd.LuiEditRowCmd;
import xap.lui.psn.cmd.LuiEditTabCardLayoutCmd;
import xap.lui.psn.cmd.LuiEditViewCmd;
import xap.lui.psn.cmd.LuiFormLoadCmd;
import xap.lui.psn.cmd.LuiOkCardLayoutCmd;
import xap.lui.psn.cmd.LuiOpenViewCmd;
import xap.lui.psn.cmd.LuiOpenWindowCmd;
import xap.lui.psn.cmd.LuiRemoveRowCmd;
import xap.lui.psn.cmd.LuiSaveAggCmd;
import xap.lui.psn.cmd.LuiSaveCmd;
import xap.lui.psn.cmd.LuiWindowDestroyCmd;
public class Command2TplCache {
	@SuppressWarnings("rawtypes")
	public static Map<String, ModelCmdConf> map = new HashMap<String, ModelCmdConf>();
	static {
		map.put(LuiAddRowCmd.class.getSimpleName(), new Model_AddRowCmd());
		map.put(LuiAddViewCmd.class.getSimpleName(), new Model_AddViewCmd());
		map.put(LuiAddCardLayoutCmd.class.getSimpleName(), new Model_AddCardLayoutCmd());
		map.put(LuiAddTabCardLayoutCmd.class.getSimpleName(), new Model_AddTabCardLayoutCmd());
		map.put(LuiAddOrEditMenuClickCmd.class.getSimpleName(), new Model_AddOrEditMenuClickCmd());
		map.put(LuiAddOrEditBeforeShowCmd.class.getSimpleName(), new Model_AddOrEditBeforeShow());

		
		map.put(LuiEditRowCmd.class.getSimpleName(), new Model_EditRowCmd());
		map.put(LuiEditViewCmd.class.getSimpleName(), new Model_EditViewCmd());
		map.put(LuiEditCardLayoutCmd.class.getSimpleName(), new Model_EditCardLayoutCmd());
		map.put(LuiEditTabCardLayoutCmd.class.getSimpleName(), new Model_EditTabCardLayoutCmd());
		
		map.put(LuiDelAggDOCmd.class.getSimpleName(), new Model_DelAggDOCmd());
		map.put(LuiDelTreeCmd.class.getSimpleName(), new Model_DelTreeCmd());
		map.put(LuiDelBaseDOCmd.class.getSimpleName(), new Model_DelBaseDOCmd());
		map.put(LuiDelTabBaseDOCmd.class.getSimpleName(), new Model_DelTabBaseDOCmd());
		
		map.put(LuiSaveCmd.class.getSimpleName(), new Model_SaveCmd());
		map.put(LuiSaveAggCmd.class.getSimpleName(), new Model_SaveAggCmd());
		map.put(LuiCardSaveDOCmd.class.getSimpleName(), new Model_CardSaveDOCmd());
		map.put(LuiCardSaveCmdForAgg.class.getSimpleName(), new Model_CardSaveCmdForAgg());
		
		map.put(LuiCancelCmd.class.getSimpleName(), new Model_CancelCmd());
		map.put(LuiCancelCardLayoutCmd.class.getSimpleName(), new Model_CancelCardLayoutCmd());
		map.put(LuiCancelTabCardLayoutCmd.class.getSimpleName(), new Model_CancelTabCardLayoutCmd());
		
		map.put(LuiDatasetLoadCmd.class.getSimpleName(), new Model_DatasetLoadCmd());
		map.put(LuiFormLoadCmd.class.getSimpleName(), new Model_FormLoadCmd());
		map.put(LuiDatasetAfterSelectCmd.class.getSimpleName(), new Model_DatasetAfterSelectCmd());
	
		map.put(LuiOpenViewCmd.class.getSimpleName(), new Model_OpenViewCmd());
		map.put(LuiCloseViewCmd.class.getSimpleName(), new Model_CloseViewCmd());
		map.put(LuiOpenWindowCmd.class.getSimpleName(), new Model_OpenWindowCmd());
		map.put(LuiWindowDestroyCmd.class.getSimpleName(), new Model_WindowDestroyCmd());
		map.put(LuiRemoveRowCmd.class.getSimpleName(), new Model_RemoveRowCmd());
		
		map.put(LuiOkCardLayoutCmd.class.getSimpleName(), new Model_OkCardLayoutCmd());
		
		
	}
	public static String getTplName(String cmdName) {
		ModelCmdConf<?> conf = map.get(cmdName);
		if (conf == null) {
			return null;
		}
		return conf.getCmdTpl();
	}
	public static ExtAttrConf[] getExtAttrConf(String cmdName) {
		ModelCmdConf<?> conf = map.get(cmdName);
		if (conf == null) {
			return null;
		}
		return conf.getExtAttrConf();
	}
}
