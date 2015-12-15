package xap.lui.psn.groupcolumns;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import xap.lui.core.cache.PaCache;
import xap.lui.core.common.InteractionUtil;
import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.common.LuiWebSession;
import xap.lui.core.comps.GridColumn;
import xap.lui.core.comps.GridColumnGroup;
import xap.lui.core.comps.GridComp;
import xap.lui.core.comps.IGridColumn;
import xap.lui.core.control.IWindowCtrl;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.PageData;
import xap.lui.core.dataset.Row;
import xap.lui.core.event.DatasetEvent;
import xap.lui.core.event.MouseEvent;
import xap.lui.core.event.PageEvent;
import xap.lui.core.event.TextEvent;
import xap.lui.core.exception.InputItem;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.exception.StringInputItem;
import xap.lui.core.model.AppSession;
import xap.lui.core.model.DataModels;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.model.LuiAppUtil;
import xap.lui.core.model.RequestLifeCycleContext;
import xap.lui.core.model.ViewPartMeta;

public class GroupColumnsCtrl implements IWindowCtrl {
	public static final String DEFAULT_STRING = " ";
	private static final String DS_LEFT = "dsleft";
	private static final String DS_RIGHT = "dsright";
	private static final String GRID_ELE = "_gridele";
	
	public void onPageClosed(PageEvent event) {
		LuiRuntimeContext.getWebContext().destroyWebSession();
	}
	//加载左边表格数据
	public void onLeftDsLoad(DatasetEvent e) {
		Dataset leftds = e.getSource();
		int idIndex = leftds.nameToIndex("id");
		int labelIndex = leftds.nameToIndex("name");
		
		GridComp editGrid = getEditorGrid();
		if(editGrid != null){
			List<IGridColumn> columns = editGrid.getColumnList();
			if(columns != null && columns.size() > 0){
				if(hasGridColumnGroup(columns))
					return;
				for(IGridColumn column : columns){
					if(!column.isVisible())
						continue;
					if(column instanceof GridColumnGroup){
						 loadColumns(leftds, idIndex, labelIndex, column);
					}else{
						Row row = leftds.getEmptyRow();
						row.setValue(idIndex, column.getId());
						row.setValue(labelIndex, column.getText());
						leftds.addRow(row);
					}
				}
			}
		}
	}
	//若表格有GridColumnGroup
	private boolean hasGridColumnGroup(List<IGridColumn> columns) {
		for(IGridColumn column : columns){
			if(column instanceof GridColumnGroup)
				return true;
		}
		return false;
	}
	private void loadColumns(Dataset leftds, int idIndex, int labelIndex, IGridColumn column) {
		List<IGridColumn> groupChilds = ((GridColumnGroup) column).getChildColumnList();
		if (groupChilds != null && groupChilds.size() > 0) {
			for (IGridColumn column2 : groupChilds) {
				if (!column2.isVisible())
					continue;
				if(column2 instanceof GridColumnGroup){
					loadColumns(leftds, idIndex, labelIndex, column2);
				}else{
					Row row = leftds.getEmptyRow();
					row.setValue(idIndex, column2.getId());
					row.setValue(labelIndex, column2.getText());
					leftds.addRow(row);
				}
			}
		}
	}
	
	//加载右树
	public void onRightDsLoad(DatasetEvent e){
		Dataset dsright = e.getSource();
		int uuidIndex = dsright.nameToIndex("uuid");
		int nameIndex = dsright.nameToIndex("name");
		Row row = dsright.getEmptyRow();              //根节点
		row.setValue(uuidIndex, DEFAULT_STRING);
		row.setValue(nameIndex, "表头分组");
		dsright.addRow(row);
		dsright.setSelectedIndex(0);
		
		GridComp editGrid = getEditorGrid();
		if(editGrid != null){
			List<IGridColumn> columns = editGrid.getColumnList();
			if(!hasGridColumnGroup(columns))
				return;
			rightLoadColumns(dsright, columns, DEFAULT_STRING);
		}
	}

