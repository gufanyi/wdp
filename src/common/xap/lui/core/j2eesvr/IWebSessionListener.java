package xap.lui.core.j2eesvr;

import java.io.Serializable;

/**
 * WebSession监听
 * @author dengjt
 *
 */
public interface IWebSessionListener extends Serializable{
	public void sessionCreated(IWebSessionEvent sesEvent);
	public void sessionDestroyed(IWebSessionEvent sesEvent);
}
