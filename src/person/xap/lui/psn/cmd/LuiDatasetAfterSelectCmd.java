package xap.lui.psn.cmd;


import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.DatasetRelation;
import xap.lui.core.dataset.DatasetRelations;
import xap.lui.core.dataset.PaginationInfo;
import xap.lui.core.dataset.Row;
import xap.lui.core.exception.LuiBusinessException;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.model.LuiAppUtil;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.core.serializer.SuperVO2DatasetSerializer;
import xap.lui.core.util.LuiClassUtil;
import xap.mw.core.data.BaseDO;
import xap.mw.log.logging.Logger;


public class LuiDatasetAfterSelectCmd extends DatasetCmd {
	private String datasetId;
	private Dataset ds;
	private ViewPartMeta widget;
	public LuiDatasetAfterSelectCmd(String dsId) {
		this.datasetId = dsId;
	}
	public LuiDatasetAfterSelectCmd(Dataset ds,ViewPartMeta widget) {
		this.ds = ds;
		this.widget = widget;
	}  
	
	public void execute() {
		Dataset masterDs = new Dataset();
		ViewPartMeta widget = new ViewPartMeta();
		if(this.ds == null){
			widget = getLifeCycleContext().getViewContext().getView();
			masterDs = widget.getViewModels().getDataset(datasetId);
		}else {
			masterDs = ds;
			widget = this.widget;
		}
		Row masterSelecteRow = getMasterRow(masterDs);
		if(masterSelecteRow == null){
			updateButtons();
			return;
		}
		DatasetRelations dsRels = widget.getViewModels().getDsrelations();
		if(dsRels != null)
		{
			DatasetRelation[] masterRels = dsRels.getDsRelations(masterDs.getId());
			for (int i = 0; i < masterRels.length; i++) {
				DatasetRelation dr = masterRels[i];
				Dataset detailDs = widget.getViewModels().getDataset(dr.getDetailDataset());
				clearDetailDs(detailDs.getId());
				int curPageIndex = getPageIndex();
				String masterKey = dr.getMasterKeyField();
				String detailFk = dr.getDetailForeignKey();
				String keyValue = (String) masterSelecteRow.getValue(masterDs.nameToIndex(masterKey));
				boolean isNewMaster = false;
				if(keyValue == null){
					isNewMaster = true;
					keyValue = masterSelecteRow.getRowId();
				}
				String detailFkValue = keyValue;
				keyValue = chanCurrentKey(keyValue);
				
				PaginationInfo pinfo = detailDs.getPaginationInfo();
				pinfo.setPageIndex(curPageIndex);
				String clazz = detailDs.getVoMeta();
				BaseDO vo = (BaseDO) LuiClassUtil.newInstance(clazz);
				
				if(!isNewMaster)
					vo.setAttrVal(detailFk, detailFkValue);
				String wherePart = detailFk + "='" + detailFkValue+"'";
				if(postProcessQueryVO(detailDs)!=null&&postProcessQueryVO(detailDs)!=""){
					wherePart +=" and " +postProcessQueryVO(detailDs);
					detailDs.setLastCondition("");
				}
				String orderPart = postOrderPart(detailDs);
				try {
					BaseDO[] vos = queryChildVOs(pinfo, vo, wherePart, isNewMaster, orderPart);
					new SuperVO2DatasetSerializer().serialize(vos, detailDs, Row.STATE_NORMAL);
					postProcessChildRowSelect(detailDs);
					detailDs.setEdit(false);
				} 
				catch (LuiBusinessException exp) {
					Logger.error(exp.getMessage(), exp);
					throw new LuiRuntimeException("查询对象出错," + exp.getMessage() + ",ds id:" + detailDs.getId(),"查询过程出现错误");
				}
			}
		}
		updateButtons();
		//processTabCode(widget, detailDs, vo);
	}
	protected void clearDetailDs(String masterDsId){
		DatasetRelations dsRels = LuiAppUtil.getCntView().getViewModels().getDsrelations();
		DatasetRelation[] masterRels_ = dsRels.getDsRelations(masterDsId);
		for(int k=0;k<masterRels_.length;k++){
			DatasetRelation dr_ = masterRels_[k];
			Dataset detailDs_ = LuiAppUtil.getCntView().getViewModels().getDataset(dr_.getDetailDataset());
			detailDs_.clear();
		}
	}
	
	protected Row getMasterRow(Dataset masterDs) {
		return masterDs.getSelectedRow();
	}

	protected String getDatasetId(){
		return datasetId;
	}
	
	protected void modifyVos(BaseDO[] vos){
		return;
	}
	protected int getPageIndex(){
		return 0;
	}
	protected String chanCurrentKey(String keyValue){
		return keyValue;
	}
}
