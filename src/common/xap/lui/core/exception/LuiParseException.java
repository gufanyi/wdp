package xap.lui.core.exception;

public class LuiParseException extends LuiRuntimeException {
	private static final long serialVersionUID = 7933322024363835575L;
	public LuiParseException(String message, Throwable cause) {
		super(message, cause);
	}

	public LuiParseException(String message, String hint, Throwable cause) {
		super(message, hint, cause);
	}

	public LuiParseException(String message, String hint) {
		super(message, hint);
	}

	public LuiParseException(String message) {
		super(message);
	}

	public LuiParseException(Throwable cause) {
		super(cause);
	}

}
