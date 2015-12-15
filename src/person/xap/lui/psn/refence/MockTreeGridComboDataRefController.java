package xap.lui.psn.refence;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import xap.lui.core.cache.PaCache;
import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.common.LuiWebSession;
import xap.lui.core.comps.TextComp;
import xap.lui.core.dataset.ComboData;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.MDComboDataConf;
import xap.lui.core.dataset.Row;
import xap.lui.core.event.DatasetEvent;
import xap.lui.core.event.GridRowEvent;
import xap.lui.core.event.MouseEvent;
import xap.lui.core.event.TextEvent;
import xap.lui.core.event.TreeNodeEvent;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.mock.MockTreeGridController;
import xap.lui.core.model.AppContext;
import xap.lui.core.model.AppSession;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.model.LuiPlugoutCmd;
import xap.lui.core.model.PagePartMeta;
import xap.lui.core.model.RequestLifeCycleContext;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.core.services.IPaEditorService;
import xap.lui.core.services.PaEditorServiceImpl;


@SuppressWarnings({"unchecked","restriction","rawtypes","unused"})
public class MockTreeGridComboDataRefController extends MockTreeGridController {
	
	private static final String PID = "pid";
	private static final String ID = "id";
	private static final String LABEL = "label";
	private static final String LOAD = "load";
	
	private static final String FIELD_CODE = "code";
	private static final String FIELD_NAME = "name";
	private static final String FIELD_PID = "pid";
	private static final String FIELD_MDDATA = "mddata";	
	
	private static final String MASTER_DS = "masterDs";
	private static final String SOURCE_VIEW = "sourceView";
	private static final String OWNER = "owner";
	private static final String SOURCE_WIN = "sourceWinId";
	private static final String DS_MIDDLE = "ds_middle";
	private static final String DATASET = "Dataset";
	private static final String TYPE = "type";
	private static final String MAIN = "main";
	private static final String REF_OK_PLUGOUT = "refOkPlugout";
	private static final String WRITE_FIELDS = "writeFields";
	private static final String YES = "Y";
	
	private static final String DEFIEND_COMBODATA = "DEFIEND_COMBODATA";
	private static final String METADATA = "METADATA";
	
	private ViewPartMeta getEditViewPart() {
		IPaEditorService ipaService = new PaEditorServiceImpl();
		String sessionId = (String) PaCache.getInstance().get("eclipse_sesionId");
		String pageId = (String) PaCache.getInstance().get("_pageId");
		String viewId = (String) PaCache.getInstance().get("_viewId");
		PagePartMeta pagemeta = ipaService.getOriPageMeta(pageId, sessionId);
		ViewPartMeta wd = pagemeta.getWidget(viewId);
		return wd;
	}
	/**
	 * 数据加载
	 */
	public void dataLoad(DatasetEvent e){
		Dataset ds = e.getSource();
		ds.clear();
		
//		LuiWebSession webSession = LuiRuntimeContext.getWebContext().getPageWebSession();
//		String sourceView = webSession.getOriginalParameter(SOURCE_VIEW);
//		IPaEditorService es =  new PaEditorServiceImpl();
//		String sourceWin = PaHelper.getCurrentEditWindowId();
//		String sessionId = LuiRuntimeContext.getWebContext().getRequest().getSession().getId();
//		PagePartMeta pm = es.getOriPageMeta(sourceWin, sessionId);
//		
//		ViewPartMeta wd = pm.getWidget(sourceView);
		
		ViewPartMeta wd = getEditViewPart();
		
		ComboData[]  cds = wd.getViewModels().getComboDatas();
		
		int idIndex = ds.nameToIndex(ID);
		int pIdIndex = ds.nameToIndex(PID);
		int labelIndex = ds.nameToIndex(LABEL);
		int loadIndex = ds.nameToIndex(LOAD);		
		
		//已定义的枚举
		Row row = ds.getEmptyRow();
		row.setValue(idIndex, DEFIEND_COMBODATA);
		row.setValue(pIdIndex, "");
		row.setValue(labelIndex, "已定义的枚举(双击编辑)");
		ds.addRow(row);
		
		
		if(cds != null){
			for(ComboData cd : cds){
				Row newRow = ds.getEmptyRow();
				newRow.setValue(idIndex, cd.getId());
				newRow.setValue(pIdIndex, DEFIEND_COMBODATA);
				newRow.setValue(labelIndex, cd.getCaption());
				ds.addRow(newRow);
			}
		}
		row.setValue(loadIndex, YES);
		
		/*
		//元数据
		Row metaDataRow = ds.getEmptyRow();
		metaDataRow.setValue(idIndex, METADATA);
		metaDataRow.setValue(pIdIndex, "");
		metaDataRow.setValue(labelIndex, "元数据");
		ds.addRow(metaDataRow);
		
		//初始化元数据项
		IDatasetProvider dp = NCLocator.getInstance().lookup(IDatasetProvider.class);		
		try {
			List<xap.lui.core.vos.MdModuleVO> list = (List<xap.lui.core.vos.MdModuleVO>)dp.getALlModuels();
			if(list != null && !list.isEmpty()){
				for(MdModuleVO vo : list){
					Row empRow = ds.getEmptyRow();
					empRow.setValue(idIndex, vo.getId());
					empRow.setValue(pIdIndex, METADATA);
					empRow.setValue(labelIndex,  vo.getDisplayname() );
					ds.addRow(empRow);
				}
			}
		} catch (Throwable exception) {
			LuiLogger.error(exception.getMessage(), exception);
			throw new LuiRuntimeException(exception.getMessage());
		}
		metaDataRow.setValue(loadIndex, YES);
		*/
		//默认选中已定义枚举
		ds.setRowSelectIndex(ds.getRowIndex(row));
	}
	
