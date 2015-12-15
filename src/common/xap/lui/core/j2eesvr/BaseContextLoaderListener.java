package xap.lui.core.j2eesvr;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import xap.lui.core.constant.ApplicationConstant;
import xap.lui.core.constant.WebConstant;

public abstract class BaseContextLoaderListener implements ServletContextListener {
	public void contextDestroyed(ServletContextEvent ctxEvent) {
	}

	public void contextInitialized(ServletContextEvent ctxEvent) {
		ServletContext ctx = ctxEvent.getServletContext();
		ctx.setAttribute(WebConstant.ROOT_PATH, ctx.getInitParameter("ctxPath"));
		// 设置web应用真实路径
		String realPath = ctx.getRealPath("/");
		ctx.setAttribute(WebConstant.REAL_PATH, realPath);
		String errorPage = ctx.getInitParameter(ApplicationConstant.ERROR_PAGE);
		if (errorPage != null)
			ctx.setAttribute(ApplicationConstant.ERROR_PAGE, errorPage);
		BaseServerListener luiListener = new DftAllServerListener(ctx);
		luiListener.afterStarted();

	}
}
