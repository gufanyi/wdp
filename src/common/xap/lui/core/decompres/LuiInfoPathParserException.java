package xap.lui.core.decompres;

public class LuiInfoPathParserException extends Exception {
	private static final long serialVersionUID = 6454281597305920894L;
	private int errCode;
	private String toUserMsg;
	private IAutoUserMsgAdapter autoUserMsg;

	public LuiInfoPathParserException(int aErrCode) {
		this.errCode = aErrCode;
	}

	public LuiInfoPathParserException(int aErrCode, String message, Throwable cause) {
		super(message, cause);
		this.errCode = aErrCode;
	}

	public LuiInfoPathParserException(int aErrCode, String message) {
		super(message);
		this.errCode = aErrCode;
	}

	public LuiInfoPathParserException(int aErrCode, Throwable cause) {
		super(cause);
		this.errCode = aErrCode;
	}

	public int getErrCode() {
		return this.errCode;
	}

	public String getToUserMsg() {
		return this.toUserMsg;
	}

	public void setToUserMsg(String toUserMsg) {
		this.toUserMsg = toUserMsg;
	}

	private void auto2UserMsg() {
		if (this.autoUserMsg != null) {
			String ftemp = this.autoUserMsg.getUserMsg(this.errCode);
			if ((ftemp != null) && (!ftemp.equals("")))
				this.toUserMsg = ftemp;
		}
	}

	public void setAutoUserMsg(IAutoUserMsgAdapter autoUserMsg) {
		this.autoUserMsg = autoUserMsg;
		auto2UserMsg();
	}

	public static abstract interface IAutoUserMsgAdapter {
		public abstract String getUserMsg(int paramInt);
	}
}
