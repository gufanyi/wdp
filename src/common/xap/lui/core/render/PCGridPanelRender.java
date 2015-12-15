package xap.lui.core.render;

import java.util.Iterator;

import org.apache.commons.lang.StringUtils;

import xap.lui.core.comps.LuiElement;
import xap.lui.core.event.IEventSupport;
import xap.lui.core.layout.UIGridLayout;
import xap.lui.core.layout.UIGridPanel;
import xap.lui.core.layout.UIGridRowLayout;
import xap.lui.core.layout.UILayoutPanel;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.model.LuiPageContext;


@SuppressWarnings("unchecked")
public class PCGridPanelRender extends UILayoutPanelRender<UIGridPanel, LuiElement> {
	
	private String colHeight;
	private String colWidth;
	private String rowSpan;
	private String colSpan;
	private String cellType = null;
	
	private String leftPadding;
	private String rightPadding;
	private String leftBorder;
	private String rightBorder;
	private String topBorder;
	private String bottomBorder;
	private String border;
	private String topPadding;
	private String bottomPadding;

	public PCGridPanelRender(UIGridPanel uiEle,PCGridRowLayoutRender parentRender) {
		super(uiEle);
		this.parentRender = parentRender;
		this.colWidth = uiEle.getColWidth();
		this.colSpan = uiEle.getColSpan();
		this.rowSpan = uiEle.getRowSpan();
		this.colHeight = uiEle.getColHeight();
		this.cellType = uiEle.getCellType();
		
		this.leftPadding = uiEle.getLeftPadding();
		this.rightPadding = uiEle.getRightPadding();
		this.leftBorder = uiEle.getLeftBorder();
		this.rightBorder = uiEle.getRightBorder();
		this.topBorder = uiEle.getTopBorder();
		this.bottomBorder = uiEle.getBottomBorder();
		this.border = uiEle.getBorder();
		this.topPadding = uiEle.getTopPadding();
		this.bottomPadding = uiEle.getBottomPadding();
	}
	

	public String getColWidth() {
		return colWidth;
	}

	public void setColWidth(String colWidth) {
		this.colWidth = colWidth;
	}

	public String getRowSpan() {
		return rowSpan;
	}

	public void setRowSpan(String rowSpan) {
		this.rowSpan = rowSpan;
	}

	public String getColSpan() {
		return colSpan;
	}

	public void setColSpan(String colSpan) {
		this.colSpan = colSpan;
	}

	protected String getSourceType(IEventSupport ele) {

		return LuiPageContext.SOURCE_TYPE_GRIDPANEL;
	}

	public String getColHeight() {
		return colHeight;
	}

	public void setColHeight(String colHeight) {
		this.colHeight = colHeight;
	}

	public String getLeftPadding() {
		return leftPadding;
	}

	public void setLeftPadding(String leftPadding) {
		this.leftPadding = leftPadding;
	}

	public String getRightPadding() {
		return rightPadding;
	}

	public void setRightPadding(String rightPadding) {
		this.rightPadding = rightPadding;
	}

	public String getLeftBorder() {
		return leftBorder;
	}

	public void setLeftBorder(String leftBorder) {
		this.leftBorder = leftBorder;
	}

	public String getRightBorder() {
		return rightBorder;
	}

	public void setRightBorder(String rightBorder) {
		this.rightBorder = rightBorder;
	}

	public String getTopBorder() {
		return topBorder;
	}

	public void setTopBorder(String topBorder) {
		this.topBorder = topBorder;
	}

	public String getBottomBorder() {
		return bottomBorder;
	}

	public void setBottomBorder(String bottomBorder) {
		this.bottomBorder = bottomBorder;
	}

	public String getBorder() {
		return border;
	}

	public void setBorder(String border) {
		this.border = border;
	}

	public String getTopPadding() {
		return topPadding;
	}

	public void setTopPadding(String topPadding) {
		this.topPadding = topPadding;
	}

