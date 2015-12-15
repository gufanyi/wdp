package xap.lui.psn.command;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import xap.lui.core.builder.RaSelfWindow;
import xap.lui.core.builder.Window;
import xap.lui.core.cache.PaCache;
import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.common.LuiWebContext;
import xap.lui.core.comps.IFrameComp;
import xap.lui.core.comps.MenuItem;
import xap.lui.core.comps.MenubarComp;
import xap.lui.core.comps.ToolBarComp;
import xap.lui.core.comps.ToolBarItem;
import xap.lui.core.control.ModePhase;
import xap.lui.core.control.ResourceFrom;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.layout.UIComponent;
import xap.lui.core.layout.UIDivid;
import xap.lui.core.layout.UIElement;
import xap.lui.core.layout.UIFlowvPanel;
import xap.lui.core.layout.UIGridLayout;
import xap.lui.core.layout.UIGridRowLayout;
import xap.lui.core.layout.UIGridRowPanel;
import xap.lui.core.layout.UILayout;
import xap.lui.core.layout.UILayoutPanel;
import xap.lui.core.layout.UIPartMeta;
import xap.lui.core.layout.UIViewPart;
import xap.lui.core.model.DataModels;
import xap.lui.core.model.PagePartMeta;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.psn.designer.CreateDesignModel;
import xap.lui.psn.pamgr.PaEntityDsListener;
import xap.lui.psn.pamgr.PaPalletDsListener;

public class PaEditorPageModel extends Window {

	protected void initPageMetaStruct() {
		ModePhase modePhase = LuiRuntimeContext.getModePhase();
		String url1 = null;
		ViewPartMeta editorView = this.getPageMeta().getWidget("editor");
		IFrameComp iframe_tmp = (IFrameComp) editorView.getViewComponents().getComponent("iframe_tmp");
		if (modePhase.equals(ModePhase.normal)) {
			modePhase = ModePhase.eclipse;
		}
		PaCache.getInstance().pub(PaCache.ModePhase, modePhase);
		if (modePhase.equals(ModePhase.eclipse)) {
			ViewPartMeta topView = this.getPageMeta().getWidget("top");
			MenubarComp menubar = topView.getViewMenus().getMenuBar("menu_top");
			MenuItem switchView = menubar.getItem("edit_switchview");// 切换视图
			switchView.setEnabled(false);
			return;
		}
		String sesionId = LuiRuntimeContext.getWebContext().getRequest().getSession().getId();
		String eclipse_sesionId = (String) PaCache.getInstance().get("eclipse_sesionId");
		if (StringUtils.isBlank(eclipse_sesionId)) {
			PaCache.getInstance().pub("eclipse_sesionId", sesionId);
		}
		{
			if (modePhase.equals(ModePhase.nodedef) || modePhase.equals(ModePhase.persona)) {
				LuiWebContext session = LuiRuntimeContext.getWebContext();
				String winId = session.getParameter(LuiRuntimeContext.DESIGNWINID);
				PaCache.getInstance().pub("_pageId", winId);
			}
			if (modePhase.equals(ModePhase.eclipse)) {
				LuiWebContext session = LuiRuntimeContext.getWebContext();
				String winId = session.getParameter(LuiRuntimeContext.DESIGNWINID);
				PaCache.getInstance().pub("_pageId", winId);
			}
		}
		{
			UIPartMeta um = (UIPartMeta) this.getUIMeta();
			UIFlowvPanel panel = (UIFlowvPanel) getUIElement(um, "editorflowvp22222");
			UIDivid splite11 = (UIDivid) getUIElement(um, "spliter11");
			UIDivid splite1 = (UIDivid) getUIElement(um, "spliter1");
			panel.removeElement(splite11);
			panel.setElement(splite1);
			ViewPartMeta topView = this.getPageMeta().getWidget("top");
			ToolBarComp topToolBar = (ToolBarComp) topView.getViewComponents().getComponent("toptoolbar");
			ToolBarItem workspaceItem = topToolBar.getElementById("workspace");
			workspaceItem.setVisible(false);
		}
		if (modePhase.equals(ModePhase.nodedef)) {
			url1 = builderNodedefUrl();
		}
		if (modePhase.equals(ModePhase.persona)) {
			url1 = builderPersonaUrl();
		}
		java.util.logging.Logger.getAnonymousLogger().info(url1);
		iframe_tmp.setSrc(url1);
	}

