package xap.lui.core.xml;

import java.lang.reflect.Field;


public class Short0Exchang extends PrimitiveTypeExchang implements IPrimitiveTypeExchang {

public void fillFieldValue(Field f, Object o, String str) throws Exception {
	f.setShort(o, Short.parseShort(str));
}

public Class getDealType() {
	return short.class;
}
	public Object getObjectValueFromString(String str) {
	if (str != null && str.length() > 0)
		return new Short(str);
	return null;
}

				public void setArrayPrimitiveValue(
	Object arrayObject,
	int location,
	String str)
	throws Exception {
	java.lang.reflect.Array.setShort(
		arrayObject,
		location,
		Short.parseShort(str));
}
}
