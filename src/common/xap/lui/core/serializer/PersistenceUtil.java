package xap.lui.core.serializer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Iterator;
import java.util.Map;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import xap.lui.core.builder.LuiSet;
import xap.lui.core.dataset.LuiParameter;
import xap.lui.core.dataset.Parameter;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.listener.DatasetRule;
import xap.lui.core.listener.FormRule;
import xap.lui.core.listener.GridRule;
import xap.lui.core.listener.TreeRule;
import xap.lui.core.listener.WidgetRule;
import xap.lui.core.xml.XMLUtil;

/**
 * 持久化操作工具类
 * 
 * @author gd 2010-35
 * @version NC6.0
 */
public class PersistenceUtil {
	public static void addParameters(Document doc, LuiParameter[] params, Element parentNode) {
		if (params != null && params.length > 0) {
			Element paramsNode = doc.createElement("Params");
			parentNode.appendChild(paramsNode);
			for (int j = 0; j < params.length; j++) {
				LuiParameter eventParam = params[j];
				Element paramNode = doc.createElement("Param");
				paramsNode.appendChild(paramNode);
				Element nameNode = doc.createElement("Name");
				paramNode.appendChild(nameNode);

				nameNode.appendChild(doc.createTextNode(eventParam.getName()));

				if (eventParam instanceof Parameter) {
					Parameter param = (Parameter) eventParam;
					Element valueNode = doc.createElement("Value");
					paramNode.appendChild(valueNode);
					valueNode.appendChild(doc.createTextNode(param.getValue()));
				}

				Element descNode = doc.createElement("Desc");
				paramNode.appendChild(descNode);
				String descValue = eventParam.getDesc() == null ? "" : eventParam.getDesc();
				descNode.appendChild(doc.createCDATASection(descValue));
			}
		}
	}

	public static void addExtendsParameters(Document doc, LuiParameter[] params, Element parentNode) {
		if (params != null && params.length > 0) {
			Element paramsNode = doc.createElement("ExtendParams");
			parentNode.appendChild(paramsNode);
			for (int j = 0; j < params.length; j++) {
				LuiParameter eventParam = params[j];
				Element paramNode = doc.createElement("Param");
				paramsNode.appendChild(paramNode);
				Element nameNode = doc.createElement("Name");
				paramNode.appendChild(nameNode);

				nameNode.appendChild(doc.createTextNode(eventParam.getName()));

				if (eventParam instanceof Parameter) {
					Parameter param = (Parameter) eventParam;
					Element valueNode = doc.createElement("Value");
					paramNode.appendChild(valueNode);
					valueNode.appendChild(doc.createTextNode(param.getValue()));
				}

				Element descNode = doc.createElement("Desc");
				paramNode.appendChild(descNode);
				String descValue = eventParam.getDesc() == null ? "" : eventParam.getDesc();
				descNode.appendChild(doc.createCDATASection(descValue));
			}
		}
	}

	public static void addSubmitContent(Document doc, Element parentNode, LuiSet<WidgetRule> widgetRule) {
		Iterator<WidgetRule> widgetIt = widgetRule.iterator();
		while (widgetIt.hasNext()) {
			WidgetRule wr = widgetIt.next();
			Element widgetRuleNode = doc.createElement("Widget");
			parentNode.appendChild(widgetRuleNode);
			widgetRuleNode.setAttribute("id", wr.getId());
			widgetRuleNode.setAttribute("cardSubmit", "" + wr.isCardSubmit());
			widgetRuleNode.setAttribute("tabSubmit", "" + wr.isTabSubmit());
			widgetRuleNode.setAttribute("panelSubmit", "" + wr.isPanelSubmit());

			DatasetRule[] dsRules = wr.getDatasetRules();
			if (dsRules != null) {
				for (int i = 0; i < dsRules.length; i++) {
					DatasetRule dr = dsRules[i];
					Element dsRuleNode = doc.createElement("Dataset");
					widgetRuleNode.appendChild(dsRuleNode);
					dsRuleNode.setAttribute("id", dr.getId());
					dsRuleNode.setAttribute("type", dr.getType());
				}
			}

			TreeRule[] treeRules = wr.getTreeRules();
			if (treeRules != null) {
				for (int i = 0; i < treeRules.length; i++) {
					TreeRule dr = treeRules[i];
					Element treeRuleNode = doc.createElement("Tree");
					widgetRuleNode.appendChild(treeRuleNode);
					treeRuleNode.setAttribute("id", dr.getId());
					treeRuleNode.setAttribute("type", dr.getType());
				}
			}

			GridRule[] gridRules = wr.getGridRules();
			if (gridRules != null) {
				for (int i = 0; i < gridRules.length; i++) {
					GridRule dr = gridRules[i];
					Element gridRuleNode = doc.createElement("Grid");
					widgetRuleNode.appendChild(gridRuleNode);
					gridRuleNode.setAttribute("id", dr.getId());
					gridRuleNode.setAttribute("type", dr.getType());
				}
			}

			FormRule[] formRules = wr.getFormRules();
			if (formRules != null) {
				for (int i = 0; i < formRules.length; i++) {
					FormRule dr = formRules[i];
					Element formRuleNode = doc.createElement("Form");
					widgetRuleNode.appendChild(formRuleNode);
					formRuleNode.setAttribute("id", dr.getId());
					formRuleNode.setAttribute("type", dr.getType());
				}
			}

		}
	}

	public static void toXmlFile(Document doc, String filePath, String fileName) {
		// 写出文件
		Writer wr = null;
		try {
			File dir = new File(filePath);
			if (!dir.exists())
				dir.mkdirs();
			File file = new File(filePath + File.separatorChar + fileName);
			if (!file.exists())
				file.createNewFile();
			wr = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
			XMLUtil.printDOMTree(wr, doc, 0, "UTF-8");
		} catch (IOException e) {
			throw new LuiRuntimeException(e);
		} finally {
			try {
				if (wr != null) {
					wr.flush();
					wr.close();
				}
			} catch (IOException e) {
			}
		}
	}
}
