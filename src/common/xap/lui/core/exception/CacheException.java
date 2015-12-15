package xap.lui.core.exception;


public class CacheException extends LuiRuntimeException {
	private static final long serialVersionUID = -1903321482523350058L;
	public CacheException(String message, String hint, Throwable cause) {
		super(message, hint, cause);
	}
	public CacheException(String message, Throwable cause) {
		super(message, cause);
	}
	public CacheException(String message) {
		super(message);
	}
	public CacheException(Throwable cause) {
		super(cause);
	}
	public CacheException(String message, String hint) {
		super(message, hint);
	}

}
