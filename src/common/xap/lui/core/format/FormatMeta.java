package xap.lui.core.format;






public class FormatMeta implements java.io.Serializable {

	
	private static final long serialVersionUID = -7525152225665425852L;

	
	private NumberFormatDoc nfm;
	
	
	private CurrencyFormatDoc cfm;
	
	
	private DateFormatDoc dfm;
	
	
	private TimeFormatDoc tfm;
	
	
	private AddressFormatDoc afm;

	public NumberFormatDoc getNfm() {
		return nfm;
	}

	public void setNfm(NumberFormatDoc nfm) {
		this.nfm = nfm;
	}

	public CurrencyFormatDoc getCfm() {
		return cfm;
	}

	public void setCfm(CurrencyFormatDoc cfm) {
		this.cfm = cfm;
	}

	public DateFormatDoc getDfm() {
		return dfm;
	}

	public void setDfm(DateFormatDoc dfm) {
		this.dfm = dfm;
	}

	public TimeFormatDoc getTfm() {
		return tfm;
	}

	public void setTfm(TimeFormatDoc tfm) {
		this.tfm = tfm;
	}

	public AddressFormatDoc getAfm() {
		return afm;
	}

	public void setAfm(AddressFormatDoc afm) {
		this.afm = afm;
	}
	
}
