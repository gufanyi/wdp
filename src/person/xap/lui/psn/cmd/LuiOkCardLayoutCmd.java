package xap.lui.psn.cmd;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import xap.lui.core.command.LuiCommand;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.DatasetRelation;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.layout.UICardLayout;
import xap.lui.core.layout.UITabComp;
import xap.lui.core.model.LuiAppUtil;
import xap.lui.core.model.ViewPartContext;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.core.model.WindowContext;
import xap.mw.core.data.DOStatus;

public class LuiOkCardLayoutCmd extends LuiCommand {
	private String masterDsId;
	private String cardLayoutId;
	private String menuBarCompId;
	public LuiOkCardLayoutCmd(String masterDsIds,String cardLayoutId,String tabId){
		
		ViewPartContext viewParCtx= LuiAppUtil.getCntViewCtx();
		UITabComp uiTabComp=(UITabComp)viewParCtx.getUIMeta().findChildById(tabId);
		int index= uiTabComp.getCurrentItem();
		String[] arrMasterDsId=masterDsIds.split(",");
		String[] arrCardLayoutId=cardLayoutId.split(",");
		this.masterDsId = arrMasterDsId[index];
		this.cardLayoutId = arrCardLayoutId[index];
	}
	public LuiOkCardLayoutCmd(String masterDsIds,String cardLayoutId,String tabId,String menuBarCompId){
		this(masterDsIds, cardLayoutId, tabId);
		this.menuBarCompId=menuBarCompId;
	}
	@Override
	public void execute() {
		ViewPartContext   viewPartContext = getLifeCycleContext().getViewContext();
		ViewPartMeta widget = viewPartContext.getView();
		if(widget == null)
			throw new LuiRuntimeException("片段为空!");
		if(this.masterDsId == null)
			throw new LuiRuntimeException("未指定主数据集id!");
		Dataset masterDs = widget.getViewModels().getDataset(masterDsId);
		if(masterDs == null)
			throw new LuiRuntimeException("数据集为空,数据集id=" + masterDsId + "!");
		UICardLayout cardLayout = (UICardLayout) viewPartContext.getUIMeta().findChildById(this.cardLayoutId);
		
		if (this.cardLayoutId == null)
			throw new LuiRuntimeException("未指定卡片布局id");
		if (cardLayout == null)
			throw new LuiRuntimeException("卡片不存在,id=" + cardLayoutId + "!");
		cardLayout.setCurrentItem("0");
		masterDs.setEdit(false);	
		if(StringUtils.isNotBlank(this.menuBarCompId))
		{
			new MenuBarItemStatusCtrl(this.menuBarCompId,"ok","card");
		}
		
	}

}
