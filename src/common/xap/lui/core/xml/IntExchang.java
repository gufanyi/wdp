package xap.lui.core.xml;

import java.lang.reflect.Field;


public class IntExchang extends PrimitiveTypeExchang implements IPrimitiveTypeExchang {

public void fillFieldValue(Field f, Object o, String str) throws Exception {
	f.setInt(o, Integer.parseInt(str));
}

	public Class getDealType() {
	return int.class;
}
	public Object getObjectValueFromString(String str) {
	if (str != null && str.length() > 0)
	{
	    if(str.indexOf('.')>0)
	    	return Integer.valueOf((int)Double.parseDouble(str));
		return Integer.valueOf(str);
	}
	return null;
}

public void setArrayPrimitiveValue(
	Object arrayObject,
	int location,
	String str)
	throws Exception {
	java.lang.reflect.Array.setInt(arrayObject, location, Integer.parseInt(str));
}
}
