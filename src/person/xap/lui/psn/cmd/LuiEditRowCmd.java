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
import xap.lui.core.model.ViewPartContext;
import xap.lui.core.model.ViewPartMeta;

public class LuiEditRowCmd extends LuiCommand  {
	private String dsId;
	private String tabId;
	private String tabds1;
	private String tabds2;
	private String tabds3;
	private ToolBarComp toolBarComp;
	private String menuBarCompId;
	public LuiEditRowCmd(String dsId) {
		this.dsId = dsId;
	}
	public LuiEditRowCmd(String dsId,ToolBarComp toolBarComp) {
		this.dsId = dsId;
		this.toolBarComp=toolBarComp;
	}
	public LuiEditRowCmd(String tabId, String tabds1, String tabds2, String tabds3) {
		this.tabId = tabId;
		this.tabds1 = tabds1;
		this.tabds2 = tabds2;
		this.tabds3 = tabds3;
	}
	public LuiEditRowCmd(String tabId, String tabds1, String tabds2, String tabds3,ToolBarComp toolBarComp) {
		this.tabId = tabId;
		this.tabds1 = tabds1;
		this.tabds2 = tabds2;
		this.tabds3 = tabds3;
		this.toolBarComp=toolBarComp;
	}
	public LuiEditRowCmd(String tabId, String tabds1, String tabds2, String tabds3,String menuBarCompId) {
		this.tabId = tabId;
		this.tabds1 = tabds1;
		this.tabds2 = tabds2;
		this.tabds3 = tabds3;
		this.menuBarCompId=menuBarCompId;
	}
	
	
	@Override
	public void execute() {
		ViewPartContext  widgetctx = getLifeCycleContext().getViewContext();
		if(tabId==null){
			boolean pageRecordUndo = false;
			ViewPartMeta widget = widgetctx.getView();
			if (this.dsId == null)
				throw new LuiRuntimeException("未指定数据集id!");
			Dataset ds = widget.getViewModels().getDataset(dsId);
			if (ds == null)
				throw new LuiRuntimeException("数据集为空,数据集id=" + dsId + "!");
			if(ds.getSelectedRow() == null){
				throw new LuiRuntimeException("请选中行");
			}else{
				ds.getSelectedRow().setState(Row.STATE_UPDATE);
			}
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
			if (pageRecordUndo)
				getLifeCycleContext().getAppContext().addBeforeExecScript("pageUI.recordUndo();\n");
			ds.setEdit(true);
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
		    }
		}
		if(this.toolBarComp!=null)
			if(toolBarComp.getElementList().size()>3)
			{
				if(StringUtils.isNotBlank(this.tabId))
					new ToolBarItemStatusCtrl(this.toolBarComp, "edit","cardFull");
				else
					new ToolBarItemStatusCtrl(this.toolBarComp, "edit","full");
			}
				
		if(StringUtils.isNotBlank(this.menuBarCompId))
			new MenuBarItemStatusCtrl(menuBarCompId, "edit","full");
	}

	public String getDsId() {
		return dsId;
	}
	public void setDsId(String dsId) {
		this.dsId = dsId;
	}
}
