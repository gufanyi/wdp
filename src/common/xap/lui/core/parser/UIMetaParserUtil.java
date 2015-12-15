package xap.lui.core.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import xap.lui.core.builder.LuiHashSet;
import xap.lui.core.builder.LuiSet;
import xap.lui.core.builder.UIPartMetaProvider;
import xap.lui.core.common.ContextResourceUtil;
import xap.lui.core.dataset.Parameter;
import xap.lui.core.exception.LuiParseException;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.layout.UIAbsoluteLayout;
import xap.lui.core.layout.UIBorder;
import xap.lui.core.layout.UIBorderTrue;
import xap.lui.core.layout.UIButton;
import xap.lui.core.layout.UICanvas;
import xap.lui.core.layout.UICanvasPanel;
import xap.lui.core.layout.UICardLayout;
import xap.lui.core.layout.UICardPanel;
import xap.lui.core.layout.UIChartComp;
import xap.lui.core.layout.UIComponent;
import xap.lui.core.layout.UIConstant;
import xap.lui.core.layout.UIDialog;
import xap.lui.core.layout.UIDivid;
import xap.lui.core.layout.UIDividCenter;
import xap.lui.core.layout.UIDividProp;
import xap.lui.core.layout.UIElement;
import xap.lui.core.layout.UIFlowhLayout;
import xap.lui.core.layout.UIFlowhPanel;
import xap.lui.core.layout.UIFlowvLayout;
import xap.lui.core.layout.UIFlowvPanel;
import xap.lui.core.layout.UIFormComp;
import xap.lui.core.layout.UIFormElement;
import xap.lui.core.layout.UIFormGroupComp;
import xap.lui.core.layout.UIGridComp;
import xap.lui.core.layout.UIGridLayout;
import xap.lui.core.layout.UIGridPanel;
import xap.lui.core.layout.UIGridRowLayout;
import xap.lui.core.layout.UIGridRowPanel;
import xap.lui.core.layout.UIIFrame;
import xap.lui.core.layout.UIImageComp;
import xap.lui.core.layout.UILabelComp;
import xap.lui.core.layout.UILayout;
import xap.lui.core.layout.UILayoutPanel;
import xap.lui.core.layout.UILinkComp;
import xap.lui.core.layout.UIMenuGroup;
import xap.lui.core.layout.UIMenuGroupItem;
import xap.lui.core.layout.UIMenubarComp;
import xap.lui.core.layout.UIPanel;
import xap.lui.core.layout.UIPanelPanel;
import xap.lui.core.layout.UIPanelRightPanel;
import xap.lui.core.layout.UIPartComp;
import xap.lui.core.layout.UIPartMeta;
import xap.lui.core.layout.UIPropertyGridComp;
import xap.lui.core.layout.UISelfDefComp;
import xap.lui.core.layout.UIShutter;
import xap.lui.core.layout.UIShutterItem;
import xap.lui.core.layout.UISilverlightWidget;
import xap.lui.core.layout.UISplitter;
import xap.lui.core.layout.UISplitterOne;
import xap.lui.core.layout.UISplitterTwo;
import xap.lui.core.layout.UITabComp;
import xap.lui.core.layout.UITabItem;
import xap.lui.core.layout.UITabRightPanel;
import xap.lui.core.layout.UITextField;
import xap.lui.core.layout.UIToolBar;
import xap.lui.core.layout.UITreeComp;
import xap.lui.core.layout.UIViewPart;
import xap.lui.core.listener.DatasetRule;
import xap.lui.core.listener.EventSubmitRule;
import xap.lui.core.listener.FormRule;
import xap.lui.core.listener.GridRule;
import xap.lui.core.listener.LuiEventConf;
import xap.lui.core.listener.TreeRule;
import xap.lui.core.listener.WidgetRule;
import xap.lui.core.logger.LuiLogger;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.model.PagePartMeta;
import xap.lui.core.model.RequestLifeCycleContext;
import xap.lui.core.model.ViewPartConfig;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.core.plugins.ILuiPaltformExtProvier;
import xap.lui.core.plugins.LuiPaltformContranier;
import xap.lui.core.util.ClassUtil;
import xap.lui.core.util.LuiClassUtil;
import xap.lui.core.util.XmlUtilPatch;

public class UIMetaParserUtil implements UIConstant {
	private static final String ID = "id";
	private static final String STATE = "state";
	private static final String WIDGET_ID = "widgetId";
	private UIPartMeta meta = null;
	private PagePartMeta pagemeta;
	
	private boolean isDesign=false;
	public UIMetaParserUtil() {
		this(false);
	}

	public UIMetaParserUtil(boolean isDesign) {
		super();
		this.isDesign = isDesign;
	}

	public PagePartMeta getPagemeta() {
		return pagemeta;
	}

	public void setPagemeta(PagePartMeta pagemeta) {
		this.pagemeta = pagemeta;
	}

	/**
	 * 解析公共view UI
	 * 
	 */
	public UIPartMeta parsePublicUIMeta(String pubViewId) {
		return this.parseUIMeta("", pubViewId);
	}

	public UIPartMeta parseUIMeta(String pageId, String viewId) {
		Document doc = null;
		InputStream input = null;
		Element rootNode = null;
		String folderPath = "/lui/nodes/"+pageId+"/";
		try {
			meta = new UIPartMeta();
			meta.setFolderPath(folderPath);
			String path = null;
			if (viewId != null && pagemeta != null) {
				ViewPartConfig wConf = pagemeta.getViewPartConf(viewId);
				String refId = wConf.getRefId();
				if (refId.startsWith("..")) {
					path = "/lui/views/" + viewId;
					meta.setFolderPath(path);
					path = path + "/" + viewId + ".layout.xml";
					input = ContextResourceUtil.getResourceAsStream(path);
				} else {
					String layOutName = (pageId + "." + viewId + ".layout.xml");
					path = folderPath  + layOutName;
					input = ContextResourceUtil.getResourceAsStream(path);
				}
				doc = XmlUtilPatch.getDocumentBuilder().parse(input);
				rootNode = (Element) doc.getFirstChild();
				ViewPartMeta widget = pagemeta.getWidget(viewId);
				String uiprovider = getAttributeValue(rootNode, UIPartMeta.UIPROVIDER);
				if (uiprovider != null && !uiprovider.equals("")) {
					UIPartMetaProvider uip = (UIPartMetaProvider) ClassUtil.newInstance(uiprovider);
					meta = uip.getDefaultUIMeta(widget);
					meta.setUiprovider(uiprovider);
				}
			} else {
				String layOutName = (pageId + ".page.layout.xml");
				path = folderPath + layOutName;
				input = ContextResourceUtil.getResourceAsStream(path);
				doc = XmlUtilPatch.getDocumentBuilder().parse(input);
				rootNode = (Element) doc.getFirstChild();
			}
			String id = getAttributeValue(rootNode, UIPartMeta.ID);
			meta.setId(id);
			String isJquery = getAttributeValue(rootNode, UIPartMeta.ISJQUERY);
			if (isJquery != null && isJquery.equals("1"))
				meta.setJquery(Integer.valueOf(1));
			else
				meta.setJquery(Integer.valueOf(0));
			String flowmode = getAttributeValue(rootNode, UIPartMeta.ISFLOW);
			if (flowmode != null && !flowmode.equals(""))
				meta.setFlowmode(Boolean.valueOf(flowmode));
			String includejs = getAttributeValue(rootNode, UIPartMeta.INCLUDEJS);
			if (includejs != null)
				meta.setIncludejs(includejs);
			String includecss = getAttributeValue(rootNode, UIPartMeta.INCLUDECSS);
			if (includecss != null)
				meta.setIncludecss(includecss);
			NodeList ndList = rootNode.getChildNodes();
			Node node = null;
			for (int i = 0; i < ndList.getLength(); i++) {
				node = ndList.item(i);
				if (node instanceof Text)
					continue;
				break;
			}
			LifeCyclePhase oriPhase = RequestLifeCycleContext.get().getPhase();
			RequestLifeCycleContext.get().setPhase(LifeCyclePhase.nullstatus);
			UIElement layout = parseLayout(node);
			RequestLifeCycleContext.get().setPhase(oriPhase);
			if (layout == null) {
				layout = parseWidget(node);
				if (layout == null)
					layout = parseComponent(node);
			}
			if (layout != null)
				meta.setElement(layout);
			meta.getDialogMap().putAll(parseDialog(ndList));
			if (viewId != null) {
				meta.setId(viewId + "_um");
				meta.adjustUI(viewId);
			}
			return meta;
		} catch (Exception e) {
			LuiLogger.error(e);
			throw new LuiParseException(e.getMessage());
		} finally {
			if (input != null)
				try {
					input.close();
				} catch (IOException e) {
					LuiLogger.error(e.getMessage(), e);
				}
		}
	}

	
	/**
	 * 
	 * @param folderPath folderPath用来用指定的目录下来加载，folderPath没有提供的话，只能从classloader下面加载
	 * @param pageId
	 * @param viewId
	 * @param isDesign 用来区分代码layout要不要直接渲染
	 * @return
	 */

