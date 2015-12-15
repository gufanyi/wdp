
package xap.lui.core.format;





public class AddressFormatDoc extends FormatDocDetail 
{
	

	
	private static final long serialVersionUID = 6377894548157623314L;
	
	private FormatItem addrUnit1 = new FormatItem("P","�ʱ�");
	
	private FormatItem addrUnit2 = new FormatItem("C","���");
	
	private FormatItem addrUnit3 = new FormatItem("S","ʡ");
	
	private FormatItem addrUnit4 = new FormatItem("T","����");

	private FormatItem addrUnit45 = new FormatItem("D","����");
	
	private FormatItem addrUnit5 = new FormatItem("R","��ַ��ַ");
	
	private String delimit1 = "";
	
	private String delimit2 = "";
	
	private String delimit3 = "";
	
	private String delimit4  = "";
	
	private String delimit45  = "";
	
	private String delimit5 = "";
	
	private AddressObjectAdaptor expSourceData = new AddressObjectAdaptor();
	
	public AddressFormatDoc(){
		addrUnit1 = new FormatItem("P","�ʱ�");
		addrUnit2 = new FormatItem("C","���");
		addrUnit3 = new FormatItem("S","ʡ");
		addrUnit4 = new FormatItem("T","����");
		addrUnit45 = new FormatItem("D","����");
		addrUnit5 = new FormatItem("R","��ַ");
		
		delimit1 = "";
		delimit2 = "";
		delimit3 = "";
		delimit4  = "";
		delimit45  = "";
		delimit5 = "";
		
		setExpText("");
	}

	public AddressFormatMeta toNCFormatMeta() 
	{
		String format = addrUnit1.getCode() + getDelimit(delimit1) +
		                addrUnit2.getCode() + getDelimit(delimit2) + 
		                addrUnit3.getCode() + getDelimit(delimit3) + 
		                addrUnit4.getCode() + getDelimit(delimit4) + 
		                addrUnit45.getCode() + getDelimit(delimit45) +
		                addrUnit5.getCode() + getDelimit(delimit5);
		AddressFormatMeta formatMeta = new AddressFormatMeta();
		formatMeta.setExpress(format);
		return formatMeta;
	}
	
	private String getDelimit(String delimit)
	{
		if(delimit == null)
		{
			delimit = "";
		}
			return "\""+delimit+"\"";
	}

	
	public FormatItem getAddrUnit1() {
		return addrUnit1;
	}

	
	public void setAddrUnit1(FormatItem addrUnit1) {
		this.addrUnit1 = addrUnit1;
	}

	
	public FormatItem getAddrUnit2() {
		return addrUnit2;
	}

	
	public void setAddrUnit2(FormatItem addrUnit2) {
		this.addrUnit2 = addrUnit2;
	}

	
	public FormatItem getAddrUnit3() {
		return addrUnit3;
	}

	
	public void setAddrUnit3(FormatItem addrUnit3) {
		this.addrUnit3 = addrUnit3;
	}

	
	public FormatItem getAddrUnit4() {
		return addrUnit4;
	}

	
	public void setAddrUnit4(FormatItem addrUnit4) {
		this.addrUnit4 = addrUnit4;
	}

	
	public FormatItem getAddrUnit5() {
		return addrUnit5;
	}

	
	public void setAddrUnit5(FormatItem addrUnit5) {
		this.addrUnit5 = addrUnit5;
	}

	
	public String getDelimit1() {
		return delimit1;
	}

	
	public void setDelimit1(String delimit1) {
		this.delimit1 = delimit1;
	}

	
	public String getDelimit2() {
		return delimit2;
	}

	
	public void setDelimit2(String delimit2) {
		this.delimit2 = delimit2;
	}

	
	public String getDelimit3() {
		return delimit3;
	}

	
	public void setDelimit3(String delimit3) {
		this.delimit3 = delimit3;
	}

	
	public String getDelimit4() {
		return delimit4;
	}

	
	public void setDelimit4(String delimit4) {
		this.delimit4 = delimit4;
	}

	
	public String getDelimit5() {
		return delimit5;
	}

	
	public void setDelimit5(String delimit5) {
		this.delimit5 = delimit5;
	}
	
	public FormatItem getAddrUnit45() {
		return addrUnit45;
	}

	public void setAddrUnit45(FormatItem addrUnit45) {
		this.addrUnit45 = addrUnit45;
	}

	public String getDelimit45() {
		return delimit45;
	}

	public void setDelimit45(String delimit45) {
		this.delimit45 = delimit45;
	}

	@Override
	public FormatResult format(Object data) throws FormatException{
		AddressFormat formater = new AddressFormat(this.toNCFormatMeta());
		if(data == null)
			return null;

		return formater.format(data);
	}

	@Override
	public Object getExpSourceData() {

		return expSourceData;
	}
	
	@Override
	public String evalueExpFormat() throws FormatException
	{
		FormatResult result = format(getExpSourceData());
		if (result!=null){
			return result.getValue();
		}
		return "";
	}
}
