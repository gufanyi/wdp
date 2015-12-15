package xap.lui.core.common;

import java.util.HashMap;
import java.util.Map;

import xap.lui.core.exception.LuiLicenseException;
import xap.lui.core.exception.LuiSecurityException;
import xap.lui.core.j2eesvr.IWebSessionEvent;
import xap.lui.core.j2eesvr.IWebSessionListener;
import xap.lui.core.j2eesvr.WebSessionEvent;
import xap.lui.core.logger.LuiLogger;


/**
 * 页面会话虚基类
 * @author dengjt
 *
 */
public abstract class AbstractPageSession implements LuiWebSession {
	private static final long serialVersionUID = -4194778196164674386L;
	private Map<String, String> paramMap;
	private IWebSessionListener[] wsListeners;
	public AbstractPageSession(IWebSessionListener... wsListener) {
		this.wsListeners = wsListener;
	}
	public String getOriginalParameter(String key) {
		return paramMap == null ? null : paramMap.get(key);
	}
	public void addOriginalParameter(String key, String value) {
		if(paramMap == null){
			paramMap = new HashMap<String, String>();
		}
		paramMap.put(key, value);
	}
	public Map<String, String> getOriginalParamMap() {
		return paramMap;
	}
	
	public void created() {
		IWebSessionListener[] wsListeners = getWebSessionListeners();
		if(wsListeners != null){
			IWebSessionEvent event = new WebSessionEvent(this);
			for (int i = 0; i < wsListeners.length; i++) {
				try{
					wsListeners[i].sessionCreated(event);
				}
				catch(LuiSecurityException se){
					LuiLogger.error(se);
					throw se;
				}
				catch(LuiLicenseException e){
					LuiLogger.error(e);
					throw e;
				}
				catch(Exception e){
					LuiLogger.error(e);
				}
			}
		}
	}
	
	public void destroy() {
		IWebSessionListener[] wsListeners = getWebSessionListeners();
		if(wsListeners != null){
			IWebSessionEvent event = new WebSessionEvent(this);
			for (int i = 0; i < wsListeners.length; i++) {
				wsListeners[i].sessionDestroyed(event);
			}
		}
	}
	
	public IWebSessionListener[] getWebSessionListeners(){
		return wsListeners;
	}
}
