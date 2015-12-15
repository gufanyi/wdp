package xap.lui.core.layout;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.render.ILuiRender;
import xap.lui.core.render.PCGridLayoutRender;
import xap.lui.core.render.notify.RenderProxy;

public class UIGridLayout extends UILayout {
	private static final long serialVersionUID = 1L;
	public static final String BORDER = "border";
	public static final String ROWCOUNT = "rowcount";
	public static final String COLCOUNT = "colcount";
	public static final String CLASSNAME = "className";
	public static final String BORDERCOLOR = "borderColor";
	public static final String BORDERSTYLE = "borderStyle";
	public static final String CELLBORDER = "cellBorder";
	public static final String CELLBORDERSTYLE = "cellBorderStyle";
	public static final String CELLBORDERCOLOR = "cellBorderColor";
	
	 
	private String borderColor = "#000000";
	private String borderStyle = "solid";
	private int rowcount;
	private int colcount;
	private String border = "0";
	private String className;
	private String cellBorder = "0";
	private String cellBorderStyle = "solid";
	private String cellBorderColor = "#000000";
	
	public String getBorderColor() {
		return borderColor;
	}

	public void setBorderColor(String borderColor) {
		this.borderColor=borderColor;
		if(LifeCyclePhase.ajax.equals(getPhase())) {
			((PCGridLayoutRender)this.getRender()).setBorder();
		}
	}


	public String getBorderStyle() {
		return borderStyle;
	}

	public void setBorderStyle(String borderStyle) {
		this.borderStyle=borderStyle;
		if(LifeCyclePhase.ajax.equals(getPhase())) {
			((PCGridLayoutRender)this.getRender()).setBorder();
		}
	}

	
	public String getCellBorder() {
		return cellBorder;
	}

	public void setCellBorder(String cellBorder) {
		this.cellBorder = cellBorder;
		if(LifeCyclePhase.ajax.equals(getPhase())) {
			((PCGridLayoutRender)this.getRender()).setCellBorder();
		}
	}

	public String getCellBorderStyle() {
		return cellBorderStyle;
	}

	public void setCellBorderStyle(String cellBorderStyle) {
		this.cellBorderStyle = cellBorderStyle;
		if(LifeCyclePhase.ajax.equals(getPhase())) {
			((PCGridLayoutRender)this.getRender()).setCellBorder();
		}
	}

	public String getCellBorderColor() {
		return cellBorderColor;
	}

	public void setCellBorderColor(String cellBorderColor) {
		this.cellBorderColor = cellBorderColor;
		if(LifeCyclePhase.ajax.equals(getPhase())) {
			((PCGridLayoutRender)this.getRender()).setCellBorder();
		}
	}

	public int getRowcount() {
		return rowcount;
	}

	
	public void setRowcount(int rowcount) {
		this.rowcount=rowcount;
	}

	public int getColcount() {
		return colcount;
	}

	public void setColcount(int colcount) {
		this.colcount=colcount;
	}

	public String getBorder() {
		return border;
	}

	public void setBorder(String border) {
		this.border=border;
		if(LifeCyclePhase.ajax.equals(getPhase())) {
			((PCGridLayoutRender)this.getRender()).setBorder();
		}
	}

	public void setClassName(String className){
		this.className=className;
	}
	
	public String getClassName(){
		return className;
	}
	

	public void addGridRow(UIGridRowLayout row) {
		UIGridRowPanel rowPanel = new UIGridRowPanel(row);
		rowPanel.setId(row.getId()+"Panel");
		row.setParent(this);
		addPanel(rowPanel);
	}
	
	public void removeGridRow(UIGridRowLayout row) {
		Iterator<UILayoutPanel> it = this.getPanelList().iterator();
		UILayoutPanel currPanel = null;
		while(it.hasNext()){
			UIGridRowPanel panel = (UIGridRowPanel) it.next();
			UIGridRowLayout layout = panel.getRow();
			if(row.getId().equals(layout.getId())){
				currPanel = panel;
				break;
			}
		}
		if(currPanel != null)
			removePanel(currPanel);
	}

	@Override
	public UIGridLayout doClone() {
		return (UIGridLayout) super.doClone();
	}

	public CellPair getCellPair(UIGridPanel panel) {
		//UIGridRowLayout row = (UIGridRowLayout) panel.getLayout();
		List<UILayoutPanel> list = this.getPanelList();
		int size = list.size();
		for (int i = 0; i < size; i++) {
			UIGridRowPanel rowPanel = (UIGridRowPanel) list.get(i);
			int index = findPanel(rowPanel, panel);
			if(index != -1)
				return new CellPair(i, index);
		}
		return null;
	}

	private int findPanel(UIGridRowPanel rowPanel, UIGridPanel panel) {
		UIGridRowLayout rowLayout = (UIGridRowLayout) rowPanel.getElement();
		List<UILayoutPanel> panelList = rowLayout.getPanelList();
		if(panelList == null)
			return -1;
		int size = panelList.size();
		for (int i = 0; i < size; i++) {
			UILayoutPanel lp = panelList.get(i);
			if(panel.equals(lp))
				return i;
		}
		return -1;
	}

	public class CellPair{
		private int col;
		private int row;
		
		public CellPair(int row, int col){
			this.col = col;
			this.row = row;
		}
		public int getCol() {
			return col;
		}
		public void setCol(int col) {
			this.col = col;
		}
		public int getRow() {
			return row;
		}
		public void setRow(int row) {
			this.row = row;
		}
	}

	public UIElement getGridCell(int rowIndex, int colIndex) {
		for(UILayoutPanel _panel : (List<UILayoutPanel>)this.getPanelList()) {
			UIGridRowLayout rowLayout = ((UIGridRowPanel)_panel).getRow();
			for(UILayoutPanel gridPanel : rowLayout.getPanelList()) {
				UIGridPanel _gridPanel= (UIGridPanel)gridPanel;
				String _rowIndex = _gridPanel.getRowIndex();
				String _colIndex = _gridPanel.getColIndex();
				if(StringUtils.equals(String.valueOf(rowIndex), _rowIndex) 
						&& StringUtils.equals(String.valueOf(colIndex), _colIndex)) {
					return _gridPanel;
				}
			}
			
		}
		return null;
	}
	
	public void refreshGridLayout() {
		if(LifeCyclePhase.ajax.equals(getPhase())) {
			((PCGridLayoutRender)this.getRender()).refreshGridLayout();
		}
	}
	
	@Override
	public ILuiRender getRender() {
		if(render==null){
			render = RenderProxy.getRender(new PCGridLayoutRender(this));
		}
		return render;
	}
}

