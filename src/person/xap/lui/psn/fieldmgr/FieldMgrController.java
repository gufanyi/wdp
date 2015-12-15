package xap.lui.psn.fieldmgr;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import xap.lui.core.cache.PaCache;
import xap.lui.core.common.InteractionUtil;
import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.common.LuiWebSession;
import xap.lui.core.comps.ButtonComp;
import xap.lui.core.comps.FormComp;
import xap.lui.core.comps.FormElement;
import xap.lui.core.comps.GridColumn;
import xap.lui.core.comps.GridColumnGroup;
import xap.lui.core.comps.GridComp;
import xap.lui.core.comps.IGridColumn;
import xap.lui.core.comps.MenuItem;
import xap.lui.core.comps.MenubarComp;
import xap.lui.core.comps.TextComp;
import xap.lui.core.constant.DatasetConstant;
import xap.lui.core.constant.EditorTypeConst;
import xap.lui.core.control.IWindowCtrl;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.Field;
import xap.lui.core.dataset.FieldRelation;
import xap.lui.core.dataset.MatchField;
import xap.lui.core.dataset.Row;
import xap.lui.core.dataset.WhereField;
import xap.lui.core.event.DatasetEvent;
import xap.lui.core.event.MouseEvent;
import xap.lui.core.event.PageEvent;
import xap.lui.core.event.TextEvent;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.layout.UIElement;
import xap.lui.core.layout.UIElementFinder;
import xap.lui.core.layout.UIPartMeta;
import xap.lui.core.model.AppContext;
import xap.lui.core.model.AppSession;
import xap.lui.core.model.DataModels;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.model.LuiAppUtil;
import xap.lui.core.model.LuiPlugoutCmd;
import xap.lui.core.model.PagePartMeta;
import xap.lui.core.model.RequestLifeCycleContext;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.core.services.IPaEditorService;
import xap.lui.core.services.PaEditorServiceImpl;
import xap.lui.core.util.ObjSelectedUtil;
import xap.lui.core.util.WFWUtil;
import xap.lui.psn.menumgr.MenuMgrController;
import xap.lui.psn.pamgr.PaEntityDsListener;
import xap.mw.coreitf.d.FBoolean;
/**
 * 字段管理控制器
 * 
 * @author wupeng1
 * 
 */
public class FieldMgrController implements IWindowCtrl, Serializable {
	private static final String DS_LEFT = "dsleft";
	private static final String DS_RIGHT = "dsright";
	private static final String GRID = "grid";
	private static final String FORMCOMP = "formcomp";
	private static final String MENUBAR = "menubar";
	
	private static final long serialVersionUID = 7532916478964732880L;
	public void sysWindowClosed(PageEvent event) {
		LuiRuntimeContext.getWebContext().destroyWebSession();
	}
	/**
	 * 左树数据集加载类
	 * 
	 * @param DatasetEvent
	 */
	public void onLeftDsLoad(DatasetEvent DatasetEvent) {
		String type = getTypeEdit();
		/**
		 * 得到当前的数据集
		 */
		Dataset currDs = DatasetEvent.getSource();
		currDs.clear();
		Row row = currDs.getEmptyRow();
		int idIndex = currDs.nameToIndex("id");
		int labelIndex = currDs.nameToIndex("name");
		if (MENUBAR.equals(type)) {
			MenubarComp menu = getEditMenu();
			List<MenuItem> items = menu.getMenuList();
			if (items != null && items.size() > 0) {
				for (int i = 0; i < items.size(); i++) {
					row = currDs.getEmptyRow();
					MenuItem item = items.get(i);
					row.setValue(idIndex, item.getId());
					row.setValue(labelIndex, item.getText());
					currDs.addRow(row);
				}
			}
		} else {
			String dsId = null;
			FormComp form = null;
			GridComp grid = null;
			if (FORMCOMP.equals(type)) {
				form = getEditForm();
				dsId = form.getDataset();
			}
			if (GRID.equals(type)) {
				grid = getEditGrid();
				dsId = grid.getDataset();
			}
			Dataset oriDs = getOriDs(dsId);
			/**
			 * 加载左边数据，并将数据写入到左边树控件的导航中
			 */
			if (oriDs != null) {
				String key = currDs.getReqParameters().getParameterValue(DatasetConstant.QUERY_KEYVALUE);
				if (key != null && !key.equals(Dataset.MASTER_KEY)) {
					PaEntityDsListener.expDatasetTree(currDs, key);
				} else {
					if(grid != null){
						setGridDatasetInfo(currDs, grid, oriDs);
					}else {
						setFormDatasetInfo(currDs, form, oriDs);
					}
				}
			}
			currDs.setEdit(true);
		}
	}
	/**
	 * 获取要编辑的数据集
	 */
	private Dataset getOriDs(String dsId) {
		ViewPartMeta widget = getEditWidget();
		Dataset oriDs = widget.getViewModels().getDataset(dsId);
		return oriDs;
	}

