
package xap.lui.core.format;





public class CurrencyFormatDoc extends NumberFormatDoc {

	
	private static final long serialVersionUID = -7459489158170424278L;

	private FormatItem positiveFormat = new FormatItem("$n","$123456");
	
    private String curSymbol = "";
	
	private final String positiveExpSource = "123456";
	
	public CurrencyFormatDoc(){
		setDecimalSymbol(".");
		setExpText("<html><body>����:��-123,456 ����:��123,456</body></html>");
		setMilSymbol(",");
		setNegativeFormat(new RedFormatItem("$-n","$-123456",false));
		setPositiveFormat(new FormatItem("$n","$123456"));
		setUseMil(true);
	}
	

	
	public FormatItem getPositiveFormat() {
		return positiveFormat;
	}



	
	public void setPositiveFormat(FormatItem positiveFormat) {
		this.positiveFormat = positiveFormat;
	}



	
	public String getCurSymbol() {
		return "";
	}



	
	public void setCurSymbol(String curSymbol) {
		this.curSymbol = curSymbol;
	}



	public CurrencyFormatMeta toNCFormatMeta() {

		CurrencyFormatMeta formatMeta = new CurrencyFormatMeta();
		formatMeta.setMarkEnable(this.isUseMil());
		formatMeta.setMarkSymbol(this.getMilSymbol());
		formatMeta.setPointSymbol(this.getDecimalSymbol());
		formatMeta.setNegativeFormat(this.getNegativeFormat().getCode());
		formatMeta.setCurSymbol(this.getCurSymbol());
		formatMeta.setPositiveFormat(this.getPositiveFormat().getCode());
		formatMeta.setNegRed(this.getNegativeFormat().isRed());
		return formatMeta;
	}

	public FormatResult format(Object data) throws FormatException {
		CurrencyFormatMeta meta = this.toNCFormatMeta();
		CurrencyFormat formater = new CurrencyFormat(meta);
		return formater.format(data);
	}



	
	public String getPositiveExpSource() {
		return positiveExpSource;
	}
	
	public String evalueExpFormat() throws FormatException
	{
		FormatResult negativeResult = format(getExpSourceData());
		FormatResult positiveResult = format(getPositiveExpSource());

		String negativeString = FormatUtils.toColorfulString(negativeResult);
		String positiveString = FormatUtils.toColorfulString(positiveResult);
		
		String result = "<html><body>"+"����:"+negativeString+" ����:"+positiveString+"</body></html>";
		this.setExpText(result);
		return getExpText();
	}
	
}
