package xap.lui.psn.guided;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import xap.dp.dmengine.d.EnumValueDO;
import xap.dp.dmengine.d.PropTypeEnum;
import xap.dp.dmengine.d.PropertyDO;
import xap.dp.dmengine.i.IDataModelRService;
import xap.lui.core.cache.PaCache;
import xap.lui.core.comps.ButtonComp;
import xap.lui.core.comps.ContextMenuComp;
import xap.lui.core.comps.MenuItem;
import xap.lui.core.comps.MenubarComp;
import xap.lui.core.comps.RecursiveTreeLevel;
import xap.lui.core.comps.ToolBarComp;
import xap.lui.core.comps.ToolBarItem;
import xap.lui.core.comps.WebComp;
import xap.lui.core.dataset.ComboData;
import xap.lui.core.dataset.DataItem;
import xap.lui.core.dataset.DataList;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.DatasetRelation;
import xap.lui.core.dataset.DatasetRelations;
import xap.lui.core.dataset.FieldRelation;
import xap.lui.core.dataset.LuiParameter;
import xap.lui.core.dataset.MatchField;
import xap.lui.core.dataset.MdDataset;
import xap.lui.core.dataset.RefMdDataset;
import xap.lui.core.dataset.WhereField;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.layout.UIPartMeta;
import xap.lui.core.listener.DatasetRule;
import xap.lui.core.listener.EventSubmitRule;
import xap.lui.core.listener.LuiEventConf;
import xap.lui.core.listener.WidgetRule;
import xap.lui.core.logger.LuiLogger;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.model.LuiAppUtil;
import xap.lui.core.model.PagePartMeta;
import xap.lui.core.model.RequestLifeCycleContext;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.core.refrence.AppRefDftCtrl;
import xap.lui.core.refrence.GenericRefNode;
import xap.lui.core.refrence.IRefModel;
import xap.lui.core.refrence.IRefNode;
import xap.lui.core.refrence.RefPubUtil;
import xap.lui.core.util.LuiClassUtil;
import xap.lui.psn.cmd.LuiAddOrEditMenuClickCmd;
import xap.lui.psn.dsmgr.DatasetConfigController;
import xap.lui.psn.pamgr.PaPalletDsListener;
import xap.lui.psn.top.TopMainViewController;
import xap.mw.core.data.BaseDO;
import xap.mw.core.data.BizException;
import xap.mw.coreitf.d.FBoolean;
import xap.mw.sf.core.util.ServiceFinder;
import xap.sys.bdrefinfo.d.BdRefInfoDO;

public class CommBaseCreateComp {
	public static final String ctrlItem_MenuBar_Remove="menuItem_Remove";
	public static final String ctrlItem_MenuBar_Save="menuItem_Save";
	public static final String ctrlItem_MenuBar_Cancle="menuItem_Cancle";
	public static final String ctrlItem_MenuBar_Ok="menuItem_Ok";
	private Dataset sourceDateset;	
	private WebComp webComp;
	private RecursiveTreeLevel TreeLevel1;
	private ContextMenuComp contextMenu;
	/**
	 * 获取Tree控件的 leve1设置
	 */
	public RecursiveTreeLevel getTreeLevel1() {
		return TreeLevel1;
	}
	/**
	 * 设置Tree控件的 leve1
	 * @param treeLevel1
	 */
	public RecursiveTreeLevel setTreeLevel1(String dsId,String masterKey){
		TreeLevel1 = new RecursiveTreeLevel();
		TreeLevel1.setId("level1");
		TreeLevel1.setDataset(dsId);
		TreeLevel1.setRecursiveField(masterKey);
		TreeLevel1.setRecursiveParentField(masterKey);
		TreeLevel1.setMasterKeyField(masterKey);
		TreeLevel1.setLabelFields(masterKey);
		return  TreeLevel1;
	}
	//构建 树的右键菜单
	public void setContextMenu(){
		contextMenu=new ContextMenuComp();
		contextMenu.setId("contextMenu"+CommTool.getRndNum(4));
		String[][] menuItemDefaults = {{"new","新建","new.png"},{"edit","编辑","edit.png"},{"delete","删除","delete.png"}};
		for(String[] menuItemDefault : menuItemDefaults) {
			MenuItem menuItem = new MenuItem();
			menuItem.setId("contextMenu_menuitem_" + menuItemDefault[0]);
			menuItem.setText(menuItemDefault[1]);
			menuItem.setImgIcon(menuItemDefault[2]);
			contextMenu.addMenuItem(menuItem);
		}
	}
	public ContextMenuComp getContextMenu(){
		return contextMenu;
	}
	
	public void AddMdDataSetToViewPartMeta(DatasetConfExpand datasetConf){
		// 初始化穿透参数
	//	initParams();
		//LuiWebSession pageSession = LuiRuntimeContext.getWebContext().getPageWebSession();
		ViewPartMeta mainWidget =  LuiAppUtil.getCntView();
		Dataset fieldDs = mainWidget.getViewModels().getDataset("fieldds");
	
		String id = datasetConf.getDsId();
		FBoolean isLazy = datasetConf.getIsLasyLoad();
		String voMeta = datasetConf.getVoMeta();
		String objMeta = datasetConf.getObjMeta();
		String displayname = datasetConf.getDispName();
		String pageSize=datasetConf.getPageSize();
		Boolean isAfterSelRowEvent=datasetConf.getIsAfterSelRowEvent();
		Boolean isSetParTabStatus=datasetConf.getIsParTabStatus();
		Boolean isDataLoad=datasetConf.getIsDataLoad();//是否给数据集配置 DatasetLoad事件
		
		if(StringUtils.isBlank(id)) {
			throw new LuiRuntimeException("数据集ID不能为空！");
		}
		
		ViewPartMeta sourceWidget =datasetConf.getWidget();//新生成的
	    sourceDateset = new Dataset(id);
		sourceDateset.setEdit(false);
		sourceDateset.setWidget(sourceWidget);
		sourceDateset.setCaption(displayname);
		sourceDateset.setLazyLoad(isLazy.booleanValue());
		DatasetRelations allDsRs = sourceWidget.getViewModels().getDsrelations();
		if (allDsRs == null) {
			allDsRs = new DatasetRelations();
			{
				allDsRs.setWidget(sourceWidget);
				sourceWidget.getViewModels().setDsrelations(allDsRs);
			}
		}
		// 关于引用元数据的处理
		if (voMeta != "" &&voMeta != null && objMeta != null && !"null".equals(voMeta) && !"null".equals(objMeta)) {
			MdDataset mdds = new MdDataset();
			mdds.setId(sourceDateset.getId());
			mdds.setLazyLoad(isLazy.booleanValue());
			mdds.setCaption(displayname);
			mdds.setVoMeta(voMeta, true);
			mdds.setObjMeta(objMeta);
			
			List<FieldRelation> fieldRelationList = new ArrayList<FieldRelation>();
			List<IRefNode> refNodeList = new ArrayList<IRefNode>();
			List<ComboData> cdList = new ArrayList<ComboData>();
			List<RefMdDataset> refMdDsList = new ArrayList<RefMdDataset>();
			IDataModelRService dataModel = ServiceFinder.find(IDataModelRService.class);// 获取元数据服务
			try {
				PropertyDO[] propDos = dataModel.findPropertyByFullClassName(voMeta, displayname, "SEQNO", null);
				setComponent(fieldRelationList, refNodeList, cdList, refMdDsList, propDos, fieldDs, mdds);
				mdds.setExtendAttribute("propDos", propDos);
			} catch (BizException | InstantiationException | IllegalAccessException e) {
				e.printStackTrace();// TODO
			}
			for (FieldRelation f : fieldRelationList) {
				mdds.getFieldRelations().addFieldRelation(f);
			}
			mdds.setCaption(sourceDateset.getCaption());
	
			sourceWidget.getViewModels().addDataset(mdds);
			sourceDateset = mdds;
			for (MdDataset refMdDs : refMdDsList) {
				sourceWidget.getViewModels().addDataset(refMdDs);
			}
			for (IRefNode refNode : refNodeList) {
				sourceWidget.getViewModels().addRefNode(refNode);
			}
			for (ComboData cd : cdList) {
				sourceWidget.getViewModels().addComboData(cd);
			}
		}
		if(datasetConf.getIsNORelation()==null||!datasetConf.getIsNORelation()){
			// 处理DatasetRelations
			String masterDsKey=datasetConf.getMasterKey();
			String masterDsId=datasetConf.getMasterDs();
			if(StringUtils.isBlank(masterDsKey)){//此处masterKey若为空，说明是主表，若不为空说明是子表
				DatasetConfigController dsCfgCtrl=	new	DatasetConfigController();
				datasetConf.setMasterKey(dsCfgCtrl.getPrimaryKey(sourceDateset));
			}
			masterDsKey=datasetConf.getMasterKey();
			
			DatasetRelation[] cntDsRs = null;
			// 原本已经存在的DsRelations
			cntDsRs = sourceWidget.getViewModels().getDsrelations().getDsRelations(masterDsId);
			{
				if(StringUtils.isNotBlank(masterDsKey)&&StringUtils.isNotBlank(masterDsId))
				{
					boolean isExist = false;
					String rowRelationId ="",detailDs="";
					if(StringUtils.isBlank(datasetConf.getTreePTabDs())){
						 rowRelationId ="rel"+datasetConf.getDsId();
						 detailDs = datasetConf.getDsId();
					}else{
						//说明是树 的 Dataset
						 setTreeLevel1(masterDsId,masterDsKey);//设置树控件的level1
						 rowRelationId ="rel"+datasetConf.getTreePTabDs();
						 detailDs =datasetConf.getTreePTabDs();
					}
					
					String detailKey = masterDsKey;
					if (cntDsRs != null) {
						for (DatasetRelation datasetRelation : cntDsRs) {
							String relationId = datasetRelation.getId();
							if (relationId.equals(rowRelationId)) {
								isExist = true;
								datasetRelation.setDetailDataset(detailDs);
								datasetRelation.setDetailForeignKey(detailKey);
							}
						}
					}
					if (!isExist) {
						DatasetRelation dsr = new DatasetRelation();
						dsr.setId(rowRelationId);
						dsr.setMasterDataset(masterDsId);
						dsr.setMasterKeyField(masterDsKey);
						dsr.setDetailDataset(detailDs);
						dsr.setDetailForeignKey(detailKey);
						allDsRs.addDsRelation(dsr);
					}
				}	
			}
		}
		if(isDataLoad)
		{
			LuiEventConf eventConf= getEventConf_OnDataLoad(id,isSetParTabStatus,sourceWidget.getController());
			addEventConfigToDataset(sourceDateset,eventConf);
		}
		if(isAfterSelRowEvent){
			LuiEventConf eventConf= getEventConf_AfterRowSelect(id,sourceWidget.getController());
			addEventConfigToDataset(sourceDateset,eventConf);
		}
		sourceDateset.setPageSize(Integer.parseInt(pageSize==null?"-1":pageSize));
		//sourceWidget.getViewModels().addDataset(sourceDateset);
	
	}
	
