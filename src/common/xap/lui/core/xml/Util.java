package xap.lui.core.xml;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

import org.w3c.dom.Document;

public class Util {
	private static Hashtable htClasdDeal = new Hashtable();

	private static Hashtable htClassPropertis = new Hashtable();

	private static Hashtable htClassPrivatePropertis = new Hashtable();

	private static IPrimitiveTypeExchang[] pte = { new BigDecimalExchang(), new BooleanExchang(), new BoolExchang(), new CharacterExchang(), new DoubleExchang(), new Float0Exchang(), new IntegerExchang(), new IntExchang(), new Long0Exchang(), new ShortExchang(), new Short0Exchang(), new StringExchang(), new Char0Exchang(), new ClassTypeExchang() };

	static {
		for (int i = 0; i < pte.length; i++) {
			htClasdDeal.put(pte[i].getDealType(), pte[i]);
		}
	}

	public Util() {
		super();
	}

	public static void fillValue(final Field f, final Object o, final String str) throws Exception {
		AccessController.doPrivileged(new PrivilegedAction() {
			public Object run() {
				try {
					IPrimitiveTypeExchang ipte = (IPrimitiveTypeExchang) htClasdDeal.get(f.getType());
					if (ipte != null) {
						ipte.fillFieldValue(f, o, str);
					}
				} catch (Throwable e) {
				}
				return null;
			}
		});
	}

	public static Class getArrayItemClass(Class arrayClass) throws Exception {
		if (arrayClass == null)
			return null;
		String className = arrayClass.getName();
		int key = className.indexOf("[L");
		if (key >= 0) {
			int lastLoc = className.indexOf(";");
			String classPureName = className.substring(key + 2, lastLoc);
			Class pureClass = Class.forName(classPureName);
			if (key == 0)
				return pureClass;
			int arrayList[] = new int[key];
			for (int i = 0; i < arrayList.length; i++) {
				arrayList[i] = 1;
			}
			return Array.newInstance(pureClass, arrayList).getClass();
		}
		String[] id = { "[B", "[C", "[I", "[J" };
		Class[] type = { byte.class, char.class, int.class, long.class };
		for (int i = 0; i < id.length; i++) {
			key = className.indexOf(id[i]);
			if (key >= 0) {
				Class pureClass = type[i];
				if (key == 0)
					return pureClass;
				int arrayList[] = new int[key];
				for (int j = 0; j < arrayList.length; j++) {
					arrayList[j] = 1;
				}
				return Array.newInstance(pureClass, arrayList).getClass();
			}
		}
		return Class.forName(className);
	}

	public static Class getArrayItemClass(String className) throws Exception {
		int key = className.indexOf("[L");
		if (key >= 0) {
			int lastLoc = className.indexOf(";");
			String classPureName = className.substring(key + 2, lastLoc);
			Class pureClass = Class.forName(classPureName);
			if (key == 0)
				return pureClass;
			int arrayList[] = new int[key];
			for (int i = 0; i < arrayList.length; i++) {
				arrayList[i] = 1;
			}
			return Array.newInstance(pureClass, arrayList).getClass();
		}
		String[] id = { "[B", "[C", "[I", "[J" };
		Class[] type = { byte.class, char.class, int.class, long.class };
		for (int i = 0; i < id.length; i++) {
			key = className.indexOf(id[i]);
			if (key >= 0) {
				Class pureClass = type[i];
				if (key == 0)
					return pureClass;
				int arrayList[] = new int[key];
				for (int j = 0; j < arrayList.length; j++) {
					arrayList[j] = 1;
				}
				return Array.newInstance(pureClass, arrayList).getClass();
			}
		}
		return Class.forName(className);
	}

	public static String getClassName(Class c) throws Exception {
		String str = getTypeName(c);
		int loc = str.lastIndexOf(".");
		if (loc > 0)
			str = str.substring(loc + 1);
		loc = str.indexOf('[');
		if (loc > 0)
			str = str.substring(0, loc);
		return str;
	}

