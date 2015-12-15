package xap.lui.core.util;

import java.util.HashMap;
import java.util.List;

import xap.lui.core.layout.UIGridLayout;
import xap.lui.core.layout.UIGridPanel;
import xap.lui.core.layout.UIGridRowLayout;
import xap.lui.core.layout.UIGridRowPanel;
import xap.lui.core.layout.UILayoutPanel;
import xap.lui.core.logger.LuiLogger;
import xap.lui.core.render.RaCmdConst;
import xap.lui.core.xml.StringUtils;


/**
 * 网格布局辅助类
 * 
 * @author licza
 */
public class GridLayoutHelper {
	
	public static final String MAP_KEY_INDEX = "index";
	public static final String MAP_KEY_PANEL = "panel";
	
	public static final String MAP_KEY_PREINDEX = "preindex";
	public static final String MAP_KEY_PREPANEL = "prepanel";
	
	public static final String MAP_KEY_NEXTINDEX = "nextindex";
	public static final String MAP_KEY_NEXTPANEL = "nextpanel";
	
	public static final String CELL_WIDTH = "120";
	public static final String CELL_HEIGHT = "30";
	
	/**
	 * 根据cell的编码从网格布局中查找cell
	 * @param gridLayout
	 * @param cellid
	 * @return UIGridPanel
	 */
	public static UIGridPanel findCellById(UIGridLayout gridLayout, String cellid){
		List<UILayoutPanel> layoutPanels = gridLayout.getPanelList();
		for(UILayoutPanel layoutPanel : layoutPanels){
			UIGridPanel panel = findCellById((UIGridRowPanel)layoutPanel, cellid);
			if(panel != null)
				return panel;
		}
		return null;
	}
	/**
	 * 
	 * 根据cell的编码从网格行中查找cell
	 * @param gridRowPanel
	 * @param cellid
	 * @return UIGridPanel
	 */
	public static UIGridPanel findCellById(UIGridRowPanel gridRowPanel, String cellid){
		UIGridRowLayout rowLayout = gridRowPanel.getRow();
		List<UILayoutPanel> layoutPanels = rowLayout.getPanelList();
		for(UILayoutPanel layoutPanel : layoutPanels){
			if(cellid.equals(layoutPanel.getId()))
				return (UIGridPanel)layoutPanel;
		}
		return null;
	}
	
	public int getGridColunmCount(){
		return 0;
	}
	
	public int getGridRowCount(){
		return 0;
	}
	
	/**
	 * 检查穿透列
	 * @return
	 */
	public int getCrossColumnSum(){
		int crossColumn = 0;
		/**
		 * 递归每一列.查看上/下列是否有跨行列的影响
		 * 调整受影响的跨行列rowspan.
		 */
		return crossColumn;
	}
	
	public void addRow(UIGridLayout gridLayout, String cellid, boolean before){
		/**
		 * 增加行的过程
		 * 1.找到当前选中Cell.获取rowspan.
		 * 2.计算列数.
		 * 3.检查穿透列.
		 * 4.列数减去穿透列得到真正的列数
		 * 5.根据Cell及Cell的rowspan.执行addRowBefore()操作
		 */
		//UIGridPanel cell = findCellById(gridLayout, cellid);
		//int rowspan = translateSpanNum(cell.getRowSpan());
	}
	
	/**
	 * 检查行内各个单元格是否有跨行
	 * 
	 * @param rowPanel
	 * @return boolean：是否跨行
	 */
	public static boolean hasCellRowSpanInRowPanel(UIGridRowPanel rowPanel){
		UIGridRowLayout rowLayout = (UIGridRowLayout)rowPanel.getElement();
		List<UILayoutPanel> cellList = rowLayout.getPanelList();
		for (int j = 0; j < cellList.size(); j++) {
			UIGridPanel gp = (UIGridPanel)cellList.get(j);
			String rowspanStr = gp.getRowSpan();
			int rowspan = translateSpanNum(rowspanStr);
			if(rowspan>1){
				return true;
			}
		}		
		return false;
	}
	
	/**
	 * 转换span属性
	 * 
	 * @param span
	 * @return int
	 */
	public static int translateSpanNum(String span){
		if(span != null && !span.isEmpty()){
			try{
				return Integer.parseInt(span);
			}catch(Exception e){
				LuiLogger.info("Translate Span to Num Error!");
			}
		}
		return 1;
	}
	
