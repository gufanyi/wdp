package xap.lui.core.util;

import java.io.Serializable;

import xap.lui.core.model.AppSession;
import xap.lui.core.model.Application;
import xap.lui.core.model.AppContext;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.core.model.PagePartMeta;
import xap.lui.core.model.ViewPartContext;
import xap.lui.core.model.WindowContext;

public class WFWUtil {
	public static Serializable getAppAttr(String key) {
		Serializable obj = (Serializable) WFWUtil.getCntAppCtx().getAppAttribute(key);
		return (Serializable) obj;
	}

	public static void addAppAttr(String key, Serializable value) {
		WFWUtil.getCntAppCtx().addAppAttribute(key, value);
	}

	public static AppContext getCntAppCtx() {
		return AppSession.current().getAppContext();
	}

	public static Application getCntApplication() {
		return WFWUtil.getCntAppCtx().getApplication();
	}

	public static WindowContext getCntWindowCtx() {
		return WFWUtil.getCntAppCtx().getCurrentWindowContext();
	}

	public static PagePartMeta getCntWindow() {
		return WFWUtil.getCntWindowCtx().getPagePartMeta();
	}

	public static ViewPartContext getCntViewCtx() {
		return WFWUtil.getCntWindowCtx().getCurrentViewContext();
	}

	public static ViewPartMeta getCntView() {
		return WFWUtil.getCntViewCtx().getView();
	}

	public static ViewPartMeta getWidget(String name) {
		return WFWUtil.getCntWindow().getWidget(name);
	}

	public static boolean isDebugModel() {
		String runMode = "develop";// System.getProperty(MWConstants.HaiYouRunMode);
		if ("develop".equals(runMode)) {
			return true;
		} else {
			return false;
		}
	}
}
