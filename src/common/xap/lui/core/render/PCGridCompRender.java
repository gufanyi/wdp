package xap.lui.core.render;

import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import org.apache.commons.lang.StringUtils;
import com.alibaba.fastjson.JSON;
import xap.lui.core.common.LuiRenderContext;
import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.comps.GridColumn;
import xap.lui.core.comps.GridColumnGroup;
import xap.lui.core.comps.GridComp;
import xap.lui.core.comps.IGridColumn;
import xap.lui.core.comps.MenuItem;
import xap.lui.core.comps.MenubarComp;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.Parameter;
import xap.lui.core.event.GridCellEvent;
import xap.lui.core.event.IEventSupport;
import xap.lui.core.layout.UIComponent;
import xap.lui.core.layout.UIConstant;
import xap.lui.core.layout.UIGridComp;
import xap.lui.core.listener.EventSubmitRule;
import xap.lui.core.listener.LuiEventConf;
import xap.lui.core.model.AppContext;
import xap.lui.core.model.AppSession;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.model.LuiPageContext;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.core.tags.GridModelUtil;

/**
 * @author renxh 表格渲染器
 * @param <T>
 * @param <K>
 */
@SuppressWarnings("unchecked")
public class PCGridCompRender extends UINormalComponentRender<UIGridComp, GridComp> {

	private static final String GRIDCOLUMN_VISIBLE_SCRIPT = "gridcolumn_visible_script";
	private static final String GRIDCOLUMN_EDITABLE_SCRIPT = "gridcolumn_editable_script";
	// private static final String GRIDCOLUMN_EDITABLE_INDEX =
	// "gridcolumn_editable_index";
	private static final String GRIDCOLUMN_PRECISION_INDEX = "gridcolumn_precision_index";
	private static final String GRIDCOLUMN_BGCOLOR_SCRIPT = "gridcolumn_bgcolor_script_";
	private static final String GRIDCOLUMN_TEXTCOLOR_SCRIPT = "gridcolumn_textcolor_script_";

	public PCGridCompRender(GridComp webEle) {
		super(webEle);
	}

