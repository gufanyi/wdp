package xap.lui.psn.cmd;

import org.apache.commons.lang.StringUtils;

import xap.lui.core.command.CmdInvoker;
import xap.lui.core.command.LuiCommand;
import xap.lui.core.comps.ToolBarComp;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.layout.UITabComp;
import xap.lui.core.model.LuiAppUtil;
import xap.lui.core.model.ViewPartContext;

public class LuiDelTabBaseDOCmd extends LuiCommand{
	private String dsIds;
	private String tabLayoutId;
	private String menuBarCompId;
	private ToolBarComp toolBarComp;
	
	public LuiDelTabBaseDOCmd(String tabLayoutId, String dsIds) {
		this.tabLayoutId=tabLayoutId;
		this.dsIds = dsIds;
	}
	public LuiDelTabBaseDOCmd(String tabLayoutId, String dsIds,String menuBarCompId) {
		this.tabLayoutId=tabLayoutId;
		this.dsIds = dsIds;
		this.menuBarCompId=menuBarCompId;
	}
	public LuiDelTabBaseDOCmd(String tabLayoutId, String dsIds,String menuBarCompId,String toolBarCompId) {
		this.tabLayoutId=tabLayoutId;
		this.dsIds = dsIds;
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
		
		if(this.toolBarComp!=null)
			CmdInvoker.invoke(new LuiDelBaseDOCmd(arrDsid[tabCurrentIndex],this.toolBarComp));
		else
			CmdInvoker.invoke(new LuiDelBaseDOCmd(arrDsid[tabCurrentIndex]));
		
		if(StringUtils.isNotBlank(this.menuBarCompId))
		{
			new MenuBarItemStatusCtrl(this.menuBarCompId,"delete","card");
		}
	}
}
