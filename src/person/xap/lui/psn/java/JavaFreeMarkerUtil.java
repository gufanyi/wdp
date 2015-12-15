package xap.lui.psn.java;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.util.Map;

import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.logger.LuiLogger;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class JavaFreeMarkerUtil {
	private static JavaFreeMarkerUtil instance = null;
	private static Configuration cfg = null;
	static {
		instance = new JavaFreeMarkerUtil();
		cfg = new Configuration();
		cfg.setDefaultEncoding("UTF-8");
		try {
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			URL fileUrl = loader.getResource("xap/lui/psn/java");
			String filePath = fileUrl.getFile();
			File file = new File(filePath);
			cfg.setDirectoryForTemplateLoading(file);
		} catch (IOException e) {
			LuiLogger.error(e.getMessage(), e);
		} finally {
			cfg.setObjectWrapper(new DefaultObjectWrapper());
		}
	}
	public static JavaFreeMarkerUtil getInstance() {
		return instance;
	}
	public static String processTemplate(String templateName, Map<String, Object> root) {
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
		Configuration config = JavaFreeMarkerUtil.cfg;
		@SuppressWarnings("deprecation")
		ClassTemplateLoader loader = new ClassTemplateLoader() {
			@Override
			protected URL getURL(String name) {
				return JavaFreeMarkerUtil.class.getClassLoader().getResource("xap/lui/psn/java/" + name);
			}
		};
		config.setTemplateLoader(loader);
		Template template = config.getTemplate(templateName, "utf-8");
		template.process(root, out);
		out.flush();
		return out.toString();
	}
}
