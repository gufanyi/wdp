package xap.mw.core.converter;

import java.util.Calendar;
import java.util.TimeZone;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;

public class CalenderConvertor implements Converter {

    Calendar defaultValue = Calendar.getInstance();

    /**
     * Should we return the default value on conversion errors?
     */
    private boolean useDefault = false;

    @SuppressWarnings("static-access")
    public Object convert(Class clz, Object value) {
	if (value == null) {
	    if (useDefault) {
		return (defaultValue);
	    } else {
		throw new ConversionException("No value specified");
	    }
	}
	try {
	    return (Calendar.getInstance((TimeZone.getDefault()
		    .getTimeZone(value.toString()))));
	} catch (Exception e) {
	    if (useDefault) {
		return (defaultValue);
	    } else {
		throw new ConversionException(e);
	    }
	}

    }

}
