package xap.lui.psn.java;
import xap.lui.psn.cmd.LuiWindowDestroyCmd;
public class Model_WindowDestroyCmd implements ModelCmdConf<LuiWindowDestroyCmd> {
	@Override
	public Class<LuiWindowDestroyCmd> getLuiCmdClazz() {
		return LuiWindowDestroyCmd.class;
	}
	@Override
	public String getCmdTpl() {
		return "WindowDestroyTpl";
	}
	@Override
	public ExtAttrConf[] getExtAttrConf() {
		return null;
	}
}
