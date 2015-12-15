package xap.lui.psn.dsmgr;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import xap.dp.dmengine.d.EnumValueDO;
import xap.dp.dmengine.d.PropTypeEnum;
import xap.dp.dmengine.d.PropertyDO;
import xap.dp.dmengine.i.IDataModelRService;
import xap.lui.core.cache.PaCache;
import xap.lui.core.common.ExtAttribute;
import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.common.LuiWebSession;
import xap.lui.core.comps.FormComp;
import xap.lui.core.comps.FormElement;
import xap.lui.core.constant.StringDataTypeConst;
import xap.lui.core.control.IWindowCtrl;
import xap.lui.core.dao.PtBaseDAO;
import xap.lui.core.dataset.ComboData;
import xap.lui.core.dataset.DataItem;
import xap.lui.core.dataset.DataList;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.DatasetRelation;
import xap.lui.core.dataset.DatasetRelations;
import xap.lui.core.dataset.Field;
import xap.lui.core.dataset.FieldRelation;
import xap.lui.core.dataset.MatchField;
import xap.lui.core.dataset.MdDataset;
import xap.lui.core.dataset.RefMdDataset;
import xap.lui.core.dataset.Row;
import xap.lui.core.dataset.WhereField;
import xap.lui.core.event.DatasetCellEvent;
import xap.lui.core.event.DatasetEvent;
import xap.lui.core.event.DialogEvent;
import xap.lui.core.event.MouseEvent;
import xap.lui.core.event.PageEvent;
import xap.lui.core.exception.LuiBusinessException;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.layout.UIElement;
import xap.lui.core.layout.UIElementFinder;
import xap.lui.core.layout.UITabComp;
import xap.lui.core.layout.UITabItem;
import xap.lui.core.logger.LuiLogger;
import xap.lui.core.model.AppContext;
import xap.lui.core.model.AppSession;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.model.LuiAppUtil;
import xap.lui.core.model.RequestLifeCycleContext;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.core.refrence.AppRefDftCtrl;
import xap.lui.core.refrence.GenericRefNode;
import xap.lui.core.refrence.IRefModel;
import xap.lui.core.refrence.IRefNode;
import xap.lui.core.refrence.RefPubUtil;
import xap.lui.core.refrence.SelfDefRefNode;
import xap.lui.core.serializer.SuperVO2DatasetSerializer;
import xap.lui.core.util.LuiClassUtil;
import xap.lui.psn.pamgr.PaEntityDsListener;
import xap.mw.core.data.BaseDO;
import xap.mw.core.data.BizException;
import xap.mw.coreitf.d.FBoolean;
import xap.mw.sf.core.util.ServiceFinder;
import xap.sys.bdrefinfo.d.BdRefInfoDO;
import xap.sys.jdbc.handler.MapListHandler;
/**
 * 表单个性化编辑器模型，数据集编辑控制类
 * 
 * @author liujmc
 */
@SuppressWarnings("rawtypes")
public class DatasetConfigController implements IWindowCtrl, Serializable {
	private static final long serialVersionUID = 7532916478964732880L;
	public static final String PARAMS_SRCWINDOW_ID = "sourceWinId";
	public static final String PARAMS_SRCVIEW_ID = "sourceView";
	public static final String PARAMS_SRCDS_ID = "dsId";
	public static final String NULL_STR = "null";
	public static final String ATTR_REFER_URL = "referer";
	public static final String CURRENT_DS_ID = "dscfgds";
	public static final String CURRENT_VIEW_ID = "main";
	public static final String FORMCOMP_ID = "dscfgform";
	public static final String FORMELEMENT_ID = "id";
	public static final String FORMELEMENT_NAME = "name";
	public static final String FORMELEMENT_LAZYLOAD = "lazyLoad";
	public static final String FORMELEMENT_DATALOADCLASS = "dataLoadClass";
	public static final String FORMELEMENT_VOMETA = "voMeta";
	public static final String FORMELEMENT_OBJMETA = "objMeta";
	private ViewPartMeta sourceWidget = null;
	private Dataset sourceDateset = null;
	private Dataset entityDateset = null;

	public void sysWindowClosed(PageEvent event) {
		LuiRuntimeContext.getWebContext().destroyWebSession();
	}

	public void initParams() {
		LuiWebSession session = LuiRuntimeContext.getWebContext().getPageWebSession();
		String dsId = session.getOriginalParameter(PARAMS_SRCDS_ID);
		String datasetType = session.getOriginalParameter("datasetType");
		HideItemByDataType(datasetType);
		this.sourceWidget = PaCache.getEditorViewPartMeta();
		if (dsId != null && this.sourceWidget != null) {
			this.sourceDateset = this.sourceWidget.getViewModels().getDataset(dsId);
		}
	}

