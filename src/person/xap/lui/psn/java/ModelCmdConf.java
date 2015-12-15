package xap.lui.psn.java;
import xap.lui.core.command.LuiCommand;
public interface ModelCmdConf<T extends LuiCommand> {
	Class<T> getLuiCmdClazz();
	String getCmdTpl();
	ExtAttrConf[] getExtAttrConf();
}
