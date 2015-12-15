package xap.lui.psn.cmd;
import java.util.ArrayList;
import java.util.List;

import xap.lui.core.command.LuiCommand;
import xap.lui.core.comps.ToolBarComp;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.DatasetRelation;
import xap.lui.core.dataset.DatasetRelations;
import xap.lui.core.dataset.Row;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.layout.UICardLayout;
import xap.lui.core.model.ViewPartContext;
import xap.lui.core.model.ViewPartMeta;
import xap.mw.core.data.DOStatus;

public class LuiEditCardLayoutCmd extends LuiCommand {
	private String dsId;
	private String cardLayoutId;
	private ToolBarComp toolBarComp;
	
	
	public LuiEditCardLayoutCmd(String dsId, String cardLayoutId) {
		this.dsId = dsId;
		this.cardLayoutId = cardLayoutId;
	}
	public LuiEditCardLayoutCmd(String dsId, String cardLayoutId,ToolBarComp toolBarComp) {
		this.dsId = dsId;
		this.cardLayoutId = cardLayoutId;
		this.toolBarComp=toolBarComp;
	}
	
	public void execute() {
		ViewPartContext  widgetctx = getLifeCycleContext().getViewContext();
		UICardLayout cardLayout = (UICardLayout) widgetctx.getUIMeta().findChildById(this.cardLayoutId);

		ViewPartMeta widget = widgetctx.getView();
		
		if (this.cardLayoutId == null)
			throw new LuiRuntimeException("未指定卡片布局id");
		if (cardLayout == null)
			throw new LuiRuntimeException("卡片不存在,id=" + cardLayoutId + "!");
		cardLayout.setCurrentItem("1");
		
		if (this.dsId == null)
			throw new LuiRuntimeException("未指定数据集id!");
		Dataset ds = widget.getViewModels().getDataset(dsId);
		if (ds == null)
			throw new LuiRuntimeException("数据集为空,数据集id=" + dsId + "!");
		
		List<String> idList = new ArrayList<String>();
		idList.add(dsId);
		DatasetRelations dsRels = widget.getViewModels().getDsrelations();
		if (dsRels != null) {
			DatasetRelation[] rels = dsRels.getDsRelations(dsId);
			if (rels != null) {
				for (int i = 0; i < rels.length; i++) {
					String detailDsId = rels[i].getDetailDataset();
					idList.add(detailDsId);
					Dataset detailDs = widget.getViewModels().getDataset(detailDsId);
					detailDs.setEdit(true);
				}
			}
		}

		
		Row selectRow = ds.getSelectedRow();
		selectRow.setState(DOStatus.UPDATED);
		ds.setEdit(true);
		
		if(this.toolBarComp!=null){
			new ToolBarItemStatusCtrl(this.toolBarComp,"edit","card"); 
		}
//		Row row = ds.getEmptyRow();
//		
//		Field[] fields = ds.getFieldSet().getFields();
//		for (Field field : fields){
//			int index = ds.nameToIndex(field.getId());
//			if(index < 0)
//				continue;
//			row.setValue(index, selectRow.getValue(index));
//		}
//		row.setState(DOStatus.UPDATED);
//		ds.removeRow(selectRow);
//		ds.insertRow(0, row);
//		ds.setSelectedIndex(0);
//		ds.setEdit(true);
		
//		widgetRecordUndo = true;
//		if (pageRecordUndo)
//		getLifeCycleContext().getAppContext().addBeforeExecScript("pageUI.recordUndo();\n");
//		boolean pageRecordUndo = false;
//		boolean widgetRecordUndo = false;
//		onAfterRowAdd(row);
	}
		
	protected void onAfterRowAdd(Row row) {
		updateButtons();
	}
	protected void onBeforeRowAdd(Row row) {}
	
	public String getDsId() {
		return dsId;
	}
	public void setDsId(String dsId) {
		this.dsId = dsId;
	}
	public String getCardLayoutId() {
		return cardLayoutId;
	}
	public void setCardLayoutId(String cardLayoutId) {
		this.cardLayoutId = cardLayoutId;
	}
}
