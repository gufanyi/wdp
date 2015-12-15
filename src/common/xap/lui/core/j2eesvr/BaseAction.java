package xap.lui.core.j2eesvr;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import xap.lui.core.logger.LuiLogger;


/**
 * BaseAction的一个实现
 * 
 * @author licza.
 * 
 */
public class BaseAction implements BaseActionInterface {
	public HttpServletRequest request;
	public HttpServletResponse response;
	public StringBuffer htmlBuffer = new StringBuffer();

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	/**
	 * 输出HTML文档.
	 * 
	 * @param html
	 * @param docType
	 * @param charset
	 */
	private void print(Object html, String docType, String charset) {
		if (response.getContentType() == null) {
			response.setContentType(docType);
		}
		response.setCharacterEncoding(charset);
		response.setDateHeader("Expires", 0);
		response.setHeader("Cache-Control", "no-store");
		try {
			PrintWriter out = response.getWriter();
			out.print(html);
			out.flush();
		} catch (IOException e) {
			LuiLogger.error(e.getMessage(),e);
		}
	}

	/**
	 * 打印文档
	 * 
	 * @param html
	 */
	public void print(Object html) {
		htmlBuffer.append(html);
	}

	/**
	 * 打印文档并在页面上换行
	 */
	public void println(Object html) {
		htmlBuffer.append(html);
		htmlBuffer.append("<br/>\r\n");
	}

	/**
	 * 打印一个换行
	 */
	public void println() {
		htmlBuffer.append("<br/>\r\n");
	}

	/**
	 * 输出数据
	 */
	public void fush() {
		if (!response.isCommitted()) {
			print(htmlBuffer, "text/html", "UTF-8");
		}
	}

	/**
	 * 填充request及response参数
	 * 
	 * @param request
	 * @param response
	 */
	public void fill(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
	}

	/**
	 * request内跳转
	 * 
	 * @param url
	 * @throws IOException 
	 * @throws ServletException 
	 * @throws PortalServiceException
	 */
	public void go(String url) throws ServletException, IOException {
		request.getRequestDispatcher(url).forward(request, response);
	}

	/**
	 * 客户端301跳转
	 * 
	 * @param url
	 * @throws IOException 
	 * @throws PortalServiceException
	 */
	public void gun(String url) throws IOException {
		response.sendRedirect(url);
	}
	/**
	 * 注销登陆
	 * 
	 * @throws IOException
	 */
	public void logout() throws IOException {
		request.getSession().invalidate();
		response.sendRedirect(request.getContextPath() +"/app/mockapp/psm_login.jsp");
	}
	/**
	 * 打印提示消息
	 * 
	 * @param str
	 */
	public void alert(String str) {
		print("<script>alert('" + str + "');</script>");
	}
	/**
	 * 执行Js
	 * @param script
	 */
	public void addExecScript(String script){
		print("<script>" + script + "</script>");
	}
}
