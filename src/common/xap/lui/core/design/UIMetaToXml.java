package xap.lui.core.design;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import xap.lui.core.layout.UIAbsoluteLayout;
import xap.lui.core.layout.UIBarChartComp;
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
import xap.lui.core.layout.UIDiv;
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
import xap.lui.core.layout.UILineCharComp;
import xap.lui.core.layout.UILinkComp;
import xap.lui.core.layout.UIMenuGroup;
import xap.lui.core.layout.UIMenuGroupItem;
import xap.lui.core.layout.UIMenubarComp;
import xap.lui.core.layout.UIPanel;
import xap.lui.core.layout.UIPanelPanel;
import xap.lui.core.layout.UIPartComp;
import xap.lui.core.layout.UIPartMeta;
import xap.lui.core.layout.UIPieChartComp;
import xap.lui.core.layout.UIPropertyGridComp;
import xap.lui.core.layout.UISelfDefComp;
import xap.lui.core.layout.UIShutter;
import xap.lui.core.layout.UIShutterItem;
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
import xap.lui.core.xml.XMLUtil;
public class UIMetaToXml {
	
	public static void toXml(UIPartMeta meta, Writer writer, String folderPath) {
		Document doc = getDocumentByUIMeta(meta);
		XMLUtil.printDOMTree(writer, doc, 0, "UTF-8");
	}
	public static String toString(UIPartMeta meta) {
		Document doc = getDocumentByUIMeta(meta);
		Writer wt = new StringWriter();
		XMLUtil.printDOMTree(wt, doc, 0, "UTF-8");
		String xmlStr = wt.toString();
		return xmlStr;
	}
	private static Document getDocumentByUIMeta(UIPartMeta meta) {
		Document doc = XMLUtil.getNewDocument();
		UIElement element = meta.getElement();
		Element node = doc.createElement("UIPart");
		node.setAttribute("xmlns","http://com.founer.xap/schema/lui");
		node.setAttribute("xsi:schemaLocation","http://com.founer.xap/schema/lui http://com.founer.xap/schema/lui/layout.xsd");
		node.setAttribute("xmlns:xsi","http://www.w3.org/2001/XMLSchema-instance" );
		String id = (String) meta.getId();
		node.setAttribute(UIPartMeta.ID, id);
		Integer isJquery = (Integer) meta.getJquery();
		if (isJquery != null)
			node.setAttribute(UIPartMeta.ISJQUERY, String.valueOf(isJquery));
		String uiprovider = meta.getUiprovider();
		if (uiprovider != null && !uiprovider.equals("")) {
			node.setAttribute("uiprovider", uiprovider);
		}
		// 是否流式布局
		Boolean isFlowMode = meta.isFlow();
		if(isFlowMode!=null) {
			node.setAttribute(UIPartMeta.ISFLOW, String.valueOf(isFlowMode));
		}
		doc.appendChild(node);
		if (meta.getIncludejs() != null) {
			String includejs = meta.getIncludejs().toString();
			if (includejs != null)
				node.setAttribute(UIPartMeta.INCLUDEJS, includejs);
		}
		if (meta.getIncludecss() != null) {
			String includecss = meta.getIncludecss().toString();
			if (includecss != null)
				node.setAttribute(UIPartMeta.INCLUDECSS, includecss);
		}
		// 判断uimeta的动态生成uimeta类是否为空,不为空保存此类
		if (meta.getAttribute(UIPartMeta.GENERATECLASS) != null) {
			String generateClass = (String) meta.getAttribute(UIPartMeta.GENERATECLASS);
			if (generateClass != null && !generateClass.equals(""))
				node.setAttribute(UIPartMeta.GENERATECLASS, generateClass);
			Boolean tabBody = meta.getTabBody();
			if (!tabBody.toString().equals("")) {
				node.setAttribute(UIPartMeta.TABBODY, tabBody.toString());
			}
		}
		if (element != null)
			createLayoutOrComponent(element, doc, node);
		if (meta.getDialogMap() != null)
			createDialog(meta.getDialogMap(), doc, node);
		// Element rootNode = doc.createElement("Widget");
		// doc.appendChild(rootNode);
		return doc;
	}
	private static void createDialog(Map<String, UIViewPart> dialogMap, Document doc, Node parent) {
		Iterator<UIViewPart> wit = dialogMap.values().iterator();
		while (wit.hasNext()) {
			UIViewPart w = wit.next();
			String widgetName = UIConstant.UIDIALOG;
			Element widget = doc.createElement(widgetName);
			widget.setAttribute("id", w.getId());
			parent.appendChild(widget);
		}
	}
	
