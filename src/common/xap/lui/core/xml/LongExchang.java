package xap.lui.core.xml;

import java.lang.reflect.Field;


public class LongExchang extends PrimitiveTypeExchang implements IPrimitiveTypeExchang {

public void fillFieldValue(Field f, Object o, String str) throws Exception {
	f.set(o, Long.valueOf(str));
}

	public Class getDealType() {
	return Long.class;
}
	public Object getObjectValueFromString(String str) {
	if (str != null && str.length() > 0)
		return new Long(str);
	return null;
}

public void setArrayPrimitiveValue(
	Object arrayObject,
	int location,
	String str)
	throws Exception {
	java.lang.reflect.Array.set(arrayObject, location, Long.valueOf(str));
}
}
