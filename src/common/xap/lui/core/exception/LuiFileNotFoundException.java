package xap.lui.core.exception;
/**
 * 用于配置文件找不到的exception
 * @author dengjt
 *
 */
public class LuiFileNotFoundException extends LuiRuntimeException {

	private static final long serialVersionUID = -7423994232359308526L;

	public LuiFileNotFoundException(String message, String hint, Throwable cause) {
		super(message, hint, cause);
	}

	public LuiFileNotFoundException(String message, String hint) {
		super(message, hint);
	}

	public LuiFileNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public LuiFileNotFoundException(String message) {
		super(message);
	}

	public LuiFileNotFoundException(Throwable cause) {
		super(cause);
	}

}
