package xap.lui.core.xml;

import java.lang.reflect.Array;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DefaultXmlToObject {
	private boolean m_AllowNoField = false;

	public static Object getJavaObjectFromDocument(Document doc, boolean allowNoField, Class c) throws Exception {
		DefaultXmlToObject xto = new DefaultXmlToObject();
		xto.setAllowNoField(allowNoField);
		Node node = doc.getDocumentElement();

		Object oe = xto.revertDocument(node, c, null);
		return oe;
	}

	public static Object getJavaObjectFromFile(String fileName, boolean allowNoField, Class c) throws Exception {

		Document doc = XMLUtil.getDocumentBuilder().parse(fileName);
		DefaultXmlToObject xto = new DefaultXmlToObject();
		xto.setAllowNoField(allowNoField);
		Node node = doc.getDocumentElement();

		Object oe = xto.revertDocument(node, c, null);
		return oe;
	}

	public static Object getJavaObjectFromNode(Node node, boolean allowNoField, Class c) throws Exception {
		DefaultXmlToObject xto = new DefaultXmlToObject();
		xto.setAllowNoField(allowNoField);
		String name = Util.getClassName(c);
		Object oe = xto.revertDocument(node, c, name);
		return oe;
	}

	public boolean isAllowNoField() {
		return m_AllowNoField;
	}

	private boolean isNullNode(Node cNode) {
		Node valueNode = cNode.getAttributes().getNamedItem("value");
		if (valueNode == null)
			return false;
		if (valueNode.getNodeValue().equals("null"))
			return true;
		return false;
	}

	private boolean isNullNodeArray(Node cNode) {
		Node valueNode = cNode.getAttributes().getNamedItem("arrayValue");
		if (valueNode == null)
			return false;
		if (valueNode.getNodeValue().equals("null"))
			return true;
		return false;
	}

	private Object revertArray(Node cNode, Class defautClass, String nodeName) throws Exception {

		String arrayName = defautClass.getName();
		Class arrayItemClass = Util.getArrayItemClass(arrayName);
		NodeList nl = cNode.getChildNodes();
		java.util.Vector vNl = new java.util.Vector();
		for (int i = 0; i < nl.getLength(); i++) {
			if (nl.item(i).getNodeName().equals(nodeName)) {
				vNl.addElement(nl.item(i));
			}
		}
		if (vNl.size() == 1)
			if (isNullNodeArray((Node) vNl.elementAt(0)))
				return null;
		Object o = Array.newInstance(arrayItemClass, vNl.size());
		if (Util.isPrimitive(arrayItemClass)) {
			for (int i = 0; i < vNl.size(); i++) {
				Node item = (Node) vNl.elementAt(i);
				String str = item.getChildNodes().item(0).getNodeValue().trim();
				Util.setArrayPrimitiveValue(o, arrayItemClass, i, str);
			}
		} else {
			for (int i = 0; i < vNl.size(); i++) {
				Node item = (Node) vNl.elementAt(i);
				Array.set(o, i, revertDocument(item, arrayItemClass, nodeName));
			}
		}
		return o;
	}

	public Object revertDocument(Node item, Class defaultClass, String nodeName) throws Exception {

		if (isNullNode(item))
			return null;

		boolean bChildClass = true;
		String className = ((Element) item).getAttribute("ClassType");
		if (className == null || className.length() == 0) {
			bChildClass = false;
			className = defaultClass.getName();
		}

		if (Util.isArrayClass(className)) {
			Class classType = defaultClass;
			if (bChildClass)
				classType = defaultClass.getClassLoader().loadClass(className);
			return revertArray(item, classType, nodeName);
		}

		Class classType = defaultClass;
		if (bChildClass)
			classType = defaultClass.getClassLoader().loadClass(className);
		if (Util.isPrimitive(classType)) {

			NodeList nlc = item.getChildNodes();
			if (nlc.getLength() > 0) {
				Node nl = nlc.item(0);
				if (nl != null)
					return Util.getObjectValueFromString(classType, nlc.item(0).getNodeValue());
			}
			return null;
		}
		if (classType == null)
			throw new Exception("Class can't be found!Class:" + className);

		Object o = classType.newInstance();
		if (Util.isPrimitive(classType)) {
			throw new Exception("Parse Error");
		}
		NodeList nl = item.getChildNodes();
		{
			XMLProperty[] fa = Util.listAllProperty(classType);
			for (int i = 0; i < fa.length; i++) {
				Node cNode = null;
				for (int j = 0; j < nl.getLength(); j++) {
					if (fa[i].getName().equalsIgnoreCase(nl.item(j).getNodeName())) {
						cNode = nl.item(j);
						break;
					}
				}

				if (cNode == null && Util.isPrimitive(fa[i].getType())) {
					Node nn = item.getAttributes().getNamedItem(fa[i].getName());
					if (nn != null) {
						try {
							fa[i].fillPrimitiveValue(o, nn.getNodeValue());
						} catch (Exception e) {
							// Logger.error(e.getMessage(), e);
						}
					}
					continue;
				}

				if (cNode == null && !(fa[i].getType().isArray())) {
					if (isAllowNoField())
						continue;
					else
						throw new Exception("缺少：" + fa[i].getName() + " 的描述");
				}
				if (fa[i].getType().isArray()) {

					Object oa = revertArray(item, fa[i].getType(), fa[i].getName());
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

	public void setAllowNoField(boolean newAllowNoField) {
		m_AllowNoField = newAllowNoField;
	}
}
