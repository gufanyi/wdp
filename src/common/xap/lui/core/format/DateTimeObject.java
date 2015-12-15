package xap.lui.core.format;

import java.util.Date;

import xap.mw.coreitf.d.FDate;
import xap.mw.coreitf.d.FDateTime;
import xap.mw.coreitf.d.FLiteralDate;
import xap.mw.coreitf.d.FTime;

public class DateTimeObject {

	private FDateTime date = null;

	private Object orignValue = null;
	private boolean isLocal = true;

	public DateTimeObject(Object orignObj, boolean isLocal) throws FormatException {
		this(orignObj);
		this.isLocal = isLocal;
	}

	public DateTimeObject(Object orignObj) throws FormatException {
		if (orignObj instanceof FDateTime)
			date = (FDateTime) orignObj;
		else if (orignObj instanceof FDate) {
			FDate tmp = (FDate) orignObj;
			date = new FDateTime(tmp.getMillis());
		} else if (orignObj instanceof FTime) {
			FTime tmp = (FTime) orignObj;
			date = new FDateTime(tmp.getMillis());
		} else if (orignObj instanceof Date)
			date = new FDateTime((Date) orignObj);
		else if (orignObj instanceof FLiteralDate) {
			this.isLocal = false;
			date = new FDateTime(((FLiteralDate) orignObj).getMillis());
		} else if (orignObj instanceof String)
			try {
				date = new FDateTime((String) orignObj);
			} catch (Exception e) {
				try {
					date = new FDateTime(new FDate(), new FTime((String) orignObj));
				} catch (Exception e1) {
					try {
						date = new FDateTime(new FDate((String) orignObj), new FTime());
					} catch (Exception e2) {
						date = null;
						throw new IllegalArgumentException();
					}
				}
			}
		else if (orignObj instanceof Number)
			date = new FDateTime(((Number) orignObj).longValue());
		else
			throw new IllegalArgumentException();
	}

	public int getYear() {
		if (isLocal)
			return date.getLocalYear();
		else
			return date.getYear();
	}

	public int getMonth() {
		if (isLocal)
			return date.getLocalMonth();
		else
			return date.getMonth();
	}

	public int getDate() {
		if (isLocal)
			return date.getLocalDay();
		else
			return date.getDay();
	}

	public int getHours() {
		if (isLocal)
			return date.getLocalHour();
		else
			return date.getHour();
	}

	public int getMinutes() {
		if (isLocal)
			return date.getLocalMinute();
		else
			return date.getMinute();
	}

	public int getSeconds() {
		return date.getSecond();
	}

	public Object getOrignValue() {
		return orignValue;
	}

	public void setOrignValue(Object orignValue) {
		this.orignValue = orignValue;
	}

}
