package xap.lui.psn.pamgr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import xap.lui.core.builder.RaSelfWindow;
import xap.lui.core.cache.PaCache;
import xap.lui.core.common.InteractionUtil;
import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.common.LuiWebContext;
import xap.lui.core.common.LuiWebSession;
import xap.lui.core.comps.BarChartComp;
import xap.lui.core.comps.ButtonComp;
import xap.lui.core.comps.CheckBoxComp;
import xap.lui.core.comps.CheckboxGroupComp;
import xap.lui.core.comps.ComboBoxComp;
import xap.lui.core.comps.ContextMenuComp;
import xap.lui.core.comps.DateTextComp;
import xap.lui.core.comps.DecimalTextComp;
import xap.lui.core.comps.FileComp;
import xap.lui.core.comps.FormComp;
import xap.lui.core.comps.FormElement;
import xap.lui.core.comps.GridColumn;
import xap.lui.core.comps.GridComp;
import xap.lui.core.comps.IFrameComp;
import xap.lui.core.comps.IGridColumn;
import xap.lui.core.comps.ImageComp;
import xap.lui.core.comps.IntegerTextComp;
import xap.lui.core.comps.LabelComp;
import xap.lui.core.comps.LineChartComp;
import xap.lui.core.comps.LinkComp;
import xap.lui.core.comps.MenuItem;
import xap.lui.core.comps.MenubarComp;
import xap.lui.core.comps.PieChartComp;
import xap.lui.core.comps.PwdTextComp;
import xap.lui.core.comps.RadioComp;
import xap.lui.core.comps.RadioGroupComp;
import xap.lui.core.comps.ReferenceComp;
import xap.lui.core.comps.SelfDefComp;
import xap.lui.core.comps.StringTextComp;
import xap.lui.core.comps.TextAreaComp;
import xap.lui.core.comps.ToolBarComp;
import xap.lui.core.comps.ToolBarItem;
import xap.lui.core.comps.TreeViewComp;
import xap.lui.core.comps.WebComp;
import xap.lui.core.comps.WebPartComp;
import xap.lui.core.constant.EditorTypeConst;
import xap.lui.core.constant.ParamConstant;
import xap.lui.core.constant.RenderTypeConst;
import xap.lui.core.constant.StringDataTypeConst;
import xap.lui.core.control.ModePhase;
import xap.lui.core.dataset.ComboData;
import xap.lui.core.dataset.DataItem;
import xap.lui.core.dataset.DataList;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.Field;
import xap.lui.core.dataset.PageData;
import xap.lui.core.dataset.RefMdDataset;
import xap.lui.core.dataset.Row;
import xap.lui.core.event.DatasetEvent;
import xap.lui.core.event.MouseEvent;
import xap.lui.core.exception.ComboInputItem;
import xap.lui.core.exception.InputItem;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.exception.StringInputItem;
import xap.lui.core.layout.UIPartMeta;
import xap.lui.core.listener.DatasetRule;
import xap.lui.core.listener.EventSubmitRule;
import xap.lui.core.listener.LuiEventConf;
import xap.lui.core.listener.WidgetRule;
import xap.lui.core.model.AppContext;
import xap.lui.core.model.AppSession;
import xap.lui.core.model.CtrlState;
import xap.lui.core.model.DataModels;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.model.LuiAppUtil;
import xap.lui.core.model.LuiMeta;
import xap.lui.core.model.LuiPageContext;
import xap.lui.core.model.PagePartMeta;
import xap.lui.core.model.PoolObjectManager;
import xap.lui.core.model.RequestLifeCycleContext;
import xap.lui.core.model.UIState;
import xap.lui.core.model.ViewPartConfig;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.core.model.ViewState;
import xap.lui.core.plugins.ILuiPaltformExtProvier;
import xap.lui.core.plugins.LuiPaltformContranier;
import xap.lui.core.render.RaParameter;
import xap.lui.core.services.IPaEditorService;
import xap.lui.core.services.PaEditorServiceImpl;
import xap.lui.core.util.LuiClassUtil;
public class PaPalletDsListener {
	public static final String DEFINED = " ";

	/**
	 * 加载表单设计器右侧树的数据
	 */
	public void onDataLoad(DatasetEvent e) {
		Dataset ds = e.getSource();
		LuiWebSession session = LuiRuntimeContext.getWebContext().getPageWebSession();
		PagePartMeta pagemeta = null;
		UIPartMeta uimeta = null;
		{
			pagemeta = PaCache.getEditorPagePartMeta();
			uimeta = PaCache.getEditorUIPartMeta();
		}
		if (ds.getId().equals("layoutds")) {
			fillLayoutDs(ds);
		}
		if (ds.getId().equals("ctrlds")) {
			String viewId = session.getOriginalParameter("viewId");
			if(StringUtils.isNotBlank(viewId)){
				fillCtrlDs(ds, pagemeta, viewId);
				ds.setSelectedIndex(0);
			}
			
		}
		if (ds.getId().equals("currds")) {
			fillCurrDs(ds, pagemeta, uimeta);
		}
	}
	
	public static void fillCurrDs(Dataset ds, PagePartMeta pagemeta, UIPartMeta um) {
		ds.clear();
		if (pagemeta == null) {
			return;
		}
		Row row = ds.getEmptyRow();
	//	row.setValue(ds.nameToIndex("id"), DEFINED);
		row.setValue(ds.nameToIndex("id"), pagemeta.getId());
		row.setValue(ds.nameToIndex("name"), pagemeta.getId()+"(已定义的View)");
		row.setValue(ds.nameToIndex("type"), LuiPageContext.SOURCE_TYPE_PAGEMETA);
		ds.addRow(row);
		
		ViewPartMeta[] widgets = pagemeta.getWidgets();
		if (widgets != null && widgets.length > 0) {
			for (int i = 0; i < widgets.length; i++) {
				row = ds.getEmptyRow();
				ViewPartMeta widget = widgets[i];
				String widgetId = widget.getId();
				row.setValue(ds.nameToIndex("id"), widgetId);
				row.setValue(ds.nameToIndex("name"), "View");
				row.setValue(ds.nameToIndex("type"), LuiPageContext.SOURCE_TYPE_WIDGT);
			//	row.setValue(ds.nameToIndex("pid"), DEFINED);
				row.setValue(ds.nameToIndex("pid"), pagemeta.getId());
//				if (um != null && UIElementFinder.findElementById(um, widgetId) != null) {
//					continue;
//				}               
				ds.addRow(row);
			}
		}
	}

	public static void fillCtrlDs(Dataset ds, PagePartMeta pagemeta, String viewId) {
		// fillUndefCtrlDs(ds);
		fillDefCtrlDs(ds, pagemeta, viewId);
	}

	public static void fillDefCtrlDs(Dataset ds, PagePartMeta pagemeta, String viewId) {
		ds.clear();
		Row row = ds.getEmptyRow();
		row.setValue(ds.nameToIndex("id"), DEFINED);
		row.setValue(ds.nameToIndex("name"), "已定义控件");
		ds.addRow(row);
		if (pagemeta == null) {
			return;
		}
		if (viewId != null) {
			ViewPartMeta widget = pagemeta.getWidget(viewId);
			if (widget != null) {
				List<String> ctrlTypes = getCtrlTypes();
				if (widget.getViewMenus() != null) {
					addMenuToRow(ds, widget.getViewMenus().getMenuBars());
					if (widget.getViewMenus().getContextMenus() != null) {
						addContextMenuToRow(ds, widget.getViewMenus().getContextMenus());
					}
				}
				addCompToRow(ds, ctrlTypes, widget);
				List<String> bindtypes = getBindTypes();
				addCompToRow(ds, bindtypes, widget);
			}
		} else {
			ViewPartMeta[] widgets = pagemeta.getWidgets();
			if (widgets != null && widgets.length > 0) {
				for (ViewPartMeta widget : widgets) {
					List<String> ctrlTypes = getCtrlTypes();
					if (widget.getViewMenus() != null) {
						addMenuToRow(ds, widget.getViewMenus().getMenuBars());
						if (widget.getViewMenus().getContextMenus() != null) {
							addContextMenuToRow(ds, widget.getViewMenus().getContextMenus());
						}
					}
					addCompToRow(ds, ctrlTypes, widget);
					List<String> bindtypes = getBindTypes();
					addCompToRow(ds, bindtypes, widget);
				}
			}
		}
	}

	private static void addContextMenuToRow(Dataset ds, ContextMenuComp[] contextMenus) {
		if (contextMenus != null) {
			for (int i = 0; i < contextMenus.length; i++) {
				Row row = ds.getEmptyRow();
				ContextMenuComp menu = contextMenus[i];
				String	id=menu.getId();
				row.setValue(ds.nameToIndex("id"), id);
				row.setValue(ds.nameToIndex("name"), "右键菜单");
				row.setValue(ds.nameToIndex("type"), LuiPageContext.SOURCE_TYPE_CONTEXTMENU);
				row.setValue(ds.nameToIndex("pid"), DEFINED);
				row.setValue(ds.nameToIndex("imgtype"), menu.getClass().getName());
				ds.addRow(row);
				List<MenuItem> menuItems=menu.getItemList();
				if(menuItems.size()>0){
					AddChildMenuItemToRow(ds, menuItems,id);
				}
			}
		}
	}
	//由于传过来的每一个 menuItems 里边可能还包括menuItems 所以要（递归）遍历
	private static void AddChildMenuItemToRow(Dataset ds,List<MenuItem> menuItems,String pid){
		for(MenuItem menuItem:menuItems){
			Row row = ds.getEmptyRow();
			String id=menuItem.getId();
			row.setValue(ds.nameToIndex("id"), id);
			row.setValue(ds.nameToIndex("name"), menuItem.getText());
			row.setValue(ds.nameToIndex("type"), LuiPageContext.SOURCE_TYPE_CONTEXTMENU_MENUITEM);
			row.setValue(ds.nameToIndex("pid"), pid);
			row.setValue(ds.nameToIndex("imgtype"), LuiPageContext.SOURCE_TYPE_CONTEXTMENU_MENUITEM);
			ds.addRow(row);
			List<MenuItem> childMenuItems=menuItem.getChildList();
			if(childMenuItems.size()>0){
				AddChildMenuItemToRow(ds, childMenuItems,id);
			}
			 
		}
		
		
	}
	

