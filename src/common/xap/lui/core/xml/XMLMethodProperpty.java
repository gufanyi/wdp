package xap.lui.core.xml;

import java.lang.reflect.*;


public class XMLMethodProperpty implements XMLProperty {
    private Method setMethod = null;

    private Method getMethod = null;

    private static Class ca[] = {};

    public XMLMethodProperpty(Method setterMethod, Method getterMethod) {
        getMethod = getterMethod;
        setMethod = setterMethod;
    }

    public void fillPrimitiveValue(Object o, String strValue) throws Exception {
        Object oa[] = { Util.getObjectValueFromString(getMethod.getReturnType(), strValue) };
        setMethod.invoke(o, oa);
    }

    public void fillValue(Object o, Object oValue) throws Exception {
        Object oa[] = { oValue };
        setMethod.invoke(o, oa);
    }

    
    public String getName() {
        String name = getMethod.getName();
        
        name = name.substring(3);
        return name;
    }

    
    public Class getType() throws Exception {
        return getMethod.getReturnType();
    }

    
    public Object getValue(Object o) throws Exception {
        return getMethod.invoke(o, ca);
    }

    
    public boolean isPrimitive() {
        Class c = getMethod.getReturnType();
        return Util.isPrimitive(c);
    }
}
