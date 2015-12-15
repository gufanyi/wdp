package xap.lui.psn.cmd;

import java.util.ArrayList;
import java.util.List;

import xap.lui.core.command.LuiCommand;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.DatasetRelation;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.model.ViewPartContext;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.core.serializer.Datasets2AggVOSerializer;
import xap.sys.appfw.orm.model.agg.BaseAggDO;



public class LuiUpdateUIDataCmd extends LuiCommand {
	private String masterDsId;
	private BaseAggDO aggVo;
	public LuiUpdateUIDataCmd(BaseAggDO aggVo, String masterDsId){
		this.aggVo = aggVo;
		this.masterDsId = masterDsId;
	}

	@Override
	public void execute() {
		ViewPartContext viewCtx = getLifeCycleContext().getViewContext();
		ViewPartMeta widget = viewCtx.getView();
		if (widget == null)
			throw new LuiRuntimeException("片段为空!");
		if (this.masterDsId == null)
			throw new LuiRuntimeException("未指定主数据集id!");
		Dataset masterDs = widget.getViewModels().getDataset(masterDsId);
		if (masterDs == null)
			throw new LuiRuntimeException("数据集为空,数据集id=" + masterDsId + "!");
	
		List<String> idList = new ArrayList<String>();
		idList.add(masterDsId);
		
		ArrayList<Dataset> detailDs = new ArrayList<Dataset>();
		DatasetRelation[] rels = null;;
		if (widget.getViewModels().getDsrelations() != null) {
			rels = widget.getViewModels().getDsrelations().getDsRelations(masterDsId);
			if (rels != null) {
				String[] detailDsIds = new String[rels.length];
				for (int i = 0; i < rels.length; i++) {
					detailDsIds[i] = rels[i].getDetailDataset();
					Dataset ds = widget.getViewModels().getDataset(detailDsIds[i]);
					detailDs.add(ds);
				}
			}
		}
		
		onAfterVOUpdate(widget, masterDs, detailDs.toArray(new Dataset[0]), aggVo, rels);
	}
	
	
	protected void onAfterVOUpdate(ViewPartMeta widget, Dataset masterDs, Dataset[] detailDss, BaseAggDO aggVo, DatasetRelation[] rels) {
		if (detailDss != null && detailDss.length > 0) {
			String newKeyValue = (String) aggVo.getParentDO().getAttrVal(rels[0].getMasterKeyField());
			for (int i = 0; i < detailDss.length; i++) {
//				if (newKeyValue != null && !newKeyValue.equals(detailDss[i].getCurrentKey()))
//					detailDss[i].replaceKeyValue(detailDss[i].getCurrentKey(), newKeyValue);
			}
		}
		Datasets2AggVOSerializer ser = new Datasets2AggVOSerializer();
		ser.update(aggVo, masterDs, detailDss);
	}
}
