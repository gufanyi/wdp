package xap.lui.psn.refence;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import xap.dp.dmengine.d.ClassDO;
import xap.dp.dmengine.d.ComponentDO;
import xap.dp.dmengine.d.ModuleDO;
import xap.dp.dmengine.d.PropertyDO;
import xap.dp.dmengine.i.IDataModelRService;
import xap.lui.core.cache.PaCache;
import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.common.LuiWebSession;
import xap.lui.core.comps.TextComp;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.Row;
import xap.lui.core.event.DatasetEvent;
import xap.lui.core.event.MouseEvent;
import xap.lui.core.event.TextEvent;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.logger.LuiLogger;
import xap.lui.core.mock.MockTreeGridController;
import xap.lui.core.model.AppSession;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.model.LuiAppUtil;
import xap.lui.core.model.LuiPlugoutCmd;
import xap.lui.core.model.RequestLifeCycleContext;
import xap.lui.core.model.ViewPartContext;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.core.serializer.SuperVO2DatasetSerializer;
import xap.lui.core.util.LuiClassUtil;
import xap.lui.psn.guided.CommBaseCreateComp;
import xap.lui.psn.guided.DatasetConfExpand;
import xap.lui.psn.guided.GuidedCfgController;
import xap.lui.psn.guided.SigTabCardOper;
import xap.lui.psn.top.TopMainViewController;
import xap.mw.core.data.BaseDO;
import xap.mw.core.data.BizException;
import xap.mw.sf.core.util.ServiceFinder;


@SuppressWarnings({"rawtypes"})
public class MockTreeGridDsConfigRefController extends MockTreeGridController {
	
	private static final String PID = "pid";
	private static final String ID = "id";
	private static final String LABEL = "label";
	private static final String LOAD = "load";
	
	private static final String FIELD_CODE = "code";
	private static final String FIELD_NAME = "name";
	private static final String FIELD_PID = "pid";
	
	private static final String MASTER_DS = "masterDs";
	private static final String TREE_DS = "treeDs";
	
	private static final String DATASET = "Dataset";
	private static final String TYPE = "type";
	private static final String MAIN = "main";
	private static final String REF_OK_PLUGOUT = "refOkPlugout";
	private static final String WRITE_DSNAME = "dscfgds";
	private static final String WRITE_FIELDS = "writeFields";
	private static final String YES = "Y";
	
	private static final String METADATA = "METADATA";
	
	/**
	 * 数据加载
	 */
	public void dataLoad(DatasetEvent e){
		Dataset ds = e.getSource();
		ds.clear();
		
		int idIndex = ds.nameToIndex(ID);
		int pIdIndex = ds.nameToIndex(PID);
		int labelIndex = ds.nameToIndex(LABEL);
		int loadIndex = ds.nameToIndex(LOAD);		
		
		//元数据
		Row metaDataRow = ds.getEmptyRow();
		metaDataRow.setValue(idIndex, METADATA);
		metaDataRow.setValue(pIdIndex, "");
		metaDataRow.setValue(labelIndex, "元数据");
		ds.addRow(metaDataRow);
		
		//初始化元数据项
		
		IDataModelRService service = ServiceFinder.find(IDataModelRService.class);		
		try {
			ModuleDO[] moduleList = service.findModuleByCond("1=1", "", Boolean.FALSE);
			if(moduleList != null && moduleList.length>0){
				for(ModuleDO vo : moduleList){
					Row empRow = ds.getEmptyRow();
					empRow.setValue(idIndex, vo.getId());
					empRow.setValue(pIdIndex, METADATA);
					empRow.setValue(labelIndex,  vo.getDisplayname() );
					ds.addRow(empRow);
				}
			}
			
			ComponentDO[]  list = service.findComponentByCond(" 1=1 ", " name ", Boolean.FALSE);
			if(list != null && list.length>0){
				for(ComponentDO vo : list){
					Row empRow = ds.getEmptyRow();
					empRow.setValue(idIndex, vo.getId());
					empRow.setValue(pIdIndex, vo.getModuleid());
					empRow.setValue(labelIndex,  vo.getDisplayname() );
					ds.addRow(empRow);
				}
			}
		} catch (Throwable exception) {
			LuiLogger.error(exception.getMessage(), exception);
			throw new LuiRuntimeException(exception.getMessage());
		}
		metaDataRow.setValue(loadIndex, YES);
	}
	
