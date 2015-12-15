package xap.lui.core.xml;

import java.security.*;
import java.lang.reflect.*;


public class XMLField implements XMLProperty {
    private Field field;

    private Object nCurrentObject = null;

    public XMLField(Field f) {
        field = f;
    }

    public void fillPrimitiveValue(Object o, String strValue) throws Exception {
        Util.fillValue(field, o, strValue);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	public void fillValue(final Object o, final Object oValue) throws Exception {
        AccessController.doPrivileged(new PrivilegedAction() {
            public Object run() {
                try {
                    field.set(o, oValue);
                } catch (Exception e) {
                }
                return null;
            }
        });
    }

    
    public String getName() {
        String name = field.getName();
        return name;
    }

    
    public Class getType() throws Exception {
        return field.getType();
    }

    
    public synchronized Object getValue(final Object o) throws Exception {
        AccessController.doPrivileged(new PrivilegedAction() {
            public Object run() {
                try {
                    nCurrentObject = field.get(o);
                } catch (Exception e) {
                }
                return null;
            }
        });
        return nCurrentObject;
    }

    
    public boolean isPrimitive() {
        return Util.isPrimitive(field.getType());
    }

    public void setField(Field f) {
        field = f;
    }
}
