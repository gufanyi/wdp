package xap.lui.core.exception;

public class LuiPluginException extends RuntimeException {

	private static final long serialVersionUID = 6461253948274704817L;
	private String hint;

	public LuiPluginException(String message, Throwable cause) {
		super(message, cause);
	}

	public LuiPluginException(String message, String hint, Throwable cause) {
		super(message, cause);
		this.hint = hint;
	}
	
	public LuiPluginException(String message) {
		super(message);
	}
	
	public LuiPluginException(String message, String hint){
		super(message);
		this.hint = hint;
	}

	public LuiPluginException(Throwable cause) {
		super(cause);
	}

	public String getHint() {
		if(hint == null)
			hint = this.getMessage();
		if(hint == null){
			if(this.getCause() != null)
				hint = this.getCause().getMessage();
		}
		return hint;
	}

	public void setHint(String hint) {
		this.hint = hint;
	}
	
}
