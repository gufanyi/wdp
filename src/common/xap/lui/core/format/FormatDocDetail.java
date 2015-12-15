package xap.lui.core.format;

import xap.lui.core.exception.LuiRuntimeException;

public abstract class FormatDocDetail implements java.io.Serializable {

	private String expText;

	abstract public NCFormatMeta toNCFormatMeta();

	abstract public FormatResult format(Object data) throws FormatException;

	abstract public Object getExpSourceData();

	public String evalueExpFormat() throws FormatException {
		String result = FormatUtils.toColorfulString(format(getExpSourceData()));
		if (result.startsWith("<font")) {
			expText = "<html><body>" + result + "</body></html>";
			;
		} else {
			expText = result;
		}
		return expText;
	}

	public String getExpText() {
		if (expText != null && expText.startsWith("<html>")) {
			try {
				expText = format(getExpSourceData()).getValue();
			} catch (FormatException e) {
				throw new LuiRuntimeException(e.getMessage());
			}
		}
		return expText;
	}

	public void setExpText(String expText) {
		this.expText = expText;
	}
}