	private static void addMenuToRow(Dataset ds, MenubarComp[] menus) {
		if (menus != null) {
			for (int i = 0; i < menus.length; i++) {
				Row row = ds.getEmptyRow();
				MenubarComp menu = menus[i];
				row.setValue(ds.nameToIndex("id"), menu.getId());
				row.setValue(ds.nameToIndex("name"), "菜单");
				row.setValue(ds.nameToIndex("type"), LuiPageContext.SOURCE_TYPE_MENUBAR);
				row.setValue(ds.nameToIndex("pid"), DEFINED);
				row.setValue(ds.nameToIndex("imgtype"), LuiPageContext.SOURCE_TYPE_MENUBAR);
				ds.addRow(row);
			}
		}
	}

	public static void fillLayoutDs(Dataset ds) {
		ds.clear();
		Row row = ds.getEmptyRow();
		ModePhase modePhase = LuiRuntimeContext.getModePhase();
		{
			row = ds.getEmptyRow();
			row.setValue(ds.nameToIndex("id"), "lui_layout");
			row.setValue(ds.nameToIndex("name"), "布局容器");
			row.setValue(ds.nameToIndex("type"), "lui_layout");
			// ds.addRow(row);
		}
		row = ds.getEmptyRow();
		row.setValue(ds.nameToIndex("id"), LuiPageContext.SOURCE_TYPE_FLOWHLAYOUT + "_" + LuiPageContext.UNKNOWN_LAYOUT_ID);
		row.setValue(ds.nameToIndex("name"), "横向容器");
		row.setValue(ds.nameToIndex("type"), LuiPageContext.SOURCE_TYPE_FLOWHLAYOUT);
		row.setValue(ds.nameToIndex("pid"), "lui_layout");
		ds.addRow(row);
		row = ds.getEmptyRow();
		row.setValue(ds.nameToIndex("id"), LuiPageContext.SOURCE_TYPE_FLOWVLAYOUT + "_" + LuiPageContext.UNKNOWN_LAYOUT_ID);
		row.setValue(ds.nameToIndex("name"), "纵向容器");
		row.setValue(ds.nameToIndex("type"), LuiPageContext.SOURCE_TYPE_FLOWVLAYOUT);
		row.setValue(ds.nameToIndex("pid"), "lui_layout");
		ds.addRow(row);
		row = ds.getEmptyRow();
		row.setValue(ds.nameToIndex("id"), LuiPageContext.SOURCE_TYPE_TAG + "_" + LuiPageContext.UNKNOWN_LAYOUT_ID);
		row.setValue(ds.nameToIndex("name"), "页签容器");
		row.setValue(ds.nameToIndex("type"), LuiPageContext.SOURCE_TYPE_TAG);
		row.setValue(ds.nameToIndex("pid"), "lui_layout");
		ds.addRow(row);
		row = ds.getEmptyRow();
		row.setValue(ds.nameToIndex("id"), LuiPageContext.SOURCE_TYPE_PANELLAYOUT + "_" + LuiPageContext.UNKNOWN_LAYOUT_ID);
		row.setValue(ds.nameToIndex("name"), "标题面板");
		row.setValue(ds.nameToIndex("type"), LuiPageContext.SOURCE_TYPE_PANELLAYOUT);
		ds.addRow(row);
		row = ds.getEmptyRow();
		row.setValue(ds.nameToIndex("id"), LuiPageContext.SOURCE_TYPE_SPLITERLAYOUT + "_" + LuiPageContext.UNKNOWN_LAYOUT_ID);
		row.setValue(ds.nameToIndex("name"), "分隔容器");
		row.setValue(ds.nameToIndex("type"), LuiPageContext.SOURCE_TYPE_SPLITERLAYOUT);
		if (modePhase != null && (modePhase.equals(ModePhase.eclipse)))
			ds.addRow(row);
		row = ds.getEmptyRow();
		row.setValue(ds.nameToIndex("id"), LuiPageContext.SOURCE_TYPE_OUTLOOKBAR + "_" + LuiPageContext.UNKNOWN_LAYOUT_ID);
		row.setValue(ds.nameToIndex("name"), "百叶窗容器");
		row.setValue(ds.nameToIndex("type"), LuiPageContext.SOURCE_TYPE_OUTLOOKBAR);
		if (modePhase != null && (modePhase.equals(ModePhase.eclipse)))
			ds.addRow(row);
		row = ds.getEmptyRow();
		row.setValue(ds.nameToIndex("id"), LuiPageContext.SOURCE_TYPE_CARDLAYOUT + "_" + LuiPageContext.UNKNOWN_LAYOUT_ID);
		row.setValue(ds.nameToIndex("name"), "卡片容器");
		row.setValue(ds.nameToIndex("type"), LuiPageContext.SOURCE_TYPE_CARDLAYOUT);
		if (modePhase != null && (modePhase.equals(ModePhase.eclipse)))
			ds.addRow(row);
		row = ds.getEmptyRow();
		row.setValue(ds.nameToIndex("id"), LuiPageContext.SOURCE_TYPE_CANVASLAYOUT + "_" + LuiPageContext.UNKNOWN_LAYOUT_ID);
		row.setValue(ds.nameToIndex("name"), "背景容器");
		row.setValue(ds.nameToIndex("type"), LuiPageContext.SOURCE_TYPE_CANVASLAYOUT);
		ds.addRow(row);
		row = ds.getEmptyRow();
		row.setValue(ds.nameToIndex("id"), LuiPageContext.SOURCE_TYPE_GRIDLAYOUT + "_" + LuiPageContext.UNKNOWN_LAYOUT_ID);
		row.setValue(ds.nameToIndex("name"), "网格容器");
		row.setValue(ds.nameToIndex("type"), LuiPageContext.SOURCE_TYPE_GRIDLAYOUT);
		ds.addRow(row);
		/**
		 * 增加绝对布局
		 */
		row = ds.getEmptyRow();
		row.setValue(ds.nameToIndex("id"), LuiPageContext.SOURCE_TYPE_ABSOLUTELAYOUT + "_" + LuiPageContext.UNKNOWN_LAYOUT_ID);
		row.setValue(ds.nameToIndex("name"), "绝对布局容器");
		row.setValue(ds.nameToIndex("type"), LuiPageContext.SOURCE_TYPE_ABSOLUTELAYOUT);
		ds.addRow(row);
	}

	public static void addCompToRow(Dataset ds, List<String> bindtypes, ViewPartMeta widget) {
		Row row = null;
		Set<WebComp> list = widget.getViewComponents().getComponents();
		Iterator<WebComp> it = list.iterator();
		Map<String,Object[]> ctrlTreeMap = getAttrForTreeNodeMap();
		while (it.hasNext()) {
			row = ds.getEmptyRow();
			WebComp inner=it.next();
			String key = (String) inner.getId();
			String type = inner.getClass().getSimpleName();
			// 过滤调临时存储的form
//			if (key.contains("_newFrm") && type.equals("FormComp")) {
//			}
			if (bindtypes.contains(type) && !(key.contains("_newFrm") && type.equals("FormComp"))) {
				Object[] props = ctrlTreeMap.get(type);
				row.setValue(ds.nameToIndex("id"), key);
				row.setValue(ds.nameToIndex("pid"), DEFINED);
				row.setValue(ds.nameToIndex("name"), (String) props[1]);
				row.setValue(ds.nameToIndex("type"), (String) props[2]);
				row.setValue(ds.nameToIndex("type2"), (String) props[3]);
				row.setValue(ds.nameToIndex("imgtype"), (String) props[4]);
				ds.addRow(row);
				if(StringUtils.equals(type, "FormComp")) {
					genFormElement2CtrlTree((FormComp)inner,ds);
				}
			}
		}
	}
	
	private static void genFormElement2CtrlTree(FormComp form,Dataset ds) {
		List<FormElement> elementList = form.getElementList();
		if(elementList != null) {
			for(FormElement formElement : elementList) {
				if(formElement.isVisible()) {
					Row formelementRow = ds.getEmptyRow();
					formelementRow.setValue(ds.nameToIndex("id"), formElement.getId());
					formelementRow.setValue(ds.nameToIndex("pid"), form.getId());
					formelementRow.setValue(ds.nameToIndex("name"), formElement.getText());
					formelementRow.setValue(ds.nameToIndex("type"), LuiPageContext.SOURCE_TYPE_FORMELE);
					formelementRow.setValue(ds.nameToIndex("type2"), null);
					formelementRow.setValue(ds.nameToIndex("imgtype"), LuiPageContext.SOURCE_TYPE_FORMELE);
					ds.addRow(formelementRow);
				}
			}
		}
	}

