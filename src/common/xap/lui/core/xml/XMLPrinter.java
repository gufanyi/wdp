
package xap.lui.core.xml;

import java.io.PrintWriter;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class XMLPrinter {

	private static boolean lastIsAString=false;

public XMLPrinter (){
	super();
}

 
private static String getSpace(int space) {
	char ca[]=new char[space];
	for(int i=0;i<ca.length;i++)
	{
		ca[i]='\t';
	}
	return new String(ca);
}


public static void printDOMTree(PrintWriter pw, Node node, int deepSet) {
	int type = node.getNodeType();
	switch (type) {
		case Node.DOCUMENT_NODE :
			{
				pw.print("<?xml version=\"1.0\" encoding='gb2312'?>");
				printDOMTree(pw, ((Document) node).getDocumentElement(), deepSet+1);
				break;
			}
		case Node.ELEMENT_NODE :
			{
				pw.println();
				pw.print(getSpace(deepSet) + "<");
				pw.print(node.getNodeName());
				NamedNodeMap attrs = node.getAttributes();
				for (int i = 0; i < attrs.getLength(); i++) {
					Node attr = attrs.item(i);
					pw.print(" " + attr.getNodeName() + "=\"" + attr.getNodeValue() + "\"");
					if (attr.getNodeValue().equals("null"))
						lastIsAString = true;
				}
				pw.print(">");
				NodeList children = node.getChildNodes();
				if (children != null) {
					int len = children.getLength();
					for (int i = 0; i < len; i++)
						printDOMTree(pw, children.item(i), deepSet+1);
				}
				break;
			}
		case Node.ENTITY_REFERENCE_NODE :
			{
				pw.print("&");
				pw.print(node.getNodeName());
				pw.print(";");
				break;
			}
		case Node.CDATA_SECTION_NODE :
			{
				pw.print(getSpace(deepSet) + "<![CDATA[");
				pw.print(node.getNodeValue());
				pw.print("]]>");
				break;
			}
		case Node.TEXT_NODE :
			{
			 String value = node.getNodeValue().trim();   
			    pw.print(value);
			    lastIsAString = !"".equals(value);
				break;
			}
		case Node.PROCESSING_INSTRUCTION_NODE :
			{
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
		case Node.COMMENT_NODE:
			{
			   pw.println();	
			   pw.print(getSpace(deepSet)+"<!--");
			   pw.print(node.getNodeValue()+"-->");
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
