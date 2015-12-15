package xap.lui.core.format;





public class AddressFormatMeta extends NCFormatMeta{

	public static final String COUNTRY = "C";
	public static final String STATE = "S";
	public static final String CITY = "T";
	public static final String ROAD = "R";
	public static final String POSTCODE = "P";

	private String express = "C S T R P";
	
	private String separator = " ";

	public String getExpress() {
		return express;
	}

	public void setExpress(String express) {
		this.express = express;
	}

	public String getSeparator() {
		return separator;
	}

	public void setSeparator(String separator) {
		this.separator = separator;
	}
	
}