	public String createBody() {
		GridComp grid = this.getWebElement();

		UIGridComp uiComp = this.getUiElement();

		// 如果没有设置默认扩展列，则以最后一列为扩展列
		if (uiComp.getAutoExpand().equals(UIConstant.TRUE))
			setAutoExpand(grid);

		Dataset ds = this.getDataset();

		StringBuilder buf = new StringBuilder();

		String widget = WIDGET_PRE + this.getCurrWidget().getId();
		buf.append("var ").append(widget).append(" = pageUI.getViewPart('" + this.getCurrWidget().getId() + "');\n");

		String gridId = getVarId();
		buf.append("var ").append(gridId);

		buf.append(" = $(\"#" + getDivId() + "\").grid({");

		buf.append("id:\"" + grid.getId()).append("\",left:\"0\",top:\"0\",width:\"100%\",height:\"100%\",position:\"relative\",");
		buf.append("editable:" + grid.isEdit()).append(",");
		buf.append("isShowTip:" + grid.isShowTip()).append(",");
		buf.append("isAllowMouseoverChange:" + grid.isAllowMouseoverChange()).append(",");
		buf.append("isMultiSelWithBox:" + grid.isMultiple()).append(",");
		buf.append("isShowNumCol:" + grid.isShowNum()).append(",");
		if(StringUtils.isNotBlank(grid.getHeaderPosition())) {
			buf.append("headerPosition:'").append(grid.getHeaderPosition()).append("',");
		}
		buf.append("isShowSumRow:" + grid.isShowTotalRow()).append(",attr:");
		buf.append("{");
		if (ds.getPageSize() != -1)
			buf.append("pageSize:").append(ds.getPageSize()).append(",");
		buf.append("flowmode:" + isFlowMode());
		// 是否运行态
		boolean isRunMode = false;
		if (LuiRuntimeContext.getWebContext().getPageWebSession().getOriginalParameter("eclipse") == null) {
			isRunMode = !LuiRuntimeContext.isEditMode();
		}
		buf.append(",isRunMode:" + isRunMode);
		buf.append(",isSimplePagination:" + grid.isSimplePagination());
		buf.append(",isShowImageBtn:" + grid.isShowToolbar());
		buf.append(",autoRowHeight:" + grid.isFitRowHeight());
		buf.append(",canCopy:" + grid.isCopy());
		if (isRunMode && grid.getToolbarRender() != null && grid.getToolbarRender().trim().length() > 0) {
			buf.append(",selfDefImageBtnRender:" + grid.getToolbarRender());
		}
		if (grid.getGridDescContents() != null && grid.getGridDescContents().length > 0) {
			String str = JSON.toJSONString(grid.getGridDescContents());

			// String descArray =
			// LuiJsonSerializer.getInstance().toJSON(grid.getGridDescContents());
			buf.append(",descArray:" + str);
		}
		buf.append("},");
		if (grid.getGroupColumns() == null || grid.getGroupColumns().equals(""))
			buf.append("groupHeaderIds:null");
		else {
			buf.append("groupHeaderIds:'" + grid.getGroupColumns() + "'");
		}
		buf.append(",sortable:").append(grid.isSortable()).append(",");
		// , , , , , , , , , , , , , , , , , ) {

		String className = uiComp.getClassName();
		buf.append("className:\"");
		buf.append(className == null ? "" : className).append("\",");
		buf.append("isPagenationTop:" + grid.isPageTop()).append(",");
		buf.append("showColInfo:" + grid.isShowColMenu()).append(",");
		buf.append("oddType:'" + grid.getOddType()).append("',");
		if (grid.getGroupColumns() != null && grid.getGroupColumns().length() > 0)
			buf.append("isGroupWithCheckbox:" + true);
		else {
			buf.append("isGroupWithCheckbox:" + false);
		}

		buf.append(",isShowHeader:").append(grid.isShowHeader()).append(",");
		buf.append("extendCellEditor:" + grid.getExtendCellEditor()).append(",");
		// 获取当前多语编号
		String currentLanguageCode = null;// String.valueOf(MultiLangContext.getInstance().getCurrentLangVO().getLangseq());
		if (currentLanguageCode == null) {
			currentLanguageCode = "1";
		}
		buf.append("rowRender:" + grid.getRowRender()).append(",currentLanguageCode:'" + currentLanguageCode + "'}).grid(\"instance\");\n");

		buf.append(gridId + ".viewpart=" + widget + ";\n");
		buf.append(widget + ".addComponent(" + gridId + ");\n");

		// 设置行高
		String rowHeight = grid.getRowHeight();
		if (rowHeight != null && rowHeight != "")
			buf.append(gridId + ".setRowHeight(" + rowHeight + ");\n");
		// 设置表头行高
		String headerRowHeight = grid.getHeaderRowHeight();
		if (headerRowHeight != null && headerRowHeight != "")
			buf.append(gridId + ".setHeaderRowHeight(" + headerRowHeight + ");\n");

		String modelStr = GridModelUtil.generateGridModel(ds, grid, getCurrWidget());
		buf.append(modelStr);
		buf.append(gridId + ".setModel(" + GridModelUtil.GRID_MODEL_TMP + ");\n");
		if (grid.isExpandTree()) {
			buf.append("setTimeout(function(){" + gridId + ".expandAllNodes('" + grid.getId() + "');},500);\n");
			// buf.append(gridId + ".expandAllNodes();\n");
		}
		// 添加右键菜单
		MenubarComp menubar = grid.getMenuBar();
		if (menubar != null) {
			List<MenuItem> menuItemList = menubar.getMenuList();
			if (menuItemList != null && menuItemList.size() > 0) {
				for (MenuItem item : menuItemList) {
					String menuId = getVarId() + item.getId();
					// 提示信息
					String tip = item.getTip() == null ? translate(item.getI18nName(), item.getText(), item.getLangDir()) : translate(item.getTipI18nName(), item.getTip(), item.getLangDir());
					String displayHotKey = item.getDisplayHotKey();
					if (displayHotKey != null && !"".equals(displayHotKey)) {
						tip += "(" + displayHotKey + ")";
					}

					buf.append("var " + menuId + " = ");
					buf.append(gridId + ".menubarComp");
					buf.append(".addMenu('").append(item.getId()).append("', '");
					buf.append(translate(item.getI18nName(), item.getText(), item.getLangDir())).append("','").append(tip).append("', ");
					if (item.getImgIcon() != null) {
						buf.append("'").append(item.getRealImgIcon()).append("', ");
					} else
						buf.append("null, ");
					buf.append(item.isCheckBoxGroup()).append(", ");
					buf.append(item.isSelected()).append(", ");
					buf.append("{imgIconOn:").append(item.getImgIconOn() == null ? "''" : "'" + item.getRealImgIconOn() + "'");
					buf.append(",imgIconDisable:").append(item.getImgIconDisable() == null ? "''" : "'" + item.getRealImgIconDisable() + "'");
					buf.append("}").append(");\n");
					// 事件
					generateGridMenuItemEvent(buf, item, menuId);

					// 为子项设置快捷键
					String hotKey = item.getHotKey();
					String modifier = String.valueOf(item.getModifiers());
					if (hotKey != null && !"".equals(hotKey)) {
						buf.append(menuId).append(".setHotKey(\"").append(hotKey + modifier).append("\");\n");
					}

					if (!item.isEnabled()) {
						buf.append(menuId + ".setActive(false);\n");
					}
					if (!item.isVisible()) {
						buf.append(menuId + ".hide();\n");
					}
					buf.append(menuId).append(".ctxChanged = false;\n");
				}
			}
		}
		return buf.toString();
	}

