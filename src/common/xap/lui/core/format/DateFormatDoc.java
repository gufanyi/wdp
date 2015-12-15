
package xap.lui.core.format;





public class DateFormatDoc extends FormatDocDetail {

	
	private static final long serialVersionUID = -3406225627984444961L;

	private FormatItem dateUnit1;
	
	private FormatItem dateUnit2;
	
	private FormatItem dateUnit3;
	
	private String delimit1;
	
	private String delimit2;
	
	private String delimit3;
	
	private FormatItem yearType;
	
	private FormatItem monthType;
	
	private FormatItem dayType;
	
	private String expSourceData = "2009-09-08";
	
	public static final String YEAR = "YY";
	
	public static final String MONTH = "MM";
	
	public static final String DAY = "DD";
	
	public static final String LONG_YEAR = "long year";
	
	public static final String SHORT_YEAR = "short year";
	
	public static final String LONG_MONTH = "long month";
	
	public static final String SHORT_MONTH = "short month";
	
	public static final String LONG_ENGLISH_MONTH = "long English month";
	
	public static final String SHORT_ENGLISH_MONTH = "short English month";

	public static final String LONG_DAY = "long day";
	
	public static final String SHORT_DAY = "short day";
	
	private boolean showTime = false;
	
	public DateFormatDoc(){
		dateUnit1 = new FormatItem("YY","��");
		dateUnit2 = new FormatItem("MM","��");
		dateUnit3 =new FormatItem("DD","��");
		delimit1 = "-";
		delimit2 = "-";
		delimit3 = "";
		
		yearType = new FormatItem("long year","�����(YYYY)");
		monthType = new FormatItem("long month","���·�(MM)");
		dayType = new FormatItem("long day","������(DD)");
		expSourceData = "2009-09-08";
		setExpText("2009-09-08");
	}

	public DateFormatMeta toNCFormatMeta() 
	{
		DateFormatMeta formatMeta = new DateFormatMeta();
		
		String unit1FormatString = toFormatString(dateUnit1);
		
		String unit2FormatString = toFormatString(dateUnit2);
		
		String unit3FormatString = toFormatString(dateUnit3);
		
		String delimit1 = getDelimitString(this.delimit1);
		String delimit2 = getDelimitString(this.delimit2);
		String delimit3 = getDelimitString(this.delimit3);
		String formatString = unit1FormatString + delimit1 + unit2FormatString + delimit2 + unit3FormatString + delimit3;
		
		formatMeta.setFormat(formatString);
		
		return formatMeta;
	}
	
	private String getDelimitString(String delimit)
	{
		if(delimit == null)
		{
			return "\"\"";
		}
		if(delimit.equals("-"))
		{
			return "-";
		}
		
		return "\"" + delimit + "\"";
	}
	
	private String toFormatString(FormatItem unit)
	{
		String code = unit.getCode();
		if(code.equals(YEAR))
		{
			return getYearFormatString();
		}
		else if(code.equals(MONTH))
		{
			return getMonthFormatString();
		}
		else if(code.equals(DAY))
		{
			return getDayFormatString();
		}
		else if(code.equals("\"\""))
		{
			return code;
		}
		throw new RuntimeException("unknown date unit type: " + code);
	}
	
	private String getYearFormatString()
	{
		String code = yearType.getCode();
		if(code.equals(LONG_YEAR))
		{
			return DateFormatMeta.yyyy;
		}
		else if (code.equals(SHORT_YEAR))
		{
			return DateFormatMeta.yy;
		}
		throw new RuntimeException("unknown year type!");
	}
	
	private String getMonthFormatString()
	{
		String code = monthType.getCode();
		if(code.equals(LONG_MONTH))
		{
			return DateFormatMeta.MM;
		}
		else if(code.equals(SHORT_MONTH))
		{
			return DateFormatMeta.M;
		}
		else if(code.equals(LONG_ENGLISH_MONTH))
		{
			return DateFormatMeta.MMMM;
		}
		else if(code.equals(SHORT_ENGLISH_MONTH))
		{
			return DateFormatMeta.MMM;
		}
		throw new RuntimeException("unknown month type!");
	}
	
	private String getDayFormatString()
	{
		String code = dayType.getCode();
		if(code.equals(LONG_DAY))
		{
			return DateFormatMeta.dd;
		}
		else if(code.equals(SHORT_DAY))
		{
			return DateFormatMeta.d;
		}
		throw new RuntimeException("unknown day type!");
	}

	
	public FormatItem getDateUnit1() {
		return dateUnit1;
	}



	
	public void setDateUnit1(FormatItem dateUnit1) {
		this.dateUnit1 = dateUnit1;
	}



	
	public FormatItem getDateUnit2() {
		return dateUnit2;
	}



	
	public void setDateUnit2(FormatItem dateUnit2) {
		this.dateUnit2 = dateUnit2;
	}



	
	public FormatItem getDateUnit3() {
		return dateUnit3;
	}



	
	public void setDateUnit3(FormatItem dateUnit3) {
		this.dateUnit3 = dateUnit3;
	}



	
	public FormatItem getYearType() {
		return yearType;
	}



	
	public void setYearType(FormatItem yearType) {
		this.yearType = yearType;
	}



	
	public FormatItem getMonthType() {
		return monthType;
	}



	
	public void setMonthType(FormatItem monthType) {
		this.monthType = monthType;
	}



	
	public FormatItem getDayType() {
		return dayType;
	}



	
	public void setDayType(FormatItem dayType) {
		this.dayType = dayType;
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

	@Override
	public FormatResult format(Object data) throws FormatException{
		DateFormat formater = new DateFormat(this.toNCFormatMeta());
		return formater.format(data);
	}

	@Override
	public Object getExpSourceData() {
		return expSourceData;
	}

	public boolean isShowTime() {
		return showTime;
	}

	public void setShowTime(boolean showTime) {
		this.showTime = showTime;
	}



}
