package xap.lui.psn.cmd;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

import xap.lui.core.cache.CacheMgr;
import xap.lui.core.command.LuiCommand;
import xap.lui.core.comps.ToolBarComp;
import xap.lui.core.constant.ExtAttrConstants;
import xap.lui.core.dao.CRUDHelper;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.DatasetRelation;
import xap.lui.core.dataset.Row;
import xap.lui.core.device.DefaultDataValidator;
import xap.lui.core.device.IDataValidator;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.exception.LuiValidateException;
import xap.lui.core.layout.UICardLayout;
import xap.lui.core.logger.LuiLogger;
import xap.lui.core.model.AppSession;
import xap.lui.core.model.ViewPartContext;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.core.serializer.Dataset2SuperVOSerializer;
import xap.lui.core.serializer.Datasets2AggVOSerializer;
import xap.lui.core.vos.VOStatus;
import xap.mw.core.data.BaseDO;
import xap.sys.appfw.orm.model.agg.BaseAggDO;
import xap.sys.jdbc.handler.BeanListHandler;


public class LuiSaveAggCmd<T extends BaseAggDO> extends LuiCommand {
	private String masterDsId;
	private String[] detailDsIds;
	private String aggVoClazz;
	private boolean bodyNotNull;
	private String cardLayoutIds;
	private List<String> notNullBodyList;
	private String billPk;
	private boolean isEditView = false;
	private String parentViewId;
	private String parentDsId;
	private String[] parentDetailDsIds;
	private Map<String, String> checkFieldMap;
	private DatasetRelation dr;
	private ToolBarComp toolBarComp;
	private String menuBarCompId;
	public String getBillPk() {
		return billPk;
	}
	public void setBillPk(String billPk){
		this.billPk = billPk;
	}
	public List<String> getNotNullBodyList(){
		return notNullBodyList;
	}
	public void setNotNullBodyList(List<String> notNullBodyList){
		this.notNullBodyList = notNullBodyList;
	}
	public LuiSaveAggCmd(String masterDsId, String[] detailDsIds, String aggVoClazz, boolean bodyNotNull,String cardLayoutIds,ToolBarComp toolBarComp){
		this.masterDsId = masterDsId;
		this.detailDsIds = detailDsIds;
		this.aggVoClazz = aggVoClazz;
		this.bodyNotNull = bodyNotNull;
		this.cardLayoutIds = cardLayoutIds;
		this.toolBarComp=toolBarComp;
	}
	
	public LuiSaveAggCmd(String masterDsId, String[] detailDsIds, String aggVoClazz,String cardLayoutIds){//卡片编辑用
		this(masterDsId, detailDsIds, aggVoClazz, true,cardLayoutIds,null);
	}
	public LuiSaveAggCmd(String masterDsId, String[] detailDsIds, String aggVoClazz,String cardLayoutIds,String menuBarCompId){//卡片编辑 MenuBarItem 状态控制
		this(masterDsId, detailDsIds, aggVoClazz, true,cardLayoutIds,null);
		this.menuBarCompId=menuBarCompId;
	}
	public LuiSaveAggCmd(String masterDsId, String[] detailDsIds, String aggVoClazz,String cardLayoutIds,ToolBarComp toolBarComp){//卡片编辑带控制ToolBarItem 状态
		this(masterDsId, detailDsIds, aggVoClazz, true,cardLayoutIds,toolBarComp);
	}
	
