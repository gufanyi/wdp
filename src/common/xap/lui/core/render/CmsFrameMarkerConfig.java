package xap.lui.core.render;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.logger.LuiLogger;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
public class CmsFrameMarkerConfig {
	public Configuration getCfg() {
		return cfg;
	}
	private static Configuration cfg = null;
	private static CmsFrameMarkerConfig instance = null;
	public static final String Www_Root_Relase = "wwwroot";
	public static final String FtlFloder = "template";
	public static final String FtlFunctionsPkg = "com.cms.freemarker.method";
	public static final String FtlMarcoPkg = "com.cms.freemarker.directivetag";
	private CmsFrameMarkerConfig() {}
	public static CmsFrameMarkerConfig getInstance() {
		if (instance == null) {
			synchronized (CmsFrameMarkerConfig.class) {
				if (instance == null) {
					synchronized (CmsFrameMarkerConfig.class) {
						instance = new CmsFrameMarkerConfig();
						cfg = new Configuration();
						cfg.setDefaultEncoding("UTF-8");
						try {
							ClassLoader loader = Thread.currentThread().getContextClassLoader();
							URL fileUrl = loader.getResource("xap/lui/core/ftl");
							String filePath = fileUrl.getFile();
							File file = new File(filePath);
							cfg.setDirectoryForTemplateLoading(file);
						} catch (IOException e) {
							LuiLogger.error(e.getMessage(), e);
							//throw new LuiRuntimeException(e.getMessage());
						}finally{
							cfg.setObjectWrapper(new DefaultObjectWrapper());
							loadSelfFuncAndMarc();
						}
					}
				}
			}
		}
		return instance;
	}
	protected static void loadSelfFuncAndMarc() {
		try {
			cfg.setSharedVariable("luibody", new LuiBodyDirectiveTemplate());
		} catch (Throwable e) {
			throw new LuiRuntimeException(e.getMessage());
		}
		// {
		// try {
		// cfg.setSharedVariable("getCatalogItself", new
		// FCatalogItselfMethod());
		// cfg.setSharedVariable("getCatalogSubordinate", new
		// FCatalogSubordinateMethod());
		// cfg.setSharedVariable("getCatalogSuperior", new
		// FCatalogSuperiorMethod());
		// cfg.setSharedVariable("getPlaceCatalog", new FPlaceCatalogMethod());
		// cfg.setSharedVariable("getPlaceAritle", new FPlaceArticleMethod());
		// } catch (Throwable e) {
		// LuiLogger.error(e.getMessage(), e);
		// throw new LuiRuntimeException(e.getMessage());
		// }
	}
}