	public UIPartMeta parseUIMeta(String folderPath, String pageId, String viewId) {
		Document doc = null;
		InputStream input = null;
		Element rootNode = null;
		try {
			meta = new UIPartMeta();
			meta.setFolderPath(folderPath);
			String path = null;
			if (viewId != null && pagemeta != null) {
				ViewPartConfig wConf = pagemeta.getViewPartConf(viewId);
				String refId = wConf.getRefId();
				if (refId.startsWith("..")) {
					path = "/lui/views/" + viewId;
					path = path + "/" + viewId + ".layout.xml";
					input = ContextResourceUtil.getResourceAsStream(path);
				} else {
					String layOutName = ((pageId + "." + viewId + ".layout.xml"));
					path = folderPath + layOutName;
					input = new FileInputStream(new File(path));
				}
				doc = XmlUtilPatch.getDocumentBuilder().parse(input);
				rootNode = (Element) doc.getFirstChild();
				ViewPartMeta widget = pagemeta.getWidget(viewId);
				String uiprovider = getAttributeValue(rootNode, UIPartMeta.UIPROVIDER);

				if (uiprovider != null && !uiprovider.equals("")) {
					if (!isDesign) {
						UIPartMetaProvider uip = (UIPartMetaProvider) ClassUtil.newInstance(uiprovider);
						meta = uip.getDefaultUIMeta(widget);
					}
					meta.setUiprovider(uiprovider);
				}

			} else {
				String layOutName = ((pageId + ".page.layout.xml"));
				path = folderPath  + "/" + layOutName;
				input = new FileInputStream(new File(path));
				doc = XmlUtilPatch.getDocumentBuilder().parse(input);
				rootNode = (Element) doc.getFirstChild();
			}
			String id = getAttributeValue(rootNode, UIPartMeta.ID);
			meta.setId(id);
			String isJquery = getAttributeValue(rootNode, UIPartMeta.ISJQUERY);
			if (isJquery != null && isJquery.equals("1"))
				meta.setJquery(Integer.valueOf(1));
			else
				meta.setJquery(Integer.valueOf(0));
			String flowmode = getAttributeValue(rootNode, UIPartMeta.ISFLOW);
			if (flowmode != null && !flowmode.equals(""))
				meta.setFlowmode(Boolean.valueOf(flowmode));
			String includejs = getAttributeValue(rootNode, UIPartMeta.INCLUDEJS);
			if (includejs != null)
				meta.setIncludejs(includejs);
			String includecss = getAttributeValue(rootNode, UIPartMeta.INCLUDECSS);
			if (includecss != null)
				meta.setIncludecss(includecss);
			NodeList ndList = rootNode.getChildNodes();
			Node node = null;
			for (int i = 0; i < ndList.getLength(); i++) {
				node = ndList.item(i);
				if (node instanceof Text)
					continue;
				break;
			}
			LifeCyclePhase oriPhase = RequestLifeCycleContext.get().getPhase();
			RequestLifeCycleContext.get().setPhase(LifeCyclePhase.nullstatus);
			UIElement layout = parseLayout(node);
			RequestLifeCycleContext.get().setPhase(oriPhase);
			if (layout == null) {
				layout = parseWidget(node);
				if (layout == null)
					layout = parseComponent(node);
			}
			if (layout != null)
				meta.setElement(layout);
			meta.getDialogMap().putAll(parseDialog(ndList));
			if (viewId != null) {
				meta.setId(viewId + "_um");
				meta.adjustUI(viewId);
			}
			//
			///java.util.logging.Logger.getAnonymousLogger().info(JaxbMarshalFactory.newIns().encodeXML(meta));
			return meta;
		} catch (Exception e) {
			LuiLogger.error(e);
			throw new LuiParseException(e.getMessage());
		} finally {
			if (input != null)
				try {
					input.close();
				} catch (IOException e) {
					LuiLogger.error(e.getMessage(), e);
				}
		}
	}

	private Map<String, UIViewPart> parseDialog(NodeList ndList) {
		Map<String, UIViewPart> map = new HashMap<String, UIViewPart>();
		for (int i = 0; i < ndList.getLength(); i++) {
			Node nd = ndList.item(i);
			if (nd instanceof Text)
				continue;
			if (nd.getNodeName().equals("Dialog")) {
				UIViewPart w = (UIViewPart) parseDialog(nd);
				map.put(w.getId(), w);
			}
		}
		return map;
	}

	private UIAbsoluteLayout parseAbsoluteLayout(Node node) {
		String nodeName = node.getNodeName();
		UIAbsoluteLayout layout = new UIAbsoluteLayout();
		{
			String id = getAttributeValue(node, UIAbsoluteLayout.ID);
			layout.setId(id);
			String widgetId = getAttributeValue(node,
					UIAbsoluteLayout.WIDGET_ID);
			layout.setViewId(widgetId);
		}

		// modify by renxh
		String id = getAttributeValue(node, ID);
		if (id != null && !id.equals(""))
			layout.setId(id);
		String widgetId = getAttributeValue(node, WIDGET_ID);
		if (widgetId != null && !widgetId.equals(""))
			layout.setViewId(widgetId);
		String className = getAttributeValue(node, UILayout.STYLECLASSNAME);
		if (className != null && !className.equals(""))
			layout.setClassName(className);
		String cssStyle = getAttributeValue(node, UILayout.CSSSTYLE);
		if (cssStyle != null && !("").equals(cssStyle))
			layout.setCssStyle(cssStyle);
		NodeList list = node.getChildNodes();
		int size = list.getLength();
		for (int i=0;i<size;i++){
			Node child = list.item(i);
			if(child.getNodeType() == Node.TEXT_NODE)
				continue;
			layout.setElement(parseLayoutOrComp(child));
		}
		return layout;
//		if (nodeName.equals(GRID_LAYOUT)) {
//			NodeList list = node.getChildNodes();
//			int size = list.getLength();
//			for (int i = 0; i < size; i++) {
//				Node child = list.item(i);
//				if (child.getNodeType() == Node.TEXT_NODE)
//					continue;
//				parseLayoutPanel(child, layout, null);
//			}
//
//			UIGridLayout gridLayout = (UIGridLayout) layout;
//			List<UILayoutPanel> gridRowPanelList = gridLayout.getPanelList();
//			if (gridRowPanelList != null) {
//				for (UILayoutPanel gridRowPanel : gridRowPanelList) {
//					UIGridRowLayout gridRowLayout = (UIGridRowLayout) gridRowPanel
//							.getElement();
//					if (gridRowLayout != null) {
//						gridRowLayout.setParent(gridLayout);
//					}
//				}
//			}
//			return gridLayout;
//		} 
	}
	
