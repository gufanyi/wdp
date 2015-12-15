package xap.lui.psn.top;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import xap.lui.core.builder.ViewPartProvider;
import xap.lui.core.comps.ContextMenuComp;
import xap.lui.core.comps.MenuItem;
import xap.lui.core.comps.MenubarComp;
import xap.lui.core.comps.ToolBarComp;
import xap.lui.core.comps.ToolBarItem;
import xap.lui.core.event.MouseEvent;
import xap.lui.core.listener.DatasetRule;
import xap.lui.core.listener.EventSubmitRule;
import xap.lui.core.listener.LuiEventConf;
import xap.lui.core.listener.WidgetRule;
import xap.lui.core.model.PagePartMeta;
import xap.lui.core.model.ViewPartMenus;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.psn.pamgr.PaPalletDsListener;

public class TopViewProvider implements ViewPartProvider {
	@Override
	public ViewPartMeta buildWidget(PagePartMeta pm, ViewPartMeta conf, Map<String, Object> paramMap,
			String currWidgetId) {
		// 菜单
		MenubarComp menu = conf.getViewMenus().getMenuBar("menu_top");
		MenuItem ctrlItem = menu.getItem("menu_top_ctrl");
		Map<String, Object[]> map = PaPalletDsListener.getAttrForTreeNodeMap();
		genCtrlTreeCxtMenu(ctrlItem, map, "");
		// toolbar
		ToolBarComp toolbar = (ToolBarComp)conf.getViewComponents().getComponent("toptoolbar");
		genCtrlToolItem(toolbar, map, conf);
		genOtherItem(toolbar, "revokeItem", "撤销", "revoke.png", "onclickBack", null);
		genOtherItem(toolbar, "recoverItem", "恢复", "recover.png", "onclickNext", null);
		String script = "var ifram_tmp_div = document.getElementById(\"iframe_tmp\");if(ifram_tmp_div.contentWindow.dropEventHandler)ifram_tmp_div.contentWindow.dropEventHandler({type:'release'});";
		genOtherItem(toolbar, "cancelItem", "取消选中", "cancel.png", "", script );
		pm.addWidget(conf);
		return conf;
	}
	private void genCtrlTreeCxtMenu(MenuItem menuItem, Map<String, Object[]> map, String pid) {
		for (String key : map.keySet()) {
			Object[] treeNodeMap = map.get(key);
			String _id = (String) key;
			String _pid = (String) treeNodeMap[6];
			if (_pid.equals(pid)) {
				MenuItem _menuItem = new MenuItem();
				_menuItem.setId(key);
				_menuItem.setText((String) treeNodeMap[1]);
				String imgIcon = null;
				String img = (String) treeNodeMap[7];
				if (StringUtils.isNotBlank(img)) {
					imgIcon = img;
				} else {
					imgIcon = "platform/theme/${theme}/comps/tree/images/icon/" + (String) treeNodeMap[0] + ".png";
				}
				_menuItem.setImgIcon(imgIcon);
				genCtrlCtxMenuEvent(_menuItem, treeNodeMap[2] != null);
				genCtrlTreeCxtMenu(_menuItem, map, _id);
				menuItem.addMenuItem(_menuItem);
			}
		}
	}
	private void genCtrlCtxMenuEvent(MenuItem menuItem, boolean hasEvent) {
		if (hasEvent) {
			LuiEventConf event = new LuiEventConf();
			event.setEventType(MouseEvent.class.getSimpleName());
			event.setOnserver(true);
			EventSubmitRule submitRule = new EventSubmitRule();
			event.setSubmitRule(submitRule);
			event.setEventName("onclick");
			event.setMethod("ctrlMenuClickHandler");
			event.setControllerClazz(TopMainViewController.class.getName());
			menuItem.addEventConf(event);
		}
	}
	private void genOtherItem(ToolBarComp toolbar, String id, String tip, String refImg, String methodName, String script) {
		ToolBarItem item = new ToolBarItem();
		item.setId(id);
		item.setTip(tip);
		item.setRefImg(refImg);
		LuiEventConf eventConf = new LuiEventConf();
		eventConf.setEventType("MouseEvent");
		eventConf.setEventName("onclick");
		eventConf.setMethod(methodName);
		eventConf.setControllerClazz(PaPalletDsListener.class.getName());
		EventSubmitRule sr = new EventSubmitRule();
		WidgetRule wr = new WidgetRule();
		wr.setId("top");
		DatasetRule dr = new DatasetRule();
		dr.setId("dsId");
		dr.setType("ds_current_line");
		wr.addDsRule(dr);
		sr.addWidgetRule(wr);
		eventConf.setSubmitRule(sr);
		if(StringUtils.isNotBlank(script)){
			eventConf.setScript(script);
			eventConf.setOnserver(false);
		}
		item.addEventConf(eventConf);
		toolbar.addElement(item);
	}

