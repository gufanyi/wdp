package xap.lui.core.xml;

public interface ITypeMap {
	public Class getClassType(String typeName);

	public String getType(Class classType);

	IClassTypeTargMap getTypeTargMap(Class c);

	boolean hasClassMap(Class c);
}
