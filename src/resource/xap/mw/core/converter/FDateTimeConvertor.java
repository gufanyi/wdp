/*
 * 
 *
 * 
 * 
 */
package xap.mw.core.converter;

import java.util.Calendar;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;

import xap.mw.coreitf.d.FDateTime;

/**
 * 
 * 
 *
 * 
 * 
 */
public class FDateTimeConvertor implements Converter {

    private Object defaultValue = null;

    private boolean useDefault = true;

    /**
     * Create a {@link Converter} that will throw a {@link ConversionException}
     * if a conversion error occurs.
     */
    public FDateTimeConvertor() {

	this.defaultValue = null;
	this.useDefault = true;

    }

    public FDateTimeConvertor(Object defaultValue) {

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
		return new FDateTime((java.util.Date) value);
	    } else if (value instanceof Calendar) {
		return new FDateTime(((Calendar) value).getTimeInMillis());
	    } else if (value instanceof FDateTime) {
		return (FDateTime) value;
	    }

	    return (new FDateTime(value.toString().intern()));
	} catch (Exception e) {
	    if (useDefault) {
		return (defaultValue);
	    } else {
		throw new ConversionException(e);
	    }
	}

    }

}
