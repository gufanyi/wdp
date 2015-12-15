package xap.lui.psn.pamgr;
import java.util.UUID;

import xap.lui.core.builder.LuiSet;
import xap.lui.core.cache.PaCache;
import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.common.LuiWebSession;
import xap.lui.core.constant.DatasetConstant;
import xap.lui.core.control.ModePhase;
import xap.lui.core.dataset.ComboData;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.Field;
import xap.lui.core.dataset.IRefDataset;
import xap.lui.core.dataset.MdDataset;
import xap.lui.core.dataset.Row;
import xap.lui.core.design.IDatasetProvider;
import xap.lui.core.event.DatasetEvent;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.model.PagePartMeta;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.core.refmodel.BaseRefModel;
import xap.lui.core.refmodel.GridRefModel;
import xap.lui.core.refmodel.TreeGridRefModel;
import xap.lui.core.refmodel.TreeRefModel;
import xap.lui.core.refrence.BaseRefNode;
import xap.lui.core.refrence.IRefNode;
import xap.lui.core.services.IPaEditorService;
import xap.lui.core.services.PaEditorServiceImpl;
import xap.lui.core.util.LuiClassUtil;
public class PaEntityDsListener {
	
	
	public void onDataLoad(DatasetEvent e) {
		Dataset ds = e.getSource();
		LuiWebSession session = LuiRuntimeContext.getWebContext().getPageWebSession();
		String sessionId = (String) PaCache.getInstance().get("eclipse_sesionId");
		String pageId = (String) PaCache.getInstance().get("_pageId");
		ModePhase modePhase =  LuiRuntimeContext.getModePhase();
		IPaEditorService service = new PaEditorServiceImpl();
		if (modePhase == null)
			throw new LuiRuntimeException("当前页面没有设置正确的状态！");
		/**
		 * 如果是个性化或者自由表单设计态，则从下面加载
		 */
		PagePartMeta pagemeta = null;
		ViewPartMeta view = null;
		if (modePhase.equals(ModePhase.persona) || modePhase.equals(ModePhase.nodedef)) {
			String viewId = session.getOriginalParameter("viewId");
			pagemeta = service.getOriPageMeta(pageId, sessionId);
			ViewPartMeta[] widgets = pagemeta.getWidgets();
			if (viewId != null) {
				view = pagemeta.getWidget(viewId);
			}
			if (widgets == null || widgets.length == 0) {
				return;
			}
			if (view != null) {
				setModelData(ds, view);
			} else {
				for (int k = 0; k < widgets.length; k++) {
					ViewPartMeta widget = widgets[k];
					setModelData(ds, widget);
				}
			}
		} else if (modePhase.equals(ModePhase.eclipse)) {
			String viewId = session.getOriginalParameter("viewId");
			String key = ds.getReqParameters().getParameterValue(DatasetConstant.QUERY_KEYVALUE);
			if (key != null && !key.equals(Dataset.MASTER_KEY)) {
				expDatasetTree(ds, key);
			} else {
				pagemeta = service.getOriPageMeta(pageId, sessionId);
				if (pagemeta != null) {
					view = pagemeta.getWidget(viewId);
				}
				setModelData(ds, view);
			}
		}
	}
	
	public static void expDatasetTree(Dataset ds, String key) {
		String[] keys = key.split(",");
		MdDataset mdDs = new MdDataset();
		mdDs.setObjMeta(keys[1]);
		IDatasetProvider dsProvider =(IDatasetProvider)  LuiClassUtil.loadClass("xap.lui.core.design.DatasetProviderImpl"); 
		mdDs = dsProvider.getMdDataset(mdDs,true);
		Field[] fss = mdDs.getFields();
		if (fss != null) {
			int uuidIndex = ds.nameToIndex("uuid");
			int idIndex = ds.nameToIndex("id");
			int nameIndex = ds.nameToIndex("name");
			int typeIndex = ds.nameToIndex("type");
			int pIdIndex = ds.nameToIndex("pid");
			int sourceIndex = ds.nameToIndex("source");
			int dsIndex = ds.nameToIndex("dsid");
			for (int j = 0; j < fss.length; j++) {
				Field f = fss[j];
				String sourceField = f.getSourceField();
				// 过滤被带出字段
				if (sourceField != null && !sourceField.equals(""))
					continue;
				Row row = ds.getEmptyRow();
				row.setValue(uuidIndex, keys[2] + "," + f.getExtSourceAttr() + "," + f.getId());
				row.setValue(pIdIndex, key);
				row.setValue(typeIndex, f.getDataType());
				row.setValue(idIndex, f.getId());
				row.setValue(nameIndex, f.getText());
				row.setValue(dsIndex, keys[0]);
				String extSource = f.getExtSource();
				if (extSource != null && extSource.equals(Field.SOURCE_MD))
					row.setValue(sourceIndex, "1");
				if (!(row == null || row.size() == 0))
					ds.addRow(row);
			}
		}
	}
	
