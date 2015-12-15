package xap.lui.core.filemgr;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import xap.lui.core.vos.FileTypeVO;


public class TempFileFilter implements Filter {

	private static String Path_TempPath = "/office/filecache";
	@Override
	public void destroy() {
		
	}

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1,
			FilterChain arg2) throws IOException, ServletException {
		String url = ((HttpServletRequest) arg0).getQueryString();
		if(url.indexOf(Path_TempPath) > -1){
			String filename = url.substring( url.lastIndexOf("/"));
			if(filename.indexOf(".") > -1){
				String ext = filename.substring(filename.lastIndexOf(".") + 1);
				FileTypeVO vo = FileTypeHelper.getFiletype(ext);
				if(vo != null){
					arg1.setContentType(vo.getMime());
					arg2.doFilter(arg0, arg1);
				}
			}
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		
	}


}