	private void setComponent(List<FieldRelation> fieldRelationList, List<IRefNode> refNodeList, List<ComboData> cdList, List<RefMdDataset> refMdDsList, PropertyDO[] classDos, Dataset fieldDs, MdDataset mdds) throws BizException, InstantiationException, IllegalAccessException {
		// 数据的组装
		for (int i = 0; i < classDos.length; i++) {
			PropertyDO attr = classDos[i];
			FieldRelation fr = new FieldRelation();
			fr.setId(attr.getName());
			if (attr.getDatatypestyle() == 305 && PropTypeEnum.REF.toString().equals(attr.getProptype())) {
				// dataSet中FieldRelation的设置
				MatchField matchField = new MatchField();
				String pkname = "";
				BaseDO basedo = (BaseDO) LuiClassUtil.forName(attr.getFullclassname()).newInstance();
				if (basedo != null)
					pkname = basedo.getPKFieldName();
				matchField.setReadField("Name");
				matchField.setWriteField(CommTool.firstLetterToUpperCase(attr.getName() + "_name"));
				fr.addMatchField(matchField);
				WhereField whereFeild = new WhereField();
				whereFeild.setKey(CommTool.firstLetterToUpperCase(pkname));
				whereFeild.setValue(CommTool.firstLetterToUpperCase(attr.getName()));
				fr.getWhereFieldList().add(whereFeild);
				fr.setRefDataset(CommTool.firstLetterToUpperCase(attr.getName()));
				fieldRelationList.add(fr);
				// 参照映射
				RefMdDataset refnddata = new RefMdDataset();
				refnddata.setCaption(attr.getDisplayname());
				refnddata.setId(CommTool.firstLetterToUpperCase(attr.getName()));// 与refNode一致
				refnddata.setVoMeta(attr.getFullclassname(), true);
				refMdDsList.add(refnddata);
				// 参照弹出框设置
				GenericRefNode refNode = new GenericRefNode();
				refNode.setQuickInput(false);
				refNode.setController(AppRefDftCtrl.class.getName());
				refNode.setHeight("400");
				refNode.setWidth("300");
				refNode.setTitle(attr.getDisplayname());
				BdRefInfoDO refinfodo = RefPubUtil.getRefinfoVO(attr.getFullclassname(), attr.getDatatype());
				if (refinfodo == null)
					continue;
				
			// DataModels dataModels=	sourceWidget.getViewModels();
				String refNodeId=refinfodo.getCode()+"_"+mdds.getId()+"_"+attr.getId().replace('-', '_');
				refNode.setId(refNodeId);
				refNode.setRefcode(refinfodo.getCode());
				refNode.setMultiple(false);
				refNode.setFilterNames(false);
				refNode.setPagemeta("reference");
				try {
					IRefModel refModelClass = RefPubUtil.getRefModel(attr.getFullclassname(), attr.getDatatype());
					if (refModelClass != null) {
						String writeFieldString = CommTool.firstLetterToUpperCase(attr.getName()) + "," + CommTool.firstLetterToUpperCase(attr.getName() + "_name");
						if ("bd_udidoc".equals(refinfodo.getPara2())) {
							writeFieldString = writeFieldString + "," + CommTool.firstLetterToUpperCase(attr.getName()).replaceAll("Id", "Sd");
						}
						refNode.setWriteDataset(sourceDateset.getId());
						refNode.setWriteFields(writeFieldString);
						refNode.setReadDataset("masterDs");
						refNode.setReadFields(refModelClass.getPkFieldCode() + "," + refModelClass.getRefNameField() + "," + refModelClass.getRefCodeField());
//						if (!refNodeList.contains(refNode)) {
//							refNodeList.add(refNode);
//						}
						refNodeList.add(refNode);
					}
				} catch (Throwable e) {
					LuiLogger.error(e.getMessage(), e);
				}
			} else if (attr.getDatatypestyle() == 305 && PropTypeEnum.ENUM.toString().equals(attr.getProptype())) {
				DataList dataList = new DataList();
				dataList.setId(StringUtils.capitalize(attr.getName()) + "_combox");
				dataList.setCaption(attr.getDisplayname());
				IDataModelRService impl = ServiceFinder.find(IDataModelRService.class);
				EnumValueDO[] valueDos = impl.findEnumVlaueByCond(" id = '" + attr.getDatatype() + "'", "", Boolean.FALSE);
				for (int j = 0; j < valueDos.length; j++) {
					EnumValueDO valueDo = valueDos[j];
					String value = valueDo.getValue();
					String text = valueDo.getName();
					DataItem dataitem = new DataItem();
					dataitem.setText(text);
					dataitem.setValue(value);
					dataList.addDataItem(dataitem);
				}
				if (!cdList.contains(dataList)) {
					cdList.add(dataList);
				}
			} else {
				continue;
			}
		}
	}
	public void configDatasetRelation(ViewPartMeta sourceWidget,List<Map<String,String>>listMap){
		DatasetRelations allDsRs = sourceWidget.getViewModels().getDsrelations();
		for(Map<String,String>map:listMap){
			// 处理DatasetRelations
			String masterDsKey=map.get("masterKey");
			String masterDsId=map.get("masterDsId");
			DatasetRelation[] cntDsRs = null;
			// 原本已经存在的DsRelations
			cntDsRs = sourceWidget.getViewModels().getDsrelations().getDsRelations(masterDsId);
			{
				if(StringUtils.isNotBlank(masterDsKey)&&StringUtils.isNotBlank(masterDsId))
				{
					boolean isExist = false;
					String rowRelationId ="",detailDs="";
					if(StringUtils.isNotBlank(map.get("isTree"))){//说明是树 的 Dataset
						 setTreeLevel1(masterDsId,masterDsKey);//设置树控件的level1
						 rowRelationId ="rel"+map.get("detailDsId");
						 detailDs =map.get("detailDsId");
					}else{
						 rowRelationId ="rel"+map.get("detailDsId");
						 detailDs = map.get("detailDsId");
					}
					
					String detailKey = map.get("detailKey");
					if (cntDsRs != null) {
						for (DatasetRelation datasetRelation : cntDsRs) {
							String relationId = datasetRelation.getId();
							if (relationId.equals(rowRelationId)) {
								isExist = true;
								datasetRelation.setDetailDataset(detailDs);
								datasetRelation.setDetailForeignKey(detailKey);
							}
						}
					}
					if (!isExist) {
						DatasetRelation dsr = new DatasetRelation();
						dsr.setId(rowRelationId);
						dsr.setMasterDataset(masterDsId);
						dsr.setMasterKeyField(masterDsKey);
						dsr.setDetailDataset(detailDs);
						dsr.setDetailForeignKey(detailKey);
						allDsRs.addDsRelation(dsr);
					}
				}	
			}
		}
	}
	
