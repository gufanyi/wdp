package xap.lui.core.builder;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import xap.lui.core.common.ClientSession;
import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.common.LuiWebContext;
import xap.lui.core.constant.WebConstant;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.exception.LuiValidateException;
import xap.lui.core.layout.UIPartMeta;
import xap.lui.core.model.DefaultPageMetaValidator;
import xap.lui.core.model.IPageMetaValidator;
import xap.lui.core.model.IUIPartMeta;
import xap.lui.core.model.PagePartMeta;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.core.state.ButtonStateManager;

public class BaseWindow {
	private String pageModelId;
	private Properties props;
	private ClientSession clientSession = new ClientSession();
	private String etag;

	public BaseWindow() {
	}

	public void destroy() {
	}

	public Properties getProps() {
		return props;
	}

	public void setProps(Properties props) {
		this.props = props;
	}

	/**
	 * 初始化方法
	 */
	public final void internalInitialize() {
		String editMode = getWebSesssion().getParameter(WebConstant.EDIT_MODE_PARAM);
		String wmodel = getWebSesssion().getParameter(WebConstant.WINDOW_MODE_PARAM);
		if (wmodel != null && wmodel.equals("1"))
			getWebSesssion().getPageWebSession().setAttribute(WebConstant.WINDOW_MODE_KEY, WebConstant.MODE_PERSONALIZATION);
		if (editMode != null && editMode.equals("1"))
			getWebSesssion().getPageWebSession().setAttribute(WebConstant.EDIT_MODE_KEY, WebConstant.MODE_PERSONALIZATION);
		// 客户端模式（是否为离线应用）
		String clientMode = getWebSesssion().getParameter(WebConstant.CLIENT_MODE);
		if (clientMode != null)
			getWebSesssion().getPageWebSession().setAttribute(WebConstant.CLIENT_MODE, clientMode);
		String cachePath = getWebSesssion().getParameter(WebConstant.OFFLINE_CACHEPATH);
		if (cachePath != null)
			getWebSesssion().getPageWebSession().setAttribute(WebConstant.OFFLINE_CACHEPATH, cachePath);
		// 创建Pagemeta,建议此处仅进行Pagemeta的基础创建（此处获得的Pagemeta一般对所有人都一样）
		PagePartMeta pageMeta = createPageMeta();
		pageMeta.setWindow((Window) this);
		// 创建其它UI辅助元素
		IUIPartMeta uiMeta = createUIMeta(pageMeta);
		// 将Pagemeta放到上下文中，以便以后的方法可以取到
		getWebSesssion().setPageMeta(pageMeta);
		getWebSesssion().setUIMeta(uiMeta);
		String appUniqueId = getWebSesssion().getAppUniqueId();
		getClientSession().setAttribute(WebConstant.APP_UNIQUE_ID, appUniqueId, true);
		pageMeta.adjustForRuntime();
		if (uiMeta != null)
			uiMeta.adjustForRuntime();
		// Pagemeta的个性初始化，可在此处根据用户权限进行Pagemeta的“剪裁”
		initPageMetaStruct();
		// UIMeta的个性初始化，可在此处根据用户权限进行UIMeta的“剪裁”
		initUIMetaStruct();
		if (LuiRuntimeContext.getMode() == WebConstant.DEBUG_MODE) {
			validate();
		}
		// 将剪裁的Pagemeta“副本”放入用户当前页面session中进行存储，以便其它ajax逻辑使用。因为这以后的Pagemeta可能再次根据逻辑被填充数据。
		// PageMetaManager.getInstance().pubPageMetaToSession(getWebContext(),
		// pageUniqueId, (PageMeta) pageMeta.clone());
		if (!LuiRuntimeContext.isClientMode())
			adjustPageMeta(pageMeta);
		getWebSesssion().getPageWebSession().setAttribute(LuiWebContext.PAGEMETA_KEY, pageMeta);
		if (uiMeta != null)
			getWebSesssion().getPageWebSession().setAttribute(LuiWebContext.UIMETA_KEY, uiMeta);
		LuiRuntimeContext.getWebContext().adjustPageUniqueId(etag);
		String pageUniqueId = getWebSesssion().getPageUniqueId();
		// 将页面唯一ID写到客户端，确保客户端能够获取到对应信息
		getClientSession().setAttribute(WebConstant.PAGE_UNIQUE_ID, pageUniqueId, true);
		String pageId = getPageModelId();
		// 将页面节点ID写到客户端。
		getClientSession().setAttribute(WebConstant.PAGE_ID, pageId, true);
		String parentUniqueId = getWebSesssion().getParentPageUniqueId();
		if (parentUniqueId != null)
			// 将页面节点ID写到客户端。
			getClientSession().setAttribute(WebConstant.OTHER_PAGE_UNIQUE_ID, parentUniqueId, true);
		String parentPageId = getWebSesssion().getParentPageId();
		if (parentPageId != null)
			getClientSession().setAttribute(WebConstant.OTHER_PAGE_ID, parentPageId, true);
		// Map<String, String> langResMap = processLangRes();
		afterInternalInitialize();
		for (ViewPartMeta widget : getPageMeta().getWidgets()) {
			widget.clearCtxChanged();
		}
	}

