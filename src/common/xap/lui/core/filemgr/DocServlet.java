package xap.lui.core.filemgr;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;



/**
 * 
 * @author lisw
 *
 */
public class DocServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 87459448170492607L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String url = req.getParameter("disp");
		if(StringUtils.isEmpty(url)) return ;
		if(url.toLowerCase().startsWith("/portal"))
			url = url.substring(7);
		if(StringUtils.isEmpty(url)) return;
		if(url.toLowerCase().startsWith("/docctr/open/word.docx"))
			return;
		
		RequestDispatcher dispatcher = req.getSession().getServletContext().getRequestDispatcher(url);
		dispatcher.forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}

}