	//添加配置好的事件到 该添加的Dataset 或者 控件里
	private void addEventConfigToDataset(Dataset editor,LuiEventConf eventConf){
		String methodName=eventConf.getMethod();
		String eventName=eventConf.getName();
		editor.removeEventConf(eventName, methodName);
		editor.addEventConf(eventConf);
	}
	private void addEventConifgToCtrlItem(ToolBarItem editor,LuiEventConf eventConf){
		String methodName=eventConf.getMethod();
		String eventName=eventConf.getName();
		editor.removeEventConf(eventName, methodName);
		editor.addEventConf(eventConf);
	}
	private void addEventConifgToCtrlItem(MenuItem editor,LuiEventConf eventConf){
		String methodName=eventConf.getMethod();
		String eventName=eventConf.getName();
		editor.removeEventConf(eventName, methodName);
		editor.addEventConf(eventConf);
	}
	private void addEventConifgToCtrlItem(ButtonComp editor,LuiEventConf eventConf){
		String methodName=eventConf.getMethod();
		String eventName=eventConf.getName();
		editor.removeEventConf(eventName, methodName);
		editor.addEventConf(eventConf);
	}
	private  LuiEventConf buildLuiEventConf(String eventName,String eventType,String method,String controller,List<LuiParameter> listPara,String modelCmd){
		LuiEventConf eventConf = new LuiEventConf();
		eventConf.setId(eventName);
		eventConf.setEventName(eventName);
		eventConf.setEventType(eventType);
		eventConf.setMethod(method);
		eventConf.setControllerClazz(controller);
		eventConf.setOnserver(Boolean.valueOf(true));
		eventConf.setAsync(Boolean.valueOf(true));
		for(LuiParameter para:listPara){
			eventConf.addExtendParam(para);
		}
		eventConf.setModelCmd(modelCmd);
		eventConf.setuIStateId(null);
		return eventConf;
	}
	private LuiEventConf getEventConf_OnDataLoad(String operDsId,Boolean isSetParTabStatus,String controller){
		String eventName="onDataLoad";
		String eventType="DatasetEvent";
		String  method=operDsId+"_onDataLoad";
		String  modelCmd="LuiDatasetLoadCmd";
		List<LuiParameter> listPara=new ArrayList<LuiParameter>();
		 {  
			LuiParameter para=new LuiParameter();
		 	para.setName("OperatorDs_Exattr");
		 	para.setValue(operDsId);
		 	listPara.add(para);
		 }
		 if(isSetParTabStatus)
		 {  
			LuiParameter para=new LuiParameter();
		 	para.setName("ToolBarStatus_Exattr");
		 	para.setValue(isSetParTabStatus?"true":"false");
		 	listPara.add(para);
		 }
		return buildLuiEventConf(eventName,eventType,method,controller,listPara,modelCmd);
		
	}
	public void setEventConf_OnFormLoad(ViewPartMeta viewPartMeta,String operDsId){
		String eventName="onDataLoad";
		String eventType="DatasetEvent";
		String  method=operDsId+"_onDataLoad";
		String  modelCmd="LuiFormLoadCmd";
		List<LuiParameter> listPara=new ArrayList<LuiParameter>();
		 {  
			LuiParameter para=new LuiParameter();
		 	para.setName("OperatorDs_Exattr");
		 	para.setValue(operDsId);
		 	listPara.add(para);
		 }
		String controller=viewPartMeta.getController();
		LuiEventConf eventConf= buildLuiEventConf(eventName,eventType,method,controller,listPara,modelCmd);
		Dataset ds=viewPartMeta.getViewModels().getDataset(operDsId);
		addEventConfigToDataset(ds,eventConf);
		
	}
	public void setEventConf_AddOrEditBeforeShow(ViewPartMeta viewPartMeta,String operDsId){
		String eventName="onDataLoad";
		String eventType="DatasetEvent";
		String  method=operDsId+"_onDataLoad";
		String  modelCmd="LuiAddOrEditBeforeShowCmd";
		List<LuiParameter> listPara=new ArrayList<LuiParameter>();
		 {  
			LuiParameter para=new LuiParameter();
		 	para.setName("OperatorDs_Exattr");
		 	para.setValue(operDsId);
		 	listPara.add(para);
		 }
		String controller=viewPartMeta.getController();
		LuiEventConf eventConf= buildLuiEventConf(eventName,eventType,method,controller,listPara,modelCmd);
		Dataset ds=viewPartMeta.getViewModels().getDataset(operDsId);
		addEventConfigToDataset(ds,eventConf);
		
	}
	
