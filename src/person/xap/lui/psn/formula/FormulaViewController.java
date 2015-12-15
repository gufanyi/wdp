package xap.lui.psn.formula;

import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import xap.lui.core.builder.LuiSet;
import xap.lui.core.cache.PaCache;
import xap.lui.core.common.ContextResourceUtil;
import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.common.LuiWebSession;
import xap.lui.core.comps.ButtonComp;
import xap.lui.core.comps.ContextMenuComp;
import xap.lui.core.comps.FormComp;
import xap.lui.core.comps.FormElement;
import xap.lui.core.comps.GridColumn;
import xap.lui.core.comps.GridColumnGroup;
import xap.lui.core.comps.GridComp;
import xap.lui.core.comps.IGridColumn;
import xap.lui.core.comps.MenuItem;
import xap.lui.core.comps.MenubarComp;
import xap.lui.core.comps.TextAreaComp;
import xap.lui.core.comps.ToolBarComp;
import xap.lui.core.comps.ToolBarItem;
import xap.lui.core.comps.WebComp;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.Row;
import xap.lui.core.event.DatasetEvent;
import xap.lui.core.event.DialogEvent;
import xap.lui.core.event.GridRowEvent;
import xap.lui.core.event.MouseEvent;
import xap.lui.core.exception.LuiParseException;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.listener.LuiEventConf;
import xap.lui.core.logger.LuiLogger;
import xap.lui.core.model.AppSession;
import xap.lui.core.model.DataModels;
import xap.lui.core.model.LuiAppUtil;
import xap.lui.core.model.LuiPageContext;
import xap.lui.core.model.LuiPlugoutCmd;
import xap.lui.core.model.PagePartMeta;
import xap.lui.core.model.ViewPartComps;
import xap.lui.core.model.ViewPartContext;
import xap.lui.core.model.ViewPartMenus;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.core.xml.StringUtils;
import xap.lui.psn.formula.ctrl.CtrlFunc;
import xap.lui.psn.formula.ctrl.CtrlType;
import xap.lui.psn.formula.ctrl.Func;
import xap.lui.psn.refence.FormulaPageModel;

/**
 * 公式编辑器弹出窗口
 * 
 */
@SuppressWarnings("rawtypes")
public class FormulaViewController{
	private static final String FILE_PATH = "/lui/nodes/formula/ctrlfunction.xml";
	private static final String EDIT_TEXTAREA = "edit_textarea";
	private static final String STATUS_TEXTAREA = "status_textarea";
	
	//ok
	private static final String DS_MIDDLE = "ds_middle";
	private static final String DS_EVENT = "ds_event";
	private static final String DATASET = "Dataset";
	private static final String ID = "id";
	private static final String TYPE = "type";
	private static final String WRITE_FIELDS = "writeFields";
	private static final String EDITOR = "editor";
	private static final String REF_OK_PLUGOUT = "refOkPlugout";
	
	private static final String ds_ID = "id";
	private static final String NAME = "name";
	private static final String DESC = "description";
	private static final String PID = "pid";
	private static final String DEMO = "demo";
	private static final String GRID="grid";
	private static final String DS = "ds";
	
	protected Map<String, String> params = new HashMap<String, String>();
	protected ViewPartMeta sourceWidget = null;
	private PagePartMeta sourceWin = null;
	
	public void initParams(){
		LuiWebSession session = LuiRuntimeContext.getWebContext().getPageWebSession();
		this.params = session.getOriginalParamMap();
		String wherefrom = this.params.get("wherefrom");
		if(StringUtils.equals("designer",wherefrom)){ //如果参数来源是设计器
			//源window
			this.sourceWin = PaCache.getEditorPagePartMeta();
			//源widget
			this.sourceWidget = PaCache.getEditorViewPartMeta();
		}else{
			String pageId =this.params.get("sourceWinId");
			String viewId = this.params.get("sourceView");
			sourceWin = LuiRuntimeContext.getWebContext().getOriginalPageMeta(pageId);
			sourceWidget = sourceWin.getWidget(viewId);
		}
	}
	
