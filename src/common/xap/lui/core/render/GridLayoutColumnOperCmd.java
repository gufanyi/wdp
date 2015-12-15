package xap.lui.core.render;

import java.util.List;
import java.util.UUID;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.layout.UIElement;
import xap.lui.core.layout.UIElementFinder;
import xap.lui.core.layout.UIGridLayout;
import xap.lui.core.layout.UIGridPanel;
import xap.lui.core.layout.UIGridRowLayout;
import xap.lui.core.layout.UIGridRowPanel;
import xap.lui.core.layout.UILayoutPanel;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.model.RequestLifeCycleContext;
import xap.lui.core.util.GridLayoutHelper;


/**
 * 网格布局列操作
 * @author liujmc
 */
public class GridLayoutColumnOperCmd extends AbstractRaCommand{
	
	private static final String MSG = "存在合并的单元格,不允许删除此列!";
	
	private static final String MSG_ADDCOLUMN = "其它行存在合并,禁止在此添加列!";
	
	/**
	 * 列操作类型（删除、向左添加列、向右添加列）
	 */
	private String oper;
	
	/**
	 * 构造
	 * 
	 * @param rp
	 * @param oper
	 */
	public GridLayoutColumnOperCmd(RaParameter rp, String oper) {
		super(rp);
		this.oper = oper;
	}