	public LuiSaveAggCmd(String masterDsId2, String[] detailDsIds2, String aggVoClazz2) {//全表编辑用
		this(masterDsId2, detailDsIds2, aggVoClazz2, true,"",null);
	}
	public LuiSaveAggCmd(String masterDsId2, String[] detailDsIds2, String aggVoClazz2,ToolBarComp toolBarComp){//全表编辑带ToolBarItem状态
		this(masterDsId2, detailDsIds2, aggVoClazz2, true,"",toolBarComp);
	}
	public LuiSaveAggCmd(String menuBarCompId,String masterDsId2, String[] detailDsIds2, String aggVoClazz2){//全表编辑带ToolBarItem状态
		this(masterDsId2, detailDsIds2, aggVoClazz2, true,"",null);
		this.menuBarCompId=menuBarCompId;
	}
	public LuiSaveAggCmd(Boolean isEditView,String parentViewId,String parentDsId,String[] parentDetailDsIds,String aggVoClazz){//弹出框编辑用
		this(isEditView,parentViewId,parentDsId,parentDetailDsIds,aggVoClazz,"");
	}
	
	public LuiSaveAggCmd(Boolean isEditView,String parentViewId,String parentDsId,String[] parentDetailDsIds,String aggVoClazz,String cardLayoutIds){//弹出框编辑用
		this.isEditView=isEditView;
		this.parentViewId=parentViewId;
		this.parentDsId=parentDsId;
		this.masterDsId=parentDsId;
		this.parentDetailDsIds=parentDetailDsIds;
		this.detailDsIds=parentDetailDsIds;
		this.aggVoClazz=aggVoClazz;
		this.cardLayoutIds=cardLayoutIds;
	}
	
	public void execute() {
		ViewPartContext viewCtx = getLifeCycleContext().getViewContext();
		ViewPartMeta widget = viewCtx.getView();
		
		ArrayList<Dataset> detailDs = new ArrayList<Dataset>();

		Dataset masterDs =getDataSet(detailDs);
		
	//	doValidate(masterDs, detailDs);
		Datasets2AggVOSerializer ser = new Datasets2AggVOSerializer();
		Dataset[] detailDss = detailDs.toArray(new Dataset[0]);
		BaseAggDO aggVo = ser.serialize(masterDs, detailDss, aggVoClazz);
		if (aggVo == null)
			return;
		BaseAggDO newAggDO = null;//保存后的aggdo
		fillCachedDeletedVO(aggVo, detailDss);
		try {
			setBillStatus(masterDs, aggVo);
			setCheckField();
			onBeforeVOSave(aggVo);
			if (!checkBeforeVOSave(aggVo))
				return;
			newAggDO = onVoSave((T)aggVo);
		} catch (Exception e) {
			throw new LuiRuntimeException(e.getMessage());
//			dealWithException(e);
		}
		setParentView();
		if (!isEditView)
			onAfterVOSave(widget, masterDs, ser, detailDss, newAggDO);
		else {
			onUpdateParentView(ser, newAggDO);
		}
		onAfterSave(masterDs, detailDss);
		reSetCardLayout(viewCtx);//如果是卡片编辑，重置卡片为第一项
		if(this.toolBarComp!=null){
			new ToolBarItemStatusCtrl(this.toolBarComp,"save","");
		}
		if(StringUtils.isNotBlank(this.menuBarCompId))
			new MenuBarItemStatusCtrl(menuBarCompId, "save","card");
	}
	private void reSetCardLayout(ViewPartContext viewCtx){
		if(cardLayoutIds!=null){
			for(String cardId: cardLayoutIds.split(",")){
				UICardLayout  cardLayout = (UICardLayout)viewCtx.getUIMeta().findChildById(cardId);
				if(cardLayout !=  null){
					cardLayout.setCurrentItem("0");
				}
			}
		}
	}
	
	
	protected  Dataset getDataSet(ArrayList<Dataset> detailDs){
		Dataset masterDs=null;
		
		if(this.isEditView){
			
			if (StringUtils.isBlank(parentViewId))
				throw new LuiRuntimeException("父页面窗口ID不能为空");
			if (StringUtils.isBlank(parentDsId))
				throw new LuiRuntimeException("父页面主数据集ID不能为空");
			ViewPartContext parentViewCtx = getLifeCycleContext().getWindowContext().getViewContext(parentViewId);
			if (parentViewCtx == null)
				throw new LuiRuntimeException("父窗口不能为空");
			ViewPartMeta parentWidget = parentViewCtx.getView();
			Dataset parentDs = parentWidget.getViewModels().getDataset(parentDsId);
			if (parentDs == null)
				throw new LuiRuntimeException("父窗口主数据集不能为空");
			masterDs=parentDs;
			
			ViewPartContext viewCtx = getLifeCycleContext().getViewContext();
			ViewPartMeta widget = viewCtx.getView();
			if (parentDetailDsIds != null && parentDetailDsIds.length > 0) {
				for (int i = 0; i < parentDetailDsIds.length; i++) {
					Dataset ds=null;
					ds=widget.getViewModels().getDataset(parentDetailDsIds[i]);
					if(ds==null)
						ds = parentWidget.getViewModels().getDataset(parentDetailDsIds[i]);
					if (ds != null)
						detailDs.add(ds);
				}
			}
			this.dr= parentWidget.getViewModels().getDsrelations().getDsRelation(masterDsId, detailDsIds[0]);
		}else{
			ViewPartContext viewCtx = getLifeCycleContext().getViewContext();
			ViewPartMeta widget = viewCtx.getView();
			if (widget == null)
				throw new LuiRuntimeException("片段为空!");
			if (this.masterDsId == null)
				throw new LuiRuntimeException("未指定主数据集id!");
			masterDs = widget.getViewModels().getDataset(masterDsId);
			if (masterDs == null)
				throw new LuiRuntimeException("数据集为空,数据集id=" + masterDsId + "!");
			if (this.aggVoClazz == null)
				aggVoClazz = BaseAggDO.class.getName();
			List<String> idList = new ArrayList<String>();
			idList.add(masterDsId);
			if (detailDsIds != null && detailDsIds.length > 0)
				idList.addAll(Arrays.asList(detailDsIds));
			if (detailDsIds != null && detailDsIds.length > 0) {
				for (int i = 0; i < detailDsIds.length; i++) {
					Dataset ds = widget.getViewModels().getDataset(detailDsIds[i]);
					if (ds != null)
						detailDs.add(ds);
				}
			}
			this.dr= widget.getViewModels().getDsrelations().getDsRelation(masterDsId, detailDsIds[0]);
		}
		return masterDs;
		
	}
	
