package xap.lui.core.j2eesvr;

import xap.lui.core.common.LuiWebContext;
import xap.lui.core.common.LuiWebSession;
import xap.lui.core.logger.LuiLogger;

/**
 * 默认WebSession 监听
 * @author dengjt
 *
 */
public class WebSessionListener implements IWebSessionListener{
	private static final long serialVersionUID = 1505801017808554358L;

	@Override
	public void sessionCreated(IWebSessionEvent sesEvent) {
		LuiWebSession ws = sesEvent.getWebSession();
		if(isAppSession(ws)){
			String appId = (String) ws.getAttribute(LuiWebContext.APP_ID);
			LuiLogger.info("App session created, id is " + appId);
		}
		else{
			LuiLogger.info("Page session created, id is " + ws.getPageId());
		}
	}

	@Override
	public void sessionDestroyed(IWebSessionEvent sesEvent) {
		LuiWebSession ws = sesEvent.getWebSession();
		if(isAppSession(ws)){
			String appId = (String) ws.getAttribute(LuiWebContext.APP_ID);
			LuiLogger.info("App session destroyed, id is " + appId);
		}
		else{
			LuiLogger.info("Page session destroyed, id is " + ws.getPageId());
		}
	}
	
	protected boolean isAppSession(LuiWebSession ws) {
		Boolean appSes = (Boolean) ws.getAttribute(LuiWebContext.APP_SES);
		return appSes == null ? false : appSes.booleanValue();
	}
	
}