	protected void generateGridMenuItemEvent(StringBuilder buf, MenuItem item, String menuId) {

		// buf.append(this.addEventSupport(item, this.getWidget(), menuId,
		// getVarId()));
		LuiEventConf event = item.getEventConfList().get(0);

		buf.append(menuId + ".element.on('menuitemonclick',function(e) {\n");
		buf.append("var proxy = $.serverproxy.getObj({async:true});\n proxy.addParam('el', '2');\n");
		buf.append("proxy.addParam('source_id', '" + this.id + "');\n");

		buf.append("proxy.addParam('event_name', 'onclick');\n");
		buf.append("proxy.addParam('m_n', '" + event.getMethod() + "');\n");
		buf.append("proxy.addParam('widget_id', '" + this.viewId + "');\n");

		EventSubmitRule submitRule = event.getSubmitRule();
		if (submitRule != null) {
			// 提交规则
			String ruleId = "sr_" + event.getName();
			buf.append("var ").append(ruleId).append(" = $.submitrule.getObj();\n");
			if (submitRule.getParamMap() != null && submitRule.getParamMap().size() > 0) {
				Iterator<Entry<String, Parameter>> it = submitRule.getParamMap().entrySet().iterator();
				while (it.hasNext()) {
					Entry<String, Parameter> entry = it.next();
					buf.append(ruleId).append(".addParam('").append(entry.getKey()).append("', '").append(entry.getValue().getValue()).append("');\n");
				}
			}
			if (submitRule.isCardSubmit()) {
				buf.append(ruleId).append(".setCardSubmit(true);\n");
			}

			String jsScript = createJsSubmitRule(submitRule, ruleId);
			buf.append(jsScript);

			EventSubmitRule pSubmitRule = submitRule.getParentSubmitRule();
			if (pSubmitRule != null) {
				String pRuleId = ruleId + "_parent";
				buf.append("var " + pRuleId + " = $.submitrule.getObj();\n");
				String pJsScript = createJsSubmitRule(pSubmitRule, pRuleId);
				buf.append(pJsScript);
				buf.append(ruleId + ".setParentSubmitRule(" + pRuleId + ");\n");
			}

			buf.append("proxy.setSubmitRule(" + ruleId + ");\n");
		}
		buf.append("proxy.execute()");
		buf.append("});\n");
	}

	protected void setAutoExpand(GridComp grid) {
		Iterator<IGridColumn> it = grid.getColumnList().iterator();
		GridColumn lastColumn = null;
		while (it.hasNext()) {
			IGridColumn col = it.next();
			if (col instanceof GridColumn) {
				if (((GridColumn) col).isVisible()) {
					lastColumn = (GridColumn) col;
					if (lastColumn.isFitWidth()) {
						break;
					}
				}
			}
			// 最后一个，并且没有autoexpand
			if (!it.hasNext()) {
				if (lastColumn != null)
					lastColumn.setFitWidth(true);
			}
		}
	}

	protected String getSourceType(IEventSupport ele) {
		return LuiPageContext.SOURCE_TYPE_GRID;
	}