	/**
	 * 加载公式数据
	 * 
	 * @param dialogEvent
	 */
	public void onBeforeShow(DialogEvent dialogEvent) {
		initParams();
		ViewPartMeta view = getFormulaEditView();
		String formulaStr = getOldFormulaString();
		TextAreaComp textArea = getEditTextArea(view, EDIT_TEXTAREA);
		textArea.setValue(formulaStr);
		
		initFunctionData(view);
	}
	
	private String getOldFormulaString() {
		initParams();
		String eleid = this.params.get("eleid");
		String type = this.params.get("type");
		String subeleId = this.params.get("subeleid");
		String dsId4Form = "";

		WebComp comp = null;
		if (sourceWidget != null) {
			if (subeleId != null) {
				WebComp pComp = sourceWidget.getViewComponents().getComponent(eleid);
				if (type != null && ("form_element".equals(type) || "grid_header".equals(type))) {
					if (pComp instanceof FormComp) {
						FormComp form = (FormComp) pComp;
						dsId4Form = form.getDataset();
						comp = form.getElementById(subeleId);
					}
					if (pComp instanceof GridComp) {
						GridComp grid = (GridComp) pComp;
						comp = grid;
						dsId4Form = grid.getDataset();
						// comp = (WebComp) grid.getColumnById(subeleId);
					}
				}
			} else {
				comp = sourceWidget.getViewComponents().getComponent(eleid);
			}
		}
		String formulaOldStr = ""; // 原始值
		if (comp != null) {
			String formulatype = this.params.get("formulatype");
			LuiEventConf eventConf = null;
			if (StringUtils.equals("ref_ext2", formulatype)) {
				eventConf = getEventConf(comp, "validate_method");
			} else if (StringUtils.equals("ref_ext3", formulatype)) {
				eventConf = getEventConf(comp, "editor_method");
			}else{
				Map<String, Object> map = getEditorEventName(sourceWidget, comp);
				String eventName = (String) map.get("eventName");
				comp = (WebComp) map.get("comp");
				eventConf = comp.getEventConf(eventName, null);
			}
			if (eventConf != null) {
				formulaOldStr = eventConf.getScript().trim();
//				formulaOldStr = JsURLEncoder.encode(eventConf.getScript(), "UTF-8");
//				if (formulaOldStr.contains("\"")) {
//					formulaOldStr = formulaOldStr.replace("\"", "\\\"");
//				}
			}
		}
		return formulaOldStr;
	}
	private LuiEventConf getEventConf(WebComp comp, String method) {
		LuiEventConf eventConf = comp.getEventConf("valueChanged", method);
		return eventConf;
	}

	protected ViewPartMeta getFormulaEditView() {
		AppSession ctx = AppSession.current();
		ViewPartContext viewctx = ctx.getViewContext();
		ViewPartMeta view = viewctx.getView();
		return view;
	}

