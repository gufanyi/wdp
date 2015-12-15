package xap.lui.core.xml;

import java.lang.reflect.Field;


public class BooleanExchang extends PrimitiveTypeExchang implements IPrimitiveTypeExchang {
	public final static Boolean FALSE_VALUE=new Boolean(false);

public void fillFieldValue(Field f, Object o, String str) throws Exception {
	if (str == null || str.length() == 0)
		f.set(o, FALSE_VALUE);
	else
		f.set(o, Boolean.valueOf(str));
}

	public Class getDealType() {
	return Boolean.class;
}
public Object getObjectValueFromString(String str) {
	return new Boolean(str);
}

public void setArrayPrimitiveValue(
	Object arrayObject,
	int location,
	String str)
	throws Exception {
	if (str == null || str.length() == 0)
		java.lang.reflect.Array.set(arrayObject, location, FALSE_VALUE);

	else
		java.lang.reflect.Array.set(arrayObject, location, Boolean.valueOf(str));
}
}
