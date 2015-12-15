package xap.lui.core.render;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import xap.lui.core.comps.LuiElement;
import xap.lui.core.event.IEventSupport;
import xap.lui.core.layout.UIElement;
import xap.lui.core.layout.UIGridLayout;
import xap.lui.core.layout.UIGridPanel;
import xap.lui.core.layout.UIGridRowLayout;
import xap.lui.core.layout.UIGridRowPanel;
import xap.lui.core.layout.UILayout;
import xap.lui.core.layout.UILayoutPanel;
import xap.lui.core.logger.LuiLogger;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.model.LuiPageContext;


/**
 * @author renxh 表格布局渲染器
 * @param <T>
 * @param <K>
 */
@SuppressWarnings("unchecked")
public class PCGridLayoutRender extends UILayoutRender<UIGridLayout, LuiElement> {

	// Grid 布局ID基础字符串
	protected static final String GRID_ID_BASE = "grid_layout_";
	
	// 行数
	private int rowcount = 0;
	
	// 列数
	private int colcount = 0;
	
	private String border;
	
	private String className;
	
	// 用来完成GridPanel的计数
	protected int childCount = 0;
	
	public PCGridLayoutRender(UIGridLayout uiEle) {
		super(uiEle);
		UIGridLayout layout = this.getUiElement();
		this.rowcount = layout.getRowcount();
		this.colcount = layout.getColcount();
		this.border = this.getFormatSize(layout.getBorder());
		this.className = layout.getClassName();
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}


	public String create() {
		StringBuffer buf = new StringBuffer();
		UIGridLayout layout = this.getUiElement();
		List<UILayoutPanel> gridRows = layout.getPanelList();
		if (gridRows != null) {
			this.rowcount = gridRows.size();
			for (int i = 0; i < gridRows.size(); i++) {
				UIGridRowPanel rowPanel = (UIGridRowPanel) gridRows.get(i);
				//UIGridRowLayout gridRow = rowPanel.getRow();
				ILuiRender render =rowPanel.getRender();
				if (render != null) {
					buf.append(render.create());
				}
			}
		}
		buf.append(this.createDesignTail());
		return buf.toString();
	}




	@Override
	public String createDesignTail() {
		if (isEditMode()) {
			if(this.isGenEditableTail())
				return "";
			String widgetId = this.getViewId() == null ? "" : this.getViewId();
			String uiid = this.getUiElement() == null ? "" : (String) this.getUiElement().getId();
			String eleid = this.getWebElement() == null ? "" : this.getWebElement().getId();
			String type = this.getRenderType(this.getWebElement());
			if (type == null)
				type = "";
			StringBuffer buf = new StringBuffer();
			if (getDivId() == null) {
				LuiLogger.error("div id is null!" + this.getClass().getName());
			} else {
				buf.append("var params = {");
				buf.append("widgetid : '").append(widgetId).append("'");
				buf.append(",uiid : '").append(uiid).append("'");
				buf.append(",eleid : '").append(eleid).append("'");
				buf.append(",type : '").append(type).append("'");
				buf.append("};\n");
				buf.append("$.design.getObj({divObj:$('#" + getDivId() + "')[0],params:params,objType:'grid_layout'});\n");
				return buf.toString();
			}
			return buf.toString();
		}
		return "";
	}

	@Override
	public String createTail() {
		if (isEditMode()) {
			if(this.isGenEditableTail())
				return "";
			String widgetId = this.getViewId() == null ? "" : this.getViewId();
			String uiid = this.getUiElement() == null ? "" : (String) this.getUiElement().getId();
			String eleid = this.getWebElement() == null ? "" : this.getWebElement().getId();
			String type = this.getRenderType(this.getWebElement());
			if (type == null)
				type = "";
			StringBuffer buf = new StringBuffer();
			if (getDivId() == null) {
				LuiLogger.error("div id is null!" + this.getClass().getName());
			} else {
				buf.append("var params = {");
				buf.append("widgetid : '").append(widgetId).append("'");
				buf.append(",uiid : '").append(uiid).append("'");
				buf.append(",eleid : '").append(eleid).append("'");
				buf.append(",type : '").append(type).append("'");
				buf.append("};\n");
				buf.append("$.design.getObj({divObj:" + getDivId() + "[0],params:params,objType:'grid_layout'});\n");
				return buf.toString();
			}
			return buf.toString();
		}
		return "";
	}


