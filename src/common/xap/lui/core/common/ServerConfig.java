package xap.lui.core.common;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Properties;

import javax.servlet.ServletContext;

import xap.lui.core.logger.LuiLogger;
/**
 * Lui web应用配置
 * 
 */
public class ServerConfig implements Serializable {
	private static final long serialVersionUID = -851156071995482872L;
	private static final String DEFAULT_THEME_ID = "DEFAULT_THEME_ID";
	private static final String DEFAULT_LANG_ID = "DEFAULT_LANG_ID";
	// 窗口打开方式,0:普通打开 1:打开最大化
	private static final String DEFAULT_WINDOW_OPENMODE = "DEFAULT_WINDOW_OPENMODE";
	private static final String LOAD_RUNNER_ADAPTER = "LOAD_RUNNER_ADAPTER";
	private static final String DOMAIN = "DOMAIN";
	private Properties props = null;
	public ServerConfig(ServletContext ctx) {
		props = new Properties();
		InputStream is = null;
		try {
			is = ctx.getResourceAsStream("/WEB-INF/conf/system.properties");
			if(is==null){
				return;
			}
			props.load(is);
		} catch (Exception e) {
			LuiLogger.error(e.getMessage(), e);
		} finally {
			try {
				if (is != null)
					is.close();
			} catch (IOException e) {
				LuiLogger.error(e.getMessage(), e);
			}
		}
	}
	public String getDefaultThemeId() {
		return props.getProperty(DEFAULT_THEME_ID) == null ? "default" : props.getProperty(DEFAULT_THEME_ID);
	}
	public String getDefaultLangId() {
		return props.getProperty(DEFAULT_LANG_ID) == null ? "simpchn" : props.getProperty(DEFAULT_LANG_ID);
	}
	public String getDefaultWindowOpenMode() {
		return props.getProperty(DEFAULT_WINDOW_OPENMODE) == null ? "0" : props.getProperty(DEFAULT_WINDOW_OPENMODE);
	}
	public String getLoadRunnerAdpater() {
		return props.getProperty(LOAD_RUNNER_ADAPTER) == null ? "0" : props.getProperty(LOAD_RUNNER_ADAPTER);
	}
	public String get(String key) {
		return props.getProperty(key);
	}
	public String getDomain() {
		String domain = props.getProperty(DOMAIN);
		if (domain == null || domain.equals(""))
			return null;
		return domain;
	}
}