	protected void setParentView() {
		return;
	}
	protected void checkDupliVO(BaseDO vo) {
		StringBuffer whereSql = new StringBuffer(" 1 = 2 ");
		StringBuffer message = new StringBuffer();
		if (checkFieldMap != null && checkFieldMap.size() > 0) {
			Iterator<Entry<String, String>> it = checkFieldMap.entrySet().iterator();
			while (it.hasNext()) {
				Entry<String, String> entry = it.next();
				String key = entry.getKey();
				String value = (String) vo.getAttrVal(key);
				whereSql.append(" OR ").append(key).append(" = '").append(value).append("' ");
				message.append((String) entry.getValue());
				if (it.hasNext())
					message.append("/");
			}
			message.append("已经存在,请重新输入!");
		}
		try {
			String sql="select * from "+vo.getTableName()+" where "+whereSql.toString();
			
			Collection vos  =(Collection)CRUDHelper.getCRUDService().executeQuery(sql, new BeanListHandler(vo.getClass()));
		//	Collection vos = dao.retrieveByClause(vo.getClass(), whereSql.toString());
			if (vos == null || vos.size() == 0)
				return;
			String pk = ((BaseDO) vos.toArray(new BaseDO[] {})[0]).getPKFieldName();
			if (!StringUtils.isBlank(pk) && !pk.equals(vo.getPKFieldName()))
				throw new LuiRuntimeException(message.toString());
		} catch (Throwable e) {
			LuiLogger.error(e.getMessage());
			throw new LuiRuntimeException(e.getMessage());
		}
	}
	protected void onUpdateParentView(Datasets2AggVOSerializer ser, BaseAggDO aggVo) {
		if (StringUtils.isBlank(parentViewId))
			throw new LuiRuntimeException("父页面窗口ID不能为空");
		if (StringUtils.isBlank(parentDsId))
			throw new LuiRuntimeException("父页面主数据集ID不能为空");
		ViewPartContext parentViewCtx = getLifeCycleContext().getWindowContext().getViewContext(parentViewId);
		if (parentViewCtx == null)
			throw new LuiRuntimeException("父窗口不能为空");
		ViewPartMeta parentWidget = parentViewCtx.getView();
		Dataset parentDs = parentWidget.getViewModels().getDataset(parentDsId);
		if (parentDs == null)
			throw new LuiRuntimeException("父窗口主数据集不能为空");
		ArrayList<Dataset> parentDetailDs = null;
		Dataset[] parentDetailDss = null;
		if (parentDetailDsIds != null && parentDetailDsIds.length > 0) {
			parentDetailDs = new ArrayList<Dataset>();
			for (int i = 0; i < parentDetailDsIds.length; i++) {
				Dataset ds = parentWidget.getViewModels().getDataset(parentDetailDsIds[i]);
				if (ds != null)
					parentDetailDs.add(ds);
			}
			if (parentDetailDs != null && parentDetailDs.size() != 0)
				parentDetailDss = parentDetailDs.toArray(new Dataset[0]);
		}
//		LuiBeanUtil<BaseDO> bu = new LuiBeanUtil<BaseDO>();
		if (isAddOper()) {
//			bu.setUIID(aggVo.getParentDO(), null);
		} else {
			Row row = parentDs.getSelectedRow();
			if (row == null)
				throw new LuiRuntimeException("父页面选中行为空！");
//			bu.setUIID(aggVo.getParentDO(), row.getRowId());
		}
		
		onAfterVOSave(parentWidget, parentDs, ser, parentDetailDss, aggVo);
		ser.update(aggVo, parentDs, parentDetailDss);//更新页面Dataset数据
		reSetCardLayout(parentViewCtx);//如果是弹出框编辑，父窗体有卡片编辑，则重置父窗体卡片为第一项
		getLifeCycleContext().getViewContext().getView().getPagemeta().setHasChanged(false);
		getLifeCycleContext().getAppContext().getCurrentWindowContext().closeView(AppSession.current().getViewContext().getId());
	}
	protected void setCheckField() {
		return;
	}
	