	public void okButtonClick(MouseEvent e){
		ViewPartMeta widget = AppSession.current().getViewContext().getView();
		Dataset currentDs =  widget.getViewModels().getDataset(MASTER_DS);
		Row currRow = null;
		currRow = currentDs.getSelectedRow();
		if(currRow==null){
			throw new LuiRuntimeException("请选择需要引用的元数据!");
		}		
		String value =  currRow.getString(currentDs.nameToIndex(FIELD_CODE));
		String name =  currRow.getString(currentDs.nameToIndex(FIELD_NAME));
		String id =  currRow.getString(1);
		String voMetaStr = name.substring(name.indexOf("(")+1, name.indexOf(")"));
		LuiWebSession session = LuiRuntimeContext.getWebContext().getPageWebSession();
		String writeDs = session.getOriginalParameter("writeDs");
	
		if(StringUtils.equals(writeDs, "dscfgds")){
			Map<String, Object> paramMap = new HashMap<String, Object>();
			Map<String, String> valueMap = new HashMap<String, String>();
			valueMap.put("objMeta", voMetaStr);
			valueMap.put("voMeta", value);
			valueMap.put("displayname", name.substring(0,name.indexOf("(")));
			paramMap.put(TYPE, DATASET);
			paramMap.put(ID, WRITE_DSNAME);
			paramMap.put(WRITE_FIELDS,valueMap);
			
			Dataset ds = LuiAppUtil.getCntAppCtx().getWindowContext("dscfg")
		       .getViewContext("main").getView().getViewModels().getDataset("classds");
			
			IDataModelRService service = ServiceFinder.find(IDataModelRService.class);	
			try {
				PropertyDO[] dos = service.findPropertyByCond("  classid = '"+id+"'", "seqno", Boolean.FALSE);
				ds.setEdit(false);
				new SuperVO2DatasetSerializer().serialize(dos, ds);
			} catch (Throwable exception) {
				LuiLogger.error(exception.getMessage(), exception);
				throw new LuiRuntimeException(exception.getMessage());
			}
			
			LuiPlugoutCmd uifPluOutCmd = new LuiPlugoutCmd(MAIN,REF_OK_PLUGOUT,paramMap);
			uifPluOutCmd.execute();
		}else{
			LifeCyclePhase phase = RequestLifeCycleContext.get().getPhase();
			RequestLifeCycleContext.get().setPhase(LifeCyclePhase.nullstatus);
			String pageId= session.getOriginalParameter("pageId");
			String viewId= session.getOriginalParameter("viewId");
			String voMeta=session.getOriginalParameter("voMeta");
			String name1=session.getOriginalParameter("name");

			ViewPartContext vpc=AppSession.current().getAppContext().getWindowContext(pageId).getViewContext(viewId);
			Dataset dsForm=	vpc.getView().getViewModels().getDataset(writeDs);
			Row selRow = dsForm.getSelectedRow();
		    BaseDO baseDo=(BaseDO)LuiClassUtil.loadClass(value);
		    String pk= baseDo.getPKFieldName();
		    String dsid="ds_"+ baseDo.getTableName();
		    IDataModelRService service = ServiceFinder.find(IDataModelRService.class);
			String writeDs_ChiTab = session.getOriginalParameter("writeDs_ChiTab");
			Dataset ds_Chi=	vpc.getView().getViewModels().getDataset(writeDs_ChiTab);
			 if (ds_Chi instanceof Dataset) {
				ds_Chi.clear();
			    try {
					PropertyDO[] dos = service.findPropertyByCond("  classid = '"+id+"'", "seqno", Boolean.FALSE);
					for(PropertyDO pro:dos){
						if(pro.getAccessorclassname()!=null){
							IDataModelRService service1 = ServiceFinder.find(IDataModelRService.class);
							ClassDO[]  classlist = service1.findClassByCond("  id = '"+pro.getDatatype()+"'", " name ", Boolean.FALSE);
							for(ClassDO classdo:classlist){
								String dsId_Chi="ds_"+ classdo.getDefaulttblname();
								String objMeta =voMetaStr.substring(0, voMetaStr.lastIndexOf('.')) + "." + classdo.getName();
								String nameChi= classdo.getDisplayname()+"("+objMeta+")";
								String className= classdo.getFullclassname();
								Map<String,String>map=new HashMap<String, String>();
								Row empRow=ds_Chi.getEmptyRow();
								empRow.setValue(ds_Chi.nameToIndex("dsid"), dsId_Chi);
								map.put("dsId", dsId_Chi);
								empRow.setValue(ds_Chi.nameToIndex("ref"), className);
								map.put("ref", className);
								empRow.setValue(ds_Chi.nameToIndex("name"), nameChi);
								map.put("name", nameChi);
								empRow.setValue(ds_Chi.nameToIndex("editType"), GuidedCfgController.editType_fullTab);
								ds_Chi.addRow(empRow);
								operMdDataset(map);
								
							}
						}
					}
				} catch (BizException e1) {
				}
			}
			Map<String,String>map=new HashMap<String, String>();
			map.put("dsId", dsid);
			map.put("ref", value);
			map.put("name", name);
			operMdDataset(map);
			selRow.setValue(dsForm.nameToIndex(voMeta), value);
			selRow.setValue(dsForm.nameToIndex(name1), name);
			selRow.setValue(dsForm.nameToIndex("dsid"), dsid);
			RequestLifeCycleContext.get().setPhase(phase);
			AppSession.current().getAppContext().closeWinDialog();//关闭新建菜单树对话框
		}
	}
	
