package xap.lui.core.exception;


/**
 * 登录过程中中断异常
 * @author dengjt
 *
 */
public class LoginInterruptedException extends LuiBusinessException {
	private static final long serialVersionUID = -9057727748632036396L;

	public LoginInterruptedException() {
		super();
	}

	public LoginInterruptedException(String message, Throwable cause) {
		super(message, cause);
	}

	public LoginInterruptedException(String s) {
		super(s);
	}

	public LoginInterruptedException(Throwable cause) {
		super(cause);
	}

}
