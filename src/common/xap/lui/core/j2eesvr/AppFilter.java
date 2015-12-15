package xap.lui.core.j2eesvr;
import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.constant.AppConsts;
import xap.lui.core.logger.LuiLogger;
/**
 * LUI App型应用URL重写过滤器,将形如 core/xx.app 地址重写为core/app/xx
 * 
 * @author dengjt
 * 
 */
public class AppFilter implements Filter {
	public static final String ORIURL = "oriurl";
	@Override
	public void init(FilterConfig config) throws ServletException {
		LuiLogger.debug("Application Filter started");
	}
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		String uri = req.getRequestURI();
		if (!StringUtils.isEmpty(req.getQueryString())) {
			uri += "?" + req.getQueryString();
		}
		req.setAttribute(ORIURL, uri);
		String url = makeAppURL(req);
		RequestDispatcher disp = LuiRuntimeContext.getServletContext().getRequestDispatcher(url);
		disp.forward(request, response);
	}
	protected String makeAppURL(HttpServletRequest req) {
		String[] params = getParams(req);
		String appId = params[3];
		String winId = null;
		String opeId = null;
		if (params.length > 4) {
			winId = params[4];
		} else {
			winId = "";
		}
		if (params.length > 5) {
			opeId = params[5];
		} else {
			opeId = "";
		}
		String url = "/core/" + appId + ".app";
		if (winId != null && winId.length() != 0) {
			url += "?" + AppConsts.PARAM_WIN_ID + "=" + winId;
		}
		if (opeId != null && opeId.length() != 0)
			if (url.indexOf("?") > -1) {
				url += "&" + AppConsts.PARAM_WIN_OPE + "=" + opeId;
			} else {
				url += "?" + AppConsts.PARAM_WIN_OPE + "=" + opeId;
			}
		return url;
	}
	protected String[] getParams(HttpServletRequest req) {
		String uri = req.getContextPath() + req.getServletPath() + StringUtils.defaultIfEmpty(req.getPathInfo(), "");
		String[] params = uri.split("/");
		return params;
	}
	@Override
	public void destroy() {
		LuiLogger.debug("Application Filter destroyed");
	}
}
