package xap.lui.core.xml;

import java.lang.reflect.Field;


public class DoubleExchang extends PrimitiveTypeExchang implements IPrimitiveTypeExchang {

public void fillFieldValue(Field f, Object o, String str) throws Exception {
	f.set(o, Double.valueOf(str));
}

	public Class getDealType() {
	return Double.class;
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
	java.lang.reflect.Array.set(
		arrayObject,
		location,
		new Double(str));
}
}