	private void setGridDatasetInfo(Dataset ds, GridComp grid, Dataset oriDs) {
		int uuidIndex = ds.nameToIndex("uuid");
		int idIndex = ds.nameToIndex("id");
		int nameIndex = ds.nameToIndex("name");
		int typeIndex = ds.nameToIndex("type");
		int pIdIndex = ds.nameToIndex("pid");
		int dragIndex = ds.nameToIndex("isdrag");
		int sourceIndex = ds.nameToIndex("source");
		int dsIndex = ds.nameToIndex("dsid");
		Row row = ds.getEmptyRow();
		String prtId = UUID.randomUUID().toString();
		row.setValue(uuidIndex, prtId);
		row.setValue(idIndex, "Dataset");
		row.setValue(nameIndex, "数据集");
		row.setValue(dragIndex, "0");
		row.setValue(typeIndex, "DATASET");
		if (!(row == null || row.size() == 0))
			ds.addRow(row);
		row = ds.getEmptyRow();
		String pid = oriDs.getId()+"_Ds";
		row.setValue(pIdIndex, prtId);
		row.setValue(uuidIndex, pid);
		row.setValue(idIndex, oriDs.getId());
		row.setValue(nameIndex, oriDs.getCaption());
		row.setValue(dragIndex, "0");
		row.setValue(typeIndex, "DATASET");
		if (!(row == null || row.size() == 0))
			ds.addRow(row);
		Field[] fss = oriDs.getFields();
		if (fss != null) {
			List<IGridColumn> gridColumns = grid.getColumnList();
			for (int j = 0; j < fss.length; j++) {
				Field f = fss[j];
				GridColumn column = fieldInGridColumns(f.getId(),gridColumns);
				if(column != null)
					continue;
				String sourceField = f.getSourceField();
				// 过滤被带出字段
				if (sourceField != null && !sourceField.equals(""))
					continue;
				row = ds.getEmptyRow();
				row.setValue(uuidIndex, oriDs.getId() + "," + f.getExtSourceAttr() + "," + f.getId());
				row.setValue(pIdIndex, pid);
				row.setValue(typeIndex, f.getDataType());
				row.setValue(idIndex, f.getId());
				row.setValue(nameIndex, f.getText());
				row.setValue(dsIndex, oriDs.getId());
				String extSource = f.getExtSource();
				if (extSource != null && extSource.equals(Field.SOURCE_MD))
					row.setValue(sourceIndex, "1");
				if (!(row == null || row.size() == 0))
					ds.addRow(row);
			}
		}
	}
	private GridColumn fieldInGridColumns(String fieldId, List<IGridColumn> gridColumns) {
		GridColumn column = null;
		for(IGridColumn icol : gridColumns){
			if(icol instanceof GridColumnGroup){
				List<IGridColumn> childColumns = ((GridColumnGroup)icol).getChildColumnList();
				column = fieldInGridColumns(fieldId, childColumns);
				if(column != null)
					return column;
			}else{
				if(StringUtils.equals(fieldId, ((GridColumn)icol).getField())){
					column = (GridColumn)icol;
					return column;
				}
			}
		}
		return column;
	}
	private void setFormDatasetInfo(Dataset ds, FormComp form, Dataset oriDs) {
		int uuidIndex = ds.nameToIndex("uuid");
		int idIndex = ds.nameToIndex("id");
		int nameIndex = ds.nameToIndex("name");
		int typeIndex = ds.nameToIndex("type");
		int pIdIndex = ds.nameToIndex("pid");
		int dragIndex = ds.nameToIndex("isdrag");
		int sourceIndex = ds.nameToIndex("source");
		int dsIndex = ds.nameToIndex("dsid");
		Row row = ds.getEmptyRow();
		String prtId = UUID.randomUUID().toString();
		row.setValue(uuidIndex, prtId);
		row.setValue(idIndex, "Dataset");
		row.setValue(nameIndex, "数据集");
		row.setValue(dragIndex, "0");
		row.setValue(typeIndex, "DATASET");
		if (!(row == null || row.size() == 0))
			ds.addRow(row);
		row = ds.getEmptyRow();
		String pid = oriDs.getId()+"_Ds";
		row.setValue(pIdIndex, prtId);
		row.setValue(uuidIndex, pid);
		row.setValue(idIndex, oriDs.getId());
		row.setValue(nameIndex, oriDs.getCaption());
		row.setValue(dragIndex, "0");
		row.setValue(typeIndex, "DATASET");
		if (!(row == null || row.size() == 0))
			ds.addRow(row);
		Field[] fss = oriDs.getFields();
		if (fss != null) {
			List<FormElement> formEles = form.getElementList();
			for (int j = 0; j < fss.length; j++) {
				Field f = fss[j];
				if(fieldInFormEles(f.getId(), formEles))
					continue;
				String sourceField = f.getSourceField();
				// 过滤被带出字段
				if (sourceField != null && !sourceField.equals(""))
					continue;
				row = ds.getEmptyRow();
				row.setValue(uuidIndex, oriDs.getId() + "," + f.getExtSourceAttr() + "," + f.getId());
				row.setValue(pIdIndex, pid);
				row.setValue(typeIndex, f.getDataType());
				row.setValue(idIndex, f.getId());
				row.setValue(nameIndex, f.getText());
				row.setValue(dsIndex, oriDs.getId());
				String extSource = f.getExtSource();
				if (extSource != null && extSource.equals(Field.SOURCE_MD))
					row.setValue(sourceIndex, "1");
				if (!(row == null || row.size() == 0))
					ds.addRow(row);
			}
		}
	}
	private boolean fieldInFormEles(String fieldId, List<FormElement> formEles) {
		for(FormElement ele : formEles){
			if(StringUtils.equals(fieldId, ele.getField()))
				return true;
		}
		return false;
	}
	/**
	 * 右树数据集加载类
	 * 
	 * @param DatasetEvent
	 */
	public void onRightDsLoad(DatasetEvent DatasetEvent) {
		// 得到当前的数据集
		Dataset currDs = DatasetEvent.getSource();
		Row row = currDs.getEmptyRow();
		int idIndex = currDs.nameToIndex("id");
		int labelIndex = currDs.nameToIndex("label");
		int visibleIndex = currDs.nameToIndex("visible");
		int fieldIndex = currDs.nameToIndex("field");
		String type = getTypeEdit();
		if (FORMCOMP.equals(type)) {
			FormComp form = getEditForm();
			/**
			 * 获取formElem的list，将对应的字段写入到有点展现的数据集中
			 */
			List<FormElement> eleList = form.getElementList();
			if (eleList != null && eleList.size() > 0) {
				for (int i = 0; i < eleList.size(); i++) {
					row = currDs.getEmptyRow();
					FormElement formEle = eleList.get(i);
					row.setValue(idIndex, formEle.getId());
					String resId = formEle.getI18nName();
					String simpchn = formEle.getText() == null ? resId : formEle.getText();
					ViewPartMeta widget = getEditWidget();
					Dataset oriDs = widget.getViewModels().getDataset(form.getDataset());
					row.setValue(labelIndex, formEle.getText());
					row.setValue(fieldIndex, formEle.getField());
					row.setValue(visibleIndex, FBoolean.valueOf(formEle.isVisible()));
					currDs.addRow(row);
				}
			}
		}
		if (GRID.equals(type)) {
			GridComp grid = getEditGrid();
			/**
			 * 获取gridcolumn的list，对应字段写入到数据集中
			 */
			List<IGridColumn> igc = grid.getColumnList();
			setGridColumnRow(currDs, idIndex, labelIndex, visibleIndex, fieldIndex, grid, igc);
		}
		if (MENUBAR.equals(type)) {
			MenubarComp menu = getEditMenu();
			/**
			 * 对于menu数据的操作
			 */
			List<MenuItem> items = menu.getMenuList();
			if (items != null && items.size() > 0) {
				for (int i = 0; i < items.size(); i++) {
					row = currDs.getEmptyRow();
					MenuItem item = items.get(i);
					row.setValue(idIndex, item.getId());
					String label = item.getText();
					row.setValue(labelIndex, label);
					row.setValue(fieldIndex, item.getId());
					row.setValue(visibleIndex, FBoolean.valueOf(item.isVisible()));
					currDs.addRow(row);
				}
			}
		}
	}
	private void setGridColumnRow(Dataset currDs, int idIndex, int labelIndex, int visibleIndex, int fieldIndex, GridComp grid, List<IGridColumn> igc) {
		Row row;
		if (igc != null && igc.size() > 0) {
			Dataset oriDs = getOriDs(grid.getDataset());
			for (int i = 0; i < igc.size(); i++) {
				row = currDs.getEmptyRow();
				if(igc.get(i) instanceof GridColumnGroup){
					List<IGridColumn> childList = ((GridColumnGroup)igc.get(i)).getChildColumnList();
					setGridColumnRow(currDs,idIndex,labelIndex,visibleIndex,fieldIndex,grid,childList);
				}else{
					GridColumn gc = (GridColumn) igc.get(i);
					String field = gc.getField();
					if(!(colInOriDs(field, oriDs))){//若列不在oriDs中，则标红
						String colorStr = "<font style=\"color:red;\">" + gc.getId() + "</font>";
						String colorStr2 = "<font style=\"color:red;\">" + gc.getText() + "</font>";
						row.setValue(idIndex, colorStr);
						row.setValue(labelIndex, colorStr2);
					}else{
						row.setValue(idIndex, gc.getId());
						row.setValue(labelIndex, gc.getText());
					}
					String resId = gc.getI18nName();
					String simpchn = gc.getText() == null ? resId : gc.getText();
					row.setValue(fieldIndex, gc.getField());
					row.setValue(visibleIndex, FBoolean.valueOf(gc.isVisible()));
					currDs.addRow(row);
				}
			}
		}
	}
	private boolean colInOriDs(String label, Dataset oriDs) {
		List<Field> fieldList = oriDs.getFieldList();
		if(CollectionUtils.isNotEmpty(fieldList)){
			for(Field field : fieldList)
				if(StringUtils.equals(label, field.getId()))
					return true;
		}
		return false;
	}
	/**
	 * 删除一个
	 * 
	 * @param mouseEvent
	 */
	public void deloneBtnonclick(MouseEvent<ButtonComp> mouseEvent) {
		DataModels models = AppSession.current().getViewContext().getView().getViewModels();
		Dataset rightDs = models.getDataset(DS_RIGHT);
		Dataset leftDs = models.getDataset(DS_LEFT);
		Row[] rtRows = rightDs.getCurrentPageSelectedRows();
		if (rtRows == null)
			return;
		int idIndex = leftDs.nameToIndex("id");
		int labelIndex = leftDs.nameToIndex("name");
		int pidIndex = leftDs.nameToIndex("pid");
		for (int i = 0; i < rtRows.length; i++) {
			Row row = rtRows[i];
			String id = row.getString(rightDs.nameToIndex("id"));
			String label = row.getString(rightDs.nameToIndex("label"));
			String pid = getLeftPid(leftDs);
			Row leftRow = leftDs.getEmptyRow();
			leftRow.setValue(idIndex, id);
			leftRow.setValue(labelIndex, label);
			leftRow.setValue(pidIndex, pid);
			leftDs.addRow(leftRow);
			rightDs.removeRow(row); 
		}
	}
	private String getLeftPid(Dataset leftDs) {
		Row[] rows = leftDs.getCurrentPageData().getRows();
		if(rows != null && rows.length > 0){
			for(Row row : rows){
				String dsUuid = row.getString(leftDs.nameToIndex("uuid"));
				if(dsUuid != null)
					if(dsUuid.endsWith("_Ds"))
						return dsUuid;
			}
		}
		return null;
	}
	/**
	 * 全部删除
	 * 
	 * @param mouseEvent
	 */
	public void delallBtnonclick(MouseEvent<ButtonComp> mouseEvent) {
		DataModels models = AppSession.current().getViewContext().getView().getViewModels();
		Dataset rightDs = models.getDataset(DS_RIGHT);
		Dataset leftDs = models.getDataset(DS_LEFT);
		/**
		 * 获取当前数据集的所有行，并将其删除
		 */
		Row[] rtRows = rightDs.getCurrentPageData().getRows();
		if (rtRows == null || rtRows.length == 0)
			return;
		int idIndex = leftDs.nameToIndex("id");
		int labelIndex = leftDs.nameToIndex("name");
		int pidIndex = leftDs.nameToIndex("pid");
		for (int i = 0; i < rtRows.length; i++){
			String id = rtRows[i].getString(rightDs.nameToIndex("id"));
			String label = rtRows[i].getString(rightDs.nameToIndex("label"));
			Row leftRow = leftDs.getEmptyRow();
			leftRow.setValue(idIndex, id);
			leftRow.setValue(labelIndex, label);
			leftRow.setValue(pidIndex, getLeftPid(leftDs));
			leftDs.addRow(leftRow);
			rightDs.removeRow(rtRows[i]);
		}
		rightDs.clear();
	}
	/**
	 * 增加一个
	 * 
	 * @param mouseEvent
	 */
	public void addoneBtnonclick(MouseEvent<ButtonComp> mouseEvent) {
		ViewPartMeta widget = AppSession.current().getViewContext().getView();
		Dataset leftDs = widget.getViewModels().getDataset(DS_LEFT);
		Dataset rightDs = widget.getViewModels().getDataset(DS_RIGHT);
		int lftIdIndex = leftDs.nameToIndex("id");
		int lftLabelIndex = leftDs.nameToIndex("name");
		int pIdIndex = leftDs.nameToIndex("pid");
		Row lftRow = leftDs.getSelectedRow();
		if (lftRow == null)
			return;
		// 获取编辑控件的ds
		Dataset editDs = getEditDs();
		/**
		 * 添加一个到右边数据集中，选中当前的，然后对应字段写入右边数据集中
		 */
		String id = (String) lftRow.getValue(lftIdIndex);
		String label = (String) lftRow.getValue(lftLabelIndex);
		String pId = (String) lftRow.getValue(pIdIndex);
		if (!isExit(rightDs, id, label)) {
			// 判断是否为参照的子字段
			boolean isRef = isFieldOfRef(leftDs, lftRow, pId);
			// 如果是参照的子字段被选中向右添加
			if (isRef) {
				addRow4Ref(editDs, rightDs, id, pId, label);
			} else {
				int rtIdIndex = rightDs.nameToIndex("id");
				int rtLableIndex = rightDs.nameToIndex("label");
				int rtFieldIndex = rightDs.nameToIndex("field");
				Row rtRow = rightDs.getEmptyRow();
				rtRow = rightDs.getEmptyRow();
				rtRow.setValue(rtIdIndex, id);
				rtRow.setValue(rtFieldIndex, id);
				rtRow.setValue(rtLableIndex, label);
				rightDs.addRow(rtRow);
			}
			leftDs.removeRow(lftRow);//移除左边的行
		}
	}
	/**
	 * 判断是否为参照的子字段
	 * 
	 * @param leftDs
	 * @param leftRow
	 * @return boolean
	 */
	public boolean isFieldOfRef(Dataset leftDs, Row leftRow, String pId) {
		boolean isRef = false;
		String key = leftDs.getReqParameters().getParameterValue(DatasetConstant.QUERY_KEYVALUE);
		if (pId.equals(key)) {
			isRef = true;
		}
		return isRef;
	}
	public void addRow4Ref(Dataset editDs, Dataset rightDs, String id, String pId, String label) {
		// 获取参照数据集
		String pkOfRefDs = pId.substring(pId.lastIndexOf(",") + 1);
		if (editDs.getFieldRelations().getFieldRelation(pkOfRefDs + "_rel") == null) {
			// 目前只支持到formelement是参照时，参照对应的数据集的下一级字段
			throw new LuiRuntimeException("请选择正确的参照对应的数据集的字段!");
		}
		String idOfRefDs = editDs.getFieldRelations().getFieldRelation(pkOfRefDs + "_rel").getRefDataset();
		Dataset refDs = getEditWidget().getViewModels().getDataset(idOfRefDs);
		Field primaryKeyField = getPKField(refDs);
		// 新增的field的ID:参照数据集的pk和字段的拼接
		String newFieldId = editDs.getId() + "_" + pkOfRefDs + "_" + id;
		if (!isExit(rightDs, newFieldId, label)) {
			Row rtRow = rightDs.getEmptyRow();
			int rtIdIndex = rightDs.nameToIndex("id");
			int rtLableIndex = rightDs.nameToIndex("label");
			int rtFieldIndex = rightDs.nameToIndex("field");
			// 向右侧数据集添加行
			rtRow = rightDs.getEmptyRow();
			rtRow.setValue(rtIdIndex, newFieldId);
			rtRow.setValue(rtLableIndex, label);
			rtRow.setValue(rtFieldIndex, newFieldId);
			rightDs.addRow(rtRow);
			// 向源ds中添加字段
			Field field = new Field();
			field.setId(newFieldId);
			field.setText(label);
			field.setDataType("String");
			field.setRequire(false);
			field.setPK(false);
			LifeCyclePhase phase = RequestLifeCycleContext.get().getPhase();
			// 将ajax的状态置为nullstatus
			RequestLifeCycleContext.get().setPhase(LifeCyclePhase.nullstatus);
			editDs.addField(field);
			RequestLifeCycleContext.get().setPhase(phase);
			// 向源ds添加FieldRelation
			FieldRelation fr = new FieldRelation();
			fr.setId(newFieldId + "_rel");
			// 对应参照的ds的ID
			fr.setRefDataset(idOfRefDs);
			MatchField mf = new MatchField();
			// 选中行的字段的id
			mf.setReadField(id);
			mf.setWriteField(field.getId());
			MatchField[] mfs = {
				mf
			};
			fr.setMatchFields(mfs);
			// 对应参照的ds的pk
			WhereField wf = new WhereField(primaryKeyField.getId(), pkOfRefDs);
			fr.setWhereField(wf);
			editDs.getFieldRelations().addFieldRelation(fr);
		}
	}
	/**
	 * 全部增加
	 * 
	 * @param mouseEvent
	 */
	public void addallBtnonclick(MouseEvent<ButtonComp> mouseEvent) {
		ViewPartMeta widget = AppSession.current().getViewContext().getView();
		Dataset leftDs = widget.getViewModels().getDataset(DS_LEFT);
		Dataset rightDs = widget.getViewModels().getDataset(DS_RIGHT);
		int lftIdIndex = leftDs.nameToIndex("id");
		int lftLabelIndex = leftDs.nameToIndex("name");
		int lftPidIndex = leftDs.nameToIndex("pid");
		int rtIdIndex = rightDs.nameToIndex("id");
		int rtLableIndex = rightDs.nameToIndex("label");
		int rtFieldIndex = rightDs.nameToIndex("field");
		Row rtRow = rightDs.getEmptyRow();
		/**
		 * 循环遍历，选择所有行，添加到右边数据集中，期间进行判断是否已经存在
		 */
		Row[] lftRows = leftDs.getCurrentPageData().getRows();
		if (lftRows != null && lftRows.length > 0) {
			for (int i = 0; i < lftRows.length; i++) {
				Row lftRow = lftRows[i];
				String id = (String) lftRow.getValue(lftIdIndex);
				String label = (String) lftRow.getValue(lftLabelIndex);
				String pid = lftRow.getString(lftPidIndex);
				if(pid != null && pid.endsWith("_Ds")){
					rtRow = rightDs.getEmptyRow();
					rtRow.setValue(rtIdIndex, id);
					rtRow.setValue(rtLableIndex, label);
					rtRow.setValue(rtFieldIndex, id);
					rightDs.addRow(rtRow);
					
					leftDs.removeRow(lftRow);
				}
			}
		}
	}
	/**
	 * 确定按钮，点击确认按钮，对控件进行重现调整
	 * 
	 * @param mouseEvent
	 */
	public void okBtnEvent(MouseEvent<ButtonComp> mouseEvent) {
		ViewPartMeta widget = AppSession.current().getViewContext().getView();
		Dataset rightDs = widget.getViewModels().getDataset(DS_RIGHT);
		LuiAppUtil.addAppAttr(LuiRuntimeContext.MODEPHASE, "pa");
		ViewPartMeta editWidget = getEditWidget();
		String type = getTypeEdit();
		/**
		 * 对于form进行重新调整，后台代码调整
		 */
		if (FORMCOMP.equals(type)) {
			reconstructEditForm(rightDs, editWidget);
		}
		/**
		 * 对于grid进行重新调整，后台代码调整
		 */
		if (GRID.equals(type)) {
			reconstructEditGrid(rightDs, editWidget);
		}
		/**
		 * 对于menu进行调整，后台代码调整
		 */
		if (MENUBAR.equals(type)) {
			reconstructEditMenu(rightDs, editWidget);
		}
		MenuMgrController.triggerUpdate();
		/**
		 * 正常执行完后，关闭当前的对话框
		 */
		getCurrentAppContext().closeWinDialog();
		LuiAppUtil.getCntAppCtx().removeAppAttribute(LuiRuntimeContext.MODEPHASE);
	}
	/**
	 * 重新构造Menu控件
	 * 
	 * @param rightDs
	 * @param editWidget
	 */
	private void reconstructEditMenu(Dataset rightDs, ViewPartMeta editWidget) {
		MenubarComp menu = getEditMenu();
		MenubarComp tempMenu = (MenubarComp) menu.clone();
		Row[] rtRows = rightDs.getCurrentPageData().getRows();
		/**
		 * 如果右边数据集为空，则说明无内容，清除控件内容
		 */
		if (rtRows == null || rtRows.length == 0) {
			clearMenu(menu);
		} else {
			clearMenu(menu);
			for (int i = 0; i < rtRows.length; i++) {
				Row rtRow = rtRows[i];
				if (rtRow != null) {
					LifeCyclePhase phase = RequestLifeCycleContext.get().getPhase();
					// 将ajax的状态置为nullstatus
					RequestLifeCycleContext.get().setPhase(LifeCyclePhase.nullstatus);
					String id = (String) rtRow.getValue(rightDs.nameToIndex("id"));
					String text = (String) rtRow.getValue(rightDs.nameToIndex("label"));
					FBoolean vis = new FBoolean((boolean)rtRow.getValue(rightDs.nameToIndex("visible")));
					MenuItem item = tempMenu.getItem(id);
					item.setId(id);
					item.setText(text);
					item.setVisible(vis.booleanValue());
					menu.addMenuItem(item);
					RequestLifeCycleContext.get().setPhase(phase);
				}
			}
		}
		/**
		 * 触发前台代码进行对应的调整
		 */
		UIElement ele = UIElementFinder.findElementById(getEditUIMeta(), menu.getId());
		String id = ObjSelectedUtil.toSelected(getEditUIMeta(), getEditPageMeta(), ele);
		getCurrentAppContext().addExecScript("var obj = {id : '" + id + "', type : 'menubar' , widgetid :'" + editWidget.getId() + "', eleid :'" + menu.getId() + "'}; \n");
		getCurrentAppContext().addExecScript("parent.toOperate(obj, 'repaintMenuBarComp');\n");
	}
	/**
	 * 重构要编辑的form
	 * 
	 * @param rightDs
	 * @param editWidget
	 */
	private void reconstructEditForm(Dataset rightDs, ViewPartMeta editWidget) {
		FormComp editForm = getEditForm();
		FormComp cloneForm = (FormComp) editForm.clone();
		Dataset editDs = editWidget.getViewModels().getDataset(editForm.getDataset());
		Row[] rtRows = rightDs.getCurrentPageData().getRows();
		/**
		 * 如果右边数据集为空，则说明无内容，清除控件内容
		 */
		clearForm(editForm);
		if (rtRows != null && rtRows.length > 0) {
			for (int i = 0; i < rtRows.length; i++) {
				Row rtRow = rtRows[i];
				if (rtRow != null) {
					String id = (String) rtRow.getValue(rightDs.nameToIndex("id"));
					FBoolean vis = (FBoolean) rtRow.getValue(rightDs.nameToIndex("visible"));
					String fieldId = (String) rtRow.getValue(rightDs.nameToIndex("field"));
					Field field = editDs.getField(fieldId);
					if (field != null) {
						LifeCyclePhase phase = RequestLifeCycleContext.get().getPhase();
						// 将ajax的状态置为nullstatus
						RequestLifeCycleContext.get().setPhase(LifeCyclePhase.nullstatus);
						addField2Form(field, editForm, cloneForm, id, vis);
						RequestLifeCycleContext.get().setPhase(phase);
					}
				}
			}
			{
				Map<String,Object> map=new HashMap<String, Object>();
				map.put("editForm", editForm);
				new LuiPlugoutCmd("main", "plugout_formdata", map).execute();
			}
		}
		/**
		 * 触发前台代码进行对应的调整
		 */
		UIElement ele = UIElementFinder.findElementById(getEditUIMeta(), editForm.getId());
		if(ele == null)//自由表单处理
			return;
		String id = ObjSelectedUtil.toSelected(getEditUIMeta(), getEditPageMeta(), ele);
		getCurrentAppContext().addExecScript("var obj = {id : '" + id + "', type : 'formcomp' , widgetid :'" + editWidget.getId() + "', eleid :'" + getEditForm().getId() + "'}; \n");
		getCurrentAppContext().addExecScript("parent.toOperate(obj, 'repaintFormComp');\n");
	}
	
