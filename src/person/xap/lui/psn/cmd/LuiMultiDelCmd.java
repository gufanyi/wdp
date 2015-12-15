package xap.lui.psn.cmd;

import xap.lui.core.command.LuiCommand;
import xap.lui.core.dao.LuiCRUDService;
import xap.lui.core.dao.LuiCRUDServiceImpl;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.Row;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.core.serializer.Dataset2SuperVOSerializer;
import xap.mw.core.data.BaseDO;
import xap.mw.sf.core.util.ServiceFinder;

public class LuiMultiDelCmd extends LuiCommand{
	
	private String masterDsId;

	public LuiMultiDelCmd(String masterDsId, String aggVoClazz){
		this.masterDsId = masterDsId;
	}
	

	@Override
	public void execute() {
		ViewPartMeta widget = getLifeCycleContext().getViewContext().getView();
		if(widget == null)
			throw new LuiRuntimeException("片段为空!");
		
		if(this.masterDsId == null)
			throw new LuiRuntimeException("未指定数据集id!");
		Dataset masterDs = widget.getViewModels().getDataset(masterDsId);
		if(masterDs == null)
			throw new LuiRuntimeException("数据集为空,数据集id=" + masterDsId + "!");
		Row[] delRows = masterDs.getAllSelectedRows();
		if(delRows != null && delRows.length > 0){
			Dataset2SuperVOSerializer ser = new Dataset2SuperVOSerializer();
			BaseDO[] superVOs = ser.serialize(masterDs, delRows);
			onDeleteVO(superVOs);
			for (int i = 0; i < delRows.length; i++) {
				masterDs.removeRowInAllRowSets(delRows[i]);
			}
		}
		
		updateButtons();

	}
	
	protected void onDeleteVO(BaseDO[] vos){
			if(vos != null && vos.length > 0){			
				LuiCRUDService cpbService = ServiceFinder.find(LuiCRUDServiceImpl.class);
				cpbService.deleteVo(vos[0]);
			}
	}
	
}
