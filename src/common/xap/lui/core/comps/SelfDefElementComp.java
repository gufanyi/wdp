package xap.lui.core.comps;

import xap.lui.core.context.BaseContext;
import xap.lui.core.context.TextAreaContext;

/**
 * 自定义表单元素控件
 * 
 * @author guoweic
 *
 */
public class SelfDefElementComp extends TextComp {

	private static final long serialVersionUID = 6509930301132104402L;

	
	@Override
	public BaseContext getContext() {
		TextAreaContext ctx = new TextAreaContext();
		ctx.setId(this.getId());
//		ctx.setEnabled(this.enabled);
//		ctx.setValue(this.getValue());
//		ctx.setFocus(this.isFocus());
		return ctx;
	}
	
	@Override
	public void setContext(BaseContext ctx) {
		TextAreaContext taCtx = (TextAreaContext) ctx;
		this.setEnabled(taCtx.isEnabled());
		this.setValue(taCtx.getValue());
		this.setCtxChanged(false);
	}
	
}