	/**
	 * 重构要编辑的grid
	 * 
	 * @param rightDs
	 * @param editWidget
	 */
	private void reconstructEditGrid(Dataset rightDs, ViewPartMeta editWidget) {
		LifeCyclePhase phase = RequestLifeCycleContext.get().getPhase();
		// 将ajax的状态置为nullstatus
		RequestLifeCycleContext.get().setPhase(LifeCyclePhase.nullstatus);
		GridComp editGrid = getEditGrid();
		GridComp cloneGrid = (GridComp) editGrid.clone();
		Dataset editDs = editWidget.getViewModels().getDataset(editGrid.getDataset());
		Row[] rtRows = rightDs.getCurrentPageData().getRows();
		/**
		 * 如果右边数据集为空，则说明无内容，清除控件内容，否则进行调整
		 */
		if (rtRows == null || rtRows.length == 0) {
			clearGrid(editGrid);
		} else {
			clearGrid(editGrid);
			for (int i = 0; i < rtRows.length; i++) {
				Row rtRow = rtRows[i];
				if (rtRow != null) {
					String id = (String) rtRow.getValue(rightDs.nameToIndex("id"));
					FBoolean vis = (FBoolean) rtRow.getValue(rightDs.nameToIndex("visible"));
					String fieldId = (String) rtRow.getValue(rightDs.nameToIndex("field"));
					Field field = editDs.getField(fieldId);
					/**
					 * 将clone的grid根据显示与否，将其填入到grid中
					 */
					if (field != null) {
						
						addField2Grid(field, editGrid, cloneGrid, id, vis);
						
					}
				}
			}
		}
		/**
		 * 触发前台代码进行对应的调整
		 */
		UIElement ele = UIElementFinder.findElementById(getEditUIMeta(), editGrid.getId());
		String id = ObjSelectedUtil.toSelected(getEditUIMeta(), getEditPageMeta(), ele);
		RequestLifeCycleContext.get().setPhase(phase);
		getCurrentAppContext().addExecScript("var obj = {id : '" + id + "', type : 'grid' , widgetid :'" + editWidget.getId() + "', eleid :'" + getEditGrid().getId() + "'}; \n");
		getCurrentAppContext().addExecScript("parent.toOperate(obj, 'repaintGridComp');\n");
	}
	/**
	 * 获取当前的Application的上下文
	 * 
	 * @return
	 */
	protected AppContext getCurrentAppContext() {
		return AppSession.current().getAppContext();
	}
	/**
	 * 取消按钮
	 * 
	 * @param mouseEvent
	 */
	public void cancelBtnEvent(MouseEvent<ButtonComp> mouseEvent) {
		AppSession.current().getAppContext().closeWinDialog();
	}
	/**
	 * 向上移动
	 * 
	 * @param mouseEvent
	 */
	public void moveUp(MouseEvent<MenuItem> mouseEvent) {
		ViewPartMeta widget = AppSession.current().getViewContext().getView();
		Dataset ds = widget.getViewModels().getDataset(DS_RIGHT);
		Row row = ds.getSelectedRow();
		if (row == null)
			throw new LuiRuntimeException("请选中要移动的数据！");
		int currIndex = ds.getSelectedIndex().intValue();
		/**
		 * 调整所选控件的位置
		 */
		if (0 < currIndex && currIndex < ds.getCurrentPageRowCount()) {
			ds.getCurrentPageData().moveRow(currIndex, currIndex - 1);
			ds.setRowSelectIndex(currIndex - 1);
		}
	}
	/**
	 * 向下移动
	 * 
	 * @param mouseEvent
	 */
	public void moveDown(MouseEvent<MenuItem> mouseEvent) {
		ViewPartMeta widget = AppSession.current().getViewContext().getView();
		Dataset ds = widget.getViewModels().getDataset(DS_RIGHT);
		Row row = ds.getSelectedRow();
		if (row == null)
			throw new LuiRuntimeException("请选中要移动的数据！");
		/**
		 * 向下移动，如果最后一行，则不调整
		 */
		int currIndex = ds.getSelectedIndex().intValue();
		if (0 <= currIndex && currIndex + 1 < ds.getCurrentPageRowCount()) {
			ds.getCurrentPageData().moveRow(currIndex, currIndex + 1);
			ds.setRowSelectIndex(currIndex + 1);
		}
	}
	/**
	 * 置顶
	 * 
	 * @param mouseEvent
	 */
	public void moveTop(MouseEvent<MenuItem> mouseEvent) {
		ViewPartMeta widget = AppSession.current().getViewContext().getView();
		Dataset ds = widget.getViewModels().getDataset(DS_RIGHT);
		Row row = ds.getSelectedRow();
		if (row == null)
			throw new LuiRuntimeException("请选中要移动的数据！");
		/**
		 * 将当前选中行，调整到最顶端
		 */
		int currIndex = ds.getSelectedIndex().intValue();
		if (0 <= currIndex && currIndex < ds.getCurrentPageRowCount()) {
			ds.getCurrentPageData().moveRow(currIndex, 0);
			ds.setRowSelectIndex(0);
		}
	}
	/**
	 * 置底
	 * 
	 * @param mouseEvent
	 */
	public void moveBottom(MouseEvent<MenuItem> mouseEvent) {
		ViewPartMeta widget = AppSession.current().getViewContext().getView();
		Dataset ds = widget.getViewModels().getDataset(DS_RIGHT);
		Row row = ds.getSelectedRow();
		if (row == null)
			throw new LuiRuntimeException("请选中要移动的数据！");
		/**
		 * 将当前选中行，调整到最底端
		 */
		int currIndex = ds.getSelectedIndex().intValue();
		if (0 <= currIndex && currIndex < ds.getCurrentPageRowCount()) {
			ds.getCurrentPageData().moveRow(currIndex, ds.getCurrentPageRowCount() - 1);
			ds.setRowSelectIndex(ds.getCurrentPageRowCount() - 1);
		}
	}
	/**
	 * 定位
	 * 
	 * @param textEvent
	 */
	public void valueChanged(TextEvent textEvent) {
		TextComp text = (TextComp)textEvent.getSource();
		String value = text.getValue();
		if (value == null)
			return;
		ViewPartMeta view = AppSession.current().getViewContext().getView();
		Dataset rightDs = view.getViewModels().getDataset(DS_RIGHT);
		Dataset leftDs = view.getViewModels().getDataset(DS_LEFT);
		// focusRowWithFilter(rightDs, value);
		focusRowWithFilter(leftDs, value);
	}
	private void focusRowWithFilter(Dataset ds, String value) {
		Row[] rows = ds.getCurrentPageData().getRows();
//		PageData rd = ds.getCurrentPageData();
//		rd.clear();
		if (rows == null || rows.length == 0)
			return;
		int idIndex = ds.nameToIndex("id");
		int labelIndex = ds.nameToIndex("label");
		int nameIndex = ds.nameToIndex("name");
		for (int i = 0; i < rows.length; i++) {
			Row row = rows[i];
			String id = (String) row.getValue(idIndex);
			String label = null;
			if (labelIndex >= 0)
				label = (String) row.getValue(labelIndex);
			else
				label = (String) row.getValue(nameIndex);
			if ((id != null && id.contains(value)) || (label != null && label.contains(value)))
				ds.addRow(row);
		}
	}
	/**
	 * 获取编辑的控件类型
	 * 
	 * @return
	 */
	public String getTypeEdit() {
		LuiWebSession session = LuiRuntimeContext.getWebContext().getPageWebSession();
		String type = session.getOriginalParameter("sourceType");
		if(StringUtils.isBlank(type))
			type = session.getOriginalParameter("type");//属性form传过来
		return type;
	}
	/**
	 * 清空form中的元素
	 * 
	 * @param editForm
	 */
	private void clearForm(FormComp editForm) {
		List<FormElement> eleList = editForm.getElementList();
		if (eleList == null || eleList.size() == 0)
			return;
		for (int i = 0; i < eleList.size(); i++) {
			editForm.getElementList().removeAll(eleList);
		}
	}
	/**
	 * 清除grid中的元素
	 * 
	 * @param editGrid
	 */
	private void clearGrid(GridComp editGrid) {
		List<IGridColumn> colList = editGrid.getColumnList();
		if (colList == null || colList.size() == 0)
			return;
		editGrid.removeColumns(colList);
	}
	/**
	 * 清除menu中的元素
	 * 
	 * @param menu
	 */
	private void clearMenu(MenubarComp menu) {
		List<MenuItem> items = menu.getMenuList();
		if (items == null || items.size() == 0)
			return;
		for (int i = items.size() - 1; i >= 0; i--) {
			menu.removeItem(items.get(i));
		}
	}
	/**
	 * 向form中添加field
	 * 
	 * @param field
	 * @return 
	 */
	private FormElement addField2Form(Field field, FormComp form, FormComp cloneForm, String id, FBoolean vis) {
		FormElement fe = null;
		FormElement cloneFe = cloneForm.getElementById(id);
		if (cloneFe != null) {
			fe = cloneFe;
			fe.setId(cloneFe.getId());
			fe.setText(cloneFe.getText());
			fe.setField(cloneFe.getField());
			fe.setEditorType(cloneFe.getEditorType());
			fe.setDataType(cloneFe.getDataType());
		} else {
			fe = new FormElement();
			fe.setId(field.getId());
			fe.setText(field.getText());
			fe.setField(field.getId());
			fe.setEditorType(EditorTypeConst.getEditorTypeByString(field.getDataType()));
			fe.setDataType(field.getDataType());
		}
		fe.setVisible(vis.booleanValue());
		form.addElement(fe);
		return fe;
	}
	/**
	 * 向grid中添加field
	 * 
	 * @param field
	 * @param editGrid
	 */
	private void addField2Grid(Field field, GridComp editGrid, GridComp cloneGrid, String id, FBoolean vis) {
		GridColumn gc = null;
		GridColumn cloneGc = (GridColumn) cloneGrid.getColumnById(id);
		if (cloneGc != null) {
			gc = cloneGc;
			gc.setId(cloneGc.getId());
			gc.setText(cloneGc.getText());
			gc.setI18nName(cloneGc.getI18nName());
			gc.setField(cloneGc.getField());
			gc.setEditorType(cloneGc.getEditorType());
			gc.setDataType(cloneGc.getDataType());
		} else {
			gc = new GridColumn();
			gc.setId(field.getId());
			gc.setText(field.getText());
			gc.setField(field.getId());
			gc.setI18nName(field.getI18nName());
			gc.setEditorType(EditorTypeConst.getEditorTypeByString(field.getDataType()));
			gc.setDataType(field.getDataType());
		}
		gc.setVisible(vis.booleanValue());
		editGrid.addColumn(gc);
	}
	/**
	 * 获取要编辑的form
	 * 
	 * @return
	 */
	private FormComp getEditForm() {
		LuiWebSession session = LuiRuntimeContext.getWebContext().getPageWebSession();
		String formId = session.getOriginalParameter("sourceEleId");
		if(StringUtils.isBlank(formId))
			formId = session.getOriginalParameter("eleid");//属性form传过来
		ViewPartMeta widget = getEditWidget();
		FormComp form = (FormComp) widget.getViewComponents().getComponent(formId);
		return form;
	}
	/**
	 * 获取要编辑的Grid
	 * 
	 * @return
	 */
	private GridComp getEditGrid() {
		LuiWebSession session = LuiRuntimeContext.getWebContext().getPageWebSession();
		String gridId = session.getOriginalParameter("sourceEleId");
		if(StringUtils.isBlank(gridId))
			gridId = session.getOriginalParameter("eleid");//属性form传过来
		ViewPartMeta widget = getEditWidget();
		GridComp grid = (GridComp) widget.getViewComponents().getComponent(gridId);
		return grid;
	}
	/**
	 * 获取要编辑的Menu
	 * 
	 * @return
	 */
	private MenubarComp getEditMenu() {
		LuiWebSession session = LuiRuntimeContext.getWebContext().getPageWebSession();
		String eleId = session.getOriginalParameter("sourceEleId");
		if(StringUtils.isBlank(eleId))
			eleId = session.getOriginalParameter("eleid");//属性form传过来
		ViewPartMeta widget = getEditWidget();
		MenubarComp menu = null;
		menu = widget.getViewMenus().getMenuBar(eleId);
		if (menu == null)
			menu = (MenubarComp) widget.getViewComponents().getComponent(eleId);
		return menu;
	}
	/**
	 * 获取要编辑的widget
	 */
	private ViewPartMeta getEditWidget() {
		LuiWebSession session = LuiRuntimeContext.getWebContext().getPageWebSession();
		IPaEditorService pes = new PaEditorServiceImpl();
		String pageId = session.getOriginalParameter("sourceWinId");
		String sessionId = (String) PaCache.getInstance().get("eclipse_sesionId");
		if (sessionId == null) {
			sessionId = LuiRuntimeContext.getWebContext().getRequest().getSession().getId();
		}
		String viewId = session.getOriginalParameter("sourceViewId");
		if(StringUtils.isBlank(viewId))
			viewId = session.getOriginalParameter("sourceView");
		PagePartMeta pagemeta = pes.getOriPageMeta(pageId, sessionId);
		ViewPartMeta widget = pagemeta.getWidget(viewId);
		return widget;
	}
	/**
	 * 获取编辑的控件的ds
	 * 
	 * @return Dataset
	 */
	public Dataset getEditDs() {
		Dataset editDs = null;
		ViewPartMeta editWidget = getEditWidget();
		String type = getTypeEdit();
		if (FORMCOMP.equals(type)) {
			FormComp editForm = getEditForm();
			editDs = editWidget.getViewModels().getDataset(editForm.getDataset());
		}
		if (GRID.equals(type)) {
			GridComp editGrid = getEditGrid();
			editDs = editWidget.getViewModels().getDataset(editGrid.getDataset());
		}
		return editDs;
	}
	public Field getPKField(Dataset refDs) {
		Field primaryKeyField = null;
		for (Field field : refDs.getFieldList()) {
			if (field.isPK()) {
				primaryKeyField = field;
				break;
			}
		}
		return primaryKeyField;
	}
	
