package xap.lui.psn.uistate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import xap.lui.core.cache.PaCache;
import xap.lui.core.common.InteractionUtil;
import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.comps.ContextMenuComp;
import xap.lui.core.comps.MenuItem;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.Row;
import xap.lui.core.event.DatasetEvent;
import xap.lui.core.event.MouseEvent;
import xap.lui.core.event.PageEvent;
import xap.lui.core.exception.InputItem;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.listener.EventSubmitRule;
import xap.lui.core.listener.LuiEventConf;
import xap.lui.core.listener.WidgetRule;
import xap.lui.core.model.AppSession;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.model.LuiAppUtil;
import xap.lui.core.model.PagePartMeta;
import xap.lui.core.model.RequestLifeCycleContext;
import xap.lui.core.model.UIState;
import xap.lui.core.services.IPaEditorService;
import xap.lui.core.services.PaEditorServiceImpl;

public class TopUistateCtrl {
	public static final String DEFINED = " ";

	public void onDataLoad(DatasetEvent e) {
		Dataset ds = e.getSource();
		PagePartMeta pagemeta = PaCache.getEditorPagePartMeta();
		fillStateDs(ds, pagemeta);
	}
	public static void fillStateDs(Dataset ds, PagePartMeta pagemeta) {
		ds.clear();
		if (pagemeta == null)
			return;
		Row row = ds.getEmptyRow();
		row.setValue(ds.nameToIndex("id"), DEFINED);
		row.setValue(ds.nameToIndex("name"), "UI状态");
		ds.addRow(row);
		List<UIState> uistates = pagemeta.getuIStates();
		if (uistates != null && uistates.size() > 0) {
			for (UIState uistate : uistates) {
				row = ds.getEmptyRow();
				String uistateId = uistate.getId();
				String uistateName = StringUtils.isNotBlank(uistate.getName()) ? uistate.getName() : uistate.getId();
				row.setValue(ds.nameToIndex("id"), uistateId);
				row.setValue(ds.nameToIndex("name"), uistateName);
				// row.setValue(ds.nameToIndex("type"),
				// LuiPageContext.SOURCE_TYPE_WIDGT);
				row.setValue(ds.nameToIndex("pid"), DEFINED);
				ds.addRow(row);
			}
		}
	}
	