	public void setEventConf_PopUpWinButtonSave(ViewPartMeta viewPartMeta,Map<String,String>map){
		String cardLayoutId=map.get("cardLayoutId");
		String masterDsId= map.get("dsId");
		String detailDsIds= map.get("dsId_Chis");
		String aggDo= map.get("aggDo");
		String btnId=map.get("btnId");
		String eventName="onclick";
		String eventType="MouseEvent";
		String  method=btnId+"_onClick";
		String  modelCmd="LuiSaveAggCmd";
		
		List<LuiParameter> listPara=new ArrayList<LuiParameter>();
		 {  
			LuiParameter para=new LuiParameter();
		 	para.setName("isEditView_Exattr");
		 	para.setValue("true");
		 	listPara.add(para);
		 }
		 {  
			LuiParameter para=new LuiParameter();
		 	para.setName("parentViewId_Exattr");
		 	para.setValue("main");
		 	listPara.add(para);
		 }
		 {  
			LuiParameter para=new LuiParameter();
		 	para.setName("parentDsId_Exattr");
		 	para.setValue(masterDsId);
		 	listPara.add(para);
		 }
		 {  
			LuiParameter para=new LuiParameter();
		 	para.setName("parentDetailDsIds_Exattr");
		 	para.setValue(detailDsIds);
		 	listPara.add(para);
		 }
		 
		 if(StringUtils.isNotBlank(cardLayoutId))
		 {
			 LuiParameter para=new LuiParameter();
		 	 para.setName("CardLayoutIds_Exattr");
		 	 para.setValue(cardLayoutId);
		 	 listPara.add(para);
		 }
		 if(StringUtils.isNotBlank(aggDo))
		 {
			 LuiParameter para=new LuiParameter();
		 	 para.setName("aggVoClazz_Exattr");
		 	 para.setValue(aggDo);
		 	 listPara.add(para);
		 }
		String controller=viewPartMeta.getController();
		LuiEventConf eventConf= buildLuiEventConf(eventName,eventType,method,controller,listPara,modelCmd);
		if(StringUtils.isNotBlank(detailDsIds)){
			EventSubmitRule submitRule= getParentSubmitRule(masterDsId, detailDsIds);
			eventConf.setSubmitRule(submitRule);
		}
		ButtonComp button=(ButtonComp)viewPartMeta.getViewComponents().getComponent(btnId);
		addEventConifgToCtrlItem(button, eventConf);
	}
	public void setEventConf_Par_PopUpWinButtonSave(ViewPartMeta viewPartMeta,Map<String,String>map){
		String masterDsId= map.get("dsId");
		String btnId=map.get("btnId");
		String eventName="onclick";
		String eventType="MouseEvent";
		String  method=btnId+"_onClick";
		String  modelCmd="LuiSaveCmd";
		
		List<LuiParameter> listPara=new ArrayList<LuiParameter>();
		 {  
			LuiParameter para=new LuiParameter();
		 	para.setName("isEditView_Exattr");
		 	para.setValue("true");
		 	listPara.add(para);
		 }
		 {  
			LuiParameter para=new LuiParameter();
		 	para.setName("parentViewId_Exattr");
		 	para.setValue("main");
		 	listPara.add(para);
		 }
		 {  
			LuiParameter para=new LuiParameter();
		 	para.setName("parentDsId_Exattr");
		 	para.setValue(masterDsId);
		 	listPara.add(para);
		 }
		 
		String controller=viewPartMeta.getController();
		LuiEventConf eventConf= buildLuiEventConf(eventName,eventType,method,controller,listPara,modelCmd);
		EventSubmitRule submitRule= getSubmitRule(masterDsId, "");
		eventConf.setSubmitRule(submitRule);
		ButtonComp button=(ButtonComp)viewPartMeta.getViewComponents().getComponent(btnId);
		addEventConifgToCtrlItem(button, eventConf);
	}
	
	public void setEventConf_PopUpWinButtonCancel(ViewPartMeta viewPartMeta,Map<String,String>map){
		String viewId= map.get("viewId");
		String btnId=map.get("btnId");
		String eventName="onclick";
		String eventType="MouseEvent";
		String  method=btnId+"_onClick";
		String  modelCmd="LuiCloseViewCmd";
		List<LuiParameter> listPara=new ArrayList<LuiParameter>();
		 {  
			LuiParameter para=new LuiParameter();
		 	para.setName("viewId_Exattr");
		 	para.setValue(viewId);
		 	listPara.add(para);
		 }
		String controller=viewPartMeta.getController();
		LuiEventConf eventConf= buildLuiEventConf(eventName,eventType,method,controller,listPara,modelCmd);
		ButtonComp button=(ButtonComp)viewPartMeta.getViewComponents().getComponent(btnId);
		button.addEventConf(eventConf);
		
	}
	

	
	private LuiEventConf getEventConf_AfterRowSelect(String operDsId,String controller) {
		String eventType="DatasetEvent";
		String eventName="onAfterRowSelect";
		String method=operDsId+"_onAfterRowSelect";
		String modelCmd="LuiDatasetAfterSelectCmd"; 
		List<LuiParameter> listPara=new ArrayList<LuiParameter>();
		 {  
			LuiParameter para=new LuiParameter();
		 	para.setName("OperatorDs_Exattr");
		 	para.setValue(operDsId);
		 	listPara.add(para);
		 }
		return buildLuiEventConf(eventName, eventType, method, controller, listPara, modelCmd);
	}
	
