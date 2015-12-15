package xap.mw.core.converter;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;

/**
 * 
 * 
 * User: hey
 *
 *
 * To change this template use File | Settings | File Templates.
 */
public class ObjectConvertor implements Converter {
    public ObjectConvertor() {
	this.defaultValue = null;
	this.useDefault = false;

    }

    public ObjectConvertor(Object defaultValue) {

	this.defaultValue = defaultValue;
	this.useDefault = true;

    }

    private Object defaultValue = null;

    private boolean useDefault = true;

    // --------------------------------------------------------- Public Methods

    /**
     * Convert the specified input object into an output object of the specified
     * type.
     *
     * @param type
     *            Data type to which this value should be converted
     * @param value
     *            The input value to be converted
     *
     * @exception org.apache.commons.beanutils.ConversionException
     *                if conversion cannot be performed successfully
     */
    public Object convert(Class type, Object value) {

	if (value == null) {
	    if (useDefault) {
		return (defaultValue);
	    } else {
		throw new ConversionException("No value specified");
	    }
	}
	try {
	    return value;
	} catch (Exception e) {
	    if (useDefault) {
		return (defaultValue);
	    } else {
		throw new ConversionException(e);
	    }
	}

    }

}
