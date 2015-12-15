package xap.lui.core.render;

import java.util.List;

import xap.lui.core.command.CmdInvoker;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.layout.UIElement;
import xap.lui.core.layout.UIElementFinder;
import xap.lui.core.layout.UIGridLayout;
import xap.lui.core.layout.UIGridPanel;
import xap.lui.core.layout.UIGridRowPanel;
import xap.lui.core.layout.UILayoutPanel;
import xap.lui.core.util.GridLayoutHelper;


/**
 * Grid布局cell扩展命令
 * @author licza
 *
 */
public class ExpansionCmd extends AbstractRaCommand{
	private static final String MSG = "这已经是最外层的单元格了!";
	private static final String MSG_1 = "准备合并的两个单元格不符合合并规则!";
	private static final String MSG_2 = "准备合并的两个单元格不在同一行中!";
	private static final String ERROR_MSG = "UIElement转换UIGridPanel失败!";
	String oper;
	public ExpansionCmd(RaParameter rp ,String oper) {
		super(rp);
		this.oper = oper;
	}

	@Override
	public void execute() {
		//
		String act = oper.substring(9).toLowerCase();
		UIGridPanel uiEle = null;
		uiEle = (UIGridPanel)findGridCell(rp.getUiMeta(), null, new String[]{rp.getUiId(), rp.getSubuiId()});
		UIGridLayout layout = (UIGridLayout) UIElementFinder.findElementById(rp.getUiMeta(), rp.getUiId());
		
		int rowIndex = Integer.parseInt(rp.getRowIndex());
		int colIndex = Integer.parseInt(rp.getColIndex());
		/**
		 * 过程:设置rowspan,向上删除一个单元格
		 */
		if("up".equals(act)){
			rowIndex = rowIndex - 1;
			UIElement targetEle = null;
			jump : while(rowIndex >= 0){
				try{
					UIGridRowPanel row = (UIGridRowPanel)layout.getPanelList().get(rowIndex);
					List<UILayoutPanel> cells = row.getRow().getPanelList();
					for(UILayoutPanel cell : cells){
						if(!(cell instanceof UIGridPanel)){
							throw new LuiRuntimeException(ERROR_MSG);
						}
						if(Integer.parseInt(uiEle.getColIndex()) == Integer.parseInt(((UIGridPanel)cell).getColIndex())){
							//准备合并的两个单元格在同一列中
							targetEle = cell;
							colIndex = cells.indexOf(cell);
							break jump;
						}
					}
				}catch(IndexOutOfBoundsException e){}
				rowIndex = rowIndex - 1;
			}
			if(targetEle == null){
				throw new LuiRuntimeException(MSG);
			}
			//rowspan校验
			String targetRowspan = ((UIGridPanel)targetEle).getRowSpan() != null ? ((UIGridPanel)targetEle).getRowSpan() : "1";
			if(Integer.parseInt(((UIGridPanel)targetEle).getRowIndex()) + Integer.parseInt(targetRowspan) != Integer.parseInt(uiEle.getRowIndex())){
				throw new LuiRuntimeException(MSG_1);
			}
			//colspan校验
			String targetColspan = ((UIGridPanel)targetEle).getColSpan() != null ? ((UIGridPanel)targetEle).getColSpan() : "1";
			String colspan = uiEle.getColSpan() != null ? uiEle.getColSpan() : "1";
			if(Integer.parseInt(targetColspan) != Integer.parseInt(colspan)){
				throw new LuiRuntimeException(MSG_1);
			}
			expansionRowUp(uiEle, (UIGridPanel)targetEle, rowIndex, colIndex);
		}
		/**
		 * 过程:设置rowspan,向下删除一个单元格
		 */
		else if("down".equals(act)){
			String rowspan = uiEle.getRowSpan() != null ? uiEle.getRowSpan():"1";
			rowIndex = rowIndex + Integer.parseInt(rowspan);
			UIElement targetEle = null;
			jump:while(rowIndex < layout.getPanelList().size()){
				try{
					UIGridRowPanel row = (UIGridRowPanel)layout.getPanelList().get(rowIndex);
					List<UILayoutPanel> cells = row.getRow().getPanelList();
					for(UILayoutPanel cell : cells){
						if(!(cell instanceof UIGridPanel)){
							throw new LuiRuntimeException(ERROR_MSG);
						}
						if(Integer.parseInt(uiEle.getColIndex()) == Integer.parseInt(((UIGridPanel)cell).getColIndex())){
							//准备合并的两个单元格在同一列中
							targetEle = cell;
							colIndex = cells.indexOf(cell);
							break jump;
						}
					}
				}catch(IndexOutOfBoundsException e){}
				rowIndex = rowIndex + 1;
			}
			if(targetEle == null){
				throw new LuiRuntimeException(MSG);
			}
			//rowspan校验
			if(Integer.parseInt(uiEle.getRowIndex()) + Integer.parseInt(rowspan) != Integer.parseInt(((UIGridPanel)targetEle).getRowIndex())){
				throw new LuiRuntimeException(MSG_1);
			}
			//colspan校验
			String targetColspan = ((UIGridPanel)targetEle).getColSpan() != null ? ((UIGridPanel)targetEle).getColSpan() : "1";
			String colspan = uiEle.getColSpan() != null ? uiEle.getColSpan() : "1";
			if(Integer.parseInt(targetColspan) != Integer.parseInt(colspan)){
				throw new LuiRuntimeException(MSG_1);
			}
			expansionRowDown(uiEle, (UIGridPanel)targetEle, rowIndex, colIndex);
		}
		/**
		 * 过程:设置colspan,向右删除一个单元格
		 */
		else if("right".equals(act)){
			colIndex = colIndex + 1;
			UIElement targetEle = null;
			try{
				targetEle = layout.getGridCell(rowIndex, colIndex);
			}catch(IndexOutOfBoundsException e){}
			if(targetEle == null){
				throw new LuiRuntimeException(MSG);
			}
			if(!(targetEle instanceof UIGridPanel)){
				throw new LuiRuntimeException(ERROR_MSG);
			}
			if(((UIGridPanel)targetEle).getRowIndex() == null || uiEle.getRowIndex()==null){
				throw new LuiRuntimeException("所要合并的网格布局不规范，无法合并！");
			}
			//校验单元格是否在同一行中
			if(Integer.parseInt(((UIGridPanel)targetEle).getRowIndex()) != Integer.parseInt(uiEle.getRowIndex())){
				throw new LuiRuntimeException(MSG_2);
			}
			//rowspan校验
			String rowspan = uiEle.getRowSpan() != null ? uiEle.getRowSpan():"1";
			String targetRowspan = ((UIGridPanel)targetEle).getRowSpan() != null ? ((UIGridPanel)targetEle).getRowSpan():"1";
			if(Integer.parseInt(rowspan) != Integer.parseInt(targetRowspan)){
				throw new LuiRuntimeException(MSG_1);
			}
			//colspan校验
			//String colspan = uiEle.getColSpan() != null ? uiEle.getColSpan() : "1";
			if(Integer.parseInt(uiEle.getColIndex()) + 1 != Integer.parseInt(((UIGridPanel)targetEle).getColIndex())){
				throw new LuiRuntimeException(MSG_1);
			}
			expansionColunmRight(uiEle, (UIGridPanel)targetEle, rowIndex, colIndex);
		}
		/**
		 * 过程:设置colspan,向右删除一个单元格
		 */
		else if("left".equals(act)){
			colIndex = colIndex - 1;
			UIElement targetEle = null;
			try{
				targetEle = layout.getGridCell(rowIndex, colIndex);
			}catch(IndexOutOfBoundsException e){}
			if(targetEle == null){
				throw new LuiRuntimeException(MSG);
			}
			if(!(targetEle instanceof UIGridPanel)){
				throw new LuiRuntimeException(ERROR_MSG);
			}
			if(((UIGridPanel)targetEle).getRowIndex() == null || uiEle.getRowIndex()==null){
				throw new LuiRuntimeException("所要合并的网格布局不规范，无法合并！");
			}			
			//校验单元格是否在同一行中
			if(Integer.parseInt(((UIGridPanel)targetEle).getRowIndex()) != Integer.parseInt(uiEle.getRowIndex())){
				throw new LuiRuntimeException(MSG_2);
			}
			//rowspan校验
			String rowspan = uiEle.getRowSpan() != null ? uiEle.getRowSpan():"1";
			String targetRowspan = ((UIGridPanel)targetEle).getRowSpan() != null ? ((UIGridPanel)targetEle).getRowSpan():"1";
			if(Integer.parseInt(rowspan) != Integer.parseInt(targetRowspan)){
				throw new LuiRuntimeException(MSG_1);
			}
			//colspan校验
			//String targetColspan = ((UIGridPanel)targetEle).getColSpan() != null ? ((UIGridPanel)targetEle).getColSpan() : "1";
			if(Integer.parseInt(((UIGridPanel)targetEle).getColIndex()) + 1 != Integer.parseInt(uiEle.getColIndex())){
				throw new LuiRuntimeException(MSG_1);
			}
			expansionColunmLeft(uiEle, (UIGridPanel)targetEle, rowIndex, colIndex);
		}
		//刷新行数
		GridLayoutHelper.refreshGridLayoutRowCount(layout);
		
		//刷新行列号
		GridLayoutHelper.refreshRowColIndexOnServer(layout);
		
		//刷新前台行列号
		layout.refreshGridLayout();
	}
	