	private static List<String> getBindTypes() {
		String[] bindTypes = { "GridComp", "TreeViewComp", "FormComp", "ExcelComp", "ChartComp" };
		List<String> bindtypes = new ArrayList<String>();
		for (int i = 0; i < bindTypes.length; i++) {
			bindtypes.add(bindTypes[i]);
		}
		return bindtypes;
	}

	public static List<String> getCtrlTypes() {
		String[] ctrlTypes = { "ButtonComp", "ImageComp", "TextComp", "LabelComp", "MenubarComp", "SelfDefComp", "IFrameComp", "ProgressBarComp", "ReferenceComp", "TextAreaComp", "RadioGroupComp",
				"CheckboxGroupComp", "LineChartComp","PieChartComp","BarChartComp", "RadioComp", "ComboBoxComp", "CheckBoxComp", "LinkComp", "ToolBarComp", "ChartComp", "StringTextComp", "IntegerTextComp", "DecimalTextComp", "PwdTextComp", "DateTextComp", "FileComp" };
		List<String> ctrltypes = new ArrayList<String>();
		for (int i = 0; i < ctrlTypes.length; i++) {
			ctrltypes.add(ctrlTypes[i]);
		}
		return ctrltypes;
	}

	/**
	 * 控件树右键菜单菜单项 key:右键菜单项ID value:
	 * 右键菜单图片名称,菜单显示文字,树节点type,树节点type2,树节点imgtype,控件类名,右键菜单项父ID
	 * 
	 * @return
	 */
	public static Map<String, Object[]> getAttrForTreeNodeMap() {
		Map<String, Object[]> map = new LinkedHashMap<String, Object[]>();
		map.put("GridComp", new Object[] { "UIGrid", "表格", LuiPageContext.SOURCE_TYPE_GRID, null, LuiPageContext.SOURCE_TYPE_GRID, GridComp.class, "", ""});
		map.put("FormComp", new Object[] { "UIFormcomp", "表单", LuiPageContext.SOURCE_TYPE_FORMCOMP, null, LuiPageContext.SOURCE_TYPE_FORMCOMP, FormComp.class, "", ""});
		map.put("TreeViewComp", new Object[] { "UIUndefinedComp", "树控件", LuiPageContext.SOURCE_TYPE_TREE, null, LuiPageContext.SOURCE_TYPE_TREE, TreeViewComp.class, "", ""});
		map.put("ToolBarComp", new Object[] { "UIMenubarComp", "工具栏", LuiPageContext.SOURCE_TYPE_TOOLBAR_BUTTON, LuiPageContext.SOURCE_TYPE_TOOLBAR_BUTTON, LuiPageContext.SOURCE_TYPE_TOOLBAR_BUTTON, ToolBarComp.class,
		"", ""});
		map.put("ContextMenuComp", new Object[] { "UIMenubarComp", "右键菜单", LuiPageContext.SOURCE_TYPE_CONTEXTMENU, LuiPageContext.SOURCE_TYPE_CONTEXTMENU, LuiPageContext.SOURCE_TYPE_CONTEXTMENU_MENUITEM, ContextMenuComp.class,
		"", ""});
		map.put("MenubarComp", new Object[] { "UIMenubarComp", "菜单", LuiPageContext.SOURCE_TYPE_MENUBAR, LuiPageContext.SOURCE_TYPE_MENUBAR, LuiPageContext.SOURCE_TYPE_MENUBAR, MenubarComp.class,
		"", ""});
		map.put("ButtonComp", new Object[] { "UIButton", "按钮", LuiPageContext.SOURCE_TYPE_BUTTON, LuiPageContext.SOURCE_TYPE_BUTTON, LuiPageContext.SOURCE_TYPE_BUTTON, ButtonComp.class, "", ""});
		map.put("LabelComp", new Object[] { "UILabelComp", "标签", LuiPageContext.SOURCE_TYPE_LABEL, LuiPageContext.SOURCE_TYPE_LABEL, LuiPageContext.SOURCE_TYPE_LABEL, LabelComp.class, "", ""});
		map.put("text_p", new Object[] { "UIStringText", "文本输入控件", null, null, null, null, "", ""});
		map.put("StringTextComp", new Object[] { "UIStringText", "字符输入控件", LuiPageContext.SOURCE_TYPE_TEXT, EditorTypeConst.STRINGTEXT, EditorTypeConst.STRINGTEXT, StringTextComp.class,
				"text_p", ""});
		map.put("ReferenceComp", new Object[] { "UIReference", "参照输入控件", LuiPageContext.SOURCE_TYPE_TEXT, EditorTypeConst.REFERENCE, EditorTypeConst.REFERENCE, ReferenceComp.class, "text_p" , ""});
		map.put("ComboBoxComp", new Object[] { "UICombox", "下拉输入控件", LuiPageContext.SOURCE_TYPE_TEXT, EditorTypeConst.COMBODATA, EditorTypeConst.COMBODATA, ComboBoxComp.class, "text_p" , ""});
		map.put("CheckBoxComp", new Object[] { "UICheckboxGroup", "复选框", LuiPageContext.SOURCE_TYPE_TEXT, EditorTypeConst.CHECKBOX, EditorTypeConst.CHECKBOX, CheckBoxComp.class, "text_p" , ""});
		map.put("CheckboxGroupComp", new Object[] { "UICheckboxGroup", "复选框组", LuiPageContext.SOURCE_TYPE_TEXT, EditorTypeConst.CHECKBOXGROUP, EditorTypeConst.CHECKBOXGROUP, CheckboxGroupComp.class, "text_p" , ""});
		map.put("RadioComp", new Object[] { "UIRadioGroup", "单选按钮", LuiPageContext.SOURCE_TYPE_TEXT, EditorTypeConst.RADIOCOMP, EditorTypeConst.RADIOCOMP, RadioComp.class, "text_p" , ""});
		map.put("RadioGroupComp", new Object[] { "UIRadioGroup", "单选按钮组", LuiPageContext.SOURCE_TYPE_TEXT, EditorTypeConst.RADIOGROUP, EditorTypeConst.RADIOGROUP, RadioGroupComp.class, "text_p" , ""});
		map.put("DateTextComp", new Object[] { "UIDateText", "日期输入控件", LuiPageContext.SOURCE_TYPE_TEXT, EditorTypeConst.DATETEXT, EditorTypeConst.DATETEXT, DateTextComp.class, "text_p" , ""});
		map.put("DecimalTextComp", new Object[] { "UIDecimalText", "浮点输入控件", LuiPageContext.SOURCE_TYPE_TEXT, EditorTypeConst.DECIMALTEXT, EditorTypeConst.DECIMALTEXT, DecimalTextComp.class,
				"text_p" , ""});
		map.put("IntegerTextComp", new Object[] { "UIIntegerText", "整型输入控件", LuiPageContext.SOURCE_TYPE_TEXT, EditorTypeConst.INTEGERTEXT, EditorTypeConst.INTEGERTEXT, IntegerTextComp.class,
				"text_p" , ""});
		map.put("PwdTextComp", new Object[] { "UIPwdText", "密码输入控件", LuiPageContext.SOURCE_TYPE_TEXT, EditorTypeConst.PWDTEXT, EditorTypeConst.PWDTEXT, PwdTextComp.class,
		"text_p" , ""});
		map.put("TextAreaComp", new Object[] { "UITextArea", "文本域", LuiPageContext.SOURCE_TYPE_TEXT, EditorTypeConst.TEXTAREA, EditorTypeConst.TEXTAREA, TextAreaComp.class, "text_p" , ""});
		map.put("FileComp", new Object[] { "UIFileComp", "文件上传", LuiPageContext.SOURCE_TYPE_TEXT, EditorTypeConst.FILECOMP, EditorTypeConst.FILECOMP, FileComp.class, "text_p" , ""});
		map.put("ImageComp", new Object[] { "UIImageComp", "图片", LuiPageContext.SOURCE_TYPE_IMAGECOMP, LuiPageContext.SOURCE_TYPE_IMAGECOMP, LuiPageContext.SOURCE_TYPE_IMAGECOMP, ImageComp.class,
				"" , ""});
		map.put("LinkComp", new Object[] { "UILinkComp", "超链接", LuiPageContext.SOURCE_TYPE_LINKCOMP, LuiPageContext.SOURCE_TYPE_LINKCOMP, LuiPageContext.SOURCE_TYPE_LINKCOMP, LinkComp.class, "" , ""});
		map.put("IFrameComp", new Object[] { "UIChildWin", "子窗口控件", LuiPageContext.SOURCE_TYPE_IFRAME, LuiPageContext.SOURCE_TYPE_IFRAME, LuiPageContext.SOURCE_TYPE_IFRAME, IFrameComp.class, "" , ""});
		map.put("WebPartComp", new Object[] { "UIHtmlcontent", "WebPart控件", LuiPageContext.SOURCE_TYPE_HTMLCONTENT, LuiPageContext.SOURCE_TYPE_HTMLCONTENT, LuiPageContext.SOURCE_TYPE_HTMLCONTENT,
				WebPartComp.class, "" , ""});
		map.put("SelfDefComp", new Object[] { "UISelfDefComp", "自定义控件", LuiPageContext.SOURCE_TYPE_SELF_DEF_COMP, LuiPageContext.SOURCE_TYPE_SELF_DEF_COMP, LuiPageContext.SOURCE_TYPE_SELF_DEF_COMP,
				SelfDefComp.class, "" , ""});
		
		map.put("LineChartComp", new Object[] { "UILineCharComp", "折线图", LuiPageContext.SOURCE_TYPE_LINE, LuiPageContext.SOURCE_TYPE_LINE, LuiPageContext.SOURCE_TYPE_LINE, LineChartComp.class, "" , ""});
		map.put("BarChartComp", new Object[] { "UIBarChartComp", "柱状图", LuiPageContext.SOURCE_TYPE_BAR, LuiPageContext.SOURCE_TYPE_BAR, LuiPageContext.SOURCE_TYPE_BAR, BarChartComp.class, "" , ""});
		map.put("PieChartComp", new Object[] { "UIPieChartComp", "饼状图", LuiPageContext.SOURCE_TYPE_PIE, LuiPageContext.SOURCE_TYPE_PIE, LuiPageContext.SOURCE_TYPE_PIE, PieChartComp.class, "" , ""});

		// 动态
		ILuiPaltformExtProvier[] providers = LuiPaltformContranier.getInstance().getProvideres();
		if(providers != null && providers.length > 0){
			map.put("extend_p", new Object[] { "extend_p", "扩展控件", null, null, null, null, "", ""});
			for (int i = 0; i < providers.length; i++) {
				ILuiPaltformExtProvier inner = providers[i];
				String text = inner.getText();
				String key = inner.getWebCompClazz().getSimpleName();
				String ui = inner.getUICompClazz().getSimpleName();
				String srcType = inner.getSourceType();
				String imgIcon = inner.getImgIcon();
				map.put(key, new Object[] {ui, text, srcType, srcType, srcType, inner.getWebCompClazz(), "extend_p", imgIcon});
			}
		}
		return map;
	}

