package xap.lui.core.render;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import xap.lui.core.cache.CacheMgr;
import xap.lui.core.cache.LUICache;
import xap.lui.core.common.ContextResourceUtil;
import xap.lui.core.common.LuiRenderContext;
import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.common.PageControlHandler;
import xap.lui.core.comps.LuiElement;
import xap.lui.core.constant.WebConstant;
import xap.lui.core.control.ModePhase;
import xap.lui.core.event.IEventSupport;
import xap.lui.core.layout.UIConstant;
import xap.lui.core.layout.UIDialog;
import xap.lui.core.layout.UIElement;
import xap.lui.core.layout.UIPartMeta;
import xap.lui.core.layout.UIViewPart;
import xap.lui.core.logger.LuiLogger;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.model.LuiPageContext;
import xap.lui.core.util.JsURLEncoder;
/**
 * @author renxh UIMeta渲染器
 * 
 */
@SuppressWarnings("unchecked")
public class PCUIMetaPanelRender extends UILayoutPanelRender<UIPartMeta, LuiElement> {
	public PCUIMetaPanelRender(UIPartMeta uiEle) {
		super(uiEle);
		this.id = uiEle.getId();
		this.viewId = uiEle.getViewId() == null ? "" : (String) uiEle.getViewId();
		if (this.viewId == null || this.viewId.equals("")) {
			if (this.id == null)
				this.id = "_win_um";
			this.divId = DIV_PRE + this.id;
		} else {
			if (this.id == null)
				this.id = this.viewId + "_um";
			this.divId = DIV_PRE + this.id;
		}
	}
	@Override
	protected String mockDivId() {
		return DIV_PRE + this.id;
	}
	@Override
	protected String mockId() {
		UIElement uiEle = getUiElement();
		String widget = uiEle.getViewId() == null ? "" : (String) uiEle.getViewId();
		String id = uiEle.getId();
		if (id == null) {
			if (widget == null || widget.equals("")) {
				id = "_win_um";
			} else {
				id = widget + "_um";
			}
		}
		return id;
	}
	/*
	 * (non-Javadoc)
	 */
	public String create() {
		StringBuffer buf = new StringBuffer("");
		buf.append(this.createDesignHead());
		buf.append(this.createHead());
		buf.append(this.createTail());
		buf.append(this.createDesignTail());
		return buf.toString();
	}
	/*
	 * (non-Javadoc)
	 */
	public String createHead() {
		StringBuffer buf = new StringBuffer("");
		buf.append("addLayoutMonitor($('#").append(getNewDivId()).append("')[0]);\n");
		UIPartMeta wuimeta = this.getUiElement();
		// if(wuimeta.getElement() == null)
		// return buf.toString();
		ILuiRender render = null;
		if (wuimeta.getElement() != null)
			render = wuimeta.getElement().getRender();
		// else
		// render = wuimeta.getRender();
		if (render != null)
			buf.append(render.create());
		Map<String, UIViewPart> dialogMap = wuimeta.getDialogMap(); // 处理dialog
		if (dialogMap != null) {
			Iterator<String> keys = dialogMap.keySet().iterator();
			while (keys.hasNext()) {
				String key = keys.next();
				ILuiRender dialogRender = dialogMap.get(key).getRender();
				buf.append(dialogRender.create());
			}
		}
		return buf.toString();
	}
	@Override
	public String placeSelf() {
		StringBuffer buf = new StringBuffer();
		String newDivId = getNewDivId();
		buf.append("var ").append(newDivId).append("=$('<div>').attr('id','").append(newDivId).append("').css({\n");
		buf.append("'width':'100%',\n");
		buf.append("'height':'100%',\n");
		buf.append("'top':'0px',\n");
		buf.append("'left':'0px',\n");
		buf.append("'position':'relative'});\n");
		if (this.isEditMode()) {
			buf.append(this.placeDesign());
			buf.append(getNewDivId()).append(".append(" + getDivId() + ");\n");
		}
		buf.append(newDivId).append(".appendTo('body');");
		return buf.toString();
	}
	public String createTail() {
		return "";
	}
	@Override
	public String placeDesign() {
		if (isEditMode()) {
			StringBuffer buf = new StringBuffer("");
			buf.append("var ").append(getDivId()).append(" = $('<div>').attr('id','").append(getDivId()).append("').css({\n");
			buf.append("'height':'100%',\n");
			buf.append("'margin':'0px',\n");
			buf.append("'padding':'0px'});\n");
			return buf.toString();
		}
		return "";
	}
	@Override
	public String createDesignTail() {
		if (isEditMode()) {
			String widgetId = this.getViewId() == null ? "" : this.getViewId();
			String uiid = this.getUiElement() == null ? "" : (String) this.getUiElement().getId();
			String eleid = "";
			String type = this.getRenderType(this.getWebElement());
			if (type == null)
				type = "";
			StringBuffer buf = new StringBuffer();
			if (getDivId() == null) {
				LuiLogger.error("div id is null!" + this.getClass().getName());
			} else {
				boolean isWinEditmode = LuiRuntimeContext.isWindowEditorMode();
				boolean isWidget = this.viewId != null && !"".equals(this.viewId);
				String oriWidgetId = LuiRuntimeContext.getWebContext().getPageWebSession().getOriginalParameter("viewId");
				if (!StringUtils.isEmpty(oriWidgetId) && widgetId.isEmpty()) {
					return "";
				}
				if ((!(isWinEditmode && isWidget)) || isGenEditableTail()) {
					buf.append(this.addEditableListener(getDivId(), widgetId, uiid, null, eleid, null, type));
				}
			}
			return buf.toString();
		}
		return "";
	}
	public Map<String, Object> initRenderMap(Map<String, Object> params, String nodeId) {
		{
			String themeId = LuiRuntimeContext.getThemeId();
			String appPath = LuiRuntimeContext.getRootPath();
			String nodePath = "/lui/nodes/" + nodeId;
			String nodeThemePath = nodePath + "/themes/" + themeId;
			String nodeStyleSheetPath = nodeThemePath + "/stylesheets";
			String nodeImagePath = nodeThemePath + "/images";
			String frameGlobalPath = appPath + "/platform/script";
			String themePath = appPath + "/platform/theme/" + themeId;
			params.put(WebConstant.NODE_PATH, nodePath);
			params.put(WebConstant.ROOT_PATH, appPath);
			params.put(WebConstant.NODE_THEME_PATH, nodeThemePath);
			params.put(WebConstant.NODE_STYLE_PATH, nodeStyleSheetPath);
			params.put(WebConstant.NODE_IMAGE_PATH, nodeImagePath);
			params.put(WebConstant.NODE_ID, nodeId);
			params.put(WebConstant.FRAME_GLOBAL_PATH, frameGlobalPath);
			params.put(WebConstant.THEME_PATH, themePath);
			params.put(LuiRuntimeContext.MODEPHASE, ModePhase.normal);
		}
		{
			File includeCssFile = ContextResourceUtil.getCntWebAppFile("lui/nodes/" + nodeId + "/" + nodeId + "." + viewId + ".css");
			if (includeCssFile != null && includeCssFile.exists()) {
				params.put("includecss", includeCssFile.getName());
			}
			File includeJsFile = ContextResourceUtil.getCntWebAppFile("lui/nodes/" + nodeId + "/" + nodeId + "." + viewId + ".js");
			if (includeJsFile != null && includeJsFile.exists()) {
				params.put("includejs", includeJsFile.getName());
			}
		}
		return params;
	}
	@LuiPhase(phase = { LifeCyclePhase.ajax })
	public void addChild(UIElement obj) {
		StringBuilder dsBuf = (StringBuilder) this.getContextAttribute(UIRender.DS_SCRIPT);
		if (dsBuf == null) {
			dsBuf = new StringBuilder();
			this.setContextAttribute(UIRender.DS_SCRIPT, dsBuf);
		}
		String divId = this.getDivId();
		ILuiRender render = null;
		if (obj instanceof UIDialog) {
			UIDialog dialog = (UIDialog) obj;
			LuiRenderContext.current().setUiPartMeta(dialog.getUimeta());
			render = dialog.getRender();
		} else {
			render = obj.getRender();
		}
		StringBuffer buf = new StringBuffer();
		String html = render.place();
		buf.append(html);
		buf.append("var tmpdiv = ").append("$('#" + divId + "');\n");
		buf.append("if(tmpdiv.size()==0) \n tmpdiv = $('body');\n");
		buf.append("tmpdiv.append(" + render.getNewDivId() + ");\n");
		buf.append(render.create());
		if (obj instanceof UIDialog) {
			String width = null;
			String height = null;
			String title = null;
			UIDialog dialog = (UIDialog) obj;
			width = (Integer.parseInt(dialog.getWidth())) + "";
			height = (Integer.parseInt(dialog.getHeight()) + 40) + "";
			title = dialog.getTitle();
			viewId = dialog.getId();
			buf.append("window.widgetId = '").append(viewId).append("';\n");
			buf.append("window.isPopView = true;\n");
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("hahahaha", buf.toString());
			String nodeId = LuiRenderContext.current().getPagePartMeta().getId();
			String template = null;
			boolean isCached = false;
			if (PageControlHandler.isGetCache(nodeId)) {
				String cacheKey = nodeId + "_" + viewId;
				LUICache cache = CacheMgr.getStrongCache(WebConstant.LUI_CORE_CACHE, "design");
				template = (String) cache.get(cacheKey);
			}
			if (StringUtils.isBlank(template)) {
				isCached = false;
				this.initRenderMap(params, nodeId);
				template = FreeMarkerUtil.processCreateHtmlName("luiview", params);
			} else {
				isCached = true;
			}
			if (!isCached && PageControlHandler.isAddCache(nodeId)) {
				viewId = dialog.getId();
				String cacheKey = nodeId + "_" + viewId;
				LUICache cache = CacheMgr.getStrongCache(WebConstant.LUI_CORE_CACHE, "design");
				cache.put(cacheKey, template);
			}
			StringBuffer dialogBuf = new StringBuffer();
			Boolean isPopClose = dialog.getPopClose().equals(UIConstant.FALSE);
			Boolean isShowLine = dialog.getButtonZone().equals(UIConstant.TRUE);
			dialogBuf.append("var dialogiframe = $.pageutils.showDialog('', '" + title + "','" + width + "', '" + height + "', '" + dialog.getId() + "', '', {isConfirmClose:" + isPopClose + ",isShowLine:" + isShowLine + "});\n");
			dialogBuf.append("var iframe = dialogiframe[1];\n");
			dialogBuf.append("$.pageutils.lazyRender.iframe = iframe;\n");
			// java.util.logging.Logger.getAnonymousLogger().info(template.toString());
			dialogBuf.append("$.pageutils.lazyRender.templateStr = decodeURIComponent('" + JsURLEncoder.encode(template.toString(), "UTF-8") + "');\n");
			dialogBuf.append("$.pageutils.lazyRender();\n");
			// java.util.logging.Logger.getAnonymousLogger().info(dialogBuf.toString());
			addDynamicScript(wrapByRequired("modaldialog", dialogBuf.toString()));
		} else
			addDynamicScript(buf.toString());
	}
	@Override
	protected String getSourceType(IEventSupport ele) {
		return LuiPageContext.SOURCE_TYPE_UIMETA;
	}
	@Override
	@LuiPhase(phase = { LifeCyclePhase.ajax })
	public void destroy() {
		// TODO Auto-generated method stub
	}
}