	private void genCtrlToolItem(ToolBarComp toolbar, Map<String, Object[]> map, ViewPartMeta view) {
		for (String key : map.keySet()) {
			Object[] treeNodeMap = map.get(key);
			String _pid = (String) treeNodeMap[6];
			if (StringUtils.isNotBlank(_pid)) {
				ToolBarItem pitem = toolbar.getElementById(_pid+"_ctrl");
				genCtrlChildItem(pitem, key, treeNodeMap, _pid, view);
			} else {
				ToolBarItem item = new ToolBarItem();
				if(StringUtils.equals(key, "extend_p")){
					item.setWithSep(true);
				}
				item.setId(key + "_ctrl");
				item.setTip((String) treeNodeMap[1]);
				String imgIcon = null;
				String img = (String) treeNodeMap[7];
				if (StringUtils.isNotBlank(img)) {
					imgIcon = img;
				} else {
					imgIcon = "icon/" + (String) treeNodeMap[0] + ".png";
				}
				item.setRefImg(imgIcon);
				if(StringUtils.equals(key, "text_p"))//文本输入框
				{
					LuiEventConf event = newEvent("addStringText");
					item.addEventConf(event);
				}else{
					genCtrlToolItemEvent(item, treeNodeMap[2] != null);
				}
				toolbar.addElement(item);
			}
		}
	}

	private void genCtrlChildItem(ToolBarItem item, String key, Object[] treeNodeMap, String _pid, ViewPartMeta view) {
		ViewPartMenus viewmenus = view.getViewMenus();
		ContextMenuComp menu = viewmenus.getContextMenu(item.getContextMenu());
		if(menu == null){
			String ctxmenuId = _pid+"_ctxmenu";
			item.setContextMenu(ctxmenuId);
			menu = new ContextMenuComp(ctxmenuId);
			viewmenus.addContextMenu(menu);
		}
		MenuItem menuItem = new MenuItem();
		menuItem.setId(key);
		menuItem.setText((String) treeNodeMap[1]);
		menuItem.setTip((String) treeNodeMap[1]);
		String imgIcon = null;
		String img = (String) treeNodeMap[7];
		if (StringUtils.isNotBlank(img)) {
			imgIcon = img;
		} else {
			imgIcon = "platform/theme/${theme}/global/images/icon/12/icon/" + (String) treeNodeMap[0] + ".png";
		}
		menuItem.setImgIcon(imgIcon);
		genCtxItemEvent(menuItem, treeNodeMap[2] != null);
		menu.addMenuItem(menuItem);
	}

	private void genCtxItemEvent(MenuItem item, boolean hasEvent) {
		if (hasEvent) {
			item.addEventConf(newEvent("ctrlMenuClickHandler"));
		}
	}
	private LuiEventConf newEvent(String method) {
		LuiEventConf event = new LuiEventConf();
		event.setEventType(MouseEvent.class.getSimpleName());
		event.setOnserver(true);
		EventSubmitRule submitRule = new EventSubmitRule();
		event.setSubmitRule(submitRule);
		event.setEventName("onclick");
		event.setMethod(method);
		event.setControllerClazz(TopMainViewController.class.getName());
		return event;
	}
	private void genCtrlToolItemEvent(ToolBarItem item, boolean hasEvent) {
		if (hasEvent) {
			item.addEventConf(newEvent("addComp"));
		}
	}

}
