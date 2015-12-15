package xap.lui.core.exception;

import xap.lui.core.exception.LuiRuntimeException;

public class LuiLicenseException extends LuiRuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3065433625787437048L;

	public LuiLicenseException(String message, String hint,
			Throwable cause) {
		super(message, hint, cause);
	}

	public LuiLicenseException(String message, String hint) {
		super(message, hint);
	}

	public LuiLicenseException(String message, Throwable cause) {
		super(message, cause);
	}

	public LuiLicenseException(String message) {
		super(message);
	}

	public LuiLicenseException(Throwable cause) {
		super(cause);
	}
}