	public void onBeforeShow(DialogEvent e) {
		DataList dscombodata = getDataList("dscombodata");
		DataList masterfield = getDataList("masterfield");
		DataList datatypefield = getDataList("datatypefield");
		
		LuiWebSession session = LuiRuntimeContext.getWebContext().getPageWebSession();
		String viewId = session.getOriginalParameter("sourceView");
		String dsId = session.getOriginalParameter("dsId");
		ViewPartMeta sourceWidget = null;
		Dataset sourceDateset = null;		
		// 源widget
		if (viewId != null) {
			sourceWidget =PaCache.getEditorViewPartMeta();
		}	
		// 源ds
		if (dsId != null && sourceWidget != null) {
			sourceDateset = sourceWidget.getViewModels().getDataset(dsId);
		}
		
		if(StringUtils.isNotBlank(dsId)){
			Dataset[] dss = sourceWidget.getViewModels().getDatasets();
			for (Dataset dataset : dss) {
				if(!(sourceDateset.getId().equals(dataset.getId())) && !(dataset instanceof RefMdDataset)){
					DataItem item = new DataItem();
					item.setText(dataset.getCaption()==null?dataset.getId():dataset.getCaption());
					item.setValue(dataset.getId());
					dscombodata.addDataItem(item);
				}
			}	
		
			List<Field> fields = sourceDateset.getFieldList();
			for (Field field : fields) {
				DataItem item = new DataItem();
				String fid = field.getId();
				String fname = field.getText();
				if(!"ts".equals(fid) && !"status".equals(fid) && !"dr".equals(fid) && !(fid.startsWith("vdef"))){
					item.setText(fid);
					item.setValue(fid);
					masterfield.addDataItem(item);
				}
			}			
		}
		
		java.lang.reflect.Field[] fields = StringDataTypeConst.class.getDeclaredFields();
		for(java.lang.reflect.Field field : fields) {
			field.setAccessible(true);
			if((field.getModifiers() & 8) == 8){
				DataItem item = new DataItem();
				try {
					item.setText((String)field.get(null));
					item.setValue((String)field.get(null));
					datatypefield.addDataItem(item);
				} catch (IllegalArgumentException | IllegalAccessException e1) {
					e1.printStackTrace();
				}
			}
		}
		
	}

	private DataList getDataList(String id) {
		DataList dl = (DataList) LuiAppUtil.getCntView().getViewModels().getComboData(id);
		return dl;
	}

	public void HideItemByDataType(String datasetType) {
		FormComp form = (FormComp) LuiAppUtil.getControl("dscfgform");
		FormElement objMeta = form.getElementById("objMeta");
		FormElement voMeta = form.getElementById("voMeta");
		UITabComp tab = (UITabComp) UIElementFinder.findElementById(LuiAppUtil.getCntViewCtx().getUIMeta(), "tag1637");
		UITabItem tabItem = tab.getTabItemById("UITabItem01635");
		UITabItem relaItem = tab.getTabItemById("UITabItem01637");
		if (StringUtils.equals("normal", datasetType)) {
			objMeta.setVisible(false);// 隐藏
			voMeta.setVisible(false);
			tabItem.setVisible(false);
		}
		LuiWebSession session = LuiRuntimeContext.getWebContext().getPageWebSession();
		String new_editstate = session.getOriginalParameter("new_edit");
		if (StringUtils.equals("new", new_editstate)) {// 新建时将关联数据集item隐藏
			relaItem.setVisible(false);
		} else {
			relaItem.setVisible(true);
		}
	}

	/**
	 * 加载数据集扩展属性
	 * 
	 * @param DatasetEvent
	 */
	public void onAttrLoad(DatasetEvent DatasetEvent) {
		// 初始化穿透参数
		initParams();
		// 获取dataLoadClass表单项
		ViewPartMeta mainWidget = getCurrentWidget();
		Dataset ds = mainWidget.getViewModels().getDataset("dsattrds");
		int idIdx = ds.nameToIndex("id");
		int descIdx = ds.nameToIndex("desc");
		int valIdx = ds.nameToIndex("val");
		if (this.sourceDateset != null) {
			Map<String, ExtAttribute> attrs = this.sourceDateset.getExtendMap();
			if (attrs != null && !attrs.isEmpty()) {
				for (ExtAttribute attrib : attrs.values()) {
					Row row = ds.getEmptyRow();
					row.setValue(idIdx, attrib.getKey());
					row.setValue(valIdx, attrib.getValue() == null ? "" : attrib.getValue().toString());
					row.setValue(descIdx, attrib.getDesc());
					ds.addRow(row);
				}
			}
		}
	}

