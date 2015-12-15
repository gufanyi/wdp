/**
 * 
 */
package xap.lui.psn.context;

import java.util.HashMap;
import java.util.Map;

import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.constant.EditorTypeConst;
import xap.lui.core.dataset.ComboDataInfo;
import xap.lui.core.dataset.DatasetInfo;
import xap.lui.core.model.LuiPageContext;
import xap.lui.core.refrence.GenericRefNodeInfo;



/**
 * @author wupeng1
 * @version 6.0 2011-8-22
 * @since 1.6
 */
public final class InfoCategory {
	public static final Map<String,  Map<String, BaseInfo> > infoMapCache = new HashMap<String,  Map<String, BaseInfo> >();
	
	public static BaseInfo getInfo(String type){
		
		String langCode = LuiRuntimeContext.getLangCode();
		boolean init = infoMapCache.containsKey(langCode);
		if(!init){
			synchronized (InfoCategory.class) {
				if(!infoMapCache.containsKey(langCode)){
					infoMapCache.put(langCode, getInfoMap());
				}
			}
		}
		Map<String, BaseInfo> infoMap = infoMapCache.get(langCode);
		return infoMap.get(type);
	}
	
	
	public static Map<String, BaseInfo> getInfoMap(){
		Map<String, BaseInfo> infoMap = new HashMap<String, BaseInfo>();
		infoMap.put(LuiPageContext.SOURCE_TYPE_BUTTON, new ButtonInfo());
		infoMap.put(LuiPageContext.SOURCE_TYPE_FORMCOMP, new FormCompInfo());
		infoMap.put(LuiPageContext.SOURCE_TYPE_FORMELE + "_ref", new ReferenceFormElementInfo());
		infoMap.put(LuiPageContext.SOURCE_TYPE_FORMELE, new FormElementInfo());
		infoMap.put(LuiPageContext.SOURCE_TYPE_GRID, new GridCompInfo());
		infoMap.put(LuiPageContext.SOURCE_TYPE_GRID_HEADER, new GridColumnInfo());
		infoMap.put(LuiPageContext.SOURCE_TYPE_TREE, new TreeViewCompInfo());
		infoMap.put(LuiPageContext.SOURCE_TYPE_MENUBAR, new MenubarCompInfo());
		infoMap.put(LuiPageContext.SOURCE_TYPE_CONTEXTMENU, new ContextMenuCompInfo());
		infoMap.put(LuiPageContext.SOURCE_TYPE_MENUBAR_MENUITEM, new MenuItemInfo());
		infoMap.put(LuiPageContext.SOURCE_TYPE_CONTEXTMENU_MENUITEM, new MenuItemInfo());
		infoMap.put(LuiPageContext.SOURCE_TYPE_LABEL, new LabelCompInfo());
		infoMap.put(LuiPageContext.SOURCE_TYPE_LINKCOMP, new LinkCompInfo());
		infoMap.put(LuiPageContext.SOURCE_TYPE_IMAGECOMP, new ImageCompInfo());
		infoMap.put(LuiPageContext.SOURCE_TYPE_SELF_DEF_COMP, new SelfDefCompInfo());
		infoMap.put(LuiPageContext.SOURCE_TYPE_IFRAME, new IFrameCompInfo());
		infoMap.put(LuiPageContext.SOURCE_TYPE_TOOLBAR_BUTTON, new ToolBarCompInfo());
		infoMap.put(LuiPageContext.SOURCE_TYPE_PROGRESS_BAR, new ProgressBarCompInfo());
		infoMap.put(LuiPageContext.SOURCE_TYPE_TEXTAREA, new TextAreaCompInfo());
		infoMap.put(LuiPageContext.SOURCE_TYPE_TEXT, new TextCompInfo());
		infoMap.put(LuiPageContext.SOURCE_TYPE_SELF_DEF_COMP, new SelfDefCompInfo());
		
		
		infoMap.put(EditorTypeConst.STRINGTEXT, new TextCompInfo());
		infoMap.put(EditorTypeConst.DATETEXT, new TextCompInfo());
		
		infoMap.put(EditorTypeConst.CHECKBOX, new CheckBoxCompInfo());
		infoMap.put(EditorTypeConst.CHECKBOXGROUP, new CheckboxGroupCompInfo());
		infoMap.put(EditorTypeConst.COMBODATA, new ComboBoxCompInfo());
		infoMap.put(EditorTypeConst.RADIOCOMP, new RadioCompInfo());
		infoMap.put(EditorTypeConst.RADIOGROUP, new RadioGroupCompInfo());
		infoMap.put(EditorTypeConst.REFERENCE, new ReferenceCompInfo());
		infoMap.put(EditorTypeConst.TEXTAREA, new TextAreaCompInfo());
		infoMap.put(EditorTypeConst.INTEGERTEXT, new IntegerTextCompInfo());
		infoMap.put(EditorTypeConst.DECIMALTEXT, new DecimalTextCompInfo());
		infoMap.put(EditorTypeConst.DATETIMETEXT, new TextCompInfo());
		
		infoMap.put(LuiPageContext.SOURCE_TYPE_HTMLCONTENT, new WebPartCompInfo());
		
		infoMap.put(LuiPageContext.SOURCE_TYPE_FLOWVLAYOUT, new FlowvLayoutInfo());
		infoMap.put(LuiPageContext.SOURCE_TYPE_FLOWHLAYOUT, new FlowhLayoutInfo());
//		infoMap.put(LuiPageContext.SOURCE_TYPE_BORDERLAYOUT, new BorderLayoutInfo());
		infoMap.put(LuiPageContext.SOURCE_TYPE_CARDLAYOUT, new CardLayoutInfo());
		infoMap.put(LuiPageContext.SOURCE_TYPE_GRIDLAYOUT, new GridLayoutInfo());
		
		/**
		 * 绝对布局
		 */
		infoMap.put(LuiPageContext.SOURCE_TYPE_ABSOLUTELAYOUT, new AbsoluteLayoutInfo());
		
		infoMap.put(LuiPageContext.SOURCE_TYPE_GRIDROWPANEL, new GridRowPanelInfo());
		infoMap.put(LuiPageContext.SOURCE_TYPE_GRIDROW, new GridRowLayoutInfo());
		infoMap.put(LuiPageContext.SOURCE_TYPE_SPLITERLAYOUT, new SpliterLayoutInfo());
		infoMap.put(LuiPageContext.SOURCE_TYPE_BORDER, new BorderInfo());
		infoMap.put(LuiPageContext.SOURCE_TYPE_PANELLAYOUT, new PanelLayoutInfo());
		infoMap.put(LuiPageContext.SOURCE_TYPE_MENU_GROUP, new MenuGroupInfo());
		infoMap.put(LuiPageContext.SOURCE_TYPE_TAG, new TabCompInfo());
		infoMap.put(LuiPageContext.SOURCE_TYPE_OUTLOOKBAR, new ShutterInfo());
		infoMap.put(LuiPageContext.SOURCE_TYPE_OUTLOOKBAR_ITEM, new ShutterItemInfo());
		
		infoMap.put(LuiPageContext.SOURCE_TYPE_WIDGT, new WidgetInfo());
		infoMap.put(LuiPageContext.SOURCE_TYPE_UIMETA, new UIMetaInfo());
		
		infoMap.put(LuiPageContext.SOURCE_TYPE_FLOWVPANEL, new FlowvPanelInfo());
		infoMap.put(LuiPageContext.SOURCE_TYPE_FLOWHPANEL, new FlowhPanelInfo());
//		infoMap.put(LuiPageContext.SOURCE_TYPE_BORDERPANEL, new BorderPanelInfo());
		infoMap.put(LuiPageContext.SOURCE_TYPE_CARDPANEL, new CardPanelInfo());
		infoMap.put(LuiPageContext.SOURCE_TYPE_GRIDPANEL, new GridPanelInfo());
		infoMap.put(LuiPageContext.SOURCE_TYPE_SPLITERONEPANEL, new SpliterOnePanelInfo());
		infoMap.put(LuiPageContext.SOURCE_TYPE_SPLITERTWOPANLE, new SpliterTwoPanelInfo());
		infoMap.put(LuiPageContext.SOURCE_TYPE_BORDERTRUE, new BorderTrueInfo());
		infoMap.put(LuiPageContext.SOURCE_TYPE_PANELPANEL, new PanelPanelInfo());
		infoMap.put(LuiPageContext.SOURCE_TYPE_MENU_GROUP_ITEM, new MenuGroupItemInfo());
		infoMap.put(LuiPageContext.SOURCE_TYPE_TABITEM, new TabItemInfo());
		infoMap.put(LuiPageContext.SOURCE_TYPE_TABSPACE, new TabRightPanelPanelInfo());
		infoMap.put(LuiPageContext.SOURCE_TYPE_CANVASLAYOUT, new CanvasInfo());
		infoMap.put(LuiPageContext.SOURCE_TYPE_CANVASPANEL, new CanvasPanelInfo());
		
		infoMap.put(LuiPageContext.SOURCE_TYPE_DATASET, new DatasetInfo());
		infoMap.put(LuiPageContext.SOURCE_TYPE_GENERICREFNODE, new GenericRefNodeInfo());
		infoMap.put(LuiPageContext.SOURCE_TYPE_COMBODATA, new ComboDataInfo());
		infoMap.put(LuiPageContext.SOURCE_TYPE_TOOLBARITEM, new ToolBarItemInfo());
		
		infoMap.put(LuiPageContext.SOURCE_TYPE_PAGEMETA, new PagePartMetaInfo());
		
		return infoMap;
	} 
}