	protected void afterInternalInitialize() {
		ButtonStateManager.updateButtons(getPageMeta());
	}

	protected void initUIMetaStruct() {
	}

	/**
	 * 生成UI的辅助元素，实现类可按照自己的UI组织形式具体化
	 * 
	 * @return
	 */
	protected IUIPartMeta createUIMeta(PagePartMeta pm) {
		return null;
	}

	/**
	 * 调整页面Listener，要求每个页面必须有close方法，以确保内存及时释放
	 * 
	 * @param pageMeta
	 */
	private void adjustPageMeta(PagePartMeta pageMeta) {
		IRuntimeAdjuster adjuster = RuntimeAdjusterFactory.getRuntimeAdjuster();
		if (adjuster != null)
			adjuster.adjust(pageMeta);
		IRuntimeAdjuster codeRuleAdjuster = new CodeRuleAdjuster();
		codeRuleAdjuster.adjust(pageMeta);
	}

	protected Map<String, String> processLangRes() {
		return null;
	}

	protected void initPageMetaStruct() {
	}

	protected PagePartMeta createPageMeta() {
		return PagePartMetaBuilder.buildPageMeta((Window)this);
	}

	public PagePartMeta getPageMeta() {
		return getWebSesssion().getPageMeta();
	}

	public UIPartMeta getUIPartMeta() {
		return (UIPartMeta) getWebSesssion().getUIMeta();
	}

	public String getPageModelId() {
		if (pageModelId == null)
			pageModelId = getWebSesssion().getPageId();
		return pageModelId;
	}

	public void setPageModelId(String pageModelId) {
		this.pageModelId = pageModelId;
	}

	public ClientSession getClientSession() {
		return clientSession;
	}

	public LuiWebContext getWebSesssion() {
		return LuiRuntimeContext.getWebContext();
	}

	private void validate() {
		try {
			IPageMetaValidator validator = getValidator();
			if (validator == null)
				return;
			validator.validate(getPageMeta());
		} catch (LuiValidateException e) {
			throw new LuiRuntimeException(e.getMessage(), e);
		}
	}

	protected IPageMetaValidator getValidator() {
		return new DefaultPageMetaValidator();
	}

	/**
	 * 获取客户端缓存标识，本方法只根据文件与模板ts做缓存，如果需要做二级缓存，需谨慎判断是否则需重写此方法。
	 * 
	 * @return
	 */
	public String getEtag() {
		PagePartMeta pm = getPageMeta();
		if (pm == null || pm.getEtag() == null)
			return null;
		return getPageModelId() + pm.getEtag();
	}

	public LinkedHashMap<String, String> getDimensions() {
		return null;
	}
}