	protected boolean isAddOper() {
		String oper = (String) getLifeCycleContext().getWindowContext().getAppAttribute(LuiAddOrEditMenuClickCmd.OPERATE_STATUS);
		if (LuiAddOrEditMenuClickCmd.ADD_OPER.equals(oper))
			return true;
		else
			return false;
	}
	
	protected void onAfterSave(Dataset masterDs, Dataset[] detailDss) {
		masterDs.setEdit(false);
		for (int i = 0; i < detailDss.length; i++) {
			detailDss[i].setEdit(false);
		}
		updateButtons();
	}
	
	protected void dealWithException(Exception e) {
		if (e instanceof LuiValidateException) {
			throw (LuiValidateException) e;
		}
		throw new LuiRuntimeException(e.getMessage());
	}
	
	protected void onAfterVOSave(ViewPartMeta widget, Dataset masterDs, Datasets2AggVOSerializer ser, Dataset[] detailDss, BaseAggDO aggVo) {
		ser.update(aggVo, masterDs, detailDss);
	}
	protected void fillCachedDeletedVO(BaseAggDO aggVo, Dataset[] detailDss) {
		BaseDO masterVO = (BaseDO) aggVo.getParentDO();
		if (CacheMgr.getSessionCache() == null)
			return;
		if (masterVO.getPkVal() == null)
			return;
		List<BaseDO> delBodyVoList = (List<BaseDO>) CacheMgr.getSessionCache().get(masterVO.getPkVal());
		if (delBodyVoList == null || delBodyVoList.size() == 0)
			return;
		for (Dataset dataset : detailDss) {
			String delRowForeignKey = masterVO.getPkVal() + "_" + dataset.getId();
			List<Row> listDelRow = (List<Row>) CacheMgr.getSessionCache().get(delRowForeignKey);
			if (listDelRow == null || listDelRow.size() == 0)
				continue;
			Dataset2SuperVOSerializer ser = new Dataset2SuperVOSerializer();
			BaseDO[] BaseDOs = ser.serialize(dataset, listDelRow.toArray(new Row[0]));
			if (aggVo instanceof BaseAggDO) {
				String tableId = null;
				Object tabcode = dataset.getExtendAttributeValue(ExtAttrConstants.TAB_CODE);
				if (tabcode != null)
					tableId = tabcode.toString();
				else {
					Object parentField = dataset.getExtendAttributeValue(ExtAttrConstants.PARENT_FIELD);
					if (parentField != null)
						tableId = parentField.toString();
				}
				if (tableId == null)
					tableId = dataset.getId();
				BaseDO[] vos = ((BaseAggDO) aggVo).getChildrenDO(tableId);
				List<BaseDO> vosList = new ArrayList<BaseDO>();
				for (BaseDO vo : vos) {
					vosList.add(vo);
				}
				vosList.addAll(Arrays.asList(BaseDOs));
				((BaseAggDO) aggVo).setChildrenVO(tableId, (BaseDO[]) vosList.toArray(new BaseDO[0]));
			} else {
				BaseDO[] vos = aggVo.getChildrenDO();
				List<BaseDO> vosList = new ArrayList<BaseDO>();
				for (int i = 0; i < vos.length; i++) {
					vosList.add(vos[i]);
				}
				vosList.addAll(Arrays.asList(BaseDOs));
				aggVo.setChildrenDO((BaseDO[]) vosList.toArray(new BaseDO[0]));
			}
			CacheMgr.getSessionCache().remove(delRowForeignKey);
		}
		BaseDO[] bodyVOs = null;
		if (aggVo instanceof BaseAggDO)
			bodyVOs = ((BaseAggDO) aggVo).getAllChildrenDO();
		else
			bodyVOs = aggVo.getChildrenDO();
		for (int i = 0; i < bodyVOs.length; i++) {
			BaseDO bodyVo = (BaseDO) bodyVOs[i];
			for (int j = 0; j < delBodyVoList.size(); j++) {
				BaseDO bodyVoChild = (BaseDO) delBodyVoList.get(j);
				if (bodyVo.getPKFieldName() != null && bodyVoChild.getPKFieldName() != null && bodyVo.getPKFieldName().equals(bodyVoChild.getPKFieldName())) {
					bodyVo.setStatus(VOStatus.DELETED);
					break;
				}
			}
		}
		CacheMgr.getSessionCache().remove(masterVO.getPKFieldName());
	}
	protected void setBillStatus(Dataset masterDs, BaseAggDO aggVo) {
		Object metaObj = masterDs.getExtendAttributeValue(ExtAttrConstants.DATASET_METAID);
		if (metaObj != null) {
		}
	}
	protected void doValidate(Dataset masterDs, List<Dataset> detailDs) throws LuiValidateException {
		ViewPartContext viewCtx = getLifeCycleContext().getViewContext();
		ViewPartMeta widget = viewCtx.getView();
		IDataValidator validator = getValidator();
		validator.validate(masterDs, widget);
		if (detailDs != null) {
			int size = detailDs.size();
			if (size > 0) {
				for (int i = 0; i < size; i++) {
					Dataset ds = detailDs.get(i);
					validator.validate(ds, widget);
					if (notNullBodyList != null && notNullBodyList.contains(ds.getId())) {
						doSingleValidateBodyNotNull(ds);
					}
				}
				if (bodyNotNull) {
					doValidateBodyNotNull(detailDs);
				}
			}
		}
	}
	