	protected void addProxyParam(StringBuilder buf, String eventName) {
		if (GridCellEvent.AFTER_EDIT.equals(eventName)) {
			buf.append("proxy.addParam('rowIndex', gridCellEvent.rowIndex);\n");
			buf.append("proxy.addParam('colIndex', gridCellEvent.colIndex);\n");
			buf.append("proxy.addParam('newValue', gridCellEvent.newValue);\n");
			buf.append("proxy.addParam('oldValue', gridCellEvent.oldValue);\n");
		} else if (GridCellEvent.BEFORE_EDIT.equals(eventName)) {
			buf.append("proxy.addParam('rowIndex', gridCellEvent.rowIndex);\n");
			buf.append("proxy.addParam('colIndex', gridCellEvent.colIndex);\n");
		} else if (GridCellEvent.ON_CELL_CLICK.equals(eventName) || GridCellEvent.CELL_EDIT.equals(eventName)) {
			buf.append("proxy.addParam('rowIndex', gridCellEvent.rowIndex);\n");
			buf.append("proxy.addParam('colIndex', gridCellEvent.colIndex);\n");
		} else if (GridCellEvent.CELL_VALUE_CHANGED.equals(eventName)) {
			buf.append("proxy.addParam('rowIndex', gridCellEvent.rowIndex);\n");
			buf.append("proxy.addParam('colIndex', gridCellEvent.colIndex);\n");
		}
	}

//	// 删除表格的列，表头触发
//	public void removeColumn(GridColumn obj) {
//		if (obj instanceof GridColumn) { // 表格列属性
//			GridColumn column = (GridColumn) obj;
//			GridComp grid = column.getGridComp();
//			StringBuilder buf = new StringBuilder();
//			String widget = grid.getWidget() != null ? grid.getWidget().getId() : this.getViewId();
//			buf.append("window.execDynamicScript2RemoveGridColumn('" + widget + "','" + grid.getId() + "','" + (column.getField() != null ? column.getField() : column.getId()) + "');");
//			this.deleteColumn(column);
//			addDynamicScript(buf.toString());
//		}
//	}
	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void addColumn(IGridColumn column) {
		
		ViewPartMeta widget = this.getWebElement().getWidget();//LuiRenderContext.current().getPagePartMeta().getWidget(this.getViewId());
		String script = this.addGridColumnScript(widget, (GridComp) this.getWebElement(), column);
		//this.addDynamicScript(script);
		this.addBeforeExeScript(script);
	}
	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void addColumns(List<IGridColumn> columns) {
		ViewPartMeta widget = LuiRenderContext.current().getPagePartMeta().getWidget(this.getViewId());
		String script = this.addGridColumnsScript(widget, (GridComp) this.getWebElement(), columns);
		//this.addDynamicScript(script);
		this.addBeforeExeScript(script);
	}
	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void deleteColumn(IGridColumn column) {
		ViewPartMeta widget = LuiRenderContext.current().getPagePartMeta().getWidget(this.getViewId());
		String script = this.delGridColumnScript(widget, (GridComp) this.getWebElement(), column);
		//this.addDynamicScript(script);
		this.addBeforeExeScript(script);
	}
	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void deleteColumns(List<IGridColumn> columns) {
		ViewPartMeta widget = LuiRenderContext.current().getPagePartMeta().getWidget(this.getViewId());
		String script = this.delGridColumnsScript(widget, (GridComp) this.getWebElement(), columns);
		this.addBeforeExeScript(script);
		//this.addDynamicScript(script);
	}

	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void setColumnEditable(IGridColumn column) {
		ViewPartMeta widget = LuiRenderContext.current().getPagePartMeta().getWidget(this.getViewId());
		this.columnEditableScript(widget, (GridComp) this.getWebElement(), (GridColumn) column);
	}
	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void setColumnVisible(IGridColumn column) {
		ViewPartMeta widget = LuiRenderContext.current().getPagePartMeta().getWidget(this.getViewId());
		this.columnVisibleScript(widget, (GridComp) this.getWebElement(), (GridColumn) column);
	}
	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void setColumnPrecision(IGridColumn column) {
		ViewPartMeta widget = LuiRenderContext.current().getPagePartMeta().getWidget(this.getViewId());
		this.columnPrecisionScript(widget, (GridComp) this.getWebElement(), (GridColumn) column);
	}
	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void setColumnText(IGridColumn column) {
		ViewPartMeta widget = LuiRenderContext.current().getPagePartMeta().getWidget(this.getViewId());
		this.columnTextScript(widget, (GridComp) this.getWebElement(), (GridColumn) column);
	}
	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void setGridTipContent(String gridTipContent) {
		ViewPartMeta widget = LuiRenderContext.current().getPagePartMeta().getWidget(this.getViewId());
		String script = this.addGridTipContentScript(widget, (GridComp) this.getWebElement(), gridTipContent);
		this.addDynamicScript(script);
	}
	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void setGridDescContent(String[] gridDescContent) {
		ViewPartMeta widget = LuiRenderContext.current().getPagePartMeta().getWidget(this.getViewId());
		String script = this.addGridDescContentScript(widget, (GridComp) this.getWebElement(), gridDescContent);
		this.addDynamicScript(script);
	}
	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void setShowImageBtn(boolean show) {
		StringBuilder buf = new StringBuilder();
		buf.append("var gridcomp = pageUI.getViewPart('" + this.getViewId() + "').getComponent('" + this.getWebElement().getId() + "');\n");
		buf.append("gridcomp.setHeaderBtnVisible(" + show + ");\n");
		this.addDynamicScript(buf.toString());
	}
	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void setColumnBgColor(IGridColumn column) {
		ViewPartMeta widget = LuiRenderContext.current().getPagePartMeta().getWidget(this.getViewId());
		columnBgcolorScript(widget, (GridComp) this.getWebElement(), (GridColumn) column);
	}
	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void setColumnTextColor(IGridColumn column) {
		ViewPartMeta widget = LuiRenderContext.current().getPagePartMeta().getWidget(this.getViewId());
		columnTextColorScript(widget, (GridComp) this.getWebElement(), (GridColumn) column);
	}
	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void setMatchValues(String matchValues) {
		StringBuilder buf = new StringBuilder();
		buf.append("var currForm = pageUI.getViewPart('" + this.viewId + "').getComponent('" + this.getId() + "');\n");
		buf.append("currForm.showComp.setMatchValues('").append(matchValues + "');\n");
		addDynamicScript(buf.toString());
	}
	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void setBeforeOpenParam(String beforeOpenParam) {
		StringBuilder buf = new StringBuilder();
		buf.append("var currForm = pageUI.getViewPart('" + this.viewId + "').getComponent('" + this.getId() + "');\n");
		buf.append("currForm.showComp.beforeOpenParam('").append(beforeOpenParam + "');\n");
		addDynamicScript(buf.toString());
	}
	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void hideErrorMsg() {
		addDynamicScript("$.grid.hideErrorMsg('" + this.viewId + "', '" + this.getId() + "');\n");
	}