	/**
	 * 数据加载，初始化表单数据
	 * 
	 * @param DatasetEvent
	 */
	public void onDataLoad(DatasetEvent DatasetEvent) {
		// 初始化穿透参数
		initParams();
		// 获取dataLoadClass表单项
		ViewPartMeta mainWidget = getCurrentWidget();
		FormComp form = (FormComp) mainWidget.getViewComponents().getComponent(FORMCOMP_ID);
		// 初始化表单对应的ds数据
		Dataset ds = DatasetEvent.getSource();
		// Field[] fss = ds.getFieldSet().getFields();
		Row row = ds.getEmptyRow();
		ds.addRow(row);
		String caption = null;
		String fullClassName = null;
		if (this.sourceDateset != null) {
			form.getElementById("id").setEnabled(false);
			caption = this.sourceDateset.getCaption();
			if (caption == null || StringUtils.isBlank(caption) || "null".equals(caption))
				caption = this.sourceDateset.getId();
			row.setValue(ds.nameToIndex(FORMELEMENT_ID), this.sourceDateset.getId());
			row.setValue(ds.nameToIndex(FORMELEMENT_NAME), caption);
			row.setValue(ds.nameToIndex(FORMELEMENT_LAZYLOAD), this.sourceDateset.isLazyLoad() == true ? FBoolean.TRUE.toString() : FBoolean.FALSE.toString());

			if (this.sourceDateset instanceof MdDataset) {// 元数据集操作
				fullClassName = this.sourceDateset.getVoMeta();
				row.setValue(ds.nameToIndex("voMeta"), fullClassName);
				row.setValue(ds.nameToIndex("objMeta"), ((MdDataset) this.sourceDateset).getObjMeta());
				if (((MdDataset) this.sourceDateset).getObjMeta() != null) {
					form.getElementById("objMeta").setEnabled(true);
				}
			} else {
				HideItemByDataType("normal");// 如果是普通数据集的话 dataType=normal
												// 要隐藏一些tab和文本框
				// 加载普通数据集
				Field[] fields = this.sourceDateset.getFields();

				Dataset fieldds = mainWidget.getViewModels().getDataset("fieldds");
				fieldds.clear();
				for (Field field : fields) {
					Row empRow = fieldds.getEmptyRow();
					empRow.setValue(fieldds.nameToIndex("Name"), field.getId());
					empRow.setValue(fieldds.nameToIndex("Displayname"), field.getText());
					empRow.setValue(fieldds.nameToIndex("Datatype_name"), field.getDataType());

					empRow.setValue(fieldds.nameToIndex("IsPKey"), field.isPK() ? "Y" : "N");
					empRow.setValue(fieldds.nameToIndex("Isnullable"), field.isRequire() ? "Y" : "N");
					fieldds.addRow(empRow);
				}

			}
			// 初始化关联关系
			Dataset gridDs = mainWidget.getViewModels().getDataset("relationsds");
			// 得到所有主数据集的关联
			DatasetRelations dsrs = this.sourceWidget.getViewModels().getDsrelations();
			DatasetRelation[] relations = null;
			if (dsrs != null) {
				relations = dsrs.getDsRelations(this.sourceDateset.getId());
				if (relations != null) {
					for (DatasetRelation datasetRelation : relations) {
						Row relationRow = gridDs.getEmptyRow();
						relationRow.setValue(gridDs.nameToIndex("id"), datasetRelation.getId());
						relationRow.setValue(gridDs.nameToIndex("masterds"), datasetRelation.getMasterDataset());
						relationRow.setValue(gridDs.nameToIndex("masterkey"), datasetRelation.getMasterKeyField());
						relationRow.setValue(gridDs.nameToIndex("detailds"), datasetRelation.getDetailDataset());
						{
							ComboData comboData = mainWidget.getViewModels().getComboData("otherfield");
							comboData.removeAllDataItems();
							ViewPartMeta viewPartMeta = PaCache.getEditorViewPartMeta();
							Dataset dataset = viewPartMeta.getViewModels().getDataset(datasetRelation.getDetailDataset());
							List<Field> fields = dataset.getFieldList();
							{
								for (Field inner : fields) {
									String fid = inner.getId();
									DataItem dataItem = new DataItem();
									dataItem.setText(fid);
									dataItem.setValue(fid);
									comboData.addDataItem(dataItem);
								}
							}
						}
						relationRow.setValue(gridDs.nameToIndex("detailkey"), datasetRelation.getDetailForeignKey());
						gridDs.addRow(relationRow);
					}
				}
			}
			ds.setRowSelectIndex(0);
			Dataset classDs = mainWidget.getViewModels().getDataset("classds");
			// 得到所有主数据集的关联
			Dataset classrs = this.sourceWidget.getViewModels().getDataset(this.sourceDateset.getId());
			PropertyDO[] propDos = null;
			if (classrs != null && classrs.getExtendAttribute("propDos") != null) {
				propDos = (PropertyDO[]) classrs.getExtendAttribute("propDos").getValue();
				new SuperVO2DatasetSerializer().serialize(propDos, classDs);
			}
			IDataModelRService service = ServiceFinder.find(IDataModelRService.class);
			try {
				String className = null;
				if ("xap.sys.xbd.udi.d.UdidocDO".equals(fullClassName)) {
					className = caption;
				}
				PropertyDO[] propOos = service.findPropertyByFullClassName(fullClassName, className, "SEQNO", null);// 获取元数据的所有属性
				classDs.setEdit(false);
				new SuperVO2DatasetSerializer().serialize(propOos, classDs);
			} catch (Throwable exception) {
				LuiLogger.error(exception.getMessage(), exception);
				throw new LuiRuntimeException(exception.getMessage());
			}
		}
		ds.setRowSelectIndex(0);
	}

