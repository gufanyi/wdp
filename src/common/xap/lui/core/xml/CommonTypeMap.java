package xap.lui.core.xml;

import java.util.HashMap;



public class CommonTypeMap implements ITypeMap {
	private HashMap hmClassToType = new HashMap();
	private HashMap hmTypeToClass = new HashMap();
	private HashMap hmIClassTypeTargMap = new HashMap();

	
	public CommonTypeMap() {
		super();
	}

	public void appendIClassTypeTargMap(IClassTypeTargMap ctm) {
		hmIClassTypeTargMap.put(ctm.getDefinClass(), ctm);
	}

	
	public Class getClassType(String typeName) {
		if (typeName == null)
			return null;
		if (typeName.length() == 0)
			return null;
		Class c = (Class) hmTypeToClass.get(typeName);
		try {
			if (c == null)
				c = Class.forName(typeName);
		} catch (ClassNotFoundException e) {
			//Logger.error(e.getMessage(), e);
		}

		return c;
	}

	
	public String getType(Class classType) {
		String type = (String) hmClassToType.get(classType);
		if (type == null)
			type = classType.getName();
		return type;
	}

	public IClassTypeTargMap getTypeTargMap(Class c) {
		return (IClassTypeTargMap) hmIClassTypeTargMap.get(c);
	}

	public boolean hasClassMap(Class c) {
		return hmClassToType.containsKey(c);
	}

	
	public void setMapRelation(Object oTypeMap[][]) {
		for (int i = 0; i < oTypeMap.length; i++) {
			hmClassToType.put(oTypeMap[i][0], oTypeMap[i][1]);
			hmTypeToClass.put(oTypeMap[i][1], oTypeMap[i][0]);
		}
	}
}
