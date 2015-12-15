package xap.lui.core.j2eesvr;

import java.io.IOException;
import java.net.URL;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.common.LuiWebContext;
import xap.lui.core.constant.SessionConstant;
import xap.lui.core.constant.WebConstant;
import xap.lui.core.logger.LuiLogger;
import xap.lui.core.sso.SessionBean;
import xap.lui.core.util.IOUtil;

public class RequestListener implements Filter {
	public void requestInitialized(ServletRequestEvent reqEvent) {
		System.setProperty("org.eclipse.jetty.server.Request.maxFormContentSize", "40000");
		HttpServletRequest request = (HttpServletRequest) reqEvent.getServletRequest();
		String path = request.getRequestURI();
		if (path == null) {
			return;
		}
		if (isResource(path)) {
			return;
		}
		LuiWebContext webCtx = new LuiWebContext(request);
		// 设置到ThreadLocal中，以便于随时取用
		LuiRuntimeContext.setWebContext(webCtx);
		// 进行Theme和lang的获取
		// LuiRuntimeEnvironment.getThemeId();
		ServletContext ctx = reqEvent.getServletContext();
		String realPath = (String) ctx.getAttribute(WebConstant.REAL_PATH);
		String rootPath = (String) ctx.getAttribute(WebConstant.ROOT_PATH);
		String corePath = (String) ctx.getAttribute(WebConstant.CORE_PATH);
		String serverName = request.getServerName();
		LuiRuntimeContext.setRealPath(realPath);
		LuiRuntimeContext.setRootPath(rootPath);
		LuiRuntimeContext.setCorePath(corePath);
		LuiRuntimeContext.setFromServerName(serverName);
		LuiRuntimeContext.setServletContext(ctx);
		request.setAttribute(WebConstant.ROOT_PATH, rootPath);
		request.setAttribute(WebConstant.REAL_PATH, realPath);
		request.setAttribute(WebConstant.CORE_PATH, corePath);
		request.setAttribute(WebConstant.LUI_ROOT_PATH, LuiRuntimeContext.getLuiCtx());
		restoreEnviroment((HttpServletRequest) reqEvent.getServletRequest());
		String debugMode = (String) request.getSession().getAttribute(WebConstant.DEBUG_MODE);
		// 优先以传入参数为主。
		if (debugMode == null) {
			return;
		}
		if (debugMode.equals("1")) {
			debugMode = WebConstant.MODE_DEBUG;
		} else if (debugMode.equals("0")) {
			debugMode = WebConstant.MODE_PRODUCTION;
		}
		LuiRuntimeContext.setMode(debugMode);

	}

	protected boolean isResource(String path) {
		return path.endsWith(".html") || path.endsWith(".gif") || path.endsWith(".png") || path.endsWith(".jpg") || path.endsWith(".css") || path.endsWith(".js") || path.endsWith(".swf");
	}

	protected void restoreEnviroment(HttpServletRequest req) {
		SessionBean bean = (SessionBean) req.getSession().getAttribute(SessionConstant.LOGIN_SESSION_BEAN);
		LuiRuntimeContext.setSessionBean(bean);
	}

	public void requestDestroyed(ServletRequestEvent reqEvent) {
		try {
			HttpServletRequest request = (HttpServletRequest) reqEvent.getServletRequest();
			String path = request.getRequestURI();
			if (path == null) {
				return;
			}
			if (isResource(path)) {
				return;
			}
			if (LuiRuntimeContext.getWebContext() == null) {
				return;
			}
			LuiRuntimeContext.reset();
		} finally {
		}
	}


	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) arg0;
		ServletRequestEvent sre = new ServletRequestEvent(request.getSession().getServletContext(), arg0);
		try {
			String path = request.getRequestURI();
			if (isResource(path) && path.indexOf("/platform/") > -1) {
				this.handlerPaltformResource(path, arg1);
				return;

			}
			if (isResource(path) && path.indexOf("/luiextcomp/") > -1) {
				this.handlerLuiExtResource(path, arg1);
				return;
			}

			if (isResource(path) && (path.indexOf("lui/nodes/") > -1 || path.indexOf("lui/views/") > -1)) {
				this.hanlderNodeResource(path, arg1);
				return;
			}

			final String str4 = "bugfile";
			int index4 = path.indexOf(str4);
			if (index4 > -1) {
				this.hanlderFSResouce(path, index4, arg1);
				return;
			}
			requestInitialized(sre);
			arg2.doFilter(arg0, arg1);
		} finally {
			requestDestroyed(sre);
		}
	}

	public void handlerPaltformResource(String path, ServletResponse arg1) {
		try {
			int index1 = path.indexOf("/platform/");
			path = path.substring(index1, path.length());
			URL url = this.getClass().getClassLoader().getResource("lui" + path);
			if (url != null) {
				byte[] bytes = IOUtil.readBytesFromStream(url.openStream());
				arg1.getOutputStream().write(bytes);
				arg1.getOutputStream().flush();
				return;
			}
		} catch (Throwable e) {
			LuiLogger.error(e.getMessage());
		}
	}

	public void hanlderFSResouce(String path, int index, ServletResponse arg1) {
//		try {
//			final String str4 = "bugfile";
//			final String itemId = path.substring(8).replace("_bugfile", "");
//			final ServletResponse arg = arg1;
//			xap.mw.core.Context.run(new VoidCallback() {
//				@Override
//				public void invoke() throws Exception {
//					Bucket b = BucketBuilder.builder(str4).build();
//					FBinary fbinary = b.handle(new Callback<FBinary>() {
//						@Override
//						public FBinary run(CallbackContext context) throws Exception {
//							Bucket b = context.getBucket();
//							BucketItem item = b.getItem(itemId);
//							Long cl = item.getMeta().getContentLength();
//							FBinary bin = BinaryBuilder.builder().withSize(cl).build();
//							bin.readBodyFromStream((InputStream) item.getObject());
//							ByteArrayOutputStream out = new ByteArrayOutputStream();
//							bin.writeBodyToStream(out);
//							return bin;
//						}
//					});
//					fbinary.writeBodyToStream(arg.getOutputStream());
//					arg.getOutputStream().flush();
//				}
//			});
//			return;
//		} catch (Throwable e) {
//			e.printStackTrace();
//		}
	}

	public void handlerLuiExtResource(String path, ServletResponse arg1) {
		try {
			String str5 = "/luiextcomp/";
			int index5 = path.indexOf(str5);
			path = path.substring(index5 + str5.length(), path.length());
			URL url = this.getClass().getClassLoader().getResource(path);
			if (url != null) {
				byte[] bytes = IOUtil.readBytesFromStream(url.openStream());
				arg1.getOutputStream().write(bytes);
				arg1.getOutputStream().flush();
				return;
			}
		} catch (Throwable e) {
			LuiLogger.error(e.getMessage());
		}
	}

	public void hanlderNodeResource(String path, ServletResponse arg1) {

		try {
			String str2 = "lui/nodes/";
			int index2 = path.indexOf(str2);
			String str3 = "lui/views/";
			int index3 = path.indexOf(str3);
			if (index2 > -1) {
				path = path.substring(index2, path.length());
			}
			if (index3 > -1) {
				path = path.substring(index3, path.length());
			}
			URL url = this.getClass().getClassLoader().getResource(path);
			if (url != null) {
				byte[] bytes = IOUtil.readBytesFromStream(url.openStream());
				arg1.getOutputStream().write(bytes);
				arg1.getOutputStream().flush();
				return;
			}
		} catch (Throwable e) {
			LuiLogger.error(e.getMessage());
		}

	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}
}
