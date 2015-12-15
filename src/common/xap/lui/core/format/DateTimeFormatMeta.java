package xap.lui.core.format;




public class DateTimeFormatMeta extends NCFormatMeta{

	
	public static final String yyyy = "yyyy";
	
	public static final String yy = "yy";
	
	public static final String MMMM = "MMMM";
	
	public static final String MMM = "MMM";
	
	public static final String MM = "MM";
	
	public static final String M = "M";
	
	public static final String dd = "dd";
	
	public static final String d = "d";
	
	public static final String h = "h";
	public static final String hh = "hh";
	public static final String H = "H";
	public static final String HH = "HH";
	public static final String m = "m";
	public static final String mm = "mm";
	public static final String s = "s";
	public static final String ss = "ss";
	
	public static final String t = "t";
	
	protected String format = "yyyy-M-d h:m:s";
	
	protected String speratorSymbol = "-";
	
	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}
	
	public String getSperatorSymbol() {
		return speratorSymbol;
	}

	public void setSperatorSymbol(String speratorSymbol) {
		this.speratorSymbol = speratorSymbol;
	}
}
