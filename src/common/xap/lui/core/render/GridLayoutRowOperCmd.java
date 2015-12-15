package xap.lui.core.render;

import java.util.HashMap;
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
import xap.lui.core.logger.LuiLogger;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.model.RequestLifeCycleContext;
import xap.lui.core.util.GridLayoutHelper;


/**
 * 网格布局行操作
 * @author licza
 */
@SuppressWarnings("rawtypes")
public class GridLayoutRowOperCmd extends AbstractRaCommand{
	
	private static final String MSG_DELETE ="存在合并的单元格,不允许删除此行!";
	private static final String MSG_ADD = "行内存在合并的单元格,不允许添加行!";
	
	/**
	 * 行操作类型（删除、向上添加行、向下添加行）
	 */
	private String oper;
	
	/**
	 * 构造
	 * 
	 * @param rp
	 * @param oper
	 */
	public GridLayoutRowOperCmd(RaParameter rp, String oper) {
		super(rp);
		this.oper = oper;
	}

	/**
	 * 根据操作类型执行
	 */
	@Override
	public void execute() {
		UIGridPanel uiEle = (UIGridPanel)findGridCell(rp.getUiMeta(), null, new String[]{rp.getUiId(), rp.getSubuiId()});
		UIGridLayout layout = (UIGridLayout) UIElementFinder.findElementById(rp.getUiMeta(), rp.getUiId());	
		//-------------------------删除行-------------------------
		if(RaCmdConst.GRIDCMD_DELROW.equals(this.oper)){
			
			//得到所编辑行
			String cellId = rp.getSubuiId();
			UIGridPanel editCell =GridLayoutHelper.findCellById(layout, cellId);			
			UILayoutPanel row = GridLayoutHelper.getEditRowByCell(layout, editCell);
			
			//判断是否能够删除:如果此行有合并的单元格if(所在行内有合并行的单元格||行内单元格数和正常行相等)
			UIGridRowPanel rowPanel = (UIGridRowPanel)row;
			//取得行的所有单元格
			List<UILayoutPanel> cellList = ((UIGridRowLayout)rowPanel.getElement()).getPanelList();
			boolean rowspanCheck = true;
			for (int i = 0; i < cellList.size(); i++) {
				UIGridPanel gp = (UIGridPanel)cellList.get(i);
				try {
					if(gp.getRowSpan()==null) continue;
					int rowSpan = Integer.parseInt(gp.getRowSpan());
					if(rowSpan>1){
						rowspanCheck=false;
						break;
					}
				} catch (Exception e) {
					LuiLogger.error(e.getMessage());
					throw new LuiRuntimeException(e);
				}
			}
			
			boolean hasSameCellCount = true;
			if(cellList.size() != layout.getColcount()){
				hasSameCellCount = false;
			}
			
			if(!rowspanCheck || !hasSameCellCount){
				throw new LuiRuntimeException(MSG_DELETE);
			}
			
			//删除行，刷新行号已经在PCGridLayoutRender中notifyUpdate的时候处理
			layout.removePanel(row);
			
			//将新的rowindex放到前台展示中
			//layout.getRender().removeChild(obj);
			//layout.notifyChange(UIElement.UPDATE);
			
			//刷新左侧nav树
			ParamObject param = new ParamObject();
			if(uiEle != null){
				param.subuiId = row.getId();
				param.uiId = layout.getId();
				param.type = rp.getType();
				param.widgetId = rp.getWidgetId();
				callServer(param, UIElement.DELETE);
			}
		}
		/**
		 * 增加行的过程
		 * 1.找到当前选中Cell.获取rowspan.
		 * 2.得到本行、上一行、下一行
		 * 3.构造新行
		 * 4.判断目标行的rosspan属性，校验合并单元格等
		 * 5.添加row，更新导航
		 */
		//---------------------------添加行---------------------------
		else if(RaCmdConst.GRIDCMD_ADDROWUP.equals(this.oper) || RaCmdConst.GRIDCMD_ADDROWDOWN.equals(this.oper)){
			String cellId = rp.getSubuiId();
			UIGridPanel gp =GridLayoutHelper.findCellById(layout, cellId);
			
			//判断rowspan
			int rowspan = GridLayoutHelper.translateSpanNum(gp.getRowSpan());
			HashMap rowMap = GridLayoutHelper.getRowpanelsByCell(layout,gp);
			int realRowIndex = (Integer) rowMap.get(GridLayoutHelper.MAP_KEY_INDEX);
			UIGridRowPanel currentRowPanel = (UIGridRowPanel)rowMap.get(GridLayoutHelper.MAP_KEY_PANEL);
			
			//得到上一行、下一行
			UIGridRowPanel preRowPanel = null;
			UIGridRowPanel nextRowPanel = null;
			if(realRowIndex>0){
				preRowPanel = (UIGridRowPanel)rowMap.get(GridLayoutHelper.MAP_KEY_PREPANEL);
			}
			if(rowspan>=1){
				nextRowPanel = (UIGridRowPanel)rowMap.get(GridLayoutHelper.MAP_KEY_NEXTPANEL);
			}
			
			//构造新行，构造时候将phase置为nullstatus，避免motifyXXX的触发
			UIGridRowLayout currentRowLayout = (UIGridRowLayout)currentRowPanel.getElement();
			UIGridRowLayout newRowLayout = currentRowLayout.doClone();
			newRowLayout.getPanelList().clear();
			String randomStr = UUID.randomUUID().toString().substring(0,4);
			LifeCyclePhase phase = RequestLifeCycleContext.get().getPhase();		
			RequestLifeCycleContext.get().setPhase(LifeCyclePhase.nullstatus);
			newRowLayout.setId("row"+randomStr+"_temp");
			newRowLayout.setViewId(newRowLayout.getViewId());
			for (int i = 0; i < layout.getColcount(); i++) {
				UIGridPanel newGp = new UIGridPanel();
				//属性示例:[id=cell202576, colIndex=0, rowIndex=2, widgetId=testView, colWidth=120, colHeight=30]
				newGp.setId("cell"+UUID.randomUUID().toString().substring(0,4)+"_temp");
				newGp.setParent(newRowLayout);
				newGp.setColHeight("30");
				newGp.setColWidth("120");
				newGp.setViewId(newRowLayout.getViewId());
				newRowLayout.getPanelList().add(newGp);
			}
			UIGridRowPanel newRowPanel = new UIGridRowPanel(newRowLayout);
			newRowPanel.setId("row"+randomStr+"Panel"+"_temp");
			RequestLifeCycleContext.get().setPhase(phase);	
			
			//向上添加行
			if(RaCmdConst.GRIDCMD_ADDROWUP.equals(this.oper)){
				//检查目标位置的行的单元格rowspan
				if(preRowPanel!=null && GridLayoutHelper.hasCellRowSpanInRowPanel(preRowPanel)){
					throw new LuiRuntimeException(MSG_ADD);
				}else{
					//如果是最后一行的向上添加行
					if(realRowIndex+1 == layout.getPanelList().size() && preRowPanel!=null){
						layout.addPanel(newRowPanel,preRowPanel,true);
					}else{
						layout.addPanel(newRowPanel,currentRowPanel,false);
					}
				}
			}
			//向下添加行
			else if(RaCmdConst.GRIDCMD_ADDROWDOWN.equals(this.oper)){
				//判断是否是最后一行
				if(nextRowPanel!=null){
					//检查目标位置的行的单元格没有合并而且数量为真正的个数
					if(GridLayoutHelper.hasCellRowSpanInRowPanel(nextRowPanel) || layout.getColcount()>GridLayoutHelper.getRealColumnCount(nextRowPanel)){
						throw new LuiRuntimeException(MSG_ADD);
					}else if(rowspan>1){
						layout.addPanel(newRowPanel,nextRowPanel,true);
					}else{
						//正常添加
						layout.addPanel(newRowPanel,currentRowPanel,true);
					}
				}else {
					//末尾添加
					layout.addPanel(newRowPanel);
				}
			}
			
			//刷新左侧nav树----------------待修改
			ParamObject param = new ParamObject();
			if(uiEle != null){
				param.uiId = layout.getId();
				param.type = rp.getType();
				param.widgetId = rp.getWidgetId();
				callServer(param, UIElement.ADD);
			}
		}
		//刷新行数
		GridLayoutHelper.refreshGridLayoutRowCount(layout);
		
		//刷新行列号
		GridLayoutHelper.refreshRowColIndexOnServer(layout);
		
		//刷新前台行列号
		layout.refreshGridLayout();
		//layout.notifyChange(UIElement.UPDATE);
	}
}
