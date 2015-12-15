package xap.lui.core.xml;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

public class XMLUtil {
	private static class XmlPrinter {
		private static boolean lastIsAString = false;

		private static String getSpace(int space) {
			char ca[] = new char[space * 4];
			for (int i = 0; i < ca.length; i += 4) {
				ca[i] = ' ';
				ca[i + 1] = ' ';
				ca[i + 2] = ' ';
				ca[i + 3] = ' ';
			}
			return new String(ca);
		}

		public static void printDOMTree(PrintWriter pw, Node node, int deepSet) {
			XmlPrinter.printDOMTree(pw, node, deepSet, "gb2312");

		}

		public static void printDOMTree(Writer writer, Node node, int deepSet, String encoding) {
			int type = node.getNodeType();
			PrintWriter pw = null;
			if (writer instanceof OutputStreamWriter) {
				pw = new PrintWriter(writer);
			} else if (writer instanceof PrintWriter) {
				pw = (PrintWriter) writer;
			} else {
				throw new IllegalArgumentException("Illegal writer to print dom tree.");
			}
			switch (type) {
			case Node.DOCUMENT_NODE: {
				pw.print("<?xml version=\"1.0\" encoding=\"" + encoding + "\"?>");

				Node preComment = ((Document) node).getDocumentElement().getPreviousSibling();
				if (preComment != null) {
					printDOMTree(pw, preComment, deepSet);
				}
				printDOMTree(pw, ((Document) node).getDocumentElement(), deepSet);
				pw.println();
				break;
			}
			case Node.ELEMENT_NODE: {
				pw.println();
				pw.print(getSpace(deepSet) + "<");
				pw.print(node.getNodeName());
				NamedNodeMap attrs = node.getAttributes();
				for (int i = 0; i < attrs.getLength(); i++) {
					Node attr = attrs.item(i);
					pw.print(" " + attr.getNodeName() + "=\"" + XMLUtil.getXMLString(attr.getNodeValue()) + "\"");
					if (null == attr.getNodeValue() || attr.getNodeValue().equals("null"))
						lastIsAString = true;
				}
				pw.print(">");
				NodeList children = node.getChildNodes();
				if (children != null) {
					int len = children.getLength();
					for (int i = 0; i < len; i++)
						printDOMTree(pw, children.item(i), deepSet + 1);
				}
				break;
			}
			case Node.ENTITY_REFERENCE_NODE: {
				pw.print("&");
				pw.print(node.getNodeName());
				pw.print(";");
				break;
			}
			case Node.CDATA_SECTION_NODE: {
				pw.print(getSpace(deepSet) + "<![CDATA[");
				pw.print(node.getNodeValue());
				pw.print("]]>");
				break;
			}
			case Node.TEXT_NODE: {
				String value = node.getNodeValue();
				if (value != null) {
					value = value.trim();
					value = getXMLString(value);
					pw.print(value);
				}
				lastIsAString = !"".equals(value);
				break;
			}
			case Node.PROCESSING_INSTRUCTION_NODE: {
				pw.print(getSpace(deepSet) + "<?");
				pw.print(node.getNodeName());
				String data = node.getNodeValue();
				{
					pw.print("");
					pw.print(data);
				}
				pw.print("?>");
				break;
			}
			case Node.COMMENT_NODE: {
				pw.println();
				pw.print(getSpace(deepSet) + "<!--");
				pw.print(node.getNodeValue() + "-->");
				break;
			}
			}
			if (type == Node.ELEMENT_NODE) {
				if (!lastIsAString) {
					pw.println();
					pw.print(getSpace(deepSet) + "</");
				} else
					pw.print("</");
				pw.print(node.getNodeName());
				pw.print('>');
				lastIsAString = false;
			}
		}
	}

	private XMLUtil() {

	}

	public static void printDOMTree(PrintWriter pw, Node node, int deepSet) {
		XmlPrinter.printDOMTree(pw, node, deepSet);
	}

	public static void writeXMLFormatString(StringBuffer fileBuffer, Node node, int depth) {
		OutputStream out = new ByteArrayOutputStream();
		Writer writer = new OutputStreamWriter(out);
		printDOMTree(writer, node, depth, "UTF-8");
		try {
			writer.close();
		} catch (IOException e) {
		}
		fileBuffer.append(out.toString());
	}

