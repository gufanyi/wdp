package xap.lui.core.format;




public class CurrencyFormatMeta extends NumberFormatMeta{
	
	public static final String CUR_POS_FORMAT_HEAD = "$n";
	public static final String CUR_POS_FORMAT_TAIL = "n$";
	public static final String CUR_POS_FORMAT_HEAD_SPACE = "$ n";
	public static final String CUR_POS_FORMAT_TAIL_SPACE = "n $";
	
	public static final String CUR_NEG_FORMAT_HEAD = "$-n";
	public static final String CUR_NEG_FORMAT_MID = "-$n";
	public static final String CUR_NEG_FORMAT_TAIL = "-n$";
	public static final String CUR_NEG_FORMAT_HEAD_SPACE = "$ -n";
	public static final String CUR_NEG_FORMAT_TAIL_SPACE = "-n $";
	public static final String CUR_NEG_FORMAT_BRACKET = "��$n��";
	
	
	private String curSymbol = "";

	public CurrencyFormatMeta(){
		positiveFormat = CUR_POS_FORMAT_HEAD;
		negativeFormat = CUR_NEG_FORMAT_HEAD;
	}

	public String getCurSymbol() {
		return curSymbol;
	}

	public void setCurSymbol(String curSymbol) {
		this.curSymbol = curSymbol;
	}
	
}