	@SuppressWarnings({"unchecked", "rawtypes"})
	public static HashMap getRowpanelsByCell(UIGridLayout layout,UIGridPanel gridpanel){
		HashMap rowMap = new HashMap();
		List<UILayoutPanel> panelList = layout.getPanelList();
		
		jump:for(int i=0;i<panelList.size();i++){
			if(panelList.get(i) instanceof UIGridRowPanel){
				UIGridRowPanel rowPanel = (UIGridRowPanel)panelList.get(i);
				UIGridRowLayout rowLayout = (UIGridRowLayout)rowPanel.getElement();
				List<UILayoutPanel> cellList = rowLayout.getPanelList();
				for (int j = 0; j < cellList.size(); j++) {
					UIGridPanel gp = (UIGridPanel)cellList.get(j);
					if(gridpanel.getId().equals(gp.getId())){
						rowMap.put(MAP_KEY_INDEX, i);
						rowMap.put(MAP_KEY_PANEL, rowPanel);
						break jump;
					}
				}
			}
		}
		
		int realRowIndex = (Integer) rowMap.get(MAP_KEY_INDEX);
		if(realRowIndex>0){
			UIGridRowPanel preRowPanel = (UIGridRowPanel)panelList.get(realRowIndex-1);
			rowMap.put(MAP_KEY_PREINDEX, realRowIndex-1);
			rowMap.put(MAP_KEY_PREPANEL, preRowPanel);
		}
		
		//根据gridpanel的rowspan，计算当前单元格的下一行的UIGridRowPanel
		String rowspanStr = gridpanel.getRowSpan();
		if(StringUtils.isBlank(rowspanStr)) rowspanStr="1";
		int rowspan = Integer.parseInt(rowspanStr);
		//如果rowspan大于等于1，则计算得到下一行，并放入map中返回
		if(rowspan>=1){
			int nextRealRowIndex = realRowIndex+rowspan;
			if(nextRealRowIndex<panelList.size()){
				UIGridRowPanel nextRowPanel = (UIGridRowPanel)panelList.get(nextRealRowIndex);
				rowMap.put(MAP_KEY_NEXTINDEX, nextRealRowIndex);
				rowMap.put(MAP_KEY_NEXTPANEL, nextRowPanel);
			}
		}
		return rowMap;
	}
	
	public static int getRealColIndexByCell(UIGridLayout layout,UIGridPanel gridpanel){
		List<UILayoutPanel> panelList = layout.getPanelList();
		for(int i=0;i<panelList.size();i++){
			int count = 0;
			if(panelList.get(i) instanceof UIGridRowPanel){
				UIGridRowPanel rowPanel = (UIGridRowPanel)panelList.get(i);
				UIGridRowLayout rowLayout = (UIGridRowLayout)rowPanel.getElement();
				List<UILayoutPanel> cellList = rowLayout.getPanelList();
				for (int j = 0; j < cellList.size(); j++) {
					UIGridPanel gp = (UIGridPanel)cellList.get(j);
					String colspanStr = gp.getColSpan();
					int colspan = translateSpanNum(colspanStr);
					if(gridpanel.getId().equals(gp.getId())){
						return count;
					}else{
						count = count+colspan;
					}
				}
			}
		}
		return -1;
	}
	
	public static UIGridRowPanel getEditRowByCell(UIGridLayout layout,UIGridPanel gridpanel){
		List<UILayoutPanel> panelList = layout.getPanelList();
		for(int i=0;i<panelList.size();i++){
			if(panelList.get(i) instanceof UIGridRowPanel){
				UIGridRowPanel rowPanel = (UIGridRowPanel)panelList.get(i);
				UIGridRowLayout rowLayout = (UIGridRowLayout)rowPanel.getElement();
				List<UILayoutPanel> cellList = rowLayout.getPanelList();
				for (int j = 0; j < cellList.size(); j++) {
					UIGridPanel gp = (UIGridPanel)cellList.get(j);
					if(gridpanel.getId().equals(gp.getId())){
						return rowPanel;
					}
				}
			}
		}
		return null;
	}	
	
	/**
	 * 根据列号得到cell
	 * 
	 * @param rowLayout
	 * @param index
	 * @return UILayoutPanel
	 */
	public static UIGridPanel getRealCellByIndex(UIGridRowLayout rowLayout,int index){
		List<UILayoutPanel> cellList = rowLayout.getPanelList();
		int startIndex = 0;
		for (int i = 0; i < cellList.size(); i++) {
			UIGridPanel gp = (UIGridPanel)cellList.get(i);
			//计算当前的index
			int colspan = translateSpanNum(gp.getColSpan());
			int endIndex = startIndex + colspan;
			
			//检查范围，找到要检查的cell
			if(startIndex <= index && index<endIndex){
				return gp;
			}
			startIndex = endIndex;
		}		
		return null;
	}	
	
	public static int getRealColumnCount(UIGridRowPanel rowPanel){
		UIGridRowLayout rowLayout = (UIGridRowLayout)rowPanel.getElement();
		List<UILayoutPanel> cellList = rowLayout.getPanelList();
		if(cellList == null) return 0;
		return cellList.size();
	}
	
