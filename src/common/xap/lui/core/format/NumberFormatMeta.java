package xap.lui.core.format;




public class NumberFormatMeta extends NCFormatMeta{

	public static final String POS_FORMAT_ORIGN = "n";
	
	public static final String NEG_FORMAT_HEAD = "-n";
	public static final String NEG_FORMAT_HEAD_SPACE = "- n";
	public static final String NEG_FORMAT_TAIL = "n-";
	public static final 	String NEG_FORMAT_TAIL_SPACE = "n -";
	public static final String NEG_FORMAT_BRACKET = "(n)";
	
	
	protected boolean isNegRed = false;

	protected boolean isMarkEnable = false;
	
	protected String markSymbol = ",";
	
	protected String pointSymbol = ".";
	
	protected String positiveFormat = POS_FORMAT_ORIGN;
	
	protected String negativeFormat = NEG_FORMAT_HEAD;

	public boolean isMarkEnable() {
		return isMarkEnable;
	}

	public void setMarkEnable(boolean isMarkEnable) {
		this.isMarkEnable = isMarkEnable;
	}

	public String getMarkSymbol() {
		return markSymbol;
	}

	public void setMarkSymbol(String markSymbol) {
		this.markSymbol = markSymbol;
	}

	public String getPointSymbol() {
		return pointSymbol;
	}

	public void setPointSymbol(String pointSymbol) {
		this.pointSymbol = pointSymbol;
	}

	public String getPositiveFormat() {
		return positiveFormat;
	}

	public void setPositiveFormat(String positiveFormat) {
		this.positiveFormat = positiveFormat;
	}

	public String getNegativeFormat() {
		return negativeFormat;
	}

	public void setNegativeFormat(String negativeFormat) {
		this.negativeFormat = negativeFormat;
	}

	public boolean isNegRed() {
		return isNegRed;
	}

	public void setNegRed(boolean isNegRed) {
		this.isNegRed = isNegRed;
	}
	
}