	private void rightLoadColumns(Dataset dsright, List<IGridColumn> columns, String uuid) {
		int uuidIndex = dsright.nameToIndex("uuid");
		int idIndex = dsright.nameToIndex("id");
		int nameIndex = dsright.nameToIndex("name");
		int pidIndex = dsright.nameToIndex("pid");
		
		if(columns != null && columns.size() > 0){
			for(IGridColumn column : columns){
				if(column.isVisible()){
					if(column instanceof GridColumnGroup){
						String _uuid = String.valueOf(UUID.randomUUID());
						Row row = dsright.getEmptyRow();
						row.setValue(uuidIndex, _uuid);
						row.setValue(idIndex, column.getId());
						row.setValue(nameIndex, column.getText());
						row.setValue(pidIndex, uuid);
						dsright.addRow(row);
						
						List<IGridColumn> cols = ((GridColumnGroup) column).getChildColumnList();
						rightLoadColumns(dsright, cols, _uuid);
					}else{
						Row row = dsright.getEmptyRow();
						String id = column.getId();
						row.setValue(uuidIndex, id+GRID_ELE);
						row.setValue(idIndex, id);
						row.setValue(nameIndex, column.getText());
						row.setValue(pidIndex, uuid);
						dsright.addRow(row);
					}
				}
			}
		}
	}
	//定位
	public void valueChanged(TextEvent textEvent) {
		
	}
	
	//添加组
	public void addGroup(MouseEvent e){
		Dataset rightDs = LuiAppUtil.getCntView().getViewModels().getDataset("dsright");
		Row selRow = rightDs.getSelectedRow();
		if(selRow == null)
			throw new LuiRuntimeException("请选中行数据！");
		InputItem groupId = new StringInputItem("gid", "组ID：", true);
		InputItem groupName = new StringInputItem("gname", "组名：", true);
		InteractionUtil.showInputDialog("添加组", new InputItem[] {
				groupId, groupName
		});
		Map<String, String> rs = InteractionUtil.getInputDialogResult();
		String gid = rs.get("gid");
		String gname = rs.get("gname");
		int uuidIndex = rightDs.nameToIndex("uuid");
		int idIndex = rightDs.nameToIndex("id");
		int nameIndex = rightDs.nameToIndex("name");
		int pidIndex = rightDs.nameToIndex("pid");
		Row row = rightDs.getEmptyRow();
		String uuid = String.valueOf(UUID.randomUUID());
		row.setValue(uuidIndex, uuid);
		row.setValue(idIndex, gid);
		row.setValue(nameIndex, gname);
		row.setValue(pidIndex, selRow.getValue(uuidIndex));
		rightDs.addRow(row);
		//rightDs.setSelectedIndex(rightDs.getRowIndex(row));
	}
	//编辑分组
	public void editGroup(MouseEvent e){
		Dataset rightDs = LuiAppUtil.getCntView().getViewModels().getDataset("dsright");
		Row selRow = rightDs.getSelectedRow();
		if(selRow == null)
			throw new LuiRuntimeException("请选中行数据！");
		String uuid = selRow.getString(rightDs.nameToIndex("uuid"));
		String pid = selRow.getString(rightDs.nameToIndex("pid"));
		if(StringUtils.isEmpty(pid) || uuid.endsWith(GRID_ELE))
			throw new LuiRuntimeException("请选中组名！");
		InputItem groupName = new StringInputItem("gname", "组名：", true);
		InteractionUtil.showInputDialog("修改组名", new InputItem[] {
				groupName
		});
		Map<String, String> rs = InteractionUtil.getInputDialogResult();
		String gname = rs.get("gname");
		selRow.setValue(rightDs.nameToIndex("name"), gname);
	}
	//删除分组
	public void delGroup(MouseEvent e){
		DataModels models = LuiAppUtil.getCntView().getViewModels();
		Dataset dsright = models.getDataset(DS_RIGHT);
		Row rSelRow = dsright.getSelectedRow();
		if(rSelRow == null)
			throw new LuiRuntimeException("请选中树行！");
		String selUuid = (String)rSelRow.getValue(dsright.nameToIndex("uuid"));
		if(selUuid.endsWith(GRID_ELE))
			throw new LuiRuntimeException("请选中分组名！");
		String selPid = (String)rSelRow.getValue(dsright.nameToIndex("pid"));
		if(selPid == null)
			throw new LuiRuntimeException("根节点不能删！");
		Dataset dsLeft = models.getDataset(DS_LEFT);
		
		removeSelectRow(dsLeft, dsright, rSelRow);
		dsright.setSelectedIndex(0);
	}
	private void removeSelectRow(Dataset dsLeft, Dataset dsright, Row rowParam) {
		int uuidIndex = dsright.nameToIndex("uuid");
		int idIndex = dsright.nameToIndex("id");
		int nameIndex = dsright.nameToIndex("name");
		int pidIndex = dsright.nameToIndex("pid");
		
		int leftIdIndex = dsLeft.nameToIndex("id");
		int leftNameIndex = dsLeft.nameToIndex("name");
		
		String uuid = (String) rowParam.getValue(uuidIndex);
		Row[] rows = dsright.getCurrentPageData().getRows();
		if(rows != null && rows.length > 0){
			for(Row row : rows){
				String pid = (String) row.getValue(pidIndex);
				if(StringUtils.equals(pid, uuid)){
					String _uuid = (String) row.getValue(uuidIndex);
					if(_uuid.endsWith(GRID_ELE)){
						Row leRow = dsLeft.getEmptyRow();
						leRow.setValue(leftIdIndex, row.getValue(idIndex));
						leRow.setValue(leftNameIndex, row.getValue(nameIndex));
						dsLeft.addRow(leRow);
						dsright.removeRow(row);
					}else{
						removeSelectRow(dsLeft, dsright, row);
					}
				}
			}
		}
		dsright.removeRow(rowParam);
	}
	