	public static org.w3c.dom.Document getDocument(String str) throws Exception {

		ByteArrayInputStream bis = new ByteArrayInputStream(str.getBytes());
		org.xml.sax.InputSource is = new org.xml.sax.InputSource(bis);
		Document doc = XMLUtil.getDocumentBuilder().parse(is);
		return doc;
	}

	public static Object getObjectValueFromString(Class itemType, String str) {
		IPrimitiveTypeExchang ipte = (IPrimitiveTypeExchang) htClasdDeal.get(itemType);
		if (ipte != null && str != null) {
			return ipte.getObjectValueFromString(str);
		}
		return null;
	}

	public static String getText(org.w3c.dom.Document doc) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintWriter dataOutStream = new PrintWriter(baos);
		XMLUtil.printDOMTree(dataOutStream, doc, 0);
		dataOutStream.flush();
		return new String(baos.toByteArray());
	}

	public static String getTypeName(Class type) {
		if (type.isArray()) {
			try {
				Class cl = type;
				int dimensions = 0;
				while (cl.isArray()) {
					dimensions++;
					cl = cl.getComponentType();
				}
				StringBuffer sb = new StringBuffer();
				sb.append(cl.getName());
				for (int i = 0; i < dimensions; i++) {
					sb.append("[]");
				}
				return sb.toString();
			} catch (Throwable e) {
			}
		}
		return type.getName();
	}

	public static boolean isArrayClass(String classArrayName) throws Exception {
		return classArrayName.startsWith("[");
	}

	public static boolean isPrimitive(Class c) {
		return htClasdDeal.get(c) != null;
	}

	public static boolean isSetPropertyAsAttribClass(Class c) {
		return IPrimitiveTypeExchang.class.isAssignableFrom(c);
	}

	public static XMLProperty[] listAllProperty(Class c) {
		XMLProperty[] xmlP = (XMLProperty[]) htClassPrivatePropertis.get(c);
		if (xmlP != null)
			return xmlP;

		UtilAssistant ua = new UtilAssistant();
		Field cFA[] = ua.getAllFields(c);
		int iNotWriteOut = Modifier.STATIC | Modifier.FINAL;
		ArrayList al = new ArrayList();
		for (int i = 0; i < cFA.length; i++) {
			if ((cFA[i].getModifiers() & iNotWriteOut) != 0)
				continue;
			al.add(new XMLField(cFA[i]));
		}
		xmlP = new XMLProperty[al.size()];
		al.toArray(xmlP);
		htClassPrivatePropertis.put(c, xmlP);
		return xmlP;
	}

	public static XMLProperty[] listProperty(Class c) {
		XMLProperty[] xmlP = (XMLProperty[]) htClassPropertis.get(c);
		if (xmlP != null)
			return xmlP;
		Hashtable hAllField = new Hashtable();
		Vector v = new Vector();

		Method ma[] = c.getMethods();
		for (int i = 0; i < ma.length; i++) {
			if ((ma[i].getModifiers() & Modifier.PUBLIC) == 0)
				continue;
			if ((ma[i].getModifiers() & Modifier.STATIC) == 0)
				continue;
			String name = ma[i].getName();

			if (!name.startsWith("get"))
				continue;
			name = name.substring(3);
			if (ma[i].getReturnType() == void.class)
				continue;
			Class cParamter[] = ma[i].getParameterTypes();

			if (cParamter.length != 0)
				continue;
			Class setterParameter[] = { ma[i].getReturnType() };
			Method mt = null;
			try {
				mt = c.getMethod("set" + name, setterParameter);
			} catch (NoSuchMethodException nsme) {
				continue;
			}
			if (mt.getReturnType() != void.class)
				continue;
			if (hAllField.get(name.toLowerCase()) != null)
				continue;
			XMLMethodProperpty xmlf = new XMLMethodProperpty(mt, ma[i]);
			hAllField.put(name.toLowerCase(), xmlf);
			v.add(xmlf);
		}

		UtilAssistant ua = new UtilAssistant();
		Field cFA[] = ua.getAllFields(c);

		Vector vNew = new Vector();
		Hashtable htProperty = new Hashtable();
		for (int j = 0; j < v.size(); j++) {

			XMLProperty xmlTemp = (XMLProperty) v.elementAt(j);
			htProperty.put(xmlTemp.getName(), Integer.valueOf(j));
		}
		for (int i = 0; i < cFA.length; i++) {

			String str = cFA[i].getName();

			char ca[] = str.toCharArray();
			for (int k = 0; k < ca.length; k++) {
				if (Character.isUpperCase(ca[k])) {
					String strPropertyName = new String(ca, k, ca.length - k);

					Integer iLoc = (Integer) htProperty.get(strPropertyName);
					if (iLoc != null) {

						if ((cFA[i].getModifiers() & Modifier.TRANSIENT) == 0)
							vNew.add(v.elementAt(iLoc.intValue()));
						else
							// Debug.error("REMOVE FIELD:" + cFA[i]);
							v.set(iLoc.intValue(), null);
					}
					break;
				}
			}
		}
		for (int i = 0; i < v.size(); i++) {
			if (v.elementAt(i) != null)
				vNew.add(v.elementAt(i));
		}

		XMLProperty xmlfa[] = new XMLProperty[vNew.size()];
		vNew.copyInto(xmlfa);
		htClassPropertis.put(c, xmlfa);
		return xmlfa;
	}

	public static boolean matchValue(Object o, Object oTher) throws Exception {
		if (o == oTher)
			return true;
		if (o == null) {
			if (oTher != null)
				return false;
		}
		if (oTher == null)
			return false;
		if (isPrimitive(o.getClass())) {
			return ((IPrimitiveTypeExchang) (htClasdDeal).get(o.getClass())).eqauls(o, oTher);
		}
		XMLProperty xmlPa[] = (XMLProperty[]) htClassPropertis.get(o.getClass());
		if (xmlPa == null) {
			xmlPa = listAllProperty(o.getClass());
			htClassPropertis.put(o.getClass(), xmlPa);
		}
		boolean bEqauls = true;
		for (int i = 0; i < xmlPa.length; i++) {
			Object oField = xmlPa[i].getValue(o);
			Object oFieldOther = xmlPa[i].getValue(oTher);
			boolean bEq = matchValue(oField, oFieldOther);
			if (!bEq)
				break;
		}
		return bEqauls;
	}

	public void print(org.w3c.dom.Document doc) {
		PrintWriter dataOutStream = new PrintWriter(System.out);
		XMLUtil.printDOMTree(dataOutStream, doc, 0);
		dataOutStream.close();
	}

	public static void printDoc(org.w3c.dom.Document doc) {
		PrintWriter dataOutStream = new PrintWriter(System.out);
		XMLUtil.printDOMTree(dataOutStream, doc, 0);
		dataOutStream.flush();
	}

	public static String replaceStringfromObject(String sp, int beginfx, String tab, java.lang.Object o) {
		int beginfx2 = 0;
		if (sp.indexOf(tab, beginfx) != -1) {
			int i1_0 = sp.indexOf(tab, beginfx);
			String s1 = sp.substring(0, i1_0);
			int i1_1 = sp.indexOf(tab, i1_0 + 1);
			String value = sp.substring(i1_0 + 1, i1_1);
			String s2 = sp.substring(i1_1 + 1, sp.length());
			Field[] fds = o.getClass().getFields();
			Object vs = null;
			for (int i = 0; i < fds.length; i++) {
				if (fds[i].getName().equalsIgnoreCase(value)) {
					try {
						vs = fds[i].get(o);
						// Debug.debug("被替换成属性：" + fds[i].getName() + " 的值：" +
						// vs);
					} catch (IllegalAccessException e) {
						// Logger.error(e.getMessage(), e);
					}
					break;
				}
			}
			if (vs != null && vs instanceof String) {
				sp = s1 + (String) vs + s2;
			}
			beginfx2 = i1_1 + 1;
		}
		if (sp.indexOf(tab, beginfx2) != -1) {
			sp = replaceStringfromObject(sp, beginfx2, tab, o);
		}
		return sp;
	}