	/**
	 * add by zuopf 2012.09.17
	 * 
	 * @param widget
	 * @param grid
	 * @param column
	 */
	private void columnTextColorScript(ViewPartMeta widget, GridComp grid, GridColumn column) {
		AppContext appCxt = AppSession.current().getAppContext();

		String scriptId = GRIDCOLUMN_TEXTCOLOR_SCRIPT + grid.getId();
		if (appCxt.getExecScript() != null) {
			for (int i = 0; i < appCxt.getExecScript().size(); i++) {
				if (appCxt.getExecScript().get(i).startsWith("/*" + scriptId + "*/")) {
					appCxt.removeExecScript(i);
					break;
				}
				;
			}
		}

		String script = "";
		String textcolorScript = (String) appCxt.getAppAttribute(scriptId);
		if (textcolorScript == null || textcolorScript.equals("")) {
			textcolorScript = "[]";
		}

		// 清空之前设置过的属性
		// bgcolorScript = bgcolorScript.replace(",\"" + (column.getField() ==
		// null ? column.getField() : column.getId())+ ":"
		// +column.getColumBgColor()+"\"", "");
		// bgcolorScript = bgcolorScript.replace("\"" + (column.getField() ==
		// null ? column.getField() : column.getId())+ ":"
		// +column.getColumBgColor()+"\"", "");

		// 该列没有被设置过颜色
		if (column.getTextColor() != null) {
			textcolorScript = textcolorScript.replace("]", ",\"" + (column.getField() == null ? column.getField() : column.getId()) + ":" + column.getTextColor() + "\"]");
		}
		textcolorScript = textcolorScript.replace("[,", "[");

		script = "/*" + scriptId + "*/pageUI.getViewPart('" + grid.getWidget().getId() + "').getComponent('" + grid.getId() + "').setColumnTextcolor(" + textcolorScript + ");\n";

		appCxt.addAppAttribute(scriptId, textcolorScript);
		appCxt.addExecScript(script);
	}