	//加载父窗口的所有控件
	public void onDataLoad_ctrl(DatasetEvent e){
		initParams();
		if(this.sourceWidget!=null){
			ViewPartMeta view = getFormulaEditView();
			initCtrlData(view);
		}
	}
	/**
	 * 加载父窗口的所有控件
	 * @param widget
	 */
	private void initCtrlData(ViewPartMeta view) {
		Dataset ctrlds = view.getViewModels().getDataset("ctrlds");
		int idIndex = ctrlds.nameToIndex("id");
		int nameIndex = ctrlds.nameToIndex("name");
		int descIndex = ctrlds.nameToIndex("description");
		int demoIndex = ctrlds.nameToIndex("demo");
		int pidIndex = ctrlds.nameToIndex("pid");
		if(sourceWidget == null)
			return;
		ViewPartComps viewPartComps = sourceWidget.getViewComponents();
		WebComp[] comps = viewPartComps.getComps();
		if(comps != null && comps.length > 0){
			for(WebComp comp : comps){
				Row row = ctrlds.getEmptyRow();
				row.setValue(idIndex, comp.getId());
				row.setValue(nameIndex, comp.getClass().getSimpleName());
				ctrlds.addRow(row);
				addFuncsRow(ctrlds, comp.getId(), comp.getClass().getSimpleName(), null);
				if(comp instanceof FormComp){
					String pEleId = comp.getId();
					FormComp form = (FormComp)comp;
					List<FormElement> elementList = form.getElementList();
					for (FormElement element : elementList) {
						Row row2 = ctrlds.getEmptyRow();
						row2.setValue(idIndex, element.getId());
						row2.setValue(nameIndex, "FormElement");
						row2.setValue(pidIndex, pEleId);
						ctrlds.addRow(row2);
						addFuncsRow(ctrlds, element.getId(), "FormElement", pEleId);
					}
				}else if(comp instanceof GridComp){
					GridComp grid = (GridComp)comp;
					List<IGridColumn> gridColumnList = grid.getColumnList();
					loadGridColumn(gridColumnList, ctrlds, comp.getId());
				}else if(comp instanceof ToolBarComp){
					String pEleId = comp.getId();
					ToolBarComp toolbar = (ToolBarComp)comp;
					List<ToolBarItem> items = toolbar.getElementList();
					if(items != null && items.size() > 0){
						for(ToolBarItem item : items){
							Row row2 = ctrlds.getEmptyRow();
							row2.setValue(idIndex, item.getId());
							row2.setValue(nameIndex, "ToolBarItem");
							row2.setValue(pidIndex, pEleId);
							ctrlds.addRow(row2);
							addFuncsRow(ctrlds, item.getId(), "ToolBarItem", pEleId);
						}
					}
				}
			}
		}
		//菜单
		ViewPartMenus viewPartMenus = sourceWidget.getViewMenus();
		if(viewPartMenus != null){
			LuiSet<MenubarComp> menubars = viewPartMenus.getMenuBarComp();
			if(menubars != null && menubars.size() > 0){
				for(MenubarComp menu : menubars){
					Row row = ctrlds.getEmptyRow();
					row.setValue(idIndex, menu.getId());
					row.setValue(nameIndex, menu.getClass().getSimpleName());
					ctrlds.addRow(row);
					addFuncsRow(ctrlds, menu.getId(), "MenubarComp", null);
					List<MenuItem> menuItems = menu.getMenuList();
					if(CollectionUtils.isNotEmpty(menuItems)){
						for(MenuItem menuItem : menuItems){
							Row row2 = ctrlds.getEmptyRow();
							row2.setValue(idIndex, menuItem.getId());
							row2.setValue(nameIndex, "MenuItem");
							row2.setValue(pidIndex, menu.getId());
							ctrlds.addRow(row2);
							String pEleId = menu.getId();
							addFuncsRow(ctrlds, menuItem.getId(), "MenuItem", pEleId);
						}
					}
				}
			}
			LuiSet<ContextMenuComp> ctxMenuComps = viewPartMenus.getContextMenuComp();
			if(menubars != null && menubars.size() > 0){
				for(ContextMenuComp ctxMenuComp : ctxMenuComps){
					Row row = ctrlds.getEmptyRow();
					row.setValue(idIndex, ctxMenuComp.getId());
					row.setValue(nameIndex, ctxMenuComp.getClass().getSimpleName());
					ctrlds.addRow(row);
					addFuncsRow(ctrlds, ctxMenuComp.getId(), "ContextMenuComp", null);
					List<MenuItem> menuItems = ctxMenuComp.getItemList();
					if(CollectionUtils.isNotEmpty(menuItems)){
						for(MenuItem menuItem : menuItems){
							Row row2 = ctrlds.getEmptyRow();
							row2.setValue(idIndex, menuItem.getId());
							row2.setValue(nameIndex, "MenuItem");
							row2.setValue(pidIndex, ctxMenuComp.getId());
							ctrlds.addRow(row2);
							String pEleId = ctxMenuComp.getId();
							addFuncsRow(ctrlds, menuItem.getId(), "MenuItem_ctx", pEleId);
						}
					}
				}
			}
		}
	}
	private void addFuncsRow(Dataset ctrlds, String eleId, String compType, String pEleId) {
		int idIndex = ctrlds.nameToIndex("id");
		int nameIndex = ctrlds.nameToIndex("name");
		int descIndex = ctrlds.nameToIndex("description");
		int demoIndex = ctrlds.nameToIndex("demo");
		int pidIndex = ctrlds.nameToIndex("pid");
		List<CtrlType> ctrlTypes = getCtrlTypes();
		if (ctrlTypes != null && ctrlTypes.size() > 0) {
			for (CtrlType ctrlType : ctrlTypes) {
				if (StringUtils.equals(compType, ctrlType.getId())) {
					List<Func> funcs = ctrlType.getFuncList();
					if(funcs != null && funcs.size() > 0){
						for(Func func : funcs){
							Row row3 = ctrlds.getEmptyRow();
							row3.setValue(idIndex, func.getId());
							row3.setValue(nameIndex, func.getName());
							row3.setValue(descIndex, func.getDesc());
							row3.setValue(pidIndex, eleId);
							Map<String, String> prms = new HashMap<String, String>();
							prms.put("eleId", eleId);
							if(StringUtils.isNotBlank(pEleId))
								prms.put("pEleId", pEleId);
							String strdemo = getTempString(func.getDemo(), prms);
							row3.setValue(demoIndex, strdemo.trim());
							ctrlds.addRow(row3);
						}
					}
				}
			}
		}
	}
	private void loadGridColumn(List<IGridColumn> gridColumnList, Dataset ctrlds, String compId){
		int idIndex = ctrlds.nameToIndex("id");
		int nameIndex = ctrlds.nameToIndex("name");
		int pidIndex = ctrlds.nameToIndex("pid");
		for(IGridColumn gridColumn : gridColumnList){
			if(gridColumn instanceof GridColumn){
				Row row2 = ctrlds.getEmptyRow();
				row2.setValue(idIndex, gridColumn.getId());
				row2.setValue(nameIndex, "GridColumn");
				row2.setValue(pidIndex, compId);
				ctrlds.addRow(row2);
				addFuncsRow(ctrlds, gridColumn.getId(), "GridColumn", compId);
			}else{
				List<IGridColumn> chIGridColumns = ((GridColumnGroup)gridColumn).getChildColumnList();
				loadGridColumn(chIGridColumns, ctrlds, compId);
			}
		}
	}
	
