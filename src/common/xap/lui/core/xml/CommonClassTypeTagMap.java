package xap.lui.core.xml;

import java.util.HashMap;

public class CommonClassTypeTagMap implements IClassTypeTargMap {
	
	public Class m_DefinClass;
	
	private HashMap hmFieldToTag = new HashMap();
	
	private HashMap htTagToField = new HashMap();
	
	private HashMap htFieldSetAsAttrib = new HashMap();

public CommonClassTypeTagMap() {
	super();
}
public void addFieldSetAsAttrib(String fields[]) {
	for (int i = 0; i < fields.length; i++) {
		String field = fields[i];
		htFieldSetAsAttrib.put(field, field);
	}
}
public void addFieldSetAsAttrib(String field) {
	htFieldSetAsAttrib.put(field, field);
}

public java.lang.Class getDefinClass() {
	return m_DefinClass;
}

public String getFieldToTag(String strSourceField) {
	String strTag = (String) hmFieldToTag.get(strSourceField);
	if (strTag == null)
		strTag = strSourceField;
	return strTag;
}

public String getTagToField(String tag) {
	String strField = (String) htTagToField.get(tag);
	if (strField == null)
		strField = tag;
	return strField;
}

public boolean isFieldOutputAsAttribut(String field) {
	return htFieldSetAsAttrib.containsKey(field);
}

public void setDefinClass(java.lang.Class newDefinClass) {
	m_DefinClass = newDefinClass;
}

public void setMapRelation(Object oTypeTagMap[][]) {
	for (int i = 0; i < oTypeTagMap.length; i++) {
		hmFieldToTag.put(oTypeTagMap[i][0], oTypeTagMap[i][1]);
		htTagToField.put(oTypeTagMap[i][1], oTypeTagMap[i][0]);
	}
}
}
