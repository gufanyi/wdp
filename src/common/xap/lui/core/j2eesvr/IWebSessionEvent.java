package xap.lui.core.j2eesvr;

import java.io.Serializable;

import xap.lui.core.common.LuiWebSession;

public interface IWebSessionEvent extends Serializable{
	public LuiWebSession getWebSession();
}
