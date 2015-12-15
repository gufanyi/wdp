package xap.lui.core.exception;

import xap.lui.core.exception.LuiRuntimeException;

public class LuiSecurityException extends LuiRuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3065433625787437048L;

	public LuiSecurityException(String message, String hint,
			Throwable cause) {
		super(message, hint, cause);
	}

	public LuiSecurityException(String message, String hint) {
		super(message, hint);
	}

	public LuiSecurityException(String message, Throwable cause) {
		super(message, cause);
	}

	public LuiSecurityException(String message) {
		super(message);
	}

	public LuiSecurityException(Throwable cause) {
		super(cause);
	}


}
