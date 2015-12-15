package xap.lui.core.j2eesvr;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.common.LuiWebContext;

/**
 * LUI Servlet基类
 * @author dengjt
 *
 */
public abstract class BaseServlet extends HttpServlet{
	private static final long serialVersionUID = 2678149808373514646L;

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		LuiWebContext webCtx = LuiRuntimeContext.getWebContext();
		if(webCtx != null)
			LuiRuntimeContext.getWebContext().setResponse(res);
		
		addRemoteCallMethod(req, res);
		super.service(req, res);
	}
	
	
	
	protected void addRemoteCallMethod(HttpServletRequest req,
			HttpServletResponse res) {
		String method = getRemoteCallMethod(req, res);
		//if(method != null && !method.equals(""))
			//ThreadTracer.getInstance().setRemoteCallMethod(method);
	}

	public String getRemoteCallMethod(HttpServletRequest req, HttpServletResponse res){
		return req.getRequestURI();
	}
}
