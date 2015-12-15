package xap.lui.core.xml;

import java.lang.reflect.Field;


public class BoolExchang extends PrimitiveTypeExchang implements IPrimitiveTypeExchang {
	public final static Boolean FALSE_VALUE=new Boolean(false);

public void fillFieldValue(Field f, Object o, String str) throws Exception {
	if (str == null || str.length() == 0)
		f.setBoolean(o, false);
	else
		f.setBoolean(o, Boolean.getBoolean(str));
}

	public Class getDealType() {
	return boolean.class;
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
		java.lang.reflect.Array.setBoolean(arrayObject, location, false);

	else
		java.lang.reflect.Array.setBoolean(arrayObject, location, Boolean.getBoolean(str));
}
}
