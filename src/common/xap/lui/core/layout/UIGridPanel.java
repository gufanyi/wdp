package xap.lui.core.layout;

import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.render.PCGridPanelRender;
import xap.lui.core.render.PCGridRowLayoutRender;
import xap.lui.core.render.notify.RenderProxy;




/**
 * @author renxh
 * 表格中的cell
 *
 */
public class UIGridPanel extends UILayoutPanel {
	private static final long serialVersionUID = 1L;
	
	public static final String ROWSPAN = "rowSpan";
	public static final String COLSPAN = "colSpan";
	public static final String COLWIDTH = "colWidth";
	public static final String COLHEIGHT = "colHeight";
	public static final String ROWINDEX = "rowIndex";
	public static final String COLINDEX = "colIndex";
	
	private String cellType;
	private String rowSpan;
	private String colSpan;
	private String colWidth;
	private String colHeight;
	private String colIndex;
	private String rowIndex;
	/**
	 * 0  表示第一个点 1 表示第二个点 2 表示第三个点 三个一组
	 * */ 
	public static final String CELLTYPE = "cellType";
	
	private UIGridRowLayout parent;
	
	
//	private UIGridPanel preCell; // 前一个兄弟节点
//	private UIGridPanel nextCell; // 后一个兄弟节点
	
	
	public void setCellType(String cellType){
		this.cellType=cellType;
		if(LifeCyclePhase.ajax.equals(getPhase())) {
			this.getRender().setCellType(cellType);
		}
	}
	
	public String getCellType(){
		return cellType;
	}
	
	public UIGridRowLayout getParent() {
		return parent;
	}
	public void setParent(UIGridRowLayout parent) {
		this.parent = parent;
	}
	public String getRowSpan() {
		return rowSpan;
	}
	public void setRowSpan(String rowSpan) {
		this.rowSpan=rowSpan;
		if(LifeCyclePhase.ajax.equals(getPhase())) {
			this.getRender().setRowspan(Integer.parseInt(rowSpan));
		}
	}
	public String getColSpan() {
		return colSpan;
	}
	public void setColSpan(String colSpan) {
	this.colSpan=colSpan;
		if(LifeCyclePhase.ajax.equals(getPhase())) {
			this.getRender().setColspan(Integer.parseInt(colSpan));
		}
	}
	public String getColWidth() {
		return  colWidth;
	}
	public void setColWidth(String colWidth) {
		this.colWidth=colWidth;
		if(LifeCyclePhase.ajax.equals(getPhase())) {
			this.getRender().setPanelWidth(colWidth);
		}
	}
	public String getColHeight() {
		return colHeight;
	}
	public void setColHeight(String colHeight) {
		this.colHeight=colHeight;
		if(LifeCyclePhase.ajax.equals(getPhase())) {
			this.getRender().setPanelHeight(colHeight);
		}
	}
	public String getColIndex(){
		return colIndex;
	}
	public void setColIndex(String colIndex){
		this.colIndex=colIndex;
	}
	public String getRowIndex(){
		return rowIndex;
	}
	public void setRowIndex(String rowIndex){
		this.rowIndex=rowIndex;
	}

	@Override
	public PCGridPanelRender getRender() {
		if(render==null){
			render = RenderProxy.getRender(new PCGridPanelRender(this,(PCGridRowLayoutRender)this.getParent().getRender()));
		}
		return (PCGridPanelRender)render;
	}
	
	
	
}
