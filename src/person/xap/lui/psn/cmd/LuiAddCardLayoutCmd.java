package xap.lui.psn.cmd;
import xap.lui.core.command.LuiCommand;
import xap.lui.core.comps.ToolBarComp;
import xap.lui.core.comps.ToolBarItem;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.DatasetRelation;
import xap.lui.core.dataset.DatasetRelations;
import xap.lui.core.dataset.Row;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.layout.UICardLayout;
import xap.lui.core.listener.LuiEventConf;
import xap.lui.core.model.AppSession;
import xap.lui.core.model.ViewPartContext;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.psn.guided.GuidedCfgController;
import xap.mw.core.data.DOStatus;

public class LuiAddCardLayoutCmd extends LuiCommand {
	private String dsId;
	private String cardLayoutId;
	private String navDatasetId;
	private ToolBarComp toolBarComp;
	
	public LuiAddCardLayoutCmd(String dsId, String cardLayoutId) {
		this.dsId = dsId;
		this.cardLayoutId = cardLayoutId;
	}
	public LuiAddCardLayoutCmd(String dsId, String cardLayoutId,ToolBarComp toolBarComp) {
		this.dsId = dsId;
		this.cardLayoutId = cardLayoutId;
		this.toolBarComp=toolBarComp;
	}
	public LuiAddCardLayoutCmd(String dsId, String cardLayoutId, String navDatasetId) {
		this.dsId = dsId;
		this.cardLayoutId = cardLayoutId;
		this.navDatasetId = navDatasetId;
	}
	
	public void execute() {
		ViewPartContext  widgetctx = getLifeCycleContext().getViewContext();
		UICardLayout cardLayout = (UICardLayout) widgetctx.getUIMeta().findChildById(this.cardLayoutId);
		ViewPartMeta widget = widgetctx.getView();
		Dataset ds = widget.getViewModels().getDataset(dsId);
		if (this.cardLayoutId == null)throw new LuiRuntimeException("未指定卡片布局id");
		if (cardLayout == null)throw new LuiRuntimeException("卡片不存在,id=" + cardLayoutId + "!");
		if (this.dsId == null) throw new LuiRuntimeException("未指定数据集id!");
		if (ds == null)throw new LuiRuntimeException("数据集为空,数据集id=" + dsId + "!");
		
		cardLayout.setCurrentItem("1");
		
		//关联数据集的处理
		DatasetRelations dsRels = widget.getViewModels().getDsrelations();
		if (dsRels != null) {
			DatasetRelation[] rels = dsRels.getDsRelations(dsId);
			if (rels != null) {
				for (int i = 0; i < rels.length; i++) {
					String detailDsId = rels[i].getDetailDataset();
					Dataset detailDs = widget.getViewModels().getDataset(detailDsId);
					detailDs.setEdit(true);
				}
			}
		}
		
		Row row = ds.getEmptyRow();
		row.setState(DOStatus.NEW);
		setDefRowValue(row);
		ds.setEdit(true);
		ds.insertRow(0, row);
		ds.setSelectedIndex(0);
		
		if(this.toolBarComp!=null){
			new ToolBarItemStatusCtrl(this.toolBarComp,"new","card"); 
		}
		
		
//		onBeforeRowAdd(row);
//		onAfterRowAdd(row);
//		boolean pageRecordUndo = false;
//		boolean widgetRecordUndo = false;
//		if (pageRecordUndo)
//			getLifeCycleContext().getAppContext().addBeforeExecScript("pageUI.recordUndo();\n");
//		widgetRecordUndo = true;
//		setNavPkToRow(row, this.navDatasetId, ds);
	}
	/**
	 * 给默认的值         提供给模式化接口
	 * @param row
	 */
	protected void setDefRowValue(Row row){
		
	}
	protected void setNavPkToRow(Row row, String navId, Dataset slaveDs) {
		if (navId == null)
			return;
		ViewPartMeta widget = AppSession.current().getViewContext().getView();
		DatasetRelations dsRels = widget.getViewModels().getDsrelations();
		if (dsRels != null) {
			DatasetRelation rel = dsRels.getDsRelation(navId, slaveDs.getId());
			Dataset ds = widget.getViewModels().getDataset(navId);
			Row navrow = ds.getSelectedRow();
			if (navrow == null) {
				throw new LuiRuntimeException("请选择导航数据集");
			}
			Object value = navrow.getValue(ds.nameToIndex(rel.getMasterKeyField()));
			row.setValue(slaveDs.nameToIndex(rel.getDetailForeignKey()), value);
		}
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
	public String getNavDatasetId() {
		return navDatasetId;
	}
	public void setNavDatasetId(String navDatasetId) {
		this.navDatasetId = navDatasetId;
	}
}
