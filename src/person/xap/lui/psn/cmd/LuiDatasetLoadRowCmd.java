package xap.lui.psn.cmd;

import xap.lui.core.command.LuiCommand;
import xap.lui.core.dao.CRUDHelper;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.Row;
import xap.lui.core.exception.LuiBusinessException;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.core.serializer.SuperVO2DatasetSerializer;
import xap.lui.core.util.LuiClassUtil;
import xap.mw.core.data.BaseDO;
import xap.mw.log.logging.Logger;


public class LuiDatasetLoadRowCmd extends LuiCommand {

	private String datasetId;
	private String billId;
	
	
	public LuiDatasetLoadRowCmd(String dsId, String billId) {
		this.datasetId = dsId;
		this.billId = billId;
	}
	
	
	public void execute() {
		ViewPartMeta widget = getLifeCycleContext().getViewContext().getView();
		Dataset ds = widget.getViewModels().getDataset(datasetId);
		String clazz = ds.getVoMeta();
		if(clazz == null)
			return;
		BaseDO vo = (BaseDO) LuiClassUtil.newInstance(clazz);
		String pk = billId;
		if(pk != null){
			vo.setPkVal(pk);
		}
		try {
			BaseDO result = queryVO(vo);
			if(result != null){
				new SuperVO2DatasetSerializer().serialize(new BaseDO[]{result}, ds, Row.STATE_NORMAL);
			}
			postProcessRowSelect(ds);
		} 
		catch (LuiBusinessException e) {
			Logger.error(e.getMessage(), e);
			throw new LuiRuntimeException("查询对象出错," + e.getMessage() + ",ds id:" + ds.getId(),"查询过程出现错误");
		}
	}
	
	protected BaseDO queryVO(BaseDO vo) throws LuiBusinessException {
		BaseDO[] vos =(BaseDO[]) CRUDHelper.getCRUDService().queryVOs(vo, null, null);
		return (vos != null && vos.length == 1) ? vos[0] : null;
	}


	
	protected void postProcessRowSelect(Dataset ds) {
		if(ds.getCurrentPageRowCount() > 0){
			ds.setRowSelectIndex(0);
		}
		ds.setEdit(false);
	}
}
