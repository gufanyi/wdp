package xap.lui.psn.pamgr;

import xap.lui.core.builder.Window;
import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.comps.IFrameComp;
import xap.lui.core.constant.PaConstant;

public class PaPageModel extends Window {

	@Override
	protected void initPageMetaStruct() {
		super.initPageMetaStruct();
		IFrameComp ifm = (IFrameComp) getPageMeta().getWidget("editor").getViewComponents().getComponent("mainif");
		String url = LuiRuntimeContext.getWebContext().getParameter(PaConstant.PA_URL);
		url = "core/uimeta.um?pageId=test" + "&emode=1";
		ifm.setSrc(url);
		
	}
	
}
