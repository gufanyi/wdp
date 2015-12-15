package xap.lui.core.render;

import xap.lui.core.command.ICommand;
import xap.lui.core.layout.UIAbsoluteLayout;
import xap.lui.core.layout.UIElement;
import xap.lui.core.layout.UIElementFinder;
import xap.lui.core.layout.UIGridLayout;
import xap.lui.core.layout.UILayout;

/**
 * 界面调整命令工厂
 * 
 * @author wupeng1
 * 
 */
public class RaCmdFactory {
	
	static final String[] addOpers = new String[] {RaCmdConst.ADDCMD,RaCmdConst.ADDCMD_LEFT,RaCmdConst.ADDCMD_RIGHT,RaCmdConst.ADDCMD_UP,RaCmdConst.ADDCMD_DOWN, RaCmdConst.ADDCMD_TABRIGHT};
	static final String[] updateOpers = new String[] {RaCmdConst.UPDATECMD};
	static final String[] deleteOpers = new String[] {RaCmdConst.DELETECMD};
	static final String[] settingOpers = new String[]{RaCmdConst.SETTINGCMD};
	static final String[] gridRowOpers = new String[] {RaCmdConst.GRIDCMD_ADDROWUP,RaCmdConst.GRIDCMD_ADDROWDOWN,RaCmdConst.GRIDCMD_DELROW};
	static final String[] columnOpers = new String[] {RaCmdConst.GRIDCMD_ADDCOLUMNLEFT,RaCmdConst.GRIDCMD_ADDCOLUMNRIGHT,RaCmdConst.GRIDCMD_DELCOLUMN};
	static final String updateIdOper =RaCmdConst.UPDATEIDCMD;
	static final String moveOper =RaCmdConst.MOVECMD;
	static final String[] updateCtrlOpers = new String[]{RaCmdConst.REPAINTFORM, RaCmdConst.REPAINTGRID, RaCmdConst.REPAINTMENU, RaCmdConst.REPAINTTOOLBAR};
	static final String[] modelOpers = new String[]{RaCmdConst.MODELCMD_ADD, RaCmdConst.MODELCMD_DELETE, RaCmdConst.MODELCMD_EDIT, RaCmdConst.MODELCMD_COPY};
	static final ICommand DEFAULT_COMMAND = new ICommand() {
		public void execute() {
		}
	};

	/**
	 * 获取界面调整命令
	 * 
	 * @param oper
	 * @param param
	 * @return ICommand
	 */
	public static ICommand getCmd(String oper, RaParameter rp) {

		if (oper == null || "null".equals(oper))
			return DEFAULT_COMMAND;
		
		/**
		 * 添加操作集合，调用对应的cmd
		 */
		if (indexOf(addOpers, oper) != -1) {
			UIElement uiEle = findCurrentUIElement(rp);
			String direction = oper.substring(3);
			/**
			 * 有方向的添加
			 */
			if(direction != null && direction.length() > 0){
				if("grid_header".equals(rp.getType())){
					return new AddGridColumnWithDirection(rp, direction);
				}else if("formcomp".equals(rp.getType())){
					return new AddFormElementCmd(rp);
				} else if ("TabRight".equals(direction)){
					return new AddTabRightCmd(rp);
				}else{
					return new AddPanelWithDirection(rp, direction);
				}
			}
			if (rp.getCurrentDropObjType2() == "isPanel" || uiEle instanceof UILayout) {
				return new AddPanelCmd(rp, uiEle);
			} else if(uiEle instanceof UIAbsoluteLayout){
				return new Add2AbsoluteLayoutCmd(rp,uiEle);
			} else {
				return new Add2PanelCmd(rp, uiEle);
			}
		}
		/**
		 * 更新操作集合，调用对应的cmd
		 */
		if (indexOf(updateOpers, oper) != -1) {
			return new UpdateCmd(rp);
		}
		/**
		 * 删除操作集合，调用对应的cmd
		 */
		if (indexOf(deleteOpers, oper) != -1) {
			return new DeleteCmd(rp);
		}
		/**
		 * 编辑操作集合，调用对应的cmd，主要针对form，grid，menu的编辑
		 */
		if (indexOf(settingOpers, oper) != -1){
			return new SetingCmd(rp);
		}
		/**
		 * 行添加和删除
		 */
		if (indexOf(gridRowOpers, oper) != -1){
			return new GridLayoutRowOperCmd(rp, oper);
		}
		
		/**
		 * 列添加和删除
		 */
		if (indexOf(columnOpers, oper) != -1){
			return new GridLayoutColumnOperCmd(rp, oper);
		}
		
		/**
		 * 拖放移动
		 */
		if (moveOper.equals(oper)){
			UIElement uiEle = findCurrentUIElement(rp);
			if(uiEle instanceof UIAbsoluteLayout)
				return new AbsoluteMoveCmd(rp,oper,uiEle);
			else
				return new MoveCmd(rp,oper,uiEle);
		}
		
		/**
		 * 更新控件ID
		 */
		if(updateIdOper.equals(oper)){
			return new UpdateIdCmd(rp);
		}
		
		/**
		 * 重画grid或者form
		 */
		if(indexOf(updateCtrlOpers, oper) != -1){
			return new RepaintCmd(rp);
		}
		
		/**
		 * 模型操作dataset,refnode,datalist
		 */
		if(indexOf(modelOpers, oper)!=-1) {
			return new ModelCmd(rp);
		}
		
		/**
		 * 扩展GridCell:合并单元格
		 */
		if(oper.startsWith("expansion")){
			return new ExpansionCmd(rp,oper);
		}
		
		
		return DEFAULT_COMMAND;
	}

	/**
	 * 根据参数获取当前控件
	 * @param rp
	 * @return
	 */
	private static UIElement findCurrentUIElement(RaParameter rp) {

		// 表格数据
		if (rp.getRowIndex() != null) {
			UIGridLayout grid = (UIGridLayout) UIElementFinder.findElementById(rp.getUiMeta(), rp.getUiId());
			return grid.getGridCell(Integer.valueOf(rp.getRowIndex()), Integer.valueOf(rp.getColIndex()));
		} else {
			if (rp.getSubuiId() != null) {
				return UIElementFinder.findElementById(rp.getUiMeta(), rp.getUiId(), rp.getSubuiId());
			} else {
				return UIElementFinder.findElementById(rp.getUiMeta(), rp.getUiId());
			}
		}
	}
	/**
	 * 数组中是否包含对应参数
	 * @param array
	 * @param objectToFind
	 * @return
	 */
	public static int indexOf(String[] array, String objectToFind) {
		if (array == null) {
			return -1;
		}
		if (objectToFind == null) {
			for (int i = 0; i < array.length; i++) {
				if (array[i] == null) {
					return i;
				}
			}
		} else {
			for (int i = 0; i < array.length; i++) {
				if (objectToFind.equals(array[i])) {
					return i;
				}
			}
		}
		return -1;
	}

}
