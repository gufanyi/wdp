package xap.lui.core.device;

import xap.lui.core.model.LuiPageContext;

public interface IUifDeletator {
	public LuiPageContext getGlobalContext();
	public void setGlobalContext(LuiPageContext ctx);
	public void execute();
}
