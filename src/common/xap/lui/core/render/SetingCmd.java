package xap.lui.core.render;

import java.util.UUID;

import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.constant.ParamConstant;
import xap.lui.core.layout.UIPartMeta;
import xap.lui.core.model.AppContext;
import xap.lui.core.model.AppSession;
import xap.lui.core.model.PagePartMeta;
import xap.lui.psn.refence.BorderCssStylePageModel;
import xap.lui.psn.refence.BorderCssStyleViewController;

/**
 * 对于控件属性进行编辑的后台处理过程
 * 
 * @author wupeng1
 */
public class SetingCmd extends AbstractRaCommand {

	public SetingCmd(RaParameter rp) {
		super(rp);
	}

	@Override
	public void execute() {
		this.doSetting(rp.getUiMeta(), rp.getPageMeta());
	}

	private void doSetting(UIPartMeta uiMeta, PagePartMeta pageMeta) {
		// 根据参数获取前台处理信息
		UpdateParameter param = rp.getParam();
		String sourceWinId = rp.getPageMeta().getId();
		String sourceEleId = rp.getEleId();
		String sourceViewId = param.getViewId();
		String sourceType = rp.getType();
		String uiId = rp.getUiId();

		// 拼接url后的参数
		String paramBuf = "?sourceEleId=" + sourceEleId + "&sourceWinId=" + sourceWinId;
		paramBuf = paramBuf + "&sourceViewId=" + sourceViewId + "&sourceType=" + sourceType;
		paramBuf = paramBuf + "&enterParam="+UUID.randomUUID();
		StringBuffer urlBuf = new StringBuffer();
		if (sourceType.equals("menubar")) {
			urlBuf.append(LuiRuntimeContext.getRootPath()).append("/app/mockapp/menumgr").append(paramBuf);
			AppSession.current().getAppContext().popOuterWindow(urlBuf.toString(), "编辑属性", "517", "400", AppContext.TYPE_DIALOG);
		} else if(sourceType.equals("toolbar_button")) {
			urlBuf.append(LuiRuntimeContext.getRootPath()).append("/app/mockapp/toolbarmgr").append(paramBuf);
			AppSession.current().getAppContext().popOuterWindow(urlBuf.toString(), "编辑属性", "517", "400", AppContext.TYPE_DIALOG);
		} else if ("flowhlayout".equalsIgnoreCase(sourceType) || "flowvlayout".equalsIgnoreCase(sourceType)) {
			String paramStr = "&uiId=" + uiId + "&sourceWinId=" + sourceWinId + "&sourceViewId=" + sourceViewId + "&sourceType=" + sourceType;
			String url = LuiRuntimeContext.getRootPath() + "/app/mockapp/cdref?model=" + BorderCssStylePageModel.class.getName() + "&ctrl=" + BorderCssStyleViewController.class.getName();
			url = url + paramStr;
			AppSession.current().getAppContext().popOuterWindow(url, "设置边框", "725", "400", AppContext.TYPE_DIALOG, true);
		} else {
			String otherPageId = LuiRuntimeContext.getWebContext().getParentPageUniqueId();
			paramBuf = paramBuf + "&" + ParamConstant.OTHER_PAGE_UNIQUE_ID + "=" +  otherPageId;
			urlBuf.append(LuiRuntimeContext.getRootPath()).append("/app/mockapp/fieldmgr").append(paramBuf);
			AppSession.current().getAppContext().popOuterWindow(urlBuf.toString(), "编辑属性", "680", "480", AppContext.TYPE_DIALOG);
		}

	}

}