//	public static FDate replaceFDatefromObject(FDate rDate, String dateName, java.lang.Object o) {
//		if (rDate == null)
//			return null;
//		if (rDate.getYear() == 4000) {
//			Field[] fs = o.getClass().getFields();
//			Object vs = null;
//			for (int i = 0; i < fs.length; i++) {
//				if (fs[i].getName().equals(dateName)) {
//					try {
//						vs = fs[i].get(o);
//						// Debug.debug("被替换成属性：" + fs[i].getName() + " 的值：" +
//						// vs);
//						break;
//					} catch (Exception e) {
//
//					}
//				}
//			}
//			if (vs != null) {
////				if (vs instanceof FDate) {
////					rDate = (FDate) vs;
////				}
//			}
//		}
//		return rDate;
//	}

	public static void replaceValue(Object o, String tab, Object bornObj) throws Exception {
		if (o == null)
			return;
		XMLProperty xmlPa[] = (XMLProperty[]) htClassPropertis.get(o.getClass());
		if (xmlPa == null) {
			xmlPa = listAllProperty(o.getClass());
			htClassPropertis.put(o.getClass(), xmlPa);
		}
		for (int i = 0; i < xmlPa.length; i++) {

			if (isPrimitive(xmlPa[i].getType())) {
				if (xmlPa[i].getType() == String.class) {
					String strValue = (String) xmlPa[i].getValue(o);
					if (strValue == null || strValue.equals(""))
						continue;
					if (strValue.indexOf(tab) != -1) {
						String strNewValue = replaceStringfromObject(strValue, 0, tab, bornObj);
						if (strNewValue != null)
							xmlPa[i].fillValue(o, strNewValue);
					}
				}
//				else if (xmlPa[i].getType() == FDate.class) {
//					FDate newdate = replaceFDatefromObject((FDate) xmlPa[i].getValue(o), xmlPa[i].getName(), bornObj);
//					xmlPa[i].fillValue(o, newdate);
//				}
				
				else {
					Object oField = xmlPa[i].getValue(o);
					if (oField != null)
						replaceValue(oField, tab, bornObj);
				}
			}
		}
	}

	public static void replaceValue(Object o, Hashtable bl) throws Exception {
		XMLProperty xmlPa[] = (XMLProperty[]) htClassPropertis.get(o.getClass());
		if (xmlPa == null) {
			xmlPa = listProperty(o.getClass());
			htClassPropertis.put(o.getClass(), xmlPa);
		}
		for (int i = 0; i < xmlPa.length; i++) {

			if (isPrimitive(xmlPa[i].getType())) {
				if (xmlPa[i].getType() == String.class) {
					String strValue = (String) xmlPa[i].getValue(o);
					if (strValue != null) {
						String strNewValue = (String) bl.get(strValue);
						if (strNewValue != null)
							xmlPa[i].fillValue(o, strNewValue);
					}
				} else {
					Object oField = xmlPa[i].getValue(o);
					replaceValue(oField, bl);
				}
			}
		}
	}

	public static void setArrayPrimitiveValue(Object o, Class itemType, int loc, String str) throws Exception {
		IPrimitiveTypeExchang ipte = (IPrimitiveTypeExchang) htClasdDeal.get(itemType);
		if (ipte != null) {
			ipte.setArrayPrimitiveValue(o, loc, str);
		}
	}

	public static Object systemCopyValue(Object oldObj) throws Exception {
		byte[] bold = new byte[] {};
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(oldObj);
		if (baos != null)
			bold = baos.toByteArray();
		oos.close();
		baos.close();
		byte[] bnew = new byte[bold.length];
		System.arraycopy(bold, 0, bnew, 0, bold.length);
		Object onew = null;
		if (bnew != null) {
			ByteArrayInputStream bais = new ByteArrayInputStream(bnew);
			ObjectInputStream ois = new ObjectInputStream(bais);
			onew = ois.readObject();
			ois.close();
			bais.close();
		}
		return onew;
	}
}
