package xap.lui.core.xml;

import java.lang.reflect.Field;


public class BigDecimalExchang extends PrimitiveTypeExchang implements IPrimitiveTypeExchang {

public void fillFieldValue(Field f, Object o, String str) throws Exception {
	f.set(o, new java.math.BigDecimal(str));
}

	public Class getDealType() {
	return java.math.BigDecimal.class;
}
public Object getObjectValueFromString(String str) {
	return new java.math.BigDecimal(str);
}

public void setArrayPrimitiveValue(
	Object arrayObject,
	int location,
	String value)
	throws Exception {
	java.lang.reflect.Array.set(
		arrayObject,
		location,
		new java.math.BigDecimal(value));
}
}