	//添加所有
	public void addAll(MouseEvent e){
		DataModels models = LuiAppUtil.getCntView().getViewModels();
		Dataset dsright = models.getDataset(DS_RIGHT);
		Dataset dsLeft = models.getDataset(DS_LEFT);
		Row rightSelRow = dsright.getSelectedRow();
		if(rightSelRow == null)
			throw new LuiRuntimeException("请选中树行！");
		String selUuid = (String)rightSelRow.getValue(dsright.nameToIndex("uuid"));
		if(selUuid.endsWith(GRID_ELE))
			throw new LuiRuntimeException("请选中分组名！");
		PageData pagedata = dsLeft.getCurrentPageData();
		if(pagedata != null){
			Row[] rows = pagedata.getRows();
			if(rows != null && rows.length > 0){
				String pid = (String) rightSelRow.getValue(dsright.nameToIndex("uuid"));
				int uuidIndex = dsright.nameToIndex("uuid");
				int idIndex = dsright.nameToIndex("id");
				int nameIndex = dsright.nameToIndex("name");
				int pidIndex = dsright.nameToIndex("pid");
				
				int leftIdIndex = dsLeft.nameToIndex("id");
				int leftNameIndex = dsLeft.nameToIndex("name");
				
				for(Row r : rows){
					Row row = dsright.getEmptyRow();
					String uuid = r.getValue(leftIdIndex)+GRID_ELE;
					row.setValue(uuidIndex, uuid);
					row.setValue(idIndex, r.getValue(leftIdIndex));
					row.setValue(nameIndex, r.getValue(leftNameIndex));
					row.setValue(pidIndex, pid);
					dsright.addRow(row);
				}
				dsLeft.clear();
			}
		}
	}
	//添加一个
	public void addOne(MouseEvent e){
		DataModels models = LuiAppUtil.getCntView().getViewModels();
		Dataset dsLeft = models.getDataset(DS_LEFT);
		Row leftRow = dsLeft.getSelectedRow();
		if(leftRow == null)
			throw new LuiRuntimeException("请选中表格行！");
		Dataset dsright = models.getDataset(DS_RIGHT);
		Row rightRow = dsright.getSelectedRow();
		if(rightRow == null)
			throw new LuiRuntimeException("请选中树行！");
		String selUuid = (String)rightRow.getValue(dsright.nameToIndex("uuid"));
		if(selUuid.endsWith(GRID_ELE))
			throw new LuiRuntimeException("请选中分组名！");
		Row row = dsright.getEmptyRow();
		String uuid = leftRow.getValue(dsLeft.nameToIndex("id")) + GRID_ELE;
		row.setValue(dsright.nameToIndex("uuid"), uuid);
		row.setValue(dsright.nameToIndex("id"), leftRow.getValue(dsLeft.nameToIndex("id")));
		row.setValue(dsright.nameToIndex("name"), leftRow.getValue(dsLeft.nameToIndex("name")));
		row.setValue(dsright.nameToIndex("pid"), rightRow.getValue(dsright.nameToIndex("uuid")));
		dsright.addRow(row);
		dsright.setSelectedIndex(dsright.getRowIndex(row));
		
		dsLeft.removeRow(leftRow);//移除已添加至右边的行
	}
	//删除一个
	public void delOne(MouseEvent e){
		DataModels models = LuiAppUtil.getCntView().getViewModels();
		Dataset rightDs = models.getDataset(DS_RIGHT);
		Row[] rtRows = rightDs.getCurrentPageSelectedRows();
		if (rtRows == null)
			throw new LuiRuntimeException("请选中右边数据！");
		int uuidIndex = rightDs.nameToIndex("uuid");
		int idIndex = rightDs.nameToIndex("id");
		int nameIndex = rightDs.nameToIndex("name");
		Dataset dsLeft = models.getDataset(DS_LEFT);
		int leidIndex = dsLeft.nameToIndex("id");
		int lenameIndex = dsLeft.nameToIndex("name");
		for (int i = 0; i < rtRows.length; i++) {
			Row row = rtRows[i];
			String uuid = (String)row.getValue(uuidIndex);
			if(!uuid.endsWith(GRID_ELE))
				throw new LuiRuntimeException("请选中除组名外的数据！");
			String id = (String) row.getValue(idIndex);
			String name = (String) row.getValue(nameIndex);
			rightDs.removeRow(row);
			//removeSelectRow(rightDs, row);
			Row leRow = dsLeft.getEmptyRow();
			leRow.setValue(leidIndex, id);
			leRow.setValue(lenameIndex, name);
			dsLeft.addRow(leRow);
		}
	}
	//删除所有(除了根节点),同时将表格列还原
	public void delAll(MouseEvent e){
		DataModels models = LuiAppUtil.getCntView().getViewModels();
		Dataset rightDs = models.getDataset(DS_RIGHT);
		Row[] rtRows = rightDs.getCurrentPageData().getRows();
		if (rtRows == null || rtRows.length == 0)
			return;
		int uuidIndex = rightDs.nameToIndex("uuid");
		int idIndex = rightDs.nameToIndex("id");
		int nameIndex = rightDs.nameToIndex("name");
		Dataset dsLeft = models.getDataset(DS_LEFT);
		int leIdIndex = dsLeft.nameToIndex("id");
		int leNameIndex = dsLeft.nameToIndex("name");
		for (int i = 0; i < rtRows.length; i++){
			String uid = (String) rtRows[i].getValue(uuidIndex);
			if(uid.endsWith(GRID_ELE)){
				Row leRow = dsLeft.getEmptyRow();
				leRow.setValue(leIdIndex, rtRows[i].getValue(idIndex));
				leRow.setValue(leNameIndex, rtRows[i].getValue(nameIndex));
				dsLeft.addRow(leRow);
				rightDs.removeRow(rtRows[i]);
			}
		}
	}
	//确定
	public void okClick(MouseEvent e){
	LifeCyclePhase pa = RequestLifeCycleContext.get().getPhase();
		RequestLifeCycleContext.get().setPhase(LifeCyclePhase.nullstatus);
		GridComp editGrid = getEditorGrid();
		if(editGrid != null){
			//将实列存在gridcolumns里，将gridcols清掉
			List<IGridColumn> gridcols = editGrid.getColumnList();
			GridColumnGroup cg = null;
			List<IGridColumn> lists = null;
			List<GridColumn> gridcolumns = new ArrayList<GridColumn>();
			if(gridcols != null && gridcols.size() > 0){
				for(IGridColumn col : gridcols){
					if(col instanceof GridColumnGroup){
						cg = new GridColumnGroup();
						lists = new ArrayList<IGridColumn>();
						cg = (GridColumnGroup) col;
						lists = cg.getChildColumnList();
						if(lists != null && lists.size() > 0){
							dealGridCol(lists, gridcolumns);
						}
					}else{
						gridcolumns.add((GridColumn)col);
					}
				}
			}
			//gridcols.clear();
			editGrid.removeColumns(gridcols);
			
			DataModels models = LuiAppUtil.getCntView().getViewModels();
			Dataset rightDs = models.getDataset(DS_RIGHT);
//			gridcols.addAll(genGridColumn(rightDs,gridcolumns,DEFAULT_STRING));
			editGrid.setColumnList(genGridColumn(rightDs,gridcolumns,DEFAULT_STRING));
			//将左边未移至右边的column加入grid
			Dataset dsLeft = models.getDataset(DS_LEFT);
			Row[] rs = dsLeft.getCurrentPageData().getRows();
			if(rs != null && rs.length > 0){
				int leIdIndex = dsLeft.nameToIndex("id");
				for(Row r : rs){
					String _id = (String) r.getValue(leIdIndex);
					editGrid.addColumn(findColInList(gridcolumns,_id));
				}
			}
		}
	    RequestLifeCycleContext.get().setPhase(pa);
		String str = "groupcols" + randomT(4);
		LuiWebSession session = LuiRuntimeContext.getWebContext().getPageWebSession();
		String dsId = session.getOriginalParameter("writeDs");
		String field = session.getOriginalParameter("writeField");
		Dataset dsMiddle = AppSession.current().getAppContext().getWindowContext("pa").getViewContext("settings").getView().getViewModels().getDataset(dsId);
		Row dsSelRow = dsMiddle.getSelectedRow();
		if(dsSelRow != null){
			dsSelRow.setValue(dsMiddle.nameToIndex(field), str);
		}
		AppSession.current().getAppContext().closeWinDialog();
	}
	public static String randomT(int length) {
		String t = String.valueOf(System.currentTimeMillis());
		return t.substring(t.length() - length);
	}
	//取消按钮
	public void cancelBtnEvent(MouseEvent e) {
		AppSession.current().getAppContext().closeWinDialog();
	}
	private GridComp getEditorGrid(){
		LuiWebSession session = LuiRuntimeContext.getWebContext().getPageWebSession();
		String eleId = session.getOriginalParameter("eleid");
		GridComp editGrid = null;
		if(StringUtils.isNotBlank(eleId)){
			ViewPartMeta view = PaCache.getEditorViewPartMeta();
			editGrid = (GridComp) view.getViewComponents().getComponent(eleId);//当前编辑的grid
		}
		return editGrid;
	}
	private List<IGridColumn> genGridColumn(Dataset ds, List<GridColumn> gridcolumns, String uuid) {
		List<IGridColumn> gridColumnList = new ArrayList<IGridColumn>();
		Row[] rows = ds.getCurrentPageData().getRows();
		int pidIndex = ds.nameToIndex("pid");
		int uuidIndex = ds.nameToIndex("uuid");
		int idIndex = ds.nameToIndex("id");
		int nameIndex = ds.nameToIndex("name");
		if(rows != null && rows.length > 0){
			for(Row row : rows) {
				String _pid = row.getString(pidIndex);
				String _uuid = row.getString(uuidIndex);
				String _id = row.getString(idIndex);
				String _name = row.getString(nameIndex);
				if(StringUtils.equals(_pid, uuid)) {
					if(_uuid.endsWith(GRID_ELE)){
						gridColumnList.add(findColInList(gridcolumns,_id));
					} else {
						GridColumnGroup colgroup = new GridColumnGroup();
						colgroup.setId(_id);
						colgroup.setText(_name);
						List<IGridColumn> childrenColumn = genGridColumn(ds,gridcolumns,_uuid);
						if(!CollectionUtils.isEmpty(childrenColumn)) {
							colgroup.setChildColumnList(childrenColumn);
							gridColumnList.add(colgroup);
						}
					}
				}
			}
		}
		return gridColumnList;
	}
	private GridColumn findColInList(List<GridColumn> gridcolumns, String id2) {
		GridColumn column = null; 
		for(GridColumn gridColumn : gridcolumns){
			if(StringUtils.equals(id2, gridColumn.getId()))
				column = gridColumn;
		}
		return column;
	}
	private List<GridColumn> dealGridCol(List<IGridColumn> gridchildColumns, List<GridColumn> gridcolumns) {
		for(IGridColumn col : gridchildColumns){
			if(col instanceof GridColumnGroup){
				List<IGridColumn> gridchildColumns2 = ((GridColumnGroup)col).getChildColumnList();
				gridcolumns = dealGridCol(gridchildColumns2, gridcolumns);
			}else{
				gridcolumns.add((GridColumn)col);
			}
		}
		return gridcolumns;
	}
	// 移除行及其子行
	//检查树中是否含有表中的行
}
