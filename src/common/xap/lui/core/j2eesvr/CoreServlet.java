/**
 * 
 */
package xap.lui.core.j2eesvr;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import xap.lui.core.common.ModelServerConfig;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.logger.LuiLogger;
import xap.lui.core.util.XmlUtilPatch;

/**
 * Portal分发Servlet.Portal容器级请求都将通过此servlet转发。
 * 由于容器基于ajax请求，所有未捕获异常将在此servlet中截获并包装成xml形式返回。
 */
public class CoreServlet extends BaseServlet {
	protected Initialization inl;
	private static final long serialVersionUID = 4374801516824860121L;
	private String ERROR_STRING = null;
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		inl=new Initialization();
		String actionFolder = getActionFolder(config);
		inl.setActionFolder(actionFolder);
		String urlPrefix = getUrlPrefix(config);
		inl.setUrlPrefix(urlPrefix);
		inl.registerUrl();
 	}

	protected String getUrlPrefix(ServletConfig config) {
		String urlPrefix=config.getInitParameter("urlprefix");
		return urlPrefix;
	}

	protected String getActionFolder(ServletConfig config) {
		
		ModelServerConfig msc = new ModelServerConfig(config.getServletContext());
		String actionFolder = config.getInitParameter("actionfolder");
		String actionProp = msc.getConfigValue("actionfolder");
		
		if(actionFolder != null){
			if(actionProp != null){
				return actionFolder + "," + actionProp;
			}
		}else{
			if(actionProp != null){
				return actionProp;
			}
		}
		return actionFolder;
	}
	
	/**
	 * 是否捕捉异常。防止捕获两重异常
	 * @return
	 */
	protected boolean catchExp()
	{
		return true;
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
	
	
	
	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.getSession();
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String url = request.getServletPath() + request.getPathInfo();
			LuiLogger.debug("URL:" + url);
			java.util.logging.Logger.getAnonymousLogger().info(url);
 			Method method = inl.getUrlMap().get(url); 
			if(method == null){
				response.sendError(404);
			}else {
				ServletForward.forward(method, request, response);
			}
		} 
		catch (Throwable e) { 
			processError(request,response,e);
		}  
	}

	protected void processError(HttpServletRequest request,
			HttpServletResponse response, Throwable e) throws IOException, ServletException {
		String title = (String) request.getAttribute("title");
		String errorContent;
		if(e instanceof LuiRuntimeException)
			errorContent = ((LuiRuntimeException)e).getHint();
		else
			errorContent = getErrorPageText(request);
		if(!response.isCommitted())
			returnXMLString(title, errorContent, e,response);
	}


	/**
	 * 返回固定格式的xml String字符串
	 * 
	 * @param windowState
	 * @param portletMode
	 * @param title
	 * @param content
	 * @param e
	 * @param response
	 * @throws IOException
	 */
	private void returnXMLString(
			 String title, String content, Throwable e,
			HttpServletResponse response) throws IOException {
		response.setContentType("text/xml");
		response.setCharacterEncoding("UTF-8");
		Document doc = XmlUtilPatch.getNewDocument();
		Element rootNode = doc.createElement("AJAX_CONTENT");
		Element contentNode = doc.createElement("p_content");
		contentNode.appendChild(doc.createCDATASection(content));
		rootNode.appendChild(contentNode);
		doc.appendChild(rootNode);
		
		Element scriptNode = doc.createElement("p_script");
		scriptNode.appendChild(doc.createTextNode(""));
		rootNode.appendChild(scriptNode);
		Element titleNode = doc.createElement("p_title");
		if (title != null && !title.equals(""))
			titleNode.appendChild(doc.createTextNode(title));
		rootNode.appendChild(titleNode);
			
		Element expNode = doc.createElement("exp-sign");
		if (e != null)
			expNode.appendChild(doc.createTextNode("true"));
		else
			expNode.appendChild(doc.createTextNode("false"));
		rootNode.appendChild(expNode);
		
		Element expTextNode = doc.createElement("exp-text");
		if (e != null)
			expTextNode.appendChild(doc.createTextNode(e.getMessage() == null ? "" : e.getMessage()));
		else
			expTextNode.appendChild(doc.createTextNode(""));
		rootNode.appendChild(expTextNode);
		
		Element expStackNode = doc.createElement("exp-stack");
		if (e != null) {
			StringWriter writer = new StringWriter();
			PrintWriter pw = new PrintWriter(writer);
			pw.close();
			writer.close();
			expStackNode.appendChild(doc.createCDATASection(writer.toString()));
		} else
			expStackNode.appendChild(doc.createTextNode(""));
		rootNode.appendChild(expStackNode);
		
//		Element typeNode = doc.createElement("p_type");
//		typeNode.appendChild(doc.createTextNode(String.valueOf(IPortletType.JSP_PORTLET)));
//		rootNode.appendChild(typeNode);
//		Writer writer;
//		try{
//			writer = response.getWriter();
//		}
//		catch(IllegalStateException ille){
//			writer = new BufferedWriter(new OutputStreamWriter(response.getOutputStream(), "UTF-8"));
//		}
//		XMLUtil.printDOMTree(writer, doc, 0, "UTF-8");
	}

	private String getErrorPageText(HttpServletRequest req) {
		ServletContext ctx = req.getSession().getServletContext();
		if (ERROR_STRING == null) {
			try {
				//ERROR_STRING = HttpUtil.URLtoString(ctx.getResource("/html/portal/portlet_error.jsp"));
			} catch (Exception e) {
				ERROR_STRING = "This Portlet has something error occurred!";
			}
		}
		return ERROR_STRING;
	}
}