	public void delItem(MouseEvent<MenuItem> mouseEvent) {
		if(InteractionUtil.showConfirmDialog("提示", "确定移除吗？")){
			ViewPartMeta widget = AppSession.current().getViewContext().getView();
			Dataset rightDs = widget.getViewModels().getDataset(DS_RIGHT);
			Row[] rtRows = rightDs.getCurrentPageSelectedRows();
			if (rtRows == null)
				return;
			for (int i = 0; i < rtRows.length; i++) {
				Row row = rtRows[i];
				rightDs.removeRow(row);
			}
		}
	}
	
	/**
	 * 获取要编辑的pagemeta
	 */
	@SuppressWarnings("restriction")
	private PagePartMeta getEditPageMeta() {
		LuiWebSession session = LuiRuntimeContext.getWebContext().getPageWebSession();
		IPaEditorService pes = new PaEditorServiceImpl();
		String pageId = session.getOriginalParameter("sourceWinId");
		String sessionId = (String) PaCache.getInstance().get("eclipse_sesionId");
		if (sessionId == null) {
			sessionId = LuiRuntimeContext.getWebContext().getRequest().getSession().getId();
		}
		PagePartMeta pagemeta = pes.getOriPageMeta(pageId, sessionId);
		return pagemeta;
	}
	/**
	 * 获取当前编辑的uimeta
	 * 
	 * @return
	 */
	@SuppressWarnings("restriction")
	private UIPartMeta getEditUIMeta() {
		LuiWebSession session = LuiRuntimeContext.getWebContext().getPageWebSession();
		IPaEditorService pes = new PaEditorServiceImpl();
		String pageId = session.getOriginalParameter("sourceWinId");
		String sessionId = LuiRuntimeContext.getWebContext().getRequest().getSession().getId();
		UIPartMeta meta = pes.getOriUIMeta(pageId, sessionId);
		return meta;
	}
	private boolean isExit(Dataset ds, String id, String label) {
		if (ds == null)
			return false;
		int idIndex = ds.nameToIndex("id");
		int labelIndex = ds.nameToIndex("label");
		if (ds.getCurrentPageData() == null)
			return false;
		Row[] rows = ds.getCurrentPageData().getRows();
		if (rows != null && rows.length > 0) {
			for (int i = 0; i < rows.length; i++) {
				Row row = rows[i];
				if (row.getValue(idIndex).equals(id) && row.getValue(labelIndex).equals(label))
					return true;
			}
		}
		return false;
	}
	
