package xap.lui.core.common;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import xap.lui.core.j2eesvr.IWebSessionListener;


public class LocalWebSession extends AbstractPageSession{
	private static final long serialVersionUID = 8733097358391655209L;
	private String pageId;
	private String sesId;
	private Map<String, Serializable> objMap = null;

	public LocalWebSession(IWebSessionListener ... wsListener) {
		super(wsListener);
	}
	
	@Override
	public void destroy() {
		super.destroy();
	}

	@Override
	public void created() {
		super.created();
	}
	
	public Serializable getAttribute(String key) {
		return objMap == null ? null : objMap.get(key);
	}

	public String getPageSessionId() {
		return sesId;
	}

	public Serializable removeAttribute(String key) {
		if(objMap == null)
			return null;
		return objMap.remove(key);
	}

	public void setAttribute(String key, Serializable value) {
		if(objMap == null)
			objMap = new HashMap<String, Serializable>();
		objMap.put(key, value);
	}

	public void setWebSessionId(String sesId) {
		this.sesId = sesId;
	}

	public String getPageId() {
		return pageId;
	}

	public void setPageId(String pageId) {
		this.pageId = pageId;
	}

	public String getSesId() {
		return sesId;
	}

	public void setSesId(String sesId) {
		this.sesId = sesId;
	}
}