	public DatasetConf getDatasetConf() {
		ViewPartMeta cntView = LuiAppUtil.getCntView();
		Dataset currentDs = cntView.getViewModels().getDataset(CURRENT_DS_ID);
		String id = StringUtils.defaultIfEmpty((String) currentDs.getRowValue(currentDs.nameToIndex(FORMELEMENT_ID)), "");
		String name = StringUtils.defaultIfEmpty((String) currentDs.getRowValue(currentDs.nameToIndex(FORMELEMENT_NAME)), "");
		FBoolean isLazyLoad = (FBoolean) currentDs.getRowValue(currentDs.nameToIndex(FORMELEMENT_LAZYLOAD));
		String voMeta = StringUtils.defaultIfEmpty((String) currentDs.getRowValue(currentDs.nameToIndex(FORMELEMENT_VOMETA)), "");
		String objMeta = StringUtils.defaultIfEmpty((String) currentDs.getRowValue(currentDs.nameToIndex(FORMELEMENT_OBJMETA)), "");
		String displayname = StringUtils.defaultIfEmpty((String) currentDs.getRowValue(currentDs.nameToIndex("displayname")), "");
		DatasetConf datasetConf = new DatasetConf();
		datasetConf.setDsId(id);
		datasetConf.setName(name);
		datasetConf.setDispName(displayname);
		datasetConf.setVoMeta(voMeta);
		datasetConf.setObjMeta(objMeta);
		datasetConf.setIsLasyLoad(isLazyLoad);
		return datasetConf;
	}

	/**
	 * 确定点击处理：保存属性到源ds中
	 * 
	 * @param mouseEvent
	 * @throws LuiBusinessException
	 */
	public Dataset getEditorDataset() {
		LuiWebSession session = LuiRuntimeContext.getWebContext().getPageWebSession();
		String dsId = session.getOriginalParameter(PARAMS_SRCDS_ID);
		ViewPartMeta viewPart = PaCache.getEditorViewPartMeta();
		if (dsId != null && viewPart != null) {
			return viewPart.getViewModels().getDataset(dsId);
		}
		return null;
	}