	//ui状态数据集行选中事件
		public void onStatedsAfterRowSelect(DatasetEvent e){
			Dataset ds = e.getSource();
			ContextMenuComp conetxtMenu = (ContextMenuComp) LuiAppUtil.getContextMenu("stateTreeContextMenu");
			conetxtMenu.removeChildrenItem();
			Row row = ds.getSelectedRow();
			String treeId = (String) row.getValue(ds.nameToIndex("id"));
			MenuItem menuItem = null;
			if (DEFINED.equals(treeId)) {
				menuItem = new MenuItem();
				menuItem.setId("newUIState");
				menuItem.setText("新建");
				genStateContextMenuEvent(menuItem);
				conetxtMenu.addMenuItem(menuItem);
			} else {
				menuItem = new MenuItem();
				menuItem.setId("editUIState");
				menuItem.setText("编辑");
				genStateContextMenuEvent(menuItem);
				conetxtMenu.addMenuItem(menuItem);
				
				menuItem = new MenuItem();
				menuItem.setId("delUIState");
				menuItem.setText("删除");
				String stateid = (String) row.getValue(ds.nameToIndex("id"));
				//默认状态(编辑态和浏览态)，不能删除
				if(StringUtils.equals("editstate",stateid) || StringUtils.equals("viewstate",stateid))
					menuItem.setEnabled(false);
				genStateContextMenuEvent(menuItem);
				conetxtMenu.addMenuItem(menuItem);
			}
		}
		/**
		 * 构造UI状态右键菜单项事件
		 */
		private void genStateContextMenuEvent(MenuItem menuItem) {
			LuiEventConf event = new LuiEventConf();
			event.setEventType(MouseEvent.class.getSimpleName());
			event.setOnserver(true);
			EventSubmitRule submitRule = new EventSubmitRule();
			if(StringUtils.equals(menuItem.getId(), "newUIState")){
				WidgetRule widgetRuled = new WidgetRule();
				widgetRuled.setId("data");
				submitRule.addWidgetRule(widgetRuled);
			}else{
				WidgetRule widgetRuled = new WidgetRule();
				widgetRuled.setId("data");
				submitRule.addWidgetRule(widgetRuled);
			}
			//取消选中事件
			LuiEventConf event2 = new LuiEventConf();
			event2.setEventType(MouseEvent.class.getSimpleName());
			event2.setOnserver(true);
			EventSubmitRule submitRule2 = new EventSubmitRule();
			event2.setSubmitRule(submitRule2);
			event2.setEventName("onclick");
			event2.setMethod("viewDelClickHandler");
			event2.setControllerClazz(this.getClass().getName());
			
			event.setSubmitRule(submitRule);
			event.setEventName("onclick");
			event.setMethod("stateContextMenuClickHandler");
			event.setControllerClazz(this.getClass().getName());
			menuItem.addEventConf(event);
			menuItem.addEventConf(event2);
		}
		public void stateContextMenuClickHandler(MouseEvent<MenuItem> e){
			String menuItemId = e.getSource().getId();
			Dataset ds = LuiAppUtil.getDataset("stateds");

			IPaEditorService ipaService = new PaEditorServiceImpl();
			String sessionId = (String) PaCache.getInstance().get("eclipse_sesionId");
			String pageId = (String) PaCache.getInstance().get("_pageId");
			PagePartMeta pagemeta = ipaService.getOriPageMeta(pageId, sessionId);

			if (StringUtils.equals(menuItemId, "delUIState")) {// 右键菜单删除
				LifeCyclePhase phase = RequestLifeCycleContext.get().getPhase();
				RequestLifeCycleContext.get().setPhase(LifeCyclePhase.design);
				Row row = ds.getSelectedRow();
				String id = (String) row.getValue(ds.nameToIndex("id"));
				pagemeta.removeUIStates(id);
				ds.removeRow(row);
				// TODO:可能还要处理画布上的view删除操作
				RequestLifeCycleContext.get().setPhase(phase);
				ipaService.setOriPageMeta(pageId, sessionId, pagemeta);
			} else if(StringUtils.equals(menuItemId, "newUIState")){//新建
				String inputDialogTitle = "新建UI状态";
				InputItem inputItem = new InputItem("input1","状态名：",false) {
					private static final long serialVersionUID = -5113693721835560432L;
					@Override
					public String getInputType() {
						return this.STRING_TYPE;
					}
				};
				InteractionUtil.showInputDialog(inputDialogTitle, new InputItem[] { inputItem });

				Map<String, String> rs = InteractionUtil.getInputDialogResult();
				String stateName = rs.get("input1");
				if (StringUtils.isEmpty(stateName)) {
					throw new LuiRuntimeException("请输入状态名!");
				}
				String stateId = UUID.randomUUID().toString();
				//添加state
				{
					UIState uIstate = new UIState();
					uIstate.setId(stateId);
					uIstate.setName(stateName);
					pagemeta.addUIStates(uIstate);
				}
				Row row = ds.getEmptyRow();
				row.setValue(ds.nameToIndex("id"), stateId);
				row.setValue(ds.nameToIndex("pid"), DEFINED);
				row.setValue(ds.nameToIndex("name"), stateName);
				//row.setValue(ds.nameToIndex("type"), LuiPageContext.SOURCE_TYPE_WIDGT);
				ds.addRow(row);
			}else{  //编辑
				Row row = ds.getSelectedRow();
				String stateid = (String) row.getValue(ds.nameToIndex("id"));
				Map<String, String> paramMap = new HashMap<String, String>();
				paramMap.put("stateid", stateid);
				paramMap.put("pi", UUID.randomUUID().toString());
				AppSession.current().getAppContext().navgateTo("uistate", "编辑UI状态", "600", "400", paramMap);
			}
		}

	public void onCancelClick(MouseEvent e) {
		AppSession.current().getAppContext().closeWinDialog();
	}
}
