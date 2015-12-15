package xap.lui.core.processor;

import xap.lui.core.model.LuiPageContext;

public class EventRequestContext {
	private static ThreadLocal<LuiPageContext> threadLocal = new ThreadLocal<LuiPageContext>(){
		protected LuiPageContext initialValue() {
	       return null;
		}
	};
	
	public static LuiPageContext getLuiPageContext() {
		return threadLocal.get();
	}
	
	public static void setLuiPageContext(LuiPageContext ctx) {
		threadLocal.set(ctx);
	}
	
	public static void reset() {
		threadLocal.remove();
	}
}