	/**
	 * 配置 单表 卡片编辑toolBar事件
	 * @param operDsId
	 * @param toolBarComp
	 * @param cardLayoutId
	 * @param controller
	 */
	public void setEventConf_sigTabCardToolBar(String operDsId,ToolBarComp toolBarComp,String cardLayoutId,String controller) {
	
		List<LuiParameter> listPara=new ArrayList<LuiParameter>();
		 {  
			LuiParameter para=new LuiParameter();
		 	para.setName("OperatorDs_Exattr");
		 	para.setValue(operDsId);
		 	listPara.add(para);
		 }
		 {  
			LuiParameter para=new LuiParameter();
		 	para.setName("CardLayoutId_Exattr");
		 	para.setValue(cardLayoutId);
		 	listPara.add(para);
		 }
		 {  
			LuiParameter para=new LuiParameter();
		 	para.setName("ToolBarCompId_Exattr");
		 	para.setValue(toolBarComp.getId());
		 	listPara.add(para);
		 }
		ToolBarItem[] toolBarItems=toolBarComp.getElements();
		for(ToolBarItem toolBarItem:toolBarItems){
			String toolBarItemId=toolBarItem.getId();
			String modelCmd="";//模式化命令
			String eventName="onclick";
			String method=toolBarItemId+"_onClick";
			String eventType="MouseEvent";
			
			//ToolBarComp工具栏，即工具栏项目的onClick事件
			 if(toolBarItemId.endsWith(GuidedCfgController.ctrlItem_ToolBar_new))
				 modelCmd="LuiAddCardLayoutCmd";
			 else if(toolBarItemId.endsWith(GuidedCfgController.ctrlItem_ToolBar_edit))
				 modelCmd="LuiEditCardLayoutCmd"; 
			 else if(toolBarItemId.endsWith(GuidedCfgController.ctrlItem_ToolBar_delete))
				 modelCmd="LuiDelBaseDOCmd";
			 else if(toolBarItemId.endsWith(GuidedCfgController.ctrlItem_ToolBar_save)){
				 modelCmd="LuiCardSaveDOCmd";
			 	 toolBarItem.setVisible(false);
			 }
				
			 else if(toolBarItemId.endsWith(GuidedCfgController.ctrlItem_ToolBar_cancel)){
				 modelCmd="LuiCancelCardLayoutCmd";
				 toolBarItem.setVisible(false);
			 }
				
			LuiEventConf eventConf=buildLuiEventConf(eventName, eventType, method, controller, listPara, modelCmd);
			addEventConifgToCtrlItem(toolBarItem, eventConf);
		}
	}
	/**
	 * 配置主子表 主表卡片编辑 toolBar事件
	 *ToolBarComp的ToolBarItem事件 
	 * @param toolBarComp
	 * @param controller
	 * @param map
	 */
	public void setEventConf_parChiTabCardToolBar(ToolBarComp toolBarComp,String controller,Map<String,String> map){
		String masterDsId=map.get("dsId");
		String detailDsIds=map.get("dsId_Chis");
		List<LuiParameter> listPara=new ArrayList<LuiParameter>();
		 {  
			LuiParameter para=new LuiParameter();
		 	para.setName("masterDsId_Exattr");
		 	para.setValue(masterDsId);
		 	listPara.add(para);
		 }
		 {  
			LuiParameter para=new LuiParameter();
		 	para.setName("ToolBarCompId_Exattr");
		 	para.setValue(toolBarComp.getId());
		 	listPara.add(para);
		 }
		 String modelCmd="";//模式化命令
		 String eventName="onclick";
		 String eventType="MouseEvent";
		 ToolBarItem[] toolBarItems=toolBarComp.getElements();
		 for(ToolBarItem toolBarItem:toolBarItems){
			 EventSubmitRule submitRule=null;
			String toolBarItemId=toolBarItem.getId();
			String method=toolBarItemId+"_onClick";
			Boolean isReConfigEvent=false;
		 //卡片
		  if(toolBarItemId.endsWith(GuidedCfgController.ctrlItem_ToolBar_delete)){
			 {  
				LuiParameter para=new LuiParameter();
			 	para.setName("aggVoClazz_Exattr");
			 	para.setValue(map.get("aggDo"));
			 	listPara.add(para);
			 }
			 modelCmd="LuiDelAggDOCmd";
			 isReConfigEvent=true;
		 }else if(toolBarItemId.endsWith(GuidedCfgController.ctrlItem_ToolBar_save)){
			 submitRule= getSubmitRule(masterDsId, detailDsIds);
			 {  
				LuiParameter para=new LuiParameter();
			 	para.setName("detailDsIds_Exattr");
			 	para.setValue(map.get("dsId_Chis"));
			 	listPara.add(para);
			 }
			 {  
				LuiParameter para=new LuiParameter();
			 	para.setName("aggVoClazz_Exattr");
			 	para.setValue(map.get("aggDo"));
			 	listPara.add(para);
			 }
			 {  
				LuiParameter para=new LuiParameter();
			 	para.setName("CardLayoutIds_Exattr");
			 	para.setValue(map.get("cardLayoutId"));
			 	listPara.add(para);
			 }
			 {  
				LuiParameter para=new LuiParameter();
			 	para.setName("bodyNotNull_Exattr");
			 	para.setValue("true");
			 	listPara.add(para);
			 }
			 modelCmd="LuiSaveAggCmd";
			 isReConfigEvent=true;
		  }
		  if(isReConfigEvent){
			 LuiEventConf eventConf=buildLuiEventConf(eventName, eventType, method, controller, listPara, modelCmd);
			 if(submitRule!=null)eventConf.setSubmitRule(submitRule);
			 addEventConifgToCtrlItem(toolBarItem, eventConf);
		  }
		}
	}
	//提交规则
	private EventSubmitRule getSubmitRule(String masterDsId,String detailDsIds){
		EventSubmitRule submitRule=new EventSubmitRule();
		 
		 WidgetRule viewPartRule=new WidgetRule();
		 viewPartRule.setCardSubmit(false);
		 viewPartRule.setId("main");
		 
		 DatasetRule datasetRule=new DatasetRule();
		 datasetRule.setId(masterDsId);
		 datasetRule.setType("ds_current_line");
		 viewPartRule.addDsRule(datasetRule);
		 for(String detailDsId:detailDsIds.split(",")){
			 DatasetRule datasetRule_=new DatasetRule();
			 datasetRule_.setId(detailDsId);
			 datasetRule_.setType("ds_all_line");
			 viewPartRule.addDsRule(datasetRule_);
		 }
		 
		 submitRule.addWidgetRule(viewPartRule);
		 
		 return submitRule;
	}
	private EventSubmitRule getParentSubmitRule(String masterDsId,String detailDsIds){
		EventSubmitRule submitRule=new EventSubmitRule();
		 
		 WidgetRule viewPartRule=new WidgetRule();
		 viewPartRule.setCardSubmit(false);
		 viewPartRule.setId("main");
		 
		 for(String detailDsId:detailDsIds.split(",")){
			 DatasetRule datasetRule_=new DatasetRule();
			 datasetRule_.setId(detailDsId);
			 datasetRule_.setType("ds_all_line");
			 viewPartRule.addDsRule(datasetRule_);
		 }
		 submitRule.addWidgetRule(viewPartRule);
		 
		 EventSubmitRule parentSubmitRule=new EventSubmitRule();
		 parentSubmitRule.setCardSubmit(false);
		 
		 WidgetRule pViewPartRule=new WidgetRule();
		 pViewPartRule.setId("edit");
		 parentSubmitRule.addWidgetRule(pViewPartRule);
		 
		 DatasetRule datasetRule_=new DatasetRule();
		 datasetRule_.setId(masterDsId);
		 datasetRule_.setType("ds_current_line");
		 pViewPartRule.addDsRule(datasetRule_);
		 
		 submitRule.setParentSubmitRule(parentSubmitRule);
		 
		 return submitRule;
	}
	
	public void setEventConf_sigTabPopUpToolBar(ToolBarComp toolBarComp,Map<String,String>map) {
		List<LuiParameter> listPara=new ArrayList<LuiParameter>();
		ToolBarItem[] toolBarItems=toolBarComp.getElements();
		String controller=map.get("controller");
		for(ToolBarItem toolBarItem:toolBarItems){
			String toolBarItemId=toolBarItem.getId();
			String modelCmd="";//模式化命令
			String eventName="onclick";
			String method=toolBarItemId+"_onClick";
			String eventType="MouseEvent";
			
			//ToolBarComp工具栏，即工具栏项目的onClick事件
			if(toolBarItemId.endsWith(GuidedCfgController.ctrlItem_ToolBar_delete)){
				 {  
					LuiParameter para=new LuiParameter();
				 	para.setName("OperatorDs_Exattr");
				 	para.setValue(map.get("dsId"));
				 	listPara.add(para);
				 }
				 modelCmd="LuiDelBaseDOCmd";
			}else{
				
				
				 {  
					LuiParameter para=new LuiParameter();
				 	para.setName("OperatorDs_Exattr");
				 	para.setValue(map.get("dsId"));
				 	listPara.add(para);
				 }
				 {  
					LuiParameter para=new LuiParameter();
				 	para.setName("EditViewId_Exattr");
				 	para.setValue(map.get("viewId"));
				 	listPara.add(para);
				 }
				 modelCmd="LuiAddOrEditMenuClickCmd";
				 if(toolBarItemId.endsWith(GuidedCfgController.ctrlItem_ToolBar_new))
				 {
					 {  
						LuiParameter para=new LuiParameter();
					 	para.setName("OperType_Exattr");
					 	para.setValue(LuiAddOrEditMenuClickCmd.ADD_OPER);
					 	listPara.add(para);
					 }
				 }
				 else if(toolBarItemId.endsWith(GuidedCfgController.ctrlItem_ToolBar_edit)){
					 {  
						LuiParameter para=new LuiParameter();
					 	para.setName("OperType_Exattr");
					 	para.setValue(LuiAddOrEditMenuClickCmd.EDIT_OPER);
					 	listPara.add(para);
					 }
				 }else if(toolBarItemId.endsWith(GuidedCfgController.ctrlItem_ToolBar_save)||toolBarItemId.endsWith(GuidedCfgController.ctrlItem_ToolBar_cancel)){
					 //弹出框编辑时，toolBar不需要 保存 和 取消按钮
					 toolBarComp.deleteElement(toolBarItem);
				 }
			}
			
			
			LuiEventConf eventConf=buildLuiEventConf(eventName, eventType, method, controller, listPara, modelCmd);
			addEventConifgToCtrlItem(toolBarItem, eventConf);
		}
	}
	public void setEventConf_sigTabFullToolBar(ToolBarComp toolBarComp,Map<String,String>map) {
		List<LuiParameter> listPara=new ArrayList<LuiParameter>();
		ToolBarItem[] toolBarItems=toolBarComp.getElements();
		String controller=map.get("controller");
		for(ToolBarItem toolBarItem:toolBarItems){
			String toolBarItemId=toolBarItem.getId();
			String modelCmd="";//模式化命令
			String eventName="onclick";
			String method=toolBarItemId+"_onClick";
			String eventType="MouseEvent";
			 {  
				LuiParameter para=new LuiParameter();
			 	para.setName("OperatorDs_Exattr");
			 	para.setValue(map.get("dsId"));
			 	listPara.add(para);
			 }
			 {  
				LuiParameter para=new LuiParameter();
			 	para.setName("ToolBarCompId_Exattr");
			 	para.setValue(toolBarComp.getId());
			 	listPara.add(para);
			 }
			 
			//ToolBarComp工具栏，即工具栏项目的onClick事件
			if(toolBarItemId.endsWith(GuidedCfgController.ctrlItem_ToolBar_delete))
				 modelCmd="LuiDelBaseDOCmd";
			else if(toolBarItemId.endsWith(GuidedCfgController.ctrlItem_ToolBar_new))
				 modelCmd="LuiAddRowCmd";
			else if(toolBarItemId.endsWith(GuidedCfgController.ctrlItem_ToolBar_edit))
				 modelCmd="LuiEditRowCmd";
			else if(toolBarItemId.endsWith(GuidedCfgController.ctrlItem_ToolBar_save))
			{
				modelCmd="LuiSaveCmd";
				toolBarItem.setVisible(false);
			}
			else if(toolBarItemId.endsWith(GuidedCfgController.ctrlItem_ToolBar_cancel)) //弹出框编辑时，toolBar不需要 保存 和 取消按钮
				 toolBarComp.deleteElement(toolBarItem);
			
			
			LuiEventConf eventConf=buildLuiEventConf(eventName, eventType, method, controller, listPara, modelCmd);
			addEventConifgToCtrlItem(toolBarItem, eventConf);
		}
	}
	