	/**
	 * 构造控件树右键菜单
	 * 
	 * @param conetxtMenu
	 * @param menuItem
	 * @param map
	 * @param pid
	 */
	private void genCtrlTreeContenxtMenu(ContextMenuComp conetxtMenu, MenuItem menuItem, Map<String, Object[]> map, String pid) {
		for (String key : map.keySet()) {
			Object[] treeNodeMap = map.get(key);
			String _id = (String) key;
			String _pid = (String) treeNodeMap[6];
			if (_pid.equals(pid)) {
				MenuItem _menuItem = new MenuItem();
				_menuItem.setId(key);
				_menuItem.setText("新建" + (String) treeNodeMap[1]);
				String imgIcon = LuiRuntimeContext.getWebContext().getPageMeta().getThemePath() + "/comps/tree/images/icon/" + (String) treeNodeMap[0] + ".png";
				_menuItem.setImgIcon(imgIcon);
				genCtrlContextMenuEvent(_menuItem, treeNodeMap[2] != null);
				genCtrlTreeContenxtMenu(conetxtMenu, _menuItem, map, _id);
				if (StringUtils.isEmpty(_pid)) {
					conetxtMenu.addMenuItem(_menuItem); 
				} else {
					menuItem.addMenuItem(_menuItem);
				}
			}
		}
	}

	/**
	 * 控件树数据集选中事件
	 * 
	 * @param e
	 */
	public void onAfterRowSelect(DatasetEvent e) {
		LuiAppUtil.getCntAppCtx().addBeforeExecScript("pageUI.getViewPart('settings').getDataset('ds_middle').silent = true");
		LuiAppUtil.getCntAppCtx().addAfterExecScript("pageUI.getViewPart('settings').getDataset('ds_middle').silent = false");
		Dataset ds = e.getSource();
		ContextMenuComp conetxtMenu = (ContextMenuComp) LuiAppUtil.getContextMenu("ctrlTreeContextMenu");
		conetxtMenu.removeChildrenItem();
		Row row = ds.getSelectedRow();
		String treeId = row.getString(ds.nameToIndex("id"));
		if (DEFINED.equals(treeId)) {
			Map<String, Object[]> map = getAttrForTreeNodeMap();
			genCtrlTreeContenxtMenu(conetxtMenu, null, map, "");
		} else {
			String compType = row.getString(ds.nameToIndex("type"));
			if(StringUtils.equals(compType, LuiPageContext.SOURCE_TYPE_FORMCOMP)) {
				MenuItem menuItem = new MenuItem();
				menuItem.setId("editFormComp");
				menuItem.setText("编辑");
				genCtrlContextMenuEvent(menuItem, true);
				conetxtMenu.addMenuItem(menuItem);
				MenuItem menuItem2 = new MenuItem();
				menuItem2.setId("deleteCtrlItem");
				menuItem2.setText("删除");
				genCtrlContextMenuEvent(menuItem2, true);
				conetxtMenu.addMenuItem(menuItem2);
			}else if(StringUtils.equals(compType, LuiPageContext.SOURCE_TYPE_CONTEXTMENU)){//不出现拖动控件的框框
				LuiAppUtil.getCntAppCtx().addBeforeExecScript("document.getElementById('iframe_tmp').contentWindow.dropEventHandler({type:'release'});");
				MenuItem menuItem = new MenuItem();
				menuItem.setId("deleteCtrlItem");
				menuItem.setText("删除");
				genCtrlContextMenuEvent(menuItem, true);
				conetxtMenu.addMenuItem(menuItem);
			}else if(StringUtils.equals(compType, LuiPageContext.SOURCE_TYPE_CONTEXTMENU_MENUITEM)){
				LuiAppUtil.getCntAppCtx().addBeforeExecScript("document.getElementById('iframe_tmp').contentWindow.dropEventHandler({type:'release'});");
			} else {
				MenuItem menuItem2 = new MenuItem();
				menuItem2.setId("deleteCtrlItem");
				menuItem2.setText("删除");
				genCtrlContextMenuEvent(menuItem2, true);
				conetxtMenu.addMenuItem(menuItem2);
			}
		}

		onCtrldsAfterRowSelect(e);
	}

