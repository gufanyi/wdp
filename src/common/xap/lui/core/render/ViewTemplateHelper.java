package xap.lui.core.render;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.commons.io.IOUtils;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.logger.LuiLogger;

public class ViewTemplateHelper {
	private static final String JSP_PATH = "html/nodes/jsp/raview.jsp";
	private static String template;
	public static String getTemplateString() {
		
		if(template == null){
			InputStream input = null;
			BufferedReader reader = null;
			try {
				input = Thread.currentThread().getContextClassLoader().getResourceAsStream(JSP_PATH);
				reader = new BufferedReader(new InputStreamReader(input));
				StringBuffer buf = new StringBuffer();
				String str = reader.readLine();
				while(str != null){
					buf.append(str);
					str = reader.readLine();
				}
				template = buf.toString();
			} 
			catch (IOException e) {
				LuiLogger.error(e);
				throw new LuiRuntimeException(e);
			}
			finally{
				if(reader != null)
					IOUtils.closeQuietly(reader);
				if(input != null)
					IOUtils.closeQuietly(input);
			}
		}
		
		return template;
	}
}
