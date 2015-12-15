package xap.lui.core.event;

import xap.lui.core.builder.IRuntimeAdjuster;
import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.control.DftAppCtrl;
import xap.lui.core.listener.LuiEventConf;
import xap.lui.core.model.AppContext;
import xap.lui.core.model.AppSession;
import xap.lui.core.model.PagePartMeta;
import xap.lui.core.model.PlugEventAdjuster;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.core.refrence.IRefNode;
import xap.lui.core.refrence.SelfDefRefNode;

/**
 * 插件运行时调整类
 *
 */
public final class AppTypeRuntimeAdjuster implements IRuntimeAdjuster{
	public void adjust(PagePartMeta pageMeta) {
		AppSession ctx = AppSession.current();
		if(ctx == null)
			return;
		PlugEventAdjuster plugEventAdjuster = new PlugEventAdjuster();
		AppContext appCtx = ctx.getAppContext();
		plugEventAdjuster.addAppPlugEventConf(appCtx, pageMeta);
		plugEventAdjuster.addPlugEventConf(pageMeta);
		adjustRefNode(pageMeta);
		
		LuiEventConf event = new LuiEventConf();
		event.setEventType(PageEvent.class.getSimpleName());
		event.setMethod("onPageClosed");
		event.setEventName("onClosed");
		event.setNmc(false);
		event.setAsync(false);
		event.setControllerClazz(DftAppCtrl.class.getName());
		pageMeta.addEventConf(event);
	}
	
	private void adjustRefNode(PagePartMeta pageMeta) {
		ViewPartMeta[] widgets = pageMeta.getWidgets();
		AppSession ctx = AppSession.current();
		String appId = ctx.getAppContext().getAppId();
		for (int i = 0; i < widgets.length; i++) {
			IRefNode[] refnodes = widgets[i].getViewModels().getRefNodes();
			for (int j = 0; j < refnodes.length; j++) {
//				refnodes[j].setWindowType(PageMeta.WIN_TYPE_APP);
				if(refnodes[j] instanceof SelfDefRefNode){
					SelfDefRefNode selfRef = (SelfDefRefNode) refnodes[j];
					String path = selfRef.getPath();
					if(!path.startsWith("/")){
						path = LuiRuntimeContext.getRootPath() + "/app/" + appId + "/" + path;
						selfRef.setPath(path);
					}
				}
			}
		}
	}

}