	/**
	 * zuopf 2012.9.13 add 设置columnBgcolor
	 * 
	 * @param widget
	 * @param grid
	 * @param column
	 */
	private void columnBgcolorScript(ViewPartMeta widget, GridComp grid, GridColumn column) {
		AppContext appCxt = AppSession.current().getAppContext();

		String scriptId = GRIDCOLUMN_BGCOLOR_SCRIPT + grid.getId();
		if (appCxt.getExecScript() != null) {
			for (int i = 0; i < appCxt.getExecScript().size(); i++) {
				if (appCxt.getExecScript().get(i).startsWith("/*" + scriptId + "*/")) {
					appCxt.removeExecScript(i);
					break;
				}
				;
			}
		}

		String script = "";
		String bgcolorScript = (String) appCxt.getAppAttribute(scriptId);
		if (bgcolorScript == null || bgcolorScript.equals("")) {
			bgcolorScript = "[]";
		}

		// 清空之前设置过的属性
		// bgcolorScript = bgcolorScript.replace(",\"" + (column.getField() ==
		// null ? column.getField() : column.getId())+ ":"
		// +column.getColumBgColor()+"\"", "");
		// bgcolorScript = bgcolorScript.replace("\"" + (column.getField() ==
		// null ? column.getField() : column.getId())+ ":"
		// +column.getColumBgColor()+"\"", "");

		// 该列没有被设置过颜色
		if (column.getColumBgColor() != null) {
			bgcolorScript = bgcolorScript.replace("]", ",\"" + (column.getField() == null ? column.getField() : column.getId()) + ":" + column.getColumBgColor() + "\"]");
		}
		bgcolorScript = bgcolorScript.replace("[,", "[");

		script = "/*" + scriptId + "*/pageUI.getViewPart('" + grid.getWidget().getId() + "').getComponent('" + grid.getId() + "').setColumnBgcolor(" + bgcolorScript + ")";

		appCxt.addAppAttribute(scriptId, bgcolorScript);
		appCxt.addExecScript(script);
	}

	private String addGridTipContentScript(ViewPartMeta widget, GridComp grid, String gridTipContent) {
		StringBuilder buf = new StringBuilder();
		buf.append("var gridcomp = pageUI.getViewPart('" + widget.getId() + "').getComponent('" + grid.getId() + "');\n");
		buf.append("gridcomp.setGridTipContent('" + (gridTipContent != null ? gridTipContent : "") + "')");
		return buf.toString();
	}

	private String addGridDescContentScript(ViewPartMeta widget, GridComp grid, String[] gridDescContents) {
		StringBuilder buf = new StringBuilder();
		if (gridDescContents != null && gridDescContents.length > 0) {
			buf.append("var gridcomp = pageUI.getViewPart('" + widget.getId() + "').getComponent('" + grid.getId() + "');\n");
			String str = JSON.toJSONString(gridDescContents);
			buf.append("gridcomp.setGridDescContent(" + str + ");");
			// buf.append("gridcomp.setGridDescContent("+LuiJsonSerializer.getInstance().toJSON(gridDescContents)+");");
		}
		return buf.toString();
	}

	/**
	 * 动态增加column
	 * 
	 * @param widget
	 * @param grid
	 * @param column
	 * @return
	 */
	private String addGridColumnScript(ViewPartMeta widget, GridComp grid, IGridColumn column) {
		StringBuilder buf = new StringBuilder();
		buf.append("var gridcomp = pageUI.getViewPart('" + widget.getId() + "').getComponent('" + grid.getId() + "');\n");
		buf.append("var grid_model_tmp = gridcomp.model;\n");
		Dataset ds = widget.getViewModels().getDataset(grid.getDataset());
		buf.append(GridModelUtil.generateGridColumn(column, ds, widget, "grid_model_tmp"));
		// buf.append("model.initBasicHeaders();\n");
		buf.append("grid_model_tmp.setDataSet(grid_model_tmp.dataset);\n");
		buf.append("gridcomp.setModel(grid_model_tmp);\n");
		buf.append("gridcomp.compsInited=false;$.grid.initEditCompsForGrid(gridcomp);\n");
		return buf.toString();
	}

	/**
	 * 动态增加columns
	 * 
	 * @param widget
	 * @param grid
	 * @param column
	 * @return
	 */
	private String addGridColumnsScript(ViewPartMeta widget, GridComp grid, List<IGridColumn> columns) {
		StringBuilder buf = new StringBuilder();
		buf.append("var gridcomp = pageUI.getViewPart('" + widget.getId() + "').getComponent('" + grid.getId() + "');\n");
		buf.append("var grid_model_tmp = gridcomp.model;\n");
		Dataset ds = widget.getViewModels().getDataset(grid.getDataset());
		for (IGridColumn col : columns)
			buf.append(GridModelUtil.generateGridColumn(col, ds, widget, "grid_model_tmp"));
		// buf.append("model.initBasicHeaders();\n");
		buf.append("grid_model_tmp.setDataSet(grid_model_tmp.dataset);\n");
		buf.append("gridcomp.setModel(grid_model_tmp);\n");
		buf.append("gridcomp.compsInited=false;$.grid.initEditCompsForGrid(gridcomp);\n");
		return buf.toString();
	}

