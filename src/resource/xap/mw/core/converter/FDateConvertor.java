package xap.mw.core.converter;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;

import xap.mw.coreitf.d.FDate;

public class FDateConvertor implements Converter {

    private Object defaultValue = null;

    private boolean useDefault = true;

    public FDateConvertor() {

	this.defaultValue = null;
	this.useDefault = true;

    }

    public FDateConvertor(Object defaultValue) {

	this.defaultValue = defaultValue;
	this.useDefault = true;

    }

    @SuppressWarnings("unchecked")
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
		return FDate.getDate((Date) value);
	    } else if (value instanceof Calendar) {
		return FDate.getDate(((Calendar) value).getTime());
	    }

	    return FDate.fromPersisted(value.toString().intern());
	} catch (Exception e) {
	    if (useDefault) {
		return (defaultValue);
	    } else {
		throw new ConversionException(e);
	    }
	}

    }
}