	public static DocumentBuilder getDocumentBuilder() {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setValidating(false);
		dbf.setNamespaceAware(true);
		try {
			return dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			throw new RuntimeException("XML解析器构造失败!");
		}
	}

	public static Document getNewDocument() {
		return getDocumentBuilder().newDocument();
	}

	public static Node getChildNodeOf(Node node, String tagName) {
		for (Node temp = node.getFirstChild(); temp != null; temp = temp.getNextSibling())
			if (temp.getNodeType() == Node.ELEMENT_NODE && tagName.equals(temp.getNodeName())) {
				return temp;
			}
		return null;
	}

	public static String getChildNodeValueOf(Node node, String tagName) {
		if (tagName.equals(node.getNodeName())) {
			return getValueOf(node);
		}
		for (Node temp = node.getFirstChild(); temp != null; temp = temp.getNextSibling()) {
			if (temp.getNodeType() == Node.ELEMENT_NODE && tagName.equals(temp.getNodeName())) {
				return getValueOf(temp);
			}
		}
		return null;
	}

	public static final String getValueOf(Node node) {
		if (node == null) {
			return null;
		} else if (node instanceof Text) {
			return node.getNodeValue().trim();
		} else if (node instanceof Element) {
			((Element) node).normalize();
			Node temp = node.getFirstChild();
			if (temp != null && (temp instanceof Text))
				return temp.getNodeValue().trim();
			else
				return "";
		} else {
			return node.getNodeValue().trim();
		}
	}

	public static final String getAtrributeValueOf(Node node, String attribute) {
		Node _node = node.getAttributes().getNamedItem(attribute);
		return getValueOf(_node);
	}

	public static Iterator getElementsByTagName(Element element, String tag) {
		ArrayList<Element> children = new ArrayList<Element>();
		if (element != null && tag != null) {
			NodeList nodes = element.getElementsByTagName(tag);
			for (int i = 0; i < nodes.getLength(); i++) {
				Node child = nodes.item(i);
				children.add((Element) child);
			}
		}
		return children.iterator();
	}

	public static Iterator getElementsByTagNames(Element element, String[] tags) {
		List<Element> children = new ArrayList<Element>();
		if (element != null && tags != null) {
			List tagList = Arrays.asList(tags);
			NodeList nodes = element.getChildNodes();
			for (int i = 0; i < nodes.getLength(); i++) {
				Node child = nodes.item(i);
				if (child.getNodeType() == Node.ELEMENT_NODE && tagList.contains(((Element) child).getTagName())) {
					children.add((Element) child);
				}
			}
		}
		return children.iterator();
	}