	protected TextAreaComp getEditTextArea(ViewPartMeta view,String compId) {
		TextAreaComp textArea = (TextAreaComp) view.getViewComponents().getComponent(compId);
		return textArea;
	}
	
	private String getTempString(String tmpStr,Map<String, String> params){
		try {
			freemarker.template.Template  tmplate=new freemarker.template.Template("1111", new StringReader(tmpStr));
			final Writer out = new StringWriter();
			tmplate.process(params, out);
			out.flush();
			return out.toString();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return "";
	}
	private List<CtrlType> getCtrlTypes() {
		CtrlFunc ctrlFunc = fetchCtrlFunc();
		List<CtrlType> ctrlTypes = ctrlFunc.getCtrltypeList();
		return ctrlTypes;
	}
	public static CtrlFunc fetchCtrlFunc() {
		CtrlFunc ctrlFunc = null;
		InputStream input = null;
		try {
			input = ContextResourceUtil.getResourceAsStream(FILE_PATH);
			if (input == null) {
				throw new LuiParseException("从路径:" + FILE_PATH + ",没有找到xml文件！");
			}
			ctrlFunc = CtrlFunc.parse(input);
			if (ctrlFunc == null) {
				throw new LuiParseException("获取CtrlFunc时出错!");
			}
		} catch (Exception e) {
			throw new LuiRuntimeException(e.getMessage());
		} finally {
			try {
				if (input != null) {
					input.close();
				}
			} catch (Exception e) {
				LuiLogger.error(e.getMessage(), e);
			}
		}
		return ctrlFunc;
	}
	
	private void initFunctionData(ViewPartMeta widget) {
		FormulaXml formulaXmlRoot = FormulaPageModel.fetchFormulaXmlRoot();
		List<Category> categories = formulaXmlRoot.getCategoryList();
		if(categories != null && categories.size()>0){
			ViewPartMeta formulaView = LuiAppUtil.getCntView();
			DataModels datamodels = formulaView.getViewModels();
			for(Category category : categories){
				List<Item> items = category.getItemList();
				if(items != null && items.size() > 0){
					Dataset ds = datamodels.getDataset(category.getId() + DS);
					if(ds != null){
						int idIndex = ds.nameToIndex(ds_ID);
						int nameIndex = ds.nameToIndex(NAME);
						int descIndex = ds.nameToIndex(DESC);
						int demoIndex = ds.nameToIndex(DEMO);
						int pidIndex = ds.nameToIndex(PID);
						if(StringUtils.equals(category.getId(),"valiformula")){//若为正则表达式
							String funchead = "String var = \"待验证的字符串\";\nboolean b = Pattern.matches(\"";//"String var = "123@qq.com";boolean b = Pattern.matches(\"b*g\",var);";
							String functail = "\", var);\nif(!b)\n  return \"格式错误！\";\n else\n return \"\";";
							for(Item item : items){
								Row row = ds.getEmptyRow();
								row.setValue(idIndex, item.getId());
								row.setValue(nameIndex, item.getName());
								row.setValue(descIndex, item.getDesc());
								String demo = (item.getDemo().trim()).replace("\\", "\\\\");
								row.setValue(demoIndex, funchead + demo + functail);
								ds.addRow(row);
//								List<Item> chItems = item.getChildItemList();
//								if(chItems != null && chItems.size() > 0){
//									for(Item ite : chItems){
//										Row row2 = ds.getEmptyRow();
//										row2.setValue(idIndex, ite.getId());
//										row2.setValue(nameIndex, ite.getName());
//										row2.setValue(descIndex, ite.getDesc());
//										row2.setValue(demoIndex, ite.getDemo());
//										row2.setValue(pidIndex, item.getId());
//										ds.addRow(row2);
//									}
//								}
							}
						}else{
							for(Item item : items){
								Row row = ds.getEmptyRow();
								row.setValue(idIndex, item.getId());
								row.setValue(nameIndex, item.getName());
								row.setValue(descIndex, item.getDesc());
								row.setValue(demoIndex, item.getDemo());
								ds.addRow(row);
								List<Item> chItems = item.getChildItemList();
								if(chItems != null && chItems.size() > 0){
									for(Item ite : chItems){
										Row row2 = ds.getEmptyRow();
										row2.setValue(idIndex, ite.getId());
										row2.setValue(nameIndex, ite.getName());
										row2.setValue(descIndex, ite.getDesc());
										row2.setValue(demoIndex, ite.getDemo().trim()+"\n");
										row2.setValue(pidIndex, item.getId());
										ds.addRow(row2);
									}
								}
							}
						}
					}
				}
			}
		}
	}

	
	//双击添加到文本域
	public void onFormulaDbClick2(GridRowEvent e){
		GridComp grid = e.getSource();
		Dataset ds = LuiAppUtil.getDataset(grid.getDataset());
		Row sRow = ds.getSelectedRow();
		
		String formula = "";
		if(sRow != null){
			formula = sRow.getString(ds.nameToIndex("demo"));
		}
		setEditAreaValue(formula);
	}
	
	//确定
	public void onOkEvent(MouseEvent<ButtonComp> e) {
		this.initParams();
		LuiWebSession session = LuiRuntimeContext.getWebContext().getPageWebSession();
		String callBackId = session.getOriginalParameter("nodeId");
		ViewPartMeta view = getFormulaEditView();
		TextAreaComp textArea = getEditTextArea(view, EDIT_TEXTAREA);
		String cssStr = textArea.getValue();
//		String viewWherefrom = this.params.get("wherefrom");
//		ViewPartMeta editView = null;
//		if(StringUtils.equals(viewWherefrom, "designer")){
//			editView = PaCache.getEditorViewPartMeta();
//		}else{
//			//其他来源处理
//		}
		if(sourceWidget == null)
			return;
		WebComp comp = null;
		if(StringUtils.equals(callBackId,"refnode_mvel")){//从事件的mvel脚本传过来的
			Map<String, Object> map = getEditorEventName(sourceWidget, comp);
			String eventName = (String) map.get("eventName");
			comp = (WebComp) map.get("comp");
			LuiEventConf eventConf = comp.getEventConf(eventName, null);
			eventConf.setScript(cssStr);
			
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put(TYPE, DATASET);
			paramMap.put(ID, DS_EVENT);
			Map<String, String> writeFields = new HashMap<String, String>();
			writeFields.put(callBackId, cssStr);
			paramMap.put(WRITE_FIELDS, writeFields);
			LuiPlugoutCmd uifPluOutCmd = new LuiPlugoutCmd(EDITOR, REF_OK_PLUGOUT, paramMap);
			uifPluOutCmd.execute();
		}else if(StringUtils.equals(callBackId,"ref_ext2") || StringUtils.equals(callBackId,"ref_ext3")){
			//将文本域的值存入内存模型
			String eleId = session.getOriginalParameter("eleid");
			String type = session.getOriginalParameter("type");
			String subeleId = session.getOriginalParameter("subeleid");
			String formulatype = session.getOriginalParameter("formulatype");
			comp = getEditorComp(sourceWidget, comp, eleId, type, subeleId);
			
			if(StringUtils.equals("ref_ext2", formulatype)){
				setEventConfig(comp, "validate_method", cssStr);
			}else if(StringUtils.equals("ref_ext3", formulatype)){
				setEventConfig(comp, "editor_method", cssStr);
			}
			
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put(TYPE, DATASET);
			paramMap.put(ID, DS_MIDDLE);
			Map<String, String> writeFields = new HashMap<String, String>();
			writeFields.put(callBackId, cssStr);
			paramMap.put(WRITE_FIELDS, writeFields);
			LuiPlugoutCmd uifPluOutCmd = new LuiPlugoutCmd(EDITOR, REF_OK_PLUGOUT, paramMap);
			uifPluOutCmd.execute();
		}else{//其他来源
			String aceptType = session.getOriginalParameter("acceptType");
			String aceptId = session.getOriginalParameter("acceptId");
			
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put(TYPE, aceptType);
			paramMap.put(ID, aceptId);
			Map<String, String> writeFields = new HashMap<String, String>();
			writeFields.put(callBackId, cssStr);
			paramMap.put(WRITE_FIELDS, writeFields);
			LuiPlugoutCmd uifPluOutCmd = new LuiPlugoutCmd(EDITOR, REF_OK_PLUGOUT, paramMap);
			uifPluOutCmd.execute();
		}
		AppSession.current().getAppContext().closeWinDialog();
	}

	private Map<String, Object> getEditorEventName(ViewPartMeta editView, WebComp comp) {
		Dataset dsEvent = AppSession.current().getAppContext().getWindowContext("pa").getViewContext("settings").getView().getViewModels().getDataset("ds_event");
		Row selRow = dsEvent.getSelectedRow();
		if(selRow != null){
			//将文本域的值存入内存模型
			String eleId = selRow.getString(dsEvent.nameToIndex("eleId"));
			String type = selRow.getString(dsEvent.nameToIndex("type"));
			String subeleId = selRow.getString(dsEvent.nameToIndex("subEleId"));
			comp = getEditorComp(editView, comp, eleId, type, subeleId);
			String pid = selRow.getString(dsEvent.nameToIndex("pid"));
			if(pid != null){
				int idIndex = dsEvent.nameToIndex("Id");
				int PidIndex = dsEvent.nameToIndex("Pid");
				Row rows[] = dsEvent.getCurrentPageData().getRows();
				for(Row row : rows){
					String id = row.getString(idIndex);
					if(StringUtils.equals(pid, id)){
						String rpid = row.getString(PidIndex);
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("comp", comp);
						map.put("eventName", rpid);
						return map;
					}
				}
			}
		}
		return null;
	}
	private WebComp getEditorComp(ViewPartMeta editView, WebComp comp, String eleId, String type, String subeleId) {
		if(StringUtils.equals(LuiPageContext.SOURCE_TYPE_MENUBAR, type)){//menubar
			if(StringUtils.isNotBlank(subeleId)){
				MenubarComp webcomp = editView.getViewMenus().getMenuBar(eleId);
				if(StringUtils.equals(LuiPageContext.SOURCE_FORMELEMENT, type)){
					comp = webcomp.getElementById(subeleId);
				}
			}else{
				comp = editView.getViewMenus().getMenuBar(eleId);
			}
		}else if(StringUtils.equals(LuiPageContext.SOURCE_TYPE_CONTEXTMENU, type)){//contextmenu
			if(StringUtils.isNotBlank(subeleId)){
				ContextMenuComp webcomp = editView.getViewMenus().getContextMenu(eleId);
				if(StringUtils.equals(LuiPageContext.SOURCE_FORMELEMENT, type)){
					comp = webcomp.getItem(subeleId);
				}
			}else{
				comp = editView.getViewMenus().getContextMenu(eleId);
			}
		}else{
			if(StringUtils.isNotBlank(subeleId)){
				WebComp webcomp = editView.getViewComponents().getComponent(eleId);
				if(StringUtils.equals(LuiPageContext.SOURCE_FORMELEMENT, type) || "grid_header".equals(type)){
					comp = ((FormComp)webcomp).getElementById(subeleId);
				}
			}else{
				comp = editView.getViewComponents().getComponent(eleId);
			}
		}
		return comp;
	}
	private void setEventConfig(WebComp comp, String method, String cssStr) {
		LuiEventConf eventConf = comp.getEventConf("valueChanged", method);
		if(eventConf==null) {
			eventConf= new LuiEventConf();
			comp.addEventConf(eventConf);
			eventConf.setMethod(method);
		} 
		eventConf.setEventType("TextEvent");
		eventConf.setEventName("valueChanged");
		eventConf.setOnserver(true);
		eventConf.setScript(cssStr);
	}
	
	/**
	 * 取消按钮点击事件，关闭窗口
	 */
	public void onCancelEvent(MouseEvent<ButtonComp> e) {
		AppSession.current().getAppContext().closeWinDialog();
	}
	
	//可供子类调用的方法
	//双击添加到编辑区
	protected void setEditAreaValue(String formula) {
		ViewPartMeta view = getFormulaEditView();
		TextAreaComp areaComp = getEditTextArea(view,EDIT_TEXTAREA );
	    String oldvalue = areaComp.getValue();
	    areaComp.setValue((oldvalue+formula).trim());
//	    areaComp.setFocus(true);
	}
	//单击设置描述内容
	protected void setDescAreaValue(String description) {
		ViewPartMeta view = getFormulaEditView();
		TextAreaComp areaComp = getEditTextArea(view, STATUS_TEXTAREA);
		areaComp.setValue(description);
	}
	
	
	
	
	public void onDateGridDbClick(GridRowEvent gridRowEvent) {

	}

	public void onNo1Click(MouseEvent mouseEvent) {

	}

	public void onMathGridSelect(GridRowEvent gridRowEvent) {

	}

	public void onMathGridDbClick(GridRowEvent gridRowEvent) {

	}

	public void onDateGridSelect(GridRowEvent gridRowEvent) {

	}

	public void onDateGridDbClidk(GridRowEvent gridRowEvent) {

	}

	public void onDatabaseGridSelect(GridRowEvent gridRowEvent) {

	}

	public void onDatabaseDbClick(GridRowEvent gridRowEvent) {

	}

	public void onStringGridSelect(GridRowEvent gridRowEvent) {

	}

	public void onStringGridDbClick(GridRowEvent gridRowEvent) {

	}

	public void onFinanceGridSelect(GridRowEvent gridRowEvent) {

	}

	public void onFinanceGridDbClick(GridRowEvent gridRowEvent) {

	}

	public void onControlformulaGridSelect(GridRowEvent gridRowEvent) {

	}

	public void onControlformulaGridDbClick(GridRowEvent gridRowEvent) {

	}

	public void onCommonGridSelect(GridRowEvent gridRowEvent) {

	}

	public void onCommonGridDbClick(GridRowEvent gridRowEvent) {

	}

	public void onSelfgridSelect(GridRowEvent gridRowEvent) {

	}

	public void onSelfGridDbClick(GridRowEvent gridRowEvent) {

	}

	public void onPropertyGridSelect(GridRowEvent gridRowEvent) {

	}

	public void onDatasetGridSelect(GridRowEvent gridRowEvent) {

	}

	public void onDatasetGridDbClick(GridRowEvent gridRowEvent) {

	}

	public void onNo9Click(MouseEvent mouseEvent) {

	}

	public void onClearBtnClick(MouseEvent mouseEvent) {

	}
	
	
}