	public String getBottomPadding() {
		return bottomPadding;
	}

	public void setBottomPadding(String bottomPadding) {
		this.bottomPadding = bottomPadding;
	}

	

	public String appendPx(String cssValue){
		if(cssValue.indexOf("px")<0){
			return cssValue+"px";
		}
		return cssValue;
	}
	


	@Override
	protected String addEditableListener(String divId, String widgetId, String uiId, String subuiId, String eleId, String subEleId, String type) {
		StringBuilder buf = new StringBuilder();
		ILuiRender gridRender = null;
		UIGridLayout grid = null;
		if(getParentRender()!=null
		  &&getParentRender().getParentRender()!=null
		  ){
			gridRender = getParentRender().getParentRender();
			grid = gridRender.getUiElement();
		}
		//如果为空，从属性中取得，针对在增加列的操作中
		if(grid==null) grid = (UIGridLayout)this.getUiElement().getAttribute("gridlayout");
		UIGridLayout.CellPair cellPair = grid.getCellPair(this.getUiElement());
		buf.append("var params = {");
		buf.append("widgetid:'").append(widgetId).append("'");
		buf.append(",uiid:'").append(gridRender==null?grid.getId():gridRender.getId()).append("'");
		buf.append(",subuiid:'").append(subuiId).append("'");
		buf.append(",eleid:'").append(eleId).append("'");
		buf.append(",subeleid:'").append(subEleId).append("'");
		buf.append(",rowindex:").append(cellPair.getRow());
		buf.append(",colindex:").append(cellPair.getCol());
		buf.append(",type:'").append(type).append("'");
		buf.append("};\n");
		buf.append("$.design.getObj({divObj:$('#" + getDivId() + "')[0],params:params,objType:'grid_panel'});\n");
		buf.append(addDragableListener(getDivId()));
		return buf.toString();
	}

	@Override
	public String placeSelf() {
		StringBuilder buf = new StringBuilder();
		buf.append("var ").append(getDivId()).append(" = $('<td>').attr({'id':'").append(getDivId()).append("',\n");
		if (this.colWidth != null && !this.colWidth.equals("")) {
			buf.append("'width':'").append(colWidth).append("',\n");
			buf.append("'haswidth':'1',\n");
		} else {
			buf.append("'haswidth':'0',\n");
		}
		if (rowSpan != null) {
			buf.append("'rowSpan':'").append(rowSpan).append("',\n");
		}
		if (colSpan != null) {
			buf.append("'colSpan':'").append(colSpan).append("',\n");
		}
		if (cellType != null) {
			buf.append("'celltype':'").append(cellType).append("',\n");
		}
		if (this.colHeight != null && !this.colHeight.equals("")) {
			buf.append("'height':'").append(colHeight).append("',\n");
			buf.append("'hasheight':'1'});\n");
		}else{
			buf.append("'hasheight':'0'});\n");
		}
		
		if(isEditMode()){
			buf.append(getDivId()).append(".addClass('editable');\n");
		}
		getCssStylesScript(buf, getDivId());
		
		UIGridPanel gridPanel = this.getUiElement();
		UIGridLayout gridLayout = gridPanel.getParent().getParent();
		String cellborder = gridLayout.getCellBorder();
	
		String _currRowIndex = gridPanel.getRowIndex();
		String _currColIndex = gridPanel.getColIndex();
		if(!isNoneBorder(cellborder) && StringUtils.isNotBlank(_currRowIndex) && StringUtils.isNotBlank(_currColIndex)) {
			int rowCount = gridLayout.getRowcount();
			int colCount = gridLayout.getColcount();
			int currRowIndex = Integer.parseInt(_currRowIndex);
			int currColIndex = Integer.parseInt(_currColIndex);
			String cellBorderStyle = gridLayout.getCellBorderStyle();
			String celBorderColor = gridLayout.getCellBorderColor();
			String _border = getBorder(cellborder,cellBorderStyle,celBorderColor);
			String _rightBorder = gridPanel.getRightBorder();
			String _bottomBorder = gridPanel.getBottomBorder();
			if(rowCount-1 == currRowIndex) {
				if(isNoneBorder(_rightBorder) && colCount-1 != currColIndex) {
					gridPanel.setRightBorder(_border,true);
				}
			} else if(colCount-1 == currColIndex) {
				if(isNoneBorder(_bottomBorder) && rowCount-1 != currRowIndex) {
					gridPanel.setBottomBorder(_border,true);
				}
			} else {
				if(isNoneBorder(_rightBorder)) {
					gridPanel.setRightBorder(_border,true);
				}
				if(isNoneBorder(_bottomBorder)) {
					gridPanel.setBottomBorder(_border,true);
				}
			}
		}
		this.getBorderScript(buf, getDivId());
		return buf.toString();
	}
	
