package xap.lui.core.model;
import java.io.Serializable;

import xap.lui.core.comps.ContextMenuComp;
import xap.lui.core.comps.MenubarComp;
import xap.lui.core.comps.WebComp;
import xap.lui.core.dataset.Dataset;

public class LuiAppUtil {
	public static Serializable getAppAttr(String key) {
		Serializable obj = (Serializable) LuiAppUtil.getCntAppCtx().getAppAttribute(key);
		return (Serializable) obj;
	}
	public static void addAppAttr(String key, Serializable value) {
		LuiAppUtil.getCntAppCtx().addAppAttribute(key, value);
	}
	public static AppContext getCntAppCtx() {
		return AppSession.current().getAppContext();
	}
	public static Application getCntApplication() {
		return LuiAppUtil.getCntAppCtx().getApplication();
	}
	public static WindowContext getCntWindowCtx() {
		return LuiAppUtil.getCntAppCtx().getCurrentWindowContext();
	}
	public static PagePartMeta getCntWindow() {
		return LuiAppUtil.getCntWindowCtx().getPagePartMeta();
	}
	/**
	 * 获取当前viewContext
	 * @return
	 */
	public static ViewPartContext getCntViewCtx() {
		return LuiAppUtil.getCntWindowCtx().getCurrentViewContext();
	}
	/**
	 * 根据ViewPartId获取viewContext
	 * @param viewPartId
	 * @return
	 */
	public static ViewPartContext getViewCtx(String viewPartId) {
		return LuiAppUtil.getCntWindowCtx().getViewContext(viewPartId);
	}
	/**
	 * 获取当前View
	 * @return
	 */
	public static ViewPartMeta getCntView() {
		return LuiAppUtil.getCntViewCtx().getView();
	}
	
	/**
	 * 根据viewPartId获取View
	 * @param viewPartId
	 * @return
	 */
	public static ViewPartMeta getView(String viewPartId) {
		return LuiAppUtil.getViewCtx(viewPartId).getView();
	}
	
	public static ViewPartMeta getWidget(String name) {
		return  LuiAppUtil.getCntWindow().getWidget(name);
	}
	
	/**
	 * 根据ID获取组件
	 * @param controlId
	 * @return
	 */
	public static WebComp getControl(String controlId) {
		return LuiAppUtil.getCntView().getViewComponents().getComponent(controlId);
	}
	
	/**
	 * 根据controlId,ViewPartId获取组件
	 * @param controlId
	 * @param viewPartId
	 * @return
	 */
	public static WebComp getControl(String controlId, String viewPartId) {
		return LuiAppUtil.getView(viewPartId).getViewComponents().getComponent(controlId);
	}
	
	/**
	 * 获取所有组件
	 * @return
	 */
	public static WebComp[] getControls() {
		return LuiAppUtil.getCntView().getViewComponents().getComps();
	}
	
	/**
	 * 根据ViewPartId获取所有组件
	 * @param viewPartId
	 * @return
	 */
	public static WebComp[] getControls(String viewPartId) {
		return LuiAppUtil.getView(viewPartId).getViewComponents().getComps();
	}
	
	/**
	 * 根据ID获取Dataset
	 * @param datasetId
	 * @return
	 */
	public static Dataset getDataset(String datasetId) {
		return LuiAppUtil.getCntView().getViewModels().getDataset(datasetId);
	}
	
	/**
	 * 根据controlId,ViewPartId获取Dataset
	 * @param datasetId
	 * @param viewPartId
	 * @return
	 */
	public static Dataset getDataset(String datasetId, String viewPartId) {
		return LuiAppUtil.getView(viewPartId).getViewModels().getDataset(datasetId);
	}
	
	/**
	 * 获取所有Dataset
	 * @param datasetId
	 * @return
	 */
	public static Dataset[] getDatasets() {
		return LuiAppUtil.getCntView().getViewModels().getDatasets();
	}
	
	/**
	 * 根据ViewPartId获取所有Dataset
	 * @param viewPartId
	 * @return
	 */
	public static Dataset[] getDatasets(String viewPartId) {
		return LuiAppUtil.getView(viewPartId).getViewModels().getDatasets();
	}
	
	/**
	 * 根据ID获取右键菜单
	 * @return
	 */
	public static ContextMenuComp getContextMenu(String contextMenuId) {
		return LuiAppUtil.getCntView().getViewMenus().getContextMenu(contextMenuId);
	}
	
	/**
	 * 根据controlId,ViewPartId获取右键菜单
	 * @param contextMenuId
	 * @param viewPartId
	 * @return
	 */
	public static ContextMenuComp getContextMenu(String contextMenuId, String viewPartId) {
		return LuiAppUtil.getView(viewPartId).getViewMenus().getContextMenu(contextMenuId);
	}
	
	/**
	 * 获取所有右键菜单
	 * @return
	 */
	public static ContextMenuComp[] getContextMenus() {
		return LuiAppUtil.getCntView().getViewMenus().getContextMenus();
	}
	
	/**
	 * 根据ViewPartId获取所有右键菜单
	 * @param viewPartId
	 * @return
	 */
	public static ContextMenuComp[] getContextMenus(String viewPartId) {
		return LuiAppUtil.getView(viewPartId).getViewMenus().getContextMenus();
	}
	
	/**
	 * 根据ID获取MenuBar
	 * @param MenuBarId
	 * @return
	 */
	public static MenubarComp getMenuBar(String MenuBarId) {
		return LuiAppUtil.getCntView().getViewMenus().getMenuBar(MenuBarId);
	}
	
	/**
	 * 根据controlId,ViewPartId获取MenuBar
	 * @param MenuBarId
	 * @param viewPartId
	 * @return
	 */
	public static MenubarComp getMenuBar(String MenuBarId, String viewPartId) {
		return LuiAppUtil.getView(viewPartId).getViewMenus().getMenuBar(MenuBarId);
	}
	
	/**
	 * 获取所有MenuBar
	 * @param MenuBarId
	 * @return
	 */
	public static MenubarComp[] getMenuBars() {
		return LuiAppUtil.getCntView().getViewMenus().getMenuBars();
	}
	
	/**
	 * 根据ViewPartId获取所有MenuBar
	 * @param viewPartId
	 * @return
	 */
	public static MenubarComp[] getMenuBars(String viewPartId) {
		return LuiAppUtil.getView(viewPartId).getViewMenus().getMenuBars();
	}
}