	/**
	 * 控件树右键菜单项单击事件
	 * 
	 * @param e
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void ctrlContextMenuClickHandler(MouseEvent<MenuItem> e) {
		String menuItemId = e.getSource().getId();
		Dataset ds = LuiAppUtil.getDataset("ctrlds");
		LifeCyclePhase phase = RequestLifeCycleContext.get().getPhase();
		RequestLifeCycleContext.get().setPhase(LifeCyclePhase.design);
		IPaEditorService ipaService = new PaEditorServiceImpl();
		String sessionId = (String) PaCache.getInstance().get("eclipse_sesionId");
		String pageId = (String) PaCache.getInstance().get("_pageId");
		String viewId = (String) PaCache.getInstance().get("_viewId");
		PagePartMeta pagemeta = ipaService.getOriPageMeta(pageId, sessionId);
		ViewPartMeta widget = pagemeta.getWidget(viewId);
		
		if(widget == null){
			throw new LuiRuntimeException("该窗口无法创建控件");
		}

		if (StringUtils.equals(menuItemId, "deleteCtrlItem")) {
			
			// 右键菜单删除事件
			Row row = ds.getSelectedRow();
			String id = row.getString(ds.nameToIndex("id"));
			//要删除View中的东东，先要判断需要删除的东东是什么类型
			if(StringUtils.equals(row.getString(ds.nameToIndex("type")), LuiPageContext.SOURCE_TYPE_CONTEXTMENU))
				widget.getViewMenus().removeContextMenu(id);
			else
				widget.getViewComponents().removeComponent(id);
			ds.removeRow(row);
			
			//将删除的控件状态从UIState删除
			delFromUIState(pagemeta,widget.getId(),id);
			
			// TODO:可能还要处理画布上的控件删除操作
			ipaService.setOriPageMeta(pageId, sessionId, pagemeta);
		}
		else if (StringUtils.equals(menuItemId, "ContextMenuComp")) {
			newContextMenu();
		} else if(StringUtils.equals(menuItemId, "editFormComp")) {
			Row row = ds.getSelectedRow();
			String id = row.getString(ds.nameToIndex("id"));
			String paramBuf = "?sourceEleId=" + id + "&sourceWinId=" + pageId;
			paramBuf = paramBuf + "&sourceViewId=" + viewId + "&sourceType=" + LuiPageContext.SOURCE_TYPE_FORMCOMP;
			String otherPageId = LuiRuntimeContext.getWebContext().getPageUniqueId();
			paramBuf = paramBuf + "&" + ParamConstant.OTHER_PAGE_UNIQUE_ID + "=" +  otherPageId;
			paramBuf = paramBuf + "&enterParam="+UUID.randomUUID();
			StringBuffer urlBuf = new StringBuffer();
			urlBuf.append(LuiRuntimeContext.getRootPath()).append("/app/mockapp/fieldmgr").append(paramBuf);
			AppSession.current().getAppContext().popOuterWindow(urlBuf.toString(), "编辑属性", "680", "480", AppContext.TYPE_DIALOG);
		} else {
			newCtrl(menuItemId, ds, pagemeta, widget);
		}
		RequestLifeCycleContext.get().setPhase(phase);
	}
	public void newContextMenu() {
		Map<String, String> paramMap = new HashMap<String, String>();
//			paramMap.put("sessionId" , sessionId);
//			paramMap.put("sourceWinId" , pageId);
//			paramMap.put("sourceView", viewId);

		//当在app中打开其他page的时候  如果没有发生改变不需要传入。page属性发生变化的时候需要（pi）属性
		paramMap.put("pi", UUID.randomUUID().toString());
		AppSession.current().getAppContext().navgateTo("rightMenu", "新建右键菜单", "540", "400", paramMap);
	}
	public void newCtrl(String menuItemId, Dataset ds, PagePartMeta pagemeta, ViewPartMeta widget) {
		Map<String, Object[]> map = getAttrForTreeNodeMap();
		Object[] treeNodeParam = map.get(menuItemId);
		// 右键菜单单击事件,特殊处理form,grid
		String inputDialogTitle = treeNodeParam[1] + "信息";
				
		InputItem ctrId = new StringInputItem("ctrId", "控件编码：", true);
		if (StringUtils.equals(menuItemId, "GridComp") || StringUtils.equals(menuItemId, "FormComp")) {
			// Grid,form需要增加数据集选择，并根据数据集生成控件
			InputItem ctrlDsId = genDatasetComboInputItem(widget, "ctrlDsId", "数据集：", true);
			InteractionUtil.showInputDialog(inputDialogTitle, new InputItem[] { ctrId, ctrlDsId });
		} else {
			InteractionUtil.showInputDialog(inputDialogTitle, new InputItem[] { ctrId });
		}

		Map<String, String> rs = InteractionUtil.getInputDialogResult();
		String ctrIdStr = rs.get("ctrId");

		WebComp webcomp = null;
		if (StringUtils.equals(menuItemId, "GridComp") || StringUtils.equals(menuItemId, "FormComp")) {
			String ctrlDsIdStr = rs.get("ctrlDsId");
			if (StringUtils.isEmpty(ctrlDsIdStr)) {
				throw new LuiRuntimeException("请选择数据集!");
			}
			webcomp = genControlByDataset(widget, rs, menuItemId);
		} else {
			webcomp = LuiClassUtil.newInstance((Class) treeNodeParam[5]);
		}
		webcomp.setId(ctrIdStr);
		
		if(StringUtils.equals("TreeViewComp", menuItemId)) {
			((TreeViewComp) webcomp).setShowRoot(true);
			((TreeViewComp) webcomp).setExpand(true);
		}
		
		if(StringUtils.equals("MenubarComp", menuItemId)) {
			widget.getViewMenus().addMenuBar((MenubarComp)webcomp);
		} else {
			widget.getViewComponents().addComponent(webcomp);
		}
		
		genChildrenComps(webcomp, menuItemId, ctrIdStr);
		
		//将创建的控件状态添加到UIState
		addToUIState(pagemeta,widget.getId(),webcomp,menuItemId);

		Row row = ds.getEmptyRow();
		row.setValue(ds.nameToIndex("id"), ctrIdStr);
		row.setValue(ds.nameToIndex("pid"), DEFINED);
		row.setValue(ds.nameToIndex("name"), (String) treeNodeParam[1]);
		row.setValue(ds.nameToIndex("type"), (String) treeNodeParam[2]);
		row.setValue(ds.nameToIndex("type2"), (String) treeNodeParam[3]);
		row.setValue(ds.nameToIndex("imgtype"), (String) treeNodeParam[4]);
		ds.addRow(row);
		
		if(StringUtils.equals(menuItemId, "FormComp")) {
			genFormElement2CtrlTree((FormComp)webcomp,ds);
		}
	}
	//将删除的控件状态从UIState删除
	private void delFromUIState(PagePartMeta pagemeta, String viewId, String webcompId) {
		List<UIState> uiStates = pagemeta.getuIStates();
		if(uiStates != null && uiStates.size() > 0){
			for(UIState us : uiStates){
				ViewState viewstate = us.getViewState(viewId);
				if(viewstate != null){
					viewstate.removeCtrlState(webcompId);
				}
			}
		}
	}
	//将创建的控件状态添加到UIState
	public void addToUIState(PagePartMeta pagemeta, String viewId, WebComp webcomp, String compType) {
		List<UIState> uiStates = pagemeta.getuIStates();
		if (uiStates != null && uiStates.size() > 0) {
			for (UIState us : uiStates) {
				ViewState viewstate = us.getViewState(viewId);
				if (viewstate != null) {
					CtrlState ctrlState = new CtrlState();
					ctrlState.setId(webcomp.getId());
					ctrlState.setVisible(webcomp.isVisible());
					ctrlState.setEnabled(webcomp.isEnabled());
					viewstate.addCtrlState(ctrlState);

					if (StringUtils.equals("MenubarComp", compType)) {  //menubaritem
						List<MenuItem> items = ((MenubarComp) webcomp).getMenuList();
						if (items != null && items.size() > 0) {
							for (MenuItem item : items) {
								CtrlState ctrlState2 = new CtrlState();
								ctrlState2.setId(item.getId());
								ctrlState2.setPid(webcomp.getId());
								ctrlState2.setVisible(item.isVisible());
								ctrlState2.setEnabled(item.isEnabled());
								viewstate.addCtrlState(ctrlState2);
							}
						}
					}
					if(StringUtils.equals("ToolBarComp", compType)){  //toolbaritem
						List<ToolBarItem> items = ((ToolBarComp)webcomp).getElementList();
						if (items != null && items.size() > 0) {
							for (ToolBarItem item : items) {
								CtrlState ctrlState2 = new CtrlState();
								ctrlState2.setId(item.getId());
								ctrlState2.setPid(webcomp.getId());
								ctrlState2.setVisible(item.isVisible());
								ctrlState2.setEnabled(item.isEnabled());
								viewstate.addCtrlState(ctrlState2);
							}
						}
					}
					if(StringUtils.equals("FormComp", compType)){  //formelement
						List<FormElement> items = ((FormComp)webcomp).getElementList();
						if (items != null && items.size() > 0) {
							for (FormElement item : items) {
								CtrlState ctrlState2 = new CtrlState();
								ctrlState2.setId(item.getId());
								ctrlState2.setPid(webcomp.getId());
								ctrlState2.setVisible(item.isVisible());
								ctrlState2.setEnabled(item.isEnabled());
								viewstate.addCtrlState(ctrlState2);
							}
						}
					}
				}
			}
		}
	}
	/**
	 * 处理含有子元素的控件初始化
	 * @param webcomp
	 * @param compType
	 * @param compId
	 */
	public void genChildrenComps(WebComp webcomp, String compType, String compId) {
		if(StringUtils.equals("MenubarComp", compType)) {
			String[][] menuItemDefaults = {{"new","新建","new.png"},{"edit","编辑","edit.png"},{"delete","删除","delete.png"}};
			for(String[] menuItemDefault : menuItemDefaults) {
				MenuItem menuItem = new MenuItem();
				menuItem.setId(compId + "_menuitem_" + menuItemDefault[0]);
				menuItem.setText(menuItemDefault[1]);
				menuItem.setImgIcon(menuItemDefault[2]);
				((MenubarComp)webcomp).addMenuItem(menuItem);
			}
		} else if(StringUtils.equals("ToolBarComp", compType)) {
			String[][] toolbarItemDefaults = {{"new","新建","new.png"},{"edit","编辑","edit.png"},{"delete","删除","delete.png"},{"save","保存","save.png"},{"cancel","取消","cancel.png"}};
			for(String[] toolbarItemDefault : toolbarItemDefaults) {
				ToolBarItem toolbarItem = new ToolBarItem();
				toolbarItem.setId(compId + "_toolbaritem_" + toolbarItemDefault[0]);
				toolbarItem.setText(toolbarItemDefault[1]);
				toolbarItem.setRefImg(toolbarItemDefault[2]);
				((ToolBarComp)webcomp).addElement(toolbarItem);
			}
		}
	} 

	/**
	 * 构造dataset的ComboInputItem
	 * 
	 * @param widget
	 * @param inputItemId
	 * @param inputItemText
	 * @return
	 */
	private InputItem genDatasetComboInputItem(ViewPartMeta widget, String inputItemId, String inputItemText, boolean require) {
		InputItem inputItem = new ComboInputItem(inputItemId, inputItemText, require);
		Dataset[] datasets = widget.getViewModels().getDatasets();
		if (ArrayUtils.isEmpty(datasets)) {
			throw new LuiRuntimeException("请先创建数据集！");
		} else{
			ComboData cd = new DataList();
			for (Dataset dss : datasets) {
				if(!(dss instanceof RefMdDataset)) {
					DataItem item = new DataItem(dss.getId(), StringUtils.isEmpty(dss.getCaption()) ? dss.getId() : dss.getCaption());
					cd.addDataItem(item);
				}
			}
			inputItem.setComboData(cd);
		}
		return inputItem;
	}