	private String getBorder(String borderWidth,String borderStyle,String borderColor) {
		if(isNoneBorder(borderWidth)) {
			borderWidth = "0";
		}
		return this.appendPx(borderWidth) + " " + StringUtils.defaultIfEmpty(borderStyle, "solid") + " " + StringUtils.defaultIfEmpty(borderColor,"#000000");
	}
	
	private boolean isNoneBorder(String border) {
		return StringUtils.isBlank(border) || StringUtils.equals(border, "0");
	}


	@Override
	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void destroy() {
		StringBuilder buf = new StringBuilder();
		buf.append("var ").append(getDivId()).append(" = $('#" + getDivId() + "').remove();\n");
		UIGridRowLayout gridRowLayout =  this.getUiElement().getParent();
		Iterator<UILayoutPanel> rows = gridRowLayout.getPanelList().iterator();
		while (rows.hasNext()) {
			UIGridPanel gridPanel = (UIGridPanel) rows.next();
			String cellId = (String)gridPanel.getId();
			buf.append("var ").append(cellId).append(" = $('#__d_" + cellId + "');\n");
			buf.append("if (").append(cellId).append(".size()>0){\n");
			buf.append("var colindex = ").append(cellId).append("[0].colindex;\n");
			buf.append("if (colindex > ").append(getDivId()).append("[0].colindex){\n");
			buf.append(cellId).append("[0].colindex = colindex - 1}};\n");
		}
		
		addDynamicScript(buf.toString());
	}

	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void setPanelWidth(String width) {
		StringBuilder buf = new StringBuilder();
		buf.append("$('#"+getDivId()+"')").append(".attr({'width':'").append(width).append("',");
		buf.append("'haswidth':'1'});\n");
		addDynamicScript(buf.toString());
	}
	
	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void setPanelHeight(String height) {
		StringBuilder buf = new StringBuilder();
		buf.append("$('#"+getDivId()+"')").append(".attr({'height':'").append(height).append("',");
		buf.append("'hasheight':'1'});\n");
		addDynamicScript(buf.toString());
	}
	
	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void setRowspan(int rowSpan) {
		StringBuilder buf = new StringBuilder();
		buf.append("$('#"+getDivId()+"')").append(".attr('rowSpan','").append(rowSpan).append("');\n");
		//对所影响cell作调整
		if(rowSpan < 1) return;
		addDynamicScript(buf.toString());
	}
	
	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void setColspan(int colSpan) {
		StringBuilder buf = new StringBuilder();
		buf.append("$('#"+getDivId()+"')").append(".attr('colSpan','").append(colSpan).append("');\n");
		//对所影响cell作调整
		if(colSpan < 1) return;
		addDynamicScript(buf.toString());
	}

	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void setCellType(String cellType) {
		StringBuilder buf = new StringBuilder();
		buf.append("$('#"+getDivId()+"')").append(".attr('celltype','").append(cellType).append("');\n");
		addDynamicScript(buf.toString());
	}
	
	public String getNewDivId() {
		return this.getDivId();
	}

}
