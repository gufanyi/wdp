package xap.lui.core.exception;
/**
 * Web开发框架基础异常类.此类将异常信息分成hint和message.其中hint将是对用户直接可见的友好提示信息.
 * message 和stacktrace是对客户端调试有用的信息.
 * @author dengjt
 * 2007-1-25
 */
public class LuiRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 6461253948274704817L;
	private String hint;

	public LuiRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public LuiRuntimeException(String message, String hint, Throwable cause) {
		super(message, cause);
		this.hint = hint;
	}
	
	public LuiRuntimeException(String message) {
		super(message);
	}
	
	public LuiRuntimeException(String message, String hint){
		super(message);
		this.hint = hint;
	}

	public LuiRuntimeException(Throwable cause) {
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
