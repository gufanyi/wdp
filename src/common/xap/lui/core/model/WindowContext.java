package xap.lui.core.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;

import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.common.LuiWebSession;
import xap.lui.core.control.DftAppCtrl;
import xap.lui.core.event.DialogEvent;
import xap.lui.core.layout.UIConstant;
import xap.lui.core.layout.UIDialog;
import xap.lui.core.layout.UIPartMeta;
import xap.lui.core.listener.LuiEventConf;
import xap.lui.core.parser.UIMetaParserUtil;

public class WindowContext implements IUIContext {
	private PagePartMeta pagePartMeta;
	private IUIPartMeta uiPartMeta;
	private String id;
	private List<ViewPartContext> viewCtxs;
	private ViewPartContext currentViewCtx;
	private List<String> execScriptList;
	private List<String> beforeExecScriptList;

	public WindowContext() {
	}

	@JSONField(serialize = true, name = "page")
	public PagePartMeta getPagePartMeta() {
		return pagePartMeta;
	}

	@JSONField(serialize = false)
	public IUIPartMeta getUIPartMeta() {
		return uiPartMeta;
	}

	public void setUIPartMeta(IUIPartMeta uiMeta) {
		this.uiPartMeta = uiMeta;
	}

	public void setPagePartMeta(PagePartMeta pageMeta) {
		this.pagePartMeta = pageMeta;
	}

	@JSONField(serialize = true)
	public List<String> getExecScript() {
		return execScriptList;
	}

	public int addExecScript(String execScript) {
		if (this.execScriptList == null) {
			this.execScriptList = new ArrayList<String>();
		}
		this.execScriptList.add(execScript);
		return this.execScriptList.size() - 1;
	}

	public void downloadFileInIframe(String url) {
		StringBuffer sb = new StringBuffer();
		sb.append("$.pageutils.sysDownloadFile('").append(url).append("');");
		addExecScript(sb.toString());
	}

	public void removeExecScript(int index) {
		if (this.execScriptList == null)
			return;
		this.execScriptList.remove(index);
	}

	public void removeBeforeExecScript(int index) {
		if (this.beforeExecScriptList == null)
			return;
		this.beforeExecScriptList.remove(index);
	}

	@JSONField(serialize = true)
	public List<String> getBeforeExecScript() {
		return beforeExecScriptList;
	}

	public void addBeforeExecScript(String beforeExecScript) {
		if (this.beforeExecScriptList == null) {
			this.beforeExecScriptList = new ArrayList<String>();
		}
		this.beforeExecScriptList.add(beforeExecScript);
	}

	public void reset() {
	}

	@JSONField(serialize = false)
	public ViewPartContext getCurrentViewContext() {
		if (currentViewCtx == null) {
			if (viewCtxs != null && viewCtxs.size() > 0) {
				currentViewCtx = viewCtxs.get(0);
			}
		}
		return currentViewCtx;
	}

	public void setCurrentViewContext(ViewPartContext ctx) {
		currentViewCtx = ctx;
	}

	public void addViewContext(ViewPartContext ctx) {
		if (viewCtxs == null)
			viewCtxs = new ArrayList<ViewPartContext>();
		viewCtxs.add(ctx);
	}

	public ViewPartContext getViewContext(String id) {
		if (viewCtxs == null)
			return null;
		Iterator<ViewPartContext> it = viewCtxs.iterator();
		while (it.hasNext()) {
			ViewPartContext ctx = it.next();
			if (ctx.getId().equals(id))
				return ctx;
		}
		return null;
	}

	@JSONField(serialize = true, name = "views")
	public ViewPartContext[] getViewContexts() {
		return viewCtxs == null ? null : viewCtxs.toArray(new ViewPartContext[0]);
	}

	@JSONField(deserialize = true, name = "views")
	public void setViewContexts(ViewPartContext[] viewCtx) {
		viewCtxs = Arrays.asList(viewCtx);
	}

	public void addAppAttribute(String key, Serializable value) {
		getWindowSession().setAttribute(key, value);
	}

	public Object removeAppAttribute(String key) {
		return getWindowSession().removeAttribute(key);
	}

	public Object getAppAttribute(String key) {
		return getWindowSession().getAttribute(key);
	}

	@JSONField(serialize = false)
	private LuiWebSession getWindowSession() {
		return LuiRuntimeContext.getWebContext().getPageWebSession();
	}

	public void addDialogEventHandler(String viewId, String event) {
		UIPartMeta um = (UIPartMeta) getUIPartMeta();
		UIDialog dialog = (UIDialog) um.getDialog(viewId);
		if (dialog == null)
			return;
		dialog.addEvent(event);
	}

