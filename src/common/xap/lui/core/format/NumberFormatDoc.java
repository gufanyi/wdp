
package xap.lui.core.format;





public class NumberFormatDoc extends FormatDocDetail{

	
	private static final long serialVersionUID = 0L;
	
	
	private boolean useMil = true;
	
	
	private String milSymbol = ",";
	
	
	private String decimalSymbol = ".";
	
	
	private RedFormatItem negativeFormat = new RedFormatItem("n","-123456",false);
	
	private String expSourceData = "-123456";
	
	public NumberFormatDoc(){
		useMil = true;
		milSymbol = ",";
		decimalSymbol = ".";
		negativeFormat = new RedFormatItem("-n","-123456",false);
		setExpText("-123,456");
		expSourceData = "-123456";
	}

	public NumberFormatMeta toNCFormatMeta() {

		NumberFormatMeta numFormatMeta = new NumberFormatMeta();
		numFormatMeta.setMarkEnable(this.isUseMil());
		numFormatMeta.setMarkSymbol(this.getMilSymbol());
		numFormatMeta.setPointSymbol(this.getDecimalSymbol());
		numFormatMeta.setNegativeFormat(this.getNegativeFormat().getCode());
		numFormatMeta.setNegRed(this.getNegativeFormat().isRed());
		
		return numFormatMeta;
	}





	
	public boolean isUseMil() {
		return useMil;
	}





	
	public void setUseMil(boolean useMil) {
		this.useMil = useMil;
	}





	
	public String getMilSymbol() {
		return milSymbol;
	}





	
	public void setMilSymbol(String milSymbol) {
		this.milSymbol = milSymbol;
	}





	
	public String getDecimalSymbol() {
		return decimalSymbol;
	}





	
	public void setDecimalSymbol(String decimalSymbol) {
		this.decimalSymbol = decimalSymbol;
	}





	
	public RedFormatItem getNegativeFormat() {
		return negativeFormat;
	}





	
	public void setNegativeFormat(RedFormatItem negativeFormat) {
		this.negativeFormat = negativeFormat;
	}





	@Override
	public FormatResult format(Object data) throws FormatException {
		NumberFormatMeta meta = this.toNCFormatMeta();
		NumberFormat formater = new NumberFormat(meta);
		return formater.format(data);
	}





	@Override
	public Object getExpSourceData() {

		return expSourceData;
	}

}
