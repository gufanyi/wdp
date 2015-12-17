package xap.lui.psn.cmd;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import xap.lui.core.command.LuiCommand;
import xap.lui.core.common.InteractionUtil;
import xap.lui.core.dao.CRUDHelper;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.DatasetRelation;
import xap.lui.core.dataset.Row;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.logger.LuiLogger;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.core.serializer.Dataset2SuperVOSerializer;
import xap.lui.core.serializer.Datasets2AggVOSerializer;
import xap.lui.core.util.LuiClassUtil;
import xap.lui.core.xml.StringUtils;
import xap.mw.core.data.BaseDO;
import xap.sys.appfw.orm.model.agg.BaseAggDO;
import xap.sys.jdbc.handler.RsHandler;

public class LuiDelTreeCmd<T extends BaseAggDO> extends LuiCommand {
	private String treeDsId;
	private String aggVoClazz;
	private String masterDsId;

	public LuiDelTreeCmd(String treeDsId) {
		this.treeDsId = treeDsId;
	}
	public LuiDelTreeCmd(String treeDsId,String masterDsId,String aggVoClazz) {
		this.treeDsId = treeDsId;
		this.masterDsId=masterDsId;
		this.aggVoClazz=aggVoClazz;
	}

	public void execute() {
		
		ViewPartMeta widget = getLifeCycleContext().getViewContext().getView();
		if (widget == null)
			throw new LuiRuntimeException("片段为空!");
		if (this.treeDsId == null)
			throw new LuiRuntimeException("未指定数据集id!");
		Dataset treeDs = widget.getViewModels().getDataset(treeDsId);
		if (treeDs == null)
			throw new LuiRuntimeException("数据集为空,数据集id=" + treeDsId + "!");
		Row[] delRows = treeDs.getCurrentPageSelectedRows();
		if (delRows != null && delRows.length > 0) {
			InteractionUtil.showConfirmDialog("删除对话框", "确认删除吗?", "删除");
			if (!InteractionUtil.getConfirmDialogResult().booleanValue())
				return;
			Dataset detailDs = null;
			DatasetRelation[] rels = null;
			if (widget.getViewModels().getDsrelations() != null) {
				rels = widget.getViewModels().getDsrelations().getDsRelations(treeDsId);
			}
			if (rels != null) {
				
				int index = treeDs.nameToIndex(rels[0].getMasterKeyField());
				String treeMasterKeyValue="";
				for (int i = 0; i < delRows.length; i++) {
					treeMasterKeyValue+="'"+(String) delRows[i].getValue(index)+"',";
				}
				
			    detailDs = widget.getViewModels().getDataset(rels[0].getDetailDataset());
				String detailForKey = rels[0].getDetailForeignKey();
				String voMeta = detailDs.getVoMeta();
				BaseDO detailDo = (BaseDO) LuiClassUtil.loadClass(voMeta);
				String tableName = detailDo.getTableName();
				treeMasterKeyValue=treeMasterKeyValue.substring(0, treeMasterKeyValue.length()-1);
				String sql = "select count(*) from " + tableName + " where " + detailForKey + " in (" + treeMasterKeyValue + ")";

				Integer count = 0;
				try {
					count = (Integer) CRUDHelper.getCRUDService().executeQuery(sql, new RsHandler() {

						@Override
						public Object handleRs(ResultSet rs) throws SQLException {
							Integer value = 0;
							if (rs.next()) {
								value = rs.getInt(1);
							}
							return value;
						}
					});
				} catch (Throwable e) {
					LuiLogger.error(e.getMessage(), e);
					throw new LuiRuntimeException(e.getMessage());
				}
				
				if(count>0){//树要删除的数据关联的子表有数据
					InteractionUtil.showConfirmDialog("diaIsDelChiData", "删除对话框", "连同该数据关联的数据一块删除吗？", "确认", "取消");
					if(InteractionUtil.getConfirmDialogResult("diaIsDelChiData")){
						if(StringUtils.isNotBlank(this.aggVoClazz)){//树关联的子表还有子表
							
							delAggDo();
						}else{
							delBaseDo();
						}
					}
				}
				
				
				detailDs.setExtendAttribute("parent_index", index);
			}
			
			Dataset2SuperVOSerializer ser = new Dataset2SuperVOSerializer();
			BaseDO[] superVOs = ser.serialize(treeDs, delRows);
			onDeleteVO(superVOs);
			for (int j = 0; j < delRows.length; j++) {
				treeDs.removeRow(treeDs.getRowIndex(delRows[j]));
			}
			
			
			if (treeDs.getCurrentPageRowCount() != 0) {
				treeDs.setSelectedIndex(0);// 设置选中行
			}
		} else
			throw new LuiRuntimeException("请选中删除行!");
//		 updateButtons();
	}
	public void delAggDo() {
		ViewPartMeta widget = getLifeCycleContext().getViewContext().getView();
		Dataset masterDs = widget.getViewModels().getDataset(masterDsId);
		if (masterDs == null)
			throw new LuiRuntimeException("数据集为空,数据集id=" + masterDsId + "!");
		Row[] delRows = masterDs.getCurrentPageData().getRows();
		if (delRows != null && delRows.length > 0) {
//			InteractionUtil.showConfirmDialog("删除对话框", "确认删除吗?", "删除");
//			if (!InteractionUtil.getConfirmDialogResult().booleanValue())
//				return;
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
	}
	public void delBaseDo() {
		ViewPartMeta widget = getLifeCycleContext().getViewContext().getView();
		Dataset masterDs = widget.getViewModels().getDataset(masterDsId);
		if(masterDs == null)
			throw new LuiRuntimeException("数据集为空,数据集id=" + masterDsId + "!");
		Row[] delRows = masterDs.getCurrentPageData().getRows();
		if(delRows != null && delRows.length > 0){
			Dataset2SuperVOSerializer ser = new Dataset2SuperVOSerializer();
			BaseDO[] superVOs = ser.serialize(masterDs, delRows);
			onDeleteVO(superVOs);
			for (int j = 0; j < delRows.length; j++) {
				masterDs.removeRow(masterDs.getRowIndex(delRows[j]));
			}
		
		}
		//updateButtons();
	}
	protected void onDeleteVO(BaseDO[] vos){
		try {
			if(vos != null && vos.length > 0)	{
				CRUDHelper.getCRUDService().deleteBeans(vos);
			}
		} catch (Exception e) {
			LuiLogger.error(e.getMessage(), e);
			throw new LuiRuntimeException(e);
		}
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
	}
}
