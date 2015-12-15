/**
 * 
 */
package xap.lui.core.builder;
import java.io.InputStream;
import java.io.StringReader;
import org.apache.commons.io.IOUtils;
import xap.lui.core.common.ContextResourceUtil;
import xap.lui.core.logger.LuiLogger;
import xap.lui.core.model.Application;
import xap.lui.core.util.JaxbMarshalFactory;
import xap.lui.core.util.JaxbUtil;
/**
 * @author chouhl
 *
 */
public class ApplicationParser {
	public static Application parse(InputStream xml) {
		Application applicationConf = null;
		// if (xml.exists() && xml.isFile()) {
		try {
			String str =ContextResourceUtil.inputStream2String(xml);
			//applicationConf=(Application)JaxbUtil.parser(Application.class, new StringReader(str));
			applicationConf = JaxbMarshalFactory.newIns().decodeXML(Application.class, str);
			applicationConf.jaxbToData();
		} catch (Exception e) {
			LuiLogger.error(e.getMessage(), e);
		}
		// }
		return applicationConf;
	}
}
