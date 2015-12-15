package xap.lui.psn.cmd;

import org.apache.commons.lang.StringUtils;

import xap.lui.core.command.CmdInvoker;
import xap.lui.core.command.LuiCommand;
import xap.lui.core.comps.ToolBarComp;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.layout.UITabComp;
import xap.lui.core.model.LuiAppUtil;
import xap.lui.core.model.ViewPartContext;

public class LuiCancelTabCardLayoutCmd extends LuiCommand{
	private String dsIds;
	private String cardLayoutIds;
	private String tabLayoutId;
	private String menuBarCompId;
	private ToolBarComp toolBarComp;
	
	public LuiCancelTabCardLayoutCmd(String tabLayoutId, String dsIds, String cardLayoutIds) {
		this.tabLayoutId=tabLayoutId;
		this.dsIds = dsIds;
		this.cardLayoutIds = cardLayoutIds;
	}
	public LuiCancelTabCardLayoutCmd(String tabLayoutId, String dsIds, String cardLayoutIds,String menuBarCompId) {
		this.tabLayoutId=tabLayoutId;
		this.dsIds = dsIds;
		this.cardLayoutIds = cardLayoutIds;
		this.menuBarCompId=menuBarCompId;
	}
	public LuiCancelTabCardLayoutCmd(String tabLayoutId, String dsIds, String cardLayoutIds,String menuBarCompId,String toolBarCompId) {
		this.tabLayoutId=tabLayoutId;
		this.dsIds = dsIds;
		this.cardLayoutIds = cardLayoutIds;
		this.menuBarCompId=menuBarCompId;
		this.toolBarComp=(ToolBarComp)LuiAppUtil.getCntView().getViewComponents().getComponent(toolBarCompId);
	}
	public void execute() {
		ViewPartContext  widgetctx = getLifeCycleContext().getViewContext();
		UITabComp tabLayout = (UITabComp) widgetctx.getUIMeta().findChildById(this.tabLayoutId);
		if (this.tabLayoutId == null)throw new LuiRuntimeException("未指定Tab布局id");
		if (tabLayout == null)throw new LuiRuntimeException("Tab不存在,id=" + tabLayoutId + "!");
		int tabCurrentIndex=tabLayout.getCurrentItem();
		String[] arrDsid=this.dsIds.split(",");
		String[] arrCardLayoutId=this.cardLayoutIds.split(",");
		if(this.toolBarComp!=null)
			CmdInvoker.invoke(new LuiCancelCardLayoutCmd(arrDsid[tabCurrentIndex],arrCardLayoutId[tabCurrentIndex],this.toolBarComp));
		else	
			CmdInvoker.invoke(new LuiCancelCardLayoutCmd(arrDsid[tabCurrentIndex],arrCardLayoutId[tabCurrentIndex]));

		if(StringUtils.isNotBlank(this.menuBarCompId))
		{
			new MenuBarItemStatusCtrl(this.menuBarCompId,"cancel","card");
		}
	}
}