	public void removeDialogEventHandler(String viewId, String event) {
		UIPartMeta um = (UIPartMeta) getUIPartMeta();
		UIDialog dialog = (UIDialog) um.getDialog(viewId);
		if (dialog == null)
			return;
		dialog.removeEvent(event);
	}

	public void popView(String viewId, String width, String height, String title, boolean isPopClose, boolean buttonZone) {
		LifeCyclePhase phase = RequestLifeCycleContext.get().getPhase();
		RequestLifeCycleContext.get().setPhase(LifeCyclePhase.render);
		UIPartMeta um = (UIPartMeta) getUIPartMeta();
		PagePartMeta pageMeta = getPagePartMeta();
		ViewPartMeta widget = getPagePartMeta().getCloneWidget(viewId);
		if (widget == null) {
			widget = (ViewPartMeta) getPagePartMeta().getWidget(viewId).clone();
			getPagePartMeta().addCloneWidget(widget);
		} else {
			getPagePartMeta().removeWidget(viewId);
			getPagePartMeta().addWidget((ViewPartMeta) widget.clone());
		}
		String refId = widget.getRefId();
		String folderPath = (String) pageMeta.getFoldPath();
		if (refId.startsWith("../")) {
			folderPath = "pagemeta/public/widgetpool";
		}
		if (folderPath == null || folderPath.equals(""))
			folderPath = "lui/nodes/" + pageMeta.getId();
		UIDialog dialog = createDialog(folderPath, widget, viewId, isPopClose, buttonZone);
		dialog.setWidth(width);
		dialog.setHeight(height);
		dialog.setTitle(title);
		String confId = "dyn_add_close_conf";
		if (widget.getEventConf(confId) == null) {
			LuiEventConf event = DialogEvent.getOncloseEvent();
			event.setId(confId);
			event.setMethod("onViewClosed");
			event.setAsync(false);
			event.setControllerClazz(DftAppCtrl.class.getName());
			widget.addEventConf(event);
		}
		um.setDialog(viewId, dialog);
		RequestLifeCycleContext.get().setPhase(phase);
	}

	public void popView(String viewId, String width, String height, String title) {
		this.popView(viewId, width, height, title, false, true);
	}

	public void popUpload(String callbackClassName, String callbakcMethod) {
		Map<String, Object> options = new HashMap<String, Object>();
		options.put("classname", callbackClassName);
		options.put("method", callbakcMethod);
		this.addExecScript("$.file.showFileListPanel(" + JSON.toJSONString(options) + ");");
	}

	public void popView(String viewId, String width, String height, String title, boolean popClose) {
		this.popView(viewId, width, height, title, popClose, true);
	}

	public void closeView(String viewId) {
		this.addExecScript("if (parent.$.pageutils.hideDialog) parent.$.pageutils.hideDialog('" + viewId + "');");
	}

	private UIDialog createDialog(String folderPath, ViewPartMeta widget, String viewId, boolean isPopClose, boolean buttonZone) {
		LifeCyclePhase phase = RequestLifeCycleContext.get().getPhase();
		try {
			RequestLifeCycleContext.get().setPhase(LifeCyclePhase.nullstatus);
			UIPartMeta um = (UIPartMeta) LuiRuntimeContext.getWebContext().getPageWebSession().getAttribute("$TMP_UM_" + viewId);
			if (um == null) {
				UIMetaParserUtil util = new UIMetaParserUtil();
				util.setPagemeta(getPagePartMeta());
				um = util.parseUIMeta(widget.getPagemeta().getId(), viewId);
			}
			UIDialog uiWidget = new UIDialog();
			uiWidget.setPopClose(isPopClose ? UIConstant.TRUE : UIConstant.FALSE);
			uiWidget.setId(viewId);
			uiWidget.setUimeta(um);
			uiWidget.setButtonZone(buttonZone ? UIConstant.TRUE : UIConstant.FALSE);
			return uiWidget;
		} finally {
			RequestLifeCycleContext.get().setPhase(phase);
		}
	}

	@JSONField(serialize = false)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Map<String, Object> getPlug(String key) {
		return getPlugMap().get(key);
	}

	public void addPlug(String key, Map<String, Object> value) {
		getPlugMap().put(key, value);
	}

	@JSONField(serialize = false)
	private Map<String, Map<String, Object>> getPlugMap() {
		Map<String, Map<String, Object>> map = (Map<String, Map<String, Object>>) getWindowSession().getAttribute("PLUGMAP");
		if (map == null) {
			map = new HashMap<String, Map<String, Object>>();
			getWindowSession().setAttribute("PLUGMAP", (Serializable) map);
		}
		return map;
	}
}
