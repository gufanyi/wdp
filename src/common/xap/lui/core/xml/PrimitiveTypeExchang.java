package xap.lui.core.xml;

import java.lang.reflect.Field;


public abstract class PrimitiveTypeExchang implements IPrimitiveTypeExchang {

public String getStringValue(Field f, Object o) throws Exception {
	return f.get(o)+"";
}

public boolean eqauls(Object o1, Object o2) {
	return o1.equals(o2);
}
}
