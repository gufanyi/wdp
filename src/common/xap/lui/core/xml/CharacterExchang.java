package xap.lui.core.xml;

import java.lang.reflect.*;


public class CharacterExchang
	extends PrimitiveTypeExchang
	implements IPrimitiveTypeExchang {

public void fillFieldValue(Field f, Object o, String str) throws Exception {
	if (str == null || str.length() == 0)
		f.set(o, Character.valueOf(' '));
	else
		f.set(o, Character.valueOf(str.charAt(0)));
}

	public Class getDealType() {
	return Character.class;
}
public Object getObjectValueFromString(String str) {
	if (str != null && str.length() > 0)
		return Character.valueOf(str.charAt(0));
	return null;
}

public void setArrayPrimitiveValue(
	Object arrayObject,
	int location,
	String str)
	throws Exception {
	if (str == null || str.length() == 0)
		java.lang.reflect.Array.set(arrayObject, location, Character.valueOf(' '));
	else
		java.lang.reflect.Array.set(
			arrayObject,
			location,
			Character.valueOf(str.charAt(0)));
}
}
