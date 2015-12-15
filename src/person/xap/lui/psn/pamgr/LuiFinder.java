package xap.lui.psn.pamgr;
import java.util.Iterator;

import xap.lui.core.comps.FormComp;
import xap.lui.core.comps.FormElement;
import xap.lui.core.comps.GridColumn;
import xap.lui.core.comps.GridComp;
import xap.lui.core.comps.IGridColumn;
import xap.lui.core.comps.LuiElement;
import xap.lui.core.comps.MenuItem;
import xap.lui.core.comps.MenubarComp;
import xap.lui.core.comps.ToolBarComp;
import xap.lui.core.comps.ToolBarItem;
import xap.lui.core.layout.UIElement;
import xap.lui.core.layout.UIElementFinder;
import xap.lui.core.layout.UIGridLayout;
import xap.lui.core.layout.UIGridPanel;
import xap.lui.core.layout.UIPartMeta;
import xap.lui.core.model.PagePartMeta;
public class LuiFinder {
	public static UIElement getGridElement(UIPartMeta uimeta, String uiId, String rowIndex, String colIndex) {
		UIGridLayout grid = (UIGridLayout) UIElementFinder.findElementById(uimeta, uiId);
		UIGridPanel panel = (UIGridPanel) grid.getGridCell(Integer.valueOf(rowIndex), Integer.valueOf(colIndex));
		return panel;
	}
	
	// 获取页面元素
	public static LuiElement getWebElement(PagePartMeta pagemeta, String widgetId, String eleId) {
		LuiElement ele = null;
		if (widgetId != null) {
			if(pagemeta.getWidget(widgetId) == null) return null;
			//TODO    当控件和数据集名称一样的时候出现bug  需要修复
			ele = pagemeta.getWidget(widgetId).getViewComponents().getComponent(eleId);
			if (ele == null) {
				ele = pagemeta.getWidget(widgetId).getViewMenus().getMenuBar(eleId);
				if (ele == null) {
					ele = pagemeta.getWidget(widgetId).getViewModels().getDataset(eleId);// Dataset
					if (ele == null) {
						ele = (LuiElement) pagemeta.getWidget(widgetId).getViewModels().getRefNode(eleId);// RefNode
						if (ele == null) {
							ele = (LuiElement) pagemeta.getWidget(widgetId).getViewModels().getComboData(eleId);// ComboData
						}
					}
				}
			}
		}else if(eleId != null) {
			ele = pagemeta.getWidget(eleId); //ViewPartMeta
			if(ele == null)
				ele = pagemeta;// PagePartMeta
		}
		return ele;
	}
	
	public static LuiElement getWebElement(PagePartMeta pagemeta, String widgetId, String eleId, String subEleId) {
		LuiElement comp = getWebElement(pagemeta, widgetId, eleId);
		if (comp instanceof GridComp) {
			Iterator<IGridColumn> cit = ((GridComp) comp).getColumnList().iterator();
			while (cit.hasNext()) {
				IGridColumn col = cit.next();
				if (col instanceof GridColumn) {
					if (((GridColumn) col).getId().equals(subEleId))
						return (GridColumn) col;
				}
			}
		} else if (comp instanceof FormComp) {
			FormElement formEle = ((FormComp) comp).getElementById(subEleId);
			if (formEle != null)
				return formEle;
		} else if (comp instanceof MenubarComp) {
			MenuItem item = ((MenubarComp) comp).getItem(subEleId);
			if (item != null)
				return item;
		} else if (comp instanceof ToolBarComp) {
			ToolBarItem item = ((ToolBarComp) comp).getElementById(subEleId);
			if (item != null)
				return item;
		}
		return null;
	}
}