	public void textValueChanged(TextEvent textEvent){
		TextComp text = (TextComp)textEvent.getSource();
		String value = text.getValue();
		if(value == null)
			return;
		
		Dataset gridDs = AppSession.current().getViewContext().getView().getViewModels().getDataset("masterDs");
		int codeIndex = gridDs.nameToIndex(FIELD_CODE);
		int nameIndex = gridDs.nameToIndex(FIELD_NAME);
		int gridPidIndex = gridDs.nameToIndex(FIELD_PID);
		int mddataIndex = gridDs.nameToIndex(FIELD_MDDATA);
		gridDs.clear();
		
		//获取源信息
		ViewPartMeta wd = getEditViewPart();
		
		//根据输入的text进行过滤，包括已经存在的下拉数据和元数据中的枚举
		ComboData[]  cds = wd.getViewModels().getComboDatas();
		if(cds != null){
			for(ComboData cd : cds){
				if(cd.getId().contains(value) || cd.getCaption().contains(value)){
					Row newRow = gridDs.getEmptyRow();
					newRow.setValue(codeIndex, cd.getId());
					newRow.setValue(nameIndex,cd.getCaption());
					newRow.setValue(gridPidIndex,DEFIEND_COMBODATA);
					gridDs.addRow(newRow);
				}
			}
		}
	}	
	