	private static void createAbsoluteLayout(UIAbsoluteLayout layout,
			Document doc, Node parent) {
		Element node = null;
		String layoutName = UIConstant.ABSOLUTE_LAYOUT;
		node = doc.createElement(layoutName);
		String id = String.valueOf(layout.getId());
		node.setAttribute(UIAbsoluteLayout.ID, id);
		Integer autoFill = layout.getAutoFill();
		node.setAttribute(UIFlowhLayout.ISAUTO_FILL, autoFill == null ? "false" : "" + UIConstant.TRUE.equals(autoFill));
		if (layout.getViewId() != null) {
			String widgetId = String.valueOf(layout.getViewId());
			node.setAttribute(UIFlowhLayout.WIDGET_ID, widgetId);
		}

		String cssStyle = layout.getCssStyle();
		if (cssStyle != null)
			node.setAttribute(UILayout.CSSSTYLE, cssStyle);
		String className = layout.getClassName();
		if (className != null) {
			node.setAttribute(UILayout.STYLECLASSNAME, className);
		}
		if (node == null)
			return;
		parent.appendChild(node);
		List<UIComponent> list = layout.getComponentList();
		Iterator<UIComponent> it = list.iterator();
		while (it.hasNext()) {
			UIComponent	 component= it.next();
			createComponent(component, doc, node);
		}
//		if (layout instanceof UITabComp) {
//			UITabComp uiTab = (UITabComp) layout;
//			UITabRightPanel rightPanel = uiTab.getRightPanel();
//			if (rightPanel != null && rightPanel.getElement() != null)
//				createLayoutPanel(rightPanel, doc, node);
//		}
	}
	
