package xap.lui.core.xml;

import java.lang.reflect.*;


public interface IPrimitiveTypeExchang {
    public boolean eqauls(Object o1, Object o2);

    void fillFieldValue(Field f, Object o, String str) throws Exception;

    Class getDealType();

    public Object getObjectValueFromString(String value);

    public String getStringValue(Field f, Object o) throws Exception;

    void setArrayPrimitiveValue(Object arrayObject, int location, String value) throws Exception;
}