	/**
	 * 参考render中对formelement的label的多语方式,PCFormElementRender第287行
	 * 
	 * @param i18nName
	 * @param fieldId
	 * @param defaultI18nName
	 * @param langDir
	 * @return String
	 */
	// public String getFieldI18nName(String i18nName, String fieldId, Dataset
	// ds, String defaultI18nName, String langDir) {
	// if (i18nName != null && !i18nName.equals("")) {
	// if (i18nName.equals("$NULL$"))
	// return "";
	// return translate(i18nName, defaultI18nName == null ? i18nName :
	// defaultI18nName, langDir);
	// }
	// if (ds == null)
	// return defaultI18nName;
	//
	// if (fieldId != null) {
	// int fldIndex = ds.nameToIndex(fieldId);
	// if (fldIndex == -1){
	// LuiLogger.error("未能在ds中找到字段:" + fieldId + ",dataset:" + ds.getId());
	// return defaultI18nName;
	// }
	// Field field = ds.getField(fldIndex);
	// i18nName = field.getI18nName();
	// String text = field.getText();
	// String defaultValue = text == null ? i18nName : text;
	// if (i18nName == null || i18nName.equals(""))
	// return defaultI18nName == null ? defaultValue : defaultI18nName;
	// else {
	// return translate(i18nName, defaultI18nName == null ? defaultValue :
	// defaultI18nName, langDir);
	// }
	// } else
	// return defaultI18nName;
	// }
	//
	/**
	 * 进行多语翻译,如果不能翻译,返回原i18nName
	 * 
	 * @param i18nName
	 * @return String
	 */
	// public String translate(String i18nName, String defaultI18nName, String
	// langDir) {
	// if(langDir == null || langDir.equals("")){
	// if(defaultI18nName == null)
	// return "";
	// return defaultI18nName;
	// }
	// if (i18nName == null && defaultI18nName == null)
	// return "";
	// String result = LanguageUtil.getWithDefaultByProductCode(langDir,
	// defaultI18nName, i18nName);
	// if(result == null)
	// return "";
	// return result;
	// }
}