	private static void createLayout(UILayout layout, Document doc, Node parent) {
		Element node = null;
		if (layout instanceof UICardLayout) {
			String layoutName = UIConstant.CARD_LAYOUT;
			String id = ((UICardLayout) layout).getId();
			node = doc.createElement(layoutName);
			if (id != null)
				node.setAttribute("id", id);
			if (((UICardLayout) layout).getCurrentItem() != null) {
				String currentItem = ((UICardLayout) layout).getCurrentItem();
				if (currentItem != null) {
					// String value = "0";
					// if(currentItem.equals("1"))
					// value = "1";
					node.setAttribute(UICardLayout.CURRENT_ITEM, currentItem);
				}
			}
			String widgetId = ((UICardLayout) layout).getViewId();
			if (widgetId != null)
				node.setAttribute("widgetId", widgetId);
		} else if (layout instanceof UIFlowhLayout) {
			String layoutName = UIConstant.FLOWH_LAYOUT;
			node = doc.createElement(layoutName);
			String id = String.valueOf(layout.getId());
			node.setAttribute(UIFlowhLayout.ID, id);
			Integer autoFill = ((UIFlowhLayout)layout).getAutoFill();
			node.setAttribute(UIFlowhLayout.ISAUTO_FILL, autoFill == null ? "false" : "" + UIConstant.TRUE.equals(autoFill));
			if (layout.getViewId() != null) {
				String widgetId = String.valueOf(layout.getViewId());
				node.setAttribute(UIFlowhLayout.WIDGET_ID, widgetId);
			}
		} else if (layout instanceof UIFlowvLayout) {
			String layoutName = UIConstant.FLOWV_LAYOUT;
			node = doc.createElement(layoutName);
			String id = String.valueOf(layout.getId());
			node.setAttribute(UIFlowvLayout.ID, id);
			if (layout.getViewId() != null) {
				String widgetId = String.valueOf(layout.getViewId());
				node.setAttribute(UIFlowvLayout.WIDGET_ID, widgetId);
			}
		} else if (layout instanceof UICanvas) {
			String canvasName = UIConstant.CANVAS_LAYOUT;
			node = doc.createElement(canvasName);
			UICanvas canvas = (UICanvas) layout;
			String id = String.valueOf(canvas.getId());
			node.setAttribute(UIElement.ID, id);
			String widgetId = canvas.getViewId();
			if (widgetId != null) {
				node.setAttribute(UIElement.WIDGET_ID, widgetId);
			}
			String className = canvas.getClassName();
			if (className != null) {
				node.setAttribute(UICanvas.STYLECLASSNAME, className);
			}
		} else if (layout instanceof UIPanel) {
			String layoutName = UIConstant.PANEL_LAYOUT;
			node = doc.createElement(layoutName);
			UIPanel panel = (UIPanel) layout;
			String id = (String) panel.getId();
			node.setAttribute("id", id);
			String title = (String) panel.getTitle();
			if (title != null && !title.equals(""))
				node.setAttribute(UIPanel.TITLE, String.valueOf(title));
			String renderType = panel.getRenderType();
			if (renderType != null && !("").equals(renderType))
				node.setAttribute(UIPanel.RENDERTYPE, renderType);
			// UIConstant中true对应0
			String expand = (panel.isExpand()!=null && panel.isExpand()) ? "true" : "false";
			if (!StringUtils.isBlank(expand))
				node.setAttribute(UIPanel.EXPAND, expand);
			String className = (String) panel.getCssStyle();
			if (className != null && !className.equals(""))
				node.setAttribute(UIPanel.STYLECLASSNAME, className);
			if (panel.getViewId() != null) {
				String widgetId = String.valueOf(panel.getViewId());
				node.setAttribute(UIPanel.WIDGET_ID, widgetId);
			}
			if (panel.getI18nName() != null) {
				String val = String.valueOf(panel.getI18nName());
				node.setAttribute(UIPanel.I18NNAME, val);
			}
			if (panel.getLangDir() != null) {
				String val = String.valueOf(panel.getLangDir());
				node.setAttribute(UIPanel.LANGDIRF, val);
			}
		} else if (layout instanceof UIBorder) {
			UIBorder border = (UIBorder) layout;
			String layoutName = UIConstant.BORDER;
			node = doc.createElement(layoutName);
			if (border.getId() != null)
				node.setAttribute(UIBorder.ID, border.getId());
			if (border.getWidth() != null)
				node.setAttribute("width", border.getWidth());
			if (border.getLeftWidth() != null)
				node.setAttribute("leftWidth", border.getLeftWidth());
			if (border.getRightWidth() != null)
				node.setAttribute("rightWidth", border.getRightWidth());
			if (border.getTopWidth() != null)
				node.setAttribute("topWidth", border.getTopWidth());
			if (border.getBottomWidth() != null)
				node.setAttribute("bottomWidth", border.getBottomWidth());
			if (border.getColor() != null)
				node.setAttribute(UIBorder.COLOR, border.getColor());
			if (border.getLeftColor() != null)
				node.setAttribute(UIBorder.LEFTCOLOR, border.getLeftColor());
			if (border.getRightColor() != null)
				node.setAttribute(UIBorder.RIGHTCOLOR, border.getRightColor());
			if (border.getTopColor() != null)
				node.setAttribute(UIBorder.TOPCOLOR, border.getTopColor());
			if (border.getBottomColor() != null)
				node.setAttribute(UIBorder.BOTTOMCOLOR, border.getBottomColor());
			if (border.getClassName() != null)
				node.setAttribute(UIBorder.CLASSNAME, border.getClassName());
			if (border.getViewId() != null)
				node.setAttribute(UIBorder.WIDGET_ID, border.getViewId());
			Integer showleft = ((UIBorder) layout).getShowLeft();
			node.setAttribute(UIBorder.ISSHOWLEFT, (showleft == null || showleft.intValue() == 0) ? "true" : "false");
			Integer showright = ((UIBorder) layout).getShowRight();
			node.setAttribute(UIBorder.ISSHOWRIGHT, (showright == null || showright.intValue() == 0) ? "true" : "false");
			Integer showtop = ((UIBorder) layout).getShowTop();
			node.setAttribute(UIBorder.ISSHOWTOP, (showtop == null || showtop.intValue() == 0) ? "true" : "false");
			Integer showbottom = ((UIBorder) layout).getShowBottom();
			node.setAttribute(UIBorder.ISSHOWBOTTOM, (showbottom == null || showbottom.intValue() == 0) ? "true" : "false");
		} else if (layout instanceof UITabComp) {
			String layoutName = UIConstant.TAB_LAYOUT;
			String id = ((UITabComp) layout).getId();
			node = doc.createElement(layoutName);
			if (id != null)
				node.setAttribute("id", id);
			String widgetId = ((UITabComp) layout).getViewId();
			if (widgetId != null)
				node.setAttribute("widgetId", widgetId);
			Integer currentIndex = ((UITabComp) layout).getCurrentItem();
			if (currentIndex != null)
				node.setAttribute(UITabComp.CURRENT_ITEM, currentIndex.toString());
			String tabType = ((UITabComp) layout).getTabType();
			if (tabType != null)
				node.setAttribute(UITabComp.TAB_TYPE, tabType);
			Boolean oneTabHide = ((UITabComp) layout).getOneTabHide();
			node.setAttribute("oneTabHide", oneTabHide.toString());
			node.setAttribute("itemWidth", ((UITabComp) layout).getTabItemWidth());
			node.setAttribute("width", ((UITabComp) layout).getTabWidth());
			node.setAttribute("height", ((UITabComp) layout).getTabHeight());
			// 增加TabItems节点
			Element tabItems = doc.createElement(UIConstant.TAB_ITEMS);
			node.appendChild(tabItems);
			// Events
			// AMCUtil.addEvents(doc,
			// (EventConf[])layout.getAttribute(UIElement.CONTROLLER_EVENT),
			// node);
			// AMCUtil.addEvents(doc, layout.getEventConfs(), node);
		} else if (layout instanceof UIShutter) {
			String layoutName = UIConstant.SHUTTER_LAYOUT;
			String id = ((UIShutter) layout).getId();
			node = doc.createElement(layoutName);
			if (id != null)
				node.setAttribute("id", id);
			String widgetId = ((UIShutter) layout).getViewId();
			if (widgetId != null)
				node.setAttribute("widgetId", widgetId);
			String className = ((UIShutter) layout).getClassName();
			if (className != null)
				node.setAttribute(UIShutter.CLASSNAME, className);
			Integer currentItem = ((UIShutter) layout).getCurrentItem();
			if (currentItem != null)
				node.setAttribute("currentItem", currentItem.toString());
			// 增加ShutterItems节点
			Element shutterItems = doc.createElement(UIConstant.SHUTTER_ITEMS);
			node.appendChild(shutterItems);
			// Events
			// AMCUtil.addEvents(doc, layout.getEventConfs(), node);
		} else if (layout instanceof UISplitter) {
			UISplitter uiSplitter = (UISplitter) layout;
			String layoutName = UIConstant.SPLIT_LAYOUT;
			node = doc.createElement(layoutName);
			String id = (uiSplitter).getId();
			if (id != null)
				node.setAttribute("id", id);
			String divideSize = uiSplitter.getDivideSize();
			if (divideSize != null) {
				node.setAttribute("divideSize", divideSize);
			}
			String widgetId = ((UISplitter) layout).getViewId();
			if (widgetId != null)
				node.setAttribute("widgetId", widgetId);
			Integer boundMode = ((UISplitter) layout).getBoundMode();
			if (boundMode != null) {
				node.setAttribute(UISplitter.BOUNDMODE, String.valueOf(boundMode));
			}
			Integer inverse = ((UISplitter) layout).getInverse();
			boolean inverseValue = false;
			if (inverse != null && inverse == 1) {
				inverseValue = true;
			}
			node.setAttribute(UISplitter.ISINVERSE, String.valueOf(inverseValue));
			String hideBar = ((UISplitter) layout).getHideBar();
			if (hideBar != null)
				node.setAttribute(UISplitter.HIDEBAR, String.valueOf(hideBar));
			// String spliterWidth = uiSplitter.getSpliterWidth();
			// if(spliterWidth != null)
			// node.setAttribute(UISplitter.SPLITERWIDTH, spliterWidth);
			//
			// Integer hideBar = uiSplitter.getHideBar();
			// boolean hidebarValue = false;
			// if(hideBar != null && hideBar == 1)
			// hidebarValue = true;
			// node.setAttribute(UISplitter.HIDEBAR,
			// String.valueOf(hidebarValue));
			Integer oneTouch = (Integer) uiSplitter.getOneTouch();
			boolean oneTouchValue = false;
			if (oneTouch != null && oneTouch == 1)
				oneTouchValue = true;
			node.setAttribute(UISplitter.ISONETOUCH, String.valueOf(oneTouchValue));
			Integer ori = uiSplitter.getOrientation();
			if (ori != null) {
				String orientation = "h";
				if (ori.intValue() == 0)
					orientation = "v";
				node.setAttribute("orientation", orientation);
			}
		} else if (layout instanceof UIDivid) {
			UIDivid divid = (UIDivid)layout;
			String layoutName = UIConstant.DIVID;
			node = doc.createElement(layoutName);
			String id = divid.getId();
			if(StringUtils.isNotBlank(id)) {
				node.setAttribute("id",id);
			}
			
			String widgetId = divid.getViewId();
			if(StringUtils.isNotBlank(widgetId)) {
				node.setAttribute("widgetId", widgetId);
			}
			
			String orient = divid.getOrientation();
			if(StringUtils.isNotBlank(orient)) {
				node.setAttribute(UIDivid.ORIENTATION, orient);
			}
			
			Integer inverseValue = divid.getInverse();
			if(inverseValue!=null) {
				if(UIConstant.TRUE.equals(inverseValue)) {
					node.setAttribute(UIDivid.ISINVERSE, "true");
				} else {
					node.setAttribute(UIDivid.ISINVERSE, "false");
				}
			}
			
			Integer isAnimate = divid.getIsAnimate();
			if(isAnimate!=null) {
				if(UIConstant.TRUE.equals(isAnimate)) {
					node.setAttribute(UIDivid.ANIMATE, "true");
				} else {
					node.setAttribute(UIDivid.ANIMATE, "false");
				}
			}
			
			Integer prop = divid.getProp();
			if(prop!=null) {
				node.setAttribute(UIDivid.PROP, prop.toString());
			}
		} else if (layout instanceof UIMenuGroup) {
			String layoutName = UIConstant.MENUGROUP_LAYOUT;
			String id = ((UIMenuGroup) layout).getId();
			node = doc.createElement(layoutName);
			if (id != null)
				node.setAttribute("id", id);
		} else if (layout instanceof UIGridLayout) {
			UIGridLayout gridLayout = (UIGridLayout) layout;
			String layoutName = UIConstant.GRID_LAYOUT;
			node = doc.createElement(layoutName);
			if (gridLayout.getId() != null) {
				node.setAttribute("id", gridLayout.getId());
			}
			if (gridLayout.getViewId() != null) {
				node.setAttribute(UIGridLayout.WIDGET_ID, gridLayout.getViewId());
			}
			if (gridLayout.getBorder() != null) {
				node.setAttribute(UIGridLayout.BORDER, gridLayout.getBorder());
			}
			if (gridLayout.getBorderColor() != null) {
				node.setAttribute(UIGridLayout.BORDERCOLOR, gridLayout.getBorderColor());
			}
			if (gridLayout.getBorderStyle() != null) {
				node.setAttribute(UIGridLayout.BORDERSTYLE, gridLayout.getBorderStyle());
			}
			
			if (gridLayout.getCellBorder() != null) {
				node.setAttribute(UIGridLayout.CELLBORDER, gridLayout.getCellBorder());
			}
			if (gridLayout.getCellBorderColor() != null) {
				node.setAttribute(UIGridLayout.CELLBORDERCOLOR, gridLayout.getCellBorderColor());
			}
			if (gridLayout.getCellBorderStyle() != null) {
				node.setAttribute(UIGridLayout.CELLBORDERSTYLE, gridLayout.getCellBorderStyle());
			}
			// if(gridLayout.getWidth() != null){
			// node.setAttribute(UIGridLayout.WIDTH, gridLayout.getWidth());
			// }
			// if(gridLayout.getHeight() != null){
			// node.setAttribute(UIGridLayout.HEIGHT, gridLayout.getHeight());
			// }
			//
			Integer colCount = gridLayout.getColcount();
			if (colCount != null && colCount.intValue() != -1) {
				node.setAttribute(UIGridLayout.COLCOUNT, colCount.toString());
			}
			Integer rowCount = gridLayout.getRowcount();
			if (rowCount != null && rowCount.intValue() != -1) {
				node.setAttribute(UIGridLayout.ROWCOUNT, rowCount.toString());
			}
		} else if (layout instanceof UIGridRowLayout) {
			UIGridRowLayout gridRow = (UIGridRowLayout) layout;
			String layoutName = UIConstant.GRID_ROW;
			node = doc.createElement(layoutName);
			if (gridRow.getId() != null) {
				node.setAttribute("id", gridRow.getId());
			}
			String widgetId = (String) gridRow.getViewId();
			if (widgetId != null) {
				node.setAttribute(UIGridRowLayout.WIDGET_ID, widgetId);
			}
			if (gridRow.getRowHeight() != null) {
				node.setAttribute(UIGridRowLayout.ROWHEIGHT, gridRow.getRowHeight());
			}
			Integer colCount = gridRow.getColcount();
			if (colCount != null && colCount.intValue() != -1) {
				node.setAttribute(UIGridRowLayout.COLCOUNT, colCount.toString());
			}
		}else if (layout instanceof UIDiv) {
			UIDiv uidiv = (UIDiv) layout;
			String layoutName = UIConstant.UIDIV;
			node = doc.createElement(layoutName);
			if (uidiv.getId()!= null) {
				node.setAttribute("id", uidiv.getId());
			}
			if (uidiv.getStyle() != null) {
				node.setAttribute(UIDiv.STYLE, uidiv.getStyle());
			}
			
		}

		String cssStyle = layout.getCssStyle();
		if (cssStyle != null)
			node.setAttribute(UILayout.CSSSTYLE, cssStyle);
		String className = layout.getClassName();
		if (className != null) {
			node.setAttribute(UILayout.STYLECLASSNAME, className);
		}
		if (node == null)
			return;
		parent.appendChild(node);
		List<UILayoutPanel> list = layout.getPanelList();
		Iterator<UILayoutPanel> it = list.iterator();
		while (it.hasNext()) {
			UILayoutPanel uipanel = it.next();
			if (!(uipanel instanceof UITabRightPanel)) {
				createLayoutPanel(uipanel, doc, node);
			}
		}
		if (layout instanceof UITabComp) {
			UITabComp uiTab = (UITabComp) layout;
			UITabRightPanel rightPanel = uiTab.getRightPanel();
			if (rightPanel != null && rightPanel.getElement() != null)
				createLayoutPanel(rightPanel, doc, node);
		}
		// if(layout instanceof UIGridLayout){
		// UIGridLayout gridLayout = (UIGridLayout) layout;
		// List<UILayoutPanel> rowList = gridLayout.getPanelList();
		// if(rowList != null && rowList.size() > 0){
		// for(int i = 0; i< rowList.size(); i++){
		// UIGridRowPanel rowPanel = (UIGridRowPanel) rowList.get(i);
		// if(rowPanel != null)
		// createLayout(rowPanel.getRow(), doc, node);
		// }
		// }
		// }
	}
	private static void createLayoutPanel(UILayoutPanel panel, Document doc, Element node) {
		String panelName = null;
		Element child = null;
		if (panel instanceof UICardPanel) {
			panelName = UIConstant.CARD_PANEL;
			UICardPanel cardPanel = (UICardPanel) panel;
			child = doc.createElement(panelName);
			child.setAttribute("id", cardPanel.getId());
		} else if (panel instanceof UICanvasPanel) {
			panelName = UIConstant.CANVAS_PANEL;
			UICanvasPanel canvasPanel = (UICanvasPanel) panel;
			child = doc.createElement(panelName);
			child.setAttribute("id", canvasPanel.getId());
		} else if (panel instanceof UIFlowhPanel) {
			panelName = UIConstant.FLOWH_PANEL;
			UIFlowhPanel flowhPanel = (UIFlowhPanel) panel;
			child = doc.createElement(panelName);
			if (flowhPanel.getWidth() != null && !flowhPanel.getWidth().equals(""))
				child.setAttribute("width", flowhPanel.getWidth().toString());
		} else if (panel instanceof UIFlowvPanel) {
			panelName = UIConstant.FLOWV_PANEL;
			UIFlowvPanel flowvPanel = (UIFlowvPanel) panel;
			child = doc.createElement(panelName);
			child.setAttribute("id", (String) flowvPanel.getId());
			if (flowvPanel.getHeight() != null && !flowvPanel.getHeight().equals(""))
				child.setAttribute("height", flowvPanel.getHeight().toString());
			if (flowvPanel.getAnchor() != null && !flowvPanel.getAnchor().equals(""))
				child.setAttribute("anchor", flowvPanel.getAnchor().toString());
		} else if (panel instanceof UIPanelPanel) {
			panelName = UIConstant.PANEL_PANEL;
			child = doc.createElement(panelName);
		} else if (panel instanceof UITabItem) {
			panelName = UIConstant.TAB_ITEM;
			UITabItem item = (UITabItem) panel;
			child = doc.createElement(panelName);
			child.setAttribute("id", item.getId());
			child.setAttribute("text", item.getText());
			child.setAttribute("i18nName", item.getI18nName());
			child.setAttribute("showCloseIcon", item.getShowCloseIcon().equals(UIConstant.TRUE) ? "true" : "false");
			if (item.getActive() != null)
				child.setAttribute("active", item.getActive().toString());
			Integer state = item.getState();
			if (state != null && !(state.intValue() == -1))
				child.setAttribute("state", state.toString());
			NodeList nl = node.getElementsByTagName(UIConstant.TAB_ITEMS);
			if (nl != null && nl.getLength() > 0) {
				node = (Element) nl.item(0);
			}
		} else if (panel instanceof UIShutterItem) {
			panelName = UIConstant.SHUTTER_ITEM;
			UIShutterItem item = (UIShutterItem) panel;
			child = doc.createElement(panelName);
			child.setAttribute("id", item.getId());
			child.setAttribute("text", item.getText());
			child.setAttribute("i18nName", item.getI18nName());
			NodeList nl = node.getElementsByTagName(UIConstant.SHUTTER_ITEMS);
			if (nl != null && nl.getLength() > 0) {
				node = (Element) nl.item(0);
			}
		} else if (panel instanceof UITabRightPanel) {
			panelName = UIConstant.TAB_RIGHT;
			UITabRightPanel item = (UITabRightPanel) panel;
			child = doc.createElement(panelName);
			if (item.getWidth() != null)
				child.setAttribute("width", item.getWidth());
		} else if (panel instanceof UISplitterTwo) {
			panelName = UIConstant.SPLIT_TWO;
			// UISplitterOne one = (UISplitterOne) panel;
			child = doc.createElement(panelName);
		} else if (panel instanceof UISplitterOne) {
			panelName = UIConstant.SPLIT_ONE;
			// UISplitterOne one = (UISplitterOne) panel;
			child = doc.createElement(panelName);
		} else if (panel instanceof UIDividProp) {
			panelName = UIConstant.DIVID_PROP;
			child = doc.createElement(panelName);
		} else if (panel instanceof UIDividCenter) {
			panelName = UIConstant.DIVID_CENTER;
			child = doc.createElement(panelName);
		} else if (panel instanceof UIMenuGroupItem) {
			UIMenuGroupItem groupItem = (UIMenuGroupItem) panel;
			panelName = UIConstant.MENUGROUP_ITEM;
			// UISplitterOne one = (UISplitterOne) panel;
			child = doc.createElement(panelName);
			Integer state = groupItem.getState();
			if (state != null && !(state.intValue() == -1))
				child.setAttribute("state", state.toString());
		} else if (panel instanceof UIGridRowPanel) {
			panelName = UIConstant.GRID_ROW_PANEL;
			child = doc.createElement(panelName);
		} else if (panel instanceof UIBorderTrue) {
			panelName = UIConstant.BORDER_PANEL;
			child = doc.createElement(panelName);
		} else if (panel instanceof UIGridPanel) {
			UIGridPanel gridPanel = (UIGridPanel) panel;
			panelName = UIConstant.GRID_PANEL;
			child = doc.createElement(panelName);
			String colHeight = gridPanel.getColHeight();
			if (colHeight != null) {
				child.setAttribute(UIGridPanel.COLHEIGHT, colHeight);
			}
			String colWidth = gridPanel.getColWidth();
			if (colWidth != null) {
				child.setAttribute(UIGridPanel.COLWIDTH, colWidth);
			}
			String colSpan = gridPanel.getColSpan();
			if (colSpan != null && !colSpan.equals("")) {
				child.setAttribute(UIGridPanel.COLSPAN, colSpan.toString());
			}
			String rowSpan = gridPanel.getRowSpan();
			if (rowSpan != null && !rowSpan.equals("")) {
				child.setAttribute(UIGridPanel.ROWSPAN, rowSpan.toString());
			}
			String rowIndex = gridPanel.getRowIndex();
			if (rowIndex != null && rowIndex.trim().length() > 0) {
				child.setAttribute(UIGridPanel.ROWINDEX, rowIndex);
			}
			String colIndex = gridPanel.getColIndex();
			if (colIndex != null && colIndex.trim().length() > 0) {
				child.setAttribute(UIGridPanel.COLINDEX, colIndex);
			}
		}
		if (panel.getId() != null) {
			child.setAttribute(UIElement.ID, (String) panel.getId());
		}
		String id = String.valueOf(panel.getId());
		child.setAttribute(UIFlowvLayout.ID, id);
		if (panel.getViewId() != null) {
			String widgetId = String.valueOf(panel.getViewId());
			child.setAttribute(UIFlowvLayout.WIDGET_ID, widgetId);
		}
		String cssStyle = panel.getCssStyle();
		if (cssStyle != null)
			child.setAttribute(UILayoutPanel.CSSSTYLE, cssStyle);
		String topPadding = panel.getTopPadding();
		if (topPadding != null)
			child.setAttribute(UILayoutPanel.TOPPADDING, topPadding);
		String bottomPadding = panel.getBottomPadding();
		if (bottomPadding != null)
			child.setAttribute(UILayoutPanel.BOTTOMPADDING, bottomPadding);
		String leftPadding = panel.getLeftPadding();
		if (leftPadding != null)
			child.setAttribute(UILayoutPanel.LEFTPADDING, leftPadding);
		String rightPadding = panel.getRightPadding();
		if (rightPadding != null)
			child.setAttribute(UILayoutPanel.RIGHTPADDING, rightPadding);
		String leftBorder = panel.getLeftBorder();
		if (leftBorder != null)
			child.setAttribute(UILayoutPanel.LEFTBORDER, leftBorder);
		String rightBorder = panel.getRightBorder();
		if (rightBorder != null)
			child.setAttribute(UILayoutPanel.RIGHTBORDER, rightBorder);
		String topBorder = panel.getTopBorder();
		if (topBorder != null)
			child.setAttribute(UILayoutPanel.TOPBORDER, topBorder);
		String bottomBorder = panel.getBottomBorder();
		if (bottomBorder != null)
			child.setAttribute(UILayoutPanel.BOTTOMBORDER, bottomBorder);
		String border = panel.getBorder();
		if (border != null)
			child.setAttribute(UILayoutPanel.BORDER, border);
		String className = panel.getClassName();
		if (className != null) {
			child.setAttribute(UILayoutPanel.STYLECLASSNAME, className);
		}
		node.appendChild(child);
		UIElement ele = panel.getElement();
		if (ele != null)
			createLayoutOrComponent(ele, doc, child);
	}
	private static void createLayoutOrComponent(UIElement ele, Document doc, Element child) {
		if (ele instanceof UIViewPart) {
			createWidget((UIViewPart) ele, doc, child);
		} else if (ele instanceof UILayout)
			createLayout((UILayout) ele, doc, child);
		else if (ele instanceof UIAbsoluteLayout)
			createAbsoluteLayout((UIAbsoluteLayout) ele,doc,child);
		else
			createComponent((UIComponent) ele, doc, child);
	}
	private static void createWidget(UIViewPart ele, Document doc, Element parent) {
		Element widget = null;
		String widgetName = UIConstant.UIViewPart;
		widget = doc.createElement(widgetName);
		widget.setAttribute("id", ele.getId());
		parent.appendChild(widget);
	}
	private static void createComponent(UIComponent ele, Document doc, Element parent) {
		Element comp = null;
		String compName = null;
		if (ele instanceof UIGridComp) {
			compName = UIConstant.UIGRID;
		} else if (ele instanceof UIPropertyGridComp) {
			compName = UIConstant.UIPROPERTYGRID;
		} else if (ele instanceof UITreeComp) {
			compName = UIConstant.UITREE;
		} else if (ele instanceof UIFormComp) {
			compName = UIConstant.UIFORM;
		} else if (ele instanceof UIFormGroupComp) {
			compName = UIConstant.UIFORMGROUP;
		} else if (ele instanceof UIMenubarComp)
			compName = UIConstant.UIMENUBAR;
		else if (ele instanceof UIButton)
			compName = UIConstant.UIBUTTON;
		else if (ele instanceof UIChartComp)
			compName = UIConstant.UICHARTCOMP;
		else if (ele instanceof UISelfDefComp)
			compName = UIConstant.UISELFDEFCOMP;
		else if (ele instanceof UILinkComp)
			compName = UIConstant.UILINKCOMP;
		else if (ele instanceof UIToolBar)
			compName = UIConstant.UITOOLBAR;
		else if (ele instanceof UILabelComp)
			compName = UIConstant.UILABEL;
		else if (ele instanceof UITextField)
			compName = UIConstant.UITEXT;
		else if (ele instanceof UIImageComp)
			compName = UIConstant.UIIMAGE;
		else if (ele instanceof UIIFrame)
			compName = UIConstant.UIIFRAME;
		else if (ele instanceof UIFormElement)
			compName = UIConstant.UIFORMELEMENT;
		else if (ele instanceof UIPartComp)
			compName = UIConstant.UIHTMLCONTENT;
		else if(ele instanceof UILineCharComp){
			compName="Line";
		}else if(ele instanceof UIBarChartComp){
			compName="Bar";
		}else if(ele instanceof UIPieChartComp){
			compName="Pie";
		}
		comp = doc.createElement(compName);
		comp.setAttribute("id", ele.getId());
		if (ele.getAlign() != null)
			comp.setAttribute("align", ele.getAlign());
		if (ele.getHeight() != null)
			comp.setAttribute("height", ele.getHeight());
		if (ele.getWidth() != null)
			comp.setAttribute("width", ele.getWidth());
		if (ele.getLeft() != null)
			comp.setAttribute("left", ele.getLeft().toString());
		if (ele.getTop() != null)
			comp.setAttribute("top", ele.getTop().toString());
		if (ele.getPosition() != null)
			comp.setAttribute("position", ele.getPosition());
		if (ele.getViewId() != null)
			comp.setAttribute("widgetId", ele.getViewId());
		if (ele.getClassName() != null)
			comp.setAttribute("className", ele.getClassName());
		if (ele instanceof UITextField) {
			String type = ((UITextField) ele).getType();
			comp.setAttribute("type", type);
			String imgSrc = ((UITextField) ele).getImgsrc();
			comp.setAttribute(UITextField.IMG_SRC, imgSrc);
			String valign = ((UITextField) ele).getValgin();
			if (valign != null && !valign.isEmpty())
				comp.setAttribute(UITextField.VALGIN, valign);
		} else if (ele instanceof UIFormGroupComp) {
			String forms = ((UIFormGroupComp) ele).getForms();
			comp.setAttribute("formList", forms);
		} else if (ele instanceof UIFormElement) {
			String formId = ((UIFormElement) ele).getFormId();
			comp.setAttribute(UIFormElement.FORM_ID, formId);
			String eleWidth = ((UIFormElement) ele).getEleWidth();
			comp.setAttribute(UIFormElement.ELEWIDTH, eleWidth);
		} else if (ele instanceof UIFormComp) {
			String labelPosition = ((UIFormComp) ele).getLabelPosition();
			comp.setAttribute(UIConstant.LABEL_POSITION, labelPosition);
		} else if (ele instanceof UILabelComp) {
			String textAlign = ((UILabelComp) ele).getTextAlign();
			String cssStyle = ((UILabelComp) ele).getCssStyle();
			String style = ((UILabelComp) ele).getStyle();
			String size = ((UILabelComp) ele).getSize();
			String family = ((UILabelComp) ele).getFamily();
			comp.setAttribute(UILabelComp.TEXTALIGN, textAlign != null ? textAlign : "left");
			comp.setAttribute(UILabelComp.CSSSTYLE, cssStyle != null ? cssStyle : "");
			comp.setAttribute(UILabelComp.STYLE, style != null ? style : "");
			comp.setAttribute(UILabelComp.SIZE, size != null ? size : "12");
			comp.setAttribute(UILabelComp.FAMILY, family != null ? family : "");
		}
		parent.appendChild(comp);
	}
}
