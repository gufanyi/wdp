package xap.lui.core.format;

import xap.lui.core.vos.IElement;




public class DateTimeFormat extends AbstractSplitFormat{

	protected DateTimeFormatMeta formatMeta = null;
	
	
	public static final String[] enShortMonth = 
		new String[]{"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
	
	public static final String[] enLongMonth = 
		new String[]{"January","February","March","April","May","June","July","August","September","October","November","December"};
	
	DateTimeFormat(){
		
	}
	
	public DateTimeFormat(DateTimeFormatMeta formatMeta){
		this.formatMeta = formatMeta;
	}
	
	private IElement doOne(String express){
		if(express.length() == 0)
			return new StringElement("");
		
		if(express.equals("yyyy"))
			return new IElement(){
				public String getValue(Object obj) {
					return getyyyy((DateTimeObject)obj);
				}
		};
		
		if(express.equals("yy"))
			return new IElement(){
				public String getValue(Object obj) {
					return getyy((DateTimeObject)obj);
				}
		};
		
		if(express.equals("MMMM"))
			return new IElement(){
				public String getValue(Object obj) {
					return getMMMM((DateTimeObject)obj);
				}
		};
		
		if(express.equals("MMM"))
			return new IElement(){
				public String getValue(Object obj) {
					return getMMM((DateTimeObject)obj);
				}
		};
			
		if(express.equals("MM"))
			return new IElement(){
			public String getValue(Object obj) {
				return getMM((DateTimeObject)obj);
			}
		};
			
		if(express.equals("M"))
			return new IElement(){
			public String getValue(Object obj) {
				return getM((DateTimeObject)obj);
			}
		};
		
		if(express.equals("dd"))
			return new IElement(){
			public String getValue(Object obj) {
				return getdd((DateTimeObject)obj);
			}
		};
		
		if(express.equals("d"))
			return new IElement(){
			public String getValue(Object obj) {
				return getd((DateTimeObject)obj);
			}
		};
		
		if(express.equals("hh"))
			return new IElement(){
			public String getValue(Object obj) {
				return gethh((DateTimeObject)obj);
			}
		};
		
		if(express.equals("h"))
			return new IElement(){
			public String getValue(Object obj) {
				return geth((DateTimeObject)obj);
			}
		};
		
		if(express.equals("mm"))
			return new IElement(){
			public String getValue(Object obj) {
				return getmm((DateTimeObject)obj);
			}
		};
		
		if(express.equals("m"))
			return new IElement(){
			public String getValue(Object obj) {
				return getm((DateTimeObject)obj);
			}
		};
		
		if(express.equals("ss"))
			return new IElement(){
			public String getValue(Object obj) {
				return getss((DateTimeObject)obj);
			}
		};
		
		if(express.equals("s"))
			return new IElement(){
			public String getValue(Object obj) {
				return gets((DateTimeObject)obj);
			}
		};
		
		if(express.equals("HH"))
			return new IElement(){
			public String getValue(Object obj) {
				return getHH((DateTimeObject)obj);
			}
		};
		
		if(express.equals("H"))
			return new IElement(){
			public String getValue(Object obj) {
				return getH((DateTimeObject)obj);
			}
		};
		
		if(express.equals("t"))
			return new IElement(){
			public String getValue(Object obj) {
				return gett((DateTimeObject)obj);
			}
		};
		
		return new StringElement(express);
	}
	
	protected String getyyyy(DateTimeObject date){
		return String.valueOf(date.getYear());
	}
	
	protected String getyy(DateTimeObject date){
		return String.valueOf(date.getYear()).substring(2);
	}
	
	protected String getM(DateTimeObject date){
		return String.valueOf(date.getMonth());
	}
	
	protected String getMM(DateTimeObject date){
		int month = date.getMonth();
		if(month < 10)
			return "0" + month;
		
		return String.valueOf(month);
	}
	
	protected String getMMM(DateTimeObject date){
		return DateFormat.enShortMonth[date.getMonth()-1];
	}
	
	protected String getMMMM(DateTimeObject date){
		return DateFormat.enLongMonth[date.getMonth()-1];
	}
	
	protected String getdd(DateTimeObject date){
		int day = date.getDate();
		if(day < 10)
			return "0" + day;
		
		return String.valueOf(date.getDate());
	}
	
	protected String getd(DateTimeObject date){
		return String.valueOf(date.getDate());
	}
	
	protected String gethh(DateTimeObject date){
		int hh = date.getHours();
		if(hh < 10)
			return "0" + hh;
		
		return String.valueOf(date.getHours());
	}
	
	protected String geth(DateTimeObject date){
		return String.valueOf(date.getHours());
	}
	
	protected String getHH(DateTimeObject date){
		int HH = date.getHours();
		
		if(HH >= 12)
			HH = HH - 12;
		
		if(HH < 10)
			return "0" + HH;
		return String.valueOf(HH);
	}
	
	protected String getH(DateTimeObject date){
		int HH = date.getHours();
		
		if(HH >= 12)
			HH = HH - 12;
		
		return String.valueOf(HH);
	}
	
	protected String getmm(DateTimeObject date){
		int mm = date.getMinutes();
		if(mm < 10)
			return "0" + mm;
		
		return String.valueOf(date.getMinutes());
	}
	
	protected String getm(DateTimeObject date){
		return String.valueOf(date.getMinutes());
	}
	
	protected String getss(DateTimeObject date){
		int ss = date.getSeconds();
		if(ss < 10)
			return "0" + ss;
		
		return String.valueOf(ss);
	}
	
	protected String gets(DateTimeObject date){
		return String.valueOf(date.getSeconds());
	}
	
	protected String gett(DateTimeObject date){
		int hh = date.getHours();
		if(hh <= 12)
			return "AM";
		else
			return "PM";
	}
	
	@Override
	protected String getExpress() {
		return formatMeta.getFormat();
	}

	@Override
	protected String[] getReplaceds() {
		return new String[]{null,formatMeta.getSperatorSymbol(),":"};
	}

	@Override
	protected String[] getSeperators() {
		return new String[]{"(\\s)+?","-",":"};
	}

	@Override
	protected IElement getVarElement(String express) {
		return doOne(express);
	}

	@Override
	protected Object formatArgument(Object obj) throws FormatException{
		return new DateTimeObject(obj);
	}
	
}
