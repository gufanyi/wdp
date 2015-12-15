package xap.lui.core.xml;

import java.io.File;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLExchang {

	private boolean m_beExchangPrivateType = true;

	public boolean m_beNotOutputNullValue;

	public boolean m_beOutputNullArrayList;

	public ITypeMap m_TypeMap;

	public boolean m_beKeepLocation;

	private boolean m_AllowNoField = false;

	public boolean m_beReplaceArrayTagAsType;

	public boolean m_beOutputArrayLength;

	public XMLExchang() {
		super();
	}

	private void appendChild(Document doc, Node parent, Node child) {
		if (parent == null)
			doc.appendChild(child);
		else
			parent.appendChild(child);
	}

	public Document getDocument(Object o) throws Exception {
		Document doc = XMLUtil.newDocument();
		String tName = o.getClass().getName();
		String name = tName.substring(tName.lastIndexOf(".") + 1);
		Element nod = doc.createElement(name);
		Node root = getDocument(doc, nod, o, 0, o.getClass(), null, false);
		doc.appendChild(root);
		return doc;
	}

	public Document getDocument(String methodName, Object[] paramterObject) throws Exception {
		Document doc = XMLUtil.newDocument();
		for (int i = 0; i < paramterObject.length; i++) {
			Element nod = doc.createElement(methodName);
			Node root = getDocument(doc, nod, paramterObject[i], 0, paramterObject[i].getClass(), null, false);
			doc.appendChild(root);
		}
		return doc;
	}

	public Node getDocument(Document doc, Element nod, Object o, int deepSet, Class defaultClass, String arrayName, boolean hasChangedTagForArrayItem) throws Exception {

		int deep = deepSet + 1;
		if (deep > 100)
			throw new Exception("嵌套深度太深，可能存在对象循环指定的问题");

		if (o == null) {
			return setNullFlag(nod, defaultClass);
		}

		if (Util.isPrimitive(o.getClass())) {
			setNodeTypeAttribute(nod, defaultClass, o, hasChangedTagForArrayItem);
			if (o.getClass() == Class.class)
				((Element) nod).appendChild(doc.createTextNode(((Class) o).getName()));
			else
				((Element) nod).appendChild(doc.createTextNode("" + o));
			return nod;
		}

		if (o.getClass() == ArrayList.class) {
			ArrayList al = (ArrayList) o;
			if (arrayName == null)
				arrayName = "NODE";
			String localArrayName = arrayName;
			for (int i = 0; i < al.size(); i++) {
				Object oElement = al.get(i);
				if (isNotOutputNullValue() && oElement == null)
					continue;
				Element arrayList = doc.createElement(localArrayName);

				getDocument(doc, arrayList, oElement, deep, Object.class, localArrayName, false);

				appendChild(doc, nod, arrayList);
			}
			return nod;
		}

		if (o.getClass().isArray()) {

			if (arrayName == null)
				arrayName = "NODE";
			Element nodArray = doc.createElement(arrayName);
			setNodeTypeAttribute(nodArray, defaultClass, o, hasChangedTagForArrayItem);

			int length = Array.getLength(o);
			if (isOutputNullArrayList())
				nodArray.setAttribute("arrayLength", "" + length);
			appendChild(doc, nod, nodArray);

			Class itemType = Util.getArrayItemClass(o.getClass());
			boolean bJumpedNullComponent = false;
			for (int j = 0; j < length; j++) {

				Object arrayItem = Array.get(o, j);
				if (isNotOutputNullValue() && arrayItem == null) {
					bJumpedNullComponent = true;
					continue;
				}

				String localArrayName = arrayName;

				boolean hasChangedTagForArrayItem0 = false;
				if (isReplaceArrayTagAsType() && getTypeMap() != null && arrayItem != null) {
					if (getTypeMap().hasClassMap(arrayItem.getClass())) {
						localArrayName = getTypeMap().getType(arrayItem.getClass());

						if (!localArrayName.equals(arrayName))
							hasChangedTagForArrayItem0 = true;
					}
				}

				Element arrayElement = doc.createElement(localArrayName);

				getDocument(doc, arrayElement, arrayItem, deep, itemType, localArrayName, hasChangedTagForArrayItem0);

				if (arrayItem != null && arrayItem.getClass().isArray()) {
					appendChild(doc, nodArray, arrayElement.getChildNodes().item(0));
				} else {
					appendChild(doc, nodArray, arrayElement);
				}
				if (bJumpedNullComponent && isKeepLocation())
					arrayElement.setAttribute("arrayLoc", "" + j);
			}
		} else {

			setNodeTypeAttribute(nod, defaultClass, o, hasChangedTagForArrayItem);
			XMLProperty xmlPa[] = null;
			if (isOutputAsField())
				xmlPa = Util.listAllProperty(o.getClass());
			else
				xmlPa = Util.listProperty(o.getClass());

			for (int i = 0; i < xmlPa.length; i++) {
				Object oF = xmlPa[i].getValue(o);
				if (isNotOutputNullValue()) {

					if (oF == null)
						continue;
					if (oF.getClass() == ArrayList.class) {
						if (((ArrayList) oF).size() == 0)
							continue;
					}
				}
				if (oF instanceof Hashtable && xmlPa[i].getName().equals("ExternalAttribute")) {

					Hashtable h = (Hashtable) oF;
					Enumeration hKeys = h.keys();
					while (hKeys.hasMoreElements()) {
						Object key = hKeys.nextElement();
						nod.setAttribute(key.toString(), h.get(key).toString());
					}

					continue;
				}
				String name = xmlPa[i].getName();
				String strTagName = getTag(o.getClass(), name);
				Element child = doc.createElement(strTagName);

				if (Map.class.isAssignableFrom(xmlPa[i].getType())) {

					Map m = (Map) oF;

					Set set = m.keySet();
					Object oa[] = set.toArray();
					for (int j = 0; j < oa.length; j++) {
						Object ov = m.get(oa[j]);
						child.setAttribute(oa[j].toString(), ov.toString());
					}
					appendChild(doc, nod, child);
					continue;
				}

				setNodeTypeAttribute(child, xmlPa[i].getType(), oF, false);
				if (oF != null && Util.isPrimitive(oF.getClass())) {
					if (isFieldSetAsAttrib(o.getClass(), name))
						nod.setAttribute(strTagName, "" + oF);
					else {
						child.appendChild(doc.createTextNode("" + oF));
						appendChild(doc, nod, child);
					}
				} else {

					if (oF != null && oF.getClass().isArray()) {
						getDocument(doc, nod, oF, deep, xmlPa[i].getType(), strTagName, hasChangedTagForArrayItem);
					} else {
						getDocument(doc, child, oF, deep, xmlPa[i].getType(), null, hasChangedTagForArrayItem);
						appendChild(doc, nod, child);
					}
				}
			}
		}
		return nod;
	}

	public Object getJavaObjectFromDocument(Document doc, Class c) throws Exception {
		Node node = doc.getDocumentElement();

		Object oe = revertDocument((Element) node, c, null);
		return oe;
	}

	public Object getJavaObjectFromFile(String fileName, Class c) throws Exception {

		Document doc = XMLUtil.getDocumentBuilder().parse(fileName);

		Node node = doc.getDocumentElement();

		Object oe = revertDocument((Element) node, c, null);
		return oe;
	}

	public Object getJavaObjectFromNode(Node node, Class c) throws Exception {

		String name = Util.getClassName(c);
		Object oe = revertDocument((Element) node, c, name);
		return oe;
	}

	public ITypeMap getTypeMap() {
		return m_TypeMap;
	}

	public boolean isAllowNoField() {
		return m_AllowNoField;
	}

	public boolean isExchangPrivateType() {
		return m_beExchangPrivateType;
	}

	public boolean isKeepLocation() {
		return m_beKeepLocation;
	}

	public boolean isNotOutputNullValue() {
		return m_beNotOutputNullValue;
	}

	private boolean isNullNode(Node cNode) {
		Node valueNode = cNode.getAttributes().getNamedItem("value");
		if (valueNode == null)
			return false;
		if (valueNode.getNodeValue().equals("null"))
			return true;
		return false;
	}

	public boolean isOutputArrayLength() {
		return m_beOutputArrayLength;
	}

	public boolean isOutputNullArrayList() {
		return m_beOutputNullArrayList;
	}

	public boolean isReplaceArrayTagAsType() {
		return m_beReplaceArrayTagAsType;
	}

	private Object revertArray(Node cNode, Class defautClass, String nodeName) throws Exception {
		if (cNode == null)
			return null;
		String arrayName = defautClass.getName();
		Class arrayItemClass = Util.getArrayItemClass(arrayName);
		NodeList nl = cNode.getChildNodes();
		Node nod = cNode.getAttributes().getNamedItem("arrayLength");
		int length = 0;
		if (nod != null) {
			String strLength = nod.getNodeValue();
			length = Integer.parseInt(strLength);
		}

		java.util.Vector vNl = new java.util.Vector();
		for (int i = 0; i < nl.getLength(); i++) {

			if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
				vNl.addElement(nl.item(i));
			}
		}

		if (length < vNl.size()) {
			length = vNl.size();
		}
		Object o = Array.newInstance(arrayItemClass, length);
		if (Util.isPrimitive(arrayItemClass)) {
			for (int i = 0; i < vNl.size(); i++) {
				Node item = (Node) vNl.elementAt(i);
				String str = item.getChildNodes().item(0).getNodeValue().trim();
				Util.setArrayPrimitiveValue(o, arrayItemClass, i, str);
			}
		} else {
			for (int i = 0; i < vNl.size(); i++) {
				Node item = (Node) vNl.elementAt(i);
				Class thisItemClass = arrayItemClass;
				if (isReplaceArrayTagAsType() && !item.getNodeName().equals(nodeName)) {
					thisItemClass = getTypeMap().getClassType(item.getNodeName());
				}
				Array.set(o, i, revertDocument((Element) item, thisItemClass, nodeName));
			}
		}
		return o;
	}

	public Object revertDocument(Element item, Class defaultClass, String nodeName) throws Exception {

		if (isNullNode(item))
			return null;

		boolean bChildClass = true;
		String className = item.getAttribute("ClassType");
		Class cThisClass = null;
		if (getTypeMap() != null) {
			String typeName = item.getAttribute("TypeMap_Type");
			cThisClass = getTypeMap().getClassType(typeName);
			if (cThisClass != null)
				className = cThisClass.getName();
		}
		if (className == null || className.length() == 0) {
			bChildClass = false;
			className = defaultClass.getName();
		}

		if (Util.isArrayClass(className)) {
			Class classType = defaultClass;
			if (bChildClass)
				classType = Class.forName(className);
			return revertArray(item, classType, nodeName);
		}

		Class classType = defaultClass;
		if (bChildClass)
			classType = Class.forName(className);
		if (Util.isPrimitive(classType)) {

			NodeList nlc = item.getChildNodes();
			if (nlc.getLength() > 0) {
				Node nl = nlc.item(0);
				if (nl != null)
					return Util.getObjectValueFromString(classType, nlc.item(0).getNodeValue());
			}
			return null;
		}
		Object o = classType.newInstance();

		if (Map.class.isAssignableFrom(classType)) {
			org.w3c.dom.NamedNodeMap nnm = item.getAttributes();
			Map map = (Map) o;
			for (int i = 0; i < nnm.getLength(); i++) {
				map.put(nnm.item(i).getNodeName(), nnm.item(i).getNodeValue());
			}
			return o;
		}
		if (Util.isPrimitive(classType)) {
			throw new Exception("Parse Error");
		}
		NodeList nl = item.getChildNodes();
		{
			XMLProperty[] fa = null;
			if (isOutputAsField())
				fa = Util.listAllProperty(classType);
			else
				fa = Util.listProperty(classType);

			Hashtable hAllInteralAttribute = new Hashtable();
			for (int i = 0; i < fa.length; i++) {
				hAllInteralAttribute.put(fa[i].getName(), fa[i]);
			}

			if (hAllInteralAttribute.containsKey(STR_ExternalAttribute)) {
				NamedNodeMap nnm = item.getAttributes();
				XMLProperty xmlProperty = (XMLProperty) hAllInteralAttribute.get(STR_ExternalAttribute);
				Hashtable htExternalProperty = (Hashtable) xmlProperty.getValue(o);
				for (int i = 0; i < nnm.getLength(); i++) {
					Node attribute = nnm.item(i);
					if (hAllInteralAttribute.containsKey(attribute.getNodeName()))
						continue;
					htExternalProperty.put(attribute.getNodeName(), attribute.getNodeValue());
				}
			}
			for (int i = 0; i < fa.length; i++) {
				Element cNode = null;

				String strFieldTagName = getTag(classType, fa[i].getName());
				if (Util.isPrimitive(fa[i].getType())) {
					if (item.getAttributes().getNamedItem(strFieldTagName) != null) {
						String str = item.getAttribute(strFieldTagName);
						fa[i].fillPrimitiveValue(o, str);
						continue;
					}
				}
				for (int j = 0; j < nl.getLength(); j++) {
					if (strFieldTagName.equalsIgnoreCase(nl.item(j).getNodeName())) {
						cNode = (Element) nl.item(j);
						break;
					}
				}

				if (cNode == null && !(fa[i].getType().isArray())) {
					if (isAllowNoField())
						continue;
					if (fa[i].getName().equals(STR_ExternalAttribute))
						continue;
					throw new Exception("缺少：" + fa[i].getName() + " 的描述");
				}
				if (fa[i].getType().isArray()) {

					Object oa = revertArray(cNode, fa[i].getType(), fa[i].getName());
					fa[i].fillValue(o, oa);
				} else {
					if (Util.isPrimitive(fa[i].getType())) {
						NodeList nlc = cNode.getChildNodes();
						boolean bSetted = false;
						if (cNode.getAttributes() != null) {
							Node n = cNode.getAttributes().getNamedItem("value");
							if (n != null && "null".equals(n.getNodeValue())) {
								fa[i].fillValue(o, null);
								bSetted = true;
							}
						}
						if (!bSetted) {
							if (nlc.item(0) == null)
								fa[i].fillPrimitiveValue(o, "");
							else
								try {
									fa[i].fillPrimitiveValue(o, nlc.item(0).getNodeValue());
								} catch (Exception e) {
									// Logger.error(e.getMessage(), e);
								}
						}
					} else {
						Object os = revertDocument(cNode, fa[i].getType(), nodeName);
						fa[i].fillValue(o, os);
					}
				}
			}
		}
		return o;
	}

	public void saveAsXmlFile(String fileName, Object o, Class defaultClass) throws Exception {
		Document doc = getDocument(o);

		String pathName = fileName;
		String tmpDirectory = "";

		pathName = pathName.replace('\\', '/');
		pathName = pathName.substring(0, pathName.lastIndexOf("/"));

		StringTokenizer st = new StringTokenizer(pathName, "/");
		while (st.hasMoreTokens()) {
			tmpDirectory += st.nextToken() + "/";
			File f = new File(tmpDirectory);
			if (!f.canRead()) {
				f.mkdir();
			}
		}

		java.io.FileWriter fileOutStream = new java.io.FileWriter(fileName);
		PrintWriter dataOutStream = new PrintWriter(fileOutStream);
		XMLUtil.printDOMTree(dataOutStream, doc, 0);
		dataOutStream.close();
		fileOutStream.close();
	}

	public void setAllowNoField(boolean newAllowNoField) {
		m_AllowNoField = newAllowNoField;
	}

	public void setExchangPrivateType(boolean newExchangPrivateType) {
		m_beExchangPrivateType = newExchangPrivateType;
	}

	public void setKeepLocation(boolean newKeepLocation) {
		m_beKeepLocation = newKeepLocation;
	}

	private void setNodeTypeAttribute(Element nod, Class defaultClass, Object o, boolean changedTagForArray) {
		if (o == null)
			return;
		if (o.getClass() == defaultClass)
			return;

		if (Util.isPrimitive(defaultClass))
			return;
		if (getTypeMap() != null) {

			if (!changedTagForArray)
				nod.setAttribute("TypeMap_Type", getTypeMap().getType(o.getClass()));
		} else if (defaultClass == Class.class) {
			nod.setAttribute("CType", ((Class) o).getName() + "");
		} else
			nod.setAttribute("ClassType", o.getClass().getName() + "");

	}

	public void setNotOutputNullValue(boolean newNotOutputNullValue) {
		m_beNotOutputNullValue = newNotOutputNullValue;
	}

	private Node setNullFlag(Element node, Class defaultClass) {
		if (!defaultClass.isArray())
			node.setAttribute("value", "null");
		else
			node.setAttribute("arrayValue", "null");
		return node;

	}

	public void setOutputArrayLength(boolean newOutputArrayLength) {
		m_beOutputArrayLength = newOutputArrayLength;
	}

	public void setOutputNullArrayList(boolean newOutputNullArrayList) {
		m_beOutputNullArrayList = newOutputNullArrayList;
	}

	public void setReplaceArrayTagAsType(boolean newReplaceArrayTagAsType) {
		m_beReplaceArrayTagAsType = newReplaceArrayTagAsType;
	}

	public void setTypeMap(ITypeMap newTypeMap) {
		m_TypeMap = newTypeMap;
	}

	public boolean m_beOutputAsField = false;

	public final static String STR_ExternalAttribute = "ExternalAttribute";

	public Object getJavaObjectFromString(String xmlString, Class c) throws Exception {

		org.xml.sax.InputSource in = new org.xml.sax.InputSource(xmlString);
		Document doc = XMLUtil.getDocumentBuilder().parse(in);

		Node node = doc.getDocumentElement();

		Object oe = revertDocument((Element) node, c, null);
		return oe;
	}

	private String getTag(Class c, String strFieldName) {
		if (getTypeMap() == null)
			return strFieldName;
		if (getTypeMap().getTypeTargMap(c) == null)
			return strFieldName;
		return getTypeMap().getTypeTargMap(c).getFieldToTag(strFieldName);
	}

	private boolean isFieldSetAsAttrib(Class c, String fieldName) {
		if (getTypeMap() == null)
			return false;
		if (getTypeMap().getTypeTargMap(c) == null)
			return false;
		return getTypeMap().getTypeTargMap(c).isFieldOutputAsAttribut(fieldName);
	}

	public boolean isOutputAsField() {
		return m_beOutputAsField;
	}

	public String saveAsXmlString(Object o, Class defaultClass) throws Exception {
		Document doc = getDocument(o);

		java.io.StringWriter strOutStream = new java.io.StringWriter();
		PrintWriter dataOutStream = new PrintWriter(strOutStream);
		XMLUtil.printDOMTree(dataOutStream, doc, 0);
		dataOutStream.close();
		strOutStream.close();
		return strOutStream.toString();

	}

	public void setOutputAsField(boolean newOutputAsField) {
		m_beOutputAsField = newOutputAsField;
	}
}