	protected void doSingleValidateBodyNotNull(Dataset detailDs) throws LuiValidateException {
		boolean hasBody = false;
		if (detailDs.getCurrentPageData() == null) {
			hasBody = false;
		}
		if (detailDs.getCurrentPageRowCount() > 0) {
			hasBody = true;
		}
		if (!hasBody) {
			throw new LuiValidateException(detailDs.getCaption() + "表体数据不能为空");
		}
	}
	protected void doValidateBodyNotNull(List<Dataset> detailDs) throws LuiValidateException {
		int size = detailDs.size();
		for (int i = 0; i < size; i++) {
			Dataset ds = detailDs.get(i);
				if (ds.getCurrentPageData() == null) {
					throw new LuiValidateException(ds.getCaption() + "的表体数据不能为空");
			}
		}
	}
	protected IDataValidator getValidator() {
		return new DefaultDataValidator();
	}

	protected void onBeforeVOSave(BaseAggDO aggVo) {
		checkDupliVO((BaseDO) aggVo.getParentDO());
	}
	
	protected boolean checkBeforeVOSave(BaseAggDO aggVo) throws Exception {
		return true;
	}
	protected T onVoSave(final T aggVo) {
		return null;
//		try {
//			return Context.run(new Callback<T>() {
//				@Override
//				public T invoke() throws Exception {
//					T[] baseAggDO = null;
//					BaseAggService<T> cpbService = new BaseAggService<T>(aggVo.getParent().getDODesc(),(Class<T>) aggVo.getClass());
//					if(aggVo.getParentDO().getPkVal() == null) {
//						aggVo.getParentDO().setStatus(DOStatus.NEW);
//						//baseAggDO = cpbService.insert(new BaseAggDO[]{aggVo});
//						T[] tt = (T[])Array.newInstance(aggVo.getClass(), 1);
//						tt[0]= aggVo;
//						baseAggDO = cpbService.insert(tt);
//					}
//					else{
//						aggVo.getParentDO().setStatus(DOStatus.UPDATED);
//						aggVo_ChiDoHandle(aggVo);
//						//baseAggDO = cpbService.update(new BaseAggDO[]{aggVo});
//						T[] tt = (T[])Array.newInstance(aggVo.getClass(), 1);
//						tt[0]= aggVo;
//						baseAggDO = cpbService.update(tt);
//					}
//					return baseAggDO[0];
//				}
//				
//			});
//		} catch (Exception e) {
//			throw new LuiRuntimeException(e.getMessage()+"数据交互错误");
//		}
	}
	
