package xap.lui.psn.cmd;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import xap.lui.core.command.LuiCommand;
import xap.lui.core.comps.MenubarComp;
import xap.lui.core.comps.ToolBarComp;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.DatasetRelation;
import xap.lui.core.dataset.DatasetRelations;
import xap.lui.core.dataset.Row;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.layout.UITabComp;
import xap.lui.core.model.AppSession;
import xap.lui.core.model.LuiAppUtil;
import xap.lui.core.model.ViewPartContext;
import xap.lui.core.model.ViewPartMeta;

public class LuiAddRowCmd extends LuiCommand {
	private String dsId;
	private String navDatasetId;
	private String tabId;
	private String tabds1;
	private String tabds2;
	private String tabds3;
	private ToolBarComp toolBarComp;
	private String menuBarCompId;
	public LuiAddRowCmd(String dsId) {
		this.dsId = dsId;
	}
	public LuiAddRowCmd(String dsId,ToolBarComp toolBarComp) {
		this.dsId = dsId;
		this.toolBarComp=toolBarComp;
	}
	public LuiAddRowCmd(String dsId, String navDatasetId) {
		this.dsId = dsId;
		this.navDatasetId = navDatasetId;
	}
	
	public LuiAddRowCmd(String tabId, String tabds1, String tabds2, String tabds3) {
		this.tabId = tabId;
		this.tabds1 = tabds1;
		this.tabds2 = tabds2;
		this.tabds3 = tabds3;
	}
	public LuiAddRowCmd(String tabId, String tabds1, String tabds2, String tabds3,ToolBarComp toolBarComp) {
		this.tabId = tabId;
		this.tabds1 = tabds1;
		this.tabds2 = tabds2;
		this.tabds3 = tabds3;
		this.toolBarComp=toolBarComp;
	}
	public LuiAddRowCmd(String tabId, String tabds1, String tabds2, String tabds3,String menuBarCompId) {
		this.tabId = tabId;
		this.tabds1 = tabds1;
		this.tabds2 = tabds2;
		this.tabds3 = tabds3;
		this.menuBarCompId=menuBarCompId;
	}
	
	public void execute() {
		ViewPartContext  widgetctx = getLifeCycleContext().getViewContext();
		if(tabId==null){
			ViewPartMeta widget = widgetctx.getView();
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
			
			Row row = ds.getEmptyRow();
			ds.setEdit(true);
//			ds.addRow(row);
			ds.insertRow(0, row);
			ds.setAllRowsEdit(false);
			row.setEdit(true);
			ds.setSelectedIndex(ds.getRowIndex(row));
			setNavPkToRow(row, this.navDatasetId, ds);
		}else{
			UITabComp  cardLayout =  
					(UITabComp)widgetctx.getUIMeta().findChildById(tabId);
			if(cardLayout ==  null){
				return;
			}else{
				String dsid = "";
				if("0".equals(cardLayout.getCurrentItem()+"")){
					dsid = tabds1;
				}else if("1".equals(cardLayout.getCurrentItem()+"")){
					dsid = tabds2;
				}else if("2".equals(cardLayout.getCurrentItem()+"")){
					dsid = tabds3;
				}
				Dataset dataset = widgetctx.getView().getViewModels().getDataset(dsid);
				dataset.setEdit(true);
				Row row = dataset.getEmptyRow();
				row.setState(Row.STATE_ADD);
				dataset.addRow(row);
		    }
			
		}
		
		if(this.toolBarComp!=null){
			if(toolBarComp.getElementList().size()>3)//3项的是 弹出框编辑，不用控制状态
			{
				if(StringUtils.isNotBlank(this.tabId))
					new	ToolBarItemStatusCtrl(this.toolBarComp,"new","cardFull");
				else
					new	ToolBarItemStatusCtrl(this.toolBarComp,"new","full");
			}
		}
		if(StringUtils.isNotBlank(this.menuBarCompId)){
			new MenuBarItemStatusCtrl(this.menuBarCompId,"new","full");
		}
		
//		widgetRecordUndo = true;
//		boolean pageRecordUndo = false;
//		boolean widgetRecordUndo = false;
		
//		if (ds.isControlwidgetopeStatus())
//		if (pageRecordUndo)
//		getLifeCycleContext().getAppContext().addBeforeExecScript("pageUI.recordUndo();\n");
//		if (widgetRecordUndo)
//		getLifeCycleContext().getAppContext().addBeforeExecScript("pageUI.getWidget('" + widget.getId() + "').recordUndo();\n");
//	{
//		for (int i = 0; i < idList.size(); i++) {
//			getLifeCycleContext().getAppContext().addBeforeExecScript("pageUI.getWidget('" + widget.getId() + "').getDataset('" + idList.get(i) + "').recordUndo();\n");
//		}
//	}
		
//		onBeforeRowAdd(row);
//		onAfterRowAdd(row);
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
}
