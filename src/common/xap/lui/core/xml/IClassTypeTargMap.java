package xap.lui.core.xml;


public interface IClassTypeTargMap {

Class getDefinClass();

String getFieldToTag(String strSourceField);

String getTagToField(String tag);
public boolean isFieldOutputAsAttribut(String field);
}