	/**
	 * 根据操作类型执行
	 */
	@Override
	public void execute() {
		UIGridLayout layout = (UIGridLayout) UIElementFinder.findElementById(rp.getUiMeta(), rp.getUiId());	
		UIGridPanel uiEle = (UIGridPanel)findGridCell(rp.getUiMeta(), null, new String[]{rp.getUiId(), rp.getSubuiId()});

		//得到当前编辑的单元格
		String cellId = rp.getSubuiId();
		UIGridPanel currentCell =GridLayoutHelper.findCellById(layout, cellId);
		String clickColspanStr = currentCell.getColSpan();
		String clickRowspanStr = currentCell.getRowSpan();
		int clickColspan = GridLayoutHelper.translateSpanNum(clickColspanStr);
		int clickRowspan = GridLayoutHelper.translateSpanNum(clickRowspanStr);
		
		//获取真实的列号
		int realCellIndex = GridLayoutHelper.getRealColIndexByCell(layout,currentCell);
		List<UILayoutPanel> rowList = layout.getPanelList();
		
		//------------------------------------删除列:用计算出的列号来删除------------------------------------------------
		if(RaCmdConst.GRIDCMD_DELCOLUMN.equals(this.oper)){
			
			//检查点击真实列是否含有跨行
			if(clickRowspan>1){
				throw new LuiRuntimeException(MSG);
			}
			//校验所有行的colspan
			for (int i = 0; i < rowList.size(); i++) {
				UIGridRowPanel rowPanel = (UIGridRowPanel)rowList.get(i);
				UIGridRowLayout rowLayout = (UIGridRowLayout)rowPanel.getElement();
				UIGridPanel checkingPanel = GridLayoutHelper.getRealCellByIndex(rowLayout,realCellIndex);
				int tempColspan = GridLayoutHelper.translateSpanNum(checkingPanel.getColSpan());
				int tempRowspan = GridLayoutHelper.translateSpanNum(checkingPanel.getRowSpan());
				if(tempColspan>1 || tempRowspan>1){
					throw new LuiRuntimeException(MSG);
				}
			}
			
			//循环删除
			for (int i = 0; i < rowList.size(); i++) {
				UIGridRowPanel rowPanel = (UIGridRowPanel)rowList.get(i);
				UIGridRowLayout rowLayout = (UIGridRowLayout)rowPanel.getElement();
				UIGridPanel deletingPanel = GridLayoutHelper.getRealCellByIndex(rowLayout,realCellIndex);
				rowLayout.removePanel(deletingPanel);
				//刷新左侧nav树
				ParamObject param = new ParamObject();
				if(uiEle != null){
					param.subuiId = deletingPanel.getId();
					param.uiId = rowLayout.getId();
					param.type = rp.getType();
					param.widgetId = rp.getWidgetId();
					callServer(param, "delete");
				}
			}	
			//刷新列数
			GridLayoutHelper.refreshGridLayoutColCount(layout);
		}
		/**
		 * 添加列的步骤
		 * 
		 * 1：获取点击的真正地单元格和排列号
		 * 2：竖切检查，判断其它行的合并情况
		 * 3：循环行，构造新的单元格并找到目标单元格
		 * 4：真正地添加单元格
		 * 5：刷新gridlayout的列数
		 */
		//----------------------------------------------------增加列-----------------------------------------------------
		else if(RaCmdConst.GRIDCMD_ADDCOLUMNLEFT.equals(this.oper) || RaCmdConst.GRIDCMD_ADDCOLUMNRIGHT.equals(this.oper)){
			//----------------------------是否允许添加列------------------------
			boolean addAble = true;
			//真正需要校验竖切的序号
			int realClickIndex = realCellIndex;
			//如果点击的单元格跨列而且是向右添加，则特殊处理
			if(clickColspan>1 && this.oper.equals(RaCmdConst.GRIDCMD_ADDCOLUMNRIGHT)) realClickIndex = realCellIndex+clickColspan-1;
			/**
			 * 新增列的竖切检查
			 * 1：如果向左添加列，则真正地上一个index的所有竖切单元格不能向右合并
			 * 2：如果向右添加列，则真正地下一个index的所有竖切单元格不能向左合并
			 */
			addAble = GridLayoutHelper.checkColumCross(layout,realClickIndex,this.oper);
			//校验是否允许添加(多行含有列合并情况).如果不允许，则抛出异常
			if(!addAble){
				throw new LuiRuntimeException(MSG_ADDCOLUMN);
			}			
			
			//循环行，添加单元格
			for (int i = 0; i < rowList.size(); i++) {
				UILayoutPanel rp = (UILayoutPanel)rowList.get(i);
				UIGridRowLayout rl = (UIGridRowLayout)rp.getElement();
				UIGridPanel newGp = new UIGridPanel();
				//属性示例:[id=cell202576, colIndex=0, rowIndex=2, widgetId=testView, colWidth=120, colHeight=30]
				newGp.setId("cell"+UUID.randomUUID().toString().substring(0,4)+"_temp");
				newGp.setParent(rl);
				
				LifeCyclePhase phase = RequestLifeCycleContext.get().getPhase();		
				RequestLifeCycleContext.get().setPhase(LifeCyclePhase.nullstatus);
				//在nullstatus状态下设置
				newGp.setColHeight(GridLayoutHelper.CELL_HEIGHT);
				newGp.setColWidth(GridLayoutHelper.CELL_WIDTH);
				newGp.setViewId(currentCell.getViewId());
				RequestLifeCycleContext.get().setPhase(phase);	
				//设置额外属性，特殊处理
				newGp.setAttribute("gridlayout", layout);
				
				//获取目标单元格
				UIGridPanel targetCell = GridLayoutHelper.getRealCellByIndex(rl, realCellIndex);
				if(targetCell==null){
					targetCell = GridLayoutHelper.getNotNullPreCellInSameRowByIndex(rl, realCellIndex-1);
				}
				
				//----------------------------向左添加列----------------------------
				if(RaCmdConst.GRIDCMD_ADDCOLUMNLEFT.equals(this.oper)){
					rl.addPanel(newGp, targetCell, false);
				}
				//----------------------------向右添加列----------------------------
				else if(RaCmdConst.GRIDCMD_ADDCOLUMNRIGHT.equals(this.oper)){
					//判断是否为合并单元格
					if(clickColspan>1){
						rl.addPanelWithIndex(newGp,clickColspan+1);
					}else{
						if(realCellIndex == rl.getPanelList().size()-1){
							//如果最后一行
							rl.addPanelWithIndex(newGp,realCellIndex+1);
						}else{
							//如果targetCell不是最后一行
							rl.addPanel(newGp, targetCell, true);
						}
					}
				}
			}
			
			//--------设置UIGridLayout的列数------------------
			GridLayoutHelper.refreshGridLayoutColCount(layout);
			
			//刷新后台行列号
			GridLayoutHelper.refreshRowColIndexOnServer(layout);
			
			//刷新前台行列号
			//layout.notifyChange(UIElement.UPDATE);			
		}
	}
}