	// public DataModelCfg getEditor() {
	// LuiWebSession pageSession =
	// LuiRuntimeContext.getWebContext().getPageWebSession();
	// LifeCyclePhase phase = RequestLifeCycleContext.get().getPhase();
	// DatasetConf datasetConf = this.getDatasetConf();
	// String id = datasetConf.getDsId();
	// String name = datasetConf.getName();
	// FBoolean isLazy = datasetConf.getIsLasyLoad();
	// String voMeta = datasetConf.getVoMeta();
	// String objMeta = datasetConf.getObjMeta();
	// String displayname = datasetConf.getDispName();
	// Dataset editorDataset = this.getEditorDataset();
	// ViewPartMeta viewPart = PaCache.getEditorViewPartMeta();
	// if (editorDataset == null) {
	// editorDataset = new Dataset(id);
	// editorDataset.setEdit(false);
	// editorDataset.setWidget(viewPart);
	// pageSession.addOriginalParameter(PARAMS_SRCDS_ID, id);
	// }
	// editorDataset.setCaption(name);
	// editorDataset.setLazyLoad(isLazy.booleanValue());
	// // 判断是否新建引用元数据的依据
	// String preVoMeta = null;
	// if (editorDataset instanceof MdDataset) {
	// preVoMeta = (String) editorDataset.getVoMeta();
	// }
	// DatasetRelations allDsRs = viewPart.getViewModels().getDsrelations();
	// if (allDsRs == null) {
	// allDsRs = new DatasetRelations();
	// {
	// allDsRs.setWidget(viewPart);
	// viewPart.getViewModels().setDsrelations(allDsRs);
	// }
	// }
	// boolean flag1 = voMeta != null;
	// boolean flag2 = objMeta != null;
	// boolean flag3 = !"null".equals(voMeta);
	// boolean flag4 = !"null".equals(objMeta);
	// boolean flag5 = !voMeta.equals(preVoMeta);
	// if (flag1 && flag2 && flag3 && flag4 && flag5) {
	// MdDataset mdds = new MdDataset();
	// mdds.setId(editorDataset.getId());
	// mdds.setLazyLoad(isLazy.booleanValue());
	// mdds.setVoMeta(voMeta, true);
	// mdds.setObjMeta(objMeta);
	// List<FieldRelation> fieldRelationList = new ArrayList<FieldRelation>();
	// List<IRefNode> refNodeList = new ArrayList<IRefNode>();
	// List<ComboData> cdList = new ArrayList<ComboData>();
	// List<RefMdDataset> refMdDsList = new ArrayList<RefMdDataset>();
	// IDataModelRService dataModel =
	// ServiceFinder.find(IDataModelRService.class);// 获取元数据服务
	// try {
	// PropertyDO[] propDos = dataModel.findPropertyByFullClassName(voMeta,
	// displayname, "SEQNO", null);
	// } catch (Throwable e) {
	// LuiLogger.error(e.getMessage(), e);
	// }
	// for (FieldRelation f : fieldRelationList) {
	// mdds.getFieldRelations().addFieldRelation(f);
	// }
	// mdds.setCaption(editorDataset.getCaption());
	// mdds.setEventConfList(editorDataset.getEventConfList());
	// viewPart.getViewModels().addDataset(mdds);
	// editorDataset = mdds;
	// for (MdDataset refMdDs : refMdDsList) {
	// viewPart.getViewModels().addDataset(refMdDs);
	// }
	// for (IRefNode refNode : refNodeList) {
	// viewPart.getViewModels().addRefNode(refNode);
	// }
	// for (ComboData cd : cdList) {
	// viewPart.getViewModels().addComboData(cd);
	// }
	// }
	// return null;
	// }
	public void onOkBtnClick(MouseEvent mouseEvent) throws LuiBusinessException {
		// 初始化穿透参数
		initParams();
		LuiWebSession pageSession = LuiRuntimeContext.getWebContext().getPageWebSession();
		ViewPartMeta mainWidget = getCurrentWidget();
		Dataset fieldDs = mainWidget.getViewModels().getDataset("fieldds");
		DatasetConf datasetConf = this.getDatasetConf();
		String id = datasetConf.getDsId();
		String name = datasetConf.getName();
		FBoolean isLazy = datasetConf.getIsLasyLoad();
		String voMeta = datasetConf.getVoMeta();
		String objMeta = datasetConf.getObjMeta();
		String displayname = datasetConf.getDispName();
		LifeCyclePhase phase = RequestLifeCycleContext.get().getPhase();
		RequestLifeCycleContext.get().setPhase(LifeCyclePhase.nullstatus);
		if (StringUtils.isBlank(id)) {
			throw new LuiRuntimeException("数据集ID不能为空！");
		}
		if (StringUtils.isBlank(name)) {
			throw new LuiRuntimeException("数据集名称不能为空！");
		}
		if (sourceDateset == null) {
			sourceDateset = new Dataset(id);
			sourceDateset.setEdit(false);
			sourceDateset.setWidget(this.sourceWidget);
			pageSession.addOriginalParameter(PARAMS_SRCDS_ID, id);
		}
		sourceDateset.setCaption(name);
		sourceDateset.setLazyLoad(isLazy.booleanValue());
		// 判断是否新建引用元数据的依据
		String preVoMeta = null;
		if (sourceDateset instanceof MdDataset) {
			preVoMeta = (String) sourceDateset.getVoMeta();
		}
		DatasetRelations allDsRs = this.sourceWidget.getViewModels().getDsrelations();
		if (allDsRs == null) {
			allDsRs = new DatasetRelations();
			{
				allDsRs.setWidget(this.sourceWidget);
				this.sourceWidget.getViewModels().setDsrelations(allDsRs);
			}
		}
		// 关于引用元数据的处理
		if (voMeta != "" && voMeta != null && objMeta != null && !"null".equals(voMeta) && !"null".equals(objMeta) && !voMeta.equals(preVoMeta)) {
			MdDataset mdds = new MdDataset();
			mdds.setId(sourceDateset.getId());
			mdds.setLazyLoad(isLazy.booleanValue());
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
				throw new LuiRuntimeException("构建发错错误:"+e.getMessage());
			}
			for (FieldRelation f : fieldRelationList) {
				mdds.getFieldRelations().addFieldRelation(f);
			}
			mdds.setCaption(sourceDateset.getCaption());
			mdds.setEventConfList(sourceDateset.getEventConfList());
			this.sourceWidget.getViewModels().addDataset(mdds);
			this.sourceDateset = mdds;
			for (MdDataset refMdDs : refMdDsList) {
				this.sourceWidget.getViewModels().addDataset(refMdDs);
			}
			for (IRefNode refNode : refNodeList) {
				this.sourceWidget.getViewModels().addRefNode(refNode);
			}
			for (ComboData cd : cdList) {
				this.sourceWidget.getViewModels().addComboData(cd);
			}
		} else {// 修改元数据集或新建普通数据集
			if (!(this.sourceDateset instanceof MdDataset)) {// 修改元数据时不清空
				this.sourceDateset.clear();
				this.sourceDateset.getFieldList().clear();
			}
			Dataset fieldgrid = mainWidget.getViewModels().getDataset("fieldds");
			{
				Row[] allRows = fieldgrid.getCurrentPageData().getRows();
				if (allRows != null) {
					for (Row row : allRows) {
						Field field = new Field();
						field.setId((String) row.getValue(fieldgrid.nameToIndex("Name")));
						field.setText((String) row.getValue(fieldgrid.nameToIndex("Displayname")));
						field.setDataType((String) row.getValue(fieldgrid.nameToIndex("Datatype_name")));
						boolean isPk = StringUtils.equals((String) row.getValue(fieldgrid.nameToIndex("IsPKey")), "Y") ? true : false;// 主键
						boolean isRequire = StringUtils.equals((String) row.getValue(fieldgrid.nameToIndex("Isnullable")), "Y") ? true : false;
						field.setPK(isPk);
						field.setRequire(isRequire);// 是否为空
						this.sourceDateset.addField(field);
					}
				}
				// fieldgrid.addField(field);

			}
		}

