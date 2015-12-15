/*
 * 
 *
 * 
 * 
 */
package xap.mw.core.converter;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;

import xap.mw.coreitf.d.FDouble;

/**
 * 
 * 
 * 
 *  
 */
public class FDoubleConvertor implements Converter {

    // ----------------------------------------------------------- Constructors

    /**
     * Create a {@link Converter} that will throw a {@link ConversionException}
     * if a conversion error occurs.
     */
    public FDoubleConvertor() {

	this.defaultValue = null;
	this.useDefault = true;

    }

    /**
     * Create a {@link Converter} that will return the specified default value
     * if a conversion error occurs.
     * 
     * @param defaultValue
     *            The default value to be returned
     */
    public FDoubleConvertor(Object defaultValue) {

	this.defaultValue = defaultValue;
	this.useDefault = true;

    }

    // ----------------------------------------------------- Instance Variables

    /**
     * The default value specified to our Constructor, if any.
     */
    private Object defaultValue = null;

    /**
     * Should we return the default value on conversion errors?
     */
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
     * @exception ConversionException
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

	    if (value instanceof Number) {
		Number tmpNum = (Number) value;
		if (tmpNum.doubleValue() == 0)
		    return FDouble.ZERO_DBL;
		else if (tmpNum.doubleValue() == 1) {
		    return FDouble.ONE_DBL;
		} else
		    return new FDouble(tmpNum.toString().intern());
	    }
	    return new FDouble(value.toString().intern());
	} catch (Exception e) {
	    if (useDefault) {
		return (defaultValue);
	    } else {
		throw new ConversionException(e);
	    }
	}

    }

}
