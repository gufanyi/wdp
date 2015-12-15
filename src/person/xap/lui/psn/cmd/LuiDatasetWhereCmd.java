package xap.lui.psn.cmd;

import xap.lui.core.command.LuiCommand;
import xap.lui.core.dao.CRUDHelper;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.PaginationInfo;
import xap.lui.core.dataset.Row;
import xap.lui.core.exception.LuiBusinessException;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.core.serializer.SuperVO2DatasetSerializer;
import xap.lui.core.util.LuiClassUtil;
import xap.mw.core.data.BaseDO;
import xap.mw.log.logging.Logger;



public class LuiDatasetWhereCmd extends LuiCommand {

	private String datasetId;
	private FromWhereSQLCmd whereSql;
	public LuiDatasetWhereCmd(String dsId, FromWhereSQLCmd whereSql) {
		this.datasetId = dsId;
		this.whereSql = whereSql;
	}
	
	
	public void execute() {
		ViewPartMeta widget = getLifeCycleContext().getViewContext().getView();
		Dataset ds = widget.getViewModels().getDataset(datasetId);
		String clazz = ds.getVoMeta();
		if(clazz == null)
			return;
		BaseDO vo = (BaseDO) LuiClassUtil.newInstance(clazz);
		
		
		String orderPart = postOrderPart(vo, ds, whereSql);
		try {
			PaginationInfo pinfo = ds.getPaginationInfo();
			BaseDO[] vos = null;
			String sql = getQuerySql(vo, ds, whereSql);
			
			String wherePart = postWherePart(vo, ds, whereSql);
			if((sql != null && !sql.equals(""))){
				 if(wherePart != null && !wherePart.equals("")){
						 sql += " and (" + wherePart + ")";
				 }
			}
			if(sql != null){
				ds.setLastCondition(sql);
			}
			if(orderPart == null || orderPart.equals(""))
				vos = queryVOs(pinfo, vo, sql);
			else{
				vos = queryVOs(pinfo, vo, sql, orderPart);
				ds.setExtendAttribute("ORDER_PART", orderPart);
			}
			new SuperVO2DatasetSerializer().serialize(vos, ds, Row.STATE_NORMAL);
			postProcessRowSelect(ds);
		} 
		catch (LuiBusinessException e) {
			Logger.error(e.getMessage(), e);
			throw new LuiRuntimeException("查询对象出错," + e.getMessage() + ",ds id:" + ds.getId(),"查询过程出现错误");
		}
		ds.setEdit(false);
		updateButtons();
	}

	protected BaseDO[] queryVOs(PaginationInfo pinfo, BaseDO vo, String sql) throws LuiBusinessException{
		return (BaseDO[])CRUDHelper.getCRUDService().queryVOs(vo, pinfo, sql, null, null);
	}

	protected BaseDO[] queryVOs(PaginationInfo pinfo, BaseDO vo, String sql,
			String orderPart) throws LuiBusinessException{
		return (BaseDO[])CRUDHelper.getCRUDService().queryVOs(vo, pinfo, sql, null, orderPart);
	}

	protected String postWherePart(BaseDO vo, Dataset ds, FromWhereSQLCmd whereSql) {
		return null;
	}

	protected String getQuerySql(BaseDO vo, Dataset ds, FromWhereSQLCmd whereSql) {
		return whereSql.getWhere();
	}

	protected void postProcessRowSelect(Dataset ds) {
		
	}

	protected String postOrderPart(BaseDO vo, Dataset ds, FromWhereSQLCmd whereSql) {
		return null;
	}

 

}