	/**
	 * 根据数据集构建组件(Grid,Form控件)
	 * 
	 * @param widget
	 * @param datasetId
	 * @param type
	 * @return
	 */
	public WebComp genControlByDataset(ViewPartMeta widget, Map<String, String> rs, String type) {
		String datasetId = rs.get("ctrlDsId");
		Dataset ds = widget.getViewModels().getDataset(datasetId);
		Field[] fields = ds.getFields();
		WebComp webcomp = null;
		switch (type) {
		case "GridComp": // 处理grid
			webcomp = new GridComp();
			((GridComp) webcomp).setDataset(datasetId);
			List<IGridColumn> columnList = new ArrayList<IGridColumn>();
			for (Field field : fields) {
				GridColumn gridColumn = new GridColumn();
				gridColumn.setId(field.getId());
				gridColumn.setField(field.getId());
				gridColumn.setRenderType(RenderTypeConst.DefaultRender);
				gridColumn.setDataType(field.getDataType());
				String name = StringUtils.isEmpty(field.getText()) ? field.getId() : field.getText();
				gridColumn.setText(name);
				if(StringDataTypeConst.FBOOLEAN.equals(field.getDataType())){
					gridColumn.setShowCheckBox(false);
					gridColumn.setRenderType(RenderTypeConst.BooleanRender);
				}
				if (field.getExtSource() != null) {
					gridColumn.setVisible(false);
					String extS =  field.getExtSource();
					if (Field.REF_KEY.equals(extS)){
						gridColumn.setEditorType(EditorTypeConst.REFERENCE);
						gridColumn.setRefNode(field.getExtSourceAttr());
						gridColumn.setVisible(true);
					}else if(Field.COMB_KEY.equals(extS)){
						gridColumn.setVisible(true);
						gridColumn.setRenderType(RenderTypeConst.ComboRender);
						gridColumn.setEditorType(EditorTypeConst.COMBODATA);
						gridColumn.setRefComboData(field.getExtSourceAttr());
					}
				}
				columnList.add(gridColumn);
			}
			((GridComp) webcomp).setColumnList(columnList);
			break;
		case "FormComp": // 处理表单
			webcomp = new FormComp();
			((FormComp) webcomp).setDataset(datasetId);
			List<FormElement> formElementList = new ArrayList<FormElement>();
			if(!ArrayUtils.isEmpty(fields)) {
				for (Field field : fields) {
					FormElement element = new FormElement();
					element.setId(field.getId());
					element.setField(field.getId());
					element.setWidget(widget);
					element.setDataType(field.getDataType());
					String name = StringUtils.isEmpty(field.getText()) ? field.getId() : field.getText();
					element.setText(name);
					element.setLabel(name);
					
					if (field.getExtSource() != null) {
						element.setVisible(false);
						String extS =  field.getExtSource();
						if (Field.REF_KEY.equals(extS)){
							element.setVisible(true);
							element.setEditorType(EditorTypeConst.REFERENCE);
							element.setRefNode(field.getExtSourceAttr());
						}else if(Field.COMB_KEY.equals(extS)){
							element.setVisible(true);
							element.setEditorType(EditorTypeConst.COMBODATA);
							element.setRefComboData(field.getExtSourceAttr());
						}
					}
					
					formElementList.add(element);
				}
			}
			
			((FormComp) webcomp).setElementList(formElementList);
			break;
		}
		return webcomp;
	}

	/**
	 * 构造控件树右键菜单项事件
	 * 
	 * @param menuItem
	 * @param hasEvent
	 */
	private void genCtrlContextMenuEvent(MenuItem menuItem, boolean hasEvent) {
		if (hasEvent) {
			LuiEventConf event = new LuiEventConf();
			event.setEventType(MouseEvent.class.getSimpleName());
			event.setOnserver(true);
			EventSubmitRule submitRule = new EventSubmitRule();
			event.setSubmitRule(submitRule);
			event.setEventName("onclick");
			event.setMethod("ctrlContextMenuClickHandler");
			event.setControllerClazz(this.getClass().getName());
			menuItem.addEventConf(event);
		}
	}
	
	//UI组件数据集行选中事件
	public void onCurrdsAfterRowSelect(DatasetEvent e) {
		Dataset ds = e.getSource();
		ContextMenuComp conetxtMenu = (ContextMenuComp) LuiAppUtil.getContextMenu("currTreeContextMenu");
		conetxtMenu.removeChildrenItem();
		Row row = ds.getSelectedRow();
		String treeId = row.getString(ds.nameToIndex("id"));
		MenuItem menuItem = null;
		String pageId = PaCache.getEditorPageId();
		if (pageId.equals(treeId)) {
			menuItem = new MenuItem();
			menuItem.setId("importPubView");
			menuItem.setText("导入公共View");
			genCurrContextMenuEvent(menuItem);
			conetxtMenu.addMenuItem(menuItem);
		} else {
			menuItem = new MenuItem();
			menuItem.setId("deleteViewItem");
			menuItem.setText("删除");
			genCurrContextMenuEvent(menuItem);
			conetxtMenu.addMenuItem(menuItem);
		}
		
		onCurrDsRowSelect(e);
	}

	/**
	 * 构造UI组件右键菜单项事件
	 */
	private void genCurrContextMenuEvent(MenuItem menuItem) {
		LuiEventConf event = new LuiEventConf();
		event.setEventType(MouseEvent.class.getSimpleName());
		event.setOnserver(true);
		EventSubmitRule submitRule = new EventSubmitRule();
		if (!StringUtils.equals(menuItem.getId(), "deleteViewItem")) {
			WidgetRule widgetRuled = new WidgetRule();
			widgetRuled.setId("project");
			DatasetRule dsRule = new DatasetRule();
			dsRule.setId("projViewTreeDs");
			dsRule.setType(DatasetRule.TYPE_ALL_LINE);
			widgetRuled.addDsRule(dsRule);
			submitRule.addWidgetRule(widgetRuled);
		}else{
			//取消选中
			LuiEventConf event2 = new LuiEventConf();
			event2.setEventType(MouseEvent.class.getSimpleName());
			event2.setOnserver(true);
			EventSubmitRule submitRule2 = new EventSubmitRule();
			event2.setSubmitRule(submitRule2);
			event2.setEventName("onclick");
			event2.setMethod("viewDelClickHandler");
			event2.setControllerClazz(this.getClass().getName());
			menuItem.addEventConf(event2);
		}
		event.setSubmitRule(submitRule);
		event.setEventName("onclick");
		event.setMethod("currContextMenuClickHandler");
		event.setControllerClazz(this.getClass().getName());
		menuItem.addEventConf(event);
	}
	/**
	 * UI组件树右键菜单项单击事件
	 */
	public void currContextMenuClickHandler(MouseEvent<MenuItem> e) {
		String menuItemId = e.getSource().getId();
		Dataset ds = LuiAppUtil.getDataset("currds");

		IPaEditorService ipaService = new PaEditorServiceImpl();
		String sessionId = (String) PaCache.getInstance().get("eclipse_sesionId");
		String pageId = (String) PaCache.getInstance().get("_pageId");
		PagePartMeta pagemeta = ipaService.getOriPageMeta(pageId, sessionId);

		if (StringUtils.equals(menuItemId, "deleteViewItem")) {
			LifeCyclePhase phase = RequestLifeCycleContext.get().getPhase();
			RequestLifeCycleContext.get().setPhase(LifeCyclePhase.design);
			// 右键菜单删除事件
			Row row = ds.getSelectedRow();
			String id = row.getString(ds.nameToIndex("id"));
			pagemeta.removeWidget(id);
			ds.removeRow(row);
			// TODO:可能还要处理画布上的view删除操作
			RequestLifeCycleContext.get().setPhase(phase);
			ipaService.setOriPageMeta(pageId, sessionId, pagemeta);
		} else {
			// 右键菜单单击事件
			String inputDialogTitle = "导入公共View";
			InputItem pubViewId = genPubViewComboInputItem("pubViewId", "选择View：", true);
			InteractionUtil.showInputDialog(inputDialogTitle, new InputItem[] { pubViewId });

			Map<String, String> rs = InteractionUtil.getInputDialogResult();
			String pubViewIdStr = rs.get("pubViewId");
			if (StringUtils.isEmpty(pubViewIdStr)) {
				throw new LuiRuntimeException("请选择pubView!");
			}
			//添加公共View
			//PublicViewUtil.addPublicView(pubViewIdStr, pubViewIdStr, pagemeta);
			{
				ViewPartConfig wconfig = new ViewPartConfig();
				wconfig.setId(pubViewIdStr);
				wconfig.setRefId("../" + pubViewIdStr);
				pagemeta.addViewPartConf(wconfig);
				
				ViewPartMeta widget = PoolObjectManager.getWidget(pubViewIdStr);
				//String providerClazz = widget.getProvider();
//				if(providerClazz != null && !providerClazz.equals("")){
//					ViewPartProvider provider = (ViewPartProvider) ClassUtil.newInstance(providerClazz);
//					widget = provider.buildWidget(pm, widget, null, id);
//					widget.setIsCustom(false);
//				}
//				widget.setId(id);
				pagemeta.addWidget(widget);
			}
			Row selectrow = ds.getSelectedRow();
			Row row = ds.getEmptyRow();
			row.setValue(ds.nameToIndex("id"), pubViewIdStr);
			row.setValue(ds.nameToIndex("pid"), selectrow.getValue(ds.nameToIndex("id")));
			row.setValue(ds.nameToIndex("name"), pubViewIdStr);
			row.setValue(ds.nameToIndex("type"), LuiPageContext.SOURCE_TYPE_WIDGT);
			ds.addRow(row);
		}
	}
	
	//删除pubView后取消选中
	public void viewDelClickHandler(MouseEvent e){
		LuiAppUtil.getCntAppCtx().addExecScript("var ifram_tmp_div = document.getElementById('iframe_tmp');if(ifram_tmp_div.contentWindow.dropEventHandler)ifram_tmp_div.contentWindow.dropEventHandler({type:'release'});");
	}
	
	/**
	 * 构造pubView的ComboInputItem
	 */
	private InputItem genPubViewComboInputItem(String inputItemId, String inputItemText, boolean require) {
		LifeCyclePhase pa = RequestLifeCycleContext.get().getPhase();
		RequestLifeCycleContext.get().setPhase(LifeCyclePhase.nullstatus);
		InputItem inputItem = new ComboInputItem(inputItemId, inputItemText, require);
		Dataset prjDs = LuiAppUtil.getCntWindowCtx().getViewContext("project").getView().getViewModels().getDataset("projViewTreeDs");
		PageData pageData = prjDs.getCurrentPageData();
		Row[] rows = pageData.getRows();
		ComboData cd = new DataList();
		for(Row row : rows){
			String pid = (String) row.getValue(prjDs.nameToIndex("pid"));
			if(StringUtils.equals("pub_ui_views", pid)){
				String viewId = (String) row.getValue(prjDs.nameToIndex("name"));
				DataItem item = new DataItem(viewId, viewId);
				cd.addDataItem(item);
			}
		}
		inputItem.setComboData(cd);
		RequestLifeCycleContext.get().setPhase(pa);
		return inputItem;
	}
	
