package xap.lui.psn.refence;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.common.LuiWebSession;
import xap.lui.core.dataset.ComboData;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.MdDataset;
import xap.lui.core.dataset.Row;
import xap.lui.core.design.IDatasetProvider;
import xap.lui.core.event.DatasetEvent;
import xap.lui.core.event.MouseEvent;
import xap.lui.core.event.TreeNodeEvent;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.mock.MockTreeViewController;
import xap.lui.core.model.AppContext;
import xap.lui.core.model.AppSession;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.model.LuiPlugoutCmd;
import xap.lui.core.model.PagePartMeta;
import xap.lui.core.model.RequestLifeCycleContext;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.core.services.IPaEditorService;
import xap.lui.core.services.PaEditorServiceImpl;
import xap.lui.core.util.LuiClassUtil;
import xap.lui.psn.command.PaHelper;


@SuppressWarnings({"unchecked","restriction","rawtypes"})
public class MockComboDataRefController extends MockTreeViewController{
	private static final String PID = "pid";
	private static final String ID = "id";
	private static final String LABEL = "label";
	private static final String LOAD = "load";
	
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
	private static final String CURRENT_WIDGET = "CURRENT_WIDGET";
	private static final String YES = "Y";
	private static final String METADATA = "METADATA";

	@Override
	public void dataLoad(DatasetEvent e) {
		LuiWebSession webSession = LuiRuntimeContext.getWebContext().getPageWebSession();
		String sourceView = webSession.getOriginalParameter(SOURCE_VIEW);
		IPaEditorService es =  new PaEditorServiceImpl();
		String sourceWin = PaHelper.getCurrentEditWindowId();
		String sessionId = LuiRuntimeContext.getWebContext().getRequest().getSession().getId();
		PagePartMeta pm = es.getOriPageMeta(sourceWin, sessionId);
		
		
		ViewPartMeta wd = pm.getWidget(sourceView);
		ComboData[]  cds = wd.getViewModels().getComboDatas();
		Dataset ds = e.getSource();
		
		int idIndex = ds.nameToIndex(ID);
		int pIdIndex = ds.nameToIndex(PID);
		int labelIndex = ds.nameToIndex(LABEL);
		int loadIndex = ds.nameToIndex(LOAD);
		
		ds.clear();
		//已定义的枚举
		Row row = ds.getEmptyRow();
		row.setValue(idIndex, CURRENT_WIDGET);
		row.setValue(pIdIndex, "");
		row.setValue(labelIndex, "已定义的枚举(双击编辑)");
		ds.addRow(row);
		
		if(cds != null){
			for(ComboData cd : cds){
				Row newRow = ds.getEmptyRow();
				newRow.setValue(idIndex, cd.getId());
				newRow.setValue(pIdIndex, CURRENT_WIDGET);
				newRow.setValue(labelIndex, cd.getCaption());
				ds.addRow(newRow);
			}
		}
		row.setValue(loadIndex, YES);
		
		//元数据
		Row metaDataRow = ds.getEmptyRow();
		metaDataRow.setValue(idIndex, METADATA);
		metaDataRow.setValue(pIdIndex, "");
		metaDataRow.setValue(labelIndex, "元数据");
		ds.addRow(metaDataRow);
		
		//选中的是元数据
		metaDataRow.setValue(loadIndex, YES);
	}
	