	public static Document getDocument(URL url) throws Exception {
		InputStream is = null;
		try {
			is = new BufferedInputStream(url.openStream());
			return getDocumentBuilder().parse(is);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {

				}
			}
		}
	}

	public static Document getDocument(File file) throws Exception {
		return getDocumentBuilder().parse(file);
	}

	public static Document getDocument(String file) throws Exception {
		return getDocumentBuilder().parse(file);
	}

	public static void copyInto(Node src, Node dest) throws DOMException {

		Document factory = dest.getOwnerDocument();
		Node parent = null;
		Node place = src;
		while (place != null) {
			Node node = null;
			int type = place.getNodeType();
			switch (type) {
			case Node.CDATA_SECTION_NODE: {
				node = factory.createCDATASection(place.getNodeValue());
				break;
			}
			case Node.COMMENT_NODE: {
				node = factory.createComment(place.getNodeValue());
				break;
			}
			case Node.ELEMENT_NODE: {
				Element element = factory.createElement(place.getNodeName());
				node = element;
				NamedNodeMap attrs = place.getAttributes();
				int attrCount = attrs.getLength();
				for (int i = 0; i < attrCount; i++) {
					Attr attr = (Attr) attrs.item(i);
					String attrName = attr.getNodeName();
					String attrValue = attr.getNodeValue();
					element.setAttribute(attrName, attrValue);

				}
				break;
			}
			case Node.ENTITY_REFERENCE_NODE: {
				node = factory.createEntityReference(place.getNodeName());
				break;
			}
			case Node.PROCESSING_INSTRUCTION_NODE: {
				node = factory.createProcessingInstruction(place.getNodeName(), place.getNodeValue());
				break;
			}
			case Node.TEXT_NODE: {
				node = factory.createTextNode(place.getNodeValue());
				break;
			}
			default: {
				throw new IllegalArgumentException("can't copy node type, " + type + " (null)");
			}
			}
			dest.appendChild(node);
			if (place.hasChildNodes()) {
				parent = place;
				place = place.getFirstChild();
				dest = node;
			} else if (parent == null) {
				place = null;
			} else {
				place = place.getNextSibling();
				while (place == null && parent != null && dest != null) {
					place = parent.getNextSibling();
					parent = parent.getParentNode();
					dest = dest.getParentNode();
				}
			}

		}

	}

	public static Element getFirstChildElement(Node parent) {

		if (parent == null)
			return null;
		Node child = parent.getFirstChild();
		while (child != null) {
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				return (Element) child;
			}
			child = child.getNextSibling();
		}
		return null;

	}

	public static Element getLastChildElement(Node parent) {

		if (parent == null)
			return null;
		Node child = parent.getLastChild();
		while (child != null) {
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				return (Element) child;
			}
			child = child.getPreviousSibling();
		}
		return null;

	}

	public static Element getNextSiblingElement(Node node) {

		if (node == null)
			return null;
		Node sibling = node.getNextSibling();
		while (sibling != null) {
			if (sibling.getNodeType() == Node.ELEMENT_NODE) {
				return (Element) sibling;
			}
			sibling = sibling.getNextSibling();
		}
		return null;

	}

	public static Element getFirstChildElement(Node parent, String elemName) {

		if (parent == null)
			return null;
		Node child = parent.getFirstChild();
		while (child != null) {
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				if (child.getNodeName().equals(elemName)) {
					return (Element) child;
				}
			}
			child = child.getNextSibling();
		}
		return null;

	}

	public static Element getLastChildElement(Node parent, String elemName) {

		if (parent == null)
			return null;
		Node child = parent.getLastChild();
		while (child != null) {
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				if (child.getNodeName().equals(elemName)) {
					return (Element) child;
				}
			}
			child = child.getPreviousSibling();
		}
		return null;

	}

	public static Element getNextSiblingElement(Node node, String elemName) {

		if (node == null)
			return null;
		Node sibling = node.getNextSibling();
		while (sibling != null) {
			if (sibling.getNodeType() == Node.ELEMENT_NODE) {
				if (sibling.getNodeName().equals(elemName)) {
					return (Element) sibling;
				}
			}
			sibling = sibling.getNextSibling();
		}
		return null;

	}

	public static String getChildText(Node node) {
		if (node == null) {
			return null;
		}
		StringBuffer str = new StringBuffer();
		Node child = node.getFirstChild();
		while (child != null) {
			short type = child.getNodeType();
			if (type == Node.TEXT_NODE) {
				str.append(child.getNodeValue());
			} else if (type == Node.CDATA_SECTION_NODE) {
				str.append(getChildText(child));
			}
			child = child.getNextSibling();
		}
		return str.toString();

	}

	public static String getElementText(Element ele) {
		if (ele == null) {
			return null;
		}
		Node child = ele.getFirstChild();
		if (child != null) {
			short type = child.getNodeType();
			if (type == Node.TEXT_NODE) {
				return child.getNodeValue();
			}
		}
		return null;
	}

	public static String getFirstChildElementText(Node node) {
		return getElementText(getFirstChildElement(node));
	}

	public static String getLastChildElementText(Node node) {
		return getElementText(getLastChildElement(node));
	}

	public static String getNextSiblingElementText(Node node) {
		return getElementText(getNextSiblingElement(node));
	}

	public static String getFirstChildElementText(Node node, String elemName) {
		return getElementText(getFirstChildElement(node, elemName));
	}

	public static String getLastChildElementText(Node node, String elemName) {
		return getElementText(getLastChildElement(node, elemName));
	}

	public static String getNextSiblingElementText(Node node, String elemName) {
		return getElementText(getNextSiblingElement(node, elemName));
	}

	public static Element createLeafElement(Document doc, String eleName, String text) {
		Element ele = doc.createElement(eleName);
		if (text != null) {
			ele.appendChild(doc.createTextNode(text));
		}
		return ele;
	}

	public static void addChildElement(Element element, String child_ele_name, String text) {
		Document doc = element.getOwnerDocument();
		Element sub_element = createLeafElement(doc, child_ele_name, text);
		element.appendChild(sub_element);
	}

	public static Element getElement(Document doc, String tagName, int index) {
		NodeList rows = doc.getDocumentElement().getElementsByTagName(tagName);
		return (Element) rows.item(index);
	}

	public static Document newDocument() throws ParserConfigurationException {
		return getDocumentBuilder().newDocument();
	}

	public static void printDOMTree(Writer pw, Node node, int deepSet, String encoding) {
		XMLUtil.XmlPrinter.printDOMTree(new PrintWriter(pw), node, deepSet, encoding);
	}

	public static String getRegularString(String value) {
		if (value != null) {
			value = value.toString().trim();
			value = StringUtils.replaceAllString(value, "&lt;", "<");
			value = StringUtils.replaceAllString(value, "&gt;", ">");
			value = StringUtils.replaceAllString(value, "&quot;", "\"");
			value = StringUtils.replaceAllString(value, "&apos;", "\'");
			value = StringUtils.replaceAllString(value, "&amp;", "&");
		}
		return value;
	}

	public static String getXMLString(String value) {
		if (value != null) {
			value = value.toString().trim();
			value = StringUtils.replaceAllString(value, "&", "&amp;");
			value = StringUtils.replaceAllString(value, "<", "&lt;");
			value = StringUtils.replaceAllString(value, "<", "&lt;");
			value = StringUtils.replaceAllString(value, ">", "&gt;");
			value = StringUtils.replaceAllString(value, "\"", "&quot;");
			value = StringUtils.replaceAllString(value, "\'", "&apos;");
		}
		return value;
	}

	public static List<Node> getAllChildNodes(Node node) {
		if (node == null)
			return null;
		List<Node> result = new ArrayList<Node>();
		NodeList nodelist = node.getChildNodes();
		for (int i = 0; i < nodelist.getLength(); i++) {
			Node curnode = nodelist.item(i);
			int type = curnode.getNodeType();
			if (type != Node.TEXT_NODE)
				result.add(nodelist.item(i));
			List<Node> childlist = getAllChildNodes(curnode);
			if (childlist != null)
				result.addAll(childlist);
		}
		return result;
	}

	public static Node appendChildNodes(Node destNode, Node parentNode) {
		if (destNode != null && parentNode != null) {
			destNode.setTextContent("");
			NodeList list = parentNode.getChildNodes();
			if (list != null) {
				for (int i = 0; i < list.getLength(); i++) {
					Node node = list.item(i);
					if (node != null) {
						copyInto(node, destNode);
					}
				}
			}
		}
		return destNode;
	}

	public static Document rebuildDocument(String xmlSnippet) throws SAXException, IOException, UnsupportedEncodingException {
		StringBuffer buffer = new StringBuffer("<?xml version=\"1.0\" encoding=\'UTF-8\'?><root>");
		buffer.append(xmlSnippet);
		buffer.append("</root>");
		Document tmpDoc = getDocumentBuilder().parse(new ByteArrayInputStream(buffer.toString().getBytes("UTF-8")));
		return tmpDoc;
	}

	public static Node removeChildren(Node parentNode) {
		if (parentNode != null) {
			NodeList list = parentNode.getChildNodes();
			if (list != null) {
				for (int i = 0; i < list.getLength(); i++) {
					Node node = list.item(i);
					if (node != null) {
						parentNode.removeChild(node);
					}
				}
			}
		}
		return parentNode;
	}

	public static StringBuffer getContentsOfNode(Node parentNode) {
		StringBuffer stringBuffer = new StringBuffer();
		if (parentNode != null) {
			NodeList list = parentNode.getChildNodes();
			if (list != null) {
				for (int i = 0; i < list.getLength(); i++) {
					Node node = list.item(i);
					if (node != null) {
						writeXMLFormatString(stringBuffer, node, 0);
					}
				}
			}
		}
		return stringBuffer;
	}
}