	public void onEntitydsAfterRowSelect(DatasetEvent e) {
		LuiAppUtil.getCntAppCtx().addBeforeExecScript("pageUI.getViewPart('settings').getDataset('ds_middle').silent = true");
		LuiAppUtil.getCntAppCtx().addAfterExecScript("pageUI.getViewPart('settings').getDataset('ds_middle').silent = false");
		Dataset ds = e.getSource();
		Row row = ds.getSelectedRow();
		String value = (String) row.getValue(ds.nameToIndex("type"));
		String pId = row.getString(ds.nameToIndex("pid"));
		ContextMenuComp contextMenu = LuiAppUtil.getCntView().getViewMenus().getContextMenu("dsModelMenu");
		if(contextMenu == null)
			throw new LuiRuntimeException("右键菜单买找着！");
		MenuItem addDsItem = contextMenu.getItem("addModel");
		MenuItem addGenDsItem = contextMenu.getItem("addModel2");
		MenuItem addRefItem = contextMenu.getItem("addRefNode");
		MenuItem addComboItem = contextMenu.getItem("addCombo");
		MenuItem editModelItem = contextMenu.getItem("editModel");
		MenuItem copyModelItem = contextMenu.getItem("copyModel");
		MenuItem deleteModelItem = contextMenu.getItem("deleteModel");
		
		MenuItem addGridRefItem = contextMenu.getItem("addGridRef");
		MenuItem addTreeRefItem = contextMenu.getItem("addTreeRef");
		MenuItem addTreeGridRefItem = contextMenu.getItem("addTreeGridRef");
		
		if (StringUtils.isNotBlank(pId)) {
			//右键菜单
			editModelItem.setVisible(true);
			copyModelItem.setVisible(true);
			deleteModelItem.setVisible(true);
			addDsItem.setVisible(false);
			addGenDsItem.setVisible(false);
			addRefItem.setVisible(false);
			addComboItem.setVisible(false);
			
			addGridRefItem.setVisible(false);
			addTreeRefItem.setVisible(false);
			addTreeGridRefItem.setVisible(false);
			
			//显示属性和事件
			PaPropertyDatasetListener paListener = new PaPropertyDatasetListener();
   
			AppSession ctx = AppSession.current();
			String viewId = (String) PaCache.getInstance().get("_viewId");
			ctx.setParameter(RaParameter.PARAM_WIDGETID, viewId);
			ctx.setParameter(RaParameter.PARAM_UIID, (String)row.getValue(ds.nameToIndex("id")));
			ctx.setParameter(RaParameter.PARAM_ELEID, (String)row.getValue(ds.nameToIndex("id")));
			ctx.setParameter(RaParameter.PARAM_SUBELEID, null);

			if("DATASET".equals(value)) {
				ctx.setParameter(RaParameter.PARAM_TYPE, "dataset");
			}else if("RefNode".equals(value)) {
				ctx.setParameter(RaParameter.PARAM_TYPE, "genericrefnode");
			}else if("ComboData".equals(value)) {
				ctx.setParameter(RaParameter.PARAM_TYPE, "combodata");
			}else{
				return;
			}

			paListener.setFormElementInvisible();//将不可见的属性框设为不可见
			// 判断是选中的控件是否获得焦点
			boolean mark = paListener.focusSelectDs();
			if (!mark)
				return;
			paListener.setFormElementVisible();//属性
			paListener.setEventGridElementVisible();//事件
		}else{//过滤右键菜单
			editModelItem.setVisible(false);
			copyModelItem.setVisible(false);
			deleteModelItem.setVisible(false);
			if(StringUtils.equals("DATASET", value)){//数据集
				addGridRefItem.setVisible(false);
				addTreeRefItem.setVisible(false);
				addTreeGridRefItem.setVisible(false);
				addDsItem.setVisible(true);
				addGenDsItem.setVisible(true);
				addRefItem.setVisible(false);
				addComboItem.setVisible(false);
			}else if(StringUtils.equals("ComboData", value)){//枚举
				addGridRefItem.setVisible(false);
				addTreeRefItem.setVisible(false);
				addTreeGridRefItem.setVisible(false);
				addDsItem.setVisible(false);
				addGenDsItem.setVisible(false);
				addRefItem.setVisible(false);
				addComboItem.setVisible(true);
			}else if(StringUtils.equals("RefNode", value)){//参照
				addGridRefItem.setVisible(false);
				addTreeRefItem.setVisible(false);
				addTreeGridRefItem.setVisible(false);
				addDsItem.setVisible(false);
				addGenDsItem.setVisible(false);
				addRefItem.setVisible(true);
				addComboItem.setVisible(false);
			}else{//参照模型
				addGridRefItem.setVisible(true);
				addTreeRefItem.setVisible(true);
				addTreeGridRefItem.setVisible(true);
				addDsItem.setVisible(false);
				addGenDsItem.setVisible(false);
				addRefItem.setVisible(false);
				addComboItem.setVisible(false);
			}
		}
	}

	public void onCtrldsAfterRowSelect(DatasetEvent e) {
		Dataset ds = e.getSource();
		Row row = ds.getSelectedRow();
		String pId = row.getString(ds.nameToIndex("pid"));
		if (StringUtils.isNotEmpty(pId)) {
			// 显示属性和事件
			PaPropertyDatasetListener paListener = new PaPropertyDatasetListener();

			AppSession ctx = AppSession.current();
			String viewId = (String) PaCache.getInstance().get("_viewId");
			
			String type = (String) row.getValue(ds.nameToIndex("type"));
			if(StringUtils.equals(type, LuiPageContext.SOURCE_TYPE_FORMELE)) {
				ctx.setParameter(RaParameter.PARAM_ELEID, pId);
				ctx.setParameter(RaParameter.PARAM_UIID, pId);
				//ctx.setParameter(RaParameter.PARAM_SUBUIID, (String) row.getValue(ds.nameToIndex("id")));
				ctx.setParameter(RaParameter.PARAM_SUBELEID, (String) row.getValue(ds.nameToIndex("id")));
			} else {
				ctx.setParameter(RaParameter.PARAM_UIID, (String) row.getValue(ds.nameToIndex("id")));
				ctx.setParameter(RaParameter.PARAM_ELEID, (String) row.getValue(ds.nameToIndex("id")));
				ctx.setParameter(RaParameter.PARAM_SUBELEID, null);
			}
			ctx.setParameter(RaParameter.PARAM_TYPE, type);
			ctx.setParameter(RaParameter.PARAM_WIDGETID, viewId);

			paListener.setFormElementInvisible();// 将不可见的属性框设为不可见

			// 判断是选中的控件是否获得焦点
			boolean mark = paListener.focusSelectDs();
			if (!mark)
				return;
			paListener.setFormElementVisible();// 属性
			paListener.setEventGridElementVisible();// 事件
		}
	}
	
	//ui视图
	private void onCurrDsRowSelect(DatasetEvent e) {
		Dataset ds = e.getSource();
		Row row = ds.getSelectedRow();
		String pId = row.getString(ds.nameToIndex("pid"));

			// 显示属性和事件
			PaPropertyDatasetListener paListener = new PaPropertyDatasetListener();

			AppSession ctx = AppSession.current();
			ctx.setParameter(RaParameter.PARAM_WIDGETID, null);
			ctx.setParameter(RaParameter.PARAM_UIID, (String) row.getValue(ds.nameToIndex("id")));
			ctx.setParameter(RaParameter.PARAM_ELEID, (String) row.getValue(ds.nameToIndex("id")));
			ctx.setParameter(RaParameter.PARAM_SUBELEID, null);

			ctx.setParameter(RaParameter.PARAM_TYPE, (String) row.getValue(ds.nameToIndex("type")));

			paListener.setFormElementInvisible();// 将不可见的属性框设为不可见
			
			pipes(ds);//管道
			connector(ds);//连接器
			paramsLoad(ds);//参数

			// 判断是选中的控件是否获得焦点
			boolean mark = paListener.focusSelectDs();
			if (!mark)
				return;

			paListener.setFormElementVisible();// 属性
			paListener.setEventGridElementVisible();// 事件
			if (pId == null) //若无pId则执行
				LuiAppUtil.getCntAppCtx().addExecScript("document.getElementById('iframe_tmp').contentWindow.dropEventHandler({type:'release'});");
	}
	//行选中事件加载管道
	private void pipes(Dataset ds){
		Row selRow = ds.getSelectedRow();
		if(selRow == null)
			return;
		PagePartMeta pagePart = PaCache.getEditorPagePartMeta();
		ViewPartMeta viewPartMeta = null;
		String pId = selRow.getString(ds.nameToIndex("pid"));
		if(pId != null)
			viewPartMeta = pagePart.getWidget((String)selRow.getValue(ds.nameToIndex("id")));
		
		PaPropertyDatasetListener paListener = new PaPropertyDatasetListener();
		//输入
		Dataset inDs = LuiRuntimeContext.getWebContext().getPageMeta().getWidget("settings").getViewModels().getDataset("ds_pipein");
		inDs.clear();
		paListener.loadPipeInDsData(inDs, viewPartMeta, pagePart);
		//输出
		Dataset outDs = LuiRuntimeContext.getWebContext().getPageMeta().getWidget("settings").getViewModels().getDataset("ds_pipeout");
		outDs.clear();
		paListener.loadPipeOutDsData(outDs, viewPartMeta, pagePart);
	}
    //行选中事件加载连接器
	private void connector(Dataset ds){
		Row selRow = ds.getSelectedRow();
		if(selRow == null)
			return;
		String pId = selRow.getString(ds.nameToIndex("pid"));
		if(pId != null)
			return;
		PagePartMeta pagePart = PaCache.getEditorPagePartMeta();
		PaPropertyDatasetListener paListener = new PaPropertyDatasetListener();
		Dataset conDs = LuiRuntimeContext.getWebContext().getPageMeta().getWidget("settings").getViewModels().getDataset("ds_connector");
		conDs.clear();
		paListener.loadConnectorDsData(conDs,  pagePart, null);
	}
	//行选中事件加载参数
	private void paramsLoad(Dataset ds){
		Row selRow = ds.getSelectedRow();
		if(selRow == null)
			return;
		String pId = selRow.getString(ds.nameToIndex("pid"));
		if(pId != null)
			return;
		PagePartMeta pagePart = PaCache.getEditorPagePartMeta();
		PaPropertyDatasetListener paListener = new PaPropertyDatasetListener();
		Dataset paramDs = LuiRuntimeContext.getWebContext().getPageMeta().getWidget("settings").getViewModels().getDataset("ds_param");
		paListener.loadParamDsData(paramDs,  pagePart.getWindow().getProps());
	}
	
