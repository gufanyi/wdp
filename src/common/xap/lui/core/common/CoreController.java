package xap.lui.core.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Web Framework core controller, all request dispatched here. if the request is
 * Ajax request, the Request Handler will be called else directly dispatch to
 * the page
 */
public class CoreController {
	// private IModuleObjectPool moduleObjectPool;
	public void handleRequest(HttpServletRequest req, HttpServletResponse res) throws Exception {
		// 对于path不为空,即 ctx/xx/xx.jsp形式的请求，统一派发到相应页面。否则认为是ajax请求
		String path = req.getPathInfo();// /cp_responsibility.app
		req.getRequestURI();// /portal/core/cp_responsibility.app
		req.getRequestURL();// http://localhost:8088/portal/core/cp_responsibility.app
		req.getQueryString();// winId=111
		ControlHandler plugin = getControlPlugin(req, res, path);
		plugin.handle(req, res, path);
	}

	protected ControlHandler getControlPlugin(HttpServletRequest req, HttpServletResponse res, String path) {
		return this.getControlPlugin(path);
	}

	public ControlHandler getControlPlugin(String path) {
		if (path == null)
			return new AjaxControlHandler();
		else if (path.endsWith(".app")) {
			return new AppControlHandler();
		} else {
			return new PageControlHandler();
		}
	}
}
