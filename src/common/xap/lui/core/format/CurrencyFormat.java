package xap.lui.core.format;





public class CurrencyFormat extends NumberFormat{
	
	public CurrencyFormat(CurrencyFormatMeta formatMeta){
		this.formatMeta = formatMeta;
	}

	//
	// public CurrencyFormat(NumberFormatMeta formatMeta) {
	// super(formatMeta);
	// // TODO Auto-generated constructor stub
	// }

	@Override
	protected FormatResult innerFormat(Object obj) throws FormatException{
		FormatResult fo = super.innerFormat(obj);
		fo.setValue(fo.getValue().replace("$", ((CurrencyFormatMeta)formatMeta).getCurSymbol()));
		return fo;
	}
	
	

}
