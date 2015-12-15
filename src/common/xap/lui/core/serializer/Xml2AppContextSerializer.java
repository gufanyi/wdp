package xap.lui.core.serializer;
import java.io.IOException;
import java.util.Map;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;
import sun.misc.BASE64Decoder;
import xap.lui.core.common.LuiWebContext;
import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.json.Json2AppCtxDerializer;
import xap.lui.core.logger.LuiLogger;
import xap.lui.core.model.AppSession;
public class Xml2AppContextSerializer implements IXml2ObjectSerializer<AppSession> {
	private AppSession lifeCycleCtx;
	public AppSession serialize(String xml, Map<String, Object> paramMap) {
		try {
			LuiWebContext webCtx = LuiRuntimeContext.getWebContext();
			String compress = webCtx.getRequest().getParameter("compress");
			if (compress != null) {
				int compressLength = Integer.parseInt(webCtx.getRequest().getParameter("compressl"));
				xml = decompressRequest(xml, compressLength);
			}
			lifeCycleCtx = new Json2AppCtxDerializer().derialize(xml);
			return lifeCycleCtx;
		} catch (Exception e) {
			throw new LuiRuntimeException(e);
		} 
	}
	private String decompressRequest(String xml, int length) throws IOException {
		try {
			BASE64Decoder base64 = new BASE64Decoder();
			Inflater decompressor = new Inflater();
			byte[] rawbytes = base64.decodeBuffer(xml);
			decompressor.setInput(rawbytes, 0, rawbytes.length);
			byte[] buffer = new byte[length * 2 + 2048];
			int resultLength = decompressor.inflate(buffer);
			decompressor.end();
			String resultString = new String(buffer, 0, resultLength, "UTF-8");
			return resultString;
		} catch (DataFormatException e) {
			LuiLogger.error(e);
			throw new LuiRuntimeException("error occurred while decompressing");
		}
	}
//	private ApplicationSession serialize(String xml, PagePartMeta pagemeta, IUIPartMeta uimeta) {
//		try {
//			Document doc = XmlUtilPatch.getDocumentBuilder().parse(new InputSource(new StringReader(xml)));
//			Element rootNode = (Element) doc.getFirstChild();
//			Element ctxNode = (Element) DomUtil.getChildNode(rootNode, EventContextConstant.eventcontext);
//			return serialize(rootNode, ctxNode, pagemeta, uimeta);
//		} catch (Exception e) {
//			// Logger.error(e.getMessage(), e);
//			throw new LuiRuntimeException(e.getMessage());
//		}
//	}
//	private ApplicationSession serialize(Node rootNode, Node ctxNode, PagePartMeta pagemeta, IUIPartMeta uimeta) throws SAXException, IOException {
//		ApplicationContext appCtx = lifeCycleCtx.getApplicationContext();
//		String winId = ((Element) ctxNode).getAttribute("id");
//		WindowContext winCtx = new WindowContext();
//		winCtx.setId(winId);
//		winCtx.setPagePartMeta(pagemeta);
//		winCtx.setUIPartMeta(uimeta);
//		appCtx.addWindowContext(winCtx);
//		Element paramsNode = (Element) DomUtil.getChildNode(ctxNode, EventContextConstant.params);
//		if (paramsNode != null) {
//			Map<String, String> paramMap = parseParams(paramsNode);
//			lifeCycleCtx.setParam(paramMap);
//		}
//		Element groupParamsNode = (Element) DomUtil.getChildNode(ctxNode, EventContextConstant.groupparams);
//		if (groupParamsNode != null) {
//			Node[] allParams = DomUtil.getChildNodes(groupParamsNode, EventContextConstant.params);
//			if (allParams.length > 0) {
//				List<Map<String, String>> gplist = new ArrayList<Map<String, String>>();// lifeCycleCtx.getGroupParamMapList();
//				lifeCycleCtx.setGroupParam(gplist);
//				for (int i = 0; i < allParams.length; i++) {
//					Element paramsEle = (Element) allParams[i];
//					Map<String, String> paramMap = parseParams(paramsEle);
//					gplist.add(paramMap);
//				}
//			}
//		}
//		Node node = DomUtil.getChildNode(ctxNode, EventContextConstant.context);
//		String context = null;
//		if (node != null) {
//			context = DomUtil.getChildNode(ctxNode, EventContextConstant.context).getTextContent();
//			PageUIContext ctx = (PageUIContext) LuiJsonSerializer.getInstance().fromJsObject(context);
//			pagemeta.setContext(ctx);
//		}
//		Node[] widgets = DomUtil.getChildNodes(ctxNode, "widget");
//		for (int i = 0; i < widgets.length; i++) {
//			Element widgetEle = (Element) widgets[i];
//			this.processwidget(winCtx, pagemeta, (UIPartMeta) uimeta, widgetEle);
//		}
//		Node[] menubars = DomUtil.getChildNodes(ctxNode, "menubar");
//		for (int i = 0; i < menubars.length; i++) {
//			Element menubarEle = (Element) menubars[i];
//			context = DomUtil.getChildNode(menubarEle, EventContextConstant.context).getTextContent();
//			MenubarContext menubarContext = (MenubarContext) LuiJsonSerializer.getInstance().fromJsObject(context);
//			MenubarComp menubar = pagemeta.getViewMenus().getMenuBar(menubarEle.getAttribute("id"));
//			if (menubar == null) {
//				for (int j = 0; j < widgets.length; j++) {
//					Element widgetEle = (Element) widgets[j];
//					ViewPartMeta widget = pagemeta.getWidget(widgetEle.getAttribute("id"));
//					menubar = widget.getViewMenus().getMenuBar(menubarEle.getAttribute("id"));
//					if (menubar != null)
//						break;
//				}
//			}
//			menubar.setContext(menubarContext);
//		}
//		NodeList allList = ctxNode.getChildNodes();// rootNode.getElementsByTagName("tab");
//		if (allList != null) {
//			for (int i = 0; i < allList.getLength(); i++) {
//				Element ele = (Element) allList.item(i);
//				if (ele.getNodeName().equals("tab"))
//					this.processTab(winCtx, pagemeta, ele);
//			}
//		}
//		if (rootNode != null) {
//			Node pcontextNode = DomUtil.getChildNode(rootNode, EventContextConstant.parentcontext);
//			if (pcontextNode != null) {
//				String pId = ((Element) pcontextNode.getFirstChild()).getAttribute("id");
//				PagePartMeta parentPm = RuntimeContext.getWebContext().getOriginalPageMeta(pId);
//				IUIPartMeta parentUm = RuntimeContext.getWebContext().getOriginalUm(pId);
//				serialize(null, pcontextNode.getFirstChild(), parentPm, parentUm);
//			}
//		}
//		return lifeCycleCtx;
//	}
//	private Map<String, String> parseParams(Element paramsNode) {
//		Node[] allParams = DomUtil.getChildNodes(paramsNode, EventContextConstant.param);
//		Map<String, String> paramMap = new HashMap<String, String>();
//		for (int i = 0; i < allParams.length; i++) {
//			Element attEle = (Element) allParams[i];
//			Element keyEle = (Element) DomUtil.getChildNode(attEle, EventContextConstant.key);
//			String key = keyEle.getTextContent();
//			Element valueEle = (Element) DomUtil.getChildNode(attEle, EventContextConstant.value);
//			String value = valueEle.getTextContent();
//			if (value != null) {
//				value = value.replaceAll("&lt;", "<");
//				value = value.replaceAll("&amp;", "&");
//			}
//			paramMap.put(key, value);
//		}
//		return paramMap;
//	}
//	private void processTab(WindowContext winCtx, PagePartMeta pagemeta, Element ele) {
//		String context = ele.getElementsByTagName(EventContextConstant.context).item(0).getTextContent();
//		TabContext ctx = (TabContext) LuiJsonSerializer.getInstance().fromJsObject(context);
//		UIPartMeta uimeta = (UIPartMeta) winCtx.getUIPartMeta();
//		UITabComp tab = (UITabComp) UIElementFinder.findElementById(uimeta, ctx.getId());
//		tab.setCurrentItem(ctx.getCurrentIndex() + "");
//	}
//	protected void processwidget(WindowContext winCtx, PagePartMeta pagemeta, UIPartMeta um, Element widgetEle) throws SAXException, IOException {
//		String widgetId = widgetEle.getAttribute("id");
//		ViewPartMeta widget = pagemeta.getWidget(widgetId);
//		ViewPartConfig wcf = pagemeta.getWidgetConf(widgetId);
//		if (widget == null) {
//			throw new LuiRuntimeException("根据ID没有找到对应的widget配置," + widgetId);
//		}
//		ViewPartContext vCtx = new ViewPartContext();
//		vCtx.setId(widgetId);
//		UIViewPart uiWidget = (UIViewPart) UIElementFinder.findUIWidget(um, widgetId);
//		if (uiWidget != null)
//			vCtx.setUIMeta(uiWidget.getUimeta());
//		else {
//			String appPath = ContextResourceUtil.getCurrentAppPath();
//			String folderPath = pagemeta.getFoldPath();// (String)
//														// pagemeta.getExtendAttributeValue(PageMeta.FOLD_PATH);
//			String refId = widget.getRefId();
//			if (refId == null)
//				refId = wcf.getRefId();
//			String path = appPath + folderPath + "/" + widgetId;
//			if (refId != null && refId.startsWith("../")) {
//				folderPath = "pagemeta/public/widgetpool";
//				path = appPath + folderPath + "/" + refId.substring(3);
//			}
//			UIPartMeta viewUIMeta = (UIPartMeta) RuntimeContext.getWebContext().getWebSession().getAttribute("$TMP_UM_" + widgetId);
//			if (viewUIMeta == null) {
//				viewUIMeta = new UIMetaParserUtil().parseUIMeta(path, widgetId);
//			}
//			vCtx.setUIMeta(viewUIMeta);
//		}
//		vCtx.setView(widget);
//		winCtx.addViewContext(vCtx);
//		String init = widgetEle.getAttribute("init");
//		if (init != null && init.equals("false")) {
//			return;
//		}
//		NodeList allList = widgetEle.getChildNodes();
//		int size = allList.getLength();
//		for (int i = 0; i < size; i++) {
//			Node node = (Node) allList.item(i);
//			if (node instanceof Text)
//				continue;
//			Element allEle = (Element) node;
//			String nodeName = allEle.getNodeName();
//			if (nodeName.equals(EventContextConstant.context)) {
//				WidgetUIContext newWidget = (WidgetUIContext) LuiJsonSerializer.getInstance().fromJsObject(allEle.getTextContent());
//				widget.setContext(newWidget);
//			} else if (nodeName.equals("dataset")) {
//				Xml2DatasetSerializer xml2datasetSerializer = new Xml2DatasetSerializer();
//				Node dataEle = DomUtil.getChildNode(allEle, "data");
//				if (dataEle != null) {
//					String dsStr = dataEle.getTextContent();
//					if (dsStr != null && !dsStr.equals("")) {
//						Map<String, Object> paramMap = new HashMap<String, Object>();
//						paramMap.put("pagemeta", pagemeta);
//						xml2datasetSerializer.serialize(dsStr, paramMap);
//					}
//				}
//			} else if (nodeName.equals("combodata")) {} else if (nodeName.equals("refnode")) {} else if (nodeName.equals("toolbar")) {
//				String context = DomUtil.getChildNode(allEle, EventContextConstant.context).getTextContent();
//				ToolbarContext toolbarContext = (ToolbarContext) LuiJsonSerializer.getInstance().fromJsObject(context);
//				ToolBarComp toolbar = (ToolBarComp) widget.getViewComponents().getComponent(allEle.getAttribute("id"));
//				if (toolbarContext != null)
//					toolbar.setContext(toolbarContext);
//			} else if (nodeName.equals("menubar")) {
//				String context = DomUtil.getChildNode(allEle, EventContextConstant.context).getTextContent();
//				MenubarContext menubarContext = (MenubarContext) LuiJsonSerializer.getInstance().fromJsObject(context);
//				MenubarComp menubar = widget.getViewMenus().getMenuBar(allEle.getAttribute("id"));
//				if (menubarContext != null)
//					menubar.setContext(menubarContext);
//			} else if (nodeName.equals("ctxmenu")) {
//				String context = DomUtil.getChildNode(allEle, EventContextConstant.context).getTextContent();
//				ContextMenuContext ctxMenuContext = (ContextMenuContext) LuiJsonSerializer.getInstance().fromJsObject(context);
//				ContextMenuComp ctxmenu = widget.getViewMenus().getContextMenu(allEle.getAttribute("id"));
//				ctxmenu.setContext(ctxMenuContext);
//			} else if (nodeName.equals("tab")) {
//				String context = DomUtil.getChildNode(allEle, EventContextConstant.context).getTextContent();
//				TabContext tabCtx = (TabContext) LuiJsonSerializer.getInstance().fromJsObject(context);
//				UIPartMeta uimeta = vCtx.getUIMeta();
//				UITabComp tab = (UITabComp) UIElementFinder.findElementById(uimeta, tabCtx.getId());
//				tab.setCurrentItem(tabCtx.getCurrentIndex() + "");
//			} else if (nodeName.equals("iframe")) {
//				String context = DomUtil.getChildNode(allEle, EventContextConstant.context).getTextContent();
//				IFrameContext iFrameContext = (IFrameContext) LuiJsonSerializer.getInstance().fromJsObject(context);
//				IFrameComp iFrameComp = (IFrameComp) widget.getViewComponents().getComponent(allEle.getAttribute("id"));
//				if (iFrameComp != null)
//					iFrameComp.setContext(iFrameContext);
//			} else {
//				ViewPartComps viewComponent = widget.getViewComponents();
//				WebComp webcomp = (WebComp) viewComponent.getComponent(allEle.getAttribute("id"));
//				String context = DomUtil.getChildNode(allEle, EventContextConstant.context).getTextContent();
//				BaseContext comp = (BaseContext) LuiJsonSerializer.getInstance().fromJsObject(context);
//				if (comp != null)
//					webcomp.setContext(comp);
//			}
//		}
//	}
}
