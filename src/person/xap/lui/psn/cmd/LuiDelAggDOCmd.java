package xap.lui.psn.cmd;
import java.util.ArrayList;

import xap.lui.core.command.LuiCommand;
import xap.lui.core.common.InteractionUtil;
import xap.lui.core.comps.ToolBarComp;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.DatasetRelation;
import xap.lui.core.dataset.Row;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.core.serializer.Datasets2AggVOSerializer;
import xap.sys.appfw.orm.model.agg.BaseAggDO;

public class LuiDelAggDOCmd<T extends BaseAggDO> extends LuiCommand {
	private String masterDsId;
	private String aggVoClazz;
	private ToolBarComp toolBarComp;
	public LuiDelAggDOCmd(String masterDsId, String aggVoClazz) {
		this.masterDsId = masterDsId;
		this.aggVoClazz = aggVoClazz;
	}
	public LuiDelAggDOCmd(String masterDsId, String aggVoClazz,ToolBarComp toolBarComp) {
		this.masterDsId = masterDsId;
		this.aggVoClazz = aggVoClazz;
		this.toolBarComp=toolBarComp;
	}
	public void execute() {
		ViewPartMeta widget = getLifeCycleContext().getViewContext().getView();
		if (widget == null)
			throw new LuiRuntimeException("片段为空!");
		if (this.masterDsId == null)
			throw new LuiRuntimeException("未指定数据集id!");
		Dataset masterDs = widget.getViewModels().getDataset(masterDsId);
		if (masterDs == null)
			throw new LuiRuntimeException("数据集为空,数据集id=" + masterDsId + "!");
		Row[] delRows = masterDs.getCurrentPageSelectedRows();
		if (delRows != null && delRows.length > 0) {
			InteractionUtil.showConfirmDialog("删除对话框", "确认删除吗?", "删除");
			if (!InteractionUtil.getConfirmDialogResult().booleanValue())
				return;
			ArrayList<Dataset> detailDss = null;
			DatasetRelation[] rels = null;
			if (widget.getViewModels().getDsrelations() != null) {
				rels = widget.getViewModels().getDsrelations().getDsRelations(masterDsId);
			}
			if (rels != null) {
				detailDss = new ArrayList<Dataset>();
				for (int i = 0; i < rels.length; i++) {
					int index = masterDs.nameToIndex(rels[i].getMasterKeyField());
					Dataset detailDs = widget.getViewModels().getDataset(rels[i].getDetailDataset());
					detailDs.setExtendAttribute("parent_index", index);
					if (detailDs != null) {
						detailDss.add(detailDs);
					}
				}
			}
			for (int i = 0; i < delRows.length; i++) {
				ArrayList<BaseAggDO> vos = new ArrayList<BaseAggDO>();
				Row masterRow = delRows[i];
				Datasets2AggVOSerializer ser = new Datasets2AggVOSerializer();
				vos.add(ser.serialize(masterDs, masterRow, detailDss == null ? null : detailDss.toArray(new Dataset[0]), aggVoClazz));
				onDeleteVO(vos, true);
				masterDs.removeRow(masterDs.getRowIndex(delRows[i]));
				if (detailDss != null) {
					int size = detailDss.size();
					if (size > 0) {
						for (int j = 0; j < size; j++) {
							Dataset detailDs = detailDss.get(j);
							for (int k = 0; k < delRows.length; k++) {
								Integer index = (Integer) detailDs.getExtendAttributeValue("parent_index");
								if (delRows[i].getValue(index) != null);
//									detailDs.re.removeRowSet((String) delRows[i].getValue(index));
							}
						}
					}
				}
			}
			if(masterDs.getCurrentPageRowCount()!=0){
				masterDs.setSelectedIndex(0);//设置选中行
			}
		}
		else
			throw new LuiRuntimeException("请选中删除行!");
//		updateButtons();
		new ToolBarItemStatusCtrl(this.toolBarComp,"delete","");//控制工具栏项隐藏或显示
	}
	protected void onDeleteVO(final ArrayList<BaseAggDO> vos,final boolean trueDel) {
//		try {
//			Context.run(new VoidCallback() {
//				@Override
//				public void invoke() throws Exception {
//					if(vos!=null&&vos.size()!=0) {
//						BaseAggService<T> cpbService = new BaseAggService<T>(vos.get(0).getParent().getDODesc(),(Class<T>) vos.get(0).getClass());
//						if(trueDel)
//							cpbService.realDelete((T[]) vos.toArray(new BaseAggDO[0]));
//						else
//							cpbService.delete((T[]) vos.toArray(new BaseAggDO[0]));
//					}
//					
//				}
//			});
//			
//			
//		} 
//		catch (Exception e) {
//			LuiLogger.error(e.getMessage(), e);
//			throw new LuiRuntimeException(e.getMessage());
//		}
//		try {
//			IUifCpbService cpbService = NCLocator.getInstance().lookup(IUifCpbService.class);
//			cpbService.deleteAggVOs(vos.toArray(new AggregatedValueObject[0]));
//		} 
//		catch (BusinessException e) {
//			CpLogger.error(e.getMessage(), e);
//			throw new LuiRuntimeException(e.getMessage());
//		}
	}
}