	private void aggVo_ChiDoHandle(BaseAggDO aggVo){
		String newKeyValue = (String) aggVo.getParentDO().getAttrVal(this.dr.getMasterKeyField());
		String detailForeignKey= this.dr.getDetailForeignKey();
		BaseDO[] dos=aggVo.getAllChildrenDO();
		for(BaseDO baseDo:dos){
			if(baseDo.getAttrVal(detailForeignKey)==null){
				baseDo.setAttrVal(detailForeignKey, newKeyValue);
			}
		}
	}
	
	public void setEditView(boolean isEditView) {
		this.isEditView = isEditView;
	}
	public boolean isEditView() {
		return isEditView;
	}
	public String getParentViewId() {
		return parentViewId;
	}
	public void setParentViewId(String parentViewId) {
		this.parentViewId = parentViewId;
	}
	public String getParentDsId() {
		return parentDsId;
	}
	public void setParentDsId(String parentDsId) {
		this.parentDsId = parentDsId;
	}
	public String[] getParentDetailDsIds() {
		return parentDetailDsIds;
	}
	public void setParentDetailDsIds(String[] parentDetailDsIds) {
		this.parentDetailDsIds = parentDetailDsIds;
	}
	public void setCheckFieldMap(Map<String, String> checkFieldMap) {
		this.checkFieldMap = checkFieldMap;
	}
	public Map<String, String> getCheckFieldMap() {
		if (checkFieldMap == null)
			checkFieldMap = new HashMap<String, String>();
		return checkFieldMap;
	}
}