	public int getRowcount() {
		return rowcount;
	}

	public void setRowcount(int rowcount) {
		this.rowcount = rowcount;
	}

	public int getColcount() {
		return colcount;
	}

	public void setColcount(int colcount) {
		this.colcount = colcount;
	}

	public int getChildCount() {
		return childCount;
	}

	public void setChildCount(int childCount) {
		this.childCount = childCount;
	}

	public String getBorder() {
		return border;
	}

	protected String getSourceType(IEventSupport ele) {
		return LuiPageContext.SOURCE_TYPE_GRIDLAYOUT;
	}
	
	private String appendPx(String cssValue){
		if(cssValue.indexOf("px")<0){
			return cssValue+"px";
		}
		return cssValue;
	}

	public String placeHead() {
		StringBuilder buf = new StringBuilder();
		String divId = getDivId();
		buf.append("var ").append(divId).append(" = $('<table>').attr({'id':'").append(divId).append("',\n");
		buf.append("'align':'center'});\n");
		if(null != this.className){
			buf.append(divId).append(".addClass('"+className+"');\n");
		}
		
		UIGridLayout gridLayout = this.getUiElement();
		String border = gridLayout.getBorder();
		if(!isNoneBorder(border)) {
			String cellBorderStyle = gridLayout.getBorderStyle();
			String celBorderColor = gridLayout.getBorderColor();
			String _border = getBorder(border,cellBorderStyle,celBorderColor);
			buf.append(divId).append(".css('border','").append(_border).append("');\n");
		}
		
		buf.append(divId).append(".attr({'cellspacing':'0',");
		buf.append("'cellpadding':'0'});\n");
		getCssStylesScript(buf, divId);
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

	// 添加行
	@Override
	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void addChild(UIElement obj) {
		UILayout layout = getUiElement();
		int size = layout.getPanelList().size();
		UILayoutPanel targetPanel = (UILayoutPanel) obj;
		int index = 0;
		for (int i = 0; i < size; i++) {
			UILayoutPanel panel = layout.getPanelList().get(i);
			if(panel.getId().equals(targetPanel.getId())){
				index = i;
				break;
			}
		}
		
		ILuiRender render = targetPanel.getRender();
		
		StringBuffer buf = new StringBuffer();
		String html = render.place();
		buf.append(html);
		
		buf.append("var div = $('#" + divId + "');\n");
		buf.append("var grid_tbody = div.find('tbody');\n");
		//最后一行的向下增加行
		if (index == (size - 1)) {
			buf.append("grid_tbody.append(" + render.getNewDivId() + ");\n");
		} else {
			buf.append(render.getNewDivId()).append(".insertBefore(grid_tbody.children(':eq(").append(index).append(")'));\n");
		}
		buf.append(render.create());
		buf.append("$(window).triggerHandler('resize');\n");
		addDynamicScript(buf.toString());
	}

	@Override
	public String place() {
		StringBuffer buf = new StringBuffer();
		buf.append(this.placeHead());
		
		// 渲染子节点
		UILayout layout = getUiElement();
		List<UILayoutPanel> pList = layout.getPanelList();
		Iterator<UILayoutPanel> it = pList.iterator();
		while (it.hasNext()) {
			UILayoutPanel panel = it.next();
			ILuiRender render =panel.getRender();
			if (render != null){
				if(render instanceof PCGridRowPanelRender){
					((PCGridRowPanelRender) render).setTableId(getDivId());
				}
				buf.append(render.place());
				if(!(render instanceof PCGridRowPanelRender)){
					buf.append(getDivId()).append(".append(").append(render.getNewDivId()).append(");\n");
				}
			}
		}
		return buf.toString();
	}


	@Override
	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void destroy() {
		UIGridLayout uiEle = this.getUiElement();
		Iterator<UILayoutPanel> rows = uiEle.getPanelList().iterator();
		while (rows.hasNext()) {
			UIGridRowPanel row = (UIGridRowPanel) rows.next();
			row.getRender().destroy();
		}
		StringBuffer buf = new StringBuffer();
		buf.append("$('#" + getDivId() + "').remove();\n");
		addDynamicScript(buf.toString());
	}

	@Override
	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void removeChild( UIElement obj) {
		if (obj != null) {
			if(obj instanceof UIGridRowLayout){
				UIGridRowLayout row = (UIGridRowLayout) obj;
				row.getRender().destroy();
			}else if(obj instanceof UIGridRowPanel){
				UIGridRowPanel rowPanel = (UIGridRowPanel) obj;
				rowPanel.getRender().destroy();
			}
		}
	}
	
	//TODO:此处参数
	public void refreshGridLayout() {
		StringBuffer buf = new StringBuffer();
		UIGridLayout layout = this.getUiElement();
		//刷新行号
		List<UILayoutPanel> afterDelList = layout.getPanelList();
		for(int i=0;i<afterDelList.size();i++){
			if(afterDelList.get(i) instanceof UIGridRowPanel){
				UIGridRowPanel rowPanel = (UIGridRowPanel)afterDelList.get(i);
				UIGridRowLayout rowLayout = (UIGridRowLayout)rowPanel.getElement();
				List<UILayoutPanel> cellList = rowLayout.getPanelList();
				for (int j = 0; j < cellList.size(); j++) {
					UIGridPanel gp = (UIGridPanel)cellList.get(j);
					String cellId = (String)gp.getId();
					buf.append("var ").append(cellId).append(" = $('#" + divId + "').find('td[id$=\""+cellId+"\"]');\n");
					buf.append("if (").append(cellId).append(".size()>0){\n");
					buf.append(cellId).append("[0].rowindex="+i+";\n");
					buf.append(cellId).append("[0].colindex="+j+";\n");
					buf.append("};\n");
				}
			}
		}		
		
//		buf.append("var ta = $('#" + getDivId() + "');\n");
//		buf.append("var ").append(getDivId()).append(" = ta;\n");
//		if (border != null && !border.equals("")) {
//			buf.append(getDivId()).append(".css('border-width','" + border + "');\n");
//		}
//		
//		if (className != null) {
//			buf.append(getDivId()).append(".attr('class','" + className + "');\n");
//		}
		
		buf.append("$(window).triggerHandler('resize');\n");
		addDynamicScript(buf.toString());
	}

	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void setBorder() {
		StringBuilder buf = new StringBuilder();
		UIGridLayout layout = this.getUiElement();
		String border = layout.getBorder();
		String borderStyle = layout.getBorderStyle();
		String borderColor = layout.getBorderColor();
		buf.append("$('#").append(getDivId()).append("').css('border','").append(this.getBorder(border,borderStyle,borderColor)).append("');");
		addDynamicScript(buf.toString());
	}
	
	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void setCellBorder() {
		UIGridLayout gridLayout = this.getUiElement();
		String cellBorder = gridLayout.getCellBorder();
		int rowCount = gridLayout.getPanelList().size();
		String cellBorderStyle = gridLayout.getCellBorderStyle();
		String celBorderColor = gridLayout.getCellBorderColor();
		String _border = getBorder(cellBorder,cellBorderStyle,celBorderColor);
		for(int i=0;i<rowCount;i++) {
			UIGridRowPanel gridRowPanel = (UIGridRowPanel)gridLayout.getPanelList().get(i);
			UIGridRowLayout gridRowLayout = gridRowPanel.getRow();
			int colCount = gridRowLayout.getPanelList().size();
			for(int j=0;j<colCount;j++) {
				UIGridPanel gridCell = (UIGridPanel)gridRowLayout.getPanelList().get(j);
				if(gridCell!=null) {
					if(rowCount-1 == i) {
						if(colCount-1 != j) {
							gridCell.setRightBorder(_border);
						}
					} else if(colCount-1 == j) {
						if(rowCount-1 != i) {
							gridCell.setBottomBorder(_border);
						}
					} else {
						gridCell.setRightBorder(_border);
						gridCell.setBottomBorder(_border);
					}
				}
			}
		}
	}
		

	
	@Override
	public String getNewDivId() {
		return this.getDivId();
	}
}