	private UILayout parseLayout(Node node) {
		String nodeName = node.getNodeName();
		UILayout layout = null;
		if (nodeName.equals(FLOWV_LAYOUT)) {
			layout = (UIFlowvLayout) new UIFlowvLayout();
			String id = getAttributeValue(node, UIFlowvLayout.ID);
			((UIFlowvLayout) layout).setId(id);
			String widgetId = getAttributeValue(node, UIFlowvLayout.WIDGET_ID);
			((UIFlowvLayout) layout).setViewId(widgetId);
		} else if (nodeName.equals(PANEL_LAYOUT)) {
			layout = new UIPanel();
			String id = getAttributeValue(node, ID);
			((UIPanel) layout).setId(id);
			String className = getAttributeValue(node, UIPanel.STYLECLASSNAME);
			((UIPanel) layout).setClassName(className);

			String title = getAttributeValue(node, UIPanel.TITLE);
			((UIPanel) layout).setTitle(title);
			String renderType = getAttributeValue(node, UIPanel.RENDERTYPE);
			((UIPanel) layout).setRenderType(renderType);
			String langDir = getAttributeValue(node, UIPanel.LANGDIRF);
			((UIPanel) layout).setLangDir(langDir);
			String i18nName = getAttributeValue(node, UIPanel.I18NNAME);
			((UIPanel) layout).setI18nName(i18nName);
			String expand = getAttributeValue(node, UIPanel.EXPAND);
			if (expand != null) {
				// 原来是 设置的 Integer类型的值，现在设置的是 boolean类型的值
				// layout.setAttribute(UIPanel.EXPAND,
				// expand.equals("false") ? UIConstant.FALSE
				// : UIConstant.TRUE);
				((UIPanel) layout).setExpand(expand.equals("false") ? false : true);
			}
			String isCanExpand = getAttributeValue(node, UIPanel.ISCANEXPAND);
			if(isCanExpand != null) {
				((UIPanel) layout).setIsCanExpand(isCanExpand.equals("true"));
			}
			
		} else if (nodeName.equals(CANVAS_LAYOUT)) {
			layout = new UICanvas();
			String className = getAttributeValue(node, UICanvas.STYLECLASSNAME);
			if (className != null && !className.equals(""))
				((UICanvas) layout).setClassName(className);
			String title = getAttributeValue(node, UICanvas.TITLE);
			if (title != null)
				((UICanvas) layout).setTitle(title);
			String i18nName = getAttributeValue(node, UICanvas.I18NNAME);
			if (i18nName != null)
				((UICanvas) layout).setI18nName(i18nName);
			String langDir = getAttributeValue(node, UICanvas.LANGDIRF);
			if (langDir != null)
				((UICanvas) layout).setLangDir(langDir);
		} else if (nodeName.equals(FLOWH_LAYOUT)) {
			layout = new UIFlowhLayout();
			String isAuto = getAttributeValue(node, UIFlowhLayout.ISAUTO_FILL);
			if (isAuto != null && isAuto.equals("true"))
				((UIFlowhLayout) layout).setAutoFill(UIConstant.TRUE);
			else
				((UIFlowhLayout) layout).setAutoFill(UIConstant.FALSE);
		} else if (nodeName.equals(CARD_LAYOUT)) {
			layout = new UICardLayout();
			String id = getAttributeValue(node, ID);
			String widgetId = getAttributeValue(node, WIDGET_ID);
			((UICardLayout) layout).setViewId(widgetId);
			((UICardLayout) layout).setId(id);
			String currentItem = getAttributeValue(node, UICardLayout.CURRENT_ITEM);
			if (currentItem != null) {
				((UICardLayout) layout).setCurrentItem(currentItem);
			} else {
				((UICardLayout) layout).setCurrentItem("0");
			}
		} else if (nodeName.equals(SPLIT_LAYOUT)) {
			UISplitter spliter = new UISplitter();
			String id = getAttributeValue(node, ID);
			spliter.setId(id);
			String widgetId = getAttributeValue(node, WIDGET_ID);
			spliter.setViewId(widgetId);
			String orient = getAttributeValue(node, UISplitter.ORIENTATION);
			if (orient.equals("h"))
				spliter.setOrientation(Integer.valueOf(1));
			else
				spliter.setOrientation(Integer.valueOf(0));
			String boundMode = getAttributeValue(node, UISplitter.BOUNDMODE);
			{
				if (StringUtils.isNotBlank(boundMode)) {
					spliter.setBoundMode(Integer.valueOf(boundMode));
				}
			}
			String inverseValue = getAttributeValue(node, UISplitter.ISINVERSE);
			if (StringUtils.isNotBlank(inverseValue)) {
				if (inverseValue.equals("true")) {
					spliter.setInverse(1);
				} else {
					spliter.setInverse(0);
				}
			}
			String inverseFlowPanel = getAttributeValue(node, UISplitter.ISINVERSEFLOWPANEL);
			if (StringUtils.isNotBlank(inverseFlowPanel)) {
				if (inverseFlowPanel.equals("true")) {
					spliter.setInverseFlowPanel(1);
				} else {
					spliter.setInverseFlowPanel(0);
				}
			}
			String divideSize = getAttributeValue(node, UISplitter.DIVIDE_SIZE);
			if (divideSize != null)
				spliter.setDivideSize(divideSize);
			String oneTouch = getAttributeValue(node, UISplitter.ISONETOUCH);
			if (oneTouch != null && oneTouch.equals("true")) {
				spliter.setOneTouch(1);
			} else
				spliter.setOneTouch(0);
			layout = spliter;
		} else if (nodeName.equals(DIVID)) {
			UIDivid divid = new UIDivid();
			String id = getAttributeValue(node, ID);
			divid.setId(id);
			String widgetId = getAttributeValue(node, WIDGET_ID);
			divid.setViewId(widgetId);
			String orient = getAttributeValue(node, UIDivid.ORIENTATION);
			divid.setOrientation(orient);
			String inverseValue = getAttributeValue(node, UIDivid.ISINVERSE);
			if (StringUtils.isNotBlank(inverseValue)) {
				if (inverseValue.equals("true")) {
					divid.setInverse(UIConstant.TRUE);
				} else {
					divid.setInverse(UIConstant.FALSE);
				}
			}
			String isAnimate = getAttributeValue(node, UIDivid.ANIMATE);
			if (StringUtils.isNotBlank(isAnimate)) {
				if (isAnimate.equals("true")) {
					divid.setIsAnimate(UIConstant.TRUE);
				} else {
					divid.setIsAnimate(UIConstant.FALSE);
				}
			}
			String prop = getAttributeValue(node, UIDivid.PROP);
			if (prop != null)
				divid.setProp(Integer.valueOf(prop));
			layout = divid;
		} else if (nodeName.equals(MENUGROUP_LAYOUT)) {
			layout = new UIMenuGroup();
			String id = getAttributeValue(node, ID);
			((UIMenuGroup) layout).setId(id);
		} else if (nodeName.equals(BORDER)) {
			UIBorder border = new UIBorder();
			String width = getAttributeValue(node, UIBorder.WIDTH);
			border.setWidth(width);
			String leftWidth = getAttributeValue(node, UIBorder.LEFTWIDTH);
			border.setLeftWidth(leftWidth);
			String rightWidth = getAttributeValue(node, UIBorder.RIGHTWIDTH);
			border.setRightWidth(rightWidth);
			String topWidth = getAttributeValue(node, UIBorder.TOPWIDTH);
			border.setTopWidth(topWidth);
			String bottomWidth = getAttributeValue(node, UIBorder.BOTTOMWIDTH);
			border.setBottomWidth(bottomWidth);
			// widgetId
			String widgetId = getAttributeValue(node, UIBorder.WIDGET_ID);
			border.setViewId(widgetId);
			String showLeft = getAttributeValue(node, UIBorder.ISSHOWLEFT);
			if (showLeft == null)
				border.setShowLeft(0);
			else {
				border.setShowLeft(showLeft.equals("true") ? 0 : 1);
			}
			String showRight = getAttributeValue(node, UIBorder.ISSHOWRIGHT);
			if (showRight == null)
				border.setShowRight(0);
			else {
				border.setShowRight(showRight.equals("true") ? 0 : 1);
			}
			String showTop = getAttributeValue(node, UIBorder.ISSHOWTOP);
			if (showTop == null)
				border.setShowTop(0);
			else {
				border.setShowTop(showTop.equals("true") ? 0 : 1);
			}
			String showBottom = getAttributeValue(node, UIBorder.ISSHOWBOTTOM);
			if (showBottom == null)
				border.setShowBottom(0);
			else {
				border.setShowBottom(showBottom.equals("true") ? 0 : 1);
			}
			String id = getAttributeValue(node, UIBorder.ID);
			border.setId(id);
			String color = getAttributeValue(node, UIBorder.COLOR);
			border.setColor(color);
			String leftcolor = getAttributeValue(node, UIBorder.LEFTCOLOR);
			border.setLeftColor(leftcolor);
			String rightcolor = getAttributeValue(node, UIBorder.RIGHTCOLOR);
			border.setRightColor(rightcolor);
			String topcolor = getAttributeValue(node, UIBorder.TOPCOLOR);
			border.setTopColor(topcolor);
			String bottomcolor = getAttributeValue(node, UIBorder.BOTTOMCOLOR);
			border.setBottomColor(bottomcolor);
			String className = getAttributeValue(node, UIBorder.CLASSNAME);
			border.setClassName(className);
			String roundBorder = getAttributeValue(node, UIBorder.ROUNDBORDER);
			if (roundBorder != null && !roundBorder.equals("")) {
				border.setRoundBorder(roundBorder.equals("true") ? 0 : 1);
			} else
				border.setRoundBorder(1);
			layout = border;
		} else if (nodeName.equals(TAB_LAYOUT)) {
			UITabComp tabComp = new UITabComp();
			String id = getAttributeValue(node, ID);
			tabComp.setId(id);
			String widgetId = getAttributeValue(node, WIDGET_ID);
			tabComp.setViewId(widgetId);
			String tabWidth = getAttributeValue(node, UITabComp.TAB_WIDTH);
			tabComp.setTabWidth(tabWidth);
			String tabHeight = getAttributeValue(node, UITabComp.TAB_HEIGHT);
			tabComp.setTabHeight(tabHeight);
			String itemWidth = getAttributeValue(node, UITabComp.TAB_ITEMWIDTH);
			tabComp.setTabItemWidth(itemWidth);
			String itemHeight = getAttributeValue(node, UITabComp.TAB_ITEMHEIGHT);
			tabComp.setTabItemHeight(itemHeight);
			String bgColor = getAttributeValue(node, UITabComp.TAB_BGCOLOR);
			tabComp.setBgColor(bgColor);
			String activeItemColor = getAttributeValue(node, UITabComp.TAB_ACTITEMCOLOR);
			tabComp.setActiveItemColor(activeItemColor);
			String normalItemColor = getAttributeValue(node, UITabComp.TAB_NORITEMCOLOR);
			tabComp.setNormalItemColor(normalItemColor);
			String activeLineColor = getAttributeValue(node, UITabComp.TAB_ACTLINECOLOR);
			tabComp.setActiveLineColor(activeLineColor);
			String normalLineColor = getAttributeValue(node, UITabComp.TAB_NORLINECOLOR);
			tabComp.setNormalLineColor(normalLineColor);
			String normalFontColor = getAttributeValue(node, UITabComp.TAB_NORMALFONTCOLOR);
			tabComp.setNormalFontColor(normalFontColor);
			String activeFontColor = getAttributeValue(node, UITabComp.TAB_ACTIVEFONTCOLOR);
			tabComp.setActiveFontColor(activeFontColor);
			String isOuterTab = getAttributeValue(node, UITabComp.IS_OUTERTAB);
			tabComp.setIsOuterTab(isOuterTab);
			String fontSize = getAttributeValue(node, UITabComp.TAB_FONTSIZE);
			tabComp.setFontSize(fontSize);
			String currentIndex = getAttributeValue(node, UITabComp.CURRENT_ITEM);
			if (currentIndex != null)
				tabComp.setCurrentItem(Integer.parseInt(currentIndex));
			String needevent = getAttributeValue(node, UITabComp.CURRENT_NEEDEVENT);
			if (needevent != null) {
				if (needevent.equals("false") || needevent.equals("" + UIConstant.FALSE))
					tabComp.setNeedEvent(UIConstant.FALSE);
			}
			String tabType = getAttributeValue(node, UITabComp.TAB_TYPE);
			if (tabType != null)
				tabComp.setTabType(tabType);
			String oneTabHide = getAttributeValue(node, "oneTabHide");
			if (oneTabHide != null && !oneTabHide.equals("")) {
				tabComp.setOneTabHide(oneTabHide.equals("true") ? true : false);
			}
			layout = tabComp;
		} else if (nodeName.equals(SHUTTER_LAYOUT)) {
			UIShutter shutter = new UIShutter();
			String id = getAttributeValue(node, ID);
			shutter.setId(id);
			String widgetId = getAttributeValue(node, WIDGET_ID);
			shutter.setViewId(widgetId);
			String className = getAttributeValue(node, UIShutter.CLASSNAME);
			shutter.setClassName(className);
			String currentItem = getAttributeValue(node, "currentItem");
			if (currentItem != null && !currentItem.equals("")) {
				shutter.setCurrentItem(Integer.parseInt(currentItem));
			}
			layout = shutter;
		} else if (nodeName.equals(GRID_LAYOUT)) {
			UIGridLayout gridLayout = new UIGridLayout();
			String id = getAttributeValue(node, ID);
			gridLayout.setId(id);
			String widgetId = getAttributeValue(node, WIDGET_ID);
			gridLayout.setViewId(widgetId);
			String border = getAttributeValue(node, UIGridLayout.BORDER);
			if (border != null) {
				gridLayout.setBorder(border);
			}
			String borderColor = getAttributeValue(node, UIGridLayout.BORDERCOLOR);
			if (borderColor != null) {
				gridLayout.setBorderColor(borderColor);
			}
			String borderStyle = getAttributeValue(node, UIGridLayout.BORDERSTYLE);
			if (borderStyle != null) {
				gridLayout.setBorderStyle(borderStyle);
			}
			
			String cellBorder = getAttributeValue(node, UIGridLayout.CELLBORDER);
			if (cellBorder != null) {
				gridLayout.setCellBorder(cellBorder);
			}
			String cellBorderColor = getAttributeValue(node, UIGridLayout.CELLBORDERCOLOR);
			if (cellBorderColor != null) {
				gridLayout.setCellBorderColor(cellBorderColor);
			}
			String cellBorderStyle = getAttributeValue(node, UIGridLayout.CELLBORDERSTYLE);
			if (cellBorderStyle != null) {
				gridLayout.setCellBorderStyle(cellBorderStyle);
			}
			
			String colCount = getAttributeValue(node, UIGridLayout.COLCOUNT);
			if (colCount != null) {
				gridLayout.setColcount(Integer.valueOf(colCount));
			}
			String rowCount = getAttributeValue(node, UIGridLayout.ROWCOUNT);
			if (rowCount != null) {
				gridLayout.setRowcount(Integer.valueOf(rowCount));
			}
			layout = gridLayout;
		} else if (nodeName.equals(GRID_ROW)) {
			layout = new UIGridRowLayout();
			String id = getAttributeValue(node, ID);
			if (id != null)
				((UIGridRowLayout) layout).setId(id);
			String rowHeight = getAttributeValue(node, UIGridRowLayout.ROWHEIGHT);
			if (rowHeight != null)
				((UIGridRowLayout) layout).setRowHeight(rowHeight);
		} else
			return null;
		// modify by renxh
		String id = getAttributeValue(node, ID);
		if (id != null && !id.equals(""))
			layout.setId(id);
		String widgetId = getAttributeValue(node, WIDGET_ID);
		if (widgetId != null && !widgetId.equals(""))
			layout.setViewId(widgetId);
		String className = getAttributeValue(node, UILayout.STYLECLASSNAME);
		if (className != null && !className.equals(""))
			layout.setClassName(className);
		String cssStyle = getAttributeValue(node, UILayout.CSSSTYLE);
		if (cssStyle != null && !("").equals(cssStyle))
			layout.setCssStyle(cssStyle);
		if (nodeName.equals(GRID_LAYOUT)) {
			NodeList list = node.getChildNodes();
			int size = list.getLength();
			for (int i = 0; i < size; i++) {
				Node child = list.item(i);
				if (child.getNodeType() == Node.TEXT_NODE)
					continue;
				parseLayoutPanel(child, layout, null);
			}

			UIGridLayout gridLayout = (UIGridLayout) layout;
			List<UILayoutPanel> gridRowPanelList = gridLayout.getPanelList();
			if (gridRowPanelList != null) {
				for (UILayoutPanel gridRowPanel : gridRowPanelList) {
					UIGridRowLayout gridRowLayout = (UIGridRowLayout) gridRowPanel.getElement();
					if (gridRowLayout != null) {
						gridRowLayout.setParent(gridLayout);
					}
				}
			}
			return gridLayout;
		} else if (nodeName.equals(GRID_ROW)) {
			NodeList list = node.getChildNodes();
			int size = list.getLength();
			for (int i = 0; i < size; i++) {
				Node child = list.item(i);
				if (child.getNodeType() == Node.TEXT_NODE)
					continue;
				parseLayoutPanel(child, layout, null);
			}
			return layout;
		} else {
			NodeList list = node.getChildNodes();
			int size = list.getLength();
			for (int i = 0; i < size; i++) {
				Node child = list.item(i);
				if (child.getNodeType() == Node.TEXT_NODE)
					continue;
				parseLayoutPanel(child, layout, null);
			}
			return layout;
		}
	}

