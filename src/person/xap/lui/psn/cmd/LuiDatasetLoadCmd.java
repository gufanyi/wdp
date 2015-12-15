package xap.lui.psn.cmd;

import xap.lui.core.cache.PaCache;
import xap.lui.core.constant.DatasetConstant;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.Field;
import xap.lui.core.dataset.PaginationInfo;
import xap.lui.core.dataset.Row;
import xap.lui.core.exception.LuiBusinessException;
import xap.lui.core.model.LuiAppUtil;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.core.serializer.SuperVO2DatasetSerializer;
import xap.lui.core.util.ClassUtil;
import xap.mw.core.data.BaseDO;




public class LuiDatasetLoadCmd extends DatasetCmd {
	//作用未知
	public static final String OPEN_BILL_ID = "openBillId";
	
	private String datasetId;
	private Dataset ds;
	private boolean isSetToolBarStatus=false;//是否配置
	
	public LuiDatasetLoadCmd(String dsId) {
		this.datasetId = dsId;
	}
	public LuiDatasetLoadCmd(String dsId,boolean isSetToolBarStatus) {
		this.datasetId = dsId;
		this.isSetToolBarStatus=isSetToolBarStatus;
	}
	public LuiDatasetLoadCmd(Dataset ds) {
		this.ds = ds;
	}
	
	public void execute() {
		//获取数据集
		if(ds == null){
			ViewPartMeta widget = getLifeCycleContext().getViewContext().getView();
			ds = widget.getViewModels().getDataset(datasetId);
		}
		String clazz = ds.getVoMeta();
		if(clazz == null)return;
		BaseDO vo = (BaseDO) ClassUtil.newInstance(clazz);
		
		String keys = ds.getReqParameters().getParameterValue(DatasetConstant.QUERY_PARAM_KEYS);
		if(keys != null && !keys.equals("")){
			String values = ds.getReqParameters().getParameterValue(DatasetConstant.QUERY_PARAM_VALUES);
			String[] keyStrs = keys.split(",");
			String[] valueStrs = values.split(",");
			for (int i = 0; i < keyStrs.length; i++) {
				vo.setAttrVal(keyStrs[i], valueStrs[i]);
			}
		}
		String pk = ds.getReqParameters().getParameterValue(OPEN_BILL_ID);
		String primaryKey = null;
		if(pk != null){
			Field[] fields = ds.getFields();
			for (int i = 0; i < fields.length; i++) {
				if(fields[i].isPK()){
					primaryKey = fields[i].getField();
					break;
				}
			}
			vo.setPkVal(pk);
			try {
				BaseDO[] vos = queryVOs(vo.getClass(), primaryKey + "='" + pk + "'");
				new SuperVO2DatasetSerializer().serialize(vos, ds, Row.STATE_NORMAL);
				postProcessRowSelect(ds);
				return;
		
			} catch (LuiBusinessException e) {
				e.printStackTrace();
			}
		}
		String wherePart = postProcessQueryVO(ds);
		String orderPart = postOrderPart(ds);
		try {
			//ds.clear();  2015年10月30日 19:06:23 李宝亮注释，如果有这个操作，无法完成分页
			//如果确实有需要此操作，请确保分页正常
			PaginationInfo pinfo = ds.getPaginationInfo();
			BaseDO[] vos = null;
			if(orderPart == null || orderPart.equals("")){
				vos = queryVOs(pinfo, vo, wherePart);
			}
			else{
				vos = queryVOs(pinfo, vo, wherePart, orderPart);
			}
			new SuperVO2DatasetSerializer().serialize(vos, ds);
			postProcessRowSelect(ds);
		} 
		catch (LuiBusinessException e) {
			e.printStackTrace();
		}
		updateDatasetState(ds);
		updateButtons();
		
		if(isSetToolBarStatus){//当加载 主表 数据集时,置 工具栏状态 为 列表（非编辑）状态，在此处设置工具栏状态是为了当页面刷新时，工具栏状态重置
			LuiAppUtil.addAppAttr(ToolBarItemStatusCtrl.toolBarStatus,ToolBarItemStatusCtrl.parTabStatus_List);
		}
		
	}
	
	
	protected void updateDatasetState(Dataset ds){
		ds.setEdit(false);
	}
}