	/**
	 * 动态删除column
	 * 
	 * @param widget
	 * @param grid
	 * @param column
	 * @return
	 */
	private String delGridColumnScript(ViewPartMeta widget, GridComp grid, IGridColumn column) {
		StringBuilder buf = new StringBuilder();
		buf.append("var gridcomp = pageUI.getViewPart('" + widget.getId() + "').getComponent('" + grid.getId() + "');\n");
		buf.append("var grid_model_tmp = gridcomp.model;\n");
		if (column instanceof GridColumn) {
			buf.append("grid_model_tmp.removeHeader('" + ((GridColumn) column).getId() + "');\n");
		} else if (column instanceof GridColumnGroup) {
			buf.append("grid_model_tmp.removeHeader('" + ((GridColumnGroup) column).getId() + "');\n");
			// delGridColumnGroup((GridColumnGroup)column, buf);
		}
		buf.append("grid_model_tmp.initBasicHeaders();\n");
		buf.append("gridcomp.setModel(grid_model_tmp);\n");
		return buf.toString();
	}

	/**
	 * 动态删除columns
	 * 
	 * @param widget
	 * @param grid
	 * @param column
	 * @return
	 */
	private String delGridColumnsScript(ViewPartMeta widget, GridComp grid, List<IGridColumn> columns) {
		StringBuilder buf = new StringBuilder();
		buf.append("var gridcomp = pageUI.getViewPart('" + widget.getId() + "').getComponent('" + grid.getId() + "');\n");
		buf.append("var grid_model_tmp = gridcomp.model;\n");
		for (IGridColumn col : columns) {
			if (col instanceof GridColumn) {
				buf.append("grid_model_tmp.removeHeader('" + ((GridColumn) col).getId() + "');\n");
			} else if (col instanceof GridColumnGroup) {
				buf.append("grid_model_tmp.removeHeader('" + ((GridColumnGroup) col).getId() + "');\n");
//			    delGridColumnGroup((GridColumnGroup)column, buf);
			}
		}
		buf.append("grid_model_tmp.initBasicHeaders();\n");
		buf.append("gridcomp.setModel(grid_model_tmp);\n");
		return buf.toString();
	}

	/**
	 * 设置column显示隐藏
	 * 
	 */
	private void columnVisibleScript(ViewPartMeta widget, GridComp grid, GridColumn column) {
		AppContext appCtx = AppSession.current().getAppContext();

		String scriptId = GRIDCOLUMN_VISIBLE_SCRIPT + grid.getId();

		if (appCtx.getExecScript() != null) {
			for (int i = 0; i < appCtx.getExecScript().size(); i++) {
				if (appCtx.getExecScript().get(i).startsWith("/*" + scriptId + "*/")) {
					appCtx.removeExecScript(i);
					break;
				}
			}
		}
		String script = null;
		String visibleScript = (String) appCtx.getAppAttribute(scriptId);
		if (visibleScript == null || visibleScript.equals("")) {
			visibleScript = "[]";
		}
		// 清空之前的对该列的设置
		visibleScript = visibleScript.replace(",\"" + (column.getField() != null ? column.getField() : column.getId()) + ":" + "true\"", "");
		visibleScript = visibleScript.replace(",\"" + (column.getField() != null ? column.getField() : column.getId()) + ":" + "false\"", "");
		visibleScript = visibleScript.replace("\"" + (column.getField() != null ? column.getField() : column.getId()) + ":" + "true\"", "");
		visibleScript = visibleScript.replace("\"" + (column.getField() != null ? column.getField() : column.getId()) + ":" + "false\"", "");

		if (column.isVisible())
			visibleScript = visibleScript.replace("]", ",\"" + (column.getField() != null ? column.getField() : column.getId()) + ":" + "true\"]");
		else
			visibleScript = visibleScript.replace("]", ",\"" + (column.getField() != null ? column.getField() : column.getId()) + ":" + "false\"]");
		visibleScript = visibleScript.replace("[,", "[");
		script = "/*" + scriptId + "*/pageUI.getViewPart('" + grid.getWidget().getId() + "').getComponent('" + grid.getId() + "').setColumnVisible(" + visibleScript + ")";
		appCtx.addAppAttribute(scriptId, visibleScript);
		appCtx.addExecScript(script);
	}

