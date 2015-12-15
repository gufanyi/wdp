package xap.mw.core.converter;

import java.util.Calendar;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;

import xap.mw.coreitf.d.FTime;

public class FTimeConvertor implements Converter {

	private Object defaultValue = null;

	private boolean useDefault = true;

	public FTimeConvertor() {
		this.defaultValue = null;
		this.useDefault = true;
	}

	public FTimeConvertor(Object defaultValue) {
		this.defaultValue = defaultValue;
		this.useDefault = true;
	}

	public Object convert(Class type, Object value) {
		if (value == null) {
			if (useDefault) {
				return (defaultValue);
			} else {
				throw new ConversionException("No value specified");
			}
		}

		try {
			if (value instanceof java.util.Date) {
				return new FTime((java.util.Date) value);
			} else if (value instanceof Calendar) {
				return new FTime(((Calendar) value).getTimeInMillis());
			}

			return (new FTime(value.toString().intern()));
		} catch (Exception e) {
			if (useDefault) {
				return (defaultValue);
			} else {
				throw new ConversionException(e);
			}
		}
	}
}
