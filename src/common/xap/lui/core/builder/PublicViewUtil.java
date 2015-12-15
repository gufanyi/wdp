package xap.lui.core.builder;

import xap.lui.core.layout.UIPartMeta;
import xap.lui.core.layout.UIViewPart;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.core.model.PagePartMeta;
import xap.lui.core.model.PoolObjectManager;
import xap.lui.core.model.ViewPartConfig;
import xap.lui.core.parser.UIMetaParserUtil;
import xap.lui.core.util.ClassUtil;

/**
 * 公共View 引入工具类
 *
 */
public final class PublicViewUtil {
	public static ViewPartMeta addPublicView(String id, String refViewId, PagePartMeta pm){
		ViewPartConfig wconfig = new ViewPartConfig();
		wconfig.setId(id);
		wconfig.setRefId("../" + refViewId);
		pm.addViewPartConf(wconfig);
		
		ViewPartMeta widget = PoolObjectManager.getWidget(refViewId);
		String providerClazz = widget.getProvider();
		if(providerClazz != null && !providerClazz.equals("")){
			ViewPartProvider provider = (ViewPartProvider) ClassUtil.newInstance(providerClazz);
			widget = provider.buildWidget(pm, widget, null, id);
			widget.setIsCustom(false);
		}
		
		widget.setId(id);
		pm.addWidget(widget);
		return widget;
	}
	
	public static UIViewPart addPublicViewUI(String id, PagePartMeta pm, ViewPartMeta widget){
		UIMetaParserUtil util = new UIMetaParserUtil(false);
		util.setPagemeta(pm);
		UIPartMeta um = util.parsePublicUIMeta(id);
		
		UIViewPart uiWidget = new UIViewPart();
		uiWidget.setId(id);
		uiWidget.addPanel(um);
		return uiWidget;
	}
}
