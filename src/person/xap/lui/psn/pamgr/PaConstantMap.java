package xap.lui.psn.pamgr;

import java.util.HashMap;
import java.util.Map;

import xap.lui.core.constant.EditorTypeConst;
import xap.lui.core.model.LuiPageContext;


/**
 * @author wupeng1
 * @version 6.0 2011-8-17
 * @since 1.6
 */
public class PaConstantMap {

	public static Map<String, Class<?>> mappingTable = new HashMap<String, Class<?>>();
	public static Map<String, String> web2ui = new HashMap<String, String>();
	public static Map<String, String> labelNameMap = new HashMap<String, String>();
	
	static{
//		
//		//����������ӦVO��Map
//		mappingTable.put(LuiPageContext.SOURCE_TYPE_BUTTON, PaButtonCompVO.class);
//		mappingTable.put(LuiPageContext.SOURCE_TYPE_GRID, PaGridCompVO.class);
//		mappingTable.put(LuiPageContext.SOURCE_TYPE_GRID_HEADER, PaGridColumnVO.class);
//		mappingTable.put(LuiPageContext.SOURCE_TYPE_FORMCOMP, PaFormCompVO.class);
//		mappingTable.put(LuiPageContext.SOURCE_TYPE_FORMELE, PaFormElementVO.class);
//		mappingTable.put(LuiPageContext.SOURCE_TYPE_FORMELE + "_ref", PaFormElementVO.class);
//		mappingTable.put(LuiPageContext.SOURCE_TYPE_TREE, PaTreeViewCompVO.class);
//		mappingTable.put(LuiPageContext.SOURCE_TYPE_MENUBAR_MENUITEM, PaMenubarCompVO.class);
//		mappingTable.put(LuiPageContext.SOURCE_TYPE_MENUBAR, PaMenubarCompVO.class);
//		mappingTable.put(LuiPageContext.SOURCE_TYPE_LABEL, PaLabelCompVO.class);
//		mappingTable.put(LuiPageContext.SOURCE_TYPE_LINKCOMP, PaLinkCompVO.class);
//		mappingTable.put(LuiPageContext.SOURCE_TYPE_IMAGECOMP, PaImageCompVO.class);
//		mappingTable.put(LuiPageContext.SOURCE_TYPE_SELF_DEF_COMP, PaSelfDefCompVO.class);
//		mappingTable.put(LuiPageContext.SOURCE_TYPE_IFRAME, PaIFrameCompVO.class);
//		mappingTable.put(LuiPageContext.SOURCE_TYPE_TOOLBAR_BUTTON, PaToolBarCompVO.class);
//		mappingTable.put(LuiPageContext.SOURCE_TYPE_PROGRESS_BAR, PaProgressBarCompVO.class);
//		mappingTable.put(LuiPageContext.SOURCE_TYPE_TEXTAREA, PaTextAreaCompVO.class);
//		mappingTable.put(LuiPageContext.SOURCE_TYPE_TEXT, PaTextCompVO.class);
//		mappingTable.put(LuiPageContext.SOURCE_TYPE_HTMLCONTENT, PaWebPartCompVO.class);
//		
//		mappingTable.put(LuiPageContext.SOURCE_TYPE_UIMETA, PaUIMetaVO.class);
//		
//		mappingTable.put(LuiPageContext.SOURCE_TYPE_FLOWVLAYOUT, PaFlowvLayoutVO.class);
//		mappingTable.put(LuiPageContext.SOURCE_TYPE_FLOWHLAYOUT, PaFlowhLayoutVO.class);
//		mappingTable.put(LuiPageContext.SOURCE_TYPE_PANELLAYOUT, PaPanelVO.class);
////		mappingTable.put(LuiPageContext.SOURCE_TYPE_BORDERLAYOUT, PaBorderLayoutVO.class);
//		mappingTable.put(LuiPageContext.SOURCE_TYPE_CARDLAYOUT, PaCardLayoutVO.class);
//		mappingTable.put(LuiPageContext.SOURCE_TYPE_GRIDLAYOUT, PaGridLayoutVO.class);
//		mappingTable.put(LuiPageContext.SOURCE_TYPE_GRIDROW, PaGridRowLayoutVO.class);
//		mappingTable.put(LuiPageContext.SOURCE_TYPE_SPLITERLAYOUT, PaSplitterVO.class);
//		mappingTable.put(LuiPageContext.SOURCE_TYPE_BORDER, PaBorderVO.class);
//		mappingTable.put(LuiPageContext.SOURCE_TYPE_PANELLAYOUT, PaPanelVO.class);
//		mappingTable.put(LuiPageContext.SOURCE_TYPE_MENU_GROUP, PaMenuGroupVO.class);
//		mappingTable.put(LuiPageContext.SOURCE_TYPE_TAG, PaTabCompVO.class);
//		mappingTable.put(LuiPageContext.SOURCE_TYPE_OUTLOOKBAR, PaShutterVO.class);
//		
//		mappingTable.put(LuiPageContext.SOURCE_TYPE_FLOWVPANEL, PaFlowvLayoutPanelVO.class);
//		mappingTable.put(LuiPageContext.SOURCE_TYPE_FLOWHPANEL, PaFlowhLayoutPanelVO.class);
//		mappingTable.put(LuiPageContext.SOURCE_TYPE_PANELPANEL, PaPanelPanelVO.class);
//		mappingTable.put(LuiPageContext.SOURCE_TYPE_CARDPANEL, PaCardPanelVO.class);
//		mappingTable.put(LuiPageContext.SOURCE_TYPE_GRIDPANEL, PaGridPanelVO.class);
//		mappingTable.put(LuiPageContext.SOURCE_TYPE_SPLITERONEPANEL, PaSplitterOneVO.class);
//		mappingTable.put(LuiPageContext.SOURCE_TYPE_SPLITERTWOPANLE, PaSplitterTwoVO.class);
//		mappingTable.put(LuiPageContext.SOURCE_TYPE_BORDERTRUE, PaBorderTrueVO.class);
//		mappingTable.put(LuiPageContext.SOURCE_TYPE_PANELPANEL, PaPanelPanelVO.class);
//		mappingTable.put(LuiPageContext.SOURCE_TYPE_MENU_GROUP_ITEM, PaMenuGroupItemVO.class);
//		mappingTable.put(LuiPageContext.SOURCE_TYPE_TABITEM, PaTabItemVO.class);
//		mappingTable.put(LuiPageContext.SOURCE_TYPE_TABSPACE, PaTabRightPanelPanelVO.class);
//		mappingTable.put(LuiPageContext.SOURCE_TYPE_OUTLOOKBAR_ITEM, PaShutterItemVO.class);
//		mappingTable.put(LuiPageContext.SOURCE_TYPE_WIDGT, PaWidgetVO.class);
//		
//		mappingTable.put(EditorTypeConst.CHECKBOX, PaCheckBoxCompVO.class);
//		mappingTable.put(EditorTypeConst.CHECKBOXGROUP, PaCheckboxGroupCompVO.class);
//		mappingTable.put(EditorTypeConst.RADIOCOMP, PaRadioCompVO.class);
//		mappingTable.put(EditorTypeConst.RADIOGROUP, PaRadioGroupCompVO.class);
//		mappingTable.put(EditorTypeConst.REFERENCE, PaReferenceCompVO.class);
//		mappingTable.put(EditorTypeConst.COMBODATA, PaComboBoxCompVO.class);
//		mappingTable.put(EditorTypeConst.TEXTAREA, PaTextAreaCompVO.class);
//		
		//Web������ͺ�UI���͵Ķ�Ӧ��
		web2ui.put("UIBorder", LuiPageContext.SOURCE_TYPE_BORDER);
//		web2ui.put("UIBorderLayout", LuiPageContext.SOURCE_TYPE_BORDERLAYOUT);
		web2ui.put("UIPanel", LuiPageContext.SOURCE_TYPE_PANELLAYOUT);
//		web2ui.put("UIBorderPanel", LuiPageContext.SOURCE_TYPE_BORDERPANEL);
		web2ui.put("UIBorderTrue", LuiPageContext.SOURCE_TYPE_BORDERTRUE);
		web2ui.put("UIButton", LuiPageContext.SOURCE_TYPE_BUTTON);
		web2ui.put("UICardLayout", LuiPageContext.SOURCE_TYPE_CARDLAYOUT);
		web2ui.put("UICardPanel", LuiPageContext.SOURCE_TYPE_CARDPANEL);
		web2ui.put("UIGridComp", LuiPageContext.SOURCE_TYPE_GRID);
		web2ui.put("UIGridLayout", LuiPageContext.SOURCE_TYPE_GRIDLAYOUT);
		web2ui.put("UIGridRowLayout", LuiPageContext.SOURCE_TYPE_GRIDROW);
		web2ui.put("UIGridRowPanel", LuiPageContext.SOURCE_TYPE_GRIDROWPANEL);
		web2ui.put("UIGridPanel", LuiPageContext.SOURCE_TYPE_GRIDPANEL);
		web2ui.put("UIExcelComp", LuiPageContext.SOURCE_TYPE_EXCEL);
		web2ui.put("UIWidget", LuiPageContext.SOURCE_TYPE_WIDGT);
		web2ui.put("UIFormComp", LuiPageContext.SOURCE_TYPE_FORMCOMP);
		web2ui.put("UIMenubarComp", LuiPageContext.SOURCE_TYPE_MENUBAR);
		web2ui.put("UIPartComp", LuiPageContext.SOURCE_TYPE_HTMLCONTENT);
		web2ui.put("UISelfDefComp", LuiPageContext.SOURCE_TYPE_SELF_DEF_COMP);
		web2ui.put("UIFlowvLayout", LuiPageContext.SOURCE_TYPE_FLOWVLAYOUT);
		web2ui.put("UIFlowvPanel", LuiPageContext.SOURCE_TYPE_FLOWVPANEL);
		web2ui.put("UIFlowhLayout", LuiPageContext.SOURCE_TYPE_FLOWHLAYOUT);
		web2ui.put("UIFlowhPanel", LuiPageContext.SOURCE_TYPE_FLOWHPANEL);
		web2ui.put("UIPanelPanel", LuiPageContext.SOURCE_TYPE_PANELPANEL);
		web2ui.put("UISplitter", LuiPageContext.SOURCE_TYPE_SPLITERLAYOUT);
		web2ui.put("UISplitterOne", LuiPageContext.SOURCE_TYPE_SPLITERONEPANEL);
		web2ui.put("UISplitterTwo", LuiPageContext.SOURCE_TYPE_SPLITERTWOPANLE);
		web2ui.put("UICanvas", LuiPageContext.SOURCE_TYPE_CANVASLAYOUT);
		web2ui.put("UICanvasPanel", LuiPageContext.SOURCE_TYPE_CANVASPANEL);
		
		web2ui.put("UIMeta", LuiPageContext.SOURCE_TYPE_UIMETA);
		web2ui.put("UITreeComp", LuiPageContext.SOURCE_TYPE_TREE);
//		web2ui.put("UITextField", LuiPageContext.SOURCE_TYPE_TEXT);
		web2ui.put("UIFormElement", LuiPageContext.SOURCE_TYPE_FORMELE);
		web2ui.put("UILabelComp", LuiPageContext.SOURCE_TYPE_LABEL);
		web2ui.put("UIIFrame", LuiPageContext.SOURCE_TYPE_IFRAME);
		web2ui.put("UITabComp", LuiPageContext.SOURCE_TYPE_TAG);
		web2ui.put("UITabItem", LuiPageContext.SOURCE_TYPE_TABITEM);
		web2ui.put("UITabRightPanel", LuiPageContext.SOURCE_TYPE_TABSPACE);
		web2ui.put("UIShutter", LuiPageContext.SOURCE_TYPE_OUTLOOKBAR);
		web2ui.put("UIShutterItem", LuiPageContext.SOURCE_TYPE_OUTLOOKBAR_ITEM);
		web2ui.put("UIImageComp", LuiPageContext.SOURCE_TYPE_IMAGECOMP);
		web2ui.put("UIToolBar", LuiPageContext.SOURCE_TYPE_TOOLBAR_BUTTON);
		web2ui.put("UIChartComp", LuiPageContext.SOURCE_TYPE_CHART);
		web2ui.put("UITextField", LuiPageContext.SOURCE_TYPE_TEXTAREA);
		
		web2ui.put("ButtonComp", LuiPageContext.SOURCE_TYPE_BUTTON);
		web2ui.put("GridComp", LuiPageContext.SOURCE_TYPE_GRID);
		web2ui.put("GridColumn", LuiPageContext.SOURCE_TYPE_GRID_HEADER);
		web2ui.put("FormComp", LuiPageContext.SOURCE_TYPE_FORMCOMP);
		web2ui.put("FormElement", LuiPageContext.SOURCE_TYPE_FORMELE);
		web2ui.put("ExcelComp", LuiPageContext.SOURCE_TYPE_EXCEL);
		web2ui.put("MenubarComp", LuiPageContext.SOURCE_TYPE_MENUBAR);
		web2ui.put("WebPartComp", LuiPageContext.SOURCE_TYPE_HTMLCONTENT);
		web2ui.put("SelfDefComp", LuiPageContext.SOURCE_TYPE_SELF_DEF_COMP);
		web2ui.put("IFrameComp", LuiPageContext.SOURCE_TYPE_IFRAME);
		web2ui.put("LabelComp", LuiPageContext.SOURCE_TYPE_LABEL);
		web2ui.put("LinkComp", LuiPageContext.SOURCE_TYPE_LINKCOMP);
		web2ui.put("UILinkComp", LuiPageContext.SOURCE_TYPE_LINKCOMP);
		web2ui.put("ToolBarComp", LuiPageContext.SOURCE_TYPE_TOOLBAR_BUTTON);
		web2ui.put("ProgressBarComp", LuiPageContext.SOURCE_TYPE_PROGRESS_BAR);
		web2ui.put("ImageComp", LuiPageContext.SOURCE_TYPE_IMAGECOMP);
		web2ui.put("ChartComp", LuiPageContext.SOURCE_TYPE_CHART);
		web2ui.put("TextAreaComp", LuiPageContext.SOURCE_TYPE_TEXTAREA);
		web2ui.put("TreeViewComp", LuiPageContext.SOURCE_TYPE_TREE);
		
		web2ui.put("TextComp", LuiPageContext.SOURCE_TYPE_TEXT);
		web2ui.put("CheckBoxComp", EditorTypeConst.CHECKBOX);
		web2ui.put("CheckboxGroupComp", EditorTypeConst.CHECKBOXGROUP);
		web2ui.put("ComboBoxComp", EditorTypeConst.COMBODATA);
		web2ui.put("RadioGroupComp", EditorTypeConst.RADIOGROUP);
		web2ui.put("RadioComp", EditorTypeConst.RADIOCOMP);
		web2ui.put("ReferenceComp", EditorTypeConst.REFERENCE);
		web2ui.put("TextAreaComp", EditorTypeConst.TEXTAREA);
		
		labelNameMap.put(LuiPageContext.SOURCE_TYPE_BUTTON, "按钮");
		labelNameMap.put(LuiPageContext.SOURCE_TYPE_IMAGECOMP, "图片");
		labelNameMap.put(LuiPageContext.SOURCE_TYPE_LABEL, "标签");
		labelNameMap.put(LuiPageContext.SOURCE_TYPE_MENUBAR,"菜单");
		labelNameMap.put(LuiPageContext.SOURCE_TYPE_LINKCOMP,"超链接");
		labelNameMap.put(LuiPageContext.SOURCE_TYPE_TEXT, "文本");
		labelNameMap.put(LuiPageContext.SOURCE_TYPE_IFRAME,"子窗口");
		labelNameMap.put(LuiPageContext.SOURCE_TYPE_HTMLCONTENT,"WebPart控件");
		
		
	}
	
}
