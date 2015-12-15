package xap.lui.core.render;

import java.util.Map;

import xap.lui.core.common.InteractionUtil;
import xap.lui.core.comps.GridColumn;
import xap.lui.core.comps.GridComp;
import xap.lui.core.constant.EditorTypeConst;
import xap.lui.core.constant.StringDataTypeConst;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.Field;
import xap.lui.core.exception.InputItem;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.exception.StringInputItem;
import xap.lui.core.model.ViewPartMeta;


public class AddGridColumnWithDirection extends AbstractRaCommand {
	private String direction;

	public AddGridColumnWithDirection(RaParameter rp, String direction) {
		super(rp);
		this.direction = direction;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void execute() {
		ViewPartMeta widget = rp.getPageMeta().getWidget(rp.getWidgetId());
		GridComp grid = (GridComp)widget.getViewComponents().getComponent(rp.getEleId());
//		IGridColumn currentCol = grid.getColumnByField(rp.getSubEleId());
//		List<IGridColumn> colList = grid.getColumnList();
//		int idx = colList.indexOf(currentCol);
//		if("Left".equals(this.direction)){
//			idx = idx > 0 ? idx -1 : idx;
//		}
		InputItem idInputItem = new StringInputItem("id", "编号", true);
		InputItem titleInputItem = new StringInputItem("title","名称", true);
		InteractionUtil.showInputDialog("确认", new InputItem[] { idInputItem, titleInputItem});
		Map<String, String> rs = InteractionUtil.getInputDialogResult();
		if (rs != null) {
			
			String id = rs.get("id");
			if(grid.getColumnByField(id) != null){
				throw new LuiRuntimeException("这个列已经存在!");
			}
			String title = rs.get("title");
			Dataset ds = widget.getViewModels().getDataset(grid.getDataset());
			Field field = new Field(id);
			field.setText(title);
			field.setDataType(StringDataTypeConst.STRING);
			ds.addField(field);
			GridColumn col = new GridColumn();
			col.setId(id);
			col.setField(id);
			col.setText(title);
			col.setEditorType(EditorTypeConst.STRINGTEXT);
			col.setVisible(true);
			grid.addColumn(col);
		}
	}
}
