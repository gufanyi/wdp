package xap.lui.core.j2eesvr;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import xap.lui.core.common.LuiRuntimeContext;
/**
 * lui项目自用的启动加载器
 * 
 */
public final class ContextLoadListener extends BaseContextLoaderListener {
	@Override
	public void contextInitialized(ServletContextEvent ctxEvent) {
		ServletContext ctx = ctxEvent.getServletContext();
		LuiRuntimeContext.setLuiPath(ctx.getRealPath("/"));
		super.contextInitialized(ctxEvent);
		new LuiServerListener(ctx).afterStarted();
		
		
	}
}
