package xap.lui.core.render;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;

import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.comps.MenuItem;
import xap.lui.core.comps.MenubarComp;
import xap.lui.core.comps.PropertyGridComp;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.layout.UIPropertyGridComp;
import xap.lui.core.tags.GridModelUtil;

public class PCPropertyGridCompRender extends PCGridCompRender {

	public PCPropertyGridCompRender(PropertyGridComp webEle) {
		super(webEle);
	}
	
	public String createBody() {
		PropertyGridComp grid = (PropertyGridComp) this.getWebElement();
		UIPropertyGridComp uiComp = (UIPropertyGridComp) this.getUiElement();

		StringBuilder buf = new StringBuilder();
		Dataset ds = grid.getPropertyDataset();
		createPropDatasetScript(buf,ds);

		String widget = WIDGET_PRE + this.getCurrWidget().getId();
		buf.append("var ").append(widget).append(" = pageUI.getViewPart('" + this.getCurrWidget().getId() + "');\n");

		String gridId = getVarId();
		buf.append("var ").append(gridId);

		buf.append(" = $(\"#" + getDivId() + "\").propertygrid({");

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
			buf.append(",descArray:" + str);
		}
		buf.append("},");
		if (grid.getGroupColumns() == null || grid.getGroupColumns().equals(""))
			buf.append("groupHeaderIds:null");
		else {
			buf.append("groupHeaderIds:'" + grid.getGroupColumns() + "'");
		}
		buf.append(",sortable:").append(grid.isSortable()).append(",");

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
		buf.append("rowRender:" + grid.getRowRender()).append(",currentLanguageCode:'" + currentLanguageCode + "'}).propertygrid(\"instance\");\n");

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
		//if (grid.isExpandTree()) {
			buf.append("setTimeout(function(){" + gridId + ".expandAllNodes('" + grid.getId() + "');},500);\n");
			// buf.append(gridId + ".expandAllNodes();\n");
		//}
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
	
	private void createPropDatasetScript(StringBuilder buf,Dataset propDataset) {
		buf.append(this.getCurrWidget().getViewModels().addDataset(propDataset,true));
	}
	

}
