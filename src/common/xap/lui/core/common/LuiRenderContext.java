package xap.lui.core.common;

import xap.lui.core.layout.UIPartMeta;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.model.PagePartMeta;

public class LuiRenderContext {

	private static ThreadLocal<LuiRenderContext> threadLocal = new ThreadLocal<LuiRenderContext>() {

		@Override
		protected LuiRenderContext initialValue() {
			LuiRenderContext ctx = new LuiRenderContext();
			return ctx;
		}

	};

	private PagePartMeta pagePartMeta;

	private UIPartMeta uiPartMeta;
	
	private LifeCyclePhase phase;
	
	

	public LifeCyclePhase getPhase() {
		return phase;
	}

	public void setPhase(LifeCyclePhase phase) {
		this.phase = phase;
	}

	public static LuiRenderContext current() {
		return threadLocal.get();
	}

	public static void current(LuiRenderContext current) {
		threadLocal.set(current);
	}

	public PagePartMeta getPagePartMeta() {
		return pagePartMeta;
	}

	public void setPagePartMeta(PagePartMeta pagePartMeta) {
		this.pagePartMeta = pagePartMeta;
	}

	public UIPartMeta getUiPartMeta() {
		return uiPartMeta;
	}

	public void setUiPartMeta(UIPartMeta uiPartMeta) {
		this.uiPartMeta = uiPartMeta;
	}

}
