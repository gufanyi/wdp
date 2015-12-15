package xap.lui.core.xml;


public interface XMLProperty {
    public void fillPrimitiveValue(Object o, String strValue) throws Exception;

    public void fillValue(Object o, Object oValue) throws Exception;

    public String getName();

    public Class getType() throws Exception;

    public Object getValue(Object o) throws Exception;

    public boolean isPrimitive();
}