	/**
	 * 2011-9-8 下午06:29:02 renxh des：grid布局，行对应的UI
	 * 
	 * @param node
	 * @param parent
	 */
	public void parseGirdRowLayout(Node node, UIGridLayout parent, String pk_template) {
		String nodeName = node.getNodeName();
		if (nodeName.equals(GRID_ROW)) {
			UIGridRowLayout layout = new UIGridRowLayout();
			String id = getAttributeValue(node, ID);
			layout.setId(id);
			String widgetId = getAttributeValue(node, WIDGET_ID);
			layout.setViewId(widgetId);
			String rowHeight = getAttributeValue(node, UIGridRowLayout.ROWHEIGHT);
			layout.setRowHeight(rowHeight);
			NodeList list = node.getChildNodes();
			int size = list.getLength();
			for (int i = 0; i < size; i++) {
				Node child = list.item(i);
				if (child.getNodeType() == Node.TEXT_NODE)
					continue;
				if (pk_template != null)
					parseLayoutPanel(child, layout, pk_template);
				else
					parseLayoutPanel(child, layout, null);
			}
			parent.addGridRow(layout);
		} else {
			// LuiLogger.error(NCLangRes4VoTransl.getNCLangRes().getStrByID("bc",
			// "UIMetaParserUtil-000000")/*错误的节点结构!*/);
		}
	}