	private void operMdDataset(Map<String,String> map){
		ViewPartMeta viewPartMeta=(ViewPartMeta)PaCache.getInstance().get(TopMainViewController.vPM_cfgDsRela);
		SigTabCardOper sigTabOper=new SigTabCardOper();
		DatasetConfExpand dscfgEx=sigTabOper.getDatasetConf(map, viewPartMeta);
		dscfgEx.setIsNORelation(true);
		CommBaseCreateComp commBase=new CommBaseCreateComp();
		
		commBase.AddMdDataSetToViewPartMeta(dscfgEx);
		
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
		gridDs.clear();
		//父级节点选中返回
		if(METADATA.equals(id)||METADATA.equals(pid))
			return;
		else{
			//选择元数据的树节点时，将元数据对应的枚举添加到右侧列表中
			String sql = " id = '"+id+"'";
			IDataModelRService service = ServiceFinder.find(IDataModelRService.class);	
			try {	
				ComponentDO[]  complist = service.findComponentByCond(sql, " name ", Boolean.FALSE);
				if(complist != null && complist.length>0){
					for(ComponentDO comp : complist){
						String classSql = "classtype = '201' and componentid = '"+comp.getId()+"'";
						ClassDO[]  classlist = service.findClassByCond(classSql, " name ", Boolean.FALSE);
						if(classlist != null && classlist.length>0){
							for(ClassDO md : classlist){
								String objMeta =comp.getNamespace() + "." + md.getName();
								Row newrow = gridDs.getEmptyRow();
								newrow.setValue(codeIndex, md.getFullclassname());
								newrow.setValue(nameIndex, md.getDisplayname()+"("+objMeta+")");
								newrow.setValue(gridPidIndex,md.getId());
								gridDs.addRow(newrow);	
							}
						}
					}
					gridDs.setSelectedIndex(0);
				}
			} catch (Throwable exception) {
				LuiLogger.error(exception.getMessage(), exception);
			}
		}
	}	
	
	
	
	
	
	/**
	 * 数据加载
	 */
	public void textValueChanged(TextEvent e){
		TextComp   comp = (TextComp)e.getSource();
		ViewPartMeta widget = AppSession.current().getViewContext().getView();
		Dataset ds =  widget.getViewModels().getDataset(TREE_DS);
		ds.clear();
		int idIndex = ds.nameToIndex(ID);
		int pIdIndex = ds.nameToIndex(PID);
		int labelIndex = ds.nameToIndex(LABEL);
		int loadIndex = ds.nameToIndex(LOAD);		
		
		//元数据
		Row metaDataRow = ds.getEmptyRow();
		metaDataRow.setValue(idIndex, METADATA);
		metaDataRow.setValue(pIdIndex, "");
		metaDataRow.setValue(labelIndex, "元数据");
		ds.addRow(metaDataRow);
		
		//初始化元数据项
		
		IDataModelRService service = ServiceFinder.find(IDataModelRService.class);	
		try {
			ModuleDO[] moduleList = service.findModuleByCond("displayname like '%"+comp.getValue()+"%'", "", Boolean.FALSE);
			if(moduleList != null && moduleList.length>0){
				for(ModuleDO vo : moduleList){
					Row empRow = ds.getEmptyRow();
					empRow.setValue(idIndex, vo.getId());
					empRow.setValue(pIdIndex, METADATA);
					empRow.setValue(labelIndex,  vo.getDisplayname() );
					ds.addRow(empRow);
				}
			}
			
			ComponentDO[]  list = service.findComponentByCond("displayname like '%"+comp.getValue()+"%'","", Boolean.FALSE);
			if(list != null && list.length>0){
				for(ComponentDO vo : list){
					Row empRow = ds.getEmptyRow();
					empRow.setValue(idIndex, vo.getId());
					empRow.setValue(pIdIndex, vo.getModuleid());
					empRow.setValue(labelIndex,  vo.getDisplayname() );
					ds.addRow(empRow);
				}
			}
		} catch (Throwable exception) {
			LuiLogger.error(exception.getMessage(), exception);
			throw new LuiRuntimeException(exception.getMessage());
		}
		metaDataRow.setValue(loadIndex, YES);
	}
}
