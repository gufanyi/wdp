package xap.lui.core.j2eesvr;

import xap.lui.core.common.AbstractPageSession;
import xap.lui.core.common.LuiWebSession;

/**
 * WebSession会话事件
 * @author dengjt
 *
 */
public class WebSessionEvent implements IWebSessionEvent {
	private static final long serialVersionUID = -7614831985850849442L;
	private LuiWebSession webSession;
	public WebSessionEvent(AbstractPageSession ws) {
		this.webSession = ws;
	}
	public LuiWebSession getWebSession() {
		return webSession;
	}
	public void setWebSession(LuiWebSession webSession) {
		this.webSession = webSession;
	}
}