	private String builderNodedefUrl() {
		String url1 = null;
		LuiWebContext session = LuiRuntimeContext.getWebContext();
		String winId = session.getParameter(LuiRuntimeContext.DESIGNWINID);
		String version = LuiRuntimeContext.getVesion();
		if (StringUtils.isBlank(version)) {
			throw new LuiRuntimeException("请提供要编辑的节点版本");
		}
		PaCache.getInstance().pub(PaCache.NodeVersion, version);
		PagePartMeta pagePartMeta = CreateDesignModel.createDesignPageMeta(winId);
		CreateDesignModel.createDesignUIMeta(pagePartMeta, winId, null);
		LuiWebContext web_ctx = LuiRuntimeContext.getWebContext();
		PagePartMeta design_page = web_ctx.getPageMeta();
		{
			DataModels dataModels = design_page.getWidget("data").getViewModels();
			Dataset ctrlDs = dataModels.getDataset("ctrlds");
			PaPalletDsListener.fillCtrlDs(ctrlDs, pagePartMeta, null);
			Dataset layOutDs = dataModels.getDataset("layoutds");
			PaPalletDsListener.fillLayoutDs(layOutDs);
			Dataset entityDs = dataModels.getDataset("entityds");
			PaEntityDsListener.setModelData(entityDs, null);
		}
		String otherPageId = LuiRuntimeContext.getWebContext().getPageUniqueId();
		url1 = "/portal/app/mockapp/" + winId;
		url1 += "?ModePhase=" + ModePhase.eclipse.toString();
		url1 += "&emode=1&model=" + RaSelfWindow.class.getName() + "&otherPageUniqueId=" + otherPageId;
		return url1;
	}

	private String builderPersonaUrl() {
		String url1 = null;
		LuiWebContext session = LuiRuntimeContext.getWebContext();
		String appId = session.getParameter("appId");
		String winId = session.getParameter(LuiRuntimeContext.DESIGNWINID);
		if (StringUtils.isBlank(winId)) {
			return url1;
		}
		session.setAttribute("_pageId", winId);
		if(StringUtils.isBlank(appId)){
			appId="mockapp";
		}
		
		PaCache.getInstance().pub("_appId", appId);
		
		String personCode = LuiRuntimeContext.getPersonaCode();
		if (StringUtils.isBlank(personCode)) {
			throw new LuiRuntimeException("请提供要个性化的编码");
		}
		PaCache.getInstance().pub(PaCache.PersonaCode, personCode);
		PagePartMeta pagePartMeta = CreateDesignModel.createDesignPageMeta(winId);
		CreateDesignModel.createDesignUIMeta(pagePartMeta, winId, null);
		LuiWebContext web_ctx = LuiRuntimeContext.getWebContext();
		PagePartMeta design_page = web_ctx.getPageMeta();
		{
			DataModels dataModels = design_page.getWidget("data").getViewModels();
			Dataset ctrlDs = dataModels.getDataset("ctrlds");
			PaPalletDsListener.fillCtrlDs(ctrlDs, pagePartMeta, null);
			Dataset layOutDs = dataModels.getDataset("layoutds");
			PaPalletDsListener.fillLayoutDs(layOutDs);
			Dataset entityDs = dataModels.getDataset("entityds");
			PaEntityDsListener.setModelData(entityDs, null);
		}
		
		url1 = LuiRuntimeContext.getRootPath() + "/app/mockapp/" + winId;
		url1 = url1 + "?" + LuiRuntimeContext.MODEPHASE + "=" + ModePhase.eclipse.toString();
		url1 = url1 +"&"+LuiRuntimeContext.RESOUCEFROM+"="+ResourceFrom.persona.toString();
		url1 = url1 +"&"+LuiRuntimeContext.PersonaCode+"="+personCode;
		url1 = url1 + "&model=" + Window.class.getName();
		url1 = url1 + "&emode=1&otherPageUniqueId=" + LuiRuntimeContext.getWebContext().getPageUniqueId();
		return url1;
	}

	private UIElement getUIElement(UIPartMeta uimeta, String id) {
		UIElement ele = null;
		UIElement uiEle = uimeta.getElement();
		if (uiEle != null && uiEle instanceof UILayout) {
			ele = findUIElementById((UILayout) uiEle, id);
		}
		return ele;
	}

	/**
	 * @param layout
	 * @param id
	 * @return
	 */
	private UIElement findUIElementById(UILayout layout, String id) {
		if (layout.getId() != null && layout.getId().equals(id)) {
			return layout;
		}
		if (layout instanceof UIGridLayout) {
			List<UILayoutPanel> listRow = ((UIGridLayout) layout).getPanelList();
			if (listRow != null && listRow.size() > 0) {
				for (UILayoutPanel rowPanel : listRow) {
					UIGridRowLayout row = ((UIGridRowPanel) rowPanel).getRow();
					UIElement findEle = findUIElementById(row, id);
					if (findEle != null)
						return findEle;
				}
			}
		} else if (layout instanceof UIViewPart) {
			UIViewPart wdg = (UIViewPart) layout;
			UIElement ele1 = wdg.getUimeta().getElement();
			UIElement findEle = findUIElementById((UILayout) ele1, id);
			return findEle;
		} else {
			List<UILayoutPanel> list = layout.getPanelList();
			if (list != null && list.size() > 0) {
				for (UILayoutPanel panel : list) {
					String panelId = (String) panel.getId();
					if (panelId != null && panelId.equals(id)) {
						return panel;
					}
					UIElement uiEle = panel.getElement();
					if (uiEle instanceof UILayout) {
						UIElement find = findUIElementById((UILayout) uiEle, id);
						if (find != null) {
							return find;
						}
					}
					if (uiEle instanceof UIComponent) {
						UIComponent comp = (UIComponent) uiEle;
						if (comp.getId() != null && comp.getId().equals(id))
							return comp;
					}
				}
			}
		}
		return null;
	}
}