		// 处理DatasetRelations
		DatasetRelation[] cntDsRs = null;
		// 原本已经存在的DsRelations
		cntDsRs = this.sourceWidget.getViewModels().getDsrelations().getDsRelations(this.sourceDateset.getId());
		Dataset relationsDs = mainWidget.getViewModels().getDataset("relationsds");
		{
			Row[] rows = relationsDs.getCurrentPageData().getRows();
			if (rows != null) {
				for (Row inner : rows) {
					boolean isExist = false;
					String rowRelationId = (String) inner.getValue(relationsDs.nameToIndex("id"));
					String detailDs = (String) inner.getValue(relationsDs.nameToIndex("detailds"));
					String detailKey = (String) inner.getValue(relationsDs.nameToIndex("detailkey"));
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
						dsr.setMasterDataset(this.sourceDateset.getId());
						dsr.setMasterKeyField(getPrimaryKey(sourceDateset));
						dsr.setDetailDataset(detailDs);
						dsr.setDetailForeignKey(detailKey);
						allDsRs.addDsRelation(dsr);
					}
				}
			}
		}
		sourceWidget.getViewModels().addDataset(sourceDateset);
		RequestLifeCycleContext.get().setPhase(phase);
		entityDateset = LuiAppUtil.getCntAppCtx().getWindowContext("pa").getViewContext("data").getView().getViewModels().getDataset("entityds");
		PaEntityDsListener.setModelData(entityDateset, sourceWidget);
		AppContext ctx = AppSession.current().getAppContext();
		ctx.addExecScript("var obj ={widgetid :'" + sourceWidget.getId() + "', type:'addDataset', uiid:'" + sourceDateset.getId() + "'};");
		ctx.addExecScript("parent.document.getElementById('iframe_tmp').contentWindow.datasetOper(obj,'addModel');");
		AppSession.current().getAppContext().closeWinDialog();

	}

	private void setComponent(List<FieldRelation> fieldRelationList, List<IRefNode> refNodeList, List<ComboData> cdList, List<RefMdDataset> refMdDsList, PropertyDO[] classDos, Dataset fieldDs,
			MdDataset mdds) throws BizException, InstantiationException, IllegalAccessException {
		// 数据的组装
		PtBaseDAO dao = PtBaseDAO.getIns();
		String sql = null;
		for (int i = 0; i < classDos.length; i++) {
			PropertyDO attr = classDos[i];
			FieldRelation fr = new FieldRelation();
			fr.setId(attr.getName());
			boolean flag0 = attr.getDatatypestyle() == 305;
			boolean flag1 = PropTypeEnum.REF.toString().equals(attr.getProptype());
			boolean flag2 = ("de".equalsIgnoreCase(attr.getDatafrom()));
			
			if (flag0 && flag1 && flag2) {
				DataList dataList = new DataList();
				dataList.setId(StringUtils.capitalize(attr.getName()) + "_combox");
				dataList.setCaption(attr.getDisplayname());
				IDataModelRService impl = ServiceFinder.find(IDataModelRService.class);
				sql = "select * from bd_udidoc a  where  a.id_udidoclist ='" + attr.getDatatype() + "'";
				List<Map<String, Object>> result = (List<Map<String, Object>>) dao.executeQuery(sql, new MapListHandler());
				for (int j = 0; j < result.size(); j++) {
					Map<String, Object> inner = result.get(j);
					String value = (String) inner.get("code");
					String text = (String) inner.get("name");
					DataItem dataitem = new DataItem();
					dataitem.setText(text);
					dataitem.setValue(value);
					dataList.addDataItem(dataitem);
				}
				if (!cdList.contains(dataList)) {
					cdList.add(dataList);
				}
				continue;
			}

			if (flag0 && flag1) {
				// dataSet中FieldRelation的设置
				MatchField matchField = new MatchField();
				String pkname = "";
				BaseDO basedo = (BaseDO) LuiClassUtil.forName(attr.getFullclassname()).newInstance();
				if (basedo != null)
					pkname = basedo.getPKFieldName();
				matchField.setReadField("Name");
				matchField.setWriteField(toUpperCaseFirstOne(attr.getName() + "_name"));
				fr.addMatchField(matchField);
				WhereField whereFeild = new WhereField();
				whereFeild.setKey(toUpperCaseFirstOne(pkname));
				whereFeild.setValue(toUpperCaseFirstOne(attr.getName()));
				fr.getWhereFieldList().add(whereFeild);
				fr.setRefDataset(toUpperCaseFirstOne(attr.getName()));
				fieldRelationList.add(fr);
				// 参照映射
				RefMdDataset refnddata = new RefMdDataset();
				refnddata.setCaption(attr.getDisplayname());
				refnddata.setId(toUpperCaseFirstOne(attr.getName()));// 与refNode一致
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
				String refNodeId=refinfodo.getCode()+"_"+mdds.getId()+"_"+attr.getId().replace('-', '_');
				refNode.setId(refNodeId);
				refNode.setRefcode(refinfodo.getCode());
				refNode.setMultiple(false);
				refNode.setFilterNames(false);
				refNode.setPagemeta("reference");
				try {
					IRefModel refModelClass = RefPubUtil.getRefModel(attr.getFullclassname(), attr.getDatatype());
					if (refModelClass != null) {
						String writeFieldString = toUpperCaseFirstOne(attr.getName()) + "," + toUpperCaseFirstOne(attr.getName() + "_name");
						if ("bd_udidoc".equals(refinfodo.getPara2())) {
							writeFieldString = writeFieldString + "," + toUpperCaseFirstOne(attr.getName()).replaceAll("Id", "Sd");
						}
						refNode.setWriteDataset(sourceDateset.getId());
						refNode.setWriteFields(writeFieldString);
						refNode.setReadDataset("masterDs");
						refNode.setReadFields(refModelClass.getPkFieldCode() + "," + refModelClass.getRefNameField() + "," + refModelClass.getRefCodeField());
						if (!refNodeList.contains(refNode)) {
							refNodeList.add(refNode);
						}
					}
				} catch (Throwable e) {
					LuiLogger.error(e.getMessage(), e);
					throw new LuiRuntimeException("构建发错错误:"+e.getMessage());
				}
				continue;
			} 
			
			
			if (attr.getDatatypestyle() == 305 && PropTypeEnum.ENUM.toString().equals(attr.getProptype())) {
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
			} 
		}
	}

	/**
	 * 取消按钮点击
	 * 
	 * @param mouseEvent
	 */
	public void onCancelClick(MouseEvent mouseEvent) {
		AppSession.current().getAppContext().closeWinDialog();
	}

	/**
	 * Tab增加
	 * 
	 * @param mouseEvent
	 */
	public void onAddRelation(MouseEvent mouseEvent) {
		UIElement ele = (UIElement) AppSession.current().getWindowContext().getUIPartMeta();
		UITabComp tab = (UITabComp) UIElementFinder.findElementById(ele, "tag1637");
		if (new Integer(1).equals(tab.getCurrentItem())) {
			onAddAttrib("fieldds");
			return;
		}
		initParams();
		String masterkey = getPrimaryKey(this.sourceDateset);
		if (masterkey == null) {
			throw new LuiRuntimeException("请先设置数据集的主键!");
		}
		ViewPartMeta mainWidget = getCurrentWidget();
		Dataset ds = mainWidget.getViewModels().getDataset("relationsds");
		ds.setEdit(true);
		Row row = ds.getEmptyRow();
		row.setRowId(UUID.randomUUID().toString());
		row.setValue(ds.nameToIndex("id"), this.sourceDateset.getId() + "_" + UUID.randomUUID().toString().substring(0, 4));
		row.setValue(ds.nameToIndex("masterds"), this.sourceDateset.getId());
		row.setValue(ds.nameToIndex("masterkey"), getPrimaryKey(this.sourceDateset));
		ds.addRow(row);
		// 选中新添加的行
		ds.setRowSelectIndex(ds.getRowIndex(row));
	}

	/**
	 * Tab删除
	 * 
	 * @param mouseEvent
	 */
	public void onDelRelation(MouseEvent mouseEvent) {
		UIElement ele = (UIElement) AppSession.current().getWindowContext().getUIPartMeta();
		UITabComp tab = (UITabComp) UIElementFinder.findElementById(ele, "tag1637");
		if ("1".equals(tab.getCurrentItem())) {
			onDelAttrib("fieldds");
			return;
		}
		initParams();
		ViewPartMeta mainWidget = getCurrentWidget();
		Dataset ds = mainWidget.getViewModels().getDataset("relationsds");
		Row selectRow = ds.getSelectedRow();
		if (selectRow == null) {
			throw new LuiRuntimeException("请选选择您要删除的行!");
		} else {
			String rowRelationId = String.valueOf(selectRow.getValue(ds.nameToIndex("id")));
			DatasetRelation dsr = this.sourceWidget.getViewModels().getDsrelations().getDsRelationById(rowRelationId);
			this.sourceWidget.getViewModels().getDsrelations().removeDsRelation(dsr);
			ds.removeRow(selectRow);
		}
	}

	/**
	 * 增加属性
	 * 
	 * @param mouseEvent
	 */
	public void onAddAttrib(String gridId) {
		initParams();
		ViewPartMeta mainWidget = getCurrentWidget();
		Dataset ds = mainWidget.getViewModels().getDataset(gridId);
		Row row = ds.getEmptyRow();
		row.setRowId(UUID.randomUUID().toString());
		ds.addRow(row);
		// 选中新添加的行
		ds.setRowSelectIndex(ds.getRowIndex(row));
	}

	public void onDelAttrib(String gridId) {
		initParams();
		ViewPartMeta mainWidget = getCurrentWidget();
		Dataset ds = mainWidget.getViewModels().getDataset(gridId);
		Row selectRow = ds.getSelectedRow();
		if (selectRow == null) {
			throw new LuiRuntimeException("请选选择您要删除的行!");
		} else {
			ds.removeRow(selectRow);
		}
	}

	public String getPrimaryKey(Dataset ds) {
		List<Field> list = ds.getFieldList();
		for (Field field : list) {
			if (field.isPK()) {
				return field.getId();
			}
		}
		return null;
	}

	public void onAfterRowSelect(DatasetEvent datasetEvent) {
		this.onAfterDataChange(null);
	}

	public void onAfterDataChange(DatasetCellEvent datasetCell) {
		// int colIndex = datasetCell.getColIndex();
		// 设置url
		ViewPartMeta mainWidget = getCurrentWidget();
		Dataset ds = mainWidget.getViewModels().getDataset("relationsds");
		// int detailIndex = ds.nameToIndex("detailds");
		// if (colIndex != detailIndex) {
		// return;
		// }
		Row row = ds.getSelectedRow();
		// {
		// String value = (String) row.getValue(colIndex);
		// if (StringUtils.isBlank(value)) {
		// return;
		// }
		// ComboData comboData =
		// mainWidget.getViewModels().getComboData("otherfield");
		// comboData.removeAllDataItems();
		// ViewPartMeta viewPartMeta = PaCache.getEditorViewPartMeta();
		// Dataset dataset = viewPartMeta.getViewModels().getDataset(value);
		// FieldSet fieldSet = dataset.getFieldSet();
		// List<Field> fields = fieldSet.getFieldList();
		// {
		// for (Field inner : fields) {
		// String fid = inner.getId();
		// // String fname = inner.getText();
		// DataItem dataItem = new DataItem();
		// dataItem.setText(fid);
		// dataItem.setValue(fid);
		// comboData.addDataItem(dataItem);
		// }
		// }
		// }
		//
		// ViewPartMeta viewPartMeta = PaCache.getEditorViewPartMeta();
		String pageId = PaCache.getEditorPageId();
		String viewId = PaCache.getEditorViewId();
		if (row == null) {
			return;
		}
		String dsId = String.valueOf(row.getValue(ds.nameToIndex("detailds")));
		SelfDefRefNode refNode = (SelfDefRefNode) (mainWidget.getViewModels().getRefNode("refnode_fields"));
		String path = refNode.getPath();
		if (path.indexOf("&currentDsId") != -1) {
			path = path.substring(0, path.indexOf("&currentDsId"));
		}
		String url = path + "&currentDsId=" + dsId + "&sourceWinId=" + pageId + "&sourceView=" + viewId + "&pi=" + UUID.randomUUID().toString();
		refNode.setPath(url);
	}

	public void setFieldData() {

	}

	public ViewPartMeta getCurrentWidget() {
		return AppSession.current().getWindowContext().getCurrentViewContext().getView();
	}

	private String toUpperCaseFirstOne(String s) {
		if (Character.isUpperCase(s.charAt(0)))
			return s;
		else
			return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
	}

	public static class DatasetConf {
		private String dsId;
		private String voMeta;
		private String objMeta;
		private FBoolean isLasyLoad;
		private String name;
		private String dispName;

		public String getDsId() {
			return dsId;
		}

		public void setDsId(String dsId) {
			this.dsId = dsId;
		}

		public String getVoMeta() {
			return voMeta;
		}

		public void setVoMeta(String voMeta) {
			this.voMeta = voMeta;
		}

		public String getObjMeta() {
			return objMeta;
		}

		public void setObjMeta(String objMeta) {
			this.objMeta = objMeta;
		}

		public FBoolean getIsLasyLoad() {
			return isLasyLoad;
		}

		public void setIsLasyLoad(FBoolean isLasyLoad) {
			this.isLasyLoad = isLasyLoad;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getDispName() {
			return dispName;
		}

		public void setDispName(String dispName) {
			this.dispName = dispName;
		}
	}

	public static class DataModelCfg {
		private List<Dataset> datasets;
		private List<IRefNode> refNodes;
		private List<ComboData> datalists;
		private List<DatasetRelation> datasetRels;

		public List<Dataset> getDatasets() {
			return datasets;
		}

		public void setDatasets(List<Dataset> datasets) {
			this.datasets = datasets;
		}

		public List<IRefNode> getRefNodes() {
			return refNodes;
		}

		public void setRefNodes(List<IRefNode> refNodes) {
			this.refNodes = refNodes;
		}

		public List<ComboData> getDatalists() {
			return datalists;
		}

		public void setDatalists(List<ComboData> datalists) {
			this.datalists = datalists;
		}

		public List<DatasetRelation> getDatasetRels() {
			return datasetRels;
		}

		public void setDatasetRels(List<DatasetRelation> datasetRels) {
			this.datasetRels = datasetRels;
		}
	}
}