	private void parseLayoutPanel(Node panelNode, UILayout parent, String pk_template) {
		UILayoutPanel panel = null;
		String nodeName = panelNode.getNodeName();
		if (EVENTS.equals(nodeName)) {
			parseEvents(panelNode, parent);
			LuiEventConf[] events = meta.getEventConfs();
			List<LuiEventConf> list = new ArrayList<LuiEventConf>();
			if (events != null) {
				for (LuiEventConf event : events) {
					list.add(event);
				}
			}
			events = parent.getEventConfs();
			if (events != null) {
				for (LuiEventConf event : events) {
					list.add(event);
				}
			}
			meta.removeAllEventConf();
			for (LuiEventConf event : list) {
				meta.addEventConf(event);
			}
			return;
		} else if (nodeName.equals(PANEL_PANEL)) {
			UIPanelPanel panelLayout = new UIPanelPanel();
			panel = panelLayout;
		} else if (PANEL_RIGHT.equals(nodeName)) {
			UIPanelRightPanel rightPanel = new UIPanelRightPanel();
			panel = rightPanel;
		} else if (nodeName.equals(CANVAS_PANEL)) {
			UICanvasPanel panelLayout = new UICanvasPanel();
			panel = panelLayout;
		} else if (nodeName.equals(FLOWV_PANEL)) {
			UIFlowvPanel flowvLayout = new UIFlowvPanel();
			String height = getAttributeValue(panelNode, "height");
			if (height != null)
				flowvLayout.setHeight(height);
			String anchor = getAttributeValue(panelNode, "anchor");
			if (anchor != null)
				flowvLayout.setAnchor(anchor);
			panel = flowvLayout;
		} else if (nodeName.equals(FLOWH_PANEL)) {
			UIFlowhPanel flowhLayout = new UIFlowhPanel();
			String width = getAttributeValue(panelNode, "width");
			if (width != null)
				flowhLayout.setWidth(width);
			panel = flowhLayout;
		} else if (nodeName.equals(CARD_PANEL)) {
			UICardPanel cardLayout = new UICardPanel();
			cardLayout.setId(getAttributeValue(panelNode, ID));
			panel = cardLayout;
		} else if (nodeName.equals(SPLIT_ONE)) {
			UISplitterOne one = new UISplitterOne();
			panel = one;
		} else if (nodeName.equals(SPLIT_TWO)) {
			UISplitterTwo two = new UISplitterTwo();
			panel = two;
		} else if (nodeName.equals(DIVID_CENTER)) {
			UIDividCenter one = new UIDividCenter();
			panel = one;
		} else if (nodeName.equals(DIVID_PROP)) {
			UIDividProp two = new UIDividProp();
			panel = two;
		} else if (nodeName.equals(BORDER_PANEL)) {
			UIBorderTrue border = new UIBorderTrue();
			panel = border;
		} else if (nodeName.equals(TAB_RIGHT)) {
			UITabRightPanel item = new UITabRightPanel();
			String width = getAttributeValue(panelNode, UITabRightPanel.WIDTH);
			if (width != null && !width.equals(""))
				item.setWidth(width);
			panel = item;
		} else if ((TAB_ITEMS).equals(nodeName)) {
			NodeList nl = panelNode.getChildNodes();
			if (nl != null && nl.getLength() > 0) {
				for (int i = 0; i < nl.getLength(); i++) {
					if (TAB_ITEM.equals(nl.item(i).getNodeName())) {
						if (pk_template != null)
							parseLayoutPanel(nl.item(i), parent, pk_template);
						else
							parseLayoutPanel(nl.item(i), parent, null);
					}
				}
			}
			return;
		} else if (nodeName.equals(TAB_ITEM)) {
			UITabItem item = new UITabItem();
			String id = getAttributeValue(panelNode, UITabItem.ID);
			item.setId(id);
			String text = getAttributeValue(panelNode, UITabItem.TEXT);
			item.setText(text);
			String i18nName = getAttributeValue(panelNode, UITabItem.I18NNAME);
			item.setI18nName(i18nName);
			String langDir = getAttributeValue(panelNode, UITabItem.LANGDIRF);
			item.setLangDir(langDir);
			String showCloseIcon = getAttributeValue(panelNode, UITabItem.SHOWCLOSEICON);
			if (showCloseIcon != null && !showCloseIcon.equals("")) {
				item.setShowCloseIcon(showCloseIcon.equals("true") ? UIConstant.TRUE : UIConstant.FALSE);
			}
			String active = getAttributeValue(panelNode, UITabItem.ACTIVE);
			if (active != null && !active.equals("")) {
				item.setActive(active.equals("1") || active.equals("true") ? 1 : 0);
			}
			String state = getAttributeValue(panelNode, UITabItem.STATE);
			if (state != null && !state.equals("")) {
				item.setState(Integer.valueOf(state));
			}
			panel = item;
		} else if ((SHUTTER_ITEMS).equals(nodeName)) {
			NodeList nl = panelNode.getChildNodes();
			if (nl != null && nl.getLength() > 0) {
				for (int i = 0; i < nl.getLength(); i++) {
					if (SHUTTER_ITEM.equals(nl.item(i).getNodeName())) {
						if (pk_template != null)
							parseLayoutPanel(nl.item(i), parent, pk_template);
						else
							parseLayoutPanel(nl.item(i), parent, null);
					}
				}
			}
			return;
		} else if (nodeName.equals(SHUTTER_ITEM)) {
			UIShutterItem item = new UIShutterItem();
			String id = getAttributeValue(panelNode, UIShutterItem.ID);
			item.setId(id);
			String text = getAttributeValue(panelNode, UIShutterItem.TEXT);
			item.setText(text);
			String i18nName = getAttributeValue(panelNode, UIShutterItem.I18NNAME);
			item.setI18nName(i18nName);
			String langDir = getAttributeValue(panelNode, UIShutterItem.LANGDIRF);
			item.setLangDir(langDir);
			panel = item;
		} else if (nodeName.equals(MENUGROUP_ITEM)) {
			UIMenuGroupItem groupItem = new UIMenuGroupItem();
			String state = getAttributeValue(panelNode, STATE);
			if (state != null)
				groupItem.setState(Integer.valueOf(state));
			panel = groupItem;
		} else if (nodeName.equals(GRID_PANEL)) {
			UIGridPanel gridPanel = new UIGridPanel();
			String rowSpan = getAttributeValue(panelNode, UIGridPanel.ROWSPAN);
			if (rowSpan != null) {
				gridPanel.setRowSpan(rowSpan);
			}
			String colSpan = getAttributeValue(panelNode, UIGridPanel.COLSPAN);
			if (colSpan != null)
				gridPanel.setColSpan(colSpan);
			String colWidth = getAttributeValue(panelNode, UIGridPanel.COLWIDTH);
			if (colWidth != null)
				gridPanel.setColWidth(colWidth);
			String colHeight = getAttributeValue(panelNode, UIGridPanel.COLHEIGHT);
			if (colHeight != null)
				gridPanel.setColHeight(colHeight);
			String id = getAttributeValue(panelNode, UIGridPanel.ID);
			if (id != null)
				gridPanel.setId(id);
			String rowIndex = getAttributeValue(panelNode, UIGridPanel.ROWINDEX);
			if (rowIndex != null) {
				gridPanel.setRowIndex(rowIndex);
			}
			String colIndex = getAttributeValue(panelNode, UIGridPanel.COLINDEX);
			if (colIndex != null) {
				gridPanel.setColIndex(colIndex);
			}
			panel = gridPanel;
		} else if (nodeName.equals(GRID_ROW_PANEL)) {
			UIGridRowPanel gridRowPanel = new UIGridRowPanel(null);
			panel = gridRowPanel;
		}
		String id = getAttributeValue(panelNode, ID);
		if (id != null)
			panel.setId(id);
		// modify by chouhl: set widgetId to attribute
		String widgetId = getAttributeValue(panelNode, WIDGET_ID);
		if (widgetId != null) {
			panel.setViewId(widgetId);
		}
		String cssStyle = getAttributeValue(panelNode, UILayoutPanel.CSSSTYLE);
		if (cssStyle != null && !cssStyle.equals(""))
			panel.setCssStyle(cssStyle);
		String leftPadding = getAttributeValue(panelNode, UILayoutPanel.LEFTPADDING);
		if (leftPadding != null && !leftPadding.equals(""))
			panel.setLeftPadding(leftPadding);
		String rightPadding = getAttributeValue(panelNode, UILayoutPanel.RIGHTPADDING);
		if (rightPadding != null && !rightPadding.equals(""))
			panel.setRightPadding(rightPadding);
		String topPadding = getAttributeValue(panelNode, UILayoutPanel.TOPPADDING);
		if (topPadding != null && !topPadding.equals(""))
			panel.setTopPadding(topPadding);
		String bottomPadding = getAttributeValue(panelNode, UILayoutPanel.BOTTOMPADDING);
		if (bottomPadding != null && !bottomPadding.equals(""))
			panel.setBottomPadding(bottomPadding);
		String leftBorder = getAttributeValue(panelNode, UILayoutPanel.LEFTBORDER);
		if (leftBorder != null && !leftBorder.equals(""))
			panel.setLeftBorder(leftBorder);
		String rightBorder = getAttributeValue(panelNode, UILayoutPanel.RIGHTBORDER);
		if (rightBorder != null && !rightBorder.equals(""))
			panel.setRightBorder(rightBorder);
		String topBorder = getAttributeValue(panelNode, UILayoutPanel.TOPBORDER);
		if (topBorder != null && !topBorder.equals(""))
			panel.setTopBorder(topBorder);
		String bottomBorder = getAttributeValue(panelNode, UILayoutPanel.BOTTOMBORDER);
		if (bottomBorder != null && !bottomBorder.equals(""))
			panel.setBottomBorder(bottomBorder);
		String border = getAttributeValue(panelNode, UILayoutPanel.BORDER);
		if (border != null && !border.equals(""))
			panel.setBorder(border);
		String className = getAttributeValue(panelNode, UILayoutPanel.STYLECLASSNAME);
		if (className != null && !className.equals(""))
			panel.setClassName(className);
		NodeList list = panelNode.getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			Node node = list.item(i);
			if (node.getNodeType() == Node.TEXT_NODE)
				continue;
			if (pk_template != null) {
				UIElement ele = parseLayoutOrComp(node);
				if (ele != null) {
					panel.setElement(ele);
				}
			} else {
				panel.setElement(parseLayoutOrComp(node));
			}
		}
		parent.addPanel(panel);
	}

	private UIElement parseLayoutOrComp(Node node) {
		UIElement ele;
		if (node.getNodeName().equals("AbsoluteLayout")){
			ele = parseAbsoluteLayout(node);
		}else if (node.getNodeName().endsWith("Layout") || node.getNodeName().equals(SPLIT_LAYOUT) || node.getNodeName().equals(DIVID) || node.getNodeName().equals(MENUGROUP_LAYOUT)
				|| node.getNodeName().equals(BORDER) || node.getNodeName().equals(GRID_ROW))
			ele = parseLayout(node);
		else if (node.getNodeName().equals(UIViewPart))
			ele = parseWidget(node);
		else
			ele = parseComponent(node);
		return ele;
	}

	private UIElement parseWidget(Node node) {
		if (!node.getNodeName().equals(UIViewPart) && !(node.getNodeName().equals(UIDIALOG)))
			return null;
		String id = getAttributeValue(node, "id");
		UIMetaParserUtil util = new UIMetaParserUtil(this.isDesign);
		util.setPagemeta(pagemeta);
		UIPartMeta childMeta = null;
		if (pagemeta == null)
			return null;
		String folderPath = meta.getFolderPath();
		String pageId=pagemeta.getId();
		if (("/lui/nodes/"+pageId+"/").equalsIgnoreCase(folderPath)) {
			childMeta = util.parseUIMeta(pagemeta.getId(), id);
		} else {
			childMeta = util.parseUIMeta(folderPath, pagemeta.getId(), id);
		}
		UIViewPart widget = new UIViewPart();
		widget.setId(id);
		widget.setUimeta(childMeta);
		String isAuto = getAttributeValue(node, UIFlowhLayout.ISAUTO_FILL);
		if (isAuto != null && isAuto.equals("true"))
			widget.setAutoFill(UIConstant.TRUE);
		else
			widget.setAutoFill(UIConstant.FALSE);
		return widget;
	}

	private UIElement parseDialog(Node node) {
		if (!node.getNodeName().equals(UIViewPart) && !(node.getNodeName().equals(UIDIALOG)))
			return null;
		String id = getAttributeValue(node, "id");
		UIMetaParserUtil util = new UIMetaParserUtil(this.isDesign);
		util.setPagemeta(pagemeta);
		UIPartMeta childMeta = null;
		String folderPath = meta.getFolderPath();
		if ("/lui/nodes/".equalsIgnoreCase(folderPath)) {
			childMeta = util.parseUIMeta(pagemeta.getId(), id);
		} else {
			childMeta = util.parseUIMeta(folderPath, pagemeta.getId(), id);
		}
		UIDialog widget = new UIDialog();
		widget.setId(id);
		widget.setUimeta(childMeta);
		String width = getAttributeValue(node, "width");
		String height = getAttributeValue(node, "height");
		if (width != null && !width.equals(""))
			widget.setWidth(width);
		if (height != null && !height.equals(""))
			widget.setHeight(height);
		return widget;
	}

	private UIComponent parseComponent(Node node) {
		UIComponent comp = null;
		if (node.getNodeName().equals(UIGRID)) {
			comp = new UIGridComp();
			String autoExpand = getAttributeValue(node, "autoExpand");
			if (autoExpand != null && !autoExpand.equals(""))
				((UIGridComp) comp).setAutoExpand((Integer.valueOf(autoExpand)));
		} else if (node.getNodeName().equals(UIPROPERTYGRID)) {
			comp = new UIPropertyGridComp();
		} else if (node.getNodeName().equals(UITREE)) {
			comp = new UITreeComp();
		} else if (node.getNodeName().equals(UIFORM)) {
			comp = new UIFormComp();
			((UIFormComp) comp).setLabelPosition(getAttributeValue(node, UIFormComp.LABEL_POSITION));
		} else if (node.getNodeName().equals(SILVERLIGHTWIDGET)) {
			comp = new UISilverlightWidget();
		} else if (node.getNodeName().equals(UIFORMGROUP)) {
			comp = new UIFormGroupComp();
			String forms = getAttributeValue(node, "formList");
			((UIFormGroupComp) comp).setForms(forms);
		} else if (node.getNodeName().equals(UIMENUBAR)) {
			comp = new UIMenubarComp();
		} else if (node.getNodeName().equals(UIIFRAME)) {
			comp = new UIIFrame();
		} else if (node.getNodeName().equals(UIHTMLCONTENT)) {
			comp = new UIPartComp();
		} else if (node.getNodeName().equals(UITEXT)) {
			comp = new UITextField();
			String type = getAttributeValue(node, "type");
			((UITextField) comp).setType(type);
			((UITextField) comp).setImgsrc(getAttributeValue(node, UITextField.IMG_SRC));
			String valgin = getAttributeValue(node, UITextField.VALGIN);
			if (valgin != null && !valgin.isEmpty()) {
				((UITextField) comp).setValgin(valgin);
			}
		} else if (node.getNodeName().equals(UIBUTTON)) {
			comp = new UIButton();
		} else if (node.getNodeName().equals(UICHARTCOMP))
			comp = new UIChartComp();
		else if (node.getNodeName().equals(UILINKCOMP)) {
			comp = new UILinkComp();
		} else if (node.getNodeName().equals(UITOOLBAR)) {
			comp = new UIToolBar();
		} else if (node.getNodeName().equals(UILABEL)) {
			UILabelComp lc = new UILabelComp();
			lc.setTextAlign(getAttributeValue(node, UILabelComp.TEXTALIGN));
			lc.setCssStyle(getAttributeValue(node, UILabelComp.CSSSTYLE));
			lc.setSize(getAttributeValue(node, UILabelComp.SIZE));
			lc.setStyle(getAttributeValue(node, UILabelComp.STYLE));
			lc.setFamily(getAttributeValue(node, UILabelComp.FAMILY));
			comp = lc;
		} else if (node.getNodeName().equals(UIIMAGE)) {
			comp = new UIImageComp();
		} else if (node.getNodeName().equals(UISELFDEFCOMP)) {
			comp = new UISelfDefComp();
		} else if (node.getNodeName().equals(UIFORMELEMENT)) {
			UIFormElement fe = new UIFormElement();
			fe.setFormId(getAttributeValue(node, UIFormElement.FORM_ID));
			String eleWidth = getAttributeValue(node, UIFormElement.ELEWIDTH);
			if (eleWidth != null && !eleWidth.equals(""))
				fe.setEleWidth(eleWidth);
			comp = fe;
		}else{
			ILuiPaltformExtProvier[] providers = LuiPaltformContranier.getInstance().getProvideres();
			for(int i=0;i<providers.length;i++){
				ILuiPaltformExtProvier inner=providers[i];
				String layOutTagName=inner.getLayoutTagName();
				if(layOutTagName.equalsIgnoreCase(node.getNodeName())){
					comp=(UIComponent)LuiClassUtil.loadClass(inner.getUICompClazz().getName());
					break;
				}
			}
		}
		if (comp == null)
			return null;
		String id = getAttributeValue(node, ID);
		comp.setId(id);
		String widgetId = getAttributeValue(node, WIDGET_ID);
		comp.setViewId(widgetId);
		String width = getAttributeValue(node, "width");
		if (width != null && !width.equals(""))
			comp.setWidth(width);
		String height = getAttributeValue(node, "height");
		if (height != null && !height.equals(""))
			comp.setHeight(height);
		String align = getAttributeValue(node, "align");
		if (align != null && !align.equals(""))
			comp.setAlign(align);
		String left = getAttributeValue(node, "left");
		if (left != null && !left.equals(""))
			comp.setLeft(Integer.parseInt(left));
		String top = getAttributeValue(node, "top");
		if (top != null && !top.equals(""))
			comp.setTop(Integer.parseInt(top));
		String position = getAttributeValue(node, "position");
		if (position != null && !position.equals(""))
			comp.setPosition(position);
		String maxWidth = getAttributeValue(node, "maxWidth");
		if (maxWidth != null && !maxWidth.equals("")) {
			comp.setMaxWidth(maxWidth);
		}
		String className = getAttributeValue(node, UIComponent.STYLECLASSNAME);
		if (className != null && !className.equals(""))
			comp.setClassName(className);
		return comp;
	}

	private static final String EVENT_TAG_EVENTS = "Events";
	private static final String EVENT_TAG_EVENT = "Event";
	private static final String EVENT_PROP_EVENT_NAME = "eventName";
	private static final String EVENT_PROP_METHOD = "method";
	private static final String EVENT_PROP_CONTROLLER = "controller";
	private static final String EVENT_PROP_JS_EVENT_CLAZZ = "eventType";
	private static final String EVENT_PROP_ON_SERVER = "onserver";
	private static final String EVENT_PROP_ASYNC = "async";
	private static final String EVENT_TAG_SUBMIT_RULE = "SubmitRule";
	private static final String EVENT_TAG_PARAMS = "Params";
	private static final String EVENT_TAG_ACTION = "Action";
	private static final String SUBMIT_RULE_PROP_CARD_SUBMIT = "cardSubmit";
