package xap.lui.core.exception;


/**
 * lui单据业务逻辑中所抛出异常
 * @author dengjt
 */
public class LuiBusinessException extends Exception {
	private static final long serialVersionUID = -7701050274700646799L;

	public LuiBusinessException() {
		super();
	}

	public LuiBusinessException(String message, Throwable cause) {
		super(message, cause);
	}

	public LuiBusinessException(String s) {
		super(s);
	}

	public LuiBusinessException(Throwable cause) {
		super(cause);
	}


}
