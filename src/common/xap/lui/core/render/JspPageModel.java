package xap.lui.core.render;

import xap.lui.core.builder.BaseWindow;
import xap.lui.core.layout.UIPartMeta;
import xap.lui.core.model.IUIPartMeta;
import xap.lui.core.model.PagePartMeta;

public class JspPageModel extends BaseWindow {
	@Override
	protected IUIPartMeta createUIMeta(PagePartMeta pm) {
		UIPartMeta um = new UIPartMeta();
		return um;
	}

}
