package xap.lui.core.serializer;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import xap.lui.core.builder.LuiSet;
import xap.lui.core.common.ExtAttribute;
import xap.lui.core.model.Connector;
import xap.lui.core.model.PagePartMeta;
import xap.lui.core.model.PipeIn;
import xap.lui.core.model.PipeInItem;
import xap.lui.core.model.PipeOut;
import xap.lui.core.model.PipeOutItem;
import xap.lui.core.model.TriggerItem;
import xap.lui.core.model.ViewPartConfig;
import xap.lui.core.util.AMCUtil;
import xap.lui.core.xml.XMLUtil;
/**
 * 将pageMeta持久化为xml
 * 
 * @author gd 2010-1-25
 * @version NC6.0
 * @since NC6.0
 */
public class PageMetaToXml {
	/**
	 * 将PageMeta持久化为xml文件
	 * 
	 * @param filePath
	 *            文件全路径
	 * @param fileName
	 *            文件名
	 * @param projectPath
	 *            工程路径
	 * @param meta
	 */
	public static void toXml(String filePath, String fileName, String projectPath, PagePartMeta meta) {
		Document doc = getDocumentByPageMeta(meta);
		// 写出文件
		PersistenceUtil.toXmlFile(doc, filePath, fileName);
	}
	public static String toString(PagePartMeta meta) {
		Document doc = getDocumentByPageMeta(meta);
		Writer wt = new StringWriter();
		XMLUtil.printDOMTree(wt, doc, 0, "UTF-8");
		String xmlStr = wt.toString();
		return xmlStr;
	}
	private static Document getDocumentByPageMeta(PagePartMeta meta) {
		Document doc = XMLUtil.getNewDocument();
		Element rootNode = doc.createElement("PagePart");
		// rootNode.setAttribute("masterWidget", meta.getMasterWidget());
		if (isNotNullString(meta.getId())) {
			rootNode.setAttribute("id", meta.getId());
		}
		if (isNotNullString(meta.getCaption())) {
			rootNode.setAttribute("caption", meta.getCaption());
		}
		if (isNotNullString(meta.getFoldPath())) {
			rootNode.setAttribute("foldPath", meta.getFoldPath());
		}
		if (isNotNullString(meta.getController())) {
			rootNode.setAttribute("controller", meta.getController());
		}
		rootNode.setAttribute("srcFolder", meta.getSrcFolder());
		if (isNotNullString(meta.getWindowType())) {
			rootNode.setAttribute("windowType", meta.getWindowType());
		}
		doc.appendChild(rootNode);
		Element processorNode = doc.createElement("Processor");
		rootNode.appendChild(processorNode);
		processorNode.appendChild(doc.createTextNode(meta.getProcessorClazz()));
		Element widgetsNode = doc.createElement("Widgets");
		rootNode.appendChild(widgetsNode);
		ViewPartConfig[] widgetConfs = meta.getViewPartConfs();
		for (int i = 0; i < widgetConfs.length; i++) {
			ViewPartConfig widget = widgetConfs[i];
			Element widgetNode = doc.createElement("Widget");
			widgetsNode.appendChild(widgetNode);
			widgetNode.setAttribute("id", widget.getId());
			if (isNotNullString(widget.getRefId()))
				widgetNode.setAttribute("refId", widget.getRefId());
			if (widget.isCanFreeDesign()) {
				widgetNode.setAttribute("canFreeDesign", "true");
			} else
				widgetNode.setAttribute("canFreeDesign", "false");
			addPlugins(doc, widgetNode, widget);
			addPlugouts(doc, widgetNode, widget);
			addWidgetExtendAttributes(doc, widgetNode, widget);
		}
		Map<String, ExtAttribute> extAttrs = meta.getExtendMap();
		if (extAttrs != null && !extAttrs.isEmpty()) {
			Element attributesNode = doc.createElement("Attributes");
			rootNode.appendChild(attributesNode);
			Iterator<String> attrIt = extAttrs.keySet().iterator();
			while (attrIt.hasNext()) {
				String attrKey = attrIt.next();
				ExtAttribute attr = extAttrs.get(attrKey);
				Element attributeNode = doc.createElement("Attribute");
				attributesNode.appendChild(attributeNode);
				Element keyNode = doc.createElement("Key");
				keyNode.appendChild(doc.createTextNode(attrKey));
				attributeNode.appendChild(keyNode);
				Element valueNode = doc.createElement("Value");
				if (attr.getValue() != null)
					valueNode.appendChild(doc.createTextNode(attr.getValue().toString()));
				attributeNode.appendChild(valueNode);
				Element descNode = doc.createElement("Desc");
				if (attr.getDesc() != null)
					descNode.appendChild(doc.createTextNode(attr.getDesc().toString()));
				attributeNode.appendChild(descNode);
			}
		}
		addPlugDesc(doc, rootNode, meta);
		// Events
		AMCUtil.addEvents(doc, meta.getEventConfs(), rootNode);
		// 持久化container
		// addContainers(doc, rootNode, meta);
		// 持久化plug关联
		addPlugConnectors(doc, rootNode, meta);
		return doc;
	}
	private static void addPlugDesc(Document doc, Element rootNode, PagePartMeta meta) {
		Element plugoutNodes = doc.createElement("PlugoutDescs");
		rootNode.appendChild(plugoutNodes);
		// plugout
		List<PipeOut> plugoutDescs = meta.getPipeOuts();
		if (plugoutDescs != null) {
			for (PipeOut plugoutDesc : plugoutDescs) {
				Element plugoutDescNodes = doc.createElement("PlugoutDesc");
				plugoutNodes.appendChild(plugoutDescNodes);
				if (isNotNullString(plugoutDesc.getId()))
					plugoutDescNodes.setAttribute("id", plugoutDesc.getId());
				List<PipeOutItem> plugoutDescItems = plugoutDesc.getItemList();
				if (plugoutDescItems != null) {
					for (PipeOutItem descItem : plugoutDescItems) {
						Element plugoutDescItemNodes = doc.createElement("PlugoutDescItem");
						plugoutDescNodes.appendChild(plugoutDescItemNodes);
						if (isNotNullString(descItem.getName()))
							plugoutDescItemNodes.setAttribute("name", descItem.getName());
						if (isNotNullString(descItem.getType()))
							plugoutDescItemNodes.setAttribute("type", descItem.getType());
						if (isNotNullString(descItem.getSource()))
							plugoutDescItemNodes.setAttribute("source", descItem.getSource());
						if (isNotNullString(descItem.getValue()))
							plugoutDescItemNodes.setAttribute("value", descItem.getValue());
						if (isNotNullString(descItem.getDesc()))
							plugoutDescItemNodes.setAttribute("desc", descItem.getDesc());
						if (isNotNullString(descItem.getClazztype()))
							plugoutDescItemNodes.setAttribute("clazztype", descItem.getClazztype());
					}
				}
				List<TriggerItem> plugoutEmitItems = plugoutDesc.getEmitList();
				if (plugoutEmitItems != null) {
					for (TriggerItem emitItem : plugoutEmitItems) {
						Element plugoutEmitItemNodes = doc.createElement("PlugoutEmitItem");
						plugoutDescNodes.appendChild(plugoutEmitItemNodes);
						if (isNotNullString(emitItem.getId()))
							plugoutEmitItemNodes.setAttribute("id", emitItem.getId());
						if (isNotNullString(emitItem.getSource()))
							plugoutEmitItemNodes.setAttribute("source", emitItem.getSource());
						if (isNotNullString(emitItem.getType()))
							plugoutEmitItemNodes.setAttribute("type", emitItem.getType());
						if (isNotNullString(emitItem.getDesc()))
							plugoutEmitItemNodes.setAttribute("desc", emitItem.getDesc());
					}
				}
			}
		}
		// plugin
		Element pluginNodes = doc.createElement("PluginDescs");
		rootNode.appendChild(pluginNodes);
		List<PipeIn> pluginDescs = meta.getPipeIns();
		if (pluginDescs != null) {
			for (PipeIn pluginDesc : pluginDescs) {
				Element pluginDescNodes = doc.createElement("PluginDesc");
				pluginNodes.appendChild(pluginDescNodes);
				if (isNotNullString(pluginDesc.getId()))
					pluginDescNodes.setAttribute("id", pluginDesc.getId());
				List<PipeInItem> pluginDescItems = pluginDesc.getItemList();
				if (pluginDescItems != null) {
					for (PipeInItem descItem : pluginDescItems) {
						Element pluginDescItemNodes = doc.createElement("PluginDescItem");
						pluginDescNodes.appendChild(pluginDescItemNodes);
						if (isNotNullString(descItem.getId()))
							pluginDescItemNodes.setAttribute("id", descItem.getId());
						if (isNotNullString(descItem.getValue()))
							pluginDescItemNodes.setAttribute("value", descItem.getValue());
						if (isNotNullString(descItem.getClazztype()))
							pluginDescItemNodes.setAttribute("clazztype", descItem.getClazztype());
					}
				}
			}
		}
	}
	private static boolean isNotNullString(String param) {
		if (param != null && !param.equals(""))
			return true;
		else
			return false;
	}
	private static void addPlugConnectors(Document doc, Element rootNode, PagePartMeta meta) {
		LuiSet<Connector> connectorMap = meta.getConnectorMap();
		if (connectorMap.size() > 0) {
			Element connNode = doc.createElement("Connectors");
			rootNode.appendChild(connNode);
			for (Iterator<Connector> i = connectorMap.iterator(); i.hasNext();) {
				Connector conn = i.next();
				Element c = doc.createElement("Connector");
				connNode.appendChild(c);
				c.setAttribute("id", conn.getId());
				c.setAttribute("pluginId", conn.getPipeinId());
				c.setAttribute("plugoutId", conn.getPipeoutId());
				c.setAttribute("sourceWindow", conn.getSourceWindow());
				c.setAttribute("targetWindow", conn.getTargetWindow());
				c.setAttribute("source", conn.getSource());
				c.setAttribute("target", conn.getTarget());
				c.setAttribute("connType", conn.getConnType());
				Map<String, String> map = conn.getMapping();
				if (map != null && map.size() > 0) {
					Element maps = doc.createElement("Maps");
					c.appendChild(maps);
					for (Iterator<String> j = map.keySet().iterator(); j.hasNext();) {
						String outValue = j.next();
						String inValue = map.get(outValue);
						Element e = doc.createElement("Map");
						maps.appendChild(e);
						Element outValueEle = doc.createElement("outValue");
						outValueEle.appendChild(doc.createTextNode(outValue));
						e.appendChild(outValueEle);
						Element inValueEle = doc.createElement("inValue");
						inValueEle.appendChild(doc.createTextNode(inValue));
						e.appendChild(inValueEle);
						e.setAttribute("outValue", outValue);
						e.setAttribute("inValue", inValue);
					}
				}
			}
		}
	}
	/**
	 * 输出信号槽
	 * 
	 * @param doc
	 * @param rootNode
	 * @param con
	 */
	private static void addPlugins(Document doc, Element rootNode, ViewPartConfig widget) {
		// List<PluginDesc> list = widget.getPluginDescs();
		// if (list == null) return;
		// Iterator<PluginDesc> it = list.iterator();
		// while(it.hasNext()){
		// PluginDesc plugin = it.next();
		// //TODO
		// }
	}
	/**
	 * 输出信号
	 * 
	 * @param doc
	 * @param rootNode
	 * @param widget
	 */
	private static void addPlugouts(Document doc, Element rootNode, ViewPartConfig widget) {
		// List<PlugoutDesc> list = widget.getPlugoutDescs();
		// if (list == null) return;
		// Iterator<PlugoutDesc> it = list.iterator();
		// while(it.hasNext()){
		// PlugoutDesc plugin = it.next();
		// //TODO
		// }
	}
	/**
	 * WidgetConfig扩展属性
	 * 
	 * @param doc
	 * @param root
	 * @param config
	 */
	private static void addWidgetExtendAttributes(Document doc, Element root, ViewPartConfig config) {
		Map<String, ExtAttribute> extAttrs = config.getExtendMap();
		if (extAttrs != null && !extAttrs.isEmpty()) {
			Element attributesNode = doc.createElement("Attributes");
			root.appendChild(attributesNode);
			String attrKey = null;
			Iterator<String> attrIt = extAttrs.keySet().iterator();
			while (attrIt.hasNext()) {
				attrKey = attrIt.next();
				ExtAttribute attr = extAttrs.get(attrKey);
				Element attributeNode = doc.createElement("Attribute");
				attributesNode.appendChild(attributeNode);
				Element keyNode = doc.createElement("Key");
				keyNode.appendChild(doc.createTextNode(attrKey));
				attributeNode.appendChild(keyNode);
				Element valueNode = doc.createElement("Value");
				if (attr.getValue() != null)
					valueNode.appendChild(doc.createTextNode(attr.getValue().toString()));
				attributeNode.appendChild(valueNode);
				Element descNode = doc.createElement("Desc");
				if (attr.getDesc() != null)
					descNode.appendChild(doc.createTextNode(attr.getDesc().toString()));
				attributeNode.appendChild(descNode);
			}
		}
	}
}