	@Override
	public void okButtonClick(MouseEvent e) {
		LuiWebSession webSession = LuiRuntimeContext.getWebContext().getPageWebSession();
		String owner = (String) webSession.getOriginalParameter(OWNER);
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put(TYPE, DATASET);
		paramMap.put(ID, DS_MIDDLE);
		
		String sourceWin = webSession.getOriginalParameter(SOURCE_WIN);
		String sourceView = webSession.getOriginalParameter(SOURCE_VIEW);
		IPaEditorService es =  new PaEditorServiceImpl();
		String sessionId = LuiRuntimeContext.getWebContext().getRequest().getSession().getId();
		PagePartMeta pm = es.getOriPageMeta(sourceWin, sessionId);
		
		ViewPartMeta wd = pm.getWidget(sourceView);
		ComboData[]  cds = wd.getViewModels().getComboDatas();		
		
		ViewPartMeta widget = AppSession.current().getViewContext().getView();
		Dataset currentDs =  widget.getViewModels().getDataset(MASTER_DS);;
		Row currRow = null;

		//支持多选
		currRow = currentDs.getSelectedRow();
		
		String value =  currRow.getString(currentDs.nameToIndex(ID)) ;
		String pid =  currRow.getString(currentDs.nameToIndex(PID)) ;
		String objMeta = currRow.getString(3) ;
		if(currRow != null){
			if(!CURRENT_WIDGET.equals(pid)){
				//从元数据的枚举新建枚举
				boolean isEx = false;
				if(cds != null && cds.length > 0){
					for(ComboData cd : cds){
						if(value.equals(cd.getId())){
							isEx = true;
						}
					}
				}
				if(!isEx){
					MdDataset mdds = new MdDataset();
//					mdds.setObjMeta(objMeta);
					IDatasetProvider dp =(IDatasetProvider) LuiClassUtil.loadClass("xap.lui.core.design.DatasetProviderImpl");
					try {
						List<ComboData> cdlist = null;//dp.getNcComoboDataList(mdds);
						for(ComboData cd : cdlist){
							if(value.equals(cd.getId())){
								RequestLifeCycleContext.get().setPhase(LifeCyclePhase.nullstatus);
								wd.getViewModels().addComboData(cd);
								RequestLifeCycleContext.get().setPhase(LifeCyclePhase.ajax);
							}
						}
						
					} catch (Exception e2) {
						throw new LuiRuntimeException("根据元数据枚举创建枚举失败!", e2);
					}
				}
			}
			Map<String, String> writeFields = new HashMap<String, String>();
			
			writeFields.put(owner, value);
			paramMap.put(WRITE_FIELDS,writeFields);
			
		}
		LuiPlugoutCmd uifPluOutCmd = new LuiPlugoutCmd(MAIN,REF_OK_PLUGOUT,paramMap);
		uifPluOutCmd.execute();
		
	}
	/**
	 * 行选中事件.用以懒加载元数据枚举
	 * @param e
	 */
	public void onAfterRowSelect(xap.lui.core.event.DatasetEvent e) {
		Dataset ds = e.getSource();
		
		int idIndex = ds.nameToIndex(ID);
		int pIdIndex = ds.nameToIndex(PID);
		int labelIndex = ds.nameToIndex(LABEL);
		int loadIndex = ds.nameToIndex(LOAD);
		
		Row row = ds.getSelectedRow();
		String id = row.getString(idIndex);
		String pid = row.getString(pIdIndex);
		String loaded = row.getString(loadIndex);
		
		if(CURRENT_WIDGET.equals(id) || CURRENT_WIDGET.equals(pid))
			return;
		 
		IDatasetProvider dp =(IDatasetProvider) LuiClassUtil.loadClass("xap.lui.core.design.DatasetProviderImpl");
		if(METADATA.equals(pid) && !YES.equals(loaded)){
			row.setString(ds.nameToIndex(LOAD), YES);
		}
	}
	
	/**
	 * 树节点双击处理
	 * 
	 * @param TreeNodeMouseEvent e
	 */
	public void onTreeDbClick(TreeNodeEvent e) {
		//获取combodata
		Dataset ds = AppSession.current().getViewContext().getView().getViewModels().getDataset(MASTER_DS);
		Row selectRow = ds.getSelectedRow();
		String id = selectRow.getString(0);
		String pid = selectRow.getString(1);
		if(CURRENT_WIDGET.equals(id) || !CURRENT_WIDGET.equals(pid)){
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
}
