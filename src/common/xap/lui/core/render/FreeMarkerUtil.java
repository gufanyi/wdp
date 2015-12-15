package xap.lui.core.render;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.util.Map;
import xap.lui.core.exception.LuiRuntimeException;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
public class FreeMarkerUtil {
	public static String processCreateHtmlName(String templateName, Map<String, Object> root) {
		String str = null;
		final Writer out = new StringWriter();
		try {
			str = processTemplate(templateName + ".ftl", root, out);
		} catch (Throwable e) {
			throw new LuiRuntimeException(e.getMessage());
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				
				
			}
		}
		return str;
	}
	public static String processTemplate(String templateName, Map<String, Object> root, Writer out) throws IOException, TemplateException {
		Configuration config = CmsFrameMarkerConfig.getInstance().getCfg();
		@SuppressWarnings("deprecation")
		ClassTemplateLoader loader=new ClassTemplateLoader(){
			@Override
			protected URL getURL(String name) {
				return FreeMarkerUtil.class.getClassLoader().getResource("xap/lui/core/ftl/"+name);
			}
		};
		config.setTemplateLoader(loader);
		Template template = config.getTemplate(templateName, "utf-8");
		template.process(root, out);
		out.flush();
		return out.toString();
	}
}
