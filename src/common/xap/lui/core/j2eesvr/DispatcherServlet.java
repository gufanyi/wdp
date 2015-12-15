package xap.lui.core.j2eesvr;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import xap.lui.core.common.CoreController;
import xap.lui.core.constant.WebConstant;
import xap.lui.core.exception.LuiInteractionException;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.logger.LuiLogger;
import xap.lui.core.util.ClassUtil;

/**
 * Web Framework dispatcher
 * Servlet.基于Lui的单据必须配置此Servlet。此servlet路径名可在ctxpath属性中配置，默认 为core
 * 
 */
public class DispatcherServlet extends BaseServlet {

	private static final long serialVersionUID = -8444315709092554126L;
	private String ctrlClazz;

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		// 获得当前servlet映射路径
		String corePath = config.getInitParameter("ctxpath");
		if (corePath == null) {
			LuiLogger.error("ctxpath is not set in context-param");
			corePath = "/core";
		}
		String ctxPath = (String) getServletContext().getAttribute(WebConstant.ROOT_PATH);
		getServletContext().setAttribute(WebConstant.CORE_PATH, ctxPath.equals("/") ? ctxPath : ctxPath + corePath);

		ctrlClazz = config.getInitParameter("ctrlclass");
		if (ctrlClazz == null) {

			ctrlClazz = CoreController.class.getName();
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String setdebugmode = req.getParameter("setdebugmode");
		long now=System.currentTimeMillis();
		// 强制设置不存储缓存
		res.addHeader("Cache-Control", "no-store");
		res.setHeader("Access-Control-Allow-Origin", "*");
		if (setdebugmode != null && setdebugmode.equals("1")) {
			HttpSession session = req.getSession(true);
			session.setAttribute(WebConstant.DEBUG_MODE, setdebugmode);
		} else {
			try {
				CoreController ctrl = (CoreController) ClassUtil.newInstance(ctrlClazz);
				ctrl.handleRequest(req, res);
			} catch (Exception e) {
				if (e instanceof InvocationTargetException)
					e = (Exception) ((InvocationTargetException) e).getTargetException();
				processError(req, res, e);
			}
		}
		long cnt=System.currentTimeMillis();
		System.out.println(cnt-now);
	}

	private void processError(HttpServletRequest req, HttpServletResponse res, Exception e) throws IOException, UnsupportedEncodingException {
		if (e instanceof LuiInteractionException)
			throw (LuiInteractionException) e;
		else if (e instanceof RuntimeException)
			throw (RuntimeException) e;
		throw new LuiRuntimeException(e);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setStatus(404);
		resp.getWriter().print("skipfish sabeetlly!");
		resp.getWriter().flush();
		resp.flushBuffer();
		return;
	}
}
