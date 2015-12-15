package xap.lui.compile.ca.log;

import xap.lui.core.logger.LuiLogger;

public final class CompileLog {
	public static final String LFW_LOGGER_NAME = "uapweb";
	public static void info(String msg){
		LuiLogger.info(msg);
	}
	
	public static void debug(String msg){
		LuiLogger.debug(msg);
	}
	
	public static void error(String msg, Throwable t){
		LuiLogger.error(msg, t);
	}
	
	public static void error(String msg){
		LuiLogger.error(msg);
	}
	
	public static void error(Throwable e){
		LuiLogger.error(e.getMessage(), e);
	}

	public static boolean isDebugEnabled() {
		return LuiLogger.isDebugEnabled();
	}

	public static boolean isInfoEnabled() {
		return LuiLogger.isInfoEnabled();
	}

	public static void warn(String string) {
		LuiLogger.warn(string);
	}

	public static boolean isWarnEnabled() {
		return LuiLogger.isWarnEnabled();
	}
}
