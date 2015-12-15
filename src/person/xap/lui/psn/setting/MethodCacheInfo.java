package xap.lui.psn.setting;

import java.lang.reflect.Method;

import xap.lui.core.logger.LuiLogger;

public class MethodCacheInfo {

	private Object obj = null;
	private Method method = null;
	
	

	public MethodCacheInfo(Object obj, Method method) {
		super();
		this.obj = obj;
		this.method = method;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public void invoke(Object args) {
		try {
			method.invoke(obj, args);
		} catch (Throwable e) {
			LuiLogger.error(e.getMessage());
		}
	}

}