	public void okButtonClick(MouseEvent e){
		LuiWebSession webSession = LuiRuntimeContext.getWebContext().getPageWebSession();
		String owner = (String) webSession.getOriginalParameter(OWNER);
		if(StringUtils.startsWith(owner, "adhintform")) {
			owner = owner.substring(10);
		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put(TYPE, DATASET);
		paramMap.put(ID, DS_MIDDLE);
		
		ViewPartMeta wd = getEditViewPart();
		ComboData[]  cds = wd.getViewModels().getComboDatas();		
		
		ViewPartMeta widget = AppSession.current().getViewContext().getView();
		Dataset currentDs =  widget.getViewModels().getDataset(MASTER_DS);
		Row currRow = null;
		//支持多选
		currRow = currentDs.getSelectedRow();
		if(currRow==null){
			throw new LuiRuntimeException("请选择引用枚举!");
		}
		
		String value =  currRow.getString(currentDs.nameToIndex(FIELD_CODE));
		String pid =  currRow.getString(currentDs.nameToIndex(FIELD_PID));
		String name =  currRow.getString(currentDs.nameToIndex(FIELD_NAME)) ;
		String objMeta = currRow.getString(currentDs.nameToIndex(FIELD_MDDATA));
		if(currRow != null){
			Map<String, String> writeFields = new HashMap<String, String>();
			if(!DEFIEND_COMBODATA.equals(pid)){
				//从元数据的枚举新建枚举，添加后可以从已定义中看到
				String compareId = value+"_"+objMeta.substring(0,5);
				boolean isEx = false;
				if(cds != null && cds.length > 0){
					for(ComboData cd : cds){
						if(compareId.equals(cd.getId())){
							isEx = true;
						}
					}
				}
				if(!isEx){
					try {
						RequestLifeCycleContext.get().setPhase(LifeCyclePhase.nullstatus);
						//创建下拉数据集
						MDComboDataConf comboData = new MDComboDataConf();
						comboData.setId(compareId);
						comboData.setCaption(name);
						comboData.setFullclassName(objMeta);
						wd.getViewModels().addComboData(comboData);
						RequestLifeCycleContext.get().setPhase(LifeCyclePhase.ajax);						
					} catch (Exception exp) {
						throw new LuiRuntimeException("根据元数据枚举创建枚举失败!", exp);
					}
				}
				writeFields.put(owner, compareId);
			}else{
				writeFields.put(owner, value);
			}
			
			paramMap.put(WRITE_FIELDS,writeFields);
			
		}
		LuiPlugoutCmd uifPluOutCmd = new LuiPlugoutCmd(MAIN,REF_OK_PLUGOUT,paramMap);
		uifPluOutCmd.execute();
	}
	
	/**
	 * 树节点选中处理
	 * 
	 * @param DatasetEvent e
	 */
	public void afterRowSel(xap.lui.core.event.DatasetEvent e){
		Dataset ds = e.getSource();
		int idIndex = ds.nameToIndex(ID);
		int pIdIndex = ds.nameToIndex(PID);
		
		Row row = ds.getSelectedRow();
		String id = row.getString(idIndex);
		String pid = row.getString(pIdIndex);
		
		Dataset gridDs = AppSession.current().getViewContext().getView().getViewModels().getDataset("masterDs");
		int codeIndex = gridDs.nameToIndex(FIELD_CODE);
		int nameIndex = gridDs.nameToIndex(FIELD_NAME);
		int gridPidIndex = gridDs.nameToIndex(FIELD_PID);
		int mddataIndex = gridDs.nameToIndex(FIELD_MDDATA);
		gridDs.clear();
		
		//父级节点选中返回
		if(METADATA.equals(id))
			return;
		
		//获取源信息
		ViewPartMeta wd = getEditViewPart();
		//添加所有已定义下拉数据集到右侧列表
		if(DEFIEND_COMBODATA.equals(id)){
			ComboData[]  cds = wd.getViewModels().getComboDatas();
			if(cds != null){
				for(ComboData cd : cds){
					Row newRow = gridDs.getEmptyRow();
					newRow.setValue(codeIndex, cd.getId());
					newRow.setValue(nameIndex,cd.getCaption());
					newRow.setValue(gridPidIndex,DEFIEND_COMBODATA);
					gridDs.addRow(newRow);
				}
			}
		}
		//添加具体的一项已定义下拉数据集
		else if(DEFIEND_COMBODATA.equals(pid)){
			ComboData[]  cds = wd.getViewModels().getComboDatas();
			if(cds != null){
				for(ComboData cd : cds){
					if(id.equals(cd.getId())){
						Row newRow = gridDs.getEmptyRow();
						newRow.setValue(codeIndex, cd.getId());
						newRow.setValue(nameIndex,cd.getCaption());
						newRow.setValue(gridPidIndex,DEFIEND_COMBODATA);
						gridDs.addRow(newRow);
					}
				}
			}
		}
		
	}	
	
	/**
	 * 树节点双击处理
	 * 
	 * @param TreeNodeMouseEvent e
	 */
	public void onTreeDbClick(TreeNodeEvent e) {
		//获取combodata
		Dataset ds = AppSession.current().getViewContext().getView().getViewModels().getDataset("treeDs");
		Row selectRow = ds.getSelectedRow();
		String id = (String)selectRow.getValue(ds.nameToIndex(ID));
		String pid = (String)selectRow.getValue(ds.nameToIndex(PID));
		if(DEFIEND_COMBODATA.equals(id) || !DEFIEND_COMBODATA.equals(pid)){
			return;
		}else{
			LuiWebSession session = LuiRuntimeContext.getWebContext().getPageWebSession();
			String sourceWin = session.getOriginalParameter(LuiRuntimeContext.DESIGNWINID);
			String sourceView = session.getOriginalParameter("sourceView");
			//弹出window
			String url = LuiRuntimeContext.getRootPath()+"/app/mockapp/combodatacfg?comboId="+id+"&" +LuiRuntimeContext.DESIGNWINID+ "="+sourceWin+"&sourceView="+sourceView;
			AppSession.current().getAppContext().popOuterWindow(url,"下拉数据集编辑","500","500",AppContext.TYPE_DIALOG,false);
		}
	}	
	
	/**
	 * 表格行双击事件处理
	 */
	public void onRowDbClick(GridRowEvent rowEvent){
		Dataset gridDs = AppSession.current().getViewContext().getView().getViewModels().getDataset("masterDs");
		String pid = (String)gridDs.getRowValue(gridDs.nameToIndex(FIELD_PID));
		if(DEFIEND_COMBODATA.equals(pid)){
			Row selectRow = gridDs.getSelectedRow();
			String id = (String)selectRow.getValue(gridDs.nameToIndex(FIELD_CODE));
			LuiWebSession session = LuiRuntimeContext.getWebContext().getPageWebSession();
			String sourceWin = session.getOriginalParameter(LuiRuntimeContext.DESIGNWINID);
			String sourceView = session.getOriginalParameter("sourceView");
			//弹出window
			String url = LuiRuntimeContext.getRootPath()+"/app/mockapp/combodatacfg?comboId="+id+"&" +LuiRuntimeContext.DESIGNWINID+ "="+sourceWin+"&sourceView="+sourceView;
			AppSession.current().getAppContext().popOuterWindow(url,"下拉数据集编辑","500","500",AppContext.TYPE_DIALOG,false);
		}else{
			return;
		}
	}
}
