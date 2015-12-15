package xap.lui.core.xml;

import java.lang.reflect.Field;


public class Float0Exchang extends PrimitiveTypeExchang implements IPrimitiveTypeExchang 
{

public void fillFieldValue(Field f, Object o, String str) throws Exception {
	f.setFloat(o, Float.parseFloat(str));
}

	public Class getDealType() {
	return float.class;
}
		public Object getObjectValueFromString(String str) {
	if (str != null && str.length() > 0)
		return new Float(str);
	return null;
}

	public void setArrayPrimitiveValue(
	Object arrayObject,
	int location,
	String str)
	throws Exception {
	java.lang.reflect.Array.setFloat(
		arrayObject,
		location,
		Float.parseFloat(str));
}
}