//	private static final String SUBMIT_RULE_PROP_TAB_SUBMIT = "tabSubmit";
//	private static final String SUBMIT_RULE_PROP_PANEL_SUBMIT = "panelSubmit";
	private static final String SUBMIT_RULE_PROP_PAGEMETA = "pagemeta";

	/**
	 * Event DOM解析
	 * 
	 * @param node
	 * @param element
	 */
	public void parseEvents(Node node, UIElement element) {
		if (node == null || element == null) {
			return;
		}
		if (EVENT_TAG_EVENTS.equals(node.getNodeName())) {
			NodeList nl = node.getChildNodes();
			if (nl != null && nl.getLength() > 0) {
				element.removeAllEventConf();
				for (int i = 0; i < nl.getLength(); i++) {
					if (EVENT_TAG_EVENT.equals(nl.item(i).getNodeName())) {
						LuiEventConf event = new LuiEventConf();
						event.setAsync(Boolean.valueOf(getAttributeValue(nl.item(i), EVENT_PROP_ASYNC)));
						event.setOnserver(Boolean.valueOf(getAttributeValue(nl.item(i), EVENT_PROP_ON_SERVER)));
						event.setEventName(getAttributeValue(nl.item(i), EVENT_PROP_EVENT_NAME));
						event.setMethod(getAttributeValue(nl.item(i), EVENT_PROP_METHOD));
						event.setControllerClazz(getAttributeValue(nl.item(i), EVENT_PROP_CONTROLLER));
						event.setEventType(getAttributeValue(nl.item(i), EVENT_PROP_JS_EVENT_CLAZZ));
						NodeList nl1 = nl.item(i).getChildNodes();
						if (nl1 != null && nl1.getLength() > 0) {
							for (int j = 0; j < nl1.getLength(); j++) {
								if (EVENT_TAG_SUBMIT_RULE.equals(nl1.item(j).getNodeName())) {
									event.setSubmitRule(parseSubmitRule(nl1.item(j)));
								} else if (EVENT_TAG_PARAMS.equals(nl1.item(j).getNodeName())) {
									NodeList params = nl1.item(j).getChildNodes();
									if (params != null && params.getLength() > 0) {
										Map<String, Parameter> map = parseParameter(params);
										Iterator<String> keys = map.keySet().iterator();
										while (keys.hasNext()) {
											event.addParam(map.get(keys.next()));
										}
									}
								} else if (EVENT_TAG_ACTION.equals(nl1.item(j).getNodeName())) {
									event.setScript(nl1.item(j).getTextContent().trim());
								}
							}
						}
						element.addEventConf(event);
					}
				}
			}
		}
	}

	private EventSubmitRule parseSubmitRule(Node node) {
		EventSubmitRule rule = null;
		if (node != null && EVENT_TAG_SUBMIT_RULE.equals(node.getNodeName())) {
			rule = new EventSubmitRule();
			rule.setPagemeta(getAttributeValue(node, SUBMIT_RULE_PROP_PAGEMETA));
			rule.setCardSubmit(Boolean.valueOf((getAttributeValue(node, SUBMIT_RULE_PROP_CARD_SUBMIT))));
			NodeList nl = node.getChildNodes();
			if (nl != null && nl.getLength() > 0) {
				rule.setWidgetRules(parseWidgetRule(nl));
				rule.setParams(parseParameter(nl));
				for (int i = 0; i < nl.getLength(); i++) {
					if (EVENT_TAG_SUBMIT_RULE.equals(nl.item(i).getNodeName())) {
						rule.setParentSubmitRule(parseSubmitRule(nl.item(i)));
					}
				}
			}
		}
		return rule;
	}

	private static final String SUBMIT_RULE_TAG_WIDGET = "Widget";
	private static final String WIDGET_RULE_PROP_ID = "id";
	private static final String WIDGET_RULE_PROP_CARD_SUBMIT = "cardSubmit";
	private static final String WIDGET_RULE_PROP_TAB_SUBMIT = "tabSubmit";
	private static final String WIDGET_RULE_PROP_PANEL_SUBMIT = "panelSubmit";

	private LuiSet<WidgetRule> parseWidgetRule(NodeList nl) {
		LuiSet<WidgetRule> widgetRule = new LuiHashSet<WidgetRule>();
		for (int i = 0; i < nl.getLength(); i++) {
			if (SUBMIT_RULE_TAG_WIDGET.equals(nl.item(i).getNodeName())) {
				WidgetRule wr = new WidgetRule();
				wr.setId(getAttributeValue(nl.item(i), WIDGET_RULE_PROP_ID));
				wr.setCardSubmit(Boolean.valueOf((getAttributeValue(nl.item(i), WIDGET_RULE_PROP_CARD_SUBMIT))));
				wr.setTabSubmit(Boolean.valueOf((getAttributeValue(nl.item(i), WIDGET_RULE_PROP_TAB_SUBMIT))));
				wr.setPanelSubmit(Boolean.valueOf((getAttributeValue(nl.item(i), WIDGET_RULE_PROP_PANEL_SUBMIT))));
				NodeList children = nl.item(i).getChildNodes();
				if (children != null && children.getLength() > 0) {
					Map<String, Object> rules = parseWidgetChildRule(children);
					Iterator<String> keys = rules.keySet().iterator();
					String key = null;
					while (keys.hasNext()) {
						key = keys.next();
						if (key.startsWith(WIDGET_RULE_TAG_DATASET)) {
							wr.addDsRule((DatasetRule) rules.get(key));
						} else if (key.startsWith(WIDGET_RULE_TAG_TREE)) {
							wr.addTreeRule((TreeRule) rules.get(key));
						} else if (key.startsWith(WIDGET_RULE_TAG_GRID)) {
							wr.addGridRule((GridRule) rules.get(key));
						} else if (key.startsWith(WIDGET_RULE_TAG_FORM)) {
							wr.addFormRule((FormRule) rules.get(key));
						}
					}
				}
				widgetRule.add(wr);
			}
		}
		return widgetRule;
	}

	private static final String WIDGET_RULE_TAG_DATASET = "Dataset";
	private static final String WIDGET_RULE_TAG_TREE = "Tree";
	private static final String WIDGET_RULE_TAG_GRID = "Grid";
	private static final String WIDGET_RULE_TAG_FORM = "Form";
	private static final String OBJECT_RULE_PROP_ID = "id";
	private static final String OBJECT_RULE_PROP_TYPE = "type";

	private Map<String, Object> parseWidgetChildRule(NodeList nl) {
		Map<String, Object> map = new HashMap<String, Object>();
		for (int i = 0; i < nl.getLength(); i++) {
			if (WIDGET_RULE_TAG_DATASET.equals(nl.item(i).getNodeName())) {
				DatasetRule dr = new DatasetRule();
				dr.setId(getAttributeValue(nl.item(i), OBJECT_RULE_PROP_ID));
				dr.setType(getAttributeValue(nl.item(i), OBJECT_RULE_PROP_TYPE));
				map.put(WIDGET_RULE_TAG_DATASET + "_" + dr.getId(), dr);
			} else if (WIDGET_RULE_TAG_TREE.equals(nl.item(i).getNodeName())) {
				TreeRule tr = new TreeRule();
				tr.setId(getAttributeValue(nl.item(i), OBJECT_RULE_PROP_ID));
				tr.setType(getAttributeValue(nl.item(i), OBJECT_RULE_PROP_TYPE));
				map.put(WIDGET_RULE_TAG_TREE + "_" + tr.getId(), tr);
			} else if (WIDGET_RULE_TAG_GRID.equals(nl.item(i).getNodeName())) {
				GridRule gr = new GridRule();
				gr.setId(getAttributeValue(nl.item(i), OBJECT_RULE_PROP_ID));
				gr.setType(getAttributeValue(nl.item(i), OBJECT_RULE_PROP_TYPE));
				map.put(WIDGET_RULE_TAG_GRID + "_" + gr.getId(), gr);
			} else if (WIDGET_RULE_TAG_FORM.equals(nl.item(i).getNodeName())) {
				FormRule fr = new FormRule();
				fr.setId(getAttributeValue(nl.item(i), OBJECT_RULE_PROP_ID));
				fr.setType(getAttributeValue(nl.item(i), OBJECT_RULE_PROP_TYPE));
				map.put(WIDGET_RULE_TAG_FORM + "_" + fr.getId(), fr);
			}
		}
		return map;
	}

	private static final String PARAMS_TAG_PARAM = "Param";
	private static final String PARAM_TAG_NAME = "Name";
	private static final String PARAM_TAG_VALUE = "Value";
	private static final String PARAM_TAG_DESC = "Desc";

	private Map<String, Parameter> parseParameter(NodeList nl) {
		Map<String, Parameter> map = new HashMap<String, Parameter>();
		for (int i = 0; i < nl.getLength(); i++) {
			if (PARAMS_TAG_PARAM.equals(nl.item(i).getNodeName())) {
				NodeList param = nl.item(i).getChildNodes();
				if (param != null && param.getLength() > 0) {
					Parameter luiParam = new Parameter();
					for (int k = 0; k < param.getLength(); k++) {
						if (PARAM_TAG_NAME.equals(param.item(k).getNodeName())) {
							luiParam.setName(param.item(k).getTextContent());
						} else if (PARAM_TAG_VALUE.equals(param.item(k).getNodeName())) {
							luiParam.setValue(param.item(k).getTextContent());
						} else if (PARAM_TAG_DESC.equals(param.item(k).getNodeName())) {
							luiParam.setDesc(param.item(k).getTextContent().trim());
						}
					}
					map.put(luiParam.getName() + "_" + i, luiParam);
				}
			}
		}
		return map;
	}

	private String getAttributeValue(Node node, String key) {
		Node attr = node.getAttributes().getNamedItem(key);
		if (attr == null)
			return null;
		return attr.getNodeValue();
	}

	// 将String流的转换成input流
	private java.io.InputStream getInput(String metaStr) {
		java.io.InputStream input = null;
		try {
			input = new java.io.ByteArrayInputStream(metaStr.getBytes("utf-8"));
		} catch (UnsupportedEncodingException e) {
			LuiLogger.error(e.getMessage(), e);
			throw new LuiRuntimeException(e.getMessage(), e);
		}
		return input;
	}
}
