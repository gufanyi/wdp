package xap.lui.core.xml;

import java.lang.reflect.Field;


public class Double0Exchang extends PrimitiveTypeExchang implements IPrimitiveTypeExchang {

public Double0Exchang() {
	super();
}

public void fillFieldValue(Field f, Object o, String str) throws Exception {
	f.setDouble(o, Double.parseDouble(str));
}

	public Class getDealType() {
	return double.class;
}
	public Object getObjectValueFromString(String str) {
	if (str != null && str.length() > 0)
		return new Double(str);
	return null;
}

public void setArrayPrimitiveValue(
	Object arrayObject,
	int location,
	String str)
	throws Exception {
	java.lang.reflect.Array.setDouble(
		arrayObject,
		location,
		Double.parseDouble(str));
}
}
