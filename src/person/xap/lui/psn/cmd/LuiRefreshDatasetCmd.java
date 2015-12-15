package xap.lui.psn.cmd;

import xap.lui.core.command.LuiCommand;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.model.AppSession;
import xap.lui.core.tags.AppDynamicCompUtil;

public class LuiRefreshDatasetCmd extends LuiCommand {

	private String dsId;
	private Integer pageIndex;

	public LuiRefreshDatasetCmd(String dsId, Integer pageIndex){
		this.dsId = dsId;
		this.pageIndex = pageIndex;
	}
	
	public LuiRefreshDatasetCmd(String dsId){
		this.dsId = dsId;
		this.pageIndex = -1;
	}
	
	@Override
	public void execute() {
		Dataset dataset = AppSession.current().getViewContext().getView().getViewModels().getDataset(dsId);
		if (this.pageIndex != null && this.pageIndex == -1){
			this.pageIndex = null;
		}
		
		AppDynamicCompUtil appUtil = new AppDynamicCompUtil(AppSession.current().getAppContext(), AppSession.current().getViewContext());
		appUtil.refreshDataset(dataset, pageIndex);
	}
	

}