	/**
	 * 新增列的竖切检查
	 * 1：如果向左添加列，则真正地上一个index的所有竖切单元格不能向右合并
	 * 2：如果向右添加列，则真正地下一个index的所有竖切单元格不能向左合并
	 * 
	 * @param gridLayout:grid总layout
	 * @param clickCell:点击的单元格GridPanel
	 * @param direction:向左或向右添加
	 */
	public static boolean checkColumCross(UIGridLayout gridLayout,int realIndex,String direction){
		int currentColumnCount = gridLayout.getColcount();
		List<UILayoutPanel> panelList = gridLayout.getPanelList();
		
		//向左添加的校验
		if(RaCmdConst.GRIDCMD_ADDCOLUMNLEFT.endsWith(direction)){
			//如果是首列
			if(realIndex==0) return true;
			int checkIndex = realIndex-1;
			
			for(int i=0;i<panelList.size();i++){
				if(panelList.get(i) instanceof UIGridRowPanel){
					UIGridRowPanel rowPanel = (UIGridRowPanel)panelList.get(i);
					UIGridRowLayout rowLayout = (UIGridRowLayout)rowPanel.getElement();
					List<UILayoutPanel> cellList = rowLayout.getPanelList();
					int startIndex = 0;
					for (int j = 0; j < cellList.size(); j++) {
						UIGridPanel gp = (UIGridPanel)cellList.get(j);
						//计算当前的index
						int colspan = translateSpanNum(gp.getColSpan());
						int endIndex = startIndex + colspan;
						
						//检查范围，找到要检查的cell
						if(startIndex <= checkIndex && checkIndex<endIndex){
							//判断endIndex是否大于realIndex
							if(endIndex>realIndex){
								return false;
							}
						}
						startIndex = endIndex;
					}
				}
			}
			
		}
		//向右添加的校验
		else if(RaCmdConst.GRIDCMD_ADDCOLUMNRIGHT.endsWith(direction)){
			//如果是最后一列
			if(realIndex == currentColumnCount-1) return true;
			int checkIndex = realIndex+1;
			for(int i=0;i<panelList.size();i++){
				if(panelList.get(i) instanceof UIGridRowPanel){
					UIGridRowPanel rowPanel = (UIGridRowPanel)panelList.get(i);
					UIGridRowLayout rowLayout = (UIGridRowLayout)rowPanel.getElement();
					List<UILayoutPanel> cellList = rowLayout.getPanelList();
					int startIndex = 0;
					for (int j = 0; j < cellList.size(); j++) {
						UIGridPanel gp = (UIGridPanel)cellList.get(j);
						//计算当前的index
						int colspan = translateSpanNum(gp.getColSpan());
						int endIndex = startIndex + colspan;
						
						//检查范围，找到要检查的cell
						if(startIndex <= checkIndex && checkIndex<endIndex){
							//判断startIndex是否小于等于realIndex
							if(startIndex<=realIndex){
								return false;
							}
						}
						startIndex = endIndex;
					}
				}
			}
		}
		//默认返回true
		return true;
	}
	
	/**
	 * 刷新列数
	 * 
	 * @param layout
	 */
	public static void refreshGridLayoutColCount(UIGridLayout layout){
		if(layout.getPanelList()!=null){
			UILayoutPanel rp = (UILayoutPanel)layout.getPanelList().get(0);
			UIGridRowLayout rl = (UIGridRowLayout)rp.getElement();
			if(rl!=null && rl.getPanelList()!=null){
				layout.setColcount(rl.getPanelList().size());
			}
		}
	}
	
	/**
	 * 刷新行数
	 * 
	 * @param layout
	 */
	public static void refreshGridLayoutRowCount(UIGridLayout layout){
		if(layout.getPanelList()!=null){
			int rowCount = layout.getPanelList().size();
			layout.setRowcount(rowCount);
		}
	}
	
	/**
	 * 刷新服务端各个单元格的行列号
	 */
	public static void refreshRowColIndexOnServer(UIGridLayout layout){
		List<UILayoutPanel> afterDelList = layout.getPanelList();
		for(int i=0;i<afterDelList.size();i++){
			if(afterDelList.get(i) instanceof UIGridRowPanel){
				UIGridRowPanel rowPanel = (UIGridRowPanel)afterDelList.get(i);
				UIGridRowLayout rowLayout = (UIGridRowLayout)rowPanel.getElement();
				List<UILayoutPanel> cellList = rowLayout.getPanelList();
				for (int j = 0; j < cellList.size(); j++) {
					UIGridPanel gp = (UIGridPanel)cellList.get(j);
					gp.setRowIndex(String.valueOf(i));
					gp.setColIndex(String.valueOf(j));
				}
			}
		}
	}
	
	/**
	 * 递归获取指定行的上一个非空单元格
	 * 
	 * @param rowLayout
	 * @param index
	 * @return UIGridPanel
	 */
	public static UIGridPanel getNotNullPreCellInSameRowByIndex(UIGridRowLayout rowLayout,int index){
		if(index>=0){
			List<UILayoutPanel> cellList = rowLayout.getPanelList();
			Object obj = cellList.get(index);
			if(obj == null){
				return getNotNullPreCellInSameRowByIndex(rowLayout,index-1);
			}else{
				return (UIGridPanel)obj;
			}
		}else{
			return null;
		}
	}
}