	/**
	 * 扩展列,以相对居左、居上单元格为主,合并相对居右、居下单元格.
	 * @param uiEle
	 * @param panelList
	 * @param targetIndex
	 */
	private void expansionColunmRight(UIGridPanel uiEle, UIGridPanel targetPanel, int targetRowIndex, int targetColIndex) {
		/**
		 * 修改colspan
		 */
		UpdateParameter param = rp.getParam();
		
		param.setAttr("colSpan");
		param.setAttrType("String");
		String colspan = uiEle.getColSpan();
		if(colspan == null){
			colspan = "1";
		}
		param.setOldValue(colspan);
		/**
		 * 合并单元格的过程要考虑到被合并的单元格的colspan
		 */
		String targetColSpan = targetPanel.getColSpan();
		if(targetColSpan == null){
			targetColSpan = "1";
		}
		Integer neColSpan = Integer.parseInt(colspan) + Integer.parseInt(targetColSpan);
		param.setNewValue(neColSpan.toString());
		
		param.setCompId(rp.getUiId()+"."+rp.getSubuiId());
		CmdInvoker.invoke(new UpdateCmd(rp));
		
		/**
		 * 删除
		 */
		rp.setColIndex(String.valueOf(targetColIndex));
		CmdInvoker.invoke(new DeleteCmd(rp));
	}
	