	// 后退按钮
	public void onclickBack(MouseEvent e) {
		LuiMeta luiMeta = PaCache.nowMeta;
		if (luiMeta != null && luiMeta.getPre() != null) {
			LuiMeta pre = luiMeta.getPre();
			// luiMeta.setPagePartMeta(pre.getPagePartMeta());
			// luiMeta.setuIPartMeta(pre.getuIPartMeta());
			PaCache.nowMeta = pre;
			String pageId1 = (String) PaCache.getInstance().get("_pageId");
			String viewId1 = (String) PaCache.getInstance().get("_viewId");
			{
				IPaEditorService ipaService = new PaEditorServiceImpl();
				String sessionId = (String) PaCache.getInstance().get("eclipse_sesionId");
				ipaService.setOriPageMeta(pageId1, sessionId, pre.getPagePartMeta());
				ipaService.setOriUIMeta(pageId1, sessionId, pre.getuIPartMeta());
			}
			// 重新加载页面
			reloadView(pageId1, viewId1);
		}
	}

	// 前进按钮
	public void onclickNext(MouseEvent e) {
		LuiMeta luiMeta = PaCache.nowMeta;
		if (luiMeta != null && luiMeta.getNext() != null) {
			// luiMeta.setPre(luiMeta);
			// luiMeta.setPagePartMeta(luiMeta.getNext().getPagePartMeta());
			// luiMeta.setuIPartMeta(luiMeta.getNext().getuIPartMeta());
			LuiMeta next = luiMeta.getNext();
			PaCache.nowMeta = next;
			String pageId1 = (String) PaCache.getInstance().get("_pageId");
			String viewId1 = (String) PaCache.getInstance().get("_viewId");
			{
				IPaEditorService ipaService = new PaEditorServiceImpl();
				String sessionId = (String) PaCache.getInstance().get("eclipse_sesionId");
				ipaService.setOriPageMeta(pageId1, sessionId, next.getPagePartMeta());
				ipaService.setOriUIMeta(pageId1, sessionId, next.getuIPartMeta());
			}
			// 重新加载页面
			reloadView(pageId1, viewId1);
		}
	}

	public static void reloadView(String pageId, String viewId) {
		String sesionId = LuiRuntimeContext.getWebContext().getRequest().getSession().getId();
		String eclipse_sesionId = (String) PaCache.getInstance().get("eclipse_sesionId");
		if (StringUtils.isBlank(eclipse_sesionId)) {
			PaCache.getInstance().pub("eclipse_sesionId", sesionId);
		}
		// 调用处理逻辑
		// PaCache.getInstance().pub("_pageId", viewId);
		// PaCache.getInstance().pub("_viewId", "");
		if (pageId == null) {
			pageId = (String) PaCache.getInstance().get("_pageId");
		} else {
			PaCache.getInstance().pub("_pageId", pageId);
       }
		if(viewId==null){
			viewId=(String)PaCache.getInstance().get("_viewId");
		}else{
			PaCache.getInstance().pub("_viewId", viewId);
		}
		PaPalletDsListener._edit_view(pageId, viewId);
	}

	private static void _edit_view(String pageId, String viewId) {
		LuiWebContext web_ctx = LuiRuntimeContext.getWebContext();
		PagePartMeta design_page = web_ctx.getPageMeta();
		IFrameComp iframe = (IFrameComp) design_page.getWidget("editor").getViewComponents().getComponent("iframe_tmp");
		{
			String otherPageId = LuiRuntimeContext.getWebContext().getPageUniqueId();
			String url1 = "/portal/app/mockapp/" + pageId;
			url1 += "?"+LuiRuntimeContext.MODEPHASE+"=eclipse&viewId=" + viewId;
			url1 += "&emode=1&model=" + RaSelfWindow.class.getName() + "&otherPageUniqueId=" + otherPageId;
			iframe.setSrc(url1);
		}
		IPaEditorService ipaService = new PaEditorServiceImpl();
		String sessionId = (String) PaCache.getInstance().get("eclipse_sesionId");
		PagePartMeta pagePartMeta = ipaService.getOriPageMeta(pageId, sessionId);
		UIPartMeta uiPartMeta = ipaService.getOriUIMeta(pageId, sessionId);
		{
			DataModels dataModels = design_page.getWidget("data").getViewModels();
			Dataset ctrlDs = dataModels.getDataset("ctrlds");
			PaPalletDsListener.fillCtrlDs(ctrlDs, pagePartMeta, viewId);
			Dataset currDs = dataModels.getDataset("currds");
			PaPalletDsListener.fillCurrDs(currDs, pagePartMeta, uiPartMeta);
			Dataset layOutDs = dataModels.getDataset("layoutds");
			PaPalletDsListener.fillLayoutDs(layOutDs);
			Dataset entityDs = dataModels.getDataset("entityds");
			PaEntityDsListener.setModelData(entityDs, pagePartMeta.getWidget(viewId));
		}
	}

	// 编辑form时管道事件
	public void pluginplugin_formdata(Map<Object, Object> obj) {
		Dataset ctrlDs = AppSession.current().getAppContext().getWindowContext("pa").getViewContext("data").getView().getViewModels().getDataset("ctrlds");
		FormComp editForm = (FormComp) obj.get("editForm");
		List<FormElement> newformeles = editForm.getElementList();
		for (FormElement formele : newformeles) {
			addRow2DataDs(editForm.getId(), formele, ctrlDs);
		}
		removeInValidRowFromCtrlDs(ctrlDs, editForm);
	}

	// 将变化列后的表格重新添加到已定义控件区（data）
	private void addRow2DataDs(String formid, FormElement formele, Dataset ctrlDs) {
		if (eleidInCtrlDs(ctrlDs, formele.getId())) {// 若formele存在，则不添加
			return;
		}
		if (formele.isVisible()) {
			Row formelementRow = ctrlDs.getEmptyRow();
			formelementRow.setValue(ctrlDs.nameToIndex("id"), formele.getId());
			formelementRow.setValue(ctrlDs.nameToIndex("pid"), formid);
			formelementRow.setValue(ctrlDs.nameToIndex("name"), formele.getText());
			formelementRow.setValue(ctrlDs.nameToIndex("type"), LuiPageContext.SOURCE_TYPE_FORMELE);
			formelementRow.setValue(ctrlDs.nameToIndex("type2"), null);
			formelementRow.setValue(ctrlDs.nameToIndex("imgtype"), LuiPageContext.SOURCE_TYPE_FORMELE);
			ctrlDs.addRow(formelementRow);
		}
	}

	private boolean eleidInCtrlDs(Dataset ctrlDs, String eleid) {
		Row[] rows = ctrlDs.getCurrentPageData().getRows();
		if (rows != null && rows.length > 0) {
			int idIndex = ctrlDs.nameToIndex("id");
			for (Row row : rows) {
				String id = row.getString(idIndex);
				if (StringUtils.equals(id, eleid))
					return true;
			}
		}
		return false;
	}

	// 移掉data区无效的表格行
	private void removeInValidRowFromCtrlDs(Dataset ctrlDs, FormComp editForm) {
		Row[] rows = ctrlDs.getCurrentPageData().getRows();
		if (rows != null && rows.length > 0) {
			int idIndex = ctrlDs.nameToIndex("id");
			int typeIndex = ctrlDs.nameToIndex("type");
			for (Row row : rows) {
				String id = row.getString(idIndex);
				if (!idInForm(editForm, id)) {
					String type = row.getString(typeIndex);
					if (StringUtils.equals(type, LuiPageContext.SOURCE_TYPE_FORMELE)) {
						ctrlDs.removeRow(row);
					}
				}

			}
		}
	}

	// ctrlDs的行id值是否跟表格的某列id相等
	private boolean idInForm(FormComp editForm, String id) {
		List<FormElement> formeles = editForm.getElementList();
		if (formeles != null && formeles.size() > 0) {
			for (FormElement ele : formeles) {
				if (StringUtils.equals(id, ele.getId())) {
					return true;
				}
			}
		}
		return false;
	}
}
