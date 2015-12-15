package xap.lui.core.xml;

import java.lang.reflect.*;

public class ClassTypeExchang extends PrimitiveTypeExchang {

public void fillFieldValue(java.lang.reflect.Field f, Object o, String str)
	throws Exception {
	f.set(o, getObjectValueFromString(str));
}

public Class getDealType() {
	return Class.class;
}

public Object getObjectValueFromString(String value) {
	try {
		return Class.forName(value);
	} catch (ClassNotFoundException cnfe) {
		return null;
	}
}
public String getStringValue(Field f, Object o) throws Exception {
	return ((Class) f.get(o)).getName();
}

public void setArrayPrimitiveValue(
	Object arrayObject,
	int location,
	String value)
	throws Exception {
	Array.set(arrayObject, location, getObjectValueFromString(value));
}
}
