package xap.lui.psn.cmd;

import xap.lui.core.command.LuiCommand;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.Row;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.layout.UITabComp;
import xap.lui.core.model.ViewPartContext;
import xap.lui.core.model.ViewPartMeta;
import xap.mw.core.data.DOStatus;

public class LuiRemoveRowCmd extends LuiCommand  {
	private String dsId;
	private String tabId;
	private String tabds1;
	private String tabds2;
	private String tabds3;
	
	public LuiRemoveRowCmd(String dsId) {
		this.dsId = dsId;
	}
	
	public LuiRemoveRowCmd(String tabId, String tabds1, String tabds2, String tabds3) {
		this.tabId = tabId;
		this.tabds1 = tabds1;
		this.tabds2 = tabds2;
		this.tabds3 = tabds3;
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
			if (pageRecordUndo)
				getLifeCycleContext().getAppContext().addBeforeExecScript("pageUI.recordUndo();\n");
			Row row = ds.getSelectedRow();
			if(row == null)
				throw new LuiRuntimeException("没有选中行！");
			row.setState(DOStatus.DELETED);
			ds.removeRow(row);
			//ds.setEdit(false);
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
				Row row = dataset.getSelectedRow();
				if(row!=null){
					dataset.removeRow(row);
					dataset.setEdit(false);
				}
				if(dataset.getCurrentPageRowCount()!=0){
					dataset.setSelectedIndex(0);
				}
		    }
		}
	}

	public String getDsId() {
		return dsId;
	}
	public void setDsId(String dsId) {
		this.dsId = dsId;
	}
	public String getTabId() {
		return tabId;
	}
	public void setTabId(String tabId) {
		this.tabId = tabId;
	}
	public String getTabds1() {
		return tabds1;
	}
	public void setTabds1(String tabds1) {
		this.tabds1 = tabds1;
	}
	public String getTabds2() {
		return tabds2;
	}
	public void setTabds2(String tabds2) {
		this.tabds2 = tabds2;
	}
	public String getTabds3() {
		return tabds3;
	}
	public void setTabds3(String tabds3) {
		this.tabds3 = tabds3;
	}
}