	/**
	 * 设置column只读
	 * 
	 */
	private void columnEditableScript(ViewPartMeta widget, GridComp grid, GridColumn column) {
		AppContext appCtx = AppSession.current().getAppContext();

		String scriptId = GRIDCOLUMN_EDITABLE_SCRIPT + grid.getId();
		if (appCtx.getExecScript() != null) {
			for (int i = 0; i < appCtx.getExecScript().size(); i++) {
				if (appCtx.getExecScript().get(i).startsWith("/*" + scriptId + "*/")) {
					appCtx.removeExecScript(i);
					break;
				}
			}
		}
		String script = null;
		String editableScript = (String) appCtx.getAppAttribute(scriptId);
		if (editableScript == null || editableScript.equals("")) {
			editableScript = "[]";
		}
		// 清空之前的对该列的设置
		editableScript = editableScript.replace(",\"" + (column.getField() != null ? column.getField() : column.getId()) + ":" + "true\"", "");
		editableScript = editableScript.replace(",\"" + (column.getField() != null ? column.getField() : column.getId()) + ":" + "false\"", "");
		editableScript = editableScript.replace("\"" + (column.getField() != null ? column.getField() : column.getId()) + ":" + "true\"", "");
		editableScript = editableScript.replace("\"" + (column.getField() != null ? column.getField() : column.getId()) + ":" + "false\"", "");

		if (column.isEdit()) {
			editableScript = editableScript.replace("]", ",\"" + (column.getField() != null ? column.getField() : column.getId()) + ":" + "true\"]");
		} else {
			editableScript = editableScript.replace("]", ",\"" + (column.getField() != null ? column.getField() : column.getId()) + ":" + "false\"]");
		}
		editableScript = editableScript.replace("[,", "[");
		script = "/*" + scriptId + "*/pageUI.getViewPart('" + grid.getWidget().getId() + "').getComponent('" + grid.getId() + "').setColumnEditable(" + editableScript + ")";
		appCtx.addAppAttribute(GRIDCOLUMN_EDITABLE_SCRIPT + grid.getId(), editableScript);
		appCtx.addAppAttribute(scriptId, editableScript);
		appCtx.addExecScript(script);
	}

	/**
	 * 设置column精度
	 * 
	 */
	private void columnPrecisionScript(ViewPartMeta widget, GridComp grid, GridColumn column) {
		AppContext appCtx = AppSession.current().getAppContext();
		Integer index = (Integer) appCtx.getAppAttribute(GRIDCOLUMN_PRECISION_INDEX + grid.getId() + "_" + column.getId());

		if (index != null) {
			appCtx.removeExecScript(index);
		}
		String script = "try{ pageUI.getViewPart('" + grid.getWidget().getId() + "').getComponent('" + grid.getId() + "').getBasicHeaderById('" + column.getId() + "').setPrecision("
				+ column.getPrecision() + ") }catch(e){};";
		index = appCtx.addExecScript(script);
		appCtx.addAppAttribute(GRIDCOLUMN_PRECISION_INDEX + grid.getId() + "_" + column.getId(), index);
	}

	/**
	 * 修改column标题
	 * 
	 */
	private void columnTextScript(ViewPartMeta widget, GridComp grid, IGridColumn column) {
		AppContext appCtx = AppSession.current().getAppContext();
		if (column instanceof GridColumnGroup) {
			appCtx.addExecScript("pageUI.getViewPart('" + widget.getId() + "').getComponent('" + grid.getId() + "').getGroupHeaderById('" + column.getId() + "').replaceText('" + column.getText()
					+ "')");
		} else {
			appCtx.addExecScript("pageUI.getViewPart('" + widget.getId() + "').getComponent('" + grid.getId() + "').getBasicHeaderById('" + column.getId() + "').replaceText('" + column.getText()
					+ "')");
		}
	}
	
	public void destroy_layout(){
		String divId = this.getDivId();
		UIComponent uiEle = this.getUiElement();
		
		StringBuilder buf = new StringBuilder();
		if (divId != null) {
			buf.append("window.execDynamicScript2RemoveComponent('" + divId + "','" + uiEle.getViewId() + "','" + uiEle.getId() + "');");
			//this.removeComponent(uiEle.getViewId(), uiEle.getId(), isMenu(uiEle));
		} else {
			buf.append("alert('删除控件失败！未获得divId')");
		}
		addDynamicScript(buf.toString());
	}

}