	public void setEventConf_parChiTabFullMenuBar(MenubarComp menuBarComp,Map<String,String> map){
		String masterDsId=map.get("dsId");
		String detailDsIds=map.get("dsId_Chis");
		EventSubmitRule submitRule=null;
		String modelCmd="";//模式化命令
		String eventName="onclick";
		String eventType="MouseEvent";
		String controller=map.get("controller");
		List<LuiParameter> listPara=new ArrayList<LuiParameter>();
		 {  
			LuiParameter para=new LuiParameter();
		 	para.setName("OperatorDs_Exattr");
		 	para.setValue(map.get("dsId"));
		 	listPara.add(para);
		 }
		 List<MenuItem> listMenuItem= menuBarComp.getMenuList();
		 {
			 MenuItem menuItm=new MenuItem();
			 menuItm.setId(ctrlItem_MenuBar_Remove);
			 menuItm.setText("移除");
			 menuItm.setImgIcon("delete.png");
			 listMenuItem.add(menuItm);
			 menuItm.setVisible(false);
		 }
		 {
			 MenuItem menuItem=new MenuItem();
			 menuItem.setId(ctrlItem_MenuBar_Save);
			 menuItem.setText("保存");
			 menuItem.setImgIcon("save.png");
			 listMenuItem.add(menuItem);
			 menuItem.setVisible(false);
		 }
		 {  
			LuiParameter para=new LuiParameter();
		 	para.setName("MenuBarCompId_Exattr");
		 	para.setValue(menuBarComp.getId());
		 	listPara.add(para);
		 }
		 
		 for(MenuItem meunItem:listMenuItem){
			String menuItemId=meunItem.getId();
			String method=menuItemId+"_onClick";
			{  
				LuiParameter para=new LuiParameter();
			 	para.setName("TabID_Exattr");
			 	para.setValue(map.get("tabCompId"));
			 	listPara.add(para);
			 }
			 {  
				LuiParameter para=new LuiParameter();
			 	para.setName("ItemDS1_Exattr");
			 	para.setValue(map.get("dsId_Chi1"));
			 	listPara.add(para);
			 }
			 {  
				String dsId_Chi2= StringUtils.isBlank(map.get("dsId_Chi2"))?"":map.get("dsId_Chi2");
				LuiParameter para=new LuiParameter();
			 	para.setName("ItemDS2_Exattr");
			 	para.setValue(dsId_Chi2);
			 	listPara.add(para);
			 }
			 {  
				 String dsId_Chi3= StringUtils.isBlank(map.get("dsId_Chi3"))?"":map.get("dsId_Chi3");
				LuiParameter para=new LuiParameter();
			 	para.setName("ItemDS3_Exattr");
			 	para.setValue(dsId_Chi3);
			 	listPara.add(para);
			 }
			 if(menuItemId.endsWith(GuidedCfgController.ctrlItem_MenuBar_new)){
				 modelCmd="LuiAddRowCmd";
			 }else if(menuItemId.endsWith(GuidedCfgController.ctrlItem_MenuBar_edit)){
				 modelCmd="LuiEditRowCmd";
			 }else if(menuItemId.endsWith(GuidedCfgController.ctrlItem_MenuBar_delete)){
				 {  
					LuiParameter para=new LuiParameter();
				 	para.setName("OperatorDs_Exattr");
				 	para.setValue(map.get("dsId_Chis"));
				 	listPara.add(para);
				 }
				 modelCmd="LuiDelTabBaseDOCmd"; 
			 }else if(StringUtils.equals(menuItemId,ctrlItem_MenuBar_Remove)){
				 {  
					LuiParameter para=new LuiParameter();
				 	para.setName("OperatorDs_Exattr");
				 	para.setValue(map.get("dsId"));
				 	listPara.add(para);
				 }
				 modelCmd="LuiRemoveRowCmd"; 
			 }else if(StringUtils.equals(menuItemId, "menuItem_Save")){
				 submitRule=getSubmitRule(masterDsId, detailDsIds);
				 {  
					LuiParameter para=new LuiParameter();
				 	para.setName("masterDsId_Exattr");
				 	para.setValue(masterDsId);
				 	listPara.add(para);
				 }
				 {  
					LuiParameter para=new LuiParameter();
				 	para.setName("detailDsIds_Exattr");
				 	para.setValue(detailDsIds);
				 	listPara.add(para);
				 }
				 {  
					LuiParameter para=new LuiParameter();
				 	para.setName("aggVoClazz_Exattr");
				 	para.setValue(map.get("aggDo"));
				 	listPara.add(para);
				 }
				 modelCmd="LuiSaveAggCmd";
			 }
			 LuiEventConf eventConf=buildLuiEventConf(eventName, eventType, method, controller, listPara, modelCmd);
			 if(submitRule!=null)
				 eventConf.setSubmitRule(submitRule);
			 addEventConifgToCtrlItem(meunItem, eventConf);
		 }
	}
	public void setEventConf_parChiTabPopUpMenuBar(MenubarComp menuBarComp,Map<String,String> map){
		String modelCmd="";//模式化命令
		String eventName="onclick";
		String eventType="MouseEvent";
		String controller=map.get("controller");
		List<LuiParameter> listPara=new ArrayList<LuiParameter>();
		 {  
			LuiParameter para=new LuiParameter();
		 	para.setName("OperatorDs_Exattr");
		 	para.setValue(map.get("dsId_Chis"));
		 	listPara.add(para);
		 }
		 {  
			LuiParameter para=new LuiParameter();
		 	para.setName("EditViewId_Exattr");
		 	para.setValue(map.get("EditViewId"));
		 	listPara.add(para);
		 }
		 {  
			LuiParameter para=new LuiParameter();
		 	para.setName("TabID_Exattr");
		 	para.setValue(map.get("tabCompId"));
		 	listPara.add(para);
		 }
		 List<MenuItem> listMenuItem= menuBarComp.getMenuList();
		 {
			 MenuItem menuItm=new MenuItem();
			 menuItm.setId(ctrlItem_MenuBar_Remove);
			 menuItm.setText("移除");
			 menuItm.setImgIcon("delete.png");
			 listMenuItem.add(menuItm);
			 menuItm.setVisible(false);
		 }
		 for(MenuItem meunItem:listMenuItem){
			String menuItemId=meunItem.getId();
			String method=menuItemId+"_onClick";
		
			 if(menuItemId.endsWith(GuidedCfgController.ctrlItem_MenuBar_new)){
				 {  
					LuiParameter para=new LuiParameter();
				 	para.setName("OperType_Exattr");
				 	para.setValue(LuiAddOrEditMenuClickCmd.ADD_OPER);
				 	listPara.add(para);
				 }
				 modelCmd="LuiAddOrEditMenuClickCmd";
			 }else if(menuItemId.endsWith(GuidedCfgController.ctrlItem_MenuBar_edit)){
				 {  
					LuiParameter para=new LuiParameter();
				 	para.setName("OperType_Exattr");
				 	para.setValue(LuiAddOrEditMenuClickCmd.EDIT_OPER);
				 	listPara.add(para);
				 }
				 modelCmd="LuiAddOrEditMenuClickCmd";
			 }else if(menuItemId.endsWith(GuidedCfgController.ctrlItem_MenuBar_delete)){
				 modelCmd="LuiDelTabBaseDOCmd"; 
			 }else if(StringUtils.equals(menuItemId,ctrlItem_MenuBar_Remove)){
				 {  
					LuiParameter para=new LuiParameter();
				 	para.setName("ItemDS1_Exattr");
				 	para.setValue(map.get("dsId_Chi1"));
				 	listPara.add(para);
				 }
				 {  
					String dsId_Chi2= StringUtils.isBlank(map.get("dsId_Chi2"))?"":map.get("dsId_Chi2");
					LuiParameter para=new LuiParameter();
				 	para.setName("ItemDS2_Exattr");
				 	para.setValue(dsId_Chi2);
				 	listPara.add(para);
				 }
				 {  
					 String dsId_Chi3= StringUtils.isBlank(map.get("dsId_Chi3"))?"":map.get("dsId_Chi3");
					LuiParameter para=new LuiParameter();
				 	para.setName("ItemDS3_Exattr");
				 	para.setValue(dsId_Chi3);
				 	listPara.add(para);
				 }
				 modelCmd="LuiRemoveRowCmd"; 
			 }	 
			 LuiEventConf eventConf=buildLuiEventConf(eventName, eventType, method, controller, listPara, modelCmd);
			 addEventConifgToCtrlItem(meunItem, eventConf);
		 }
	}
	public void setEventConf_parChiTabCardMenuBar(MenubarComp menuBarComp,Map<String,String> map){
		String masterDsId=map.get("dsId");
		String detailDsIds=map.get("dsId_Chis");
		String cardLayoutId=map.get("cardLayoutId_Chis");
		String aggDo=map.get("aggDo");
		EventSubmitRule submitRule=null;
				
		String modelCmd="";//模式化命令
		String eventName="onclick";
		String eventType="MouseEvent";
		String controller=map.get("controller");
		List<LuiParameter> listPara=new ArrayList<LuiParameter>();
		 {  
			LuiParameter para=new LuiParameter();
		 	para.setName("OperatorDs_Exattr");
		 	para.setValue(map.get("dsId_Chis"));
		 	listPara.add(para);
		 }
		 {  
			LuiParameter para=new LuiParameter();
		 	para.setName("CardLayoutIds_Exattr");
		 	para.setValue(cardLayoutId);
		 	listPara.add(para);
		 }
		 {  
			LuiParameter para=new LuiParameter();
		 	para.setName("TabID_Exattr");
		 	para.setValue(map.get("tabCompId"));
		 	listPara.add(para);
		 }
		 {  
			LuiParameter para=new LuiParameter();
		 	para.setName("MenuBarCompId_Exattr");
		 	para.setValue(menuBarComp.getId());
		 	listPara.add(para);
		 }
		 List<MenuItem> listMenuItem= menuBarComp.getMenuList();
		 {
			 MenuItem menuItm=new MenuItem();
			 menuItm.setId(ctrlItem_MenuBar_Remove);
			 menuItm.setText("移除");
			 menuItm.setImgIcon("delete.png");
			 listMenuItem.add(menuItm);
			 menuItm.setVisible(false);
		 }
		 {
			 MenuItem menuItm=new MenuItem();
			 menuItm.setId(ctrlItem_MenuBar_Save);
			 menuItm.setText("保存");
			 menuItm.setImgIcon("save.png");
			 listMenuItem.add(menuItm);
			 menuItm.setVisible(false);
		 }
		 {
			 MenuItem menuItm=new MenuItem();
			 menuItm.setId(ctrlItem_MenuBar_Ok);
			 menuItm.setText("确定");
			 menuItm.setImgIcon("save.png");
			 listMenuItem.add(menuItm);
			 menuItm.setVisible(false);
		 }
		 {
			 MenuItem menuItm=new MenuItem();
			 menuItm.setId(ctrlItem_MenuBar_Cancle);
			 menuItm.setText("取消");
			 menuItm.setImgIcon("cancel.png");
			 listMenuItem.add(menuItm);
			 menuItm.setVisible(false);
		 }
		 for(MenuItem menuItem:listMenuItem){
			String menuItemId=menuItem.getId();
			String method=menuItemId+"_onClick";
			 if(menuItemId.endsWith(GuidedCfgController.ctrlItem_MenuBar_new)){
				 modelCmd="LuiAddTabCardLayoutCmd";
			 }else if(menuItemId.endsWith(GuidedCfgController.ctrlItem_MenuBar_edit)){
				 modelCmd="LuiEditTabCardLayoutCmd";
			 }else if(menuItemId.endsWith(GuidedCfgController.ctrlItem_MenuBar_delete)){
				 modelCmd="LuiDelTabBaseDOCmd"; 
			 }else if(StringUtils.equals(menuItemId,ctrlItem_MenuBar_Remove)){
				 {  
					LuiParameter para=new LuiParameter();
				 	para.setName("ItemDS1_Exattr");
				 	para.setValue(map.get("dsId_Chi1"));
				 	listPara.add(para);
				 }
				 {  
					String dsId_Chi2= StringUtils.isBlank(map.get("dsId_Chi2"))?"":map.get("dsId_Chi2");
					LuiParameter para=new LuiParameter();
				 	para.setName("ItemDS2_Exattr");
				 	para.setValue(dsId_Chi2);
				 	listPara.add(para);
				 }
				 {  
					 String dsId_Chi3= StringUtils.isBlank(map.get("dsId_Chi3"))?"":map.get("dsId_Chi3");
					LuiParameter para=new LuiParameter();
				 	para.setName("ItemDS3_Exattr");
				 	para.setValue(dsId_Chi3);
				 	listPara.add(para);
				 }
				 modelCmd="LuiRemoveRowCmd"; 
			 }else if(StringUtils.equals(menuItemId, ctrlItem_MenuBar_Cancle)){
				 modelCmd="LuiCancelTabCardLayoutCmd";  
			 }else if(StringUtils.equals(menuItemId, ctrlItem_MenuBar_Ok)){ 
				 modelCmd="LuiOkCardLayoutCmd";  //TODO
			 }
			 else if(StringUtils.equals(menuItemId, ctrlItem_MenuBar_Save)){
				 submitRule=getSubmitRule(masterDsId, detailDsIds);
				 {  
					LuiParameter para=new LuiParameter();
				 	para.setName("masterDsId_Exattr");
				 	para.setValue(masterDsId);
				 	listPara.add(para);
				 }
				 {  
					LuiParameter para=new LuiParameter();
				 	para.setName("detailDsIds_Exattr");
				 	para.setValue(detailDsIds);
				 	listPara.add(para);
				 }
				 if(StringUtils.isNotBlank(aggDo))
				 {
					 LuiParameter para=new LuiParameter();
				 	 para.setName("aggVoClazz_Exattr");
				 	 para.setValue(aggDo);
				 	 listPara.add(para);
				 }
				 modelCmd="LuiSaveAggCmd";  
			 }	
			 
			 LuiEventConf eventConf=buildLuiEventConf(eventName, eventType, method, controller, listPara, modelCmd);
			 if(submitRule!=null)
				 eventConf.setSubmitRule(submitRule);
			 addEventConifgToCtrlItem(menuItem, eventConf);
		 }
	}
	public void setEventConf_treeTabCardTreeContextMenuComp(ContextMenuComp contextMenuComp,Map<String,String> map){
		 String modelCmd="";//模式化命令
		 String eventName="onclick";
		 String eventType="MouseEvent";
		 String controller=map.get("controller");
		 List<LuiParameter> listPara=new ArrayList<LuiParameter>();
		 {  
			LuiParameter para=new LuiParameter();
		 	para.setName("OperatorDs_Exattr");
		 	para.setValue(map.get("dsId"));
		 	listPara.add(para);
		 }
		 {  
			LuiParameter para=new LuiParameter();
		 	para.setName("EditViewId_Exattr");
		 	para.setValue(map.get("EditViewId"));
		 	listPara.add(para);
		 }

		 List<MenuItem> menuItems=contextMenuComp.getItemList();
		 for(MenuItem menuItem:menuItems){
			  String menuItemId=menuItem.getId();
			  String method=menuItemId+"_onClick";
			  if(menuItemId.endsWith(TreeTabCardOper.ctrlItem_ContextMenu_delete)){
				 {  
					LuiParameter para=new LuiParameter();
				 	para.setName("treeDsId_Exattr");
				 	para.setValue(map.get("dsId"));
				 	listPara.add(para);
				 } 
				 {  
					LuiParameter para=new LuiParameter();
				 	para.setName("masterDsId_Exattr");
				 	para.setValue(map.get("tabDsId"));
				 	listPara.add(para);
				 } 
				 {  
					LuiParameter para=new LuiParameter();
				 	para.setName("aggVoClazz_Exattr");
				 	para.setValue(map.get("aggDo"));
				 	listPara.add(para);
				 } 
				  modelCmd="LuiDelTreeCmd";
			  }else{
						
				  modelCmd="LuiAddOrEditMenuClickCmd";
				  if(menuItemId.endsWith(TreeTabCardOper.ctrlItem_ContextMenu_new)){						
						LuiParameter para=new LuiParameter();
					 	para.setName("OperType_Exattr");
					 	para.setValue(LuiAddOrEditMenuClickCmd.ADD_OPER);
					 	listPara.add(para);
				   }else if(menuItemId.endsWith(TreeTabCardOper.ctrlItem_ContextMenu_edit)){
					    LuiParameter para=new LuiParameter();
					 	para.setName("OperType_Exattr");
					 	para.setValue(LuiAddOrEditMenuClickCmd.EDIT_OPER);
					 	listPara.add(para);
				   }
			  }
			 LuiEventConf eventConf=buildLuiEventConf(eventName, eventType, method, controller, listPara, modelCmd);
			 addEventConifgToCtrlItem(menuItem, eventConf);
		}
			
//				 }else if(menuItemId.endsWith(GuidedCfgController.ctrlItem_MenuBar_delete)){
//					 modelCmd="LuiDelTabBaseDOCmd"; 
//				 }	 
		  
	}
	
	
	
	
	public String addCtrlToViewPartMeta(PagePartMeta pagePartMeta,ViewPartMeta viewPartMeta,String dsId,String ctrlType){
		LifeCyclePhase phase = RequestLifeCycleContext.get().getPhase();
		RequestLifeCycleContext.get().setPhase(LifeCyclePhase.nullstatus);
		//ViewPartMeta viewPartMeta=PaCache.getEditorViewPartMeta();
		PaPalletDsListener paPalletDsL=new PaPalletDsListener();
		Map<String,String> rs= new HashMap<String, String>();
		rs.put("ctrlDsId",dsId );
		String ctrlId="";
	    webComp=null;
		if(StringUtils.equals(ctrlType, GuidedCfgController.ctrlType_GridComp)){
			//表格控件
			webComp = paPalletDsL.genControlByDataset(viewPartMeta, rs,ctrlType);
			ctrlId="tab_"+dsId+CommTool.getRndNum(4);
		}else if(StringUtils.equals(ctrlType, TreeTabCardOper.ctrlType_TreeViewComp)){
			ctrlId="treeView_"+dsId+CommTool.getRndNum(4);
			getWebCompByType(TreeTabCardOper.ctrlType_TreeViewComp,ctrlId);
			
		}else if(StringUtils.equals(ctrlType, TreeTabCardOper.ctrlType_ToolBarComp)){
			ctrlId="toolBar_"+dsId+CommTool.getRndNum(4);
			getWebCompByType(SigTabCardOper.ctrlType_ToolBarComp,ctrlId);
			
		}else if(StringUtils.equals(ctrlType, GuidedCfgController.ctrlType_FormComp)){
			webComp = paPalletDsL.genControlByDataset(viewPartMeta, rs,ctrlType);
			ctrlId="form_"+dsId+CommTool.getRndNum(4);
		}
		else if(StringUtils.equals(ctrlType, GuidedCfgController.ctrlType_MenuBarComp)){
			ctrlId="menulBar_"+dsId+CommTool.getRndNum(4);
			getWebCompByType(ParChiTabCardFullOper.ctrlType_MenuBarComp,ctrlId);
		}else if(StringUtils.equals(ctrlType, SigTabPopUpOper.ctrlType_ButtonComp)){
			ctrlId=dsId;
			getWebCompByType(SigTabPopUpOper.ctrlType_ButtonComp,ctrlId);
			if(StringUtils.equals(ctrlId, SigTabPopUpOper.editView_btnSaveId))
			{
				((ButtonComp)webComp).setText("保存");
			}
			else if(StringUtils.equals(ctrlId, SigTabPopUpOper.editView_btnOkId)){
				((ButtonComp)webComp).setText("确定");
				((ButtonComp)webComp).setVisible(false);
			}
			else if(StringUtils.equals(ctrlId, SigTabPopUpOper.editView_btnCancelId)){
				((ButtonComp)webComp).setText("取消");
			}
		}
		webComp.setId(ctrlId);//tab_id
		
		if(StringUtils.equals(ctrlType, GuidedCfgController.ctrlType_MenuBarComp)){
			viewPartMeta.getViewMenus().addMenuBar((MenubarComp)webComp);
		}else{
			viewPartMeta.getViewComponents().addComponent(webComp);
		}
		paPalletDsL.addToUIState(pagePartMeta,viewPartMeta.getId(),webComp,ctrlType);
		RequestLifeCycleContext.get().setPhase(phase);
		return ctrlId;
	}
	
