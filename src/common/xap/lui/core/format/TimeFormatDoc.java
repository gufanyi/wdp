
package xap.lui.core.format;


import xap.mw.coreitf.d.FTime;


public class TimeFormatDoc extends FormatDocDetail {



	
	private static final long serialVersionUID = 5500502352843005819L;
	
	private FormatItem timeFormat;
	
	private FTime expSourceData = new FTime("15:30:25");
	
	public TimeFormatDoc(){
		timeFormat = new FormatItem("hh\":\"mm\":\"ss","15:30:25");
		expSourceData = new FTime("15:30:25");
	}

	public TimeFormatMeta toNCFormatMeta() {
		TimeFormatMeta formatMeta = new TimeFormatMeta();
		if(this.getTimeFormat() != null)
			formatMeta.setFormat(this.getTimeFormat().getCode());
		return formatMeta;
	}

	
	public FormatItem getTimeFormat() {
		return timeFormat;
	}

	
	public void setTimeFormat(FormatItem timeFormat) {
		this.timeFormat = timeFormat;
		if (timeFormat != null && timeFormat.getValue() != null) {
			expSourceData = new FTime(timeFormat.getValue());
		}
	}

	@Override
	public FormatResult format(Object data) throws FormatException {
		TimeFormat formater = new TimeFormat(this.toNCFormatMeta());
		return formater.format(data);
	}

	@Override
	public Object getExpSourceData() {

		return expSourceData;
	}



}
