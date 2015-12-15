package xap.lui.core.j2eesvr;

import javax.servlet.ServletContext;

import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.constant.WebConstant;


public abstract class BaseServerListener{
	private ServletContext ctx;
	public BaseServerListener(ServletContext ctx) {
		this.ctx = ctx;
	}
	
	public ServletContext getCtx() {
		return ctx;
	}
	
	public void afterStarted() {
		LuiRuntimeContext.setServletContext(ctx);
		LuiRuntimeContext.setRootPath((String) ctx.getAttribute(WebConstant.ROOT_PATH));
		doAfterStarted();
	}

	protected abstract void doAfterStarted();
}