	private void getWebCompByType(String ctrlType,String ctrlId){
		PaPalletDsListener paPalletDsL=new PaPalletDsListener();
		Map<String, Object[]> map =PaPalletDsListener.getAttrForTreeNodeMap();
		Object[] treeNodeParam = map.get(ctrlType);
		webComp = LuiClassUtil.newInstance((Class<WebComp>)treeNodeParam[5]);
		paPalletDsL.genChildrenComps(webComp, ctrlType, ctrlId);
	}
	
	public void saveFiles(CommBaseCreateFile files){
		{
			PagePartMeta pagePartMeta=files.getPagePartMeta();
			File file=	files.createPagePartMetaFile(pagePartMeta.getId());
			String xml0 = pagePartMeta.toXml();
			try {
				FileUtils.write(file, xml0, "utf-8");
			} catch (IOException e) {
				LuiLogger.error(e.getMessage(), e);
			}
		}
		
		for(String key:files.getMapViewPartMeta().keySet()){//keySet获取到所有的Key
			{
				ViewPartMeta viewPartMeta=files.getMapViewPartMeta().get(key);
				String xml0 = viewPartMeta.toXml();
				PaCache.getInstance().pub("_viewId", key);
				File file = files.createViewPartMetaFile();
				try {
					FileUtils.write(file, xml0, "utf-8");
				} catch (IOException e) {
					LuiLogger.error(e.getMessage(), e);
				}
			}
			{
				UIPartMeta viewUiPartMeta=files.getMapViewUiPartMeta().get(key);
				String xml0 = viewUiPartMeta.toXml();
				String pageId=(String)PaCache.getInstance().get("_pageId");
				File file = files.createUIPartMetaFile(pageId, key);
				try {
					FileUtils.write(file, xml0, "utf-8");
				} catch (IOException e) {
					LuiLogger.error(e.getMessage(), e);
				}
			}
			new TopMainViewController().saveEventToJavaFile();
		}
	}
	


}
