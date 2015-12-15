package xap.lui.core.logger;

/**
 * log日志处理类
 */
public class LuiLogger {

	public static final String LUI_LOGGER_NAME = "appwdp";

	public static void info(String msg) {
		//Logger.info(msg);
	}

	public static void debug(String msg) {
		//Logger.debug(msg);
	}

	public static void error(String msg, Throwable t) {
		//Logger.error(msg, t);
	}

	public static void error(String msg) {
		//Logger.error(msg);
	}

	public static void error(Throwable e) {
		//Logger.error(e.getMessage(), e);
	}

	public static boolean isDebugEnabled() {
		return false;
	}

	public static boolean isInfoEnabled() {
		return false;
	}

	public static void warn(String string) {
	}

	public static boolean isWarnEnabled() {
		return false;
	}
}
