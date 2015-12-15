package xap.lui.core.xml;

import java.lang.reflect.Field;


public class StringExchang extends PrimitiveTypeExchang implements IPrimitiveTypeExchang {

public void fillFieldValue(Field f, Object o, String str) throws Exception {
	f.set(o, str);
}

public Class getDealType() {
	return String.class;
}
	public Object getObjectValueFromString(String str) {
 return str;
}

public void setArrayPrimitiveValue(
	Object arrayObject,
	int location,
	String str)
	throws Exception {
	java.lang.reflect.Array.set(arrayObject, location, str);
}
}
