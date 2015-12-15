package xap.lui.psn.pamgr;

import xap.lui.core.exception.LuiBusinessException;

/**
 * 个性化设置中所要继承的Exception
 * @author wupeng1
 * @version 6.0 2011-7-18
 * @since 1.6
 */
public class PaBusinessException extends LuiBusinessException{

	private static final long serialVersionUID = -2971100135245303405L;

	public PaBusinessException() {
		super();
	}

	public PaBusinessException(String message, Throwable cause) {
		super(message, cause);
	}

	public PaBusinessException(String message) {
		super(message);
	}

	public PaBusinessException(Throwable cause) {
		super(cause);
	}


}