	private void expansionColunmLeft(UIGridPanel uiEle, UIGridPanel targetPanel, int targetRowIndex, int targetColIndex) {
		/**
		 * 修改colspan
		 */
		UpdateParameter param = rp.getParam();
		
		param.setAttr("colSpan");
		param.setAttrType("String");
		String colspan = uiEle.getColSpan();
		if(colspan == null){
			colspan = "1";
		}
		param.setOldValue(colspan);
		/**
		 * 合并单元格的过程要考虑到被合并的单元格的colspan
		 */
		String targetColSpan = targetPanel.getColSpan();
		if(targetColSpan == null){
			targetColSpan = "1";
		}
		Integer neColSpan = Integer.parseInt(colspan) + Integer.parseInt(targetColSpan);
		param.setNewValue(neColSpan.toString());
		
		param.setCompId(rp.getUiId()+"."+targetPanel.getId());
		/**
		 * 删除
		 */
		CmdInvoker.invoke(new DeleteCmd(rp));
		
		rp.setColIndex(String.valueOf(targetColIndex));
		CmdInvoker.invoke(new UpdateCmd(rp));
	}
	
	private void expansionRowDown(UIGridPanel uiEle, UIGridPanel targetPanel, int targetRowIndex, int targetColIndex) {
		/**
		 * 修改colspan
		 */
		UpdateParameter param = rp.getParam();
		
		param.setAttr("rowSpan");
		param.setAttrType("String");
		String rowspan = uiEle.getRowSpan();
		if(rowspan == null){
			rowspan = "1";
		}
		param.setOldValue(rowspan);
		/**
		 * 合并单元格的过程要考虑到被合并的单元格的colspan
		 */
		String targetRowSpan = targetPanel.getRowSpan();
		if(targetRowSpan == null){
			targetRowSpan = "1";
		}
		Integer neRowSpan = Integer.parseInt(rowspan) + Integer.parseInt(targetRowSpan);
		param.setNewValue(neRowSpan.toString());
		
		param.setCompId(rp.getUiId()+"."+rp.getSubuiId());
		CmdInvoker.invoke(new UpdateCmd(rp));
		
		/**
		 * 删除
		 */
		rp.setRowIndex(String.valueOf(targetRowIndex));
		rp.setColIndex(String.valueOf(targetColIndex));
		CmdInvoker.invoke(new DeleteCmd(rp));
	}
	
	private void expansionRowUp(UIGridPanel uiEle, UIGridPanel targetPanel, int targetRowIndex, int targetColIndex) {
		/**
		 * 修改colspan
		 */
		UpdateParameter param = rp.getParam();
		
		param.setAttr("rowSpan");
		param.setAttrType("String");
		String rowspan = uiEle.getRowSpan();
		if(rowspan == null){
			rowspan = "1";
		}
		param.setOldValue(rowspan);
		/**
		 * 合并单元格的过程要考虑到被合并的单元格的colspan
		 */
		String targetRowSpan = targetPanel.getRowSpan();
		if(targetRowSpan == null){
			targetRowSpan = "1";
		}
		Integer neRowSpan = Integer.parseInt(rowspan) + Integer.parseInt(targetRowSpan);
		param.setNewValue(neRowSpan.toString());
		
		param.setCompId(rp.getUiId()+"."+targetPanel.getId());
		/**
		 * 删除
		 */
		CmdInvoker.invoke(new DeleteCmd(rp));
		
		rp.setRowIndex(String.valueOf(targetRowIndex));
		rp.setColIndex(String.valueOf(targetColIndex));
		CmdInvoker.invoke(new UpdateCmd(rp));
	}

}
