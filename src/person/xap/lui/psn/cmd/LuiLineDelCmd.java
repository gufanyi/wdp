package xap.lui.psn.cmd;

import java.util.ArrayList;
import java.util.List;

import xap.lui.core.cache.CacheMgr;
import xap.lui.core.command.LuiCommand;
import xap.lui.core.dao.LuiCRUDServiceImpl;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.DatasetRelation;
import xap.lui.core.dataset.Row;
import xap.lui.core.device.IBodyInfo;
import xap.lui.core.device.MultiBodyInfo;
import xap.lui.core.device.SingleBodyInfo;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.model.ViewPartContext;
import xap.lui.core.serializer.Dataset2SuperVOSerializer;
import xap.mw.core.data.BaseDO;


public class LuiLineDelCmd extends LuiCommand {
	private IBodyInfo bodyInfo;
	private boolean delete = false;
	public LuiLineDelCmd(IBodyInfo bodyInfo) {
		this.bodyInfo = bodyInfo;
	}
	
	public void execute() {
		ViewPartContext widgetctx = getLifeCycleContext().getViewContext();
		String dsId = getSlaveDataset(widgetctx);
		Dataset ds = widgetctx.getView().getViewModels().getDataset(dsId);
		Row selRow = ds.getSelectedRow();
		if(selRow == null)
			throw new LuiRuntimeException("请选择要删除的行!");
		Dataset2SuperVOSerializer ser = new Dataset2SuperVOSerializer();
		BaseDO vo = ser.serialize(ds, selRow)[0];
		if(vo.getPkVal() == null)
			delete = true;
		if(delete)
			doDeleteVO(vo);
		else
			doCache(vo, ds,  selRow);
		if(selRow != null){
			int rowIndex = ds.getRowIndex(selRow);
			ds.removeRow(rowIndex);
			if(rowIndex > ds.getCurrentPageRowCount() - 1)
				rowIndex = ds.getCurrentPageRowCount() - 1;
			ds.setRowSelectIndex(rowIndex);
		}
		doAfterDelLine();
	}

	private void doCache(BaseDO vo, Dataset ds,  Row row) {
		ViewPartContext widgetCtx = getLifeCycleContext().getViewContext();
		String dsId = getSlaveDataset(widgetCtx);
		DatasetRelation[] rels = widgetCtx.getView().getViewModels().getDsrelations().getDsRelations();
		if(rels == null)
			return;
		String foreignKey = null;
		for (int i = 0; i < rels.length; i++) {
			DatasetRelation dsRel = rels[i];
			if(dsRel.getDetailDataset().equals(dsId)){
				foreignKey = dsRel.getDetailForeignKey();
				break;
			}
		}
		if(foreignKey == null)
			return;
		String foreignValue = (String) vo.getAttrVal(foreignKey);
		List<BaseDO> list = (List<BaseDO>) CacheMgr.getSessionCache().get(foreignValue);
		if(list == null){
			list = new ArrayList<BaseDO>();
		}
		list.add(vo);
		CacheMgr.getSessionCache().put(foreignValue, list);
		String delRowForeignKey = foreignValue + "_" + dsId;
		List<Row> listDelRow = (List<Row>) CacheMgr.getSessionCache().get(delRowForeignKey);
		if(listDelRow == null){
			listDelRow = new ArrayList<Row>();
		}
		listDelRow.add(row);
		CacheMgr.getSessionCache().put(delRowForeignKey, listDelRow);
	}

	private void doDeleteVO(BaseDO vo) {
			new LuiCRUDServiceImpl().deleteVo(vo);	
	}

	
	protected String getSlaveDataset(ViewPartContext widgetCtx) {
		String dsId = null;
		if(bodyInfo != null){
			if(bodyInfo instanceof MultiBodyInfo){
				MultiBodyInfo mbi = (MultiBodyInfo) bodyInfo;
				String bodyTabId = mbi.getBodyTabId();
			}
			else{
				SingleBodyInfo sbi = (SingleBodyInfo) bodyInfo;
				dsId = sbi.getBodyDataset();
			}
		}
		if(dsId == null)
			throw new LuiRuntimeException("没有找到待操作数据集,请确认配置正确","没有找到待操作数据集");
		return dsId;
	}
	
	protected void doAfterDelLine(){
		
	}
}