	public static void setModelData(Dataset ds, ViewPartMeta view) {
		ds.clear();
		Dataset[] dss = null;
		if (view != null) {
			dss = view.getViewModels().getDatasets();
		}
		setCurrDsInfo(ds, dss);
		IRefNode[] refNodes = null;
		if (view != null) {
			refNodes = view.getViewModels().getRefNodes();
		}
		setRefDsInfo(ds, refNodes);
		ComboData[] comboDatas = null;
		if (view != null) {
			comboDatas = view.getViewModels().getComboDatas();
		}
		setComboDateInfo(ds, comboDatas);
		LuiSet<BaseRefModel> refmodels = null;
		if(view != null){
			refmodels = view.getViewModels().getRefModelList();
		}
		setRefModelInfo(ds, refmodels);
	}
	private static void setComboDateInfo(Dataset ds, ComboData[] comboDatas) {
		int uuidIndex = ds.nameToIndex("uuid");
		int idIndex = ds.nameToIndex("id");
		int nameIndex = ds.nameToIndex("name");
		int typeIndex = ds.nameToIndex("type");
		int pIdIndex = ds.nameToIndex("pid");
		int dragIndex = ds.nameToIndex("isdrag");
		// int sourceIndex = ds.nameToIndex("source");
		// int dsIndex = ds.nameToIndex("dsid");
		Row row = ds.getEmptyRow();
		String prtId = UUID.randomUUID().toString();
		row.setValue(uuidIndex, prtId);
		row.setValue(idIndex, "ComboData");
		row.setValue(nameIndex, "枚举");
		row.setValue(typeIndex, "ComboData");
		row.setValue(dragIndex, "0");
		if (!(row == null || row.size() == 0))
			ds.addRow(row);
		if (comboDatas != null && comboDatas.length > 0) {
			for (int i = 0; i < comboDatas.length; i++) {
				ComboData comboData = comboDatas[i];
				row = ds.getEmptyRow();
				// String pid = UUID.randomUUID().toString();
				row.setValue(uuidIndex, comboData.getId());
				row.setValue(pIdIndex, prtId);
				row.setValue(nameIndex, comboData.getCaption());
				row.setValue(idIndex, comboData.getId());
				row.setValue(dragIndex, "0");
				row.setValue(typeIndex, "ComboData");
				if (!(row == null || row.size() == 0))
					ds.addRow(row);
			}
		}
	}
	public static void setNewComboDateInfo(Dataset ds, ComboData comboData) {
		int uuidIndex = ds.nameToIndex("uuid");
		int idIndex = ds.nameToIndex("id");
		int nameIndex = ds.nameToIndex("name");
		int typeIndex = ds.nameToIndex("type");
		int pIdIndex = ds.nameToIndex("pid");
		int dragIndex = ds.nameToIndex("isdrag");
		Row selRow = ds.getSelectedRow();
		String pid = null;
		if(selRow != null) {
			pid = (String) selRow.getValue(pIdIndex);
		}
		if (comboData != null && pid == null) {
			Row row = ds.getEmptyRow();
			row.setValue(uuidIndex, comboData.getId());
			row.setValue(pIdIndex, selRow.getValue(uuidIndex));
			row.setValue(nameIndex, comboData.getCaption());
			row.setValue(idIndex, comboData.getId());
			row.setValue(dragIndex, "0");
			row.setValue(typeIndex, "ComboData");
			if (!(row == null || row.size() == 0))
				ds.addRow(row);
		}else if(comboData != null && pid != null){
			selRow.setValue(nameIndex, comboData.getCaption());
			selRow.setValue(idIndex, comboData.getId());
		}
	}
	private  static void setRefDsInfo(Dataset ds, IRefNode[] refNodes) {
		int uuidIndex = ds.nameToIndex("uuid");
		int idIndex = ds.nameToIndex("id");
		int nameIndex = ds.nameToIndex("name");
		int typeIndex = ds.nameToIndex("type");
		int pIdIndex = ds.nameToIndex("pid");
		int dragIndex = ds.nameToIndex("isdrag");
		Row row = ds.getEmptyRow();
		String prtId = UUID.randomUUID().toString();
		row.setValue(uuidIndex, prtId);
		row.setValue(idIndex, "RefNode");
		row.setValue(nameIndex, "参照");
		row.setValue(dragIndex, "0");
		row.setValue(typeIndex, "RefNode");
		if (!(row == null || row.size() == 0))
			ds.addRow(row);
		if (refNodes != null && refNodes.length > 0) {
			for (int i = 0; i < refNodes.length; i++) {
				BaseRefNode refNode = (BaseRefNode) refNodes[i];
				row = ds.getEmptyRow();
				row.setValue(uuidIndex, refNode.getId());
				row.setValue(pIdIndex, prtId);
				row.setValue(idIndex, refNode.getId());
				row.setValue(nameIndex, refNode.getTitle());
				row.setValue(dragIndex, "0");
				row.setValue(typeIndex, "RefNode");
				if (!(row == null || row.size() == 0))
					ds.addRow(row);
			}
		}
	}
	/**
	 * 参照模型
	 * @param ds
	 * @param refmodels
	 */
	private static void setRefModelInfo(Dataset ds, LuiSet<BaseRefModel> refmodels) {
		int uuidIndex = ds.nameToIndex("uuid");
		int idIndex = ds.nameToIndex("id");
		int nameIndex = ds.nameToIndex("name");
		int typeIndex = ds.nameToIndex("type");
		int pIdIndex = ds.nameToIndex("pid");
		int dragIndex = ds.nameToIndex("isdrag");
		Row row = ds.getEmptyRow();
		String prtId = UUID.randomUUID().toString();
		row.setValue(uuidIndex, prtId);
		row.setValue(idIndex, "RefModel");
		row.setValue(nameIndex, "参照模型");
		row.setValue(dragIndex, "0");
		row.setValue(typeIndex, "RefModel");
		if (!(row == null || row.size() == 0))
			ds.addRow(row);
		if (refmodels != null && refmodels.size() > 0) {
			for (BaseRefModel refmodel : refmodels) {
				BaseRefModel refModel = (BaseRefModel) refmodel;
				row = ds.getEmptyRow();
				String refid = refModel.getId();
				String uuidValue = null;
				if(refmodel instanceof TreeGridRefModel)
					uuidValue = refid+"_treegrid";
				else if(refmodel instanceof TreeRefModel)
					uuidValue = refid+"_tree";
				else if(refmodel instanceof GridRefModel)
					uuidValue = refid+"_grid";
				row.setValue(uuidIndex, uuidValue);
				row.setValue(pIdIndex, prtId);
				row.setValue(idIndex, refid);
				row.setValue(nameIndex, refModel.getRefTitle());
				row.setValue(dragIndex, "0");
				row.setValue(typeIndex, "RefModel");
				if (!(row == null || row.size() == 0))
					ds.addRow(row);
			}
		}
	}
	/**
	 * 添加已定义的数据集
	 * 
	 * @param ds
	 * @param dss
	 */
	public static void setCurrDsInfo(Dataset ds, Dataset[] dss) {
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
		if (dss != null && dss.length > 0) {
			for (int i = 0; i < dss.length; i++) {
				Dataset currDs = dss[i];
				if (currDs instanceof IRefDataset)
					continue;
				row = ds.getEmptyRow();
				String pid = currDs.getId();
				row.setValue(pIdIndex, prtId);
				row.setValue(uuidIndex, currDs.getId());
				row.setValue(idIndex, currDs.getId());
				row.setValue(nameIndex, currDs.getCaption());
				row.setValue(dragIndex, "0");
				row.setValue(typeIndex, "DATASET");
				if (!(row == null || row.size() == 0))
					ds.addRow(row);
				Field[] fss = currDs.getFields();
				if (fss != null) {
					for (int j = 0; j < fss.length; j++) {
						Field f = fss[j];
						String sourceField = f.getSourceField();
						// 过滤被带出字段
						if (sourceField != null && !sourceField.equals(""))
							continue;
						row = ds.getEmptyRow();
						row.setValue(uuidIndex, currDs.getId() + "," + f.getExtSourceAttr() + "," + f.getId());
						row.setValue(pIdIndex, pid);
						row.setValue(typeIndex, f.getDataType());
						row.setValue(idIndex, f.getId());
						row.setValue(nameIndex, f.getText());
						row.setValue(dsIndex, currDs.getId());
						String extSource = f.getExtSource();
						if (extSource != null && extSource.equals(Field.SOURCE_MD))
							row.setValue(sourceIndex, "1");
						if (!(row == null || row.size() == 0))
							ds.addRow(row);
					}
				}
			}
		}
	}
}
