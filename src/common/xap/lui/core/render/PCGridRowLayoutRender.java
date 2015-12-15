package xap.lui.core.render;

import java.util.Iterator;
import java.util.List;

import xap.lui.core.comps.LuiElement;
import xap.lui.core.event.IEventSupport;
import xap.lui.core.layout.UIElement;
import xap.lui.core.layout.UIGridRowLayout;
import xap.lui.core.layout.UILayoutPanel;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.model.LuiPageContext;

@SuppressWarnings("unchecked")
public class PCGridRowLayoutRender extends UILayoutRender<UIGridRowLayout, LuiElement> {

	private String tableId = null;

	public PCGridRowLayoutRender(UIGridRowLayout uiEle , PCGridLayoutRender parentRender) {
		super(uiEle);
		this.parentRender = parentRender;
		UIGridRowLayout gridRow = this.getUiElement();
		this.rowHeight = gridRow.getRowHeight();

	}

	protected static final String GRID_ID_ROW = "grid_row_";
	private String rowHeight;

	public String getRowHeight() {
		return rowHeight;
	}

	public void setRowHeight(String rowHeight) {
		this.rowHeight = rowHeight;
	}

	protected String getSourceType(IEventSupport ele) {
		return LuiPageContext.SOURCE_TYPE_GRIDROW;
	}
	
	@Override
	public String placeSelf() {
		StringBuffer buf = new StringBuffer();
		if (tableId != null) {
			buf.append("var ").append(getDivId()).append(" = $('<tr>').appendTo(").append(tableId).append(").attr({'id':'").append(getDivId()).append("',\n");
			if (this.getRowHeight() != null) {
				buf.append("'isHeight':'1'});\n");
				buf.append(getDivId()).append(".css('height','" + this.rowHeight + "');\n");
			} else {
				buf.append("'isHeight':'0'});\n");
			}
		} else {
			buf.append("var ").append(getDivId()).append(" = $('<tr>').attr({'id':'").append(getDivId()).append("',\n");
			if (this.getRowHeight() != null) {
				buf.append("'isHeight':'1'});\n");
				buf.append(getDivId()).append(".css('height','" + this.rowHeight + "');\n");
			} else {
				buf.append("'isHeight':'0'});\n");
			}
		}
		return buf.toString();
	}

	@Override
	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void destroy() {
		UIGridRowLayout row = this.getUiElement();
		Iterator<UILayoutPanel> cells = row.getPanelList().iterator();
		while (cells.hasNext()) {
			UILayoutPanel cell = cells.next();
			cell.getRender().destroy();
		}
		StringBuffer buf = new StringBuffer();
		buf.append("$('#" + getDivId() + "').remove();\n");
		addDynamicScript(buf.toString());
	}

	@Override
	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void removeChild(UIElement obj) {
		if (obj != null) {
			UILayoutPanel cell = (UILayoutPanel) obj;
			cell.getRender().destroy();
		}

	}

	@Override
	public String createTail() {
		return "";
	}

	

	/**
	 * 
	 * @return
	 */
	public String getNewDivId() {
		return this.getDivId();
	}

	public String getTableId() {
		return tableId;
	}

	public void setTableId(String tableId) {
		this.tableId = tableId;
	}

}
