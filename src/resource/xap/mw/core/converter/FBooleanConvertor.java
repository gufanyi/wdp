package xap.mw.core.converter;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;

import xap.mw.coreitf.d.FBoolean;

/**
 * 
 * 
 * 
 * 
 */
public class FBooleanConvertor implements Converter {
    public FBooleanConvertor() {

	this.defaultValue = null;
	this.useDefault = true;

    }

    public FBooleanConvertor(Object defaultValue) {

	this.defaultValue = defaultValue;
	this.useDefault = true;

    }

    private Object defaultValue = null;

    private boolean useDefault = true;

    public Object convert(Class type, Object value) {

	if (value == null) {
	    return null;
	}

	try {
	    return (FBoolean.valueOf(value.toString()));
	} catch (Exception e) {
	    if (useDefault) {
		return (defaultValue);
	    } else {
		throw new ConversionException(e);
	    }
	}

    }

}
